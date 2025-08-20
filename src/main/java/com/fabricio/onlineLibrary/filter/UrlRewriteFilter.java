package com.fabricio.onlinelibrary.filter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@WebFilter("/*") 
public class UrlRewriteFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        String uri = req.getRequestURI();
        String context = req.getContextPath(); 
        String path = uri.substring(context.length()); 

        if (path.equals("/login")) { 
            request.getRequestDispatcher("/pages/login.zul").forward(request, response);
            return;
        }

        if (path.equals("/register")) { 
            request.getRequestDispatcher("/pages/register.zul").forward(request, response);
            return;
        }

        if (path.equals("/dashboard")) { 
            request.getRequestDispatcher("/pages/dashboard.zul").forward(request, response);
            return;
        } 

         if (path.equals("/people")) { 
            request.getRequestDispatcher("/pages/people.zul").forward(request, response);
            return;
        } 
        if (path.equals("/books")) { 
            request.getRequestDispatcher("/pages/books.zul").forward(request, response);
            return;
        } 
          
        chain.doFilter(request, response);
    }
}
