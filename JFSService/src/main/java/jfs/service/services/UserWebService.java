package jfs.service.services;

import com.google.gson.Gson;
import jfs.service.transferobjects.LoginDTO;
import jfs.service.transferobjects.RegisterDTO;
//import org.apache.cxf.rs.security.cors.CrossOriginResourceSharing;

import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;
import javax.ws.rs.*;

/**
 * Created by zade on 26-10-2015.
 */
@Path("/users")
public class UserWebService
{
    @Inject
    UserService service;

    //@CrossOriginResourceSharing(allowOrigins = { "*" })
    @POST @Path("/register") @Consumes("application/json") @Produces("application/json")
    public String register(RegisterDTO register){
        Boolean result = this.service.registerStudent(register.email, register.password);
        return new Gson().toJson(result);
    }

    //@CrossOriginResourceSharing(allowOrigins = { "*" })
    @POST @Path("/login") @Consumes("application/json") @Produces("application/json")
    public String login(LoginDTO login){
        return new Gson().toJson(this.service.loginUser(login.email, login.password));
    }
}
