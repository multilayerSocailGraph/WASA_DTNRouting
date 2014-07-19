package com.bit.control;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;
import java.util.Stack;
import java.util.StringTokenizer;
import java.util.Vector;

import com.bit.model.Node;

class BeforeRouting 
{
	int train_Start_Time = 6207;//训练的开始时间(sortedContact.dat文件中节点开始发送数据的时间)
	int train_End_Time = 230000;//训练的结束时间<--------->同时也是开始测试的时间
//	static int test_Start_Time = 220000;//训练的结束时间<--------->同时也是开始测试的时间
	static int[] test_TimeArray = {230000,233600,237200,240800,244400,248000,251600,255200,258800,262400,266000,269600,273200,276800,280400,284000,287600,291200,294800,298400,302000,305600,309200,312800,316400,320000,323600,327200,330800,334400,338000,341600};
//	static int test_End_Time = 340808;//测试结束时间(sortedContact.dat文件中节点最后一次发送数据的时间)
	
	public BeforeRouting()
	{
		System.out.println("start scan validNodes...");
		createValidNodes();//ok
		System.out.println("scan validNodes complete!\n");
	}
	
	public void createMatrix()
	{
		System.out.println("start create contactDurationMatrix...");
		createContactDurationMatrix();
		System.out.println("create contactDurationMatrix complete!\n");
		
		System.out.println("start create contactCountMatrix...");
		createContactCountMatrix();
		System.out.println("create contactCountMatrix complete!\n");
		
		System.out.println("start create contactRateMatrix...");
		createContactRateMatrix();
		System.out.println("create contactRateMatrix complete!\n");
		
		System.out.println("start create contactFreshMatrix...");
		createContactFreshMatrix();
		System.out.println("create contactFreshMatrix complete!\n");
		
		System.out.println("start create socialGraphMatrix...");
		createSocialGraphMatrix();
		System.out.println("create socialGraphMatrix complete!\n");
		
		System.out.println("start create betweenessArray...");
		createBetweenessArray();
		System.out.println("create betweenessArray complete!\n");
		
		System.out.println("start create similarityMatrix...");
		createSimilarityMatrix();
		System.out.println("create similarityMatrix complete!\n");
	}
	
	public void createValidNodes()
	{
		ArrayList<String> array = new ArrayList<String>();
		
		File f = new File("files/ValidNodes.txt");
		BufferedReader br = null;
		try{
			br = new BufferedReader(new FileReader(f));
			String line = null;
			while((line=br.readLine())!= null)
			{
				array.add(line);
			}
			br.close();
		}catch(Exception e){
			e.printStackTrace();
		}
		String[] validNodesName = array.toArray(new String[] {});
		
		Main.validNodes = new Node[validNodesName.length];
		for(int i = 0; i<validNodesName.length; i++)
		{
			Node node = new Node(validNodesName[i]);
			node.indexInValidNodes = getNodeIndex(validNodesName, node.name);
			Main.validNodes[i] = node;
		}
		
	}
	
