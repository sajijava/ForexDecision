package com.snr.fxstrategyea.simulator;

import org.apache.commons.lang.builder.ToStringBuilder;

public class InvestmentConfig {

	private String dataAbsoluteFileName;
	private double initialInvestment;
	private double comission;
	private double stopLoss;
	private double risk;
	private double targetRate = 1.5;
	
	public double getTargetRate() {
		return targetRate;
	}
	public void setTargetRate(double targetRate) {
		this.targetRate = targetRate;
	}
	public String getDataAbsoluteFileName() {
		return dataAbsoluteFileName;
	}
	public void setDataAbsoluteFileName(String dataAbsoluteFileName) {
		this.dataAbsoluteFileName = dataAbsoluteFileName;
	}
	public double getInitialInvestment() {
		return initialInvestment;
	}
	public void setInitialInvestment(double initialInvestment) {
		this.initialInvestment = initialInvestment;
	}
	public double getComission() {
		return comission;
	}
	public void setComission(double comission) {
		this.comission = comission;
	}
	public double getStopLoss() {
		return stopLoss;
	}
	public void setStopLoss(double stopLoss) {
		this.stopLoss = stopLoss;
	}
	public double getRisk() {
		return risk;
	}
	public void setRisk(double risk) {
		this.risk = risk;
	}

	@Override
	public String toString() {
		
		return ToStringBuilder.reflectionToString(this);
	}
}
