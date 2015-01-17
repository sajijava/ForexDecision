package com.snr.fxstrategyea.agent.impl;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.snr.fxstrategyea.agent.Action;
import com.snr.fxstrategyea.model.OHLC;

public class MACrossOverAgent extends IndicatorAgentImpl{
	private final int shortMAPeriod;
	private final int longMAPeriod;
	
	
	public MACrossOverAgent(final int shortMAPeriod, final int longMAPeriod){
		this.shortMAPeriod = shortMAPeriod;
		this.longMAPeriod = longMAPeriod;
	}
	
	public Action getAction(List<OHLC> data) {

		Action returnAction = Action.HOLD;
		
		List<Double> shortSMA = IndicatorUtil.SMA(getClose(data), shortMAPeriod);
		List<Double> longSMA = IndicatorUtil.SMA(getClose(data), longMAPeriod);		
		
		int short_T0 = shortSMA.size() - 1;
		int long_T0 = longSMA.size() - 1;
		
		if(longSMA.size() > 2 && shortSMA.get(short_T0 - 2) <= longSMA.get(long_T0 - 2) && shortSMA.get(short_T0) > longSMA.get(long_T0) )
			returnAction = Action.BUY;
		else if(longSMA.size() > 2  && shortSMA.get(short_T0 - 2) >= longSMA.get(long_T0 - 2) && shortSMA.get(short_T0) < longSMA.get(long_T0) )
			returnAction = Action.SELL;
		
		return returnAction;
	}
	
	public int indexOffset() {
		// TODO Auto-generated method stub
		return longMAPeriod + 2;
	}

	
	public List<Action> typeOfOutcome() {
		// TODO Auto-generated method stub
		return Arrays.asList(Action.BUY,Action.SELL,Action.HOLD);
	}

	public String getName() {
		// TODO Auto-generated method stub
		return "MACrossOverAgent("+this.shortMAPeriod+","+this.longMAPeriod+")";
	}

	

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return ToStringBuilder.reflectionToString(this);
	}
	
}
