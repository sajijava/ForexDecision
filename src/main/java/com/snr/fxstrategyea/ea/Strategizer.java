package com.snr.fxstrategyea.ea;

import java.util.List;
import java.util.Random;

import org.uncommons.maths.random.MersenneTwisterRNG;
import org.uncommons.watchmaker.framework.CandidateFactory;
import org.uncommons.watchmaker.framework.EvolutionEngine;
import org.uncommons.watchmaker.framework.EvolutionObserver;
import org.uncommons.watchmaker.framework.EvolutionaryOperator;
import org.uncommons.watchmaker.framework.FitnessEvaluator;
import org.uncommons.watchmaker.framework.GenerationalEvolutionEngine;
import org.uncommons.watchmaker.framework.PopulationData;
import org.uncommons.watchmaker.framework.SelectionStrategy;
import org.uncommons.watchmaker.framework.operators.EvolutionPipeline;
import org.uncommons.watchmaker.framework.termination.TargetFitness;

import com.snr.fxstrategyea.agent.IndicatorAgent;
import com.snr.fxstrategyea.model.DecisionTree;

public class Strategizer {

	CandidateFactory<DecisionTree> factory;
	List<EvolutionaryOperator<DecisionTree>> operators;
	FitnessEvaluator<DecisionTree> fitnessEvaluator;
	SelectionStrategy<Object> selection;
	
	
	public Strategizer(CandidateFactory<DecisionTree> factory,
			List<EvolutionaryOperator<DecisionTree>> operators,
			FitnessEvaluator<DecisionTree> fitnessEvaluator,
			SelectionStrategy<Object> selection) {
		super();
		this.factory = factory;
		this.operators = operators;
		this.fitnessEvaluator = fitnessEvaluator;
		this.selection = selection;
	}

		
	public void runEngine(){
		
		EvolutionaryOperator<DecisionTree> pipeline = new EvolutionPipeline<DecisionTree>(this.operators);
		Random rng = new MersenneTwisterRNG();

		EvolutionEngine<DecisionTree> engine= new GenerationalEvolutionEngine<DecisionTree>(this.factory,
		                                              pipeline,
		                                              this.fitnessEvaluator,
		                                              this.selection,
		                                              rng);

		engine.addEvolutionObserver(new EvolutionObserver<DecisionTree>()
				{
				    public void populationUpdate(PopulationData<? extends DecisionTree> data)
				    {
				        System.out.printf("Generation %d: %s\n",
				                          data.getGenerationNumber(),
				                          data.getBestCandidate());
				    }
				});

		engine.evolve(100, 5, new TargetFitness(0, false));
	}
}
