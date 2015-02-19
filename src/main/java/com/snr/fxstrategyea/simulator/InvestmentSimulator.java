package com.snr.fxstrategyea.simulator;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.snr.fxstrategyea.agent.AgentMetric;
import com.snr.fxstrategyea.agent.IndicatorAgent;
import com.snr.fxstrategyea.model.DecisionTree;
import com.snr.fxstrategyea.model.OHLC;

public abstract class InvestmentSimulator<T> {

	private Logger logger = LoggerFactory.getLogger(InvestmentSimulator.class);
	protected List<Transaction> simResult = new LinkedList<Transaction>();
	protected List<OHLC> data = null;
	protected SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd"); 
	protected InvestmentConfig invConfig;
	protected Map<String,IndicatorAgent> agentMap = new HashMap<String,IndicatorAgent>();
	protected AgentMetric metric = new AgentMetric();

	public InvestmentSimulator(InvestmentConfig config) throws IOException, ParseException{
		data = new LinkedList<OHLC>();	
		this.invConfig = config;
		loadData(new BufferedReader(new FileReader(this.invConfig.getDataAbsoluteFileName())));
	}
	public void loadData(BufferedReader br) throws IOException, ParseException{
		String line = "";
		while(( line = br.readLine()) != null){
			//2014.08.22,05:00,1.32868,1.32914,1.32841,1.32866,1064
			String[] obj = line.split(",");
			data.add(new OHLC(sdf.parse(obj[0]),Double.parseDouble(obj[2]),Double.parseDouble(obj[3]),Double.parseDouble(obj[4]),Double.parseDouble(obj[5]),Double.parseDouble(obj[6])));
		}
	}
	protected void initialize(){
		simResult = new LinkedList<Transaction>();
		metric = new AgentMetric();
	}
	public abstract void simulate(T t);
	public abstract InvestmentSimulator<T> getInstance() throws IOException, ParseException;


