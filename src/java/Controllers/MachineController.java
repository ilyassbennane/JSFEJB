package Controllers;

import Entities.Machine;
import Controllers.util.JsfUtil;
import Controllers.util.JsfUtil.PersistAction;
import Services.MachineFacade;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.chart.Axis;
import javafx.scene.chart.CategoryAxis;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.BarChartModel;
import org.primefaces.model.chart.ChartSeries;

@ManagedBean(name = "machineController")
@SessionScoped
public class MachineController implements Serializable {

    @EJB
    private Services.MachineFacade ejbFacade;
    private List<Machine> items = null;
    private Machine selected;

    public MachineController() {
    }

    public Machine getSelected() {
        return selected;
    }

    public void setSelected(Machine selected) {
        this.selected = selected;
    }

    protected void setEmbeddableKeys() {
    }

    protected void initializeEmbeddableKey() {
    }

    private MachineFacade getFacade() {
        return ejbFacade;
    }

    public Machine prepareCreate() {
        selected = new Machine();
        initializeEmbeddableKey();
        return selected;
    }

    public void create() {
        persist(PersistAction.CREATE, ResourceBundle.getBundle("/Bundle").getString("MachineCreated"));
        if (!JsfUtil.isValidationFailed()) {
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public void update() {
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("MachineUpdated"));
    }

    public void destroy() {
        persist(PersistAction.DELETE, ResourceBundle.getBundle("/Bundle").getString("MachineDeleted"));
        if (!JsfUtil.isValidationFailed()) {
            selected = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public List<Machine> getItems() {
        if (items == null) {
            items = getFacade().findAll();
        }
        return items;
    }

    private void persist(PersistAction persistAction, String successMessage) {
        if (selected != null) {
            setEmbeddableKeys();
            try {
                if (persistAction != PersistAction.DELETE) {
                    getFacade().edit(selected);
                } else {
                    getFacade().remove(selected);
                }
                JsfUtil.addSuccessMessage(successMessage);
            } catch (EJBException ex) {
                String msg = "";
                Throwable cause = ex.getCause();
                if (cause != null) {
                    msg = cause.getLocalizedMessage();
                }
                if (msg.length() > 0) {
                    JsfUtil.addErrorMessage(msg);
                } else {
                    JsfUtil.addErrorMessage(ex, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
                }
            } catch (Exception ex) {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
                JsfUtil.addErrorMessage(ex, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            }
        }
    }

    public List<Machine> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<Machine> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }

    @FacesConverter(forClass = Machine.class)
    public static class MachineControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            MachineController controller = (MachineController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "machineController");
            return controller.getFacade().find(getKey(value));
        }

        java.lang.Integer getKey(String value) {
            java.lang.Integer key;
            key = Integer.valueOf(value);
            return key;
        }

        String getStringKey(java.lang.Integer value) {
            StringBuilder sb = new StringBuilder();
            sb.append(value);
            return sb.toString();
        }

        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof Machine) {
                Machine o = (Machine) object;
                return getStringKey(o.getId());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), Machine.class.getName()});
                return null;
            }
        }

    }
    ///chart 

    public BarChartModel getMachineChartModel() {
    BarChartModel chartModel = new BarChartModel();
        ChartSeries series = new ChartSeries();
    series.setLabel("Machines Acquises");

        Map<String, Integer> machinesParAnnee = getFacade().countMachinesParAnnee();

    for (Map.Entry<String, Integer> entry : machinesParAnnee.entrySet()) {
        series.set(entry.getKey(), entry.getValue());
    }

    chartModel.addSeries(series);

    chartModel.setTitle("Nombre de Machines Acquises par Annee");
    chartModel.setLegendPosition("ne");


    return chartModel;
}

}
