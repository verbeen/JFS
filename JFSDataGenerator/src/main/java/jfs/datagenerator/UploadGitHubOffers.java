package jfs.datagenerator;

import com.google.gson.Gson;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import jfs.datagenerator.connections.GitHubConnection;
import jfs.datagenerator.transferobjects.GitHubJobDTO;
import jfs.transferdata.transferobjects.*;

import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by lpuddu on 13-11-2015.
 *
 * Class that is used to upload job offers from Git Hub directly into JFS Backend server
 * Allows a selection of server that is used
 *
 */
public class UploadGitHubOffers {
    //Pick if localhost, dev-serve or master-server will be used:
    //private String apiURL = "http://localhost:8080/service";
    private String apiURL = "http://server-devforstudents.rhcloud.com/service";
    //private String apiURL = "http://server-jobsforstudents.rhcloud.com/service";
    private String addCompanyPATH = "/users/register/company";
    private String addOffersPATH = "/offers/addmore";
    private String addOfferPATH = "/offers/add";
    private String loginPATH = "/users/login";
    private String password = "root";

    //Connection used for access to Git Hub job offers
    private GitHubConnection connection = new GitHubConnection();

    private HashMap<String, String> tokens = new HashMap<String, String>();

    //Constructor to upload jobs from Git Hub directly to the provided server
    public UploadGitHubOffers(){
        for(int i = 0; i < 6; i++){
            GitHubJobDTO[] jobs = this.connection.doGithubCall(i);
            for(GitHubJobDTO job : jobs){
                job.company = job.company.toLowerCase().replace(" ", ".") + "@jfs.com";
                String token = this.tokens.get(job.company);
                if(token == null || token == ""){
                    this.createCompany(job.company);
                    token = this.loginCompany(job.company);
                    this.tokens.put(job.company , token);
                }
                CreateJobOfferDTO createOffer = this.connection.toJobOfferDTO(job, token);
                this.addOffer(createOffer);
            }
        }
    }

    //Create a company by email
    private void createCompany(String email){
        RegisterDTO reg = new RegisterDTO();
        reg.email = email;
        reg.password = this.password;

        Boolean regResult = this.doJFSCall(this.addCompanyPATH, reg, Boolean.class);
    }

    //Add a job offer as CreateJobOfferDTO
    private void addOffer(CreateJobOfferDTO offerDTO){
        ActionResultDTO result = this.doJFSCall(this.addOfferPATH, offerDTO, ActionResultDTO.class);

    }

    //Add several job offers as CreateJobOffersDTO
    private void addOffers(CreateJobOffersDTO offersDTO){
        LoginResultDTO result = this.doJFSCall(this.addOffersPATH, offersDTO, LoginResultDTO.class);
    }

    //Login as a company by email
    //Needed because job offer creation is only possible as a company
    private String loginCompany(String email){
        AuthenticationDTO auth = new AuthenticationDTO();
        auth.email = email;
        auth.password = this.password;

        LoginResultDTO resultDTO = this.doJFSCall(this.loginPATH, auth, LoginResultDTO.class);
        return resultDTO.token;
    }

    //doJFSCall will execute the calls on the actual server that is provided
    //this function is used by all other functions as a Wrapper
    private <T> T doJFSCall(String path, Object obj, Class<T> returnType){
        T result = null;
        try {
            String json = Unirest.post(this.apiURL + path)
                    .header("Content-Type", "application/json")
                    .header("accept", "application/json")
                    .body(new Gson().toJson(obj))
                    .asString().getBody();
            result = new Gson().fromJson(json, returnType);
        }
        catch(UnirestException ex){
            Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).log(Level.SEVERE, ex.getMessage(), ex);
        }
        return result;
    }

    //Main method will create UploadGitHubOffers
    public static void main(String[] args){
        new UploadGitHubOffers();
    }
}