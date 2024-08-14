package user.service.global.exception;

public class UnableToSendEmailException extends RuntimeException{
	public UnableToSendEmailException (String message) {
		super(message);
	}
	
	public UnableToSendEmailException() {
		
	}

}
