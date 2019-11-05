package Lexer.Token;

import Lexer.Lexer;

public class Token {

    private String name, lexeme;
    private  long line, column;

    public Token(String name, String lexeme, long line, long column) {
        this.name = name;
        this.lexeme = lexeme;
        this.line = line;
        this.column = column;
    }

    public String toString(){
        return "<" + this.getName() + " , \"" + this.getLexeme() + "\">";
    }

    public String getName() {
        return name;
    }

    public String getLexeme() {
        return lexeme;
    }

    public long getLine() {
        return line;
    }

    public long getColumn() {
        return column;
    }

    public void setLine(long line) {
        this.line = line;
    }

    public void setColumn(long column) {
        this.column = column;
    }
}
