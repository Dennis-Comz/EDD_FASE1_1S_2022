package proyecto_fase1;

public class QueueBwPrinter {
    class Node{
        int id_cliente;
        Node next;
        public Node(int id_cliente){
            this.id_cliente = id_cliente;
            this.next = null;
        }
    }

    private Node head;
    public void push(int id_cliente) {
        Node newNode = new Node(id_cliente);
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

    public boolean puedo_entregar() {
        if (head != null) {
            return true;
        }
        return false;
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

    public Node get_node() {
        return head;
    }
    
    public void print_queue() {
        Node temp = head;
        System.out.println("Cola de imagenes a Blanco y Negro");
        while (temp != null) {
            System.out.println("ID: " + temp.id_cliente);
            temp = temp.next;
        }
    }
}