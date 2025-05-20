package com.ssafy.local.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

import com.ssafy.local.dto.UserDto;
import com.ssafy.local.service.UserService;
import com.ssafy.local.service.UserServiceImpl;

import java.io.IOException;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;

@Controller
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

	private final UserService userService;

	@GetMapping("/mypage")
	private String mypage(HttpServletRequest req, HttpSession session) {

		session = req.getSession();
		UserDto sessionUser = (UserDto) session.getAttribute("loginUser");
		try {
			UserDto userFromDb = userService.selectUserByID(sessionUser.getUser_id());
			// JSP에서 EL로 사용할 수 있도록 request 영역에 담아줍니다.
			req.setAttribute("user", userFromDb);
		} catch (Exception e) {
			e.printStackTrace();
			return "error"; // 에러 페이지로 이동
		}
		// 마이페이지로 이동
		return "user/mypage";
	}

	@GetMapping("/updatepage")
	public String goUpdatePage(@SessionAttribute("loginUser") UserDto sessionUser, Model model) {
	    try {
	        UserDto userFromDb = userService.selectUserByID(sessionUser.getUser_id());
	        model.addAttribute("user", userFromDb);
	    } catch (Exception e) {
	        e.printStackTrace();
	        return "error";
	    }
	    return "user/update";
	}
	
	@GetMapping("gologinpage")
	public String goLoginPage() {
		System.out.println("dddd");
		return "/user/login";
	}
	@GetMapping("goregistpage")

	public String goRegistPage() {
		System.out.println("dddd");
		return "/user/signup";
	}
	
	
	@PostMapping("/update")
	private String update(@SessionAttribute("loginUser") UserDto sessionUser, @RequestParam String user_id,
			@RequestParam String name, @RequestParam String phone, @RequestParam String email,
			@RequestParam(required = false) String pw, Model model, HttpSession session, HttpServletRequest req) {
		System.out.println("사용자이름:    "+user_id);
		String passwordToUse = (pw == null || pw.trim().isEmpty()) ? sessionUser.getPw() : pw;
		session = req.getSession();
		UserDto userSession = (UserDto) session.getAttribute("user");
		
		model.addAttribute("user", userSession); // 실패 시 값 유지
		
		
		UserDto updatedUser = new UserDto(user_id, passwordToUse, email, name, phone, null);
		
		
		try {
			if (userService.isEmailExists(email) && !email.equals(sessionUser.getEmail())) {
				model.addAttribute("error", "이미 사용중인 이메일입니다.");
				return "user/updatepage";
			}

			int result = userService.updateUser(updatedUser);
			if (result > 0) {
				session.setAttribute("loginUser", updatedUser);
				System.out.println("성공");
				model.addAttribute("success", "회원 정보가 성공적으로 수정되었습니다.");
			} else {
				System.out.println("실패");
				model.addAttribute("error", "회원 정보 수정에 실패했습니다.");
			}

		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("error", "오류가 발생했습니다. 관리자에게 문의하세요.");
		}

		return "redirect:/user/mypage";
	}

	@PostMapping("/delete")
	public String delete(@RequestParam String user_id, HttpSession session) {
		try {
			int result = userService.deleteUser(user_id);
			System.out.println(result);
			if (result > 0) {
				session.invalidate();
				return "redirect:/index"; // suffix 설정에 따라 .jsp 자동 추가됨
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return "redirect:/homes/error";
	}

	@PostMapping("/login")
    private String login(@RequestParam String user_id, @RequestParam String pw, HttpSession session,
    		Model model) {
        UserDto user = userService.login(user_id, pw);
        if (user != null) {
            session.setAttribute("loginUser", user);
            return "redirect:/index";
        } else {
            model.addAttribute("error", "아이디 또는 비밀번호가 틀렸습니다.");
            return "user/login";
        }
    }

	@GetMapping("/logout")
    public String logout(HttpSession session) {
		System.out.println("컨트롤러 로그아웃");
    	session.invalidate();
    	return"redirect:/index";
    }

	@PostMapping("/signup")
	public String signup(@ModelAttribute UserDto userDto, Model model, HttpSession session) {
		System.out.println("컨트롤러 회원가입");
	    try {
	        if (userService.isUserIdExists(userDto.getUser_id())) {
	            model.addAttribute("error", "이미 사용중인 아이디입니다.");
	            model.addAttribute("formData", userDto);
	            return "user/signup";
	        }

	        if (userService.isEmailExists(userDto.getEmail())) {
	            model.addAttribute("error", "이미 사용중인 이메일입니다.");
	            model.addAttribute("formData", userDto);
	            return "user/signup";
	        }

	        userService.insertUser(userDto);
	        session.setAttribute("signupSuccess", "회원가입이 완료되었습니다.");
	        return "redirect:/user/gologinpage";

	    } catch (Exception e) {
	        e.printStackTrace();
	        return "error";
	    }
	}
	
	@PostMapping("/findpw")
	public String findPassword(@RequestParam String email,Model model) {
		System.out.println("비밀번호찾기 컨트롤러");
        try {
            UserDto user = userService.selectUserByEmail(email);
            if (user == null) {
                model.addAttribute("message", "존재하지 않는 이메일입니다.");
                model.addAttribute("messageType", "danger");
                return "user/findpassword";
            }
            model.addAttribute("email", email);
            return "/user/resetpassword";
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error", "서버 오류가 발생했습니다.");
            return "error";
        }
		
	}
	
	@GetMapping("/findpasswordform")
	public String goFindPassword() {
		return "/user/findpassword";
		
	}
	
	
	
	
	
	  @PostMapping("/resetpw")
	    public String resetPassword(
	            @RequestParam String email,
	            @RequestParam String code,
	            @RequestParam String newPw,
	            @RequestParam String confirmPw,
	            Model model
	    ) {
	        if (code == null || code.trim().isEmpty()) {
	            model.addAttribute("message", "인증번호를 입력해주세요.");
	            model.addAttribute("messageType", "danger");
	            model.addAttribute("email", email);
	            return "user/resetpassword";
	        }

	        if (!newPw.equals(confirmPw)) {
	            model.addAttribute("message", "비밀번호가 일치하지 않습니다.");
	            model.addAttribute("messageType", "danger");
	            model.addAttribute("email", email);
	            return "user/resetpassword";
	        }

	        try {
	            UserDto user = userService.selectUserByEmail(email);
	            if (user == null) {
	                model.addAttribute("message", "사용자를 찾을 수 없습니다.");
	                model.addAttribute("messageType", "danger");
	                return "user/resetpassword";
	            }

	            user.setPw(newPw);
	            userService.updateUser(user);

	            model.addAttribute("signupSuccess", "비밀번호가 성공적으로 변경되었습니다.");
	            return "redirect:/user/gologinpage";

	        } catch (Exception e) {
	            e.printStackTrace();
	            model.addAttribute("message", "비밀번호 변경 중 오류가 발생했습니다.");
	            model.addAttribute("messageType", "danger");
	            return "user/resetpassword";
	        }
	    }
	
	
}
