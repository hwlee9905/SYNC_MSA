package user.service.global.exception;

public class LinkCannotBeSavedException extends RuntimeException{
	public LinkCannotBeSavedException(String message) {
		super(message);
	}
	
	public LinkCannotBeSavedException() {
		
	}
}
