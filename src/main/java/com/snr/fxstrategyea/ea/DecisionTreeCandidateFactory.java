package com.snr.fxstrategyea.ea;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.uncommons.watchmaker.framework.factories.AbstractCandidateFactory;

import com.snr.fxstrategyea.agent.IndicatorAgent;
import com.snr.fxstrategyea.engine.AgentDecisionTreeBuilder;
import com.snr.fxstrategyea.model.DecisionTree;
import com.snr.fxstrategyea.simulator.InvestmentSimulator;

public class DecisionTreeCandidateFactory extends AbstractCandidateFactory<DecisionTree>{
	private Logger logger = LoggerFactory.getLogger(DecisionTreeCandidateFactory.class);
	private int treeDepth = 1;
	private List<IndicatorAgent> agentList = null;
	private final AgentDecisionTreeBuilder treeBuilder;
	private final InvestmentSimulator<DecisionTree> investmentSimulator;

	public DecisionTreeCandidateFactory(int treeDepth, List<IndicatorAgent> agentList,InvestmentSimulator<DecisionTree> investmentSimulator){
		this.treeDepth = treeDepth;
		this.agentList = agentList;
		this.investmentSimulator = investmentSimulator;
		treeBuilder = new AgentDecisionTreeBuilder(this.treeDepth);

	}
	public DecisionTree generateRandomCandidate(Random rng) {
		if(this.agentList == null) throw new IllegalArgumentException("List of agents is null");
		DecisionTree dt = null;
		do{
			dt = this.treeBuilder.buildTree(this.agentList);
			try {
				this.investmentSimulator.getInstance().simulate(dt);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}while(dt.getMetrics().getTotalGain() <= 0);
		
		AgentDecisionTreeBuilder.showDepthFirst(dt.getRootNode());
		logger.info("Metic "+dt.getMetrics().getTotalGain());
		return dt;
	}


}
