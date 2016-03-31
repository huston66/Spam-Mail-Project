package scut.javamail;

public class Demo4 {

	/**
	 * 训练时的调用的方法
	 * 
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {

		CreateDataSet ds = new CreateDataSet("g:\\experiment\\textres\\train",
				"g:\\experiment\\indextrain.txt");
//		CopyOfCreateDataSetOldTrain ds=new CopyOfCreateDataSetOldTrain("G:\\研究生\\实验室\\experiment\\ceas08-1\\all", "G:\\indexyang.txt");
		ds.create();
		System.out.println("----------------------");
		DataSet ds1 = ds.getData();

		ds1.outPutFeature("g:\\experiment\\program\\train\\feature");
		ds1.outPutLabel("g:\\experiment\\program\\train\\label");
		ds1.outPutValue("g:\\experiment\\program\\train\\value");

		int tnum = ds1.totalNum();
		int spam = ds1.getSpamNum();
		int lg = ds1.getLegNum();
		int less = ds1.getFeatureNum();
		System.out.println(tnum + " " + spam + " " + lg);
		System.out.println(less);

	}
}
