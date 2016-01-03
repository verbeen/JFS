package jfs.service.services;

import com.google.gson.Gson;
import io.swagger.annotations.*;
import jfs.data.dataobjects.UserDO;
import jfs.data.dataobjects.enums.UserType;
import jfs.service.sessions.Session;
import jfs.transferdata.transferobjects.*;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import java.util.List;

/**
 * Created by zade on 26-10-2015.
 *
 * Wrapper for UserService. Accessible through @Path
 *
 */
@Path("/users")
@Api(value = "/users")
public class UserWebService {
    @Inject
    UserService service;

    @POST
    @Path("/register/company")
    @ApiOperation(value = "Register company",
        notes = "A user account for company will be created.")
    @Consumes("application/json")
    @Produces("application/json")
    public Boolean registerCompany(RegisterDTO register) {
        Boolean result = false;
        result = this.service.registerUser(register.email, register.password, UserType.COMPANY);
        return result;
    }

    @POST
    @Path("/register/student")
    @ApiOperation(value = "Register student",
        notes = "A user account for student will be created.")
    @Consumes("application/json")
    @Produces("application/json")
    public Boolean registerStudent(RegisterDTO register){
        Boolean result = false;
        result = this.service.registerUser(register.email, register.password, UserType.STUDENT);
        return result;
    }

    @POST
    @Path("/register/admin")
    @ApiOperation(value = "Register admin",
        notes = "A user account for admin will be created.")
    @Consumes("application/json")
    @Produces("application/json")
    public Boolean registerAdmin(RegisterDTO register){
        Boolean result = false;
        result = this.service.registerUser(register.email, register.password, UserType.ADMIN);
        return result;
    }

    @POST
    @Path("/login")
    @ApiOperation(value = "Login",
        notes = "Login with email and password.")
    @Consumes("application/json")
    @Produces("application/json")
    public LoginResultDTO login(AuthenticationDTO login){
        LoginResultDTO result = this.service.loginUser(login.email, login.password);
        return result;
    }

    @POST
    @Path("/all")
    @ApiOperation(value = "Get all user accounts",
        notes = "Returns an array of all registered user accounts.")
    @Consumes("application/json")
    @Produces("application/json")
    public List<UserDTO> getAll(String token){
        if(token != null && token != "") {
            Session session = SessionService.sessions.get(token);
            if (session != null) {
                List<UserDTO> result = this.service.getAllUsers();
                return result;
            }
        }
        return null;
    }
}