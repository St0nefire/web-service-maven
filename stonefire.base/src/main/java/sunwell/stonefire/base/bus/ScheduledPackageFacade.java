/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 * MasterMenuFacade.java
 *
 * Created on Jul 21, 2017, 1:20:04 PM
 */

package sunwell.stonefire.base.bus;

import java.util.Date;

import java.util.List;
//import javax.ejb.EJB;
//import javax.ejb.Stateless;
//import javax.ejb.TransactionAttribute;
//import javax.ejb.TransactionAttributeType;
//import javax.ejb.TransactionManagement;
//import javax.ejb.TransactionManagementType;
//import javax.inject.Inject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import sunwell.stonefire.base.dao.MasterMenuDAO;
import sunwell.stonefire.base.dao.MenuIndividualDAO;
import sunwell.stonefire.base.dao.ScheduledPackageDAO;
import sunwell.stonefire.core.entity.MasterMenu;
import sunwell.stonefire.core.entity.ScheduledPackage;
import sunwell.stonefire.core.entity.Tenant;
import sunwell.stonefire.core.entity.UserCred;
import sunwell.stonefire.core.entity.UserType;

/**
 *
 * @author Benny
 */
//@Stateless
//@TransactionManagement(TransactionManagementType.CONTAINER)
@Service
@Transactional
public class ScheduledPackageFacade 
{
//    @Inject
	@Autowired
    ScheduledPackageDAO spDAO;
        
    public List<ScheduledPackage> findByTenant(Tenant _t) {
        return spDAO.findByTenant (_t);
    }
}
