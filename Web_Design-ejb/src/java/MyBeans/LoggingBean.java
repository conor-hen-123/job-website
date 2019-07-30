/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MyBeans;

import entities.Logs;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/* Message driven bean for logging purpose*/
@MessageDriven(activationConfig = {
    @ActivationConfigProperty(propertyName = "destinationLookup", propertyValue = "jms/MyTest")
    ,
        @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue")
})
public class LoggingBean implements MessageListener {
    
    @PersistenceContext(name= "Web_Design")
    private EntityManager em;
    
    public LoggingBean() {
    }
    
    /************************************************onMessage Method*****************************************************
    * This method is used to read the log message from the queue and write it to the database
    * It accepts message as a parameter, this message is written to the database
    * It also create a unique id for each log message
     * @param message
    *****************************************************************************************************************************/
    @Override
    public void onMessage(Message message) {
        //store the message as TextMessage
        TextMessage tm = (TextMessage) message;
        //named query to get all the Logs in the database
        Query query1 = em.createNamedQuery("Logs.findAll");
        try {
            //store the list of Logs returned as result
            List<Logs> log = query1.getResultList();
            //variable to store the highest ID present in the database
            int max = 0;
            //for loop to traverse the list and find the highest ID 
            for (Logs l1 : log) {
                if(max<=l1.getId()){
                    max = l1.getId();
                }
        }
        //add one to the highest ID to generate a unique ID
        max=max+1;
        //query to update the database with the new message
        Query query = em.createNativeQuery("INSERT INTO Logs (id,message) values(?,?)");
        //set required values
        query.setParameter(1, max);
        query.setParameter(2, tm.getText());
        //execute the query
        int rowsUpdated = query.executeUpdate();
        System.out.println("entities Updated: " + rowsUpdated);
        } catch (JMSException ex) {
            Logger.getLogger(LoggingBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
