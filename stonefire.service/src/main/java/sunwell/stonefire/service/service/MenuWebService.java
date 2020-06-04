/*
 * To change this license header, choose License Headers in Project Properties.

 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 * StonefireWebService.java
 *
 * Created on Jul 17, 2017, 10:59:20 AM
 */

package sunwell.stonefire.service.service;


//import com.sun.jersey.core.header.FormDataContentDisposition;
//import com.sun.jersey.multipart.FormDataParam;
import aegwyn.core.web.model.UserSession;




import aegwyn.core.web.model.UserSessionContainer;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.StreamingOutput;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.validation.beanvalidation.SpringValidatorAdapter;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

//import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
//import org.glassfish.jersey.media.multipart.FormDataParam;
import sunwell.stonefire.base.bus.GenericFacade;
import sunwell.stonefire.base.bus.MasterMenuFacade;
import sunwell.stonefire.base.bus.MenuIndividualFacade;
import sunwell.stonefire.base.bus.ScheduledPackageFacade;
import sunwell.stonefire.base.bus.UserCredFacade;
import sunwell.stonefire.base.dto.MasterMenuDTO;
import sunwell.stonefire.base.dto.MenuIndividualDTO;
import sunwell.stonefire.base.dto.MasterMenuListDTO;
import sunwell.stonefire.base.dto.MasterTagDTO;
import sunwell.stonefire.base.dto.MasterTagListDTO;
import sunwell.stonefire.base.dto.MenuIndividualListDTO;
import sunwell.stonefire.base.dto.ScheduledPackageDTO;
import sunwell.stonefire.base.dto.ScheduledPackageListDTO;
import sunwell.stonefire.base.dto.StandardDTO;
import sunwell.stonefire.base.dto.UserCredDTO;
import sunwell.stonefire.core.entity.MasterMenu;
import sunwell.stonefire.core.entity.MasterTag;
import sunwell.stonefire.core.entity.MenuIndividual;
import sunwell.stonefire.core.entity.MenuIndividualMenu;
import sunwell.stonefire.core.entity.ScheduledPackage;
import sunwell.stonefire.core.entity.ScheduledPackageMenu;
import sunwell.stonefire.core.entity.Tenant;
import sunwell.stonefire.core.entity.UserCred;
import sunwell.stonefire.service.annotation.RestEndpoint;

/**
 *
 * @author Benny
 */
//@Stateless
//@Path("")

@RestEndpoint
//@Transactional
public class MenuWebService 
{
	@Autowired
    UserCredFacade userFacade;
    
	@Autowired
    MasterMenuFacade masterMenufacade;
    
	@Autowired
    MenuIndividualFacade miFacade;
    
	@Autowired
    ScheduledPackageFacade spFacade;
    
	@Autowired
    GenericFacade genericFacade;
    
	@Inject
    UserSessionContainer usc;
	
	@Inject
	ServletContext sCtx;
	
	@Inject
	MessageSource messageSource ;
	
	@Inject
	HttpServletRequest request ;
	
	@Inject 
	SpringValidatorAdapter validator;
            
    @RequestMapping(value = "mastermenu", method = RequestMethod.GET,
    		produces = {"application/json"}
	)
    public ResponseEntity<MasterMenuListDTO> getMasterMenus(
    		@RequestParam("sessionString") String _sessionString,
    		@RequestParam(value = "tenantId", required = false) String _tenantId) throws Exception
    {
		Tenant tenant = null;
        MasterMenuListDTO retval = new MasterMenuListDTO();
        if(_tenantId != null) {
            tenant = genericFacade.findById (_tenantId, Tenant.class);
        }
        else {
            tenant = getTenant (_sessionString);
        }
        if(tenant == null) {
        		retval.setErrorMessage (
            		messageSource.getMessage("error_no_tenant", null, request.getLocale()));
        }
        else {
            List<MasterMenu> listMasterMenu = masterMenufacade.findByTenant (tenant);
            if(listMasterMenu != null) {
                retval.setData (listMasterMenu);
            }
        }
        return new ResponseEntity<MasterMenuListDTO>(retval, null, HttpStatus.OK);
    }
    
