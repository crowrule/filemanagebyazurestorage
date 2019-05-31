package com.browndwarf.filemanagebyazurestorage.config;

import java.net.URISyntaxException;
import java.security.InvalidKeyException;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.microsoft.azure.storage.CloudStorageAccount;
import com.microsoft.azure.storage.blob.CloudBlobClient;

//import com.microsoft.azure.storage.blob.CloudBlobContainer;

@Configuration
public class AzureStorageConfig {
	
	private final String  storageConnectionString = "DefaultEndpointsProtocol=https;" +
			"AccountName=browndwarfstorage;" +
			"AccountKey=F1Op1OmYuz5KbsCu2egQOPpdY3+kRaDyBDt28eonfe17jlj9UCL/YeN+tL+s8nd+k3p+c8leDnHY6hXLkk6EPg==";
	

	
	@Bean
	public CloudStorageAccount cloudStorageAccount() throws InvalidKeyException, URISyntaxException {
		return CloudStorageAccount.parse(storageConnectionString);
	}
	
	@Bean
	public CloudBlobClient cloudBlobClient() throws InvalidKeyException, URISyntaxException  {

        CloudBlobClient cloudBlobClient;

        cloudBlobClient = cloudStorageAccount().createCloudBlobClient();
        
        return cloudBlobClient;
	}

}
