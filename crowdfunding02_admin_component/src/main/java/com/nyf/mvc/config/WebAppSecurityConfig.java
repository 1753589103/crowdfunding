package com.nyf.mvc.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Configuration            // 设置为配置类
@EnableWebSecurity        // 开启web环境下的权限控制功能
//@EnableGlobalMethodSecurity(prePostEnabled = true) //开启全局权限控制
// 需要继承WebSecurityConfigurerAdapter
public class WebAppSecurityConfig extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity security) throws Exception {
        // String数组，列出需要放行的资源的路径
        String[] permitUrls = {"/index.jsp","/bootstrap/**",
                "/crowd/**","/css/**","/fonts/**","/img/**",
                "/jquery/**","/layer/**","/script/**","/ztree/**","/admin/login/page.html"};
        //范围小的放在大范围之前
        security
                .authorizeRequests()//表示对请求进行授权
                .antMatchers(permitUrls)//传入的ant风格的url
                .permitAll()//允许上面的所有请求，不需要认证
                .antMatchers("/role/get/page/info.json")	// 设置要得到admin的分页信息
                .hasRole("经理")						   // 必须具有经理的角色
                .anyRequest()//设置其它未设置的全部请求
                .authenticated()//表示需要认证
                .and()
                .exceptionHandling()
                .accessDeniedHandler(new AccessDeniedHandler() {
                    @Override
                    public void handle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AccessDeniedException e) throws IOException, ServletException {
                        httpServletRequest.setAttribute("exception", new Exception("抱歉，您没有权限访问该资源！"));
                        httpServletRequest.getRequestDispatcher("/WEB-INF/system-error.jsp").forward(httpServletRequest,httpServletResponse);
                    }
                })
                .and()
                .csrf()
                .disable()
                .formLogin()                                        //开启表单登录的功能
                .loginPage("/admin/login/page.html")                //指定登陆页面
                .loginProcessingUrl("/security/do/login.html")      //指定处理登录请求的地址
                .defaultSuccessUrl("/admin/main/page.html")         //指定登陆成功后前往的页面
                .usernameParameter("login-user")                    //账号的请求参数名称
                .passwordParameter("login-pwd")                     //密码的请求参数名称
                .and()
                .logout()                                       // 开启退出登录功能
                .logoutUrl("/security/do/logout.html")          // 设置退出登录的url
                .logoutSuccessUrl("/admin/login/page.html")    // 设置退出成功后前往的页面

                ;
    }

    @Autowired
    BCryptPasswordEncoder passwordEncoder;

    @Autowired
    CrowdUserDetailService crowdUserDetailService;
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth.inMemoryAuthentication().passwordEncoder(new BCryptPasswordEncoder()).withUser("admin").password(new BCryptPasswordEncoder().encode("123456")).roles("ADMIN");
        auth
                .userDetailsService(crowdUserDetailService)
                ;

    }
}
