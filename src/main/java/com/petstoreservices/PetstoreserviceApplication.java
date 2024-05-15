package com.petstoreservices;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@SpringBootApplication
@RestController
public class PetstoreserviceApplication {

	public static void main(String[] args){

		try (Stream<Path> stream = Files.list(Paths.get("./datastore/original"))) {
			List<Path> paths = stream.filter(path -> path.toString().endsWith(".json")).collect(Collectors.toList());
			for (Path source : paths) {
				Path destination = Paths.get("./datastore/application" + File.separator + "petstore.json");
				Files.copy(source, destination, StandardCopyOption.REPLACE_EXISTING);
			}
			//stream.collect(Collectors.toList());
		}catch (IOException e)
		{
			e.printStackTrace();

		}catch(Exception e)
		{
			e.printStackTrace();
		}

		SpringApplication.run(PetstoreserviceApplication.class, args);

	}

	@GetMapping("/hello")
	public String sayHello(@RequestParam(value = "myName", defaultValue = "World") String name) {
		return String.format("Hello %s!", name);
	}

}
