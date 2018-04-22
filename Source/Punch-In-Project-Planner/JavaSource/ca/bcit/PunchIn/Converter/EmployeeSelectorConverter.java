package ca.bcit.PunchIn.Converter;

import java.util.ArrayList;
import java.util.List;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.inject.Inject;
import javax.inject.Named;

import ca.bcit.PunchIn.DAOs.EmployeeDAO;
import ca.bcit.PunchIn.Entities.Employee.EmployeeEntity;
import ca.bcit.PunchIn.Models.Employee.Employee;

@Named
// @FacesConverter(value = "projectManagerSelectorConverter")
public class EmployeeSelectorConverter implements Converter {

	@Inject
	private EmployeeDAO empDAO;

	@Override
	public Object getAsObject(FacesContext contex, UIComponent component, String str) {
		Object ret = null;
		if (component != null) {
			List<EmployeeEntity> dl = null;
			if (empDAO != null) {
				dl = new ArrayList<EmployeeEntity>();
				for(Employee e : empDAO.getEmployees())
				{
					dl.add(e.getBasicInfo());
				}
			} else {
				 //System.out.println("empDAO is null");
			}
			for (EmployeeEntity e : dl) {
				// System.out.println(e);
				// String id = "" + e.getCredentials().getUserName() +"(" + e.getFirstName()+" "
				// + e.getLastName()+ ")";
				String id = "" + e.getFirstName() + " " + e.getLastName();
				if (str.equals(id)) {
					ret = e;
					break;
				}
			}
			if (ret == null)
				for (EmployeeEntity e : dl) {
					// String id = "" + e.getCredentials().getUserName() +"(" + e.getFirstName()+" "
					// + e.getLastName()+ ")";
					String id = "" + e.getFirstName() + " " + e.getLastName();
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
		// System.out.println("Enter pm converter: getAsString");
		if (entity instanceof EmployeeEntity) {
			//System.out.println("EmployeeEntity");
			EmployeeEntity e = (EmployeeEntity) entity;
			str = "" + e.getFirstName() + " " + e.getLastName();
		}
		return str;

	}

}
