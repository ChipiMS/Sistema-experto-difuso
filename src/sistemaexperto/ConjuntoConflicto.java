package sistemaexperto;
import java.util.ArrayList;
public class ConjuntoConflicto {
    ArrayList<ElementoCC> elementos;
    public ConjuntoConflicto(){
        elementos = new ArrayList<ElementoCC>();
    }
    void agregarElemento(ElementoCC elemento){
        elementos.add(elemento);
    }
    void eliminarElemento(ElementoCC elemento){
        for(int i = 0; i < elementos.size(); i++){
            if(elementos.get(i).clausula == elemento.clausula){
                elementos.remove(i);
            }
        }
    }
    boolean estaVacio(){
        return elementos.isEmpty();
    }
}
