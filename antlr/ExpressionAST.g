grammar ExpressionAST;

options {
	backtrack = false;
}

tokens {
	PLUS = '+';
	MINUS = '-';
	MULT = '*';
	DIV = '/';
	MOD = '%';
	LPAREN = '(';
	RPAREN = ')';
	POW = '^';
	SQRT = 'SQRT';
	RAND = 'RAND()'; // random number
	GRAND = 'GRAND()'; // gaussian random number
	SIN = 'SIN';
	COS = 'COS';  // trig fuctions
	TAN = 'TAN';
	SINH = 'SINH';
	COSH = 'COSH';  // hyperbolic functions
	TANH = 'TANH';
}

@header {
package uk.ac.ic.doc.neuralnets.expressions.ast;

import java.util.Random;
import java.util.Map;
import java.util.Set;
import java.util.HashMap;
import uk.ac.ic.doc.neuralnets.expressions.ExpressionException;
}

@lexer::header {
package uk.ac.ic.doc.neuralnets.expressions.ast;
}

@members {
	private final Random r = new Random();
	
	private Map<String,Variable> vars = new HashMap<String,Variable>();
	
	public Map<String,Variable> getVariables() {
		return vars;
	}
}

// Parser Definitions
getTree returns [Component value]
	:   lowLevelExpr NEWLINE { $value = new NoOpComponent( $lowLevelExpr.value ); }
    ;

lowLevelExpr returns [Component value]
	:	l=multLevelExpr { $value = $l.value; }
	        ( PLUS r=multLevelExpr { 
	        	$value = new BinaryOperator( $value, $r.value, "+" ) {
				public Double evaluate() throws ExpressionException {
					return l.evaluate() + r.evaluate();
				}
			};
	        }
	        | MINUS r=multLevelExpr { 
	        	$value = new BinaryOperator( $value, $r.value, "-" ) {
				public Double evaluate() throws ExpressionException {
					return l.evaluate() - r.evaluate();
				}
			}; 
	        }
	        )*
	;

multLevelExpr returns [Component value]
	:   	l=powLevelExpr { $value = $l.value; }
	    	( MULT r=powLevelExpr { 
	    	  	$value = new BinaryOperator( $value, $r.value, "*" ) {
				public Double evaluate() throws ExpressionException {
					return l.evaluate() * r.evaluate();
				}
			};
	    	}
	    	| DIV r=powLevelExpr { 
	    		$value = new BinaryOperator( $value, $r.value, "/" ) {
				public Double evaluate() throws ExpressionException {
					return l.evaluate() / r.evaluate();
				}
			};
	    	}
	    	| MOD r=powLevelExpr { 
	    		$value = new BinaryOperator( $value, $r.value, "\%" ) {
				public Double evaluate() throws ExpressionException {
					return l.evaluate() \% r.evaluate();
				}
			};
	    	}
	    	)*
	;

powLevelExpr returns [Component value]
	: 	l=unary { $value = $l.value; }
		( POW r=unary { 
			$value = new BinaryOperator( $value, $r.value, "^" ) {
				public Double evaluate() throws ExpressionException {
					return Math.pow( l.evaluate(), r.evaluate() );
				}
			}; 
		}
		)*
	;

unary returns [Component value]
	:	e=atom { $value = $e.value; }
	|	MINUS e=atom { 
			$value = new UnaryOperator( $e.value, "-" ) {
				public Double evaluate() throws ExpressionException {
					return -c.evaluate();
				}
			}; 
		}
	;

atom returns [Component value] 
	:   	INT { $value = new Literal( $INT.text ); }
	|	DOUBLE { $value = new Literal( $DOUBLE.text ); }
	|	VAR { 
			$value = new Variable( $VAR.text ); 
			vars.put( $VAR.text, (Variable)$value );
		}
	|   	LPAREN lowLevelExpr RPAREN { $value = $lowLevelExpr.value; }
	|	SQRT LPAREN e=lowLevelExpr{ 
			$value = new UnaryOperator( $e.value, "SQRT" ) {
				public Double evaluate() throws ExpressionException {
					return Math.sqrt( c.evaluate() );
				}
			}; 
		} RPAREN
	| 	RAND { 
			$value = new NullaryOperator( "RAND" ) {
				public Double evaluate() throws ExpressionException {
					return r.nextDouble(); 
				}
								
				public Set<Component> getVariables() {
					variables.add( this );
					return super.getVariables();
				}
			}; 
		}
	| 	GRAND { 
			$value = new NullaryOperator( "GRAND" ) {
				public Double evaluate() throws ExpressionException {
					return r.nextGaussian(); 
				}
				
				public Set<Component> getVariables() {
					variables.add( this );
					return super.getVariables();
				}
			}; 
		}
	|	SINH LPAREN e=lowLevelExpr{ 
			$value = new UnaryOperator( $e.value, "SINH" ) {
				public Double evaluate() throws ExpressionException {
					return Math.sinh( c.evaluate() );
				}
			}; 
		} RPAREN
	|	COSH LPAREN e=lowLevelExpr{ 
			$value = new UnaryOperator( $e.value, "COSH" ) {
				public Double evaluate() throws ExpressionException {
					return Math.cosh( c.evaluate() );
				}
			};
		} RPAREN
	|	TANH LPAREN e=lowLevelExpr{ 
			$value = new UnaryOperator( $e.value, "TANH" ) {
				public Double evaluate() throws ExpressionException {
					return Math.tanh( c.evaluate() );
									}
							};
									} RPAREN
	|	SIN LPAREN e=lowLevelExpr{ 
			$value = new UnaryOperator( $e.value, "SIN" ) {
				public Double evaluate() throws ExpressionException {
					return Math.sin( c.evaluate() );
				}
			};
		} RPAREN
	|	COS LPAREN e=lowLevelExpr{ 
			$value = new UnaryOperator( $e.value, "COS" ) {
				public Double evaluate() throws ExpressionException {
					return Math.cos( c.evaluate() );
				}
			};
		} RPAREN
	|	TAN LPAREN e=lowLevelExpr{ 
			$value = new UnaryOperator( $e.value, "TAN" ) {
				public Double evaluate() throws ExpressionException {
					return Math.tan( c.evaluate() );
				}
			};
		} RPAREN
    ;

// Lexer definitions
INT 	: '0'..'9'+ ;
DOUBLE 	: '0'..'9'* '.' '0'..'9'+;
NEWLINE : '\r'? '\n' ;
VAR 	: ('a'..'z'|'A'..'Z')+;
WS 	: (' '|'\t')+ {skip();};
