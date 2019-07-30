/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MyMBeans;

import MyBeans.AdminBeanRemote;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import javax.ejb.EJB;

/* Session Scoped bean to store details of the logged in admin user 
 * contains methods for basic admin tasks*/
@Named(value = "adminMBean")
@SessionScoped
public class AdminMBean implements Serializable {
    
    //Create object of the EJB for admin user
    @EJB
    AdminBeanRemote adm;
    
    //variables to store the admin information
    String username,password,name;
    boolean correct=false;

    /**
     * Creates a new instance of AdminMBean
     */
    public AdminMBean() {
    }

    public boolean isCorrect() {
        return correct;
    }

    public void setCorrect(boolean correct) {
        this.correct = correct;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    /************************************************searchAdminName Method*****************************************************
    * This method is used to find the admin name that matches the username which is entered by the user during login
    * It calls the searchAdminName method in the EJB for the admin
    * It sets the variable correct to true or false depending upon successful or unsuccessful login
     * @return 
    *****************************************************************************************************************************/
    public String searchAdminName() {
        //store the results returned from searchAdminName method
        String[] data = adm.searchAdminName(username,password);
        //check for successful or unsuccessful login
        if(data[1]=="correct"){
            //if success, assign true
            correct=true;
        }
        else{
            //else assign false
            correct=false;
        }
        //get the name of the logged in Admin from the data that was returned
        String uname = (String) data[0];
        return uname;
        
    }
    
    /************************************************logout Method*****************************************************
    * This method is used to find when the admin logs out
    * the admin info is erased
    ********************************************************************************************************************/
    public void logout(){
        //erase the store admin info
        username="";
        password="";
    }
    
}
