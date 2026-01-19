package org.ziranziyuanting.authcenter.config.seurity.dto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.ziranziyuanting.authcenter.config.seurity.CustomUserDetails;

import lombok.Data;

@Data
public class UsernamePasswordAuthenticationTokenDTO {
    private List<SimpleGrantedAuthorityDTO> authorities;
    private WebAuthenticationDetailsDTO details;
    // private boolean authenticated;
    private CustomUserDetails principal;
    public static UsernamePasswordAuthenticationTokenDTO from(UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken){
        UsernamePasswordAuthenticationTokenDTO usernamePasswordAuthenticationTokenDTO = new UsernamePasswordAuthenticationTokenDTO();
        List<SimpleGrantedAuthorityDTO> simpleGrantedAuthorityDTOs = new ArrayList<>();// 需要DTO
        usernamePasswordAuthenticationToken.getAuthorities().forEach(item -> {
            SimpleGrantedAuthorityDTO simpleGrantedAuthorityDTO = new SimpleGrantedAuthorityDTO();
            simpleGrantedAuthorityDTO.setRole(item.getAuthority());
            simpleGrantedAuthorityDTOs.add(simpleGrantedAuthorityDTO);
        });
        usernamePasswordAuthenticationTokenDTO.authorities = simpleGrantedAuthorityDTOs;
        
        var details = usernamePasswordAuthenticationToken.getDetails();
        if(details != null){
            WebAuthenticationDetails webAuthenticationDetails = (WebAuthenticationDetails)usernamePasswordAuthenticationToken
                .getDetails();
            WebAuthenticationDetailsDTO webAuthenticationDetailsDTO = new WebAuthenticationDetailsDTO();
            webAuthenticationDetailsDTO.setRemoteAddress(webAuthenticationDetails.getRemoteAddress());
            webAuthenticationDetailsDTO.setSessionId(webAuthenticationDetails.getSessionId());
            usernamePasswordAuthenticationTokenDTO.details = (webAuthenticationDetailsDTO);
        }
        // usernamePasswordAuthenticationTokenDTO.authenticated = (true);
        usernamePasswordAuthenticationTokenDTO.principal = ((CustomUserDetails)usernamePasswordAuthenticationToken.getPrincipal());
        return usernamePasswordAuthenticationTokenDTO;
    }
    public UsernamePasswordAuthenticationToken toUsernamePasswordAuthenticationToken(){
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        authorities.forEach(item -> {
            GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(item.getRole());
            grantedAuthorities.add(grantedAuthority);
        });
        //UsernamePasswordAuthenticationToken(Object principal, Object credentials, Collection<? extends GrantedAuthority> authorities)
        UsernamePasswordAuthenticationToken r = new UsernamePasswordAuthenticationToken(principal, principal.getPassword(), grantedAuthorities);
        WebAuthenticationDetails webAuthenticationDetails = new WebAuthenticationDetails(details.getRemoteAddress(), details.getSessionId());
        r.setDetails(webAuthenticationDetails);
        // r.setAuthenticated(true);
        return r;
    }
}
