package com.johnfrey.bupt;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Bayesian {
	private static List<List<String>> trainData;
	private static List<Integer> trainDataClass;
	private static List<String> vocabulary;
	private static List<List<Integer>> trainVector;
	private static double pAbusive;
	private static List<Double> p1Vec;
	private static List<Double> p0Vec;
	private static boolean hasHistory;
	/**
	 * @param args
	 */
	public static void main(String args[]){
		init();
		getTrainData();
		getVocab();
		data2Vector();
		train();
		writeDictnFeature();
		List<String> test = new ArrayList<>();
		List<Integer> testVec = new ArrayList<>();
		test.add("sad");
		test.add("my");
		test.add("friend");
		test.add("is");
		test.add("gone");
		testVec = test2Vector(test);
		judge(testVec);
		System.out.println(test);
	}

	//初始化
	public static void init() {
		trainData = new ArrayList<>();
		trainDataClass = new ArrayList<>();
		vocabulary = new ArrayList<>();
		trainVector = new ArrayList<>();
		p1Vec = new ArrayList<>();
		p0Vec = new ArrayList<>();
		hasHistory = false;
	}

	//从文本中形成固定格式的训练集
	public static void getTrainData(){
		//先查看字典是否已经生成,若已生成，跳过生成训练集的部分
		File dict = new File("./dict.txt");
		try{
			FileInputStream in = new FileInputStream(dict);
			InputStreamReader inputreader = new InputStreamReader(in);
			BufferedReader buffreader = new BufferedReader(inputreader);
			if(buffreader.readLine() != null){
				hasHistory = true;
				return;
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		//如果字典还是空的，从训练文本生成训练集
		File trainSet = new File("./data_temp.txt");
		try {
			FileInputStream in = new FileInputStream(trainSet);
			if (in != null) {
				InputStreamReader inputreader = new InputStreamReader(in);
				BufferedReader buffreader = new BufferedReader(inputreader);
				String line;
				int lnum = 0;
				while ((line = buffreader.readLine()) != null) {
					line = line.trim();
					int wnum = 0;
					while (line.charAt(wnum) != '	') {
						wnum++;
					}
					wnum++;
					trainDataClass.add((int) line.charAt(wnum) - 48);
					line = line.substring(wnum + 1).trim();
					Pattern p = Pattern.compile("[.,\"\\?!:'\r\n]");
					Matcher m = p.matcher(line);
					line = m.replaceAll(" ");
					String temp[] = line.toLowerCase().split(" ");
					trainData.add(Arrays.asList(temp));
					lnum++;
				}
				in.close();
			}
		}
		catch (java.io.FileNotFoundException e)
		{
			System.out.println("The File does not exist.");
		}
		catch (IOException e)
		{
			System.out.println(e.getMessage());
		}
//		trainData.add(Arrays.asList(new String[]{"my","dog","has","flea"}));
//		trainData.add(Arrays.asList(new String[]{"you","are","an","ass","ass"}));
//		trainData.add(Arrays.asList(new String[]{"don't","touch","him","asshole"}));
//		trainData.add(Arrays.asList(new String[]{"take","him","for","a","walk"}));
//		trainDataClass.add(1);
//		trainDataClass.add(0);
//		trainDataClass.add(0);
//		trainDataClass.add(1);
	}

	//从训练集形成字典
	public static void getVocab(){
		//如果字典文件不为空，直接从文件形成字典
		if(hasHistory){
			System.out.println("has history!");
			//TODO:获取存取的历史字典
			File dict = new File("./dict.txt");
			try{
				FileInputStream in = new FileInputStream(dict);
				InputStreamReader inputreader = new InputStreamReader(in);
				BufferedReader buffreader = new BufferedReader(inputreader);
				String word;
				while ((word = buffreader.readLine()) != null){
					vocabulary.add(word);
				}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			System.out.println(vocabulary);
		}
		//字典文件为空，从训练集生成字典
		else {
			for (List<String> data : trainData) {
				for (String word : data) {
					if (!vocabulary.contains(word)) {
						vocabulary.add(word);
					}
				}
			}
		}
	}

	//将列表格式的训练集转换为向量
	public static void data2Vector() {
		if(hasHistory){
			//有历史则不再计算向量
		}
		else {
			for (int j = 0; j < trainData.size(); j++) {
				List<Integer> vec = new ArrayList<>();
				int[] temp = new int[vocabulary.size()];
				for (int i = 0; i < vocabulary.size(); i++) {
					vec.add(0);
				}
				for (int k = 0; k <vocabulary.size(); k++){
					temp[k] = 0;
				}
				for (int i = 0; i < trainData.get(j).size(); i++) {
					if (vocabulary.contains(trainData.get(j).get(i))) {
						temp[(vocabulary.indexOf(trainData.get(j).get(i)))]++;
					}
				}
				for (int i = 0; i < vocabulary.size(); i++) {
					vec.set(i, temp[i]);
				}
				trainVector.add(vec);
			}
		}
	}

	//训练，由向量获取特征向量
	public static void train() {
		if(hasHistory){
			//TODO：获取p0Vec.txt/p1Vec.txt中的特征值
			File feat = new File("./p0Vec.txt");
			try{
				FileInputStream in = new FileInputStream(feat);
				InputStreamReader inputreader = new InputStreamReader(in);
				BufferedReader buffreader = new BufferedReader(inputreader);
				String line;
				while((line = buffreader.readLine()) != null){
					String temp[] = line.split("\t");
					for(int i = 0;i<temp.length;i++){
						p0Vec.add(Double.parseDouble(temp[i]));
					}
				}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			feat = new File("./p1Vec.txt");
			try{
				FileInputStream in = new FileInputStream(feat);
				InputStreamReader inputreader = new InputStreamReader(in);
				BufferedReader buffreader = new BufferedReader(inputreader);
				String line;
				while((line = buffreader.readLine()) != null){
					String temp[] = line.split("\t");
					for(int i = 0;i<temp.length;i++){
						p1Vec.add(Double.parseDouble(temp[i]));
					}
				}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			feat = new File("./pAbusive.txt");
			try{
				FileInputStream in = new FileInputStream(feat);
				InputStreamReader inputreader = new InputStreamReader(in);
				BufferedReader buffreader = new BufferedReader(inputreader);
				String line;
				if((line = buffreader.readLine()) != null){
					pAbusive = Double.parseDouble(line);
				}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		//没有历史特征向量，开始计算特征向量
		else {
			int trainDocNum = trainVector.size();
			int wordsNum = trainVector.get(0).size();
			int count = 0;
			for (int i = 0; i < trainDataClass.size(); i++) {
				if (trainDataClass.get(i) == 1) {
					count++;
				}
			}
			pAbusive = (double) (count) / trainDocNum;
//		pAbusive = (double) (trainDataClass.stream().filter(dataClass -> dataClass.equals(1)).count()) / trainDocNum;
			int[] p1Num = new int[wordsNum];
			laplace(p1Num, 1);
			int[] p0Num = new int[wordsNum];
			laplace(p0Num, 1);
			int p1Den = 2;
			int p0Den = 2;
			for (int i = 0; i < trainDocNum; i++) {
				if (trainDataClass.get(i) == 1) {
					for (int j = 0; j < wordsNum; j++) {
						p1Num[j] += trainVector.get(i).get(j);
					}
					int countp1 = 0;
					for (int j = 0; j < trainVector.get(i).size(); j++) {
						if (trainVector.get(i).get(j) == 1) {
							countp1++;
						}
					}
					p1Den += countp1;
//				p1Den += trainVector.get(i).stream().filter(isThereWord -> isThereWord.equals(1)).count();

				} else {
					for (int k = 0; k < wordsNum; k++) {
						p0Num[k] += trainVector.get(i).get(k);
					}
					int countp2 = 0;
					for (int k = 0; k < trainVector.get(i).size(); k++) {
						if (trainVector.get(i).get(k) == 1) {
							countp2++;
						}
					}
					p0Den += countp2;
//				p0Den += trainVector.get(i).stream().filter(isThereWord -> isThereWord.equals(1)).count();
				}
			}
			for (int m = 0; m < wordsNum; m++) {
				p1Vec.add(Math.log((double) (p1Num[m]) / p1Den));
				p0Vec.add(Math.log((double) (p0Num[m]) / p0Den));
			}
		}
		System.out.println("Training complete!");
	}

	//将字典和特征向量写入文件，下次直接读入
	private static void writeDictnFeature() {
		if(hasHistory){
			//有历史则不再写入dict.txt
		}
		else {
			//将字典写入历史
			FileOutputStream fos = null;
			try {
				fos = new FileOutputStream("./dict.txt");
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			OutputStreamWriter osw = new OutputStreamWriter(fos);
			BufferedWriter bw = new BufferedWriter(osw);
			for (int i = 0; i < vocabulary.size(); i++) {
				try {
					bw.write(vocabulary.get(i));
					bw.write("\r\n");
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			try {
				bw.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
			//将特征向量p0写入历史
			try {
				fos = new FileOutputStream("./p0Vec.txt");
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			osw = new OutputStreamWriter(fos);
			bw = new BufferedWriter(osw);
			try {
				for(int i = 0;i<p0Vec.size();i++) {
					bw.write(p0Vec.get(i)+"\t");
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				bw.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}

			//将特征向量p1写入历史
			try {
				fos = new FileOutputStream("./p1Vec.txt");
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			osw = new OutputStreamWriter(fos);
			bw = new BufferedWriter(osw);
			try {
				for(int i = 0;i<p1Vec.size();i++) {
					bw.write(p1Vec.get(i)+"\t");
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				bw.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}

			//将特征概率pAbusive写入历史
			try {
				fos = new FileOutputStream("./pAbusive.txt");
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			osw = new OutputStreamWriter(fos);
			bw = new BufferedWriter(osw);
			try {
				bw.write(Double.toString(pAbusive));
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				bw.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	//判决
	public static void judge(List<Integer> testVec) {

		double p1 = 0;
		double p0 = 0;
		for (int x = 0; x < testVec.size(); x++) {
			p0 += p0Vec.get(x) * testVec.get(x);
			p1 += p1Vec.get(x) * testVec.get(x);

		}
		p1 += Math.log(pAbusive);
		p0 += Math.log(1 - pAbusive);
		System.out.println("p0=" + p0 + "   p1=" + p1 + "   pA="+pAbusive);
		if(p0 > -0.4 || p1>-1.4){
			System.out.println("Neutral Words!");
		}
		else if (p1 > p0) {
			System.out.println("Positive Words!");
		}
		else if(p0 > p1) {
			System.out.println("Negative Words!");
		}

	}

	public static int[] laplace(int[] s, int weight) {
		for (int i = 0; i < s.length; i++) {
			s[i] = weight;
		}
		return s;
	}

	public static List<Integer> test2Vector(List<String> data) {
		List<Integer> vec = new ArrayList<>();
		int[] temp = new int[vocabulary.size()];
		for (int i = 0; i < vocabulary.size(); i++) {
			vec.add(0);
		}
		for (int i = 0; i < data.size(); i++) {
			if (vocabulary.contains(data.get(i))) {
				temp[(vocabulary.indexOf(data.get(i)))]++;
			}
		}
		for (int i = 0; i < vocabulary.size(); i++) {
			vec.set(i, temp[i]);
		}
		return vec;
	}
}
