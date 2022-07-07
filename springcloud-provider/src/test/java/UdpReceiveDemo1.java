import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class UdpReceiveDemo1 {
    public static void main(String[] args) throws Exception{
        DatagramSocket socket=new DatagramSocket(7777);
        while (true){
            byte[] buf= new byte[1024];
            DatagramPacket packet = new DatagramPacket(buf, 0, buf.length);
            socket.receive(packet);//阻塞式接收包裹
            byte[] data = packet.getData();
            String receive = new String(data, 0, data.length);
            System.err.println(receive.trim());
            if (receive.equals("bye")){
                break;
            }
        }

    }
}
