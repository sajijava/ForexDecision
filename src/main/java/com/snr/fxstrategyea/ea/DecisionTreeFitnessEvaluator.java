package com.snr.fxstrategyea.ea;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.uncommons.watchmaker.framework.FitnessEvaluator;

import com.snr.fxstrategyea.engine.AgentDecisionTreeBuilder;
import com.snr.fxstrategyea.model.DecisionTree;
import com.snr.fxstrategyea.simulator.InvestmentSimulator;

public class DecisionTreeFitnessEvaluator implements FitnessEvaluator<DecisionTree> {
	private Logger logger = LoggerFactory.getLogger(DecisionTreeFitnessEvaluator.class);
	private final InvestmentSimulator<DecisionTree> investmentSimulator;
	public DecisionTreeFitnessEvaluator(InvestmentSimulator<DecisionTree> investmentSimulator){
		this.investmentSimulator = investmentSimulator;
	}

	public double getFitness(DecisionTree candidate, List<? extends DecisionTree> population) {
		//AgentDecisionTreeBuilder.showDepthFirst(candidate.getRootNode());
			try {
				this.investmentSimulator.getInstance().simulate(candidate);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		//	logger.info("Calculate fitness for candidates {} {}",candidate.getRootNode().getAgent().getName(),candidate.getMetrics());
		return (candidate.getMetrics().getTotalGain() < 0)?0:candidate.getMetrics().getTotalGain();
	}

	public boolean isNatural() {
		// TODO Auto-generated method stub
		return true;
	}


}
