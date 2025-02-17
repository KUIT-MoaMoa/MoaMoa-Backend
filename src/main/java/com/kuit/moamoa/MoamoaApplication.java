package com.kuit.moamoa;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication
public class MoamoaApplication {

	public static void main(String[] args) {
		SpringApplication.run(MoamoaApplication.class, args);
	}

}
