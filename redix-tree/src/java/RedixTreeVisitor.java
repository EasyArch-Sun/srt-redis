package java;

public interface RedixTreeVisitor<V ,R > {


    //visit node(key value)
    public abstract void visit(String key,V value);


    //result
    public abstract R getResult();

}
