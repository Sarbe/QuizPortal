package bean;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class EmpDetails implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private int empId ;
	private String empName ="" ;
	private String empEmail= "";	
	private String empType= "";
	private String isDeveloper= "";
	private String passPhrase= "";
	private String theme = "";
	private String userLoginStaus = "";
	private String sessionId = "";
	private String prefered_quizType = "";
	private String isChangeRequired = "";
	
	
	private List<QuizEmpDetails> quizSetDetails;
	
	private List<String> dumpSets;
	
	private List<String> quizTypes;
	
//////////////
	
	

	public int getEmpId() {
		return empId;
	}
	public void setEmpId(int empId) {
		this.empId = empId;
	}
	
	public String getEmpName() {
		return empName;
	}
	public void setEmpName(String empName) {
		this.empName = empName;
	}
	public String getEmpEmail() {
		return empEmail;
	}
	public void setEmpEmail(String empEmail) {
		this.empEmail = empEmail;
	}

	public String getEmpType() {
		return empType;
	}
	public void setEmpType(String empType) {
		this.empType = empType;
	}
	public String getIsDeveloper() {
		return isDeveloper;
	}
	public void setIsDeveloper(String isDeveloper) {
		this.isDeveloper = isDeveloper;
	}
	public String getPassPhrase() {
		return passPhrase;
	}
	public void setPassPhrase(String passPhrase) {
		this.passPhrase = passPhrase;
	}

	public String getTheme() {
		return theme;
	}
	public void setTheme(String theme) {
		this.theme = theme;
	}

	public List<QuizEmpDetails> getQuizSetDetails() {
		return quizSetDetails;
	}
	public void setQuizSetDetails(List<QuizEmpDetails> quizSetDetails) {
		this.quizSetDetails = quizSetDetails;
	}
	
	public List<String> getDumpSets() {
		return dumpSets;
	}
	public void setDumpSets(List<String> dumpSets) {
		this.dumpSets = dumpSets;
	}
	public List<String> getQuizTypes() {
		return quizTypes;
	}
	public void setQuizTypes(List<String> quizTypes) {
		this.quizTypes = quizTypes;
	}
	public String getUserLoginStaus() {
		return userLoginStaus;
	}
	public void setUserLoginStaus(String userLoginStaus) {
		this.userLoginStaus = userLoginStaus;
	}
	public String getSessionId() {
		return sessionId;
	}
	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}
	public String getPrefered_quizType() {
		return prefered_quizType;
	}
	public void setPrefered_quizType(String prefered_quizType) {
		this.prefered_quizType = prefered_quizType;
	}
	public String getIsChangeRequired() {
		return isChangeRequired;
	}
	public void setIsChangeRequired(String isChangeRequired) {
		this.isChangeRequired = isChangeRequired;
	}
	
}
