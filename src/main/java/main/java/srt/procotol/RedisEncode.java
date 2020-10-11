package main.java.srt.procotol;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.List;

public class RedisEncode {


    public static void writeError(BufferedWriter bufferedWriter, String message) throws IOException {

        bufferedWriter.write("-");
        bufferedWriter.write(message);
        bufferedWriter.write("\r\n");
        bufferedWriter.flush();

    }

    public static void writeBulkString(BufferedWriter bufferedWriter, String str) throws IOException {
        bufferedWriter.write('$');
        bufferedWriter.write(String.valueOf(str.length()));
        bufferedWriter.write("\r\n");
        bufferedWriter.write(str);
        bufferedWriter.write("\r\n");
        bufferedWriter.flush();

    }

    public static void writeString(BufferedWriter bufferedWriter) throws IOException {
        bufferedWriter.write('+');
        bufferedWriter.write("ok");
        bufferedWriter.write("\r\n");
        bufferedWriter.flush();
    }

    public static void writeInteger(BufferedWriter bufferedWriter, String len) throws IOException {
        bufferedWriter.write(':');
        bufferedWriter.write(len);
        bufferedWriter.write("\r\n");
        bufferedWriter.flush();
    }

    public static void writeArray(BufferedWriter bufferedWriter, List<?> list) throws IOException {
        bufferedWriter.write('*');
        bufferedWriter.write(String.valueOf(list.size()));
        bufferedWriter.write("\r\n");
        bufferedWriter.flush();

        for(Object item:list){
            if(item instanceof Integer){
                writeInteger(bufferedWriter,(String) item);
            }else if(item instanceof Long){
                writeInteger(bufferedWriter,(String) item);
            }else if(item instanceof String){
                writeBulkString(bufferedWriter,(String) item);
            }else if(item instanceof List<?>){
                writeArray(bufferedWriter,(List<?>) item);
            }
        }

    }
}
