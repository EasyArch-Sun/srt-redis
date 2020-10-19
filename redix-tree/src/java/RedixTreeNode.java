package java;

import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;
import java.util.TreeSet;

public class RedixTreeNode<V extends Serializable> implements Iterable<RedixTreeNode<V>>,
    Comparable<RedixTreeNode<V>>,Serializable{

    //node prefix
    private String prefix;

    //node value
    private V value;

    //node 是否有值(RedisTreeVistor)
    private boolean hasValue;

    //在这用TreeSet，被RadixTree遍历
    private Collection<RedixTreeNode<V>> children;


    //prefix value
    RedixTreeNode(String prefix, V value) {

        this.prefix=prefix;
        this.value=value;
        this.hasValue=true;
    }

    //prefix
    RedixTreeNode(String prefix){

        this(prefix,null);
        this.hasValue=false;
    }


    V getValue(){
        return value;
    }

    void setValue(V value){
        this.value=value;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public boolean isHasValue() {
        return hasValue;
    }

    public void setHasValue(boolean hasValue) {
        this.hasValue = hasValue;

        if(!hasValue){
            this.value=null;
        }
    }

    public Collection<RedixTreeNode<V>> getChildren() {

        //延迟创建子代以减少内存开销
        if(children==null){
            children=new TreeSet<RedixTreeNode<V>>();
        }

        return children;
    }

    public void setChildren(Collection<RedixTreeNode<V>> children) {
        this.children = children;
    }

    @Override
    public int compareTo(RedixTreeNode<V> node) {
        int result=prefix.toString().compareTo(node.getPrefix().toString());
        return result;
    }

    @Override
    public Iterator<RedixTreeNode<V>> iterator() {

        if(children==null){
            return new Iterator<RedixTreeNode<V>>() {

                @Override
                public boolean hasNext() {
                    return false;
                }

                @Override
                public RedixTreeNode<V> next() {
                    return null;
                }

                @Override
                public void remove() {

                }
            };
        }
        return children.iterator();
    }
}
