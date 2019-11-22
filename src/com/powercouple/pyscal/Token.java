package com.powercouple.pyscal;

public class Token {

    private String name;
    private String lexeme;
    private long line;
    private long column;
    private Tag tipo;

    public Token(String name, String lexeme, long line, long column) {
        this.name = name;
        this.lexeme = lexeme;
        this.line = line;
        this.column = column;
        this.tipo = Tag.EMPTY;
    }

    public String toString(){
        return "<" + this.getName() + " , \"" + this.getLexeme() + "\">";
    }

    public String getName() {
        return name;
    }

    private String getLexeme() {
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

    public Tag getTipo() {
        return tipo;
    }

    public void setTipo(Tag tipo) {
        this.tipo = tipo;
    }
}
