/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package onetowtrip;

/**
 *
 * @author p.chavdarov
 */

import com.google.gson.Gson;
import java.io.IOException;
import java.util.ArrayList;


public class OneTowTrip {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args){
        String json_string = "";
        String answer = "";
        Gson gson = new Gson();
        ConnectionInterface conn = new Connection();
        
        
        conn.initConnection("https://api.twiket.com/mt/registerUsers", "POST", "api@rgsbank", "fhoouihfhoouih");

        UsersRegRequest udata = new UsersRegRequest();
        udata.setIgnoreDuplicate(false);

        UserRequest urec = new UserRequest();
        urec.setUid("1000000006");

        udata.users.add(urec);

        json_string = gson.toJson(udata, UsersRegRequest.class);
        try {
            conn.sendData(json_string);
        } catch (IOException ex) {

            System.out.println(ex);
        }

        try {
            answer = conn.getData();
            if (answer != null){
                UsersRegResponse regAnswer = gson.fromJson(answer, UsersRegResponse.class);
                System.out.println("success: " + regAnswer.getUsers().get(0).getSuccess());
            }
        } catch (IOException ex) {
            System.out.println(ex);
        }

//====================================
//        conn.initConnection("https://api.twiket.com/mt/addFunds", "POST", "api@rgsbank", "fhoouihfhoouih");
//
//
//        BonusRequest bdata = new BonusRequest();
//        bdata.setUid("1000000002");
//        bdata.setAmount(-2900000);
//        bdata.setOperId("20161226173512");
//        bdata.setDesc("списание бонусов");
//
//        BonusAddRequest bonus = new BonusAddRequest();
//        bonus.setIgnoreDuplicate(false);
//        ArrayList<BonusRequest> bonuslist = new ArrayList<BonusRequest>();
//        bonuslist.add(bdata);
//        bonus.setBonuses(bonuslist);
//
//
//        json_string = gson.toJson(bonus, BonusAddRequest.class);
//        try {
//            conn.sendData(json_string);
//        } catch (IOException ex) {
//            ex.printStackTrace();
//        }
//        try {
//            answer = conn.getData();
//        } catch (IOException ex) {
//            ex.printStackTrace();
//        }
//        System.out.println("answer: " + answer);
//            BonusAddResponce bonusResponse = new BonusAddResponce();
//            bonusResponse = gson.fromJson(answer, BonusAddResponce.class);
//            System.out.println("uid: " + bonusResponse.getBonuses().get(0).getUid());
//            System.out.println("success: " + bonusResponse.getBonuses().get(0).getSuccess());
//            System.out.println("error: " + bonusResponse.getBonuses().get(0).getError());
//
////       //====================================
       
//       conn.initConnection("https://api.twiket.com/mt/newMovements", "GET", "api@rgsbank", "fhoouihfhoouih");
//
//       BonusMovements b_moves = new BonusMovements();
//
//       try {
//            answer = conn.getData();
//        } catch (IOException ex) {
//            ex.printStackTrace();
//        }
//       System.out.println(answer);
//       if (answer.length() >0){
//           b_moves = gson.fromJson(answer, BonusMovements.class);
//           for (BonusMovement i : b_moves.movements){
//               System.out.println("movId: "+ i.getMovId());
//               System.out.println("uid: "+ i.getUid());
//               System.out.println("amount: "+ String.valueOf(i.getAmount()));
//           }
//       }
       //====================================
//       conn.initConnection("https://api.twiket.com/mt/updateMovementsState", "POST", "api@rgsbank", "fhoouihfhoouih");
//       
//       BonusAck ack = new BonusAck();
//       BonusStatusAck status_ack = new BonusStatusAck();
//       
//       status_ack.setMovId("bbea05e2-6dae-47f9-be71-2cb161480547");
//       status_ack.setStatus("ack_error");
//       status_ack.setError("тестовая отмена");
//       
//       ack.movements.add(status_ack);
//       
//       json_string = gson.toJson(ack, BonusAck.class);
//       System.out.println("json_string: " + json_string);
//       try {
//            conn.sendData(json_string);
//        } catch (IOException ex) {
//            Logger.getLogger(OneTowTrip.class.getName()).log(Level.SEVERE, null, ex);
//        }
//       try {
//            answer = conn.getData();
//        } catch (IOException ex) {
//            Logger.getLogger(OneTowTrip.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        System.out.println("answer: " + answer);
//        
//        ack = gson.fromJson(answer, BonusAck.class);
//        for (BonusStatusAck a: ack.movements){
//            System.out.println("movId: "+ a.getMovId());
//            System.out.println("success: "+ a.getSuccess());
//            System.out.println("error: "+ a.getError());
//        }


    }
    
}
