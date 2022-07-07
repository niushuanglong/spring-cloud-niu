import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class Socket1 {

    public static void main(String[] args) {
        Socket socket=null;
        OutputStream os=null;
        try {
            InetAddress ip=InetAddress.getByName("127.0.0.1");
            socket=new Socket(ip,9999);
            os = socket.getOutputStream();
            os.write("学习学习将军金甲夜不脱".getBytes(StandardCharsets.UTF_8));

        }catch (Exception e){
            e.printStackTrace();
        }finally {
            try {
                os.close();
                socket.close();
            }catch (Exception c){
                c.printStackTrace();
            }

        }
    }
}