	protected boolean checkIfTradeExist(){
		int count = 0;
		if(simResult != null){
			for(Transaction tran : simResult){
				if(tran.getStatus() == OrderStatus.OPEN)
					count++;
			}
		}
		return count > 0;
	}
	protected void createOrder(OHLC ohlc, TradeDirection dir)
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
			tran.setDir(TradeDirection.LONG);
			tran.setBuyDate(ohlc.getDate());
			tran.setBuyPrice(ohlc.getHigh());
			tran.setStopLoss(ohlc.getLow()  - this.invConfig.getStopLoss());
			tran.setTarget(ohlc.getHigh()  + (this.invConfig.getTargetRate() * this.invConfig.getStopLoss()));
			break;	
		}
		case SHORT:{
			tran.setDir(TradeDirection.SHORT);
			tran.setSellDate(ohlc.getDate());
			tran.setSellPrice(ohlc.getLow());
			tran.setStopLoss(ohlc.getHigh()  + this.invConfig.getStopLoss());
			tran.setTarget(ohlc.getLow()  - (this.invConfig.getTargetRate() * this.invConfig.getStopLoss()));
			break;
		}
		}
		tran.setUnits(calcUnits(tran));
		simResult.add(tran);
	}
	protected void closeOrder(OHLC ohlc, TradeDirection dir){
		for(Transaction tran : simResult){
			if(tran.getStatus() == OrderStatus.OPEN){
				switch(dir){
				case LONG:{
					if(tran.getDir() == dir){
						tran.setSellDate(ohlc.getDate());
						tran.setSellPrice(ohlc.getClose());
						tran.setStatus(OrderStatus.CLOSE);
						tran.setEndingBal(calcEndingBalance(tran));

						this.metric.addLongCount();

						if(tran.getEndingBal() > tran.getStartingBal()) 
							this.metric.addLongProfitableCount();

						this.metric.addLongGL(tran.getEndingBal() - tran.getStartingBal());
					}				
					break;
				}
				case SHORT:{
					if(tran.getDir() == dir){
						tran.setBuyDate(ohlc.getDate());
						tran.setBuyPrice(ohlc.getClose());
						tran.setStatus(OrderStatus.CLOSE);
						tran.setEndingBal(calcEndingBalance(tran));

						this.metric.addShortCount();

						if(tran.getEndingBal() > tran.getStartingBal()) 
							this.metric.addShortProfitableCount();

						this.metric.addShortGL(tran.getEndingBal() - tran.getStartingBal());
					}
					break;
				}			
				}
				/*				if(tran.getStatus() == OrderStatus.CLOSE){
					double diff = (tran.getDir() == TradeDirection.LONG?tran.getSellPrice() - tran.getBuyPrice():tran.getBuyPrice() - tran.getSellPrice()) * tran.getUnits();
					if( diff > this.metrics.get(MetricKey.BIGGEST_WIN))  this.metrics.put(MetricKey.BIGGEST_WIN, diff);
					if( diff < this.metrics.get(MetricKey.BIGGEST_DRAW))  this.metrics.put(MetricKey.BIGGEST_DRAW, diff);
				}
				 */
			//	logger.debug(""+tran);
			}

		}
	} 
	protected double calcUnits(Transaction tran){

		double riskAmt = tran.getStartingBal() * this.invConfig.getRisk();
		double riskedPip = riskAmt / (this.invConfig.getStopLoss() * 10000);
		double unit = riskedPip; // In this case, with 10k units (or one mini lot), each pip move is worth USD 1. USD 0.25 per pip * [(10k units of EUR/USD)/(USD 1 per pip)] = 2,500 units of EUR/USD

		return unit;
	}

	protected double calcEndingBalance(Transaction tran){

		double entryAmt = 10000 * tran.getUnits() * (tran.getDir() == TradeDirection.LONG?tran.getBuyPrice():tran.getSellPrice());
		double exitAmt  = 10000 * tran.getUnits() * (tran.getDir() == TradeDirection.LONG?tran.getSellPrice():tran.getBuyPrice());
		return tran.getStartingBal() + (tran.getDir() == TradeDirection.LONG?(exitAmt - entryAmt):(entryAmt - exitAmt));
	}
	public AgentMetric getAgentMap(){
		return this.metric;
	}
	protected void calculateMetrics(){
		for(Transaction tran : simResult){
			if(tran.getStatus() == OrderStatus.CLOSE){
				if(tran.getDir() == TradeDirection.LONG){
					this.metric.addLongCount();

					if(tran.getEndingBal() > tran.getStartingBal()) 
						this.metric.addLongProfitableCount();

					this.metric.addLongGL(tran.getEndingBal() - tran.getStartingBal());
				}else  if(tran.getDir() == TradeDirection.SHORT){
					this.metric.addShortCount();

					if(tran.getEndingBal() > tran.getStartingBal()) 
						this.metric.addShortProfitableCount();

					this.metric.addShortGL(tran.getEndingBal() - tran.getStartingBal());

				}
			}
		}

	}
	protected void tradeManagement(OHLC data){
		if(simResult != null && !simResult.isEmpty()){
			for(Transaction tran : simResult){
				if(tran.getStatus() == OrderStatus.OPEN){
					if(tran.getDir() == TradeDirection.LONG){
						if(data.getLow() < tran.getStopLoss()){
							tran.setSellDate(data.getDate());
							tran.setSellPrice(data.getLow());
							tran.setStatus(OrderStatus.CLOSE);
							tran.setEndingBal(calcEndingBalance(tran));
						}else if(data.getHigh() > tran.getTarget()){
							tran.setSellDate(data.getDate());
							tran.setSellPrice(data.getHigh());
							tran.setStatus(OrderStatus.CLOSE);
							tran.setEndingBal(calcEndingBalance(tran));
						}


					}else  if(tran.getDir() == TradeDirection.SHORT){
						if(data.getLow() > tran.getTarget()){
							tran.setBuyDate(data.getDate());
							tran.setBuyPrice(data.getLow());
							tran.setStatus(OrderStatus.CLOSE);
							tran.setEndingBal(calcEndingBalance(tran));
						}else if(data.getHigh() < tran.getStopLoss()){
							tran.setBuyDate(data.getDate());
							tran.setBuyPrice(data.getHigh());
							tran.setStatus(OrderStatus.CLOSE);
							tran.setEndingBal(calcEndingBalance(tran));
						}
					}
				}
			}
		}
	}

}
