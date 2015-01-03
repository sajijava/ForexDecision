package com.snr.fxstrategyea.simulator;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.snr.fxstrategyea.agent.IndicatorAgent;
import com.snr.fxstrategyea.agent.impl.MACrossOverAgent;
import com.snr.fxstrategyea.model.OHLC;

public class InvestmentSimulator {

	private List<Transaction> simResult = new LinkedList<Transaction>();
	private List<OHLC> data = null;
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd"); 
	private InvestmentConfig invConfig;
	private Map<MetricKey,Double> metrics = new HashMap<MetricKey,Double>();
	public InvestmentSimulator(InvestmentConfig config) throws IOException, ParseException{
		data = new LinkedList<OHLC>();
		this.invConfig = config;
		loadData(new BufferedReader(new FileReader(this.invConfig.getDataAbsoluteFileName())));

		for(OHLC o : data){
			//			System.out.println(o);
		}
		metrics.put(MetricKey.LONG_COUNT,0d);
		metrics.put(MetricKey.SHORT_COUNT,0d);
		metrics.put(MetricKey.BIGGEST_WIN,0d);
		metrics.put(MetricKey.BIGGEST_DRAW,0d);

	}
	public void loadData(BufferedReader br) throws IOException, ParseException{
		String line = "";
		while(( line = br.readLine()) != null){
			//2014.08.22,05:00,1.32868,1.32914,1.32841,1.32866,1064
			String[] obj = line.split(",");
			data.add(new OHLC(sdf.parse(obj[0]),Double.parseDouble(obj[2]),Double.parseDouble(obj[3]),Double.parseDouble(obj[4]),Double.parseDouble(obj[5]),Double.parseDouble(obj[6])));
		}
	}
	public void simulate(List<IndicatorAgent> agents){
		if(data == null) throw new IllegalArgumentException("No Data");
		if(agents == null || agents.isEmpty()) throw new IllegalArgumentException("No Agents to simulate");
		Collections.sort(data);
		for(IndicatorAgent agent : agents){
			for(int i = agent.indexOffset() + 1; i < data.size(); i++){
				switch(agent.getAction(data.subList(i - agent.indexOffset() - 1, i))){
				case BUY:{
					if(!checkIfTradeExist()){
						createOrder(data.get(i),TradeDir.LONG);
					}else{
						closeOrder(data.get(i),TradeDir.LONG);
					}

				}
				case SELL:{
					if(!checkIfTradeExist()){
						createOrder(data.get(i),TradeDir.SHORT);
					}else{
						closeOrder(data.get(i),TradeDir.SHORT);
					}
				}
				case HOLD:
					break;
				}
			}
			closeOrder(data.get(data.size() -1),TradeDir.LONG);
			closeOrder(data.get(data.size() -1),TradeDir.SHORT);
		}
		for(Transaction tran : simResult){
			System.out.println(tran);
		}

	}
	private boolean checkIfTradeExist(){
		int count = 0;
		for(Transaction tran : simResult){
			if(tran.getStatus() == OrderStatus.OPEN)
				count++;
		}
		return count > 0;
	}
	private void createOrder(OHLC ohlc, TradeDir dir)
	{
		Transaction tran = new Transaction();
		tran.setStatus(OrderStatus.OPEN);
		if(simResult.size() > 1){
			tran.setStartingBal(simResult.get(simResult.size()-1).getEndingBal());
		}else{
			tran.setStartingBal(this.invConfig.getInitialInvestment());
		}
		switch(dir){
		case LONG:{
			tran.setDir(TradeDir.LONG);
			tran.setBuyDate(ohlc.getDate());
			tran.setBuyPrice(ohlc.getClose());
			break;	
		}
		case SHORT:{
			tran.setDir(TradeDir.SHORT);
			tran.setSellDate(ohlc.getDate());
			tran.setSellPrice(ohlc.getClose());
			break;
		}
		}
		tran.setUnits(calcUnits(tran));
		simResult.add(tran);
	}
	private void closeOrder(OHLC ohlc, TradeDir dir){
		for(Transaction tran : simResult){
			if(tran.getStatus() == OrderStatus.OPEN){
				switch(dir){
				case LONG:{
					if(tran.getDir() == dir){
						tran.setSellDate(ohlc.getDate());
						tran.setSellPrice(ohlc.getClose());
						tran.setStatus(OrderStatus.CLOSE);
						tran.setEndingBal(calcEndingBalance(tran));
						this.metrics.put(MetricKey.LONG_COUNT, new Double(this.metrics.get(MetricKey.LONG_COUNT).intValue()+1));
					}				
					break;
				}
				case SHORT:{
					if(tran.getDir() == dir){
						tran.setBuyDate(ohlc.getDate());
						tran.setBuyPrice(ohlc.getClose());
						tran.setStatus(OrderStatus.CLOSE);
						tran.setEndingBal(calcEndingBalance(tran));
						this.metrics.put(MetricKey.SHORT_COUNT, new Double(this.metrics.get(MetricKey.SHORT_COUNT).intValue()+1));
					}
					break;
				}			
				}
				if(tran.getStatus() == OrderStatus.CLOSE){
					double diff = (tran.getDir() == TradeDir.LONG?tran.getSellPrice() - tran.getBuyPrice():tran.getBuyPrice() - tran.getSellPrice()) * tran.getUnits();
					if( diff > this.metrics.get(MetricKey.BIGGEST_WIN))  this.metrics.put(MetricKey.BIGGEST_WIN, diff);
					if( diff < this.metrics.get(MetricKey.BIGGEST_DRAW))  this.metrics.put(MetricKey.BIGGEST_DRAW, diff);
				}

			}

		}
	} 
	private int calcUnits(Transaction tran){

		double riskAmt = tran.getStartingBal() * this.invConfig.getRisk();
		double riskedPip = riskAmt / this.invConfig.getStopLoss();
		double unit = riskedPip * 10000; // In this case, with 10k units (or one mini lot), each pip move is worth USD 1. USD 0.25 per pip * [(10k units of EUR/USD)/(USD 1 per pip)] = 2,500 units of EUR/USD

		return new Double(unit).intValue();
	}

