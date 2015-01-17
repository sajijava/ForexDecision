package com.snr.fxstrategyea.engine;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.snr.fxstrategyea.agent.IndicatorAgent;
import com.snr.fxstrategyea.simulator.InvestmentSimulator;

public class AgentEvaluator {
	private Logger logger = LoggerFactory.getLogger(AgentEvaluator.class);
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
		logger.debug(""+agentMap);
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
					((((double)agent.getMetric().getLongProfitableCount()/(double)agent.getMetric().getTotalTrade()) * entropy((double)agent.getMetric().getLongProfitableCount()/(double)agent.getMetric().getTotalTrade(),(double)(agent.getMetric().getLongCount() - agent.getMetric().getLongProfitableCount())/(double)agent.getMetric().getTotalTrade()))
					+ (((double)agent.getMetric().getShortProfitableCount()/(double)agent.getMetric().getTotalTrade()) * entropy((double)agent.getMetric().getShortProfitableCount()/(double)agent.getMetric().getTotalTrade(),(double)(agent.getMetric().getShortCount() - agent.getMetric().getShortProfitableCount())/(double)agent.getMetric().getTotalTrade())));
		
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