package db;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import util.CommonFns;
import util.ConfigFile;
import bean.EmpDetails;
import bean.QuestionBean;
import bean.QuizDetails;
import bean.QuizEmpDetails;

public class DBManager {
	static Logger log = Logger.getLogger(DBManager.class);
	
	private Connection con = null;
	private Statement st = null;
	private PreparedStatement ps = null;
	private ResultSet rs = null;

	public DBManager() {
		// con = ConnectionManager.getConnection();
	}


	public int saveQuestionToDB(QuestionBean qb) throws Exception {
		log.info("DBManager.saveQuestionToDB()");
		int result = 0;
		int status = 0;
		List qbList = null;
		try {
			con = ConnectionManager.getConnection();
			st = con.createStatement();
			
			String qry = "SELECT COUNT(1) FROM QSTN_TBL WHERE QUIZ_NBR ="+qb.getQuiz_nbr()+" AND QSTN_NBR="+qb.getQuestion_Nbr();
			
			rs = st.executeQuery(qry);
			while (rs.next()) {
				status = rs.getInt(1);
			}
			
			if(status==0){
				ps = con
						.prepareStatement("INSERT INTO QSTN_TBL(QUIZ_NBR,QSTN_NBR,QSTN_TXT,OPT_1,OPT_2,OPT_3,OPT_4,ANSWER,COMMENTS,QSTN_IMG) VALUES( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
			
				ps.setInt(1, qb.getQuiz_nbr());
				ps.setInt(2, qb.getQuestion_Nbr());
				ps.setString(3, qb.getQuestion_Txt());
				ps.setString(4, qb.getOption1());
				ps.setString(5, qb.getOption2());
				ps.setString(6, qb.getOption3());
				ps.setString(7, qb.getOption4());
				ps.setString(8, qb.getAnswer());
				ps.setString(9, qb.getComments());
				ps.setString(10, qb.getQuestion_Img());
				result = ps.executeUpdate();
				con.commit();
			}else{
				ps = con
						.prepareStatement("UPDATE QSTN_TBL SET QSTN_TXT = ?,OPT_1= ?,OPT_2= ?,OPT_3= ?,OPT_4= ?,ANSWER= ?,COMMENTS= ?,QSTN_IMG= ? WHERE QUIZ_NBR =? AND QSTN_NBR = ?");
				
				ps.setString(1, qb.getQuestion_Txt());
				ps.setString(2, qb.getOption1());
				ps.setString(3, qb.getOption2());
				ps.setString(4, qb.getOption3());
				ps.setString(5, qb.getOption4());
				ps.setString(6, qb.getAnswer());
				ps.setString(7, qb.getComments());
				ps.setString(8, qb.getQuestion_Img());
				ps.setInt(9, qb.getQuiz_nbr());
				ps.setInt(10, qb.getQuestion_Nbr());
				result = ps.executeUpdate();
				con.commit();
				
			}
		} catch (Exception e) {
			con.rollback();
			throw e;
		} finally {
			close();
		}
		return result;
	}

	public int altetQuizStatus(int quizNo, String quizStatus) throws Exception {
		log.info("DBManager.altetQuizStatus()");
		int status = 0;
		
		con = ConnectionManager.getConnection();
		String qry = "UPDATE QUIZ_TM_TBL SET IS_PUBLISH = ?, SHOW_QUIZ_NBR=? WHERE QUIZ_NBR = ?";
		
		String show_quiz_Nbr_qry = (quizStatus.equals("Y"))?getMaxSHowQUizNumber():""; 
		
		try {
			ps = con.prepareStatement(qry);
			ps.setString(1, quizStatus);
			ps.setString(2, show_quiz_Nbr_qry);			
			ps.setInt(3, quizNo);
			status = ps.executeUpdate();
			con.commit();
		} catch (Exception e) {
			con.rollback();
			throw e;
		} finally {
			close();
		}
		return status;
	}
	
	private void close() throws SQLException {
		if (rs != null)
			rs.close();
		if (st != null)
			st.close();
		if (con != null)
			con.close();
	}


	public int addNewQuizSet(EmpDetails ed, QuizDetails qd) throws Exception {
		log.info("DBManager.addNewQuizSet()");
		int result = 0;
		int maxSeqNo = 0;
		try {
			con = ConnectionManager.getConnection();
			st = con.createStatement();
			
			String qry = "SELECT MAX(QUIZ_NBR) FROM QUIZ_TM_TBL";
			
			rs = st.executeQuery(qry);
			while (rs.next()) {
				maxSeqNo = rs.getInt(1);
			}
			
			ps = con
					.prepareStatement("INSERT INTO QUIZ_TM_TBL (QUIZ_NBR,START_DT,END_DT,RESULT_DECLARED,QUIZ_TYPE,PREPARED_BY) VALUES( ?, ?, ?, ?, ?, ?)");
			ps.setInt(1, maxSeqNo+1);
			ps.setString(2,qd.getStart_Dt());
			ps.setString(3, qd.getEnd_Dt());
			ps.setString(4, "N");
			ps.setString(5, qd.getQuizType());
			ps.setInt(6, ed.getEmpId());
			result = ps.executeUpdate();
			con.commit();
		} catch (Exception e) {
			con.rollback();
			throw e;
		} finally {
			close();
		}
		return maxSeqNo+1;
	}
	
	
	public int changeQuizInformation(int quizNo, String start_dt, String end_dt) throws Exception {
		log.info("DBManager.changeQuizInformation()");
		int result = 0;
		try {
			con = ConnectionManager.getConnection();
			ps = con.prepareStatement("UPDATE QUIZ_TM_TBL SET START_DT = ? ,END_DT = ? WHERE QUIZ_NBR = ? ");
			ps.setString(1, start_dt);
			ps.setString(2, end_dt);
			ps.setInt(3, quizNo);
			result = ps.executeUpdate();
			con.commit();
		} catch (Exception e) {
			e.printStackTrace();
			con.rollback();
			throw e;
		} finally {
			close();
		}
		return result;
	}
	
	public QuizEmpDetails getQuizInfoByQuizNbr(EmpDetails ed,int quizNo) throws Exception {
		log.info("DBManager.getQuizInfoByQuizNbr()");
		QuizEmpDetails qed = new QuizEmpDetails();
		
		con = ConnectionManager.getConnection();
		try {
		st = con.createStatement();
		String qry = "SELECT QTT.QUIZ_NBR,QTT.SHOW_QUIZ_NBR,TO_CHAR(START_DT,'DD-MON-YYYY HH:MI:SS AM') START_DT,TO_CHAR(END_DT,'DD-MON-YYYY HH:MI:SS AM') END_DT," +
				"RESULT_DECLARED, QUIZ_TYPE, PREPARED_BY,IS_PUBLISH FROM QUIZ_TM_TBL QTT WHERE QTT.QUIZ_NBR="+quizNo ;
		
		////System.out.println("getQuizInfoByQuizNbr 1: "+qry);
		
		rs = st.executeQuery(qry);
		while (rs.next()) {
			
			qed.setQuiz_nbr(rs.getInt("QUIZ_NBR"));
			QuizDetails qd = new QuizDetails();
			qd.setQuiz_nbr(rs.getInt("QUIZ_NBR"));
			qd.setShow_quiz_nbr(CommonFns.NVL(rs.getString("SHOW_QUIZ_NBR")));
			qd.setStart_Dt(CommonFns.NVL(rs.getString("START_DT")));
			qd.setEnd_Dt(CommonFns.NVL(rs.getString("END_DT")));
			qd.setIsResultDeclared(CommonFns.NVL(rs.getString("RESULT_DECLARED")));
			qd.setQuizType(CommonFns.NVL(rs.getString("QUIZ_TYPE")));
			qd.setPreparedBy(CommonFns.NVL(rs.getString("PREPARED_BY")));
			qd.setIsPublished(rs.getString("IS_PUBLISH"));
			qed.setQuizDtl(qd);
			
		}
		
		if(ed.getEmpType().equals("N")){
			qry = "SELECT EMP_ID,QUIZ_NBR,TO_CHAR(QUIZ_SUBMITTED_DT,'DD-MON-YYYY HH:MI:SS') QUIZ_SUBMITTED_DT,VISITED,SCORE FROM QUIZ_EMP_DETAILS " +
					" WHERE EMP_ID = " +ed.getEmpId()+" AND QUIZ_NBR="+quizNo ;
			//System.out.println("getQuizInfoByQuizNbr 2: "+qry);
			
			rs = st.executeQuery(qry);
			while (rs.next()) {
				// EMP's Quiz details
				qed.setEmpId(rs.getInt("EMP_ID"));
				qed.setAppear_Dt(CommonFns.NVL(rs.getString("QUIZ_SUBMITTED_DT")));
				qed.setIsVisited(rs.getString("VISITED"));
				qed.setScore(CommonFns.NVL(rs.getString("SCORE")));
			}
			
			if(qed.getIsVisited().equals("Y")){ // visited 
				
				if(qed.getAppear_Dt().equals("")){ // Saved
					if(CommonFns.compareWithToday(qed.getQuizDtl().getEnd_Dt())<=0){ // quiz not over
						qed.setMode("E");
					}else{
						qed.setMode("V");
					}
				}else{ // Submitted
					if(CommonFns.compareWithToday(qed.getQuizDtl().getEnd_Dt())<=0){ // quiz not over
						qed.setMode("T");
					}else{
						qed.setMode("V");
					}
				}
			}else{ // not visited
				if(CommonFns.compareWithToday(qed.getQuizDtl().getEnd_Dt())<=0){ // quiz not over
						qed.setMode("E");
				}else{
						qed.setMode("V");
				}
			}
			
		}
		
		}catch (Exception e) {
			con.rollback();
			throw e;
		} finally {
			close();
		}
		return qed;
	}
	
	
	public List<QuestionBean> getQuizSetByQuizNbr(int quizNo) throws Exception {
		log.info("DBManager.getQuizSetByQuizNbr()");
		List<QuestionBean> qbList = new ArrayList<QuestionBean>();
		con = ConnectionManager.getConnection();
		try {
		st = con.createStatement();
		
		String qry = "SELECT QUIZ_NBR,QSTN_NBR,QSTN_TXT,OPT_1,OPT_2,OPT_3,OPT_4,ANSWER,COMMENTS,QSTN_IMG FROM QSTN_TBL WHERE QUIZ_NBR= "+quizNo+" ORDER BY QSTN_NBR";

		rs = st.executeQuery(qry);
		while (rs.next()) {
			QuestionBean qb = new QuestionBean();
			qb.setQuiz_nbr(rs.getInt("QUIZ_NBR"));
			qb.setQuestion_Nbr(rs.getInt("QSTN_NBR"));
			qb.setQuestion_Txt(CommonFns.NVL(rs.getString("QSTN_TXT")));
			qb.setOption1(CommonFns.NVL(rs.getString("OPT_1")));
			qb.setOption2(CommonFns.NVL(rs.getString("OPT_2")));
			qb.setOption3(CommonFns.NVL(rs.getString("OPT_3")));
			qb.setOption4(CommonFns.NVL(rs.getString("OPT_4")));
			qb.setAnswer(CommonFns.NVL(rs.getString("ANSWER")));
			qb.setComments(CommonFns.NVL(rs.getString("COMMENTS")));
			qb.setQuestion_Img(CommonFns.NVL(rs.getString("QSTN_IMG")));
			qbList.add(qb);
		}
		}catch (Exception e) {
			con.rollback();
			throw e;
		} finally {
			close();
		}
		return qbList;
	}
	
	public List<QuestionBean> getQuestionsByQuizNbrForNormalUser(int empId, int quizNo) throws Exception {
		log.info("DBManager.getQuestionsByQuizNbrForNormalUser()");
		List<QuestionBean> qbList = new ArrayList<QuestionBean>();
		con = ConnectionManager.getConnection();
		try {
		st = con.createStatement();
		
		String qry = //"SELECT QUIZ_NBR,QSTN_NBR,QSTN_TXT,OPT_1,OPT_2,OPT_3,OPT_4,ANSWER,COMMENTS FROM QSTN_TBL WHERE QUIZ_NBR= "+quizNo+" ORDER BY QSTN_NBR";
					"SELECT QT.QUIZ_NBR,QT.QSTN_NBR,QT.QSTN_TXT,QT.OPT_1,QT.OPT_2,QT.OPT_3,QT.OPT_4,QT.ANSWER,QT.COMMENTS,QT.QSTN_IMG ,TA.ANSWER USR_ANS FROM QSTN_TBL QT," +
					"(SELECT QUIZ_NBR,QSTN_NBR,ANSWER,EMP_ID FROM TEST_ANSWERS WHERE  EMP_ID="+empId+") TA " +
					"WHERE QT.QUIZ_NBR = TA.QUIZ_NBR(+) AND QT.QSTN_NBR = TA.QSTN_NBR(+) AND QT.QUIZ_NBR="+ quizNo +
					" ORDER BY QT.QUIZ_NBR,QT.QSTN_NBR";
					//" ORDER BY DBMS_RANDOM.RANDOM";
		
		//System.out.println("getQuizSetByQuizNbrForNormalUser:: "+qry);
		rs = st.executeQuery(qry);
		while (rs.next()) {
			QuestionBean qb = new QuestionBean();
			qb.setQuiz_nbr(rs.getInt("QUIZ_NBR"));
			qb.setQuestion_Nbr(rs.getInt("QSTN_NBR"));
			qb.setQuestion_Txt(CommonFns.NVL(rs.getString("QSTN_TXT")));
			qb.setQuestion_Img(CommonFns.NVL(rs.getString("QSTN_IMG")));
			qb.setOption1(CommonFns.NVL(rs.getString("OPT_1")));
			qb.setOption2(CommonFns.NVL(rs.getString("OPT_2")));
			qb.setOption3(CommonFns.NVL(rs.getString("OPT_3")));
			qb.setOption4(CommonFns.NVL(rs.getString("OPT_4")));
			qb.setAnswer(CommonFns.NVL(rs.getString("ANSWER")));
			qb.setComments(CommonFns.NVL(rs.getString("COMMENTS")));
			qb.setUser_ans(CommonFns.NVL(rs.getString("USR_ANS")));
			
			qbList.add(qb);
		}
		}catch (Exception e) {
			con.rollback();
			throw e;
		} finally {
			close();
		}
		return qbList;
	}

	
	public int entryToQuizEmdDetais(EmpDetails ed, int quizNo) throws Exception {
		log.info("DBManager.entryToQuizEmdDetais()");
		con = ConnectionManager.getConnection();
		int result = 0;
		int status = 0;
		try {
			st = con.createStatement();

			String qry = "SELECT COUNT(1) FROM QUIZ_EMP_DETAILS WHERE EMP_ID="+ ed.getEmpId()+" AND QUIZ_NBR="+quizNo;
			rs = st.executeQuery(qry);
			
			while (rs.next()) {
				result = rs.getInt(1);
			}
			
			if(result == 0){

				qry = "INSERT INTO QUIZ_EMP_DETAILS (EMP_ID,QUIZ_NBR) VALUES (" + ed.getEmpId() + "," + quizNo +")";
				status = st.executeUpdate(qry);
				con.commit();
				
			}else{
				status = 1;
			}
			
		} catch (Exception e) {
			throw e;
		} 
		return status;
	}

	public int submitQuizAnswer(EmpDetails ed, List qbList) throws Exception {
		log.info("DBManager.submitQuizAnswer()");
		int result = 0;
		int status = 0;
		try {
			con = ConnectionManager.getConnection();
			
			status = entryToQuizEmdDetais(ed, ((QuestionBean) qbList.get(0)).getQuiz_nbr());

			if(status!=0){
				st = con.createStatement();
				
				String qry1 = "SELECT COUNT(1) FROM TEST_ANSWERS WHERE QUIZ_NBR = ? AND QSTN_NBR= ? AND EMP_ID= ?";
				String qry2 = "INSERT INTO TEST_ANSWERS(QUIZ_NBR,QSTN_NBR,ANSWER,EMP_ID) VALUES( ?, ?, ?, ?)";
				String qr3 = "UPDATE TEST_ANSWERS SET ANSWER = ? WHERE QUIZ_NBR =? AND QSTN_NBR = ? AND EMP_ID = ?";
				
				for(int i=0;i<qbList.size();i++){
					QuestionBean qb = (QuestionBean) qbList.get(i);
					ps = con.prepareStatement(qry1);
					ps.setInt(1, qb.getQuiz_nbr());
					ps.setInt(2, qb.getQuestion_Nbr());
					ps.setInt(3, ed.getEmpId());
					rs = ps.executeQuery();
					//rs = st.executeQuery(qry1);
					while (rs.next()) {
						result = rs.getInt(1);
					}
					
					if(result==0){
						ps = con.prepareStatement(qry2);
						ps.setInt(1, qb.getQuiz_nbr());
						ps.setInt(2, qb.getQuestion_Nbr());
						ps.setString(3, qb.getUser_ans());
						ps.setInt(4, ed.getEmpId());
						status = ps.executeUpdate();
					}else{
						ps = con.prepareStatement(qr3);
						ps.setString(1, qb.getUser_ans());
						ps.setInt(2, qb.getQuiz_nbr());
						ps.setInt(3, qb.getQuestion_Nbr());
						ps.setInt(4, ed.getEmpId());
						status = ps.executeUpdate();
						
					}
				}
			}
			
			
			con.commit();
			
		} catch (Exception e) {
			con.rollback();
			throw e;
		} finally {
			close();
		}
		return status;
	}
	
	
	public EmpDetails getEmpDetails(int empId, String passPhrase) throws Exception {
		log.info("DBManager.getEmpDetails()");
		con = ConnectionManager.getConnection();
		
		EmpDetails ed = null;
		int status =0;
		
		try {
			
			
			st = con.createStatement();

			String qry = "SELECT EMP_ID,EMP_EMAIL,EMP_NAME,EMP_TYPE,PASS_PHRASE,THEME,IS_DEVELOPER,PRFERED_QUIZ_TYPE,IS_USER_DETAILS_REQUIRED FROM EMP_DETAILS WHERE EMP_ID="+empId ;
			rs = st.executeQuery(qry);
			
			while (rs.next()) {
				ed = new EmpDetails();
				ed.setEmpId(rs.getInt("EMP_ID"));
				ed.setEmpEmail(CommonFns.NVL(rs.getString("EMP_EMAIL")));
				ed.setEmpName(CommonFns.NVL(rs.getString("EMP_NAME")));
				ed.setEmpType(CommonFns.NVL(rs.getString("EMP_TYPE")));
				ed.setPassPhrase(rs.getString("PASS_PHRASE"));
				ed.setTheme(rs.getString("THEME"));
				ed.setIsDeveloper(CommonFns.NVL(rs.getString("IS_DEVELOPER")));
				ed.setPrefered_quizType(CommonFns.NVL(rs.getString("PRFERED_QUIZ_TYPE")));
				ed.setIsChangeRequired(CommonFns.NVL(rs.getString("IS_USER_DETAILS_REQUIRED")));
			}
			
			if(null != ed){
				if(!ed.getPassPhrase().equals(passPhrase))
					ed = null;
			}else{
				// If user doesn't exist add new NORMAL user
				qry = "INSERT INTO EMP_DETAILS (EMP_ID,PASS_PHRASE,EMP_TYPE) VALUES (?, ?, ?)";
				
				ps = con.prepareStatement(qry);
				ps.setInt(1, empId);
				ps.setString(2, passPhrase);
				ps.setString(3, "N");
				status = ps.executeUpdate();
				con.commit();
				
				if(status!=0){
					ed = new EmpDetails();
					ed.setEmpId(empId);
					
					ed.setPassPhrase(passPhrase);
					ed.setEmpType("N");
				}
				
			}
			
			
		} catch (SQLException e) {
			con.rollback();
			throw e;
		} finally {
			close();
		}
		return ed;
	}
	
	public EmpDetails getQuizDetailsforNormalUser(EmpDetails ed ) throws Exception {
		log.info("DBManager.getQuizDetailsforNormalUser()");
		List<QuizEmpDetails> quizDetails = new ArrayList<QuizEmpDetails>();
		//List<QuizEmpDetails> unAttendedQuizSets = new ArrayList<QuizEmpDetails>();
		
		con = ConnectionManager.getConnection();
		st = con.createStatement();
		
		try {

			String qry ="SELECT QTT.QUIZ_NBR,QTT.QUIZ_TYPE, QTT.SHOW_QUIZ_NBR,TO_CHAR(QTT.START_DT ,'DD-MON-YYYY HH:MI:SS AM') START_DT," +
					"TO_CHAR(QTT.END_DT ,'DD-MON-YYYY HH:MI:SS AM') END_DT,IS_PUBLISH,QED.EMP_ID, " +
						"TO_CHAR(QED.QUIZ_SUBMITTED_DT ,'DD-MON-YYYY HH:MI:SS AM') QUIZ_SUBMITTED_DT,NVL(QED.VISITED,'N') VISITED  " +
						" FROM QUIZ_TM_TBL QTT , (SELECT EMP_ID,QUIZ_NBR,QUIZ_SUBMITTED_DT,VISITED,SCORE FROM QUIZ_EMP_DETAILS WHERE EMP_ID = "+ed.getEmpId()+") QED " +
						" WHERE QTT.QUIZ_NBR=QED.QUIZ_NBR(+)" +
					//	" AND QTT.IS_PUBLISH = 'Y'"+
					//	" AND QED.VISITED ='Y' " +
					//	" AND QTT.END_DT < TRUNC(SYSDATE) " +
						" ORDER BY QUIZ_TYPE,QUIZ_NBR";
			
			//System.out.println("getQuizDetailsforNormalUser 1: " + qry);
			
			rs = st.executeQuery(qry);
			while (rs.next()) {
				QuizEmpDetails qed = new QuizEmpDetails();
				
				qed.setQuiz_nbr(rs.getInt("QUIZ_NBR"));
				qed.setEmpId(rs.getInt("EMP_ID"));
				qed.setAppear_Dt(CommonFns.NVL(rs.getString("QUIZ_SUBMITTED_DT")));
				qed.setIsVisited(rs.getString("VISITED"));
				
				QuizDetails quizDtl = new QuizDetails();
				quizDtl.setQuiz_nbr(rs.getInt("QUIZ_NBR"));
				quizDtl.setShow_quiz_nbr(CommonFns.NVL(rs.getString("SHOW_QUIZ_NBR")));
				quizDtl.setQuizType(CommonFns.NVL(rs.getString("QUIZ_TYPE")));
				quizDtl.setStart_Dt(rs.getString("START_DT"));
				quizDtl.setEnd_Dt(rs.getString("END_DT"));
				quizDtl.setIsPublished(rs.getString("IS_PUBLISH"));
				qed.setQuizDtl(quizDtl);
				
				
				quizDetails.add(qed);
			}
			
			//ed.setResultQuizSets(viewResultQuizSets);
			ed.setQuizSetDetails(quizDetails);
			
			
			//////////////////
			
			/*qry = "SELECT QTT.QUIZ_TYPE,QTT.QUIZ_NBR,QTT.SHOW_QUIZ_NBR, TO_CHAR(QTT.START_DT ,'DD-MON-YYYY HH:MI:SS AM') START_DT,TO_CHAR(QTT.END_DT ,'DD-MON-YYYY HH:MI:SS AM') END_DT," +
					"IS_PUBLISH,TO_CHAR(QED.QUIZ_SUBMITTED_DT ,'DD-MON-YYYY HH:MI:SS AM') QUIZ_SUBMITTED_DT, NVL(QED.VISITED,'N') VISITED,QED.EMP_ID " +
					"FROM QUIZ_TM_TBL QTT, (SELECT EMP_ID,QUIZ_NBR,QUIZ_SUBMITTED_DT,VISITED,SCORE FROM QUIZ_EMP_DETAILS WHERE EMP_ID = "+ed.getEmpId()+") QED " +
					"WHERE QTT.QUIZ_NBR=QED.QUIZ_NBR(+) AND START_DT <= TRUNC(SYSDATE) AND END_DT>= TRUNC(SYSDATE)  AND IS_PUBLISH ='Y' " +
					"ORDER BY QUIZ_TYPE,QUIZ_NBR";
			
			//System.out.println("getQuizDetailsforNormalUser 2:" + qry);
			
			rs = st.executeQuery(qry);
			while (rs.next()) {
				
				QuizEmpDetails qed = new QuizEmpDetails();
				qed.setQuiz_nbr(rs.getInt("QUIZ_NBR"));
				qed.setAppear_Dt(CommonFns.NVL(rs.getString("QUIZ_SUBMITTED_DT")));
				qed.setIsVisited(rs.getString("VISITED"));
				
				QuizDetails quizDtl = new QuizDetails();
				quizDtl.setQuiz_nbr(rs.getInt("QUIZ_NBR"));
				quizDtl.setShow_quiz_nbr(CommonFns.NVL(rs.getString("SHOW_QUIZ_NBR")));
				quizDtl.setQuizType(rs.getString("QUIZ_TYPE"));
				quizDtl.setStart_Dt(rs.getString("START_DT"));
				quizDtl.setEnd_Dt(rs.getString("END_DT"));
				quizDtl.setIsPublished(rs.getString("IS_PUBLISH"));
				qed.setQuizDtl(quizDtl);
				
				unAttendedQuizSets.add(qed);
			}
			
			ed.setUnAttendedQuizSets(unAttendedQuizSets);*/
			 
		} catch (SQLException e) {
			con.rollback();
			throw e;
		} finally {
			close();
		}
		return ed;
	}
	
	public EmpDetails getQuizDetailsforAdminUser(EmpDetails ed) throws Exception {
		log.info("DBManager.getQuizDetailsforAdminUser()");
		List<QuizEmpDetails> quizDetails = new ArrayList<QuizEmpDetails>();
		//List<QuizEmpDetails> unPublishedQuizList = new ArrayList<QuizEmpDetails>();
		

		con = ConnectionManager.getConnection();
		st = con.createStatement();
		try {

			
			String qry = "SELECT QTT.QUIZ_NBR,QTT.QUIZ_TYPE,QTT.SHOW_QUIZ_NBR,TO_CHAR(QTT.START_DT ,'DD-MON-YYYY HH:MI:SS AM') START_DT," +
					"TO_CHAR(QTT.END_DT ,'DD-MON-YYYY HH:MI:SS AM') END_DT,IS_PUBLISH,RESULT_DECLARED " +
					" FROM QUIZ_TM_TBL QTT " +
					//" WHERE QTT.IS_PUBLISH = 'N' " +
					" ORDER BY QUIZ_TYPE,QUIZ_NBR";
			
			//System.out.println("getQuizDetailsforAdminUser 2: " + qry);
			
			rs = st.executeQuery(qry);
			while (rs.next()) {
				QuizEmpDetails qed = new QuizEmpDetails();

				qed.setQuiz_nbr(rs.getInt("QUIZ_NBR"));

				QuizDetails quizDtl = new QuizDetails();
				quizDtl.setQuiz_nbr(rs.getInt("QUIZ_NBR"));
				quizDtl.setQuizType(rs.getString("QUIZ_TYPE"));
				quizDtl.setShow_quiz_nbr(CommonFns.NVL(rs.getString("SHOW_QUIZ_NBR")));
				quizDtl.setStart_Dt(rs.getString("START_DT"));
				quizDtl.setEnd_Dt(rs.getString("END_DT"));
				quizDtl.setIsPublished(rs.getString("IS_PUBLISH"));
				quizDtl.setIsResultDeclared(rs.getString("RESULT_DECLARED"));
				qed.setQuizDtl(quizDtl);
				
				quizDetails.add(qed);
			}

		//	ed.setModifyQuizSets(unPublishedQuizList);
			ed.setQuizSetDetails(quizDetails);
			

		} catch (SQLException e) {
			con.rollback();
			throw e;
		} finally {
			close();
		}
		return ed;

	}
	
	public int saveNormalUserDetail(EmpDetails ed) throws Exception {
		log.info("DBManager.saveNormalUserDetail()");
		int status = 0;
		try {
			con = ConnectionManager.getConnection();
			st = con.createStatement();
			
			String qry1 = "UPDATE EMP_DETAILS SET EMP_EMAIL = ?,EMP_NAME= ?,PRFERED_QUIZ_TYPE = ?,IS_USER_DETAILS_REQUIRED='N' WHERE EMP_ID = ? ";
			//String qry2 = "SELECT EMP_ID,EMP_EMAIL,EMP_TYPE,PASS_PHRASE FROM EMP_DETAILS WHERE EMP_ID = ? ";
			ps = con.prepareStatement(qry1);
			ps.setString(1, ed.getEmpEmail());
			ps.setString(2, ed.getEmpName());
			ps.setString(3, ed.getPrefered_quizType());
			ps.setInt(4, ed.getEmpId());
			
			status = ps.executeUpdate();
			con.commit();
			
		} catch (Exception e) {
			con.rollback();
			throw e;
		} finally {
			close();
		}
		return status;
	}
	
	public int finalAnsSubmit(EmpDetails ed, int quizNo, String score) throws Exception {
		log.info("DBManager.finalAnsSubmit()");
		con = ConnectionManager.getConnection();
		int status = 0;
		
		try {
			
			status = entryToQuizEmdDetais(ed, quizNo);

			if(status!=0){
				st = con.createStatement();

				//String qry = "UPDATE QUIZ_EMP_DETAILS SET QUIZ_SUBMITTED_DT = SYSDATE,VISITED='Y' WHERE EMP_ID=" + ed.getEmpId() + " AND QUIZ_NBR = "+ quizNo;
				String qry = "UPDATE QUIZ_EMP_DETAILS SET QUIZ_SUBMITTED_DT = SYSDATE,VISITED='Y'," +
						" SCORE="+score+"  WHERE EMP_ID=" + ed.getEmpId() + " AND QUIZ_NBR = "+ quizNo;
				
				//System.out.println("finalAnsSubmit 1: "+ qry );
				status = st.executeUpdate(qry);
			}
			
			con.commit();
			
		} catch (Exception e) {
			con.rollback();
			throw e;
		} finally {
			close();
		}
		return status;//String.format("%.2f", finalScore);
	
	}
	
	public List<QuizEmpDetails> viewdeclareResult(int quizNo) throws Exception {
		log.info("DBManager.viewdeclareResult()");
		con = ConnectionManager.getConnection();
		List<QuizEmpDetails> quizEmpList = new ArrayList<QuizEmpDetails>();  
		try {
			st = con.createStatement();

			String qry = //"SELECT EMP_ID, EMP_EMAIL,EMP_NAME, QUIZ_SUBMITTED_DT,SCORE,ROWNUM RANK FROM (" +
					" SELECT A.*,ROWNUM RANK FROM (SELECT QED.EMP_ID,ED.EMP_EMAIL,ED.EMP_NAME,TO_CHAR(QUIZ_SUBMITTED_DT ,'DD-MON-YYYY HH:MI:SS AM') " +
					"QUIZ_SUBMITTED_DT,SCORE " +
					" FROM QUIZ_EMP_DETAILS QED,EMP_DETAILS ED WHERE QED.EMP_ID = ED.EMP_ID AND QUIZ_SUBMITTED_DT IS NOT NULL AND QUIZ_NBR = " +quizNo +
					" ORDER BY SCORE DESC,QUIZ_SUBMITTED_DT ASC) A ORDER BY RANK" ;
					//+") ";
			//System.out.println("resultOfQuizByQuizNumber:: "+qry);
			rs = st.executeQuery(qry);
			while (rs.next()) {

				QuizEmpDetails qed = new QuizEmpDetails();

				EmpDetails ed = new EmpDetails();
				ed.setEmpId(rs.getInt("EMP_ID"));
				qed.setEmpId(rs.getInt("EMP_ID"));
				ed.setEmpName(rs.getString("EMP_NAME"));
				ed.setEmpEmail(CommonFns.NVL(rs.getString("EMP_EMAIL")));
				qed.setEmpDtl(ed);
				qed.setAppear_Dt(CommonFns.NVL(rs.getString("QUIZ_SUBMITTED_DT")));
				qed.setScore(CommonFns.NVL(rs.getString("SCORE")));
				qed.setRank(CommonFns.NVL(rs.getString("RANK")));
				quizEmpList.add(qed);
			}
			
		}catch (Exception e) {
			con.rollback();
			throw e;
		} finally {
			close();
		}
		return quizEmpList;
	}
	

	public void calculateResult(int quizNo) throws Exception {
		log.info("DBManager.calculateResult()");
		con = ConnectionManager.getConnection();
		try {
			
			CallableStatement cs = null;
			
			cs = con.prepareCall("{call SP_CALCULATE_RESULT(?)}");
			cs.setInt(1, quizNo);
			boolean status = cs.execute();
			
			//if(status){
			/*st = con.createStatement();
				String	qry = "UPDATE QUIZ_TM_TBL SET RESULT_DECLARED = 'Y' WHERE QUIZ_NBR = "+quizNo ;
				System.out.println("declareResult 1:: "+qry);
				st.executeUpdate(qry);
				con.commit();*/
				
				// View Result
				//quizEmpList = viewdeclareResult(quizNo);
		//	}
			
			
		}catch (Exception e) {
			con.rollback();
			throw e;
		} finally {
			close();
		}
		//return quizEmpList;
	}
	
	
	public void markQuizDeclared(int quizNo) throws Exception {
		log.info("DBManager.declareResult()");
		con = ConnectionManager.getConnection();
		try {

			st = con.createStatement();
			String qry = "UPDATE QUIZ_TM_TBL SET RESULT_DECLARED = 'Y' WHERE QUIZ_NBR = "
					+ quizNo;
			//System.out.println("declareResult 1:: " + qry);
			st.executeUpdate(qry);
			con.commit();

		} catch (Exception e) {
			con.rollback();
			throw e;
		} finally {
			close();
		}
	}
	
	
	
	public String getMaxSHowQUizNumber() throws Exception {
		log.info("DBManager.getMaxSHowQUizNumber()");
		String max_nbr = ""; 
		st = con.createStatement();

		String qry = "SELECT NVL(MAX(SHOW_QUIZ_NBR),0)+1 SHOW_QUIZ_NBR FROM QUIZ_TM_TBL";
		//System.out.println("chechForLatestQuizNo:: "+qry);

		rs = st.executeQuery(qry);
		while (rs.next()) {
			max_nbr = rs.getString("SHOW_QUIZ_NBR");
		}
		//System.out.println(max_nbr);
		return max_nbr;
	}

	
	public int saveThemeDetails(EmpDetails ed) throws Exception {
		log.info("DBManager.saveThemeDetails()");
		con = ConnectionManager.getConnection();
		int status = 0;
		try {
			st = con.createStatement();

			String qry = "UPDATE EMP_DETAILS SET THEME = '" + ed.getTheme()
					+ "' WHERE EMP_ID= " + ed.getEmpId();

			status = st.executeUpdate(qry);

			con.commit();

		} catch (Exception e) {
			con.rollback();
			throw e;
		} finally {
			close();
		}
		return status;
	}

	public int saveExportedQuestionToDB(String quizType, List qbList) throws Exception {
		log.info("DBManager.saveExportedQuestionToDB()");
		int count = 0;
		try {
			con = ConnectionManager.getConnection();
			ps = con.prepareStatement("INSERT INTO QSTN_DUMP(QUIZ_TYPE,QSTN_TXT,OPT_1,OPT_2,OPT_3,OPT_4,ANSWER,COMMENTS,QSTN_NBR) VALUES( ?, ?, ?, ?, ?, ?, ?, ?,QSTN_NBR_SEQ.NEXTVAL)");
			
			for (int i = 0; i < qbList.size(); i++) {
				QuestionBean qb = (QuestionBean) qbList.get(i);
				if (qb.getQuestion_Txt() != null
						|| !qb.getQuestion_Txt().equals("")) {

					ps.setString(1, quizType);
					ps.setString(2, qb.getQuestion_Txt());
					ps.setString(3, qb.getOption1());
					ps.setString(4, qb.getOption2());
					ps.setString(5, qb.getOption3());
					ps.setString(6, qb.getOption4());
					ps.setString(7, qb.getAnswer());
					ps.setString(8, qb.getComments());
					ps.executeUpdate();
					
					count++;
				}
			}
			con.commit();

		} catch (Exception e) {
			con.rollback();
			throw e;
		} finally {
			close();
		}
		return count;
	}
	
	public int createQstnSetFromDump(QuizDetails qd,int noOfQstn,EmpDetails ed) throws Exception {
		log.info("DBManager.createQstnSetFromDump()");
		con = ConnectionManager.getConnection();
		int quizNo = 0;
		try {
			
			CallableStatement cs = null;
			
			cs = con.prepareCall("{call SP_QUIZ_SET_FORM_QSTN_DUMP( ?, ?, ?, ?, ?, ? )}");
			//SP_QUIZ_SET_FORM_QSTN_DUMP( 'ORACLE','23-SEP-14 11.59.59 PM','29-SEP-14 11.59.59 PM',10,1000);
			cs.setString(1, qd.getQuizType());
			cs.setString(2, qd.getStart_Dt());
			cs.setString(3, qd.getEnd_Dt());
			cs.setInt(4, noOfQstn);
			cs.setInt(5, ed.getEmpId());
			cs.registerOutParameter(6, java.sql.Types.NUMERIC);
			boolean status = cs.execute();
			quizNo = cs.getInt(6);
		}catch (Exception e) {
			con.rollback();
			throw e;
		} finally {
			close();
		}
		return quizNo;
	}
	
	public QuestionBean selectQstnFromDump(String quizType) throws Exception {
		//System.out.println("DBManager.selectQstnFromDump()");
		QuestionBean qb = null;
		List<QuestionBean> qbList = new ArrayList<QuestionBean>();
		try {
			con = ConnectionManager.getConnection();
			st = con.createStatement();
			String qry1 = " SELECT QUIZ_TYPE,QSTN_TXT,OPT_1,OPT_2,OPT_3,OPT_4,ANSWER,COMMENTS,QSTN_NBR FROM " +
					" (SELECT * FROM QSTN_DUMP WHERE QUIZ_TYPE = '"+quizType+"' ORDER BY SYS.DBMS_RANDOM.VALUE)A WHERE ROWNUM<2";
			// Get questions Form Dump
			rs = st.executeQuery(qry1);
			while (rs.next()) {
				qb = new QuestionBean();
				qb.setQuestion_Nbr(rs.getInt("QSTN_NBR"));
				qb.setQuestion_Txt(CommonFns.NVL(rs.getString("QSTN_TXT")));
				qb.setOption1(CommonFns.NVL(rs.getString("OPT_1")));
				qb.setOption2(CommonFns.NVL(rs.getString("OPT_2")));
				qb.setOption3(CommonFns.NVL(rs.getString("OPT_3")));
				qb.setOption4(CommonFns.NVL(rs.getString("OPT_4")));
				qb.setAnswer(CommonFns.NVL(rs.getString("ANSWER")));
				qb.setComments(CommonFns.NVL(rs.getString("COMMENTS")));
			}
			
		} catch (Exception e) {
			con.rollback();
			throw e;
		} finally {
			close();
		}
		return qb;
	}


	public void deleteQstnFromDUmp(int dump_qstn_nbr) throws Exception {
		try {
			con = ConnectionManager.getConnection();
			st = con.createStatement();
			String qry1 = " DELETE FROM QSTN_DUMP WHERE QSTN_NBR = "+ dump_qstn_nbr;
			st.execute(qry1);
		} catch (Exception e) {
			con.rollback();
			throw e;
		} finally {
			close();
		}
		
	}
	
	public List<String> countDumpQstns() throws Exception {
		//Map<String, Integer> dumpSets = new HashMap<String, Integer>();
		List<String> dumpSets = new ArrayList<String>();
		try {
			con = ConnectionManager.getConnection();
			st = con.createStatement();
			String qry = "SELECT QUIZ_TYPE,COUNT(*) CNT FROM QSTN_DUMP GROUP BY QUIZ_TYPE ORDER BY QUIZ_TYPE";
			rs = st.executeQuery(qry);
			while (rs.next()) {
				dumpSets.add(rs.getString("QUIZ_TYPE")+"|"+rs.getInt("CNT"));
			}
		} catch (Exception e) {
			con.rollback();
			throw e;
		} finally {
			close();
		}
		
		return dumpSets;
	}
	
	public List<String> getQuizTypes() throws Exception {
		//Map<String, Integer> dumpSets = new HashMap<String, Integer>();
		List<String> quizTypes = new ArrayList<String>();
		try {
			con = ConnectionManager.getConnection();
			st = con.createStatement();
			String qry = "SELECT DISTINCT QUIZ_TYPE FROM QUIZ_TM_TBL";
			rs = st.executeQuery(qry);
			while (rs.next()) {
				quizTypes.add(rs.getString("QUIZ_TYPE"));
			}
		} catch (Exception e) {
			con.rollback();
			throw e;
		} finally {
			close();
		}
		
		return quizTypes;
	}
	
	public List<QuizEmpDetails> getAttemptedUserDetails(int quizNo) throws Exception {
		//Map<String, Integer> dumpSets = new HashMap<String, Integer>();
		//System.out.println("DBManager.getAttemptedUserDetails()");
		List<QuizEmpDetails> quizEmpList = new ArrayList<QuizEmpDetails>();
		try {
			con = ConnectionManager.getConnection();
			st = con.createStatement();
			String qry = "SELECT QED.EMP_ID,ED.EMP_NAME,ED.EMP_EMAIL,TO_CHAR(QUIZ_SUBMITTED_DT ,'DD-MON-YYYY HH:MI:SS AM') QUIZ_SUBMITTED_DT FROM QUIZ_EMP_DETAILS QED," +
					"EMP_DETAILS ED WHERE QED.EMP_ID = ED.EMP_ID AND VISITED ='Y' AND QUIZ_NBR = "+quizNo;
			rs = st.executeQuery(qry);
			while (rs.next()) {
				QuizEmpDetails qed = new QuizEmpDetails();

				EmpDetails ed = new EmpDetails();
				ed.setEmpId(rs.getInt("EMP_ID"));
				qed.setEmpId(rs.getInt("EMP_ID"));
				ed.setEmpName(rs.getString("EMP_NAME"));
				ed.setEmpEmail(CommonFns.NVL(rs.getString("EMP_EMAIL")));
				qed.setEmpDtl(ed);
				qed.setAppear_Dt(CommonFns.NVL(rs.getString("QUIZ_SUBMITTED_DT")));
				quizEmpList.add(qed);
			}
		} catch (Exception e) {
			con.rollback();
			throw e;
		} finally {
			close();
		}
		
		return quizEmpList;
	}


// AUDIT

	public void auditLog(String action, String empId) throws Exception {
		log.info("DBManager.auditLog()");
		con = ConnectionManager.getConnection();
		try {
			st = con.createStatement();
			String qry = "INSERT INTO QUIZ_AUDIT(SL_NO,ACTION,EMP_ID,ACTION_DT) VALUES (?, ?, ?, SYSDATE)";

			ps = con.prepareStatement(qry);
			ps.setInt(1, 1);
			ps.setString(2, action);
			ps.setString(3, empId);
			con.commit();

		} catch (SQLException e) {
			con.rollback();
			throw e;
		} finally {
			close();
		}
	}


	public String getTotalNoOfQstn(String quizNo) throws Exception {
		String noOfQstn ="";
		log.info("DBManager.getTotalNoOfQstn()");
		try {
			con = ConnectionManager.getConnection();
			st = con.createStatement();
			String qry = "SELECT COUNT(*) NOOFQSTN FROM QSTN_TBL WHERE QUIZ_NBR = "	+ quizNo;
			rs = st.executeQuery(qry);
			while (rs.next()) {
				noOfQstn = rs.getString("NOOFQSTN");
			}
		} catch (Exception e) {
			con.rollback();
			throw e;
		} finally {
			close();
		}
		return noOfQstn;

	}


	public int submitFeedback(int empId, String feedback) throws Exception {
		int result = 0;
		log.info("DBManager.submitFeedback()");
		try {
			con = ConnectionManager.getConnection();
			st = con.createStatement();
			String qry = "INSERT INTO QUIZ_FEEDBACK(EMP_ID,FEEDBACK_TXT,SL_NO) VALUES( ?,?,feedback_seq.NEXTVAL)";
			ps = con.prepareStatement(qry);
			ps.setInt(1, empId);
			ps.setString(2, feedback);
			result = ps.executeUpdate();
			con.commit();

		} catch (Exception e) {
			con.rollback();
			throw e;
		} finally {
			close();
		}
		return result;
	}
	
	public List<String> retrivrFeedback() throws Exception {
		List<String> feedBacks = new ArrayList<String>();
		log.info("DBManager.retrivrFeedback()");
		try {
			con = ConnectionManager.getConnection();
			st = con.createStatement();
			String qry = "SELECT SL_NO||'#'||ED.EMP_NAME||'#'||QF.FEEDBACK_TXT AS FEEDBACK FROM QUIZ_FEEDBACK QF, " +
					" EMP_DETAILS ED WHERE ED.EMP_ID=QF.EMP_ID AND RESOLVED='N' ORDER BY COMMENT_TM DESC";
			rs = st.executeQuery(qry);
			while (rs.next()) {
				feedBacks.add(rs.getString("FEEDBACK"));
			}

		} catch (Exception e) {
			con.rollback();
			throw e;
		} finally {
			close();
		}
		return feedBacks;
	}


	public int resolveFeedback(int slNo) throws Exception {
		int result = 0;
		log.info("DBManager.resolveFeedback()");
		try {
			con = ConnectionManager.getConnection();
			st = con.createStatement();
			String qry = "UPDATE QUIZ_FEEDBACK SET RESOLVED = 'Y' WHERE SL_NO = "+slNo;
			result = st.executeUpdate(qry);
			

		} catch (Exception e) {
			con.rollback();
			throw e;
		} finally {
			close();
		}
		return result;
	}
	
	public List<EmpDetails> getAllEmpDetails() throws Exception {
		List<EmpDetails> empList = new ArrayList<EmpDetails>();
		log.info("DBManager.getAllEmpDetails()");
		try {
			con = ConnectionManager.getConnection();
			st = con.createStatement();
			
			String timeout = ConfigFile.getKey("session.MaxTime");
			
			String qry = "SELECT  EMP_ID,EMP_EMAIL,EMP_NAME,EMP_TYPE,PASS_PHRASE,THEME,SESSION_ID,PRFERED_QUIZ_TYPE,IS_USER_DETAILS_REQUIRED, " +
					" CASE WHEN LAST_VISIT IS NULL THEN 'OFFLINE' ELSE CASE WHEN (ROUND((CAST(SYSDATE AS DATE) - CAST(LAST_VISIT AS DATE))* 24 * 60 * 60)) > "+timeout+" THEN " +
					" 'OFFLINE' ELSE 'ONLINE' END END AS USR_STATUS FROM EMP_DETAILS WHERE  IS_DEVELOPER = 'N' ORDER BY EMP_ID";
			System.out.println("getAllEmpDetails:: "+qry);
			rs = st.executeQuery(qry);
			while (rs.next()) {
				EmpDetails ed = new EmpDetails();
				ed.setEmpId(rs.getInt("EMP_ID"));
				ed.setEmpEmail(CommonFns.NVL(rs.getString("EMP_EMAIL")));
				ed.setEmpName(CommonFns.NVL(rs.getString("EMP_NAME")));
				ed.setEmpType(rs.getString("EMP_TYPE"));
				ed.setPassPhrase(rs.getString("PASS_PHRASE"));
				ed.setTheme(rs.getString("THEME"));
				ed.setUserLoginStaus(rs.getString("USR_STATUS"));
				ed.setSessionId(CommonFns.NVL(rs.getString("SESSION_ID")));
				ed.setPrefered_quizType(CommonFns.NVL(rs.getString("PRFERED_QUIZ_TYPE")));
				ed.setIsChangeRequired(CommonFns.NVL(rs.getString("IS_USER_DETAILS_REQUIRED")));
				empList.add(ed);
			}

		} catch (Exception e) {
			con.rollback();
			throw e;
		} finally {
			close();
		}
		return empList;
	}


	public int updateUserDetails(EmpDetails emp) throws Exception {
		int result = 0;
		log.info("DBManager.updateUserDetails()");
		try {
			con = ConnectionManager.getConnection();
			st = con.createStatement();
			String qry = "UPDATE EMP_DETAILS SET EMP_NAME = ? ,EMP_EMAIL = ? ,PASS_PHRASE = ?,EMP_TYPE = ?,PRFERED_QUIZ_TYPE=?,IS_USER_DETAILS_REQUIRED=? WHERE EMP_ID = ? ";
			//System.out.println("updateUserDetails:: "+qry);
			ps = con.prepareStatement(qry);
			ps.setString(1, emp.getEmpName());
			ps.setString(2, emp.getEmpEmail());
			ps.setString(3, emp.getPassPhrase());
			ps.setString(4, emp.getEmpType());
			ps.setString(5, emp.getPrefered_quizType());
			ps.setString(6, emp.getIsChangeRequired());
			ps.setInt(7, emp.getEmpId());
			result = ps.executeUpdate();
			con.commit();

		} catch (Exception e) {
			con.rollback();
			throw e;
		} finally {
			close();
		}
		return result;
	}


	public boolean removeUserDetails(int empId) throws Exception {
		log.info("DBManager.removeUserDetails()");
		con = ConnectionManager.getConnection();
		boolean status = false;
		try {
			
			CallableStatement cs = null;
			
			cs = con.prepareCall("{call SP_DELETE_EMD_DETAILS(?)}");
			cs.setInt(1, empId);
			status = cs.execute();
			
		} catch (Exception e) {
			con.rollback();
			throw e;
		} finally {
			close();
		}
		
		return status;
	}


	public void updateLastRevisit(EmpDetails emp, String sessionId, String status) throws Exception {
		log.info("DBManager.updateLastRevisit()");
		try {
			con = ConnectionManager.getConnection();
			st = con.createStatement();
			String date = status.equals("OFFLINE")?"null":"SYSDATE";
			String qry = "UPDATE EMP_DETAILS SET LAST_VISIT = "+ date+",SESSION_ID=? WHERE EMP_ID = ? " ;
			//System.out.println("updateLastRevisit:: " + qry);
			ps = con.prepareStatement(qry);
			ps.setString(1, status.equals("ONLINE")?sessionId:"");
			ps.setInt(2, emp.getEmpId());
			ps.executeUpdate();
			con.commit();

		} catch (Exception e) {
			con.rollback();
			throw e;
		} finally {
			close();
		}
	}


	public int isUniqueEmailAddress(String emailAdd, String empId) throws Exception {
		log.info("DBManager.isUniqueEmailAddress()");
		int count = 0;
		try {
			con = ConnectionManager.getConnection();
			st = con.createStatement();
			
			String qry = "SELECT COUNT(1) CNT FROM EMP_DETAILS WHERE EMP_EMAIL='"+emailAdd+"' AND EMP_ID <> "+empId;
			System.out.println("getAllEmpDetails:: "+qry);
			rs = st.executeQuery(qry);
			while (rs.next()) {
				count = rs.getInt("CNT");
			}

		} catch (Exception e) {
			con.rollback();
			throw e;
		} finally {
			close();
		}
		return count;
	}

	
}