    @RequestMapping(value = "mastermenu", method = RequestMethod.POST,
    		produces = {"application/json"} 
	)
    public ResponseEntity<MasterMenuDTO> addMasterMenu(
        @RequestPart(value = "uploadFile", required = false) MultipartFile file,
        @RequestParam("name") String _name,
        @RequestParam("konten") String _konten,
        @RequestParam(value = "memo", required = false) String _memo, 
        @RequestParam("sessionString") String _sessionString ) throws Exception {
        
        MasterMenuDTO retval = new MasterMenuDTO();
		String errorMessages = null;
		
        if(!validateLogin (_sessionString)) {
            retval.setErrorMessage (messageSource.getMessage("error_no_login_session", null, 
            		request.getLocale()));
        }
        else {
            UserCred user = (UserCred)usc.getSession (_sessionString, false).getObject ("user");
            Tenant tenant = user.getTenant ();
            if(file != null) {
                String uploadedFileLocation = sCtx.getInitParameter ("imagePath") + tenant.getId () + "/" + file.getOriginalFilename();
                File dir = new File(sCtx.getInitParameter ("imagePath") + tenant.getId ());
                if(!dir.exists ()) {
                    dir.mkdir ();
                }
                writeToFile(file.getInputStream(), uploadedFileLocation);
            }
            
            MasterMenu mm = null;
            
            try {
            		mm = masterMenufacade.create (tenant, _name, _konten, _memo, file!= null ? file.getOriginalFilename() : null);
            }
            catch(ConstraintViolationException exception) {
            		exception.printStackTrace();
        			errorMessages = "";
            		for(ConstraintViolation cv : exception.getConstraintViolations()) {
            			errorMessages += cv.getMessage() + "\n";
            		}
            		errorMessages = errorMessages.substring(0, errorMessages.length() - 1);
            		System.out.println("ERROR MESSAGES: " + errorMessages);
            }
            
            if(errorMessages != null) {
            		retval.setErrorMessage(errorMessages);
            }
            else {
            		retval.setData (mm);
            }
        }
        return new ResponseEntity<MasterMenuDTO>(retval, null, HttpStatus.CREATED);
    }
    
   
    @RequestMapping(value = "mastermenu", method = RequestMethod.PUT,
    		consumes = {"application/json"}, produces = {"application/json"}
	)
    public ResponseEntity<MasterMenuDTO> editMasterMenuWithJSon(@RequestBody MasterMenuDTO _dto ) {
        MasterMenuDTO retval = new MasterMenuDTO();
        String errorMessages = null;
        
        if(!validateLogin (_dto.getSessionString())) {
            retval.setErrorMessage (messageSource.getMessage("error_no_login_session", null, 
            		request.getLocale()));
        }
        else {
        		UserCred user = (UserCred)usc.getSession (_dto.getSessionString(), false).getObject ("user");
	        Tenant tenant = user.getTenant ();
            MasterMenu mm = masterMenufacade.findById (_dto.getId ());
            mm.setName (_dto.getName () != null ? _dto.getName () : mm.getName ());
            mm.setKonten (_dto.getKonten () != null ? _dto.getKonten () : mm.getKonten ());
            mm.setMemo (_dto.getMemo () != null ? _dto.getMemo () : mm.getMemo ());
            mm.setActive (_dto.isActive () != null ? _dto.isActive () : mm.isActive ());
            try {
            		mm = masterMenufacade.edit (mm);
            }
            catch(ConstraintViolationException exception) {
	            	exception.printStackTrace();
	        		System.out.println("ERROR MESSAGES: " + exception.getMessage());
	    			errorMessages = "";
	        		for(ConstraintViolation cv : exception.getConstraintViolations()) {
	        			errorMessages += cv.getMessage() + "\n";
	        		}
	        		errorMessages = errorMessages.substring(0, errorMessages.length() - 1);
            }
            
            if(errorMessages != null) {
            		retval = new MasterMenuDTO();
            		retval.setErrorMessage(errorMessages);
            }
            else if(_dto.getUploadFile() != null ) {
                String uploadedFileLocation = sCtx.getInitParameter ("imagePath") + tenant.getId () + "/" + _dto.getImage();
            		File dir = new File(sCtx.getInitParameter ("imagePath") + tenant.getId ());
                if(!dir.exists ()) {
                    dir.mkdir ();
                }
                mm.setImage(_dto.getImage());
                writeToFile(Base64.getDecoder ().decode (_dto.getUploadFile()), uploadedFileLocation);
            }
            retval.setData (mm);
        }
        return new ResponseEntity<MasterMenuDTO>(retval, null, HttpStatus.CREATED);
    }
    
