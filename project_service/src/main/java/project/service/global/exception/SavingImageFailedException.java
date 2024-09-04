package project.service.global.exception;

public class SavingImageFailedException extends RuntimeException{

	public SavingImageFailedException (String message) {
		super(message);
	}
	
	public SavingImageFailedException() {
		
	}
}
