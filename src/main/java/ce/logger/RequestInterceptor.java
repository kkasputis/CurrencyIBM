package ce.logger;




import java.time.LocalDateTime;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import ce.entity.RequestActivityLog;
import ce.repository.RequestActivityLogRepository;




@Component
public class RequestInterceptor extends HandlerInterceptorAdapter {

	@Autowired
	RequestActivityLogRepository requestActivityLog;

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
    	if (!request.getMethod().equals("OPTIONS")) {
    	RequestActivityLog log = new RequestActivityLog();
    	log.setTime(LocalDateTime.now());
    	log.setRequestType(request.getMethod());
    	log.setUrl(request.getRequestURL().toString());
    	log.setResponseCode(response.getStatus());
    	requestActivityLog.save(log);
    	}
    }

}