package com.bit.model;


public class Contact 
{
	public Node firstNode = null;
	public Node secondNode = null;
	public int startTime;
	public int endTime;
	public int duration;
	
	public Contact(Node firstNode, Node secondNode, int startTime, int endTime, int duration)
	{
		this.firstNode = firstNode;
		this.secondNode = secondNode;
		this.startTime = startTime;
		this.endTime = endTime;
		this.duration = duration;
	}
}
