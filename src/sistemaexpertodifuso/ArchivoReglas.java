package sistemaexpertodifuso;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.swing.JOptionPane;

public class ArchivoReglas {

    ArrayList<ReglaDifusa> arrayReglasDifusas = new ArrayList<ReglaDifusa>();

    private void escribeReglaDifusa(ReglaDifusa aEscribir, RandomAccessFile raf) throws IOException {
        VariableConjunto vc;
        raf.writeInt(aEscribir.llave);
        for (int i = 0; i < aEscribir.antecedentes.size(); i++) {
            vc = aEscribir.antecedentes.get(i);
            raf.writeInt(vc.llaveVariableLiguistica);
            raf.writeInt(vc.llaveConjunto);
        }
        raf.writeInt(aEscribir.consecuente.llaveVariableLiguistica);
        raf.writeInt(aEscribir.consecuente.llaveConjunto);
        raf.writeInt(-1);
    }

    private void borraReglaDifusa(int llave) {

    }

    public boolean existe(int llave) throws IOException {
        boolean existeArchivo = true, ultimoEraVacio = true, encontrado = false;
        int actual;
        RandomAccessFile lector = null;
        try {
            lector = new RandomAccessFile("reglasDifusas", "r");
        } catch (FileNotFoundException e) {
            existeArchivo = false;
        }
        if (existeArchivo) {
            while (lector.getFilePointer() != lector.length() && !encontrado) {
                actual = lector.readInt();
                if (ultimoEraVacio && actual == llave) {
                    encontrado = true;
                }
                if (actual == -1) {
                    ultimoEraVacio = true;
                } else {
                    ultimoEraVacio = false;
                }
            }
            lector.close();
            return encontrado;
        } else {
            return false;
        }
    }

    public void ArrayReglasDifusas() throws IOException {
        int actual;
        boolean existeArchivo = true;
        RandomAccessFile lector = null;
        try {
            lector = new RandomAccessFile("reglasDifusas", "r");
        } catch (FileNotFoundException w) {
            existeArchivo = false;
        }

        if (existeArchivo) {
            System.out.println("    Llaves:");
            while (lector.getFilePointer() != lector.length()) {
                actual = lector.readInt();
                System.out.print(actual - 1);
            }
        }

    }

    public void insertar(ReglaDifusa regla) throws IOException {
        boolean existe = true;
        RandomAccessFile lector = null, escritorIndice, escritor;
        try {
            lector = new RandomAccessFile("reglasDifusas", "r");
        } catch (FileNotFoundException e) {
            existe = false;
        }
        if (existe) {
            lector.close();
            escritor = new RandomAccessFile("reglasDifusas", "rw");
            escritor.seek(escritor.length());
            escribeReglaDifusa(regla, escritor);
            escritor.close();
        } else {
            escritor = new RandomAccessFile("reglasDifusas", "rw");
            escribeReglaDifusa(regla, escritor);
            escritor.close();
        }
    }

    public String muestra_reglasDifusas() throws IOException {
        long ap_actual, ap_final;
        int elemento, elemento2;
        String salida = "";
        boolean existeArchivo = true;
        RandomAccessFile lector = null;
        try {
            lector = new RandomAccessFile("reglasDifusas", "r");
        } catch (FileNotFoundException w) {
            existeArchivo = false;
        }
        if (existeArchivo) {
            //String salida = "\nReglas difusas\n---------------------------\n";
            RandomAccessFile leer_archi = new RandomAccessFile("reglasDifusas", "r");
            while ((ap_actual = leer_archi.getFilePointer()) != (ap_final = leer_archi.length())) {
                elemento = leer_archi.readInt();
                if (elemento == -1) {
                    salida += elemento + "\n\n";
                } else {
                    salida += elemento + " ";
                }
            }//Fin while
        }else{
            JOptionPane.showMessageDialog(null, "No existe el archivo, agregue reglas difusas para crearlo");
        }
        ArrayReglasDifusas();
        return salida;
    }
}
