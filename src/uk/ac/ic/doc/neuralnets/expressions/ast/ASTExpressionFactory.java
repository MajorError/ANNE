package uk.ac.ic.doc.neuralnets.expressions.ast;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import uk.ac.ic.doc.neuralnets.expressions.ExpressionException;

/**
 * Factory for flyweight ASTExpression objects
 * @author Fred van den Driessche
 */
public class ASTExpressionFactory {
    private static final Logger log = Logger.getLogger( ASTExpressionFactory.class );
	private static ASTExpressionFactory instance;
	
	//Cached expressions
	private Map<String, ASTExpression> expressionMap;
	
	private ASTExpressionFactory(){}
	
    /**
     * Answer the instance of this singleton service
     * @return The ASTExpressionFactory
     */
	public static ASTExpressionFactory get(){
		if(instance == null){
			instance = new ASTExpressionFactory();
		}
		return instance;	
	}
	
    /**
     * Return a flyweight ASTExpression respresenting the given input string.
     * Attempts to do some disambiguation through removal of whitespace
     * before seeking an equivalent expression. Does not attempt any
     * re-ordering of expression components or more complex semantic
     * equivalence tests.
     * @param expressionString The expression to parse into an ASTExpression
     * @return An ASTExpression object, pulled from cache wherever possible.
     * @see uk.ac.ic.doc.neuralnets.expressions.ast.ASTExpression
     * @throws uk.ac.ic.doc.neuralnets.expressions.ExpressionException
     */
	public ASTExpression getExpression(String expressionString) throws ExpressionException{
		log.debug("Creating expression for " + expressionString);
		String expr = expressionString.replaceAll("\\s", "");
		if(getExpressionMap().containsKey(expr)){ //cached.
			log.trace("Retrieving expression " + expr + " from cache");
			return getExpressionMap().get(expr);
		}else{
			log.trace("Creating expression " + expr + ", adding to cache");
			ASTExpression r = new ASTExpression(expr);
			getExpressionMap().put(expressionString, r);
			return r;
		}
	}
	
    /**
     * Convenience method to answer an expression for a simple Double value.
     * @param d the Double to encode as an ASTExpression
     * @return The ASTExpression flyweight for this Double
     * @see uk.ac.ic.doc.neuralnets.expressions.ast.ASTExpression
     * @throws uk.ac.ic.doc.neuralnets.expressions.ExpressionException
     */
	public ASTExpression getExpression(Double d) throws ExpressionException{
		return getExpression(d.toString());
	}
	
    /**
     * Clear the cache of expressions, preventing any further replication of
     * old flyweights.
     */
	public void flushCache(){
		getExpressionMap().clear();
	}

	private Map<String, ASTExpression> getExpressionMap() {
		if(expressionMap == null){
			setExpressionMap(new HashMap<String, ASTExpression>());
		}
		return expressionMap;
	}

	private void setExpressionMap(Map<String, ASTExpression> expressionMap) {
		this.expressionMap = expressionMap;
	}
}
