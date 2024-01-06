package com.jadecross.guestbook;

import org.springframework.web.multipart.MultipartFile;

public class Post {
	
	private String name;
	private String writeDate;
	private String content;
	private MultipartFile uploadingFile;
	private String attachedFile;

	public Post() {
	}

	public Post(String name, String writeDate, String content) {
		this.name = name;
		this.writeDate = writeDate;
		this.content = content;
	}
	
	public Post(String name, String writeDate, String content, MultipartFile uploadingFile) {
		this.name = name;
		this.writeDate = writeDate;
		this.content = content;
		this.uploadingFile = uploadingFile;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getWriteDate() {
		return writeDate;
	}

	public void setWriteDate(String writeDate) {
		this.writeDate = writeDate;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}


	public MultipartFile getUploadingFile() {
		return uploadingFile;
	}

	public void setUploadingFile(MultipartFile uploadingFile) {
		this.uploadingFile = uploadingFile;
	}

	public String getAttachedFile() {
		return attachedFile;
	}

	public void setAttachedFile(String attachedFile) {
		this.attachedFile = attachedFile;
	}

	@Override
	public String toString() {
		StringBuffer sb =  new StringBuffer();
		sb.append("작성자=" + this.name + "\n");
		sb.append("작성일시=" + this.writeDate + "\n");
		sb.append("내용=" + this.content + "\n");
		sb.append("첨부파일=" + this.attachedFile + "\n");
		sb.append("\n");
		return sb.toString();
	}
	
}
