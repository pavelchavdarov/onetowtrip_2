/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package onetowtrip;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author p.chavdarov
 */
public class BonusAddRequest {
    private boolean ignoreDuplicate;
    protected List<BonusRequest> bonuses;

    public BonusAddRequest() {
        bonuses = new ArrayList<BonusRequest>();
        ignoreDuplicate = false;
    }

    public boolean isIgnoreDuplicate() {
        return ignoreDuplicate;
    }

    public void setIgnoreDuplicate(boolean ignoreDuplicate) {
        this.ignoreDuplicate = ignoreDuplicate;
    }

//    public List<BonusRequest> getBonuses() {
//        return bonuses;
//    }
//
//    public void setBonuses(List<BonusRequest> bonuses) {
//        this.bonuses = bonuses;
//    }
    
}

class BonusRequest {
    private String uid;
    private Integer amount;
    private String operId;
    private String desc;

    
    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public String getOperId() {
        return operId;
    }

    public void setOperId(String operId) {
        this.operId = operId;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

}

