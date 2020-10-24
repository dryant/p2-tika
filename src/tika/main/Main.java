/*
 * Abrir una carpeta pasada por argumento y que:
 * lea todos los documentos en él,
 * leerlos,
 * extraer los links
 * separarlo en palabras
 * contar cuantas veces aparece cada palabra
 * y sacar por pantalla todos las palabras en orden de frecuencia de aparicion
 *
 * /Users/dryant/Facultad/RI/P2/datos-20201006/sherlock.epub
 * /Users/dryant/Facultad/RI/P2/datos-20201006/dracula.epub
 * /Users/dryant/Facultad/RI/P2/datos-20201006/DonJuanTenorio.htm
 * /Users/dryant/Facultad/RI/P2/DocumentacionTika-20201006/Iliada.pdf
 *
 * Memoria
 * ==========
 * Cual es el problema
 * Como lo he resuelto
 * Parte de resultados
 * Como se ejecuta el software
 *
 * */
package tika.main;

import java.io.PrintStream;
import java.lang.String;
import java.util.InputMismatchException;
import java.util.Scanner;

import java.io.File;
import java.util.regex.Pattern;


public class Main {

    public static <ex> void main(String[] args) throws Exception {

        System.out.println("hola");

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
                        documento.createPlotDataFile();
                        interfaz.pause();
                        interfaz.clearConsole();
                        break;
                    case 5:
                        System.out.println("plot 'mapfrecuency.data' using 2:3\nf(x) = a*x + b\nfit f(x) 'archivo.dat' using 2:3 via a,b");
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
                reader.next();
            }

        }

    }

}

