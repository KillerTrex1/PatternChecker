/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package randomsongmaker;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 *
 * @author Mart Henrick Gamutan
 */
public class ExcelReader {
    public String fileName ;
    public ExcelReader(String fileName){
        this.fileName = fileName;
    }
    public ArrayList <Sheet> ReadData() throws FileNotFoundException, IOException{
        ArrayList <Sheet> Sheets = new ArrayList();
        try
        {
            
            FileInputStream file = new FileInputStream(new File(this.fileName));
 
            //Create Workbook instance holding reference to .xlsx file
            XSSFWorkbook workbook = new XSSFWorkbook(file);
 
            //Get first/desired sheet from the workbook
            XSSFSheet sheet = workbook.getSheetAt(0);
 
            //Iterate through each rows one by one
            Iterator<Row> rowIterator = sheet.iterator();
            
            while (rowIterator.hasNext()) 
            {
                Row row = rowIterator.next();
                //For each row, iterate through all the columns
                Iterator<Cell> cellIterator = row.cellIterator();
                Cell cell = cellIterator.next();
                String name =  cell.getStringCellValue();
                ArrayList <String> pat = new ArrayList();
                while (cellIterator.hasNext()) 
                {
                    
                    cell = cellIterator.next();
                    //Check the cell type and format accordingly
                    pat.add(cell.getStringCellValue());
                }
                Sheets.add(new Sheet(pat,name));
                //System.out.println("");
            }
            
            file.close();
            
        } 
        catch (Exception e) 
        {
            e.printStackTrace();
        }
        return Sheets;
    }
    
    
    public ArrayList <Pattern> ReadPatterns() throws FileNotFoundException, IOException{
        ArrayList <Pattern> pats = new ArrayList();
        try
        {
            
            FileInputStream file = new FileInputStream(new File(this.fileName));
 
            //Create Workbook instance holding reference to .xlsx file
            XSSFWorkbook workbook = new XSSFWorkbook(file);
 
            //Get first/desired sheet from the workbook
            XSSFSheet sheet = workbook.getSheetAt(0);
 
            //Iterate through each rows one by one
            Iterator<Row> rowIterator = sheet.iterator();
            
            while (rowIterator.hasNext()) 
            {
                Row row = rowIterator.next();
                //For each row, iterate through all the columns
                Iterator<Cell> cellIterator = row.cellIterator();
                Cell cell = cellIterator.next();
                String name =  cell.getStringCellValue();
                
                cell = cellIterator.next();
                int ctr = (int)cell.getNumericCellValue();
                
                Pattern x = new Pattern(name);
                x.ctr = ctr;
                
                pats.add(x);
                //System.out.println("");
            }
            
            file.close();
            
        } 
        catch (Exception e) 
        {
            e.printStackTrace();
        }
        return pats;
    }
}
