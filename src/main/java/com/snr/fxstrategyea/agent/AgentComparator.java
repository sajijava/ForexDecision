package com.snr.fxstrategyea.agent;

import java.util.Comparator;

public class AgentComparator implements Comparator<IndicatorAgent> {

	public int compare(IndicatorAgent arg0, IndicatorAgent arg1) {
		
		return (arg0.getInfoGain() > arg1.getInfoGain())?1:(arg0.getInfoGain() < arg1.getInfoGain())?-1:0;
	}

	
}