    @RequestMapping(value = "mastermenu", method = RequestMethod.DELETE,
    		produces = {"application/json"}
	)
    public ResponseEntity<StandardDTO> deleteMasterMenu(
    		@RequestParam("id") Integer _id, 
    		@RequestParam("sessionString") String _sessionString) throws Exception {
        
        StandardDTO retval = new StandardDTO();
        if(!validateLogin(_sessionString)) {
            retval.setErrorMessage (messageSource.getMessage("error_no_login_session", null, 
            		request.getLocale()));
        }
        else {
            masterMenufacade.delete (_id);
        }
        return new ResponseEntity<StandardDTO>(retval, null, HttpStatus.OK);
    }
    
//    @GET
//    @Path("/scheduledpackage")
//    @Produces(MediaType.APPLICATION_JSON)
//    public Response getScheduledPackages(@QueryParam("sessionString")String _sessionString) throws Exception
//    {
//        boolean res = validateLogin(_sessionString);
//        ScheduledPackageListDTO retval = new ScheduledPackageListDTO();
//        if(!res) {
//            retval.setErrorMessage ("ERROR, NO LOGIN SESSION IS CURRENTLY ACTIVE");
//        }
//        else {
//            List<ScheduledPackage> listScheduledPackage = genericFacade.findAll (ScheduledPackage.class);
//            if(listScheduledPackage != null) {
//                retval.setData (listScheduledPackage);
//            }
//        }
//        return Response.ok ().entity (retval).type (MediaType.APPLICATION_JSON).build ();
//    }
    
//    @GET
//    @Path("/scheduledpackage")
//    @Produces(MediaType.APPLICATION_JSON)
    @RequestMapping(value = "scheduledpackage", method = RequestMethod.GET,
    		produces = {"application/json"}
	)
    public ResponseEntity<ScheduledPackageListDTO> getScheduledPackages(
    		@RequestParam("sessionString")String _sessionString,
    		@RequestParam(value = "tenantId", required = false) String _tenantId) throws Exception
    {
        Tenant tenant = null;
        ScheduledPackageListDTO retval = new ScheduledPackageListDTO();

        if(_tenantId != null) {
            tenant = genericFacade.findById (_tenantId, Tenant.class);
        }
        else if(_sessionString != null) {
            tenant = getTenant (_sessionString);
        }
                
        if(tenant == null) {
            retval.setErrorMessage (
            		messageSource.getMessage("error_no_tenant", null, request.getLocale()));
        }
        else {
            List<ScheduledPackage> listScheduledPackage = spFacade.findByTenant (tenant);
            if(listScheduledPackage != null) {
                retval.setData (listScheduledPackage);
            }
        }
//        ResponseBuilder rp;
//        if(retval.getErrorMessage () == null)
//            rp = Response.ok();
//        else
//            rp = Response.serverError();
        
        return new ResponseEntity<ScheduledPackageListDTO>(retval, null, HttpStatus.OK);

        
//        return rp.entity (retval).type (MediaType.APPLICATION_JSON).build ();
    }
        
//    @POST
//    @Path("/scheduledpackage")
//    @Consumes(MediaType.MULTIPART_FORM_DATA)
//    @Produces(MediaType.APPLICATION_JSON)
//    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    @RequestMapping(value = "scheduledpackage", method = RequestMethod.POST,
    		produces = {"application/json"}
	)
    public ResponseEntity<ScheduledPackageDTO> addScheduledPackage(
        @RequestPart(value = "uploadFile", required = false) MultipartFile file,
//        @FormDataParam("uploadFile") FormDataContentDisposition fileDetail,
        @RequestParam("name") String _name,
        @RequestParam("konten") String _konten,
        @RequestParam(value = "memo", required = false) String _memo,
        @RequestParam("price") Double _price,
        @RequestParam(value = "minOrder", required = false) Integer _minOrder,
        @RequestParam(value = "tags", required = false) String _tags,
        @RequestParam("menus") String _menus,
        @RequestParam("sessionString") String _sessionString ) throws Exception {
                
        ScheduledPackageDTO retval = new ScheduledPackageDTO();
        if(!validateLogin (_sessionString)) {
            retval.setErrorMessage ("ERROR, NO LOGIN SESSION IS ACTIVE");
        }
        else {
            UserCred user = (UserCred)usc.getSession (_sessionString, false).getObject ("user");
            Tenant tenant = user.getTenant ();
            String imageName = file != null ? file.getOriginalFilename() : null;
            if(file != null) {
                String uploadedFileLocation = "/Users/sunwell/Documents/upload-example/" + tenant.getId () + "/" + file.getOriginalFilename();
                File dir = new File("/Users/sunwell/Documents/upload-example/" + tenant.getId ());
                if(!dir.exists ()) {
                    dir.mkdir ();
                }
                writeToFile(file.getInputStream(), uploadedFileLocation);
            }
            ScheduledPackage sp = new ScheduledPackage (tenant, _name, _konten, _memo, _price, _minOrder, true, imageName != null ? imageName : "");
            if(_tags != null && _tags.length () > 0) {
                String[] tagNames = _tags.split (";");
                List<MasterTag> listMasterTags = genericFacade.findAll (MasterTag.class);
                List<MasterTag> listScheduledPackageTags = new LinkedList<>();
                if(listMasterTags != null) {
                    for (String tagName : Arrays.asList (tagNames)) {
                        for (MasterTag mt : listMasterTags) {
                            if(tagName.trim ().toLowerCase ().equals (mt.getName ().trim ().toLowerCase ())) {
                                listScheduledPackageTags.add (mt);
                            }
                        }
                    }
                    sp.setTags (listScheduledPackageTags);
                }
            }
            
            if(_menus != null && _menus.length () > 0) {
                List<ScheduledPackageMenu> menus = new LinkedList<>();
                String[] stringMenus = _menus.split (";");
                for (String stringMenu : stringMenus) {
                    ScheduledPackageMenu menu = new ScheduledPackageMenu ();
                    String[] menuInfo = stringMenu.split ("_");
                    MasterMenu mm = genericFacade.findById (Integer.valueOf (menuInfo[0]), MasterMenu.class);
                    menu.setMasterMenu (mm);
                    menu.setDate (Integer.valueOf (menuInfo[1]));
                    menu.setCreatedAt (new Date ());
                    menu.setScheduledPackage (sp);
                    menus.add (menu);
                }
                sp.setMenus (menus);
            }
            
            sp = genericFacade.create (sp);
            retval.setData (sp);
        }
        return new ResponseEntity<ScheduledPackageDTO>(retval, null, HttpStatus.CREATED);
//        return Response.ok ().entity (retval).type (MediaType.APPLICATION_JSON).build ();
    }
    
