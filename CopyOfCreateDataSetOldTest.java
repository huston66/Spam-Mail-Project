package scut.javamail;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Properties;
import java.util.Set;
import java.util.TreeSet;

import javax.mail.Address;
import javax.mail.Session;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

public class CopyOfCreateDataSetOldTest {
	private String filePath;
	private String indexPath;
	private int fNum;
	private String method;
	private DataSet2 data;
	public ArrayList<String> indexList = new ArrayList<String>();

	public CopyOfCreateDataSetOldTest(String filePath, String indexPath)
			throws IOException {
		super();
		fNum = 0;
		this.method = "WordNum";
		this.filePath = filePath;
		this.indexPath = indexPath;
		data = new DataSet2();
		createWord();
		createFeat();
		createSpamFeat();
		createGoodWord();
		createStopWord();

	}

	public DataSet2 getData() {
		return data;
	}

	public void setfNum(int num) {
		fNum = num;
	}

	public int getfNum() {
		return fNum;
	}

	public void create(int num) throws Exception {
		readIndexInfo();
		Meger(num);

	}

	private void readIndexInfo() throws Exception {
		File index = new File(indexPath);
		BufferedReader br = new BufferedReader(new FileReader(index));
		String idx = null;
		while ((idx = br.readLine()) != null) {
			String[] str = idx.split(" +");
			if (str[0].contains("1"))
				spamnum++;
			indexList.add(idx);
		}
		br.close();
	}

	public static int spamnum = 0;

	private int setIndex(String name) {
		for (int i = 0; i < indexList.size(); i++) {
			String index = indexList.get(i);
			if (index.contains(name)) {
				String[] str = index.split(" +");
				if (str[0].contains("1")) {
					return 1;
				} else {
					return 0;
				}
			}
		}
		return 1;
	}

