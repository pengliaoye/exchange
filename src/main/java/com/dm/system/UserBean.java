/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dm.system;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.enterprise.inject.Model;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;

import org.owasp.esapi.Authenticator;
import org.owasp.esapi.ESAPI;
import org.owasp.esapi.User;
import org.owasp.esapi.errors.AuthenticationException;

import com.dm.exchange.Constants;
import com.dm.exchange.rest.RestClient;
import com.dm.exchange.rest.bean.RecaptchaVerifyResp;
import com.dm.service.UserService;
import com.dm.util.JsfUtil;

/**
 *
 * @author Administrator
 */
@Model
public class UserBean {

    private static final Logger logger = Logger.getLogger(UserBean.class.getName());

    @Inject
    private RestClient client;
        
	@Inject
	private UserService userService;

    @NotNull(message="{user.username}")
    private String username;
    @NotNull(message="{user.password}")
    private String password;
    @NotNull(message="{login.recaptcha.response}")
    private String recaptchaResp;
    
    private String fullname;
    private String email;
    private String address;
    private String city;
    private String country;
    private String rpassword;

    Authenticator authenticator = ESAPI.authenticator();

    /**
     * Creates a new instance of UserBean
     */
    public UserBean() {
    }

    public void createUser() {
        try {
            //password = authenticator.generateStrongPassword();
            System.out.println(password);
            User user = authenticator.createUser(username, password, rpassword);
            com.dm.entity.User usr = new com.dm.entity.User();
            usr.setId(user.getAccountId());
            usr.setFullName(fullname);
            usr.setEmail(email);
            usr.setAddress(address);
            usr.setCity(city);
            usr.setCountry(country);
            userService.edit(usr); 
        } catch (AuthenticationException ex) {
            logger.log(Level.SEVERE, ex.getLogMessage(), ex);
            JsfUtil.addErrorMessage(ex.getUserMessage());
        }
    }

    public String login() {

    	//RecaptchaVerifyResp verifyResp = client.recaptchaVerify(Constants.G_RECAPTCHA_SECRET, recaptchaResp);
        User user = null;
        try {
            user = authenticator.login();

            if (user != null) {
                HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
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

    public void changePwd() {
        // authenticator.getCurrentUser().changePassword(oldPassword, newPassword1, newPassword2)
    }

    public void removeUser() {
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

    public String getRecaptchaResp() {
        return recaptchaResp;
    }

    public void setRecaptchaResp(String recaptchaResp) {
        this.recaptchaResp = recaptchaResp;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getRpassword() {
        return rpassword;
    }

    public void setRpassword(String rpassword) {
        this.rpassword = rpassword;
    }

    
}
