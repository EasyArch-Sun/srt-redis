package org;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class Test {
    public static void main(String[] args) {
        try {

            Socket s=new Socket("localhost",6379);
            OutputStream outputStream=s.getOutputStream();
            InputStream inputStream=s.getInputStream();

            outputStream.write("*2\r\n$3\r\nget\r\n$3\r\nabc\r\n".getBytes());
            outputStream.flush();

            int len=0;
            byte[] bytes=new byte[15];

            while((len=inputStream.read(bytes))>0){

                String s1=new String(bytes);

                System.out.println(s1);
            }

            s.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
