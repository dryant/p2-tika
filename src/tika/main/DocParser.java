package tika.main;

import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

import org.apache.tika.Tika ;
import org.apache.tika.exception.TikaException;
import java.io.InputStream ;
import java.net.URL;

import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;

import org.apache.tika.sax.*;
import org.apache.tika.sax.Link;

import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.html.HtmlParser;
import org.apache.tika.parser.pdf.PDFParser;

import org.apache.tika.language.detect.LanguageDetector;
import org.apache.tika.langdetect.OptimaizeLangDetector;
import org.apache.tika.language.detect.LanguageResult;

import org.apache.tika.metadata.Metadata;


public class DocParser {

    //    ==================  ATRIBUTOS ==================
    // text tiene el contenido del libro en formato String
    String text;

    //Mapa de palabras y frecuencias
    HashMap<String, Integer> wfmap = new HashMap<String, Integer>();

    //Path del archivo mapfrecuency.data
    String dataFilePath = "";

    //Path del documento a tratar
    String bookPath;

    //El archivo del documento a tratar.
    File f;

    //    ================= FIN ATRIBUTOS =================


    //    ==================== METODOS ====================

    public DocParser() throws IOException {
        // Se parsean un fichero para tener algo con lo que trabajar
        bookPath = "Sherlock.epub";
        dataFilePath = "./"+this.getFileName()+"-mf.data";
    }

    public void setBookPath(String bookPath) {
        this.bookPath = bookPath;
//        System.out.println("El path del libro es " + bookPath);
    }

    public String getBookPath() {
        return bookPath;
    }

    public static void printHorizontalLine(int length) {
        for (int i = 0; i < length; i++) {
            System.out.print("-");
        }
        System.out.println();
    }

    public String parserFile() throws IOException {

        // Creamos una instancia de Tika con la configuracion por defecto
        Tika tika = new Tika();

        File f = new File(bookPath);

        try {

            // Detectamos el MIME tipo del fichero
            String type = tika.detect(f);

            // Extraemos el texto plano en un string
            text = tika.parseToString(f);

            return text;

        } catch (TikaException ex) {

            System.out.println(ex);

        }

        return "Se ha producido un error al parsear el fichero";

    }

    public String getFileName() throws IOException {
        // Creamos una instancia de Tika con la configuracion por defecto
        Tika tika = new Tika();

        // Abrimos el fichero
        File f = new File(bookPath);

        return f.toString();
    }

    public String getType() throws IOException {

        //Codigo para saber el path donde se ejecuta el programa
        String path = new File(".").getAbsolutePath();

        // Creamos una instancia de Tika con la configuracion por defecto
        Tika tika = new Tika();

        File f = new File(bookPath);

        // Detectamos el MIME tipo del fichero
        String type = tika.detect(f);

        return type;

    }

    public String getCharset() throws IOException {

        File in =  new File(bookPath);
        InputStreamReader r = new InputStreamReader(new FileInputStream(in));

        return r.getEncoding();

    }

    public static String getLanguage(String text) throws IOException {
        LanguageDetector identifier = new OptimaizeLangDetector().loadModels();
        LanguageResult idioma = identifier.detect(text);
        return idioma.getLanguage();
    }

    public Hashtable<String, String> getAllMetadata() throws IOException, TikaException, SAXException {

        Hashtable<String, String> metadatos = new Hashtable<String, String>();

        FileInputStream inputstream = new FileInputStream(new File(bookPath));
        BodyContentHandler handler = new BodyContentHandler();
        Metadata metadata = new Metadata();

        ParseContext pcontext = new ParseContext();

        AutoDetectParser parser = new AutoDetectParser();

        org.apache.tika.metadata.Metadata extractedMetadata = new org.apache.tika.metadata.Metadata();

        metadatos.put("nombre", getFileName());
        metadatos.put("tipo", this.getType());
        metadatos.put("lenguaje", this.getLanguage(this.parserFile()));
        metadatos.put("encoding",this.getCharset());

        return metadatos;
    }

