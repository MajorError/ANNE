// $ANTLR 3.1.1 F:\\My Documents\\Documents\\Coding\\NeuralNetwork\\antlr\\ExpressionAST.g 2008-12-16 17:22:57

package uk.ac.ic.doc.neuralnets.expressions.ast;

import java.util.Random;
import java.util.Map;
import java.util.Set;
import java.util.HashMap;
import uk.ac.ic.doc.neuralnets.expressions.ExpressionException;


import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

public class ExpressionASTParser extends Parser {
    public static final String[] tokenNames = new String[] {
        "<invalid>", "<EOR>", "<DOWN>", "<UP>", "PLUS", "MINUS", "MULT", "DIV", "MOD", "LPAREN", "RPAREN", "POW", "SQRT", "RAND", "GRAND", "SIN", "COS", "TAN", "SINH", "COSH", "TANH", "NEWLINE", "INT", "DOUBLE", "VAR", "WS"
    };
    public static final int MOD=8;
    public static final int INT=22;
    public static final int GRAND=14;
    public static final int COSH=19;
    public static final int MULT=6;
    public static final int MINUS=5;
    public static final int SQRT=12;
    public static final int EOF=-1;
    public static final int SINH=18;
    public static final int LPAREN=9;
    public static final int RPAREN=10;
    public static final int TANH=20;
    public static final int WS=25;
    public static final int POW=11;
    public static final int NEWLINE=21;
    public static final int SIN=15;
    public static final int COS=16;
    public static final int RAND=13;
    public static final int TAN=17;
    public static final int DOUBLE=23;
    public static final int PLUS=4;
    public static final int VAR=24;
    public static final int DIV=7;

    // delegates
    // delegators


        public ExpressionASTParser(TokenStream input) {
            this(input, new RecognizerSharedState());
        }
        public ExpressionASTParser(TokenStream input, RecognizerSharedState state) {
            super(input, state);
             
        }
        

    public String[] getTokenNames() { return ExpressionASTParser.tokenNames; }
    public String getGrammarFileName() { return "F:\\My Documents\\Documents\\Coding\\NeuralNetwork\\antlr\\ExpressionAST.g"; }


    	private final Random r = new Random();
    	
    	private Map<String,Variable> vars = new HashMap<String,Variable>();
    	
    	public Map<String,Variable> getVariables() {
    		return vars;
    	}



