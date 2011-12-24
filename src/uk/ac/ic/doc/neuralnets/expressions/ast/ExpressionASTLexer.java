// $ANTLR 3.1.1 F:\\My Documents\\Documents\\Coding\\NeuralNetwork\\antlr\\ExpressionAST.g 2008-12-16 17:22:58

package uk.ac.ic.doc.neuralnets.expressions.ast;


import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

public class ExpressionASTLexer extends Lexer {
    public static final int MOD=8;
    public static final int GRAND=14;
    public static final int INT=22;
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
    public static final int TAN=17;
    public static final int RAND=13;
    public static final int DOUBLE=23;
    public static final int PLUS=4;
    public static final int VAR=24;
    public static final int DIV=7;

    // delegates
    // delegators

    public ExpressionASTLexer() {;} 
    public ExpressionASTLexer(CharStream input) {
        this(input, new RecognizerSharedState());
    }
    public ExpressionASTLexer(CharStream input, RecognizerSharedState state) {
        super(input,state);

    }
    public String getGrammarFileName() { return "F:\\My Documents\\Documents\\Coding\\NeuralNetwork\\antlr\\ExpressionAST.g"; }

    // $ANTLR start "PLUS"
    public final void mPLUS() throws RecognitionException {
        try {
            int _type = PLUS;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // F:\\My Documents\\Documents\\Coding\\NeuralNetwork\\antlr\\ExpressionAST.g:7:6: ( '+' )
            // F:\\My Documents\\Documents\\Coding\\NeuralNetwork\\antlr\\ExpressionAST.g:7:8: '+'
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
            // F:\\My Documents\\Documents\\Coding\\NeuralNetwork\\antlr\\ExpressionAST.g:8:7: ( '-' )
            // F:\\My Documents\\Documents\\Coding\\NeuralNetwork\\antlr\\ExpressionAST.g:8:9: '-'
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
            // F:\\My Documents\\Documents\\Coding\\NeuralNetwork\\antlr\\ExpressionAST.g:9:6: ( '*' )
            // F:\\My Documents\\Documents\\Coding\\NeuralNetwork\\antlr\\ExpressionAST.g:9:8: '*'
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
            // F:\\My Documents\\Documents\\Coding\\NeuralNetwork\\antlr\\ExpressionAST.g:10:5: ( '/' )
            // F:\\My Documents\\Documents\\Coding\\NeuralNetwork\\antlr\\ExpressionAST.g:10:7: '/'
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
            // F:\\My Documents\\Documents\\Coding\\NeuralNetwork\\antlr\\ExpressionAST.g:11:5: ( '%' )
            // F:\\My Documents\\Documents\\Coding\\NeuralNetwork\\antlr\\ExpressionAST.g:11:7: '%'
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
            // F:\\My Documents\\Documents\\Coding\\NeuralNetwork\\antlr\\ExpressionAST.g:12:8: ( '(' )
            // F:\\My Documents\\Documents\\Coding\\NeuralNetwork\\antlr\\ExpressionAST.g:12:10: '('
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
            // F:\\My Documents\\Documents\\Coding\\NeuralNetwork\\antlr\\ExpressionAST.g:13:8: ( ')' )
            // F:\\My Documents\\Documents\\Coding\\NeuralNetwork\\antlr\\ExpressionAST.g:13:10: ')'
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
            // F:\\My Documents\\Documents\\Coding\\NeuralNetwork\\antlr\\ExpressionAST.g:14:5: ( '^' )
            // F:\\My Documents\\Documents\\Coding\\NeuralNetwork\\antlr\\ExpressionAST.g:14:7: '^'
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

    // $ANTLR start "SQRT"
    public final void mSQRT() throws RecognitionException {
        try {
            int _type = SQRT;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // F:\\My Documents\\Documents\\Coding\\NeuralNetwork\\antlr\\ExpressionAST.g:15:6: ( 'SQRT' )
            // F:\\My Documents\\Documents\\Coding\\NeuralNetwork\\antlr\\ExpressionAST.g:15:8: 'SQRT'
            {
            match("SQRT"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "SQRT"

    // $ANTLR start "RAND"
    public final void mRAND() throws RecognitionException {
        try {
            int _type = RAND;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // F:\\My Documents\\Documents\\Coding\\NeuralNetwork\\antlr\\ExpressionAST.g:16:6: ( 'RAND()' )
            // F:\\My Documents\\Documents\\Coding\\NeuralNetwork\\antlr\\ExpressionAST.g:16:8: 'RAND()'
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
            // F:\\My Documents\\Documents\\Coding\\NeuralNetwork\\antlr\\ExpressionAST.g:17:7: ( 'GRAND()' )
            // F:\\My Documents\\Documents\\Coding\\NeuralNetwork\\antlr\\ExpressionAST.g:17:9: 'GRAND()'
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
            // F:\\My Documents\\Documents\\Coding\\NeuralNetwork\\antlr\\ExpressionAST.g:18:5: ( 'SIN' )
            // F:\\My Documents\\Documents\\Coding\\NeuralNetwork\\antlr\\ExpressionAST.g:18:7: 'SIN'
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
            // F:\\My Documents\\Documents\\Coding\\NeuralNetwork\\antlr\\ExpressionAST.g:19:5: ( 'COS' )
            // F:\\My Documents\\Documents\\Coding\\NeuralNetwork\\antlr\\ExpressionAST.g:19:7: 'COS'
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
            // F:\\My Documents\\Documents\\Coding\\NeuralNetwork\\antlr\\ExpressionAST.g:20:5: ( 'TAN' )
            // F:\\My Documents\\Documents\\Coding\\NeuralNetwork\\antlr\\ExpressionAST.g:20:7: 'TAN'
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
            // F:\\My Documents\\Documents\\Coding\\NeuralNetwork\\antlr\\ExpressionAST.g:21:6: ( 'SINH' )
            // F:\\My Documents\\Documents\\Coding\\NeuralNetwork\\antlr\\ExpressionAST.g:21:8: 'SINH'
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
            // F:\\My Documents\\Documents\\Coding\\NeuralNetwork\\antlr\\ExpressionAST.g:22:6: ( 'COSH' )
            // F:\\My Documents\\Documents\\Coding\\NeuralNetwork\\antlr\\ExpressionAST.g:22:8: 'COSH'
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
            // F:\\My Documents\\Documents\\Coding\\NeuralNetwork\\antlr\\ExpressionAST.g:23:6: ( 'TANH' )
            // F:\\My Documents\\Documents\\Coding\\NeuralNetwork\\antlr\\ExpressionAST.g:23:8: 'TANH'
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
            // F:\\My Documents\\Documents\\Coding\\NeuralNetwork\\antlr\\ExpressionAST.g:208:6: ( ( '0' .. '9' )+ )
            // F:\\My Documents\\Documents\\Coding\\NeuralNetwork\\antlr\\ExpressionAST.g:208:8: ( '0' .. '9' )+
            {
            // F:\\My Documents\\Documents\\Coding\\NeuralNetwork\\antlr\\ExpressionAST.g:208:8: ( '0' .. '9' )+
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
            	    // F:\\My Documents\\Documents\\Coding\\NeuralNetwork\\antlr\\ExpressionAST.g:208:8: '0' .. '9'
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
            // F:\\My Documents\\Documents\\Coding\\NeuralNetwork\\antlr\\ExpressionAST.g:209:9: ( ( '0' .. '9' )* '.' ( '0' .. '9' )+ )
            // F:\\My Documents\\Documents\\Coding\\NeuralNetwork\\antlr\\ExpressionAST.g:209:11: ( '0' .. '9' )* '.' ( '0' .. '9' )+
            {
            // F:\\My Documents\\Documents\\Coding\\NeuralNetwork\\antlr\\ExpressionAST.g:209:11: ( '0' .. '9' )*
            loop2:
            do {
                int alt2=2;
                int LA2_0 = input.LA(1);

                if ( ((LA2_0>='0' && LA2_0<='9')) ) {
                    alt2=1;
                }


                switch (alt2) {
            	case 1 :
            	    // F:\\My Documents\\Documents\\Coding\\NeuralNetwork\\antlr\\ExpressionAST.g:209:11: '0' .. '9'
            	    {
            	    matchRange('0','9'); 

            	    }
            	    break;

            	default :
            	    break loop2;
                }
            } while (true);

            match('.'); 
            // F:\\My Documents\\Documents\\Coding\\NeuralNetwork\\antlr\\ExpressionAST.g:209:25: ( '0' .. '9' )+
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
            	    // F:\\My Documents\\Documents\\Coding\\NeuralNetwork\\antlr\\ExpressionAST.g:209:25: '0' .. '9'
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
            // F:\\My Documents\\Documents\\Coding\\NeuralNetwork\\antlr\\ExpressionAST.g:210:9: ( ( '\\r' )? '\\n' )
            // F:\\My Documents\\Documents\\Coding\\NeuralNetwork\\antlr\\ExpressionAST.g:210:11: ( '\\r' )? '\\n'
            {
            // F:\\My Documents\\Documents\\Coding\\NeuralNetwork\\antlr\\ExpressionAST.g:210:11: ( '\\r' )?
            int alt4=2;
            int LA4_0 = input.LA(1);

            if ( (LA4_0=='\r') ) {
                alt4=1;
            }
            switch (alt4) {
                case 1 :
                    // F:\\My Documents\\Documents\\Coding\\NeuralNetwork\\antlr\\ExpressionAST.g:210:11: '\\r'
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
            // F:\\My Documents\\Documents\\Coding\\NeuralNetwork\\antlr\\ExpressionAST.g:211:6: ( ( 'a' .. 'z' | 'A' .. 'Z' )+ )
            // F:\\My Documents\\Documents\\Coding\\NeuralNetwork\\antlr\\ExpressionAST.g:211:8: ( 'a' .. 'z' | 'A' .. 'Z' )+
            {
            // F:\\My Documents\\Documents\\Coding\\NeuralNetwork\\antlr\\ExpressionAST.g:211:8: ( 'a' .. 'z' | 'A' .. 'Z' )+
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
            	    // F:\\My Documents\\Documents\\Coding\\NeuralNetwork\\antlr\\ExpressionAST.g:
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
            // F:\\My Documents\\Documents\\Coding\\NeuralNetwork\\antlr\\ExpressionAST.g:212:5: ( ( ' ' | '\\t' )+ )
            // F:\\My Documents\\Documents\\Coding\\NeuralNetwork\\antlr\\ExpressionAST.g:212:7: ( ' ' | '\\t' )+
            {
            // F:\\My Documents\\Documents\\Coding\\NeuralNetwork\\antlr\\ExpressionAST.g:212:7: ( ' ' | '\\t' )+
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
            	    // F:\\My Documents\\Documents\\Coding\\NeuralNetwork\\antlr\\ExpressionAST.g:
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
        // F:\\My Documents\\Documents\\Coding\\NeuralNetwork\\antlr\\ExpressionAST.g:1:8: ( PLUS | MINUS | MULT | DIV | MOD | LPAREN | RPAREN | POW | SQRT | RAND | GRAND | SIN | COS | TAN | SINH | COSH | TANH | INT | DOUBLE | NEWLINE | VAR | WS )
        int alt7=22;
        alt7 = dfa7.predict(input);
        switch (alt7) {
            case 1 :
                // F:\\My Documents\\Documents\\Coding\\NeuralNetwork\\antlr\\ExpressionAST.g:1:10: PLUS
                {
                mPLUS(); 

                }
                break;
            case 2 :
                // F:\\My Documents\\Documents\\Coding\\NeuralNetwork\\antlr\\ExpressionAST.g:1:15: MINUS
                {
                mMINUS(); 

                }
                break;
            case 3 :
                // F:\\My Documents\\Documents\\Coding\\NeuralNetwork\\antlr\\ExpressionAST.g:1:21: MULT
                {
                mMULT(); 

                }
                break;
            case 4 :
                // F:\\My Documents\\Documents\\Coding\\NeuralNetwork\\antlr\\ExpressionAST.g:1:26: DIV
                {
                mDIV(); 

                }
                break;
            case 5 :
                // F:\\My Documents\\Documents\\Coding\\NeuralNetwork\\antlr\\ExpressionAST.g:1:30: MOD
                {
                mMOD(); 

                }
                break;
            case 6 :
                // F:\\My Documents\\Documents\\Coding\\NeuralNetwork\\antlr\\ExpressionAST.g:1:34: LPAREN
                {
                mLPAREN(); 

                }
                break;
            case 7 :
                // F:\\My Documents\\Documents\\Coding\\NeuralNetwork\\antlr\\ExpressionAST.g:1:41: RPAREN
                {
                mRPAREN(); 

                }
                break;
            case 8 :
                // F:\\My Documents\\Documents\\Coding\\NeuralNetwork\\antlr\\ExpressionAST.g:1:48: POW
                {
                mPOW(); 

                }
                break;
            case 9 :
                // F:\\My Documents\\Documents\\Coding\\NeuralNetwork\\antlr\\ExpressionAST.g:1:52: SQRT
                {
                mSQRT(); 

                }
                break;
            case 10 :
                // F:\\My Documents\\Documents\\Coding\\NeuralNetwork\\antlr\\ExpressionAST.g:1:57: RAND
                {
                mRAND(); 

                }
                break;
            case 11 :
                // F:\\My Documents\\Documents\\Coding\\NeuralNetwork\\antlr\\ExpressionAST.g:1:62: GRAND
                {
                mGRAND(); 

                }
                break;
            case 12 :
                // F:\\My Documents\\Documents\\Coding\\NeuralNetwork\\antlr\\ExpressionAST.g:1:68: SIN
                {
                mSIN(); 

                }
                break;
            case 13 :
                // F:\\My Documents\\Documents\\Coding\\NeuralNetwork\\antlr\\ExpressionAST.g:1:72: COS
                {
                mCOS(); 

                }
                break;
            case 14 :
                // F:\\My Documents\\Documents\\Coding\\NeuralNetwork\\antlr\\ExpressionAST.g:1:76: TAN
                {
                mTAN(); 

                }
                break;
            case 15 :
                // F:\\My Documents\\Documents\\Coding\\NeuralNetwork\\antlr\\ExpressionAST.g:1:80: SINH
                {
                mSINH(); 

                }
                break;
            case 16 :
                // F:\\My Documents\\Documents\\Coding\\NeuralNetwork\\antlr\\ExpressionAST.g:1:85: COSH
                {
                mCOSH(); 

                }
                break;
            case 17 :
                // F:\\My Documents\\Documents\\Coding\\NeuralNetwork\\antlr\\ExpressionAST.g:1:90: TANH
                {
                mTANH(); 

                }
                break;
            case 18 :
                // F:\\My Documents\\Documents\\Coding\\NeuralNetwork\\antlr\\ExpressionAST.g:1:95: INT
                {
                mINT(); 

                }
                break;
            case 19 :
                // F:\\My Documents\\Documents\\Coding\\NeuralNetwork\\antlr\\ExpressionAST.g:1:99: DOUBLE
                {
                mDOUBLE(); 

                }
                break;
            case 20 :
                // F:\\My Documents\\Documents\\Coding\\NeuralNetwork\\antlr\\ExpressionAST.g:1:106: NEWLINE
                {
                mNEWLINE(); 

                }
                break;
            case 21 :
                // F:\\My Documents\\Documents\\Coding\\NeuralNetwork\\antlr\\ExpressionAST.g:1:114: VAR
                {
                mVAR(); 

                }
                break;
            case 22 :
                // F:\\My Documents\\Documents\\Coding\\NeuralNetwork\\antlr\\ExpressionAST.g:1:118: WS
                {
                mWS(); 

                }
                break;

        }

    }


    protected DFA7 dfa7 = new DFA7(this);
    static final String DFA7_eotS =
        "\11\uffff\5\21\1\31\4\uffff\6\21\1\uffff\1\21\1\42\2\21\1\46\1"+
        "\50\1\51\1\52\1\uffff\2\21\1\55\1\uffff\1\56\4\uffff\1\21\3\uffff";
    static final String DFA7_eofS =
        "\60\uffff";
    static final String DFA7_minS =
        "\1\11\10\uffff\1\111\1\101\1\122\1\117\1\101\1\56\4\uffff\1\122"+
        "\2\116\1\101\1\123\1\116\1\uffff\1\124\1\101\1\104\1\116\4\101\1"+
        "\uffff\1\50\1\104\1\101\1\uffff\1\101\4\uffff\1\50\3\uffff";
    static final String DFA7_maxS =
        "\1\172\10\uffff\1\121\1\101\1\122\1\117\1\101\1\71\4\uffff\1\122"+
        "\2\116\1\101\1\123\1\116\1\uffff\1\124\1\172\1\104\1\116\4\172\1"+
        "\uffff\1\50\1\104\1\172\1\uffff\1\172\4\uffff\1\50\3\uffff";
    static final String DFA7_acceptS =
        "\1\uffff\1\1\1\2\1\3\1\4\1\5\1\6\1\7\1\10\6\uffff\1\23\1\24\1\25"+
        "\1\26\6\uffff\1\22\10\uffff\1\14\3\uffff\1\15\1\uffff\1\16\1\11"+
        "\1\17\1\12\1\uffff\1\20\1\21\1\13";
    static final String DFA7_specialS =
        "\60\uffff}>";
    static final String[] DFA7_transitionS = {
            "\1\22\1\20\2\uffff\1\20\22\uffff\1\22\4\uffff\1\5\2\uffff\1"+
            "\6\1\7\1\3\1\1\1\uffff\1\2\1\17\1\4\12\16\7\uffff\2\21\1\14"+
            "\3\21\1\13\12\21\1\12\1\11\1\15\6\21\3\uffff\1\10\2\uffff\32"+
            "\21",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "\1\24\7\uffff\1\23",
            "\1\25",
            "\1\26",
            "\1\27",
            "\1\30",
            "\1\17\1\uffff\12\16",
            "",
            "",
            "",
            "",
            "\1\32",
            "\1\33",
            "\1\34",
            "\1\35",
            "\1\36",
            "\1\37",
            "",
            "\1\40",
            "\7\21\1\41\22\21\6\uffff\32\21",
            "\1\43",
            "\1\44",
            "\7\21\1\45\22\21\6\uffff\32\21",
            "\7\21\1\47\22\21\6\uffff\32\21",
            "\32\21\6\uffff\32\21",
            "\32\21\6\uffff\32\21",
            "",
            "\1\53",
            "\1\54",
            "\32\21\6\uffff\32\21",
            "",
            "\32\21\6\uffff\32\21",
            "",
            "",
            "",
            "",
            "\1\57",
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
            return "1:1: Tokens : ( PLUS | MINUS | MULT | DIV | MOD | LPAREN | RPAREN | POW | SQRT | RAND | GRAND | SIN | COS | TAN | SINH | COSH | TANH | INT | DOUBLE | NEWLINE | VAR | WS );";
        }
    }
 

}