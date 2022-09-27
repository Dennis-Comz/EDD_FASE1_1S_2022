package proyecto_fase1;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class CircularClientesEspera {
    class Node{
        ColaRecepcion.Node cliente;
        ListaImagenes imagenes;
        Node next;
        Node prev;
        public Node(ColaRecepcion.Node cliente){
            this.cliente = cliente;
            this.imagenes = new ListaImagenes();
            this.next = null;
            this.prev = null;
        }
    }

    private Node head;

    public void append(ColaRecepcion.Node cliente){
        Node newNode = new Node(cliente);
        newNode.cliente.increment_paso();
        if (head == null) {
            newNode.next = newNode.prev = newNode;
            head = newNode;
        }else{
            Node last = head.prev;
            newNode.next = head;
            head.prev = newNode;
            newNode.prev = last;
            last.next = newNode;
        }
    }

    public void set_images(int id_cliente, String tipo) {
        Node temp = head;
        while (temp.cliente.id_cliente != id_cliente && temp.next != head) {
            temp = temp.next;
        }
        temp.imagenes.append(tipo);
        temp.cliente.increment_paso();
    }

    public Node finalizado(){
        Node temp = head;
        while (temp != null && temp.next != head) {
            if (temp.cliente.cant_imagenes == temp.imagenes.get_index()) {
                return temp;
            }
            temp = temp.next;
        }
        if (temp != null){
            if (temp.cliente.cant_imagenes == temp.imagenes.get_index()) {
                return temp;
            }
        }
        return null;
    }

    public Node delete_node(int hashcode) {
        if (head == null) {
            return null;
        }
        Node curr = head, prev_1= null;
        while (curr.hashCode() != hashcode) {
            if (curr.next == head) {
                return head;
            }
            prev_1 = curr;
            curr = curr.next;
        }

        if (curr.next == head && prev_1 == null) {
            (head) = null;
            return head;
        }

        if (curr == head) {
            prev_1 = head.prev;
            head = head.next;

            prev_1.next = head;
            head.prev = prev_1;
        }else if(curr.next == head){
            prev_1.next = head;
            head.prev = prev_1;
        }else{
            Node temp = curr.next;
            prev_1.next = temp;
            temp.prev = prev_1;
        }
        return head;
    }
    
    public void increment_paso() {
        Node temp = head;
        while (temp != null && temp.next != head) {
            temp.cliente.increment_paso();
            temp = temp.next;
        }
    }
    
    public void generate_graphviz() throws IOException {
        String result = "digraph G{\nlabelloc=\"t\";\nlabel=<<B>Lista de Espera</B>>;\nbgcolor=\"#50FAA3\";\n"+
            "graph[splines=ortho, nodesep=1];\nnode[shape=box fillcolor=\"#8BDE47\" style=filled];\n"+
            "edge[arrowsize=0.6];\nsubgraph clushter{\nstyle=invis;";
        String conexiones = "";
        String nodos = "";
        Node aux = head;
        Node buscando_final = head;
        if (aux != null) {
            while (aux.next != head) {
                nodos += "C"+aux.cliente.hashCode()+"[label=\"Cliente "+aux.cliente.id_cliente+"\\n"+aux.cliente.nombre_cliente+"\"];\n";
                if (aux.imagenes.is_empty() == false) {
                    ListaImagenes.Node img = aux.imagenes.get_node();
                    conexiones += "C"+aux.cliente.hashCode()+" -> "+"I"+img.hashCode()+";\n";
                    while (img != null) {
                        nodos+="I"+img.hashCode()+"[label=\"Imagen\\n"+img.tipo+"\"];\n";
                        if (img.next != null) {
                            conexiones += "I"+img.hashCode()+" -> "+"I"+img.next.hashCode()+";\n";
                        }
                        img = img.next;
                    }
                }
                if (aux.next != null) {
                    conexiones += "{ rank = same;\n";
                    conexiones += "C"+aux.cliente.hashCode()+" -> "+"C"+aux.next.cliente.hashCode()+";\n";
                    conexiones += "};\n";
                    conexiones += "{ rank = same;\n";
                    conexiones += "C"+aux.cliente.hashCode()+" -> "+"C"+aux.next.cliente.hashCode()+"[dir=back];\n";
                    conexiones += "};\n";
                }
    
                aux = aux.next;
            }
            nodos += "C"+aux.cliente.hashCode()+"[label=\"Cliente "+aux.cliente.id_cliente+"\\n"+aux.cliente.nombre_cliente+"\"];\n";
            if (aux.imagenes.is_empty() == false) {
                ListaImagenes.Node img = aux.imagenes.get_node();
                conexiones += "C"+aux.cliente.hashCode()+" -> "+"I"+img.hashCode()+";\n";
                while (img != null) {
                    nodos+="I"+img.hashCode()+"[label=\"Imagen\\n"+img.tipo+"\"];\n";
                    if (img.next != null) {
                        conexiones += "I"+img.hashCode()+" -> "+"I"+img.next.hashCode()+";\n";
                    }
                    img = img.next;
                }
                if (aux.next != null) {
                    conexiones += "{ rank = same;\n";
                    conexiones += "C"+aux.cliente.hashCode()+" -> "+"C"+aux.next.cliente.hashCode()+";\n";
                    conexiones += "};\n";
                    conexiones += "{ rank = same;\n";
                    conexiones += "C"+aux.cliente.hashCode()+" -> "+"C"+aux.next.cliente.hashCode()+"[dir=back];\n";
                    conexiones += "};\n";
                }
            }
    
            while (buscando_final.next != head) {
                buscando_final = buscando_final.next;
            }
            conexiones += "C"+head.cliente.hashCode()+" -> "+"C"+buscando_final.cliente.hashCode()+";\n";
            conexiones += "C"+buscando_final.cliente.hashCode()+" -> "+"C"+head.cliente.hashCode()+";\n";
            result += "//Agrendo Nodos\n"+nodos+"\n";
            result += "//Agregando conexiones\n\n"+conexiones+"\n}\n}";
        }

        // Ubicaciones donde se generaran las imagenes
        String ubicacion_dot_recepcion = "C:\\Users\\denni\\Documents\\Varios_Progra\\EDD_PROYECTO_FASE1\\Proyecto_fase1\\src\\graphviz\\espera.dot";
        String ubicacion_img_recepcion = "C:\\Users\\denni\\Documents\\Varios_Progra\\EDD_PROYECTO_FASE1\\Proyecto_fase1\\src\\graphviz\\espera.png";
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
        while (temp != null && temp.next != head) {
            System.out.println("ID: " + temp.cliente.id_cliente+"; Nombre: "+temp.cliente.nombre_cliente+"; Ventanilla: "+temp.cliente.ventanilla+"; Imagenes: "+temp.cliente.cant_imagenes);
            temp = temp.next;
        }
        if (temp != null) {
            System.out.println("ID: " + temp.cliente.id_cliente+"; Nombre: "+temp.cliente.nombre_cliente+"; Ventanilla: "+temp.cliente.ventanilla+"; Imagenes: "+temp.cliente.cant_imagenes);            
        }
    }
}
