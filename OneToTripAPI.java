/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package onetowtrip;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import com.google.gson.Gson;
import oracle.jdbc.OracleConnection;


/**
 *
 * @author p.chavdarov
 */
public class OneToTripAPI {
    static private ConnectionInterface conn;
    static private String request;
    static private String answer;
    static private Gson gson;

    static void init(){
        conn = new Connection4DB();
        gson = new Gson();

        System.setProperty("ott_login","api@rgsbank");
        System.setProperty("ott_password","fhoouihfhoouih");
    }

    static OracleConnection getOracleConnection(){
        oracle.jdbc.OracleDriver ora = new oracle.jdbc.OracleDriver();
        java.sql.Connection conn = null;
        try {
            conn = ora.defaultConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return (oracle.jdbc.OracleConnection) conn;
    }




    static java.sql.Array array_wrapper(String typeName, Object elements) throws SQLException {
        oracle.jdbc.OracleDriver ora = new oracle.jdbc.OracleDriver();
        java.sql.Connection conn = ora.defaultConnection();
        oracle.jdbc.OracleConnection oraConn = (oracle.jdbc.OracleConnection)conn;
        return oraConn.createARRAY(typeName.toUpperCase(), elements);
    }

    // Регистрация клиента
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
            System.out.println(conn.getResponseCode());
            conn.initConnection("https://api.twiket.com/mt/registerUsers", "POST", System.getProperty("ott_login"), System.getProperty("ott_password"));
            answer = conn.getData();
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
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

    // Массовое начисление
    public static void addFunds(java.sql.Array bonusToCharge) throws SQLException {
        if (conn == null || gson ==null)
            init();
        conn.initConnection("https://api.twiket.com/mt/addFunds", "POST", System.getProperty("ott_login"), System.getProperty("ott_password"));
        BonusAddRequest charge = new  BonusAddRequest();
        String[] bonuses = (String[])bonusToCharge.getArray();
        for (String bonuse : bonuses) {
            System.out.println("adding fund: " + bonuse);
            String[] recs = bonuse.split("#");

            BonusRequest bonus_req = new BonusRequest();
            bonus_req.setUid(recs[0]);
            bonus_req.setAmount(Integer.valueOf(recs[1]));
            bonus_req.setOperId(recs[2]);
            bonus_req.setDesc(recs[3]);

            charge.bonuses.add(bonus_req);
        }

        request = gson.toJson(charge, BonusAddRequest.class);
        System.out.println(request);
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
            BonusAddResponce bonusAddResponce = gson.fromJson(answer, BonusAddResponce.class);

            oracle.jdbc.OracleConnection oraConn = getOracleConnection();
            if (oraConn != null){
                try {
                    CallableStatement callStm = oraConn.prepareCall("{call OTT_ADD_FUNDS_RESULT(?,?,?,?,?,?)}");
                    int i;
                    for (BonusResponce resp : bonusAddResponce.bonuses) {
                        i = 1;
                        callStm.setString(i++, resp.getUid());
                        callStm.setFloat( i++, resp.getAmount());
                        callStm.setString(i++, resp.getOperId());
                        callStm.setString(i++, resp.getDesc());
                        callStm.setInt(i++, resp.getSuccess()?1:0);
                        callStm.setString(i++, resp.getError());

                        callStm.execute();
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // Одинарное начисление
    public static String addFund(String pUID, Integer pAmount, String pOperId, String pDesc) throws SQLException {
        if (conn == null || gson ==null)
            init();
        conn.initConnection("https://api.twiket.com/mt/addFunds", "POST", System.getProperty("ott_login"), System.getProperty("ott_password"));
        BonusAddRequest charge = new  BonusAddRequest();
        BonusRequest bonus_req = new BonusRequest();
        bonus_req.setUid(pUID);
        bonus_req.setAmount(pAmount);
        bonus_req.setOperId(pOperId);
        bonus_req.setDesc(pDesc);

        charge.bonuses.add(bonus_req);

        request = gson.toJson(charge, BonusAddRequest.class);
        System.out.println(request);
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
            BonusAddResponce bonusAddResp = gson.fromJson(answer, BonusAddResponce.class);
            BonusResponce bonusResp = bonusAddResp.bonuses.get(0);

            if (bonusResp.getSuccess())
                return "SUCCESS";
            else
                return bonusResp.getError();
        }
        else
            return null;
    }

    // Получение бонусных бвижений
    public static void newMovements(){
        if (conn == null || gson ==null)
            init();

        conn.initConnection("https://api.twiket.com/mt/newMovements", "GET", System.getProperty("ott_login"), System.getProperty("ott_password"));
        try {
            answer = conn.getData();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        System.out.println("answer: " + answer);
        if (answer.length() >0) {
            BonusMovements bmoves = gson.fromJson(answer, BonusMovements.class);
/*
            oracle.jdbc.OracleDriver ora = new oracle.jdbc.OracleDriver();
            java.sql.Connection conn = null;
            try {
                conn = ora.defaultConnection();
            } catch (SQLException e) {
                e.printStackTrace();
            }*/
            oracle.jdbc.OracleConnection oraConn = getOracleConnection();

            /*
             Функция ott_add_bonus_movements добавляет запись о бонусном движении в спрасовник бонусных движений
             (или что-то в роде того) или сразу обрабатывает данные. Это буфферная зона, чтобы серверная логика оставалась
             в plsql.
             */
            if (oraConn != null){
                try {
                    CallableStatement callStm = oraConn.prepareCall("{call ott_add_bonus_movements_p(?,?,?,?,?,?,?,?,?,?,?)}");

                    for (BonusMovement bmove : bmoves.movements) {
                        int i = 1;
//                        callStm.registerOutParameter(1, Types.VARCHAR);

                        callStm.setString(i++, bmove.getMovId());
                        callStm.setString(i++, bmove.getUid());
                        callStm.setString(i++, bmove.getType());
                        callStm.setString(i++, bmove.getTs());
                        callStm.setFloat( i++, bmove.getAmount());
                        callStm.setString(i++, bmove.getDesc());
                        callStm.setString(i++, bmove.getOrderId());
                        callStm.setString(i++, bmove.getProd());
                        callStm.setString(i++, bmove.getName());
                        callStm.setString(i++, bmove.getEmail());
                        callStm.setString(i++, bmove.getPhone());

                        callStm.execute();
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

        }

//        return res;

    }

    public static void updateMovementsState(java.sql.Array updateStates) throws SQLException {
        if (conn == null || gson ==null)
            init();

        conn.initConnection("https://api.twiket.com/mt/updateMovementsState", "POST", System.getProperty("ott_login"), System.getProperty("ott_password"));

        BonusAck bonusAck = new BonusAck();
        String[] updates = (String[])updateStates.getArray();

        for (String update : updates) {
            System.out.println("updateState: " + update);
            String[] recs = update.split("#");

            BonusStatusAck status = new BonusStatusAck();
            status.setMovId(recs[0]);
            status.setStatus(recs[1]);
            status.setError(recs[2]);
            bonusAck.movements.add(status);
        }

        request = gson.toJson(bonusAck, BonusAck.class);
        System.out.println(request);
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

        if (answer.length() >0) {
            bonusAck = gson.fromJson(answer, BonusAck.class);
            /*
            oracle.jdbc.OracleDriver ora = new oracle.jdbc.OracleDriver();
            java.sql.Connection conn = null;
            try {
                conn = ora.defaultConnection();
            } catch (SQLException e) {
                e.printStackTrace();
            }*/
            oracle.jdbc.OracleConnection oraConn = getOracleConnection();
            // запишем статус в таблицу бонусных движений
            if (oraConn != null) {
                try {
                    CallableStatement callStm = oraConn.prepareCall("{call ott_bonus_ack_p(?,?,?)}");

                    for (BonusStatusAck bsa : bonusAck.movements) {
                        int i = 1;
                        callStm.setString(i++, bsa.getMovId());
                        callStm.setInt(i++, bsa.getSuccess()?1:0);
                        callStm.setString(i++, bsa.getError());

                        callStm.execute();
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
