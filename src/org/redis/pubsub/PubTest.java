package org.redis.pubsub;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPubSub;

public class PubTest extends JedisPubSub {

    public void onMessage(String channel,String message){
        System.out.println("=========================");
        System.out.println(channel+"="+message);
    }

    public void onPMessage(String pattern,String channel,String message){
        System.out.println("=========================");
        System.out.println(channel+"="+message);
    }

    public void onSubscribe(String channel, int subscribedChannels) {
        System.out.println("=========================");
        System.out.println(channel+"="+subscribedChannels);
    }

    public void onUnsubscribe(String channel, int subscribedChannels) {
        System.out.println("=========================");
        System.out.println(channel+"="+subscribedChannels);
    }

    public void onPUnsubscribe(String pattern, int subscribedChannels) {
        System.out.println("=========================");
        System.out.println(pattern+"="+subscribedChannels);
    }

    public void onPSubscribe(String pattern, int subscribedChannels) {
        System.out.println("=========================");
        System.out.println(pattern+"="+subscribedChannels);
    }

    public static void main(String[] args) {

        Jedis jedis=new Jedis("127.0.0.1",6379);
        jedis.connect();

        SubTest jedisPubSub=new SubTest();
        jedis.subscribe(jedisPubSub,"news");

        jedis.disconnect();

    }
}
