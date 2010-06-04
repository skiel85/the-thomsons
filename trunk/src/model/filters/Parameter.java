package model.filters;

public class Parameter {
	
	private String name;
	private String type;
	private Object data;
	private Parameter[] others;
	private int othersCount;
	
	public static String INTEGER = "Integer";
	public static String FLOAT = "Float";
	public static String DOUBLE = "Double";
	public static String BOOLEAN = "Boolean";
	

	public Parameter (String name, String type, Object data, int othersCount){
		this.name = name;
		this.data = data;
		this.type = type;
		this.othersCount = othersCount;
		this.others = new Parameter[othersCount];
	}

	public boolean isType(String type){
		if (this.type.compareTo(type)==0)
			return true;
		return false;
	}
	
	public void addParameterAt(int index, Parameter parameter){
		this.others[index] = parameter;
	}
	
	public static Parameter normalMinMaxFloat (String name,int min,int max, float data){
		Parameter p = new Parameter (name,Parameter.FLOAT,new Float(data),2);
		p.addParameterAt(0, new Parameter("min",Parameter.INTEGER,new Integer(min),0));
		p.addParameterAt(1, new Parameter("max",Parameter.INTEGER,new Integer(max),0));
		return p;
	}
	
	public static Parameter normalMinMaxInteger (String name,int min,int max, int data){
		Parameter p = new Parameter (name,Parameter.INTEGER,new Integer(data),2);
		p.addParameterAt(0, new Parameter("min",Parameter.INTEGER,new Integer(min),0));
		p.addParameterAt(1, new Parameter("max",Parameter.INTEGER,new Integer(max),0));
		return p;
	}
	
	public static String getTransformationMethodName(Object data2) {
		if (data2.getClass().isInstance(new Float(0))){
			return "floatValue";
		}
		if (data2.getClass().isInstance(new Boolean(true))){
			return "booleanValue";
		}
		if (data2.getClass().isInstance(new Integer(0))){
			return "intValue";
		}
		if (data2.getClass().isInstance(new Double(0))){
			return "doubleValue";
		}
		return null;
	}
	
	public static Class<?> getParameterClass(Object data2) {
		if (data2.getClass().isInstance(new Float(0))){
			return float.class;
		}
		if (data2.getClass().isInstance(new Boolean(true))){
			return boolean.class;
		}
		if (data2.getClass().isInstance(new Integer(0))){
			return int.class;
		}
		if (data2.getClass().isInstance(new Double(0))){
			return double.class;
		}
		return null;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public Parameter[] getOthers() {
		return others;
	}

	public void setOthers(Parameter[] others) {
		this.others = others;
	}

	public int getOthersCount() {
		return othersCount;
	}

	public void setOthersCount(int othersCount) {
		this.othersCount = othersCount;
	}


	
}
