package proyecto_fase1;

public class ColaApoyo {
    class Node{
        ColaRecepcion.Node cliente;
        Node next;
        public Node(ColaRecepcion.Node cliente) {
            this.cliente = cliente;
        }
    }
    private Node head;
    private int index = 0;

    public void push(ColaRecepcion.Node cliente) {
        Node newNode = new Node(cliente);
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

    public ColaRecepcion.Node pop(){
        if (head == null) {
            return null;
        }else{
            Node temp = head;
            head = temp.next;
            index -= 1;
            return temp.cliente;
        }
    }

    public boolean is_empty() {
        Node temp = head;
        if (temp != null) {
            return false;
        }
        return true;
    }

    public int get_cantidad() {
        return index;
    }
}
