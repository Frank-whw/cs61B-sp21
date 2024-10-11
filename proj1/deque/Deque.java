package deque;

public interface Deque<T> {
    public void addFirst(T x);
    public void addLast(T x);
    default boolean isEmpty(){
        return size() == 0;
    }
    public int size();
    public void printDeque();
    public T removeFirst();
    public T removeLast();
    public T get(int index);
}
