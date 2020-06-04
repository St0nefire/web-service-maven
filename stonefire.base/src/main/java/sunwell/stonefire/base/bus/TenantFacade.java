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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

//import javax.ejb.EJB;
//import javax.ejb.Stateless;
//import javax.ejb.TransactionAttribute;
//import javax.ejb.TransactionAttributeType;
//import javax.ejb.TransactionManagement;
//import javax.ejb.TransactionManagementType;
//import javax.inject.Inject;
import sunwell.stonefire.base.dao.MasterMenuDAO;
import sunwell.stonefire.base.dao.TenantDAO;
import sunwell.stonefire.core.entity.MasterMenu;
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
public class TenantFacade 
{
//    @Inject
	@Autowired
    TenantDAO tenantDAO;
    
    
    public List<Tenant> findByProvinceAndCity(String _province, String _city) {
        return tenantDAO.findByProvinceAndCity (_province, _city);
    }
    
//    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public Tenant edit(Tenant _t) {
        return tenantDAO.edit (_t);
    }
}
