package com.johnfrey.bupt;

import java.util.ArrayList;
import java.util.List;

public class TrainVector {

	private List<List<Integer>> trainVector = new ArrayList<>();
	//根据词汇表将句子转化为向量
	public void data2Vector(List<String> vocab,List<String> data){
		List<Integer> vec = new ArrayList<Integer>();
		int[] temp = new int[vocab.size()];
		for(int i=0;i<vocab.size();i++){
			vec.add(0);
		}
		for(int i=0;i<data.size();i++){
			if(vocab.contains(data.get(i))){
				temp[(vocab.indexOf(data.get(i)))]++;
			}
		}
		for(int i=0;i<vocab.size();i++){
			vec.set(i, temp[i]);
		}
		trainVector.add(vec);
	}
	public List<List<Integer>> get(){
		return trainVector;
	}
	public void print(){
		for(int i = 0;i<trainVector.size();i++){
			System.out.println(trainVector.get(i));
		}
	}
}
