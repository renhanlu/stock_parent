package com.itheima.stock.security.filter;

import com.itheima.stock.utils.JwtTokenUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * @author by itheima
 * @Date 2021/12/26
 * @Description 自定义鉴权过滤器
 */
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        //判断是否已经有token
        //获取token
        String tokenStr = request.getHeader(JwtTokenUtil.TOKEN_HEADER);
        // 若请求头中没有Authorization信息 则直接放行
        if (tokenStr == null || tokenStr.trim()==""){
            //放行到的登录页面登录操作
            chain.doFilter(request,response);
            //停止当前逻辑判断
            return;
        }
        // 从Token中解密获取用户名
        String username = JwtTokenUtil.getUsername(tokenStr);
        // 从Token中解密获取用户角色和权限集合字符串
        String role = JwtTokenUtil.getUserRole(tokenStr);
        //获取以逗号间隔的权限字符串
        String rolesNames = StringUtils.strip(role, "[]");
        //获取权限集合
        List<GrantedAuthority> authorities = AuthorityUtils.commaSeparatedStringToAuthorityList(rolesNames);
        if (username== null){
            throw new RuntimeException("token无效");
        }
        //生成授权token
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(username, null, authorities);
        //认证token对象存入security上下文中,其它security过滤器都可以直接在上下文对象中获取该token
        SecurityContextHolder.getContext().setAuthentication(token);
        chain.doFilter(request,response);
    }
}
