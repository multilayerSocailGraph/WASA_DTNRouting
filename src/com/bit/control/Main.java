package com.bit.control;

import java.text.DecimalFormat;

import com.bit.model.Node;
  
public class Main
{
	public static Node[] validNodes;			//��ѡ�ڵ�
	public static int[][] contactDurationMatrix;
	public static int[][] contactCountMatrix;
	public static double[][] contactRateMatrix;
	public static int[][] contactFreshMatrix;
	public static int[][] socialGraphMatrix;
	public static int[][] similarityMatrix;
	public static double[] betweeness;
	
	public static int testTimes = BeforeRouting.test_TimeArray.length-1;//���еĲ��Դ���
	public static int[] coupleArray = new int[testTimes];//ĳ�β��Ե������������������ڳ��������趨�ã�һ����ÿ��·���㷨�Ĺ��캯�����趨
	public static int[] successArray = new int[testTimes];//ĳ�β������ս����ɹ���������Ŀ
	public static int[] delayArray = new int[testTimes];//ĳ�β��������гɹ�������������ʱ���ܺ�
	public static int[] hopArray = new int[testTimes];//ĳ�β��������гɹ������������������ܺ�
	public static int[] forwardArray = new int[testTimes];//ĳ�β����е�����������ת���������ܺ�
	public static int[] maxLoadInOneTest = new int[testTimes];//ĳ�β����е����нڵ�������
	public static double[] averageLoadInOneTest = new double[testTimes];//ĳ�β����е����нڵ��ƽ������
	
	public static int[] load;//ÿһ���ڵ�ĸ������
	
	public static void main(String[] args)
	{
		BeforeRouting beforeRounting = new BeforeRouting();
		beforeRounting.createMatrix();
		Main.load = new int[Main.validNodes.length];
		
//		System.out.println("\n------------------SimBetRouting--------------");
//		clearData();
//		for(int i = 0; i<testTimes; i++)//SimBetRouting
//		{
//			SimBetRouting simbet = new SimBetRouting(BeforeRouting.test_TimeArray[0], BeforeRouting.test_TimeArray[i+1]);
//			simbet.execute();
//			simbet.calcPerformance(i);
//			Main.clearLoad();
//		}
//		showPerformance();
		
		
		
		clearData();
		System.out.println("\n------------------SimBetWithTTLRouting--------------");
		for(int i = 0; i<testTimes; i++)//BetWithTTLRouting
		{
			SimBetWithTTLRouting simbet_ttl = new SimBetWithTTLRouting(BeforeRouting.test_TimeArray[0], BeforeRouting.test_TimeArray[i+1]);
			simbet_ttl.execute();
			simbet_ttl.calcPerformance(i);
			Main.clearLoad();
		}
		showPerformance();


//		clearData();
//		System.out.println("\n------------------EpidemicRouting--------------");
//		for(int i = 0; i<testTimes; i++)//EpidemicRouting
//		{
//			EpidemicRouting epidemic = new EpidemicRouting(BeforeRouting.test_TimeArray[0], BeforeRouting.test_TimeArray[i+1]);
//			epidemic.execute();
//			epidemic.calcPerformance(i);
//			Main.clearLoad();
//		}
//		showPerformance();

		
		
//		clearData();
//		System.out.println("\n------------------FreshRouting--------------");
//		for(int i = 0; i<testTimes; i++)//FreshRouting
//		{
//			FreshRouting fresh = new FreshRouting(BeforeRouting.test_TimeArray[0], BeforeRouting.test_TimeArray[i+1]);
//			fresh.execute();
//			fresh.calcPerformance(i);
//			Main.clearLoad();
//		}
//		showPerformance();

		
		
//		clearData();
//		System.out.println("\n------------------FrequencyRouting--------------");
//		for(int i = 0; i<testTimes; i++)//FrequencyRouting
//		{
//			FrequencyRouting frequency = new FrequencyRouting(BeforeRouting.test_TimeArray[0], BeforeRouting.test_TimeArray[i+1]);
//			frequency.execute();
//			frequency.calcPerformance(i);
//			Main.clearLoad();
//		}
//		showPerformance();
		
	}
	
	public static void showPerformance()
	{
		System.out.print("delivery ratio:");
		for(int i = 0; i<testTimes; i++)
		{
			double deliRatio = (double)successArray[i]/6006;
			DecimalFormat df = new DecimalFormat("#.000");
			System.out.print("0"+df.format(deliRatio)+",");
		}
		System.out.print("\ndelay:");
		for(int i = 0; i<testTimes; i++)
		{
			double delay = (double)delayArray[i]/Main.successArray[i];
			DecimalFormat df = new DecimalFormat("#.000");
			System.out.print(df.format(delay)+",");
		}
		System.out.print("\nhop:");
		for(int i = 0; i<testTimes; i++)
		{
			double hop = (double)hopArray[i]/Main.successArray[i];
			DecimalFormat df = new DecimalFormat("#.000");
			System.out.print(df.format(hop)+",");
		}
		System.out.print("\nforwards:");
		for(int i = 0; i<testTimes; i++)
		{
			double forward = (double)forwardArray[i]/6006;
			DecimalFormat df = new DecimalFormat("#.000");
			System.out.print(df.format(forward)+",");
		}
		System.out.print("\nmaxLoad:");
		for(int i = 0; i<testTimes; i++)
		{
			System.out.print(Main.maxLoadInOneTest[i]+",");
		}
		System.out.print("\naverageLoad:");
		for(int i = 0; i<testTimes; i++)
		{
			DecimalFormat df = new DecimalFormat("#.000");
			System.out.print(df.format(Main.averageLoadInOneTest[i])+",");
		}
	}
		
	public static void clearData()
	{
		for(int i = 0; i<testTimes; i++)//BetweennessRouting
		{
			coupleArray[i] = 0;
			successArray[i] = 0;
			delayArray[i] = 0;
			hopArray[i] = 0;
			forwardArray[i] = 0;
			maxLoadInOneTest[i] = 0;
			averageLoadInOneTest[i] = 0;
		}
		for(int i = 0; i<load.length; i++)
		{
			load[i] = 0;
		}
	}
	
	public static void clearLoad()
	{
		for(int i = 0; i<Main.load.length; i++)
		{
			Main.load[i] = 0;
		}
	}
	
}
