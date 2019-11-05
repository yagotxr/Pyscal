package Lexer.Token;

public enum Tag {

    EOF,
    KW_FALSE,
    KW_TRUE,

    KW_CLASS,
    KW_END,
    KW_DEF,
    KW_RETURN,
    KW_VOID,
    KW_MAIN,
    KW_DEFSTATIC,


    KW_STRING,
    KW_DOUBLE,
    KW_INTEGER,
    KW_BOOL,

    KW_IF,
    KW_ELSE,
    KW_WHILE,
    KW_WRITE,

    OP_OR,
    OP_AND,

    OP_SOMA,
    OP_SUBTRACAO,
    OP_MULTIPLICACAO,
    OP_DIVISAO,

    OP_MAIOR,
    OP_MAIOR_IGUAL,
    OP_MENOR,
    OP_MENOR_IGUAL,
    OP_IGUAL,
    OP_DIFERENTE,
    OP_COMPARACAO,

    OPUNARIO_NEGACAO,
    OPUNARIO_NEGATIVO,

    ID,
    CONST_INT,
    CONST_STRING,
    CONST_DOUBLE,

    PONTO, // .
    DOIS_PONTOS, // :
    PONTO_VIRGULA, //;
    VIRGULA,  // ,
    ABRE_PARENTESES,  // (
    FECHA_PARENTESES, // )
    ABRE_COLCHETE, // [
    FECHA_COLCHETE, // ]
}
