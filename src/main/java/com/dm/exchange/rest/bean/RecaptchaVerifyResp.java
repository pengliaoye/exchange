package com.dm.exchange.rest.bean;

import java.util.List;
import javax.xml.bind.annotation.XmlElement;

public class RecaptchaVerifyResp {

    private boolean success;

    @XmlElement(name = "error-codes")
    private List<String> errorCodes;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public List<String> getErrorCodes() {
        return errorCodes;
    }

    public void setErrorCodes(List<String> errorCodes) {
        this.errorCodes = errorCodes;
    }

}
