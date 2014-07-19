package com.bit.model;

import java.util.ArrayList;


/*
 * ����˵����
 * 
 */

public class DijkstraPath
{
	public ArrayList<Side> map;
	public ArrayList<String> redAgg;
	public ArrayList<String> blueAgg;
	public Side[] parents;
	public String sourceNode;//Դ�ڵ�
	public String destinationNode;//Ŀ�Ľڵ�
	public ArrayList<String> addedNodeList;//��¼�Ѿ���ӵĽ��
	public ArrayList<String> shortestPath;//Դ��㵽Ŀ�Ľ������·��
	public int shortestPathWeight;//Դ��㵽Ŀ�Ľ������·����Ȩֵ

	public DijkstraPath(String sourceNode,String destinationNode)
	{
		this.map = new ArrayList<Side>();
		this.redAgg = null;
		this.blueAgg = null;
		this.parents = null;
		this.sourceNode = sourceNode;
		this.destinationNode = destinationNode;
		this.addedNodeList = new ArrayList<String>();
		this.addedNodeList.add(sourceNode);
		this.shortestPath = null;
		this.shortestPathWeight = -1;
	}

	public String getParent(Side[] parents, String node)
	{
		if (parents != null)
		{
			for (Side nd : parents)
			{
				if (nd.node.equals(node))
				{
					return nd.preNode;
				}
			}
		}
		return "None";
	}

	public boolean addSide(String nodeI,String nodeJ,int weight)
	{
    	String preNode;
    	String node;
    	if(nodeI.equals(nodeJ))
    	{
    		return false;
    	}
    	//�趨����nodeI��nodeJ��addedNodeList�еĴ�������趨preNode��node
    	else{
        	int nodeIIndex = this.addedNodeList.indexOf(nodeI);
        	int nodeJIndex = this.addedNodeList.indexOf(nodeJ);
        	//���nodeI��nodeJ��sourceNode,���趨��ΪpreNode
        	if(nodeIIndex == 0){
        		preNode = nodeI;
        		node = nodeJ;
        	}
        	else if(nodeJIndex == 0){
        		preNode = nodeJ;
        		node = nodeI;
        	}
        	//���nodeI��nodeJ������sourceNode
        	else{
        		//���nodeI��nodeJ��������addedNodeList�У�����򲻱�
        	    if(nodeIIndex > 0 && nodeJIndex > 0){
        			preNode = nodeI;
        			node = nodeJ;
        	    }
        	    //���ֻ��nodeI������addedNodeList�У�������ΪpreNode
        		else if(nodeIIndex > 0 && nodeJIndex == -1){
        			preNode = nodeI;
        			node = nodeJ;
        		}
        	    //���ֻ��nodeJ������addedNodeList�У�������ΪpreNode
        		else if(nodeIIndex == -1 && nodeJIndex > 0) {
        			preNode = nodeJ;
        			node = nodeI;
        		}
        	    //���nodeI��nodeJ����������addedNodeList�У�����Ӵ˱�
        		else{
        			return false;
        		}
        	}
 	    }
    	//���node��������addedNodeList�У������
    	if(!this.addedNodeList.contains(node))
    		this.addedNodeList.add(node);
    	this.map.add(new Side(preNode,node,weight));
       	return true;
	}

