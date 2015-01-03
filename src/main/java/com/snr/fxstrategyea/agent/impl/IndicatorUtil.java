package com.snr.fxstrategyea.agent.impl;

import java.util.LinkedList;
import java.util.List;

public class IndicatorUtil {

	public static List<Double> SMA(List<Double> data, int period){
		List<Double> sma = new LinkedList<Double>();
		for(int i = period + 1; i <= data.size(); i++)
		{
			List<Double> subList = data.subList(i - period -1, i-1);
			double smas = SMAScalar(subList);
			//System.out.println(i + " "+smas);
			sma.add(smas);
		}
		return sma;
		
	}
	public static Double SMAScalar(List<Double> data){
		double smaScalar = 0;
		for(Double d : data){
			smaScalar += d;
		}
		
		return smaScalar/data.size();
		
	}
}
