package filters;

import javax.servlet.*;
import java.io.IOException;

public class EncodingFilter implements Filter {
    private String encoding;
    
    public void init(FilterConfig filterConfig) throws ServletException {
        encoding = filterConfig.getInitParameter("encoding");
    }
    
    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {
        String requestEncoding = request.getCharacterEncoding();
        String responseEncoding = response.getCharacterEncoding();
        if (encoding != null &&
                (!encoding.equalsIgnoreCase(requestEncoding) || !encoding.equalsIgnoreCase(responseEncoding))) {
            request.setCharacterEncoding(encoding);
            response.setCharacterEncoding(encoding);
        }
        chain.doFilter(request, response);
    }
    
    public void destroy() {
        encoding = null;
    }
}
