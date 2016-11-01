package it.marcoberri.ms.commons.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

public class TokenInterceptor implements HandlerInterceptor {

	private static final Logger logger = Logger.getLogger(TokenInterceptor.class);

	private boolean enable=false;
	
	private String fieldName;
	
	private String url;

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object arg2, Exception arg3) throws Exception {
		// TODO Auto-generated method stub
		logger.info("****** afterCompletion *****");
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object arg2, ModelAndView mav) throws Exception {

		logger.info("****** postHandle *****");
	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object arg2) throws Exception {
		
		if(!isEnable())
			return true;
		
		try {
			logger.info("****** preHandle *****");
			logger.info("request token header:" + request.getHeader(getFieldName()));
			logger.info("request token params:" + request.getParameter(getFieldName()));
			String token = request.getHeader(getFieldName());
			if (token == null)
				token = request.getParameter(getFieldName());
			logger.info("token:" + token);

			RestTemplate restTemplate = new RestTemplate();
			LinkedMultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
			map.add(getFieldName(), token);

			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

			HttpEntity<LinkedMultiValueMap<String, String>> req = new HttpEntity<LinkedMultiValueMap<String, String>>(map, headers);

			ResponseEntity<String> res = restTemplate.postForEntity(getUrl(), req, String.class);

			logger.debug("****** preHandle *****");
			return true;
			
		} catch (final Exception exception) {
			response.sendError(HttpServletResponse.SC_FORBIDDEN);
		
			return false;
		}
		
		
		
	}

	public boolean isEnable() {
		return enable;
	}

	public void setEnable(boolean enable) {
		this.enable = enable;
	}

	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

}
