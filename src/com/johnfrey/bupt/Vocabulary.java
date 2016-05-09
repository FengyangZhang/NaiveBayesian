package com.johnfrey.bupt;
import java.util.ArrayList;
import java.util.List;

public class Vocabulary {
	public List<String> getVocab(List<List<String>> datas){
		List<String> vocabulary = new ArrayList<String>();
		for(List<String> data:datas){
			for(String word:data){
				if(!vocabulary.contains(word)){//ʵ�ֲ���
					vocabulary.add(word);
				}
			}
		}
		return vocabulary;
	}
}
