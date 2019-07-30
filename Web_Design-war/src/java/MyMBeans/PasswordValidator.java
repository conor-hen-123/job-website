/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MyMBeans;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.*;
 
/* Custom validator to validate the passwords*/
@FacesValidator("passvalidator")
public class PasswordValidator implements Validator
{
    
    /***************************************************validate Method*********************************************************
    * This method is used to check whether the password entered meets required conditions
    * if required conditions are not met an exception is thrown
    * It accepts context, a ui component(c) and entered password(val) as parameter
     * @param context
     * @param c
     * @param val
    *****************************************************************************************************************************/
    @Override
    public void validate(FacesContext context, UIComponent c, Object val) throws ValidatorException
    {
        //store the password as string value
        String password = (String) val;
        //variable to tell if password is good or bad
        boolean good = true;
        //variables to count uppercase and special characters in the password
        int count1=0,count2=0;
        //for loop to traverse all the characters in the password
        for(int i=0; i<password.length(); i++){
            //get ascii value of the character
            char ch = password.charAt(i);
            //check if this is a upper case character
            //if yes, increment the count
            if(ch >= 65 && ch <= 90) {
                    count1+=1;
            }
            //check if this is a special character amongst @,#,$,%,&
            //if yes, increment the count
            if((ch >= 35 && ch <= 38)||ch==64) {
                    count2+=1;
            }
        }
        //check if the password contains atleast one uppercase, one special character and if the length is greater than 8
        if(count1==0 || count2==0 || password.length()<8){
            //if any of the conditions fail, it is a bad passowrd
            good = false;
        }
 
        //if the passowrd is bad
        if (!good) {
            FacesMessage message = new FacesMessage();
            //set the required message to display to the user
            message.setDetail("Please enter a valid password");
            message.setSummary("Password not valid. The password must contain atleast one - Uppercase character||A special character(@,#,$,&,%)||Minimum length of 8 characters");
            message.setSeverity(FacesMessage.SEVERITY_ERROR);
            //throw the exception
            throw new ValidatorException(message);
        }
 
    }
}
