// $ANTLR 3.1.1 C:\\Users\\Peter Coetzee\\Coding\\NeuralNetwork\\antlr\\Calculation.g 2008-11-24 15:57:46

package uk.ac.ic.doc.neuralnets.expressions;


import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

public class CalculationLexer extends Lexer {
    public static final int MOD=8;
    public static final int GRAND=13;
    public static final int INT=21;
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
    public static final int TAN=16;
    public static final int RAND=12;
    public static final int DOUBLE=23;
    public static final int PLUS=4;
    public static final int VAR=22;
    public static final int DIV=7;

    // delegates
    // delegators

    public CalculationLexer() {;} 
    public CalculationLexer(CharStream input) {
        this(input, new RecognizerSharedState());
    }
    public CalculationLexer(CharStream input, RecognizerSharedState state) {
        super(input,state);

    }
    public String getGrammarFileName() { return "C:\\Users\\Peter Coetzee\\Coding\\NeuralNetwork\\antlr\\Calculation.g"; }

    // $ANTLR start "PLUS"
    public final void mPLUS() throws RecognitionException {
        try {
            int _type = PLUS;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // C:\\Users\\Peter Coetzee\\Coding\\NeuralNetwork\\antlr\\Calculation.g:7:6: ( '+' )
            // C:\\Users\\Peter Coetzee\\Coding\\NeuralNetwork\\antlr\\Calculation.g:7:8: '+'
            {
            match('+'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "PLUS"

    // $ANTLR start "MINUS"
    public final void mMINUS() throws RecognitionException {
        try {
            int _type = MINUS;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // C:\\Users\\Peter Coetzee\\Coding\\NeuralNetwork\\antlr\\Calculation.g:8:7: ( '-' )
            // C:\\Users\\Peter Coetzee\\Coding\\NeuralNetwork\\antlr\\Calculation.g:8:9: '-'
            {
            match('-'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "MINUS"

    // $ANTLR start "MULT"
    public final void mMULT() throws RecognitionException {
        try {
            int _type = MULT;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // C:\\Users\\Peter Coetzee\\Coding\\NeuralNetwork\\antlr\\Calculation.g:9:6: ( '*' )
            // C:\\Users\\Peter Coetzee\\Coding\\NeuralNetwork\\antlr\\Calculation.g:9:8: '*'
            {
            match('*'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "MULT"

    // $ANTLR start "DIV"
    public final void mDIV() throws RecognitionException {
        try {
            int _type = DIV;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // C:\\Users\\Peter Coetzee\\Coding\\NeuralNetwork\\antlr\\Calculation.g:10:5: ( '/' )
            // C:\\Users\\Peter Coetzee\\Coding\\NeuralNetwork\\antlr\\Calculation.g:10:7: '/'
            {
            match('/'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "DIV"

    // $ANTLR start "MOD"
    public final void mMOD() throws RecognitionException {
        try {
            int _type = MOD;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // C:\\Users\\Peter Coetzee\\Coding\\NeuralNetwork\\antlr\\Calculation.g:11:5: ( '%' )
            // C:\\Users\\Peter Coetzee\\Coding\\NeuralNetwork\\antlr\\Calculation.g:11:7: '%'
            {
            match('%'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "MOD"

    // $ANTLR start "LPAREN"
    public final void mLPAREN() throws RecognitionException {
        try {
            int _type = LPAREN;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // C:\\Users\\Peter Coetzee\\Coding\\NeuralNetwork\\antlr\\Calculation.g:12:8: ( '(' )
            // C:\\Users\\Peter Coetzee\\Coding\\NeuralNetwork\\antlr\\Calculation.g:12:10: '('
            {
            match('('); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "LPAREN"

    // $ANTLR start "RPAREN"
    public final void mRPAREN() throws RecognitionException {
        try {
            int _type = RPAREN;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // C:\\Users\\Peter Coetzee\\Coding\\NeuralNetwork\\antlr\\Calculation.g:13:8: ( ')' )
            // C:\\Users\\Peter Coetzee\\Coding\\NeuralNetwork\\antlr\\Calculation.g:13:10: ')'
            {
            match(')'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "RPAREN"

    // $ANTLR start "POW"
    public final void mPOW() throws RecognitionException {
        try {
            int _type = POW;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // C:\\Users\\Peter Coetzee\\Coding\\NeuralNetwork\\antlr\\Calculation.g:14:5: ( '^' )
            // C:\\Users\\Peter Coetzee\\Coding\\NeuralNetwork\\antlr\\Calculation.g:14:7: '^'
            {
            match('^'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "POW"

    // $ANTLR start "RAND"
    public final void mRAND() throws RecognitionException {
        try {
            int _type = RAND;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // C:\\Users\\Peter Coetzee\\Coding\\NeuralNetwork\\antlr\\Calculation.g:15:6: ( 'RAND()' )
            // C:\\Users\\Peter Coetzee\\Coding\\NeuralNetwork\\antlr\\Calculation.g:15:8: 'RAND()'
            {
            match("RAND()"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "RAND"

    // $ANTLR start "GRAND"
    public final void mGRAND() throws RecognitionException {
        try {
            int _type = GRAND;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // C:\\Users\\Peter Coetzee\\Coding\\NeuralNetwork\\antlr\\Calculation.g:16:7: ( 'GRAND()' )
            // C:\\Users\\Peter Coetzee\\Coding\\NeuralNetwork\\antlr\\Calculation.g:16:9: 'GRAND()'
            {
            match("GRAND()"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "GRAND"

    // $ANTLR start "SIN"
    public final void mSIN() throws RecognitionException {
        try {
            int _type = SIN;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // C:\\Users\\Peter Coetzee\\Coding\\NeuralNetwork\\antlr\\Calculation.g:17:5: ( 'SIN' )
            // C:\\Users\\Peter Coetzee\\Coding\\NeuralNetwork\\antlr\\Calculation.g:17:7: 'SIN'
            {
            match("SIN"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "SIN"

    // $ANTLR start "COS"
    public final void mCOS() throws RecognitionException {
        try {
            int _type = COS;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // C:\\Users\\Peter Coetzee\\Coding\\NeuralNetwork\\antlr\\Calculation.g:18:5: ( 'COS' )
            // C:\\Users\\Peter Coetzee\\Coding\\NeuralNetwork\\antlr\\Calculation.g:18:7: 'COS'
            {
            match("COS"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "COS"

    // $ANTLR start "TAN"
    public final void mTAN() throws RecognitionException {
        try {
            int _type = TAN;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // C:\\Users\\Peter Coetzee\\Coding\\NeuralNetwork\\antlr\\Calculation.g:19:5: ( 'TAN' )
            // C:\\Users\\Peter Coetzee\\Coding\\NeuralNetwork\\antlr\\Calculation.g:19:7: 'TAN'
            {
            match("TAN"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "TAN"

    // $ANTLR start "SINH"
    public final void mSINH() throws RecognitionException {
        try {
            int _type = SINH;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // C:\\Users\\Peter Coetzee\\Coding\\NeuralNetwork\\antlr\\Calculation.g:20:6: ( 'SINH' )
            // C:\\Users\\Peter Coetzee\\Coding\\NeuralNetwork\\antlr\\Calculation.g:20:8: 'SINH'
            {
            match("SINH"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "SINH"

    // $ANTLR start "COSH"
    public final void mCOSH() throws RecognitionException {
        try {
            int _type = COSH;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // C:\\Users\\Peter Coetzee\\Coding\\NeuralNetwork\\antlr\\Calculation.g:21:6: ( 'COSH' )
            // C:\\Users\\Peter Coetzee\\Coding\\NeuralNetwork\\antlr\\Calculation.g:21:8: 'COSH'
            {
            match("COSH"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "COSH"

    // $ANTLR start "TANH"
    public final void mTANH() throws RecognitionException {
        try {
            int _type = TANH;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // C:\\Users\\Peter Coetzee\\Coding\\NeuralNetwork\\antlr\\Calculation.g:22:6: ( 'TANH' )
            // C:\\Users\\Peter Coetzee\\Coding\\NeuralNetwork\\antlr\\Calculation.g:22:8: 'TANH'
            {
            match("TANH"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "TANH"

    // $ANTLR start "INT"
    public final void mINT() throws RecognitionException {
        try {
            int _type = INT;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // C:\\Users\\Peter Coetzee\\Coding\\NeuralNetwork\\antlr\\Calculation.g:123:6: ( ( '0' .. '9' )+ )
            // C:\\Users\\Peter Coetzee\\Coding\\NeuralNetwork\\antlr\\Calculation.g:123:8: ( '0' .. '9' )+
            {
            // C:\\Users\\Peter Coetzee\\Coding\\NeuralNetwork\\antlr\\Calculation.g:123:8: ( '0' .. '9' )+
            int cnt1=0;
            loop1:
            do {
                int alt1=2;
                int LA1_0 = input.LA(1);

                if ( ((LA1_0>='0' && LA1_0<='9')) ) {
                    alt1=1;
                }


                switch (alt1) {
            	case 1 :
            	    // C:\\Users\\Peter Coetzee\\Coding\\NeuralNetwork\\antlr\\Calculation.g:123:8: '0' .. '9'
            	    {
            	    matchRange('0','9'); 

            	    }
            	    break;

            	default :
            	    if ( cnt1 >= 1 ) break loop1;
                        EarlyExitException eee =
                            new EarlyExitException(1, input);
                        throw eee;
                }
                cnt1++;
            } while (true);


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "INT"

    // $ANTLR start "DOUBLE"
    public final void mDOUBLE() throws RecognitionException {
        try {
            int _type = DOUBLE;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // C:\\Users\\Peter Coetzee\\Coding\\NeuralNetwork\\antlr\\Calculation.g:124:9: ( ( '0' .. '9' )* '.' ( '0' .. '9' )+ )
            // C:\\Users\\Peter Coetzee\\Coding\\NeuralNetwork\\antlr\\Calculation.g:124:11: ( '0' .. '9' )* '.' ( '0' .. '9' )+
            {
            // C:\\Users\\Peter Coetzee\\Coding\\NeuralNetwork\\antlr\\Calculation.g:124:11: ( '0' .. '9' )*
            loop2:
            do {
                int alt2=2;
                int LA2_0 = input.LA(1);

                if ( ((LA2_0>='0' && LA2_0<='9')) ) {
                    alt2=1;
                }


                switch (alt2) {
            	case 1 :
            	    // C:\\Users\\Peter Coetzee\\Coding\\NeuralNetwork\\antlr\\Calculation.g:124:11: '0' .. '9'
            	    {
            	    matchRange('0','9'); 

            	    }
            	    break;

            	default :
            	    break loop2;
                }
            } while (true);

            match('.'); 
            // C:\\Users\\Peter Coetzee\\Coding\\NeuralNetwork\\antlr\\Calculation.g:124:25: ( '0' .. '9' )+
            int cnt3=0;
            loop3:
            do {
                int alt3=2;
                int LA3_0 = input.LA(1);

                if ( ((LA3_0>='0' && LA3_0<='9')) ) {
                    alt3=1;
                }


                switch (alt3) {
            	case 1 :
            	    // C:\\Users\\Peter Coetzee\\Coding\\NeuralNetwork\\antlr\\Calculation.g:124:25: '0' .. '9'
            	    {
            	    matchRange('0','9'); 

            	    }
            	    break;

            	default :
            	    if ( cnt3 >= 1 ) break loop3;
                        EarlyExitException eee =
                            new EarlyExitException(3, input);
                        throw eee;
                }
                cnt3++;
            } while (true);


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "DOUBLE"

    // $ANTLR start "NEWLINE"
    public final void mNEWLINE() throws RecognitionException {
        try {
            int _type = NEWLINE;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // C:\\Users\\Peter Coetzee\\Coding\\NeuralNetwork\\antlr\\Calculation.g:125:9: ( ( '\\r' )? '\\n' )
            // C:\\Users\\Peter Coetzee\\Coding\\NeuralNetwork\\antlr\\Calculation.g:125:11: ( '\\r' )? '\\n'
            {
            // C:\\Users\\Peter Coetzee\\Coding\\NeuralNetwork\\antlr\\Calculation.g:125:11: ( '\\r' )?
            int alt4=2;
            int LA4_0 = input.LA(1);

            if ( (LA4_0=='\r') ) {
                alt4=1;
            }
            switch (alt4) {
                case 1 :
                    // C:\\Users\\Peter Coetzee\\Coding\\NeuralNetwork\\antlr\\Calculation.g:125:11: '\\r'
                    {
                    match('\r'); 

                    }
                    break;

            }

            match('\n'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "NEWLINE"

    // $ANTLR start "VAR"
    public final void mVAR() throws RecognitionException {
        try {
            int _type = VAR;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // C:\\Users\\Peter Coetzee\\Coding\\NeuralNetwork\\antlr\\Calculation.g:126:6: ( ( 'a' .. 'z' | 'A' .. 'Z' )+ )
            // C:\\Users\\Peter Coetzee\\Coding\\NeuralNetwork\\antlr\\Calculation.g:126:8: ( 'a' .. 'z' | 'A' .. 'Z' )+
            {
            // C:\\Users\\Peter Coetzee\\Coding\\NeuralNetwork\\antlr\\Calculation.g:126:8: ( 'a' .. 'z' | 'A' .. 'Z' )+
            int cnt5=0;
            loop5:
            do {
                int alt5=2;
                int LA5_0 = input.LA(1);

                if ( ((LA5_0>='A' && LA5_0<='Z')||(LA5_0>='a' && LA5_0<='z')) ) {
                    alt5=1;
                }


                switch (alt5) {
            	case 1 :
            	    // C:\\Users\\Peter Coetzee\\Coding\\NeuralNetwork\\antlr\\Calculation.g:
            	    {
            	    if ( (input.LA(1)>='A' && input.LA(1)<='Z')||(input.LA(1)>='a' && input.LA(1)<='z') ) {
            	        input.consume();

            	    }
            	    else {
            	        MismatchedSetException mse = new MismatchedSetException(null,input);
            	        recover(mse);
            	        throw mse;}


            	    }
            	    break;

            	default :
            	    if ( cnt5 >= 1 ) break loop5;
                        EarlyExitException eee =
                            new EarlyExitException(5, input);
                        throw eee;
                }
                cnt5++;
            } while (true);


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "VAR"

    // $ANTLR start "WS"
    public final void mWS() throws RecognitionException {
        try {
            int _type = WS;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // C:\\Users\\Peter Coetzee\\Coding\\NeuralNetwork\\antlr\\Calculation.g:127:5: ( ( ' ' | '\\t' )+ )
            // C:\\Users\\Peter Coetzee\\Coding\\NeuralNetwork\\antlr\\Calculation.g:127:7: ( ' ' | '\\t' )+
            {
            // C:\\Users\\Peter Coetzee\\Coding\\NeuralNetwork\\antlr\\Calculation.g:127:7: ( ' ' | '\\t' )+
            int cnt6=0;
            loop6:
            do {
                int alt6=2;
                int LA6_0 = input.LA(1);

                if ( (LA6_0=='\t'||LA6_0==' ') ) {
                    alt6=1;
                }


                switch (alt6) {
            	case 1 :
            	    // C:\\Users\\Peter Coetzee\\Coding\\NeuralNetwork\\antlr\\Calculation.g:
            	    {
            	    if ( input.LA(1)=='\t'||input.LA(1)==' ' ) {
            	        input.consume();

            	    }
            	    else {
            	        MismatchedSetException mse = new MismatchedSetException(null,input);
            	        recover(mse);
            	        throw mse;}


            	    }
            	    break;

            	default :
            	    if ( cnt6 >= 1 ) break loop6;
                        EarlyExitException eee =
                            new EarlyExitException(6, input);
                        throw eee;
                }
                cnt6++;
            } while (true);

            skip();

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "WS"

    public void mTokens() throws RecognitionException {
        // C:\\Users\\Peter Coetzee\\Coding\\NeuralNetwork\\antlr\\Calculation.g:1:8: ( PLUS | MINUS | MULT | DIV | MOD | LPAREN | RPAREN | POW | RAND | GRAND | SIN | COS | TAN | SINH | COSH | TANH | INT | DOUBLE | NEWLINE | VAR | WS )
        int alt7=21;
        alt7 = dfa7.predict(input);
        switch (alt7) {
            case 1 :
                // C:\\Users\\Peter Coetzee\\Coding\\NeuralNetwork\\antlr\\Calculation.g:1:10: PLUS
                {
                mPLUS(); 

                }
                break;
            case 2 :
                // C:\\Users\\Peter Coetzee\\Coding\\NeuralNetwork\\antlr\\Calculation.g:1:15: MINUS
                {
                mMINUS(); 

                }
                break;
            case 3 :
                // C:\\Users\\Peter Coetzee\\Coding\\NeuralNetwork\\antlr\\Calculation.g:1:21: MULT
                {
                mMULT(); 

                }
                break;
            case 4 :
                // C:\\Users\\Peter Coetzee\\Coding\\NeuralNetwork\\antlr\\Calculation.g:1:26: DIV
                {
                mDIV(); 

                }
                break;
            case 5 :
                // C:\\Users\\Peter Coetzee\\Coding\\NeuralNetwork\\antlr\\Calculation.g:1:30: MOD
                {
                mMOD(); 

                }
                break;
            case 6 :
                // C:\\Users\\Peter Coetzee\\Coding\\NeuralNetwork\\antlr\\Calculation.g:1:34: LPAREN
                {
                mLPAREN(); 

                }
                break;
            case 7 :
                // C:\\Users\\Peter Coetzee\\Coding\\NeuralNetwork\\antlr\\Calculation.g:1:41: RPAREN
                {
                mRPAREN(); 

                }
                break;
            case 8 :
                // C:\\Users\\Peter Coetzee\\Coding\\NeuralNetwork\\antlr\\Calculation.g:1:48: POW
                {
                mPOW(); 

                }
                break;
            case 9 :
                // C:\\Users\\Peter Coetzee\\Coding\\NeuralNetwork\\antlr\\Calculation.g:1:52: RAND
                {
                mRAND(); 

                }
                break;
            case 10 :
                // C:\\Users\\Peter Coetzee\\Coding\\NeuralNetwork\\antlr\\Calculation.g:1:57: GRAND
                {
                mGRAND(); 

                }
                break;
            case 11 :
                // C:\\Users\\Peter Coetzee\\Coding\\NeuralNetwork\\antlr\\Calculation.g:1:63: SIN
                {
                mSIN(); 

                }
                break;
            case 12 :
                // C:\\Users\\Peter Coetzee\\Coding\\NeuralNetwork\\antlr\\Calculation.g:1:67: COS
                {
                mCOS(); 

                }
                break;
            case 13 :
                // C:\\Users\\Peter Coetzee\\Coding\\NeuralNetwork\\antlr\\Calculation.g:1:71: TAN
                {
                mTAN(); 

                }
                break;
            case 14 :
                // C:\\Users\\Peter Coetzee\\Coding\\NeuralNetwork\\antlr\\Calculation.g:1:75: SINH
                {
                mSINH(); 

                }
                break;
            case 15 :
                // C:\\Users\\Peter Coetzee\\Coding\\NeuralNetwork\\antlr\\Calculation.g:1:80: COSH
                {
                mCOSH(); 

                }
                break;
            case 16 :
                // C:\\Users\\Peter Coetzee\\Coding\\NeuralNetwork\\antlr\\Calculation.g:1:85: TANH
                {
                mTANH(); 

                }
                break;
            case 17 :
                // C:\\Users\\Peter Coetzee\\Coding\\NeuralNetwork\\antlr\\Calculation.g:1:90: INT
                {
                mINT(); 

                }
                break;
            case 18 :
                // C:\\Users\\Peter Coetzee\\Coding\\NeuralNetwork\\antlr\\Calculation.g:1:94: DOUBLE
                {
                mDOUBLE(); 

                }
                break;
            case 19 :
                // C:\\Users\\Peter Coetzee\\Coding\\NeuralNetwork\\antlr\\Calculation.g:1:101: NEWLINE
                {
                mNEWLINE(); 

                }
                break;
            case 20 :
                // C:\\Users\\Peter Coetzee\\Coding\\NeuralNetwork\\antlr\\Calculation.g:1:109: VAR
                {
                mVAR(); 

                }
                break;
            case 21 :
                // C:\\Users\\Peter Coetzee\\Coding\\NeuralNetwork\\antlr\\Calculation.g:1:113: WS
                {
                mWS(); 

                }
                break;

        }

    }


    protected DFA7 dfa7 = new DFA7(this);
    static final String DFA7_eotS =
        "\11\uffff\5\21\1\30\4\uffff\5\21\1\uffff\2\21\1\41\1\43\1\45\2"+
        "\21\1\50\1\uffff\1\51\1\uffff\1\52\2\uffff\1\21\4\uffff";
    static final String DFA7_eofS =
        "\54\uffff";
    static final String DFA7_minS =
        "\1\11\10\uffff\1\101\1\122\1\111\1\117\1\101\1\56\4\uffff\1\116"+
        "\1\101\1\116\1\123\1\116\1\uffff\1\104\1\116\3\101\1\50\1\104\1"+
        "\101\1\uffff\1\101\1\uffff\1\101\2\uffff\1\50\4\uffff";
    static final String DFA7_maxS =
        "\1\172\10\uffff\1\101\1\122\1\111\1\117\1\101\1\71\4\uffff\1\116"+
        "\1\101\1\116\1\123\1\116\1\uffff\1\104\1\116\3\172\1\50\1\104\1"+
        "\172\1\uffff\1\172\1\uffff\1\172\2\uffff\1\50\4\uffff";
    static final String DFA7_acceptS =
        "\1\uffff\1\1\1\2\1\3\1\4\1\5\1\6\1\7\1\10\6\uffff\1\22\1\23\1\24"+
        "\1\25\5\uffff\1\21\10\uffff\1\13\1\uffff\1\14\1\uffff\1\15\1\11"+
        "\1\uffff\1\16\1\17\1\20\1\12";
    static final String DFA7_specialS =
        "\54\uffff}>";
    static final String[] DFA7_transitionS = {
            "\1\22\1\20\2\uffff\1\20\22\uffff\1\22\4\uffff\1\5\2\uffff\1"+
            "\6\1\7\1\3\1\1\1\uffff\1\2\1\17\1\4\12\16\7\uffff\2\21\1\14"+
            "\3\21\1\12\12\21\1\11\1\13\1\15\6\21\3\uffff\1\10\2\uffff\32"+
            "\21",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "\1\23",
            "\1\24",
            "\1\25",
            "\1\26",
            "\1\27",
            "\1\17\1\uffff\12\16",
            "",
            "",
            "",
            "",
            "\1\31",
            "\1\32",
            "\1\33",
            "\1\34",
            "\1\35",
            "",
            "\1\36",
            "\1\37",
            "\7\21\1\40\22\21\6\uffff\32\21",
            "\7\21\1\42\22\21\6\uffff\32\21",
            "\7\21\1\44\22\21\6\uffff\32\21",
            "\1\46",
            "\1\47",
            "\32\21\6\uffff\32\21",
            "",
            "\32\21\6\uffff\32\21",
            "",
            "\32\21\6\uffff\32\21",
            "",
            "",
            "\1\53",
            "",
            "",
            "",
            ""
    };

    static final short[] DFA7_eot = DFA.unpackEncodedString(DFA7_eotS);
    static final short[] DFA7_eof = DFA.unpackEncodedString(DFA7_eofS);
    static final char[] DFA7_min = DFA.unpackEncodedStringToUnsignedChars(DFA7_minS);
    static final char[] DFA7_max = DFA.unpackEncodedStringToUnsignedChars(DFA7_maxS);
    static final short[] DFA7_accept = DFA.unpackEncodedString(DFA7_acceptS);
    static final short[] DFA7_special = DFA.unpackEncodedString(DFA7_specialS);
    static final short[][] DFA7_transition;

    static {
        int numStates = DFA7_transitionS.length;
        DFA7_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA7_transition[i] = DFA.unpackEncodedString(DFA7_transitionS[i]);
        }
    }

    class DFA7 extends DFA {

        public DFA7(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 7;
            this.eot = DFA7_eot;
            this.eof = DFA7_eof;
            this.min = DFA7_min;
            this.max = DFA7_max;
            this.accept = DFA7_accept;
            this.special = DFA7_special;
            this.transition = DFA7_transition;
        }
        public String getDescription() {
            return "1:1: Tokens : ( PLUS | MINUS | MULT | DIV | MOD | LPAREN | RPAREN | POW | RAND | GRAND | SIN | COS | TAN | SINH | COSH | TANH | INT | DOUBLE | NEWLINE | VAR | WS );";
        }
    }
 

}