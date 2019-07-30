/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MyMBeans;

import MyBeans.ShowJobsBeanRemote;
import entities.Jobs;
import java.io.Serializable;
import java.util.List;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

/* Session Scoped bean to store details of the jobs 
 * contains methods for basic tasks related to jobs*/
@Named(value = "showJobsMBean")
@SessionScoped
public class ShowJobsMBean implements Serializable{
    
    //ShowJobsBeanRemote object to call methods of EJB ShowJobsBean
    @EJB
    ShowJobsBeanRemote jobs;
    
    //variables to store the attributes associated with the job
    private String description, title, search,keywords;
    private int id, pay;
    //variable to tell if a job is open or not
    private boolean isOpen;

    /**
     * Creates a new instance of ShowJobsMBean
     */
    public ShowJobsMBean() {
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
    }

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPay() {
        return pay;
    }

    public void setPay(int pay) {
        this.pay = pay;
    }

    public boolean isIsOpen() {
        return isOpen;
    }

    public void setIsOpen(boolean isOpen) {
        this.isOpen = isOpen;
    }
    
    /************************************************searchJobs Method*****************************************************
    * This method is used to find all the jobs that contain a specific keyword
    * It calls the searchJobs method in the ShowJobsBean EJB to search for all such jobs from the database
    * It returns a list of all such jobs found
     * @return 
    ************************************************************************************************************************/
    public List<Jobs> searchJobs() {
        //convert the search string to lower case
        search = search.toLowerCase();
        //return the list of jobs found
        return jobs.searchJobs(search);
    }
    
    /*************************************searchJobsByID Method*************************************
    * This method is used to find all the job having a specific id
    * It calls the searchJobsByID method in the ShowJobsBean EJB to search for the job
    * It returns a list of the job found
     * @return 
    *************************************************************************************************/
    public List<Jobs> searchJobsByID() {
        return jobs.searchJobsByID(id);
    }
    
    /*************************************openJobs Method*************************************
    * This method is used to find all the Open jobs 
    * It calls the openJobs method in the ShowJobsBean EJB to search for the jobs
    * It returns a list of all the Open jobs found
     * @return 
    *******************************************************************************************/
    public List<Jobs> openJobs(){
        return jobs.openJobs();
    } 
    
    /*************************************Jobs Method*************************************
    * This method is used to find all the jobs in the database
    * It calls the Jobs method in the ShowJobsBean EJB to search for the jobs
    * It returns a list of all the jobs found
     * @return 
    ***************************************************************************************/
    public List<Jobs> Jobs(){
        return jobs.Jobs();
    } 
    
    /*************************************delete Method*************************************
    * This method is used to delete a job from the database
    * It calls the delete method in the ShowJobsBean EJB to delete the job
    * It accepts a Jobs object as a parameter
     * @param j 
    *******************************************************************************************/
    public void delete(Jobs j){
        jobs.delete(j.getId());
    }
    
}
