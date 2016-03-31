package scut.javamail;




import java.util.TreeMap;


public class Email {
	private String name; // �ʼ���
	private int isSpam ; // �Ƿ��������ʼ�����Ϊ1������Ϊ0
	private TreeMap<String,Integer> feature;
	private int fNum ; // ���ڵ�����ֵ����

	public Email() {
		super();
		isSpam=0;
		feature=new TreeMap<String,Integer>();
		fNum=0;
	}

	public Email(String name, int isSpam) {
		super();
		this.name = name;
		this.isSpam = isSpam;
		feature=new TreeMap<String,Integer>();
		fNum=0;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int IsSpam() {
		return isSpam;
	}

	public void setIsSpam(int isSpam) {
		this.isSpam = isSpam;
	}

	public TreeMap<String,Integer> getAllFeature(){
		return feature;
	}
	
	public Integer getValue(String fName){
		return feature.get(fName);
	}
	
	public void putFeature(String fName){
		
		int count=0;
		Integer value=feature.get(fName);
		if(value!=null){
			count=value+1;			
		}else{
			count=1;
			fNum++;
		}		
		feature.put(fName, count);		
	}
	
	
	public int getfNum() {
		return fNum;
	}
	
	

}
