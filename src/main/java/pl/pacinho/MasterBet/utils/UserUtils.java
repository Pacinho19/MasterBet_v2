package pl.pacinho.MasterBet.utils;

import org.springframework.security.core.userdetails.UserDetails;

public class UserUtils {

    public static boolean checkRole(UserDetails userDetails, String role) {
        return userDetails.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_" + role));
    }
}
