package com.kodgemisi.InternshipProject;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

public interface JobListingRepository extends CrudRepository<JobListing, Long> {

  List<JobListing> findByJobTitle(String jobTitle);

  JobListing findById(long id);
}