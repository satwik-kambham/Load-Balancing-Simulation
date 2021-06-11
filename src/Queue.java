import java.util.Iterator;
import java.util.NoSuchElementException;

public class Queue implements Iterable<Info> {
    private Node first;
    private Node last;
    private int n;
    private int slowness;
    public int processTime;

    private static class Node {
        private Info item;
        private Node next;
    }

    public Queue(int _slowness) {
        first = null;
        last = null;
        n = 0;
        processTime = 0;
        slowness = _slowness;
    }

    public boolean isEmpty() {
        return first == null;
    }

    public int size() {
        return n;
    }

    public Info peek() {
        if (isEmpty()) return null;
        return first.item;
    }

    public void enqueue(Info item) {
        Node oldlast = last;
        last = new Node();
        last.item = item;
        last.next = null;
        if (isEmpty()) first = last;
        else oldlast.next = last;
        n++;
        processTime += item.delay * slowness;
    }

    public Info dequeue() {
        if (isEmpty()) return null;
        Info item = first.item;
        first = first.next;
        n--;
        if (isEmpty()) last = null;   // to avoid loitering
        processTime -= item.delay * slowness;
        return item;
    }

    public String toString() {
        StringBuilder s = new StringBuilder();
        for (Info item : this) {
            s.append(item);
            s.append(' ');
        }
        return s.toString();
    }

    public Iterator<Info> iterator() {
        return new LinkedIterator(first);
    }

    private static class LinkedIterator implements Iterator<Info> {
        private Node current;

        public LinkedIterator(Node first) {
            current = first;
        }

        public boolean hasNext() {
            return current != null;
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }

        public Info next() {
            if (!hasNext()) throw new NoSuchElementException();
            Info item = current.item;
            current = current.next;
            return item;
        }
    }

}
