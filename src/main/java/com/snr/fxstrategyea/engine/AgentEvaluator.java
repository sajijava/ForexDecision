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
		calculateInfoGain(agentMap);
	}
	
	public void calculateInfoGain(Map<String, IndicatorAgent> agentMap){
		// Calculate entrropy and info gain.
		for(Map.Entry<String, IndicatorAgent> entry : agentMap.entrySet()){
			double infoGain = infoGain(entry.getValue());
			entry.getValue().setInfoGain(infoGain);
		}
		
	}
	public double infoGain(IndicatorAgent agent){
		double expectedEntropy = 1 - 
					(((agent.getMetric().getLongProfitableCount()/agent.getMetric().getTotalTrade()) * entropy(agent.getMetric().getLongProfitableCount()/agent.getMetric().getTotalTrade(),(agent.getMetric().getLongCount() - agent.getMetric().getLongProfitableCount())/agent.getMetric().getTotalTrade()))
					+ ((agent.getMetric().getShortProfitableCount()/agent.getMetric().getTotalTrade()) * entropy(agent.getMetric().getShortProfitableCount()/agent.getMetric().getTotalTrade(),(agent.getMetric().getShortCount() - agent.getMetric().getShortProfitableCount())/agent.getMetric().getTotalTrade())));
		
		return expectedEntropy;
	}
	
	public double entropy(double...args){
		double h = 0d;
		// H(p/p+n,n/p+n) = p/p+n log2 (p/p+n) - n/p+n log2 (n/p+n)
		for(double arg:args){
			h -= arg * Math.log(arg);
		}
		return h;
	}


}