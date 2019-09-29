public class Token {

    private String name, lexeme;
    private  int line = 0, column = 0;

    public Token(String name, String lexeme, int line, int column) {
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

    public int getLine() {
        return line;
    }

    public int getColumn() {
        return column;
    }
}
