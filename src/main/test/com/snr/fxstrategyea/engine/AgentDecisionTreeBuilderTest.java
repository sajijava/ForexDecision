package com.snr.fxstrategyea.engine;

import java.util.ArrayList;
import java.util.List;

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
import com.snr.fxstrategyea.model.DecisionTree;

public class AgentDecisionTreeBuilderTest {

	@Test
	public void testBuildTree() {
		List<IndicatorAgent> agentList = new ArrayList<IndicatorAgent>();
		agentList.add(new MACrossOverAgent(5, 20));

		AgentDecisionTreeBuilder treeBuilder = new AgentDecisionTreeBuilder(5);
		DecisionTree dt = treeBuilder.buildTree(agentList);
		AgentDecisionTreeBuilder.showDepthFirst(dt.getRootNode());
	}

}
