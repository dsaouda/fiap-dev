package com.github.dsaouda.fiap.devops.model;

import com.google.cloud.Timestamp;

public class Image {

	private String name;
	private Timestamp created = Timestamp.now();
	private Timestamp updated = Timestamp.now();
	private String jsonLabel = "";
	private String jsonFace = "";
	private String jsonCropHint = "";
	private String jsonText = "";
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public Timestamp getCreated() {
		return created;
	}
	
	public void setCreated(Timestamp created) {
		this.created = created;
	}
	
	public Timestamp getUpdated() {
		return updated;
	}
	
	public void setUpdated(Timestamp updated) {
		this.updated = updated;
	}
	
	public String getJsonLabel() {
		return jsonLabel;
	}
	
	public void setJsonLabel(String jsonLabel) {
		this.jsonLabel = jsonLabel;
	}
	
	public String getJsonFace() {
		return jsonFace;
	}
	
	public void setJsonFace(String jsonFace) {
		this.jsonFace = jsonFace;
	}
	
	public String getJsonCropHint() {
		return jsonCropHint;
	}
	
	public void setJsonCropHint(String jsonCropHint) {
		this.jsonCropHint = jsonCropHint;
	}
	
	public String getJsonText() {
		return jsonText;
	}
	
	public void setJsonText(String jsonText) {
		this.jsonText = jsonText;
	}	
}
