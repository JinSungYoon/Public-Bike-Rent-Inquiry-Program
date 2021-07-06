package org.jindory.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.activation.MimetypesFileTypeMap;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.jindory.domain.AttachFileDTO;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import lombok.extern.log4j.Log4j;
import net.coobird.thumbnailator.Thumbnailator;

@Controller
@Log4j
public class AttachUploadController {

	@GetMapping("/uploadAjax")
	public void uplaodAjax() {
		log.info("upalod form");
	}
	
	@PostMapping(value="/uploadAjaxAction",produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	public ResponseEntity<List<AttachFileDTO>> uplaodAjaxPost(MultipartFile[] uploadFile,Model model) {
		
		log.info("update ajax post..............");
		List<AttachFileDTO> list = new ArrayList<>();
		String uploadFolder = "D:\\Programming\\Java\\workspace\\publicDataPortal\\upload";
		
		String uploadFolderPath = getFolder();
		// Make folder
		File uploadPath = new File(uploadFolder,uploadFolderPath);// parent 폴더 경로의 child라는 파일에 대한 File 객체를 생성한다.
		log.info("upload path : "+uploadPath);
		
		if(uploadPath.exists() == false) {
			uploadPath.mkdirs();
		}
		
		for(MultipartFile multipartFile : uploadFile) {
			
			AttachFileDTO attachDTO = new AttachFileDTO();
			
			log.info("-------------------------------------------------------");
			log.info("Upload file Name " +multipartFile.getOriginalFilename());
			log.info("Upload File Size " +multipartFile.getSize());
			
			String uploadFileName = multipartFile.getOriginalFilename();
			// IE has file path
			uploadFileName = uploadFileName.substring(uploadFileName.lastIndexOf("\\")+1);
			attachDTO.setFileName(uploadFileName);
			log.info("only file name : " + uploadFileName);
			
			UUID uuid = UUID.randomUUID();
			
			uploadFileName = uuid.toString() + "_" + uploadFileName;
			
			try {
				File saveFile = new File(uploadPath,uploadFileName);
				multipartFile.transferTo(saveFile);
				
				attachDTO.setUuid(uuid.toString());
				attachDTO.setUploadPath(uploadFolderPath);
				
				// check image type file
				if(checkImageType(saveFile)) {
					attachDTO.setImage(true);
					FileOutputStream thumbnail = new FileOutputStream(new File(uploadPath,"s_"+uploadFileName));
					Thumbnailator.createThumbnail(multipartFile.getInputStream(),thumbnail,100,100);
					thumbnail.close();
				}

				// add to List
				list.add(attachDTO);
				
			}catch(Exception e) {
				e.printStackTrace();
			}
			
		}
		
		return new ResponseEntity<>(list,HttpStatus.OK);
	}
	
	@GetMapping("/display")
	@ResponseBody
	public ResponseEntity<byte[]> getFile(String fileName){
		log.info("search target fileName : "+fileName);
		
		File file = new File("D:\\Programming\\Java\\workspace\\publicDataPortal\\upload\\"+fileName);
		log.info("search target file : "+file);
		
		ResponseEntity<byte[]> result = null;
		
		try {
			HttpHeaders header = new HttpHeaders();
			// 파일에 해당하는 MIME타입을 Header에 넣어준다.
			if(file.exists()){
				header.add("Content-Type",Files.probeContentType(file.toPath()));
				result = new ResponseEntity<>(FileCopyUtils.copyToByteArray(file),header,HttpStatus.OK);
			}
		}catch(IOException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	@GetMapping(value="/downloadFile",produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
	@ResponseBody
	public ResponseEntity<Resource> downloadFile(@RequestHeader("User-Agent") String userAgent,String fileName){
		
		log.info("download file : " + fileName);
		Resource resource = new FileSystemResource("D:\\Programming\\Java\\workspace\\publicDataPortal\\upload\\"+fileName);
		log.info("resource : "+resource);
		
		if(resource.exists()==false) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		
		String resourceName = resource.getFilename();
		
		// remove UUID
		String resourceOriginalName = resourceName.substring(resourceName.indexOf("_")+1); 
		HttpHeaders headers = new HttpHeaders();
		try {
			
			String downloadName = null;
			
			if(userAgent.contains("trident")) {
				log.info("IE browser");
				downloadName = URLEncoder.encode(resourceOriginalName,"UTF-8").replaceAll("\\+"," ");
			}else if(userAgent.contains("Edge")) {
				log.info("Edge browser");
				downloadName = URLEncoder.encode(resourceOriginalName,"UTF-8");
				log.info("Edge name : "+downloadName);
			}else {
				log.info("Chrome browser");
				downloadName = new String(resourceOriginalName.getBytes("UTF-8"),"ISO-8859-1");
			}
			
			log.info("Download Filename : "+downloadName);
			
			headers.add("Content-disposition","attachment;filename="+new String(resourceOriginalName.getBytes("UTF-8"),"ISO-8859-1"));
			
		}catch(UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return new ResponseEntity<Resource>(resource,headers,HttpStatus.OK);
	}
	
	@GetMapping(value="/downloadFileAll",produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
	@ResponseBody
	public void downloadFileAll(@RequestHeader("User-Agent") String userAgent,@RequestParam(value="fileList[]") List<String> fileList,HttpServletResponse response) throws Exception{
				
		String filePath;
		
		for(String fileName : fileList) {
			
			log.info("download file : " + fileName);
			Resource resource = new FileSystemResource("D:\\Programming\\Java\\workspace\\publicDataPortal\\upload\\"+fileName);
			filePath = "D:\\Programming\\Java\\workspace\\publicDataPortal\\upload\\"+fileName;
			log.info("resource : "+resource);
			if(resource.exists()==false) {
				//return new ResponseEntity<>(HttpStatus.NOT_FOUND);
				return;
			}
			
			String resourceName = resource.getFilename();
			HttpHeaders headers = new HttpHeaders();
			// remove UUID
			String resourceOriginalName = resourceName.substring(resourceName.indexOf("_")+1); 
			

			File file = new File(filePath);
			
			String mimeType = Files.probeContentType(file.toPath());
			
			if(mimeType == null) {
				response.setContentType("application/octet-stream");
			}
			
			try {
				String downloadName = null;
				if(userAgent.contains("trident")) {
					log.info("IE browser");
					downloadName = URLEncoder.encode(resourceOriginalName,"UTF-8").replaceAll("\\+"," ");
				}else if(userAgent.contains("Edge")) {
					log.info("Edge browser");
					downloadName = URLEncoder.encode(resourceOriginalName,"UTF-8");
					log.info("Edge name : "+downloadName);
				}else {
					log.info("Chrome browser");
					downloadName = new String(resourceOriginalName.getBytes("UTF-8"),"ISO-8859-1");
				}
				
				log.info("Download Filename : "+downloadName);
				
				headers.add("Content-disposition","attachment;filename="+new String(resourceOriginalName.getBytes("UTF-8"),"ISO-8859-1"));

				response.setContentLength((int)file.length());
				response.setHeader("Content-Disposition","attachment;filename="+new String(resourceOriginalName.getBytes("UTF-8"),"ISO-8859-1"));

				OutputStream out = response.getOutputStream();
				FileInputStream fileInputStream = new FileInputStream(file);
				ServletOutputStream servletOutputStream = response.getOutputStream();
				
				byte b[] = new byte[1024];
				int data = 0;
				
				while((data=(fileInputStream.read(b,0,b.length)))!=-1) {
					servletOutputStream.write(b,0,data);
				}
				
				servletOutputStream.flush();
				servletOutputStream.close();
				fileInputStream.close();
				
			}catch(UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		
		//return new ResponseEntity<Resource>(resource,headers,HttpStatus.OK);
	}
	
	@PostMapping("/deleteFile")
	@ResponseBody
	public ResponseEntity<String> deleteFile(String fileName,String type,String fileIndex){
		log.info("deleteFile : "+fileName);
		File file;
		try {
			
			file = new File("D:\\Programming\\Java\\workspace\\publicDataPortal\\upload\\"+URLDecoder.decode(fileName,"UTF-8"));
			file.delete();
			
			if(type.equals("image")) {
				String largeFileName = file.getAbsolutePath().replace("s_","");
				log.info("largeFileName : "+largeFileName);
				file = new File(largeFileName);
				file.delete();
			}
			
		}catch(UnsupportedEncodingException e){
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		
		return new ResponseEntity<String>("Successfully deleted File : "+fileIndex,HttpStatus.OK);
		
	}
	
	private String getFolder() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date date = new Date();
		String str = sdf.format(date);
		return str.replace("-", File.separator);
	}
	
	
	private boolean checkImageType(File file) {
		try {
			String contentType = Files.probeContentType(file.toPath());
			return contentType.startsWith("image");
		}catch(IOException e) {
			e.printStackTrace();
		}
		return false;
	}
}
