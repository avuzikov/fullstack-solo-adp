package com.example.data_service.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.Map;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

	private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

	@Autowired
	private RestTemplate restTemplate;

	@Value("${auth.service.url}")
	private String authServiceUrl;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		logger.debug("JwtAuthenticationFilter is processing a request to: {}", request.getRequestURI());

		// Skip authentication for /api endpoint
		if ("/api".equals(request.getRequestURI())) {
			filterChain.doFilter(request, response);
			return;
		}

		String token = extractToken(request);
		logger.debug("Extracted token: {}", token);

		if (token != null) {
			try {
				boolean isValid = validateToken(token);
				logger.debug("Token validation result: {}", isValid);
				if (isValid) {
					String userEmail = getUserEmailFromToken(token);
					logger.debug("User email from token: {}", userEmail);
					UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
							userEmail, null, Collections.emptyList());
					SecurityContextHolder.getContext().setAuthentication(authentication);
					logger.debug("Authentication set in SecurityContextHolder");
				} else {
					logger.warn("Token is not valid");
					response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
					return;
				}
			} catch (Exception e) {
				logger.error("Error processing token", e);
				response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
				return;
			}
		} else {
			logger.debug("No token found in the request");
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			return;
		}

		filterChain.doFilter(request, response);
	}

	private String extractToken(HttpServletRequest request) {
		String bearerToken = request.getHeader("Authorization");
		if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
			return bearerToken.substring(7);
		}
		return null;
	}

	private boolean validateToken(String token) {
		HttpHeaders headers = new HttpHeaders();
		headers.set("Authorization", "Bearer " + token);
		HttpEntity<String> entity = new HttpEntity<>(headers);

		try {
			logger.debug("Sending token validation request to auth service at URL: {}", authServiceUrl + "/account/validate");
			ResponseEntity<Map> response = restTemplate.exchange(
					authServiceUrl + "/account/validate",
					HttpMethod.POST,
					entity,
					Map.class
			);
			logger.debug("Token validation response status: {}", response.getStatusCode());
			logger.debug("Token validation response body: {}", response.getBody());

			Map<String, Object> responseBody = response.getBody();
			return responseBody != null && (boolean) responseBody.get("valid");
		} catch (Exception e) {
			logger.error("Error validating token", e);
			return false;
		}
	}

	private String getUserEmailFromToken(String token) {
		HttpHeaders headers = new HttpHeaders();
		headers.set("Authorization", "Bearer " + token);
		HttpEntity<String> entity = new HttpEntity<>(headers);

		try {
			ResponseEntity<Map> response = restTemplate.exchange(
					authServiceUrl + "/account/validate",
					HttpMethod.POST,
					entity,
					Map.class
			);

			Map<String, Object> responseBody = response.getBody();
			return responseBody != null ? (String) responseBody.get("email") : null;
		} catch (Exception e) {
			logger.error("Error getting user email from token", e);
			return null;
		}
	}
}
