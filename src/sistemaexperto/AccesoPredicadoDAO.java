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

public class AccesoPredicadoDAO {

    Dominio dominio;
    String[] Valores;
    String Arch = "Predicado", pred;
    String regresaPredicados[];
    String[][] Variables;
    String Linea;
    String Predi;
    boolean existe = false;
    String[] Linea2;
    String[] Var = new String[10];
    String partes = "";
    int cont = 0;
    DAO_Predicado predicado;

    public char charAt;

    Scanner lee = new Scanner(System.in);
    Scanner sc = new Scanner(System.in);

    public Predicado[] Predicados() throws IOException {

        int noPred = 0;
        int noVar = 0;
        BufferedReader lin;
        lin = new BufferedReader(new FileReader("Predicado"));
        Linea = lin.readLine();
        do {
            if (Linea == null) {
                noPred = 0;
            } else {
                noPred = noPred + 1;
            }
            Linea = lin.readLine();
            Linea = lin.readLine();
        } while (Linea != null);
        lin.close();

        BufferedReader lin2;
        lin2 = new BufferedReader(new FileReader("Dominio"));
        Linea = lin2.readLine();
        do {
            if (Linea == null) {
                noVar = 0;
            } else {
                noVar = noVar + 1;
            }
            Linea = lin2.readLine();
            Linea = lin2.readLine();
        } while (Linea != null);
        lin2.close();
        cont = 0;

        Predicado predicados[] = new Predicado[noPred];
        predicado = new DAO_Predicado();

        BufferedReader lin3;
        lin3 = new BufferedReader(new FileReader("Predicado"));
        Linea = lin3.readLine();
        do {
            predicado.nombre = Linea;
            String separaNom = Pattern.quote("(");
            String[] part1 = predicado.nombre.split(separaNom);

            String separaNom1 = Pattern.quote(")");
            String[] part2 = part1[1].split(separaNom1);

            String separaNom2 = Pattern.quote(",");
            String[] v = part2[0].split(separaNom2);

            Variables = new String[v.length][];

            for (int x = 0; x < v.length; x++) {

                dominio = new Dominio();
                dominio.dominio = v[x];
                BufferedReader lin4;
                lin4 = new BufferedReader(new FileReader("Dominio"));
                Linea = lin4.readLine();
                do {
                    if (Linea.equals(dominio.dominio)) {
                        Linea = lin4.readLine();
                        String separaV = Pattern.quote(",");
                        String[] var = Linea.split(separaV);
                        if (!var[0].equals(" ")&&!var[0].equals("")) {
                            String[] va1 = var;
                            Variables[x] = va1;
                        } else {
                            String[] va1 = new String[0];
                            Variables[x] = va1;
                        }

                    }
                    Linea = lin4.readLine();

                } while (Linea != null);
                lin4.close();

            }

            Linea = lin3.readLine();
            predicado.pred = Linea;

            predicados[cont] = new Predicado(predicado.pred, predicado.nombre, Variables);
            cont = cont + 1;
            Linea = lin3.readLine();
        } while (Linea != null);
        lin3.close();

        return predicados;
    }

    public void guardar() {
        try {
            predicado = new DAO_Predicado();

            boolean existe = true;
            RandomAccessFile lector = null, escritorIndice, escritor;
            pred = JOptionPane.showInputDialog("NUEVO PREDICADO:");
            String separaNom = Pattern.quote(":");
            String[] nombre = pred.split(separaNom);
            predicado.nombre = nombre[0];
            predicado.pred = nombre[1];

            BufferedWriter bw;
            bw = new BufferedWriter(new FileWriter(Arch, true));
            bw.write(predicado.nombre);
            bw.newLine();
            bw.write(predicado.pred);
            bw.newLine();
            bw.close();

        } catch (IOException ex) {
            System.out.println("No se pudieron guardar los datos\n"
                    + ex.getMessage());
        }
    }

