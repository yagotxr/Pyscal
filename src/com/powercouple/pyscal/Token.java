package com.powercouple.pyscal;

public class Token implements Cloneable{

    private String name, lexeme;
    private  long line, column;
    private Tag type;

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

    public void setType(Tag type) {
        this.type = type;
    }

    public Tag getType() {
        return type;
    }
}
