# NaiveBayesian
Naive Bayesian method for a simple Android app
An implementation of a simple machine learning algorithm--Naive Bayesian algorithm in order to judge if a given list of string is abusive or not.
<!-- more -->

## Usage:
This program is for the specific use of my android diary app *Whisper*,which is a private diary highlighting a function of judging if a diary is positive or not and giving the corresponding feedback.
[The diary app *"Whisper"* on Github](https://github.com/JohnfyBUPT/Whisper)
Codes on *Whisper* will fall a little behind this one.

---
## Theory foundation:
Naive Bayes classifier is a simple and effective classify method based on Bayes theorem.
### Bayes theorem:
    
    P(A|B) = P(B|A) P(A) / P(B)
Now let's see A as a specific category C from a collection C1、C2、...、Cm,and see B as a combination of n features F1、F2、...Fn of a certain individual.
What we want to do is to deduct which class an individual belongs from his/her set of features.We now know how to apply Bayes theorem to classifying procedure,which is to calculate the maximum value of the expression below:

    P(C|F1F2...Fn) = P(F1F2...Fn|C)P(C) / P(F1F2...Fn)
From which P(F1F2...Fn) can be left out,for it has the same value when C changes.The question deteriorate into calculate the maximum of:
    
    P(F1F2...Fn|C)P(C)
### Naive Bayes theorem:
If we take a step further and assume all n features are independent,we are using Naive Bayesian theorem,and the question is simplified again:
    
    P(F1F2...Fn|C)P(C) = P(F1|C)P(F2|C) ... P(Fn|C)P(C)
We can get every *P* on the right side of the  equation from our training set,and the problem is solved.

---
## Key points and codes:
### Train data to dictionary:
```java    
    for (List<String> data : trainData) {
		for (String word : data) {
			if (!vocabulary.contains(word)) {
				vocabulary.add(word);
			}
		}
	}
```
### Train data to train vectors:
```java		
    for (int i = 0; i < trainData.get(j).size(); i++) {
		if (vocabulary.contains(trainData.get(j).get(i))) {
			temp[(vocabulary.indexOf(trainData.get(j).get(i)))]++;
		}
	}
	for (int i = 0; i < vocabulary.size(); i++) {
		vec.set(i, temp[i]);
	}
	trainVector.add(vec);
```
### Train vectors to feature vector:
```java    
    pAbusive = (double) (count) / trainDocNum;
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
			for (int j = 0; j < trainVector.get(i).size(); j++) {
				if (trainVector.get(i).get(j) == 1) {
					countp1++;
				}
			}
			p1Den += countp1;
		} else {
			for (int k = 0; k < wordsNum; k++) {
				p0Num[k] += trainVector.get(i).get(k);
			}
			int countp2 = 0;
			for (int k = 0; k < trainVector.get(i).size(); k++) {
				if (trainVector.get(i).get(k) == 1) {
					countp2++;
				}
			}
			p0Den += countp2;
		}
	}
	for (int m = 0; m < wordsNum; m++) {
		p1Vec.add(Math.log((double) (p1Num[m]) / p1Den));
		p0Vec.add(Math.log((double) (p0Num[m]) / p0Den));
	}
```
We used a function called laplace here, and we also set the denominator to a certain value in case the value is too small and cause unpredicted situation. 
### Classify the given sentence:
```java    
    for (int x = 0; x < testVec.size(); x++) {
		p0 += p0Vec.get(x) * testVec.get(x);
		p1 += p1Vec.get(x) * testVec.get(x);

	}
	p1 += Math.log(pAbusive);
	p0 += Math.log(1 - pAbusive);
	if(p0 > -0.4 || p1>-1.4){
		System.out.println("Neutral Words!");
	}
	else if (p1 > p0) {
		System.out.println("Positive Words!");
	}
	else if(p0 > p1) {
		System.out.println("Negative Words!");
	}
```	
Maybe you noticed that we used logarithm adding instead of probability multiply,FYI that's to avoid overflow because the probability may be small.
### No need to train everytime
I also append a store function so that we don't need to train the raw data everytime,when there are history dictionary and feature vector,just parse the txt file and use it,making it a lot faster and convinient.

---
## See the output:
Take a simple sentence "Sad my friend is gone" for example,the algorithm will use the dictioary to convert this sentence to a vector and calculate p0 and p1 using the feature vector.And this is the console output:
    
    p0=-29.07335725097412   p1=-34.40233568665568   pA=0.421406667326145
	Negative Words!
	[sad, my, friend, is, gone]

---
## View codes on github:
[https://github.com/JohnfyBUPT/NaiveBayesian](https://github.com/JohnfyBUPT/NaiveBayesian)

---
## Reference：
[机器学习笔记：朴素贝叶斯方法（Naive Bayes）原理和实现 ](http://blog.csdn.net/tanhongguang1/article/details/45016421)
