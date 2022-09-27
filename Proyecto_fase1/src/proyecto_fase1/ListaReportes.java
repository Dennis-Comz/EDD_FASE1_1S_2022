package proyecto_fase1;

import java.awt.Desktop;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class ListaReportes {
    class Node{
        ColaRecepcion.Node datos;
        Node next;
        public Node(ColaRecepcion.Node datos){
            this.datos = datos;
            this.next = null;
        }
    }

    private Node head;

    public void append(ColaRecepcion.Node datos) {
        Node newNode = new Node(datos);
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

    // Algoritmos de ordenamiento similares
    public void sort_by_pasos(){
        Node current = head, index = null;
        ColaRecepcion.Node temp = head.datos;
        if (head == null) {
            return;
        }else{
            while (current != null) {
                index = current.next;
                while (index != null) {
                    if (current.datos.pasos < index.datos.pasos) {
                        temp = current.datos;
                        current.datos = index.datos;
                        index.datos = temp;
                    }
                    index = index.next;
                }
                current = current.next;
            }
        }
        imprimir_mas_pasos();
    }
    public void imprimir_mas_pasos() {
        Node temp = head;
        String result = "digraph G{\nbgcolor=\"#50FAA3\"\nnode[shape=note fillcolor=\"#8BDE47\" style=filled];\n";
        result += "subgraph cluster_p{\nlabel=<<B>Cliente Que Estuvo Mas Pasos En El Sistema</B>>;\n";
        String nodos = "C"+temp.datos.hashCode()+"[label=\"Cliente "+temp.datos.id_cliente+"\\n"+temp.datos.nombre_cliente+"\\nImagenes: "+temp.datos.cant_imagenes+"\\nPasos: "+temp.datos.pasos+"\"];\n";
        nodos += "}\n}";
        result += nodos;
        String ubicacion_dot = "C:\\Users\\denni\\Documents\\Varios_Progra\\EDD_PROYECTO_FASE1\\Proyecto_fase1\\src\\graphviz\\maspasos.dot";
        String ubicacion_img = "C:\\Users\\denni\\Documents\\Varios_Progra\\EDD_PROYECTO_FASE1\\Proyecto_fase1\\src\\graphviz\\maspasos.png";
        File dotFile = new File(ubicacion_dot);
        try {
            dotFile.createNewFile();
        } catch (IOException e) {
            System.out.println("Ocurrio un error creando el archivo");
            e.printStackTrace();
        }        
        FileWriter writer;
        try {
            writer = new FileWriter(ubicacion_dot);
            writer.write(result);
            writer.close();
        } catch (IOException e) {
            System.out.println("Ocurrio un error escribiendo el archivo");
            e.printStackTrace();
        }
        String comando = "dot -Tpng " + ubicacion_dot + " -o " + ubicacion_img;
        ProcessBuilder builder = new ProcessBuilder("cmd.exe","/c"+comando);
        builder.redirectErrorStream(true);
        try {
            Process p = builder.start();
        } catch (IOException e) {e.printStackTrace();}

        System.out.println("Cliente que mas pasos estuvo en el sistema graficado.\n");

    }

    public void sort_by_imgColor(){
        Node current = head, index = null;
        ColaRecepcion.Node temp = head.datos;
        if (head == null) {
            return;
        }else{
            while (current != null) {
                index = current.next;
                while (index != null) {
                    if (current.datos.img_color_sinM < index.datos.img_color_sinM) {
                        temp = current.datos;
                        current.datos = index.datos;
                        index.datos = temp;
                    }
                    index = index.next;
                }
                current = current.next;
            }
        }
        imprimir_5_color();
    }
    public void imprimir_5_color() {
        String result = "digraph G{\nlabelloc=\"t\";\nlabel=<<B>TOP 5 CLIENTES CON MAS IMAGENES A COLOR</B>>;\nbgcolor=\"#50FAA3\";\n"+
        "graph[splines=ortho, nodesep=1];\nnode[shape=box fillcolor=\"#8BDE47\" style=filled];\n"+
        "edge[arrowsize=0.6];\nsubgraph clushter{\nstyle=invis;";
        String conexiones = "";
        String nodos = "";
        Node temp = head;
        int contador = 1;
        while (temp != null && contador <= 5) {
            nodos += "C"+temp.datos.hashCode()+"[label=\"No "+contador+"\\nCliente "+temp.datos.id_cliente+"\\nImagenes: "+temp.datos.img_color_sinM+"\"];\n";
            if (contador != 5 && temp.next != null) {
                conexiones += "C"+temp.datos.hashCode()+" -> C"+temp.next.datos.hashCode()+";\n";
            }
            contador += 1;
            temp = temp.next;
        }
        result += "//Agrendo Nodos\n"+nodos+"\n";
        result += "//Agregando conexiones\n\n"+conexiones+"\n}\nrankdir = RL\n}";

        String ubicacion_dot = "C:\\Users\\denni\\Documents\\Varios_Progra\\EDD_PROYECTO_FASE1\\Proyecto_fase1\\src\\graphviz\\top5color.dot";
        String ubicacion_img = "C:\\Users\\denni\\Documents\\Varios_Progra\\EDD_PROYECTO_FASE1\\Proyecto_fase1\\src\\graphviz\\top5color.png";
        File dotFile = new File(ubicacion_dot);
        try {
            dotFile.createNewFile();
        } catch (IOException e) {
            System.out.println("Ocurrio un error creando el archivo");
            e.printStackTrace();
        }        
        FileWriter writer;
        try {
            writer = new FileWriter(ubicacion_dot);
            writer.write(result);
            writer.close();
        } catch (IOException e) {
            System.out.println("Ocurrio un error escribiendo el archivo");
            e.printStackTrace();
        }
        String comando = "dot -Tpng " + ubicacion_dot + " -o " + ubicacion_img;
        ProcessBuilder builder = new ProcessBuilder("cmd.exe","/c"+comando);
        builder.redirectErrorStream(true);
        try {
            Process p = builder.start();
        } catch (IOException e) {e.printStackTrace();}
        System.out.println("Top 5 clientes con mas imagenes a color graficado.");

    }

    public void sort_by_imgBN(){
        Node current = head, index = null;
        ColaRecepcion.Node temp = head.datos;
        if (head == null) {
            return;
        }else{
            while (current != null) {
                index = current.next;
                while (index != null) {
                    if (current.datos.img_bw_sinM > index.datos.img_bw_sinM) {
                        temp = current.datos;
                        current.datos = index.datos;
                        index.datos = temp;
                    }
                    index = index.next;
                }
                current = current.next;
            }
        }
        imprimir_5_bn();
    }
    public void imprimir_5_bn() {
        String result = "digraph G{\nlabelloc=\"t\";\nlabel=<<B>TOP 5 CLIENTES CON MENOS IMAGENES A BLANCO Y NEGRO</B>>;\nbgcolor=\"#50FAA3\";\n"+
        "graph[splines=ortho, nodesep=1];\nnode[shape=box fillcolor=\"#8BDE47\" style=filled];\n"+
        "edge[arrowsize=0.6];\nsubgraph clushter{\nstyle=invis;";
        String conexiones = "";
        String nodos = "";
        Node temp = head;
        int contador = 1;
        while (temp != null && contador <= 5) {
            nodos += "C"+temp.datos.hashCode()+"[label=\"No "+contador+"\\nCliente "+temp.datos.id_cliente+"\\nImagenes: "+temp.datos.img_bw_sinM+"\"];\n";
            if (contador != 5 && temp.next != null) {
                conexiones += "C"+temp.datos.hashCode()+" -> C"+temp.next.datos.hashCode()+";\n";
            }
            contador += 1;
            temp = temp.next;
        }
        result += "//Agrendo Nodos\n"+nodos+"\n";
        result += "//Agregando conexiones\n\n"+conexiones+"\n}\nrankdir = RL\n}";

        String ubicacion_dot = "C:\\Users\\denni\\Documents\\Varios_Progra\\EDD_PROYECTO_FASE1\\Proyecto_fase1\\src\\graphviz\\top5bn.dot";
        String ubicacion_img = "C:\\Users\\denni\\Documents\\Varios_Progra\\EDD_PROYECTO_FASE1\\Proyecto_fase1\\src\\graphviz\\top5bn.png";
        File dotFile = new File(ubicacion_dot);
        try {
            dotFile.createNewFile();
        } catch (IOException e) {
            System.out.println("Ocurrio un error creando el archivo");
            e.printStackTrace();
        }        
        FileWriter writer;
        try {
            writer = new FileWriter(ubicacion_dot);
            writer.write(result);
            writer.close();
        } catch (IOException e) {
            System.out.println("Ocurrio un error escribiendo el archivo");
            e.printStackTrace();
        }
        String comando = "dot -Tpng " + ubicacion_dot + " -o " + ubicacion_img;
        ProcessBuilder builder = new ProcessBuilder("cmd.exe","/c"+comando);
        builder.redirectErrorStream(true);
        try {
            Process p = builder.start();
        } catch (IOException e) {e.printStackTrace();}
        System.out.println("Top 5 clientes con menos imagenes a blanco y negro graficado.");
    }

    public void sort_by_id(){
        Node current = head, index = null;
        ColaRecepcion.Node temp = head.datos;
        if (head == null) {
            return;
        }else{
            while (current != null) {
                index = current.next;
                while (index != null) {
                    if (current.datos.id_cliente < index.datos.id_cliente) {
                        temp = current.datos;
                        current.datos = index.datos;
                        index.datos = temp;
                    }
                    index = index.next;
                }
                current = current.next;
            }
        }
    }

    public void buscar_cliente_especifico(int id_cliente) {
        Node temp = head;
        while (temp != null && temp.datos.id_cliente != id_cliente) {
            temp = temp.next;
        }
        if (temp != null) {
            String result = "digraph G{\nbgcolor=\"#50FAA3\"\nnode[shape=note fillcolor=\"#8BDE47\" style=filled];\n";
            result += "subgraph cluster_p{\nlabel=<<B>Datos de Cliente Especifico</B>>;\n";
            String nodos = "C"+temp.datos.hashCode()+"[label=\"Cliente "+temp.datos.id_cliente+"\\n"+temp.datos.nombre_cliente+"\\nImagenes: "+temp.datos.cant_imagenes+"\\nBN "+temp.datos.img_bw_sinM+" Color "+temp.datos.img_color_sinM+"\\nPasos: "+temp.datos.pasos+"\\nVentanilla: "+temp.datos.ventanilla+"\"];\n";
            nodos += "}\n}";
            result += nodos;
            String ubicacion_dot = "C:\\Users\\denni\\Documents\\Varios_Progra\\EDD_PROYECTO_FASE1\\Proyecto_fase1\\src\\graphviz\\especifico.dot";
            String ubicacion_img = "C:\\Users\\denni\\Documents\\Varios_Progra\\EDD_PROYECTO_FASE1\\Proyecto_fase1\\src\\graphviz\\especifoc.png";
            File dotFile = new File(ubicacion_dot);
            try {
                dotFile.createNewFile();
            } catch (IOException e) {
                System.out.println("Ocurrio un error creando el archivo");
                e.printStackTrace();
            }        
            FileWriter writer;
            try {
                writer = new FileWriter(ubicacion_dot);
                writer.write(result);
                writer.close();
            } catch (IOException e) {
                System.out.println("Ocurrio un error escribiendo el archivo");
                e.printStackTrace();
            }
            String comando = "dot -Tpng " + ubicacion_dot + " -o " + ubicacion_img;
            ProcessBuilder builder = new ProcessBuilder("cmd.exe","/c"+comando);
            builder.redirectErrorStream(true);
            try {
                Process p = builder.start();
            } catch (IOException e) {e.printStackTrace();}
            System.out.println("Cliente encontrado y graficado.");
        }
    }

    public void print_list() {
        Node temp = head;
        while (temp != null) {
            System.out.println("ID: "+temp.datos.id_cliente+"; Nombre: "+temp.datos.nombre_cliente+"; Cant. Img: "+temp.datos.cant_imagenes+"; BN: "+temp.datos.img_bw_sinM+"; Color: "+temp.datos.img_color_sinM+"; Pasos: "+temp.datos.pasos);
            temp = temp.next;
        }
    }
}