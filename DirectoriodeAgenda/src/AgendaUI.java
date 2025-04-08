import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

/**
 * Interfaz gráfica para la agenda de citas con cambio de tema.
 */
public class AgendaUI extends JFrame {
    private Agenda agenda;
    private JLabel lblFechaActual;
    private JTable tablaCitas;
    private DefaultTableModel modeloTabla;
    private JButton btnAgregar, btnEliminar, btnBuscar;
    private JButton btnAnterior, btnSiguiente, btnIrA;

    public AgendaUI() {
        agenda = new Agenda();
        inicializarComponentes();
    }

    private void inicializarComponentes() {
        // Configuración básica de la ventana
        setTitle("Agenda de Citas");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(700, 500);
        setLocationRelativeTo(null);

        // Establecer el icono de la aplicación
        try {
            // Carga el icono desde los recursos o una ruta específica
            ImageIcon icono = new ImageIcon(getClass().getResource("/recursos/agenda_icon.png"));
            setIconImage(icono.getImage());
        } catch (Exception e) {
            System.err.println("No se pudo cargar el icono de la aplicación: " + e.getMessage());
        }

        // Panel principal con BorderLayout
        JPanel panelPrincipal = new JPanel(new BorderLayout(10, 10));
        panelPrincipal.setBorder(new EmptyBorder(10, 10, 10, 10));

        // Crear panel para el logo en la parte superior
        JPanel panelLogo = new JPanel(new FlowLayout(FlowLayout.CENTER));
        try {
            ImageIcon logoIcon = new ImageIcon(getClass().getResource("/recursos/agenda_logo.png"));
            // Redimensionar si es necesario
            Image img = logoIcon.getImage();
            Image newImg = img.getScaledInstance(150, 120, java.awt.Image.SCALE_SMOOTH);
            logoIcon = new ImageIcon(newImg);

            JLabel labelLogo = new JLabel(logoIcon);
            panelLogo.add(labelLogo);
        } catch (Exception e) {
            // Si no se puede cargar el logo, mostrar un texto como alternativa
            JLabel labelLogo = new JLabel("AGENDA DE CITAS");
            labelLogo.setFont(new Font("Arial", Font.BOLD, 18));
            labelLogo.setForeground(new Color(0, 102, 204)); // Azul oscuro
            panelLogo.add(labelLogo);
            System.err.println("No se pudo cargar el logo: " + e.getMessage());
        }

        // Panel superior para navegación
        JPanel panelNavegacion = new JPanel(new GridLayout(1, 5, 5, 0));
        btnAnterior = new JButton("◀ Anterior");
        lblFechaActual = new JLabel("Sin página seleccionada", SwingConstants.CENTER);
        lblFechaActual.setFont(new Font("Arial", Font.BOLD, 14));
        btnSiguiente = new JButton("Siguiente ▶");
        btnIrA = new JButton("Ir a fecha...");

        panelNavegacion.add(btnAnterior);
        panelNavegacion.add(new JLabel()); // Espaciador
        panelNavegacion.add(lblFechaActual);
        panelNavegacion.add(new JLabel()); // Espaciador
        panelNavegacion.add(btnSiguiente);

        JPanel panelSuperior = new JPanel(new BorderLayout());
        panelSuperior.add(panelNavegacion, BorderLayout.CENTER);
        panelSuperior.add(btnIrA, BorderLayout.EAST);

        // Agregar el combo de temas al panelSuperior en la zona WEST
        String[] opcionesTemas = {"Claro", "Oscuro", "Ultra Contraste"};
        JComboBox<String> comboTema = new JComboBox<>(opcionesTemas);
        comboTema.setSelectedIndex(0); // por defecto: claro
        comboTema.addActionListener(e -> {
            String temaSeleccionado = (String) comboTema.getSelectedItem();
            aplicarTema(temaSeleccionado);
        });
        panelSuperior.add(comboTema, BorderLayout.WEST);

        // Modificar la estructura para incluir el logo
        JPanel panelNorte = new JPanel(new BorderLayout(5, 10));
        panelNorte.add(panelLogo, BorderLayout.NORTH);
        panelNorte.add(panelSuperior, BorderLayout.CENTER);

        // Panel central con la tabla de citas
        String[] columnas = {"Hora", "Título", "Descripción"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // No permitir edición directa en la tabla
            }
        };