	private void Meger(int GoodwordNum) throws Exception {

		File file = new File(filePath);
		File[] files = file.listFiles();
		int countofeml = 0;
		// System.out.println(spamnum);
		for (int i = 0; i < files.length; i++) {
			if (files[i].isDirectory()) {
				return;
			} else {
				// System.out.println(files[i].getName());
				InputStream is = new FileInputStream(files[i]);
				Session session = Session.getInstance(new Properties());
				MimeMessage msg = new MimeMessage(session, is);
				is.close();
				StringBuilder sb = new StringBuilder();
				// System.out.println(msg.getContentType());
				String sub = null;
				try {
					sub = msg.getSubject().toString().toLowerCase();
				} catch (Exception ex) {
				}
				// /System.out.println(sub);
				if (rus(sub)) {
					// System.out.println(sub);
					data.addEmialUselessName(files[i].getName());

				} else {
					sb.append(sub + " ");
					try {
						Address[] to = msg.getAllRecipients();
						for (int j = 0; j < to.length; j++) {
							sb.append(to[j].toString() + " ");
						}
					} catch (Exception ex) {
					}
					try {
						Address[] from = msg.getFrom();
						for (int j = 0; j < from.length; j++) {
							sb.append(from[j].toString() + " ");
						}
					} catch (Exception ex) {
					}
					String[] rec = msg.getHeader("Received");
					for (int j = 0; j < rec.length; j++) {
						sb.append(rec[j] + " ");
					}
					try {
						String[] cc = msg.getHeader("cc");
						for (int j = 0; j < cc.length; j++) {
							sb.append(cc[j] + " ");
						}
					} catch (Exception ex) {
					}
					String type = msg.getContentType();
					if (type.contains("text")) {
						getString(msg, sb);
					} else if (type.contains("multipart")) {
						MimeMultipart mn = (MimeMultipart) msg.getContent();
						boolean b = Mul(mn, sb);
						if (b == false) {
							data.addEmialUselessName(files[i].getName());
							// System.out.println(files[i].getName());
							continue;
						}
					}

					Email email = new Email();
					String name = files[i].getName();
					email.setName(name);
					int count = setIndex(name);
					if (count != -1) {
						email.setIsSpam(count);
					}

					// int num=5000;
					if (countofeml < spamnum / 2 && count == 1) {
						StringBuilder st = insertGoodWord(GoodwordNum);
						sb.append(st);
						countofeml++;
						// System.out.println(countofeml+"  "+count);
					}

					// String s3=s2.replaceAll("[\\w-]+@\\w+(\\.\\w+)+",
					// " ").replaceAll("\\w+://\\S+",
					// " ").replaceAll("(&[a-z]+;)+",
					// " ").replaceAll("<[^>]+>"," ");
					String[] ft = sb.toString().toLowerCase()
							.replaceAll("<[^>]+>", " ").trim()
							.split("[^a-zA-Z]+");

					// createStopWord();
					for (int j = 0; j < ft.length; j++) {
						String feat = ft[j];
						if (list.contains(feat.toString())) {
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

						if (testfeat.contains(s.toString()))
							;
						{
							// data.addFeature(s.toString());
							email.putFeature(s.toString());

						}

						// System.out.println(files[i].getName());
						// System.out.println(s.toString());
						// System.out.println(email.getValue(s.toString()));

					}

					data.addEmail(email);

				}

			}

		}
		for (int j = 0; j < testfeat.size(); j++) {
			String spam = testfeat.get(j).toLowerCase().toString();
			data.addFeature(spam);
		}
	}

	private StringBuilder insertGoodWord(int num) {

		StringBuilder str = new StringBuilder();
		if (num == 0) {
			return null;
		}
		for (int i = 0; i < num; i++) {
			int count = (int) Math.round(Math.random() * goodword.size());
			str.append("  "
					+ goodword.get(Math.min(goodword.size() - 1, count)));

		}
		return str;

	}

	private static boolean rus(String sub) {
		if (sub == null)
			return true;
		char[] ch = sub.toCharArray();
		for (int i = 0; i < ch.length; i++) {
			int encode = ch[i];
			if (encode > 200) {
				return true;
			}
		}
		return false;
	}

	private static void getString(Object obj, StringBuilder sb)
			throws Exception, IOException {
		if (obj instanceof MimeMessage) {
			MimeMessage mm = (MimeMessage) obj;
			if (mm.getContentType().toLowerCase().contains("utf-7")) {

				ByteArrayOutputStream bs = new ByteArrayOutputStream();
				mm.writeTo(bs);
				String str = bs.toString()
						.replaceFirst("[^{}]+\\r\\n\\r\\n", "").toLowerCase();

				sb.append(str);

			} else {
				sb.append(mm.getContent().toString().toLowerCase());
			}
		} else {
			MimeBodyPart mm = (MimeBodyPart) obj;
			if (mm.getContentType().toLowerCase().contains("utf-7")) {

				ByteArrayOutputStream bs = new ByteArrayOutputStream();
				mm.writeTo(bs);
				String str = bs.toString()
						.replaceFirst("[^{}]+\\r\\n\\r\\n", "").toLowerCase();
				sb.append(str);

			} else {
				sb.append(mm.getContent().toString().toLowerCase());
			}
		}
	}

	public static boolean Mul(MimeMultipart mm, StringBuilder sb)
			throws IOException, Exception {

		String type = mm.getContentType().toLowerCase();

		if (type.contains("alternative")) {
			MimeBodyPart mbp = null;
			try {
				mbp = (MimeBodyPart) mm.getBodyPart(1);
			} catch (Exception e) {
				return false;
			}
			getString(mbp, sb);

		} else {
			int count = mm.getCount();
			for (int i = 0; i < count; i++) {
				MimeBodyPart bp = (MimeBodyPart) mm.getBodyPart(i);
				String str = bp.getContentType().toLowerCase();
				if (str.contains("multipart")) {
					MimeMultipart mp = (MimeMultipart) bp.getContent();
					Mul(mp, sb);
				} else if (str.contains("text")) {

					getString(bp, sb);
				}
			}
		}
		return true;
	}

	public static ArrayList<String> list;

	private static void createStopWord() throws IOException {
		// 生成停用词列表
		BufferedReader br = new BufferedReader(new FileReader("stopword.txt"));

		String str = null;
		list = new ArrayList<String>();
		while ((str = br.readLine()) != null) {
			list.add(str.toLowerCase().trim());
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
			if (!spamfeat.contains(str.toLowerCase().trim())) {
				goodword.add(str.toLowerCase().trim());
			}
		}
		br.close();
		BufferedReader br2 = new BufferedReader(new FileReader(
				"g:\\experiment\\program\\goodfeat.txt"));
		int i = 0;
		while ((str = br2.readLine()) != null) {
			goodword.set(i, str.toLowerCase().trim());
			i++;
		}
		br2.close();
		// System.out.println(goodword.get(250));
	}

	public static ArrayList<String> testfeat;

	private static void createFeat() throws IOException {

		BufferedReader br = new BufferedReader(new FileReader(
				"g:\\experiment\\program\\feat.txt"));

		String str = null;
		testfeat = new ArrayList<String>();
		while ((str = br.readLine()) != null) {
			Stemmer s = new Stemmer();
			s.add(str.toLowerCase().trim());
			s.stem();
			testfeat.add(s.toString());
		}
		br.close();
	}

	public static ArrayList<String> spamfeat;

	private static void createSpamFeat() throws IOException {

		BufferedReader br = new BufferedReader(new FileReader(
				"g:\\experiment\\program\\spamfeat.txt"));

		String str = null;
		spamfeat = new ArrayList<String>();
		while ((str = br.readLine()) != null) {
			Stemmer s = new Stemmer();
			s.add(str.toLowerCase().trim());
			s.stem();
			spamfeat.add(s.toString());
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
				word.add(str);
			}
		}
		br.close();
	}
}
