import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Clase Pagina: representa una página de la agenda con día, mes y lista de citas
 */
public class Pagina {
    private int dia;
    private int mes;
    private ArrayList<Cita> citas;
    
    public Pagina(int dia, int mes) {
        setDia(dia);
        setMes(mes);
        this.citas = new ArrayList<>();
    }
    
    // Getters y setters con validación
    public int getDia() {
        return dia;
    }
    
    public void setDia(int dia) {
        if (dia >= 1 && dia <= 31) {
            this.dia = dia;
        } else {
            throw new IllegalArgumentException("El día debe estar entre 1 y 31");
        }
    }
    
    public int getMes() {
        return mes;
    }
    
    public void setMes(int mes) {
        if (mes >= 1 && mes <= 12) {
            this.mes = mes;
        } else {
            throw new IllegalArgumentException("El mes debe estar entre 1 y 12");
        }
    }
    
    // Métodos para gestionar citas
    public void agregarCita(Cita cita) {
        citas.add(cita);
        ordenarCitas();
    }
    
    // Método para ordenar citas por hora y minuto
    private void ordenarCitas() {
        Collections.sort(citas, new Comparator<Cita>() {
            @Override
            public int compare(Cita c1, Cita c2) {
                if (c1.getHora() != c2.getHora()) {
                    return c1.getHora() - c2.getHora();
                } else {
                    return c1.getMinuto() - c2.getMinuto();
                }
            }
        });
    }
    
    public boolean borrarCita(int indice) {
        if (indice >= 0 && indice < citas.size()) {
            citas.remove(indice);
            return true;
        }
        return false;
    }
    
    public boolean borrarCita(Cita cita) {
        return citas.remove(cita);
    }
    
    public Cita buscarCitaPorHora(int hora, int minuto) {
        for (Cita cita : citas) {
            if (cita.getHora() == hora && cita.getMinuto() == minuto) {
                return cita;
            }
        }
        return null;
    }
    
    public List<Cita> getCitas() {
        return Collections.unmodifiableList(citas);
    }
    
    public String getFechaFormateada() {
        return String.format("%02d/%02d", dia, mes);
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("Página: %02d/%02d\n", dia, mes));
        
        if (citas.isEmpty()) {
            sb.append("No hay citas programadas para este día.\n");
        } else {
            sb.append("Citas:\n");
            for (int i = 0; i < citas.size(); i++) {
                sb.append(i + 1).append(". ").append(citas.get(i).toString()).append("\n");
            }
        }
        
        return sb.toString();
    }
}