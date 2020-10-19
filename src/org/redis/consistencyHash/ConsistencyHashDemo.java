package org.redis.consistencyHash;


import redis.clients.jedis.Jedis;

import java.util.*;

class JedisProxy{

    //服务器
    private static String[][] redisNodeList={
            {"localhost","6379"},
            {"localhost","6380"},
            {"localhost","6381"},
            {"localhost","6382"}

    };

   // private static Map<String, Jedis> serverConnectMap = new HashMap<>();

    //虚拟结点名称
    private static SortedMap<Integer,String> virtualNodes =new TreeMap<>();

    private static final int VIRTUAL_NODES = 10;

    static {
        for(int i=0;i<redisNodeList.length;i++){

            addServer("localhost","6379");
            addServer("localhost","6380");
            addServer("localhost","6381");
            addServer("localhost","6382");
        }
        System.out.println("xxx");
    }

    protected static int getHash(String str){

        final int p=1677619;
        int hash = (int) 2166136261L;

        for (int i=0;i<str.length();i++){

            hash=(hash^str.charAt((i))*p);
        }

        hash += hash << 13;
        hash ^= hash << 7;
        hash += hash << 3;
        hash ^= hash << 17;
        hash += hash << 5;

        if(hash < 0){
            hash=Math.abs((hash));
        }

        return hash;
    }

    protected static String getServer(String node){

        //得到带路由的结点的Hash值
        int hash=getHash(node);

        //得到大于该Hash值的所有Map
        SortedMap<Integer,String> subMap = virtualNodes.tailMap(hash);

        //第一个key就是顺时针过去离node最近的那个结点
        if(subMap.isEmpty()){
            subMap=virtualNodes.tailMap(0);
        }

        Integer i=subMap.firstKey();

        //返回对应的结点名称，通过字符串截取一下
        String virtualNodes =subMap.get(i);

        return virtualNodes.substring(0,virtualNodes.indexOf("&&"));

    }


    public static void addServer(String ip,String port){

        for (int i=0;i<VIRTUAL_NODES;i++){

            String virtualNodeName =ip+":"+port+"&&VN"+String.valueOf(i);
            int hash =getHash(virtualNodeName);

            System.out.println("虚拟结点["+virtualNodeName+"]被添加，Hash值为"+hash);

            virtualNodes.put(hash,virtualNodeName);
        }

        //serverConnectMap.put(ip+":"+port,new Jedis(ip,Integer.parseInt(port)));
    }


//    public String get(String key){
//
//        String server=getServer(key);
//
//        Jedis serverConnector=serverConnectMap.get(server);
//
//        if(serverConnector.get(key)==null){
//            System.out.println(key+"not in host :" +server);
//        }
//
//        return serverConnector.get(key);
//    }


    public void set(String key,String value){

        String server=getServer(key);

        //Jedis serverConnector =serverConnectMap.get(server);

        //serverConnector.set(key,value);

        System.out.println("set"+key+"into host :"+server);
    }



//    public float targetPercent(List<String> keyList){
//
//        int mingzhong = 0;
//        for(String key:keyList){
//            String server=getServer(key);
//            Jedis serverConnector =serverConnectMap.get(server);
//
//            if(serverConnector.get(key) !=null){
//                mingzhong++;
//            }
//        }
//
//        return (float) mingzhong/keyList.size();
//    }

}




public class ConsistencyHashDemo {

    public static void main(String[] args) {

        JedisProxy jedis=new JedisProxy();

        List<String> keyList = new ArrayList<>();


        for(int i=0;i<12;i++){
            keyList.add("zzz");
            jedis.set(Integer.toString(i),"value" );
        }


        String[] keys={"小明","小兰"};
        for(int i=0;i<keys.length;i++){
            System.out.println("["+keys[i]+"]的hash值为"+
                    jedis.getHash(keys[i])+",被路由到的结点["+jedis.getServer(keys[i])+"]");
        }
//
//        float tp = jedis.targetPercent(keyList);
//
//        System.out.println("before:"+tp);
//        JedisProxy.addServer("localhost","6379");
//        System.out.println("after:"+tp);

    }
}
