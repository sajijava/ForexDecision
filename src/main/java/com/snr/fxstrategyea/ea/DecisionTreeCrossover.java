package com.snr.fxstrategyea.ea;

import java.util.List;
import java.util.Random;

import org.uncommons.watchmaker.framework.operators.AbstractCrossover;

import com.snr.fxstrategyea.model.DecisionTree;

public class DecisionTreeCrossover extends AbstractCrossover<DecisionTree>{

	
	
	public DecisionTreeCrossover(int crossoverPoints) {
		super(crossoverPoints);
		// TODO Auto-generated constructor stub
	}

	

	@Override
	protected List<DecisionTree> mate(DecisionTree parent1,
			DecisionTree parent2, int numberOfCrossoverPoints, Random rng) {
		// TODO Auto-generated method stub
		return null;
	}

}
