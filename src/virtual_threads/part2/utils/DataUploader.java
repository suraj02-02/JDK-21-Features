package virtual_threads.part2.utils;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.Duration;

public class DataUploader {

    public static void uploadData(InputStream requestBody , String fileName) throws IOException {

//        // Mimic sleep
//        try {
//            Thread.sleep(Duration.ofSeconds(1).toMillis());
//        } catch (InterruptedException e) {
//            throw new RuntimeException(e);
//        }
        InputStreamReader inputStreamReader = new InputStreamReader(requestBody, StandardCharsets.UTF_8);
             BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

             // file creation
             File file = new File(fileName);
             FileWriter fileWriter = new FileWriter(file , true);
             BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
             String dataLine = bufferedReader.readLine();

             // file writing
              while (dataLine != null) {
                 bufferedWriter.write(dataLine);
                 bufferedWriter.newLine();
                 dataLine = bufferedReader.readLine();
             }
             bufferedReader.close();
             bufferedWriter.close();
         }
}