	/**
	 * �����������㼯��ʣ��ڵ�����·������
	 */
	public void setWeight(String preNode)
	{
		if (this.map != null && this.parents != null && this.blueAgg != null)
		{
			for (String node : this.blueAgg)
			{
				MinShortPath msp= getMinPath(node);
				int w1 = msp.getWeight();
				if (w1 == -1)
					continue;
				for (Side n : this.parents)
				{
					if (n.node == node)
					{
						if (n.weight == -1 || n.weight > w1)
						{
							n.weight = w1;
							n.preNode = (preNode);//�������ö���ĸ�����
							break;
						}
					}
				}
			}
		}
	}
	/**
	 * �õ�����ڵ�֮���Ȩ��
	 *
	 * @param map
	 * @param preNode
	 * @param node
	 * @return
	 */
	public int getWeight(String preNode, String node)
	{
		if (this.map != null){
			for (Side s : this.map){
				if (s.preNode.equals(preNode) && s.node.equals(node))
					return s.weight;
			}
		}
		return -1;
	}
	/**
	 * �����㼯�����ҳ�·����С���Ǹ��ڵ�
	 *
	 * @param map
	 * @param blueAgg
	 * @return
	 */
	public MinShortPath getMinSideNode(){
		MinShortPath minMsp = null;
		if (this.blueAgg.size() > 0){
			int index = 0;
			for (int j = 0; j < this.blueAgg.size(); j++){
				MinShortPath msp = getMinPath(this.blueAgg.get(j));
				if (minMsp == null || msp.getWeight()!=-1 && msp.getWeight() < minMsp.getWeight()){
					minMsp = msp;
					index = j;
				}
			}
			this.blueAgg.remove(index);
		}
		return minMsp;
	}
	/**
	 * �õ�ĳһ�ڵ�����·��(ʵ���Ͽ����ж���,����ֻ����һ��)
	 *
	 * @param node
	 * @return
	 */
	public MinShortPath getMinPath(String node)
	{
		MinShortPath msp = new MinShortPath(node);
		if (this.parents != null && this.redAgg != null){
			for (int i = 0; i < this.redAgg.size(); i++){
				MinShortPath tempMsp = new MinShortPath(node);
				String parent = this.redAgg.get(i);
				String curNode = node;
				while (!parent.equals("None")){
					int weight = getWeight(parent, curNode);
					if (weight > -1){
						tempMsp.addNode(parent);
						tempMsp.addWeight(weight);
						curNode = parent;
						parent = getParent(this.parents, parent);
					} else
						break;
				}
				if (msp.getWeight() == -1 || tempMsp.getWeight()!=-1 && msp.getWeight() > tempMsp.getWeight())
					msp = tempMsp;
			}
		}
		return msp;
	}
	/**
	 * �õ���Դ�㵽Ŀ�Ľڵ�����·��
	 *
	 * @param nodes
	 * @return
	 */
	public boolean findShortestPath()
	{
		//��ʼ����֪���·���Ķ��㼯������㼯��ֻ���붥��0
		this.redAgg = new ArrayList<String>();
		this.redAgg.add(this.sourceNode);
		//��ʼ��δ֪���·���Ķ��㼯,�����㼯
		this.blueAgg = new ArrayList<String>();
		for (int i = 1; i < this.addedNodeList.size(); i++)
			this.blueAgg.add(this.addedNodeList.get(i));
		//��ʼ��ÿ�����������·���еĸ����,������֮���Ȩ��,Ȩ��-1��ʾ����ͨ
		this.parents = new Side[this.addedNodeList.size()];
		this.parents[0] = new Side("None", this.sourceNode, 0);
		for (int i = 0; i < this.blueAgg.size(); i++){
			String n = this.blueAgg.get(i);
			this.parents[i + 1] = new Side(this.sourceNode, n, this.getWeight(this.sourceNode, n));
		}
		//�Ҵ����㼯���ҳ�Ȩ����С���Ǹ�����,���������뵽��㼯��
		while (this.blueAgg.size() > 0)
		{
			MinShortPath msp = this.getMinSideNode();
//			System.out.println(msp.getNodeList().toString());//
			String lastNode = msp.getLastNode();
			if(lastNode.equals(this.destinationNode))
			{
				this.shortestPath = msp.getNodeList();
				this.shortestPathWeight = msp.getWeight();
				return true;
			}
			this.redAgg.add(lastNode);
			// �����Ϊ�������µĶ���,���������㼯�еĶ�������·����С,��Ҫ��Ҫ����
			this.setWeight(lastNode);
		}
		return false;
	}

}


class MinShortPath
{
	public ArrayList<String> nodeList;// ���·����
	public int weight;// ���·��
	
	public MinShortPath(String node)
	{
		nodeList = new ArrayList<String>();
		nodeList.add(node);
		weight = -1;
	}
	public ArrayList<String> getNodeList(){
		return nodeList;
	}
	public void setNodeList(ArrayList<String> nodeList){
		this.nodeList = nodeList;
	}
	public void addNode(String node){
		if (nodeList == null)
			nodeList = new ArrayList<String>();
		nodeList.add(0, node);
	}
	public String getLastNode(){
		int size = nodeList.size();
		return nodeList.get(size - 1);
	}
	public int getWeight(){
		return weight;
	}
	public void setWeight(int weight){
		this.weight = weight;
	}
	public ArrayList<String> outputPath(int weight,String srcNode){
		if(weight == -1)
			nodeList.add(srcNode);
		System.out.println(nodeList.toString()+":"+weight);
		return nodeList;
	}
	public void addWeight(int w){
		if (weight == -1)
			weight = w;
		else
			weight += w;
	}
}