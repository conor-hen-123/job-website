/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MyMBeans;

import MyBeans.FreelanceProfileBeanRemote;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;

/* Request Scoped bean used to register a freelancer*/
@Named(value = "registerFreelancerMBean")
@RequestScoped
public class RegisterFreelancerMBean {
    
    //FreelanceProfileBeanRemote object to call methods of EJB FreelanceProfileBean
    @EJB
    FreelanceProfileBeanRemote freelance;
    
     //variables to store the attributes of a freelancer
    String name,description,skills,username,password;

    /**
     * Creates a new instance of RegisterFreelancerMBean
     */
    public RegisterFreelancerMBean() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSkills() {
        return skills;
    }

    public void setSkills(String skills) {
        this.skills = skills;
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
    * This method is used to create a new freelancer in the database
    * It calls the registerFreelancer method of the FreelanceProfileBean EJB to store the new provider in the database
    * It passes all the required values as parameter to the method
    *******************************************************************************************************************************/
    public void register(){
        freelance.registerFreelancer(name, skills, description, username, password);
    }
    
    
    
}
