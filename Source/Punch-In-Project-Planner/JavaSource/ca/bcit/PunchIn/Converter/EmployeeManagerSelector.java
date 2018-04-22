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
public class EmployeeManagerSelector implements Converter {

	@Inject
	private EmployeeDAO empDAO;

	@Override
	public Object getAsObject(FacesContext contex, UIComponent component, String str) {
		Object ret = null;
		if (component != null) {
			System.out.println("Gathering component.");
			List<EmployeeEntity> dl = null;
			if (empDAO != null) {
				dl = empDAO.getProjectManagers();
				
			} else {
				 //System.out.println("empDAO is null");
			}
			for (EmployeeEntity e : dl) {
				String id = "" + e.getFirstName() + " " + e.getLastName();
				if (str.equals(id)) {
					ret = e;
					break;
				}
			}
			if (ret == null)
				for (EmployeeEntity e : dl) {
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