    // $ANTLR start "getTree"
    // F:\\My Documents\\Documents\\Coding\\NeuralNetwork\\antlr\\ExpressionAST.g:52:1: getTree returns [Component value] : lowLevelExpr NEWLINE ;
    public final Component getTree() throws RecognitionException {
        Component value = null;

        Component lowLevelExpr1 = null;


        try {
            // F:\\My Documents\\Documents\\Coding\\NeuralNetwork\\antlr\\ExpressionAST.g:53:2: ( lowLevelExpr NEWLINE )
            // F:\\My Documents\\Documents\\Coding\\NeuralNetwork\\antlr\\ExpressionAST.g:53:6: lowLevelExpr NEWLINE
            {
            pushFollow(FOLLOW_lowLevelExpr_in_getTree199);
            lowLevelExpr1=lowLevelExpr();

            state._fsp--;

            match(input,NEWLINE,FOLLOW_NEWLINE_in_getTree201); 
             value = new NoOpComponent( lowLevelExpr1 ); 

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return value;
    }
    // $ANTLR end "getTree"


    // $ANTLR start "lowLevelExpr"
    // F:\\My Documents\\Documents\\Coding\\NeuralNetwork\\antlr\\ExpressionAST.g:56:1: lowLevelExpr returns [Component value] : l= multLevelExpr ( PLUS r= multLevelExpr | MINUS r= multLevelExpr )* ;
    public final Component lowLevelExpr() throws RecognitionException {
        Component value = null;

        Component l = null;

        Component r = null;


        try {
            // F:\\My Documents\\Documents\\Coding\\NeuralNetwork\\antlr\\ExpressionAST.g:57:2: (l= multLevelExpr ( PLUS r= multLevelExpr | MINUS r= multLevelExpr )* )
            // F:\\My Documents\\Documents\\Coding\\NeuralNetwork\\antlr\\ExpressionAST.g:57:4: l= multLevelExpr ( PLUS r= multLevelExpr | MINUS r= multLevelExpr )*
            {
            pushFollow(FOLLOW_multLevelExpr_in_lowLevelExpr223);
            l=multLevelExpr();

            state._fsp--;

             value = l; 
            // F:\\My Documents\\Documents\\Coding\\NeuralNetwork\\antlr\\ExpressionAST.g:58:10: ( PLUS r= multLevelExpr | MINUS r= multLevelExpr )*
            loop1:
            do {
                int alt1=3;
                int LA1_0 = input.LA(1);

                if ( (LA1_0==PLUS) ) {
                    alt1=1;
                }
                else if ( (LA1_0==MINUS) ) {
                    alt1=2;
                }


                switch (alt1) {
            	case 1 :
            	    // F:\\My Documents\\Documents\\Coding\\NeuralNetwork\\antlr\\ExpressionAST.g:58:12: PLUS r= multLevelExpr
            	    {
            	    match(input,PLUS,FOLLOW_PLUS_in_lowLevelExpr238); 
            	    pushFollow(FOLLOW_multLevelExpr_in_lowLevelExpr242);
            	    r=multLevelExpr();

            	    state._fsp--;

            	     
            	    	        	value = new BinaryOperator( value, r, "+" ) {
            	    				public Double evaluate() throws ExpressionException {
            	    					return l.evaluate() + r.evaluate();
            	    				}
            	    			};
            	    	        

            	    }
            	    break;
            	case 2 :
            	    // F:\\My Documents\\Documents\\Coding\\NeuralNetwork\\antlr\\ExpressionAST.g:65:12: MINUS r= multLevelExpr
            	    {
            	    match(input,MINUS,FOLLOW_MINUS_in_lowLevelExpr257); 
            	    pushFollow(FOLLOW_multLevelExpr_in_lowLevelExpr261);
            	    r=multLevelExpr();

            	    state._fsp--;

            	     
            	    	        	value = new BinaryOperator( value, r, "-" ) {
            	    				public Double evaluate() throws ExpressionException {
            	    					return l.evaluate() - r.evaluate();
            	    				}
            	    			}; 
            	    	        

            	    }
            	    break;

            	default :
            	    break loop1;
                }
            } while (true);


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return value;
    }
    // $ANTLR end "lowLevelExpr"


    // $ANTLR start "multLevelExpr"
    // F:\\My Documents\\Documents\\Coding\\NeuralNetwork\\antlr\\ExpressionAST.g:75:1: multLevelExpr returns [Component value] : l= powLevelExpr ( MULT r= powLevelExpr | DIV r= powLevelExpr | MOD r= powLevelExpr )* ;
    public final Component multLevelExpr() throws RecognitionException {
        Component value = null;

        Component l = null;

        Component r = null;


        try {
            // F:\\My Documents\\Documents\\Coding\\NeuralNetwork\\antlr\\ExpressionAST.g:76:2: (l= powLevelExpr ( MULT r= powLevelExpr | DIV r= powLevelExpr | MOD r= powLevelExpr )* )
            // F:\\My Documents\\Documents\\Coding\\NeuralNetwork\\antlr\\ExpressionAST.g:76:7: l= powLevelExpr ( MULT r= powLevelExpr | DIV r= powLevelExpr | MOD r= powLevelExpr )*
            {
            pushFollow(FOLLOW_powLevelExpr_in_multLevelExpr295);
            l=powLevelExpr();

            state._fsp--;

             value = l; 
            // F:\\My Documents\\Documents\\Coding\\NeuralNetwork\\antlr\\ExpressionAST.g:77:7: ( MULT r= powLevelExpr | DIV r= powLevelExpr | MOD r= powLevelExpr )*
            loop2:
            do {
                int alt2=4;
                switch ( input.LA(1) ) {
                case MULT:
                    {
                    alt2=1;
                    }
                    break;
                case DIV:
                    {
                    alt2=2;
                    }
                    break;
                case MOD:
                    {
                    alt2=3;
                    }
                    break;

                }

                switch (alt2) {
            	case 1 :
            	    // F:\\My Documents\\Documents\\Coding\\NeuralNetwork\\antlr\\ExpressionAST.g:77:9: MULT r= powLevelExpr
            	    {
            	    match(input,MULT,FOLLOW_MULT_in_multLevelExpr307); 
            	    pushFollow(FOLLOW_powLevelExpr_in_multLevelExpr311);
            	    r=powLevelExpr();

            	    state._fsp--;

            	     
            	    	    	  	value = new BinaryOperator( value, r, "*" ) {
            	    				public Double evaluate() throws ExpressionException {
            	    					return l.evaluate() * r.evaluate();
            	    				}
            	    			};
            	    	    	

            	    }
            	    break;
            	case 2 :
            	    // F:\\My Documents\\Documents\\Coding\\NeuralNetwork\\antlr\\ExpressionAST.g:84:9: DIV r= powLevelExpr
            	    {
            	    match(input,DIV,FOLLOW_DIV_in_multLevelExpr323); 
            	    pushFollow(FOLLOW_powLevelExpr_in_multLevelExpr327);
            	    r=powLevelExpr();

            	    state._fsp--;

            	     
            	    	    		value = new BinaryOperator( value, r, "/" ) {
            	    				public Double evaluate() throws ExpressionException {
            	    					return l.evaluate() / r.evaluate();
            	    				}
            	    			};
            	    	    	

            	    }
            	    break;
            	case 3 :
            	    // F:\\My Documents\\Documents\\Coding\\NeuralNetwork\\antlr\\ExpressionAST.g:91:9: MOD r= powLevelExpr
            	    {
            	    match(input,MOD,FOLLOW_MOD_in_multLevelExpr339); 
            	    pushFollow(FOLLOW_powLevelExpr_in_multLevelExpr343);
            	    r=powLevelExpr();

            	    state._fsp--;

            	     
            	    	    		value = new BinaryOperator( value, r, "%" ) {
            	    				public Double evaluate() throws ExpressionException {
            	    					return l.evaluate() % r.evaluate();
            	    				}
            	    			};
            	    	    	

            	    }
            	    break;

            	default :
            	    break loop2;
                }
            } while (true);


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return value;
    }
    // $ANTLR end "multLevelExpr"


    // $ANTLR start "powLevelExpr"
    // F:\\My Documents\\Documents\\Coding\\NeuralNetwork\\antlr\\ExpressionAST.g:101:1: powLevelExpr returns [Component value] : l= unary ( POW r= unary )* ;
    public final Component powLevelExpr() throws RecognitionException {
        Component value = null;

        Component l = null;

        Component r = null;


        try {
            // F:\\My Documents\\Documents\\Coding\\NeuralNetwork\\antlr\\ExpressionAST.g:102:2: (l= unary ( POW r= unary )* )
            // F:\\My Documents\\Documents\\Coding\\NeuralNetwork\\antlr\\ExpressionAST.g:102:5: l= unary ( POW r= unary )*
            {
            pushFollow(FOLLOW_unary_in_powLevelExpr372);
            l=unary();

            state._fsp--;

             value = l; 
            // F:\\My Documents\\Documents\\Coding\\NeuralNetwork\\antlr\\ExpressionAST.g:103:3: ( POW r= unary )*
            loop3:
            do {
                int alt3=2;
                int LA3_0 = input.LA(1);

                if ( (LA3_0==POW) ) {
                    alt3=1;
                }


                switch (alt3) {
            	case 1 :
            	    // F:\\My Documents\\Documents\\Coding\\NeuralNetwork\\antlr\\ExpressionAST.g:103:5: POW r= unary
            	    {
            	    match(input,POW,FOLLOW_POW_in_powLevelExpr380); 
            	    pushFollow(FOLLOW_unary_in_powLevelExpr384);
            	    r=unary();

            	    state._fsp--;

            	     
            	    			value = new BinaryOperator( value, r, "^" ) {
            	    				public Double evaluate() throws ExpressionException {
            	    					return Math.pow( l.evaluate(), r.evaluate() );
            	    				}
            	    			}; 
            	    		

            	    }
            	    break;

            	default :
            	    break loop3;
                }
            } while (true);


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return value;
    }
    // $ANTLR end "powLevelExpr"


    // $ANTLR start "unary"
    // F:\\My Documents\\Documents\\Coding\\NeuralNetwork\\antlr\\ExpressionAST.g:113:1: unary returns [Component value] : (e= atom | MINUS e= atom );
    public final Component unary() throws RecognitionException {
        Component value = null;

        Component e = null;


        try {
            // F:\\My Documents\\Documents\\Coding\\NeuralNetwork\\antlr\\ExpressionAST.g:114:2: (e= atom | MINUS e= atom )
            int alt4=2;
            int LA4_0 = input.LA(1);

            if ( (LA4_0==LPAREN||(LA4_0>=SQRT && LA4_0<=TANH)||(LA4_0>=INT && LA4_0<=VAR)) ) {
                alt4=1;
            }
            else if ( (LA4_0==MINUS) ) {
                alt4=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 4, 0, input);

                throw nvae;
            }
            switch (alt4) {
                case 1 :
                    // F:\\My Documents\\Documents\\Coding\\NeuralNetwork\\antlr\\ExpressionAST.g:114:4: e= atom
                    {
                    pushFollow(FOLLOW_atom_in_unary408);
                    e=atom();

                    state._fsp--;

                     value = e; 

                    }
                    break;
                case 2 :
                    // F:\\My Documents\\Documents\\Coding\\NeuralNetwork\\antlr\\ExpressionAST.g:115:4: MINUS e= atom
                    {
                    match(input,MINUS,FOLLOW_MINUS_in_unary415); 
                    pushFollow(FOLLOW_atom_in_unary419);
                    e=atom();

                    state._fsp--;

                     
                    			value = new UnaryOperator( e, "-" ) {
                    				public Double evaluate() throws ExpressionException {
                    					return -c.evaluate();
                    				}
                    			}; 
                    		

                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return value;
    }
    // $ANTLR end "unary"


    // $ANTLR start "atom"
    // F:\\My Documents\\Documents\\Coding\\NeuralNetwork\\antlr\\ExpressionAST.g:124:1: atom returns [Component value] : ( INT | DOUBLE | VAR | LPAREN lowLevelExpr RPAREN | SQRT LPAREN e= lowLevelExpr RPAREN | RAND | GRAND | SINH LPAREN e= lowLevelExpr RPAREN | COSH LPAREN e= lowLevelExpr RPAREN | TANH LPAREN e= lowLevelExpr RPAREN | SIN LPAREN e= lowLevelExpr RPAREN | COS LPAREN e= lowLevelExpr RPAREN | TAN LPAREN e= lowLevelExpr RPAREN );
    public final Component atom() throws RecognitionException {
        Component value = null;

        Token INT2=null;
        Token DOUBLE3=null;
        Token VAR4=null;
        Component e = null;

        Component lowLevelExpr5 = null;


        try {
            // F:\\My Documents\\Documents\\Coding\\NeuralNetwork\\antlr\\ExpressionAST.g:125:2: ( INT | DOUBLE | VAR | LPAREN lowLevelExpr RPAREN | SQRT LPAREN e= lowLevelExpr RPAREN | RAND | GRAND | SINH LPAREN e= lowLevelExpr RPAREN | COSH LPAREN e= lowLevelExpr RPAREN | TANH LPAREN e= lowLevelExpr RPAREN | SIN LPAREN e= lowLevelExpr RPAREN | COS LPAREN e= lowLevelExpr RPAREN | TAN LPAREN e= lowLevelExpr RPAREN )
            int alt5=13;
            switch ( input.LA(1) ) {
            case INT:
                {
                alt5=1;
                }
                break;
            case DOUBLE:
                {
                alt5=2;
                }
                break;
            case VAR:
                {
                alt5=3;
                }
                break;
            case LPAREN:
                {
                alt5=4;
                }
                break;
            case SQRT:
                {
                alt5=5;
                }
                break;
            case RAND:
                {
                alt5=6;
                }
                break;
            case GRAND:
                {
                alt5=7;
                }
                break;
            case SINH:
                {
                alt5=8;
                }
                break;
            case COSH:
                {
                alt5=9;
                }
                break;
            case TANH:
                {
                alt5=10;
                }
                break;
            case SIN:
                {
                alt5=11;
                }
                break;
            case COS:
                {
                alt5=12;
                }
                break;
            case TAN:
                {
                alt5=13;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 5, 0, input);

                throw nvae;
            }

            switch (alt5) {
                case 1 :
                    // F:\\My Documents\\Documents\\Coding\\NeuralNetwork\\antlr\\ExpressionAST.g:125:7: INT
                    {
                    INT2=(Token)match(input,INT,FOLLOW_INT_in_atom440); 
                     value = new Literal( (INT2!=null?INT2.getText():null) ); 

                    }
                    break;
                case 2 :
                    // F:\\My Documents\\Documents\\Coding\\NeuralNetwork\\antlr\\ExpressionAST.g:126:4: DOUBLE
                    {
                    DOUBLE3=(Token)match(input,DOUBLE,FOLLOW_DOUBLE_in_atom447); 
                     value = new Literal( (DOUBLE3!=null?DOUBLE3.getText():null) ); 

                    }
                    break;
                case 3 :
                    // F:\\My Documents\\Documents\\Coding\\NeuralNetwork\\antlr\\ExpressionAST.g:127:4: VAR
                    {
                    VAR4=(Token)match(input,VAR,FOLLOW_VAR_in_atom454); 
                     
                    			value = new Variable( (VAR4!=null?VAR4.getText():null) ); 
                    			vars.put( (VAR4!=null?VAR4.getText():null), (Variable)value );
                    		

                    }
                    break;
                case 4 :
                    // F:\\My Documents\\Documents\\Coding\\NeuralNetwork\\antlr\\ExpressionAST.g:131:7: LPAREN lowLevelExpr RPAREN
                    {
                    match(input,LPAREN,FOLLOW_LPAREN_in_atom464); 
                    pushFollow(FOLLOW_lowLevelExpr_in_atom466);
                    lowLevelExpr5=lowLevelExpr();

                    state._fsp--;

                    match(input,RPAREN,FOLLOW_RPAREN_in_atom468); 
                     value = lowLevelExpr5; 

                    }
                    break;
                case 5 :
                    // F:\\My Documents\\Documents\\Coding\\NeuralNetwork\\antlr\\ExpressionAST.g:132:4: SQRT LPAREN e= lowLevelExpr RPAREN
                    {
                    match(input,SQRT,FOLLOW_SQRT_in_atom475); 
                    match(input,LPAREN,FOLLOW_LPAREN_in_atom477); 
                    pushFollow(FOLLOW_lowLevelExpr_in_atom481);
                    e=lowLevelExpr();

                    state._fsp--;

                     
                    			value = new UnaryOperator( e, "SQRT" ) {
                    				public Double evaluate() throws ExpressionException {
                    					return Math.sqrt( c.evaluate() );
                    				}
                    			}; 
                    		
                    match(input,RPAREN,FOLLOW_RPAREN_in_atom484); 

                    }
                    break;
                case 6 :
                    // F:\\My Documents\\Documents\\Coding\\NeuralNetwork\\antlr\\ExpressionAST.g:139:5: RAND
                    {
                    match(input,RAND,FOLLOW_RAND_in_atom490); 
                     
                    			value = new NullaryOperator( "RAND" ) {
                    				public Double evaluate() throws ExpressionException {
                    					return r.nextDouble(); 
                    				}
                    								
                    				public Set<Component> getVariables() {
                    					variables.add( this );
                    					return super.getVariables();
                    				}
                    			}; 
                    		

                    }
                    break;
                case 7 :
                    // F:\\My Documents\\Documents\\Coding\\NeuralNetwork\\antlr\\ExpressionAST.g:151:5: GRAND
                    {
                    match(input,GRAND,FOLLOW_GRAND_in_atom498); 
                     
                    			value = new NullaryOperator( "GRAND" ) {
                    				public Double evaluate() throws ExpressionException {
                    					return r.nextGaussian(); 
                    				}
                    				
                    				public Set<Component> getVariables() {
                    					variables.add( this );
                    					return super.getVariables();
                    				}
                    			}; 
                    		

                    }
                    break;
                case 8 :
                    // F:\\My Documents\\Documents\\Coding\\NeuralNetwork\\antlr\\ExpressionAST.g:163:4: SINH LPAREN e= lowLevelExpr RPAREN
                    {
                    match(input,SINH,FOLLOW_SINH_in_atom505); 
                    match(input,LPAREN,FOLLOW_LPAREN_in_atom507); 
                    pushFollow(FOLLOW_lowLevelExpr_in_atom511);
                    e=lowLevelExpr();

                    state._fsp--;

                     
                    			value = new UnaryOperator( e, "SINH" ) {
                    				public Double evaluate() throws ExpressionException {
                    					return Math.sinh( c.evaluate() );
                    				}
                    			}; 
                    		
                    match(input,RPAREN,FOLLOW_RPAREN_in_atom514); 

                    }
                    break;
                case 9 :
                    // F:\\My Documents\\Documents\\Coding\\NeuralNetwork\\antlr\\ExpressionAST.g:170:4: COSH LPAREN e= lowLevelExpr RPAREN
                    {
                    match(input,COSH,FOLLOW_COSH_in_atom519); 
                    match(input,LPAREN,FOLLOW_LPAREN_in_atom521); 
                    pushFollow(FOLLOW_lowLevelExpr_in_atom525);
                    e=lowLevelExpr();

                    state._fsp--;

                     
                    			value = new UnaryOperator( e, "COSH" ) {
                    				public Double evaluate() throws ExpressionException {
                    					return Math.cosh( c.evaluate() );
                    				}
                    			};
                    		
                    match(input,RPAREN,FOLLOW_RPAREN_in_atom528); 

                    }
                    break;
                case 10 :
                    // F:\\My Documents\\Documents\\Coding\\NeuralNetwork\\antlr\\ExpressionAST.g:177:4: TANH LPAREN e= lowLevelExpr RPAREN
                    {
                    match(input,TANH,FOLLOW_TANH_in_atom533); 
                    match(input,LPAREN,FOLLOW_LPAREN_in_atom535); 
                    pushFollow(FOLLOW_lowLevelExpr_in_atom539);
                    e=lowLevelExpr();

                    state._fsp--;

                     
                    			value = new UnaryOperator( e, "TANH" ) {
                    				public Double evaluate() throws ExpressionException {
                    					return Math.tanh( c.evaluate() );
                    									}
                    							};
                    									
                    match(input,RPAREN,FOLLOW_RPAREN_in_atom542); 

                    }
                    break;
                case 11 :
                    // F:\\My Documents\\Documents\\Coding\\NeuralNetwork\\antlr\\ExpressionAST.g:184:4: SIN LPAREN e= lowLevelExpr RPAREN
                    {
                    match(input,SIN,FOLLOW_SIN_in_atom547); 
                    match(input,LPAREN,FOLLOW_LPAREN_in_atom549); 
                    pushFollow(FOLLOW_lowLevelExpr_in_atom553);
                    e=lowLevelExpr();

                    state._fsp--;

                     
                    			value = new UnaryOperator( e, "SIN" ) {
                    				public Double evaluate() throws ExpressionException {
                    					return Math.sin( c.evaluate() );
                    				}
                    			};
                    		
                    match(input,RPAREN,FOLLOW_RPAREN_in_atom556); 

                    }
                    break;
                case 12 :
                    // F:\\My Documents\\Documents\\Coding\\NeuralNetwork\\antlr\\ExpressionAST.g:191:4: COS LPAREN e= lowLevelExpr RPAREN
                    {
                    match(input,COS,FOLLOW_COS_in_atom561); 
                    match(input,LPAREN,FOLLOW_LPAREN_in_atom563); 
                    pushFollow(FOLLOW_lowLevelExpr_in_atom567);
                    e=lowLevelExpr();

                    state._fsp--;

                     
                    			value = new UnaryOperator( e, "COS" ) {
                    				public Double evaluate() throws ExpressionException {
                    					return Math.cos( c.evaluate() );
                    				}
                    			};
                    		
                    match(input,RPAREN,FOLLOW_RPAREN_in_atom570); 

                    }
                    break;
                case 13 :
                    // F:\\My Documents\\Documents\\Coding\\NeuralNetwork\\antlr\\ExpressionAST.g:198:4: TAN LPAREN e= lowLevelExpr RPAREN
                    {
                    match(input,TAN,FOLLOW_TAN_in_atom575); 
                    match(input,LPAREN,FOLLOW_LPAREN_in_atom577); 
                    pushFollow(FOLLOW_lowLevelExpr_in_atom581);
                    e=lowLevelExpr();

                    state._fsp--;

                     
                    			value = new UnaryOperator( e, "TAN" ) {
                    				public Double evaluate() throws ExpressionException {
                    					return Math.tan( c.evaluate() );
                    				}
                    			};
                    		
                    match(input,RPAREN,FOLLOW_RPAREN_in_atom584); 

                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return value;
    }
    // $ANTLR end "atom"

    // Delegated rules


 

    public static final BitSet FOLLOW_lowLevelExpr_in_getTree199 = new BitSet(new long[]{0x0000000000200000L});
    public static final BitSet FOLLOW_NEWLINE_in_getTree201 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_multLevelExpr_in_lowLevelExpr223 = new BitSet(new long[]{0x0000000000000032L});
    public static final BitSet FOLLOW_PLUS_in_lowLevelExpr238 = new BitSet(new long[]{0x0000000001DFF220L});
    public static final BitSet FOLLOW_multLevelExpr_in_lowLevelExpr242 = new BitSet(new long[]{0x0000000000000032L});
    public static final BitSet FOLLOW_MINUS_in_lowLevelExpr257 = new BitSet(new long[]{0x0000000001DFF220L});
    public static final BitSet FOLLOW_multLevelExpr_in_lowLevelExpr261 = new BitSet(new long[]{0x0000000000000032L});
    public static final BitSet FOLLOW_powLevelExpr_in_multLevelExpr295 = new BitSet(new long[]{0x00000000000001C2L});
    public static final BitSet FOLLOW_MULT_in_multLevelExpr307 = new BitSet(new long[]{0x0000000001DFF220L});
    public static final BitSet FOLLOW_powLevelExpr_in_multLevelExpr311 = new BitSet(new long[]{0x00000000000001C2L});
    public static final BitSet FOLLOW_DIV_in_multLevelExpr323 = new BitSet(new long[]{0x0000000001DFF220L});
    public static final BitSet FOLLOW_powLevelExpr_in_multLevelExpr327 = new BitSet(new long[]{0x00000000000001C2L});
    public static final BitSet FOLLOW_MOD_in_multLevelExpr339 = new BitSet(new long[]{0x0000000001DFF220L});
    public static final BitSet FOLLOW_powLevelExpr_in_multLevelExpr343 = new BitSet(new long[]{0x00000000000001C2L});
    public static final BitSet FOLLOW_unary_in_powLevelExpr372 = new BitSet(new long[]{0x0000000000000802L});
    public static final BitSet FOLLOW_POW_in_powLevelExpr380 = new BitSet(new long[]{0x0000000001DFF220L});
    public static final BitSet FOLLOW_unary_in_powLevelExpr384 = new BitSet(new long[]{0x0000000000000802L});
    public static final BitSet FOLLOW_atom_in_unary408 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_MINUS_in_unary415 = new BitSet(new long[]{0x0000000001DFF200L});
    public static final BitSet FOLLOW_atom_in_unary419 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_INT_in_atom440 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_DOUBLE_in_atom447 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_VAR_in_atom454 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LPAREN_in_atom464 = new BitSet(new long[]{0x0000000001DFF220L});
    public static final BitSet FOLLOW_lowLevelExpr_in_atom466 = new BitSet(new long[]{0x0000000000000400L});
    public static final BitSet FOLLOW_RPAREN_in_atom468 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_SQRT_in_atom475 = new BitSet(new long[]{0x0000000000000200L});
    public static final BitSet FOLLOW_LPAREN_in_atom477 = new BitSet(new long[]{0x0000000001DFF220L});
    public static final BitSet FOLLOW_lowLevelExpr_in_atom481 = new BitSet(new long[]{0x0000000000000400L});
    public static final BitSet FOLLOW_RPAREN_in_atom484 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_RAND_in_atom490 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_GRAND_in_atom498 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_SINH_in_atom505 = new BitSet(new long[]{0x0000000000000200L});
    public static final BitSet FOLLOW_LPAREN_in_atom507 = new BitSet(new long[]{0x0000000001DFF220L});
    public static final BitSet FOLLOW_lowLevelExpr_in_atom511 = new BitSet(new long[]{0x0000000000000400L});
    public static final BitSet FOLLOW_RPAREN_in_atom514 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_COSH_in_atom519 = new BitSet(new long[]{0x0000000000000200L});
    public static final BitSet FOLLOW_LPAREN_in_atom521 = new BitSet(new long[]{0x0000000001DFF220L});
    public static final BitSet FOLLOW_lowLevelExpr_in_atom525 = new BitSet(new long[]{0x0000000000000400L});
    public static final BitSet FOLLOW_RPAREN_in_atom528 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_TANH_in_atom533 = new BitSet(new long[]{0x0000000000000200L});
    public static final BitSet FOLLOW_LPAREN_in_atom535 = new BitSet(new long[]{0x0000000001DFF220L});
    public static final BitSet FOLLOW_lowLevelExpr_in_atom539 = new BitSet(new long[]{0x0000000000000400L});
    public static final BitSet FOLLOW_RPAREN_in_atom542 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_SIN_in_atom547 = new BitSet(new long[]{0x0000000000000200L});
    public static final BitSet FOLLOW_LPAREN_in_atom549 = new BitSet(new long[]{0x0000000001DFF220L});
    public static final BitSet FOLLOW_lowLevelExpr_in_atom553 = new BitSet(new long[]{0x0000000000000400L});
    public static final BitSet FOLLOW_RPAREN_in_atom556 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_COS_in_atom561 = new BitSet(new long[]{0x0000000000000200L});
    public static final BitSet FOLLOW_LPAREN_in_atom563 = new BitSet(new long[]{0x0000000001DFF220L});
    public static final BitSet FOLLOW_lowLevelExpr_in_atom567 = new BitSet(new long[]{0x0000000000000400L});
    public static final BitSet FOLLOW_RPAREN_in_atom570 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_TAN_in_atom575 = new BitSet(new long[]{0x0000000000000200L});
    public static final BitSet FOLLOW_LPAREN_in_atom577 = new BitSet(new long[]{0x0000000001DFF220L});
    public static final BitSet FOLLOW_lowLevelExpr_in_atom581 = new BitSet(new long[]{0x0000000000000400L});
    public static final BitSet FOLLOW_RPAREN_in_atom584 = new BitSet(new long[]{0x0000000000000002L});

}