    public String[] getBookInfo(boolean withHeader, boolean printTable) throws IOException, TikaException, SAXException {

        // Codigo para saber el path donde se ejecuta el programa
        // (para saber donde tengo que poner el libro de prueba)
        String path = new File(".").getAbsolutePath();

        // Creamos una instancia de Tika con la configuracion por defecto
        Tika tika = new Tika();

        // Abrimos el fichero
        File f = new File(bookPath);


        // Definimos párametros de la tabla y cabeceras
        int ancho=120;
        String titleHeader = "Nombre";
        String tipoHeader = "Tipo";
        String codifHeader = "Codificación";
        String langHeader = "Idioma";


        Hashtable<String, String> metadatos = new Hashtable<String, String>();
        metadatos = this.getAllMetadata();

        String nombre = metadatos.get("nombre");
        String tipo = metadatos.get("tipo");
        String lenguaje = metadatos.get("lenguaje");
        String codificacion = metadatos.get("encoding");

        /*
         * Damos el formato a la tabla:
         * Cada columna medira 30 caracteres
         */
        String format = "%-40s|%-40s|%-30s|%-30s%n";

        if (printTable) {
            // Si el parametro withHeader pasado a la funcion es verdadero, dibujamos la cabecera
            if (withHeader) {
                System.out.printf(format, titleHeader, tipoHeader, codifHeader, langHeader);
                printHorizontalLine(ancho);
            }

            // Imprimimos la información del libro
            System.out.printf(format, nombre, tipo, codificacion, lenguaje );
            printHorizontalLine(ancho);
        }


        String[] bookInfo = {nombre, tipo, codificacion, lenguaje};

        return bookInfo;

    }

    public String[] getMetadata() throws IOException, TikaException, SAXException {

        FileInputStream inputstream = new FileInputStream(new File(bookPath));
        BodyContentHandler handler = new BodyContentHandler();
        Metadata metadata = new Metadata();
        ParseContext pcontext = new ParseContext();

        AutoDetectParser parser = new AutoDetectParser();

        try {
            parser.parse(inputstream, handler, metadata, pcontext);
        } catch (SAXException ex) {
            System.out.println(ex);
        }

        // getting the content of the document
        //System.out.println("Contents of the PDF :" + handler.toString());

        // getting metadata of the document
        System.out.println("Metadata of the PDF:");
        String[] metadataNames = metadata.names();

        //for (String name : metadataNames) {
        //    System.out.println(name + " *:* " + metadata.get(name));
        //}
        System.out.println("Y el nombre del fichero es: "+metadata.get("Content-Type"));
        System.out.println("Y el titulo es: "+metadata.get("title"));
        System.out.println("Y codificación es: "+metadata.get("title"));
        System.out.println("Y el lenguaje es: "+metadata.get("Content-Type"));


        return metadataNames;
    }

    public List<Link> getLinksFromUrl (String urlString) throws Exception {

        URL url = new URL(urlString);

        InputStream input = url.openStream();

        LinkContentHandler linkHandler = new LinkContentHandler();
        ContentHandler textHandler = new BodyContentHandler();
        ToHTMLContentHandler toHTMLHandler = new ToHTMLContentHandler();
        TeeContentHandler teeHandler = new TeeContentHandler(linkHandler, textHandler, toHTMLHandler);

        Metadata metadata = new Metadata();
        ParseContext parseContext = new ParseContext();
        HtmlParser parser = new HtmlParser();

        parser.parse(input, teeHandler, metadata, parseContext);

        List<Link> linksObtenidos = linkHandler.getLinks();

//      Eliminamos posibles links duplicados
//      pasando la lista a un SET (que no permite duplicados)
//      y volviendo a transformar el SET en lista despues de haberla vaciado
//      ========================================================================

        Set<Link> set = new HashSet<Link>(linksObtenidos);
        linksObtenidos.clear();
        linksObtenidos.addAll(set);

//      Creamos la lista que devolveremos
//      ========================================================================

        List<Link> linksClean = new ArrayList<Link>();

        System.out.println("Se han encontrado " + linksObtenidos.size() + " links únicos:");

//      Mostramos todos los links encontrados
//      a la vez que los insertamos en la lista ya limpiada de duplicados y valores vacios
//      ========================================================================

        for (Link link : linksObtenidos) {
            if (link.getUri()!="") {
                linksClean.add(link);
                System.out.println(link.getUri());
            }
        }

        return linksClean;
    }

