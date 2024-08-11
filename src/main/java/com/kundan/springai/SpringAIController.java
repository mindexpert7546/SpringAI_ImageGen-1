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
	public ResponseEntity<byte[]> getImage(@RequestParam  String prompt){
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
