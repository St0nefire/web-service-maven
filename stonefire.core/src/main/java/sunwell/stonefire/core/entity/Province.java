/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 * Provinsis.java
 *
 * Created on Jul 17, 2017, 10:44:06 AM
 */

package sunwell.stonefire.core.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Benny
 */
@Entity 
@Table(name = "province")
@NamedQueries({
    @NamedQuery(name = "Province.findAll", query = "SELECT p FROM Province p")
    , @NamedQuery(name = "Province.findById", query = "SELECT p FROM Province p WHERE p.id = :id")
    , @NamedQuery(name = "Province.findByProvinceName", query = "SELECT p FROM Province p WHERE p.provinceName = :provinceName")
    , @NamedQuery(name = "Province.findByCreatedAt", query = "SELECT p FROM Province p WHERE p.createdAt = :createdAt")
    , @NamedQuery(name = "Province.findByUpdatedAt", query = "SELECT p FROM Province p WHERE p.updatedAt = :updatedAt")})
@XmlRootElement
public class Province implements Serializable 
{

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    
    @Basic(optional = false)
    @Column(name = "province_name")
    private String provinceName;
    
    @Column(name = "created_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;
    
    @Column(name = "updated_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "provinsiId")
    private List<Regency> kotasList;

    public Province ()
    {
    }

    public Province (Integer id)
    {
        this.id = id;
    }

    public Province (Integer id, String provinceName)
    {
        this.id = id;
        this.provinceName = provinceName;
    }

    public Integer getId ()
    {
        return id;
    }

    public void setId (Integer id)
    {
        this.id = id;
    }

    public String getProvinceName ()
    {
        return provinceName;
    }

    public void setProvinceName (String provinceName)
    {
        this.provinceName = provinceName;
    }

    public Date getCreatedAt ()
    {
        return createdAt;
    }

    public void setCreatedAt (Date createdAt)
    {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt ()
    {
        return updatedAt;
    }

    public void setUpdatedAt (Date updatedAt)
    {
        this.updatedAt = updatedAt;
    }

    @XmlTransient
    public List<Regency> getKotasList ()
    {
        return kotasList;
    }

    public void setKotasList (List<Regency> kotasList)
    {
        this.kotasList = kotasList;
    }

    @Override
    public int hashCode ()
    {
        int hash = 0;
        hash += (id != null ? id.hashCode () : 0);
        return hash;
    }

    @Override
    public boolean equals (Object object)
    {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Province)) {
            return false;
        }
        Province other = (Province) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals (other.id)))
            return false;
        return true;
    }

    @Override
    public String toString ()
    {
        return "sunwell.stonefire.cred.model.entity.Provinsis[ id=" + id + " ]";
    }

}
