public abstract enum Tag {

    public static final int EOF = -1;

    public static final int KW_FALSE = 0;
    public static final int KW_TRUE = 1;

    public static final int KW_CLASS = 10;
    public static final int KW_END = 11;
    public static final int KW_DEF = 12;
    public static final int KW_RETURN = 13;
    public static final int KW_VOID = 14;
    public static final int KW_MAIN = 15;
    public static final int KW_DEFSTATIC = 16;


    public static final int KW_STRING = 20;
    public static final int KW_DOUBLE = 21;
    public static final int KW_INTEGER  = 22;
    public static final int KW_BOOL = 23;

    public static final int KW_IF = 30;
    public static final int KW_ELSE = 31;
    public static final int KW_WHILE = 32;
    public static final int KW_WRITE = 33;

    public static final int OP_SUM = 40;
    public static final int OP_SUBTRACT = 41;
    public static final int OP_MULTIPLICATION = 42;
    public static final int OP_DIVISION = 43;

    public static final int OP_GREATER = 50;
    public static final int OP_GREATER_EQUAL = 51;
    public static final int OP_LESS = 52;
    public static final int OP_LESS_EQUAL = 53;
    public static final int OP_EQUAL = 54;
    public static final int OP_DIFFERENT = 55;

    public static final int UNARYOP_DENIAL = 60;
    public static final int UNARYOP_NEGATIVE = 61;

    public static final int ID = 70;
    public static final int NUM = 80;

    public static final int PERIOD = 100; // .
    public static final int COLON = 101; // :
    public static final int SEMICOLON = 102; //;
    public static final int COMMA = 103; // ,
    public static final int OPENED_PARENTHESES = 104; // (
    public static final int CLOSED_PARENTHESES = 105; // )
}
