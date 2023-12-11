/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controllers;

/**
 *
 * @author PC
 */
import Services.AdminService;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.context.RequestContext;


import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

@ManagedBean
@SessionScoped
public class AdminBean {

    private String userName;
    private String password;

    @Inject
    private AdminService adminService;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public AdminService getAdminService() {
        return adminService;
    }

    public void setAdminService(AdminService adminService) {
        this.adminService = adminService;
    }


// AdminBean.java
public String login() {
    if (adminService.authenticate(userName, password)) {
        return "/faces/employe/List.xhtml";
    } else {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Authentification échouée", null));
        
        // Update and show the dialog
        RequestContext.getCurrentInstance().update("loginDialog");
        RequestContext.getCurrentInstance().execute("PF('loginDialogVar').show()");
        
        return null; 
    }
}

}

