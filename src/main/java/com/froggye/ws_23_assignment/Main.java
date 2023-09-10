package com.froggye.ws_23_assignment;

import com.froggye.ws_23_assignment.Replacement;

import java.io.IOException;
import java.net.URL;
import java.util.Scanner;
import javax.net.ssl.HttpsURLConnection;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Collections;
import java.util.stream.Collectors;



public class Main {
    
    public static void main(String[] args) throws IOException {
        
        ArrayList<String> data = new ArrayList<String>();
        List<Replacement> replacement = new ArrayList<Replacement>();
        
        
        // === получить data.json ===
        
        URL urlObj = new URL("https://raw.githubusercontent.com/thewhitesoft/student-2023-assignment/main/data.json");
        HttpsURLConnection connection = (HttpsURLConnection)urlObj.openConnection();
        connection.setRequestMethod("GET");
        
        int responceCode = connection.getResponseCode();
        
        if (responceCode == HttpsURLConnection.HTTP_OK) {
            StringBuilder sb = new StringBuilder();
            Scanner scanner = new Scanner(connection.getInputStream());
            while (scanner.hasNext()) {
                sb.append(scanner.nextLine());
            }
            
            // data - массив строк сообщений
            ObjectMapper objectMapper = new ObjectMapper();
            data = objectMapper.readValue(String.valueOf(sb), 
                    new TypeReference<ArrayList<String>>() {});
            
            scanner.close();
            
        } else {
            System.out.println("Ошибка HTTP GET запроса");
            System.exit(1);
        }
        
       
        
        // === прочитать replacement.json ===
        
        // replacement - массив объектов класса Replacement
        ObjectMapper or = new ObjectMapper();
        try {

            Main instance = new Main();
            InputStream is = instance.getFileAsIOStream("replacement.json");
            
            
            replacement = or.readValue(
                //new File("replacement.json"), 
                is,
                new TypeReference<List<Replacement>>() {});
            //replacement.forEach(x -> System.out.println(x.toString()));
        }
        catch (FileNotFoundException e){
            System.out.println("Ошибка чтения файла");
            System.exit(1);
        }
               
        
        // === очистить replacement от повторов ===
        
        Collections.reverse(replacement);
        List<Replacement> uniqueReplacement = replacement
                .stream()
                .distinct()
                .collect(Collectors.toList());
                
        //Collections.reverse(uniqueReplacement);
        
        
        // === удалить поля null ===
        
        for (Replacement item : uniqueReplacement) {
            if (item.getSource() == null) {
                for (int i = 0; i < data.size(); i++) {
                    if (data.get(i).contains(item.getReplacement())) {
                        data.remove(i);
                        if (i != 0)
                            i -= 1; 
                    }
                }

            }
        }
        
        
        // === произвести оставшиеся замены ===
        
        for (int dataIndex = 0; dataIndex < data.size(); dataIndex++) {

            for (int replacementIndex = 0; replacementIndex < uniqueReplacement.size(); replacementIndex++) {
                if (data.get(dataIndex).contains(uniqueReplacement.get(replacementIndex).getReplacement())) { 
                    data.set(dataIndex, (data.get(dataIndex)
                            .replace(uniqueReplacement.get(replacementIndex).getReplacement(), 
                                    uniqueReplacement.get(replacementIndex).getSource())));     //etDataMessage(replacementList.get(index).getSource());
                }
            }

        }
        
        //System.out.println(data.toString());
        
        
        
        // === записать результат в result.json ===
        
        try {
            ObjectMapper ow = new ObjectMapper();
            ow.writeValue(new File("result.json"), data);
        } catch (IOException e) {
            System.out.println("Ошибка записи файла");
            System.exit(1);
        }
        
    }
    
    
    
    private InputStream getFileAsIOStream(final String fileName) 
    {
        InputStream ioStream = this.getClass()
            .getClassLoader()
            .getResourceAsStream(fileName);
        
        if (ioStream == null) {
            throw new IllegalArgumentException(fileName + " is not found");
        }
        return ioStream;
    }
}
