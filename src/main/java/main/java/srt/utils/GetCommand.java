package main.java.srt.utils;

import main.java.srt.BaseData;

import java.io.BufferedWriter;
import java.util.List;
import java.util.Map;

public class GetCommand implements Command {

    private List<String> params;

    @Override
    public void params(List<String> args) {
        this.params=params;
    }

    @Override
    public void run(BufferedWriter os) {
        if(1==params.size()){
            String key=params.remove(0);
            Map<String,String> string= BaseData.getInstance().string ;
            String value=string.get(key);

            if(null!=value){
                RedisEncode.writeBulkString(os,value);
            }
        }else {

            RedisEncode.writeError(os,"ERR wrong number of arguments for"+"'"+"get"+"'"+"command");

        }

    }
}
