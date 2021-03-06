/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 * MasterMenuDAO.java
 *
 * Created on Jul 21, 2017, 1:16:44 PM
 */

package sunwell.stonefire.base.dao;

import java.util.List;


//import javax.enterprise.context.Dependent;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

//import javax.transaction.Transactional;
import sunwell.stonefire.core.entity.MasterMenu;
import sunwell.stonefire.core.entity.MenuIndividual;
import sunwell.stonefire.core.entity.Tenant;
import sunwell.stonefire.core.entity.UserCred;
import sunwell.stonefire.core.entity.UserType;

/**
 *
 * @author Benny
 */
//@Dependent
@Repository
@Transactional
public class MenuIndividualDAO 
{
    @PersistenceContext
    EntityManager em;
        
    public List<MenuIndividual> findByTenant(Tenant _t) {
        TypedQuery<MenuIndividual> query = em.createNamedQuery ("MenuIndividual.findByTenant", MenuIndividual.class).setParameter ("tenant", _t);
        List<MenuIndividual> resultList = query.getResultList ();
        if(resultList.size () > 0)
            return resultList;
        else
            return null;
    }
}
