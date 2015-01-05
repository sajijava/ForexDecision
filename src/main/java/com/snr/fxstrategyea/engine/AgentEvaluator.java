package com.snr.fxstrategyea.engine;

import java.util.List;
import java.util.Map;

import com.snr.fxstrategyea.agent.IndicatorAgent;
import com.snr.fxstrategyea.simulator.InvestmentSimulator;

public class AgentEvaluator {
	private List<IndicatorAgent> agentList;
	private InvestmentSimulator simulator;
	public AgentEvaluator(List<IndicatorAgent> agentList, InvestmentSimulator simulator){

		this.agentList = agentList;
		this.simulator = simulator;
	}
	public void evaluate(){

		simulator.simulate(agentList);
		Map<String, IndicatorAgent> agentMap = simulator.getAgentMap();
		
	}
	
	public void calculateInfoGain(Map<String, IndicatorAgent> agentMap){
		// Calculate entrropy and info gain.
		
	}

}
