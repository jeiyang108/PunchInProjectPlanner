package ca.bcit.PunchIn.Converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import org.primefaces.component.picklist.PickList;
import org.primefaces.model.DualListModel;

import ca.bcit.PunchIn.Entities.Employee.EmployeeEntity;

@FacesConverter(value = "employeePickListConverter")
public class EmployeePickListConverter implements Converter {

	@Override
	public Object getAsObject(FacesContext contex, UIComponent component, String str) {
		 Object ret = null;
		    if (component instanceof PickList) {
		        Object dualList = ((PickList) component).getValue();
		        @SuppressWarnings("unchecked")
				DualListModel<EmployeeEntity> dl = (DualListModel<EmployeeEntity>) dualList;
		        for (EmployeeEntity e : dl.getSource()) {
		            //String id = "" + e.getCredentials().getUserName() +"(" + e.getFirstName()+" " + e.getLastName()+ ")";
		        	String id = "" + e.getFirstName()+ " " + e.getLastName();
		            if (str.equals(id)) {
		                ret = e;
		                break;
		            }
		        }
		        if (ret == null)
		            for ( EmployeeEntity e : dl.getTarget()) {
		            	//String id = "" + e.getCredentials().getUserName() +"(" + e.getFirstName()+" " + e.getLastName()+ ")";
		            	String id =   ""  + e.getFirstName()+ " " + e.getLastName();
		                if (str.equals(id)) {
		                    ret = e;
		                    break;
		                }
		            }
		    }
		    return ret;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object entity) {
		// TODO Auto-generated method stub
		 String str = "";
		    if (entity instanceof EmployeeEntity) {
		    	EmployeeEntity e = (EmployeeEntity) entity;
		        str = "" + e.getFirstName()+" " + e.getLastName();
		    }
	    return str;

	}
	

}
