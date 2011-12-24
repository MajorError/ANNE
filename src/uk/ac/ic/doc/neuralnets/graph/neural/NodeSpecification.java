package uk.ac.ic.doc.neuralnets.graph.neural;

import uk.ac.ic.doc.neuralnets.graph.*;
import java.io.Serializable;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.apache.log4j.Logger;
import uk.ac.ic.doc.neuralnets.expressions.ast.ASTExpression;
import uk.ac.ic.doc.neuralnets.graph.neural.Neurone;
import uk.ac.ic.doc.neuralnets.graph.neural.EdgeDecoration;
import uk.ac.ic.doc.neuralnets.util.plugins.PluginLoadException;
import uk.ac.ic.doc.neuralnets.util.plugins.PluginManager;

/**
 * Default NodeSpecification
 * 
 * @author Peter Coetzee
 */
public class NodeSpecification<T extends Node> implements Serializable {

	private static final long serialVersionUID = 4009971866982162717L;
	private Map<String, ASTExpression> parameters = new HashMap<String, ASTExpression>();
	private Class<T> target;
	private EdgeDecoration ed;
	private String name;

	private static final Logger log = Logger.getLogger(NodeSpecification.class);

	public NodeSpecification() {
		this((Class) Neurone.class);
	}

	public NodeSpecification(Class<T> target) {
		this.target = target;
	}

	/**
	 * Get the parameter key set.
	 * 
	 * @return Parameter key set.
	 */
	public Set<String> getParameters() {
		return parameters.keySet();
	}

	/**
	 * Get the AST expression for input parameter.
	 * 
	 * @param param
	 *            String
	 * @return AST expression
	 */
	public ASTExpression get(String param) {
		return parameters.get(param);
	}

	/**
	 * Get target of node specification.
	 * 
	 * @return Target
	 */
	public Class<T> getTarget() {
		return target;
	}

	/**
	 * Set name of node specification.
	 * 
	 * @param n
	 *            Name
	 */
	public void setName(String n) {
		name = n;
	}

	/**
	 * Get the name of the node specification.
	 * 
	 * @return The name.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Set the edge decorator for the node specification.
	 * 
	 * @param ed
	 *            The edge decoration.
	 */
	public void setEdgeDecoration(EdgeDecoration ed) {
		this.ed = ed;
	}

	/**
	 * Get the edge decoration for the node specification.
	 * 
	 * @return The edge decoration.
	 */
	public EdgeDecoration getEdgeDecoration() {
		if (ed == null) {
			try {
				ed = PluginManager.get().getPlugin("Arrow",
						EdgeDecoration.class);
			} catch (PluginLoadException ex) {
				log.error("Unable to load default edge decoration", ex);
			}
		}
		return ed;
	}

	/**
	 * Set a parameter to an AST expresion.
	 * 
	 * @param param
	 *            Parameter name
	 * @param target
	 *            AST expression value.
	 * @return Itself.
	 */
	public NodeSpecification set(String param, ASTExpression target) {
		parameters.put(param, target);
		return this;
	}

}
