package com.bit.model;

class Side
{
	public String preNode; // ǰ��ڵ�
	public String node;// ����ڵ�
	public int weight;// Ȩ��

	public Side(String preNode, String node, int weight)
	{
		this.preNode = preNode;
		this.node = node;
		this.weight = weight;
	}
}
