/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MyBeans;

import entities.Freelancers;
import entities.Jobs;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/*Session Bean to perform Freelancer related tasks*/
@Stateless
public class FreelanceProfileBean implements FreelanceProfileBeanRemote {

   //entity manager object to interact with the database
   @PersistenceContext(name= "Web_Design")
   private EntityManager em;
   
   @Override
    public List<Freelancers> searchFreelancers() {
        // create query string - attention: use of string
        // concatenation enables Injection Attacks!!!!
        // !!! do not use this in a real program !!!
        // create query
        Query query = em.createNamedQuery("Freelancers.findAll");
        // return query result
        return query.getResultList();
    }
    
    /************************************************getFreelancerName Method***************************************************
    * This method is used to return the freelancer that matches the username which is entered by the user during login
    * It accepts username and password as parameters which are entered by the user during login
    * It uses a named query to search the database for the username and compares the entered password with the stored password
    * It returns a String array which contains the name of the freelancer and a variable to flag correct password
     * @param username
     * @param password
     * @return 
    *****************************************************************************************************************************/
   @Override
    public String[] getFreelancerName(String username,String password) {
        String[] data=new String[2]; //String array to store the results
        try{
            //Named query to be executed to get the freelancer object from database
            Query query = em.createNamedQuery("Freelancers.findByUsername").setParameter("username", username);
            //the result is stored in an freelancer object
            Freelancers free = (Freelancers)query.getResultList().get(0);
            //store the name of the freelancer
            data[0] = free.getName();
            //check if the entered password matches the stored password
            if(free.getPassword().equals(password)){
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
        return data;
    }
    
    /************************************************getFreelancerBalance Method***************************************************
    * This method is used to return the balance in the freelancer's account that matches the given username
    * It uses a named query to search the database for the username
    * It accepts username as parameters. It returns an integer which store the balance
     * @param username
     * @return 
    *****************************************************************************************************************************/
   @Override
    public int getFreelancerBalance(String username) {
        //Named query to be executed to get the freelancer object from database
        Query query = em.createNamedQuery("Freelancers.findByUsername").setParameter("username", username);
        //the result is stored in an freelancer object
        Freelancers free = (Freelancers)query.getResultList().get(0);
        //get the balance and return
        return free.getBalance();
    }

    public void persist(Object object) {
        try {
            em.persist(object);
        } catch (Exception e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", e);
            throw new RuntimeException(e);
        }
    }

    /************************************************searchFreelancersName Method***************************************************
    * This method is used to return the freelancer that matches the username which is entered by the user during login
    * It uses a named query to search the database for the username
    * It accepts username as parameters. It returns Freelancer objects
     * @param username
     * @return 
    *****************************************************************************************************************************/
    @Override
    public List<Freelancers> searchFreelancersName(String username) {
        //Named query to be executed to get the freelancer object from database
        Query query = em.createNamedQuery("Freelancers.findByUsername").setParameter("username", username);
        //return the result of the query which is a list of freelancers
        return query.getResultList();
    }

    /*****************************updateFreelancer Method**********************
    * This method is used to edit the freelancer profile in the database
    * It accepts username, name, skills, description, password as parameters.
     * @param name
     * @param skills
     * @param description
     * @param username
     * @param password
    ****************************************************************************/
    @Override
    public void updateFreelancer(String name, String skills, String description, String username, String password){
        //create named query to update the database
        Query query = em.createQuery("UPDATE Freelancers f SET f.name=:name,f.skills=:skills,f.description=:description,f.username=:username,f.password=:password WHERE f.username = :username");
        //set the required values
        query.setParameter("name", name);
        query.setParameter("skills", skills);
        query.setParameter("description", description);
        query.setParameter("username", username);
        query.setParameter("password", password);
        //execute the query to update the database
        int rowsUpdated = query.executeUpdate();
        System.out.println("entities Updated: " + rowsUpdated);
    }

    /*****************************registerFreelancer Method**********************
    * This method is used add a new freelancer in the database
    * It uses a native query to perform the insert function
    * It accepts username, name, skills, description, password as parameters.
    * This method auto-generates a unique ID for each new freelancer 
     * @param name
     * @param skills
     * @param description
     * @param username
     * @param password
    ****************************************************************************/
    @Override
    public void registerFreelancer(String name, String skills, String description, String username, String password) {
        //named query to get all the freelancers in the database
        Query query1 = em.createNamedQuery("Freelancers.findAll");
        //store the list of freelancers returned as result
        List<Freelancers> freelan = query1.getResultList();
        //variable to store the highest ID present in the database
        int max = 0;
        //for loop to traverse the list and find the highest ID 
        for (Freelancers freelan1 : freelan) {
            if(max<=freelan1.getId()){
                max = freelan1.getId();
            }
        }
        //add one to the highest ID to generate a unique ID
        max=max+1;
        //Native query to perform the insert function
        Query query = em.createNativeQuery("INSERT INTO Freelancers (id,name,skills,description,username,password,balance) values(?,?,?,?,?,?,?)");
        //set the necessary values
        query.setParameter(2, name);
        query.setParameter(3, skills);
        query.setParameter(4, description);
        query.setParameter(5, username);
        query.setParameter(6, password);
        query.setParameter(7, 0);
        query.setParameter(1, max);
        //execute the query
        int rowsUpdated = query.executeUpdate();
        System.out.println("entities Updated: " + rowsUpdated);
    }

    /**********************************delete Method***************************
    * This method is used remove a freelancer from the database
    * It uses a native query to perform the delete function
    * It accepts username as parameter and deletes the row having this username 
     * @param username
    ****************************************************************************/
    @Override
    public void delete(String username) {
        //Native query to perfrom the insert function
        Query query = em.createQuery("DELETE FROM Freelancers f WHERE f.username = :username");
        //set the necessary values
        query.setParameter("username", username);
        //execute the query
        int rowsUpdated = query.executeUpdate();
        System.out.println("entities Updated: " + rowsUpdated);
    }

    /****************************************applyJob Method**********************************
    * This method is used when a freelancer applies for a job
    * It finds the job by its id and checks if the freelancer has already applied
    * If not applied, then the method add the freelanser's name in the list of applicants
    * It accepts username and job id as parameters
     * @param id
     * @param username
    *******************************************************************************************/
    @Override
    public void applyJob(int id, String username) {
        //named query to get the job by its id
        Query query1 = em.createNamedQuery("Jobs.findById");
        //set the necessary value
        query1.setParameter("id", id);
        //store the returned job object
        Jobs j = (Jobs) query1.getResultList().get(0);
        //store the list of applicants for this job
        String fl_uname = j.getFlNames();
        //if the list is empty, add the freelancer's name
        if(fl_uname==null){
            fl_uname=username;
        }
        //else check if the name is already present
        else{
            if(fl_uname.contains(username)){
                return;
            }
            //if name is not present, then add the name 
            else{
                fl_uname = fl_uname+","+username;
            }
        }
        //query to update the database with the new names of applicants
        Query query = em.createQuery("UPDATE Jobs j SET j.flNames=:fl_uname WHERE j.id=:id");
        //set the required values
        query.setParameter("fl_uname", fl_uname);
        query.setParameter("id", id);
        //execute the query
        int rowsUpdated = query.executeUpdate();
    }

    /******************************************revokeJob Method************************************
    * This method is used when a freelancer revokes the application for a job
    * It finds the job by its id and removes the name of the freelancer from the list of applicants
     * @param id
     * @param username
    ************************************************************************************************/
    @Override
    public void revokeJob(int id, String username) {
        //named query to get the job by its id
        Query query1 = em.createNamedQuery("Jobs.findById");
        //set the necessary value
        query1.setParameter("id", id);
        //store the returned job object
        Jobs j = (Jobs) query1.getResultList().get(0);
        //store the list of applicants for this job
        String fl_uname = j.getFlNames();
        //split the string to store each name as a value in the array
        String[] flnames = fl_uname.split(",");
        //variable to store the updated list of applicants
        String finalNames="";
        //traverse the array to remove the freelancers name from it
        for(String fn : flnames ){
            if(fn.equals(username)){
            }
            else{
                if("".equals(finalNames)){
                    finalNames=fn; //add the other name to the updated list
                }
                else{
                    fn=","+fn;
                    finalNames=finalNames+fn; //add the other names to the updated list
                }
            }
        }
        //if no applicant is left, then set the value of applicants to null
        if("".equals(finalNames)){
            finalNames=null;
        }
        //query to update the database
        Query query = em.createQuery("UPDATE Jobs j SET j.flNames=:fl_uname WHERE j.id=:id");
        //set the necessary value
        query.setParameter("fl_uname", finalNames);
        query.setParameter("id", id);
        //execute the query
        int rowsUpdated = query.executeUpdate();
    }
}
