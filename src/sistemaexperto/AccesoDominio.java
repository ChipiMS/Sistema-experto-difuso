package sistemaexperto;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Scanner;
import java.util.regex.Pattern;
import javax.swing.JOptionPane;

public class AccesoDominio {

    String Arch = "Dominio";
    Dominio dominio;
    String Linea;
    boolean existe = false;
    int cont;
    String dom;
    Scanner sc = new Scanner(System.in);

    public void guardar() {

        try {
            dominio = new Dominio();
            boolean existe = true;
            RandomAccessFile lector = null, escritorIndice, escritor;
            dom = JOptionPane.showInputDialog("NUEVO DOMINIO:");
          

            String separa = Pattern.quote(":");
            String[] nombre = dom.split(separa);
            dominio.dominio = nombre[0];
            dominio.variables = nombre[1];

            String separa1 = Pattern.quote("{");
            String[] nombre1 = dominio.variables.split(separa1);
            String separa2 = Pattern.quote("}");
            String[] variables = nombre1[1].split(separa2);

            dominio.variables = variables[0];

            BufferedWriter bw;
            bw = new BufferedWriter(new FileWriter(Arch, true));
            bw.write(dominio.dominio);
            bw.newLine();
            bw.write(dominio.variables);
            bw.newLine();
            bw.close();

        } catch (IOException ex) {
            System.out.println("No se pudieron guardar los datos\n"
                    + ex.getMessage());
        }
    }

    public String mostrar() throws IOException {
        String Imprime = "";
        cont = 0;
        int sig = 0;
        BufferedReader bam;
        bam = new BufferedReader(new FileReader("Dominio"));
        Imprime = Imprime + "DOMINIOS\n";
     
        Linea = bam.readLine();
        do {

            if (sig == 0) {
                cont = cont + 1;
                sig = 1;
                Imprime = Imprime + (cont + ".- " + Linea);
               
                Linea = bam.readLine();
            } else {
                sig = 0;
                Imprime = Imprime + (":{" + Linea + "}\n");
                Linea = bam.readLine();
            }
        } while (Linea != null);
        bam.close();
        return Imprime;
    }

    public Dominio buscar(Dominio dom) throws FileNotFoundException, IOException {

        existe = false;
        cont = 0;

        BufferedReader lin;
        lin = new BufferedReader(new FileReader("Dominio"));
        Linea = lin.readLine();
        do {
            if (Linea == null) {
                cont = 0;
            } else {
                cont = cont + 1;
            }
            Linea = lin.readLine();
        } while (Linea != null);
        lin.close();

        BufferedReader bra = new BufferedReader(new FileReader("Dominio"));
        Linea = bra.readLine();
        if (cont != 0) {
            do {

                if (Linea.equals(dominio.dominio)) {
                    dom.dominio = Linea;
                    Linea = bra.readLine();
                    dom.variables = Linea;
                }
                Linea = bra.readLine();

            } while (Linea != null && existe == false);
            bra.close();
        }
        cont = 0;
        return dom;
    }

    public String eliminar() throws FileNotFoundException, IOException {
        dominio = new Dominio();
        boolean existe = false;
        dominio.dominio = JOptionPane.showInputDialog(mostrar()+"\nQUE DOMINIO QUIERES ELIMINAR?: ");
        dominio = buscar(dominio);
        if (dominio.variables == null) {
            cont = 0;
            JOptionPane.showMessageDialog(null, "NO EXISTE");

        } else {
            cont = 0;
            BufferedReader li2;
            li2 = new BufferedReader(new FileReader("Dominio"));
            Linea = li2.readLine();
            do {
                if (Linea.equals("") || Linea == null) {
                    cont = 0;
                } else {
                    cont = cont + 1;
                }
                Linea = li2.readLine();
            } while (Linea != null);
            li2.close();

            if (cont != 0) {
                BufferedReader brg;
                brg = new BufferedReader(new FileReader("Dominio"));

                Linea = brg.readLine();
                do {

                    if (!Linea.equals(dominio.dominio) || cont == 2) {
                        if (cont == 2) {

                            BufferedWriter bw;
                            bw = new BufferedWriter(new FileWriter("Temporal", true));
                            bw.close();
                            Linea = brg.readLine();
                        } else {

                            BufferedWriter bw;
                            bw = new BufferedWriter(new FileWriter("Temporal", true));
                            bw.write(Linea);
                            bw.newLine();
                            Linea = brg.readLine();
                            bw.write(Linea);
                            bw.newLine();
                            bw.close();
                        }
                    } else {
                        Linea = brg.readLine();
                    }
                    Linea = brg.readLine();
                } while (Linea != null);
                brg.close();
                File fichero = new File("Dominio");
                fichero.delete();
                File f1 = new File("Temporal");
                File f2 = new File("Dominio");
                f1.renameTo(f2);
            }
            System.out.print("\nSE HA ELIMINADO: " + dominio.dominio);
        }
        return dominio.dominio;
    }

    public String Editar() throws FileNotFoundException, IOException {
        dominio = new Dominio();
        dominio.dominio = JOptionPane.showInputDialog(mostrar()+"QUE DOMINIO QUIERES EDITAR?: ");


        dominio = buscar(dominio);
        if (dominio.variables == null) {
            cont = 0;
            JOptionPane.showMessageDialog(null, "NO EXISTE");

        } else {
            cont = 0;
            dominio.variables = JOptionPane.showInputDialog("DOMINIO A EDITAR: " + dominio.dominio + ":{" + dominio.variables + "}"
                    + "" + dominio.dominio + ":{");
            
            String separaD = Pattern.quote("}");
            String[] dom = dominio.variables.split(separaD);
            dominio.variables = dom[0];

            BufferedReader li2;
            li2 = new BufferedReader(new FileReader("Dominio"));
            Linea = li2.readLine();
            do {
                if (Linea.equals("") || Linea == null) {
                    cont = 0;
                } else {
                    cont = cont + 1;
                }
                Linea = li2.readLine();
            } while (Linea != null);
            li2.close();

            if (cont != 0) {
                BufferedReader brg;
                brg = new BufferedReader(new FileReader("Dominio"));

                Linea = brg.readLine();
                do {

                    if (!Linea.equals(dominio.dominio) || cont == 2) {
                        if (cont == 2) {

                            BufferedWriter bw;
                            bw = new BufferedWriter(new FileWriter("Temporal", true));
                            bw.write(dominio.dominio);
                            bw.newLine();
                            bw.write(dominio.variables);
                            bw.newLine();
                            bw.close();
                            Linea = brg.readLine();
                        } else {
                            BufferedWriter bw;
                            bw = new BufferedWriter(new FileWriter("Temporal", true));
                            bw.write(Linea);
                            bw.newLine();
                            Linea = brg.readLine();
                            bw.write(Linea);
                            bw.newLine();
                            bw.close();
                        }
                    } else {
                        BufferedWriter bw;
                        bw = new BufferedWriter(new FileWriter("Temporal", true));
                        bw.write(dominio.dominio);
                        bw.newLine();
                        bw.write(dominio.variables);
                        bw.newLine();
                        bw.close();
                        Linea = brg.readLine();
                    }
                    Linea = brg.readLine();
                } while (Linea != null);
                brg.close();
                File fichero = new File("Dominio");
                fichero.delete();
                File f1 = new File("Temporal");
                File f2 = new File("Dominio");
                f1.renameTo(f2);
            }
            System.out.print("\nSE HA MODIFICADO EL DOMINIO " + dominio.dominio);
        }

        return dominio.dominio;
    }

}
