/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MyBeans;

import entities.Freelancers;
import entities.Jobs;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/*Session Bean to perform tasks related to Jobs*/
@Stateless
public class ShowJobsBean implements ShowJobsBeanRemote{
    
   //entity manager object to interact with the database
   @PersistenceContext(name= "Web_Design")
   private EntityManager em;
    
   /************************************************searchJobs Method*****************************************************
    * This method is used to find the jobs which contains a specific keyword
    * It accepts search as parameter which is the string value to be searched in the keywords in the job
    * It returns the list of jobs that contains the keyword
     * @param search
     * @return 
    *****************************************************************************************************************************/
   @Override
    public List<Jobs> searchJobs(String search) {
        //named query to get the job by the keyword
        Query query = em.createNamedQuery("Jobs.findByKeywords").setParameter("keywords", search);
        //return the list of jobs
        return query.getResultList();
    }

    /************************************************searchJobsByID Method*****************************************************
    * This method is used to find a job by its id
    * It accepts job id as parameter which is used to search for the job in the database
    * It returns the list of job with this id
     * @param id
     * @return 
    *****************************************************************************************************************************/
   @Override
    public List<Jobs> searchJobsByID(int id) {
        //named query to get the job by its id
        Query query = em.createNamedQuery("Jobs.findById").setParameter("id", id);
        //return the list of jobs
        return query.getResultList();
    }
    
    /************************************************openJobs Method*****************************************************
    * This method is used to find all the jobs in Open state
    * It returns the list of jobs which are in Open state
     * @return 
    **********************************************************************************************************************/
   @Override
    public List<Jobs> openJobs(){
        //named query to get all the job in Open state
        Query query = em.createNamedQuery("Jobs.findAllOpen");
        //return the list of jobs
        return query.getResultList();
    } 
    
    /************************************************Jobs Method*****************************************************
    * This method is used to find all the jobs in any state
    * It returns the list of all the jobs
     * @return 
    ******************************************************************************************************************/
    @Override
    public List<Jobs> Jobs(){
        //named query to get all the jobs
        Query query = em.createNamedQuery("Jobs.findAll");
        //return the list of jobs
        return query.getResultList();
    } 
    
    public void persist(Object object) {
        try {
            em.persist(object);
        } catch (Exception e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", e);
            throw new RuntimeException(e);
        }
    }
    
    /************************************************delete Method*****************************************************
    * This method is used to delete a job from the database
    * It accepts job id as parameter and searches for this job in the database and deletes it
     * @param id
    ********************************************************************************************************************/
   @Override
    public void delete(int id){
        //query to delete a job by its id
        Query query = em.createQuery("DELETE FROM Jobs j WHERE j.id = :id");
        //set required values
        query.setParameter("id", id);
        //execute the query
        int rowsUpdated = query.executeUpdate();
        System.out.println("entities Updated: " + rowsUpdated);
    }
    
    /************************************************searchProviderJob Method****************************************
    * This method is used to find all the jobs posted by a provider
    * It accepts username as parameter and searches for all the jobs posted by this username in the database
    * It returns the list of all such jobs found
     * @param username
     * @return 
    ******************************************************************************************************************/
   @Override
    public List<Jobs> searchProviderJob(String username){
        //list to store the result
        List<Jobs> j = new ArrayList<>();
        try{
            //query to find jobs posted by a particular provider
            Query query = em.createNamedQuery("Jobs.findPerProv").setParameter("username", username);
            //store the results
            j = query.getResultList();
        }
        catch(Exception e){
        }
        
        // return query result
        return j;
    }

    /************************************************showOpenJobs Method******************************************
    * This method is used to find all the jobs posted by a provider which are in Open state
    * It accepts username as parameter and searches for all the Open jobs posted by this username in the database
    * It returns the list of all such jobs found
     * @param username
     * @return 
    ***************************************************************************************************************/
    @Override
    public List<Jobs> showOpenJobs(String username) {
        //named query to find Open jobs posted by a particular provider
        Query query = em.createNamedQuery("Jobs.findOpenPerProv").setParameter("username", username);
        //return the list of jobs
        return query.getResultList();
    }

    /************************************************seeApplicants Method******************************************
    * This method is used to find all the applicants that have applied for a job
    * It accepts job id as parameter and lists all the freelancers that have applied to it
    * It returns the list freelancers
     * @param id
     * @return 
    ***************************************************************************************************************/
    @Override
    public List<Freelancers> seeApplicants(int id) {
        //initialise list of freelancers
        List<Freelancers> fl = new ArrayList<>();
        try{
            //named query to find the job by its id
            Query query1 = em.createNamedQuery("Jobs.findById").setParameter("id", id);
            //store the result in a job object
            Jobs j = (Jobs) query1.getResultList().get(0);
            //get the usernames of all the frelancers who have applied for this job
            String names = j.getFlNames();
            //split the usernames
            String[] fl_names = names.split(",");
            //for each username, get the corresponding row form the Freelancers database and store in the list
            for(String fl1 : fl_names){
                Query query = em.createNamedQuery("Freelancers.findByUsername").setParameter("username", fl1);
                fl.add((Freelancers)query.getResultList().get(0));
            }
        }
        catch(Exception e){
        }
        //return the list of freelancers
        return fl;
    }

    /************************************************jobAssign Method**********************************************
    * This method is used to assign a job to a freelancer and mark it as Closed
    * It accepts job id and username as parameters and sets the job with that id to Closed state
     * @param id
     * @param username
    ***************************************************************************************************************/
    @Override
    public void jobAssign(int id,String username) {
        //query to update the job with the username of the freelancer to whom it is assigned and mark it as closed
        Query query = em.createQuery("UPDATE Jobs j SET j.status='Closed',j.flNames=:username WHERE j.id=:id");
        //set required values
        query.setParameter("username", username);
        query.setParameter("id", id);
        //execute the query
        int rowsUpdated = query.executeUpdate();
        System.out.println(rowsUpdated);
    }

    /************************************************showAssJobs Method*******************************************
    * This method is used to find all the jobs posted by a provider that are assigned to some freelancer
    * It accepts username as parameter and searches for all the Closed jobs posted by this username in the database
    * It returns the list of all such jobs found
     * @param username
     * @return 
    ***************************************************************************************************************/
    @Override
    public List<Jobs> showAssJobs(String username) {
        Query query = em.createNamedQuery("Jobs.findAssJobs").setParameter("username", username);
        //return the list of jobs
        return query.getResultList();
    }

    /************************************************jobAssToMe Method*********************************************
    * This method is used to find all the jobs that are assigned to a freelancer
    * It accepts username as parameter and searches for all the Closed jobs for this username from the database
    * It returns the list of all such jobs found
     * @param username
     * @return 
    ***************************************************************************************************************/
    @Override
    public List<Jobs> jobAssToMe(String username) {
        Query query = em.createNamedQuery("Jobs.jobsAssignToMe").setParameter("username", username);
        return query.getResultList();
    }

    
}
