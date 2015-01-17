package com.snr.fxstrategyea.ea;

import java.util.List;

import org.uncommons.watchmaker.framework.FitnessEvaluator;

import com.snr.fxstrategyea.model.DecisionTree;
import com.snr.fxstrategyea.simulator.InvestmentSimulator;

public class DecisionTreeFitnessEvaluator implements FitnessEvaluator<DecisionTree> {

	private final InvestmentSimulator investmentSimulator;
	public DecisionTreeFitnessEvaluator(InvestmentSimulator investmentSimulator){
		this.investmentSimulator = investmentSimulator;
	}
	
	public double getFitness(DecisionTree candidate, List<? extends DecisionTree> population) {
		this.investmentSimulator.simulate(candidate);
		
		return 0;
	}

	public boolean isNatural() {
		// TODO Auto-generated method stub
		return false;
	}

	
}
