package com.red.ink.controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.red.ink.constant.Constants;
import com.red.ink.dto.ResponseDto;
import com.red.ink.jsonconstractor.JsonConstractor;

@RestController
public class BulkUploadController {
	
	JsonConstractor constructJson = new JsonConstractor();

	@GetMapping("/getBulkUploadTemplate")
	public ResponseEntity<Object> getBulkUploadTemplate(){
		
		String fileName = "Question_BulkUpload_Template.xls";
		String contextPath = Constants.TEMPLATE_PATH;
		FileOutputStream fileOutputStream = null;
		File file = null;
		try {
			file = new File(contextPath+fileName);
			if (!file.exists()) {
				file.createNewFile();
			}
			fileOutputStream = new FileOutputStream(file);
		} catch (FileNotFoundException ex) {
			ex.printStackTrace();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		HSSFWorkbook hssfWorkbook = new HSSFWorkbook();
		HSSFSheet hssfSheet = hssfWorkbook.createSheet("Questions");
		HSSFRow headerRow = hssfSheet.createRow(0);
 
		Font boldFont = hssfWorkbook.createFont();
		boldFont.setBold(true);
//		boldFont.setColor(IndexedColors.BLACK.getIndex());
		boldFont.setColor(IndexedColors.BLACK.getIndex());
		
		CellStyle tableHeaderStyle = hssfWorkbook.createCellStyle();
		tableHeaderStyle.setBorderBottom(BorderStyle.THIN);
		tableHeaderStyle.setBottomBorderColor(IndexedColors.BLACK.getIndex());
		tableHeaderStyle.setBorderLeft(BorderStyle.THIN);
		tableHeaderStyle.setLeftBorderColor(IndexedColors.BLACK.getIndex());
		tableHeaderStyle.setBorderRight(BorderStyle.THIN);
		tableHeaderStyle.setRightBorderColor(IndexedColors.BLACK.getIndex());
		tableHeaderStyle.setBorderTop(BorderStyle.THIN);
		tableHeaderStyle.setTopBorderColor(IndexedColors.BLACK.getIndex());
		tableHeaderStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
		tableHeaderStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);;
		tableHeaderStyle.setAlignment(HorizontalAlignment.CENTER);;
		tableHeaderStyle.setVerticalAlignment(VerticalAlignment.JUSTIFY);
		tableHeaderStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		tableHeaderStyle.setFont(boldFont);
		
		CellStyle tableHeaderStyle1 = hssfWorkbook.createCellStyle();
		Font font = hssfWorkbook.createFont();
		font.setColor(IndexedColors.RED.getIndex());
		tableHeaderStyle1.setFont(font);
		
		HSSFCell subjectName = headerRow.createCell(0);
		subjectName.setCellValue("Subject Name *");
		subjectName.setCellStyle(tableHeaderStyle);
		subjectName.setCellType(CellType.STRING);;

		HSSFCell question = headerRow.createCell(1);
		question.setCellValue("Question *");
		question.setCellStyle(tableHeaderStyle);
		question.setCellType(CellType.STRING);;

		HSSFCell option1  = headerRow.createCell(2);
		option1.setCellValue("Option 1 *");
		option1.setCellStyle(tableHeaderStyle);
		option1.setCellType(CellType.STRING);;

		HSSFCell option2 = headerRow.createCell(3);
		option2.setCellValue("Option 2 *" );
		option2.setCellStyle(tableHeaderStyle);
		option2.setCellType(CellType.STRING);
		
		HSSFCell option3 = headerRow.createCell(4);
		option3.setCellValue("Option 3 *" );
		option3.setCellStyle(tableHeaderStyle);
		option3.setCellType(CellType.STRING);

		HSSFCell option4 = headerRow.createCell(5);
		option4.setCellValue("Option 4 *" );
		option4.setCellStyle(tableHeaderStyle);
		option4.setCellType(CellType.STRING);

		HSSFCell correctOption = headerRow.createCell(6);
		correctOption.setCellValue("Correct Option *" );
		correctOption.setCellStyle(tableHeaderStyle);
		correctOption.setCellType(CellType.STRING);


		hssfSheet.autoSizeColumn(0);
		hssfSheet.autoSizeColumn(1);
		hssfSheet.autoSizeColumn(2);
		hssfSheet.autoSizeColumn(3);
		hssfSheet.autoSizeColumn(4);
		hssfSheet.autoSizeColumn(5);
		hssfSheet.autoSizeColumn(6);
		
		try {
			if (hssfWorkbook != null) {
				hssfWorkbook.write(fileOutputStream);
			}
			if (fileOutputStream != null) {
				fileOutputStream.flush();
				fileOutputStream.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		 String path= Constants.TEMPLATE_ACCESSPATH + fileName;
		 ResponseDto dto = new ResponseDto();
		 dto.setTemplatePath(path);
		
		return constructJson.responseCreation(true, "Success!","Success!", dto);
	}	
	
	
}
