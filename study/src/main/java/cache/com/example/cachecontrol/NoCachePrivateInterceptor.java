package cache.com.example.cachecontrol;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpHeaders;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

public class NoCachePrivateInterceptor implements HandlerInterceptor {

    public static final String NO_CACHE_PRIVATE = CacheControl.noCache().cachePrivate().getHeaderValue();

    @Override
    public void postHandle(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler,
            ModelAndView modelAndView
    ) {
        response.setHeader(HttpHeaders.CACHE_CONTROL, NO_CACHE_PRIVATE);
    }
}
