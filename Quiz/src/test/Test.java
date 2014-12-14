package test;

import java.lang.reflect.Field;

public class Test {
	public String a = "ffffff";
	public String b="";

	public static void main(String[] args) throws IllegalArgumentException, IllegalAccessException {

		Test t = new Test();
		t.toString();
	}
	
	public String toString() {
	    try {
	        StringBuffer sb = new StringBuffer();
	        Class<?> objClass = this.getClass();

	        Field[] fields = objClass.getFields();
	        for(Field field : fields) {
	            String name = field.getName();
	            Object value = field.get(this);

	            sb.append(name + ": " + value.toString() + "\n");
	        }

	        return sb.toString();
	    } catch(Exception e) {
	        e.printStackTrace();
	        return null;
	    }
	}
}
