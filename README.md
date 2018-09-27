# evnt
Event management system

A Java application which allows users to create and invite other users to events. It is meant as a privacy-conscious alternative to Facebook's event system. It currently has the following features:

- User Logins (and an account registration page)
- A main view which shows all future events the user is invited to
- The ability to create a new event, including setting a cover image and a rich text description
- The ability to invite users, make them co-hosts, and uninvite users from events
- The ability to RSVP to an event from the read-only event view
- Emails are queued up to be sent to notify users about events.
- Those emails use freemarker templates to substitute text and generate the body of the emails
- I am currently using Google's smtp server with my personal email account set as the sender

Technologies used:
- Java 8
- Spring Boot (RESTful APIs)
- Spring Security (for login authorization)
- Gradle (for building)
- Vaadin 8 (for the UI)
- MyBatis (for SQL access)
- MySQL
- JavaMailSender

There are three services which comprise the application:
- evnt-webapp: The front end
- evnt-api: The back end, linked to the MySQL database
- mail-api: A back end specifically for sending emails using the JavaMailSender library
