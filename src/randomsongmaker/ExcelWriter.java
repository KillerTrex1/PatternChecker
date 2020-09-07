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
import java.util.ArrayList;
import java.util.Random;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelWriter {

    public static void sample(){
        for(int i = 0;i<20;i++){
            System.out.println(generateSong("",0,true));
        }
    }
    public static void WriteSheetToExel(int count,int countNote){
        XSSFWorkbook workbook = new XSSFWorkbook(); 
  
        // Create a blank sheet 
        XSSFSheet sheet = workbook.createSheet("DATA"); 

        for (int rownum = 0;rownum<count;rownum++){
            Row row = sheet.createRow(rownum);
            
            Cell cell = row.createCell(0); 
            String firstVal = "Sheeet #"+(rownum+1);
            cell.setCellValue(firstVal);
            int cellnum = 1;
            for (cellnum = 1;cellnum<21;cellnum++){
                String randomPattern;
                if (rownum < countNote){
                    randomPattern = generateSong("",0,false);
                }else{
                    randomPattern = generateSong("",0,true);
                }
                    
                cell = row.createCell(cellnum); 
                cell.setCellValue(randomPattern);
            }
        }
        for (int i=0;i<21;i++){
            sheet.autoSizeColumn(i);
        }

        try { 
            // this Writes the workbook gfgcontribute 
            FileOutputStream out = new FileOutputStream(new File("data.xlsx")); 
            workbook.write(out); 
            out.close(); 
            System.out.println("data.xlsx written successfully on disk."); 
        } 
        catch (Exception e) { 
            e.printStackTrace(); 
        }  
    }
    public static void WritePatternToExel(ArrayList <Pattern> pats){
        XSSFWorkbook workbook = new XSSFWorkbook(); 
  
        // Create a blank sheet 
        XSSFSheet sheet = workbook.createSheet("Pattterns"); 

        for (int rownum = 0;rownum<pats.size();rownum++){
            Row row = sheet.createRow(rownum);
            
            Cell cell = row.createCell(0); 
            cell.setCellValue(pats.get(rownum).pattern);
            Cell cell2 = row.createCell(1); 
            cell2.setCellValue(pats.get(rownum).ctr);
 
        }
        for (int i=0;i<2;i++){
            sheet.autoSizeColumn(i);
        }

        try { 
            // this Writes the workbook gfgcontribute 
            FileOutputStream out = new FileOutputStream(new File("patterns.xlsx")); 
            workbook.write(out); 
            out.close(); 
            System.out.println("pattern.xlsx written successfully on disk."); 
        } 
        catch (Exception e) { 
            e.printStackTrace(); 
        }  
    }
    public static String generateSong(String curPattern,int noteVal,Boolean hasRest){
        //System.out.println("NOTE: "+noteVal);
        int max = ((16-noteVal));
        //System.out.println("MAX: "+max);
        int val = 0;
        //System.out.println(max);
        Random rand = new Random();
        if (max == 16){
            //whole3
            val = RandomNoteWithWeights(3);
        }else if (max >=8){
            //half2
            val = RandomNoteWithWeights(2);
        }else if (max >=4){
            //quarter1
            val = RandomNoteWithWeights(1);
        }else if (max >=2){
            //eight0
            val = RandomNoteWithWeights(0);
        }else{
            val = 0;
        }
        int val2 = 0;
        if (hasRest){
            val2 = rand.nextInt(2);
        }
        
        
        //Random rand = new Random();
        
        switch(val){
            case 4:
                if (val2==0)
                    curPattern= curPattern+"W";
                else
                    curPattern= curPattern+"Wr";
                noteVal+=16;
                break;
            case 3:
                if (val2==0)
                    curPattern= curPattern+"H";
                else
                    curPattern= curPattern+"Hr";
                noteVal+=8;
                break;
            case 2:
                if (val2==0)
                    curPattern= curPattern+"Q";
                else
                    curPattern= curPattern+"Qr";
                noteVal+=4;
                break;
            case 1:
                if (val2==0)
                    curPattern= curPattern+"E";
                else
                    curPattern= curPattern+"Er";
                noteVal+=2;
                break;
            default:
                if (val2==0)
                    curPattern= curPattern+"S";
                else
                    curPattern= curPattern+"Sr";
                noteVal+=1;
                break;
        }
        
        if (noteVal==16){
            return curPattern;
        }else{
            return generateSong(curPattern,noteVal,hasRest);
        }
    }
    public static int RandomNoteWithWeights(int max){
        Random rand = new Random();
        int x = rand.nextInt(100);
        switch(max){
            case 3:
                if (x<10){
                    return 4;
                }else if(x<25){
                    return 3;
                }else if(x<45){
                    return 2;
                }else if(x<70){
                    return 1;
                }else{
                    return 0;
                }
            case 2:
                if (x<10){
                    return 3;
                }else if(x<30){
                    return 2;
                }else if(x<60){
                    return 1;
                }else{
                    return 0;
                }
            case 1:
               if (x<25){
                    return 2;
                }else if(x<60){
                    return 1;
                }else{
                    return 0;
                }
            default:
                if (x<50){
                    return 1;
                }else{
                    return 0;
                }
        }
    }
}
