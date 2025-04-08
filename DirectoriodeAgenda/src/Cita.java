/**
 * Clase Cita: representa una cita con título, texto, hora y minuto
 */
public class Cita {
    private String titulo;
    private String texto;
    private int hora;
    private int minuto;
    
    public Cita(String titulo, String texto, int hora, int minuto) {
        this.titulo = titulo;
        this.texto = texto;
        setHora(hora);
        setMinuto(minuto);
    }
    
    // Getters y setters con validación
    public String getTitulo() {
        return titulo;
    }
    
    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }
    
    public String getTexto() {
        return texto;
    }
    
    public void setTexto(String texto) {
        this.texto = texto;
    }
    
    public int getHora() {
        return hora;
    }
    
    public void setHora(int hora) {
        if (hora >= 0 && hora <= 23) {
            this.hora = hora;
        } else {
            throw new IllegalArgumentException("La hora debe estar entre 0 y 23");
        }
    }
    
    public int getMinuto() {
        return minuto;
    }
    
    public void setMinuto(int minuto) {
        if (minuto >= 0 && minuto < 60) {
            this.minuto = minuto;
        } else {
            throw new IllegalArgumentException("Los minutos deben estar entre 0 y 59");
        }
    }
    
    @Override
    public String toString() {
        return String.format("%02d:%02d - %s: %s", hora, minuto, titulo, texto);
    }
    
    // Método para obtener la hora en formato de visualización
    public String getHoraFormateada() {
        return String.format("%02d:%02d", hora, minuto);
    }
}