package org.redis.consistencyHash;


import redis.clients.jedis.Jedis;

import java.util.*;

class JedisProxy{

    private static String[][] redisNodeList={
            {"localhost","6379"},
            {"localhost","6380"},
            {"localhost","6381"},
            {"localhost","6382"}

    };

    private static Map<String, Jedis> serverConnectMap = new HashMap<>();

    private static SortedMap<Integer,String> virtualNodes =new TreeMap<>();

    private static final int VIRTUAL_NODES = 10;

    static {
        for(String[] str1: redisNodeList){


        }
        System.out.println("xxx");
    }

    private static int getHash(String str){

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

    private static String getServer(String node){

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

        serverConnectMap.put(ip+":"+port,new Jedis(ip,Integer.parseInt(port)));
    }


    public String get(String key){

        String server=getServer(key);

        Jedis serverConnector=serverConnectMap.get(server);

        if(serverConnector.get(key)==null){
            System.out.println(key+"not in host :" +server);
        }

        return serverConnector.get(key);
    }


    public void set(String key,String value){

        String server=getServer(key);

        Jedis serverConnector =serverConnectMap.get(server);

        serverConnector.set(key,value);

        System.out.println("set"+key+"into host :"+server);
    }


    public void flushdb(){
        for(String str:serverConnectMap.keySet()){

            System.out.println("清空host："+str);
            serverConnectMap.get(str).flushDB();
        }
    }


    public float targetPercent(List<String> keyList){

        int mingzhong = 0;
        for(String key:keyList){
            String server=getServer(key);
            Jedis serverConnector =serverConnectMap.get(server);

            if(serverConnector.get(key) !=null){
                mingzhong++;
            }
        }

        return (float) mingzhong/keyList.size();
    }

}




public class ConsistencyHashDemo {

    public static void main(String[] args) {

        JedisProxy jedis=new JedisProxy();
        jedis.flushdb();

        List<String> keyList = new ArrayList<>();

        for(int i=0;i<1000000;i++){
            keyList.add(Integer.toString(i));
            jedis.set(Integer.toString(i),"value" );
        }

        float tp = jedis.targetPercent(keyList);

        System.out.println("before:"+tp);
        JedisProxy.addServer("localhost","6379");
        System.out.println("after:"+tp);

    }
}
