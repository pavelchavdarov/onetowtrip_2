/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package onetowtrip;

import java.util.ArrayList;

/**
 *
 * @author p.chavdarov
 */
public class BonusAck {
    ArrayList<BonusStatusAck> movements;
    public BonusAck(){
        movements = new ArrayList<BonusStatusAck>();
    }
}

class BonusStatusAck{
    private String  movId;
    private String  status;
    private String  error;
    private Boolean success;

    public String getMovId() {
        return movId;
    }

    public void setMovId(String movId) {
        this.movId = movId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }
    
    
}