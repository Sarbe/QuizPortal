package velocity;

import java.util.ArrayList;
import java.util.List;

public class VelocityHelperClass {
	ArrayList empList = null;
	int quizNo = 0;

	public VelocityHelperClass(List l, int qNo) {
		empList = (ArrayList) l;
		quizNo = qNo;
	}

	public ArrayList getEmpList() {
		return empList;
	}

	public void setEmpList(ArrayList empList) {
		this.empList = empList;
	}

	
}
