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

  //Logger for testing purposes
  private static final Logger log = LoggerFactory.getLogger(InternshipProjectApplication.class);
	
  @Autowired
  private JobListingRepository repository;
  
  @Autowired
  private JobApplicationRepository application_repository;	
  
  //Handles homepage traffic
  @GetMapping("/")
  public String home() {
	  
	return "home";
  }
  
  // PUBLIC SECTION
  // ----------------------------------------------
  // HR MANAGER SECTION (LOGIN REQUIRED)
  
  //Handles HR manager hub traffic
  @GetMapping("/hub")
  public String hub() {
	  
	return "hub";
  }
    
  //For "GET" requests on job listing creation page
  @GetMapping("/joblisting")
  public String jobListingForm(Model model) {
    model.addAttribute("joblisting", new JobListing());
    
    return "create_job_listing";
  }
  
  //For "POST" requests on job listing creation page
  //WARNING: User inputs are not checked, possible overflow exceptions are handled basically. User input control should be implemented on the next bugfix.
  @PostMapping("/joblisting")
  public String jobListingSubmit(@ModelAttribute JobListing jobListing) {
	try {
	  repository.save(new JobListing(jobListing.getJobTitle(), jobListing.getJobDescription(), jobListing.getNumberOfPeople(), jobListing.getLastDate()));	
	} catch (Exception e) {
	  log.info("Error while receiving input from the user.");
	  return "error";
	}
    
	//Fetch all job listings for testing and control purposes (can be commented out)
	//-----------
	log.info("Job listings found with findAll():");
    log.info("-------------------------------");
    for (JobListing jobListing2list : repository.findAll()) {
      log.info(jobListing2list.toString());
    }
    log.info("");
    //-----------
    
    return "view_job_listing";
  }
  
  //For "GET" requests on page displaying all jobs created
  @GetMapping("/alljobs")
  public String jobList(Model model) {
    model.addAttribute("jobs", repository.findAll());
    
	return "view_all_job_listings";
  }
  
  //For "GET" requests on detail page displaying a single job listing
  @RequestMapping(path = {"/joblisting/{id}"})
  public String jobListDetail(Model model, @PathVariable("id") Optional<Long> id) {
	Optional<JobListing> optionalListing = repository.findById(id.get());
    model.addAttribute("jobListing", optionalListing.get());
    
    return "view_job_listing";
  }
  
  //For "GET" requests on job listing specific page deleting a job listing
  //WARNING: No confirm prompt implemented so far, any request on the link below will cause job listing deletion
  @RequestMapping(path = {"/alljobs/{id}"})
  public String jobListDelete(Model model, @PathVariable("id") Optional<Long> id) {
	repository.delete(repository.findById(id.get()).get());
    model.addAttribute("jobs", repository.findAll());
    
    return "view_all_job_listings";
  }  
  
  //For "GET" requests on page displaying all job applications posted
  @GetMapping("/allapplications")
  public String jobApplicationList(Model model) {
    model.addAttribute("jobApplications", application_repository.findAll());
    
	return "view_all_job_applications";
  }

  //For "GET" requests on detail page displaying a single job application
  @RequestMapping(path = {"/allapplications/{id}"})
  public String jobApplicationDetail(Model model, @PathVariable("id") Optional<Long> id) {
	Optional<JobApplication> optionalApplication = application_repository.findById(id.get());
    model.addAttribute("jobApplication", optionalApplication.get());
    
    return "view_job_application";
  }
  
  //For file download handling
  @GetMapping("/files/{filename:.+}")
  @ResponseBody
  public ResponseEntity<Resource> serveFile(@PathVariable String filename) {
	Resource file = storageService.loadAsResource(filename);
	
	return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"").body(file);
  }
  
  // HR MANAGER SECTION (LOGIN REQUIRED)
  // ----------------------------------------------
  // USER SECTION  
  
  //Handles applicant hub traffic
  @GetMapping("/user-hub")
  public String user_hub() {
	  
	return "user-hub";
  }

  //For "GET" requests on page displaying all jobs created, modified for users that they cannot delete a listing
  @GetMapping("/user-alljobs")
  public String userJobList(Model model) {
    model.addAttribute("jobs", repository.findAll());
    
	return "user-view_all_job_listings";
  }

  //For "GET" requests on detail page displaying a single job listing, modified for users that they can apply for the job listing
  @RequestMapping(path = {"/user-joblisting/{id}"})
  public String userJobListDetail(Model model, @PathVariable("id") Optional<Long> id) {
	Optional<JobListing> optionalListing = repository.findById(id.get());
    model.addAttribute("jobListing", optionalListing.get());
    
    return "user-view_job_listing";
  }
  
  //For "GET" requests on application page containing two form fields (one for information, one for actual job application)
  @GetMapping(path = {"/apply/{id}"})
  public String userJobApplicationDetail(Model model, @PathVariable("id") Optional<Long> id) {
	Optional<JobListing> optionalListing = repository.findById(id.get());
    model.addAttribute("joblisting", optionalListing.get());
    model.addAttribute("jobapplication", new JobApplication());
    
    return "apply_job_listing";
  }
  
  //For "POST" requests on detail page displaying the job application posted, file name storing with basic exception handling is implemented
  //WARNING: Changing the file name before save is planned, but could not be implemented. Should be fixed on the next bugfix.
  //WARNING: User inputs are not checked, possible overflow exceptions are handled basically. User input control should be implemented on the next bugfix.
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
    
	// Fetch all job applications for testing and control purposes (can be commented out)
	//-----------
    log.info("Job application found with findAll():");
    log.info("-------------------------------");
    for (JobApplication jobApplication2List : application_repository.findAll()) {
      log.info(jobApplication2List.toString());
    }
    log.info("");
    //-----------
    
    return "user-view_job_application";
  }

}