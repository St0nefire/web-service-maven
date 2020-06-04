/**
 * MasterMenus.java
 *
 * Created on Jul 17, 2017, 2:57:41 PM
 */
package sunwell.stonefire.base.dto;

import sunwell.stonefire.core.entity.*;

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
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Benny
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class GenericMenuDTO extends StandardDTO
{
   
    private Integer id;
    
    
    private String name;
    
    
    private String konten;
    
   
    private String memo;
    
    
    private Double price;
    
    
    private String image;
    
    
    private Date createdAt;
    
    
    private Date updatedAt;
    
    private Integer minOrder;
    
    private Boolean available;
    
//    @JoinColumn(name = "user_id", referencedColumnName = "id")
//    @ManyToOne(optional = false)
//    private UserCred userId;
    
//    @OneToMany(cascade = CascadeType.ALL, mappedBy = "masterMenuId")
//    private List<MasterMenuScheduledPackage> masterMenuScheduledPackageList;

    public GenericMenuDTO ()
    {
    }
    
    public GenericMenuDTO (MenuIndividual _mi)
    {
        setData (_mi);
    }

    public void setData (MenuIndividual _mi)
    {
        this.id = _mi.getId ();
        this.name = _mi.getName ();
        this.konten = _mi.getKonten ();
        this.memo = _mi.getMemo ();
        this.image = _mi.getImage ();
        this.price = _mi.getPrice ();
        this.available = _mi.isAvailable ();
        this.createdAt = _mi.getCreatedAt ();
        this.updatedAt = _mi.getUpdatedAt ();
    }
    
    public void setData (ScheduledPackage _sp)
    {
        this.id = _sp.getId ();
        this.name = _sp.getName ();
        this.konten = _sp.getKonten ();
        this.memo = _sp.getMemo ();
        this.image = _sp.getImage ();
        this.price = _sp.getPrice ();
        this.available = _sp.isAvailable ();
        this.minOrder = _sp.getMinOrder ();
        this.createdAt = _sp.getCreatedAt ();
        this.updatedAt = _sp.getUpdatedAt ();
    }
    
    public void setData (MasterMenu _mm)
    {
        this.id = _mm.getId ();
        this.name = _mm.getName ();
        this.konten = _mm.getKonten ();
        this.memo = _mm.getMemo ();
        this.image = _mm.getImage ();
        this.createdAt = _mm.getCreatedAt ();
        this.updatedAt = _mm.getUpdatedAt ();
    }

    public Integer getId ()
    {
        return id;
    }

    public void setId (Integer id)
    {
        this.id = id;
    }

    public String getName ()
    {
        return name;
    }

    public void setName (String name)
    {
        this.name = name;
    }

    public String getKonten ()
    {
        return konten;
    }

    public void setKonten (String konten)
    {
        this.konten = konten;
    }

    public String getMemo ()
    {
        return memo;
    }

    public void setMemo (String memo)
    {
        this.memo = memo;
    }

    public Double getPrice ()
    {
        return price;
    }

    public void setPrice (Double price)
    {
        this.price = price;
    }

    public String getImage ()
    {
        return image;
    }

    public void setImage (String image)
    {
        this.image = image;
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

//    public UserCred getUserId ()
//    {
//        return userId;
//    }
//
//    public void setUserId (UserCred userId)
//    {
//        this.userId = userId;
//    }

//    public List<MasterMenuScheduledPackage> getMasterMenuScheduledPackageList ()
//    {
//        return masterMenuScheduledPackageList;
//    }
//
//    public void setMasterMenuScheduledPackageList (List<MasterMenuScheduledPackage> masterMenuScheduledPackageList)
//    {
//        this.masterMenuScheduledPackageList = masterMenuScheduledPackageList;
//    }

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
        if (!(object instanceof GenericMenuDTO)) {
            return false;
        }
        GenericMenuDTO other = (GenericMenuDTO) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals (other.id)))
            return false;
        return true;
    }

//    @Override
//    public String toString ()
//    {
//        return "sunwell.stonefire.entity.MasterMenus[ id=" + id + " ]";
//    }
    
     /**
     * @return the minOrder
     */
    public Integer getMinOrder ()
    {
        return minOrder;
    }

    /**
     * @param minOrder the minOrder to set
     */
    public void setMinOrder (Integer minOrder)
    {
        this.minOrder = minOrder;
    }

    /**
     * @return the available
     */
    public Boolean isAvailable ()
    {
        return available;
    }

    /**
     * @param isAvalaible the available to set
     */
    public void setAvailable (Boolean isAvalaible)
    {
        this.available = isAvalaible;
    }

}
