package com.redhat.developers;

public class Repository {
	
	private String fullName;
	private String gitUrl;
	private boolean isPrivate;
	private String createdAt;
	private String updatedAt;
	private String pushedAt;
	
	public String getFullName() {
		return fullName;
	}
	
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
	
	public String getGitUrl() {
		return gitUrl;
	}
	
	public void setGitUrl(String htmlUrl) {
		this.gitUrl = htmlUrl;
	}
	
	public boolean isPrivate() {
		return isPrivate;
	}
	
	public void setPrivate(boolean isPrivate) {
		this.isPrivate = isPrivate;
	}
	
	public String getCreatedAt() {
		return createdAt;
	}
	
	public void setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
	}
	
	public String getUpdatedAt() {
		return updatedAt;
	}
	
	public void setUpdatedAt(String updatedAt) {
		this.updatedAt = updatedAt;
	}
	
	public String getPushedAt() {
		return pushedAt;
	}
	
	public void setPushedAt(String pushedAt) {
		this.pushedAt = pushedAt;
	}
}
