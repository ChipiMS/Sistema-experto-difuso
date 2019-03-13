package sistemaexpertodifuso;

import java.io.FileNotFoundException;
import java.io.IOException;

public class SistemaExperto {
    public static void main(String[] args) throws IOException, FileNotFoundException, ClassNotFoundException {
        ConjuntosDifusos conjuntosDifusos = new ConjuntosDifusos();
        new GUI(conjuntosDifusos);
    }
}
