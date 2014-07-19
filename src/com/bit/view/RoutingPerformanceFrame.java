package com.bit.view;

import java.awt.Color;
import java.awt.Graphics;
import java.text.DecimalFormat;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

public class RoutingPerformanceFrame extends JFrame 
{
	private static final long serialVersionUID = 1L;
	private MyPanel contentPane;
	public int maxInLoad;
	public int minInLoad;
	
	public int[] load;
	public int successSum;
	public double deliverRatio;
	public double averageDelay;
	public double averageHops;
	public double averageForwards;

	public RoutingPerformanceFrame(int[] load, String algorithmName, int successSum, double deliverRatio,
			double averageDelay, double averageHops, double averageForwards)
	{
		setTitle(algorithmName);
		this.successSum = successSum;
		this.deliverRatio = deliverRatio;
		this.averageDelay = averageDelay;
		this.averageHops = averageHops;
		this.averageForwards = averageForwards;
		this.load = load;
		maxInLoad = maxInLoad();
		minInLoad = minInLoad();
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 600, 550);
		contentPane = new MyPanel(load, algorithmName, successSum, deliverRatio,
				 averageDelay, averageHops, averageForwards, maxInLoad, minInLoad);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
	}
	
	public int maxInLoad()
	{
		int max = load[0];
		for(int i = 0; i<load.length; i++)
		{
			if(load[i] > max)
				max = load[i];
		}
		return max;
	}
	
	public int minInLoad()
	{
		int min = load[0];
		for(int i = 0; i<load.length; i++)
		{
			if(load[i] < min)
				min = load[i];
		}
		return min;
	}

}

class MyPanel extends JPanel
{
	private static final long serialVersionUID = 1L;
	int ZERO_POINT_X = 50;
	int ZERO_POINT_Y = 480;
	int UNIT_DISTANCE = 4;
	int START_POINT_Y = 470;
	public int maxInLoad;
	public int minInLoad;
	
	public int[] load;
	int[] newLoad;
	public int successSum;
	public double deliverRatio;
	public double averageDelay;
	public double averageHops;
	public double averageForwards;
	
	public MyPanel(int[] load, String algorithmName, int successSum, double deliverRatio,
			double averageDelay, double averageHops, double averageForwards, int maxInLoad, int minInLoad)
	{
		super();
		this.successSum = successSum;
		this.deliverRatio = deliverRatio;
		this.averageDelay = averageDelay;
		this.averageHops = averageHops;
		this.averageForwards = averageForwards;
		this.load = load;
		this.minInLoad = minInLoad;
		this.maxInLoad = maxInLoad;
		newLoad = new int[load.length];
		for(int i = 0; i<load.length; i++)
		{
			double temp1 = (double)load[i]-minInLoad;
			double temp2 = (double)maxInLoad - minInLoad;
			newLoad[i] = (int)(temp1/temp2 *100);
		}
	}
	
	public void paint(Graphics g)
	{
		super.paint(g);
		drawXY(g);
		drawLoad(g);
		
		g.setColor(Color.RED);
		DecimalFormat df = new DecimalFormat("#.000");
		g.drawString("successSum = "+successSum, 400, 50);
		g.drawString("DeliverRatio = 0"+df.format(deliverRatio), 400, 70);
		g.drawString("AverageDelay = "+df.format(averageDelay), 400, 90);
		g.drawString("AverageHops = "+df.format(averageHops), 400, 110);
		g.drawString("AverageForwards = "+df.format(averageForwards), 400, 130);
	}
	
	public void drawXY(Graphics g)
	{
		g.drawLine(ZERO_POINT_X, ZERO_POINT_Y, ZERO_POINT_X + UNIT_DISTANCE * 100, ZERO_POINT_Y);
		g.drawLine(ZERO_POINT_X + UNIT_DISTANCE * 100, ZERO_POINT_Y, ZERO_POINT_X + UNIT_DISTANCE * 100 -5, ZERO_POINT_Y -5);
		g.drawLine(ZERO_POINT_X + UNIT_DISTANCE * 100, ZERO_POINT_Y, ZERO_POINT_X + UNIT_DISTANCE * 100 -5, ZERO_POINT_Y +5);
		g.drawString("0", ZERO_POINT_X  -6, ZERO_POINT_Y +15);
		g.drawString("Node INDEX", ZERO_POINT_X + UNIT_DISTANCE * 105, ZERO_POINT_Y+5);
		for(int i = 1; i<=load.length; i++)
		{
			g.drawLine(ZERO_POINT_X + UNIT_DISTANCE * i, ZERO_POINT_Y, ZERO_POINT_X + UNIT_DISTANCE * i, ZERO_POINT_Y-2);
			if(i%10 == 0)
			{
				g.drawLine(ZERO_POINT_X + UNIT_DISTANCE * i, ZERO_POINT_Y, ZERO_POINT_X + UNIT_DISTANCE * i, ZERO_POINT_Y-4);
				g.drawString(String.valueOf(i), ZERO_POINT_X + UNIT_DISTANCE * i -6, ZERO_POINT_Y +15);
			}
		}//X坐标处理完毕
		
		g.drawLine(ZERO_POINT_X, START_POINT_Y, ZERO_POINT_X, START_POINT_Y - UNIT_DISTANCE * 105);
		g.drawLine(ZERO_POINT_X, START_POINT_Y - UNIT_DISTANCE * 105, ZERO_POINT_X-5, START_POINT_Y - UNIT_DISTANCE * 105 +5);
		g.drawLine(ZERO_POINT_X, START_POINT_Y - UNIT_DISTANCE * 105, ZERO_POINT_X+5, START_POINT_Y - UNIT_DISTANCE * 105 +5);
		g.setColor(Color.red);
		g.drawLine(ZERO_POINT_X, START_POINT_Y, ZERO_POINT_X, ZERO_POINT_Y);
		g.setColor(Color.BLACK);
		g.drawString(String.valueOf(minInLoad), ZERO_POINT_X -20, START_POINT_Y  +4);
		g.drawString("Load of Node", ZERO_POINT_X -30, START_POINT_Y - UNIT_DISTANCE * 105 -5);
		for(int i = 1; i<=100; i++)
		{
			g.drawLine(ZERO_POINT_X, START_POINT_Y - UNIT_DISTANCE * i, ZERO_POINT_X + 2, START_POINT_Y - UNIT_DISTANCE * i);
			if(i%10 == 0)
			{
				g.drawLine(ZERO_POINT_X , START_POINT_Y - UNIT_DISTANCE * i, ZERO_POINT_X + 4, START_POINT_Y - UNIT_DISTANCE * i);
				g.drawString(String.valueOf((maxInLoad - minInLoad)/100 * i + minInLoad), ZERO_POINT_X -20, START_POINT_Y - UNIT_DISTANCE * i +4);
			}
		}
	}//XY坐标建立完成
	
	public void drawLoad(Graphics g)
	{
		g.setColor(Color.BLUE);
		for(int i = 1; i<newLoad.length; i++)
		{
			int preX = ZERO_POINT_X + (i-1) * UNIT_DISTANCE;
			int preY = START_POINT_Y - newLoad[i-1] * UNIT_DISTANCE;
			int nowX = ZERO_POINT_X + i * UNIT_DISTANCE;
			int nowY = START_POINT_Y - newLoad[i] * UNIT_DISTANCE;
			g.drawLine(preX, preY, nowX, nowY);
		}
	}//drawLoad()
	
}
