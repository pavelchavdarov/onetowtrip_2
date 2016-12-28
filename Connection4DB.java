package onetowtrip;

import org.apache.commons.codec.binary.Base64;

import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.net.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by p.chavdarov on 28/12/2016.
 */
public class Connection4DB implements ConnectionInterface {

    private HttpsURLConnection conn;
    private Proxy proxy;
    private String connMethod;

    @Override
    public HttpURLConnection initConnection(String pUrl, String pMethod, String pLogin, String pPassword){
        URL url = null;
        int res_code = 0;
        String encoded = Base64.encodeBase64String((pLogin + ":" + pPassword).getBytes());

        // пока заглушка
        proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("10.95.5.19", 8888));
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
}
