package com.bit.model;

public class Message 
{
	public static final int MAXTTL = 5;
	public Node srcNode = null;
	public Node desNode = null;
	public int TTL;		//����һ��Message������ʱ�䣬���TTL��Ϊ0�Ͱ�������
	
	public int flag = 0;
	
	public Message(Node src, Node des)
	{
		this.srcNode = src;
		this.desNode = des;
		TTL = 5;
	}
	
}