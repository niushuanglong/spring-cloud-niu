import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;

public class UdpSenderDome1 {
    public static void main(String[] args) throws Exception{
        DatagramSocket socket=new DatagramSocket(6666);
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        while (true){
            String data = bufferedReader.readLine();
            byte[] datas = data.trim().getBytes();
            DatagramPacket localhost = new DatagramPacket(datas, 0, datas.length, new InetSocketAddress("localhost", 7777));
            socket.send(localhost);
            if (data.equals("bye")){
                break;
            }
        }

        socket.close();


    }
}
