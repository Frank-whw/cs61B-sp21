package bstmap;

import edu.princeton.cs.algs4.BST;

import java.util.*;

public class BSTMap<K extends Comparable<K>, V> implements Map61B<K, V> {
    @Override
    public Iterator<K> iterator() {
        return null;
    }

    private class BSTNode{
        private K key;
        private V val;
        private int size;
        private BSTNode left, right;
        //左边的小
        public BSTNode(K key, V val, int size){
            this.key = key;
            this.val = val;
            this.size = size;
        }

    }
    private BSTNode root;
    /** Removes all of the mappings from this map. */
    @Override
    public void clear(){
        root = null;
    }

    /* Returns true if this map contains a mapping for the specified key. */
    @Override
    public boolean containsKey(K key){
        return getNode(root, key) != null;
    }

    private BSTNode getNode(BSTNode root, K key) {
        if (root == null){
            return null;
        }
        int cmp = key.compareTo(root.key);
        if (cmp < 0) {
            // 说明key小于当前root的值
            return getNode(root.left, key);
        } else if (cmp == 0){
            return root;
        } else {
            return getNode(root.right, key);
        }
    }

    /* Returns the value to which the specified key is mapped, or null if this
     * map contains no mapping for the key.
     */
    @Override
    public V get(K key){
        BSTNode node = getNode(root, key);
        return node == null ? null : node.val;
    }

    /* Returns the number of key-value mappings in this map. */
    @Override
    public int size() {
        return root == null ? 0 : root.size;
    }


    /* Associates the specified value with the specified key in this map. */
    @Override
    public void put(K key, V value){
        root = put(root, key, value);
    }

    private BSTNode put(BSTNode node, K key, V value) {
        if (node == null) {
            return new BSTNode(key, value,1);
        }
        int cmp = key.compareTo(node.key);
        if (cmp < 0) {
            node.left = put(node.left, key, value);
            node.size += 1;
        } else if (cmp > 0) {
            node.right = put(node.right, key, value);
            node.size += 1;
        } else {
            node.val = value;
        }
        return node;

    }

    @Override
    public Set<K> keySet() {
        return null;
    }

    @Override
    public V remove(K key) {
        return null;
    }

    @Override
    public V remove(K key, V value) {
        return null;
    }

    public void printInOrder(){
        BSTNode current = root;
        if (root != null) {
            System.out.print(root.val);
        }
        while (current != null) {
            System.out.print(  "->" + root.val);
        }
        System.out.println();
    }


}