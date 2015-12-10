package jfs.datagenerator.connections;

import com.google.gson.Gson;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import jfs.datagenerator.transferobjects.GitHubJobDTO;
import jfs.transferdata.transferobjects.CreateJobOfferDTO;
import jfs.transferdata.transferobjects.JobOfferDTO;
import jfs.transferdata.transferobjects.enums.JobTypeDTO;

import java.util.GregorianCalendar;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by lpuddu on 7-12-2015.
 */
public class GitHubConnection {
    private String githubApiUrl = "https://jobs.github.com/positions.json?description=&location=&page=";
    private Random random = new Random();
    private long monthInMillis = 2592000000l;


    public CreateJobOfferDTO toJobOfferDTO(GitHubJobDTO gitHubJob, String token){
        CreateJobOfferDTO createOffer = new CreateJobOfferDTO();
        createOffer.companyId = gitHubJob.company;
        createOffer.jobOffer = this.createJobOfferDTO(gitHubJob);
        createOffer.token = token;
        return createOffer;
    }

    public GitHubJobDTO[] doGithubCall(int page){
        GitHubJobDTO[] result = null;
        try {
            String json = Unirest.get(this.githubApiUrl + page)
                    .header("accept", "application/json")
                    .asString().getBody();
            result = new Gson().fromJson(json, GitHubJobDTO[].class);
        }
        catch(UnirestException ex){
            Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).log(Level.SEVERE, ex.getMessage(), ex);
        }
        return result;
    }

    public JobOfferDTO createJobOfferDTO(GitHubJobDTO githubJob){
        JobOfferDTO offer = new JobOfferDTO(
                "", githubJob.company, githubJob.company, githubJob.title, githubJob.title, githubJob.description, githubJob.how_to_apply,
                "C#, Java, Mobile", this.getDuration(12), new GregorianCalendar().getTimeInMillis() + this.getDuration(4),
                new GregorianCalendar().getTimeInMillis() + this.getDuration(6), githubJob.location,
                githubJob.company_url, this.getType(githubJob.type)
        );
        return offer;
    }

    private long getDuration(int maxMonths){
        return (this.random.nextInt(maxMonths - 1) + 1) * this.monthInMillis;
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

}