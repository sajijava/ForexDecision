package com.snr.fxstrategyea.agent.impl;

import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.snr.fxstrategyea.agent.Action;
import com.snr.fxstrategyea.ea.DecisionTreeCrossover;
import com.snr.fxstrategyea.model.OHLC;

public class CandleStick extends IndicatorAgentImpl {

	private Logger logger = LoggerFactory.getLogger(CandleStick.class);
	public Action getAction(List<OHLC> data) {
		int index = data.size() - 1;
		Action engulfAction = engulf(data, index);
		Action haramiAction = harami(data, index);
		Action hammerAction = hammer(data, index);
		return  hammerAction == Action.HOLD
					?(engulfAction == Action.HOLD 
						?haramiAction
						:engulfAction)
					:hammerAction;
	}

	public int indexOffset() {
		// TODO Auto-generated method stub
		return 10;
	}

	public List<Action> typeOfOutcome() {
		// TODO Auto-generated method stub
		return Arrays.asList(Action.BUY,Action.SELL,Action.HOLD);

	}

	public String getName() {
		// TODO" Auto-generated method stub
		return "CandleStick";
	}

	/*
	 * Haram
	 * - trend check with SMA(5) - fails to get good returns
	 */
	private Action harami(List<OHLC> data, int index){
		Action returnAction = Action.HOLD;
		
		
		OHLC data3 = data.get(index - 3);
		OHLC data2 = data.get(index - 2);
		double t3High = data3.getHigh();
		double t3Low = data3.getLow();
		double t2High = data2.getHigh();
		double t2Low = data2.getLow();
		double t1Close = data.get(index - 1).getClose();
		double t2Up = 0;
		double t2Down = 0;
		
		
		List<Double> sma = IndicatorUtil.SMA(getClose(data), 5);	
		
		//logger.debug(index+""+data.get(index).getDate());
		//logger.debug(""+sma);
		if(t3High > t2High && t3Low < t2Low &&  t1Close > t3High && getBodyUp(data3) > getBodyUp(data2) && getBodyBottom(data3) < getBodyBottom(data2))
			returnAction = Action.BUY;
		else if (t3High > t2High && t3Low < t2Low &&  t1Close < t3Low && getBodyUp(data3) > getBodyUp(data2) && getBodyBottom(data3) < getBodyBottom(data2))
			returnAction = Action.SELL; 
		
		return returnAction;
	}
	
	private Action engulf(List<OHLC> data,int index){
		OHLC data0 = data.get(index);
		OHLC data1 = data.get(index - 1);
		OHLC data2 = data.get(index - 2);
		Action returnAction = Action.HOLD;
		
		
		if(isBullish(data1) && isBearish(data2) && getBodyUp(data1) > getBodyUp(data2) && getBodyBottom(data1) < getBodyBottom(data2) && data0.getClose() > data1.getHigh()){
			returnAction = Action.BUY;
		}else if(isBearish(data1) && isBullish(data2) && getBodyUp(data1) > getBodyUp(data2) && getBodyBottom(data1) < getBodyBottom(data2) && data0.getClose() < data1.getLow()){
			returnAction = Action.SELL;
		}
		return returnAction;
	}
	private Action hammer(List<OHLC> data,int index){
		OHLC data0 = data.get(index);
		OHLC data1 = data.get(index - 1);
		double body = data0.getClose() - data0.getOpen();
		double range = data0.getHigh() - data0.getLow(); 
		
		Action returnAction = Action.HOLD;
		boolean candleStruct = Math.abs(body) <= range * 0.3;
		
		if(candleStruct && data0.getClose() > data1.getHigh()){
			returnAction = Action.BUY;
		}else if(candleStruct && data0.getClose() < data1.getLow()){
			returnAction = Action.SELL;
		}
		return returnAction;
	}
	

	private boolean isBullish(OHLC data){
		return data.getClose() > data.getOpen();
	}
	private boolean isBearish(OHLC data){
		return data.getClose() < data.getOpen();
	}
	private double getBodyUp(OHLC data){
		double up = 0;
		if(data.getClose() > data.getOpen()){
			up = data.getClose();
		}else{
			up = data.getOpen();
		}
		return up;
		
	}
	private double getBodyBottom(OHLC data){
		double bottom = 0;
		if(data.getClose() < data.getOpen()){
			bottom = data.getClose();
		}else{
			bottom = data.getOpen();
		}
		return bottom;
	}
	/* candle sticks to implement
	 *  
	 *  - Bullish/bearish Piercing
	 *  - Doji
	 *  - Hammer
	 *  - Hanging man
	 *  - tweezer
	 */
}
