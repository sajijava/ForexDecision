package com.snr.fxstrategyea.agent;

import java.util.List;

import com.snr.fxstrategyea.model.OHLC;

public interface IndicatorAgent {

	public Action getAction(List<OHLC> data);
	public int indexOffset();
	public List<Action> typeOfOutcome();
	public String getName();

}
