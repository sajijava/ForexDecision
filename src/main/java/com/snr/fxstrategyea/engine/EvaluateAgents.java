package com.snr.fxstrategyea.engine;

import com.snr.fxstrategyea.simulator.InvestmentConfig;
import com.snr.fxstrategyea.simulator.InvestmentSimulator;

public class EvaluateAgents {

	private InvestmentSimulator simulator;
	public EvaluateAgents(InvestmentConfig config){
		simulator = new InvestmentSimulator(config);
	}
	

}


