package com.johnfrey.bupt;

import java.util.ArrayList;
import java.util.List;

public class Bayesian {
	private static List<List<String>> trainData;
	private static List<Integer> trainDataClass;
	private static List<String> vocabulary;
	/**
	 * @param args
	 */
	public static void main(String args[]){
		TrainData dataManager = new TrainData();
		Vocabulary vocabularyManager = new Vocabulary();
		TrainVector vectorManager = new TrainVector();
		trainData = dataManager.getTrainData();
		trainDataClass = dataManager.getTrainDataClass();
		vocabulary = vocabularyManager.getVocab(trainData);
		vectorManager.init();
		for(int i = 0;i<trainData.size();i++){
			vectorManager.data2Vector(vocabulary, trainData.get(i));
		}
		vectorManager.train(trainDataClass);
		List<String> test = new ArrayList<>();
		List<Integer> testVec = new ArrayList<>();
		test.add("sad");
		testVec = vectorManager.test2Vector(vocabulary, test);
		vectorManager.judge(testVec);
		System.out.println(test);
	}



	public void addEntry(int dataClass,String... data){
		trainDataClass.add(dataClass);
		List<String> mdata = new ArrayList<String>();
		for(String word:data){
			mdata.add(word);
		}
		trainData.add(mdata);
	}
}