//    @PUT
//    @Path("/scheduledpackage")
//    @Consumes(MediaType.MULTIPART_FORM_DATA)
//    @Produces(MediaType.APPLICATION_JSON)
//    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    @RequestMapping(value = "scheduledpackage", method = RequestMethod.PUT,
    		produces = {"application/json"}
	)
    public ResponseEntity<ScheduledPackageDTO> editScheduledPackage(
        @RequestParam(value = "uploadFile", required = false) MultipartFile file,
//        @FormDataParam("uploadFile") FormDataContentDisposition fileDetail,
        @RequestParam("id") Integer _id,
        @RequestParam(value = "name", required = false) String _name,
        @RequestParam(value = "konten", required = false) String _konten,
        @RequestParam(value = "memo", required = false) String _memo,
        @RequestParam(value = "price", required = false) Double _price,
        @RequestParam(value = "minOrder", required = false) Integer _minOrder,
        @RequestParam(value = "available", required = false) Boolean available,
        @RequestParam(value = "tags", required = false) String _tags,
        @RequestParam(value = "menus", required = false) String _menus,
        @RequestParam("sessionString") String _sessionString ) throws Exception {
                
        ScheduledPackageDTO retval = new ScheduledPackageDTO();
        if(!validateLogin (_sessionString)) {
            retval.setErrorMessage ("ERROR, NO LOGIN SESSION IS ACTIVE");
        }
        else {
            UserCred user = (UserCred)usc.getSession (_sessionString, false).getObject ("user");
            Tenant tenant = user.getTenant ();
            if(file != null ) {
                String uploadedFileLocation = "/Users/sunwell/Documents/upload-example/" + tenant.getId () + "/" + file.getOriginalFilename();
                File dir = new File("/Users/sunwell/Documents/upload-example/" + tenant.getId ());
                if(!dir.exists ()) {
                    dir.mkdir ();
                }
                writeToFile(file.getInputStream(), uploadedFileLocation);
            }
            ScheduledPackage sp = genericFacade.findById (_id, ScheduledPackage.class);
            sp.setImage (file!= null ? file.getOriginalFilename(): sp.getImage ());
            sp.setName (_name != null ? _name : sp.getName ());
            sp.setKonten (_konten != null ? _konten : sp.getKonten ());
            sp.setMemo (_memo != null ? _memo : sp.getMemo ());
            sp.setPrice (_price != null ? _price : sp.getPrice ());
            sp.setMinOrder (_minOrder != null ? _minOrder : sp.getMinOrder ());
            sp.setAvailable (available != null ? available : sp.isAvailable ());
            if(_tags != null && _tags.length () > 0) {
                String[] tagNames = _tags.split (";");
                List<MasterTag> listMasterTags = genericFacade.findAll (MasterTag.class);
                List<MasterTag> listScheduledPackageTags = new LinkedList<>();
                if(listMasterTags != null) {
                    for (String tagName : Arrays.asList (tagNames)) {
                        for (MasterTag mt : listMasterTags) {
                            if(tagName.trim ().toLowerCase ().equals (mt.getName ().trim ().toLowerCase ())) {
                                listScheduledPackageTags.add (mt);
                            }
                        }
                    }
                    sp.setTags (listScheduledPackageTags);
                }
            }
            
            sp.setMenus (null);
            genericFacade.flush ();
            
            if(_menus != null && _menus.length () > 0) {
                List<ScheduledPackageMenu> menus = new LinkedList<>();
                String[] stringMenus = _menus.split (";");
                for (String stringMenu : stringMenus) {
                    ScheduledPackageMenu menu = new ScheduledPackageMenu ();
                    String[] menuInfo = stringMenu.split ("_");
                    MasterMenu mm = genericFacade.findById (Integer.valueOf (menuInfo[0]), MasterMenu.class);
                    menu.setMasterMenu (mm);
                    menu.setDate (Integer.valueOf (menuInfo[1]));
                    menu.setCreatedAt (new Date ());
                    menu.setScheduledPackage (sp);
                    menus.add (menu);
                }
                sp.setMenus (menus);
            }
            
            sp = genericFacade.edit (sp);
            retval.setData (sp);
        }
        return new ResponseEntity<ScheduledPackageDTO>(retval, null, HttpStatus.CREATED);
//        return Response.ok ().entity (retval).type (MediaType.APPLICATION_JSON).build ();
    }
    
