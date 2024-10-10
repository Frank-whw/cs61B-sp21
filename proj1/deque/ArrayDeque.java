package deque;
import edu.princeton.cs.algs4.ST;
import net.sf.saxon.functions.ConstantFunction;

import java.util.Deque;
public class ArrayDeque<T>{
    T[] items;
    int size;
    //creat an empty ArrayDeque
    public ArrayDeque(){
        items =(T[]) new Object[8];
        size = 0;
    }
    /*
    public ArrayDeque(int x){
        items = new int[8];
        items[0] = x;
        size = 1;
    }
    */
    public void addLast(T x){
        if(size == items.length){
            resize(size * 2);
        }
        items[size] = x;
        size ++;
    }

    public T getLast(){
        return items[size - 1];
    }

    public T get(int i){
        return items[i];
    }
    public int size(){
        return size;
    }
    public T removeLast(){
        T removedItem = getLast();
        items[size - 1] = null;
        size --;
        return removedItem;
    }
    private void resize(int capacity){
        if(capacity >= 16){
            if(size < capacity / 4){
                capacity = capacity / 4;
            }
        }
        int[] a = new int[capacity];
        System.arraycopy(items, 0 , a, 0, size);
    }
}