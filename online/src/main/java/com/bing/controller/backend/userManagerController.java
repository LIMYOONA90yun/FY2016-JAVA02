package com.bing.controller.backend;

import com.bing.common.ServerResponse;
import com.bing.pojo.User;
import com.bing.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/manage/")
public class userManagerController {
    @Autowired
    IUserService userService;
    @Autowired
    HttpSession session;
    @RequestMapping("loginout")
    public ServerResponse login(User user){
        ServerResponse serverResponse= userService.login(user,0);

        if(serverResponse.isSucess()){
            session.setAttribute("user",serverResponse.getData());}
            return serverResponse;
    }
}
