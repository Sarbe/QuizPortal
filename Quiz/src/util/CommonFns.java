package util;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.fileupload.FileItem;
import org.apache.log4j.Logger;

import bean.QuestionBean;

public class CommonFns {
	
	static Logger log = Logger.getLogger(CommonFns.class);
	public static String EMPTY = "";
	public static String startDtTime = " 12:00:01 AM";
	public static String endDtTime = " 11:59:59 PM";
	
	public static String IMG_FOLDER = "img";
	public static String EXCEL_FOLDER = "excel";
	public static String IMG_EXTN = ".JPG";
	public static String EXCEL_EXTN = ".XLS";

	public static java.sql.Date getSQLDateFromString(String dt) throws ParseException {
		SimpleDateFormat format = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss");
        Date parsed = format.parse(dt);
        java.sql.Date sqlDt = new java.sql.Date(parsed.getTime());
        return sqlDt;

	}
	
	public static String NVL(String value) {
		if(value==null){
			value = "";
		}
		return value;
	}

	
	public static String scoreCalculation(List<QuestionBean> qstnList) {
		
		int correctAns = 0;
		double finalScore = 0.0;
		
		for(int i=0;i<qstnList.size();i++){
			QuestionBean qb = qstnList.get(i);
			if(qb.getAnswer().equals(qb.getUser_ans())){
				correctAns ++;
			}
		}
		finalScore = ((double)correctAns/qstnList.size())*100;
		return String.format("%.2f", finalScore);
	}
	
	public static Date getDateFromString(String strDt) throws ParseException {
		SimpleDateFormat sfd = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss a");
        Date parsedDt = sfd.parse(strDt);
        Date dt = new Date(parsedDt.getTime());
        return dt;

	}
	
	public static String changeDateFormat(String strDt,String oldformat,String newformat) throws ParseException {
		SimpleDateFormat sfd = new SimpleDateFormat(oldformat);
        Date parsedDt = sfd.parse(strDt);
        sfd = new SimpleDateFormat(newformat);
        String newFormat = sfd.format(parsedDt);
        return newFormat;

	}
	
	public static String changeDateFormat(String strDt) throws ParseException {
		SimpleDateFormat sfd = new SimpleDateFormat("dd-MMM-yyyy");
        Date parsedDt = sfd.parse(strDt);
        String newFormat = sfd.format(parsedDt);
        return newFormat;

	}
	/* -1 if today is less than the date 
	 * 1 if today is greater  
	 * 0 if equal
	 */
	public static int compareWithToday(String strDt) throws ParseException {
		Date today = Calendar.getInstance().getTime();
		//strDt = changeDateFormat(strDt,"dd-MMM-yyyy HH:mm:ss","dd-MMM-yyyy hh:mm:ss a");
		Date dtTocmpr = getDateFromString(strDt);
		return today.compareTo(dtTocmpr);
	}
	
	public static void main(String[] args) throws ParseException {
		
		String str = "31-Dec-2014"+endDtTime;
		Date d = getDateFromString(str);
		System.out.println(d.toString());
		
		System.out.println(compareWithToday(str));
	}

	public static Map<String, String> processFormItemForFormField(List<FileItem> formItems) throws IOException {
		Map<String, String> requestFields = new HashMap<String, String>();
		if (formItems != null && formItems.size() > 0) {
			for (FileItem item : formItems) {
				if (item.isFormField()) {
//					System.out.println("Form field " + item.getFieldName() + " with value " + item.getString() + " detected.");
					requestFields.put(item.getFieldName(), item.getString());
				}
			}
		}
		return requestFields;
	}
	
	
	public static String processAndSaveFileItem(List<FileItem> formItems,String uploadPath) throws Exception {
		
		File uploadDir = new File(uploadPath);
		if (!uploadDir.exists()) {
			uploadDir.mkdir();
		}
		String filePath = "";
		if (formItems != null && formItems.size() > 0) {
			for (FileItem item : formItems) {
				// processes only fields that are not form fields
				if (!item.isFormField()) {
					System.out.println("File field " + item.getName() + " with file name " + item.getName() + " detected.");
					
					String fileName = new File(item.getName()).getName();
					filePath = uploadPath + File.separator + fileName;
					File storeFile = new File(filePath);

					// saves the file on disk
					item.write(storeFile);
				}
			}
		}
		return filePath;
	}
	
	public static void processFileItem(List<FileItem> formItems,
			String uploadPath) throws Exception {

		if (formItems != null && formItems.size() > 0) {
			for (FileItem item : formItems) {
				// processes only fields that are not form fields
				if (!item.isFormField()) {
					System.out.println("File field " + item.getName()
							+ " with file name " + item.getName()
							+ " detected.");

					/*
					 * String fileName = new File(item.getName()).getName();
					 * String filePath = uploadPath + File.separator + fileName;
					 * File storeFile = new File(filePath);
					 * 
					 * // saves the file on disk item.write(storeFile);
					 */
					item.getInputStream();
				}
			}
		}
	}
	
}
