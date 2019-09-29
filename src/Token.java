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
        return "<" + this.getNome() + " , \"" + this.getLexema() + "\">";
    }

    public String getNome() {
        return nome;
    }

    public String getLexema() {
        return lexema;
    }
}
