//package com.suthirr.mapReduce.TfIdf;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CalculateTfIdf {
	
	public static void main(String[] args) {
		try {
			Map<String, Integer> phase1Output = new HashMap<>();
			Map<String, Integer> phase2Output = new HashMap<>();
			Map<String, Integer> phase3Output = new HashMap<>();
			Map<String, String> outputMap = new HashMap<>();
			ArrayList<String> outputDataList = new ArrayList<>();
			List<String> sortedDataOrder=new ArrayList<>();
			FileWriter writer=new FileWriter("output.txt");

//			File f = new File("phaseOne.txt");
			FileReader reader = new FileReader(new File("phaseOne.txt"));
			BufferedReader br = new BufferedReader(reader);
			ArrayList<String> tempDataList=getDataFromFile(br);
			for(String data:tempDataList) {
				String[] s=data.split("\t");
					phase1Output.put(s[0], Integer.parseInt(s[1]));
			}

			
			reader = new FileReader(new File("phaseTwo.txt"));
			br = new BufferedReader(reader);
			tempDataList=getDataFromFile(br);
			for(String data:tempDataList) {
				String[] s=data.split("\t");
				phase2Output.put(s[0], Integer.parseInt(s[1]));
			}
//			System.out.println(phase2Output.get("0001"));
			reader = new FileReader(new File("phaseThree.txt"));
			br = new BufferedReader(reader);
			tempDataList=getDataFromFile(br);
			for(String data:tempDataList) {
				String[] s=data.split("\t");
				phase3Output.put(s[0], Integer.parseInt(s[1]));
			}
			
			
			
			int count=0;
			DecimalFormat df=new DecimalFormat("##.##########");
			for(String tempDataKey: phase1Output.keySet())
			{
				int totalDocs= phase2Output.size();
				int docWithTerm= phase3Output.get(tempDataKey.split(",")[0]);
//				System.out.println("totalDocs/docWithTerm:"+(double)totalDocs/docWithTerm);
				double totalBydoc=(double)totalDocs/(double)docWithTerm;
				double idf=Math.log10(totalBydoc);
				int tFrequency= phase1Output.get(tempDataKey);
//				System.out.println("tFrequency:"+tFrequency);
				int totalTerms=phase2Output.get(tempDataKey.split(",")[1]);
//				System.out.println("totalTerms:"+totalTerms);
				double tf=(double)tFrequency/totalTerms;
				String tfIdf=String.format("%.12f", tf*idf);/*Double.parseDouble(df.format(tf*idf));*/
				String invertedTempData=tempDataKey.split(",")[1]+","+tempDataKey.split(",")[0];
				outputMap.put(invertedTempData, tfIdf);
				outputDataList.add(invertedTempData);
				
				String sortedData=tempDataKey.split(",")[1]+","+tfIdf+","+tempDataKey.split(",")[0];
				sortedDataOrder.add(sortedData);

			}
			Collections.sort(outputDataList);
			for(String tempData:outputDataList)
			{
//				System.out.println("Name:"+tempData.split(",")[1]+"\t\tDocName:"+tempData.split(",")[0]+"\t\tValue:"+outputMap.get(tempData));
				writer.write("Name:"+tempData.split(",")[1]+"\t\tDocName:"+tempData.split(",")[0]+"\t\tValue:"+outputMap.get(tempData)+"\n");
			}
			writer=new FileWriter("outputValueSorted.txt");
			Collections.sort(sortedDataOrder);
			String prevDocName="";
			int valuesPerDocumentCount=0;
			for(int i=sortedDataOrder.size()-1;i>=0;i--)
			{
				String tempData=sortedDataOrder.get(i);
				String[] tempDetails=tempData.split(",");
				if(!tempDetails[0].equals(prevDocName))
				{
					valuesPerDocumentCount=0;
					prevDocName=tempDetails[0];
				}
				if(valuesPerDocumentCount<15) {
				System.out.println("Name:"+tempDetails[2]+"\t\tDocName:"+tempDetails[0]+"\t\tValue:"+tempDetails[1]);
				writer.write("Name:"+tempData.split(",")[2]+"\t\tDocName:"+tempData.split(",")[0]+"\t\tValue:"+tempData.split(",")[1]+"\n");
				valuesPerDocumentCount++;
				}
			}
			

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	static ArrayList<String> getDataFromFile(BufferedReader br) throws IOException {
		ArrayList<String> tempData=new ArrayList<>();
		String data = "";
		String s = br.readLine();
//		System.out.println(s);
		while (s != null) {
			// data.add(s);
			// writer.write(s);
			tempData.add(s);

			s = br.readLine();
		}
		return tempData;
	}


}
