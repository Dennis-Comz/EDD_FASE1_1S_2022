package proyecto_fase1;

public class ListaImagenes {
    class Node{
        String tipo;
        Node next;
        public Node(String tipo){
            this.tipo = tipo;
            this.next = null;
        }
    }
    private Node head;
    private int index = 0;

    public void append(String tipo){
        Node newNode = new Node(tipo);
        index += 1;
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

    public int get_index() {
        return index;
    }

    public Node get_node(){
        return head;
    }

    public boolean is_empty() {
        if (head != null) {
            return false;
        }
        return true;
    }

    public void print_list() {
        Node temp = head;
        while (temp != null) {
            System.out.println("Imagen: " + temp.tipo);
            temp = temp.next;
        }
    }
}
