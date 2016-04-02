package com.johnfrey.bupt;

public class Bayesian {
	public static void main(String args[]){
		TrainData dataManager = new TrainData();//用该单例操纵训练集
		Vocabulary vocabulary = new Vocabulary();//操纵词汇表
		TrainVector vectorManager = new TrainVector();//操纵向量表
		//初始化
		dataManager.init();
		vocabulary.init(dataManager.getData());
		for(int i = 0;i<dataManager.getData().size();i++){
			vectorManager.data2Vector(vocabulary.get(), dataManager.getData().get(i));
		}
		vectorManager.print();
	}
}
