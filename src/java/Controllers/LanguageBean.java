package Controllers;

import java.io.Serializable;
import java.util.Locale;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.event.ValueChangeEvent;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.context.FacesContext;

@ManagedBean
@SessionScoped
public class LanguageBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private String selectedLanguage = "fr"; // Default language is English

    private static Map<String, Object> supportedLanguages;

    static {
        supportedLanguages = new LinkedHashMap<>();
           supportedLanguages.put("French", "fr");
        supportedLanguages.put("English", "en");
     
        // Add more languages as needed
    }

    public Map<String, Object> getSupportedLanguages() {
        return supportedLanguages;
    }

    public String getSelectedLanguage() {
        return selectedLanguage;
    }

    public void setSelectedLanguage(String selectedLanguage) {
        this.selectedLanguage = selectedLanguage;
    }

    // Value change event listener
    public void changeLanguage(ValueChangeEvent event) {
        handleChangeLanguage(event.getNewValue().toString());
    }

    // Ajax behavior event listener
    public void changeLanguage(AjaxBehaviorEvent event) {
        handleChangeLanguage(event.getComponent().getAttributes().get("value").toString());
    }

    private void handleChangeLanguage(String newLocaleValue) {
        // Loop through supported languages map to compare the locale code
        for (Map.Entry<String, Object> entry : supportedLanguages.entrySet()) {
            if (entry.getValue().toString().equals(newLocaleValue)) {
                FacesContext.getCurrentInstance().getViewRoot().setLocale(new Locale(newLocaleValue));
            }
        }
    }
}
