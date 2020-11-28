package ua.skidchenko.registrationform.interceptors;

import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

@Log4j2
public class PutUserInfoInterceptor implements HandlerInterceptor {

    @Override
    public void postHandle(HttpServletRequest aRequest, HttpServletResponse aResponse, Object aHandler, ModelAndView aModelAndView) throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Optional<? extends GrantedAuthority> userRole = authentication
                .getAuthorities()
                .stream()
                .findFirst();
        String username = authentication.getName();
        if (aModelAndView != null && userRole.isPresent()) {
            aModelAndView.getModelMap().addAttribute("userRole",userRole.get());
            aModelAndView.getModelMap().addAttribute("username",username);
            log.info("Attached to the model: User role " + userRole.get() + ", Username " + username + ".");
        }
    }

}
