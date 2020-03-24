package com.kodgemisi.InternshipProject;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class JobApplication {

  @Id
  @GeneratedValue(strategy=GenerationType.AUTO)
  private Long id;
  private String name;
  private String eMail;
  private String phone;
  private String thoughts;
  private String resume_filename;
  private String listing_id;

  protected JobApplication() {}

  public JobApplication(String name, String eMail, String phone, String thoughts, String resume_filename, String listing_id) {
	this.name = name;
    this.eMail = eMail;
    this.phone = phone;
    this.thoughts = thoughts;
    this.resume_filename = resume_filename;
    this.listing_id = listing_id;
  }

  @Override
  public String toString() {
    return String.format(
        "Job Application[id=%d, name='%s', eMail='%s', phone='%s', thoughts='%s', resume_filename='%s', listing_id='%s']",
        id, name, eMail, phone, thoughts, resume_filename, listing_id);
  }

  public Long getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public String getEMail() {
    return eMail;
  }
  
  public String getPhone() {
	return phone;
  }

  public String getThoughts() {
    return thoughts;
  }
  
  public String getResume_filename() {
	    return resume_filename;
  }
  
  public String getListing_id() {
	    return listing_id;
  }
  
  public void setResume_filename(String resume_filename) {
	  	this.resume_filename = resume_filename;
  }
  
  public void setListing_id(String listing_id) {
	  	this.listing_id = listing_id;
  }
}