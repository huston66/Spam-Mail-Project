package scut.javamail;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Set;
import java.util.TreeSet;

public class CreateDataSet {
	private String filePath;
	private String indexPath;
	private int fNum;
	private DataSet data;
	public ArrayList<Integer> indexList = new ArrayList<Integer>();

	public CreateDataSet(String filePath, String indexPath) throws IOException {
		super();
		fNum = 0;
		this.filePath = filePath;
		this.indexPath = indexPath;
		data = new DataSet();
		createWord();
		createGoodWord();
		createStopWord();
	}

	public DataSet getData() {
		return data;
	}

	public void setfNum(int num) {
		fNum = num;
	}

	public int getfNum() {
		return fNum;
	}

	public void create() throws Exception {
		readIndexInfo();
		Meger();
		BufferedWriter bw=new BufferedWriter(new FileWriter(new File("G:\\aa.txt")));
		for (int  id : indexList) {
			bw.write(id);
			bw.newLine();
		}
		bw.close();
	}
	public static int spamnum = 0;
	private void readIndexInfo() throws Exception {
		File index = new File(indexPath);
		BufferedReader br = new BufferedReader(new FileReader(index));
		String idx = null;
		while ((idx = br.readLine()) != null) {
			String[] str = idx.split(" +");
			if (!str[0].trim().equals("-1")) {
				spamnum++;
			}
			indexList.add(Integer.parseInt(str[0].trim()));

		}
		br.close();
	}
	
	/*private int setIndex(String name) {
//		System.out.println(name);
		for (int i = 0; i < indexList.size(); i++) {
			String index = indexList.get(i);			
			if (index.contains(name)) {
				String[] str = index.split(" +");
				if (str[0].contains("-1")) {
					return -1;
				} else {
					return 1;
				}
			}
		}
		return 0;
	}*/

	private void Meger() throws Exception {

		File file = new File(filePath);
		File[] files = file.listFiles();
		int countofeml = 0;
		for (int i = 0; i < files.length; i++) {
			if (files[i].isDirectory()) {
				return;
			} else {
				BufferedReader br = new BufferedReader(new FileReader(files[i]));
				StringBuilder sb = new StringBuilder();
				String str = null;
				while ((str = br.readLine()) != null) {
					sb.append(str + " ");
				}
				br.close();
				Email email = new Email();
				String name = files[i].getName();
				email.setName(name);
//				int count = setIndex(name);
				int count=indexList.get(i);
				if (count != 0) {
					email.setIsSpam(count);
				}

				int num = 0;
				if (countofeml < spamnum / 2 && count == 1) {
					StringBuilder st = insertGoodWord(num);
					sb.append(st);
					countofeml++;
				}

				String[] ft = sb.toString().toLowerCase()
						.replaceAll("<[^>]+>", " ").trim().split("[^a-zA-Z]+");
				/*
				 * BufferedWriter bw = new BufferedWriter(new FileWriter(new
				 * File( "G:\\experiment\\text\\" + files[i].getName()))); for
				 * (int j = 0; j < ft.length; j++) { bw.write(ft[j] + " "); }
				 * bw.close();
				 */

				for (int j = 0; j < ft.length; j++) {
					String feat = ft[j];
					if (list.contains(feat)) {
						continue;
					}

					if (feat.startsWith("un"))
						feat = feat.substring(2);

					if (!word.contains(feat))
						continue;

					Stemmer s = new Stemmer();
					s.add(feat);
					s.stem();

					if (s.toString().length() <= 2
							|| s.toString().length() > 10) {
						continue;
					}

					data.addFeature(s.toString());
					email.putFeature(s.toString());

				}
				data.addEmail(email);

			}
		}
	}

	private StringBuilder insertGoodWord(int num) {

		StringBuilder str = new StringBuilder();
		if (num == 0) {
			return null;
		}
		for (int i = 0; i < num; i++) {
			int count = (int) Math.round(Math.random() * goodword.size());
			str.append("  " + goodword.get(Math.min(goodword.size()-1, count)));

		}
		return str;

	}

	public static ArrayList<String> list;

	private static void createStopWord() throws IOException {
		// 生成停用词列表
		BufferedReader br = new BufferedReader(new FileReader("stopword.txt"));

		String str = null;
		list = new ArrayList<String>();
		while ((str = br.readLine()) != null) {
			list.add(str);
		}
		br.close();

	}

	public static Set<String> word;

	private static void createWord() throws IOException {
		File file = new File("word.txt");
		word = new TreeSet<String>();
		BufferedReader br = new BufferedReader(new FileReader(file));
		String str = null;
		while ((str = br.readLine()) != null) {
			if (!str.isEmpty() && str.length() != 0) {
				word.add(str.toLowerCase().trim());
			}
		}
		br.close();
	}

	public static ArrayList<String> goodword;

	private static void createGoodWord() throws IOException {

		BufferedReader br = new BufferedReader(new FileReader(
				"g:\\experiment\\program\\goodword.txt"));

		String str = null;
		goodword = new ArrayList<String>();
		while ((str = br.readLine()) != null) {
			goodword.add(str.toLowerCase().trim());
		}
		br.close();
	}
}
