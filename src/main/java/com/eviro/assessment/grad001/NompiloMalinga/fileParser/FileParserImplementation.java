package com.eviro.assessment.grad001.NompiloMalinga.fileParser;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.*;
import java.net.URI;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;

@Service
public class FileParserImplementation implements FileParser{

    @Autowired
    private AccountProfileEntityRepo accountProfileEntityRepo;

    private  String imageFormat;
    @Override
    public void parseCSV(File csvFile) {


        try {
            BufferedReader reader = new BufferedReader(new FileReader(csvFile));
            //skipping the header row
            String header = reader.readLine();
            //reading lines
            String line;
            while ((line= reader.readLine())!= null){
                //splitting lines into columns using a comma
                String []columns = line.split(",");
                //accessing columns
                String name = columns[0];
                String surname = columns[1];
                imageFormat = columns[2];
                String httpImageLink = columns[3];

               File file = convertCSVDataToImage(httpImageLink);

                AccountProfileEntity accountProfileEntity = new AccountProfileEntity();
                accountProfileEntity.setName(name);
                accountProfileEntity.setSurname(surname);
                accountProfileEntity.setHttpImageLink(createImageLink(file).toString());

                accountProfileEntityRepo.save(accountProfileEntity);
            }
            reader.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public File convertCSVDataToImage(String base64ImageData) {

        try {
            // Decode base64 image data
            byte[] imageBytes = Base64.getDecoder().decode(base64ImageData);

            // Splitting the file data into file name and format eg  image/png
            String[] fileDataParts = imageFormat.split("/");

            //Extracting the file name and format
            String fileName = fileDataParts[0];
            String fileFormat = fileDataParts[1];

            //Defining the directory where i want to store the files
            String directory = "src/main/resources/images";

            // Create the directory if it doesn't exist
            File directoryFile = new File(directory);
            if (!directoryFile.exists()) {
                directoryFile.mkdirs();
            }

            //Creating a new file with the directory, extracted file name, and file format
            File imageFile = new File(directory + "/" + fileName + "." + fileFormat);

            //Writing the image data to the file
            FileOutputStream fileOutputStream = new FileOutputStream(imageFile);
            fileOutputStream.write(imageBytes);
            fileOutputStream.close();

            return imageFile;
        } catch (IOException e) {
            throw new RuntimeException("Error converting base64 image data to file: " + e.getMessage());
        }
    }

    @Override
    public URI createImageLink(File fileImage) {
        if (fileImage != null && fileImage.exists()) {
            Path filePath = Paths.get(fileImage.getPath());
            return filePath.toUri();
        }
        return null;

    }
}
