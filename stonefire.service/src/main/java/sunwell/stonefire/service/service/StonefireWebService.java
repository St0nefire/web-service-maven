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

import aegwyn.core.web.util.Util;



import aegwyn.core.web.model.UserSession;
import aegwyn.core.web.model.UserSessionContainer;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.servlet.ServletContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.StreamingOutput;
//import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
//import org.glassfish.jersey.media.multipart.FormDataParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import sunwell.stonefire.base.bus.GenericFacade;
import sunwell.stonefire.base.bus.MasterMenuFacade;
import sunwell.stonefire.base.bus.TenantFacade;
import sunwell.stonefire.base.bus.UserCredFacade;
import sunwell.stonefire.base.dto.MasterMenuDTO;
import sunwell.stonefire.base.dto.MasterMenuListDTO;
import sunwell.stonefire.base.dto.ProvinceListDTO;
import sunwell.stonefire.base.dto.TenantDTO;
import sunwell.stonefire.base.dto.TenantsListDTO;
import sunwell.stonefire.base.dto.UserCredDTO;
import sunwell.stonefire.core.entity.MasterMenu;
import sunwell.stonefire.core.entity.Province;
import sunwell.stonefire.core.entity.Tenant;
import sunwell.stonefire.core.entity.TenantCategory;
import sunwell.stonefire.core.entity.UserCred;

/**
 *
 * @author Benny
 */
//@Stateless
//@Path("")
@Controller
@Transactional
public class StonefireWebService 
{
//    @EJB
	@Autowired
    UserCredFacade userFacade;
    
//    @EJB
//    UsersFacadeREST usersFacade;
    
//    @EJB
	@Autowired
    MasterMenuFacade masterMenufacade;
    
//    @EJB
	@Autowired
    TenantFacade tenantFacade;
    
//    @EJB
	@Autowired
    GenericFacade genericFacade;
    
    @Inject
    UserSessionContainer usc;
    
