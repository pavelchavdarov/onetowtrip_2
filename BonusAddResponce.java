package onetowtrip;

import java.util.ArrayList;

/**
 * Created by p.chavdarov on 26/12/2016.
 */
public class BonusAddResponce {
    private ArrayList<BonusResponce> bonuses;

    public BonusAddResponce() {
        bonuses = new ArrayList<BonusResponce>();
    }

    public ArrayList<BonusResponce> getBonuses() {
        return bonuses;
    }

    public void setBonuses(ArrayList<BonusResponce> bonuses) {
        this.bonuses = bonuses;
    }
}

class BonusResponce extends BonusRequest {
    private Boolean success;
    private String error;

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}