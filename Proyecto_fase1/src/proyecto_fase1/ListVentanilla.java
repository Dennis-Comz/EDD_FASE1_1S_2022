package proyecto_fase1;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
public class ListVentanilla {
    class Node{
        int num;
        boolean estado;
        ColaRecepcion.Node cliente;
        PilaImagenes imagenes;
        Node next;
        public Node(int num){
            this.num = num;
            this.estado = false;
            this.cliente = null;
            this.imagenes = new PilaImagenes();
            this.next = null;
        }
    }
    private Node head;
    private ColaApoyo apoyo = new ColaApoyo();

    public void append(int num){
        Node newNode = new Node(num);
        if (head == null) {
            head = newNode;
        }else{
            Node last = head;
            while(last.next != null) {
                last = last.next;
            }
            last.next = newNode;
        }
    }

    public boolean find_space(){
        Node temp = head;
        while (temp != null && temp.estado != false) {
            temp = temp.next;
        }
        if (temp != null) {
            return true;            
        }else{
            return false;
        }
    }

    public void free_space(int num){
        Node temp = head;
        while (temp.num != num) {
            temp = temp.next;
        }
        temp.estado = false;
        temp.cliente = null;
        temp.imagenes = new PilaImagenes();
    }

    public ColaApoyo se_puede_sacar() {
        Node temp = head;
        while (temp != null && temp.cliente != null) {
            if (temp.cliente.img_bw == 0 && temp.cliente.img_color == 0) {
                    temp.cliente.imagenes = temp.imagenes;
                    Node nodo_lista = pop_cliente(temp.hashCode());
                    apoyo.push(nodo_lista.cliente);
            }
            temp = temp.next;
        }
        if (apoyo.is_empty() == false) {    
            return apoyo;
        }
        return null;
    }
// Uso interno de la lista
    private Node  pop_cliente(int objeto) {
        Node temp = head;
        while (temp.hashCode() != objeto) {
            temp = temp.next;
        }
        return temp;
    }

    public void set_user(ColaRecepcion.Node usuario){
        Node temp = head;
        while (temp.estado != false) {
            temp = temp.next;
        }
        temp.estado = true;
        temp.cliente = usuario;
        temp.cliente.ventanilla = temp.num;
        temp.cliente.increment_paso();
    }

    public void set_image(){
        Node temp = head;
        while (temp != null && temp.cliente != null) {
            temp.cliente.increment_paso();
            if (temp.cliente.img_bw != 0 ) {
                temp.imagenes.push(temp.cliente.id_cliente, false, true);
                temp.cliente.img_bw -= 1;
                System.out.println("\t La ventanilla " + temp.num + " recibio una imagen blanco y negro.");
            }else if (temp.cliente.img_color != 0) {
                temp.imagenes.push(temp.cliente.id_cliente, true, false);
                temp.cliente.img_color -= 1;
                System.out.println("\t La ventanilla " + temp.num + " recibio una imagen a color.");
            }
            temp = temp.next;
        }
    }

    public int get_ventanilla(int id_cliente) {
        Node temp = head;
        while (temp != null) {
            if (temp.cliente.id_cliente == id_cliente) {
                return temp.num;
            }
            temp = temp.next;
        }
        return 0;
    }
    
    public void generate_graphviz() throws IOException {
        String result = "digraph G{\nbgcolor=\"#50FAA3\"\nnode[shape=box fillcolor=\"#8BDE47\" style=filled];\n";
        result += "subgraph cluster_p{\nlabel=<<B>Lista de Ventanillas</B>>;\n";
        Node ventanilla_aux = head;
        String conexiones = "";
        String nodos = "";
        while (ventanilla_aux != null) {
            // Nodos imagenes
            if (ventanilla_aux.imagenes.is_empty() == false) {
                PilaImagenes.Node img_aux = ventanilla_aux.imagenes.get_nodo();
                while (img_aux != null) {
                    if (img_aux.img_color) {
                        nodos += "I"+img_aux.hashCode()+"[label=\"Imagen\\nColor\"];\n";
                    }else{
                        nodos += "I"+img_aux.hashCode()+"[label=\"Imagen\\nBW\"];\n";
                    }
                    img_aux = img_aux.next;
                }
            }
            // Nodos ventanillas
            nodos += "V"+ventanilla_aux.hashCode()+"[label=\"Ventanilla "+ventanilla_aux.num+"\"];\n";
            // Nodos clientes
            if (ventanilla_aux.cliente != null) {
                nodos += "C"+ventanilla_aux.cliente.hashCode()+"[label=\"Cliente "+ventanilla_aux.cliente.id_cliente+"\\nImg BN: "+ventanilla_aux.cliente.img_bw+"\\nImg Color: "+ventanilla_aux.cliente.img_color+"\"];\n";
                // Conexion cliente ventanilla
                conexiones += "{rank=same;\n";
                conexiones += "C"+ventanilla_aux.cliente.hashCode()+" -> "+"V"+ventanilla_aux.hashCode()+";\n}\n";
            }
            // Conexion entre ventanillas
            if (ventanilla_aux.next != null) {
                conexiones += "V"+ventanilla_aux.hashCode()+" -> "+"V"+ventanilla_aux.next.hashCode()+";\n";
            }
            if (ventanilla_aux.imagenes.is_empty() == false) {
                PilaImagenes.Node img_conex = ventanilla_aux.imagenes.get_nodo();
                PilaImagenes.Node img_aux = ventanilla_aux.imagenes.get_nodo();
                while (img_aux != null) {
                    if (img_aux.next != null) {
                        conexiones += "{rank=same;\n";
                        conexiones += "I"+img_aux.hashCode()+" -> "+"I"+img_aux.next.hashCode()+";\n}\n";
                    }
                    img_aux = img_aux.next;
                }
                conexiones += "{rank=same;\n";
                conexiones += "V"+ventanilla_aux.hashCode()+" -> "+"I"+img_conex.hashCode()+";\n}\n";
            }
            ventanilla_aux = ventanilla_aux.next;
        }
        result += "//Agrendo Nodos\n"+nodos+"\n";
        result += "//Agregando conexiones\n\n"+conexiones+"\n}\nrankdir = TB\n}";
        // Ubicaciones donde se generaran las imagenes
        String ubicacion_dot_recepcion = "C:\\Users\\denni\\Documents\\Varios_Progra\\EDD_PROYECTO_FASE1\\Proyecto_fase1\\src\\graphviz\\ventanillas.dot";
        String ubicacion_img_recepcion = "C:\\Users\\denni\\Documents\\Varios_Progra\\EDD_PROYECTO_FASE1\\Proyecto_fase1\\src\\graphviz\\ventanillas.png";
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

    public void print_list() {
        Node temp = head;
        while (temp != null) {
            if (temp.cliente != null) {
                System.out.println("Cliente: " +temp.cliente.id_cliente +"; No. " + temp.num + "; Estado: " + temp.estado);
                temp.imagenes.print_images();                
            }else{
                System.out.println("Cliente: 0" +"; No. " + temp.num + "; Estado: " + temp.estado);
                temp.imagenes.print_images();
            }
            temp = temp.next;
        }
    }
}