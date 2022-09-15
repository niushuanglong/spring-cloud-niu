import java.io.*;

public class SocketServer1 {
    public static void main(String[] args) throws Exception {

        PrintStream stream=new PrintStream(new FileOutputStream("6.txt"));
        stream.println("账上");
        stream.println("呵呵哈哈哈");
        boolean b = stream.checkError();
        stream.flush();
        stream.close();
        File file=new File("读取.txt");
        FileReader reader=new FileReader("读取.txt");
        FileWriter writer=new FileWriter("输出.txt");
        BufferedWriter bufferedWriter=new BufferedWriter(writer);
        BufferedReader bufferedReader=new BufferedReader(reader);
        String line=null;

        while ((line=bufferedReader.readLine())!=null){
            bufferedWriter.write(line);
            bufferedWriter.newLine();
            bufferedWriter.flush();
        }
        bufferedWriter.close();
        bufferedReader.close();






    }
}
