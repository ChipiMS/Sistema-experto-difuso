package sistemaexpertodifuso;

import java.io.FileNotFoundException;
import java.io.IOException;

public class SistemaExpertoDifuso {
    public static void main(String[] args) throws IOException, FileNotFoundException, ClassNotFoundException {
        VariablesLinguisticas variablesLinguisticas = new VariablesLinguisticas();
        ArchivoReglas archivoReglas = new ArchivoReglas();
        new GUI(variablesLinguisticas, archivoReglas);
    }
}
