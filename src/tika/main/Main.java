/* Pedro Blanch Bejarano - UGR
 * ============================================
 * creado el 14 de octubre de 2020
 *
 * Abrir una carpeta pasada por argumento y que:
 *
 * - lea todos los documentos en él,
 * - leerlos,
 * - extraer los links
 * - separarlo en palabras
 * - contar cuantas veces aparece cada palabra
 * - y sacar por pantalla todos las palabras en orden de frecuencia de aparicion
 *
 * Libros que se pueden usar:
 *
 * /Users/dryant/Facultad/RI/P2/datos-20201006/sherlock.epub
 * /Users/dryant/Facultad/RI/P2/datos-20201006/dracula.epub
 * /Users/dryant/Facultad/RI/P2/datos-20201006/DonJuanTenorio.htm
 * /Users/dryant/Facultad/RI/P2/DocumentacionTika-20201006/Iliada.pdf
 *
 * Memoria
 * ==========
 * - Cual es el problema
 * - Como lo he resuelto
 * - Parte de resultados
 * - Como se ejecuta el software
 *
 * */
package tika.main;

import java.io.FileWriter;
import java.io.PrintStream;
import java.lang.String;
import java.util.InputMismatchException;
import java.util.Scanner;

import java.io.File;
import java.util.regex.Pattern;


public class Main {

