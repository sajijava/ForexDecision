package com.snr.fxstrategyea.simulator;

import java.io.IOException;
import java.text.ParseException;
import java.util.Collections;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.snr.fxstrategyea.agent.IndicatorAgent;
import com.snr.fxstrategyea.model.DecisionTree;

public class IndicatorAgentInvSimulator extends InvestmentSimulator<IndicatorAgent> {

	private Logger logger = LoggerFactory.getLogger(IndicatorAgentInvSimulator.class);
	public IndicatorAgentInvSimulator(InvestmentConfig config)
			throws IOException, ParseException {
		super(config);
		// TODO Auto-generated constructor stub
	}
	@Override
	public InvestmentSimulator<IndicatorAgent> getInstance() throws IOException, ParseException{
		return new IndicatorAgentInvSimulator(this.invConfig);
	}
	@Override
	public   void simulate(IndicatorAgent agent){
		if(data == null) throw new IllegalArgumentException("No Data");
		if(agent == null ) throw new IllegalArgumentException("No Agents to simulate");
		Collections.sort(data);
		for(int i = agent.indexOffset() + 1; i < data.size(); i++){
			switch(agent.getAction(data.subList(i - agent.indexOffset() - 1, i))){
			case BUY:{
				if(!checkIfTradeExist())
					createOrder(data.get(i),TradeDirection.LONG);
			}
			case SELL:{
				if(!checkIfTradeExist())
					createOrder(data.get(i),TradeDirection.SHORT);
			}
			case HOLD:
				break;
			}
			tradeManagement(data.get(i));
		}
		closeOrder(data.get(data.size() -1),TradeDirection.LONG);
		closeOrder(data.get(data.size() -1),TradeDirection.SHORT);
		/*for(Transaction tran : simResult){
			logger.debug(""+tran);
		}*/
		calculateMetrics();
	}

}
