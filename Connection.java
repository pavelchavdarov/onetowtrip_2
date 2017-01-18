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

    private HttpURLConnection conn;
    private Proxy proxy;
    private String url_addr;
    private String login;
    private String password;
    private String authString;

    public Connection(String pUrl, String pLogin, String pPassword, Proxy pProxy) {
        this.url_addr = pUrl;
        this.authString = Base64.encodeBase64String((pLogin + ":" + pPassword).getBytes());
        this.proxy = pProxy;
    }

    @Override
    public void initConnection(String pUri, String pMethod) throws Exception{
        URL url = new URL(this.url_addr + pUri);
        conn = (HttpURLConnection) url.openConnection(proxy);
        conn.setDoInput(true);
        conn.setDoOutput(true);
        conn.setUseCaches(false);
        conn.setConnectTimeout(60000);
        conn.setRequestMethod(pMethod);
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setRequestProperty("Authorization", "Basic " + this.authString);
        //conn.setRequestProperty("Cookie", "ENVID=dev-linode-03");
        //return conn;
    }

    public int sendData(String pData) throws IOException{
        if (conn == null)
            return 1;
        OutputStreamWriter outstrean = new OutputStreamWriter (conn.getOutputStream ());
        BufferedWriter wr = new BufferedWriter (outstrean);
        wr.write(pData);
        wr.flush();

        return 0;
    }
  
    public String getData() throws IOException{
        String result="";
        String inputLine;
        
        if (conn != null) {
            InputStreamReader instrean = new InputStreamReader(conn.getInputStream());
            BufferedReader in = new BufferedReader(instrean);
            while ((inputLine = in.readLine()) != null) {
                result += inputLine;
            }
        }
        return result;
    }
}
