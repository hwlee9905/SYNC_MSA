package user.service.global.exception;

public class UnknownException extends RuntimeException{
	public UnknownException(String message) {
        super(message);
    }
	
	public UnknownException() {
		
	}
}
