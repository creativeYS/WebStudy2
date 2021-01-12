package com.example.demo;

import com.example.demo.entity.User;
import com.example.demo.entity.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class DemoRestController {

    @Autowired
    UserRepository userRepository;

    @RequestMapping(method = RequestMethod.GET, value="/user")
    @ResponseBody
    public String test(User user) {
        if(user == null) return "";
        userRepository.save(user);
        return "";
    }

    /*
    @GetMapping("/user/id")
    public User join(@RequestParam Map<String, String> param){
        String id = param.get("id");
        if(id == "") return new User();
        //return userMapper.findAll(id);
        return new 0;
    }
    */
    /*
    @PostMapping("/join/create")
    public String create(@RequestParam Map<String, String> param) {
        String name = param.get("name");
        if(name != "")
        {
            User user = new User();
            user.setName(name);
            userMapper.insert(user);
            return "created";
        }
        return "create failed";
    }
    */

    /*
    @PostMapping("/sign/login")
    public String login(@RequestParam Map<String, String> param)
    {
        String id = param.get("id");
        String name = param.get("name");
        if(id != null && name != null)
        {
            User user = userMapper.find(id, name);
            if( user != null)
            {
                if(user.getName().length() > 0 && user.getId() > 0)
                {
                    return "logged in";
                }
            }
        }

        return "login failed";
    }
    */


    /*
    @PostMapping("/join/create")
    public String create() {
        return "main";
    }
    */
}