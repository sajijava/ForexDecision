package com.snr.fxstrategyea.engine;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.uncommons.watchmaker.framework.CandidateFactory;
import org.uncommons.watchmaker.framework.EvolutionaryOperator;
import org.uncommons.watchmaker.framework.SelectionStrategy;
import org.uncommons.watchmaker.framework.selection.RouletteWheelSelection;

import com.snr.fxstrategyea.agent.IndicatorAgent;
import com.snr.fxstrategyea.agent.impl.HigherHigh;
import com.snr.fxstrategyea.agent.impl.HigherLow;
import com.snr.fxstrategyea.agent.impl.LowerHigh;
import com.snr.fxstrategyea.agent.impl.LowerLow;
import com.snr.fxstrategyea.agent.impl.MAHighLowCrossoverAgent;
import com.snr.fxstrategyea.ea.DecisionTreeCandidateFactory;
import com.snr.fxstrategyea.ea.DecisionTreeCrossover;
import com.snr.fxstrategyea.ea.DecisionTreeFitnessEvaluator;
import com.snr.fxstrategyea.ea.DecisionTreeMutation;
import com.snr.fxstrategyea.ea.Strategizer;
import com.snr.fxstrategyea.model.DecisionTree;
import com.snr.fxstrategyea.simulator.DecisionTreeInvSimulator;
import com.snr.fxstrategyea.simulator.IndicatorAgentInvSimulator;
import com.snr.fxstrategyea.simulator.InvestmentConfig;
import com.snr.fxstrategyea.simulator.InvestmentSimulator;

public class FXStrategyEngine {

	private Logger logger = LoggerFactory.getLogger(DecisionTreeMutation.class);

	final AgentEvaluator evaluator;
	private Strategizer strategizer;
	public FXStrategyEngine(InvestmentConfig config) throws IOException, ParseException{
		
		List<IndicatorAgent> agentList = new ArrayList<IndicatorAgent>();
		DecisionTreeInvSimulator decisionTreeInvSimulator =new DecisionTreeInvSimulator(config);
		
		buildIndicatorAgent(agentList);
		InvestmentSimulator<IndicatorAgent> simulator = new IndicatorAgentInvSimulator(config);
		this.evaluator = new AgentEvaluator(agentList,simulator);
		
		CandidateFactory<DecisionTree> candidateFactory = new DecisionTreeCandidateFactory(2,agentList,decisionTreeInvSimulator);
		SelectionStrategy<Object> selection = new RouletteWheelSelection();
		
		List<EvolutionaryOperator<DecisionTree>> operators   =  new LinkedList<EvolutionaryOperator<DecisionTree>>();
		operators.add(new DecisionTreeMutation(agentList));
		operators.add(new DecisionTreeCrossover(2));
		
		DecisionTreeFitnessEvaluator fitnessEvaluator = new DecisionTreeFitnessEvaluator(decisionTreeInvSimulator);
		this.strategizer = new Strategizer(candidateFactory,operators,fitnessEvaluator,selection);
		
		
		
		
	}
	private void buildIndicatorAgent(List<IndicatorAgent> agentList){
		int[] maSeq = new int[]{2,3,5,8,13,21,34,55,89,144};
		
		for(int i = 1; i < maSeq.length; i++){
			agentList.add(new MAHighLowCrossoverAgent(maSeq[i-1], maSeq[i]));
		}
		for(int i = 2; i < 5 ; i++){
			agentList.add(new HigherHigh(i));
			agentList.add(new LowerLow(i));
			agentList.add(new HigherLow(i));
			agentList.add(new LowerHigh(i));
		}
		//agentList.add(new CandleStick());
	}
	public void run(){
		logger.info("Starting to valuate");
		this.evaluator.evaluate();
		this.strategizer.runEngine();
		
		logger.info("Done evaluating");
		
	}
	
	public static void main(String[] args) {
		try {
			InvestmentConfig config = new InvestmentConfig();
			config.setDataAbsoluteFileName("/home/saji/R/fx/GBPUSDpro1440.csv");
			config.setInitialInvestment(1000d);
			config.setComission(0d);
			config.setStopLoss(0.002d);
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
