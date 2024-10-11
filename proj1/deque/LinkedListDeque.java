package deque;

/*import java.util.Arrays;
import java.util.Iterator;
import java.util.Objects;*/
//import edu.princeton.cs.algs4.ST;
//import net.sf.saxon.functions.ConstantFunction;

//import java.util.Deque;

import java.util.Iterator;

public class LinkedListDeque<T> implements Deque<T>, Iterable<T> {
    //@SuppressWarnings("unchecked")
    private StuffNode sentinelFirst;
    private StuffNode sentinelBack;
    private int size;

    //定义每个节点
    private class StuffNode {
        private StuffNode prev;
        private T item;
        private StuffNode next;

        StuffNode(StuffNode n, T i, StuffNode m) {
            prev = n;
            item = i;
            next = m;
        }
    }

    //creat A LinkedListDeque
    /*public LinkedListDeque(T i) {
        sentinelFirst = new StuffNode(null, null, null);
        sentinelBack = new StuffNode(null, null, null);
        StuffNode currentNode = new StuffNode(sentinelFirst, i, sentinelBack);
        sentinelFirst.next = currentNode;
        sentinelBack.prev = currentNode;
        size = 1;
    }*/
    public LinkedListDeque() {
        sentinelFirst = new StuffNode(null, null, null);
        sentinelBack  = new StuffNode(null, null, null);
        sentinelBack.prev = sentinelFirst;
        sentinelFirst.next = sentinelBack;
        size = 0;
    }

    public void addFirst(T item) {
        StuffNode oldfirstNode = sentinelFirst.next;
        StuffNode currentNode = new StuffNode(sentinelFirst, item, oldfirstNode);
        //这个时候sentinelFirst.next 应该还是指向原来的第一个Node
        oldfirstNode.prev = currentNode;
        sentinelFirst.next = currentNode;
        size += 1;
    }

    public void addLast(T item) {
        StuffNode oldlastNode = sentinelBack.prev;
        StuffNode currentNode = new StuffNode(oldlastNode, item, sentinelBack);
        oldlastNode.next = currentNode;
        sentinelBack.prev = currentNode;
        size++;
    }
//    public boolean isEmpty(){
//        return size == 0;
//    }
    public int size() {
        return size;
    }
    public T removeFirst() {
        if (isEmpty()) {
            return null;
        }
        /*StuffNode SecondNode = sentinelFirst.next.next;
        SecondNode.prev = sentinelFirst;
        sentinelFirst.next = SecondNode;
        size --;
        return*/ //这边发现不知道如何return被删除的节点，故改变逻辑重写
        StuffNode firstNode = sentinelFirst.next;
        T removedItem = firstNode.item;
        sentinelFirst.next = firstNode.next;
        firstNode.next.prev = sentinelFirst;
        size--;
        return removedItem;
    }

    public T removeLast() {
        if (isEmpty()) {
            return null;
        }
        StuffNode lastNode = sentinelBack.prev;
        T removedItem = lastNode.item;
        sentinelBack.prev = lastNode.prev;
        lastNode.prev.next = sentinelBack;
        size--;
        return removedItem;
    }
    public T get(int index) {
        if (index < 0 || index >= size) {
            return null;
        }
        StuffNode p = sentinelFirst.next;
        for (int i = 0; i < index; i++) {
            p = p.next;
        }

        return p.item;
    }


    public T getRecursive(int index) {
        if (index < 0 || index >= size) {
            return null;
        }
        return getRecursiveHelper(sentinelFirst.next, index);
    }

    /**
     * A helper method that helps getRecursive function.
     * @param node
     * @param index
     * @return
     */
    private T getRecursiveHelper(StuffNode node, int index) {
        if (index == 0) {
            return node.item;
        }
        return getRecursiveHelper(node.next, index - 1);
    }
    //two special method
    /*public Iterable<T> iterator(){
        StuffNode currentNode = sentinelFirst.next;
        while(currentNode.next != sentinelBack){
            yield currentNode;
            currentNode = currentNode.next;
        }
    }*/
    //发现目前的水平不足以支持我写完，放弃

    public Iterator<T> iterator() {
        return new LinkedListDequeIterator();
    }

    private class LinkedListDequeIterator implements Iterator<T> {
        private StuffNode p;

        LinkedListDequeIterator() {
            p = sentinelFirst.next;
        }

        public boolean hasNext() {
            return p != sentinelBack;
        }

        public T next() {
            T item = p.item;
            p = p.next;
            return item;
        }
    }
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }
        if (this == o) {
            return true;
        } //means they are actually the same thing
        if (!(o instanceof Deque)) {
            return false;
        }
        Deque<?> ad = (Deque<?>) o;
        if (ad.size() != size) {
            return false;
        }
        for (int i = 0; i < size; i++) {
            if (ad.get(i) != get(i)) {
            //if (!Object.equals(ad.get(i), get(i)))
                return false;
            }
        }
        return true;
    }

    public void printDeque() {
        StuffNode p = sentinelFirst.next;
        for (int i = 0; i < size; i++) {
            System.out.print(p.item);
            p = p.next;
            if (i != size - 1) {
                System.out.print(" ");
            } else {
                System.out.println();
            }
        }
    }

}
