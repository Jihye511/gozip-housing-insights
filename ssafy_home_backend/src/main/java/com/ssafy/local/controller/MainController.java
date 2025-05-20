package com.ssafy.local.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ssafy.local.dto.UserDto;
import com.ssafy.local.service.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping({"/","index"})
@RequiredArgsConstructor
public class MainController {
	@GetMapping({"/","/index"})
	public String index() {
		return "index";
	}
}
