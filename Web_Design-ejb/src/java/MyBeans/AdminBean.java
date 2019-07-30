/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MyBeans;

import entities.Admin;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/*Session Bean to perform Admin realted tasks*/
@Stateless
public class AdminBean implements AdminBeanRemote{
    
    //entity manager object to interact with the database
    @PersistenceContext(name= "Web_Design")
    private EntityManager em;

    /************************************************searchAdminName Method*****************************************************
    * This method is used to find the admin name that matches the username which is entered by the user during login
    * It accepts username and password as parameters which are entered by the admin during login
    * It uses a named query to search the database for the username and compares the entered password with the stored password
    * It returns a String array which contains the name of the admin and a variable to flag correct password
     * @param username
     * @param password
     * @return 
    *****************************************************************************************************************************/
    @Override
    public String[] searchAdminName(String username,String password) {
        String[] data=new String[2]; //String array to store the results
        try{
            //Named query to be executed to get the admin object from database
            Query query = em.createNamedQuery("Admin.findByUsername").setParameter("username", username);
            //the result is stored in an admin object
            Admin adm = (Admin)query.getResultList().get(0);
            //store the name of the admin
            data[0] = adm.getName();
            //check if the entered password matches the stored password
            if(adm.getPassword().equals(password)){
                data[1] = "correct"; 
            }
            else{
                data[1] = "incorrect";
            }
        }
        //exception occurs if the username entered is incorrect and the query returns no results
        catch(Exception e){
            data[0] = "Error";
            data[1] = "incorrect";
        }
        //return the results
        return data;
    }

    /************************************************searchAdminName Method*****************************************************
    * This method is used to find the admin object that matches the username which is entered by the user during login
    * It accepts username as parameter which are entered by the admin during login
    * It uses a named query to search the database for the username. It then returns the result which is list of admin objects
     * @param username
     * @return 
    *****************************************************************************************************************************/
    @Override
    public List<Admin> searchAdmin(String username) {
        //Named query to be executed to get the admin object from database
        Query query = em.createNamedQuery("Admin.findByUsername").setParameter("username", username);
        //return the list of admin objects
        return query.getResultList();
    }
    
}
