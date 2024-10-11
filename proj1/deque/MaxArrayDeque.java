package deque;
//import net.sf.saxon.functions.Minimax;

import java.util.Comparator;

//从别人的answer中得知: public class MaxArrayDeque<T> extends ArrayDeque<T>可以避免重复的的代码——继承
public class MaxArrayDeque<T> {
    private int size;
    private T[] items = (T[]) new Object[8];
    private int nextFirst;
    private int nextLast;
    private Comparator<T> comparator;
    public MaxArrayDeque(Comparator<T> c) {
        nextFirst = 4;
        nextLast = 5;
        comparator = c;
    }

    public void addFirst(T item) {
        if (size == items.length) {
            resize(size * 2);
        }
        items[nextFirst] = item;
        nextFirst = (nextFirst - 1 + items.length) % items.length;
        size += 1;
    }
    public void addLast(T item) {
        if (size == items.length) {
            resize(size * 2);
        }
        items[nextLast] = item;
        nextLast = (nextLast + 1 + items.length) % items.length;
        size += 1;
    }
    public boolean isEmpty() {
        return size == 0;
    }
    public int size() {
        return size;
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
    public T removeFirst() {
        nextFirst = (nextFirst + 1) % items.length;
        T removedItem = items[nextFirst];
        items[nextFirst] = null;
        size -= 1;
        shrinkSize();
        return removedItem;
    }
    public T removeLast() {
        nextLast = (nextLast - 1 + items.length) % items.length;
        T removeItem = items[nextLast];
        items[nextLast] = null;
        size -= 1;
        shrinkSize();
        return removeItem;
    }
    public T get(int index) {
        if (index < 0 || index >= size) {
            return  null;
        }
        int numberIndex = (nextFirst + 1 + index) % items.length;
        return items[numberIndex];
    }
    public void shrinkSize() {
        if (items.length / 4 > size && size >= 4) {
            resize(items.length / 4);
        } else if (isEmpty()) {
            resize(8);
        }
    }
    public void resize(int capacity) {
        T[] newItems = (T[]) new Object[capacity];
        for (int i = 0; i < size; i++) {
            newItems[i] = get(i);
        }
        nextFirst = capacity - 1;
        nextLast = size;
        items = newItems;
    }
    public T max(Comparator<T> c) {
        if (isEmpty()) {
            return null;
        }
        int maxIndex = 0;
        for (int i = 1; i < size; i++) {
            if (comparator.compare(get(i), get(maxIndex)) > 0) {
                maxIndex = i;
            }
        }
        return get(maxIndex);
    }
    public T max() {
        return max(comparator);
    }
}
