package com.snr.fxstrategyea.ea;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.uncommons.watchmaker.framework.EvolutionaryOperator;

import com.snr.fxstrategyea.agent.Action;
import com.snr.fxstrategyea.agent.IndicatorAgent;
import com.snr.fxstrategyea.engine.AgentDecisionTreeBuilder;
import com.snr.fxstrategyea.model.DecisionTree;

/*
 * for mutating a tree we mutate a single agent.
 * First pick a random agent from the agent list
 * second pick a random level
 * third pick a random branch
 * 
 * once the branch is identifed. replace the agent with the selected agent in step 1.
 */
public class DecisionTreeMutation implements EvolutionaryOperator<DecisionTree>{

	private Logger logger = LoggerFactory.getLogger(DecisionTreeMutation.class);
	private final List<IndicatorAgent> agentList;
	public DecisionTreeMutation(List<IndicatorAgent> agentList){
		this.agentList = agentList;
	}
	public List<DecisionTree> apply(List<DecisionTree> selectedCandidates,Random rng) {
		
		List<DecisionTree> mutated = new ArrayList<DecisionTree>(selectedCandidates.size());
		for(DecisionTree tree : selectedCandidates){
			logger.info("Before Mutation");
			AgentDecisionTreeBuilder.showDepthFirst(tree.getRootNode());
			List<Action> actionList = new ArrayList<Action>();
			DecisionTreeHelper.buildPath(tree.getRootNode(), tree.getDepth(), rng, actionList);
			logger.info("Action List "+actionList);
			mutate(tree.getRootNode(),this.agentList.get(rng.nextInt(this.agentList.size())),actionList);
			logger.info("after Mutation");
			AgentDecisionTreeBuilder.showDepthFirst(tree.getRootNode());
			
		}
		return mutated;
	}
	private void mutate(DecisionTree.Node node, IndicatorAgent agent, List<Action> actionList) {
		DecisionTreeHelper.getNode(node,  actionList).setAgent(agent);
	}
}