    public String mostrar() throws IOException {
        cont = 0;
        String Imprime = "";
        int sig = 0;
        BufferedReader bam;
        bam = new BufferedReader(new FileReader("Predicado"));
        Imprime = Imprime + "PREDICADOS\n";
        Linea = bam.readLine();
        do {

            if (sig == 0) {
                cont = cont + 1;
                sig = 1;
                Imprime = Imprime + (cont + ".- " + Linea);
                Linea = bam.readLine();
            } else {
                sig = 0;
                Imprime = Imprime + (":" + Linea + "\n");
                Linea = bam.readLine();
            }
        } while (Linea != null);
        bam.close();
        return Imprime;
    }

    public DAO_Predicado buscar(DAO_Predicado pred) throws FileNotFoundException, IOException {
        existe = false;
        cont = 0;

        BufferedReader lin;
        lin = new BufferedReader(new FileReader("Predicado"));
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

        BufferedReader bra = new BufferedReader(new FileReader("Predicado"));
        Linea = bra.readLine();
        if (cont != 0) {
            do {

                if (Linea.equals(pred.nombre)) {

                    pred.nombre = Linea;
                    Linea = bra.readLine();
                    pred.pred = Linea;
                }
                Linea = bra.readLine();

            } while (Linea != null && existe == false);
            bra.close();
        }
        cont = 0;
        return pred;
    }

    public String Editar() throws FileNotFoundException, IOException {
        predicado = new DAO_Predicado();
        predicado.nombre = JOptionPane.showInputDialog(mostrar() + "\nQUE PREDICADO QUIERES EDITAR?: ");

        predicado = buscar(predicado);
        if (predicado.pred == null) {
            cont = 0;
            JOptionPane.showMessageDialog(null, "NO EXISTE");

        } else {
            cont = 0;
            Predi = JOptionPane.showInputDialog("PREDICADO EDITAR: " + predicado.nombre + ":" + predicado.pred
                    + "\nNevo:");

            String separaNom = Pattern.quote(":");
            String[] nombre = Predi.split(separaNom);
            predicado.nombre = nombre[0];
            predicado.pred = nombre[1];

            BufferedReader li2;
            li2 = new BufferedReader(new FileReader("Predicado"));
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
                brg = new BufferedReader(new FileReader("Predicado"));

                Linea = brg.readLine();
                do {

                    if (!Linea.equals(predicado.nombre) || cont == 2) {
                        if (cont == 2) {

                            BufferedWriter bw;
                            bw = new BufferedWriter(new FileWriter("Temporal", true));
                            bw.write(predicado.nombre);
                            bw.newLine();
                            bw.write(predicado.pred);
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
                        bw.write(predicado.nombre);
                        bw.newLine();
                        bw.write(predicado.pred);
                        bw.newLine();
                        bw.close();
                        Linea = brg.readLine();
                    }
                    Linea = brg.readLine();
                } while (Linea != null);
                brg.close();
                File fichero = new File("Predicado");
                fichero.delete();
                File f1 = new File("Temporal");
                File f2 = new File("Predicado");
                f1.renameTo(f2);
            }
            JOptionPane.showMessageDialog(null, "\nSE HA MODIFICADO: " + predicado.nombre);
        }

        return predicado.pred;
    }

    public String eliminar() throws FileNotFoundException, IOException {

        predicado = new DAO_Predicado();
        boolean existe = false;

        predicado.nombre = JOptionPane.showInputDialog(mostrar() + "\nQUE PREDICADO QUIERES ELIMINAR?: ");

        predicado = buscar(predicado);
        if (predicado.pred == null) {
            cont = 0;
            JOptionPane.showMessageDialog(null, "NO EXISTE");

        } else {
            cont = 0;
            BufferedReader li2;
            li2 = new BufferedReader(new FileReader("Predicado"));
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
                brg = new BufferedReader(new FileReader("Predicado"));

                Linea = brg.readLine();
                do {

                    if (!Linea.equals(predicado.nombre) || cont == 2) {
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
                File fichero = new File("Predicado");
                fichero.delete();
                File f1 = new File("Temporal");
                File f2 = new File("Predicado");
                f1.renameTo(f2);
            }
            JOptionPane.showMessageDialog(null, "\nSE HA ELIMINADO: " + predicado.nombre);
        }
        return predicado.pred;
    }

}