    @Autowired
    ServletContext sCtx;
    
//    @POST
//    @Path("/login")
//    @Consumes(MediaType.APPLICATION_JSON)
//    @Produces(MediaType.APPLICATION_JSON)
    @RequestMapping(value = "login", method = RequestMethod.POST,
    		consumes = "application/json", produces = {"application/json"}
	)
    public ResponseEntity<UserCredDTO> login(UserCredDTO _dto) throws Exception
    {
        UserCredDTO retval = new UserCredDTO();
        if(_dto.getEmail () == null) {
            retval.setErrorMessage ("ERROR, NO EMAIL IS SPECIFIED");
        }
        else if(_dto.getPassword () == null) {
            retval.setErrorMessage ("ERROR, NO PASSWORD IS SPECIFIED");
        }
        else {
            UserCred usr = userFacade.validate (_dto.getEmail (), _dto.getPassword ());
            if(usr == null) {
                retval.setErrorMessage ("ERROR, CAN't FIND THE SPECIFIED USER");
            }
            else {
                UserSession us = usc.newSession ();
                us.setSessionName ("Login");
                us.addObject ("user", usr);
                us.setLastActivity (Calendar.getInstance ());
                retval.setData (usr);
                retval.setSessionString (us.getSessionId ());
            }
        }
        return new ResponseEntity<UserCredDTO>(retval, null, HttpStatus.CREATED);
//        return Response.ok ().entity (retval).type (MediaType.APPLICATION_JSON).build ();
    }
    
//    @POST
//    @Path("/logout")
//    @Consumes(MediaType.APPLICATION_JSON)
//    @Produces(MediaType.APPLICATION_JSON)
    @RequestMapping(value = "logout", method = RequestMethod.POST,
    		consumes = "application/json", produces = {"application/json"}
	)
    public ResponseEntity<UserCredDTO> logout(UserCredDTO _dto) throws Exception
    {
        UserCredDTO retval = new UserCredDTO();
        if(!validateLogin (_dto.getSessionString ())) {
            retval.setErrorMessage ("ERROR, NO LOGIN SESSION IS ACTIVE");
        }
        else {
            usc.removeSession (_dto.getSessionString ());
        }
        return new ResponseEntity<UserCredDTO>(retval, null, HttpStatus.CREATED);
//        return Response.ok ().entity (retval).type (MediaType.APPLICATION_JSON).build ();
    }
    
//    @POST
//    @Path("/register")
//    @Consumes(MediaType.MULTIPART_FORM_DATA)
//    @Produces(MediaType.APPLICATION_JSON)
//    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    @RequestMapping(value = "register", method = RequestMethod.POST,
    		consumes = "application/json", produces = {"application/json"}
	)
    public ResponseEntity<UserCredDTO> register(
        @RequestPart(value = "tenantUploadFile", required = false) MultipartFile tenantFile,
//        @FormDataParam("tenantUploadFile") FormDataContentDisposition _tenantFileDetail,
        @RequestPart(value = "uploadFile", required = false) MultipartFile file,
//        @FormDataParam("uploadFile") FormDataContentDisposition _fileDetail,
        @RequestParam("tenantName") String _tenantName,
        @RequestParam("tenantEmail") String _tenantEmail,
        @RequestParam("tenantPassword") String _tenantPassword, 
        @RequestParam("tenantPhoneNumber") String _tenantPhoneNumber,
        @RequestParam(value = "tenantProvince", required = false) String _tenantProvince,
        @RequestParam(value = "tenantRegency") String _tenantRegency,
        @RequestParam(value = "tenantAddress", required = false) String _tenantAddress,
        @RequestParam(value = "description", required = false) String _description,
        @RequestParam("name") String _name,
        @RequestParam("email") String _email,
        @RequestParam("password") String _password, 
        @RequestParam("phoneNumber") String _phoneNumber, 
        @RequestParam("sessionString") String _sessionString ) throws Exception {
        
        System.out.println ("CALLED REGISTER");
        
        UserCredDTO retval = new UserCredDTO();
        if(_tenantName == null || _tenantName.length () <= 0) {
            retval.setErrorMessage ("ERROR, NO TENANT NAME IS SPECIFIED");
        }
        else if(_tenantEmail == null || _tenantEmail.length () <= 0) {
            System.out.println ("EXCPT IN TE");
            retval.setErrorMessage ("ERROR, NO TENANT EMAIL IS SPECIFIED");
        }
        else if(_tenantPassword == null || _tenantPassword.length () <= 0) {
            retval.setErrorMessage ("ERROR, NO TENANT PASSWORD IS SPECIFIED");
        }
        else if(_tenantPhoneNumber == null || _tenantPhoneNumber.length () <= 0) {
            retval.setErrorMessage ("ERROR, NO TENANT PHONE NUMBER IS SPECIFIED");
        }
        else if(_name == null || _name.length () <= 0) {
            retval.setErrorMessage ("ERROR, NO NAME IS SPECIFIED");
        }
        else if(_email == null || _email.length () <= 0) {
            retval.setErrorMessage ("ERROR, NO EMAIL IS SPECIFIED");
        }
        else if(_password == null || _password.length () <= 0) {
            retval.setErrorMessage ("ERROR, NO PASSWORD IS SPECIFIED");
        }
        else if(_phoneNumber == null || _phoneNumber.length () <= 0) {
            retval.setErrorMessage ("ERROR, NO PHONE NUMBER IS SPECIFIED");
        }
//        else if(_tenantProvince == null || _tenantProvince.length () <= 0) {
//            retval.setErrorMessage ("ERROR, NO PROVINCE IS SPECIFIED");
//        }
//        else if(_tenantRegency == null || _tenantRegency.length () <= 0) {
//            retval.setErrorMessage ("ERROR, No CITY IS SPECIFIED");
//        }
//        else if(_tenantAddress == null || _tenantAddress.length () <= 0) {
//            retval.setErrorMessage ("ERROR, NO ADDRESS IS SPECIFIED");
//        }
        else {
            Tenant tenant = new Tenant ();
            tenant.setEmail (_tenantEmail);
            tenant.setPassword (_tenantPassword);
            tenant.setName (_tenantName);
            tenant.setPhoneNumber (_tenantPhoneNumber);
            tenant.setDescription (_description);
            tenant.setProvince (_tenantProvince);
            tenant.setCity (_tenantRegency);
            tenant.setAddress (_tenantAddress);
            tenant.setProfilePicture (tenantFile != null ? tenantFile.getOriginalFilename() : null);
            tenant.setCreatedAt (new Date());
            tenant.setCategory (genericFacade.findById ("1", TenantCategory.class));
            
            UserCred usr = new UserCred ();
            usr.setEmail (_email);
            usr.setPassword (_password);
            usr.setName (_name);
            usr.setPhoneNumber (_phoneNumber);
            usr.setProfilePicture (file != null ? file.getOriginalFilename() : null);
            usr.setCreatedAt (new Date());
            
            tenant = genericFacade.create (tenant);
            usr.setTenant (tenant);
            usr = genericFacade.create (usr);
            
            genericFacade.refresh (tenant);
            genericFacade.refresh (usr);
            
            String path = "/Users/sunwell/Documents/upload-example/" + tenant.getId () + "/";
            File dir = new File(path);
            if(!dir.exists ()) {
                dir.mkdir ();
            }
            
            if(tenantFile != null) {
                String uploadedFileLocation = path + tenantFile.getOriginalFilename();
                writeToFile(tenantFile.getInputStream(), uploadedFileLocation);
            }
            
            if(file != null) {
                String uploadedFileLocation = path + file.getOriginalFilename();
                writeToFile(file.getInputStream(), uploadedFileLocation);
            }
            retval.setData (usr);
        }
        return new ResponseEntity<UserCredDTO>(retval, null, HttpStatus.CREATED);
//        return Response.ok ().entity (retval).type (MediaType.APPLICATION_JSON).build ();
    }
    
