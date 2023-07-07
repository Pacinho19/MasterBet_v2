package pl.pacinho.MasterBet.config;

import lombok.Getter;
import org.apache.log4j.Logger;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.authentication.event.LogoutSuccessEvent;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class ApplicationConfig {

    protected final Logger logger = Logger.getLogger(ApplicationConfig.class);

    @Getter
    private final String NAME = "MasterBet2.0";
    @Getter
    private final String VERSION = "dist_20211004_v1";

    @EventListener
    public void onApplicationEvent(ApplicationEvent appEvent) {
        if (appEvent instanceof AuthenticationSuccessEvent) {
            AuthenticationSuccessEvent event = (AuthenticationSuccessEvent) appEvent;
            UserDetails userDetails = (UserDetails) event.getAuthentication().getPrincipal();
            logger.info("[LOGIN] -> User Login :  " + userDetails.getUsername() + " (ROLES : "
                    + userDetails.getAuthorities()
                    .stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.joining(", ")) +") Login Success !");
        } else if (appEvent instanceof AuthenticationFailureBadCredentialsEvent) {
            AuthenticationFailureBadCredentialsEvent event = (AuthenticationFailureBadCredentialsEvent) appEvent;
            Object userName = event.getAuthentication().getPrincipal();
            logger.error("[LOGIN] -> " + userName + " Login Failed !");
        }
        else if (appEvent instanceof LogoutSuccessEvent) {
            LogoutSuccessEvent event = (LogoutSuccessEvent) appEvent;
            UserDetails userDetails =  (UserDetails) event.getAuthentication().getPrincipal();
            logger.error("[LOGIN] -> " + userDetails.getUsername() + " Logout !");
        }
    }

    @EventListener
    public void appReady(ApplicationReadyEvent event) {
        logger.info("[SYSTEM] -> Start Application  " + getNAME() + " " + getVERSION());
    }
}
