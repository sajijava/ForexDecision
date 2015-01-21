package com.snr.fxstrategyea.ea;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.snr.fxstrategyea.agent.Action;
import com.snr.fxstrategyea.model.DecisionTree;

public class DecisionTreeHelper {

	private static Logger logger = LoggerFactory.getLogger(DecisionTreeHelper.class);
	public static void buildPath(DecisionTree.Node node,int level, Random rng,List<Action> actionList)
	{
		if(node.hasChildren()){
			List<Action> keys = new ArrayList<Action>(node.getChildren().keySet());
			int indexLeaf = rng.nextInt(keys.size());
			if(level > 0){
				actionList.add(keys.get(indexLeaf));
				if(node.getChildren().get(keys.get(indexLeaf)).hasChildren())
					buildPath(node.getChildren().get(keys.get(indexLeaf)),--level, rng, actionList);
			}
		}
	}
	public static DecisionTree.Node getNode(DecisionTree.Node node, List<Action> actionList){
		DecisionTree.Node traversableNode = node;
		if(node.hasChildren()){
			for(Action action : actionList){
				traversableNode = traversableNode.getChildren().get(action);
			}
		}
		return traversableNode;
	}

}
