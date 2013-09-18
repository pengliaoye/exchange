/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dm.system;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.enterprise.inject.Model;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

import org.owasp.esapi.ESAPI;
import org.owasp.esapi.User;
import org.owasp.esapi.errors.AuthenticationException;

/**
 *
 * @author Administrator
 */
@Model
public class UserBean {

    private String username;
    private String password;

    /**
     * Creates a new instance of UserBean
     */
    public UserBean() {
    }

    public void createUser() {
        try {
            ESAPI.authenticator().createUser(username, password, password);
        } catch (AuthenticationException ex) {
            Logger.getLogger(UserBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void login() {
        
        User user = null;
        try {
            user = ESAPI.authenticator().login();
        } catch (AuthenticationException ex) {
            Logger.getLogger(UserBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        if (user != null) {
            HttpServletRequest request = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    
}
