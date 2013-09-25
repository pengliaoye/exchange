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

import org.owasp.esapi.Authenticator;
import org.owasp.esapi.ESAPI;
import org.owasp.esapi.User;
import org.owasp.esapi.errors.AuthenticationException;

import com.dm.util.JsfUtil;

/**
 *
 * @author Administrator
 */
@Model
public class UserBean {
	
	private Logger logger = Logger.getLogger(UserBean.class.getName());

    private String username;
    private String password;
    Authenticator authenticator = ESAPI.authenticator();

    /**
     * Creates a new instance of UserBean
     */
    public UserBean() {
    }

    public void createUser() {
        try {        	
        	password = authenticator.generateStrongPassword();
        	authenticator.createUser(username, password, password);
        } catch (AuthenticationException ex) {
            logger.log(Level.SEVERE, ex.getLogMessage(), ex);
            JsfUtil.addErrorMessage(ex.getUserMessage());
        }
    }

    public String login() {
        
        User user = null;
        try {
            user = authenticator.login();
            
            if (user != null) {
                HttpServletRequest request = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
                if (request.getParameter("remember") != null) {
                    ESAPI.httpUtilities().setRememberToken(request.getParameter("password"), 8000, "", "");
                }
            }
            
            return "index";
        } catch (AuthenticationException ex) {
            logger.log(Level.SEVERE, null, ex);
            return null;
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
