package scut.javamail;

public class Demo3 {

	/**
	 * 测试时调用的方法
	 * 
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {

		for (int i = 0; i < 11; i++) {
			CreateDataSet2 ds = new CreateDataSet2(
					"g:\\experiment\\textres\\test",
					"g:\\experiment\\indextest.txt");
			ds.create(i * 50);
			DataSet2 ds1 = ds.getData();
			if (i == 0) {
				ds1.outPutLabel("g:\\experiment\\program\\test\\label");
				ds1.outPutValue("g:\\experiment\\program\\test\\value0");
			}
//			ds1.outPutValue("g:\\experiment\\program\\test\\value" + i);
			System.out.println("----------out" + i + "------------");
		}

	}

}
