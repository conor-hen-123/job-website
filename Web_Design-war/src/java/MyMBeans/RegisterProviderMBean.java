/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MyMBeans;

import MyBeans.ProviderBeanRemote;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;

/* Request Scoped bean used to register a provider*/
@Named(value = "registerProviderMBean")
@RequestScoped
public class RegisterProviderMBean {
    
    //ProviderBeanRemote object to call methods of EJB ProviderBean
    @EJB
    ProviderBeanRemote provider;
    
    //variables to store the attributes of a provider
    String name,username,password;

    /**
     * Creates a new instance of RegisterProviderMBean
     */
    public RegisterProviderMBean() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    
    /*******************************************************register Method********************************************************
    * This method is used to create a new provider in the database
    * It calls the registerProvider method of the ProviderBean EJB to store the new provider in the database
    * It passes all the required values as parameter to the method
    *******************************************************************************************************************************/
    public void register(){
        provider.registerProvider(name,username, password);
    }
}
