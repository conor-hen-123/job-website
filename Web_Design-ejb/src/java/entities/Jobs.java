/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/*Entitiy class to interact with the JOBS Database Table*/
@Entity
@Table(name = "JOBS")
@XmlRootElement 
@NamedQueries({
    @NamedQuery(name = "Jobs.findAll", query = "SELECT j FROM Jobs j")
    ,@NamedQuery(name = "Jobs.findAllOpen", query = "SELECT j FROM Jobs j WHERE j.status='Open'")
    ,@NamedQuery(name = "Jobs.findAssJobs", query = "SELECT j FROM Jobs j WHERE j.eUname=:username AND j.status='Closed'")
    ,@NamedQuery(name = "Jobs.jobsAssignToMe", query = "SELECT j FROM Jobs j WHERE j.flNames=:username AND j.status='Closed'")
    ,@NamedQuery(name = "Jobs.findPerProv", query = "SELECT j FROM Jobs j WHERE j.eUname=:username")
    ,@NamedQuery(name = "Jobs.findOpenPerProv", query = "SELECT j FROM Jobs j WHERE j.eUname=:username AND j.status='Open'")
    , @NamedQuery(name = "Jobs.findById", query = "SELECT j FROM Jobs j WHERE j.id = :id")
    , @NamedQuery(name = "Jobs.findByTitle", query = "SELECT j FROM Jobs j WHERE j.title = :title")
    , @NamedQuery(name = "Jobs.findByKeywords", query = "SELECT j FROM Jobs j WHERE j.keywords LIKE CONCAT('%', :keywords, '%')")
    , @NamedQuery(name = "Jobs.findByDescription", query = "SELECT j FROM Jobs j WHERE j.description = :description")
    , @NamedQuery(name = "Jobs.findByPay", query = "SELECT j FROM Jobs j WHERE j.pay = :pay")})
public class Jobs implements Serializable {

    @Size(max = 200)
    @Column(name = "FL_NAMES")
    private String flNames;

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "E_UNAME")
    private String eUname;

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 15)
    @Column(name = "STATUS")
    private String status;

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "TITLE")
    private String title;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "KEYWORDS")
    private String keywords;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 400)
    @Column(name = "DESCRIPTION")
    private String description;
    @Basic(optional = false)
    @NotNull
    @Column(name = "PAY")
    private int pay;

    public Jobs() {
    }

    public Jobs(Integer id) {
        this.id = id;
    }

    public Jobs(Integer id, String title, String keywords, String description, int pay) {
        this.id = id;
        this.title = title;
        this.keywords = keywords;
        this.description = description;
        this.pay = pay;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getPay() {
        return pay;
    }

    public void setPay(int pay) {
        this.pay = pay;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Jobs)) {
            return false;
        }
        Jobs other = (Jobs) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.Jobs[ id=" + id + " ]";
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getEUname() {
        return eUname;
    }

    public void setEUname(String eUname) {
        this.eUname = eUname;
    }

    public String getFlNames() {
        return flNames;
    }

    public void setFlNames(String flNames) {
        this.flNames = flNames;
    }
    
}
