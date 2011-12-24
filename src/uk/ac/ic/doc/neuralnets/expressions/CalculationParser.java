// $ANTLR 3.1.1 C:\\Users\\Peter Coetzee\\Coding\\NeuralNetwork\\antlr\\Calculation.g 2008-11-24 15:57:46

package uk.ac.ic.doc.neuralnets.expressions;

import java.util.Random;
import java.util.Map;
import java.util.HashMap;


import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

public class CalculationParser extends Parser {
    public static final String[] tokenNames = new String[] {
        "<invalid>", "<EOR>", "<DOWN>", "<UP>", "PLUS", "MINUS", "MULT", "DIV", "MOD", "LPAREN", "RPAREN", "POW", "RAND", "GRAND", "SIN", "COS", "TAN", "SINH", "COSH", "TANH", "NEWLINE", "INT", "VAR", "DOUBLE", "WS"
    };
    public static final int MOD=8;
    public static final int INT=21;
    public static final int GRAND=13;
    public static final int COSH=18;
    public static final int MULT=6;
    public static final int MINUS=5;
    public static final int EOF=-1;
    public static final int SINH=17;
    public static final int LPAREN=9;
    public static final int RPAREN=10;
    public static final int TANH=19;
    public static final int WS=24;
    public static final int POW=11;
    public static final int NEWLINE=20;
    public static final int SIN=14;
    public static final int COS=15;
    public static final int RAND=12;
    public static final int TAN=16;
    public static final int DOUBLE=23;
    public static final int PLUS=4;
    public static final int VAR=22;
    public static final int DIV=7;

    // delegates
    // delegators


        public CalculationParser(TokenStream input) {
            this(input, new RecognizerSharedState());
        }
        public CalculationParser(TokenStream input, RecognizerSharedState state) {
            super(input, state);
             
        }
        

    public String[] getTokenNames() { return CalculationParser.tokenNames; }
    public String getGrammarFileName() { return "C:\\Users\\Peter Coetzee\\Coding\\NeuralNetwork\\antlr\\Calculation.g"; }


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



