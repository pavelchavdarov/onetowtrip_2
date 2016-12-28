package onetowtrip;

import java.sql.DriverManager;

/**
 * Created by p.chavdarov on 26/12/2016.
 */
public class APITest {
    public static void main(String[] args){
        OneToTripAPI.init();

        String uid = "1000000007";
//        try {
//            System.out.println(OneToTripAPI.registerUsers(uid));
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

        try {
            DriverManager.registerDriver (new oracle.jdbc.OracleDriver());
            java.sql.Connection conn = DriverManager.getConnection("jdbc:oracle:thin:IBS/Pustobreh1937@//test03.msk.russb.org:1521/rbotest8.msk.russb.org");
            oracle.jdbc.OracleConnection oraConn = (oracle.jdbc.OracleConnection)conn;
            String[] bonuses = new String[3];
            bonuses[0] = "1000000005#1000#20161228100911#списание бонусов";
            bonuses[1] = "1000000006#100#20161228100912#начисление бонусов";
            bonuses[2] = "1000000007#1000#20161228100913#начисление бонусов";
            java.sql.Array arr = oraConn.createARRAY("OTT_BONUS_SERIAL".toUpperCase(), bonuses);

            System.out.println(OneToTripAPI.addFunds(arr));
        } catch (Exception e) {
            e.printStackTrace();
        }
//        String q = "1000000002#-2900000#20161226173512#списание бонусов";
//        String[] qa = q.split("#");
//
//        BonusRequest bdata = new BonusRequest();
//        bdata.setUid("1000000002");
//        bdata.setAmount(-2900000);
//        bdata.setOperId("20161213142012");
//        bdata.setDesc("списание бонусов");
//
////        ArrayList<BonusRequest> bonuslist = new ArrayList<BonusRequest>();
//
//        BonusAddRequest bonus = new BonusAddRequest();
//        bonus.setIgnoreDuplicate(true);
//        bonus.bonuses.add(bdata);
//
//        oracle.jdbc.OracleDriver ora = new oracle.jdbc.OracleDriver();
//        java.sql.Connection conn = null;
//        try {
//            conn = ora.defaultConnection();
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        oracle.jdbc.OracleConnection oraConn = (oracle.jdbc.OracleConnection)conn;
//
//
//        try {
//            oracle.sql.ARRAY array = oraConn.createARRAY("OTT_BONUS_CHARGE_REQEST", bonuslist);
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }


    }
}
