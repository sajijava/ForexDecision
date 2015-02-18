package com.snr.fxstrategyea.model;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.snr.fxstrategyea.agent.Action;
import com.snr.fxstrategyea.agent.AgentMetric;
import com.snr.fxstrategyea.agent.IndicatorAgent;

/**
 * @author saji
 *
 */
public class DecisionTree {

	private Node rootNode;
	private int depth;
	private AgentMetric metrics;

	
	
	public AgentMetric getMetrics() {
		return metrics;
	}

	public void setMetrics(AgentMetric metrics) {
		this.metrics = metrics;
	}

	public int getDepth() {
		return depth;
	}

	public void setDepth(int depth) {
		this.depth = depth;
	}

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
		private Map<Action,Node> children = null;

		public Node(){
			
		}
		public Node(IndicatorAgent agent) {
			super();
			this.agent = agent;
		}
		private void setChildren(){
			if(this.agent == null) throw new IllegalArgumentException("No agent found");
			this.children = new HashMap<Action, DecisionTree.Node>();
			for(Action action : this.agent.typeOfOutcome()){
				if(!this.children.containsKey(action))
					children.put(action, null);
			}
		}
		
		public Map<Action, Node> getChildren() {
			if(children == null){
				setChildren();
			}
			return children;
		}
		public boolean hasChildren()
		{
			return (this.children != null && this.children.keySet().size() > 0 && !this.children.containsValue(null));
		}
		public IndicatorAgent getAgent() {
			return agent;
		}
		public void setAgent(IndicatorAgent agent) {
			this.agent = agent;
		}
		@Override
		public String toString() {
			// TODO Auto-generated method stub
			return ToStringBuilder.reflectionToString(this);
		}

	}
}
