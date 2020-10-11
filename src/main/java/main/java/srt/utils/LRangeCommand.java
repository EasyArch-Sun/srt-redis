package main.java.srt.utils;

import main.java.srt.BaseData;
import main.java.srt.procotol.RedisEncode;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class LRangeCommand implements Command {

    private List<String> params;
    private int start;
    private int end;
    @Override
    public void params(List<String> args) {
        this.params=params;
    }

    @Override
    public void run(BufferedWriter os) throws IOException {

        if (3 == params.size()) {
            String key = params.remove(0);
            List<String> baseDate = BaseData.getInstance().getList(key);

            start = Integer.parseInt(params.remove(0));
            end = Integer.parseInt(params.remove(0));

            List<String> list = new ArrayList<>();

            try {
                if (start >= 0 && start < baseDate.size()) {
                    if (end < 0) {
                        end = baseDate.size() + end;
                        for (int i = end; i < baseDate.size(); i++) {
                            list.add(baseDate.get(i));
                        }
                        RedisEncode.writeArray(os, list);
                    } else {
                        if(end>=baseDate.size()){

                            RedisEncode.writeArray(os,baseDate);


                        }else {
                            for (int i=start;i<end+1;i++){
                                list.add(baseDate.get(i));
                            }

                            RedisEncode.writeArray(os,list);
                        }

                    }
                } else {
                    for (int i = baseDate.size() + start; i < baseDate.size(); i++) {
                        list.add(baseDate.get(i));
                    }

                    RedisEncode.writeArray(os, baseDate.subList(baseDate.size() + start, baseDate.size()));

                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }else {
            try {

                RedisEncode.writeError(os,"ERR wrong number of arguments for 'lrange' command");

            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }
}
