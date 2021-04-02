package com.hkk.cloudtv.core.manager.view.action;

import com.hkk.cloudtv.core.service.IUserService;
import com.hkk.cloudtv.core.utils.CaptchaUtil;
import com.hkk.cloudtv.entity.User;
import com.hkk.cloudtv.vo.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.thymeleaf.util.StringUtils;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

@Api(description = "登录控制器")
@RestController
@RequestMapping(value = "/buyer")
public class LoginController{

    @Autowired
    private IUserService userService;

   /* @RequestMapping("/login")
    public Object login(){
        Object object = SecurityUtils.getSubject().getPrincipal();
        String user = (String)object;
        if(user != null){
            return new Result(200, "已登录",object);
        }else{
            return new Result(-100, "未登录",object);
        }
    }*/

    @ApiOperation("登录方法")
    @RequestMapping("/login")
    public Object login(HttpServletRequest request, HttpServletResponse response,
                        String username, String password,@ApiParam("验证码") String captcha){
        String msg = "";
        // 通过安全工具类获取 Subject
        Subject subject = SecurityUtils.getSubject();
        // 获取当前已登录用户
        //User user = (User) SecurityUtils.getSubject().getPrincipal();
      /*  String username = request.getParameter("username");
        String password = request.getParameter("password");*/
       /* String sale = SaltUtils.getSalt(8);
        Md5Hash md5Hash = new Md5Hash(password, sale, 1024);*/
        Session session = SecurityUtils.getSubject().getSession();
        String sessionCaptcha = (String) session.getAttribute("captcha");
        if(captcha != null && !StringUtils.isEmpty(captcha) && !StringUtils.isEmpty(sessionCaptcha)){
            if(sessionCaptcha.toUpperCase().equals(captcha.toUpperCase())){
                String login_username = (String) SecurityUtils.getSubject().getPrincipal();
                boolean flag = true;// 当前用户是否已登录
                if(login_username != null && !login_username.equals("")){
                    if(login_username.equals(username)){
                        flag = false;
                    }
                }
                if(flag){
                    UsernamePasswordToken token = new UsernamePasswordToken(username,password);
                    try {
                        subject.login(token);
                        session.removeAttribute("captcha");
                        return new Result(200,"Successfully");
                        //  return "redirect:/index.jsp";
                    } catch (UnknownAccountException e) {
                        e.printStackTrace();
                        msg = "User name error";
                        System.out.println("用户名错误");

                        return new Result(410, msg);
                    } catch (IncorrectCredentialsException e){
                        e.printStackTrace();
                        msg = "wrong password";
                        System.out.println("密码错误");
                        return new Result(420, msg);
                    }
                }
           }else{
                return new Result(430, "Captcha error");
            }
        }else{
        return new Result(400,  "Captcha has expired");
    }
    return null;
  }

    /**
     * 验证码图片
     */
    @GetMapping("/captcha")
    public void captcha(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //设置响应头信息，通知浏览器不要缓存
        response.setHeader("Expires", "-1");
        response.setHeader("Cache-Control", "no-cache");
        response.setHeader("Pragma", "-1");
        response.setContentType("image/jpeg");

        // 获取验证码
        String code = CaptchaUtil.getRandomCode();
        // 将验证码输入到session中，用来验证
        HttpSession session = request.getSession();
        session.setAttribute("captcha", code);
        this.removeAttrbute(session, "captcha");
        // 输出到web页面
        ImageIO.write(CaptchaUtil.genCaptcha(code), "jpg", response.getOutputStream());
    }

    public void removeAttrbute(final HttpSession session, final String attrName) {
        final Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            public void run() {
                // 删除session中存的验证码
                session.removeAttribute(attrName);
                timer.cancel();
            }
        }, 5 * 60 * 1000); //5 * 60 * 1000
    }
 /*   摘取自下方
————————————————
    版权声明：本文为CSDN博主「xxWillxx」的原创文章，遵循CC 4.0 BY-SA版权协议，转载请附上原文出处链接及本声明。
    原文链接：https://blog.csdn.net/qq_40858048/article/details/81075805
*/
    @RequestMapping("/logout")
    public Object logout(){
        Subject subject = SecurityUtils.getSubject();
        String username = (String) subject.getPrincipal();
        User obj =  this.userService.findByUserName(username);
        if(obj != null){
            subject.logout(); // 退出登录
            return new Result(200, "Successfully");
        }else{
            return new Result(401,"log in");
        }
    }



/*    @RequestMapping(value = "getYzm", method = RequestMethod.GET)
    public void getYzm(HttpServletResponse response, HttpServletRequest request) {
        try {
            response.setHeader("Pragma", "No-cache");
            response.setHeader("Cache-Control", "no-cache");
            response.setDateHeader("Expires", 0);
            response.setContentType("image/jpeg");
            // 生成随机字串
            String verifyCode = VerifyCodeUtils.generateVerifyCode(4);
            // 存入会话session
            HttpSession session = request.getSession(true);
            String attrName = "_code";
            session.setAttribute(attrName, verifyCode.toLowerCase());
            // 生成验证码流
            int w = 99, h = 38;
            VerifyCodeUtils.outputImage(w, h, response.getOutputStream(), verifyCode);
            //定时5分钟删除验证码
            this.removeAttrbute(session, attrName);
        } catch (Exception e) {
            log.error("获取验证码异常：{}",e.getCause().toString());
        }
    }

    *//**
     * 设置5分钟后删除session中的验证码
     * @param session
     * @param attrName
     *//*
    private void removeAttrbute(final HttpSession session, final String attrName) {
        final Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                // 删除session中存的验证码
                session.removeAttribute(attrName);
                timer.cancel();
            }
        }, 5 * 60 * 1000);
    }
}
————————————————
        版权声明：本文为CSDN博主「请持续率性」的原创文章，遵循CC 4.0 BY-SA版权协议，转载请附上原文出处链接及本声明。
        原文链接：https://blog.csdn.net/k21325/article/details/78365872*/
}

