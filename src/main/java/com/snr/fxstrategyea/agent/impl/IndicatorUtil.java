package com.snr.fxstrategyea.agent.impl;

import java.util.LinkedList;
import java.util.List;

import com.snr.fxstrategyea.model.OHLC;

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
	public static double getHighestHigh(List<OHLC> data){
		double highestHigh = 0;
		for(OHLC ohlc : data){
			if(ohlc.getHigh() > highestHigh){
				highestHigh = ohlc.getHigh();
			}
		}
		return highestHigh; 
	}
	public static double getLowestLow(List<OHLC> data){
		double lowestLow = 9999;
		for(OHLC ohlc : data){
			if(ohlc.getLow() < lowestLow){
				lowestLow = ohlc.getLow();
			}
		}
		return lowestLow; 
	}
}
