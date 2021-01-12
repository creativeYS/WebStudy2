package com.example.demo;

import com.example.demo.Security.UserAccountChangedEvent;
import com.example.demo.entity.Role;
import com.example.demo.entity.User;
import com.example.demo.entity.UserDetailsVO;
import com.example.demo.entity.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

@Controller
public class DemoController {

    @Resource(name = "userDetailsService")
    private UserDetailsService userDetailsService;

    @Autowired
    ApplicationEventPublisher eventPublisher;

    @Autowired
    UserRepository userRepository;

    @Autowired
    BCryptPasswordEncoder passwordEncoder;

    boolean isAnnonymous() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if(auth == null) return true;
        boolean isAnonymousUser = auth instanceof AnonymousAuthenticationToken;
        return isAnonymousUser;
    }

    UserDetailsVO getUserDetail() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if(auth == null) return null;
        if(auth.getPrincipal() != null && auth.getPrincipal() instanceof UserDetailsVO)
        {
            UserDetailsVO userDetail = (UserDetailsVO)auth.getPrincipal();
            return userDetail;
        }
        return null;
    }

    @GetMapping("/")
    public String main(HttpSession session, ModelMap modelMap) {
        if(!isAnnonymous()) return "redirect:/sign";

        return "main";
    }

    @GetMapping("/sign")
    public String sign(HttpSession session, ModelMap modelMap) {
        if(isAnnonymous()) return "redirect:/";
/*
        String userid = (String)session.getAttribute("userid");
        User user = userRepository.findByUserid(userid);
        modelMap.addAttribute("name", user.getName());

 */
        UserDetailsVO user = getUserDetail();
        if(user != null)
        {
            modelMap.addAttribute("name", user.getName());
        }

        return "sign";
    }

    @GetMapping("/join")
    public String join(){
        return "join";
    }

    @GetMapping("/denied")
    public String denied(){
        return "denied";
    }

    @GetMapping("/admin")
    public String admin(){
        return "admin";
    }