//    @PUT
//    @Path("/tenant")
//    @Consumes(MediaType.MULTIPART_FORM_DATA)
//    @Produces(MediaType.APPLICATION_JSON)
//    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    @RequestMapping(value = "tenant", method = RequestMethod.PUT,
    		consumes = "application/json", produces = {"application/json"}
	)
    public ResponseEntity<TenantDTO> editTenant(
        @RequestPart("logo") MultipartFile logo,
//        @FormDataParam("logo") FormDataContentDisposition logoDetail,
        @RequestPart("profilePicture") MultipartFile ppFile,
//        @RequestParam("profilePicture") FormDataContentDisposition ppDetail,
        @RequestParam("name") String _name,
        @RequestParam("description") String _description,
        @RequestParam("city") String _city, 
        @RequestParam("province") String _province, 
        @RequestParam("address") String _address,
        @RequestParam("phoneNumber") String _phoneNumber, 
        @RequestParam("email") String _email,
        @RequestParam("sessionString") String _sessionString ) throws Exception {
        
        TenantDTO retval = new TenantDTO();
        if(!validateLogin (_sessionString)) {
            retval.setErrorMessage ("ERROR, NO LOGIN SESSION IS ACTIVE");
        }
        else {
            UserCred user = (UserCred)usc.getSession (_sessionString, false).getObject ("user");
            Tenant tenant = user.getTenant ();
            if(logo != null) {
                String uploadedFileLocation = "/Users/sunwell/Documents/upload-example/" + tenant.getId () + "/" + logo.getOriginalFilename();
                File dir = new File("/Users/sunwell/Documents/upload-example/" + tenant.getId ());
                if(!dir.exists ()) {
                    dir.mkdir ();
                }
                writeToFile(logo.getInputStream(), uploadedFileLocation);
            }
            if(ppFile != null) {
                String uploadedFileLocation = "/Users/sunwell/Documents/upload-example/" + tenant.getId () + "/" + ppFile.getOriginalFilename();
                File dir = new File("/Users/sunwell/Documents/upload-example/" + tenant.getId ());
                if(!dir.exists ()) {
                    dir.mkdir ();
                }
                writeToFile(ppFile.getInputStream(), uploadedFileLocation);
            }
            System.out.println ("NAME: " + _name + " DESC: " + _description);
            tenant.setName (_name != null ? _name : tenant.getName ());
            tenant.setDescription (_description != null ? _description : tenant.getDescription ());
            tenant.setLogo (logo != null ? logo.getOriginalFilename() : tenant.getLogo ());
            tenant.setProfilePicture (ppFile != null ? ppFile.getOriginalFilename() : tenant.getProfilePicture ());
            tenant.setPhoneNumber (_phoneNumber != null ? _phoneNumber : tenant.getPhoneNumber ());
            tenant.setEmail (_email != null ? _email : tenant.getEmail ());
            tenant.setCity (_city != null ? _city : tenant.getCity ());
            tenant.setProvince (_province != null ? _province : tenant.getProvince ());
            tenant.setAddress (_address != null ? _address : tenant.getAddress ());
            System.out.println ("SUCCESS EDIT TENANT");
            tenantFacade.edit (tenant);
            retval.setData (tenant);
        }
        
        ResponseBuilder rp = null;
        if(retval.getErrorMessage () == null)
            rp = Response.ok ();
        else
            rp = Response.serverError ();
        
        return new ResponseEntity<TenantDTO>(retval, null, HttpStatus.CREATED);
//        return rp.entity (retval).type (MediaType.APPLICATION_JSON).build ();
    }
    
//    @GET
//    @Path("hellousers")
//    @Produces({MediaType.APPLICATION_JSON})
//    public List<UserCred> findAll ()
//    {
//        return usersFacade.findAll ();
//    }
    
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
    