//    @DELETE
//    @Path("/scheduledpackage")
//    @Consumes(MediaType.APPLICATION_JSON)
//    @Produces(MediaType.APPLICATION_JSON)
//    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    @RequestMapping(value = "scheduledpackage", method = RequestMethod.DELETE,
    		produces = {"application/json"}
	)
    public ResponseEntity<StandardDTO> deleteScheduledPackage(@QueryParam("id") Integer _id, @QueryParam("sessionString") String _sessionString) {
        StandardDTO retval = new StandardDTO();
        if(!validateLogin (_sessionString)) {
            retval.setErrorMessage ("ERROR, NO LOGIN SESSION IS ACTIVE");
        }
        else {
            genericFacade.delete (_id, ScheduledPackage.class);
        }
        return new ResponseEntity<StandardDTO>(retval, null, HttpStatus.OK);
//        return Response.ok ().entity (retval).type (MediaType.APPLICATION_JSON).build ();
    }
    
//    @GET
//    @Path("/menuindividual")
//    @Produces(MediaType.APPLICATION_JSON)
//    public Response getMenuIndividual(@QueryParam("sessionString")String _sessionString) throws Exception
//    {
//        boolean res = validateLogin(_sessionString);
//        MenuIndividualListDTO retval = new MenuIndividualListDTO();
//        if(!res) {
//            retval.setErrorMessage ("ERROR, NO LOGIN SESSION IS CURRENTLY ACTIVE");
//        }
//        else {
//            List<MenuIndividual> listMenuIndividual = genericFacade.findAll (MenuIndividual.class);
//            if(listMenuIndividual != null) {
//                retval.setData (listMenuIndividual);
//            }
//        }
//        return Response.ok ().entity (retval).type (MediaType.APPLICATION_JSON).build ();
//    }
    
