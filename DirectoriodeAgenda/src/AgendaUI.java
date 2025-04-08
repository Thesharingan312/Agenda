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
    private JComboBox<String> comboTema;

    // Variables de color para los temas
    private final Color colorFondoClaro = new Color(0xF5F5DC); // Beige
    private final Color colorTextoClaro = new Color(0x333333); // Gris Oscuro
    private final Color colorAcentoClaro = new Color(0xA9A9A9); // Gris Claro
    private final Color colorFondoOscuro = new Color(0x303030); // Gris Oscuro
    private final Color colorTextoOscuro = new Color(0xF0F0F0); // Gris Claro
    private final Color colorAcentoOscuro = new Color(0x555555); // Gris Medio
    private final Color colorFondoContraste = new Color(0x000000); // Negro
    private final Color colorTextoContraste = new Color(0xFFFF00); // Amarillo
    private final Color colorAcentoContraste = new Color(0xFF4500); // Naranja Rojizo

    public AgendaUI() {
        agenda = new Agenda();
        inicializarComponentes();
        aplicarTema("Claro"); // Establecer tema inicial
    }

    private void inicializarComponentes() {
        // Configuración básica de la ventana
        setTitle("Agenda de Citas");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 520);
        setLocationRelativeTo(null);

        // Establecer el icono de la aplicación
        try {
            ImageIcon icono = new ImageIcon(getClass().getResource("/recursos/agenda_icon.png"));
            setIconImage(icono.getImage());
        } catch (Exception e) {
            System.err.println("No se pudo cargar el icono de la aplicación: " + e.getMessage());
        }

        // Panel principal con BorderLayout
        JPanel panelPrincipal = new JPanel(new BorderLayout(10, 10));
        panelPrincipal.setBorder(new EmptyBorder(10, 10, 10, 10));

        // Panel para el logo en la parte superior
        JPanel panelLogo = new JPanel(new FlowLayout(FlowLayout.CENTER));
        try {
            ImageIcon logoIcon = new ImageIcon(getClass().getResource("/recursos/agenda_logo.png"));
            Image img = logoIcon.getImage();
            Image newImg = img.getScaledInstance(150, 120, java.awt.Image.SCALE_SMOOTH);
            logoIcon = new ImageIcon(newImg);

            JLabel labelLogo = new JLabel(logoIcon);
            panelLogo.add(labelLogo);
        } catch (Exception e) {
            JLabel labelLogo = new JLabel("AGENDA DE CITAS");
            labelLogo.setFont(new Font("Arial", Font.ITALIC, 20));
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
        comboTema = new JComboBox<>(opcionesTemas);
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
                    JOptionPane.showMessageDialog(this, "Cita encontrada:\n" + citaEncontrada,
                            "Cita Encontrada", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, "No se encontró ninguna cita a esa hora.",
                            "Cita No Encontrada", JOptionPane.INFORMATION_MESSAGE);
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void aplicarTema(String tema) {
        switch (tema) {
            case "Claro":
                establecerColores(colorFondoClaro, colorTextoClaro, colorAcentoClaro);
                break;
            case "Oscuro":
                establecerColores(colorFondoOscuro, colorTextoOscuro, colorAcentoOscuro);
                break;
            case "Ultra Contraste":
                establecerColores(colorFondoContraste, colorTextoContraste, colorAcentoContraste);
                break;
        }
        // Actualizar la apariencia de la interfaz
        SwingUtilities.updateComponentTreeUI(this);
    }

    private void establecerColores(Color colorFondo, Color colorTexto, Color colorAcento) {
        // Establecer colores de fondo
        getContentPane().setBackground(colorFondo);

        // Establecer colores para la tabla
        tablaCitas.setBackground(colorFondo);
        tablaCitas.setForeground(colorTexto);
        tablaCitas.getTableHeader().setBackground(colorAcento); // Color de acento para el encabezado
        tablaCitas.getTableHeader().setForeground(colorTexto);

        // Establecer colores para los botones
        btnAgregar.setBackground(colorAcento);
        btnAgregar.setForeground(colorTexto);
        btnEliminar.setBackground(colorAcento);
        btnEliminar.setForeground(colorTexto);
        btnBuscar.setBackground(colorAcento);
        btnBuscar.setForeground(colorTexto);

        // Establecer colores para las etiquetas
        lblFechaActual.setForeground(colorTexto);

        // Establecer colores para otros componentes
        // ... (puedes añadir más componentes aquí)

        // Llamar a un método recursivo para aplicar los colores a los componentes anidados
        aplicarColorRecursivo(getContentPane(), colorFondo, colorTexto, colorAcento);
    }

    private void aplicarColorRecursivo(Component component, Color colorFondo, Color colorTexto, Color colorAcento) {
        component.setBackground(colorFondo);
        component.setForeground(colorTexto);

        if (component instanceof JPanel) {
            JPanel panel = (JPanel) component;
            for (Component child : panel.getComponents()) {
                aplicarColorRecursivo(child, colorFondo, colorTexto, colorAcento);
            }
        } else if (component instanceof JScrollPane) {
            JScrollPane scrollPane = (JScrollPane) component;
            scrollPane.getViewport().setBackground(colorFondo);
            scrollPane.getViewport().setForeground(colorTexto);
            if (scrollPane.getViewport().getView() != null) {
                aplicarColorRecursivo(scrollPane.getViewport().getView(), colorFondo, colorTexto, colorAcento);
            }
        } else if (component instanceof JTable) {
            JTable table = (JTable) component;
            table.setBackground(colorFondo);
            table.setForeground(colorTexto);
            table.setGridColor(colorTexto);
            table.getTableHeader().setBackground(colorAcento);
            table.getTableHeader().setForeground(colorTexto);
        } else if (component instanceof AbstractButton) {
            AbstractButton button = (AbstractButton) component;
            button.setBackground(colorAcento);
            button.setForeground(colorTexto);
        } else if (component instanceof JLabel) {
            JLabel label = (JLabel) component;
            label.setForeground(colorTexto);
        } else if (component instanceof JTextField) {
            JTextField textField = (JTextField) component;
            textField.setBackground(colorFondo);
            textField.setForeground(colorTexto);
        } else if (component instanceof JSpinner) {
            JSpinner spinner = (JSpinner) component;
            spinner.setBackground(colorFondo);
            spinner.setForeground(colorTexto);
            JComponent editor = spinner.getEditor();
            if (editor instanceof JSpinner.DefaultEditor) {
                ((JSpinner.DefaultEditor) editor).getTextField().setBackground(colorFondo);
                ((JSpinner.DefaultEditor) editor).getTextField().setForeground(colorTexto);
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            AgendaUI agendaUI = new AgendaUI();
            agendaUI.setVisible(true);
        });
    }
}
