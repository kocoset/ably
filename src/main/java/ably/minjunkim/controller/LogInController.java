package ably.minjunkim.controller;

import ably.minjunkim.domain.Member;
import ably.minjunkim.service.LoginService;
import ably.minjunkim.session.SessionConst;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;
import java.util.regex.Pattern;

@Controller
@RequiredArgsConstructor
@Slf4j
public class LogInController {
    private final LoginService loginService;

    @GetMapping("/") // 홈메인
    public String homeLogin(@SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) Member loginMember, Model model) {
        //세션에 회원 데이터가 없으면 home
        if (loginMember == null) {
            return "home";
        }
        //세션이 유지되면 로그인으로 이동
        model.addAttribute("member", loginMember);
        return "loginHome";
    }

    @GetMapping("/login") // 로그인
    public String loginForm(@SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) Member loginMember, @ModelAttribute Member member, Model model) {
        // 세션이 이미 있으면 로그인 상태 화면 이동
        if (loginMember != null) {
            model.addAttribute("member", loginMember);
            return "loginHome";
        }

        return "login/loginForm";
    }

    @PostMapping("/login") // 로그인
    public String login(@Valid @ModelAttribute Member member, BindingResult bindingResult,
                          @RequestParam(defaultValue = "/") String redirectURL,
                          HttpServletRequest request) {

        // 로그인 정보 에러 시 로그인폼 화면 이동
        if (bindingResult.hasErrors()) {
            return "login/loginForm";
        }
        //회원 조회
        List<Member> memberList = loginService.login(member);

        if (memberList.isEmpty()) {
            bindingResult.reject("loginFail", "아이디 또는 비밀번호가 맞지 않습니다.");
            return "login/loginForm";
        }

        //로그인 성공 처리
        //세션이 있으면 있는 세션 반환, 없으면 신규 세션을 생성
        HttpSession session = request.getSession();
        //세션에 로그인 회원 정보 보관
        session.setAttribute(SessionConst.LOGIN_MEMBER, memberList.get(0));

        return "redirect:" + redirectURL;
    }

    @PostMapping("/logout") // 로그아웃
    public String logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate(); // 세션 삭제
        }
        return "redirect:/";
    }

    @GetMapping("/members/add") // 회원가입
    public String addForm(@SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) Member loginMember, @ModelAttribute("member") Member member, Model model) {
        // 세션이 이미 있으면 로그인 상태 화면 이동
        if (loginMember != null) {
            model.addAttribute("member", loginMember);
            return "loginHome";
        }

        return "members/addMemberForm";
    }

    @PostMapping("/members/add") // 회원가입(기능)
    public String save(@Valid @ModelAttribute Member member, BindingResult bindingResult) {
        // 이메일 양식 체크 패턴
        String pattern_mail = "^[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*.[a-zA-Z]{2,3}$";
        // 패스워드 양식 체크 패턴
        String pattern_password = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*\\W).{8,12}$";

        if (bindingResult.hasErrors()) {
            return "members/addMemberForm";
        }

        if (!Pattern.matches(pattern_mail, member.getLoginId())) {
            bindingResult.reject("loginFail", "올바른 이메일 형식이 아닙니다.");
            return "members/addMemberForm";
        }

        if (!Pattern.matches(pattern_password, member.getPassword())) {
            bindingResult.reject("loginFail", "비밀번호는 영문과 특수문자 숫자를 포함 8자 이상, 12자 이하입니다.");
            return "members/addMemberForm";
        }
        try {
            // 회원 가입
            loginService.save(member);
        } catch(Exception e) {
            if(e.getMessage().equals("이미 존재하는 회원입니다.")) {
                bindingResult.reject("loginFail", e.getMessage());
                return "members/addMemberForm";
            }
            throw new RuntimeException(e);
        }
        return "redirect:/";
    }
}