//    @GET
//    @Path("/provinces")
//    @Produces(MediaType.APPLICATION_JSON)
    @RequestMapping(value = "provinces", method = RequestMethod.GET,
    		produces = {"application/json"}
	)
    public ResponseEntity<ProvinceListDTO> getProvinces() throws Exception
    {
        ProvinceListDTO retval = new ProvinceListDTO();
        List<Province> listProvinces = genericFacade.findAll (Province.class);
        if(listProvinces != null)
            retval.setData (listProvinces);
        return new ResponseEntity<ProvinceListDTO>(retval, null, HttpStatus.OK);
//        return Response.ok ().entity (retval).type (MediaType.APPLICATION_JSON).build ();
    }
    
//    @GET
//    @Path("/tenants")
//    @Produces(MediaType.APPLICATION_JSON)
    @RequestMapping(value = "tenants", method = RequestMethod.GET,
    		produces = {"application/json"}
	)
    public ResponseEntity<TenantsListDTO> getTenants(
    		@RequestParam("province")String _province, 
			@RequestParam("city")String _city) throws Exception
    {
        System.out.println ("GET TENANTS CALLED");
        TenantsListDTO retval = new TenantsListDTO();
        List<Tenant> listTenants = tenantFacade.findByProvinceAndCity (_province, _city);
        if(listTenants != null && listTenants.size () > 0)
            retval.setData (listTenants);
        return new ResponseEntity<TenantsListDTO>(retval, null, HttpStatus.OK);
//        return Response.ok ().entity (retval).type (MediaType.APPLICATION_JSON).build ();
    }
    
//    @GET
//    @Path("/image")
//    @Produces({"image/png", "image/jpg"})
    @RequestMapping(value = "image", method = RequestMethod.GET,
    		produces = {"image/jpg", "image/png"}
	)
    public ResponseEntity<byte[]> getImage(
    		@RequestParam("sessionString")String _sessionString,
    		@RequestParam(value = "tenantId", required = false)String _tenantId,
    		@RequestParam("image") String _image) throws Exception
    {
        
        Tenant tenant = null;
        if(_tenantId != null) {
            tenant = genericFacade.findById (_tenantId, Tenant.class);
        }
        else if(_sessionString != null) {
            UserSession us = usc.getSession (_sessionString, false);
            if(us != null) {
                UserCred user = null;
                user = (UserCred)us.getObject ("user");
                tenant = user.getTenant ();
            }
            
        }
        else {
            System.out.println ("Tenant id is null");
            return null;
        }
        
        if(tenant == null) {
            System.out.println ("Tenant can't be found tid: " + _tenantId);
            return null ;
        }
        
        String path = "/Users/sunwell/Documents/upload-example/" + tenant.getId () + "/" + _image;
        File file = new File(path);
        if(file.exists ()) { 
            FileInputStream fis = new FileInputStream(file);
            long length = file.length ();
            byte[] filecontent = new byte[(int)length];
            fis.read(filecontent,0,(int)length); 
            
            return new ResponseEntity<byte[]>(filecontent, null, HttpStatus.OK);

            
//            return Response.ok().entity(new StreamingOutput(){
//                @Override
//                public void write(OutputStream output) throws IOException, WebApplicationException {
//                   output.write(filecontent);
//                   output.flush();
//                }
//                }).build();
        }
        else {
            System.out.println ("File doesn't exist, tid: " + _tenantId + " image: " + _image);
            return null;
//            return Response.noContent ().build();
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
    
//    @GET
//    @Path("/image")
//    @Produces({"image/png", "image/jpg"})
//    public Response getPr(@QueryParam("sessionString")String _sessionString, @QueryParam("image") String _image) throws Exception
//    {
//        boolean res = validateLogin(_sessionString);
//        UserCred user = (UserCred)usc.getSession (_sessionString, false).getObject ("user");
//        String path = "/Users/sunwell/Documents/upload-example/" + user.getId () + "/" + _image;
//        File file = new File(path);
//        if(file.exists ()) { 
//            FileInputStream fis = new FileInputStream(file);
//            long length = file.length ();
//            byte[] filecontent = new byte[(int)length];
//            fis.read(filecontent,0,(int)length); 
//            
//            return Response.ok().entity(new StreamingOutput(){
//                @Override
//                public void write(OutputStream output) throws IOException, WebApplicationException {
//                   output.write(filecontent);
//                   output.flush();
//                }
//                }).build();
//        }
//        else {
//            return Response.noContent ().build();
//        }
//    }
}
