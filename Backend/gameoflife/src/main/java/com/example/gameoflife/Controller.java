package com.example.gameoflife;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import javax.annotation.PostConstruct;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/grid")
public class Controller {
    
    int cols = 30;
    int rows = 30;
    int steps = 0;
    String[][] grid = new String[cols][rows];
    
    @PostConstruct
    public void onStart() throws FileNotFoundException, IOException {
        for(int i = 0; i < cols; i++) {
            for(int j = 0; j < rows; j++) {
                grid[i][j] = ".";
            }
        }
        
        FileReader file = new FileReader("lif_files/gliders6.lif");
        BufferedReader reader = new BufferedReader(file);
        String line = null;
        int lineNumber = 0;
        while((line = reader.readLine()) != null && lineNumber < rows) {
            log(line);
            if(!line.startsWith("#")) {
                System.arraycopy(line.split(""), 0, grid[lineNumber], 0, line.split("").length);  
                lineNumber++;        
            }
        }     
    }
    
    public void step() {
      
        steps++;
 
        log("====================================");
        log("Generation: " + steps);
        
        for(int i = 0; i < cols; i++) {
            for(int j = 0; j < rows; j++) {
                String cell = grid[i][j];

                int livingNeighbor = 0;
                
                log("Cella: [" + i + "][" + j + "] = " + cell);
                
                //SZOMSZÉDOK
                //Felső szomszédok balról jobbra
                if(i != 0 && j != 0) {
                    String firstNeighbor = grid[i - 1][j - 1];
                    Boolean firstLive = isLiving(firstNeighbor);
                    if(firstLive) { livingNeighbor++; }
                    log("1. szomszed: [" + (i - 1) + "][" + (j - 1) + "] = " + firstNeighbor + " : " + firstLive);
                }
                
                if(i != 0) {
                    String secondNeighbor = grid[i - 1][j];
                    Boolean secondLive = isLiving(secondNeighbor);
                    if(secondLive) { livingNeighbor++; }
                    log("2. szomszed: [" + (i - 1) + "][" + j + "] = " + secondNeighbor + " : " + secondLive);
                }
                
                if(i != 0 && j != (cols - 1)) {
                    String thirdNeighbor = grid[i - 1][j + 1];
                    Boolean thirdLive = isLiving(thirdNeighbor);
                    if(thirdLive) { livingNeighbor++; }
                    log("3. szomszed: [" + (i - 1) + "][" + (j + 1) + "] = " + thirdNeighbor + " : " + thirdLive);
                }
                
                //Alsó szomszédok balról jobbra
                if(i != (rows-1) && j != 0) {
                    String fourthNeighbor = grid[i + 1][j - 1];
                    Boolean fourthLive = isLiving(fourthNeighbor);
                    if(fourthLive) { livingNeighbor++; }
                    log("4. szomszed: [" + (i + 1) + "][" + (j - 1) + "] = " + fourthNeighbor + " : " + fourthLive);
                }
                
                if(i != (rows-1)) {
                    String fifthNeighbor = grid[i + 1][j];
                    Boolean fifthLive = isLiving(fifthNeighbor);
                    if(fifthLive) { livingNeighbor++; }
                    log("5. szomszed: [" + (i + 1) + "][" + j + "] = " + fifthNeighbor + " : " + fifthLive);
                }
                
                if(i != (rows - 1) && j != (cols - 1)) {
                    String sixthNeighbor = grid[i + 1][j + 1];
                    Boolean sixthLive = isLiving(sixthNeighbor);
                    if(sixthLive) { livingNeighbor++; }
                    log("6. szomszed: [" + (i + 1) + "][" + (j + 1) + "] = " + sixthNeighbor + " : " + sixthLive);
                }
                
                //Mellette lévő szomszédok
                if(j != 0) {
                    String seventhNeighbor = grid[i][j - 1];
                    Boolean seventhLive = isLiving(seventhNeighbor);
                    if(seventhLive) { livingNeighbor++; }
                    log("7. szomszed: [" + i + "][" + (j - 1) + "] = " + seventhNeighbor + " : " + seventhLive);
                }
                
                if(j != (cols - 1)) {
                    String eighthNeighbor = grid[i][j + 1];
                    Boolean eighthLive = isLiving(eighthNeighbor);
                    if(eighthLive) { livingNeighbor++; }
                    log("8. szomszed: [" + i + "][" + (j + 1) + "] = " + eighthNeighbor + " : " + eighthLive);
                }
                
                log("Szomszédok száma: " + livingNeighbor);
                
                //SZABÁLYOK
                //HA SZOMSZÉDJA < 2 VAGY > 3  --> MEGHAL
                if (isLiving(cell) && ((livingNeighbor < 2) || (livingNeighbor > 3))) {
                    
                    grid[i][j] = ".";
                    log("csillag --> pont");
                }
                
                //HA 2 VAGY 3 SZOMSZÉDJA VAN --> TÚLÉLI
                if (isLiving(cell) && ((livingNeighbor == 2) || (livingNeighbor == 3))) {

                    grid[i][j] = "*";
                    log("csillag --> csillag");
                } 
                
                 //HA PONTOSAN 3 SZOMSZÉDJA VAN --> SZÜLETIK
                if (!isLiving(cell) && (livingNeighbor == 3)) {

                    grid[i][j] = "*";
                    log("pont --> csillag");
                } 
            }
        }
    }
        
    @GetMapping("")
    public ResponseEntity<String[][]> getAll() {
        step();
        return ResponseEntity.ok (grid);
    }
    
    @GetMapping("/steps")
    public ResponseEntity<Integer> getSteps() {
        return ResponseEntity.ok (steps);
    }
    
    private void log(String s) {
        //System.out.println(s);
    }
    
    //Élő cella
    private Boolean isLiving(String cell) {
        return cell.equals("*");
    }
}
