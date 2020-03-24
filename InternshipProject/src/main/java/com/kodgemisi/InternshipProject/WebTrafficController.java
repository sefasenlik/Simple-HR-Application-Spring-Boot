package com.kodgemisi.InternshipProject;

import com.kodgemisi.InternshipProject.storage.StorageFileNotFoundException;
import com.kodgemisi.InternshipProject.storage.StorageService;

import java.util.Optional;
import java.util.stream.Collectors;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;

@Controller
public class WebTrafficController {
  private final StorageService storageService;

  @Autowired
  public WebTrafficController(StorageService storageService) {
    this.storageService = storageService;
  }

  private static final Logger log = LoggerFactory.getLogger(InternshipProjectApplication.class);
	
  @Autowired
  private JobListingRepository repository;
  
  @Autowired
  private JobApplicationRepository application_repository;	
  
  @GetMapping("/")
  public String home() {
	  
	return "home";
  }
  
  // PUBLIC SECTION
  // ----------------------------------------------
  // HR MANAGER SECTION
  
  @GetMapping("/hub")
  public String hub() {
	  
	return "hub";
  }
  
  //when: job creation
  @GetMapping("/joblisting")
  public String jobListingForm(Model model) {
    model.addAttribute("joblisting", new JobListing());
    
    return "create_job_listing";
  }
  
  //when: after job creation
  @PostMapping("/joblisting")
  public String jobListingSubmit(@ModelAttribute JobListing jobListing) {
	try {
	  repository.save(new JobListing(jobListing.getJobTitle(), jobListing.getJobDescription(), jobListing.getNumberOfPeople(), jobListing.getLastDate()));	
	} catch (Exception e) {
	  log.info("Error while receiving input from the user.");
	  return "error";
	}
    
	// fetch all jobListings
    log.info("Job listings found with findAll():");
    log.info("-------------------------------");
    for (JobListing jobListing2list : repository.findAll()) {
      log.info(jobListing2list.toString());
    }
    log.info("");
    
    return "view_job_listing";
  }
  
  //when: display all jobs (for manager)
  @GetMapping("/alljobs")
  public String jobList(Model model) {
    model.addAttribute("jobs", repository.findAll());
    
	return "view_all_job_listings";
  }
  
  //when: display single job (for manager)
  @RequestMapping(path = {"/joblisting/{id}"})
  public String jobListDetail(Model model, @PathVariable("id") Optional<Long> id) {
	Optional<JobListing> optionalListing = repository.findById(id.get());
    model.addAttribute("jobListing", optionalListing.get());
    
    return "view_job_listing";
  }
  
  //when: delete single job (for manager)
  @RequestMapping(path = {"/alljobs/{id}"})
  public String jobListDelete(Model model, @PathVariable("id") Optional<Long> id) {
	repository.delete(repository.findById(id.get()).get());
    model.addAttribute("jobs", repository.findAll());
    
    return "view_all_job_listings";
  }  
  
  //when: display all job applications (for manager)
  @GetMapping("/allapplications")
  public String jobApplicationList(Model model) {
    model.addAttribute("jobApplications", application_repository.findAll());
    
	return "view_all_job_applications";
  }
  
  //when: display single job application (for manager)
  @RequestMapping(path = {"/allapplications/{id}"})
  public String jobApplicationDetail(Model model, @PathVariable("id") Optional<Long> id) {
	Optional<JobApplication> optionalApplication = application_repository.findById(id.get());
    model.addAttribute("jobApplication", optionalApplication.get());
    
    return "view_job_application";
  }
  
  @GetMapping("/files/{filename:.+}")
  @ResponseBody
  public ResponseEntity<Resource> serveFile(@PathVariable String filename) {
	Resource file = storageService.loadAsResource(filename);
	
	return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"").body(file);
  }
  
  // HR MANAGER SECTION
  // ----------------------------------------------
  // USER SECTION  
  
  @GetMapping("/user-hub")
  public String user_hub() {
	  
	return "user-hub";
  }

  //when: display all jobs (for user)
  @GetMapping("/user-alljobs")
  public String userJobList(Model model) {
    model.addAttribute("jobs", repository.findAll());
    
	return "user-view_all_job_listings";
  }

  //when: display single job (for user)
  @RequestMapping(path = {"/user-joblisting/{id}"})
  public String userJobListDetail(Model model, @PathVariable("id") Optional<Long> id) {
	Optional<JobListing> optionalListing = repository.findById(id.get());
    model.addAttribute("jobListing", optionalListing.get());
    
    return "user-view_job_listing";
  }
  
  //when: job application
  @GetMapping(path = {"/apply/{id}"})
  public String userJobApplicationDetail(Model model, @PathVariable("id") Optional<Long> id) {
	Optional<JobListing> optionalListing = repository.findById(id.get());
    model.addAttribute("joblisting", optionalListing.get());
    model.addAttribute("jobapplication", new JobApplication());
    
    return "apply_job_listing";
  }
  
  
  //IMPROVEMENTS
  //YOU MAY WANT TO CHANGE FILENAME
  //when: after job application
  @PostMapping("/apply/{id}")
  public String jobListingSubmit(@ModelAttribute JobApplication jobApplication, @PathVariable("id") Optional<Long> id, @RequestParam("file") MultipartFile file) {
	try {
	  storageService.store(file);
	} catch (Exception e) {
	    log.info("Error while storing file, probably because of file name! Redirecting the user...");
	    return "error_file_save";
	}
	try {
	  application_repository.save(new JobApplication(jobApplication.getName(), jobApplication.getEMail(), jobApplication.getPhone(), jobApplication.getThoughts(), file.getOriginalFilename(), id.get().toString()));
	  jobApplication.setResume_filename(file.getOriginalFilename());
	  jobApplication.setListing_id(id.get().toString());	
	} catch (Exception e) {
	  log.info("Error while receiving input from the user.");
	  return "error";
	}
    
	// fetch all jobApplications
    log.info("Job application found with findAll():");
    log.info("-------------------------------");
    for (JobApplication jobApplication2List : application_repository.findAll()) {
      log.info(jobApplication2List.toString());
    }
    log.info("");
    
    return "user-view_job_application";
  }

}