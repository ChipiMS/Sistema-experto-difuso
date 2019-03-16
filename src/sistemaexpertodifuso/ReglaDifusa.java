package sistemaexpertodifuso;
import java.util.ArrayList;
public class ReglaDifusa {
    int llave;
    ArrayList<VariableConjunto> antecedentes;
    VariableConjunto consecuente;

    public ReglaDifusa(int llave){
        this.llave = llave;
        antecedentes = new ArrayList<>();
    }
}
