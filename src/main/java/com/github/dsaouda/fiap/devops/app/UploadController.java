package com.github.dsaouda.fiap.devops.app;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.github.dsaouda.fiap.devops.dao.ImageDao;
import com.github.dsaouda.fiap.devops.model.Image;
import com.github.dsaouda.fiap.devops.service.StorageService;
import com.google.cloud.storage.Blob;

@RestController
@RequestMapping("/v1/upload")
public class UploadController {

	@Autowired
	private StorageService storage;
	
	@Autowired
	private ImageDao imageDao;
	
	@PostMapping
	public ResponseEntity<String> create(@RequestParam("file") MultipartFile fileUpload) {		
		try {
			Blob blob = storage.uploadFile(fileUpload);
			
			Image image = new Image();
			image.setName(blob.getName());			
			imageDao.put(image);
			
			return new ResponseEntity<>(blob.getName(), HttpStatus.CREATED);
		} catch (IOException e) {
			return new ResponseEntity<>("solicitação não foi bem sucedida. Envie um arquivo válido.", HttpStatus.BAD_REQUEST);
		}		
	}	
}