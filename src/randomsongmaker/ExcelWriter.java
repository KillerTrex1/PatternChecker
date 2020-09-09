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

    public static void WritePatternToExel(ArrayList <Pattern> pats,String filename){
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
            FileOutputStream out = new FileOutputStream(new File(filename)); 
            workbook.write(out); 
            out.close(); 
            System.out.println(filename+" written successfully on disk."); 
        } 
        catch (Exception e) { 
            e.printStackTrace(); 
        }  
    }

}
