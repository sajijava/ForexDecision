package com.snr.fxstrategyea.model;

import java.util.Date;

import org.apache.commons.lang.builder.ToStringBuilder;

public class OHLC implements Comparable<OHLC> {

	private Date date;
	private double open;
	private double high;
	private double low;
	private double close;
	private double volume;


	public OHLC() {
		super();
	}
	public OHLC(Date date, double open, double high, double low, double close,
			double volume) {
		super();
		this.date = date;
		this.open = open;
		this.high = high;
		this.low = low;
		this.close = close;
		this.volume = volume;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public double getOpen() {
		return open;
	}
	public void setOpen(double open) {
		this.open = open;
	}
	public double getHigh() {
		return high;
	}
	public void setHigh(double high) {
		this.high = high;
	}
	public double getLow() {
		return low;
	}
	public void setLow(double low) {
		this.low = low;
	}
	public double getClose() {
		return close;
	}
	public void setClose(double close) {
		this.close = close;
	}
	public double getVolume() {
		return volume;
	}
	public void setVolume(double volume) {
		this.volume = volume;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
	@Override
	public boolean equals(Object obj) {
		if(obj == null){
			return obj == this.date;
		}else if(obj instanceof Date){
			Date dt = (Date)obj;
			return this.date.equals(dt);

		}else{
			return false;

		}
	}
	
	public int compareTo(OHLC arg0) {
		// TODO Auto-generated method stub
		return (this.date != null && arg0 != null && arg0.getDate() != null)?this.date.compareTo(arg0.getDate()):0;
	}
	
}
