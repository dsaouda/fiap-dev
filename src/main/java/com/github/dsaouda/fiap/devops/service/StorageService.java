package com.github.dsaouda.fiap.devops.service;

import java.io.IOException;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Bucket;
import com.google.cloud.storage.BucketInfo;
import com.google.cloud.storage.Storage;
import com.google.common.io.Files;

@Service
public class StorageService {

	private Storage storage;
	private Environment env;
	private Bucket bucket;
	
	@Autowired
	public StorageService(Storage storage, Environment env) {
		this.storage = storage;
		this.env = env;
		
		getOrCreateBucket();
	}
	
	public Bucket getOrCreateBucket() {
		if (bucket != null) {
			return bucket;
		}

		bucket = storage.get(env.getProperty("DSAOUDA_GOOGLE_BUCKET"));

		if (bucket == null) {
			bucket = storage.create(BucketInfo.of(env.getProperty("DSAOUDA_GOOGLE_BUCKET")));
		}

		return bucket;
	}
	
	public Blob getBlobByFile(String filename) {
		BlobId blobId = BlobId.of(env.getProperty("DSAOUDA_GOOGLE_BUCKET"), filename);		
		return storage.get(blobId);		
	}

	public Blob uploadFile(MultipartFile fileUpload) throws IOException {
		String extention = Files.getFileExtension(fileUpload.getOriginalFilename());
		
		String filename = UUID.randomUUID() + "." + extention;
		String contentType = fileUpload.getContentType();
		
		BlobId blobId = BlobId.of(env.getProperty("DSAOUDA_GOOGLE_BUCKET"), filename);
		BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setContentType(contentType).build();
				
		return storage.create(blobInfo, fileUpload.getInputStream());
	}
}
