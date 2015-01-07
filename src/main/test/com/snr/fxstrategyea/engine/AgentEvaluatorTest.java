package com.snr.fxstrategyea.engine;

import org.easymock.EasyMock;
import org.junit.Test;

import com.snr.fxstrategyea.agent.AgentMetric;
import com.snr.fxstrategyea.agent.IndicatorAgent;

public class AgentEvaluatorTest {

	@Test
	public void testEntropy() {
		AgentEvaluator evaluator = new AgentEvaluator(null, null);
		double e1 = evaluator.entropy(1);
		org.junit.Assert.assertEquals(e1,0d,0);

		e1 = evaluator.entropy(0.5,0.5);
		org.junit.Assert.assertEquals(e1,0.69,0.009);

		e1 = evaluator.entropy(0.5,0.4,0.3,0.2,0.1);
		org.junit.Assert.assertEquals(e1,1.62,0.009);	
	}

}
