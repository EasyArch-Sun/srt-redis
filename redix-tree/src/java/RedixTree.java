package java;

import java.io.Serializable;
import java.util.*;

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



    public List<Map.Entry<String,V>> getEntriesWithPrefix(String prefix){
        RedixTreeVisitor<V,List<Map.Entry<String,V>>> visitor=new RedixTreeVisitor<V, List<Map, Entry<String, V>>>() {

            List<Map.Entry<String,V>> result=new ArrayList<Map.Entry<String,V>>();

            @Override
            public void visit(String key, V value) {
                result.add(new AbstractMap.SimpleEntry<String,v>(key,value));
            }

            @Override
            public List<Map.Entry<String,V>> getResult() {
                return result;
            }
        };

        visit(visitor,prefix);
        return visitor.getResult();

    }


    public List<V> getValuesWithPrefix(String prefix){

        if(prefix==null){
            throw new NullPointerException("prefix cannot be null");
        }

        RedixTreeVisitor<V,List<V>> visitor=new RedixTreeVisitor<V, List<V>>() {

            List<V> result=new ArrayList<V>();

            @Override
            public void visit(String key, V value) {
                result.add(value);
            }

            @Override
            public List<V> getResult() {
                return result;
            }
        };

        visit(visitor,prefix);
        return visitor.getResult();
    }



    public List<String> getKeysWithPrefix(String prefix){

        if (prefix==null){

            throw new NullPointerException("prefix cannot be null");
        }

        RedixTreeVisitor<V,List<String>> visitor=new RedixTreeVisitor<V, List<String>>() {

            List<String> result=new ArrayList<String>();

            @Override
            public void visit(String key, V value) {
                result.add(key);

            }

            @Override
            public List<String> getResult() {
                return result;
            }
        };

        visit(visitor,prefix);
        return visitor.getResult();
    }






    @Override
    public int size() {

        RedixTreeVisitor<V,Integer> visitor=new RedixTreeVisitor<V, Integer>() {

            int count =0;
            @Override
            public void visit(String key, V value) {
                ++count;
            }

            @Override
            public Integer getResult() {
                return count;
            }
        };

        visit(visitor);
        return visitor.getResult();
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

        if(key==null){
            throw new NullPointerException(KCBN);
        }
        return put(key,value,root);
    }

    private V put(String key,V value,RedixTreeNode<V> node){
        V result=null;

        final int largePrefix = RedixTreeUtil.largestPrefixLength(key,node.getPrefix());

        if(largePrefix==node.getPrefix().length()&&largePrefix==key.length()){
            result=node.getValue();
            node.setValue(value);
            node.setHasValue(true);
        }else if(largePrefix==0||
        largePrefix<key.length()&&largePrefix>=node.getPrefix().length()){

            final String leftoverKey=key.substring(largePrefix);

            boolean found=false;

            for(RedixTreeNode<V>child: node){
                if(child.getPrefix().charAt((0)==leftoverKey.charAt(0))){
                    found=true;
                    result=put(leftoverKey,value,child);
                    break;
                }
            }

            if(!found) {
                // No child exists with any prefix of the given key, so add a new one
                RedixTreeNode<V> n = new RedixTreeNode<V>(leftoverKey, value);
                node.getChildren().add(n);
            }
        } else if(largePrefix < node.getPrefix().length()) {
            // Key and node.getPrefix() share a prefix, so split node
            final String leftoverPrefix = node.getPrefix().substring(largePrefix);
            final RedixTreeNode<V> n = new RedixTreeNode<V>(leftoverPrefix, node.getValue());
            n.setHasValue(node.hasValue());
            n.getChildren().addAll(node.getChildren());

            node.setPrefix(node.getPrefix().substring(0, largePrefix));
            node.getChildren().clear();
            node.getChildren().add(n);

            if(largePrefix == key.length()) {
                // The largest prefix is equal to the key, so set this node's value
                result = node.getValue();
                node.setValue(value);
                node.setHasValue(true);
            } else {
                // There's a leftover suffix on the key, so add another child
                final String leftoverKey = key.substring(largePrefix);
                final RedixTreeNode<V> keyNode = new RedixTreeNode<V>(leftoverKey, value);
                node.getChildren().add(keyNode);
                node.setHasValue(false);
            }
        } else {
            // node.getPrefix() is a prefix of key, so add as child
            final String leftoverKey = key.substring(largePrefix);
            final RedixTreeNode<V> n = new RedixTreeNode<V>(leftoverKey, value);
            node.getChildren().add(n);
        }
        return result;
    }



    @Override
    public V remove(Object key) {

        if (key==null){
            throw new NullPointerException(KCBN);
        }
        if(!(key instanceof String)){
            throw new ClassCastException(KMBSI);
        }

        //root node
        final String sKey = (String)key;
        if(sKey.equals("")) {
            final V value = root.getValue();
            root.setHasValue(false);
            return value;
        }

        return remove(sKey, root);
    }


    // Remove the value with the given key from the subtree rooted at the
    //	  given node.
    private V remove(String key, RedixTreeNode<V> node) {
        V ret = null;
        final Iterator<RedixTreeNode<V>> iter = node.getChildren().iterator();
        while(iter.hasNext()) {
            final RedixTreeNode<V> child = iter.next();
            final int largestPrefix = RedixTreeUtil.largestPrefixLength(key, child.getPrefix());
            if(largestPrefix == child.getPrefix().length() && largestPrefix == key.length()) {
                // Found our match, remove the value from this node
                if(child.getChildren().isEmpty()) {
                    // Leaf node, simply remove from parent
                    ret = child.getValue();
                    iter.remove();
                    break;
                } else if(child.hasValue()) {
                    // Internal node
                    ret = child.getValue();
                    child.setHasValue(false);

                    if(child.getChildren().size() == 1) {
                        // The subchild's prefix can be reused, with a little modification
                        final RedixTreeNode<V> subchild = child.getChildren().iterator().next();
                        final String newPrefix = child.getPrefix() + subchild.getPrefix();

                        // Merge child node with its single child
                        child.setValue(subchild.getValue());
                        child.setHasValue(subchild.hasValue());
                        child.setPrefix(newPrefix);
                        child.getChildren().clear();
                    }

                    break;
                }
            } else if(largestPrefix > 0 && largestPrefix < key.length()) {
                // Continue down subtree of child
                final String leftoverKey = key.substring(largestPrefix);
                ret = remove(leftoverKey, child);
                break;
            }
        }

        return ret;
    }

    @Override
    public void putAll(Map<? extends String, ? extends V> m) {
        for(Map.Entry<? extends String,? extends V> entry: m.entrySet())
            put(entry.getKey(),entry.getValue());

    }

    @Override
    public void clear() {

        root.getChildren().clear();
    }


    @Override
    public Set<String> keySet() {
        RedixTreeVisitor<V, Set<String>> visitor = new RedixTreeVisitor<V, Set<String>>() {
            Set<String> result = new TreeSet<String>();

            @Override
            public void visit(String key, V value) {
                result.add(key);
            }

            @Override
            public Set<String> getResult() {
                return result;
            }
        };
        visit(visitor);
        return visitor.getResult();    }


    @Override
    public Collection<V> values() {

        RedixTreeVisitor<V, Collection<V>> visitor = new RedixTreeVisitor<V, Collection<V>>() {
            Collection<V> result = new ArrayList<V>();

            @Override
            public void visit(String key, V value) {
                result.add(value);
            }

            @Override
            public Collection<V> getResult() {
                return result;
            }
        };
        visit(visitor);
        return visitor.getResult();    }


    @Override
    public Set<Entry<String, V>> entrySet() {
        RedixTreeVisitor<V, Set<Entry<String, V> visitor = new RedixTreeVisitor<V, Set<Entry<String, V>>>() {

            Set<Entry<String, V>> result = new HashSet<Entry<String, V>>();

            @Override
            public void visit(String key, V value) {
                result.add(new AbstractMap.SimpleEntry<String, V>(key, value));
            }

            @Override
            public Set<Entry<String, V>> getResult() {
                return result;
            }
        };

        visit(visitor);
        return visitor.getResult();
    }
}
