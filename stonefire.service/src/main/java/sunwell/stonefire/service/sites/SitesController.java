package sunwell.stonefire.service.sites;

import java.security.Principal;


import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.enterprise.inject.Model;
import javax.inject.Inject;
import javax.ws.rs.HttpMethod;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.view.RedirectView;

import aegwyn.core.web.model.UserSession;
import aegwyn.core.web.model.UserSessionContainer;
import sunwell.stonefire.base.bus.MasterMenuFacade;
import sunwell.stonefire.base.bus.UserCredFacade;
import sunwell.stonefire.base.dto.MasterMenuListDTO;
import sunwell.stonefire.base.dto.ScheduledPackageListDTO;
import sunwell.stonefire.base.dto.StandardDTO;
import sunwell.stonefire.base.dto.UserCredDTO;
import sunwell.stonefire.base.service.MasterMenuSvc;
import sunwell.stonefire.base.service.ScheduledPackageSvc;
import sunwell.stonefire.core.entity.UserCred;
import sunwell.stonefire.core.entity.MasterMenu;
import sunwell.stonefire.core.entity.ScheduledPackage;
import sunwell.stonefire.service.annotation.WebController;

@WebController
@Transactional
public class SitesController {
	
	@Autowired
	ScheduledPackageSvc spSvc;
	
	@Autowired
	MasterMenuSvc mmSvc;
	
	@Autowired
	UserCredFacade userFacade;

	@Autowired
	MasterMenuFacade masterMenufacade;

	@Inject
	UserSessionContainer usc;

	@RequestMapping("welcome")
	public String welcome() {
		System.out.println("WELCOME IS CALLED");
		return "welcome";
	}

	@RequestMapping(value = "login", method = RequestMethod.GET)
	public String login(Map<String, Object> _model) {
		System.out.println("LOGIN IS CALLED");
		// _model.put("objectTest", new MasterMenuListDTO());
		// _model.put("user", new UserCred());
		return "login";
	}
	
	@RequestMapping(value = "mastermenu", method = RequestMethod.GET)
	public String getMasterMenu(Map<String, Object> _model, Principal _principal, Pageable _page) throws Exception {
			
		System.out.println("P: " + _principal.getName());
		int countPerPage = 6;
//		List<MasterMenu> allMM = masterMenufacade.findAll();
		Page<MasterMenu> pageMM = mmSvc.findAllMasterMenu(_page);
		List<MasterMenu> allMM = pageMM != null ? pageMM.getContent() : null;
		allMM = new LinkedList<>(allMM);
//		Page<MasterMenu> pageMM = mmSvc.findAllMasterMenu(_page);
		List<MasterMenu> listMM = masterMenufacade.findAll(1, countPerPage);
		MasterMenuListDTO listMasterMenu = new MasterMenuListDTO();
		
//		allMM = pageMM.getContent();

//		int count = allMM == null ? 0 : allMM.size();
//		int pageCount = count / countPerPage;
//		if (count % countPerPage > 0)
//			pageCount += 1;
		
		long totalElements = pageMM.getTotalElements();
		int totalPages = pageMM.getTotalPages();
//		if (count % countPerPage > 0)
//			pageCount += 1;


		System.out.println("TOTAL ELEMENTS: " + totalElements + " TOTAL PAGES: " + totalPages);

		// add new empty master menu to fill the page
		for (int i = allMM.size(); i < pageMM.getSize(); i++) {
			allMM.add(new MasterMenu());
		}

		listMasterMenu.setData(allMM);

		_model.put("objectTest", new StandardDTO());
		_model.put("listMasterMenu", listMasterMenu);
		_model.put("pageNo", 1);
		_model.put("count", totalElements);
		_model.put("pageCount", totalPages);
		_model.put("userName", _principal.getName());
			
		return "mastermenu";
	}
	
	@RequestMapping(value = "scheduledpackage", method = RequestMethod.GET)
	public String getScheduledPackage(Map<String, Object> _model, 
			Principal _principal, Pageable _page) throws Exception {
			
		System.out.println("P: " + _principal.getName() + " page size: " + _page.getPageSize() + " No: " + _page.getPageNumber());;
		Page<ScheduledPackage> pageSP = spSvc.findAllScheduledPackage(_page);
		List<MasterMenu> listMM = mmSvc.findAllMasterMenu();
		ScheduledPackageListDTO retval = new ScheduledPackageListDTO();
		MasterMenuListDTO mmDTO = new MasterMenuListDTO();
		mmDTO.setData(listMM);

		if(pageSP.hasContent())
			retval.setData(pageSP.getContent());

		_model.put("listMenuPaket", retval);
		_model.put("listMasterMenu", mmDTO);
		_model.put("pageNo", _page.getPageNumber() + 1);
		_model.put("count", pageSP.getNumberOfElements());
		_model.put("totalPages", pageSP.getTotalPages());
		_model.put("totalElements", pageSP.getTotalElements());
//		_model.put("pageNo", pageSP.getNumber());
		_model.put("userName", _principal.getName());
			
		return "menupaket";
	}

	@RequestMapping(value = "login", method = RequestMethod.POST)
	public String login(UserCredDTO _dto, Map<String, Object> _model) throws Exception {
		if (_dto != null) {
			System.out.println("EMAIL: " + _dto.getEmail() + " PWD: " + _dto.getPassword());
		} else
			System.out.println("DTO IS NULL");

		UserCredDTO retval = new UserCredDTO();
		if (_dto.getEmail() == null) {
			_model.put("errorMessage", "ERROR, NO EMAIL IS SPECIFIED");
		} else if (_dto.getPassword() == null) {
			_model.put("errorMessage", "ERROR, NO PASSWORD IS SPECIFIED");
		} else {
			UserCred user = userFacade.validate(_dto.getEmail(), _dto.getPassword());
			if (user == null) {
				_model.put("errorMessage", "ERROR, CAN't FIND THE SPECIFIED USER");
			} else {
				int countPerPage = 6;
				List<MasterMenu> allMM = masterMenufacade.findAll();
				List<MasterMenu> listMM = masterMenufacade.findAll(1, countPerPage);
				MasterMenuListDTO listMasterMenu = new MasterMenuListDTO();

				int count = allMM == null ? 0 : allMM.size();
				int pageCount = count / countPerPage;
				if (count % countPerPage > 0)
					pageCount += 1;

				retval.setData(user);

				System.out.println("COUNT: " + count + " PC: " + pageCount + " size: " + listMM.size());

				// add new empty master menu to fill the page
				for (int i = listMM.size(); i < countPerPage; i++) {
					listMM.add(new MasterMenu());
				}

				listMasterMenu.setData(listMM);
				UserSession us = usc.newSession();
				us.setSessionName("Login");
				us.addObject("user", user);
				us.setLastActivity(Calendar.getInstance());
				retval.setData(user);
				retval.setSessionString(us.getSessionId());

				_model.put("objectTest", new StandardDTO());
				_model.put("user", retval);
				_model.put("sessionString", us.getSessionId());
				_model.put("listMasterMenu", listMasterMenu);
				_model.put("pageNo", 1);
				_model.put("count", count);
				_model.put("pageCount", pageCount);
			}
		}

		if (_model.get("errorMessage") != null)
			return "login";
		else
			return "mastermenu";
	}
	
	@RequestMapping(value = "test", method = RequestMethod.GET)
	public String test() throws Exception {
		return "test";
	}
}
