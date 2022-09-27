package proyecto_fase1;

public class PilaImagenes {
    class Node{
        int id_cliente;
        boolean img_color;
        boolean img_bw;
        Node next;
        public Node(int id_cliente, boolean img_color, boolean img_bw){
            this.id_cliente = id_cliente;
            this.img_color = img_color;
            this.img_bw = img_bw;
            this.next = null;
        }
    }

    private Node head;

    public void push(int id, boolean color, boolean bw){
        Node newNode = new Node(id, color, bw);
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

    public Node pop(){
        if (head == null) {
            return null;
        }else{
            Node temp = head;
            head = temp.next;
            return temp;
        }
    }
    
    public int cant_imagenes(int id_cliente) {
        Node temp = head;
        int contador = 0;
        while (temp != null) {
            if (temp.id_cliente == id_cliente) {
                contador += 1;
            }
            temp = temp.next;
        }
        return contador;
    }

    public Node get_nodo(){
        return head;
    }
    
    public boolean is_empty() {
        if (head == null) {
            return true;
        }
        return false;
    }
    public void print_images() {
        Node temp = head;
        while (temp != null) {
            System.out.println("ID_Cliente: " + temp.id_cliente + "; Color: " + temp.img_color + "; BW: " + temp.img_bw);
            temp = temp.next;
        }
    }
}