//    @GET
//    @Path("/menuindividual")
//    @Produces(MediaType.APPLICATION_JSON)
    @RequestMapping(value = "menuindividual", method = RequestMethod.GET,
    		produces = {"application/json"}
	)
    public ResponseEntity<MenuIndividualListDTO> getMenuIndividual(
    		@RequestParam("sessionString")String _sessionString,
    		@RequestParam(value = "tenantId", required = false) String _tenantId) throws Exception
    {
        Tenant tenant = null;
        MenuIndividualListDTO retval = new MenuIndividualListDTO();
        
        if(_tenantId != null) {
            tenant = genericFacade.findById (_tenantId, Tenant.class);
        }
        else if(_sessionString != null) {
            tenant = getTenant (_sessionString);
        }
        if(tenant == null) {
            retval.setErrorMessage ("ERROR, NO TENANT FOUND FOR CURRENT REQUEST");
        }
        else {
            List<MenuIndividual> listMenuIndividual = miFacade.findByTenant (tenant);
            if(listMenuIndividual != null) {
                retval.setData (listMenuIndividual);
            }
        }
        return new ResponseEntity<MenuIndividualListDTO>(retval, null, HttpStatus.OK);
//        return Response.ok ().entity (retval).type (MediaType.APPLICATION_JSON).build ();
    }
        
//    @POST
//    @Path("/menuindividual")
//    @Consumes(MediaType.MULTIPART_FORM_DATA)
//    @Produces(MediaType.APPLICATION_JSON)
//    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    @RequestMapping(value = "menuindividual", method = RequestMethod.POST,
    		consumes = {"application/json"}, produces = {"application/json"}
	)
    public ResponseEntity<MenuIndividualDTO> addMenuIndividual(
        @RequestPart(value = "uploadFile", required = false) MultipartFile file,
//        @FormDataParam("uploadFile") FormDataContentDisposition fileDetail,
        @RequestParam("name") String _name,
        @RequestParam("konten") String _konten,
        @RequestParam(value = "memo", required = false) String _memo,
        @RequestParam("price") Double _price,
        @RequestParam(value = "tags", required = false) String _tags,
        @RequestParam("menus") String _menus,
        @RequestParam("sessionString") String _sessionString ) throws Exception {
                
        MenuIndividualDTO retval = new MenuIndividualDTO();
        if(!validateLogin (_sessionString)) {
            retval.setErrorMessage ("ERROR, NO LOGIN SESSION IS ACTIVE");
        }
        else {
            UserCred user = (UserCred)usc.getSession (_sessionString, false).getObject ("user");
            Tenant tenant = user.getTenant ();
            String imageName = file != null ? file.getOriginalFilename() : null;
            if(file != null) {
                String uploadedFileLocation = "/Users/sunwell/Documents/upload-example/" + tenant.getId () + "/" + imageName;
                File dir = new File("/Users/sunwell/Documents/upload-example/" + tenant.getId ());
                if(!dir.exists ()) {
                    dir.mkdir ();
                }
                writeToFile(file.getInputStream(), uploadedFileLocation);
            }
            MenuIndividual mi = new MenuIndividual (tenant, _name, _konten, _memo, _price, imageName);
            if(_tags != null && _tags.length () > 0) {
                String[] tagNames = _tags.split (";");
                List<MasterTag> listMasterTags = genericFacade.findAll (MasterTag.class);
                List<MasterTag> listMenuIndividualTags = new LinkedList<>();
                if(listMasterTags != null) {
                    for (String tagName : Arrays.asList (tagNames)) {
                        for (MasterTag mt : listMasterTags) {
                            if(tagName.trim ().toLowerCase ().equals (mt.getName ().trim ().toLowerCase ())) {
                                listMenuIndividualTags.add (mt);
                            }
                        }
                    }
                    mi.setTags (listMenuIndividualTags);
                }
            }
            
            if(_menus != null && _menus.length () > 0) {
                List<MenuIndividualMenu> menus = new LinkedList<>();
                String[] stringMenus = _menus.split (";");
                for (String stringMenu : stringMenus) {
                    MenuIndividualMenu menu = new MenuIndividualMenu ();
                    MasterMenu mm = genericFacade.findById (Integer.valueOf (stringMenu), MasterMenu.class);
                    menu.setMasterMenu (mm);
                    menu.setCreatedAt (new Date ());
                    menu.setMenuIndividual (mi);
                    menus.add (menu);
                }
                mi.setMenus (menus);
            }
            
            mi = genericFacade.create (mi);
            retval.setData (mi);
        }
        return new ResponseEntity<MenuIndividualDTO>(retval, null, HttpStatus.CREATED);
//        return Response.ok ().entity (retval).type (MediaType.APPLICATION_JSON).build ();
    }
    
