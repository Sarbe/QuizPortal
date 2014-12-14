package util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import bean.QuestionBean;
import db.DBManager;

public class ImportQstnExcel {
	private static final long serialVersionUID = 1L;
	
	private static List<String> errList = null;
	static Logger log = Logger.getLogger(ImportQstnExcel.class);

	public static int processAndSaveExcelData(String filepath) throws Exception {
		
		List<QuestionBean> qbList = new ArrayList<QuestionBean>();
		int noOfRecords = 0;
		errList = new ArrayList<String>();
		try {

			FileInputStream file = new FileInputStream(filepath);
			
			// Get the workbook instance for XLS file
			HSSFWorkbook workbook = new HSSFWorkbook(file);

			// Get first sheet from the workbook
			HSSFSheet sheet = workbook.getSheetAt(0);

			// Iterate through each rows from first sheet
			Iterator<Row> rowIterator = sheet.iterator();
			int rowNo = 1;
			String quizType = "";
			QuestionBean qb = null;
			while (rowIterator.hasNext()) {

				Row row = rowIterator.next();
				if (rowNo == 1) {
					quizType = cellValue(row.getCell(1),rowNo);
				} else if (rowNo % 7 == 2) {
					qb = new QuestionBean();
					qb.setQuestion_Txt(cellValue(row.getCell(1),rowNo));
				} else if (rowNo % 7 == 3) {
					qb.setOption1(cellValue(row.getCell(1),rowNo));
				} else if (rowNo % 7 == 4) {
					qb.setOption2(cellValue(row.getCell(1),rowNo));
				} else if (rowNo % 7 == 5) {
					qb.setOption3(cellValue(row.getCell(1),rowNo));
				} else if (rowNo % 7 == 6) {
					qb.setOption4(cellValue(row.getCell(1),rowNo));
				} else if (rowNo % 7 == 0) {
					qb.setAnswer(cellValue(row.getCell(1),rowNo));
				} else if (rowNo % 7 == 1) {
					qb.setComments(cellValue(row.getCell(1),rowNo));
					qbList.add(qb);

				}
				rowNo++;
			}
			file.close();
			
			System.out.println("SIZE:: "+qbList.size());
			
			if(errList.size()==0){
				DBManager dbmgr = new DBManager();
				noOfRecords = dbmgr.saveExportedQuestionToDB(quizType,qbList);
				System.out.println(noOfRecords);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return noOfRecords;
	}
	
	private static String cellValue(Cell cell,int rowNo) {
		String value = "";
		if (cell != null) {
			switch (cell.getCellType()) {
			case Cell.CELL_TYPE_BOOLEAN:
				value = String.valueOf(cell.getBooleanCellValue());
				break;
			case Cell.CELL_TYPE_NUMERIC:
				value = String.valueOf(cell.getNumericCellValue());
				break;
			case Cell.CELL_TYPE_STRING:
				value = String.valueOf(cell.getStringCellValue());
				break;
			default:
				value = "";
			}
		}
		if(value.equals("")){
			errList.add("Row "+ rowNo +" has error");
			System.out.println("Row "+ rowNo +" has error");
		}
		return value;
	}
	
	public static void main(String[] args) throws Exception {
		processAndSaveExcelData("C:/Users/Sarbe/Desktop/apache/qstn.xls");
		
	}

}
