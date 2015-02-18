package com.snr.fxstrategyea.agent.impl;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.snr.fxstrategyea.agent.Action;
import com.snr.fxstrategyea.model.OHLC;

public class RSI extends IndicatorAgentImpl {

	private final String GAIN = "GAIN";
	private final String LOSS = "LOSS";
	int period = 14;
	public RSI(int period){
		this.period = period;
	}
	public Action getAction(List<OHLC> data) {
		Action returnAction = Action.HOLD;
		if(data.size() >= this.period){
			
			Map<String,DoubleAggregator> gainLoss = getGainLoss(data);
			double rs = gainLoss.get(GAIN).getValue()/gainLoss.get(LOSS).getValue();
			double rsi = 100  - (100/ (1 + rs));
			
			if(rsi < 30)
				returnAction = Action.BUY;
			else if(rsi > 70)
				returnAction = Action.SELL;
				
		}
		return returnAction;
	}
	
	public Map<String,DoubleAggregator> getGainLoss(List<OHLC> data){
		Map<String,DoubleAggregator> returnMap = new HashMap<String,DoubleAggregator>();
		returnMap.put(GAIN, new DoubleAggregator());
		returnMap.put(LOSS, new DoubleAggregator());
		
		for(int i = data.size() - 2 ; i >0; i--){
			double gain = data.get(i).getClose() - data.get(i -1).getClose();
			if(gain > 0) returnMap.get(GAIN).add(gain);
			else if(gain < 0)returnMap.get(LOSS).add(Math.abs(gain));
		}
		
		return returnMap;
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
		return "RSI";
	}

	class DoubleAggregator{
		private double counter = 0d;
		
		public void add(double val)
		{
			this.counter += val;
		}
		public double getValue(){
			return this.counter;
		}
	}
}
