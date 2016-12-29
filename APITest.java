package onetowtrip;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.sql.DriverManager;

/**
 * Created by p.chavdarov on 26/12/2016.
 */
public class APITest {
    static void toConsole(String str){
        System.out.println(str);
    }

    public static void main(String[] args){
        OneToTripAPI.init();

//      work
        try {
        String uid = "1000000009";
            System.out.println(OneToTripAPI.registerUsers(uid));
        } catch (Exception e) {
            e.printStackTrace();
        }

//      ~work
//        try {
//            DriverManager.registerDriver (new oracle.jdbc.OracleDriver());
//            java.sql.Connection conn = DriverManager.getConnection("jdbc:oracle:thin:IBS/Pustobreh1937@//test03.msk.russb.org:1521/rbotest8.msk.russb.org");
//            oracle.jdbc.OracleConnection oraConn = conn.unwrap(oracle.jdbc.OracleConnection.class); //(oracle.jdbc.OracleConnection)conn;
//            String[] bonuses = new String[4];
//            bonuses[0] = "1000000005#1000#20161228100911#списание бонусов";
//            bonuses[1] = "1000000006#100#20161228100912#начисление бонусов";
//            bonuses[2] = "1000000007#1000#20161228100913#начисление бонусов";
//            bonuses[3] = "1000000008#1000#20161228100914#начисление бонусов";
//            java.sql.Array arr = oraConn.createARRAY("OTT_BONUS_SERIAL", bonuses);
//            OneToTripAPI.addFunds(arr);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

//      work
//        try{
//            Date date = new Date();
//            SimpleDateFormat sdf= new SimpleDateFormat("yyyyMMddHHmmSSS");
//            System.out.println(OneToTripAPI.addFund("1000000005",1000, sdf.format(date), "бонусы!"));
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

//        try{
//            OneToTripAPI.newMovements();
//        }catch (Exception e) {
//            e.printStackTrace();
//        }



    }
}
