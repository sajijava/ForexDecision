package com.snr.fxstrategyea.engine;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.snr.fxstrategyea.agent.AgentMetric;
import com.snr.fxstrategyea.agent.IndicatorAgent;
import com.snr.fxstrategyea.simulator.InvestmentSimulator;

public class AgentEvaluator {
	private Logger logger = LoggerFactory.getLogger(AgentEvaluator.class);
	private List<IndicatorAgent> agentList;
	private InvestmentSimulator<IndicatorAgent> simulator;
	public AgentEvaluator(List<IndicatorAgent> agentList, InvestmentSimulator<IndicatorAgent> simulator){

		this.agentList = agentList;
		this.simulator = simulator;
	}
	public void evaluate(){

		for(IndicatorAgent agent : this.agentList){
			simulator.simulate(agent);
			agent.setInfoGain(infoGain(simulator.getAgentMap()));
		}
		
		//logger.debug(""+agentList);
	}
	
	public double infoGain(AgentMetric agent){
		double expectedEntropy = 1 - 
					((((double)agent.getLongProfitableCount()/(double)agent.getTotalTrade()) * entropy((double)agent.getLongProfitableCount()/(double)agent.getTotalTrade(),(double)(agent.getLongCount() - agent.getLongProfitableCount())/(double)agent.getTotalTrade()))
					+ (((double)agent.getShortProfitableCount()/(double)agent.getTotalTrade()) * entropy((double)agent.getShortProfitableCount()/(double)agent.getTotalTrade(),(double)(agent.getShortCount() - agent.getShortProfitableCount())/(double)agent.getTotalTrade())));
		
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