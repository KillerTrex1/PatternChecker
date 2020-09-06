/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package randomsongmaker;

/**
 *
 * @author Mart Henrick Gamutan
 */
import java.io.File;
import java.io.FileOutputStream;
import java.util.Random;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelWriter {
    public ExcelWriter(){
        XSSFWorkbook workbook = new XSSFWorkbook(); 
  
        // Create a blank sheet 
        XSSFSheet sheet = workbook.createSheet("DATA"); 
  
        // This data needs to be written (Object[]) 
        //Map<String, Object[]> data = new TreeMap<String, Object[]>(); 
        //data.put("1", new Object[]{ "ID", "NAME", "LASTNAME" }); 
        //data.put("2", new Object[]{ 1, "Pankaj", "Kumar" }); 
        //data.put("3", new Object[]{ 2, "Prakashni", "Yadav" }); 
        //data.put("4", new Object[]{ 3, "Ayan", "Mondal" }); 
        //data.put("5", new Object[]{ 4, "Virat", "kohli" }); 
  
        // Iterate over data and write to sheet 
        //Set<String> keyset = data.keySet(); 
        //int rownum = 0; 
        for (int rownum = 0;rownum<5000;rownum++){
            Row row = sheet.createRow(rownum);
            
            Cell cell = row.createCell(0); 
            String firstVal = "Sheeet #"+(rownum+1);
            cell.setCellValue(firstVal);
            int cellnum = 1;
            for (cellnum = 1;cellnum<21;cellnum++){
                String randomPattern = generateSongWithRests("",0);
                cell = row.createCell(cellnum); 
                cell.setCellValue(randomPattern);
            }
        }
        for (int i=0;i<21;i++){
            sheet.autoSizeColumn(i);
        }
        
        /*
        for (String key : keyset) { 
            // this creates a new row in the sheet 
            Row row = sheet.createRow(rownum++); 
            Object[] objArr = data.get(key); 
            int cellnum = 0; 
            for (Object obj : objArr) { 
                // this line creates a cell in the next column of that row 
                Cell cell = row.createCell(cellnum++); 
                if (obj instanceof String) 
                    cell.setCellValue((String)obj); 
                else if (obj instanceof Integer) 
                    cell.setCellValue((Integer)obj); 
            } 
        } 
        */
        try { 
            // this Writes the workbook gfgcontribute 
            FileOutputStream out = new FileOutputStream(new File("data.xlsx")); 
            workbook.write(out); 
            out.close(); 
            System.out.println("gfgcontribute.xlsx written successfully on disk."); 
        } 
        catch (Exception e) { 
            e.printStackTrace(); 
        } 
        
    }
    public static String generateSong(String curPattern,int noteVal){
        //System.out.println("NOTE: "+noteVal);
        int max = ((8-noteVal)/2);
        //System.out.println("MAX: "+max);
        int val = 0;
        //System.out.println(max);
        if (max>0){
            Random rand = new Random();
            val = rand.nextInt(max);
        }
        //Random rand = new Random();
        
        switch(val){
            case 3:
                curPattern= curPattern+"W ";
                noteVal+=8;
                break;
            case 2:
                curPattern= curPattern+"H ";
                noteVal+=4;
                break;
            case 1:
                curPattern= curPattern+"Q ";
                noteVal+=2;
                break;
            default:
                curPattern= curPattern+"E ";
                noteVal+=1;
                break;
        }
        
        if (noteVal==8){
            return curPattern;
        }else{
            return generateSong(curPattern,noteVal);
        }
    }
    public static String generateSongWithRests(String curPattern,int noteVal){
        //System.out.println("NOTE: "+noteVal);
        int max = ((8-noteVal)/2);
        //System.out.println("MAX: "+max);
        int val = 0;
        //System.out.println(max);
        Random rand = new Random();
        if (max>0){
            
            val = rand.nextInt(max);
        }
        
        int val2 = rand.nextInt(2);
        
        //Random rand = new Random();
        
        switch(val){
            case 3:
                if (val2==0)
                    curPattern= curPattern+"W ";
                else
                    curPattern= curPattern+"WR ";
                noteVal+=8;
                break;
            case 2:
                if (val2==0)
                    curPattern= curPattern+"H ";
                else
                    curPattern= curPattern+"HR ";
                noteVal+=4;
                break;
            case 1:
                if (val2==0)
                    curPattern= curPattern+"Q ";
                else
                    curPattern= curPattern+"QR ";
                noteVal+=2;
                break;
            default:
                if (val2==0)
                    curPattern= curPattern+"E ";
                else
                    curPattern= curPattern+"ER ";
                noteVal+=1;
                break;
        }
        
        if (noteVal==8){
            return curPattern;
        }else{
            return generateSongWithRests(curPattern,noteVal);
        }
    }
}
