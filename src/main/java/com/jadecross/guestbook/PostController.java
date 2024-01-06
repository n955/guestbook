package com.jadecross.guestbook;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class PostController {
	
	@Autowired
	private PostService postService;
	
	private int requestCount = 0; // error 및 rollback 시뮬레이션용 - 요청이 5회 이상일때 오류 발생 재연에 사용

	@GetMapping("/")
	public String index(Model model) {
		// 01. 방명록 조회
		model.addAttribute("posts", postService.getAll());
		model.addAttribute("host", new Host());
		
//		// 5회 이상의 요청일 경우 error 페이지로 이동
//		// 잘못된 버전 배포 후 롤백 데모시 주석 해제할 것!!!
//		requestCount++;
//	    if(requestCount > 5) {
//	    	model.addAttribute("health", "UnHealthy");
//	    	return "error";
//	    }
	    
		// 02. 의미없이 CPU 사용량 증가
		this.useCPU(1000);
		
		// 03. 1~5초 사이로 랜덤하게 응답지연
		try {
			Thread.sleep(randomRange(1,5) * 1000);
		} catch (InterruptedException e) {}
		
		return "index";
	}

	@PostMapping("/")
	public String insertPost(@ModelAttribute Post post, Model model) {
		// 01-1. 파일첨부
		if(post.getUploadingFile().getOriginalFilename().equals("") == false) {
			String uploadedFile = this.uploadFile(post.getUploadingFile(), post.getName());
			post.setAttachedFile(uploadedFile);
		}
		
		// 01-2. DB에 저장
		post.setWriteDate((new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(new Date()));
		postService.add(post);
		
		System.out.println("===> 방명록 저장");
		System.out.println(post);

		// 02. 방명록 조회
		model.addAttribute("posts", postService.getAll());
		model.addAttribute("host", new Host());

		return "index";
	}
	
	@GetMapping("/healthcheck")
	public String healthCheck(Model model) {

		// 01. 방명록 조회
		model.addAttribute("posts", postService.getAll());
		model.addAttribute("host", new Host());
		
		// 5회 이상의 요청일 경우 500 Internal Server Error 발생
		requestCount++;
	    if(requestCount > 5) throw new RuntimeException();

	    return "index";
	}
	
	@GetMapping("/downloadFile/{fileName:.+}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileName, HttpServletRequest request) throws Exception {
		//
		File downloadFile = new File(System.getProperty("user.dir") + "/upload/" + fileName);
		Resource resource = new UrlResource(downloadFile.toURI());
		String contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());

		if(contentType == null) contentType = "text/plain";
		
		return ResponseEntity.ok()
				.contentType(MediaType.parseMediaType(contentType))
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
				.body(resource);
    }
	
	/**
	 * 현재 Working Directory/upload 폴더에 파일을 첨부
	 * @param file
	 * @param uploaderName
	 * @return 업로드한 파일명
	 */
	private String uploadFile(MultipartFile file, String uploaderName) {
		//
		String currentWorkingDirectory = System.getProperty("user.dir");
		String originalFilename = file.getOriginalFilename(); // 클라이언트 시스템의 FullPath 포함한 파일명
		String uploadFileName = this.getCurrentTimeMillisFormat() + "_" + uploaderName + "_" + FilenameUtils.getName(originalFilename);
		File uploadFile = new File( currentWorkingDirectory + "/upload/" + uploadFileName);
		
		try {
			uploadFile.getParentFile().mkdirs(); // upload 디렉토리가 없으면 생성
			file.transferTo(uploadFile);
		} catch (Exception ex) { } 
		
		return uploadFileName;
	}
	
	/**
	 * 현재시간을 "년월일시분초밀리초" 로 반환
	 * @return
	 */
	private String getCurrentTimeMillisFormat() {
		long currentTime = System.currentTimeMillis(); 
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmssSSS"); 
		return dateFormat.format(new Date(currentTime)); 
	}
	
	/**
	 * CPU 사용률을 높이기 위해 LOOP를 돌면서 산술연산(double 연산) 수행
	 * @param loopCount
	 * @return
	 */
	private double useCPU(int loopCount) {
		double result = 0;
		for (int i = 1; i < loopCount; i++) {
			result = i * Math.random() / loopCount;
			for (int j = 1; j < loopCount; j++) {
				result = i * j * Math.random() / loopCount;
				for (int k = 1; k < loopCount; k++) {
					// CPU 만 소모
				}
			}
		}
		return result;
	}
	
	/**
	 * 지정된 범위의 정수 1개를 램덤하게 반환
	 * @param n1 - 하한값
	 * @param n2 - 상한값
	 * @return
	 */
	private int randomRange(int n1, int n2) {
		return (int) (Math.random() * (n2 - n1 + 1)) + n1;
	}
}