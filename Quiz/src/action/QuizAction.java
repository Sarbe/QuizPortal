package action;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.log4j.Logger;
import org.apache.naming.factory.SendMailFactory;

import util.CommonFns;
import util.ConfigFile;
import util.DiskFileManager;
import util.ImageManager;
import util.ImportQstnExcel;
import util.SendEmail;
import velocity.VelocityHelperClass;
import velocity.VelocityHtml;
import bean.EmpDetails;
import bean.QuestionBean;
import bean.QuizDetails;
import bean.QuizEmpDetails;

import com.google.gson.Gson;

import db.DBManager;


@WebServlet("/ss/QuizAction")
public class QuizAction extends HttpServlet {
	private static final long serialVersionUID = 1L;
	//HttpSession session = null;
	DBManager db = null;
	
	static Logger log = Logger.getLogger(QuizAction.class);
	
    public QuizAction() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		execute(request,response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		execute(request,response);
	}

	
	private void execute(HttpServletRequest request, HttpServletResponse response) {
		try {
			
				db = new DBManager();

				if (ServletFileUpload.isMultipartContent(request)) {
					log.info("Request has multiPart Content");
					ServletFileUpload upload = DiskFileManager.fileUploadInstance();
					@SuppressWarnings("unchecked")
					List<FileItem> formItems = upload.parseRequest(request);
					Map<String, String> requestFields = CommonFns.processFormItemForFormField(formItems);
					
					request.setAttribute("LINK_NO", requestFields.get("linkNo"));

					String method = requestFields.get("method") == null ? "" : requestFields.get("method");

					if (!method.equals("")) {

						if (method.equals("addQuizSet")) {
							addQuizSet(request, response, requestFields);
						}else if (method.equals("addQuestion")) {
							addQuestion(request, response, requestFields);
						} else if (method.equals("createSetFromDump")) {
							createSetFromDump(request, response, requestFields);
						}else if (method.equals("uploadImg")) {
							uploadImage(request, response,formItems, requestFields);
						} else if (method.equals("saveQuestion")) {
							saveQuestion(request, response, requestFields);
						} else if (method.equals("showQuestion")) {
							showQuestion(request, response, requestFields);
						} else if (method.equals("editQuizSet")) {
							editQuizSet(request, response, requestFields);
						}  else if (method.equals("saveUserDetails")) {
							saveUserDetails(request, response, requestFields);
						} else if (method.equals("giveTest")) {
							giveTest(request, response, requestFields);
						} else if (method.equals("saveQuizAnswer")) {
							saveQuizAnswer(request, response, requestFields);
						} else if (method.equals("finalSubmitAnswer")) {
							finalSubmitAns(request, response, requestFields);
						}else if (method.equals("goToResultPage")) {
							goToResultPage(request, response, requestFields);
						}
						else if (method.equals("calculateResult")) {
							calculateResult(request, response, requestFields);
						} else if (method.equals("viewResult")) {
							viewResult(request, response, requestFields);
						} else if (method.equals("alterStatusOfQuiz")) {
							alterStatusOfQuiz(request, response, requestFields);
						} else if (method.equals("themeSelector")) {
							themeSelector(request, response, requestFields);
						}else if (method.equals("backToHome")) {
							backToHome(request, response, requestFields);
						}else if (method.equals("uploadExcelFile")) {
							uploadExcelFile(request, response,formItems);
						}else if (method.equals("logout")) {
							logout(request, response,requestFields);
						} 
						
						

					} else {
						request.setAttribute("MSG", "Invalid Request.");
						response.sendRedirect("start.jsp");

					}

				}else{
					log.info("Request is a simple request");
					
					String method = request.getParameter("method") == null ? "" : request.getParameter("method");

					if (!method.equals("")) {
						if (method.equals("changeQuizDate")) {
							changeQuizDate(request, response);
						} else if (method.equals("getQstnFromDump")) {
							getQstnFromDump(request, response);
						}else if (method.equals("getNoofQstn")) {
							getNoofQstn(request, response);
						}else if (method.equals("sendFeedback")) {
							sendFeedback(request, response);
						}else if (method.equals("retriveFeedback")) {
							retriveFeedback(request, response);
						}else if (method.equals("resolveFeedback")) {
							resolveFeedback(request, response);
						}else if (method.equals("markQuizAsDeclared")) {
							markQuizAsDeclared(request, response);
						}else if (method.equals("manageUser")) {
							manageUser(request, response);
						}else if (method.equals("updateUser")) {
							updateUser(request, response);
						}else if (method.equals("removeUser")) {
							removeUser(request, response);
						}else if (method.equals("searchUser")) {
							searchUser(request, response);
						}else if (method.equals("uniqueEmailAddress")) {
							uniqueEmailAddress(request, response);
						}
						
					} else {
						request.setAttribute("MSG", "Invalid Request.");
						response.sendRedirect("start.jsp");

					}
				}

				
				
		} catch (Exception e) {
			//e.printStackTrace();
			request.setAttribute("MSG", e.getMessage());
			String a = (String) request.getAttribute("MSG");
			System.out.println(""+a);
			log.info(e.getMessage());
			try {
//				request.setAttribute("MSG", "Invalid Request.");
				//response.sendRedirect("start.jsp");
				request.getRequestDispatcher("start.jsp").forward(request, response);
			} catch (IOException e1) {
				e1.printStackTrace();
			} catch (ServletException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		} 
	}

	
	private void uniqueEmailAddress(HttpServletRequest request,	HttpServletResponse response) 
			throws Exception {
		HttpSession session = getSession(request, request.getParameter("sessiodnId"));
		try {
			String emailAdd = request.getParameter("emailAdd"); 
			String empId = request.getParameter("empId"); 
			
			int emailCount = db.isUniqueEmailAddress(emailAdd,empId);
			String str = new Gson().toJson(emailCount);
			response.getWriter().write(str);
		} catch (Exception e) {
			throw e;
		}finally{
		}

	}

	private void logout(HttpServletRequest request, HttpServletResponse response, Map<String, String> requestFields)
			throws Exception {
		try {
			HttpSession session = request.getSession();
			EmpDetails ed = (EmpDetails) session.getAttribute("EMP_DETAIL");
			db.updateLastRevisit(ed,null,"OFFLINE");
			session.setAttribute("EMP_DETAIL", null);
			session.invalidate();
			response.sendRedirect("start.jsp");
		} catch (IOException e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	
	private void manageUser(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		HttpSession session = getSession(request, request.getParameter("sessiodnId"));
		EmpDetails ed = (EmpDetails) session.getAttribute("EMP_DETAIL");
		try {
			List<EmpDetails> empList = db.getAllEmpDetails();
			session.setAttribute("ALLEMPLIST", empList);
			
			String str = new Gson().toJson(empList);
			response.getWriter().write(str);
		} catch (Exception e) {
			throw e;
		}finally{
			db.updateLastRevisit(ed,session.getId(), "ONLINE");
		}

	}
	
	private void searchUser(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		HttpSession session = getSession(request,
				request.getParameter("sessiodnId"));
		EmpDetails ed = (EmpDetails) session.getAttribute("EMP_DETAIL");
		try {
			String searchKey = request.getParameter("searchKey").toUpperCase();
			@SuppressWarnings("unchecked")
			List<EmpDetails> empList = (List<EmpDetails>) session.getAttribute("ALLEMPLIST");
			List<EmpDetails> tempEmpList = new ArrayList<EmpDetails>();
			if (!searchKey.equals("")) {

				for (int i = 0; i < empList.size(); i++) {
					EmpDetails tempED = empList.get(i);
					if (String.valueOf(tempED.getEmpId()).toUpperCase().contains(searchKey)
							|| tempED.getEmpName().toUpperCase().contains(searchKey)
							|| tempED.getEmpEmail().toUpperCase().contains(searchKey)) {
						tempEmpList.add(tempED);
					}
				}
			} else {
				tempEmpList = empList;
			}
			String str = new Gson().toJson(tempEmpList);
			response.getWriter().write(str);
		} catch (Exception e) {
			throw e;
		} finally {
			db.updateLastRevisit(ed,session.getId(), "ONLINE");
		}

	}
	
	private void updateUser(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		HttpSession session = getSession(request, request.getParameter("sessiodnId"));
		EmpDetails ed = (EmpDetails) session.getAttribute("EMP_DETAIL");
		try {
			
			EmpDetails emp = new EmpDetails();
			emp.setEmpId(Integer.parseInt(request.getParameter("empId")));
			emp.setEmpName(request.getParameter("empName"));
			emp.setEmpEmail(request.getParameter("empMail"));
			emp.setPassPhrase(request.getParameter("empPass"));
			emp.setEmpType(request.getParameter("empType"));
			emp.setPrefered_quizType(request.getParameter("prfrdType"));
			emp.setIsChangeRequired(request.getParameter("changeReqd"));
			

			int status = db.updateUserDetails(emp);
			String str = "";
			if (status != 0) {
				List<EmpDetails> empList = db.getAllEmpDetails();
				str = new Gson().toJson(empList);
			}
			response.getWriter().write(str);
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			db.updateLastRevisit(ed,session.getId(), "ONLINE");
		}

	}
	
	
	private void removeUser(HttpServletRequest request, HttpServletResponse response) throws Exception {

		HttpSession session = getSession(request, request.getParameter("sessiodnId"));
		EmpDetails ed = (EmpDetails) session.getAttribute("EMP_DETAIL");
		try {
			
			int empId = Integer.parseInt(request.getParameter("empId"));
			boolean status = db.removeUserDetails(empId);
			String str = "";
			List<EmpDetails> empList = db.getAllEmpDetails();
			str = new Gson().toJson(empList);

			response.getWriter().write(str);
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			db.updateLastRevisit(ed,session.getId(), "ONLINE");
		}
	}
	

	private void resolveFeedback(HttpServletRequest request, HttpServletResponse response)  throws Exception {
		try {
			int slNo = Integer.parseInt(request.getParameter("slNo"));
			int result = db.resolveFeedback(slNo);
			System.out.println(result);
		}catch (Exception e) {
			throw e;
		}
	}

	private void retriveFeedback(HttpServletRequest request, HttpServletResponse response) throws Exception {
		try {
			String feedback = request.getParameter("feedback");
			List<String> feddbacks = db.retrivrFeedback();
			PrintWriter out = response.getWriter();
			String str = new Gson().toJson(feddbacks);
			System.out.println(str);
			out.write(str);
		}catch (Exception e) {
			throw e;
		}
	}

	private void sendFeedback(HttpServletRequest request, HttpServletResponse response) throws Exception {
		HttpSession session = getSession(request, request.getParameter("sessiodnId"));
		try {
			String feedback = request.getParameter("feedback");
			EmpDetails ed = (EmpDetails) session.getAttribute("EMP_DETAIL");
			int result = db.submitFeedback(ed.getEmpId(),feedback);
			response.getWriter().write("Feedback submitted Successfully");
		}catch (Exception e) {
			throw e;
		}
		
	}

	private void getNoofQstn(HttpServletRequest request, HttpServletResponse response) throws Exception {

		try {
			String quizNo = request.getParameter("quizNo");
			String noOfQstn = db.getTotalNoOfQstn(quizNo);
			response.getWriter().write(noOfQstn);
		}catch (Exception e) {
			throw e;
		}
	}

	private HttpSession getSession(HttpServletRequest request, String SID) throws Exception {
		
		//String SID = (String) requestFields.get("sessiodnId");
		HttpSession session = request.getSession(false);
		
		if (SID!=null && !SID.equals(session.getId())) {
			System.out.println("RQST :: "+SID +"  SSID:: "+session.getId());
			//session.invalidate();
			request.setAttribute("sessiodnId", null);		
			throw new Exception("Session invalid.");
		} else{
			request.setAttribute("SESSIONID", session.getId());
		}
		return session;

	}
	
	protected void uploadExcelFile(HttpServletRequest request,
			HttpServletResponse response,List<FileItem> formItems) throws Exception {
		HttpSession session = getSession(request,request.getParameter("sessiodnId"));
		EmpDetails ed = (EmpDetails) session.getAttribute("EMP_DETAIL");
		try{
			
		String msg = "";
		List<String> errList = null;
		
		String uploadPath = getServletContext().getRealPath("")+File.separator+CommonFns.EXCEL_FOLDER;
		//String uploadPath = ConfigFile.getKey("folder.excel")+CommonFns.EXCEL_FOLDER;
		
		
		File uploadDir = new File(uploadPath);
		if (!uploadDir.exists()) {
			uploadDir.mkdir();
		}
		
		if (formItems != null && formItems.size() > 0) {
			for (FileItem item : formItems) {
				String fileName;
				String extn;
				String pathwithName;
				// processes only fields that are not form fields
				if (!item.isFormField()) {
					fileName = new File(item.getName()).getName();
					extn = fileName.substring(fileName.lastIndexOf("."), fileName.length());
					
						if (extn.equalsIgnoreCase(CommonFns.EXCEL_EXTN)) {
							//pathwithName = uploadPath + File.separator + fileName;
							pathwithName = uploadPath + File.separator + ed.getEmpId()+"_"+System.currentTimeMillis()+extn.toLowerCase();
							
							File storeFile = new File(pathwithName);
							// saves the file on disk
							item.write(storeFile);
							
							if(ed.getEmpType().equals("A")){
								// Process and Save Excel Data
								int noOfRecords = ImportQstnExcel.processAndSaveExcelData(pathwithName);
								
								// Delte upon Processing
								storeFile.delete();
								if (noOfRecords > 0)
									msg = "FIle Processed Successfully";
								else
									msg = "Error Occured While processing file.";

							}else{
								msg = "File Uploaded and send to Admin.";
							}

							
						}else{
							msg = "Only " + CommonFns.EXCEL_EXTN + " file allowed";
					}
					
				}
			}
		}else{
			msg = "No Image found to upload";
		}
		
		request.setAttribute("MSG", msg );
		request.setAttribute("ERROR", errList );
		
		if(ed.getEmpType().equals("A"))
			request.getRequestDispatcher("quizHomeAdmin.jsp").forward(request, response);
		else
			request.getRequestDispatcher("quizHome.jsp").forward(request, response);
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
			db.updateLastRevisit(ed,session.getId(), "ONLINE");
		}
	}


	private void uploadImage(HttpServletRequest request,
			HttpServletResponse response, List<FileItem> formItems, Map<String, String> requestFields) throws Exception {
		HttpSession session = getSession(request, requestFields.get("sessiodnId"));
		String msg = "";
		QuestionBean qb = (QuestionBean) session.getAttribute("QUESTION_TO_EDIT");
		QuizEmpDetails qed = (QuizEmpDetails) session.getAttribute("QUIZ_DETAILS");
		
		// Fetch form input Data
		
		qb.setQuestion_Txt(requestFields.get(""));
		qb.setQuestion_Txt(requestFields.get("question"));
		qb.setOption1(requestFields.get("option1"));
		qb.setOption2( requestFields.get("option2"));
		qb.setOption3(requestFields.get("option3"));
		qb.setOption4(requestFields.get("option4"));
		qb.setAnswer(requestFields.get("chk_ans"));
		qb.setComments(requestFields.get("comments"));
		
		String imgName = qed.getQuiz_nbr()+"_"+qb.getQuestion_Nbr();
		
		log.info("COntext Path :: "+getServletContext().getRealPath(""));
		String uploadPath = getServletContext().getRealPath("")+File.separator+CommonFns.IMG_FOLDER;
		System.out.println("uploadPath :: "+uploadPath);
		log.info("uploadPath :: "+uploadPath);
		
		File uploadDir = new File(uploadPath);
		if (!uploadDir.exists()) {
			uploadDir.mkdir();
		}
		
		if (formItems != null && formItems.size() > 0) {
			for (FileItem item : formItems) {
				String fileName;
				String extn;
				String pathwithName;
				// processes only fields that are not form fields
				if (!item.isFormField()) {
//					System.out.println("File field " + item.getName() + " with file name " + item.getName() + " detected.");
					
					fileName = new File(item.getName()).getName();
					extn = fileName.substring(fileName.lastIndexOf("."), fileName.length());
					
					if (extn.equalsIgnoreCase(CommonFns.IMG_EXTN)) {
						pathwithName = uploadPath + File.separator + imgName + extn;
						File storeFile = new File(pathwithName);
						// saves the file on disk
						item.write(storeFile);
						//Compress File if size is more than 100KB
						String toCompress = ConfigFile.getKey("key.compressImg");
						log.info("Image Compression Flag :: "+toCompress);
						if (toCompress.equals("Y")) {
							if (storeFile.length() / 1024 > 100)
								ImageManager.compressImage(pathwithName, pathwithName);
						}
						qb.setQuestion_Img(imgName+ extn);
						session.setAttribute("QUESTION_TO_EDIT",qb);
						msg = "Image Uploaded";
					}else{
						msg = "Only " + CommonFns.IMG_EXTN + " file allowed";
					}
				}
			}
		}else{
			msg = "No Image found to upload";
		}
		request.setAttribute("MSG", msg);
		request.getRequestDispatcher("QstnPage.jsp").forward(request, response);
	}

	private void themeSelector(HttpServletRequest request, HttpServletResponse response, Map<String, String> requestFields) throws Exception {
		HttpSession session = getSession(request, requestFields.get("sessiodnId"));
		EmpDetails ed = (EmpDetails) session.getAttribute("EMP_DETAIL");
		try {
			
			ed.setTheme(requestFields.get("themeSelector"));
			int status = db.saveThemeDetails(ed);
			if (status != 0)
				session.setAttribute("EMP_DETAIL", ed);

			if (ed.getEmpType().equals("A")) {
				// Get Quiz Details
				request.getRequestDispatcher("quizHomeAdmin.jsp").forward(
						request, response);
			} else {
				// Get Quiz Details
				request.getRequestDispatcher("quizHome.jsp").forward(request,
						response);
			}
		} catch (Exception e) {
			throw e;
		}finally{
			db.updateLastRevisit(ed,session.getId(), "ONLINE");
		}

	}


	//Login for both Admin / Normal User

	
	private void backToHome(HttpServletRequest request,
			HttpServletResponse response, Map<String, String> requestFields)
			throws Exception {
		HttpSession session = getSession(request, requestFields.get("sessiodnId"));
		EmpDetails ed = (EmpDetails) session.getAttribute("EMP_DETAIL");
		try {
			if (ed.getEmpType().equals("A")) {
				request.getRequestDispatcher("quizHomeAdmin.jsp").forward( request, response);
			} else {
				request.getRequestDispatcher("quizHome.jsp").forward(request, response);
			}
		} catch (Exception e) {
			throw e;
		} finally {
			db.updateLastRevisit(ed,session.getId(), "ONLINE");
		}
	}
	
	
	
	
	// Admin Functionality
	
	//Add Quiz set
	private void addQuizSet(HttpServletRequest request,
			HttpServletResponse response, Map<String, String> requestFields) throws Exception {
		HttpSession session = getSession(request, requestFields.get("sessiodnId"));
		EmpDetails ed = (EmpDetails) session.getAttribute("EMP_DETAIL");
		try {
			
			QuizDetails qd = new QuizDetails();
			String quizType = requestFields.get("quizType");
			if(quizType.equals("NEW"))
				quizType = requestFields.get("newType");
			qd.setQuizType(quizType);
			qd.setStart_Dt(requestFields.get("start_dt")+CommonFns.startDtTime);
			qd.setEnd_Dt(requestFields.get("end_dt")+CommonFns.endDtTime);
			
			
			
			int quizNo =  db.addNewQuizSet(ed,qd);
			
			//Refresh Emp Details
			ed = db.getQuizDetailsforAdminUser(ed);
			session.setAttribute("EMP_DETAIL", ed);
			
			QuizEmpDetails qed = db.getQuizInfoByQuizNbr(ed, quizNo);
			session.setAttribute("QUIZ_DETAILS", qed);
			/////
			session.setAttribute("QUESTION_LIST_EDIT", new ArrayList());
			QuestionBean qb = new QuestionBean();
			qb.setQuestion_Nbr(1);
			session.setAttribute("QUESTION_TO_EDIT", qb);
			request.getRequestDispatcher("QstnPage.jsp").forward(request, response);
			
		} catch (Exception e) {
			throw e;
		}finally {
			db.updateLastRevisit(ed,session.getId(), "ONLINE");
		}
		
	}
	
	private void createSetFromDump(HttpServletRequest request,
			HttpServletResponse response, Map<String, String> requestFields) throws Exception {
		HttpSession session = getSession(request, requestFields.get("sessiodnId"));
		EmpDetails ed = (EmpDetails) session.getAttribute("EMP_DETAIL");
		try {
			QuizDetails qd = new QuizDetails();
			qd.setQuizType(requestFields.get("d_quizType"));
			qd.setStart_Dt(requestFields.get("d_start_dt")+CommonFns.startDtTime);
			qd.setEnd_Dt(requestFields.get("d_end_dt")+CommonFns.endDtTime);
			int noOfQstn = Integer.parseInt(requestFields.get("d_noOfQstn"));
			
			
			int quizNo =  db.createQstnSetFromDump(qd,noOfQstn,ed);
			System.out.println(quizNo);
			//Refresh Emp Details
			ed = db.getQuizDetailsforAdminUser(ed);
			ed.setDumpSets(db.countDumpQstns());
			session.setAttribute("EMP_DETAIL", ed);
			
			String msg = "Quiz:: "+quizNo +" created Successfully. <a class=\"editQuizSet\" id="+quizNo +">Click here </a> to edit Quiz Set";
			request.setAttribute("MSG", msg);
			request.getRequestDispatcher("quizHomeAdmin.jsp").forward(request, response);
			
		} catch (Exception e) {
			throw e;
		}finally {
			db.updateLastRevisit(ed,session.getId(), "ONLINE");
		}
		
	}
	
	private void getQstnFromDump(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		HttpSession session = getSession(request, request.getParameter("sessiodnId"));
		
		EmpDetails ed = (EmpDetails) session.getAttribute("EMP_DETAIL");
		QuizEmpDetails qed = (QuizEmpDetails) session.getAttribute("QUIZ_DETAILS");
		
		QuestionBean qb = db.selectQstnFromDump(qed.getQuizDtl().getQuizType());
		PrintWriter out = response.getWriter();
		String str = "NQ";
		if(qb!=null)
			str = new Gson().toJson(qb);
		//System.out.println(str);
		out.write(str);
		
	}

	
	// Edit Quiz Set
	private void editQuizSet(HttpServletRequest request,
			HttpServletResponse response, Map<String, String> requestFields) throws Exception {
		HttpSession session = getSession(request, requestFields.get("sessiodnId"));
		EmpDetails ed = (EmpDetails)session.getAttribute("EMP_DETAIL");
		try {
			int quizNo =  Integer.parseInt(requestFields.get("quizNo"));
			List qbList = db.getQuizSetByQuizNbr(quizNo);
			session.setAttribute("QUESTION_LIST_EDIT", qbList);
			
			
			QuizEmpDetails qed = db.getQuizInfoByQuizNbr(ed, quizNo);
			session.setAttribute("QUIZ_DETAILS", qed);
			
			QuestionBean qb = new QuestionBean();
			if(qbList.size()>0)
				qb = (QuestionBean) qbList.get(0);
			else{
				qb.setQuestion_Nbr(1);
				qb.setQuiz_nbr(quizNo);
			}
			session.setAttribute("QUESTION_TO_EDIT", qb);
			request.getRequestDispatcher("QstnPage.jsp").forward(request, response);
			
		} catch (Exception e) {
			throw e;
		}finally {
			db.updateLastRevisit(ed,session.getId(), "ONLINE");
		}
		
	}
	
	
	// Change quiz Date
	private void changeQuizDate(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		HttpSession session = getSession(request, request.getParameter("sessiodnId"));
		EmpDetails ed = (EmpDetails)session.getAttribute("EMP_DETAIL");
		try {
			String start_dt = request.getParameter("start_dt");
			start_dt += CommonFns.startDtTime;
			String end_dt = request.getParameter("end_dt");
			end_dt += CommonFns.endDtTime;

			Date sDt = CommonFns.getDateFromString(start_dt);
			Date eDt = CommonFns.getDateFromString(end_dt);

			String msg = "Details didn't save Sussessfully";
			if (sDt.compareTo(eDt) < 0) {
				QuizEmpDetails qed = (QuizEmpDetails) session.getAttribute("QUIZ_DETAILS");

				int status = db.changeQuizInformation(qed.getQuiz_nbr(), start_dt, end_dt);
				
				if (status != 0) {
					// refresh emp details
					ed = db.getQuizDetailsforAdminUser(ed);
					session.setAttribute("EMP_DETAIL", ed);

					// Refresh Quiz Details
					qed = db.getQuizInfoByQuizNbr(ed, qed.getQuiz_nbr());
					session.setAttribute("QUIZ_DETAILS", qed);
					msg = "Details changed Sussessfully";
				}
			} else {
				msg = "Start Date should be less than End Date";
			}
			
			response.getWriter().write(msg);

		}catch (Exception e) {
			throw e;
		}finally {
			db.updateLastRevisit(ed,session.getId(), "ONLINE");
		}
		
	}

	// Show blank question 
	private void addQuestion(HttpServletRequest request,
			HttpServletResponse response, Map<String, String> requestFields) throws Exception {
		HttpSession session = getSession(request, requestFields.get("sessiodnId"));
		EmpDetails ed = (EmpDetails)session.getAttribute("EMP_DETAIL");
		try {
			
			QuizEmpDetails qed = (QuizEmpDetails) session.getAttribute("QUIZ_DETAILS");
			
			//int quiz_nbr = Integer.parseInt(requestFields.get("quizNo"));
			List qbList = (List) session.getAttribute("QUESTION_LIST_EDIT");
			
			QuestionBean qb = new QuestionBean();
			qb.setQuestion_Nbr(qbList.size()+1);
			qb.setQuiz_nbr(qed.getQuiz_nbr());
			session.setAttribute("QUESTION_TO_EDIT", qb);
			
			request.getRequestDispatcher("QstnPage.jsp").forward(request, response);
			
		} catch (Exception e) {
			throw e;
		}finally {
			db.updateLastRevisit(ed,session.getId(), "ONLINE");
		}
		
	}
	
	// Save question when Save button is Clicked
	
	private void saveQuestion(HttpServletRequest request,
			HttpServletResponse response, Map<String, String> requestFields) throws Exception {
		HttpSession session = getSession(request, requestFields.get("sessiodnId"));
		EmpDetails ed = (EmpDetails)session.getAttribute("EMP_DETAIL");
		try {

			
			QuestionBean qb = (QuestionBean) session.getAttribute("QUESTION_TO_EDIT");
			QuizEmpDetails qed = (QuizEmpDetails) session.getAttribute("QUIZ_DETAILS");
			
			QuestionBean qbSave = new QuestionBean();
			qbSave.setQuiz_nbr(qed.getQuiz_nbr());
			qbSave.setQuestion_Nbr(qb.getQuestion_Nbr());
			qbSave.setQuestion_Txt(requestFields.get("question"));
			qbSave.setOption1(requestFields.get("option1"));
			qbSave.setOption2( requestFields.get("option2"));
			qbSave.setOption3(requestFields.get("option3"));
			qbSave.setOption4(requestFields.get("option4"));
			qbSave.setAnswer(requestFields.get("chk_ans"));
			qbSave.setComments(requestFields.get("comments"));
			qbSave.setQuestion_Img(requestFields.get("qstnImage"));
			//Save to Database
			int result = db.saveQuestionToDB(qbSave);
			
			if(result!=0){
				//Delete Qstn From Dump if used
				/*int dump_qstn_nbr = requestFields.get("dump_question_Nbr").equals("")?0:Integer.parseInt(requestFields.get("dump_question_Nbr"));
				if(dump_qstn_nbr != 0 )
					db.deleteQstnFromDUmp(dump_qstn_nbr);*/
				
				//Refresh Question Set
				List qbList = db.getQuizSetByQuizNbr(qed.getQuiz_nbr());
				session.setAttribute("QUESTION_LIST_EDIT", qbList);
				qbSave = (QuestionBean) qbList.get(qb.getQuestion_Nbr()-1);
				session.setAttribute("QUESTION_TO_EDIT", qbSave);
				
				request.setAttribute("MSG", "Question Saved Successfully");
			}else{
				request.setAttribute("MSG", "Question could not be Saved");
			}
			
			request.getRequestDispatcher("QstnPage.jsp").forward(request,response);
		} catch (Exception e) {
			throw e;
		}finally {
			db.updateLastRevisit(ed,session.getId(), "ONLINE");
		}
		
	}
	
	// Alter status if Quiz Publish or Unpublish
	
	private void alterStatusOfQuiz(HttpServletRequest request,
			HttpServletResponse response, Map<String, String> requestFields) throws Exception {
		HttpSession session = getSession(request, requestFields.get("sessiodnId"));
		EmpDetails ed = (EmpDetails) session.getAttribute("EMP_DETAIL");
		try {
			String msg = "";
			
			QuizEmpDetails qed = (QuizEmpDetails) session.getAttribute("QUIZ_DETAILS");
			//Save to Database
			
			String statusVal = qed.getQuizDtl().getIsPublished().equals("Y")?"N":"Y";
			int status =0;
			//Can publish/Un-publish before start date only
			if(CommonFns.compareWithToday(qed.getQuizDtl().getStart_Dt())<0){
				status = db.altetQuizStatus(qed.getQuiz_nbr(),statusVal);
				//refresh emp details
				ed = db.getQuizDetailsforAdminUser(ed);
				session.setAttribute("EMP_DETAIL", ed);
				//Refresh Quiz Details
				qed = db.getQuizInfoByQuizNbr(ed, qed.getQuiz_nbr());
				session.setAttribute("QUIZ_DETAILS", qed);
				
				msg = "Quiz "+ (statusVal.equals("Y") ? "Published" : "Unpublished") + " Successfully.";
				
			}else{
				msg = "Can not Publish/Unpublish Once quiz start Date has Passed.";
				System.out.println(msg);
			}
			
			request.setAttribute("MSG", msg);
			request.getRequestDispatcher("QstnPage.jsp").forward(request,response);
		}  catch (Exception e) {
			throw e;
		}finally {
			db.updateLastRevisit(ed,session.getId(), "ONLINE");
		}
		
	}
	
	// Traverse question set/ View purpose
	private void showQuestion(HttpServletRequest request,
			HttpServletResponse response, Map<String, String> requestFields) throws Exception {
		HttpSession session = getSession(request, requestFields.get("sessiodnId"));
		EmpDetails ed = (EmpDetails) session.getAttribute("EMP_DETAIL");
		
		try {
			int qstn_nbr = Integer.parseInt(requestFields.get("qstn_nbr"));
			List qbList = (List) session.getAttribute("QUESTION_LIST_EDIT");
			QuestionBean qb = (QuestionBean) qbList.get(qstn_nbr-1);

			session.setAttribute("QUESTION_TO_EDIT", qb);
			request.getRequestDispatcher("QstnPage.jsp").forward(request, response);
			
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}finally {
			db.updateLastRevisit(ed,session.getId(), "ONLINE");
		}
		
	}
	
	// Result Declaration
	
	private void goToResultPage(HttpServletRequest request,
			HttpServletResponse response, Map<String, String> requestFields) throws Exception {
		HttpSession session = getSession(request, requestFields.get("sessiodnId"));
		EmpDetails ed = (EmpDetails) session.getAttribute("EMP_DETAIL");
		try {
			int quizNo =  Integer.parseInt(requestFields.get("quizNo"));
			
			
			QuizEmpDetails qed = db.getQuizInfoByQuizNbr(ed, quizNo);
			session.setAttribute("QUIZ_DETAILS", qed);
			
			List<QuizEmpDetails> quizEmpList = null;
			
				quizEmpList = db.viewdeclareResult(quizNo);
//				quizEmpList = db.getAttemptedUserDetails(quizNo);
//			
			
			session.setAttribute("RESULT_LIST", quizEmpList);
			request.setAttribute("QUIZ_NO", quizNo);
			
			// Refresh Details
			ed = db.getQuizDetailsforAdminUser(ed);
			session.setAttribute("EMP_DETAIL",ed);
			
			request.getRequestDispatcher("resultPage.jsp").forward(request, response);
			
		} catch (Exception e) {
			throw e;
		}finally {
			db.updateLastRevisit(ed,session.getId(), "ONLINE");
		}
		
	}
	
	
	private void calculateResult(HttpServletRequest request,
			HttpServletResponse response, Map<String, String> requestFields) throws Exception {
		HttpSession session = getSession(request, requestFields.get("sessiodnId"));
		EmpDetails ed = (EmpDetails) session.getAttribute("EMP_DETAIL");
		try {
			
			QuizEmpDetails qed = (QuizEmpDetails) session.getAttribute("QUIZ_DETAILS");
			int quizNo =  qed.getQuiz_nbr();
			
			ed = db.getQuizDetailsforAdminUser(ed);
			qed = db.getQuizInfoByQuizNbr(ed, quizNo);
			
			db.calculateResult(quizNo);
			
			List<QuizEmpDetails> quizEmpList = db.viewdeclareResult(quizNo);
			request.setAttribute("RESULT_LIST", quizEmpList);
			request.setAttribute("QUIZ_NO", quizNo);
			
			// Refresh Details
			session.setAttribute("EMP_DETAIL",ed);
			
			request.getRequestDispatcher("resultPage.jsp").forward(request, response);
			
		} catch (Exception e) {
			throw e;
		}finally {
			db.updateLastRevisit(ed,session.getId(), "ONLINE");
		}
		
	}

	private void markQuizAsDeclared(HttpServletRequest request, HttpServletResponse response) throws Exception {
		HttpSession session = getSession(request, request.getParameter("sessiodnId"));
		EmpDetails ed = (EmpDetails) session.getAttribute("EMP_DETAIL");
		try {
			
			QuizEmpDetails qed = (QuizEmpDetails) session.getAttribute("QUIZ_DETAILS");
			int quizNo =  qed.getQuiz_nbr();
			//db.markQuizDeclared(quizNo);
			
			// Send Result Mail
			List<QuizEmpDetails> quizEmpList = (List<QuizEmpDetails>) session.getAttribute("RESULT_LIST");
			if(ConfigFile.getKey("key.sendmail").equals("Y")){
				if(quizEmpList.size()>0){
					VelocityHelperClass vc = new VelocityHelperClass(quizEmpList,quizNo);
					
					String vm = ConfigFile.getKey("vm.quizResult");
					
					String str = VelocityHtml.getHtmlTepmlate(vc,vm);
					
					System.out.println(str);
					SendEmail.sendmail(str);
				}
			}
			
			response.getWriter().write("Quiz Marked as Declared. A Mail notification will be sent.");
			
		} catch (Exception e) {
			throw e;
		}finally {
			db.updateLastRevisit(ed,session.getId(), "ONLINE");
		}
		
	}
	
	
	
	
	//////////////////////////////////

	
	// Normal User functionality
	
	// Save email details for first time user
	
	private void saveUserDetails(HttpServletRequest request,
			HttpServletResponse response, Map<String, String> requestFields) throws Exception {
		HttpSession session = getSession(request, requestFields.get("sessiodnId"));
		EmpDetails ed = (EmpDetails) session.getAttribute("EMP_DETAIL");
		try {
			String emailAdd = requestFields.get("emailAdd");
			String empName = requestFields.get("empName");
			String quizType = requestFields.get("quizType");
			
			ed.setEmpEmail(emailAdd);
			ed.setEmpName(empName);
			ed.setPrefered_quizType(quizType);
			int status = db.saveNormalUserDetail(ed);
			
			if(status!=0){
				ed.setEmpEmail(emailAdd);
				ed.setEmpName(empName);
				ed.setIsChangeRequired("N");
			}
			
			session.setAttribute("EMP_DETAIL",ed);
			request.setAttribute("MSG", "Details saved successfully");
			request.getRequestDispatcher("quizHome.jsp").forward(request,response);
			
		} catch (Exception e) {
			throw e;
		}finally {
			db.updateLastRevisit(ed,session.getId(), "ONLINE");
		}
		
	}
	
	/*
	 * Appear for Test
	 * Details will be saved only when saved or submitted 
	 */
	
	private void giveTest(HttpServletRequest request,
			HttpServletResponse response, Map<String, String> requestFields) throws Exception {
		HttpSession session = getSession(request, requestFields.get("sessiodnId"));
		EmpDetails ed = (EmpDetails) session.getAttribute("EMP_DETAIL");
		
		try {
			
			int quizNo = Integer.parseInt(requestFields.get("quizNo"));
			
			
			QuizEmpDetails qed = db.getQuizInfoByQuizNbr(ed,quizNo);
			session.setAttribute("QUIZ_DETAILS", qed);
			
			
			List qbList = db.getQuestionsByQuizNbrForNormalUser(ed.getEmpId(),quizNo);
			
			session.setAttribute("QUESTION_LIST", qbList);
			
			request.getRequestDispatcher("QstnPageForUser.jsp").forward(request, response);
			
		} catch (Exception e) {
			throw e;
		}finally {
			db.updateLastRevisit(ed,session.getId(), "ONLINE");
		}
	}
	
	/*
	 * For saving the Test. Later user can submit.
	 * User need to submit to finish the test.
	 * 
	 */

	private void saveQuizAnswer(HttpServletRequest request,
			HttpServletResponse response, Map<String, String> requestFields) throws Exception {
		HttpSession session = getSession(request, requestFields.get("sessiodnId"));
		EmpDetails ed = (EmpDetails) session.getAttribute("EMP_DETAIL");
		try {
			
			QuizEmpDetails qed = (QuizEmpDetails)session.getAttribute("QUIZ_DETAILS");
			List qbList = (List)session.getAttribute("QUESTION_LIST");
			
			// Set the answer
			for(int i=0;i<qbList.size();i++ ){
				((QuestionBean) qbList.get(i)).setUser_ans(requestFields.get("answer_chkbx_qstn_"+(i+1)));
			}
			
			// Save to database
			int status = db.submitQuizAnswer(ed,qbList);
			
			if(status!=0){
				
				// Refresh the Question details
				qbList = db.getQuestionsByQuizNbrForNormalUser(ed.getEmpId(),qed.getQuiz_nbr());
				session.setAttribute("QUESTION_LIST",qbList);
				
				//Refresh user details
				ed = db.getQuizDetailsforNormalUser(ed);
				session.setAttribute("EMP_DETAIL",ed);
				
				request.setAttribute("MSG", "Details saved Successfully");
			}else{
				request.setAttribute("MSG", "Details could not be saved");
			}			
			
			request.getRequestDispatcher("QstnPageForUser.jsp").forward(request,response);
			
		} catch (Exception e) {
			throw e;
		}finally {
			db.updateLastRevisit(ed,session.getId(), "ONLINE");
		}
	}
	
	// Final Submission of Test
	private void finalSubmitAns(HttpServletRequest request, 
			HttpServletResponse response, Map<String, String> requestFields) throws Exception {
		HttpSession session = getSession(request, requestFields.get("sessiodnId"));
		EmpDetails ed = (EmpDetails) session.getAttribute("EMP_DETAIL");
		try {
			
			QuizEmpDetails qed = (QuizEmpDetails)session.getAttribute("QUIZ_DETAILS");
			
			List qbList = (List)session.getAttribute("QUESTION_LIST");
			
			// Set the answer
			for(int i=0;i<qbList.size();i++ ){
				((QuestionBean) qbList.get(i)).setUser_ans(requestFields.get("answer_chkbx_qstn_"+(i+1)));
			}
			
			// Save answer
			int status = db.submitQuizAnswer(ed,qbList);
			
			// Calculate Score
			String score = CommonFns.scoreCalculation(qbList);
			
			// Set Status for final Submission
			status = db.finalAnsSubmit(ed,qed.getQuiz_nbr(),score);
			
			if(status!=0){
				qbList = db.getQuestionsByQuizNbrForNormalUser(ed.getEmpId(),qed.getQuiz_nbr());			
				session.setAttribute("QUESTION_LIST",qbList);
				
				//Refresh user details
				ed = db.getQuizDetailsforNormalUser(ed);
				session.setAttribute("EMP_DETAIL",ed);
				request.setAttribute("MSG", "Quiz submitted Successfully");
			}else{
				request.setAttribute("MSG", "Details could not be saved");
			}	
			
			// Calculate Score
//			String score = CommonFns.scoreCalculation(qbList);
//			request.setAttribute("SCORE", score);
			
			qed = db.getQuizInfoByQuizNbr(ed, qed.getQuiz_nbr());
			session.setAttribute("QUIZ_DETAILS", qed);
			
			request.getRequestDispatcher("QstnPageForUser.jsp").forward(request,response);
			
		} catch (Exception e) {
			throw e;
		}finally {
			db.updateLastRevisit(ed,session.getId(), "ONLINE");
		}
	}
	
	
	// View Result of test
	private void viewResult(HttpServletRequest request,
			HttpServletResponse response, Map<String, String> requestFields) throws Exception {
		HttpSession session = getSession(request, requestFields.get("sessiodnId"));
		EmpDetails ed = (EmpDetails) session.getAttribute("EMP_DETAIL");
		try {
			int quizNo = Integer.parseInt(requestFields.get("quizNo"));
			
			QuizEmpDetails qed = db.getQuizInfoByQuizNbr(ed,quizNo);
			session.setAttribute("QUIZ_DETAILS", qed);
			
			List qbList =db.getQuestionsByQuizNbrForNormalUser(ed.getEmpId(),qed.getQuiz_nbr());
			
			session.setAttribute("QUESTION_LIST", qbList);
			
			request.getRequestDispatcher("QstnPageForUser.jsp").forward(request, response);
			
		} catch (Exception e) {
			throw e;
		}finally {
			db.updateLastRevisit(ed,session.getId(), "ONLINE");
		}
	}
	
	

}
