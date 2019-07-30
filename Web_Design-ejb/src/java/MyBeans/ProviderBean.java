/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MyBeans;

import entities.Freelancers;
import entities.Jobs;
import entities.Provider;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/*Session Bean to perform Provider related tasks*/
@Stateless
public class ProviderBean implements ProviderBeanRemote {
    
    //entity manager object to interact with the database
    @PersistenceContext(name= "Web_Design")
    private EntityManager em;

    @Override
    public List<Provider> searchProviders() {
        Query query = em.createNamedQuery("Provider.findAll");
        // return query result
        return query.getResultList();
    }

    /************************************************getProviderName Method*****************************************************
    *This method is used to return the provider that matches the username which is entered by the user during login
    * It accepts username and password as parameters which are entered by the user during login
    * It uses a named query to search the database for the username and compares the entered password with the stored password
    * It returns a String array which contains the name of the provider and a variable to flag correct password
     * @param username
     * @param password
     * @return 
    *****************************************************************************************************************************/
    @Override
    public String[] getProviderName(String username, String password) {
        String[] data=new String[2]; //String array to store the results
        try{
            //Named query to be executed to get the provider object from database
            Query query = em.createNamedQuery("Provider.findByUsername").setParameter("username", username);
            //the result is stored in an provider object
            Provider prov = (Provider)query.getResultList().get(0);
            //store the name of the provider
            data[0] = prov.getName();
            //check if the entered password matches the stored password
            if(prov.getPassword().equals(password)){
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

    /*****************************registerProvider Method**********************
    * This method is used add a new provider in the database
    * It uses a native query to perform the insert function
    * It accepts username, name, password as parameters.
    * This method auto-generates a unique ID for each new provider 
     * @param name
     * @param username
     * @param password
    ****************************************************************************/
    @Override
    public void registerProvider(String name, String username, String password) {
        //named query to get all the provider in the databa`se
        Query query1 = em.createNamedQuery("Provider.findAll");
        //store the list of provider returned as result
        List<Provider> prov = query1.getResultList();
        //variable to store the highest ID present in the database
        int max = 0;
        //for loop to traverse the list and find the highest ID 
        for (Provider prov1 : prov) {
            if(max<=prov1.getId()){
                max = prov1.getId();
            }
        }
        //add one to the highest ID to generate a unique ID
        max=max+1;
        //Native query to perform the insert function
        Query query = em.createNativeQuery("INSERT INTO Provider (id,name,username,password) values(?,?,?,?)");
        //set the necessary values
        query.setParameter(2, name);
        query.setParameter(3, username);
        query.setParameter(4, password);
        query.setParameter(1, max);
        //execute the query
        int rowsUpdated = query.executeUpdate();
        System.out.println("entities Updated: " + rowsUpdated);
    }

    /**********************************delete Method***************************
    * This method is used remove a provider from the database
    * It uses a native query to perform the delete function
    * It accepts username as parameter and deletes the row having this username 
     * @param username
    ****************************************************************************/
    @Override
    public void delete(String username) {
        //Native query to perfrom the insert function
        Query query = em.createQuery("DELETE FROM Provider p WHERE p.username = :username");
        //set the necessary values
        query.setParameter("username", username);
        //execute the query
        int rowsUpdated = query.executeUpdate();
        System.out.println("entities Updated: " + rowsUpdated);
    }
    
    /**********************************createJob Method***************************
    * This method is used to add a new job in the JOBS database
    * It uses a native query to perform the insert function
    * It accepts title, description, pay, keywords and username as parameters
    * The method auto-generates a unique ID for each new job 
     * @param title
     * @param description
     * @param pay
     * @param keywords
     * @param username
    ****************************************************************************/
    @Override
    public void createJob(String title, String description, int pay, String keywords,String username) {
        //named query to get all the jobs in the database
        Query query1 = em.createNamedQuery("Jobs.findAll");
        //store the list of jobs returned as result
        List<Jobs> j = query1.getResultList();
        //variable to store the highest ID present in the database
        int max = 0;
        //for loop to traverse the list and find the highest ID 
        for (Jobs j1 : j) {
            if(max<=j1.getId()){
                max = j1.getId();
            }
        }
        //add one to the highest ID to generate a unique ID
        max=max+1;
        //Native query to perform the insert function
        Query query = em.createNativeQuery("INSERT INTO Jobs (id,title,description,pay,keywords,e_uname,status) values(?,?,?,?,?,?,?)");
        //set the necessary values
        query.setParameter(2, title);
        query.setParameter(3, description);
        query.setParameter(4, pay);
        query.setParameter(5, keywords);
        query.setParameter(6, username);
        query.setParameter(7, "Open");
        query.setParameter(1, max);
        //execute the query
        int rowsUpdated = query.executeUpdate();
        System.out.println("entities Updated: " + rowsUpdated);
    }
    
    /****************************************complete Method************************************
    * This method is used to mark a job as complete and assign the payment to the freelancer
    * It accepts job id as parameter and updates that job as completed in the database
    * It also add the payment to the balance of the freelancer
     * @param id
    **********************************************************************************************/
    @Override
    public void complete(int id) {
        //named query to find the required job by id
        Query query1 = em.createNamedQuery("Jobs.findById").setParameter("id", id);
        //store the resulyt in the job object
        Jobs j1 = (Jobs)query1.getResultList().get(0);
        //get the username of the freelancer to whom the job was assigned
        String uname = j1.getFlNames();
        //get the pay of the job
        int payment = j1.getPay();
        //query to update the job as completed
        Query query2 = em.createQuery("UPDATE Jobs j SET j.status='Completed' WHERE j.id=:id");
        //set required values
        query2.setParameter("id", id);
        //execute the query
        int rowsUpdated = query2.executeUpdate();
        //query to search the freelancer to whom job was assigned
        Query query3 = em.createNamedQuery("Freelancers.findByUsername").setParameter("username", uname);
        //store the result in a freelancer object
        Freelancers f1 = (Freelancers)query3.getResultList().get(0);
        //add the payment to freelancer's balance
        payment = payment+f1.getBalance();
        //query to update the freelancer's balance in the database
        Query query4 = em.createQuery("UPDATE Freelancers f SET f.balance=:payment WHERE f.username=:uname");
        //set required values
        query4.setParameter("payment", payment);
        query4.setParameter("uname", uname);
        //execute the query
        int rowsUpdated3 = query4.executeUpdate();
    }

    /****************************************changePass Method************************************
    * This method is used to change the provider's password
    * It accepts old_password, new_password and username as parameters
    * It checks the old password entered with the password stored in the database.
    * If they match then onl the new password is updated in the database
    * It returns a value indicating whether password change was successful or no
     * @param old_password
     * @param new_password
     * @param username
     * @return 
    **********************************************************************************************/
    @Override
    public boolean changePass(String old_password, String new_password, String username) {
        //variable to tell if password change was successful
        boolean pass_set=false;
        //Named query to be executed to get the provider object from database
        Query query = em.createNamedQuery("Provider.findByUsername").setParameter("username", username);
        //the result is stored in an provider object
        Provider prov = (Provider)query.getResultList().get(0);
        //store the password of the provider
        String pass = prov.getPassword();
        //check if the old_password matches
        if(pass.equals(old_password)){
            //query to update the provider's password in the database
            Query query1 = em.createQuery("UPDATE Provider p SET p.password=:new_password WHERE p.username=:username");
            //set required values
            query1.setParameter("new_password", new_password);
            query1.setParameter("username", username);
            //execute the query
            int rowsUpdated3 = query1.executeUpdate();
            //set the flag to true
            pass_set=true;
        }
        return pass_set;
    }
    
}
