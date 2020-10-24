package tika.main;

import java.util.Scanner;

public class Gui {
    String welcomeMsg;
    String opciones[] ;

    public Gui(){
        welcomeMsg = "Practica 2 - Recuperacion de información\n-----------------------------------\nPedro Blanch Bejarano\n\n";
        opciones = new String[]{"Mostrar tabla con info de los documentos de un directorio", "Extraer links del documento (solo URLS)", "Mostrar tabla de frecuencias de un libro","Generar archivo con tabla de frecuencias para GNUPLOT","Mostrar comando para crear "};
    }
    public void showMenu() {
        int i=1;

        System.out.println(welcomeMsg);
        for (String opcion :
                opciones) {
            System.out.println(i + " : " + opcion);
            i++;
        }


    }

    public final static void clearConsole()
    {
        try
        {
            final String os = System.getProperty("os.name");

            if (os.contains("Windows"))
            {
                Runtime.getRuntime().exec("cls");
            }
            else
            {
                Runtime.getRuntime().exec("clear");
            }
        }
        catch (final Exception e)
        {
            //  Handle any exceptions.
        }
    }

    public void pause(){
        System.out.println("Pulsa enter para continuar");
        Scanner scanner = new Scanner(System.in);
        String entrada  ="";
        do{
            entrada  = scanner.nextLine();
            System.out.println(entrada);
        }
        while(!entrada.equals(""));
    }
}
