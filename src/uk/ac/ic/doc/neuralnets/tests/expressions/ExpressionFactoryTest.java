package uk.ac.ic.doc.neuralnets.tests.expressions;

import java.io.File;

import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;

import uk.ac.ic.doc.neuralnets.expressions.ExpressionException;
import uk.ac.ic.doc.neuralnets.expressions.ast.ASTExpression;
import uk.ac.ic.doc.neuralnets.expressions.ast.ASTExpressionFactory;

public class ExpressionFactoryTest {
	private static final Logger log = Logger.getLogger( ExpressionFactoryTest.class );
	
	public static void main(String[] args) throws ExpressionException {
		DOMConfigurator.configure("conf" + File.separator + "log-conf.xml");
		log.info("Starting");
		String test1 = "0.02 + 0.08 * RAND()";
		String test2 = "0.02  +  0.08  *  RAND()";
		Double test3 = 1234d;
		
		//Create expression
		log.info("Getting expression for " + test1);
		ASTExpression exp1 = ASTExpressionFactory.get().getExpression(test1);
		//Retrieve;
		log.info("Getting expression for " + test1);
		ASTExpression exp2 = ASTExpressionFactory.get().getExpression(test1);
		//Retrieve
		log.info("Getting expression for " + test2);
		ASTExpression exp3 = ASTExpressionFactory.get().getExpression(test2);
		//Create
		log.info("Getting expression for " + test3);
		ASTExpression exp4 = ASTExpressionFactory.get().getExpression(test3);
		//Retrieve
		log.info("Getting expression for " + test3);
		ASTExpression exp5 = ASTExpressionFactory.get().getExpression(test3);
		
		boolean res1 = exp1.getExpression().equals(exp2.getExpression());
		boolean res2 = exp1.getExpression().equals(exp3.getExpression());
		boolean res3 = exp4.getExpression().equals(exp5.getExpression());
		
		log.trace(res1);
		log.trace(res2);
		log.trace(res3);
		
		log.info((res1 && res2 && res3) ? "Pass" : "Fail");
	}
	

}
