package com.browndwarf.filemanagebyazurestorage.contoller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.browndwarf.filemanagebyazurestorage.service.FileManageService;

import lombok.extern.slf4j.Slf4j;


@Controller
@Slf4j
public class AzureController {
	
	@Autowired
	FileManageService	fileManageService;
	
    @GetMapping("/")
    public String index() {
        return "index";
    }

    
    @PostMapping("/upload")
    //@ResponseBody
    public String upload(@RequestParam("uploadFile") MultipartFile uploadFile) {
    	
    	log.info("upload... {}", uploadFile.getOriginalFilename());
    	
        if (uploadFile.getOriginalFilename().equals(fileManageService.upload(uploadFile)))
        		return "redirect:/list";
        else
        		return "index";
    }
    
    
    @GetMapping("/list")
    public String fileList(Model model) throws Exception {
    	
    	model.addAttribute("fileList", fileManageService.getFileList());
    	
		return "fileList";
	}
    
	@GetMapping(value = "image/{imageName}")
	public ResponseEntity<InputStreamResource> downloadFile1(
			@PathVariable(value = "imageName") String imageName) throws IOException {
 
        File receivedFile = fileManageService.download(imageName);
        InputStreamResource resource = new InputStreamResource(new FileInputStream(receivedFile));
 
        return ResponseEntity.ok()
                // Content-Disposition
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + receivedFile.getName())
                // Content-Type
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                // Contet-Length
                .contentLength(receivedFile.length()) //
                .body(resource);
    }
}
