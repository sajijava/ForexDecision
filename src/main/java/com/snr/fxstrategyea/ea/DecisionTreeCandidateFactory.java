package com.snr.fxstrategyea.ea;

import java.util.List;
import java.util.Random;

import org.uncommons.watchmaker.framework.factories.AbstractCandidateFactory;

import com.snr.fxstrategyea.agent.IndicatorAgent;
import com.snr.fxstrategyea.engine.AgentDecisionTreeBuilder;
import com.snr.fxstrategyea.model.DecisionTree;

public class DecisionTreeCandidateFactory extends AbstractCandidateFactory<DecisionTree>{

	private int treeDepth = 1;
	private List<IndicatorAgent> agentList = null;
	private final AgentDecisionTreeBuilder treeBuilder;
	
	public DecisionTreeCandidateFactory(int treeDepth, List<IndicatorAgent> agentList){
		this.treeDepth = treeDepth;
		this.agentList = agentList;
		treeBuilder = new AgentDecisionTreeBuilder(this.treeDepth);
		
	}
	public DecisionTree generateRandomCandidate(Random rng) {
		if(this.agentList == null) throw new IllegalArgumentException("List of agents is null");
		return this.treeBuilder.buildTree(this.agentList);
	}

	
}
