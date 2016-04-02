package com.johnfrey.bupt;

import java.util.ArrayList;
import java.util.List;

public class Bayesian {
	public static void main(String args[]){
		TrainData dataManager = new TrainData();//用该单例操纵训练集
		Vocabulary vocabulary = new Vocabulary();//操纵词汇表
		TrainVector vectorManager = new TrainVector();//操纵向量表
		//初始化
		dataManager.init();
		vocabulary.init(dataManager.getData());
		vectorManager.init();
		for(int i = 0;i<dataManager.getData().size();i++){
			vectorManager.data2Vector(vocabulary.get(), dataManager.getData().get(i));
		}
		vectorManager.train(dataManager.getDataClass());
		List<String> test = new ArrayList<>();
		List<Integer> testVec = new ArrayList<>();
		test.add("my");
		test.add("ass");
		testVec = vectorManager.test2Vector(vocabulary.get(), test);
		vectorManager.judge(testVec);
	}
}
