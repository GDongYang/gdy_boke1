package com.gdy.boke;

import com.gdy.boke.service.Kaptcha;
import com.gdy.boke.service.impl.GoogleKaptcha;
import com.google.code.kaptcha.impl.DefaultKaptcha;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class BokeApplication {

	public static void main(String[] args) {
		SpringApplication.run(BokeApplication.class, args);
	}



}
