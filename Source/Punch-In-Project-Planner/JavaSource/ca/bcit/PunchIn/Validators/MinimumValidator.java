package ca.bcit.PunchIn.Validators;

import java.math.BigDecimal;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

@FacesValidator("MinVal")
public class MinimumValidator implements Validator {
    private int lowerBound;
    private int upperBound;
    private int val;

    @Override
    public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {
      //some values don't need an upperbound. setting upperBound variable to highest an int can hold
        System.out.println("In validator");
        if(component.getAttributes().get("upperBound").equals("max")) {
            lowerBound = Integer.valueOf((String) component.getAttributes().get("lowerBound"));
            upperBound = Integer.MAX_VALUE;
        } else {
            lowerBound = Integer.valueOf((String) component.getAttributes().get("lowerBound"));
            upperBound = Integer.valueOf((String) component.getAttributes().get("upperBound"));
        }
        
        //values must be standardized to int
        if(value instanceof BigDecimal) {
            val = ((BigDecimal) value).intValueExact();
        } else if (value instanceof Long){
            val = ((Long) value).intValue();
        } else if (value instanceof Double) {
            if (Double.compare((Double) value, Double.valueOf(0.0)) < 0){
                FacesMessage msg = new FacesMessage (component.getAttributes().get("msg").toString());
                throw new ValidatorException(msg);
            }
        }
        
        //actual validation. checking value is between lowerBound & upperBound
        if (val < lowerBound || val > upperBound) {
            FacesMessage msg = new FacesMessage (component.getAttributes().get("msg").toString());
            System.out.println("Not valid");
            throw new ValidatorException(msg);
        }
    }
}
