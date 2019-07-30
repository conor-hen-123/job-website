/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MyMBeans;

import MyBeans.FreelanceProfileBeanRemote;
import MyBeans.ShowJobsBeanRemote;
import entities.Freelancers;
import entities.Jobs;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.List;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;

/* Session Scoped bean to store details of the logged in freelancer user 
 * contains methods for freelancer tasks*/
@Named(value = "freelanceProfileMBean")
@SessionScoped
public class FreelanceProfileMBean implements Serializable {

    //objects to connect to the queue used for logging facility
    @Resource(mappedName = "jms/MyTest")
    private Queue myTest;
    @Resource(mappedName = "jms/testConnectionFactory")
    private ConnectionFactory queue;
    
    //FreelanceProfileBeanRemote object to call methods of EJB FreelanceProfileBean
    @EJB
    FreelanceProfileBeanRemote freeBean;
    //ShowJobsBeanRemote object to call methods of EJB ShowJobsBean
    @EJB
    ShowJobsBeanRemote job;
   
    //variables to store the attributes associated with the freelancer
    private String username, password, name, description, skills;
    private int balance, id;
    //variable to store the job id
    private int jobid; 
    //variable to store whether the freelancer applied for the job or no
    private boolean canApply;
    //variable to store whether the job is in complete or closed state
    private boolean isClosed=false;
    //variable to specify successful or unsuccessful login
    private boolean correct=false;
    
    /**
     * Creates a new instance of FreelanceProfileMBean
     */
    public FreelanceProfileMBean() {
        canApply=true;
    }

    public boolean isCorrect() {
        return correct;
    }

    public int getJobid() {
        return jobid;
    }

    public void setJobid(int jobid) {
        this.jobid = jobid;
    }

    public boolean isIsClosed() {
        return isClosed;
    }

