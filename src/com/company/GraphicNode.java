package com.company;

import java.util.AbstractMap;

public class GraphicNode<K, V> {
    private int x, y;
    private AbstractMap.SimpleEntry<K, V> value;
    private GraphicNode parent;
    private Type type;
    int level;

    public GraphicNode(Object value, GraphicNode parent, Type type, int level) {
        this.value = (AbstractMap.SimpleEntry)value;
        this.parent = parent;
        this.type = type;
        this.level = level;
    }

    public Type getType() {
        return type;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public AbstractMap.SimpleEntry<K, V> getValue() {
        return value;
    }

    public GraphicNode getParent() {
        return parent;
    }

    enum Type {
        VALUE, POINTER
    }

    public int getLevel() {
        return level;
    }
//
//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (o == null || getClass() != o.getClass()) return false;
//
//        GraphicNode<?, ?> that = (GraphicNode<?, ?>) o;
//
//        if (x != that.x) return false;
//        if (y != that.y) return false;
//        if (level != that.level) return false;
//        if (value != null ? !value.equals(that.value) : that.value != null) return false;
//        if (parent != null ? !parent.equals(that.parent) : that.parent != null) return false;
//        return type == that.type;
//
//    }
//
//    @Override
//    public int hashCode() {
//        int result = x;
//        result = 31 * result + y;
//        result = 31 * result + (value != null ? value.hashCode() : 0);
//        result = 31 * result + (parent != null ? parent.hashCode() : 0);
//        result = 31 * result + (type != null ? type.hashCode() : 0);
//        result = 31 * result + level;
//        return result;
//    }
}