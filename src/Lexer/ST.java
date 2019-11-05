package Lexer;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class ST {

    private Map<String, Token> simbolTable;

    public ST() {
        this.simbolTable = new HashMap<>();
        this.simbolTable.put("EOF", new Token(Tag.EOF.toString(), "EOF", 0, 0));
        this.simbolTable.put("true", new Token(Tag.KW_TRUE.toString(), "true", 0, 0));
        this.simbolTable.put("false", new Token(Tag.KW_FALSE.toString(), "false", 0, 0));
        this.simbolTable.put("class", new Token(Tag.KW_CLASS.toString(), "class", 0, 0));
        this.simbolTable.put("end", new Token(Tag.KW_END.toString(), "end", 0, 0));
        this.simbolTable.put("def", new Token(Tag.KW_DEF.toString(), "def", 0, 0));
        this.simbolTable.put("return", new Token(Tag.KW_RETURN.toString(), "return", 0, 0));
        this.simbolTable.put("void", new Token(Tag.KW_VOID.toString(), "void", 0, 0));
        this.simbolTable.put("main", new Token(Tag.KW_MAIN.toString(), "main", 0, 0));
        this.simbolTable.put("defstatic", new Token(Tag.KW_DEFSTATIC.toString(), "defstatic", 0, 0));

        this.simbolTable.put("String", new Token(Tag.KW_STRING.toString(), "String", 0, 0));
        this.simbolTable.put("double", new Token(Tag.KW_DOUBLE.toString(), "double", 0, 0));
        this.simbolTable.put("integer", new Token(Tag.KW_INTEGER.toString(), "integer", 0, 0));
        this.simbolTable.put("bool", new Token(Tag.KW_BOOL.toString(), "bool", 0, 0));

        this.simbolTable.put("if", new Token(Tag.KW_IF.toString(), "if", 0, 0));
        this.simbolTable.put("else", new Token(Tag.KW_ELSE.toString(), "else", 0, 0));
        this.simbolTable.put("while", new Token(Tag.KW_WHILE.toString(), "while", 0, 0));
        this.simbolTable.put("write", new Token(Tag.KW_WRITE.toString(), "write", 0, 0));
        this.simbolTable.put("or", new Token(Tag.OP_OR.toString(), "or", 0, 0));
        this.simbolTable.put("and", new Token(Tag.OP_AND.toString(), "and", 0, 0));
    }

    public Optional<Token> getToken(String lexema){
        Token foundToken = simbolTable.get(lexema);
        if(foundToken == null){
            return Optional.empty();
        }
        return Optional.of(simbolTable.get(lexema));
    }

    public void addToken(String lexema, Token token){
        this.simbolTable.put(lexema, token);
    }

    public void printST(){
        simbolTable.forEach((key, value) -> System.out.println(key + " : " + value.toString()));
    }
}
