package com.snr.fxstrategyea.agent;

import org.apache.commons.lang.builder.ToStringBuilder;

public class AgentMetric {

	private int longCount = 0;
	private int longProfitableCount = 0;
	private int shortCount = 0;
	private int shortProfitableCount = 0;
	private double longGL = 0d;
	private double shortGL = 0d;
	
	public int getTotalTrade(){		return longCount + shortCount;	}
	public int getTotalProfitableTrade(){		return longProfitableCount + shortProfitableCount;	}
	
	public int getLongCount() {
		return longCount;
	}
	public void addLongCount() {
		this.longCount++;
	}
	public int getLongProfitableCount() {
		return longProfitableCount;
	}
	public void addLongProfitableCount() {
		this.longProfitableCount++;
	}
	public int getShortCount() {
		return shortCount;
	}
	public void addShortCount() {
		this.shortCount++;
	}
	public int getShortProfitableCount() {
		return shortProfitableCount;
	}
	public void addShortProfitableCount() {
		this.shortProfitableCount++;
	}
	public double getLongGL() {
		return longGL;
	}
	public void addLongGL(double longGL) {
		this.longGL += longGL;
	}
	public double getShortGL() {
		return shortGL;
	}
	public void addShortGL(double shortGL) {
		this.shortGL += shortGL;
	}
	public double getTotalGain() {
		return this.getLongGL() + this.getShortGL();
	}
	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
	
}
