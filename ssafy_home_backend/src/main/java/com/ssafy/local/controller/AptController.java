package com.ssafy.local.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssafy.local.dto.HouseDealDto;
import com.ssafy.local.dto.HouseInfoDto;
import com.ssafy.local.service.HouseDealService;
import com.ssafy.local.service.HouseInfoService;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/home")
@RequiredArgsConstructor
public class AptController  extends HttpServlet {

    private final HouseDealService dealService; 
    private final HouseInfoService infoService;


    @GetMapping("/search")
    private String goSearchpage() {
    	return "home/dealmap";
    }
    
    @GetMapping("/deal")
    private String deal(@RequestParam String aptSeq, HttpServletResponse resp) throws ServletException, IOException {
        
        List<HouseDealDto> deals = null;
		try {
			deals = dealService.selectHouseDealbyAptSeq(aptSeq);
		} catch (Exception e) {
			e.printStackTrace();
		}
        resp.setContentType("application/json;charset=utf-8");
        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(resp.getWriter(), deals);
        return null; 
    }
    @GetMapping("/search/apt")
    private String searchByAptName(@RequestParam String aptName, Model model){
		List<HouseInfoDto> ls = null;
		try {
			ls = infoService.selectHouseInfoByAptName(aptName);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		model.addAttribute("aptList", ls);
		return "home/dealmap";
	}
    @GetMapping("/search/region")
    private String searchByRegion(@RequestParam String dong, Model model){
		List<HouseInfoDto> ls = null;
		try {
			ls = infoService.selectHouseInfobyDongCode(dong);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		model.addAttribute("aptList", ls);
		return "home/dealmap";
	}
} 