    public List<Link> getLinks () throws Exception {

        f = new File(bookPath);
        InputStream input = new FileInputStream(f);

        LinkContentHandler linkHandler = new LinkContentHandler();
        ContentHandler textHandler = new BodyContentHandler();
        ToHTMLContentHandler toHTMLHandler = new ToHTMLContentHandler();
        TeeContentHandler teeHandler = new TeeContentHandler(linkHandler, textHandler, toHTMLHandler);

        Metadata metadata = new Metadata();
        ParseContext parseContext = new ParseContext();
        HtmlParser parser = new HtmlParser();

        parser.parse(input, teeHandler, metadata, parseContext);

        List<Link> linksObtenidos = linkHandler.getLinks();

//      Eliminamos posibles links duplicados
//      pasando la lista a un SET (que no permite duplicados)
//      y volviendo a transformar el SET en lista despues de haberla vaciado
//      ========================================================================

        Set<Link> set = new HashSet<Link>(linksObtenidos);
        linksObtenidos.clear();
        linksObtenidos.addAll(set);

//      Creamos la lista que devolveremos
//      ========================================================================

        List<Link> linksClean = new ArrayList<Link>();

        System.out.println("Se han encontrado " + linksObtenidos.size() + " links únicos:");

//      Mostramos todos los links encontrados
//      a la vez que los insertamos en la lista ya limpiada de duplicados y valores vacios
//      ========================================================================

        for (Link link : linksObtenidos) {
            if (link.getUri()!="") {
                linksClean.add(link);
                System.out.println(link.getUri());
            }
        }

        return linksClean;
    }

    public static Metadata getMet(URL url) throws IOException, SAXException, TikaException {
        Metadata met = new Metadata();
        PDFParser parser = new PDFParser();
        parser.parse(url.openStream(), new BodyContentHandler(), met, new ParseContext());
        return met;
    }

    public String getText() {
        return text;
    }

    public HashMap<String, Integer> getWfmap() {
        return wfmap;
    }

    public String getDataFilePath() {
        return dataFilePath;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setDataFilePath(String dataFilePath) {
        this.dataFilePath = dataFilePath;
    }

    private String normalizar() {

        String regex = "\\.|,|\\(|\\)|\\?|\\¿|!|¡|\\;|\\:|\\=|\\/";
        String replacement = " ";
        text = text.replaceAll(regex, replacement);
        text = text.toLowerCase();
        text = text.trim();
        text = text.replaceAll("\\s{2,}", " ");


        text = StringUtils.stripAccents(text);

        return text;
    }

    /*
     *Devuelve el texto un vector de Strings de los tokens extraidos del texto.
     */
    private String[] tokenizar() {
        text = this.normalizar();
        return text.split("\\s");
    }

    public void createWFMap() throws IOException {

        this.parserFile();

        String[] libro = this.tokenizar();

        for (int i = 0; i < libro.length; i++) {

            if (i == 0) {
                wfmap.put(libro[i], 1);
            }

            if (!wfmap.containsKey(libro[i])) {
                wfmap.put(libro[i], 1);
            } else {
                wfmap.put(libro[i], (wfmap.get(libro[i]) + 1));
            }

        }

        wfmap = sortByValue(wfmap);

        System.out.println("Tamaño del mapa : " + wfmap.size());

        //Eliminamos posibles cadenas vacías
        wfmap.remove("");

    }

    protected void showWFMap() {
        for (Map.Entry<String, Integer> entry : wfmap.entrySet()) {
            System.out.println(entry.getKey() + " : " + entry.getValue());
        }
    }

    protected void createPlotDataFile() throws IOException {
        System.out.println("Creando data file");

        String mensaje = "";

        int i = 1;

        for (Map.Entry<String, Integer> entry : wfmap.entrySet()) {
            mensaje=mensaje+i+"\t"+(entry.getKey() + "\t" + entry.getValue())+"\n";
            i++;
        }
//        System.out.println(mensaje);

        try {
            FileWriter fichero = new FileWriter(dataFilePath);
            fichero.write(mensaje);
            fichero.close();
            System.out.println("Archivo '"+this.getDataFilePath()+"' creado correctamente.\n==========================");

        } catch (Exception exception) {

            exception.printStackTrace();

        }

    }

    protected void createPlotCommands() {

    }

    private static HashMap<String, Integer> sortByValue(HashMap<String, Integer> vfmap) {
        return vfmap.entrySet()
                .stream()
                .sorted((Map.Entry.<String, Integer>comparingByValue().reversed()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
    }
}
