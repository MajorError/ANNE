grammar Calculation;

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
}

@header {
package uk.ac.ic.doc.neuralnets.expressions;
}

@lexer::header {
package uk.ac.ic.doc.neuralnets.expressions;
}

@members {
// MEMBERS go here
}

// Parser Definitions
stat returns [double value]
	:   expr NEWLINE { $value = $expr.value; }
	|	ID '=' expr NEWLINE { $value = new SymbVarType($ID.text, $expr.value); f.setvar($ID.text, $expr.value); }
    |   NEWLINE { $value = null; }
    ;

expr returns [double value]
    :   e=multExpr { $value = $e.value; }
        ( PLUS e=multExpr { $value = $value.add($e.value); }
        | MINUS e=multExpr { $value = $value.subtract($e.value); }
        )*
    ;

multExpr returns [double value]
    :   e=p1Expr { $value = $e.value; } 
    	( 
    	  MULT e=p1Expr { $value = $value.mult($e.value); }
    	| DIV e=p1Expr { $value = $value.div($e.value); }
    	| MOD e=p1Expr { $value = $value.mod($e.value); }
    	)*
    ;

p1Expr returns [double value]
	:	e=unary { $value = $e.value; }
		( POW e=unary { $value = $value.pow($e.value); }
		)*
	;

unary returns [double value]
	:	e=atom { $value = $e.value; }
	|	'-' e=atom { $value = $e.value.negate(); }
	|	'~' e=atom { $value = $e.value.not(); }
	;

atom returns [double value]
    :   INT { $value = new IntVarType($INT.text); }
    |	FLOAT { $value = new FloatVarType($FLOAT.text); }
    |	ID { $value = f.getvar($ID.text); }
    |	ID LPAREN args RPAREN { $value = f.call($ID.text, $args.value); }
    |	STRING { $value = new StringVarType($STRING.text.substring(1, $STRING.text.length()-1)); }
    |   LPAREN expr RPAREN { $value = $expr.value; $value.formula = "\\left(" + $value.formula + "\\right)"; }
    ;

args returns [List<VarType> value]
	: e=expr { $value = new ArrayList<VarType>(); $value.add($e.value); }
	 (',' e=expr { $value.add($e.value); } )*
	;

// Lexer definitions
INT : '0'..'9'+ ;
FLOAT : '0'..'9'* '.' '0'..'9'+;
ID : ('a'..'z' | 'A'..'Z')+;
STRING : '\"' .* '\"';
NEWLINE :'\r'? '\n' ;
WS : (' '|'\t')+ {skip();} ;
