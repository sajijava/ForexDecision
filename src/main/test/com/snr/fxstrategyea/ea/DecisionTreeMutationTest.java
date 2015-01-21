package com.snr.fxstrategyea.ea;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.junit.Test;

import com.snr.fxstrategyea.agent.IndicatorAgent;
import com.snr.fxstrategyea.agent.impl.BearishEngulfCandleStick;
import com.snr.fxstrategyea.agent.impl.BullishEngulfCandleStick;
import com.snr.fxstrategyea.agent.impl.BullishHaramiCandleStick;
import com.snr.fxstrategyea.agent.impl.BullishPiercingCandleStick;
import com.snr.fxstrategyea.agent.impl.DojiCandleStick;
import com.snr.fxstrategyea.agent.impl.MACrossOverAgent;
import com.snr.fxstrategyea.agent.impl.Momentum;
import com.snr.fxstrategyea.agent.impl.RSI;
import com.snr.fxstrategyea.agent.impl.Stochastic;
import com.snr.fxstrategyea.agent.impl.TwezzerCandleStick;
import com.snr.fxstrategyea.engine.AgentDecisionTreeBuilder;
import com.snr.fxstrategyea.model.DecisionTree;

public class DecisionTreeMutationTest {

	@Test
	public void testMutation() {
		
		List<IndicatorAgent> agentList = new ArrayList<IndicatorAgent>();
		agentList.add(new MACrossOverAgent(5, 20));
		agentList.add(new BullishEngulfCandleStick());
		agentList.add(new BearishEngulfCandleStick());
		agentList.add(new TwezzerCandleStick());
		agentList.add(new Stochastic());
		agentList.add(new RSI());
		agentList.add(new Momentum());
		agentList.add(new DojiCandleStick());
		agentList.add(new BullishHaramiCandleStick());
		agentList.add(new BullishPiercingCandleStick());


		AgentDecisionTreeBuilder treeBuilder = new AgentDecisionTreeBuilder(3);
		DecisionTree dt = treeBuilder.buildTree(agentList);
		List<DecisionTree> selectedCandidates = new ArrayList<DecisionTree>();
		selectedCandidates.add(treeBuilder.buildTree(agentList));
		
		DecisionTreeMutation mutation = new DecisionTreeMutation(agentList);
		mutation.apply(selectedCandidates, new Random());
		
		
	}

}
