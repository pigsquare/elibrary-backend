package db2.elibrary.util;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;


/**
 *@program: JWTtest
 *@description: to fetch the current user info from context
 */
public class UserUtil {
    private static Authentication getCurrentAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }
    public static Object getCurrentPrincipal() {
        return getCurrentAuthentication().getPrincipal();
    }
    public UserDetails getCurrentUser(@AuthenticationPrincipal UserDetails userDetails) {
        return userDetails;
    }
    public static String getCurrentUserAccount() {
        Authentication authentication = getCurrentAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            return authentication.getName();
        } else return null;
    }
}
