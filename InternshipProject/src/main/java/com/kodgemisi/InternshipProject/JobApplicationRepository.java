package com.kodgemisi.InternshipProject;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

public interface JobApplicationRepository extends CrudRepository<JobApplication, Long> {

  List<JobApplication> findByName(String name);

  JobApplication findById(long id);
}