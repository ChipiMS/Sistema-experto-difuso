package sistemaexperto;
public class Predicado {
    String nombre;
    String predicado;
    String[][] variables;
    public Predicado(String nombre, String predicado, String[][] variables) {
        this.nombre = nombre;
        this.predicado = predicado;
        this.variables = variables;
    }
}
