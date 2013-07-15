/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dm.validator;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;
import javax.servlet.http.Part;

/**
 *
 * @author Administrator
 */
@FacesValidator(value="FileValidator")
public class FileValidator implements Validator{

    @Override
    public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {
        Part file = (Part)value;
    }
    
}
