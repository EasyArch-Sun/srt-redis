package main.java.srt;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class RedisServer {

    private static final Logger LOGGER= LoggerFactory.getLogger(RedisServer.class);
    private int port;

    private void init(int port) throws IOException {
        this.port=port;
        run();
    }

    private void run() throws IOException {


            ServerSocket serverSocket = new ServerSocket(port);
            LOGGER.info("等待连接。。。"+serverSocket.getInetAddress());

            //redis单线程，但是为了处理客户端传来的字符串，写成了多线程
            ExecutorService service= Executors.newSingleThreadExecutor();

            while (true){
                Socket client=serverSocket.accept();
                LOGGER.info("连接成功。。。"+client.getRemoteSocketAddress());
                service.execute(new MutiThread(client));
            }

    }





}
