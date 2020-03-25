
# Simple-HR-Application-Spring-Boot

*InternshipApplication* folder contains the assignment, **Simple HR Application** required for KodGemisi internship application process.\
This application is created using **Spring Boot framework**, its services and libraries, **Thymeleaf** and **Semantic UI** libraries. Also, custom **CSS** files are used for basic styling. Being a **Maven project**, all properties and dependencies can be found in "pom.xml" file.

### Login Info
You will need these credentials when you try to access **"HR Manager Hub"** section and its subsections:
- **User name:** user
- **Password:** pass

## Features

 - Clean UI
 - File upload support
 - Mostly robust design
 - Error pages for UX

## Required Improvements and Bugfixes
This is the list of the improvements spotted but could not be taken care of:
 - *JobListing* class field, *"lastDate"* is being taken as a plain text input. No formatting implemented.
 - *JobApplication* class field, *"resume_filename"* could not be modified while being received from the user. Possible file name clashes need to be resolved. *(Error page is provided when file name clash occurs)*
- Some possible exceptions are left unhandled due to time limit of the project.
- Many of the user inputs are not being checked while being received. Primitive type size overflows are highly possible.
