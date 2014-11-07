package org.benp.notify;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributeView;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.Seconds;


import java.util.*;
import javax.mail.*;
import javax.mail.internet.*;
import javax.activation.*;


/**
 * This is a simple program to check the last time a file was updated and if it was later
 * than given time then send email.
 * 
 * @author Ben Pritzker
 *
 */
public class NotifyOnNoUpdate {
	
	private static final Logger log = LogManager.getLogger("NotifyOnNoUpdate");

	private String emailToNotify;
	private File fileToCheck;
	private int secondsIfNoUpdateToNotify;
	
	public static void main(String[] args) {
		System.out.println("Running...");
		NotifyOnNoUpdate notifyOnNoUpdate = new NotifyOnNoUpdate();
		notifyOnNoUpdate.run();
	}

	public void run() {
		init();
		start();
	}
	
	private void start() {
		checkFile();
	}
	
	private void checkFile() {
		boolean sendAlert = isLastUpdatedAfterThreshold();
		log.info("sendAlert: " + sendAlert);
//		if (sendAlert) {
//			sendAlert();
//		}
	}

	private void sendAlert() {
	      // Recipient's email ID needs to be mentioned.
	      String to = emailToNotify;

	      // Sender's email ID needs to be mentioned
	      String from = "alert@ps.everbridge.net";

	      // Assuming you are sending email from localhost
	      String host = "localhost";

	      // Get system properties
	      Properties properties = System.getProperties();

	      // Setup mail server
	      properties.setProperty("166.78.116.41", host);

	      // Get the default Session object.
	      Session session = Session.getDefaultInstance(properties);

	      try{
	         // Create a default MimeMessage object.
	         MimeMessage message = new MimeMessage(session);

	         // Set From: header field of the header.
	         message.setFrom(new InternetAddress(from));

	         // Set To: header field of the header.
	         message.addRecipient(Message.RecipientType.TO,
	                                  new InternetAddress(to));

	         // Set Subject: header field
	         message.setSubject("!ALERT - LOG FILE HAS NOT BEEN UPDATED!!");

	         // Now set the actual message
	         message.setText("The log file on the produciton server has not been updated! Please check on it.");

	         // Send message
	         Transport.send(message);
	         System.out.println("Sent message successfully....");
	      }catch (MessagingException mex) {
	         mex.printStackTrace();
	      }
	}

	private void init() {
		// TODO: Load from config
		emailToNotify = "ben.pritzker@everbridge.com";
		fileToCheck = new File("C:/Temp/JunkFile.txt");
		secondsIfNoUpdateToNotify = 30;
		
		log.info("PARAM::secondsIfNoUpdateToNotify: " + secondsIfNoUpdateToNotify);
	}
	
	
	
	
	
	
	private boolean isLastUpdatedAfterThreshold() {
		Path path = Paths.get(fileToCheck.getAbsolutePath());
		BasicFileAttributes view = getView(path);
		FileTime lastModifiedFileTime = view.lastModifiedTime();
		Date lastModifiedDate = new Date(lastModifiedFileTime.toMillis());
		Date currentDate = new Date();
		
		int secondsFromNow = Seconds.secondsBetween(
				new DateTime(lastModifiedDate), 
				new DateTime(currentDate)).getSeconds(); 
		
		DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
		log.info(fileToCheck.getName() + " --  " + df.format(lastModifiedDate) + "  -- Seconds: " + secondsFromNow);
		if (secondsFromNow >= secondsIfNoUpdateToNotify) {
			return true;
		} else {
			return false;
		}
	}	
	
	private BasicFileAttributes getView(Path path) {
		BasicFileAttributes view = null;
		try {
			view
			   = Files.getFileAttributeView(path, BasicFileAttributeView.class)
			          .readAttributes();
		} catch (IOException ioe) {
			throw new RuntimeException("Could not get view!", ioe);
		}
		return view;
	}
	
	
	

}
