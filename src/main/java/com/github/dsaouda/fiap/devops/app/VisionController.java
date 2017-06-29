package com.github.dsaouda.fiap.devops.app;

import java.io.IOException;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.dsaouda.fiap.devops.dao.ImageDao;
import com.github.dsaouda.fiap.devops.service.StorageService;
import com.github.dsaouda.fiap.devops.service.VisionService;
import com.google.cloud.datastore.Entity;
import com.google.cloud.datastore.StringValue;
import com.google.cloud.storage.Blob;
import com.google.gson.Gson;

@RestController
@RequestMapping("/v1/vision")
public class VisionController {

	@Autowired
	private StorageService storage;

	@Autowired
	private VisionService vision;
	
	@Autowired
	private ImageDao imageDao;

	@PostMapping(path="/label", produces={MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<Object> detectLabel(@RequestBody Map<String, String> req) throws IOException {
		return detect(req, "LABEL");
	}

	@PostMapping(path="/face", produces={MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<Object> detectFace(@RequestBody Map<String, String> req) throws IOException {
		return detect(req, "FACE");
	}

	@PostMapping(path="/text", produces={MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<Object> detectText(@RequestBody Map<String, String> req) throws IOException {
		return detect(req, "TEXT");
	}

	@PostMapping(path="/crop-hint", produces={MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<Object> detectCropHint(@RequestBody Map<String, String> req) throws IOException {
		return detect(req, "CROP");
	}

	private ResponseEntity<Object> detect(Map<String, String> req, String type) throws IOException {
		String fileId = req.get("fileId");
		Blob blob = storage.getBlobByFile(fileId);

		try {
			String jsonAnnotate = getJsonAnnotateFromCacheOrGenerate(type, blob);
			return new ResponseEntity<>(jsonAnnotate, HttpStatus.OK);
			
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}

	//TODO refactoring metódo
	private String getJsonAnnotateFromCacheOrGenerate(String type, Blob blob) throws IOException {
		Entity entity = imageDao.findByName(blob.getName());
		
		String objJson;
		String keyName;
		
		switch(type) {
		
			case "LABEL":
				keyName = "jsonLabel";
				
				objJson = entity.getString(keyName);						
				
				if (objJson.trim().isEmpty()) {
					logUsoVision(keyName);
					objJson = new Gson().toJson(vision.detectLabel(blob));
				}
				
				break;
			
			case "TEXT" :
				
				keyName = "jsonText";
				objJson = entity.getString(keyName);
				
				if (objJson.trim().isEmpty()) {
					logUsoVision(keyName);
					objJson = new Gson().toJson(vision.detectText(blob));
				}
				
				break;
			
			case "FACE" : 
				keyName = "jsonFace";
				
				objJson = entity.getString(keyName);
				if (objJson.trim().isEmpty()) {
					logUsoVision(keyName);
					objJson = new Gson().toJson(vision.detectFace(blob));
				}
				
				break;
			
			case "CROP" : 
				keyName = "jsonCropHint";
				objJson = entity.getString(keyName);
				
				if (objJson.trim().isEmpty()) {
					logUsoVision(keyName);
					objJson = new Gson().toJson(vision.detectCropHint(blob));
				}
				
				break;
	
			default: 
				throw new RuntimeException("Tipo " + type + " não é válido");
				
		}
		
		StringValue json = StringValue.newBuilder(objJson).setExcludeFromIndexes(true).build();
		
		imageDao.update(Entity.newBuilder(entity).set(keyName, json).build());		
		return objJson;
	}

	private void logUsoVision(String keyName) {
		System.out.println(keyName + " gerando na api vision");
	}

}
