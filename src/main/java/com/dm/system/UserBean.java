/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dm.system;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.enterprise.inject.Model;
import javax.faces.bean.ManagedProperty;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import org.owasp.esapi.Authenticator;
import org.owasp.esapi.ESAPI;
import org.owasp.esapi.User;
import org.owasp.esapi.errors.AuthenticationException;

import com.dm.exchange.Constants;
import com.dm.exchange.rest.RestClient;
import com.dm.exchange.rest.bean.RecaptchaVerifyResp;
import com.dm.util.JsfUtil;

/**
 *
 * @author Administrator
 */
@Model
public class UserBean {
	
	private Logger logger = Logger.getLogger(UserBean.class.getName());
	
	@Inject
	private RestClient client;

    private String username;
    private String password;
    
    @ManagedProperty(value="#{param.g-recaptcha-response}")
    private String recaptchaResp;
    Authenticator authenticator = ESAPI.authenticator();

    /**
     * Creates a new instance of UserBean
     */
    public UserBean() {
    }

    public void createUser() {
        try {        	
        	password = authenticator.generateStrongPassword();
                System.out.println(password);
        	authenticator.createUser(username, password, password);
        } catch (AuthenticationException ex) {
            logger.log(Level.SEVERE, ex.getLogMessage(), ex);
            JsfUtil.addErrorMessage(ex.getUserMessage());
        }
    }

    public String login() {
    	
    	RecaptchaVerifyResp verifyResp = client.recaptchaVerify(Constants.G_RECAPTCHA_SECRET, recaptchaResp);
        
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
            logger.log(Level.SEVERE, ex.getLogMessage(), ex);
            JsfUtil.addErrorMessage(ex.getUserMessage());
            return null;
        }

    }
    
    public void changePwd(){
    	// authenticator.getCurrentUser().changePassword(oldPassword, newPassword1, newPassword2)
    }
    
    public void removeUser(){
    	// authenticator.removeUser(accountName);
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
