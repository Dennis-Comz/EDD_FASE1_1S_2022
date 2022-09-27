package proyecto_fase1;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class ColaRecepcion {
    class Node{
        int id_cliente;
        int img_color;
        int img_bw;
        int img_color_sinM;
        int img_bw_sinM;
        int ventanilla;
        int cant_imagenes;
        int pasos;
        String nombre_cliente;
        PilaImagenes imagenes;
        Node next;
        public Node(int id_cliente,String nombre_cliente,int img_color,int img_bw){
            this.id_cliente = id_cliente;
            this.img_color = img_color;
            this.img_bw = img_bw;
            this.img_bw_sinM = img_bw;
            this.img_color_sinM = img_color;
            this.ventanilla = 0;
            this.pasos = 0;
            this.nombre_cliente = nombre_cliente;
            this.cant_imagenes = img_color+img_bw;
            this.imagenes = null;
            this.next = null;
        }
        public void increment_paso() {
            this.pasos += 1;
        }
    }

    private Node head;
    public int index = 0;

    public void push(int id_cliente,String nombre_cliente,int img_color,int img_bw){
        Node newNode = new Node(id_cliente, nombre_cliente, img_color, img_bw);
        if (head == null) {
            head = newNode;
            index += 1;
        }else{
            Node last = head;
            while(last.next != null){
                last = last.next;
            }
            last.next = newNode;
            index += 1;
        }
    }

    public Node pop(){
        if (head == null) {
            return null;
        }else{
            Node temp = head;
            head = temp.next;
            return temp;
        }
    }
    
    // Uso para crear usuarios random
    public int get_index(){
        return index;
    }

    public void generate_graphviz() throws IOException {
        String result = "digraph G{\nbgcolor=\"#50FAA3\"\nnode[shape=box fillcolor=\"#8BDE47\" style=filled height=2 width=3 fontsize=25];\n";
        result += "subgraph cluster_p{\nlabel=<<B>Cola de Recepcion</B>>;\n";
        Node aux = head;
        String conexiones = "";
        String nodos = "";
        while (aux != null) {
            nodos += "N"+aux.hashCode()+"[label=\"Cliente: "+String.valueOf(aux.id_cliente);
            nodos += "\\n"+aux.nombre_cliente+"\\nIMG BN: "+aux.img_bw_sinM+"\\nIMG COLOR: "+aux.img_color_sinM;
            nodos += "\"];\n";
            if (aux.next != null) {
                conexiones += "N"+aux.hashCode()+" -> "+"N"+aux.next.hashCode()+"[dir=back];\n";
            }
            aux = aux.next;
        }
        result += "//Agrendo Nodos\n"+nodos+"\n";
        result += "//Agregando conexiones\n\n"+conexiones+"\n}\nrankdir = RL\n}";

        // Ubicaciones donde se generaran las imagenes
        String ubicacion_dot_recepcion = "C:\\Users\\denni\\Documents\\Varios_Progra\\EDD_PROYECTO_FASE1\\Proyecto_fase1\\src\\graphviz\\recepcion.dot";
        String ubicacion_img_recepcion = "C:\\Users\\denni\\Documents\\Varios_Progra\\EDD_PROYECTO_FASE1\\Proyecto_fase1\\src\\graphviz\\recepcion.png";
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

    public void print_clients(){
        Node temp = head;
        while (temp != null) {
            System.out.println("ID: "+temp.id_cliente+"; Nombre: "+temp.nombre_cliente+"; Imagenes a Color: "+temp.img_color+"; Imagenes a BN: "+temp.img_bw);
            temp = temp.next;
        }
    }
}
