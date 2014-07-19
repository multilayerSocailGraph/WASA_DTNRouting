package com.bit.control;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.StringTokenizer;
import com.bit.model.Contact;
import com.bit.model.Couple;
import com.bit.model.DijkstraPath;
import com.bit.model.Message;
import com.bit.model.Node;
import com.bit.view.RoutingPerformanceFrame;

public class EpidemicRouting 
{
	ArrayList<Couple> coupleList = new ArrayList<Couple>();
	ArrayList<Contact> contactList = new ArrayList<Contact>();

	public EpidemicRouting(int test_startTime, int test_endTime)//��ʼ��messageList��contactList
	{
		for(int i = 0; i < Main.validNodes.length; i++)//����һЩmsg����messageList���в���
		{
			for(int j = 0; j < Main.validNodes.length; j++)
			{
				if(i == j)
					continue;
				Couple couple = new Couple(Main.validNodes[i], Main.validNodes[j]);	//node i is the source node and node j is the destination node
				coupleList.add(couple);
			}
		}
		
		File f = new File("files/sortedContact.dat");
		Scanner scan = null;
		try {
			scan = new Scanner(f);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		while (scan.hasNextLine()) 
		{
			String temp = scan.nextLine();
			StringTokenizer tempTokenizer = new StringTokenizer(temp);
			String firstNodeName = tempTokenizer.nextToken();
			Node firstNode = Node.findNodeByName(firstNodeName, Main.validNodes);
			String secondNodeName = tempTokenizer.nextToken();
			Node secondNode = Node.findNodeByName(secondNodeName, Main.validNodes);
			int startTime = Integer.parseInt(tempTokenizer.nextToken());
			int endTime = Integer.parseInt(tempTokenizer.nextToken());
			int duration = endTime - startTime;
			if(firstNode != null && secondNode != null &&
					startTime >= test_startTime && startTime <= test_endTime)
			{
				Contact contact = new Contact(firstNode, secondNode, startTime, endTime, duration);
				contactList.add(contact);
			}
		}
		scan.close();
	}
	
	public void execute()
	{
		for(int i = 0; i < coupleList.size(); i++)//��ʼʹ��SimBet�㷨����ÿһ����Ϣ
		{
			Couple couple = coupleList.get(i);
			Node srcNode = couple.srcNode;
			Node desNode = couple.desNode;
			for(int w = 0; w<Main.validNodes.length; w++)
				Main.validNodes[w].msgQueue.clear();
			Message msg = new Message(srcNode, desNode);
			srcNode.msgQueue.addLast(msg);						//�趨srcNode��һ��message
			DijkstraPath path = new DijkstraPath(srcNode.name, desNode.name);
			
			for(int j = 0; j < contactList.size(); j++)
			{
				Node firstNodeInContact = contactList.get(j).firstNode;
				Node secondNodeInContact = contactList.get(j).secondNode;
				if(couple.startFowardTime == -1 && (firstNodeInContact.name.equals(srcNode.name) || secondNodeInContact.name.equals(srcNode.name)))
				{
					couple.startFowardTime = contactList.get(j).startTime;
				}
				
				Node preNode = null;	//Ŀǰ������Ϣ�Ľڵ㣬��Ҫת����Ϣ�Ľڵ�
				Node node = null;		//preNodeҪת��������һ���ڵ�
				
				if(firstNodeInContact.msgQueue.size() > 0 && secondNodeInContact.msgQueue.size() == 0)
				{
					preNode = firstNodeInContact;
					node = secondNodeInContact;
				}
				else if(secondNodeInContact.msgQueue.size() > 0 && firstNodeInContact.msgQueue.size() == 0)
				{
					preNode = secondNodeInContact;
					node = firstNodeInContact;
				}
				if(preNode!=null && node!=null && node.msgQueue.size()==0)
				{
					if(node.name.equals(desNode.name))
					{//���ת������Ŀ�Ľڵ�
						preNode.msgQueue.getFirst().TTL--;
						preNode.copyMessageToAnotherNode(node, 1);	//ת����Ϣ
						path.addSide(preNode.name, node.name, 1);
						couple.endFowardTime = contactList.get(j).endTime;
						couple.fowardTimes++;
						couple.success = true;
						Main.load[desNode.indexInValidNodes]++;
						Main.load[preNode.indexInValidNodes]++;
						break;
					}
					else
					{//��ûת����Ŀ�Ľڵ�
						preNode.msgQueue.getFirst().TTL--;
						if(preNode.msgQueue.getFirst().TTL == 0)
						{
							preNode.msgQueue.removeFirst();
							break;
						}
						else
						{
							preNode.copyMessageToAnotherNode(node, 1);
							path.addSide(preNode.name, node.name, 1);
							couple.fowardTimes++;
							Main.load[node.indexInValidNodes]++;
							Main.load[preNode.indexInValidNodes]++;
						}

					}//��ѡ�м̽���ת��
				}
			}//����contactList
			
			if(couple.success == true)
			{
				path.findShortestPath();
				couple.hops = path.shortestPath.size() - 1;
				if(couple.endFowardTime - couple.startFowardTime > 0)
				{
					couple.delay = couple.endFowardTime - couple.startFowardTime;
				}
			}
			
		}//����coupleList��ִ��ÿһ��ת��
	}//execute
	
	public void calcPerformance(int i)
	{	
		for(int w = 0; w<coupleList.size(); w++)
		{
			Couple couple = coupleList.get(w);
			if(couple.success == true)
			{
				Main.successArray[i]++;
				Main.delayArray[i] = Main.delayArray[i] + couple.delay;
				Main.hopArray[i] = Main.hopArray[i] + couple.hops;
			}
			Main.coupleArray[i]++;
			Main.forwardArray[i] = Main.forwardArray[i] + couple.fowardTimes;
		}
		Main.maxLoadInOneTest[i] = getMaxLoad(Main.load);
		Main.averageLoadInOneTest[i] = getAverageLoad(Main.load);
		
		double deliverRatio = (double)Main.successArray[i]/Main.coupleArray[i];
		double averageDelay = (double)Main.delayArray[i]/Main.successArray[i];
		double averageHops = (double)Main.hopArray[i]/Main.successArray[i];
		double averageForwards = (double)Main.forwardArray[i]/Main.coupleArray[i];
		System.out.println("\nEpidemicRouting---��"+(i+1)+"�β�������:"+Main.coupleArray[i]);
		System.out.println("·�ɳɹ���Ŀ : "+Main.successArray[i]);
		System.out.println("�ɹ���:"+deliverRatio);
		System.out.println("ƽ����ʱ:"+averageDelay);
		System.out.println("ƽ������:"+averageHops);
		System.out.println("ƽ��ת������:"+averageForwards);
		if(i==9)
		{
			RoutingPerformanceFrame frame = new RoutingPerformanceFrame(Main.load, "Epidemic Routing", Main.successArray[i],
			deliverRatio, averageDelay, averageHops, averageForwards);
			frame.setVisible(true);
		}
		
	}//showPerf()
	
	public int getMaxLoad(int[] load)
	{
		int max = 0;
		
		for(int i = 0; i<load.length; i++)
		{
			if(load[i]>max)
				max = load[i];
		}
		return max;
	}
	
	public double getAverageLoad(int[] load)
	{
		double sum = 0;
		
		for(int i = 0; i<load.length; i++)
		{
				sum += load[i];
		}
		return sum/load.length;
	}
	
}
