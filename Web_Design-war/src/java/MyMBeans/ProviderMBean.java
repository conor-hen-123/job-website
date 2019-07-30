/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MyMBeans;

import MyBeans.ProviderBeanRemote;
import MyBeans.ShowJobsBeanRemote;
import entities.Freelancers;
import entities.Jobs;
import entities.Provider;
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

/* Session Scoped bean to store details of the logged in provider user 
 * contains methods for provider tasks*/
@Named(value = "providerMBean")
@SessionScoped
public class ProviderMBean implements Serializable {

    //ProviderBeanRemote object to call methods of EJB ProviderBean
    @EJB
    ProviderBeanRemote provBean;
    //ShowJobsBeanRemote object to call methods of EJB ShowJobsBean
    @EJB
    ShowJobsBeanRemote job;
    
    //objects to connect to the queue used for logging facility
    @Resource(mappedName = "jms/MyTest")
    private Queue myTest;
    @Resource(mappedName = "jms/testConnectionFactory")
    private ConnectionFactory queue;
   
    //variables to store the attributes associated with the provider
    private String username, password, name;
    //variables to store the attributes associated with a job
    private String title,description,keywords;
    int pay,jobid;
    //variable to specify successful or unsuccessful login
    private boolean correct=false;
    //variables used during password change
    private String old_password,new_password;
    private boolean passChange=false;
    
    /**
     * Creates a new instance of ProviderMBean
     */
    public ProviderMBean() {
    }

    public String getUsername() {
        return username;
    }

    public String getOld_password() {
        return old_password;
    }

    public void setOld_password(String old_password) {
        this.old_password = old_password;
    }

    public String getNew_password() {
        return new_password;
    }

    public void setNew_password(String new_password) {
        this.new_password = new_password;
    }

    public int getJobid() {
        return jobid;
    }

    public void setJobid(int jobid) {
        this.jobid = jobid;
    }

    public boolean isCorrect() {
        return correct;
    }

    public void setCorrect(boolean correct) {
        this.correct = correct;
    }

    public boolean isPassChange() {
        return passChange;
    }

    public void setPassChange(boolean passChange) {
        this.passChange = passChange;
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public int getPay() {
        return pay;
    }

    public void setPay(int pay) {
        this.pay = pay;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    public List<Provider> searchProviders() {
        return provBean.searchProviders();
    }
    
    /**************************************************getProviderName Method****************************************************
    * This method is used to get the provider's name when he/she logs in
    * It calls the getProviderName method of the ProviderBean EJB to get the name
    * It sets the variable correct to true or false depending upon successful or unsuccessful login
     * @return 
    *******************************************************************************************************************************/
    public String getProviderName() {
        //store the results returned from getProviderName method
        String[] data = provBean.getProviderName(username,password);
        //check for successful or unsuccessful login
        if(data[1]=="correct"){
            //if success, assign true
            correct=true;
        }
        else{
            //else assign false
            correct=false;
        }
        //get the name of the logged in provider from the data that was returned
        String uname = (String) data[0];
        return uname;
    }
    
    /*******************************************delete Method******************************************
    * This method is used when the admin removes a provider from the database
    * It accepts Provider object as a parameter.
    * It calls the delete method of the ProviderBean EJB to remove the provider from the database
     * @param pr
    ****************************************************************************************************/
    public void delete(Provider pr){
        provBean.delete(pr.getUsername());
    }
    
    /*******************************************providerJob Method******************************************
    * This method is used when to get all the jobs posted by the provider
    * It calls the searchProviderJob method of the ShowJobsBean EJB to get the jobs from the database
    * It returns the list of these jobs
     * @return 
    *********************************************************************************************************/
    public List<Jobs> providerJob() {
        return job.searchProviderJob(username);
    }
    
    /*******************************createJob Method******************************
    * This method is used when to add a new job in the database
    * It calls the createJob method of the ProviderBean EJB to create the job
    * It passes the required parameters to the method to create the job
    ******************************************************************************/
    public void createJob(){
        //convert the keywords string to lower case
        keywords = keywords.toLowerCase();
        provBean.createJob(title, description, pay, keywords,username);
    }
    
    /*******************************showJobApplicants Method******************************
    * This method is used get all the jobs in Open state
    * It calls the showOpenJobs method of the ShowJobsBean EJB to get the Open jobs
    * It returns the list of these jobs
     * @return 
    ***************************************************************************************/
    public List<Jobs> showJobApplicants() {
        return job.showOpenJobs(username);
    }
    
    /**********************************seeApplicants Method******************************
    * This method is used get all the freelancers that have applied to a particular job
    * It calls the seeApplicants method of the ShowJobsBean EJB to get list of freelancers
    * It returns the list of these freelancers
     * @return 
    **************************************************************************************/
    public List<Freelancers> seeApplicants(){
        return job.seeApplicants(jobid);
    }
    
    /************************************************storeJobId Method******************************************************
    * This method is used to store the id of the job when the provider wishes to see the applicants for a particular job
    * It accepts jobs object as a parameter and stores its id in the bean 
     * @param j
    ************************************************************************************************************************/
    public void storeJobId(Jobs j){
        jobid=j.getId();
    }
    
    /************************************************jobAssign Method******************************************************
    * This method is used to assign the job to a freelancer
    * It calls the jobAssign method of the ShowJobsBean EJB to get assign the job to the freelancer
    * It also generates a message for the message driven bean and sends it to the queue
     * @param fl
    ************************************************************************************************************************/
    public void jobAssign(Freelancers fl){
        //create the message for the logs
        String messageData = username+" Has assigned the job with job id: "+Integer.toString(jobid)+" to "+fl.getUsername();
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
        job.jobAssign(jobid,fl.getUsername());
    }
    
    /*********************logout Method**************************
    * This method is called when the provider logs out
    * the provider's info is erased
    **************************************************************/
    public void logout(){
        //erase provider's info
        username="";
        password="";
    }
    
    /*************************************providerAssJob Method**********************************
    * This method is used get all the jobs that are assigned by this provider to a freelancer
    * It calls the showAssJobs method of the ShowJobsBean EJB to get list of such jobs
    * It returns the list of these jobs
     * @return 
    **********************************************************************************************/
    public List<Jobs> providerAssJob(){
        return job.showAssJobs(username);
    }
    
    /***************************************complete Method**************************************
    * This method is used get mark a job as complete
    * It calls the complete method of the ProviderBean EJB to mark the job complete in database 
     * @param j
    **********************************************************************************************/
    public void complete(Jobs j){
         //create the message for the logs
        String messageData = username+" Has marked the job with job id: "+Integer.toString(jobid)+" as complete";
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
        provBean.complete(j.getId());
    }
    
     /*********************changePass Method**************************
    * This method is used to change the provider's password
    * the provider's info is erased
    **************************************************************/
    public void changePass(){
        passChange = provBean.changePass(old_password,new_password,username);
        //change the password in the bean variable
        if(passChange){
            password=new_password;
        }
    }
    
}