	private double calcEndingBalance(Transaction tran){
		double entryAmt = tran.getStartingBal() - (tran.getUnits() * (tran.getDir() == TradeDir.LONG?tran.getBuyPrice():tran.getSellPrice())) - this.invConfig.getComission();
		double exitAmt = (tran.getUnits() * (tran.getDir() == TradeDir.LONG?tran.getSellPrice():tran.getBuyPrice())) - this.invConfig.getComission();
		return entryAmt + exitAmt;
	}
	public void summary(){
		String summary  = "Number of Trades : " + simResult.size()+
				"\n No. Of Long : {1}" +this.metrics.get(MetricKey.LONG_COUNT).intValue()+
				"\n No. Of Short : {2}" +this.metrics.get(MetricKey.SHORT_COUNT).intValue()+
				"\n Biggest Win : {3}" +this.metrics.get(MetricKey.BIGGEST_WIN)+
				"\n Biggest drawdown : {4}" +this.metrics.get(MetricKey.BIGGEST_DRAW)+
				"\n Total Equity : {5}"+( this.simResult.get(simResult.size() -1 ).getEndingBal() - this.simResult.get(0).getStartingBal());
		System.out.println(summary);
	}

	public static void main(String[] args) {
		try {
			InvestmentConfig config = new InvestmentConfig();
			config.setDataAbsoluteFileName("/home/saji/R/fx/EURUSDpro1440.csv");
			config.setInitialInvestment(1000d);
			config.setComission(0d);
			config.setStopLoss(20d);
			config.setRisk(0.01);
			
			InvestmentSimulator sim = new InvestmentSimulator(config);
			List<IndicatorAgent> agents = new LinkedList<IndicatorAgent>();
			agents.add(new MACrossOverAgent(13,55));
			sim.simulate(agents);
			sim.summary();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
class Transaction{
	private Date buyDate;
	private Double buyPrice;
	private Date sellDate;
	private Double sellPrice;
	private TradeDir dir;
	private OrderStatus status;
	private int units;
	private double startingBal;
	private double endingBal;


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
	public TradeDir getDir() {
		return dir;
	}
	public void setDir(TradeDir dir) {
		this.dir = dir;
	}
	public OrderStatus getStatus() {
		return status;
	}
	public void setStatus(OrderStatus status) {
		this.status = status;
	}


	public int getUnits() {
		return units;
	}
	public void setUnits(int units) {
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
enum OrderStatus{
	OPEN,CLOSE;
}
enum TradeDir{
	LONG,SHORT
}
enum MetricKey{
	LONG_COUNT, SHORT_COUNT,
	BIGGEST_WIN, BIGGEST_DRAW;
}