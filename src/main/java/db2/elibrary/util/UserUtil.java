package db2.elibrary.util;

/**
 * @Author: Tianshi Chen
 * @Description:
 * @Date created at 4:17 PM
 */

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;


/**
 *@program: JWTtest
 *@description: to fetch the current user info from context
 *@author: Tianshi Chen
 *@create: 2020-05-01 16:17
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
