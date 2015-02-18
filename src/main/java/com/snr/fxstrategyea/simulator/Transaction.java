package com.snr.fxstrategyea.simulator;

import java.util.Date;

import org.apache.commons.lang.builder.ToStringBuilder;

public class Transaction {
		private Date buyDate;
		private Double buyPrice;
		private Date sellDate;
		private Double sellPrice;
		private TradeDirection dir;
		private OrderStatus status;
		private double units;
		private double startingBal;
		private double endingBal;
		private Double stopLoss;
		private Double target;


		
		public Double getStopLoss() {
			return stopLoss;
		}
		public void setStopLoss(Double stopLoss) {
			this.stopLoss = stopLoss;
		}
		public Double getTarget() {
			return target;
		}
		public void setTarget(Double target) {
			this.target = target;
		}
		public Date getBuyDate() {
			return buyDate;
		}
		public void setBuyDate(Date buyDate) {
			this.buyDate = buyDate;
		}
		public Double getBuyPrice() {
			return buyPrice;
		}
		public void setBuyPrice(Double buyPrice) {
			this.buyPrice = buyPrice;
		}
		public Date getSellDate() {
			return sellDate;
		}
		public void setSellDate(Date sellDate) {
			this.sellDate = sellDate;
		}
		public Double getSellPrice() {
			return sellPrice;
		}
		public void setSellPrice(Double sellPrice) {
			this.sellPrice = sellPrice;
		}
		public TradeDirection getDir() {
			return dir;
		}
		public void setDir(TradeDirection dir) {
			this.dir = dir;
		}
		public OrderStatus getStatus() {
			return status;
		}
		public void setStatus(OrderStatus status) {
			this.status = status;
		}


		public double getUnits() {
			return units;
		}
		public void setUnits(double units) {
			this.units = units;
		}
		public double getStartingBal() {
			return startingBal;
		}
		public void setStartingBal(double startingBal) {
			this.startingBal = startingBal;
		}
		public double getEndingBal() {
			return endingBal;
		}
		public void setEndingBal(double endingBal) {
			this.endingBal = endingBal;
		}
		@Override
		public String toString() {
			return ToStringBuilder.reflectionToString(this);
		}


}
