package jfs.service.services;

import com.google.gson.Gson;

import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;

/**
 * Created by zade on 26-10-2015.
 */
@Path("/users") @Startup @Singleton
public class UserWebService
{
    @Inject
    JFSService service;

    @GET @Path("/register")
    public String register(@QueryParam("email") String email, @QueryParam("password") String password, @QueryParam("name") String name){
        this.service.RegisterUser(email, password, name);
        return "ok";
    }

    @GET @Path("/login")
    public String login(@QueryParam("email") String email, @QueryParam("password") String password){
        return new Gson().toJson(this.service.loginUser(email, password));
    }
}
