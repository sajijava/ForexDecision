package com.snr.fxstrategyea.ea;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.uncommons.watchmaker.framework.operators.AbstractCrossover;

import com.snr.fxstrategyea.agent.Action;
import com.snr.fxstrategyea.engine.AgentDecisionTreeBuilder;
import com.snr.fxstrategyea.model.DecisionTree;

public class DecisionTreeCrossover extends AbstractCrossover<DecisionTree>{

	
	private Logger logger = LoggerFactory.getLogger(DecisionTreeCrossover.class);
	public DecisionTreeCrossover(int crossoverPoints) {
		super(crossoverPoints);
	}

	@Override
	protected List<DecisionTree> mate(DecisionTree parent1,DecisionTree parent2, int numberOfCrossoverPoints, Random rng) {
		if(parent1 == null ) throw new IllegalArgumentException("Parents cannot be null");
		if(numberOfCrossoverPoints < 1) throw new IllegalArgumentException("Crossover point need to be positive and  > 0");

		for(int i = 0; i < numberOfCrossoverPoints; i++){
			crossOver(parent1,parent2,rng);
		}
		return null;
	}
	
	private void crossOver(DecisionTree parent1,DecisionTree parent2, Random rng){
		List<Action> actionList = new ArrayList<Action>();

		logger.info("Parent 1");
		AgentDecisionTreeBuilder.showDepthFirst(parent1.getRootNode());
		logger.info("Parent 2");
		AgentDecisionTreeBuilder.showDepthFirst(parent2.getRootNode());

		DecisionTreeHelper.buildPath(parent1.getRootNode(),rng.nextInt(parent1.getDepth()),rng,actionList);
		logger.info("build path for crossover " +actionList);

		DecisionTree.Node nodeP1 = DecisionTreeHelper.getNode(parent1.getRootNode(), actionList);
		DecisionTree.Node nodeP2 = DecisionTreeHelper.getNode(parent2.getRootNode(), actionList);
		logger.info("Node P1");
		AgentDecisionTreeBuilder.showDepthFirst(nodeP1);
		logger.info("Node P2");
		AgentDecisionTreeBuilder.showDepthFirst(nodeP2);

		if(actionList.size() < 1){
			parent1.setRootNode(nodeP2);
			parent2.setRootNode(nodeP1);
		}else if(actionList.size()-1 > 0){
			List<Action> parentActionList = actionList.subList(0,actionList.size() - 1);
			logger.info("build path for parent action list" +parentActionList);

			Action key = actionList.get(actionList.size()-1);

			DecisionTreeHelper.getNode(parent1.getRootNode(), parentActionList).getChildren().put(key, nodeP2);
			DecisionTreeHelper.getNode(parent2.getRootNode(), parentActionList).getChildren().put(key, nodeP1);
		}else{
			Action key = actionList.get(0);

			parent1.getRootNode().getChildren().put(key, nodeP2);
			parent2.getRootNode().getChildren().put(key, nodeP1);
		}
		logger.info("parent 1");
		AgentDecisionTreeBuilder.showDepthFirst(parent1.getRootNode());
		logger.info("parent 2");
		AgentDecisionTreeBuilder.showDepthFirst(parent2.getRootNode());
		
	}

}
