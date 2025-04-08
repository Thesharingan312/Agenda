import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Clase Agenda: gestiona las páginas de la agenda
 */
public class Agenda {
    private ArrayList<Pagina> paginas;
    private int paginaActual;
    
    public Agenda() {
        paginas = new ArrayList<>();
        paginaActual = -1;
    }
    
    // Buscar página por día y mes
    public Pagina buscarPagina(int dia, int mes) {
        for (int i = 0; i < paginas.size(); i++) {
            Pagina p = paginas.get(i);
            if (p.getDia() == dia && p.getMes() == mes) {
                paginaActual = i;
                return p;
            }
        }
        
        // Si no existe la página, la creamos
        Pagina nuevaPagina = new Pagina(dia, mes);
        paginas.add(nuevaPagina);
        ordenarPaginas();
        
        // Actualizar el índice de la página actual
        for (int i = 0; i < paginas.size(); i++) {
            if (paginas.get(i).getDia() == dia && paginas.get(i).getMes() == mes) {
                paginaActual = i;
                break;
            }
        }
        
        return nuevaPagina;
    }
    
    // Método para ordenar páginas por mes y día
    private void ordenarPaginas() {
        Collections.sort(paginas, new Comparator<Pagina>() {
            @Override
            public int compare(Pagina p1, Pagina p2) {
                if (p1.getMes() != p2.getMes()) {
                    return p1.getMes() - p2.getMes();
                } else {
                    return p1.getDia() - p2.getDia();
                }
            }
        });
    }
    
    // Navegar a la página siguiente
    public Pagina paginaSiguiente() {
        if (paginas.isEmpty()) {
            return null;
        }
        
        if (paginaActual == -1) {
            paginaActual = 0;
        } else if (paginaActual < paginas.size() - 1) {
            paginaActual++;
        } else {
            // Si estamos en la última página, volvemos a la primera
            paginaActual = 0;
        }
        
        return paginas.get(paginaActual);
    }
    
    // Navegar a la página anterior
    public Pagina paginaAnterior() {
        if (paginas.isEmpty()) {
            return null;
        }
        
        if (paginaActual == -1) {
            paginaActual = paginas.size() - 1;
        } else if (paginaActual > 0) {
            paginaActual--;
        } else {
            // Si estamos en la primera página, vamos a la última
            paginaActual = paginas.size() - 1;
        }
        
        return paginas.get(paginaActual);
    }
    
    // Obtener la página actual
    public Pagina getPaginaActual() {
        if (paginas.isEmpty() || paginaActual == -1) {
            return null;
        }
        return paginas.get(paginaActual);
    }
    
    // Verificar si la agenda tiene páginas
    public boolean tienesPaginas() {
        return !paginas.isEmpty();
    }
    
    // Obtener todas las páginas (inmutable)
    public List<Pagina> getPaginas() {
        return Collections.unmodifiableList(paginas);
    }
}