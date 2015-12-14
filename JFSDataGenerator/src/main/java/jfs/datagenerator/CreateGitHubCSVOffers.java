package jfs.datagenerator;

import com.fasterxml.jackson.databind.SequenceWriter;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import jfs.datagenerator.connections.GitHubConnection;
import jfs.datagenerator.gui.GitHubDirectoryChooser;
import jfs.datagenerator.transferobjects.GitHubJobDTO;
import jfs.transferdata.transferobjects.JobOfferDTO;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lpuddu on 7-12-2015.
 */
public class CreateGitHubCSVOffers {
    private GitHubConnection connection = new GitHubConnection();

    public CreateGitHubCSVOffers(String directory) throws IOException {
        CsvMapper mapper = new CsvMapper();
        CsvSchema schema = mapper.schemaFor(JobOfferDTO.class).withHeader().withLineSeparator("\r\n");

        for(int i = 0; i < 6; i++) {
            GitHubJobDTO[] jobs = this.connection.doGithubCall(i);
            List<JobOfferDTO> jobOffers = new ArrayList<>();
            for(GitHubJobDTO job : jobs){
                job.company = "";
                JobOfferDTO jobOffer = this.connection.createJobOfferDTO(job);
                jobOffers.add(jobOffer);
            }

            SequenceWriter writer = mapper.writer(schema).writeValues(new File(directory + "\\CSVOffers-p" + i + ".csv"));
            writer.writeAll(jobOffers);
            writer.close();
        }
    }

    public static void main(String[] args){
        String directory = new GitHubDirectoryChooser().showDialog();
        try{
            new CreateGitHubCSVOffers(directory);
            System.exit(0);
        }
        catch(IOException ex){
            ex.printStackTrace();
        }
    }
}
