/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package onetowtrip;

import java.io.IOException;
import java.net.HttpURLConnection;

/**
 *
 * @author p.chavdarov
 */
interface ConnectionInterface{
  public void initConnection(String pUri, String pMethod) throws Exception;
  public int sendData(String pData) throws IOException;
  public String getData() throws IOException;
}