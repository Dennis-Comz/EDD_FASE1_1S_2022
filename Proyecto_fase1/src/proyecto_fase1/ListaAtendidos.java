package proyecto_fase1;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class ListaAtendidos {
    private class Node{
        ColaRecepcion.Node cliente;
        Node next;
        public Node(ColaRecepcion.Node cliente){
            this.cliente = cliente;
            this.next = null;
        }
    }

    private Node head;

    public void append(ColaRecepcion.Node cliente){
        Node newNode = new Node(cliente);
        newNode.cliente.increment_paso();
        if (head == null) {
            head = newNode;
        }else{
            Node last = head;
            while (last.next != null) {
                last = last.next;
            }
            last.next = newNode;
        }
    }

    public void generate_graphviz() throws IOException {
        String result = "digraph G{\nbgcolor=\"#50FAA3\"\nnode[shape=box fillcolor=\"#8BDE47\" style=filled];\n";
        result += "subgraph cluster_p{\nlabel=<<B>Lista de Atendidos</B>>;\n";
        Node aux = head;
        String conexiones = "";
        String nodos = "";
        while (aux != null) {
            nodos += "N"+aux.hashCode()+"[label=\"Cliente: "+String.valueOf(aux.cliente.id_cliente);
            nodos += "\\n"+aux.cliente.nombre_cliente+"\\nIMG BN: "+aux.cliente.img_bw_sinM+"\\nIMG COLOR: "+aux.cliente.img_color_sinM+"\\nPasos: "+String.valueOf(aux.cliente.pasos);
            nodos += "\"];\n";
            if (aux.next != null) {
                conexiones += "N"+aux.hashCode()+" -> "+"N"+aux.next.hashCode()+";\n";
            }
            aux = aux.next;
        }
        result += "//Agrendo Nodos\n"+nodos+"\n";
        result += "//Agregando conexiones\n\n"+conexiones+"\n}\nrankdir = LR\n}";

        // Ubicaciones donde se generaran las imagenes
        String ubicacion_dot_recepcion = "C:\\Users\\denni\\Documents\\Varios_Progra\\EDD_PROYECTO_FASE1\\Proyecto_fase1\\src\\graphviz\\atendidos.dot";
        String ubicacion_img_recepcion = "C:\\Users\\denni\\Documents\\Varios_Progra\\EDD_PROYECTO_FASE1\\Proyecto_fase1\\src\\graphviz\\atendidos.png";
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
    
    public void print_list(){
        Node temp = head;
        while (temp != null) {
            System.out.println("ID: "+temp.cliente.id_cliente+"; Nombre: "+temp.cliente.nombre_cliente+"; Ventanilla: "+temp.cliente.ventanilla+"; Cant. Imagenes: "+temp.cliente.cant_imagenes+"; IMG BN: "+temp.cliente.img_bw_sinM+"; IMG COLOR: "+temp.cliente.img_color_sinM+"; Pasos: "+temp.cliente.pasos);
            temp = temp.next;
        }
    }
}
