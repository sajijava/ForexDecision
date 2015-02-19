package com.snr.fxstrategyea.simulator;

import java.io.IOException;
import java.text.ParseException;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.snr.fxstrategyea.agent.Action;
import com.snr.fxstrategyea.ea.DecisionTreeCrossover;
import com.snr.fxstrategyea.model.DecisionTree;

public class DecisionTreeInvSimulator extends InvestmentSimulator<DecisionTree> {

	private Logger logger = LoggerFactory.getLogger(DecisionTreeCrossover.class);
	public DecisionTreeInvSimulator(InvestmentConfig config)
			throws IOException, ParseException {
		super(config);
		// TODO Auto-generated constructor stub
	}
	@Override
	public InvestmentSimulator<DecisionTree> getInstance() throws IOException, ParseException{
		return new DecisionTreeInvSimulator(this.invConfig);
	}
	@Override
	public  void simulate(DecisionTree decisionTree) {
			this.initialize();
			for(int i = getMaxOffset(decisionTree); i < data.size(); i++){
				Action action = tradeableSignal(decisionTree.getRootNode(),i);

				switch(action){
				case BUY:{
					if(!checkIfTradeExist())
						createOrder(data.get(i),TradeDirection.LONG);
				}
				case SELL:{
					if(!checkIfTradeExist())
						createOrder(data.get(i),TradeDirection.SHORT);
				}
				case HOLD:
					break;
				}
				tradeManagement(data.get(i));

			}
			calculateMetrics();
			decisionTree.setMetrics(this.getAgentMap());
	}
	private Action tradeableSignal(DecisionTree.Node node, int index){
		Action action = node.getAgent().getAction(data.subList(index - node.getAgent().indexOffset() - 1, index));

		if((action == Action.BUY || action == Action.SELL) && !node.getChildren().containsValue(null)){
			action = tradeableSignal(node.getChildren().get(action),index);
		}
		return action;

	}

	private int getMaxOffset(DecisionTree dt){
		return getChildOffset(dt.getRootNode(), 0) + 1;
	}
	private int getChildOffset(DecisionTree.Node dt,int offset){

		if(dt.getAgent().indexOffset() > offset)
			offset = dt.getAgent().indexOffset();

		for(Map.Entry<Action, DecisionTree.Node> item: dt.getChildren().entrySet()){
			if(item.getValue() != null){
				offset = getChildOffset(item.getValue(),offset);
			}
		}
		return offset;
	}


}
