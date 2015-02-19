package com.snr.fxstrategyea.agent.impl;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.snr.fxstrategyea.agent.Action;
import com.snr.fxstrategyea.model.OHLC;

public class MAHighLowCrossoverAgent extends IndicatorAgentImpl{
	private final int shortMAPeriod;
	private final int longMAPeriod;
	
	
	public MAHighLowCrossoverAgent(final int shortMAPeriod, final int longMAPeriod){
		this.shortMAPeriod = shortMAPeriod;
		this.longMAPeriod = longMAPeriod;
	}
	
	public Action getAction(List<OHLC> data) {

		Action returnAction = Action.HOLD;
		
		List<Double> highShortSMA = IndicatorUtil.SMA(getHigh(data), shortMAPeriod);
		List<Double> highLongSMA = IndicatorUtil.SMA(getHigh(data), longMAPeriod);		
		List<Double> lowShortSMA = IndicatorUtil.SMA(getLow(data), shortMAPeriod);
		List<Double> lowLongSMA = IndicatorUtil.SMA(getLow(data), longMAPeriod);		
		
		int hShort_T0 = highShortSMA.size() - 1;
		int hLong_T0 = highLongSMA.size() - 1;

		int lShort_T0 = lowShortSMA.size() - 1;
		int lLong_T0 = lowLongSMA.size() - 1;
		
		if(lowShortSMA.get(lShort_T0) > highLongSMA.get(hLong_T0) && highShortSMA.get(hShort_T0) > highLongSMA.get(hLong_T0) )
			returnAction = Action.BUY;
		else if(lowShortSMA.get(lShort_T0) < lowLongSMA.get(hLong_T0) && highShortSMA.get(hShort_T0) < lowLongSMA.get(hLong_T0))
			returnAction = Action.SELL;
		
		return returnAction;
	}
	
	public int indexOffset() {
		// TODO Auto-generated method stub
		return longMAPeriod + 2;
	}

	
	public List<Action> typeOfOutcome() {
		// TODO Auto-generated method stub
		return Arrays.asList(Action.BUY,Action.SELL);
	}

	public String getName() {
		// TODO Auto-generated method stub
		return "MAHighLowCrossoverAgent("+this.shortMAPeriod+","+this.longMAPeriod+")";
	}

	

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return ToStringBuilder.reflectionToString(this);
	}
	
}
