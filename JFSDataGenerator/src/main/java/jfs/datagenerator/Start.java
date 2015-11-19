package jfs.datagenerator;

import com.google.gson.Gson;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.sun.istack.internal.logging.Logger;
import jfs.transferdata.transferobjects.*;
import jfs.transferdata.transferobjects.enums.JobTypeDTO;
import jfs.datagenerator.transferobjects.GitHubJobDTO;

import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Random;
import java.util.logging.Level;

/**
 * Created by lpuddu on 13-11-2015.
 */
public class Start {
    private String githubApiUrl = "https://jobs.github.com/positions.json?description=&location=&page=";

    private String apiURL = "http://localhost:8080/service";
    private String addCompanyPATH = "/users/register/company";
    private String addOffersPATH = "/offers/addmore";
    private String addOfferPATH = "/offers/add";
    private String loginPATH = "/users/login";
    private String password = "root";

    private Random random = new Random();
    private long monthInMillis = 2592000000l;

    private HashMap<String, String> tokens = new HashMap<String, String>();

    public Start(){
        for(int i = 0; i < 6; i++){
            GitHubJobDTO[] jobs = this.doGithubCall(i);
            for(GitHubJobDTO job : jobs){
                job.company = job.company.toLowerCase().replace(" ", ".") + "@jfs.com";
                String token = this.tokens.get(job.company);
                if(token == null || token == ""){
                    this.createCompany(job.company);
                    token = this.loginCompany(job.company);
                    this.tokens.put(job.company , token);
                }
                CreateJobOfferDTO createOffer = new CreateJobOfferDTO();
                createOffer.companyId = job.company;
                createOffer.jobOffer = this.createJobOfferDTO(job);
                createOffer.token = token;
                this.addOffer(createOffer);
            }
        }
    }

    private long getDuration(int maxMonths){
        return (this.random.nextInt(maxMonths - 1) + 1) * this.monthInMillis;
    }

    private JobOfferDTO createJobOfferDTO(GitHubJobDTO githubJob){
        JobOfferDTO offer = new JobOfferDTO(
                "", githubJob.company, githubJob.title, githubJob.title, githubJob.description, githubJob.how_to_apply,
                this.getDuration(12), new GregorianCalendar().getTimeInMillis() + this.getDuration(4),
                new GregorianCalendar().getTimeInMillis() + this.getDuration(6), githubJob.location,
                githubJob.company_url, this.getType(githubJob.type)
        );
        return offer;
    }

    private JobTypeDTO getType(String type){
        switch(type){
            case "Full Time":
                return JobTypeDTO.full_time;
            case "Part Time":
                return JobTypeDTO.part_time;
            case "Contract":
                return JobTypeDTO.contract;
            default:
                return JobTypeDTO.bachelor_thesis;
        }
    }

    private void createCompany(String email){
        RegisterDTO reg = new RegisterDTO();
        reg.email = email;
        reg.password = this.password;

        Boolean regResult = this.doJFSCall(this.addCompanyPATH, reg, Boolean.class);
    }

    private void addOffer(CreateJobOfferDTO offerDTO){
        Boolean result = this.doJFSCall(this.addOfferPATH, offerDTO, Boolean.class);

    }

    private void addOffers(CreateJobOffersDTO offersDTO){
        Boolean result = this.doJFSCall(this.addOffersPATH, offersDTO, Boolean.class);
    }

    private String loginCompany(String email){
        AuthenticationDTO auth = new AuthenticationDTO();
        auth.email = email;
        auth.password = this.password;

        LoginResultDTO resultDTO = this.doJFSCall(this.loginPATH, auth, LoginResultDTO.class);
        return resultDTO.token;
    }

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
            Logger.getLogger(Start.class).log(Level.SEVERE, ex.getMessage(), ex);
        }
        return result;
    }

    private GitHubJobDTO[] doGithubCall(int page){
        GitHubJobDTO[] result = null;
        try {
            String json = Unirest.get(this.githubApiUrl + page)
                    .header("accept", "application/json")
                    .asString().getBody();
            result = new Gson().fromJson(json, GitHubJobDTO[].class);
        }
        catch(UnirestException ex){
            Logger.getLogger(Start.class).log(Level.SEVERE, ex.getMessage(), ex);
        }
        return result;
    }

    public static void main(String[] args){
        new Start();

    }
}
