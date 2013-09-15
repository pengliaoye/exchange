/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dm.system;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import org.owasp.esapi.ESAPI;
import org.owasp.esapi.User;
import org.owasp.esapi.errors.AuthenticationException;

/**
 *
 * @author Administrator
 */
@ManagedBean
@RequestScoped
public class UserBean {

    private String username;
    private String password1;
    private String password2;

    /**
     * Creates a new instance of UserBean
     */
    public UserBean() {
    }

    public void createUser() {
        try {
            ESAPI.authenticator().createUser(username, password1, password2);
        } catch (AuthenticationException ex) {
            Logger.getLogger(UserBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void login() {
        User user = ESAPI.authenticator().login();
        if (user != null) {
            if (request.getParameter("remember") != null) {
                ESAPI.httpUtilities().setRememberToken(request.getParameter("password"), 8000, "", "");
            }
        }
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword1() {
        return password1;
    }

    public void setPassword1(String password1) {
        this.password1 = password1;
    }

    public String getPassword2() {
        return password2;
    }

    public void setPassword2(String password2) {
        this.password2 = password2;
    }
}
