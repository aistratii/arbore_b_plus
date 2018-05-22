package com.company;

import java.util.AbstractMap;
import java.util.Arrays;
import java.util.Map;

public class Main {

    public static void main(String args[]){

        Node node = new Node<String>(2);
        node.add(2, "doi");
        node.add(1, "unu");
        node.add(4, "patru");
        node.add(5, "cinci");
        node.add(3, "trei");

        System.out.println(node);
        //NodeUtils.display(node);

        System.out.println(node.read(6));
    }

}


class Node<V> {
    private Object[] elements;
    private final int originalLength;
    private boolean isInit;
    private Node<V> parent;

    public Node(int length){
        elements = new Object[length + length +1];
        this.originalLength = length;
    }

    private Node(int length, Node parent){
        elements = new Object[length + length +1];
        this.originalLength = length;
        this.parent = parent;
    }

    public void add(int key, V value){
        if (isRoot() && !isFilled()){
            placeElement(key, value);
        }

        else if (isFilled()){
            for (int i = 1; i < elements.length-1; i+=2)
                if (i == 1 && key < ((AbstractMap.SimpleEntry<Integer, V>)elements[i]).getKey()){
                    //insert left to the first element
                    if (elements[i -1] == null) elements[i -1] = new Node(originalLength, this);
                    ((Node<V>)elements[i-1]).add(key, value);
                    break;
                } else if (i == elements.length-2 && key > ((AbstractMap.SimpleEntry<Integer, V>)elements[i]).getKey()){
                    //insert right to the last element
                    if (elements[i+1] == null) elements[i+1] = new Node(originalLength, this);
                    ((Node<V>)elements[i+1]).add(key, value);
                    break;
                } else {
                    //insert between nodes
                    if (((Map.Entry<Integer, V>)elements[i]).getKey() < key && key < ((Map.Entry<Integer, V>)elements[i + 2]).getKey()) {
                        if (elements[i+1] == null) elements[i+1] = new Node<V>(originalLength);
                        ((Node) elements[i + 1]).add(key, value);
                        break;
                    }
                }
        }

        else if (!isRoot() && !isFilled()){
            placeElement(key, value);
        }
    }

    private void placeElement(int key, V value) {
        for (int i = 1; i < elements.length; i+=2)
            if (elements[i] == null){
                elements[i] = new AbstractMap.SimpleEntry<Integer, V>(key, value);
                break;
            } else if (((AbstractMap.SimpleEntry<Integer, V>)elements[i]).getKey() > key){
                //shift to right
                for (int j = elements.length-1; j > i; j--)
                    elements[j] = elements[j-2];
                elements[i] = new AbstractMap.SimpleEntry<>(key, value);
                break;
            }
    }

    public void delete(Float f){
        throw new UnsupportedOperationException();
    }

    public V read (Integer key){
        try {
            return read(key, elements);
        } catch(NullPointerException ex){}

        throw new RuntimeException("No element with key [" + key + "] found");
    }

    private V read(Integer key, Object[] elements) {
        for (int i = 1; i < elements.length - 1; i+=2)
            if (elements[i] != null && key < ((Map.Entry<Integer, V>)elements[i]).getKey())
                return read(key, ((Node)elements[i - 1]).getElements());
            else if (elements[i] != null && key == ((Map.Entry<Integer, V>)elements[i]).getKey())
                return ((Map.Entry<Integer, V>)elements[i]).getValue();
            else if (elements[i] != null && i >= elements.length - 2)
                return read(key, ((Node)elements[i + 1]).getElements());

        throw new RuntimeException("No element with key [" + key + "] found");
    }

    private boolean isRoot(){
        return parent == null;
    }

    @Override
    public String toString() {
        return "Node{" +
                "elements=" + Arrays.toString(elements) +
                '}';
    }

    public boolean isFilled() {
        int fill = 0;

        for (int i = 1; i < elements.length; i+=2)
            if (elements[i] != null)
                fill++;

        return fill == originalLength;
    }


    public Object[] getElements() {
        return elements;
    }
}