        tablaCitas = new JTable(modeloTabla);
        tablaCitas.getColumnModel().getColumn(0).setPreferredWidth(60);
        tablaCitas.getColumnModel().getColumn(1).setPreferredWidth(150);
        tablaCitas.getColumnModel().getColumn(2).setPreferredWidth(350);
        tablaCitas.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollTabla = new JScrollPane(tablaCitas);

        // Panel inferior para botones de acción
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        btnAgregar = new JButton("Agregar Cita");
        btnEliminar = new JButton("Eliminar Cita");
        btnBuscar = new JButton("Buscar por Hora");

        panelBotones.add(btnAgregar);
        panelBotones.add(btnEliminar);
        panelBotones.add(btnBuscar);

        // Agregar paneles al panel principal
        panelPrincipal.add(panelNorte, BorderLayout.NORTH);
        panelPrincipal.add(scrollTabla, BorderLayout.CENTER);
        panelPrincipal.add(panelBotones, BorderLayout.SOUTH);

        // Agregar panel principal a la ventana
        setContentPane(panelPrincipal);

        // Configurar acciones de los botones
        configurarEventos();

        // Inicialmente, crear una página para hoy
        java.util.Calendar cal = java.util.Calendar.getInstance();
        int hoy = cal.get(java.util.Calendar.DAY_OF_MONTH);
        int mesActual = cal.get(java.util.Calendar.MONTH) + 1; // Calendar.MONTH es 0-based
        agenda.buscarPagina(hoy, mesActual);
        actualizarVista();
    }

    private void configurarEventos() {
        // Botones de navegación
        btnAnterior.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Pagina anterior = agenda.paginaAnterior();
                if (anterior != null) {
                    actualizarVista();
                }
            }
        });

        btnSiguiente.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Pagina siguiente = agenda.paginaSiguiente();
                if (siguiente != null) {
                    actualizarVista();
                }
            }
        });

        btnIrA.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mostrarDialogoIrAFecha();
            }
        });

        // Botones de acción
        btnAgregar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mostrarDialogoAgregarCita();
            }
        });

        btnEliminar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                eliminarCitaSeleccionada();
            }
        });

        btnBuscar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mostrarDialogoBuscarCita();
            }
        });

        // Desactivar botón eliminar hasta que se seleccione una cita
        btnEliminar.setEnabled(false);
        tablaCitas.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                btnEliminar.setEnabled(tablaCitas.getSelectedRow() != -1);
            }
        });
    }

    // Actualiza la vista con la página actual
    private void actualizarVista() {
        Pagina paginaActual = agenda.getPaginaActual();
        if (paginaActual != null) {
            lblFechaActual.setText("Página: " + paginaActual.getFechaFormateada());
            actualizarTablaCitas(paginaActual.getCitas());
        } else {
            lblFechaActual.setText("Sin página seleccionada");
            limpiarTablaCitas();
        }
    }

    // Actualiza la tabla con las citas de la página actual
    private void actualizarTablaCitas(List<Cita> citas) {
        limpiarTablaCitas();
        for (Cita cita : citas) {
            Object[] fila = {
                cita.getHoraFormateada(),
                cita.getTitulo(),
                cita.getTexto()
            };
            modeloTabla.addRow(fila);
        }
    }

    // Limpia la tabla de citas
    private void limpiarTablaCitas() {
        while (modeloTabla.getRowCount() > 0) {
            modeloTabla.removeRow(0);
        }
    }

    // Muestra diálogo para ir a una fecha específica
    private void mostrarDialogoIrAFecha() {
        JPanel panel = new JPanel(new GridLayout(2, 2, 5, 5));
        SpinnerNumberModel modeloDia = new SpinnerNumberModel(1, 1, 31, 1);
        SpinnerNumberModel modeloMes = new SpinnerNumberModel(1, 1, 12, 1);
        JSpinner spinnerDia = new JSpinner(modeloDia);
        JSpinner spinnerMes = new JSpinner(modeloMes);

        panel.add(new JLabel("Día:"));
        panel.add(spinnerDia);
        panel.add(new JLabel("Mes:"));
        panel.add(spinnerMes);

        int resultado = JOptionPane.showConfirmDialog(this, panel, "Ir a fecha", 
                                                    JOptionPane.OK_CANCEL_OPTION, 
                                                    JOptionPane.PLAIN_MESSAGE);

        if (resultado == JOptionPane.OK_OPTION) {
            try {
                int dia = (Integer) spinnerDia.getValue();
                int mes = (Integer) spinnerMes.getValue();

                agenda.buscarPagina(dia, mes);
                actualizarVista();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), 
                                                "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // Muestra diálogo para agregar una cita
    private void mostrarDialogoAgregarCita() {
        Pagina paginaActual = agenda.getPaginaActual();
        if (paginaActual == null) {
            JOptionPane.showMessageDialog(this, "No hay una página seleccionada.", 
                                            "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        JPanel panel = new JPanel(new GridLayout(4, 2, 5, 5));
        JTextField txtTitulo = new JTextField();
        JTextField txtTexto = new JTextField();
        SpinnerNumberModel modeloHora = new SpinnerNumberModel(12, 0, 23, 1);
        SpinnerNumberModel modeloMinuto = new SpinnerNumberModel(0, 0, 59, 1);
        JSpinner spinnerHora = new JSpinner(modeloHora);
        JSpinner spinnerMinuto = new JSpinner(modeloMinuto);

        // Configurar formato para los spinners
        JSpinner.NumberEditor editorHora = new JSpinner.NumberEditor(spinnerHora, "00");
        spinnerHora.setEditor(editorHora);
        JSpinner.NumberEditor editorMinuto = new JSpinner.NumberEditor(spinnerMinuto, "00");
        spinnerMinuto.setEditor(editorMinuto);

        // Panel para hora y minuto
        JPanel panelHora = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelHora.add(spinnerHora);
        panelHora.add(new JLabel(":"));
        panelHora.add(spinnerMinuto);

        panel.add(new JLabel("Título:"));
        panel.add(txtTitulo);
        panel.add(new JLabel("Descripción:"));
        panel.add(txtTexto);
        panel.add(new JLabel("Hora:"));
        panel.add(panelHora);

        int resultado = JOptionPane.showConfirmDialog(this, panel, "Agregar Cita", 
                                                    JOptionPane.OK_CANCEL_OPTION, 
                                                    JOptionPane.PLAIN_MESSAGE);

        if (resultado == JOptionPane.OK_OPTION) {
            try {
                String titulo = txtTitulo.getText().trim();
                String texto = txtTexto.getText().trim();
                int hora = (Integer) spinnerHora.getValue();
                int minuto = (Integer) spinnerMinuto.getValue();

                if (titulo.isEmpty()) {
                    throw new IllegalArgumentException("El título no puede estar vacío");
                }

                Cita nuevaCita = new Cita(titulo, texto, hora, minuto);
                paginaActual.agregarCita(nuevaCita);
                actualizarVista();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), 
                                                "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // Elimina la cita seleccionada
    private void eliminarCitaSeleccionada() {
        int filaSeleccionada = tablaCitas.getSelectedRow();
        if (filaSeleccionada == -1) {
            return;
        }

        Pagina paginaActual = agenda.getPaginaActual();
        if (paginaActual != null) {
            int confirmacion = JOptionPane.showConfirmDialog(this, 
                                                            "¿Está seguro de eliminar esta cita?", 
                                                            "Confirmar eliminación", 
                                                            JOptionPane.YES_NO_OPTION, 
                                                            JOptionPane.QUESTION_MESSAGE);

            if (confirmacion == JOptionPane.YES_OPTION) {
                paginaActual.borrarCita(filaSeleccionada);
                actualizarVista();
            }
        }
    }

    // Muestra diálogo para buscar una cita por hora
    private void mostrarDialogoBuscarCita() {
        Pagina paginaActual = agenda.getPaginaActual();
        if (paginaActual == null) {
            JOptionPane.showMessageDialog(this, "No hay una página seleccionada.", 
                                            "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
        SpinnerNumberModel modeloHora = new SpinnerNumberModel(12, 0, 23, 1);
        SpinnerNumberModel modeloMinuto = new SpinnerNumberModel(0, 0, 59, 1);
        JSpinner spinnerHora = new JSpinner(modeloHora);
        JSpinner spinnerMinuto = new JSpinner(modeloMinuto);

        // Configurar formato para los spinners
        JSpinner.NumberEditor editorHora = new JSpinner.NumberEditor(spinnerHora, "00");
        spinnerHora.setEditor(editorHora);
        JSpinner.NumberEditor editorMinuto = new JSpinner.NumberEditor(spinnerMinuto, "00");
        spinnerMinuto.setEditor(editorMinuto);

        panel.add(new JLabel("Hora:"));
        panel.add(spinnerHora);
        panel.add(new JLabel(":"));
        panel.add(spinnerMinuto);

        int resultado = JOptionPane.showConfirmDialog(this, panel, "Buscar Cita", 
                                                    JOptionPane.OK_CANCEL_OPTION, 
                                                    JOptionPane.PLAIN_MESSAGE);

        if (resultado == JOptionPane.OK_OPTION) {
            try {
                int hora = (Integer) spinnerHora.getValue();
                int minuto = (Integer) spinnerMinuto.getValue();

                Cita citaEncontrada = paginaActual.buscarCitaPorHora(hora, minuto);
                if (citaEncontrada != null) {
                    // Seleccionar la cita en la tabla
                    List<Cita> citas = paginaActual.getCitas();
                    for (int i = 0; i < citas.size(); i++) {
                        if (citas.get(i) == citaEncontrada) {
                            tablaCitas.setRowSelectionInterval(i, i);
                            break;
                        }
                    }

                    JOptionPane.showMessageDialog(this, 
                                                "Cita encontrada:\n" + citaEncontrada, 
                                                "Resultado de búsqueda", 
                                                JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, 
                                                "No se encontró ninguna cita a las " + 
                                                String.format("%02d:%02d", hora, minuto), 
                                                "Resultado de búsqueda", 
                                                JOptionPane.INFORMATION_MESSAGE);
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), 
                                                "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // Método para aplicar el tema seleccionado
    private void aplicarTema(String modo) {
        Color fondo, texto, fondoBoton;

        switch (modo.toLowerCase()) {
            case "oscuro":
                fondo = new Color(45, 45, 45);
                texto = Color.WHITE;
                fondoBoton = new Color(70, 70, 70);
                break;
            case "ultra contraste":
                fondo = Color.BLACK;
                texto = Color.YELLOW;
                fondoBoton = Color.WHITE;
                break;
            case "claro":
            default:
                fondo = Color.WHITE;
                texto = Color.BLACK;
                fondoBoton = UIManager.getColor("Button.background");
                break;
        }

        // Aplicamos el fondo al contenedor principal
        getContentPane().setBackground(fondo);

        // Aplicar los colores de forma recursiva a todos los componentes
        for (Component c : getContentPane().getComponents()) {
            aplicarColoresRecursivo(c, fondo, texto, fondoBoton);
        }

        // Refrescar la vista
        repaint();
        revalidate();
    }

    // Método auxiliar recursivo para aplicar colores a cada componente
    private void aplicarColoresRecursivo(Component comp, Color fondo, Color texto, Color fondoBoton) {
        if (comp instanceof JPanel || comp instanceof JScrollPane) {
            comp.setBackground(fondo);
        }

        if (comp instanceof JLabel) {
            comp.setForeground(texto);
        }

        if (comp instanceof JButton) {
            comp.setBackground(fondoBoton);
            comp.setForeground(texto);
        }

        if (comp instanceof JTable) {
            JTable tabla = (JTable) comp;
            tabla.setBackground(fondo);
            tabla.setForeground(texto);
            tabla.setSelectionBackground(new Color(100, 100, 255));
            tabla.setSelectionForeground(Color.WHITE);
            tabla.getTableHeader().setBackground(fondoBoton);
            tabla.getTableHeader().setForeground(texto);
        }

        if (comp instanceof Container) {
            for (Component hijo : ((Container) comp).getComponents()) {
                aplicarColoresRecursivo(hijo, fondo, texto, fondoBoton);
            }
        }
    }
}
