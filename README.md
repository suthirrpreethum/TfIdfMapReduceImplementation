# TfIdf for two gram words
Terminologies:
1. TF-IDF is the product of two statistics : Term Frequency (TF) and Inverse Document Frequency (IDF).
2. TF is the number of times a term (word) occurs in a document.
3. IDF is a numerical statistic that is intended to reflect how important a word is to a document.
4. Two Grams is a pair of words which is taken together as a single value. The two grams are used, so that the output tfidf values will produce a better signature about the document

Files:
PhaseOne.java, PhaseTwo.java, PhaseThree.java are the three phases of map reduce program for calculating the values needed for generating Tfidf values.
CalculateTfIdf.java is a stand alone code, which recieves the output of map reduce program and calculates the TfIdf values. 
PhaseOne.txt, PhaseTwo.txt, PhaseThree.txt are the output files generated at the end of map reduce phase

Result:
The values(two grams) with highest tfIdf will provide a better signature about the document.
