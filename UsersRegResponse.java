package onetowtrip;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by p.chavdarov on 26/12/2016.
 */

public class UsersRegResponse {
    private List<UserResponce> users;

    public UsersRegResponse(){
        users = new ArrayList<UserResponce>();
    }

    public List<UserResponce> getUsers() {
        return users;
    }

    public void setUsers(List<UserResponce> users) {
        this.users = users;
    }
}

class UserResponce {
    private String uid;
    private Boolean success;
    private String error;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

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
