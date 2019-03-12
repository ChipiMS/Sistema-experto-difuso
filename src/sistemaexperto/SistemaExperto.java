package sistemaexperto;

import java.io.FileNotFoundException;
import java.io.IOException;

public class SistemaExperto {
    public static void main(String[] args) throws IOException, FileNotFoundException, ClassNotFoundException {
        BaseDeConocimientos baseDeConocimientos = new BaseDeConocimientos();
        BaseDeHechos baseDeHechos = new BaseDeHechos();
        new GUI(baseDeConocimientos, baseDeHechos);
    }
}
