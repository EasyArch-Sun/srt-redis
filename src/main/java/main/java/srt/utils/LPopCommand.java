package main.java.srt.utils;

import main.java.srt.BaseData;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.List;

public class LPopCommand implements Command {

    private List<String> params;

    @Override
    public void params(List<String> args) {
        this.params=params;
    }

    @Override
    public void run(BufferedWriter os) throws IOException {

        if (1==params.size()){
            String key=params.remove(0);
            List<String> list= BaseData.getInstance().getList(key);
        }
    }
}
