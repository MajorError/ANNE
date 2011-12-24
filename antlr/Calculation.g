grammar Calculation;

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
package uk.ac.ic.doc.neuralnets.expressions;

import java.util.Random;
import java.util.Map;
import java.util.HashMap;
}

@lexer::header {
package uk.ac.ic.doc.neuralnets.expressions;
}

@members {
	private Random r = new Random();
	
	private Map<String,Double> bindings = new HashMap<String,Double>();
	        
        public void bind( String var, Double val ) {
        	bindings.put( var, val );
        }
	
	
	private List<String> errors = new ArrayList<String>();
	
	public void displayRecognitionError( String[] tokenNames,
                                        RecognitionException e ) {        
		String hdr = getErrorHeader( e );
        	String msg = getErrorMessage( e, tokenNames );
        	errors.add( hdr + " " + msg );
	}
        
        public Double evaluate() throws ExpressionException {
            try {
                Double out = stat();
                if ( errors.size() > 0 )
                    throw new ExpressionException( errors.get( 0 ) );
                return out;
            } catch ( RecognitionException ex ) {
                throw new ExpressionException( ex.getMessage() );
            }
        }
}

// Parser Definitions
stat returns [Double value]
	:   lowLevelExpr NEWLINE { $value = $lowLevelExpr.value; }
    ;

lowLevelExpr returns [Double value]
    :   e=multLevelExpr { $value = $e.value; }
        ( PLUS e=multLevelExpr { $value = $value + $e.value; }
        | MINUS e=multLevelExpr { $value = $value - $e.value; }
        )*
    ;

multLevelExpr returns [Double value]
    :   e=powLevelExpr { $value = $e.value; } 
    	( 
    	  MULT e=powLevelExpr { $value = $value * $e.value; }
    	| DIV e=powLevelExpr { $value = $value / $e.value; }
    	| MOD e=powLevelExpr { $value = $value \% $e.value; }
    	)*
    ;

powLevelExpr returns [Double value]
	:	e=unary { $value = $e.value; }
		( POW e=unary { $value = Math.pow( $value, $e.value ); }
		)*
	;

unary returns [Double value]
	:	e=atom { $value = $e.value; }
	|	MINUS e=atom { $value = -$e.value; }
	;

atom returns [Double value] 
	:   	INT { $value = Double.parseDouble( $INT.text ); }
	|	VAR { if ( !bindings.containsKey( $VAR.text ) ) {
			errors.add( "No binding for '" + $VAR.text + "'" );
			$value = new Double( 0 );
		} else {
			$value = bindings.get( $VAR.text );
		}
	}
	|	DOUBLE { $value = Double.parseDouble( $DOUBLE.text ); }
	| 	RAND { $value = r.nextDouble(); }
	| 	GRAND { $value = r.nextGaussian(); }
	|   	LPAREN lowLevelExpr RPAREN { $value = $lowLevelExpr.value; }
	|	SINH LPAREN e=lowLevelExpr{ $value = Math.sinh( $e.value ); } RPAREN
	|	COSH LPAREN e=lowLevelExpr{ $value = Math.cosh( $e.value ); } RPAREN
	|	TANH LPAREN e=lowLevelExpr{ $value = Math.tanh( $e.value ); } RPAREN
	|	SIN LPAREN e=lowLevelExpr{ $value = Math.sin( $e.value ); } RPAREN
	|	COS LPAREN e=lowLevelExpr{ $value = Math.cos( $e.value ); } RPAREN
	|	TAN LPAREN e=lowLevelExpr{ $value = Math.tan( $e.value ); } RPAREN
    ;

// Lexer definitions
INT 	: '0'..'9'+ ;
DOUBLE 	: '0'..'9'* '.' '0'..'9'+;
NEWLINE : '\r'? '\n' ;
VAR 	: ('a'..'z'|'A'..'Z')+;
WS 	: (' '|'\t')+ {skip();};
