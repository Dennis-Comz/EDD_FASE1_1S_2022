package proyecto_fase1;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class Administrator {
    private ColaRecepcion cola_recepcion = new ColaRecepcion();
    private ListVentanilla ventanillas = new ListVentanilla();
    private ListaAtendidos atendidos = new ListaAtendidos();
    private QueueBwPrinter impresora_bn = new QueueBwPrinter();
    private QueueColorPrinter impresora_color = new QueueColorPrinter();
    private CircularClientesEspera lista_espera = new CircularClientesEspera();
    private ListaReportes reportes = new ListaReportes();

    private int pasos = 0;

    public Administrator(){}

    public void read_json(String path) throws IOException, ParseException{
        // C:\Users\denni\Desktop\entrada.json
        JSONParser parser = new JSONParser();
        try(Reader reader = new FileReader(path)){
            JSONObject jsonobject = (JSONObject) parser.parse(reader);
            JSONArray values = (JSONArray) parser.parse(jsonobject.values().toString());
            for (int i = 0; i < values.size(); i++) {
                JSONObject inf_cliente = (JSONObject)jsonobject.get("Cliente"+(i+1));
                cola_recepcion.push(i+1, inf_cliente.get("nombre_cliente").toString(), Integer.parseInt(inf_cliente.get("img_color").toString()), Integer.parseInt(inf_cliente.get("img_bw").toString()));
            }
            System.out.println("Clientes cargados\n");
        }
    }
    
    public void create_ventanilla(int n) {
        for (int i = 1; i <= n; i++) {
            ventanillas.append(i);
        }
        System.out.println("Ventanillas creadas");
    }

    public void realizar_paso() {
        pasos += 1;
        create_random();
        System.out.println("---------------------------- PASO " + pasos + "----------------------------");
        // VALIDACION SI UN CLIENTE EN ESPERA YA RECIBIO TODAS SUS IMAGENES
        sacando_de_espera();
        // ENTREGAR IMAGENES DE LOS CLIENTES EN ESPERA
        entregando_imagenes();
        // VALIDACION SI PUEDO SACAR CLIENTES ANTES DE INGRESAR LOS NUEVOS
        enviando_a_espera();
        // ENCONTRANDO VENTANILLA DISPONIBLE
        boolean ventanilla_disponible = ventanillas.find_space();
        // ENTREGA IMAGENES DE TODAS LAS VENTANILLAS SI TIENEN USUARIOS
        ventanillas.set_image();
        if (ventanilla_disponible) {
            agregar_usuario();
        }
    }

    public void agregar_usuario() {
        // OBTENIENDO CLIENTE DE LA COLA DE RECEPCION
        ColaRecepcion.Node cliente_ventanilla = cola_recepcion.pop();
        if (cliente_ventanilla != null) {
            ventanillas.set_user(cliente_ventanilla);
            System.out.println("\t El cliente " + cliente_ventanilla.id_cliente + " " + cliente_ventanilla.nombre_cliente + " Ingreso a la ventanilla " + ventanillas.get_ventanilla(cliente_ventanilla.id_cliente));
        }          
    }

    public void enviando_a_espera() {
        ColaApoyo clientes_apoyo = ventanillas.se_puede_sacar();
        if (clientes_apoyo != null) {
            int cantidad = clientes_apoyo.get_cantidad();
            // SI MAS DE 1 CLIENTE ENTREGO SUS IMAGENES AL MISMO TIEMPO SE DEBE ENVIAR A ESPERA
            for (int i = 0; i < cantidad; i++) {
                ColaRecepcion.Node cliente_atendido = clientes_apoyo.pop();
                System.out.println("\t El cliente " + cliente_atendido.id_cliente + " fue enviado a lista de espera.");       
                lista_espera.append(cliente_atendido);
                PilaImagenes imagenes_aceptadas = cliente_atendido.imagenes;
                int iterador = imagenes_aceptadas.cant_imagenes(cliente_atendido.id_cliente);
                System.out.println("\t Imagenes del cliente " + cliente_atendido.id_cliente + " recibidas en impresoras.");
                for (int j = 0; j < iterador; j++) {
                    PilaImagenes.Node imagen = imagenes_aceptadas.pop();
                    if (imagen.img_bw == true) {
                        impresora_bn.push(cliente_atendido.id_cliente);
                    }else if(imagen.img_color == true){
                        impresora_color.push(cliente_atendido.id_cliente);
                    }
                }
                ventanillas.free_space(cliente_atendido.ventanilla);
            }            
        }
    }

    public void entregando_imagenes() {
        if (impresora_color.puedo_entregar() == true) {
            impresora_color.contador_pasos_color();
            QueueColorPrinter.Node imagen_color = impresora_color.pop();
            if(imagen_color != null){
                lista_espera.increment_paso();
                lista_espera.set_images(imagen_color.id_cliente, "Color");
                System.out.println("\t Imagen a color entregada a cliente " + imagen_color.id_cliente);
            }    
        }
        if (impresora_bn.puedo_entregar() == true) {
            QueueBwPrinter.Node imagen_bn = impresora_bn.pop();
            if (imagen_bn != null) {
                lista_espera.set_images(imagen_bn.id_cliente, "Blanco y Negro");
                System.out.println("\t Imagen a blanco y negro entregada a cliente " + imagen_bn.id_cliente);
            }                    
        }
    }

    public void sacando_de_espera() {
        CircularClientesEspera.Node atendido = lista_espera.finalizado();
            if (atendido != null) {
                System.out.println("\t Cliente " + atendido.cliente.id_cliente + " atendido, enviando a lista de atendidos");
                lista_espera.delete_node(atendido.hashCode());
                atendidos.append(atendido.cliente);
                reportes.append(atendido.cliente);
            }
    }

    public void create_random(){
        String[] nombres = new String[]{"Hugo", "Martin", "Lucas", "Mateo", "Leo", "Daniel", "Alejandro", "Pablo", "Manuel", "Alvaro"};
        String[] apellidos = new String[]{"Hernandez", "Garcia", "Martinez", "Lopez", "Gonzales", "Perez", "Rodriguez", "Sanchez", "Ramirez", "Cruz"};
        
        int random_clientes = (int)Math.floor(Math.random()*(4));
        for (int i = 1; i <= random_clientes; i++) {
            int random_nombre = (int)(Math.random()*nombres.length);
            int random_apellido = (int)(Math.random()*apellidos.length);
            String full_name = nombres[random_nombre] + " " + apellidos[random_apellido];
            int random_color = (int)Math.floor(Math.random()*(4+1));
            int random_bn = (int)Math.floor(Math.random()*(4+1));
            cola_recepcion.push(1+cola_recepcion.get_index(), full_name, random_color, random_bn);

        }
    }

    public void estructuras_memoria() throws IOException {
        cola_recepcion.generate_graphviz();
        atendidos.generate_graphviz();
        ventanillas.generate_graphviz();
        generar_graphviz_imgs();
        lista_espera.generate_graphviz();
        System.out.println("Estructuras Graficas creadas");
    }
    
    public void generar_graphviz_imgs() throws IOException {
        QueueBwPrinter.Node bn = impresora_bn.get_node();
        QueueColorPrinter.Node color = impresora_color.get_node();
        QueueBwPrinter.Node primeroBN = bn;
        QueueColorPrinter.Node primeroCR = color;
        if (primeroBN != null || primeroCR != null) {
            String result = "digraph G{\nbgcolor=\"#50FAA3\"\nnode[shape=box fillcolor=\"#8BDE47\" style=filled];\n";
            result += "subgraph cluster_p{\nlabel=<<B>Colas de Impresion</B>>;\n";
            String conexiones = "";
            String nodos = "";
    
            nodos += "ImpresoraBN[label=\"Impresora\\nBlanco y Negro\"];\n";
            nodos += "ImpresoraColor[label=\"Impresora\\nColor\"];\n";
    
            conexiones += "ImpresoraBN -> ImpresoraColor[dir=none, color=\"#50FAA3\"];\n";
            conexiones += "{rank=same;";
            while (bn != null) {
                nodos += "BN"+bn.hashCode()+"[label=\"Imagen BW\\nDe Cliente "+bn.id_cliente+"\"];\n";
                if (bn.next != null) {
                    conexiones += "BN"+bn.hashCode()+" -> "+"BN"+bn.next.hashCode()+";\n";
                }
                bn = bn.next;
            }
            conexiones += "}\n";
            conexiones += "{rank=same;";
            while (color != null) {
                nodos += "CR"+color.hashCode()+"[label=\"Imagen Color\\nDe Cliente "+color.id_cliente+"\"];\n";
                if (color.next != null) {
                    conexiones += "CR"+color.hashCode()+" -> "+"CR"+color.next.hashCode()+";\n";
                }
                color = color.next;
            }
            conexiones += "}\n";
            if (primeroBN != null) {
                conexiones += "{rank=same;\nImpresoraBN ->"+"BN"+primeroBN.hashCode()+";\n}";                
            }
            if (primeroCR != null) {
                conexiones += "{rank=same;\nImpresoraColor ->"+"CR"+primeroCR.hashCode()+";\n}";                
            }
            result += "//Agrendo Nodos\n"+nodos+"\n";
            result += "//Agregando conexiones\n\n"+conexiones+"\n}\nrankdir = TB\n}";
    
            String ubicacion_dot_recepcion = "C:\\Users\\denni\\Documents\\Varios_Progra\\EDD_PROYECTO_FASE1\\Proyecto_fase1\\src\\graphviz\\imagenes.dot";
            String ubicacion_img_recepcion = "C:\\Users\\denni\\Documents\\Varios_Progra\\EDD_PROYECTO_FASE1\\Proyecto_fase1\\src\\graphviz\\imagenes.png";
            File dotFile = new File(ubicacion_dot_recepcion);
            try {
                dotFile.createNewFile();
            } catch (IOException e) {
                System.out.println("Ocurrio un error creando el archivo");
                e.printStackTrace();
            }        
            FileWriter writer;
            try {
                writer = new FileWriter(ubicacion_dot_recepcion);
                writer.write(result);
                writer.close();
            } catch (IOException e) {
                System.out.println("Ocurrio un error escribiendo el archivo");
                e.printStackTrace();
            }
            String comando = "dot -Tpng " + ubicacion_dot_recepcion + " -o " + ubicacion_img_recepcion;
            ProcessBuilder builder = new ProcessBuilder("cmd.exe","/c"+comando);
            builder.redirectErrorStream(true);
            Process p = builder.start();
    
        }
    }


    public void cliente_mas_pasos() {
        reportes.sort_by_pasos();
    }

    public void top5_mas_color() {
        reportes.sort_by_imgColor();
    }

    public void top5_menos_bn(){
        reportes.sort_by_imgBN();
    }

    public void datos_cliente(int id){
        reportes.buscar_cliente_especifico(id);
    }
}