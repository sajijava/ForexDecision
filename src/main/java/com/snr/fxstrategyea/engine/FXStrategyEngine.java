package com.snr.fxstrategyea.engine;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.uncommons.watchmaker.framework.CandidateFactory;
import org.uncommons.watchmaker.framework.EvolutionaryOperator;
import org.uncommons.watchmaker.framework.SelectionStrategy;
import org.uncommons.watchmaker.framework.selection.RouletteWheelSelection;

import com.snr.fxstrategyea.agent.IndicatorAgent;
import com.snr.fxstrategyea.agent.impl.MACrossOverAgent;
import com.snr.fxstrategyea.ea.DecisionTreeCandidateFactory;
import com.snr.fxstrategyea.ea.DecisionTreeCrossover;
import com.snr.fxstrategyea.ea.DecisionTreeFitnessEvaluator;
import com.snr.fxstrategyea.ea.DecisionTreeMutation;
import com.snr.fxstrategyea.ea.Strategizer;
import com.snr.fxstrategyea.model.DecisionTree;
import com.snr.fxstrategyea.simulator.InvestmentConfig;
import com.snr.fxstrategyea.simulator.InvestmentSimulator;

public class FXStrategyEngine {


	final AgentEvaluator evaluator;
	private Strategizer strategizer;
	public FXStrategyEngine(InvestmentConfig config) throws IOException, ParseException{
		
		List<IndicatorAgent> agentList = new ArrayList<IndicatorAgent>();
		agentList.add(new MACrossOverAgent(5, 20));
		
		InvestmentSimulator simulator = new InvestmentSimulator(config);
		this.evaluator = new AgentEvaluator(agentList,simulator);
		
		CandidateFactory<DecisionTree> candidateFactory = new DecisionTreeCandidateFactory(2,agentList);
		SelectionStrategy<Object> selection = new RouletteWheelSelection();
		
		List<EvolutionaryOperator<DecisionTree>> operators   =  new LinkedList<EvolutionaryOperator<DecisionTree>>();
		operators.add(new DecisionTreeMutation(agentList));
		operators.add(new DecisionTreeCrossover(2));
		
		DecisionTreeFitnessEvaluator evaluator = new DecisionTreeFitnessEvaluator(new InvestmentSimulator(config));
		this.strategizer = new Strategizer(candidateFactory,operators,evaluator,selection);
		
		
	}
	public void run(){
		this.evaluator.evaluate();
		this.strategizer.runEngine();
		
	}
	
	public static void main(String[] args) {
		try {
			InvestmentConfig config = new InvestmentConfig();
			config.setDataAbsoluteFileName("/home/saji/R/fx/EURUSDpro1440.csv");
			config.setInitialInvestment(1000d);
			config.setComission(0d);
			config.setStopLoss(20d);
			config.setRisk(0.01);
			
			FXStrategyEngine engine = new FXStrategyEngine(config);
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
