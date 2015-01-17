package com.snr.fxstrategyea.agent.impl;

import java.util.Arrays;
import java.util.List;

import com.snr.fxstrategyea.agent.Action;
import com.snr.fxstrategyea.model.OHLC;

public class BullishEngulfCandleStick extends IndicatorAgentImpl {

	public Action getAction(List<OHLC> data) {
		// TODO Auto-generated method stub
		return null;
	}

	public int indexOffset() {
		// TODO Auto-generated method stub
		return 0;
	}

	public List<Action> typeOfOutcome() {
		// TODO Auto-generated method stub
		return Arrays.asList(Action.BUY,Action.SELL,Action.HOLD);
	}

	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

}
