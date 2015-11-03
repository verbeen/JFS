package jfs.service.services;

import com.google.gson.Gson;
import jfs.service.transferobjects.LoginDTO;
import jfs.service.transferobjects.RegisterDTO;

import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;
import javax.ws.rs.*;

/**
 * Created by zade on 26-10-2015.
 */
@Path("/users") @Startup @Singleton
public class UserWebService
{
    @Inject
    UserService service;

    @POST @Path("/register") @Consumes("application/json")
    public String register(RegisterDTO register){
        this.service.registerStudent(register.email, register.password);
        return "ok";
    }

    @POST @Path("/login") @Consumes("application/json")
    public String login(LoginDTO login){
        return new Gson().toJson(this.service.loginUser(login.id, login.password));
    }
}
