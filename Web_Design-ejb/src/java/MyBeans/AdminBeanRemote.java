/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MyBeans;

import entities.Admin;
import java.util.List;
import javax.ejb.Remote;


@Remote
/* Interface for AdminBean*/
public interface AdminBeanRemote {
    
    public List<Admin> searchAdmin(String username);
    
    public String[] searchAdminName(String username,String password);
    
}
