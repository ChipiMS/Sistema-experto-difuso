package sistemaexpertodifuso;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

public class ArchivoReglas {
    private void escribeReglaDifusa(ReglaDifusa aEscribir, RandomAccessFile raf) throws IOException{
        VariableConjunto vc;
        raf.writeInt(aEscribir.llave);
        for(int i = 0; i < aEscribir.antecedentes.size(); i++){
            vc = aEscribir.antecedentes.get(i);
            raf.writeInt(vc.llaveVariableLiguistica);
            raf.writeInt(vc.llaveConjunto);
        }
        raf.writeInt(aEscribir.consecuente.llaveVariableLiguistica);
        raf.writeInt(aEscribir.consecuente.llaveConjunto);
        raf.writeInt(-1);
    }
    public boolean existe(int llave) throws IOException{
        boolean existeArchivo = true, ultimoEraVacio = true, encontrado = false;
        int actual;
        RandomAccessFile lector = null;
        try{
            lector = new RandomAccessFile("reglasDifusas", "r");
        }
        catch(FileNotFoundException e){
            existeArchivo = false;
        }
        if(existeArchivo){
            while(lector.getFilePointer() != lector.length() && !encontrado){
                actual = lector.readInt();
                if(ultimoEraVacio && actual == llave){
                    encontrado = true;
                }
                if(actual == -1){
                    ultimoEraVacio = true;
                }
                else{
                    ultimoEraVacio = false;
                }
            }
            lector.close();
            return encontrado;
        }
        else{
            return false;
        }
    }
    public void insertar(ReglaDifusa regla) throws IOException{
        boolean existe = true;
        RandomAccessFile lector = null, escritorIndice, escritor;
        try{
            lector = new RandomAccessFile("reglasDifusas", "r");
        }
        catch(FileNotFoundException e){
            existe = false;
        }
        if(existe){
            lector.close();
            escritor = new RandomAccessFile("reglasDifusas", "rw");
            escritor.seek(escritor.length());
            escribeReglaDifusa(regla, escritor);
            escritor.close();
        }
        else{
            escritor = new RandomAccessFile("reglasDifusas", "rw");
            escribeReglaDifusa(regla, escritor);
            escritor.close();
        }
    }
}
