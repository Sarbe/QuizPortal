package bean;

import java.io.Serializable;

public class QuizDetails implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private int quiz_nbr = 0;
	private String show_quiz_nbr = "";
	private String start_Dt = "";
	private String end_Dt = "";
	private String isResultDeclared = "";
	private String quizType = "";
	private String preparedBy = "";
	private String isPublished = "";
	
	public int getQuiz_nbr() {
		return quiz_nbr;
	}

	public void setQuiz_nbr(int quiz_nbr) {
		this.quiz_nbr = quiz_nbr;
	}

	public String getShow_quiz_nbr() {
		return show_quiz_nbr;
	}

	public void setShow_quiz_nbr(String show_quiz_nbr) {
		this.show_quiz_nbr = show_quiz_nbr;
	}

	public String getStart_Dt() {
		return start_Dt;
	}

	public void setStart_Dt(String start_Dt) {
		this.start_Dt = start_Dt;
	}

	public String getEnd_Dt() {
		return end_Dt;
	}

	public void setEnd_Dt(String end_Dt) {
		this.end_Dt = end_Dt;
	}

	public String getIsResultDeclared() {
		return isResultDeclared;
	}

	public void setIsResultDeclared(String isResultDeclared) {
		this.isResultDeclared = isResultDeclared;
	}

	public String getQuizType() {
		return quizType;
	}

	public String getPreparedBy() {
		return preparedBy;
	}

	public void setPreparedBy(String preparedBy) {
		this.preparedBy = preparedBy;
	}

	public void setQuizType(String quizType) {
		this.quizType = quizType;
	}

/*	public String getQuiz_nbrs() {
		return quiz_nbrs;
	}

	public void setQuiz_nbrs(String quiz_nbrs) {
		this.quiz_nbrs = quiz_nbrs;
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

	public String getIsAppeared() {
		return isAppeared;
	}

	public void setIsAppeared(String isAppeared) {
		this.isAppeared = isAppeared;
	}

	public String getScore() {
		return score;
	}

	public void setScore(String score) {
		this.score = score;
	}

	public String getAttemp_Dt() {
		return attemp_Dt;
	}

	public void setAttemp_Dt(String attemp_Dt) {
		this.attemp_Dt = attemp_Dt;
	}

	public String getRank() {
		return rank;
	}

	public void setRank(String rank) {
		this.rank = rank;
	}*/

//	public String getMode() {
//		return mode;
//	}
//
//	public void setMode(String mode) {
//		this.mode = mode;
//	}

	public String getIsPublished() {
		return isPublished;
	}

	public void setIsPublished(String isPublished) {
		this.isPublished = isPublished;
	}
	

}
