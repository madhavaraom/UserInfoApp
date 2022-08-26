package org.test;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserInfoController {
	
	private String fileName = "user-data.txt";

	@GetMapping("/message")
	public String message() {
		return "Welcome";
	}

	@PostMapping("/add-user")
	public void addUser(@RequestBody User user) {
		FileWriter fw;
		try {
			fw = new FileWriter(fileName, true);
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(user.getFirstName());
			bw.newLine();
			bw.write(user.getLastName());
			bw.newLine();
			bw.write(user.getContactNumber());
			bw.newLine();
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@GetMapping("/get-user")
	public User getUser() {
		User user = new User();
		List<String> lines = Collections.emptyList();
		try {
			lines = Files.readAllLines(Paths.get(fileName), StandardCharsets.UTF_8);
		} catch (IOException e) {
			e.printStackTrace();
		}
		user.setFirstName(lines.get(0));
		user.setLastName(lines.get(1));
		user.setContactNumber(lines.get(2));
		return user;
	}
	
	@DeleteMapping("/remove-user")
	public String deletUser() {
		File file = new File(fileName);
		if (file.delete()) {
			return "User deleted successfully";
		} else {
			return "Unable to delete the user data";
		}
	}
	
	@PutMapping("/update-user")
	public String updateUser(@RequestBody User user) {
		File fileToBeModified = new File(fileName);
		try {
			BufferedReader reader = new BufferedReader(new FileReader(fileToBeModified));
			String line = reader.readLine();
			FileWriter writer = new FileWriter(fileToBeModified);
            while (line != null) 
            {
            	if(!(user.getFirstName().isEmpty()) && !(line.equalsIgnoreCase(user.getFirstName()))) {
            		writer.write(line.replaceAll(line, user.getFirstName()));
            	}
            	if(!(user.getLastName().isEmpty()) && !(line.equalsIgnoreCase(user.getLastName()))) {
            		writer.write(line.replaceAll(line, user.getLastName()));
            	}
            	if(!(user.getContactNumber().isEmpty()) && !(line.equalsIgnoreCase(user.getContactNumber()))){
            		writer.write(line.replaceAll(line, user.getContactNumber()));
            	}
                line = reader.readLine();
            }
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "Updated successfully!";
	}
}
