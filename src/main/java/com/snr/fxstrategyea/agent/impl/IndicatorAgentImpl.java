package com.snr.fxstrategyea.agent.impl;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import com.snr.fxstrategyea.agent.AgentMetric;
import com.snr.fxstrategyea.agent.IndicatorAgent;
import com.snr.fxstrategyea.model.OHLC;

public abstract class IndicatorAgentImpl implements IndicatorAgent {

	private AgentMetric metric = new AgentMetric();
	private double infoGain;
	
	protected List<Double> getClose(List<OHLC> data){
		
		List<Double> close = new LinkedList<Double>();
		Collections.sort(data);
		
		for(OHLC ohlc : data){
			close.add(ohlc.getClose());
			
		}
		
		return close;
	}
	protected int getTnIndex(int offset,List<?> list){
		return (!list.isEmpty() && list.size() > offset )?list.size() - offset:list.size();
	}
	protected int T0(List<?> list){
		return getTnIndex(0, list);
	}
	protected int T1(List<?> list){
		return getTnIndex(1, list);
	}
	protected int T2(List<?> list){
		return getTnIndex(2, list);
	}
	public AgentMetric getMetric() {
		return metric;
	}

	
	public double getInfoGain() {
		return infoGain;
	}

	public void setInfoGain(double infoGain) {
		this.infoGain = infoGain;
		
	}

}
