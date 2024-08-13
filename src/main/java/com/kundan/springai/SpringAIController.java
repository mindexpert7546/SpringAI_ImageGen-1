package com.kundan.springai;

import java.awt.Image;
import java.util.Base64;

import org.springframework.ai.image.ImageModel;
import org.springframework.ai.image.ImagePrompt;
import org.springframework.ai.image.ImageResponse;
import org.springframework.ai.stabilityai.api.StabilityAiImageOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SpringAIController {
	
	@Autowired
	private ImageModel openaiImageModel;
	
	@GetMapping("/springai")
	public ResponseEntity<byte[]> getImage(@RequestParam(defaultValue="")  String prompt){
		
		//If the Prompt is null then the return the default error image 
		if(prompt.isEmpty()) {
			// Decode Base64 string
			String base64Default = "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";
			 byte[] imageBytes = Base64.getDecoder().decode(base64Default);
			 
			 // Set headers and return the image
			 HttpHeaders headers = new HttpHeaders();
		     headers.setContentType(MediaType.IMAGE_PNG); 
		     return new ResponseEntity<>(imageBytes, headers, HttpStatus.OK);
		}
		
		//if the prompt is not null 
		ImageResponse response = openaiImageModel.call(
		        new ImagePrompt(prompt ,
		        StabilityAiImageOptions.builder()
		                .withStylePreset("cinematic")
		                .withN(4)
		                .withHeight(1024)
		                .withWidth(1024).build())

		);

		        // Decode Base64 string
		        String base64Image = response.getResult().getOutput().getB64Json();
		        byte[] imageBytes = Base64.getDecoder().decode(base64Image);

		        // Set headers and return the image
		        HttpHeaders headers = new HttpHeaders();
		        headers.setContentType(MediaType.IMAGE_PNG); // Adjust the media type if necessary
		        return new ResponseEntity<>(imageBytes, headers, HttpStatus.OK);
	}
}