/*
    @PostMapping("/sign/login")
    public String login(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception
    {
        String id = request.getParameter("userid");
        String pw = request.getParameter("pw");
        User user = userRepository.findByUserid(id);

        if(user != null &&
                user.getPw().compareTo(pw) == 0)
        {
            session.setAttribute("logged", 1);
            session.setAttribute("userid", id);
            return "redirect:/";
        }

        PrintWriter out = response.getWriter();
        out.println("<script>alert('Fail to Login');location.href='/'</script>");
        out.flush();
        return "/";
    }
 */

    @PostMapping("/sign/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response)
    {
        new SecurityContextLogoutHandler().logout(request, response, SecurityContextHolder.getContext().getAuthentication());
        return "redirect:/";
    }

    @PostMapping("/join/create")
    @ResponseBody
    public ModelAndView userCreate(HttpServletRequest request, HttpServletResponse response) throws Exception {
        ModelAndView mv = new ModelAndView();

        User user = User.builder()
                .name(request.getParameter("name"))
                .userid(request.getParameter("userid"))
                .pw(passwordEncoder.encode(request.getParameter("pw")))
                .role(Role.valueOf(request.getParameter("role")))
                .build();

        User userExist = userRepository.findByUserid(user.getUserid());
        if(userExist == null &&
                user.getUserid().length() > 0 &&
                user.getName().length() > 0 &&
                user.getPw().length() > 0)
        {
            userRepository.save(user);
/*
            PrintWriter out = response.getWriter();
            out.println("<script>alert('Successed');location.href='/'</script>");
            out.flush();
 */

            mv.setViewName("redirect:/");
            return mv;

        }

        PrintWriter out = response.getWriter();
        out.println("<script>alert('Fail to register'); location.href='/';</script>");
        out.flush();

        mv.setViewName("redirect:/join");
        return mv;
    }

    @GetMapping("/mod")
    public String mod(HttpServletRequest request, HttpServletResponse response, ModelMap modelMap, HttpSession session) throws Exception {
        if(isAnnonymous())
        {
            /*
            PrintWriter out = response.getWriter();
            out.println("<script>alert('invalid access');location.href='/'</script>");
            out.flush();

             */
            return "redirect:/";
        }

        UserDetailsVO user = getUserDetail();

/*
        String userid = (String)session.getAttribute("userid");
        User user = userRepository.findByUserid(userid);
        String name = user.getName();
        */

        modelMap.addAttribute("userid", user.getUsername());
        modelMap.addAttribute("name", user.getName());
        modelMap.addAttribute("email", user.getUserVO().getEmail());

        return "mod";
    }

    @PostMapping("/mod/modify")
    public String UserModify(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception {
        if(isAnnonymous()) return "redirect:/";

        UserDetailsVO user = getUserDetail();
        if(user == null) return "redirect:/";

        if (passwordEncoder.matches(request.getParameter("pw"), user.getPw()))
        {
            User userUpdate = userRepository.findByUserid(user.getUserid());
            userUpdate.setName(request.getParameter("name"));
            userUpdate.setEmail(request.getParameter("email"));
            userRepository.save(userUpdate);

            eventPublisher.publishEvent(new UserAccountChangedEvent(this, userUpdate.getUserid()));
            return "redirect:/";
        }

        return "redirect:/mod";

/*
        if(session.getAttribute("logged") == null)
        {
            PrintWriter out = response.getWriter();
            out.println("<script>alert('invalid access');location.href='/'</script>");
            out.flush();
            return new ModelAndView("redirect:/mod");
        }
        String userid = (String)session.getAttribute("userid");
        if(userid == null)
        {
            PrintWriter out = response.getWriter();
            out.println("<script>alert('invalid access');location.href='/'</script>");
            out.flush();
            return new ModelAndView("redirect:/mod");
        }

        User userExist = userRepository.findByUserid(userid);
        if(userExist == null)
        {
            PrintWriter out = response.getWriter();
            out.println("<script>alert('invalid access');location.href='/'</script>");
            out.flush();
            return new ModelAndView("redirect:/mod");
        }

        if(userExist.getPw().compareTo(request.getParameter("pw")) != 0)
        {
            PrintWriter out = response.getWriter();
            out.println("<script>alert('Wrong Password');location.href='/mod'</script>");
            out.flush();
            return new ModelAndView("redirect:/mod");
        }

        // success
        userExist.setName(request.getParameter("name"));
        userRepository.save(userExist);
        return new ModelAndView("redirect:/");
        */
    }

    @PostMapping("/mod/unsign")
    @ResponseBody
    public ModelAndView UserUnsign(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception {
        if(session.getAttribute("logged") == null)
        {
            PrintWriter out = response.getWriter();
            out.println("<script>alert('invalid access');location.href='/'</script>");
            out.flush();
            return new ModelAndView("redirect:/mod");
        }
        String userid = (String)session.getAttribute("userid");
        if(userid == null)
        {
            PrintWriter out = response.getWriter();
            out.println("<script>alert('invalid access');location.href='/'</script>");
            out.flush();
            return new ModelAndView("redirect:/mod");
        }

        User userExist = userRepository.findByUserid(userid);
        if(userExist == null)
        {
            PrintWriter out = response.getWriter();
            out.println("<script>alert('invalid access');location.href='/'</script>");
            out.flush();
            return new ModelAndView("redirect:/mod");
        }

        if(userExist.getPw().compareTo(request.getParameter("pw")) != 0)
        {
            PrintWriter out = response.getWriter();
            out.println("<script>alert('Wrong Password');location.href='/mod'</script>");
            out.flush();
            return new ModelAndView("redirect:/mod");
        }

        session.setAttribute("logged", null);
        session.setAttribute("userid", null);

        // success
        userRepository.delete(userExist);
        return new ModelAndView("redirect:/");
    }
};