    public static <ex> void main(String[] args) throws Exception {

        System.setErr(new PrintStream("/dev/null"));
        Gui interfaz = new Gui();

        int opcion = 0;

        DocParser documento = new DocParser();

        boolean salir = false;


        while (!salir) {

            interfaz.showMenu();
            System.out.println("9- Salir");

            Scanner reader = new Scanner(System.in);
            try {
                opcion = reader.nextInt();
                switch (opcion) {
                    case 1:
                        System.out.println("Introduce el path del directorio a escanear");
                        String directoryPath = reader.next();
                        while (!Pattern.matches("[a-zA-Z0-9\\/]+",directoryPath)) {
                            System.out.println("Introduce un directorio correcto");
                            directoryPath = reader.next();
                        }

                        File carpeta = new File(directoryPath);
                        if (!carpeta.exists()) {
                            System.out.println("¡¡No existe el directorio especificado!!");
                            interfaz.pause();
                            break;
                        }

                        File[] archivos = carpeta.listFiles();

                        PrettyTable infoTable = new PrettyTable("Nombre", "Tipo", "Codificacion", "Lenguaje");

                        String[] bookInfo;

                        for (int i=0; i< archivos.length; i++) {

                            documento.setBookPath(archivos[i].toString());
                            bookInfo = documento.getBookInfo(true, false);

                            String nombreFichero = bookInfo[0].replace(carpeta.toString()+"/", "");

                            infoTable.addRow(nombreFichero, bookInfo[1],bookInfo[2],bookInfo[3]);

                        }

                        System.out.println(infoTable);
                        try {
                            new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
                        } catch (Exception e) {
                            /*No hacer nada*/
                        }
                        interfaz.pause();
                        interfaz.clearConsole();
                        break;
                    case 2:
                        System.out.println("Introduce la URL a extraer links");
                        String url = reader.next();
                        String regex = "^(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]";
                        while (!Pattern.matches(regex,url)) {
                            System.out.println("Introduce una url correcta (incluyendo http/https).");
                            url = reader.next();
                        }
                        documento.getLinksFromUrl(url);
                        interfaz.pause();
                        interfaz.clearConsole();
                        break;
                    case 3:
                        System.out.println("Introduce el path del libro a escanear");
                        String docPath = reader.next();

                        documento.setBookPath(docPath);

                        documento.createWFMap();
                        documento.showWFMap();
                        interfaz.pause();
                        interfaz.clearConsole();
                        break;
                    case 4:
                        System.out.println("Introduce el path del directorio a escanear");
                        String directoryPath2 = reader.next();
                        while (!Pattern.matches("[a-zA-Z0-9\\-\\.\\_\\/]+",directoryPath2)) {
                            System.out.println("Introduce un directorio correcto");
                            directoryPath2 = reader.next();
                        }

                        File carpeta2 = new File(directoryPath2);
                        File nuevaCarpeta = new File("./data");
                        nuevaCarpeta.mkdir();

                        if (!carpeta2.exists()) {
                            System.out.println("¡¡No existe el directorio especificado!!");
                            interfaz.pause();
                            break;
                        }
                        File[] archivos2 = carpeta2.listFiles();

                        FileWriter comandoTotal = new FileWriter("comandos_gnuplot.p");

                        String totalCommand = "";
                        String command1 = "#0.- Establece el titulo de la tabla y el nombre de los ejes vertical y horizontal\n\nset title 'Terminos'\nset xlabel 'Ranking'\nset ylabel 'Frecuencia'\n\n#1.- Gráficos de cada libro sin suavizar. Copiar y pegar en GNUPLOT cada linea de forma independiente\n\n";
                        String command2 = "\n#2.- Dibuja la grafica suavizada por cada libro. \n#Copiar y pegar en GNUPLOT cada línea de forma individual\n\n";
                        String command3 = "\n#3.- Creamos la funcion f(x).\n\nf(x) = log (k) -m*x\n";
                        String command4 = "\n#4.- Creamos la tabla suavizada\n\n";
                        String command5 = "\n#5.- Dibujamos en un solo gráfico todas las gráficas y el ajuste lineal\n\nplot ";

                        for (int i=0; i< archivos2.length; i++) {
                            String nombreFichero = "";
                            nombreFichero = (archivos2[i].toString()).replace(carpeta2.toString() + "/", "");
                            String lenguaje = "";
                            nombreFichero = nombreFichero.substring(0, nombreFichero.lastIndexOf('.'));
                            System.out.println(nombreFichero);
                            if (!nombreFichero.equals("")) {
                                documento.setDataFilePath("./data/"+nombreFichero+"-"+"ms.data");
                                documento.setBookPath(archivos2[i].toString());
                                documento.createWFMap();
                                documento.createPlotDataFile();

                                lenguaje = (documento.getBookInfo(false, false))[3];
                                command1 += "plot 'data/" + nombreFichero + "-" + "ms.data' using 1:3 title '"+lenguaje+"' with lines\n";
                                command2 += "plot 'data/" + nombreFichero + "-" + "ms.data' using (log($1)):(log($3)) title '"+lenguaje+"' with lines\n";
                                command4 += "fit f(x) 'data/" + nombreFichero + "-" + "ms.data' using (log($1)):(log($3)) via k,m\n";
                                if (i != (archivos2.length) - 1) {
                                    command5 += "'data/" + nombreFichero + "-" + "ms.data' using (log($1)):(log($3)) title '" + lenguaje + "' with lines, \\\n" +
                                            " ";
                                } else {
                                    command5 += "'data/" + nombreFichero + "-" + "ms.data' using (log($1)):(log($3)) title '"+lenguaje+"' with lines, f(x) title 'Ajuste lineal general' \n";
                                }

                            }

                        }


                        totalCommand = command1 + command2 + command3 + command4 + command5;
                        comandoTotal.write(totalCommand);
                        comandoTotal.close();
                        interfaz.pause();
                        interfaz.clearConsole();
                        break;
                    case 5:

                        System.out.println("Con la opcion 4 se genera un archivo en la raíz del programa llamado plotP2.p en al cual están todos los comandos para crear las gráficas en GNUPLOT.\nPara usar ese programa se puede usar copiando y pegando las distintas opciones que hay en el archivo, o con el comando:\n\nload plotP2.p\t\tlanzado desde gnuplot");
                        interfaz.pause();
                        interfaz.clearConsole();
                        break;
                    case 9:
                        salir = true;
                        break;
                    default:
                        break;
                }
            }catch (InputMismatchException e) {
                System.out.println("Debes insertar un número de los existentes");
                interfaz.pause();
                reader.next();
            }

        }

    }

}

