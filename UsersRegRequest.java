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
public class UsersRegRequest {
    private boolean ignoreDuplicate;
    protected List<UserRequest> users;

    public UsersRegRequest() {
        users = new ArrayList<UserRequest>();
        ignoreDuplicate = false;
    }

    public boolean isIgnoreDuplicate() {
        return ignoreDuplicate;
    }

    public void setIgnoreDuplicate(boolean ignoreDuplicate) {
        this.ignoreDuplicate = ignoreDuplicate;
    }

}

class UserRequest {
    private String uid;
    private String firstName;
    private String lastName;
    private String middleName;
    private String email;
    private String phone;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
