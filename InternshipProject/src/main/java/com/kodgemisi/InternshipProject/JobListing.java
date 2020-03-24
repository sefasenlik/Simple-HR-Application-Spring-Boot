package com.kodgemisi.InternshipProject;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class JobListing {

  @Id
  @GeneratedValue(strategy=GenerationType.AUTO)
  private Long id;
  private String jobTitle;
  private String jobDescription;
  private int numberOfPeople;
  private String lastDate;

  protected JobListing() {}

  public JobListing(String jobTitle, String jobDescription, int numberOfPeople, String lastDate) {
	this.jobTitle = jobTitle;
    this.jobDescription = jobDescription;
    this.numberOfPeople = numberOfPeople;
    this.lastDate = lastDate;
  }

  @Override
  public String toString() {
    return String.format(
        "Job Listing[id=%d, jobTitle='%s', jobDescription='%s', numberOfPeople='%d', lastDate='%s']",
        id, jobTitle, jobDescription, numberOfPeople, lastDate);
  }

  public Long getId() {
    return id;
  }

  public String getJobTitle() {
    return jobTitle;
  }

  public String getJobDescription() {
    return jobDescription;
  }
  
  public int getNumberOfPeople() {
	return numberOfPeople;
  }

  public String getLastDate() {
    return lastDate;
  }
}