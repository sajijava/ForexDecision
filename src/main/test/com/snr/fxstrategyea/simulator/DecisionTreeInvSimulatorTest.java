package com.snr.fxstrategyea.simulator;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.snr.fxstrategyea.agent.IndicatorAgent;
import com.snr.fxstrategyea.agent.impl.CandleStick;
import com.snr.fxstrategyea.agent.impl.MACrossOverAgent;
import com.snr.fxstrategyea.agent.impl.RSI;
import com.snr.fxstrategyea.agent.impl.Stochastic;
import com.snr.fxstrategyea.ea.DecisionTreeCrossover;
import com.snr.fxstrategyea.engine.AgentDecisionTreeBuilder;
import com.snr.fxstrategyea.model.DecisionTree;

public class DecisionTreeInvSimulatorTest {
	private Logger logger = LoggerFactory.getLogger(DecisionTreeCrossover.class);
	private InvestmentSimulator<DecisionTree> simulator;
	@Before
	public void setup() throws IOException, ParseException{
		InvestmentConfig config = new InvestmentConfig();
		config.setDataAbsoluteFileName("/home/saji/R/fx/EURUSDpro1440.csv");
		config.setInitialInvestment(1000d);
		config.setComission(0d);
		config.setStopLoss(0.0050d);
		config.setRisk(0.01);
		
		simulator = new DecisionTreeInvSimulator(config);
		
	}
	

	@Test
	public void testDecisionTreeSimulator() {
		int[] maSeq = new int[]{2,3,5,8,13,21,34,55,89,144};
		
		List<IndicatorAgent> agentList = new ArrayList<IndicatorAgent>();
		for(int i = 1; i < maSeq.length; i++){
			agentList.add(new MACrossOverAgent(maSeq[i-1], maSeq[i]));
			agentList.add(new RSI(maSeq[i]));
			agentList.add(new Stochastic(maSeq[i]));
		}
		
		
		agentList.add(new CandleStick());
		
		
		AgentDecisionTreeBuilder treeBuilder = new AgentDecisionTreeBuilder(2);
		DecisionTree dt = treeBuilder.buildTree(agentList);
		
		AgentDecisionTreeBuilder.showDepthFirst(dt.getRootNode());
		simulator.simulate(dt);
		
		logger.debug(simulator.getAgentMap().toString());
		logger.debug(""+simulator.getAgentMap().getTotalGain());
		
	}

}
