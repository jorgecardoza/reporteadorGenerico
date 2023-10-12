package com.davi.reportes;

import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;

public class TablaDinamica {

	public static void main(String[] args) throws IOException {
		   XWPFDocument document = new XWPFDocument();
		   XWPFTable tableOne = document.createTable();
		   for(int i= 0;i<=7;i++) {
		   XWPFTableRow tableOneRowOne = tableOne.getRow(0);
		   tableOneRowOne.getCell(0).setText("Header1");
		   tableOneRowOne.addNewTableCell().setText("header2");
		   XWPFTableRow tableOneRowTwo = tableOne.createRow();
		   tableOneRowTwo.getCell(0).setText("Data1");
		   tableOneRowTwo.getCell(1).setText("Data2");
		   FileOutputStream outStream = new FileOutputStream("tigre.doc");
		   document.write(outStream);
		   outStream.close();
		   }
		 }

}
