package java;

import java.io.Serializable;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

public class RedixTree <V extends Serializable> implements Map<String, V>, Serializable {

    public static final String KCBN="key cannot be null";
    public static final String KMBSI="key must be String instances";

    //root node
    RedixTreeNode<V> root;

    //default constructor
    public RedixTree() {
        this.root=new RedixTreeNode<>("");
    }

    public void visit(RedixTreeVisitor<V, ?> visitor){

        visit(root,"" ,"",visitor);
    }

    public void visit(RedixTreeVisitor<V,?> visitor,String prefix){

        visit(root,prefix,"",visitor);
    }


    private void visit(RedixTreeNode<V> node,String prefixAllowed,String prefix){

        if (node.)
    }




    @Override
    public int size() {
        return 0;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public boolean containsKey(Object key) {
        return false;
    }

    @Override
    public boolean containsValue(Object value) {
        return false;
    }

    @Override
    public V get(Object key) {
        return null;
    }

    @Override
    public V put(String key, V value) {
        return null;
    }

    @Override
    public V remove(Object key) {
        return null;
    }

    @Override
    public void putAll(Map<? extends String, ? extends V> m) {

    }

    @Override
    public void clear() {

    }

    @Override
    public Set<String> keySet() {
        return null;
    }

    @Override
    public Collection<V> values() {
        return null;
    }

    @Override
    public Set<Entry<String, V>> entrySet() {
        return null;
    }
}
