package com.github.dsaouda.fiap.devops.app;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/vision")
public class VisionController {
	
	@PostMapping("/{fileId}")
	public ResponseEntity<String> create(@PathVariable String fileId) {
		
		return new ResponseEntity<String>(fileId, HttpStatus.OK);		
	}
	
}
