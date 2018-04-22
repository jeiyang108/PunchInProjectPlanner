package ca.bcit.PunchIn.Validators;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

@FacesValidator("NotNull")
public class NullItemValidator implements Validator{
    @Override
    public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {
        if(value instanceof String) {
            if(((String) value).isEmpty()) {
                FacesMessage msg = new FacesMessage (component.getAttributes().get("msg").toString());
                throw new ValidatorException(msg);
            }
        } else {
            if(value == null) {
                FacesMessage msg = new FacesMessage (component.getAttributes().get("msg").toString());
                throw new ValidatorException(msg);
            }
        }
    }
}
