package com.snr.fxstrategyea.agent.impl;

import java.util.Arrays;
import java.util.List;

import com.snr.fxstrategyea.agent.Action;
import com.snr.fxstrategyea.model.OHLC;

public class Stochastic extends IndicatorAgentImpl {

	int period = 14;
	
	public Stochastic(int period) {
		this.period = period;
	}

	public Action getAction(List<OHLC> data) {
		Action returnAction = Action.HOLD;
		
		int index = data.size() - 1;
		double k = (data.get(index).getClose() - IndicatorUtil.getLowestLow(data))/(IndicatorUtil.getHighestHigh(data) - IndicatorUtil.getLowestLow(data)); 
		if(k <= 20){
			returnAction = Action.BUY;
		}else if(k >= 80){
			returnAction = Action.SELL;
		}
		return returnAction;
	}

	public int indexOffset() {
		// TODO Auto-generated method stub
		return this.period;
	}

	public List<Action> typeOfOutcome() {
		// TODO Auto-generated method stub
		return Arrays.asList(Action.BUY,Action.SELL,Action.HOLD);
	}

	public String getName() {
		// TODO Auto-generated method stub
		return "Stochastic("+this.period+")";
	}
	
}
