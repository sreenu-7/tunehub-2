package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.example.demo.entities.Song;
import com.example.demo.entities.Users;
import com.example.demo.service.SongService;
import com.example.demo.service.UsersService;
import jakarta.servlet.http.HttpSession;

@Controller
public class UsersController {
	@Autowired
	UsersService service;
	
	@Autowired
	SongService songService;
	
	@PostMapping("/register")
	public String addUsers(@ModelAttribute Users users){
		boolean userStatus=service.emailExits(users.getEmail());
		if(userStatus==false) {
			service.addUsers(users);
			System.out.println("User added");
		}else{
			System.out.println("User already exists");
		}
		return "home";
	}
	@PostMapping("/validate")
	public String validate(@RequestParam("email") String email,
			@RequestParam("password") String password,
		HttpSession session, Model model){
		if (service.validateUser(email, password) == true) {
			String role=service.getRole(email);
			session.setAttribute("email", email);
			if(role.equals("admin")) {
				return "admin";
			}else{
				Users user=service.getUser(email);
				boolean userStatus = user.isPremium();
				List<Song> songsList = songService.fetchAllSongs();
				model.addAttribute("isPremium" , userStatus);
				return "customer";
				
			}
		}
		else {
			return "login";
		}
					
	}
//	@GetMapping("/pay")
//	public String pay(@RequestParam String email) {
//		boolean paymentStatus=false;//payment api
//		if(paymentStatus==true) {
//			Users user=service.getUser(email);
//			user.setPremium(true);
//			service.updateUsers(user);
//		}
//		return "login";
//	}
	@GetMapping("/logout")
	public String logout(HttpSession session) {
		session.invalidate();
		return "login";
	}
}
