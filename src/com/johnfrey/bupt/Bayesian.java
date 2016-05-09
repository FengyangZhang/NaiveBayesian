package com.johnfrey.bupt;

import java.util.ArrayList;
import java.util.List;

public class Bayesian {
	/**
	 * @param args
	 */
	public static void main(String args[]){
		TrainData dataManager = new TrainData();//�øõ�������ѵ����
		Vocabulary vocabulary = new Vocabulary();//���ݴʻ��
		TrainVector vectorManager = new TrainVector();//����������
		//��ʼ��
		dataManager.init();
		vocabulary.init(dataManager.getData());
		vectorManager.init();
		for(int i = 0;i<dataManager.getData().size();i++){
			vectorManager.data2Vector(vocabulary.get(), dataManager.getData().get(i));
		}
		vectorManager.train(dataManager.getDataClass());
		List<String> test = new ArrayList<>();
		List<Integer> testVec = new ArrayList<>();
		test.add("sad");
		testVec = vectorManager.test2Vector(vocabulary.get(), test);
		vectorManager.judge(testVec);
		System.out.println(test);
	}
}
