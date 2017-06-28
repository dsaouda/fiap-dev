package com.github.dsaouda.fiap.devops.app;

import java.io.IOException;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Bucket;
import com.google.cloud.storage.BucketInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;

@RestController
@RequestMapping("/v1/upload")
public class UploadController {

	
	@PostMapping
	public ResponseEntity<String> create(@RequestParam("file") MultipartFile fileUpload) {
		Storage storage = StorageOptions.getDefaultInstance().getService();
		String bucketName = "fiap-devops";
		Bucket bucket = storage.get(bucketName);
				
		if (bucket == null) {
			bucket = storage.create(BucketInfo.of(bucketName));
		}
		
		String filename = UUID.randomUUID() + fileUpload.getOriginalFilename().toLowerCase().replaceAll("[^a-z0-9-._]", "");
		String contentType = fileUpload.getContentType();
		
		BlobId blobId = BlobId.of(bucketName, filename);
		BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setContentType(contentType).build();
		try {
			storage.create(blobInfo, fileUpload.getInputStream());
		} catch (IOException e) {
			new ResponseEntity<String>("solicitação não foi bem sucedida. Envie um arquivo válido.", HttpStatus.BAD_REQUEST);
		}
		
		return new ResponseEntity<String>(filename, HttpStatus.CREATED);		
	}
	
}
