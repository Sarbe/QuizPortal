package bean;

import java.io.Serializable;

public class QuestionBean implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private int quiz_nbr= 0;
	private int question_Nbr= 0;
	private String question_Txt="";
	private String question_Img="";
	private String option1="";
	private String option2="";
	private String option3="";
	private String option4="";
	private String answer="";
	private String comments="";
	
	private String user_ans="";
	

	public int getQuiz_nbr() {
		return quiz_nbr;
	}

	public void setQuiz_nbr(int quiz_nbr) {
		this.quiz_nbr = quiz_nbr;
	}

	public int getQuestion_Nbr() {
		return question_Nbr;
	}

	public void setQuestion_Nbr(int question_Nbr) {
		this.question_Nbr = question_Nbr;
	}

	public String getQuestion_Txt() {
		return question_Txt;
	}

	public void setQuestion_Txt(String question_Txt) {
		this.question_Txt = question_Txt;
	}

	public String getQuestion_Img() {
		return question_Img;
	}

	public void setQuestion_Img(String question_Img) {
		this.question_Img = question_Img;
	}

	public String getOption1() {
		return option1;
	}

	public void setOption1(String option1) {
		this.option1 = option1;
	}

	public String getOption2() {
		return option2;
	}

	public void setOption2(String option2) {
		this.option2 = option2;
	}

	public String getOption3() {
		return option3;
	}

	public void setOption3(String option3) {
		this.option3 = option3;
	}

	public String getOption4() {
		return option4;
	}

	public void setOption4(String option4) {
		this.option4 = option4;
	}

	public String getAnswer() {
		return answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public String getUser_ans() {
		return user_ans;
	}

	public void setUser_ans(String user_ans) {
		this.user_ans = user_ans;
	}
	

}
