package com.github.dsaouda.fiap.devops;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import com.google.cloud.vision.spi.v1.ImageAnnotatorClient;
import com.google.cloud.vision.v1.AnnotateImageRequest;
import com.google.cloud.vision.v1.AnnotateImageResponse;
import com.google.cloud.vision.v1.BatchAnnotateImagesResponse;
import com.google.cloud.vision.v1.Feature;
import com.google.cloud.vision.v1.Image;
import com.google.cloud.vision.v1.Feature.Type;
import com.google.protobuf.ByteString;

public class Vision {
	public static void main(String[] args) throws IOException {

		// Instantiates a client
		ImageAnnotatorClient vision = ImageAnnotatorClient.create();

		// The path to the image file to annotate
		String fileName = "data/2-image.jpg";

		// Reads the image file into memory
		Path path = Paths.get(fileName);
		byte[] data = Files.readAllBytes(path);
		ByteString imgBytes = ByteString.copyFrom(data);

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

		// Performs label detection on the image file
		BatchAnnotateImagesResponse response = vision.batchAnnotateImages(requests);
		List<AnnotateImageResponse> responses = response.getResponsesList();

		for (AnnotateImageResponse res : responses) {

			if (res.hasError()) {
				System.out.printf("Error: %s\n", res.getError().getMessage());
				return;
			}

			//				System.out.println("==================================");
			//				System.out.println("getFaceAnnotationsList");
			//				res.getFaceAnnotationsList().stream().forEach(a -> analisar(a));
			//
			//				System.out.println("==================================");
			//				System.out.println("getLabelAnnotationsList");
			//				res.getLabelAnnotationsList().stream().forEach(a -> analisar(a));
			//				
			//				System.out.println("==================================");
			//				System.out.println("getCropHintsAnnotation");
			//				analisar(res.getCropHintsAnnotation());
			//				
			//				System.out.println("==================================");
			//				System.out.println("getTextAnnotationsList");
			//				res.getTextAnnotationsList().stream().forEach(a -> analisar(a));
			//				
			//				System.out.println("==================================");
			//				System.out.println("getTextAnnotationsList");
			//				analisar(res.getFullTextAnnotation());
			//				
			//				System.out.println("==================================");
			//				System.out.println("getImagePropertiesAnnotation");
			//				analisar(res.getImagePropertiesAnnotation());
			//				
			//				System.out.println("==================================");
			//				System.out.println("getLandmarkAnnotationsList");
			//				res.getLandmarkAnnotationsList().stream().forEach(a -> analisar(a));

			//				System.out.println("==================================");
			//				System.out.println("getAllFields");
			res.getAllFields().forEach((k,v)-> System.out.printf("%s=%s\n", k, v.toString()));
		}
	}
}
