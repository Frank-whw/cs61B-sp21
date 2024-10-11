package deque;
//import edu.princeton.cs.algs4.ST;
//import net.sf.saxon.functions.ConstantFunction;

public class ArrayDeque<T> implements Deque<T> {
    private T[] items = (T[]) new Object[8];
    private int size;
    private int nextFirst;
    private int nextLast;
    //creat an empty ArrayDeque
    public ArrayDeque() {
        size = 0;
        nextFirst = 4;
        nextLast = 5;
    }

    public ArrayDeque(T x) {
        items[4] = x;
        nextFirst = 3;
        nextLast = 5;
        size = 1;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public void addFirst(T x) {
        if (size == items.length) {
            resize(size * 2);
        }
        items[nextFirst] = x;
        size++;
        nextFirst = (nextFirst - 1 + items.length) % items.length;

    }
    public void addLast(T x) {
        //make sure that the length is enough
        if (size == items.length) {
            resize(size * 2);
        }
        items[nextLast] = x;
        nextLast = (nextLast + 1) % items.length;
        size++;
    }

    public T getLast() {
        return items[nextLast - 1];
    }
    public T get(int i) {
        if (i < 0 || i >= size) {  // 使用 && 来确保索引合法
            return null;
        }
        return items[(nextFirst + 1 + i) % items.length];  // 环绕索引
    }

    public int size() {
        return size;
    }
    public T removeFirst() {
        if (isEmpty()) {
            return null;
        }
        nextFirst = (nextFirst + 1) % items.length;
        T removedItem = items[nextFirst];
        items[nextFirst] = null;
        size -= 1;
        shrinkSize();
        return removedItem;
    }
    public T removeLast() {
        if (isEmpty()) {
            return null;
        }
        nextLast = (nextLast - 1 + items.length) % items.length;
        T removedItem = items[nextLast];
        items[nextLast] = null;
        size--;
        shrinkSize();
        return removedItem;
    }
    private void shrinkSize() {
        if (isEmpty()) {
            resize(8);
        } else if (items.length / 4 > size && size >= 4) {
            resize(items.length / 2);
        }

    }
    private void resize(int capacity) {
        T[] a = (T[]) new Object[capacity];
        for (int i = 0; i < size; i++) {
            a[i] = get(i);
        }
        nextFirst = capacity - 1;
        nextLast = size;
        items = a;
    }
    public void printDeque() {
        for (int i = 0; i < size; i++) {
            System.out.print(get(i));
            if (i != size - 1) {
                System.out.print(" ");
            } else {
                System.out.println();
            }
        }
    }
}
