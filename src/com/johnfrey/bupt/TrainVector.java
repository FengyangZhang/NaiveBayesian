package com.johnfrey.bupt;

import java.util.ArrayList;
import java.util.List;

public class TrainVector {
	private List<List<Integer>> trainVector;
	private double pAbusive;
	private List<Double> p1Vec;
	private List<Double> p0Vec;

	public void init() {
		trainVector = new ArrayList<>();
		p1Vec = new ArrayList<>();
		p0Vec = new ArrayList<>();
	}

	// 根据词汇表将句子转化为向量
	public void data2Vector(List<String> vocab, List<String> data) {
		List<Integer> vec = new ArrayList<Integer>();
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

	public List<Integer> test2Vector(List<String> vocab, List<String> data) {
		List<Integer> vec = new ArrayList<Integer>();
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
		return vec;
	}

	public void train(List<Integer> trainDataClass) {
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

	public void judge(List<Integer> testVec) {

		double p1 = 0;
		double p0 = 0;
		for (int x = 0; x < testVec.size(); x++) {
			p0 += p0Vec.get(x) * testVec.get(x);
			p1 += p1Vec.get(x) * testVec.get(x);

		}
		p1 += Math.log(pAbusive);
		p0 += Math.log(1 - pAbusive);
		System.out.println("p0=" + p0 + "   p1=" + p1);
		if (p0 > p1) {
			System.out.println("Positive Words!");
		}
		else if(p1 > p0) {
			System.out.println("Negative Words!");
		} 
		else
			System.out.println("Neutral Words!");
	}

	public int[] laplace(int[] s, int weight) {
		for (int i = 0; i < s.length; i++) {
			s[i] = weight;
		}
		return s;
	}

	public List<List<Integer>> get() {
		return trainVector;
	}

	public void print() {
		for (int i = 0; i < trainVector.size(); i++) {
			System.out.println(trainVector.get(i));
		}
	}
}
