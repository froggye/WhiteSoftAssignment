package com.froggye.ws_23_assignment;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.stream.Collectors;



public class Main {
    
    public static void main(String[] args) throws IOException {
                
        JsonParser parser = new JsonParser();
        
        
        // === получить data.json ===
         
        ArrayList<String> data = parser.readURL("https://raw.githubusercontent.com/thewhitesoft/student-2023-assignment/main/data.json",
                String.class);
        if (data == null) 
        {
            System.out.println("Ошибка HTTP GET запроса");
            System.exit(1);
        }
       
        
        // === прочитать replacement.json ===

        ArrayList<Replacement> replacement = parser.readLocal("replacement.json", Replacement.class);
        if (replacement == null) 
        {
            System.out.println("Ошибка чтения файла");
            System.exit(1);
        }   
        
        
        // === сделать замены ===
        
        data = makeReplacements(data, replacement);
        
        
        // === записать результат в result.json ===
        
        if (!parser.writeFile(data)){
            System.exit(1);
        }
        
    }
    
    
    private static ArrayList<String> makeReplacements (ArrayList<String> data, ArrayList<Replacement> replacement) {
        
        // === очистить replacement от повторов ===
        
        Collections.reverse(replacement);
        replacement = replacement
                .stream()
                .distinct()
                .collect(Collectors.toCollection(ArrayList::new));
                
        //Collections.reverse(uniqueReplacement);
        
        
        // === обработать оставшиеся replacement ===
        
        for (Replacement item : replacement) {
            String currentSource = item.getSource();
            String currentReplacement = item.getReplacement();
            
            // === удалить поля из data, где replacement должен быть null ===
            if (currentSource == null) {
                data = data.stream()
                        .filter(s -> !s.contains(currentReplacement))
                        .collect(Collectors.toCollection(ArrayList::new));
            }
            // === иначе заменить все подстроки ===
            else {
                data.replaceAll(s -> s.replace(currentReplacement, currentSource));
            }
        }
        
        return data;
    }

}
