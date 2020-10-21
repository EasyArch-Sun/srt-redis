package java;

import java.io.Serializable;

public class RedixTreeUtil {

    private RedixTreeUtil(){

    }

    public static int largestPrefixLength(CharSequence a,CharSequence b){

        int len=0;
        for(int i=0;i<Math.min(a.length(),b.length());i++){
            if(a.charAt(i)!=b.charAt(i)){
                break;
            }
            ++len;
        }
        return len;
    }


    public static <V extends Serializable> void dumpTree(RedixTree<V> tree){
        dumpTree(tree.root, "");
    }






    static <V extends Serializable> void dumpTree(RedixTree<V> node,String outputPrefix){

        if(node.hashValue()){
            System.out.format("%s{%s : %s}%n", outputPrefix, node.getPrefix(), node.getValue());

        }else {
            System.out.format("%s{%s}%n", outputPrefix, node.getPrefix(), node.getValue());
        }

        for(RedixTreeNode<V> child: node){
            dumpTree(child,outputPrefix+"\t");
        }
    }
}
