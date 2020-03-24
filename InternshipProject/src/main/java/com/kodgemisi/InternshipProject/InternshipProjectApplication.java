package com.kodgemisi.InternshipProject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.kodgemisi.InternshipProject.storage.StorageProperties;
import com.kodgemisi.InternshipProject.storage.StorageService;

@SpringBootApplication
public class InternshipProjectApplication {

  private static final Logger log = LoggerFactory.getLogger(InternshipProjectApplication.class);

  public static void main(String[] args) {
    SpringApplication.run(InternshipProjectApplication.class);
  }

  @Bean
  public CommandLineRunner demo(StorageService storageService,
		  						JobListingRepository repository, JobApplicationRepository application_repository) {
    return (args) -> {
      // set up storage service
	  storageService.deleteAll();
	  storageService.init();
		
      // save a few jobListings
      repository.save(new JobListing("Summer Intern", "Search and apply for Summer Internship 2020 and start your career today.", 10, "31/03/2020"));
      repository.save(new JobListing("HR Manager", "Human resource management is the strategic approach to the effective management of people.", 1, "30/06/2020"));
      repository.save(new JobListing("Graphic Designer", "A graphic designer creates the graphics primarily for published, printed or electronic media.", 5, "15/04/2020"));
      
      /*
       * FOR TEST PURPOSES ONLY - process of saving file to resume file repository is MISSING
      // save a few jobApplications
      application_repository.save(new JobApplication("Sefa Senlik", "senliksefa@gmail.com", "+90 535 654 8572", "I need to find an internship", "Cover Letter.pdf", "1"));
      application_repository.save(new JobApplication("Mahmut Tuncer", "tuncermahmut@gmail.com", "+90 555 765 4321", "I need to find a job", "CV - Spring 2019 (OLD).pdf", "2"));
      application_repository.save(new JobApplication("Vladimir Putin", "putinvladimir@yandex.ru", "+7 952 279 2838", "Do you have open CEO positions?", "CV (OLD).pdf", "2"));
      application_repository.save(new JobApplication("Pablo Picasso", "picassopablo@gmail.com", "+39 888 777 6655", "I love to design", "me.png", "3"));
      */
            

      log.info("");
      // fetch all jobListings
      log.info("Job listings found with findAll():");
      log.info("-------------------------------");
      for (JobListing jobListing : repository.findAll()) {
        log.info(jobListing.toString());
      }
      log.info("-------------------------------");

      // fetch all jobApplications
      log.info("Job application found with findAll():");
      log.info("-------------------------------");
      for (JobApplication jobApplication : application_repository.findAll()) {
        log.info(jobApplication.toString());
      }
      log.info("-------------------------------");
      
      /*
       * FOR TEST PURPOSES ONLY 
      // fetch an individual jobListing by ID
      JobListing jobListing  = repository.findById(1L);
      log.info("Job listing found with findById(1L):");
      log.info("--------------------------------");
      log.info(jobListing.toString());
      log.info("");

      // fetch jobListings by last name
      log.info("Job listing found with findByJobTitle('HR Manager'):");
      log.info("--------------------------------------------");
      repository.findByJobTitle("HR Manager").forEach(hr -> {
        log.info(hr.toString());
      });
      // for (JobListing hr : repository.findByJobTitle('HR Manager')) {
      //  log.info(hr.toString());
      // }
      */
      
      log.info("");
    };
  }

}