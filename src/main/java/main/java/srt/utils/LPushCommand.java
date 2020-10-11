package main.java.srt.utils;

import main.java.srt.BaseData;
import main.java.srt.procotol.RedisEncode;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.List;

public class LPushCommand implements Command {

    private List<String> params;
    @Override
    public void params(List<String> args) {
        this.params=params;
    }

    @Override
    public void run(BufferedWriter os) throws IOException {

        if(2==params.size()){
            String key=params.remove(0);
            String value=params.remove(0);
            List<String> list= BaseData.getInstance().getList(key) ;
            list.add(value);

            RedisEncode.writeInteger(os,String.valueOf(list.size()));

        }else if(3==params.size()){
            String key=params.remove(0);
            List<String> list=BaseData.getInstance().getList(key);
            list.addAll(params);

            RedisEncode.writeInteger(os,String.valueOf(list.size()));

        }else {
            RedisEncode.writeError(os,"ERR wrong number of arguments for 'lpush' command");
        }
    }
}
