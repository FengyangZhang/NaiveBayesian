package com.johnfrey.bupt;

public class Bayesian {
	public static void main(String args[]){
		TrainData dataManager = new TrainData();//�øõ�������ѵ����
		Vocabulary vocabulary = new Vocabulary();//���ݴʻ��
		TrainVector vectorManager = new TrainVector();//����������
		//��ʼ��
		dataManager.init();
		vocabulary.init(dataManager.getData());
		for(int i = 0;i<dataManager.getData().size();i++){
			vectorManager.data2Vector(vocabulary.get(), dataManager.getData().get(i));
		}
		vectorManager.print();
	}
}