//    @PUT
//    @Path("/menuindividual")
//    @Consumes(MediaType.MULTIPART_FORM_DATA)
//    @Produces(MediaType.APPLICATION_JSON)
//    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    @RequestMapping(value = "menuindividual", method = RequestMethod.PUT,
    		consumes = {"application/json"}, produces = {"application/json"}
	)
    public ResponseEntity<MenuIndividualDTO> editMenuIndividual( 
        @RequestPart(value = "uploadFile", required = false) MultipartFile file,
//        @FormDataParam("uploadFile") FormDataContentDisposition fileDetail,
        @RequestParam("id") Integer _id,
        @RequestParam(value = "name", required = false) String _name,
        @RequestParam(value = "konten", required = false) String _konten,
        @RequestParam(value = "memo", required = false) String _memo,
        @RequestParam(value = "price", required = false) Double _price,
        @RequestParam(value = "available", required = false) Boolean available,
        @RequestParam(value = "tags", required = false) String _tags,
        @RequestParam(value = "menus", required = false) String _menus,
        @RequestParam("sessionString") String _sessionString ) throws Exception {
                
        MenuIndividualDTO retval = new MenuIndividualDTO();
        UserCred user = getUser (_sessionString);
        Tenant tenant = user.getTenant ();
        if(user == null) {
            retval.setErrorMessage ("ERROR, NO LOGIN SESSION IS ACTIVE");
        }
        else {
//            UserCred user = (UserCred)usc.getSession (_sessionString, false).getObject ("user");
            if(file != null ) {
                String uploadedFileLocation = "/Users/sunwell/Documents/upload-example/" + tenant.getId () + "/" + file.getOriginalFilename();
                File dir = new File("/Users/sunwell/Documents/upload-example/" + tenant.getId ());
                if(!dir.exists ()) {
                    dir.mkdir ();
                }
                writeToFile(file.getInputStream(), uploadedFileLocation);
            }
            MenuIndividual mi = genericFacade.findById (_id, MenuIndividual.class);
            mi.setImage (file != null ? file.getOriginalFilename(): mi.getImage ());
            mi.setName (_name != null ? _name : mi.getName ());
            mi.setKonten (_konten != null ? _konten : mi.getKonten ());
            mi.setMemo (_memo != null ? _memo : mi.getMemo ());
            mi.setPrice (_price != null ? _price : mi.getPrice ());
            mi.setAvailable (available != null ? available : mi.isAvailable ());
            if(_tags != null && _tags.length () > 0) {
                String[] tagNames = _tags.split (";");
                List<MasterTag> listMasterTags = genericFacade.findAll (MasterTag.class);
                List<MasterTag> listMenuIndividualTags = new LinkedList<>();
                if(listMasterTags != null) {
                    for (String tagName : Arrays.asList (tagNames)) {
                        for (MasterTag mt : listMasterTags) {
                            if(tagName.trim ().toLowerCase ().equals (mt.getName ().trim ().toLowerCase ())) {
                                listMenuIndividualTags.add (mt);
                            }
                        }
                    }
                    mi.setTags (listMenuIndividualTags);
                }
            }
            
            mi.setMenus (null);
            this.genericFacade.flush ();
            
            if(_menus != null && _menus.length () > 0) {
                List<MenuIndividualMenu> menus = new LinkedList<>();
                String[] stringMenus = _menus.split (";");
                for (String stringMenu : stringMenus) {
                    MenuIndividualMenu menu = new MenuIndividualMenu ();
                    MasterMenu mm = genericFacade.findById (Integer.valueOf (stringMenu), MasterMenu.class);
                    menu.setMasterMenu (mm);
                    menu.setCreatedAt (new Date ());
                    menu.setMenuIndividual (mi);
                    menus.add (menu);
                }
                mi.setMenus (menus);
            }
            
            mi = genericFacade.edit (mi);
            retval.setData (mi);
        }
        
        return new ResponseEntity<MenuIndividualDTO>(retval, null, HttpStatus.CREATED);

//        return Response.ok ().entity (retval).type (MediaType.APPLICATION_JSON).build ();
    }
    
