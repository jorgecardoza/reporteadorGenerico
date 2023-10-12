package com.davi.reportes;

import java.io.File;  
import java.io.FileOutputStream;  
import org.apache.poi.xwpf.usermodel.XWPFDocument;  
import org.apache.poi.xwpf.usermodel.XWPFTable;  
import org.apache.poi.xwpf.usermodel.XWPFTableRow;  
public class TableExample {  
    public static void main(String[] args)throws Exception {  
    	
          XWPFDocument document= new XWPFDocument();  
          try(FileOutputStream out = new FileOutputStream(new File("Javatpoint.docx"))){  
//        	  var document = new XWPFDocument();
              // Creating Table  
              XWPFTable tab = document.createTable();  
              XWPFTableRow row = tab.getRow(0); // First row  
              // Columns  
              row.getCell(0).setText("Sl. No.");  
              row.addNewTableCell().setText("Name");  
              row.addNewTableCell().setText("Email");  
              row = tab.createRow(); // Second Row  
              row.getCell(0).setText("1.");  
              row.getCell(1).setText("Irfan");  
              row.getCell(2).setText("irfan@gmail.com");  
              row = tab.createRow(); // Third Row  
              row.getCell(0).setText("2.");  
              row.getCell(1).setText("Mohan");  
              row.getCell(2).setText("mohan@gmail.com");        
              document.write(out);  
          }catch(Exception e) {  
              System.out.println(e);  
          }  
       }  
}  