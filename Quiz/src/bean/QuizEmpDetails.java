package bean;

import java.io.Serializable;
import java.util.List;


public class QuizEmpDetails implements Serializable {

	private static final long serialVersionUID = 1L;
	
	
	// Emp-Quiz details
	private int quiz_nbr = 0;
	private int empId;
	private String appear_Dt = "";
	private String isVisited = "";
	private String quiz_nbrs = "";
	private String score = "";
	private String rank = "";
	private String mode = ""; // For Normal User- E-Exam Mode, V- View Result Mode, T - Traverse Mode
	
	
	private EmpDetails empDtl = new EmpDetails();
	private QuizDetails quizDtl = new QuizDetails();
	
	
	
	
	private List<QuizDetails> quizDtlList;
	private List<EmpDetails> empDtlList= null;
	
	
	
	///////////////////////////////////////////////
	public String getMode() {
		return mode;
	}

	public void setMode(String mode) {
		this.mode = mode;
	}
	public int getQuiz_nbr() {
		return quiz_nbr;
	}
	public void setQuiz_nbr(int quiz_nbr) {
		this.quiz_nbr = quiz_nbr;
	}
	public int getEmpId() {
		return empId;
	}
	public void setEmpId(int empId) {
		this.empId = empId;
	}
	public String getAppear_Dt() {
		return appear_Dt;
	}
	public void setAppear_Dt(String appear_Dt) {
		this.appear_Dt = appear_Dt;
	}

	
	public String getIsVisited() {
		return isVisited;
	}

	public void setIsVisited(String isVisited) {
		this.isVisited = isVisited;
	}

	public String getQuiz_nbrs() {
		return quiz_nbrs;
	}
	public void setQuiz_nbrs(String quiz_nbrs) {
		this.quiz_nbrs = quiz_nbrs;
	}
	public String getScore() {
		return score;
	}
	public void setScore(String score) {
		this.score = score;
	}
	public String getRank() {
		return rank;
	}
	public void setRank(String rank) {
		this.rank = rank;
	}
	public EmpDetails getEmpDtl() {
		return empDtl;
	}
	public void setEmpDtl(EmpDetails empDtl) {
		this.empDtl = empDtl;
	}
	public QuizDetails getQuizDtl() {
		return quizDtl;
	}
	public void setQuizDtl(QuizDetails quizDtl) {
		this.quizDtl = quizDtl;
	}
	public List<QuizDetails> getQuizDtlList() {
		return quizDtlList;
	}
	public void setQuizDtlList(List<QuizDetails> quizDtlList) {
		this.quizDtlList = quizDtlList;
	}
	public List<EmpDetails> getEmpDtlList() {
		return empDtlList;
	}
	public void setEmpDtlList(List<EmpDetails> empDtlList) {
		this.empDtlList = empDtlList;
	}
	
	
}