//    @DELETE
//    @Path("/menuindividual")
//    @Consumes(MediaType.APPLICATION_JSON)
//    @Produces(MediaType.APPLICATION_JSON)
//    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    @RequestMapping(value = "menuindividual", method = RequestMethod.DELETE,
    		produces = {"application/json"}
	)
    public ResponseEntity<StandardDTO> deleteMenuIndividual(
    		@RequestParam("id") Integer _id, 
    		@RequestParam("sessionString") String _sessionString) {
                
        StandardDTO retval = new StandardDTO();
        if(!validateLogin (_sessionString)) {
            retval.setErrorMessage ("ERROR, NO LOGIN SESSION IS ACTIVE");
        }
        else {
            genericFacade.delete (_id, MenuIndividual.class);
        }
        
        return new ResponseEntity<StandardDTO>(retval, null, HttpStatus.OK);
//        return Response.ok ().entity (retval).type (MediaType.APPLICATION_JSON).build ();
    }
        
    
    
//    @GET
//    @Path("/tags")
//    @Produces(MediaType.APPLICATION_JSON)
    @RequestMapping(value = "tags", method = RequestMethod.GET,
    		produces = {"application/json"}
	)
    public ResponseEntity<MasterTagListDTO> getTags(@RequestParam("sessionString")String _sessionString) throws Exception
    {
        System.out.println ("GET TAGS CALLED");
        boolean res = validateLogin(_sessionString);
        MasterTagListDTO retval = new MasterTagListDTO();
        if(!res) {
            retval.setErrorMessage ("ERROR, NO LOGIN SESSION IS CURRENTLY ACTIVE");
        }
        else {
            List<MasterTag> listT = genericFacade.findAll (MasterTag.class);
            System.out.println ("LIST LENGTH: " + listT.size ());
            if(listT != null) {
                retval.setData (listT);
            }
        }
        return new ResponseEntity<MasterTagListDTO>(retval, null, HttpStatus.OK);
//        return Response.ok ().entity (retval).type (MediaType.APPLICATION_JSON).build ();
    }
 
    private void writeToFile(InputStream uploadedInputStream,
        String uploadedFileLocation) {

        try {
            OutputStream out = new FileOutputStream(new File(
                            uploadedFileLocation));
            int read = 0;
            byte[] bytes = new byte[1024];

            out = new FileOutputStream(new File(uploadedFileLocation));
            while ((read = uploadedInputStream.read(bytes)) != -1) {
                    out.write(bytes, 0, read);
            }
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    
    private boolean validateLogin(String _sessionString) {
        return getUser (_sessionString) != null ? true : false;
    }
    
    private UserCred getUser(String _sessionString) {
        UserSession session = usc.getSession (_sessionString, false);
        if(session == null)
            return null;
        
        UserCred usr = (UserCred)session.getObject ("user");
        return usr;
    }
    
    private Tenant getTenant(String _sessionString) {
        UserCred user = getUser (_sessionString);
        if(user == null)
            return null;
        
        return user.getTenant ();
    }
    
    private static void writeToFile(byte[] _bytes, String uploadedFileLocation) {

        try {
            OutputStream out = new FileOutputStream(new File(
                            uploadedFileLocation));
            int read = 0;
//            byte[] bytes = new byte[1024];

            out = new FileOutputStream(new File(uploadedFileLocation));
            out.write (_bytes);
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
