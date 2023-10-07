package com.froggye.ws_23_assignment;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.type.TypeFactory;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;
import javax.net.ssl.HttpsURLConnection;


public class JsonParser {
    
    // чтение и запись json файлов
    // 1. Чтение из URL
    // 2. Чтение из файла в проекте
    // 3. Запись json файла
    
    private final ObjectMapper objectMapper;
    
    public JsonParser() {
        objectMapper = new ObjectMapper();
    }
    
    // прочитать из url
    // возвращает список элементов типа или класса itemType
    public <T> ArrayList<T> readURL(final String url, Class<T> itemType) 
            throws IOException {   
        URL urlObj = new URL(url);
        HttpsURLConnection connection = (HttpsURLConnection)urlObj.openConnection();
        connection.setRequestMethod("GET");
        
        int responceCode = connection.getResponseCode();
        
        if (responceCode == HttpsURLConnection.HTTP_OK) {
            // успешно получили файл
            
            // прочитать как строку и переделать в список нужного типа              
            StringBuilder sb = new StringBuilder();
            Scanner scanner = new Scanner(connection.getInputStream());
            while (scanner.hasNext()) {
                sb.append(scanner.nextLine());
            }            
            scanner.close();
            
            TypeFactory t = TypeFactory.defaultInstance(); // нужен, чтобы правильно составить ArrayList из элементов itemType
            
            return objectMapper.readValue(
                    String.valueOf(sb), 
                    t.constructCollectionType(ArrayList.class,itemType));
        } else {
            return null;
        }
    }
    
    // прочитать из файла
    // возвращает список элементов типа или класса itemClass
    public <T> ArrayList<T> readLocal(final String path, Class<T> itemType) 
            throws IOException {   
        try {
            JsonParser instance = new JsonParser();
            InputStream is = instance.getFileAsIOStream(path);
            TypeFactory t = TypeFactory.defaultInstance();
            
            return objectMapper.readValue(is,
                    t.constructCollectionType(ArrayList.class,itemType));
        }
        catch (FileNotFoundException e){
            return null;
        }
    }
    
    // получить поток ввода из файла внутри проекта
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
    
    // создать файл json с массивом объектов
    public <T> boolean writeFile(ArrayList<T> data) {
        try {
            // ?? - prettyfier не работает почему-то
            objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
            objectMapper.writeValue(new File("result.json"), data);
            return true;
        } catch (IOException e) {
            System.out.println("Ошибка записи файла");
            return false;
        }
    }
    
}
