package com.github.dsaouda.fiapdev;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/hello")
public class HelloController {

	@GetMapping("/version")
	public ResponseEntity<String> versao() {
		return new ResponseEntity<>("v1", HttpStatus.OK);
	}
	
	@GetMapping
	public ResponseEntity<String> index() {
		return new ResponseEntity<>("Data atual é " + LocalDateTime.now(), HttpStatus.OK);
	}
	
}
