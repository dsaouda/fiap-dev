package com.github.dsaouda.fiap.devops.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.cloud.storage.Blob;
import com.google.cloud.vision.spi.v1.ImageAnnotatorClient;
import com.google.cloud.vision.v1.AnnotateImageRequest;
import com.google.cloud.vision.v1.AnnotateImageResponse;
import com.google.cloud.vision.v1.BatchAnnotateImagesResponse;
import com.google.cloud.vision.v1.CropHintsAnnotation;
import com.google.cloud.vision.v1.EntityAnnotation;
import com.google.cloud.vision.v1.FaceAnnotation;
import com.google.cloud.vision.v1.Feature;
import com.google.cloud.vision.v1.Feature.Type;
import com.google.cloud.vision.v1.Image;
import com.google.protobuf.ByteString;

@Service
public class VisionService {

	private ImageAnnotatorClient vision;
		
	@Autowired
	public VisionService(ImageAnnotatorClient vision) {
		this.vision = vision;
	}
	
	public List<EntityAnnotation> detectLabel(Blob blob) {
		Feature feature = Feature.newBuilder().setType(Type.LABEL_DETECTION).build();
		List<AnnotateImageResponse> responses = detect(blob, feature);
		
		AnnotateImageResponse response = responses.iterator().next();
		return response.getLabelAnnotationsList();
	}
	
	public List<FaceAnnotation> detectFace(Blob blob) {
		Feature feature = Feature.newBuilder().setType(Type.FACE_DETECTION).build();
		List<AnnotateImageResponse> responses = detect(blob, feature);
		
		AnnotateImageResponse response = responses.iterator().next();
		return response.getFaceAnnotationsList();
	}
	
	public List<EntityAnnotation> detectText(Blob blob) {
		Feature feature = Feature.newBuilder().setType(Type.TEXT_DETECTION).build();
		List<AnnotateImageResponse> responses = detect(blob, feature);
		
		AnnotateImageResponse response = responses.iterator().next();
		return response.getTextAnnotationsList();
	}
	
	public CropHintsAnnotation detectCropHint(Blob blob) {
		Feature feature = Feature.newBuilder().setType(Type.CROP_HINTS).build();
		List<AnnotateImageResponse> responses = detect(blob, feature);
		
		AnnotateImageResponse response = responses.iterator().next();
		return response.getCropHintsAnnotation();
	}
	
	//	Feature feature = Feature.newBuilder().setType(Type.LABEL_DETECTION).build();
	//	Feature f2 = Feature.newBuilder().setType(Type.FACE_DETECTION).build();
	//	Feature f3 = Feature.newBuilder().setType(Type.CROP_HINTS).build();
	//	Feature f4 = Feature.newBuilder().setType(Type.DOCUMENT_TEXT_DETECTION).build();
	//	Feature f5 = Feature.newBuilder().setType(Type.TEXT_DETECTION).build();
	//	Feature f6 = Feature.newBuilder().setType(Type.TYPE_UNSPECIFIED).build();		
	//	Feature f8 = Feature.newBuilder().setType(Type.LANDMARK_DETECTION).build();
	public List<AnnotateImageResponse> detect(Blob blob, Feature feature) {
		ByteString imgBytes = ByteString.copyFrom(blob.getContent());
		List<AnnotateImageRequest> requests = new ArrayList<>();
		Image img = Image.newBuilder().setContent(imgBytes).build();
		

		AnnotateImageRequest request = AnnotateImageRequest.newBuilder()
				.addFeatures(feature)		
				.setImage(img)
				.build();

		requests.add(request);

		BatchAnnotateImagesResponse response = vision.batchAnnotateImages(requests);
		return response.getResponsesList();
	}
}
