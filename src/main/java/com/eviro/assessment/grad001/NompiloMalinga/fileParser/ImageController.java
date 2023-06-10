package com.eviro.assessment.grad001.NompiloMalinga.fileParser;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("/v1/api/image")
public class ImageController {

    private final AccountProfileEntityRepo accountProfileEntityRepo;

    private final FileParserImplementation fileParserImplementation;
    public ImageController(FileParserImplementation fileParserImplementation,AccountProfileEntityRepo accountProfileEntityRepo){
        this.fileParserImplementation=fileParserImplementation;
        this.accountProfileEntityRepo=accountProfileEntityRepo;
    }


    @PostConstruct
    public void init() {
        String csvFilePath = "src/main/resources/1672815113084-GraduateDev_AssessmentCsv_Ref003.csv";
        Path path = Paths.get(csvFilePath);
        File csvFile = path.toFile();
        fileParserImplementation.parseCSV(csvFile);
    }

    @GetMapping(value = "/{name}/{surname}/{FileNameFormat}", produces = { MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_PNG_VALUE })
    public FileSystemResource gethttpImageLink(@PathVariable String name, @PathVariable String surname) throws FileNotFoundException, URISyntaxException {
        //Retrieving the account profile entity based on the provided name and surname
        AccountProfileEntity accountProfileEntity = accountProfileEntityRepo.findByNameAndSurname(name, surname);
        if (accountProfileEntity != null) {
            // Get the httpImageLink from the account profile entity
            String httpImageLink = accountProfileEntity.getHttpImageLink();

            //Creating a Path object that represents the location of the image file in the file system.
            //Path filePath = Paths.get(httpImageLink);
            URI uri = new URI(httpImageLink);
            String actualPath = uri.getPath();
            // Create a FileSystemResource based on the Path object
            //allowing me to work with the file represented by filePath
            FileSystemResource fileResource = new FileSystemResource(actualPath);

            if (fileResource.exists()) {
                return fileResource;
            }
        }
        // If the file doesn't exist
        throw new FileNotFoundException("File not found ");

    }
}