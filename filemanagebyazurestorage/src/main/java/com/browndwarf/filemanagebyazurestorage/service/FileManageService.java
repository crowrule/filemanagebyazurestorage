package com.browndwarf.filemanagebyazurestorage.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.microsoft.azure.storage.OperationContext;
import com.microsoft.azure.storage.StorageException;
import com.microsoft.azure.storage.blob.BlobContainerPublicAccessType;
import com.microsoft.azure.storage.blob.BlobRequestOptions;
import com.microsoft.azure.storage.blob.CloudBlobClient;
import com.microsoft.azure.storage.blob.CloudBlobContainer;
import com.microsoft.azure.storage.blob.CloudBlockBlob;
import com.microsoft.azure.storage.blob.ListBlobItem;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class FileManageService {
	
	private final String containerName = "browndwarfstorageblob";
	
	@Autowired
	CloudBlobClient	cloudBlobClient;
	
    public String upload(MultipartFile multipartFile) {

		log.info("Upload file {} to {}", multipartFile.getOriginalFilename(), containerName);
		
    	File receivedFile = new File(multipartFile.getOriginalFilename());
    	
    	try {
    		receivedFile.createNewFile();
    		FileOutputStream	fos = new FileOutputStream(receivedFile);
    		fos.write(multipartFile.getBytes());
    		fos.close();
		} catch (IllegalStateException | IOException e) {
			// TODO Auto-generated catch block

    		e.printStackTrace();
    		
    		return "-1";
		}
    	
    	try {
        	CloudBlobContainer container = cloudBlobClient.getContainerReference(containerName);

    		// Create the container if it does not exist with public access.
    		container.createIfNotExists(BlobContainerPublicAccessType.CONTAINER, new BlobRequestOptions(), new OperationContext());		    

    		//Getting a blob reference & Creating blob and uploading file to it
    		CloudBlockBlob blob = container.getBlockBlobReference(multipartFile.getOriginalFilename());
    		blob.uploadFromFile(receivedFile.getAbsolutePath());    		
    	}catch(IOException | StorageException | URISyntaxException e) {
    		e.printStackTrace();
    		
    		return "-1";   		
    	} 

    	
    	receivedFile.delete();
    	
    	return multipartFile.getOriginalFilename();
    }
    
    
    public List<String> getFileList() {
    	List<String> fileList = new ArrayList<String>();
    	
    	try {
    		CloudBlobContainer container = cloudBlobClient.getContainerReference(containerName);
        	
        	for (ListBlobItem blobItem : container.listBlobs()) {
        		fileList.add(FilenameUtils.getName(blobItem.getUri().getPath()));
        	}
        	
    	}catch(Exception e) {
    		e.printStackTrace();

    	}
    	
    	return fileList;
    }

    public File download(String objectName) {
    	File downloadFile = new File(objectName); 

        try {
        	CloudBlobContainer container = cloudBlobClient.getContainerReference(containerName);
            CloudBlockBlob blob = container.getBlockBlobReference(objectName);
            blob.download(new FileOutputStream(downloadFile));

		} catch (IOException | URISyntaxException | StorageException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
			return null;
		}
    	
    	return downloadFile;
    }
}
