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
	/**
	 * @param args
	 */
	public static void main(String args[]){
		init();
		getTrainData();
		getVocab();
//		FileOutputStream fos = null;
//		try {
//			fos = new FileOutputStream("./dict.txt");
//		} catch (FileNotFoundException e) {
//			e.printStackTrace();
//		}
//		OutputStreamWriter osw = new OutputStreamWriter(fos);
//		BufferedWriter bw = new BufferedWriter(osw);
//		for(int i = 0;i<vocabulary.size();i++){
//			try {
//				bw.write(vocabulary.get(i));
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//		}
//		try {
//			bw.flush();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
		for(int i = 0;i<trainData.size();i++){
			data2Vector(vocabulary, trainData.get(i));
		}
		train(trainDataClass);
		List<String> test = new ArrayList<>();
		List<Integer> testVec = new ArrayList<>();
		test.add("sad");
		test.add("my");
		test.add("friend");
		test.add("is");
		test.add("gone");
		testVec = test2Vector(vocabulary, test);
		judge(testVec);
		System.out.println(test);
	}

	public static void init() {
		trainData = new ArrayList<>();
		trainDataClass = new ArrayList<>();
		vocabulary = new ArrayList<>();
		trainVector = new ArrayList<>();
		p1Vec = new ArrayList<>();
		p0Vec = new ArrayList<>();
	}

	public void addEntry(int dataClass,String... data){
		trainDataClass.add(dataClass);
		List<String> mdata = new ArrayList<String>();
		for(String word:data){
			mdata.add(word);
		}
		trainData.add(mdata);
	}

	public static void getTrainData(){
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
	public static void getVocab(){
		for(List<String> data:trainData){
			for(String word:data){
				if(!vocabulary.contains(word)){
					vocabulary.add(word);
				}
			}
		}
	}

	public static void data2Vector(List<String> vocab, List<String> data) {
		List<Integer> vec = new ArrayList<>();
		int[] temp = new int[vocab.size()];
		for (int i = 0; i < vocab.size(); i++) {
			vec.add(0);
		}
		for (int i = 0; i < data.size(); i++) {
			if (vocab.contains(data.get(i))) {
				temp[(vocab.indexOf(data.get(i)))]++;
			}
		}
		for (int i = 0; i < vocab.size(); i++) {
			vec.set(i, temp[i]);
		}
		trainVector.add(vec);
	}

	public static void train(List<Integer> trainDataClass) {
		int trainDocNum = trainVector.size();
		int wordsNum = trainVector.get(0).size();
		int count = 0;
		for(int i = 0;i<trainDataClass.size();i++){
			if(trainDataClass.get(i) == 1){
				count++;
			}
		}
		pAbusive = (double)(count)/trainDocNum;
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
				for(int j = 0;j<trainVector.get(i).size();j++){
					if(trainVector.get(i).get(j) == 1){
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
				for(int k = 0;k<trainVector.get(i).size();k++){
					if(trainVector.get(i).get(k) == 1){
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
		System.out.println("Training complete!");
	}

	public static void judge(List<Integer> testVec) {

		double p1 = 0;
		double p0 = 0;
		for (int x = 0; x < testVec.size(); x++) {
			p0 += p0Vec.get(x) * testVec.get(x);
			p1 += p1Vec.get(x) * testVec.get(x);

		}
		p1 += Math.log(pAbusive);
		p0 += Math.log(1 - pAbusive);
		System.out.println("p0=" + p0 + "   p1=" + p1);
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

	public static List<Integer> test2Vector(List<String> vocab, List<String> data) {
		List<Integer> vec = new ArrayList<>();
		int[] temp = new int[vocab.size()];
		for (int i = 0; i < vocab.size(); i++) {
			vec.add(0);
		}
		for (int i = 0; i < data.size(); i++) {
			if (vocab.contains(data.get(i))) {
				temp[(vocab.indexOf(data.get(i)))]++;
			}
		}
		for (int i = 0; i < vocab.size(); i++) {
			vec.set(i, temp[i]);
		}
		System.out.println(vec);
		return vec;
	}
}
