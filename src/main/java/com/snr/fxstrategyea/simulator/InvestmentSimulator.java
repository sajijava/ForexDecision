package com.snr.fxstrategyea.simulator;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.snr.fxstrategyea.agent.IndicatorAgent;
import com.snr.fxstrategyea.agent.impl.MACrossOverAgent;
import com.snr.fxstrategyea.model.DecisionTree;
import com.snr.fxstrategyea.model.OHLC;

public class InvestmentSimulator {

	private Logger logger = LoggerFactory.getLogger(InvestmentSimulator.class);
	private List<Transaction> simResult = new LinkedList<Transaction>();
	private List<OHLC> data = null;
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd"); 
	private InvestmentConfig invConfig;
	private Map<String,IndicatorAgent> agentMap = new HashMap<String,IndicatorAgent>();
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
	public void simulate(DecisionTree decisionTree){
		
	}
	public void simulate(List<IndicatorAgent> agents){
		if(data == null) throw new IllegalArgumentException("No Data");
		if(agents == null || agents.isEmpty()) throw new IllegalArgumentException("No Agents to simulate");
		Collections.sort(data);
		for(IndicatorAgent agent : agents){
			agentMap.put(agent.getName(),agent);
			for(int i = agent.indexOffset() + 1; i < data.size(); i++){
				switch(agent.getAction(data.subList(i - agent.indexOffset() - 1, i))){
				case BUY:{
					if(!checkIfTradeExist()){
						createOrder(data.get(i),TradeDirection.LONG);
					}else{
						closeOrder(data.get(i),TradeDirection.LONG, agent);
					}

				}
				case SELL:{
					if(!checkIfTradeExist()){
						createOrder(data.get(i),TradeDirection.SHORT);
					}else{
						closeOrder(data.get(i),TradeDirection.SHORT, agent);
					}
				}
				case HOLD:
					break;
				}
			}
			closeOrder(data.get(data.size() -1),TradeDirection.LONG,agent);
			closeOrder(data.get(data.size() -1),TradeDirection.SHORT,agent);
		}
/*		for(Transaction tran : simResult){
			logger.debug(""+tran);
		}*/
		//logger.debug(""+this.agentMap);
	}
	private boolean checkIfTradeExist(){
		int count = 0;
		for(Transaction tran : simResult){
			if(tran.getStatus() == OrderStatus.OPEN)
				count++;
		}
		return count > 0;
	}
	private void createOrder(OHLC ohlc, TradeDirection dir)
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
			tran.setBuyPrice(ohlc.getClose());
			break;	
		}
		case SHORT:{
			tran.setDir(TradeDirection.SHORT);
			tran.setSellDate(ohlc.getDate());
			tran.setSellPrice(ohlc.getClose());
			break;
		}
		}
		tran.setUnits(calcUnits(tran));
		simResult.add(tran);
	}
	private void closeOrder(OHLC ohlc, TradeDirection dir,IndicatorAgent agent){
		for(Transaction tran : simResult){
			if(tran.getStatus() == OrderStatus.OPEN){
				switch(dir){
				case LONG:{
					if(tran.getDir() == dir){
						tran.setSellDate(ohlc.getDate());
						tran.setSellPrice(ohlc.getClose());
						tran.setStatus(OrderStatus.CLOSE);
						tran.setEndingBal(calcEndingBalance(tran));
						
						this.agentMap.get(agent.getName()).getMetric().addLongCount();

						if(tran.getEndingBal() > tran.getStartingBal()) 
							this.agentMap.get(agent.getName()).getMetric().addLongProfitableCount();
						
						this.agentMap.get(agent.getName()).getMetric().addLongGL(tran.getEndingBal() - tran.getStartingBal());
					}				
					break;
				}
				case SHORT:{
					if(tran.getDir() == dir){
						tran.setBuyDate(ohlc.getDate());
						tran.setBuyPrice(ohlc.getClose());
						tran.setStatus(OrderStatus.CLOSE);
						tran.setEndingBal(calcEndingBalance(tran));

						this.agentMap.get(agent.getName()).getMetric().addShortCount();

						if(tran.getEndingBal() > tran.getStartingBal()) 
							this.agentMap.get(agent.getName()).getMetric().addShortProfitableCount();
						
						this.agentMap.get(agent.getName()).getMetric().addShortGL(tran.getEndingBal() - tran.getStartingBal());
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
		double entryAmt = tran.getStartingBal() - (tran.getUnits() * (tran.getDir() == TradeDirection.LONG?tran.getBuyPrice():tran.getSellPrice())) - this.invConfig.getComission();
		double exitAmt = (tran.getUnits() * (tran.getDir() == TradeDirection.LONG?tran.getSellPrice():tran.getBuyPrice())) - this.invConfig.getComission();
		return entryAmt + exitAmt;
	}
	public Map<String,IndicatorAgent> getAgentMap(){
		return this.agentMap;
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
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
