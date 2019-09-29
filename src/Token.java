public class Token {


    private String nome, lexema;
    private  int linha = 0, coluna = 0;

    public Token(String nome, String lexema, int linha, int coluna) {
        this.nome = nome;
        this.lexema = lexema;
        this.linha = linha;
        this.coluna = coluna;
    }

    public String toString(){
        return "<" + this.getNome() + "," + this.getLexema() + ">";
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getLexema() {
        return lexema;
    }

    public void setLexema(String lexema) {
        this.lexema = lexema;
    }

    public int getLinha() {
        return linha;
    }

    public void setLinha(int linha) {
        this.linha = linha;
    }

    public int getColuna() {
        return coluna;
    }

    public void setColuna(int coluna) {
        this.coluna = coluna;
    }
}
