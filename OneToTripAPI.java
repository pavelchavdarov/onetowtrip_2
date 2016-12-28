/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package onetowtrip;

import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.google.gson.Gson;

import java.sql.SQLException;

/**
 *
 * @author p.chavdarov
 */
public class OneToTripAPI {
    static private ConnectionInterface conn;
    static private String request;
    static private String answer;
    static private Gson gson;

    public static void init(){
        conn = new Connection();
        gson = new Gson();

        System.setProperty("ott_login","api@rgsbank");
        System.setProperty("ott_password","fhoouihfhoouih");
    }

    static java.sql.Array array_wrapper(
        String typeName,
        Object elements
    ) throws SQLException {
        oracle.jdbc.OracleDriver ora = new oracle.jdbc.OracleDriver();
        java.sql.Connection conn = ora.defaultConnection();
        oracle.jdbc.OracleConnection oraConn = (oracle.jdbc.OracleConnection)conn;
        java.sql.Array arr = oraConn.createARRAY(typeName.toUpperCase(), elements);
        return arr;
    }
    
    public static String registerUsers(String uid) throws Exception{
        if (conn == null)
            init();
        conn.initConnection("https://api.twiket.com/mt/registerUsers", "POST", System.getProperty("ott_login"), System.getProperty("ott_password"));
        UsersRegRequest udata = new UsersRegRequest();
        UserRequest ur = new UserRequest();
        ur.setUid(uid);
        udata.users.add(ur);


        request = gson.toJson(udata, UsersRegRequest.class);
        System.out.println(request);

        try {
            conn.sendData(request);
            answer = conn.getData();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        if (answer.length() > 0) {
            System.out.println(answer);
            UsersRegResponse regAnswers= gson.fromJson(answer, UsersRegResponse.class);
            UserResponce result = regAnswers.getUsers().get(0);
//            System.out.println("uid: " + result.getUid());
//            System.out.println("    success: " + result.getSuccess());
//            System.out.println("    error: " + result.getError());
            if (result.getSuccess())
                return "SUCCESS";
            else
                return result.getError();
        }
        else return "EMPTY_RESULT";
    }


    public static String addFunds(java.sql.Array bonusToCharge) throws SQLException {
        if (conn == null || gson ==null)
            init();
        conn.initConnection("https://api.twiket.com/mt/addFunds", "POST", System.getProperty("ott_login"), System.getProperty("ott_password"));
        BonusAddRequest charge = new  BonusAddRequest();
        String[] bonuses = (String[])bonusToCharge.getArray();
        for (int i =0; i< bonuses.length; i++){
            String[] recs = bonuses[i].split("#");

            BonusRequest bonus_req = new BonusRequest();
            bonus_req.setUid(recs[0]);
            bonus_req.setAmount(Integer.valueOf(recs[1]));
            bonus_req.setOperId(recs[2]);
            bonus_req.setDesc(recs[3]);

            charge.bonuses.add(bonus_req);
        }

        request = gson.toJson(charge, BonusAddRequest.class);

        try {
            conn.sendData(request);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        try {
            answer = conn.getData();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        System.out.println("answer: " + answer);
        if (answer.length() >0){
            BonusResponce bonusResp = gson.fromJson(answer, BonusAddResponce.class).getBonuses().get(0);
//            System.out.println("uid: " + bonusResponse.getBonuses().get(0).getUid());
//            System.out.println("success: " + bonusResponse.getBonuses().get(0).getSuccess());
//            System.out.println("error: " + bonusResponse.getBonuses().get(0).getError());

            if (bonusResp.getSuccess())
                return "SUCCESS";
            else
                return bonusResp.getError();
        }

        return "EMPTY_RESULT";
    }
}
