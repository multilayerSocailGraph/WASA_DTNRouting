package com.bit.model;


public class Couple 
{
	public Node srcNode = null;
	public Node desNode = null;
	
    public int startFowardTime = -1;
    public int endFowardTime = -1;
    public int fowardTimes = 0;	//ת��������������Ϣ�Ƿ�ɹ�������ת��һ�Σ�fowardTimes������1
    public int hops = 0;//·�������������Ϣ�ɹ���������ô��������ת����������������Ϊ0
    public int delay = 0;//��Ϣ��ʱ����Ϣ�ɹ�����ʱ������ʱ���ȥ��Ϣ����ʱ�������ʱ
    public boolean success = false;
    public int contactTimes = 0;
    
    public Couple(Node srcNode, Node desNode)
    {
    	this.srcNode = srcNode;
    	this.desNode = desNode;
    }//Couple
    
}
