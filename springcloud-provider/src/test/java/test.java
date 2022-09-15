import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class test {
    public static void main(String[] args) throws Exception {

        ServerSocket serverSocket=new ServerSocket(9999);
        Socket accept = serverSocket.accept();
        InputStream inputStream = accept.getInputStream();
        ByteArrayOutputStream bt=new ByteArrayOutputStream();
        byte[] b= new byte[1024];
        int len;
        while ((len=inputStream.read(b))!=-1){
            bt.write(b,0,len);
        }
        System.err.println(bt.toString());
//try {
//    close需要关闭  没写 懒得写
//}
    }
}
