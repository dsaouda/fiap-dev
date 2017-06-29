package com.github.dsaouda.fiap.devops.app;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.github.dsaouda.fiap.devops.dao.ImageDao;
import com.github.dsaouda.fiap.devops.model.Image;
import com.github.dsaouda.fiap.devops.service.StorageService;
import com.google.cloud.datastore.Entity;
import com.google.cloud.storage.Blob;

@RestController
@RequestMapping(path="/images")
public class ImageController {
	
	@Autowired
	private StorageService service;
	
	@Autowired
	private ImageDao imageDao;
	
	@GetMapping(path="view")
	public void show(@RequestParam("file") String file, HttpServletResponse response) throws IOException {
		Blob blob = service.getBlobByFile(file);
		
		if (blob == null) {
			response.setStatus(HttpStatus.NOT_FOUND.value());
			return ;
		}
		response.setContentType(blob.getContentType());
		IOUtils.copy(new ByteArrayInputStream(blob.getContent()), response.getOutputStream());
	}
	
	
	@GetMapping(path="list")
	public List<Image> list() {
		return imageDao.findAll();
	}
	
	@GetMapping(path="detail", produces={MediaType.APPLICATION_JSON_VALUE})
	public Image detail(@RequestParam("file") String file) {
		Entity entity = imageDao.findByName(file);
		
		Image image = new Image();
		image.setCreated(entity.getTimestamp("created"));
		image.setJsonCropHint(entity.getString("jsonCropHint"));
		image.setJsonFace(entity.getString("jsonFace"));
		image.setJsonLabel(entity.getString("jsonLabel"));
		image.setJsonText(entity.getString("jsonText"));
		image.setName(entity.getString("name"));
		image.setUpdated(entity.getTimestamp("updated"));
		
		return image;
	}
}
