//package com.simple.book.domain.message.controller;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.messaging.handler.annotation.MessageMapping;
//import org.springframework.messaging.handler.annotation.SendTo;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RestController;
//
//import com.simple.book.domain.message.service.MessageService;
//
//@RestController
//public class MessageController {
//	
//	@Autowired
//	private MessageService messageService;
//	
////	@PostMapping("/kafka/send")
////	public ResponseEntity<?> send(@RequestBody String message) {
////		messageService.sendMessage("test", message);
////		return ResponseEntity.status(HttpStatus.OK).body("완료");
////	}
//	
////	@GetMapping("/kafka/read")
////	public ResponseEntity<?> listen(){
////		String message = messageService.read();
////		return ResponseEntity.status(HttpStatus.OK).body(message);
////	}
//	
////	@MessageMapping("/kakfa")
////	@SendTo("/topic/text")
////	public void send(@RequestBody String message) {
////		System.out.println("message: " + message);
////	}
//	
//	@MessageMapping("/chat")
//	@SendTo("/topic/messages")
//	public void socketSend(String message) {
//		messageService.sendMessage("test", message);
//	}
//}
