package main.java.srt.procotol;

import main.java.srt.constant.Constant;
import main.java.srt.exception.RedisException;
import main.java.srt.utils.Command;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class RedisDecode {

    private AnalysisInputStrean analysis;
    private BufferedWriter bufferedWriter;

    public RedisDecode(BufferedReader bufferedReader, BufferedWriter bufferedWriter) {

        this.analysis=new AnalysisInputStrean(bufferedReader);
        this.bufferedWriter=bufferedWriter;
    }


    public Command command() throws IOException {
        Object object=analysis.process();
        List<String> list=(List<String>) object;
        String params=list.remove(0);
        String className;
        Class<?> cls=null;
        Command command=null;

        if(null==params){
            RedisEncode.writeError(bufferedWriter,"ERR unkuown command"+"'"+params+"'");
            throw new RedisException("服务端异常");
        }else {
            className=String.format(Constant.COMMAND,params.toUpperCase());
        }
        try {
            cls=Class.forName(className);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        if(null==cls||Command.class.isAssignableFrom(cls)){

            RedisEncode.writeError(bufferedWriter,"unknown command,Please try again!");

        }else {
            try {
                command=(Command) cls.newInstance();
                command.params(list);
            }catch (InstantiationException|IllegalAccessException e){
                e.printStackTrace();
            }
        }
        return command;
    }



    private class AnalysisInputStrean{

        private BufferedReader is;

        public AnalysisInputStrean(BufferedReader is){
            this.is=is;
        }

        public Object process() throws IOException {
            String line=is.readLine();

            if(line.startsWith("*")){
                String len=line.substring(1);
                return processArray(len);
            }else if(line.startsWith("$")){
                return processBulkString();
            }

          return null;
        }

        private String processBulkString() throws IOException {
            String s=is.readLine();
            if(null==s||"".equals(s)){
                return null;
            }
            return s;
        }


        private List<String> processArray(String size) throws IOException {
            int len=Integer.parseInt(size);
            List<String> list=new ArrayList<>();

            for (int i=0;i<len;i++){
                String process=(String) process();
                list.add(process);
            }
            return list;
        }

    }

}