	public void createContactDurationMatrix()
	{
		File f = new File("files/sortedContact.dat");
		Scanner scan = null;
		try {
			scan = new Scanner(f);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		//获得contactDuration矩阵
		Main.contactDurationMatrix = new int[Main.validNodes.length][Main.validNodes.length];
		while (scan.hasNextLine()) 
		{
			String temp = scan.nextLine();
			StringTokenizer tempTokenizer = new StringTokenizer(temp);
			Node srcNode = Node.findNodeByName(tempTokenizer.nextToken(), Main.validNodes);
			Node desNode = Node.findNodeByName(tempTokenizer.nextToken(), Main.validNodes);
			if(srcNode == null || desNode == null)
				continue;
			int startTime = Integer.parseInt(tempTokenizer.nextToken());
			int endTime = Integer.parseInt(tempTokenizer.nextToken());
			int duration = endTime-startTime;
			
			if(srcNode.indexInValidNodes >= 0 && desNode.indexInValidNodes >= 0 && duration > 0 && startTime >= train_Start_Time && startTime < train_End_Time)
			{
				Main.contactDurationMatrix[srcNode.indexInValidNodes][desNode.indexInValidNodes] += duration;
				Main.contactDurationMatrix[desNode.indexInValidNodes][srcNode.indexInValidNodes] += duration;
			}
		}
		scan.close();
		//把contactDuration矩阵写入文件中
		File durationFile = new File("files/ContactDurationMatrix.txt");
		BufferedWriter bw = null;
		try{
			bw = new BufferedWriter(new FileWriter(durationFile));
			for(int i=0; i<Main.validNodes.length; i++)
			{
				for(int j = 0; j<Main.validNodes.length; j++)
				{
					bw.write(Main.contactDurationMatrix[i][j]+"\t");
				}
				bw.write("\n");
			}
			bw.close();
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
	
	public void createContactCountMatrix()
	{
		File f = new File("files/sortedContact.dat");
		Scanner scan = null;
		try {
			scan = new Scanner(f);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		Main.contactCountMatrix = new int[Main.validNodes.length][Main.validNodes.length];
		while (scan.hasNextLine()) 
		{
			String temp = scan.nextLine();
			StringTokenizer tempTokenizer = new StringTokenizer(temp);
			Node srcNode = Node.findNodeByName(tempTokenizer.nextToken(), Main.validNodes);
			Node desNode = Node.findNodeByName(tempTokenizer.nextToken(), Main.validNodes);
			if(srcNode == null || desNode == null)
				continue;
			int startTime = Integer.parseInt(tempTokenizer.nextToken());
			int endTime = Integer.parseInt(tempTokenizer.nextToken());
			int duration = endTime-startTime;
			
			if(srcNode.indexInValidNodes >= 0 && desNode.indexInValidNodes >= 0 && duration > 0 && startTime >= train_Start_Time && startTime < train_End_Time)
			{
				Main.contactCountMatrix[srcNode.indexInValidNodes][desNode.indexInValidNodes] += 1;
				Main.contactCountMatrix[desNode.indexInValidNodes][srcNode.indexInValidNodes] += 1;
			}
		}
		scan.close();
		
		File contactCountFile = new File("files/ContactCountMatrix.txt");
		BufferedWriter bw = null;
		try{
			bw = new BufferedWriter(new FileWriter(contactCountFile));
			for(int i=0; i<Main.validNodes.length; i++)
			{
				for(int j = 0; j<Main.validNodes.length; j++)
				{
					bw.write(Main.contactCountMatrix[i][j]+"\t");
				}
				bw.write("\n");
			}
			bw.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void createContactRateMatrix()
	{		
		Main.contactRateMatrix = new double[Main.validNodes.length][Main.validNodes.length];
		double totalDuration = (double)(340808 - 6207);
		for(int i = 0; i < Main.validNodes.length; i++)
		{
			for(int j = 0; j < Main.validNodes.length; j++)
			{
				Main.contactRateMatrix[i][j] = (1.0f * Main.contactCountMatrix[i][j] * 12*3600) / totalDuration;
			}
		}
		
		File contactRateFile = new File("files/ContactRateMatrix.txt");
		BufferedWriter bw = null;
		try{
			bw = new BufferedWriter(new FileWriter(contactRateFile));
			for(int i=0; i<Main.validNodes.length; i++)
			{
				for(int j = 0; j<Main.validNodes.length; j++)
				{
					bw.write(Main.contactRateMatrix[i][j]+"\t");
				}
				bw.write("\n");
			}
			bw.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void createContactFreshMatrix()
	{
		File f = new File("files/sortedContact.dat");
		Scanner scan = null;
		try {
			scan = new Scanner(f);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		Main.contactFreshMatrix = new int[Main.validNodes.length][Main.validNodes.length];
		while (scan.hasNextLine()) 
		{
			String temp = scan.nextLine();
			StringTokenizer tempTokenizer = new StringTokenizer(temp);
			Node srcNode = Node.findNodeByName(tempTokenizer.nextToken(), Main.validNodes);
			Node desNode = Node.findNodeByName(tempTokenizer.nextToken(), Main.validNodes);
			if(srcNode == null || desNode == null)
				continue;
			int startTime = Integer.parseInt(tempTokenizer.nextToken());
			int endTime = Integer.parseInt(tempTokenizer.nextToken());
			int duration = endTime-startTime;
			
			if(srcNode.indexInValidNodes >= 0 && desNode.indexInValidNodes >= 0 && duration > 0 && startTime > Main.contactFreshMatrix[srcNode.indexInValidNodes][desNode.indexInValidNodes] && startTime >= train_Start_Time && startTime < train_End_Time)
			{
				Main.contactFreshMatrix[srcNode.indexInValidNodes][desNode.indexInValidNodes] = startTime;
				Main.contactFreshMatrix[desNode.indexInValidNodes][srcNode.indexInValidNodes] = startTime;
			}
		}
		scan.close();
		
		File contactFreshFile = new File("files/ContactFreshMatrix.txt");
		BufferedWriter bw = null;
		try{
			bw = new BufferedWriter(new FileWriter(contactFreshFile));
			for(int i=0; i<Main.validNodes.length; i++)
			{
				for(int j = 0; j<Main.validNodes.length; j++)
				{
					bw.write(Main.contactFreshMatrix[i][j]+"\t");
				}
				bw.write("\n");
			}
			bw.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void createSocialGraphMatrix()
	{
		int CONTACT_COUNT_THRESHOLD_TO_FORM_SOCIALGRAPH = 1;
		Main.socialGraphMatrix = new int[Main.validNodes.length][Main.validNodes.length];
		for(int i = 0; i < Main.validNodes.length; i++)
		{
			for(int j = i; j < Main.validNodes.length; j++) 
			{
				if(Main.contactCountMatrix[i][j] >= CONTACT_COUNT_THRESHOLD_TO_FORM_SOCIALGRAPH)
					Main.socialGraphMatrix[i][j] = Main.socialGraphMatrix[j][i] = 1;
			}
		}
		
		File socialGraphFile = new File("files/SocialGraphMatrix.txt");
		BufferedWriter bw = null;
		try{
			bw = new BufferedWriter(new FileWriter(socialGraphFile));
			for(int i=0; i<Main.validNodes.length; i++)
			{
				for(int j = 0; j<Main.validNodes.length; j++)
				{
					bw.write(Main.socialGraphMatrix[i][j]+"\t");
				}
				bw.write("\n");
			}
			bw.close();
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
	
	public void createBetweenessArray() 
	{
		int nodeNum = Main.validNodes.length;
		Main.betweeness = new double[nodeNum];
		for(int i = 0; i < nodeNum; i++) 
		{
			Main.betweeness[i] = 0;
		}
		for(int s = 0; s < nodeNum; s++) 
		{
			Vector<ArrayList<Integer>> p = new Vector<ArrayList<Integer>>();
			for(int i = 0; i < nodeNum; i++) 
			{
				p.add(new ArrayList<Integer>());
			}
			Stack<Integer> S = new Stack<Integer>();
			Queue<Integer> Q = new LinkedList<Integer>();
			double[] delta = new double[nodeNum];
	   	  	for(int h = 0; h < nodeNum; h++) 
	   	  	{
	   	  		delta[h] = 0.0;
	   	  	}
	   	  	delta[s] = 1.0;
	   	  	int[] d = new int[nodeNum];
	   	  	for(int e = 0 ; e < nodeNum; e++) {
	   	  		d[e] = -1;
	   	  	}
	   	  	d[s] = 0;
	   	  	Q.add(s);
	   	  	while(!Q.isEmpty())
	   	  	{
	   	  		int v = Q.remove();
	   	  		S.push(v);
	   	  		for(int w = 0; w < nodeNum; w++) 
	   	  		{
	   	  			if(Main.socialGraphMatrix[v][w]!=0 && w != v) 
	   	  			{
	   	  				if(d[w] < 0) 
	   	  				{
	   	  					Q.add(w);
	   	  					d[w] = d[v] +1;
	   	  				}
	   	  				//shortest path to w via v
	   	  				if(d[w] == d[v] +1 && w != v)
	   	  				{
	   	  					delta[w] = delta[w] +delta[v];
	   	  					p.elementAt(w).add(v);
	   	  				}
	   	  			}
	   	  		}
	   	  	}
	   	  	
	   	  	double[] sum = new double[nodeNum];
	   	  	int v;
	   	  	for(v = 0; v < nodeNum; v++) 
	   	  	{
	   	  		sum[v]=0;
	   	  	}
	   	  	
	   	  	while(!S.empty()) 
	   	  	{
	   	  		int w = S.pop();
	   	  		Iterator<Integer> ix = p.elementAt(w).iterator();
	   	  		while(ix.hasNext()) 
	   	  		{
	   	  			int tmp = (Integer) ix.next();
	   	  			sum[tmp] = sum[tmp] + (delta[tmp] / delta[w]) * (1.0 + sum[w]);
	   	  		}
	   	  		if(w != s)
	   	  		Main.betweeness[w] = Main.betweeness[w] + sum[w] / 2.0;
	   	  	}
	   	  	
		}

		File betweenessArrayFile = new File("files/BetweenessArray.txt");
		BufferedWriter bw = null;
		try{
			bw = new BufferedWriter(new FileWriter(betweenessArrayFile));
			for(int i = 0; i<Main.validNodes.length; i++)
			{
				bw.write(Main.betweeness[i]+"\n");
			}
			bw.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	public void createSimilarityMatrix()
	{
		Main.similarityMatrix = new int[Main.validNodes.length][Main.validNodes.length];
		int temp;
		for(int i = 0; i < Main.validNodes.length; i++)
		{
			for(int j = Main.validNodes.length - 1; j >= i; j--) 
			{
				temp = 0;
				for(int k = 0; k < Main.validNodes.length; k++) 
				{
					if(Main.socialGraphMatrix[i][k] == 1 && Main.socialGraphMatrix[j][k] == 1)
						temp++;
				}
				Main.similarityMatrix[i][j] = temp;
				Main.similarityMatrix[j][i] = temp;
			}
		}

		File similarityMatrixFile = new File("files/SimilarityMatrix.txt");
		BufferedWriter bw = null;
		try{
			bw = new BufferedWriter(new FileWriter(similarityMatrixFile));
			for(int i=0; i<Main.validNodes.length; i++)
			{
				for(int j = 0; j<Main.validNodes.length; j++)
				{
					bw.write(Main.similarityMatrix[i][j]+"\t");
				}
				bw.write("\n");
			}
			bw.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static int getNodeIndex(String[] nodes, String node) 
	{
		int low = 0;
		int high = nodes.length - 1;
		while(low<=high)
		{
			int mid = (low+high) / 2;
			if(nodes[mid].equals(node))
				return mid;
			else if(nodes[mid].compareTo(node) < 0)
				low = mid + 1;
			else
				high = high - 1;

		}
		return -1;
	}
	
}
