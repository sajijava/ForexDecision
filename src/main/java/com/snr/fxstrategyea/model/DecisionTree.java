package com.snr.fxstrategyea.model;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.snr.fxstrategyea.agent.Action;
import com.snr.fxstrategyea.agent.IndicatorAgent;

/**
 * @author saji
 *
 */
public class DecisionTree {

	private Node rootNode;

	public Node getRootNode()
	{
		return this.rootNode;
	}
	
	public void setRootNode(Node rootNode) {
		this.rootNode = rootNode;
	}

	public static DecisionTree.Node getNewNode(IndicatorAgent agent){
		return new DecisionTree().new Node(agent);
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return ToStringBuilder.reflectionToString(this);
	}

	public class Node{
		private IndicatorAgent agent = null;
		private final Map<Action,Node> children = new HashMap<Action, DecisionTree.Node>();

		public Node(){
			
		}
		public Node(IndicatorAgent agent) {
			super();
			this.agent = agent;
			setChildren();
		}
		private void setChildren(){
			if(this.agent == null) throw new IllegalArgumentException("No agent found");
			for(Action action : this.agent.typeOfOutcome()){
				if(!this.children.containsKey(action))
					children.put(action, null);
			}
		}
		
		public Map<Action, Node> getChildren() {
			return children;
		}
		public IndicatorAgent getAgent() {
			return agent;
		}
		public void setAgent(IndicatorAgent agent) {
			this.agent = agent;
			setChildren();
		}
		@Override
		public String toString() {
			// TODO Auto-generated method stub
			return ToStringBuilder.reflectionToString(this);
		}

	}
}
