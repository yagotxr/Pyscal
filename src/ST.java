import java.util.HashMap;
import java.util.Map;

public class ST {

    private Map<Integer, Token> simbolTable;

    public ST() {
        this.simbolTable = new HashMap<>();
        this.simbolTable.put(Tag.EOF, new Token("EOF", "EOF", 0, 0));
        this.simbolTable.put(Tag.KW_TRUE, new Token("KW_TRUE", "true", 0, 0));
        this.simbolTable.put(Tag.KW_FALSE, new Token("KW_FALSE", "false", 0, 0));
        this.simbolTable.put(Tag.KW_CLASS, new Token("KW_CLASS", "class", 0, 0));
        this.simbolTable.put(Tag.KW_END, new Token("KW_END", "end", 0, 0));
        this.simbolTable.put(Tag.KW_DEF, new Token("KW_DEF", "def", 0, 0));
        this.simbolTable.put(Tag.KW_RETURN, new Token("KW_RETURN", "return", 0, 0));
        this.simbolTable.put(Tag.KW_VOID, new Token("KW_VOID", "void", 0, 0));
        this.simbolTable.put(Tag.KW_MAIN, new Token("KW_MAIN", "main", 0, 0));
        this.simbolTable.put(Tag.KW_DEFSTATIC, new Token("KW_DEFSTATIC", "defstatic", 0, 0));

        this.simbolTable.put(Tag.KW_STRING, new Token("KW_STRING", "String", 0, 0));
        this.simbolTable.put(Tag.KW_DOUBLE, new Token("KW_DOUBLE", "double", 0, 0));
        this.simbolTable.put(Tag.KW_INTEGER, new Token("KW_INTEGER", "integer", 0, 0));
        this.simbolTable.put(Tag.KW_BOOL, new Token("KW_BOOL", "bool", 0, 0));

        this.simbolTable.put(Tag.KW_IF, new Token("KW_IF", "if", 0, 0));
        this.simbolTable.put(Tag.KW_ELSE, new Token("KW_ELSE", "else", 0, 0));
        this.simbolTable.put(Tag.KW_WHILE, new Token("KW_WHILE", "while", 0, 0));
        this.simbolTable.put(Tag.KW_WRITE, new Token("KW_WRITE", "write", 0, 0));

        this.simbolTable.put(Tag.OP_SUM, new Token("OP_SOMA", "+", 0, 0));
        this.simbolTable.put(Tag.OP_SUBTRACT, new Token("OP_SUBTRACAO", "-", 0, 0));
        this.simbolTable.put(Tag.OP_MULTIPLICATION, new Token("OP_MULTIPLICACAO", "*", 0, 0));
        this.simbolTable.put(Tag.OP_DIVISION, new Token("OP_DIVISAO", "/", 0, 0));

        this.simbolTable.put(Tag.OP_GREATER, new Token("OP_MAIOR", ">", 0, 0));
        this.simbolTable.put(Tag.OP_GREATER_EQUAL, new Token("OP_MAIOR_IGUAL", ">=", 0, 0));
        this.simbolTable.put(Tag.OP_LESS, new Token("OP_MENOR", "<", 0, 0));
        this.simbolTable.put(Tag.OP_LESS_EQUAL, new Token("OP_MENOR_IGUAL", "<=", 0, 0));
        this.simbolTable.put(Tag.OP_EQUAL, new Token("OP_IGUAL", "==", 0, 0));
        this.simbolTable.put(Tag.OP_DIFFERENT, new Token("OP_DIFERENTE", "!=", 0, 0));

        this.simbolTable.put(Tag.UNARYOP_DENIAL, new Token("OPUNARIO_NEGACAO", "!", 0, 0));
        this.simbolTable.put(Tag.UNARYOP_NEGATIVE, new Token("OPUNARIO_NEGATIVO", "-", 0, 0));

        this.simbolTable.put(Tag.PERIOD, new Token("PONTO", ".", 0, 0));
        this.simbolTable.put(Tag.COLON, new Token("DOIS_PONTOS", ":", 0, 0));
        this.simbolTable.put(Tag.SEMICOLON, new Token("PONTO_VIRGULA", ";", 0, 0));
        this.simbolTable.put(Tag.COMMA, new Token("VIRGULA", ",", 0, 0));

        this.simbolTable.put(Tag.OPENED_PARENTHESES, new Token("ABRE_PARENTESES"))


    }
}
