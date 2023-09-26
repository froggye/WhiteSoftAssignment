package com.froggye.ws_23_assignment;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Collections;
import java.util.stream.Collectors;



public class Main {
    
    public static void main(String[] args) throws IOException {
        
        ArrayList<String> data = new ArrayList<String>();
        ArrayList<Replacement> replacement = new ArrayList<Replacement>();
        
        JsonParser parser = new JsonParser();
        
        
        // === получить data.json ===
         
        data = parser.readURL("https://raw.githubusercontent.com/thewhitesoft/student-2023-assignment/main/data.json",
                String.class);
        if (data == null) 
        {
            System.exit(1);
        }
       
        
        // === прочитать replacement.json ===

        replacement = parser.readLocal("replacement.json", Replacement.class);
        if (replacement == null) 
        {
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
            String currentData = data.get(dataIndex);
            
            for (int replacementIndex = 0; replacementIndex < uniqueReplacement.size(); replacementIndex++) {
                String currentReplacement = uniqueReplacement.get(replacementIndex).getReplacement();
                
                if (currentData.contains(currentReplacement))
                { 
                    String currentSource = uniqueReplacement.get(replacementIndex)
                                        .getSource();
                    // В data[dataIndex]: подстроку Replacement заменить на Source
                    currentData = currentData.replace(
                            currentReplacement,
                            currentSource);
                    
                    data.set(dataIndex, currentData);
                }
                
            }
        }
        
        return data;
    }

}
