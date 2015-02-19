package com.snr.fxstrategyea.agent.impl;

import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.snr.fxstrategyea.agent.Action;
import com.snr.fxstrategyea.model.OHLC;

public class LowerHigh extends IndicatorAgentImpl {

	private Logger logger = LoggerFactory.getLogger(LowerHigh.class);
	private int lookback = 1;
	
	public LowerHigh(int lookback){
		this.lookback = lookback;
	}
	public Action getAction(List<OHLC> data) {
		Action returnAction = Action.HOLD;
		if(data.size() > this.lookback){
			for(int i = data.size() - 1; i > data.size() - this.lookback - 1 ; i--){
				//logger.debug(data.get(i).getLow()+" "+data.get(i - 1).getLow());
				if(data.get(i).getHigh() < data.get(i - 1).getHigh())
				{
					returnAction = Action.SELL;
				}else{
					returnAction = Action.HOLD;
					break;
				}
			}
			//logger.debug(""+returnAction);
		}
		return returnAction;
	}

	public int indexOffset() {
		// TODO Auto-generated method stub
		return this.lookback + 1;
	}

	public List<Action> typeOfOutcome() {
		// TODO Auto-generated method stub
		return Arrays.asList(Action.SELL);
	}

	public String getName() {
		// TODO Auto-generated method stub
		return "LowerHigh("+this.lookback+")";
	}

	
}
