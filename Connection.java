/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package onetowtrip;

//import com.sun.xml.internal.messaging.saaj.util.Base64;
import org.apache.commons.codec.binary.Base64;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.Proxy;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.net.ssl.HttpsURLConnection;

/**
 *
 * @author p.chavdarov
 */

public class Connection implements ConnectionInterface{

    private HttpsURLConnection conn;
    private Proxy proxy;
    private String connMethod;

    @Override
    public HttpURLConnection initConnection(String pUrl, String pMethod, String pLogin, String pPassword){
        URL url = null;
        int res_code = 0;
        String encoded = Base64.encodeBase64String((pLogin + ":" + pPassword).getBytes());

        // пока заглушка
        proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("10.95.17.46", 8080));
//        proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("10.95.5.19", 8888));
        try{
                url = new URL(pUrl);
        }catch(MalformedURLException mx){
                Logger.getLogger("https_conn").log(Level.SEVERE, null, mx);
                res_code = 1;
        }
        try {
            if (res_code == 0){
                conn = (HttpsURLConnection) url.openConnection(proxy);
                conn.setDoInput(true);
                conn.setDoOutput(true);
                conn.setUseCaches(false);
                conn.setConnectTimeout(60000);
                conn.setRequestMethod(pMethod);
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setRequestProperty("Authorization", "Basic " + encoded);
                conn.setRequestProperty("Cookie", "ENVID=dev-linode-03");
                //conn.setRequestProperty("charset", "utf-8");
            }
            else
                res_code = 1;
        } catch (IOException ex){
                Logger.getLogger("https_conn").log(Level.SEVERE, null, ex);
                res_code = 1;
        }
        if (res_code == 0){
            System.out.println("Connection done...");
            return conn;
        }
        else
            System.out.println("Connection error!!!");
        return null;
    }

    @Override
    public int sendData(String pData) throws IOException{
        if (conn == null)
            return 1;
        OutputStreamWriter outstrean = new OutputStreamWriter (conn.getOutputStream ());
        BufferedWriter wr = new BufferedWriter (outstrean);
        wr.write(pData);
        wr.flush();

        

        return 0; //conn.getResponseCode();

    } 
  
    @Override
    public String getData() throws IOException{
        String result="";
        String inputLine;
        
        if (conn != null) {
            try{
                InputStreamReader instrean = new InputStreamReader(conn.getInputStream());
                BufferedReader in = new BufferedReader(instrean);
                while ((inputLine = in.readLine()) != null) {
                    result += inputLine;
                }
            } catch (IOException ex) {
                System.out.println(ex);
            }    
           
        }
        return result;
    }
    
    public String getFile(String fileName) throws IOException{
            
        if (conn != null) {
            ReadableByteChannel rbc = Channels.newChannel(conn.getInputStream());
            FileOutputStream fos = new FileOutputStream(fileName);
            fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
        }
        return fileName;
    }

    public String getResponseCode() throws IOException {
        return String.valueOf(conn.getResponseCode());
    }
}