    public void setIsClosed(boolean isClosed) {
        this.isClosed = isClosed;
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

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isCanApply() {
        return canApply;
    }

    public void setCanApply(boolean canApply) {
        this.canApply = canApply;
    }

    /***************************************************searchFreelancers Method***************************************************
    * This method is used to get the all the freelancers from the database
    * It calls the searchFreelancers method of the FreelanceProfileBean EJB to get the data from the database and returns it
     * @return 
    *******************************************************************************************************************************/
    public List<Freelancers> searchFreelancers() {
        return freeBean.searchFreelancers();
    }
    
    /*************************************************searchFreelancersName Method*************************************************
    * This method is used to get the freelancer's object from the database
    * It calls the searchFreelancersName method of the FreelanceProfileBean EJB to get the data from the database
    * It sets the bean variables to the values received from the database
     * @return 
    *******************************************************************************************************************************/
    public List<Freelancers> searchFreelancersName() {
        //call the method in the EJB and store the result list
        List<Freelancers> fl = freeBean.searchFreelancersName(username);
        //get the freelancer object from the list
        Freelancers frl = fl.get(0);
        //set the required variables for this freelancer 
        name = frl.getName();
        description = frl.getDescription();
        skills = frl.getSkills();
        password = frl.getPassword();    
        //return the list
        return fl;
    }
    
    /**************************************************getFreelancerName Method****************************************************
    * This method is used to get the freelancer's name when he/she logs in
    * It calls the getFreelancerName method of the FreelanceProfileBean EJB to get the name
    * It sets the variable correct to true or false depending upon successful or unsuccessful login
     * @return 
    *******************************************************************************************************************************/
    public String getFreelancerName() {
        //store the results returned from getFreelancerName method
        String[] data = freeBean.getFreelancerName(username,password);
        //check for successful or unsuccessful login
        if(data[1]=="correct"){
            //if success, assign true
            correct=true;
        }
        else{
            //else assign false
            correct=false;
        }
        //get the name of the logged in freelancer from the data that was returned
        String uname = (String) data[0];
        return uname;
    }
    
    /**************************************************getFreelancerBalance Method*************************************************
    * This method is used to get the freelancer's balance
    * It calls the getFreelancerBalance method of the FreelanceProfileBean EJB to get the balance amount
     * @return 
    *******************************************************************************************************************************/
    public int getFreelancerBalance() {
       return freeBean.getFreelancerBalance(username);
    }
    
    /**************************************************updateFreelancer Method*****************************************************
    * This method is used when the freelancer updates his/her profile
    * It calls the updateFreelancer method of the FreelanceProfileBean EJB to update the database
    * It passes all the required parameters for the update
    *******************************************************************************************************************************/
    public void updateFreelancer() { 
       freeBean.updateFreelancer(name,skills,description,username,password);
    }
    
    /**************************************************delete Method********************************************************
    * This method is used when the admin removes a freelancer from the database
    * It accepts Freelancers object as a parameter.
    * It calls the delete method of the FreelanceProfileBean EJB to remove the freelancer from the database
     * @param fl
    ************************************************************************************************************************/
    public void delete(Freelancers fl){
        freeBean.delete(fl.getUsername());
    }
    
    /**************************************************applyJob Method*****************************************************
    * This method is used when the freelancer wishes to apply for a job application
    * It accepts jobs object as a parameter.
    * It calls the applyJob method of the FreelanceProfileBean EJB to update the database
    * It also produces and queues a message for the message driven bean
     * @param j
    ************************************************************************************************************************/
    public void applyJob(Jobs j){
        //set the variable to specify that the freelancer has already applied for this job
        canApply=false;
        //create the message for the logs
        String messageData = username+" Has applied for a job with job id: "+Integer.toString(j.getId());
        try{
            //create connection to the queue
            Connection con = queue.createConnection();
            //create a sesion
            Session s = con.createSession();
            //set the message in the queue
            MessageProducer mp = s.createProducer(myTest);
            TextMessage tm = s.createTextMessage();
            tm.setText(messageData);
            mp.send(tm);
        }
        catch(Exception e){
            System.out.println(e);
        }
        //call the method in the EJB to perform the task
        freeBean.applyJob(j.getId(),username);
    }
    
    /**************************************************revokeJob Method*****************************************************
    * This method is used when the freelancer wishes to revoke his/her job application
    * It accepts jobs object as a parameter.
    * It calls the revokeJob method of the FreelanceProfileBean EJB to update the database
     * @param j
    ************************************************************************************************************************/
    public void revokeJob(Jobs j){
        //set the variable so that the freelancer can again apply to this job
        canApply=true;
        //call the method in EJB to revoke the application
        freeBean.revokeJob(j.getId(),username);
    }
    
    /************************************************storeJobId Method******************************************************
    * This method is used to store the id of the job when the freelancer wishes to see the description of a particular job
    * It accepts jobs object as a parameter and stores its id in the bean 
     * @param j
    ************************************************************************************************************************/
    public void storeJobId(Jobs j){
        jobid = j.getId();
    }

    /************************************************logout Method*******************************************************
    * This method is called when the freelancer logs out
    * the freelancer's info is erased
    ********************************************************************************************************************/
    public void logout(){
        //erase freelancer's info
        username="";
        password="";
    }
    
    /************************************************singleJob Method******************************************************
    * This method is used to get the full description of a single job selected by the freelancer
    * It calls the searchJobsByID method of the ShowJobsBean EJB to get the results
    * It then checks if this user has already applied to this job or no and sets the variables canApply and isClosed accordingly 
    * It returns the job list
     * @return 
    ***********************************************************************************************************************/
    public List<Jobs> singleJob(){
        //store the list of jobs returned by searchJobsByID
        List<Jobs> j = job.searchJobsByID(jobid);
        //store the names of the freelancers that has applied to this job
        String unames = j.get(0).getFlNames();
        String status = j.get(0).getStatus();
        //if job status is closed or complete set the necessary variables
        if(status.equals("Closed") || status.equals("Completed")){
            canApply=false;
            isClosed=true;
            return j;
        }
        //checks if this user has already applied to this job or no 
        //if yes then set the canApply to false
        //if no the set the canApply to true
        if(unames==null){
            canApply=true;
            isClosed=false;
            return j;
        }
        else if(unames.contains(username)){
            canApply=false;
            isClosed=false;
            return j;
        }
        else{
            canApply=true;
            isClosed=false;
            return j;
        }
    }
    
    /********************************************jobAssToMe Method**********************************************
    * This method is used to get all the jobs assigned to a freelancer
    * It calls the jobAssToMe method of the ShowJobsBean EJB to get the results as a list of jobs
    * It returns the job list
     * @return 
    *************************************************************************************************************/
    public List<Jobs> jobAssToMe(){
        return job.jobAssToMe(username);
    }
    
}
