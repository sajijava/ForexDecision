package com.snr.fxstrategyea.engine;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.snr.fxstrategyea.agent.Action;
import com.snr.fxstrategyea.agent.AgentComparator;
import com.snr.fxstrategyea.agent.IndicatorAgent;
import com.snr.fxstrategyea.model.DecisionTree;
import com.snr.fxstrategyea.model.DecisionTree.Node;

public class AgentDecisionTreeBuilder {

	private Logger logger = LoggerFactory.getLogger(AgentDecisionTreeBuilder.class);
	private static final Comparator<IndicatorAgent> comparator = new AgentComparator();
	private int maxDepth;
	public AgentDecisionTreeBuilder(int maxDepth){
		this.maxDepth = maxDepth;
	}
	public DecisionTree buildTree(List<IndicatorAgent> agentList){
	
		Collections.sort(agentList, comparator);
		DecisionTree dt = new DecisionTree();
		Random random = new Random();
		dt.setRootNode(DecisionTree.getNewNode(getRandomAgent(agentList, random)));
		buildNode(dt.getRootNode(),agentList,1,random);
		
		return dt;
	}
	private void buildNode(DecisionTree.Node node, List<IndicatorAgent> agentList, int level, Random random)
	{
		if(level < maxDepth){
			level++;
			for(Map.Entry<Action, DecisionTree.Node> item: node.getChildren().entrySet()){
				if(item.getValue() == null){
					item.setValue(DecisionTree.getNewNode(getRandomAgent(agentList, random)));
					buildNode(item.getValue(),agentList,level,random);
				}
			}
		}else{
			level--;
		}
	}
	private IndicatorAgent getRandomAgent(List<IndicatorAgent> agentList, Random random){
		int nextInt = random.nextInt(agentList.size());
		return agentList.get(nextInt);
		
	}
	public static void showDepthFirst(DecisionTree.Node dt, int tabSpace){
			
			System.out.println(dt.getAgent().getClass().getSimpleName());
			showChildren(dt,tabSpace+1);
		
	}
	public static void showChildren(DecisionTree.Node dt, int tabSpace){
		String tabs = "";
		for(int i = 0; i < tabSpace; i++)	tabs += "\t";

		for(Map.Entry<Action, DecisionTree.Node> item: dt.getChildren().entrySet()){
			if(item.getValue() != null){
			System.out.println(tabs+item.getKey()+"."+item.getValue().getAgent().getClass().getSimpleName());
			showChildren(item.getValue(),tabSpace + 1);
			}
		}
	}
}

