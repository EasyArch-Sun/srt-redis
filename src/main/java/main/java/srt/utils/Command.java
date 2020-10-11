package main.java.srt.utils;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.List;

public interface Command {

    void params(List<String> args);
    void run(BufferedWriter os) throws IOException;
}