    // $ANTLR start "stat"
    // C:\\Users\\Peter Coetzee\\Coding\\NeuralNetwork\\antlr\\Calculation.g:70:1: stat returns [Double value] : lowLevelExpr NEWLINE ;
    public final Double stat() throws RecognitionException {
        Double value = null;

        Double lowLevelExpr1 = null;


        try {
            // C:\\Users\\Peter Coetzee\\Coding\\NeuralNetwork\\antlr\\Calculation.g:71:2: ( lowLevelExpr NEWLINE )
            // C:\\Users\\Peter Coetzee\\Coding\\NeuralNetwork\\antlr\\Calculation.g:71:6: lowLevelExpr NEWLINE
            {
            pushFollow(FOLLOW_lowLevelExpr_in_stat191);
            lowLevelExpr1=lowLevelExpr();

            state._fsp--;

            match(input,NEWLINE,FOLLOW_NEWLINE_in_stat193); 
             value = lowLevelExpr1; 

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
    // $ANTLR end "stat"


    // $ANTLR start "lowLevelExpr"
    // C:\\Users\\Peter Coetzee\\Coding\\NeuralNetwork\\antlr\\Calculation.g:74:1: lowLevelExpr returns [Double value] : e= multLevelExpr ( PLUS e= multLevelExpr | MINUS e= multLevelExpr )* ;
    public final Double lowLevelExpr() throws RecognitionException {
        Double value = null;

        Double e = null;


        try {
            // C:\\Users\\Peter Coetzee\\Coding\\NeuralNetwork\\antlr\\Calculation.g:75:5: (e= multLevelExpr ( PLUS e= multLevelExpr | MINUS e= multLevelExpr )* )
            // C:\\Users\\Peter Coetzee\\Coding\\NeuralNetwork\\antlr\\Calculation.g:75:9: e= multLevelExpr ( PLUS e= multLevelExpr | MINUS e= multLevelExpr )*
            {
            pushFollow(FOLLOW_multLevelExpr_in_lowLevelExpr220);
            e=multLevelExpr();

            state._fsp--;

             value = e; 
            // C:\\Users\\Peter Coetzee\\Coding\\NeuralNetwork\\antlr\\Calculation.g:76:9: ( PLUS e= multLevelExpr | MINUS e= multLevelExpr )*
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
            	    // C:\\Users\\Peter Coetzee\\Coding\\NeuralNetwork\\antlr\\Calculation.g:76:11: PLUS e= multLevelExpr
            	    {
            	    match(input,PLUS,FOLLOW_PLUS_in_lowLevelExpr234); 
            	    pushFollow(FOLLOW_multLevelExpr_in_lowLevelExpr238);
            	    e=multLevelExpr();

            	    state._fsp--;

            	     value = value + e; 

            	    }
            	    break;
            	case 2 :
            	    // C:\\Users\\Peter Coetzee\\Coding\\NeuralNetwork\\antlr\\Calculation.g:77:11: MINUS e= multLevelExpr
            	    {
            	    match(input,MINUS,FOLLOW_MINUS_in_lowLevelExpr252); 
            	    pushFollow(FOLLOW_multLevelExpr_in_lowLevelExpr256);
            	    e=multLevelExpr();

            	    state._fsp--;

            	     value = value - e; 

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
    // C:\\Users\\Peter Coetzee\\Coding\\NeuralNetwork\\antlr\\Calculation.g:81:1: multLevelExpr returns [Double value] : e= powLevelExpr ( MULT e= powLevelExpr | DIV e= powLevelExpr | MOD e= powLevelExpr )* ;
    public final Double multLevelExpr() throws RecognitionException {
        Double value = null;

        Double e = null;


        try {
            // C:\\Users\\Peter Coetzee\\Coding\\NeuralNetwork\\antlr\\Calculation.g:82:5: (e= powLevelExpr ( MULT e= powLevelExpr | DIV e= powLevelExpr | MOD e= powLevelExpr )* )
            // C:\\Users\\Peter Coetzee\\Coding\\NeuralNetwork\\antlr\\Calculation.g:82:9: e= powLevelExpr ( MULT e= powLevelExpr | DIV e= powLevelExpr | MOD e= powLevelExpr )*
            {
            pushFollow(FOLLOW_powLevelExpr_in_multLevelExpr294);
            e=powLevelExpr();

            state._fsp--;

             value = e; 
            // C:\\Users\\Peter Coetzee\\Coding\\NeuralNetwork\\antlr\\Calculation.g:83:6: ( MULT e= powLevelExpr | DIV e= powLevelExpr | MOD e= powLevelExpr )*
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
            	    // C:\\Users\\Peter Coetzee\\Coding\\NeuralNetwork\\antlr\\Calculation.g:84:8: MULT e= powLevelExpr
            	    {
            	    match(input,MULT,FOLLOW_MULT_in_multLevelExpr314); 
            	    pushFollow(FOLLOW_powLevelExpr_in_multLevelExpr318);
            	    e=powLevelExpr();

            	    state._fsp--;

            	     value = value * e; 

            	    }
            	    break;
            	case 2 :
            	    // C:\\Users\\Peter Coetzee\\Coding\\NeuralNetwork\\antlr\\Calculation.g:85:8: DIV e= powLevelExpr
            	    {
            	    match(input,DIV,FOLLOW_DIV_in_multLevelExpr329); 
            	    pushFollow(FOLLOW_powLevelExpr_in_multLevelExpr333);
            	    e=powLevelExpr();

            	    state._fsp--;

            	     value = value / e; 

            	    }
            	    break;
            	case 3 :
            	    // C:\\Users\\Peter Coetzee\\Coding\\NeuralNetwork\\antlr\\Calculation.g:86:8: MOD e= powLevelExpr
            	    {
            	    match(input,MOD,FOLLOW_MOD_in_multLevelExpr344); 
            	    pushFollow(FOLLOW_powLevelExpr_in_multLevelExpr348);
            	    e=powLevelExpr();

            	    state._fsp--;

            	     value = value % e; 

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
    // C:\\Users\\Peter Coetzee\\Coding\\NeuralNetwork\\antlr\\Calculation.g:90:1: powLevelExpr returns [Double value] : e= unary ( POW e= unary )* ;
    public final Double powLevelExpr() throws RecognitionException {
        Double value = null;

        Double e = null;


        try {
            // C:\\Users\\Peter Coetzee\\Coding\\NeuralNetwork\\antlr\\Calculation.g:91:2: (e= unary ( POW e= unary )* )
            // C:\\Users\\Peter Coetzee\\Coding\\NeuralNetwork\\antlr\\Calculation.g:91:4: e= unary ( POW e= unary )*
            {
            pushFollow(FOLLOW_unary_in_powLevelExpr378);
            e=unary();

            state._fsp--;

             value = e; 
            // C:\\Users\\Peter Coetzee\\Coding\\NeuralNetwork\\antlr\\Calculation.g:92:3: ( POW e= unary )*
            loop3:
            do {
                int alt3=2;
                int LA3_0 = input.LA(1);

                if ( (LA3_0==POW) ) {
                    alt3=1;
                }


                switch (alt3) {
            	case 1 :
            	    // C:\\Users\\Peter Coetzee\\Coding\\NeuralNetwork\\antlr\\Calculation.g:92:5: POW e= unary
            	    {
            	    match(input,POW,FOLLOW_POW_in_powLevelExpr386); 
            	    pushFollow(FOLLOW_unary_in_powLevelExpr390);
            	    e=unary();

            	    state._fsp--;

            	     value = Math.pow( value, e ); 

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
    // C:\\Users\\Peter Coetzee\\Coding\\NeuralNetwork\\antlr\\Calculation.g:96:1: unary returns [Double value] : (e= atom | MINUS e= atom );
    public final Double unary() throws RecognitionException {
        Double value = null;

        Double e = null;


        try {
            // C:\\Users\\Peter Coetzee\\Coding\\NeuralNetwork\\antlr\\Calculation.g:97:2: (e= atom | MINUS e= atom )
            int alt4=2;
            int LA4_0 = input.LA(1);

            if ( (LA4_0==LPAREN||(LA4_0>=RAND && LA4_0<=TANH)||(LA4_0>=INT && LA4_0<=DOUBLE)) ) {
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
                    // C:\\Users\\Peter Coetzee\\Coding\\NeuralNetwork\\antlr\\Calculation.g:97:4: e= atom
                    {
                    pushFollow(FOLLOW_atom_in_unary414);
                    e=atom();

                    state._fsp--;

                     value = e; 

                    }
                    break;
                case 2 :
                    // C:\\Users\\Peter Coetzee\\Coding\\NeuralNetwork\\antlr\\Calculation.g:98:4: MINUS e= atom
                    {
                    match(input,MINUS,FOLLOW_MINUS_in_unary421); 
                    pushFollow(FOLLOW_atom_in_unary425);
                    e=atom();

                    state._fsp--;

                     value = -e; 

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
    // C:\\Users\\Peter Coetzee\\Coding\\NeuralNetwork\\antlr\\Calculation.g:101:1: atom returns [Double value] : ( INT | VAR | DOUBLE | RAND | GRAND | LPAREN lowLevelExpr RPAREN | SINH LPAREN e= lowLevelExpr RPAREN | COSH LPAREN e= lowLevelExpr RPAREN | TANH LPAREN e= lowLevelExpr RPAREN | SIN LPAREN e= lowLevelExpr RPAREN | COS LPAREN e= lowLevelExpr RPAREN | TAN LPAREN e= lowLevelExpr RPAREN );
    public final Double atom() throws RecognitionException {
        Double value = null;

        Token INT2=null;
        Token VAR3=null;
        Token DOUBLE4=null;
        Double e = null;

        Double lowLevelExpr5 = null;


        try {
            // C:\\Users\\Peter Coetzee\\Coding\\NeuralNetwork\\antlr\\Calculation.g:102:2: ( INT | VAR | DOUBLE | RAND | GRAND | LPAREN lowLevelExpr RPAREN | SINH LPAREN e= lowLevelExpr RPAREN | COSH LPAREN e= lowLevelExpr RPAREN | TANH LPAREN e= lowLevelExpr RPAREN | SIN LPAREN e= lowLevelExpr RPAREN | COS LPAREN e= lowLevelExpr RPAREN | TAN LPAREN e= lowLevelExpr RPAREN )
            int alt5=12;
            switch ( input.LA(1) ) {
            case INT:
                {
                alt5=1;
                }
                break;
            case VAR:
                {
                alt5=2;
                }
                break;
            case DOUBLE:
                {
                alt5=3;
                }
                break;
            case RAND:
                {
                alt5=4;
                }
                break;
            case GRAND:
                {
                alt5=5;
                }
                break;
            case LPAREN:
                {
                alt5=6;
                }
                break;
            case SINH:
                {
                alt5=7;
                }
                break;
            case COSH:
                {
                alt5=8;
                }
                break;
            case TANH:
                {
                alt5=9;
                }
                break;
            case SIN:
                {
                alt5=10;
                }
                break;
            case COS:
                {
                alt5=11;
                }
                break;
            case TAN:
                {
                alt5=12;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 5, 0, input);

                throw nvae;
            }

            switch (alt5) {
                case 1 :
                    // C:\\Users\\Peter Coetzee\\Coding\\NeuralNetwork\\antlr\\Calculation.g:102:7: INT
                    {
                    INT2=(Token)match(input,INT,FOLLOW_INT_in_atom446); 
                     value = Double.parseDouble( (INT2!=null?INT2.getText():null) ); 

                    }
                    break;
                case 2 :
                    // C:\\Users\\Peter Coetzee\\Coding\\NeuralNetwork\\antlr\\Calculation.g:103:4: VAR
                    {
                    VAR3=(Token)match(input,VAR,FOLLOW_VAR_in_atom453); 
                     if ( !bindings.containsKey( (VAR3!=null?VAR3.getText():null) ) ) {
                    			errors.add( "No binding for '" + (VAR3!=null?VAR3.getText():null) + "'" );
                    			value = new Double( 0 );
                    		} else {
                    			value = bindings.get( (VAR3!=null?VAR3.getText():null) );
                    		}
                    	

                    }
                    break;
                case 3 :
                    // C:\\Users\\Peter Coetzee\\Coding\\NeuralNetwork\\antlr\\Calculation.g:110:4: DOUBLE
                    {
                    DOUBLE4=(Token)match(input,DOUBLE,FOLLOW_DOUBLE_in_atom460); 
                     value = Double.parseDouble( (DOUBLE4!=null?DOUBLE4.getText():null) ); 

                    }
                    break;
                case 4 :
                    // C:\\Users\\Peter Coetzee\\Coding\\NeuralNetwork\\antlr\\Calculation.g:111:5: RAND
                    {
                    match(input,RAND,FOLLOW_RAND_in_atom468); 
                     value = r.nextDouble(); 

                    }
                    break;
                case 5 :
                    // C:\\Users\\Peter Coetzee\\Coding\\NeuralNetwork\\antlr\\Calculation.g:112:5: GRAND
                    {
                    match(input,GRAND,FOLLOW_GRAND_in_atom476); 
                     value = r.nextGaussian(); 

                    }
                    break;
                case 6 :
                    // C:\\Users\\Peter Coetzee\\Coding\\NeuralNetwork\\antlr\\Calculation.g:113:7: LPAREN lowLevelExpr RPAREN
                    {
                    match(input,LPAREN,FOLLOW_LPAREN_in_atom486); 
                    pushFollow(FOLLOW_lowLevelExpr_in_atom488);
                    lowLevelExpr5=lowLevelExpr();

                    state._fsp--;

                    match(input,RPAREN,FOLLOW_RPAREN_in_atom490); 
                     value = lowLevelExpr5; 

                    }
                    break;
                case 7 :
                    // C:\\Users\\Peter Coetzee\\Coding\\NeuralNetwork\\antlr\\Calculation.g:114:4: SINH LPAREN e= lowLevelExpr RPAREN
                    {
                    match(input,SINH,FOLLOW_SINH_in_atom497); 
                    match(input,LPAREN,FOLLOW_LPAREN_in_atom499); 
                    pushFollow(FOLLOW_lowLevelExpr_in_atom503);
                    e=lowLevelExpr();

                    state._fsp--;

                     value = Math.sinh( e ); 
                    match(input,RPAREN,FOLLOW_RPAREN_in_atom506); 

                    }
                    break;
                case 8 :
                    // C:\\Users\\Peter Coetzee\\Coding\\NeuralNetwork\\antlr\\Calculation.g:115:4: COSH LPAREN e= lowLevelExpr RPAREN
                    {
                    match(input,COSH,FOLLOW_COSH_in_atom511); 
                    match(input,LPAREN,FOLLOW_LPAREN_in_atom513); 
                    pushFollow(FOLLOW_lowLevelExpr_in_atom517);
                    e=lowLevelExpr();

                    state._fsp--;

                     value = Math.cosh( e ); 
                    match(input,RPAREN,FOLLOW_RPAREN_in_atom520); 

                    }
                    break;
                case 9 :
                    // C:\\Users\\Peter Coetzee\\Coding\\NeuralNetwork\\antlr\\Calculation.g:116:4: TANH LPAREN e= lowLevelExpr RPAREN
                    {
                    match(input,TANH,FOLLOW_TANH_in_atom525); 
                    match(input,LPAREN,FOLLOW_LPAREN_in_atom527); 
                    pushFollow(FOLLOW_lowLevelExpr_in_atom531);
                    e=lowLevelExpr();

                    state._fsp--;

                     value = Math.tanh( e ); 
                    match(input,RPAREN,FOLLOW_RPAREN_in_atom534); 

                    }
                    break;
                case 10 :
                    // C:\\Users\\Peter Coetzee\\Coding\\NeuralNetwork\\antlr\\Calculation.g:117:4: SIN LPAREN e= lowLevelExpr RPAREN
                    {
                    match(input,SIN,FOLLOW_SIN_in_atom539); 
                    match(input,LPAREN,FOLLOW_LPAREN_in_atom541); 
                    pushFollow(FOLLOW_lowLevelExpr_in_atom545);
                    e=lowLevelExpr();

                    state._fsp--;

                     value = Math.sin( e ); 
                    match(input,RPAREN,FOLLOW_RPAREN_in_atom548); 

                    }
                    break;
                case 11 :
                    // C:\\Users\\Peter Coetzee\\Coding\\NeuralNetwork\\antlr\\Calculation.g:118:4: COS LPAREN e= lowLevelExpr RPAREN
                    {
                    match(input,COS,FOLLOW_COS_in_atom553); 
                    match(input,LPAREN,FOLLOW_LPAREN_in_atom555); 
                    pushFollow(FOLLOW_lowLevelExpr_in_atom559);
                    e=lowLevelExpr();

                    state._fsp--;

                     value = Math.cos( e ); 
                    match(input,RPAREN,FOLLOW_RPAREN_in_atom562); 

                    }
                    break;
                case 12 :
                    // C:\\Users\\Peter Coetzee\\Coding\\NeuralNetwork\\antlr\\Calculation.g:119:4: TAN LPAREN e= lowLevelExpr RPAREN
                    {
                    match(input,TAN,FOLLOW_TAN_in_atom567); 
                    match(input,LPAREN,FOLLOW_LPAREN_in_atom569); 
                    pushFollow(FOLLOW_lowLevelExpr_in_atom573);
                    e=lowLevelExpr();

                    state._fsp--;

                     value = Math.tan( e ); 
                    match(input,RPAREN,FOLLOW_RPAREN_in_atom576); 

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


 

    public static final BitSet FOLLOW_lowLevelExpr_in_stat191 = new BitSet(new long[]{0x0000000000100000L});
    public static final BitSet FOLLOW_NEWLINE_in_stat193 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_multLevelExpr_in_lowLevelExpr220 = new BitSet(new long[]{0x0000000000000032L});
    public static final BitSet FOLLOW_PLUS_in_lowLevelExpr234 = new BitSet(new long[]{0x0000000000EFF220L});
    public static final BitSet FOLLOW_multLevelExpr_in_lowLevelExpr238 = new BitSet(new long[]{0x0000000000000032L});
    public static final BitSet FOLLOW_MINUS_in_lowLevelExpr252 = new BitSet(new long[]{0x0000000000EFF220L});
    public static final BitSet FOLLOW_multLevelExpr_in_lowLevelExpr256 = new BitSet(new long[]{0x0000000000000032L});
    public static final BitSet FOLLOW_powLevelExpr_in_multLevelExpr294 = new BitSet(new long[]{0x00000000000001C2L});
    public static final BitSet FOLLOW_MULT_in_multLevelExpr314 = new BitSet(new long[]{0x0000000000EFF220L});
    public static final BitSet FOLLOW_powLevelExpr_in_multLevelExpr318 = new BitSet(new long[]{0x00000000000001C2L});
    public static final BitSet FOLLOW_DIV_in_multLevelExpr329 = new BitSet(new long[]{0x0000000000EFF220L});
    public static final BitSet FOLLOW_powLevelExpr_in_multLevelExpr333 = new BitSet(new long[]{0x00000000000001C2L});
    public static final BitSet FOLLOW_MOD_in_multLevelExpr344 = new BitSet(new long[]{0x0000000000EFF220L});
    public static final BitSet FOLLOW_powLevelExpr_in_multLevelExpr348 = new BitSet(new long[]{0x00000000000001C2L});
    public static final BitSet FOLLOW_unary_in_powLevelExpr378 = new BitSet(new long[]{0x0000000000000802L});
    public static final BitSet FOLLOW_POW_in_powLevelExpr386 = new BitSet(new long[]{0x0000000000EFF220L});
    public static final BitSet FOLLOW_unary_in_powLevelExpr390 = new BitSet(new long[]{0x0000000000000802L});
    public static final BitSet FOLLOW_atom_in_unary414 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_MINUS_in_unary421 = new BitSet(new long[]{0x0000000000EFF200L});
    public static final BitSet FOLLOW_atom_in_unary425 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_INT_in_atom446 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_VAR_in_atom453 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_DOUBLE_in_atom460 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_RAND_in_atom468 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_GRAND_in_atom476 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LPAREN_in_atom486 = new BitSet(new long[]{0x0000000000EFF220L});
    public static final BitSet FOLLOW_lowLevelExpr_in_atom488 = new BitSet(new long[]{0x0000000000000400L});
    public static final BitSet FOLLOW_RPAREN_in_atom490 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_SINH_in_atom497 = new BitSet(new long[]{0x0000000000000200L});
    public static final BitSet FOLLOW_LPAREN_in_atom499 = new BitSet(new long[]{0x0000000000EFF220L});
    public static final BitSet FOLLOW_lowLevelExpr_in_atom503 = new BitSet(new long[]{0x0000000000000400L});
    public static final BitSet FOLLOW_RPAREN_in_atom506 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_COSH_in_atom511 = new BitSet(new long[]{0x0000000000000200L});
    public static final BitSet FOLLOW_LPAREN_in_atom513 = new BitSet(new long[]{0x0000000000EFF220L});
    public static final BitSet FOLLOW_lowLevelExpr_in_atom517 = new BitSet(new long[]{0x0000000000000400L});
    public static final BitSet FOLLOW_RPAREN_in_atom520 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_TANH_in_atom525 = new BitSet(new long[]{0x0000000000000200L});
    public static final BitSet FOLLOW_LPAREN_in_atom527 = new BitSet(new long[]{0x0000000000EFF220L});
    public static final BitSet FOLLOW_lowLevelExpr_in_atom531 = new BitSet(new long[]{0x0000000000000400L});
    public static final BitSet FOLLOW_RPAREN_in_atom534 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_SIN_in_atom539 = new BitSet(new long[]{0x0000000000000200L});
    public static final BitSet FOLLOW_LPAREN_in_atom541 = new BitSet(new long[]{0x0000000000EFF220L});
    public static final BitSet FOLLOW_lowLevelExpr_in_atom545 = new BitSet(new long[]{0x0000000000000400L});
    public static final BitSet FOLLOW_RPAREN_in_atom548 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_COS_in_atom553 = new BitSet(new long[]{0x0000000000000200L});
    public static final BitSet FOLLOW_LPAREN_in_atom555 = new BitSet(new long[]{0x0000000000EFF220L});
    public static final BitSet FOLLOW_lowLevelExpr_in_atom559 = new BitSet(new long[]{0x0000000000000400L});
    public static final BitSet FOLLOW_RPAREN_in_atom562 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_TAN_in_atom567 = new BitSet(new long[]{0x0000000000000200L});
    public static final BitSet FOLLOW_LPAREN_in_atom569 = new BitSet(new long[]{0x0000000000EFF220L});
    public static final BitSet FOLLOW_lowLevelExpr_in_atom573 = new BitSet(new long[]{0x0000000000000400L});
    public static final BitSet FOLLOW_RPAREN_in_atom576 = new BitSet(new long[]{0x0000000000000002L});

}