package ca.bcit.PunchIn.Converter;

import java.util.List;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.component.selectonemenu.SelectOneMenu;

import ca.bcit.PunchIn.DAOs.EmployeeDAO;
import ca.bcit.PunchIn.Entities.Employee.EmployeeEntity;

@Named
// @FacesConverter(value = "projectManagerSelectorConverter")
public class ProjectManagerSelectorConverter implements Converter {

	@Inject
	private EmployeeDAO empDAO;

	@Override
	public Object getAsObject(FacesContext contex, UIComponent component, String str) {
		Object ret = null;
		//System.out.println("Enter pm converter : getAsObject");
		//if (component instanceof SelectOneMenu) {
		if (component != null) {
			 //System.out.println("SelectOneMenu, " + component.getClass().getName());
			List<EmployeeEntity> dl = null;
			if (empDAO != null) {
				dl = empDAO.getProjectManagers();
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
