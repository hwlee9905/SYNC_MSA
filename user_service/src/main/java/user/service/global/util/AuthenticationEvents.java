package user.service.global.util;

import org.springframework.context.event.EventListener;
import org.springframework.security.authorization.event.AuthorizationDeniedEvent;
import org.springframework.stereotype.Component;

import user.service.global.advice.ErrorCode;
import user.service.global.exception.AuthorizationFailureException;

@Component
public class AuthenticationEvents {
	@EventListener
	public void onFailure(AuthorizationDeniedEvent failure) {
		String errorMessage = "Authorization denied for the requested operation. pls login and try again.";
		throw new AuthorizationFailureException(errorMessage, ErrorCode.USER_FAILED_AUTHORIZATION);
	}
}