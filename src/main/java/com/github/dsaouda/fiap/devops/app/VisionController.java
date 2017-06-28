package com.github.dsaouda.fiap.devops.app;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import com.google.cloud.vision.spi.v1.ImageAnnotatorClient;
import com.google.cloud.vision.v1.AnnotateImageRequest;
import com.google.cloud.vision.v1.AnnotateImageResponse;
import com.google.cloud.vision.v1.BatchAnnotateImagesResponse;
import com.google.cloud.vision.v1.Feature;
import com.google.cloud.vision.v1.Feature.Type;
import com.google.cloud.vision.v1.Image;
import com.google.protobuf.ByteString;

@RestController
@RequestMapping("/v1/vision")
public class VisionController {
	
	@PostMapping
	public ResponseEntity<?> create(@RequestBody Map<String, String> req) throws IOException {
		Storage storage = StorageOptions.getDefaultInstance().getService();
		String bucketName = "fiap-devops";
		
		String fileId = req.get("fileId");
		
		BlobId blobId = BlobId.of(bucketName, fileId);		
		Blob blob = storage.get(blobId);

		ImageAnnotatorClient vision = ImageAnnotatorClient.create();

		ByteString imgBytes = ByteString.copyFrom(blob.getContent());

		// Builds the image annotation request
		List<AnnotateImageRequest> requests = new ArrayList<>();
		Image img = Image.newBuilder().setContent(imgBytes).build();


		Feature f1 = Feature.newBuilder().setType(Type.LABEL_DETECTION).build();
		Feature f2 = Feature.newBuilder().setType(Type.FACE_DETECTION).build();
		Feature f3 = Feature.newBuilder().setType(Type.CROP_HINTS).build();
		Feature f4 = Feature.newBuilder().setType(Type.DOCUMENT_TEXT_DETECTION).build();
		Feature f5 = Feature.newBuilder().setType(Type.TEXT_DETECTION).build();
		Feature f6 = Feature.newBuilder().setType(Type.TYPE_UNSPECIFIED).build();		
		Feature f8 = Feature.newBuilder().setType(Type.LANDMARK_DETECTION).build();


		AnnotateImageRequest request = AnnotateImageRequest.newBuilder()
				.addFeatures(f1)
				.addFeatures(f2)
				.addFeatures(f3)
				.addFeatures(f4)
				.addFeatures(f5)
				.addFeatures(f6)				
				.addFeatures(f8)				
				.setImage(img)
				.build();


		requests.add(request);

		BatchAnnotateImagesResponse response = vision.batchAnnotateImages(requests);
		List<AnnotateImageResponse> responses = response.getResponsesList();
		
		List<String> listField = new ArrayList<>();
		for (AnnotateImageResponse res : responses) {

			if (res.hasError()) {
				return new ResponseEntity<String>("Erro com a Vision API", HttpStatus.BAD_REQUEST);
			}
			
			listField.add(res.getAllFields().toString());
		}
		
		return new ResponseEntity<>(listField, HttpStatus.OK);		
	}
	
}
