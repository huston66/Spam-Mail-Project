package scut.javamail;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

public class DataSet2 {
	private int featureNum; // 特征值个数
	private int spamNum; // 垃圾邮件个数
	private int legNum; // 正常邮件个数
	private List<String> feature; // 特征值列表
	private ArrayList<String> email_useless; // 无用邮件列表
	private ArrayList<Email> email_useful; // 有用邮件列表

	public DataSet2() throws IOException {
		super();
		featureNum = 0;
		spamNum = 0;
		legNum = 0;
		feature = new ArrayList<String>();
		email_useless = new ArrayList<String>();
		email_useful = new ArrayList<Email>();

	}

	public int getFeatureNum() {
		return featureNum;
	}

	public int getSpamNum() {
		return spamNum;
	}

	public void setSpamNum(int spamNum) {
		this.spamNum = spamNum;
	}

	public int getLegNum() {
		return legNum;
	}

	public void setLegNum(int legNum) {
		this.legNum = legNum;
	}

	public int totalNum() {
		return spamNum + legNum;
	}

	public int uselessNum() {
		return email_useless.size();
	}

	public List<String> getFeature() {
		return feature;
	}

	public void addFeature(String feature) {
		this.feature.add(feature);
		featureNum = this.feature.size();
	}

	public ArrayList<String> getEmailUselessName() {
		return email_useless;
	}

	public void addEmail(Email email) {
		email_useful.add(email);
		if (email.IsSpam() == 1) {
			spamNum++;
		} else {
			legNum++;
		}
	}

	public void addEmialUselessName(String name) {
		email_useless.add(name);
	}

	public ArrayList<String> getEmailUsefulName() {
		ArrayList<String> name = new ArrayList<String>();
		int num = email_useful.size();
		for (int i = 0; i < num; i++) {
			name.add(email_useful.get(i).getName());
		}
		return name;
	}

	public ArrayList<Email> getEmail() {
		return email_useful;
	}

	public void outPutValue(String path) throws IOException {
		File Value = new File(path);
		if (!Value.exists()) {
			Value.createNewFile();
		}
		BufferedWriter bw = new BufferedWriter(new FileWriter(Value));
		// Iterator<String> it=feature.iterator();
		// //System.out.println(featureNum);
		// ArrayList<String> featList=new ArrayList<String>();
		// while(it.hasNext()){
		//
		// String feat=it.next();
		// featList.add(feat);
		// }
		// int[][] feat = new int[featureNum][email_useful.size()];
		// System.out.println(featList.size());
		for (int i = 0; i < email_useful.size(); i++) {

			TreeMap<String, Integer> featMap = email_useful.get(i)
					.getAllFeature();
			for (int j = 0; j < featureNum; j++) {
				Integer inte = featMap.get(feature.get(j));
				if (inte == null) {
					bw.write(0 + "  ");
				} else {
					bw.write(inte + "  ");
				}
			}
			bw.newLine();
		}
		bw.flush();
		bw.close();

		/*
		 * for (int i = 0; i < featureNum; i++) { for (int j = 0; j <
		 * email_useful.size(); j++) { // System.out.print(feat[i][j]+"\t");
		 * bw.write(feat[i][j] + "   "); } // System.out.println();
		 * bw.newLine(); } bw.close();
		 */
	}

	public void outPutFeature(String path) throws IOException {
		File Feature = new File(path);
		if (!Feature.exists()) {
			Feature.createNewFile();
		}
		BufferedWriter bw = new BufferedWriter(new FileWriter(Feature));
		for (int i = 0; i < feature.size(); i++) {
			bw.write(feature.get(i));
			bw.newLine();
		}
		bw.close();
	}

	public void outPutLabel(String path) throws IOException {
		File Label = new File(path);
		if (!Label.exists()) {
			Label.createNewFile();
		}
		BufferedWriter bw = new BufferedWriter(new FileWriter(Label));
		for (int i = 0; i < email_useful.size(); i++) {
			Email email = email_useful.get(i);
			int lbl = email.IsSpam();
			// System.out.println(lbl);
			bw.write(lbl + "\n");

		}
		bw.close();
	}

}
