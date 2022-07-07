import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

public class UrlDemo {
    public static void main(String[] args) throws Exception{

    URL url=new URL("https://p6.music.126.net/obj/wonDlsKUwrLClGjCm8Kx/14574582771/4cc0/8ccc/2c6d/45aa387f66a3e27957bf4f56929ab22a.png");
    HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
        InputStream inputStream = urlConnection.getInputStream();
        FileOutputStream fileOutputStream = new FileOutputStream("c.png");
        int len;
        byte[] bytes=new byte[1024];
        while ((len=inputStream.read(bytes))!=-1){
            fileOutputStream.write(bytes,0,len);
        }
        fileOutputStream.close();
        inputStream.close();

    }
}
