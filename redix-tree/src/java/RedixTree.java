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


    private void visit(RedixTreeNode<V> node,String prefixAllowed,String prefix,RedixTreeVisitor<V,?> visitor){

        if (node.hasValue() && prefix.startsWith(prefixAllowed)){
            visitor.visit(prefix,node.getValue());
        }

        for(RedixTreeNode<V> child :node){

            final int prefixlen=prefix.length();
            final String newPrefix=prefix+child.getPrefix();

            if(prefixAllowed.length()<=prefixlen||
            newPrefix.length()<=prefixlen||
            newPrefix.charAt(prefixlen)==prefixAllowed.charAt(prefixlen)){

                visit(child,prefixAllowed,newPrefix,visitor);
            }
        }
    }







    @Override
    public int size() {
        return 0;
    }

    @Override
    public boolean isEmpty() {
        return root.getChildren().isEmpty();
    }

    @Override
    public boolean containsKey(final Object keyToCheck) {

        if(keyToCheck==null){
            throw new NullPointerException(KCBN);
        }
        if(!(keyToCheck instanceof String)){
            throw new ClassCastException(KMBSI);
        }

        RedixTreeVisitor<V,Boolean> visitor=new RedixTreeVisitor<V, Boolean>() {
            boolean found=false;

            @Override
            public void visit(String key, V value) {
                if(key.equals(keyToCheck)){
                    found=true;
                }
            }

            @Override
            public Boolean getResult() {
                return found;
            }
        };
        visit(visitor,(String)keyToCheck);
        return visitor.getResult();
    }

    @Override
    public boolean containsValue(final Object val) {

        RedixTreeVisitor<V,Boolean> visitor=new RedixTreeVisitor<V, Boolean>() {

            boolean found=false;
            @Override
            public void visit(String key, V value) {
                if(val==value||value!=null&&value.equals(val)){
                    found=true;
                }
            }

            @Override
            public Boolean getResult() {
                return found;
            }
        };
        visit(visitor);
        return visitor.getResult();
    }

    @Override
    public V get(final Object keyToCheck) {

        if(keyToCheck==null){
            throw new NullPointerException(KCBN);
        }
        if(!(keyToCheck instanceof String)){
            throw new ClassCastException(KMBSI);
        }

        RedixTreeVisitor<V,V> visitor=new RedixTreeVisitor<V, V>() {

            V result=null;
            @Override
            public void visit(String key, V value) {
                if(key.equals(keyToCheck)){
                    result=value;
                }
            }

            @Override
            public V getResult() {
                return result;
            }
        };

        visit(visitor,(String)keyToCheck);
        return visitor.getResult();
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

        root.getChildren().clear();
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
