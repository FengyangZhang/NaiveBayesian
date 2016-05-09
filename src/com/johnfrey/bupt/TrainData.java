package com.johnfrey.bupt;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.crypto.Data;
public class TrainData {
	private List<List<String>> trainData;
	private List<Integer> trainDataClass;

	public void init(){
		trainData = new ArrayList<List<String>>();
		trainDataClass = new ArrayList<Integer>();
		File trainSet = new File("./data.txt");
		try {
            FileInputStream in = new FileInputStream(trainSet);
            if (in != null) {
                InputStreamReader inputreader = new InputStreamReader(in);
                BufferedReader buffreader = new BufferedReader(inputreader);
                String line;
                //���ж�ȡ
                int lnum = 0;
                while ((line = buffreader.readLine()) != null) {
					line = line.trim();
					//��ȡ���࣬����ѵ����
					int wnum = 0;
					while (line.charAt(wnum) != '	') {
						wnum++;
					}
					wnum++;
					System.out.println((int) line.charAt(wnum) - 48);
					trainDataClass.add((int) line.charAt(wnum) - 48);
					//��ȡÿ�仰�еĴ������ѵ����
					line = line.substring(wnum + 1).trim();
					Pattern p = Pattern.compile("[.,\"\\?!:'\r\n]");// ���Ӷ�Ӧ�ı��
					Matcher m = p.matcher(line);
					line = m.replaceAll(" ");
					String temp[] = line.toLowerCase().split(" ");
					System.out.println(line);
					trainData.add(Arrays.asList(temp));
					lnum++;
					System.out.println(lnum);
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
		trainData.add(Arrays.asList(new String[]{"my","dog","has","flea"}));
		trainData.add(Arrays.asList(new String[]{"you","are","an","ass","ass"}));
		trainData.add(Arrays.asList(new String[]{"don't","touch","him","asshole"}));
		trainData.add(Arrays.asList(new String[]{"take","him","for","a","walk"}));
		trainDataClass.add(1);
		trainDataClass.add(0);
		trainDataClass.add(0);
		trainDataClass.add(1);
	}
	
	public void print(){
		for(int i = 0;i<trainDataClass.size();i++){
			System.out.println(trainData.get(i)+trainDataClass.get(i).toString());
		}
	}
	
	public void addEntry(int dataClass,String... data){
		trainDataClass.add(dataClass);
		List<String> mdata = new ArrayList<String>();
		for(String word:data){
			mdata.add(word);
		}
		trainData.add(mdata);
	}
	
	public List<List<String>> getData(){
		return trainData;
	}
	
	public List<Integer> getDataClass(){
		return trainDataClass;
	}
}
