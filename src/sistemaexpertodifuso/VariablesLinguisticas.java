package sistemaexpertodifuso;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import javax.swing.JOptionPane;

public class VariablesLinguisticas{
    private final int registerLength;
    private Arbol arbol;
    private int direccionSiguiente, direccionActual, borrados, desbordados, ordenados, direccionReorganizados;
    private Indice indice;
    VariablesLinguisticas() throws IOException, FileNotFoundException, ClassNotFoundException {
        registerLength = 1024;
        indice = new Indice();
        recuperarArbol();
        recuperarControl();
    }
    public void actualizar(VariableLinguistica clausula) throws FileNotFoundException, IOException{
        RandomAccessFile escritor;
        clausula.llave = indice.llave;
        indice.direccion = arbol.buscar(indice.llave);
        if(indice.direccion == -1){
            JOptionPane.showMessageDialog(null, "No hay una clausula con la llave "+indice.llave);
        }
        else{
            escritor = new RandomAccessFile("maestroClausula", "rw");
            escritor.seek(indice.direccion*registerLength);
            escribeClausula(clausula, escritor);
            escritor.close();
        }
    }
    public void borrar(int llave) throws FileNotFoundException, IOException, ClassNotFoundException{
        RandomAccessFile escritor;
        VariableLinguistica clausula = new VariableLinguistica();
        indice.llave = clausula.llave = llave;
        indice.direccion = arbol.buscar(indice.llave);
        if(indice.direccion == -1){
            JOptionPane.showMessageDialog(null, "No hay una clausula con la llave "+indice.llave);
        }
        else{
            clausula.llave = 0;
            clausula.predicadosNegados[0] = "******************************";
            clausula.predicado = "******************************";
            escritor = new RandomAccessFile("maestroClausula", "rw");
            escritor.seek(indice.direccion*registerLength);
            escribeClausula(clausula, escritor);
            escritor.close();
            marcarIndice(indice.llave);
            arbol.borrar(llave);
            borrados++;
            reescribirControl();
        }
    }
    private void escribeIndice(Indice aEscribir, RandomAccessFile raf) throws IOException{
        raf.writeInt(aEscribir.llave);
        raf.writeInt(aEscribir.direccion);
    }
    private void escribeClausula(VariableLinguistica aEscribir, RandomAccessFile raf) throws IOException{
        int i;
        raf.writeInt(aEscribir.llave);
        for(i = 0; i < 16; i++){
            if(aEscribir.predicadosNegados[i] == null){
                aEscribir.predicadosNegados[i] = "******************************";
            }
            raf.writeChars(aEscribir.predicadosNegados[i]);
        }
        raf.writeChars(aEscribir.predicado);
    }
    public void insertar(VariableLinguistica clausula) throws IOException{
        boolean existe = true;
        RandomAccessFile lector = null, escritorIndice, escritor;
        indice.llave = clausula.llave;
        indice.direccion = direccionSiguiente;
        if(arbol.buscar(clausula.llave) != -1){
            JOptionPane.showMessageDialog(null, "La clave ya existe");
        }
        else{
            try{
                lector = new RandomAccessFile("maestroClausula", "r");
            }
            catch(FileNotFoundException e){
                existe = false;
            }
            if(existe){
                lector.close();
                escritor = new RandomAccessFile("maestroClausula", "rw");
                escritorIndice = new RandomAccessFile("indiceClausula", "rw");
                escritor.seek(escritor.length());
                escribeClausula(clausula, escritor);
                escritorIndice.seek(escritorIndice.length());
                escribeIndice(indice, escritorIndice);
                escritor.close();
                escritorIndice.close();
                desbordados++;
            }
            else{
                escritor = new RandomAccessFile("maestroClausula", "rw");
                escritorIndice = new RandomAccessFile("indiceClausula", "rw");
                escribeClausula(clausula, escritor);
                escribeIndice(indice, escritorIndice);
                escritor.close();
                escritorIndice.close();
                ordenados++;
            }
            arbol.insertar(clausula.llave, direccionSiguiente);
            direccionSiguiente++;
            reescribirControl();
        }
    }
    private void marcarIndice(int llave) throws IOException, ClassNotFoundException{
        boolean existe = true;
        RandomAccessFile lector = null;
        long apuntadorFinal, ultimoApuntador;
        boolean marcado = false;
        try{
            lector = new RandomAccessFile("indiceClausula", "rw");
        }
        catch(FileNotFoundException e){
            existe = false;
        }
        if(existe){
            apuntadorFinal = lector.length();
            while((ultimoApuntador = lector.getFilePointer()) != apuntadorFinal && !marcado){
                indice = recuperaIndice(lector);
                if(indice.llave == llave){
                    lector.seek(ultimoApuntador);
                    indice.direccion = -1;
                    escribeIndice(indice, lector);
                }
            }
            lector.close();
        }
    }
    private void recorreArbol(Nodo nodo, RandomAccessFile raf, ArrayList<VariableLinguistica> arreglo) throws IOException{
        VariableLinguistica clausula;
        if(nodo.izquierda != null){
            recorreArbol(nodo.izquierda, raf, arreglo);
        }
        if(direccionActual != nodo.direccion){
            raf.seek(nodo.direccion*registerLength);
            direccionActual = nodo.direccion;
        }
        clausula = recuperaClausula(raf);
        arreglo.add(clausula);
        direccionActual++;
        if(nodo.derecha != null){
            recorreArbol(nodo.derecha, raf, arreglo);
        }
    }
    private void recorreArbolReestructurar(Nodo nodo, RandomAccessFile lector, RandomAccessFile escritor) throws IOException{
        VariableLinguistica clausula;
        if(nodo.izquierda != null){
            recorreArbolReestructurar(nodo.izquierda, lector, escritor);
        }
        if(direccionActual != nodo.direccion){
            lector.seek(nodo.direccion*registerLength);
            direccionActual = nodo.direccion;
        }
        clausula = recuperaClausula(lector);
        escribeClausula(clausula, escritor);
        direccionActual++;
        nodo.direccion = direccionReorganizados;
        direccionReorganizados++;
        if(nodo.derecha != null){
            recorreArbolReestructurar(nodo.derecha, lector, escritor);
        }
    }
    public VariableLinguistica recuperarAleatorio(int llave) throws FileNotFoundException, IOException{
        VariableLinguistica clausula = null;
        RandomAccessFile lector = null;
        indice.llave = llave;
        indice.direccion = arbol.buscar(indice.llave);
        if(indice.direccion == -1){
            //JOptionPane.showMessageDialog(null, "No hay una clausula con la llave "+indice.llave);
        }
        else{
            lector = new RandomAccessFile("maestroClausula", "r");
            lector.seek(indice.direccion*registerLength);
            clausula = recuperaClausula(lector);
            lector.close();
        }
        return clausula;
    }
    private void recuperarArbol() throws IOException{
        boolean existe = true;
        RandomAccessFile lector = null;
        arbol = new Arbol();
        try{
            lector = new RandomAccessFile("indiceClausula", "r");
        }
        catch(FileNotFoundException e){
            existe = false;
        }
        if(existe){
            while(lector.getFilePointer() != lector.length()){
                indice = recuperaIndice(lector);
                if(indice.direccion != -1){
                    arbol.insertar(indice.llave, indice.direccion);
                }
            }
            lector.close();
        }
    }
    private void recuperarControl() throws IOException{
        boolean existe = true;
        RandomAccessFile lector = null;
        try{
            lector = new RandomAccessFile("controlClausula", "r");
        }
        catch(FileNotFoundException e){
            existe = false;
        }
        if(existe){
            direccionSiguiente = lector.readInt();
            borrados = lector.readInt();
            desbordados = lector.readInt();
            ordenados = lector.readInt();
            lector.close();
        }
        else{
            direccionSiguiente = borrados = desbordados = ordenados = 0;
        }
    }
    private Indice recuperaIndice(RandomAccessFile raf) throws IOException{
        Indice i = new Indice();
        i.llave = raf.readInt();
        i.direccion = raf.readInt();
        return i;
    }
    private VariableLinguistica recuperaClausula(RandomAccessFile lector) throws IOException{
        int i, c;
        char premisa[] = new char[30];
        VariableLinguistica l = new VariableLinguistica();
        l.llave = lector.readInt();
        for(i = 0; i < 16; i++){
            for(c = 0; c < premisa.length; c++){
                premisa[c] = lector.readChar();
            }
            l.predicadosNegados[i] = new String(premisa).replace('\0', ' ');
        }
        for(c = 0; c < premisa.length; c++){
            premisa[c] = lector.readChar();
        }
        l.predicado = new String(premisa).replace('\0', ' ');
        return l;
    }
    public VariableLinguistica[] recuperarSecuencial() throws FileNotFoundException, IOException{
        VariableLinguistica[] clausulas;
        boolean existe = true;
        RandomAccessFile lector = null;
        direccionActual = 0;
        ArrayList<VariableLinguistica> arreglo = new ArrayList<VariableLinguistica>();
        try{
            lector = new RandomAccessFile("maestroClausula", "r");
        }
        catch(FileNotFoundException e){
            existe = false;
        }
        if(existe){
            if(arbol.raiz != null){
                recorreArbol(arbol.raiz, lector, arreglo);
            }
            lector.close();
        }
        clausulas = new VariableLinguistica[arreglo.size()];
        for(int i = 0; i < arreglo.size(); i++){
            clausulas[i] = arreglo.get(i);
        }
        return clausulas;
    }
    private void reescribirControl() throws IOException{
        boolean existe = true;
        RandomAccessFile lector = null;
        try{
            lector = new RandomAccessFile("controlClausula", "rw");
        }
        catch(FileNotFoundException e){
            existe = false;
        }
        if(existe){
            lector.writeInt(direccionSiguiente);
            lector.writeInt(borrados);
            lector.writeInt(desbordados);
            lector.writeInt(ordenados);
            lector.close();
        }
        if(0.4*ordenados < borrados+desbordados){
            reestructurar();
        }
    }
    private void reestructurar() throws IOException{
        boolean existe = true;
        RandomAccessFile lector = null, escritor;
        File file, newName;
        direccionReorganizados = direccionActual = 0;
        try{
            lector = new RandomAccessFile("maestroClausula", "r");
        }
        catch(FileNotFoundException e){
            existe = false;
        }
        if(existe){
            escritor = new RandomAccessFile("tmp", "rw");
            if(arbol.raiz != null){
                recorreArbolReestructurar(arbol.raiz, lector, escritor);
            }
            lector.close();
            escritor.close();
            file = new File("tmp");
            newName = new File("maestroClausula");
            newName.delete();
            file.renameTo(newName);
            
            lector = new RandomAccessFile("indiceClausula", "rw");
            escritor = new RandomAccessFile("tmp", "rw");
            while(lector.getFilePointer() != lector.length()){
                indice = recuperaIndice(lector);
                indice.direccion = arbol.buscar(indice.llave);
                if(indice.direccion != -1){
                    escribeIndice(indice, escritor);
                }
            }
            lector.close();
            escritor.close();
            file = new File("tmp");
            newName = new File("indiceClausula");
            newName.delete();
            file.renameTo(newName);
        }
        ordenados = ordenados+desbordados-borrados;
        direccionSiguiente = ordenados;
        desbordados = borrados = 0;
        reescribirControl();
    }
}
