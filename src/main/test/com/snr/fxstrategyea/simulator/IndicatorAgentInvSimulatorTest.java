package com.snr.fxstrategyea.simulator;

import java.io.IOException;
import java.text.ParseException;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.snr.fxstrategyea.agent.IndicatorAgent;
import com.snr.fxstrategyea.agent.impl.MAHighLowCrossoverAgent;
import com.snr.fxstrategyea.ea.DecisionTreeCrossover;

public class IndicatorAgentInvSimulatorTest {

	private Logger logger = LoggerFactory.getLogger(DecisionTreeCrossover.class);
	private InvestmentSimulator<IndicatorAgent> simulator;
	@Before
	public void setup() throws IOException, ParseException{
		InvestmentConfig config = new InvestmentConfig();
		config.setDataAbsoluteFileName("/home/saji/R/fx/EURUSDpro1440.csv");
		config.setInitialInvestment(1000d);
		config.setComission(0d);
		config.setStopLoss(0.002d);
		config.setRisk(0.01);
		
		simulator = new IndicatorAgentInvSimulator(config);
		
	}
	
	@Test
	public void testHarami() {
			//simulator.simulate(new CandleStick());
			simulator.simulate(new MAHighLowCrossoverAgent(50,200));
			//simulator.simulate(new HigherHigh(3));
			//simulator.simulate(new LowerLow(3));
			logger.debug(""+simulator.getAgentMap());
	}

}
