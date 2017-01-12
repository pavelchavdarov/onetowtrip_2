/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package onetowtrip;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.Proxy;
import java.sql.*;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;
import oracle.jdbc.OracleConnection;
import oracle.sql.ARRAY;
import oracle.sql.ArrayDescriptor;
import oracle.sql.STRUCT;
import oracle.sql.StructDescriptor;


/**
 *
 * @author p.chavdarov
 */
public class OneToTripAPI {
    static private ConnectionInterface iConn;
    static private String request;
    static private String answer;
    static private Gson gson;

    static void init(){
        iConn = new Connection(  "http://api.twiket.com/",
                                "api@rgs", "fhoouihfhoouih",
                                new Proxy(Proxy.Type.HTTP, new InetSocketAddress("10.95.5.19",8888)));
        gson = new Gson();
    }

    private static OracleConnection getOracleConnection(){
        try {
            oracle.jdbc.OracleDriver ora = new oracle.jdbc.OracleDriver();
            return (oracle.jdbc.OracleConnection) ora.defaultConnection();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

//    static java.sql.Array array_wrapper(String typeName, Object elements) throws SQLException {
//        oracle.jdbc.OracleDriver ora = new oracle.jdbc.OracleDriver();
//        java.sql.Connection conn = ora.defaultConnection();
//        oracle.jdbc.OracleConnection oraConn = (oracle.jdbc.OracleConnection)conn;
//        return oraConn.createARRAY(typeName.toUpperCase(), elements);
//    }

    // Регистрация клиента

    public static Array registerUser(String uid){
        if (iConn == null)
            init();
        try {
            UsersRegRequest regRequest = new UsersRegRequest();
            UserRequest ur = new UserRequest(uid);
            regRequest.users.add(ur);

            request = gson.toJson(regRequest, UsersRegRequest.class);
            System.err.println(request);

            iConn.initConnection("mt/registerUsers", "POST");
            // отправка запроса и получение ответа
            iConn.sendData(request);
            answer = iConn.getData();
        } catch (Exception e) {
            e.printStackTrace();
            // Попытка вернуть таблицу из одной записи с описанием возникшего исключения.
            // Может быт это понадобится на стороне Ритейла...
            // Если и это не получается, то возвращаем null
            oracle.jdbc.OracleConnection oraConn = getOracleConnection();
            STRUCT[] respsAsStructs = new STRUCT[1];
            try {
                StructDescriptor respTypeDesc = StructDescriptor.createDescriptor("OTT_USER_REG", oraConn);
                Object[] respFields = new Object[]{ "", 0, "", "", e.getMessage()};
                STRUCT respStruct = new STRUCT(respTypeDesc, oraConn, respFields);
                respsAsStructs[0] = respStruct;
                ArrayDescriptor respArrayDesc = ArrayDescriptor.createDescriptor("OTT_USER_REG_TAB", oraConn);
                return new ARRAY(respArrayDesc, oraConn, respsAsStructs);
            } catch (SQLException e1) {
                e1.printStackTrace();
                return null;
            }
        }
        // если мы добрались сюда, значит получен штатный ответ от OTT
        // для сохранения ответа в файл трейса (или дампа)
        System.err.println(answer);
        UsersRegResponse regAnswer= gson.fromJson(answer, UsersRegResponse.class);
        // Т.к. запрос отправлялся только по одному клиенту, то и в ответе только одна запись
        UserResponce resp = regAnswer.users.get(0);

        oracle.jdbc.OracleConnection oraConn = getOracleConnection();
        try {
            if (oraConn != null) {
                // http://stackoverflow.com/questions/19892674/pass-array-from-java-to-plsql-stored-procedure
                STRUCT[] respsAsStructs = new STRUCT[1];
                StructDescriptor respTypeDesc = StructDescriptor.createDescriptor("OTT_USER_REG", oraConn);
                Object[] respFields = new Object[]{
                        resp.getUid(),
                        resp.getSuccess(),
                        resp.getError(),
                        request, // т.к. регистрируется один клиент, то можно подставить целиком request
                        "" /* нустое исключение*/ };
                STRUCT respStruct = new STRUCT(respTypeDesc, oraConn, respFields);
                respsAsStructs[0] = respStruct;

                ArrayDescriptor respArrayDesc = ArrayDescriptor.createDescriptor("OTT_USER_REG_TAB", oraConn);

                return new ARRAY(respArrayDesc, oraConn, respsAsStructs);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public static Array registerUsersStr(java.sql.Array usersToReg){
        if (iConn == null || gson ==null) init();
        try {
            UsersRegRequest regRequest = new UsersRegRequest();
            String[] users_str = (String[])usersToReg.getArray();
            for (String usr : users_str) {
                UserRequest user = new UserRequest(usr);
                regRequest.users.add(user);
            }
            request = gson.toJson(regRequest, UsersRegRequest.class);
            System.err.println("request: " + request);

            iConn.initConnection("mt/registerUsers", "POST");
            // отправка запроса и получение ответа
            iConn.sendData(request);
            answer = iConn.getData();
        } catch (Exception e) {
            e.printStackTrace();
            // Попытка вернуть таблицу из одной записи с описанием возникшего исключения.
            // Может быт это понадобится на стороне Ритейла...
            // Если и это не получается, то возвращаем null
            oracle.jdbc.OracleConnection oraConn = getOracleConnection();
            STRUCT[] respsAsStructs = new STRUCT[1];
            try {
                StructDescriptor respTypeDesc = StructDescriptor.createDescriptor("OTT_USER_REG", oraConn);
                Object[] respFields = new Object[]{ "", 0, "", "", e.getMessage()};
                STRUCT respStruct = new STRUCT(respTypeDesc, oraConn, respFields);
                respsAsStructs[0] = respStruct;
                ArrayDescriptor respArrayDesc = ArrayDescriptor.createDescriptor("OTT_USER_REG_TAB", oraConn);
                return new ARRAY(respArrayDesc, oraConn, respsAsStructs);
            } catch (SQLException e1) {
                e1.printStackTrace();
                return null;
            }
        }
        // если мы добрались сюда, значит получен штатный ответ от OTT
        // для сохранения ответа в файл трейса (или дампа)
        System.err.println("answer: " + answer);
        UsersRegResponse regAnswer= gson.fromJson(answer, UsersRegResponse.class);

        // преобразоание bonusAddResponce в массив java.sql.Array
        oracle.jdbc.OracleConnection oraConn = getOracleConnection();

        try {
            if (oraConn != null) {
                // http://stackoverflow.com/questions/19892674/pass-array-from-java-to-plsql-stored-procedure
                STRUCT[] respsAsStructs = new STRUCT[regAnswer.users.size()];
                StructDescriptor respTypeDesc = StructDescriptor.createDescriptor("OTT_USER_REG", oraConn);
                int i = 0;
                for (UserResponce resp : regAnswer.users) {
                    Object[] respFields = new Object[]{ resp.getUid(),
                            resp.getSuccess(),
                            resp.getError(),
                            gson.toJson(resp, UserRequest.class),
                            "" // нустое исключение
                    };
                    STRUCT respStruct = new STRUCT(respTypeDesc, oraConn, respFields);
                    respsAsStructs[i++] = respStruct;
                }
                ArrayDescriptor respArrayDesc = ArrayDescriptor.createDescriptor("OTT_USER_REG_TAB", oraConn);

                return new ARRAY(respArrayDesc, oraConn, respsAsStructs);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }

    // Заготовка на будущее, если клиентов начнут регистрировать с передачей перс. данных (ФИО, тел., почта,...)
    public static Array registerUsersTab(java.sql.Array usersToReg){
        if (iConn == null || gson ==null) init();
        try {
            UsersRegRequest regRequest = new UsersRegRequest();
            Object[] users_obj = (Object[])usersToReg.getArray();
            for (Object usr : users_obj) {
                Object[] user_reqs = ((java.sql.Struct)usr).getAttributes();
                UserRequest user = new UserRequest((String)user_reqs[0]);
                regRequest.users.add(user);
            }
            request = gson.toJson(regRequest, UsersRegRequest.class);
            System.err.println("request: " + request);

            iConn.initConnection("mt/registerUsers", "POST");
            // отправка запроса и получение ответа
            iConn.sendData(request);
            answer = iConn.getData();
        } catch (Exception e) {
            e.printStackTrace();
            // Попытка вернуть таблицу из одной записи с описанием возникшего исключения.
            // Может быт это понадобится на стороне Ритейла...
            // Если и это не получается, то возвращаем null
            oracle.jdbc.OracleConnection oraConn = getOracleConnection();
            STRUCT[] respsAsStructs = new STRUCT[1];
            try {
                StructDescriptor respTypeDesc = StructDescriptor.createDescriptor("OTT_USER_REG", oraConn);
                Object[] respFields = new Object[]{ "", 0, "", "", e.getMessage()};
                STRUCT respStruct = new STRUCT(respTypeDesc, oraConn, respFields);
                respsAsStructs[0] = respStruct;
                ArrayDescriptor respArrayDesc = ArrayDescriptor.createDescriptor("OTT_USER_REG_TAB", oraConn);
                return new ARRAY(respArrayDesc, oraConn, respsAsStructs);
            } catch (SQLException e1) {
                e1.printStackTrace();
                return null;
            }
        }
        // если мы добрались сюда, значит получен штатный ответ от OTT
        // для сохранения ответа в файл трейса (или дампа)
        System.err.println("answer: " + answer);
        UsersRegResponse regAnswer= gson.fromJson(answer, UsersRegResponse.class);

        // преобразоание bonusAddResponce в массив java.sql.Array
        oracle.jdbc.OracleConnection oraConn = getOracleConnection();

        try {
            if (oraConn != null) {
                // http://stackoverflow.com/questions/19892674/pass-array-from-java-to-plsql-stored-procedure
                STRUCT[] respsAsStructs = new STRUCT[regAnswer.users.size()];
                StructDescriptor respTypeDesc = StructDescriptor.createDescriptor("OTT_USER_REG", oraConn);
                int i = 0;
                for (UserResponce resp : regAnswer.users) {
                    Object[] respFields = new Object[]{ resp.getUid(),
                                                        resp.getSuccess(),
                                                        resp.getError(),
                                                        gson.toJson(resp, UserResponce.class),
                                                        "" // нустое исключение
                    };
                    STRUCT respStruct = new STRUCT(respTypeDesc, oraConn, respFields);
                    respsAsStructs[i++] = respStruct;
                }
                ArrayDescriptor respArrayDesc = ArrayDescriptor.createDescriptor("OTT_USER_REG_TAB", oraConn);

                return new ARRAY(respArrayDesc, oraConn, respsAsStructs);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }

    // Массовое начисление
//    public static void addFunds(java.sql.Array bonusToCharge) throws Exception {
//        if (iConn == null || gson ==null)
//            init();
//        iConn.initConnection("mt/addFunds", "POST");
//        BonusAddRequest charge = new  BonusAddRequest();
//        String[] bonuses = (String[])bonusToCharge.getArray();
//        for (String bonuse : bonuses) {
//            System.out.println("adding fund: " + bonuse);
//            String[] recs = bonuse.split("#");
//
//            BonusRequest bonus_req = new BonusRequest();
//            bonus_req.setUid(recs[0]);
//            bonus_req.setAmount(Integer.valueOf(recs[1]));
//            bonus_req.setOperId(recs[2]);
//            bonus_req.setDesc(recs[3]);
//
//            charge.bonuses.add(bonus_req);
//        }
//
//        request = gson.toJson(charge, BonusAddRequest.class);
//        System.out.println(request);
//        try {
//            iConn.sendData(request);
//            answer = iConn.getData();
//        } catch (IOException ex) {
//            ex.printStackTrace();
//        }
//
//        System.out.println("answer: " + answer);
//        if (answer.length() >0){
//            BonusAddResponce bonusAddResponce = gson.fromJson(answer, BonusAddResponce.class);
//
//            oracle.jdbc.OracleConnection oraConn = getOracleConnection();
//            if (oraConn != null){
//                try {
//                    CallableStatement callStm = oraConn.prepareCall("{call OTT_ADD_FUNDS_RESULT(?,?,?,?,?,?)}");
//                    int i;
//                    for (BonusResponce resp : bonusAddResponce.bonuses) {
//                        i = 1;
//                        callStm.setString(i++, resp.getUid());
//                        callStm.setFloat( i++, resp.getAmount());
//                        callStm.setString(i++, resp.getOperId());
//                        callStm.setString(i++, new String(resp.getDesc().getBytes(),"windows-1251"));
//                        callStm.setInt(i++, resp.getSuccess()?1:0);
//                        callStm.setString(i++, resp.getError());
//
//                        callStm.execute();
//                    }
//                } catch (SQLException e) {
//                    e.printStackTrace();
//                } catch (UnsupportedEncodingException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//    }

    // Массовое начисление с таблицами
    public static Array addFundsTab(java.sql.Array bonusToCharge) {
        Exception excp = null;

        if (iConn == null || gson ==null) init();
        try {
            // формирование json-запроса
            BonusAddRequest charge = new  BonusAddRequest();
            Object[] bonuses_obj = (Object[])bonusToCharge.getArray();
            for (Object obj : bonuses_obj) {
                Object[] bonus_reqs = ((java.sql.Struct)obj).getAttributes();
                BonusRequest bonus = new BonusRequest();
                /*
                * Было бы здорово преобразовывать вот так просто, но возникает ClassCastException
                * BonusRequest bonus = (BonusRequest)obj;
                * */
                bonus.setUid((String)bonus_reqs[0]);
                // sql number возвращается в java как BigDecimal
                bonus.setAmount(((java.math.BigDecimal)bonus_reqs[1]).intValue());
                bonus.setOperId((String)bonus_reqs[2]);
                bonus.setDesc((String)bonus_reqs[3]);
                charge.bonuses.add(bonus);
            }
            request = gson.toJson(charge, BonusAddRequest.class);
            System.err.println("request: " + request);

            iConn.initConnection("mt/addFunds", "POST");
            // отправка запроса и получение ответа
            iConn.sendData(request);
            answer = iConn.getData();
        } catch (Exception e) {
            e.printStackTrace();
            // Попытка вернуть таблицу из одной записи с описанием возникшего исключения.
            // Может быт это понадобится на стороне Ритейла...
            // Если и это не получается, то возвращаем null
            oracle.jdbc.OracleConnection oraConn = getOracleConnection();
            STRUCT[] respsAsStructs = new STRUCT[1];

            try {
                StructDescriptor respTypeDesc = StructDescriptor.createDescriptor("OTT_BONUS_CHARGE", oraConn);
                Object[] respFields = new Object[]{ "", 0, "", "", false, "", "", e.getMessage()};
                STRUCT respStruct = new STRUCT(respTypeDesc, oraConn, respFields);
                respsAsStructs[0] = respStruct;
                ArrayDescriptor respArrayDesc = ArrayDescriptor.createDescriptor("OTT_BONUS_CHARGE_TAB", oraConn);
                return new ARRAY(respArrayDesc, oraConn, respsAsStructs);
            } catch (SQLException e1) {
                e1.printStackTrace();
                return null;
            }

        }

        // если мы добрались сюда, значит получен штатный ответ от OTT
        // для созранение ответа в файл трейса (ала дампа)
        System.err.println("answer: " + answer);
        BonusAddResponce bonusAddResponce = gson.fromJson(answer, BonusAddResponce.class);

        // преобразоание bonusAddResponce в массив java.sql.Array
        oracle.jdbc.OracleConnection oraConn = getOracleConnection();

        try {
            if (oraConn != null) {
                // http://stackoverflow.com/questions/19892674/pass-array-from-java-to-plsql-stored-procedure
                STRUCT[] respsAsStructs = new STRUCT[bonusAddResponce.bonuses.size()];
                StructDescriptor respTypeDesc = StructDescriptor.createDescriptor("OTT_BONUS_CHARGE", oraConn);
                int i = 0;
                for (BonusResponce resp : bonusAddResponce.bonuses) {
                    Object[] respFields = new Object[]{resp.getUid(),
                            resp.getAmount(),
                            resp.getOperId(),
                            resp.getDesc(),
                            resp.getSuccess(),
                            resp.getError()
                            , gson.toJson(resp, BonusRequest.class)
                            , "" // нустое исключение
                    };
                    /*
                    * Здесь надо вставить вызов операции записи запроса responseStr в "Карты. OneTwoTrip".История запросов
                    * */
                    STRUCT respStruct = new STRUCT(respTypeDesc, oraConn, respFields);
                    respsAsStructs[i++] = respStruct;
                }
                ArrayDescriptor respArrayDesc = ArrayDescriptor.createDescriptor("OTT_BONUS_CHARGE_TAB", oraConn);

                return new ARRAY(respArrayDesc, oraConn, respsAsStructs);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }

    // Одинарное начисление
    public static Array addFund(String pUID, Integer pAmount, String pOperId, String pDesc) throws Exception {
        if (iConn == null || gson ==null)
            init();
        try {
            BonusAddRequest charge = new  BonusAddRequest();
            BonusRequest bonus_req = new BonusRequest();
            bonus_req.setUid(pUID);
            bonus_req.setAmount(pAmount);
            bonus_req.setOperId(pOperId);
            bonus_req.setDesc(pDesc);
            charge.bonuses.add(bonus_req);

            request = gson.toJson(charge, BonusAddRequest.class);
            System.err.println(request);

            iConn.initConnection("mt/addFunds", "POST");
            iConn.sendData(request);
            answer = iConn.getData();
        } catch (IOException e) {
            e.printStackTrace();
            // Попытка вернуть таблицу из одной записи с описанием возникшего исключения.
            // Может быт это понадобится на стороне Ритейла...
            // Если и это не получается, то возвращаем null
            oracle.jdbc.OracleConnection oraConn = getOracleConnection();
            STRUCT[] respsAsStructs = new STRUCT[1];

            try {
                StructDescriptor respTypeDesc = StructDescriptor.createDescriptor("OTT_BONUS_CHARGE", oraConn);
                Object[] respFields = new Object[]{ "", 0, "", "", false, "", "", e.getMessage()};
                STRUCT respStruct = new STRUCT(respTypeDesc, oraConn, respFields);
                respsAsStructs[0] = respStruct;
                ArrayDescriptor respArrayDesc = ArrayDescriptor.createDescriptor("OTT_BONUS_CHARGE_TAB", oraConn);
                return new ARRAY(respArrayDesc, oraConn, respsAsStructs);
            } catch (SQLException e1) {
                e1.printStackTrace();
                return null;
            }
        }

        // если мы добрались сюда, значит получен штатный ответ от OTT
        // для созранение ответа в файл трейса (ала дампа)
        System.err.println("answer: " + answer);

        BonusAddResponce bonusAddResp = gson.fromJson(answer, BonusAddResponce.class);
        BonusResponce bonusResp = bonusAddResp.bonuses.get(0);

        oracle.jdbc.OracleConnection oraConn = getOracleConnection();

        try {
            if (oraConn != null) {
                // http://stackoverflow.com/questions/19892674/pass-array-from-java-to-plsql-stored-procedure
                STRUCT[] respsAsStructs = new STRUCT[1];
                StructDescriptor respTypeDesc = StructDescriptor.createDescriptor("OTT_BONUS_CHARGE", oraConn);

                Object[] respFields = new Object[]{bonusResp.getUid(),
                        bonusResp.getAmount(),
                        bonusResp.getOperId(),
                        bonusResp.getDesc(),
                        bonusResp.getSuccess(),
                        bonusResp.getError(),
                        request,
                        "" // нустое исключение
                };
                STRUCT respStruct = new STRUCT(respTypeDesc, oraConn, respFields);
                respsAsStructs[0] = respStruct;

                ArrayDescriptor respArrayDesc = ArrayDescriptor.createDescriptor("OTT_BONUS_CHARGE_TAB", oraConn);

                return new ARRAY(respArrayDesc, oraConn, respsAsStructs);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }

    // Получение бонусных бвижений
    public static void newMovements(){
        if (iConn == null || gson ==null)
            init();


        try {
            iConn.initConnection("mt/newMovements", "GET");
            answer = iConn.getData();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("answer: " + answer);
        if (answer.length() >0) {
            BonusMovements bmoves = gson.fromJson(answer, BonusMovements.class);

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

    public static void updateMovementsState(java.sql.Array updateStates) throws Exception {
        if (iConn == null || gson ==null)
            init();

        iConn.initConnection("mt/updateMovementsState", "POST");

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
            iConn.sendData(request);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        try {
            answer = iConn.getData();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        System.out.println("answer: " + answer);

        if (answer.length() >0) {
            bonusAck = gson.fromJson(answer, BonusAck.class);
            /*
            oracle.jdbc.OracleDriver ora = new oracle.jdbc.OracleDriver();
            java.sql.Connection iConn = null;
            try {
                iConn = ora.defaultConnection();
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
