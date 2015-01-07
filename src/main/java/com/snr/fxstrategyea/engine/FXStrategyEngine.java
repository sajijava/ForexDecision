package com.snr.fxstrategyea.engine;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import com.snr.fxstrategyea.agent.IndicatorAgent;
import com.snr.fxstrategyea.simulator.InvestmentConfig;
import com.snr.fxstrategyea.simulator.InvestmentSimulator;

public class FXStrategyEngine {

	final AgentEvaluator evaluator;
	public FXStrategyEngine() throws IOException, ParseException{
	
		InvestmentConfig config = new InvestmentConfig();
		List<IndicatorAgent> agentList = new ArrayList<IndicatorAgent>();
		InvestmentSimulator simulator = new InvestmentSimulator(config);
		this.evaluator = new AgentEvaluator(agentList,simulator);
		
		
	}
	public void run(){
		this.evaluator.evaluate();
	}
	
	public static void main(String[] args) {
		try {
			FXStrategyEngine engine = new FXStrategyEngine();
			engine.run();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
