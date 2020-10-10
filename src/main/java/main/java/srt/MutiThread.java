package main.java.srt;

import main.java.srt.utils.Command;

import java.io.*;
import java.net.Socket;

public class MutiThread {

    private Socket socket;

    public MutiThread(Socket client) {
        this.socket=socket;
    }

    public void run(){
        while (true) {
            try {
                BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(socket.getInputStream()));
                BufferedWriter bufferedWriter=new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

                while (true){
                    Command command=new RedisDecode(bufferedReader,bufferedWriter).command();
                    if(command!=null){
                        command.run(bufferedWriter);
                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
