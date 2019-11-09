package com.powercouple.pyscal;

import java.io.IOException;

public class ParserImpl implements Parser{

    private Lexer lexer;
    private Token token;

    public ParserImpl(Lexer lexer) throws IOException {
        this.lexer = lexer;
        this.token = lexer.nextToken().orElseThrow(() -> new RuntimeException("Token not found"));
    }

    public void sintaticError(String msg){
        System.out.println(">>>>>>>>>>>>>>>>>> [Erro Sintatico] na linha " + token.getLine() + " e coluna " + token.getColumn() + ": ");
        System.out.println(msg + "\n");
    }

    public void advance() throws IOException {
        System.out.println("[DEBUG] token: " + token.toString());
        this.token = lexer.nextToken().orElseThrow(() -> new RuntimeException("Token not found"));
    }

    public void skip(String msg) throws IOException {
        this.sintaticError(msg);
        this.advance();
    }

    public boolean eat(String token) throws IOException {
        if(this.token.getName().equals(token)){
            this.advance();
            return true;
        }
        return false;
    }

    //PROGRAMA -> CLASSE EOF
    @Override
    public void programa() throws IOException {
        this.classe();
        if(!this.token.getName().equals(Tag.EOF.toString()))
            this.sintaticError("Esperado \"EOF\"; encontrado " + "\"" + this.token.getLexeme() + "\"");
    }

    //CLASSE -> class ID : LISTAFUNCAO MAIN end .
    @Override
    public void classe() throws IOException {
        if(this.eat(Tag.KW_CLASS.toString())) {
             if (!this.eat(Tag.ID.toString()))
                this.sintaticError("Esperado \"ID\"; encontrado " + "\"" + this.token.getLexeme() + "\"");
            else if (!this.eat(Tag.DOIS_PONTOS.toString()))
                this.sintaticError("Esperado \":\"; encontrado " + "\"" + this.token.getLexeme() + "\"");
            this.listaFuncao();
            this.main();
            if (!this.eat(Tag.KW_END.toString()))
                this.sintaticError("Esperado \"end\"; encontrado " + "\"" + this.token.getLexeme() + "\"");
            if (!this.eat(Tag.PONTO.toString()))
                this.sintaticError("Esperado \".\"; encontrado " + "\"" + this.token.getLexeme() + "\"");
        } else {
            this.sintaticError("Esperado \"class\"; encontrado " + "\"" + this.token.getLexeme() + "\"");
        }
    }

    @Override
    //DECLARAID -> TIPOPRIMITIVO ID ;
    public void declaraId() throws IOException {
        this.tipoPremitivo();
        if(!eat(Tag.ID.toString()))
            this.sintaticError("Esperado \"ID\"; encontrado " + "\"" + this.token.getLexeme() + "\"");
        if(!eat(Tag.PONTO_VIRGULA.toString())){
            this.sintaticError("Esperado \";\"; encontrado " + "\"" + this.token.getLexeme() + "\"");
        }
    }

    @Override
    //LISTAFUNCAO -> LISTAFUNCAO_
    public void listaFuncao() {
        listaFuncao_();
    }

    @Override
    //LISTAFUNCAO_ -> FUNCAO LISTAFUNCAO_ | ε
    public void listaFuncao_() {
        //LISTAFUNCAO_ -> FUNCAO LISTAFUNCAO_
        funcao();
        listaFuncao_();

    }

    @Override
    public void funcao() {

    }

    @Override
    public void regexDeclaraId() {

    }

    @Override
    public void listaArg() {

    }

    @Override
    public void listaArg_() {

    }

    @Override
    public void arg() {

    }

    @Override
    public void retorno() {

    }

    @Override
    public void main() {

    }

    @Override
    public void tipoPremitivo() {

    }

    @Override
    public void listaCmd() {

    }

    @Override
    public void listaCmd_() {

    }

    @Override
    public void cmd() {

    }

    @Override
    public void cmdAttibFunc() {

    }

    @Override
    public void cmdIf() {

    }

    @Override
    public void cmdIf_() {

    }

    @Override
    public void cmdWhile() {

    }

    @Override
    public void cmdWrite() {

    }

    @Override
    public void cmdAtribui() {

    }

    @Override
    public void cmdFuncao() {

    }

    @Override
    public void regexExp() {

    }

    @Override
    public void regexExp_() {

    }

    @Override
    public void expressao() {

    }

    @Override
    public void exp_() {

    }

    @Override
    public void exp1() {

    }

    @Override
    public void exp1_() {

    }

    @Override
    public void exp2() {

    }

    @Override
    public void exp2_() {

    }

    @Override
    public void exp3() {

    }

    @Override
    public void exp3_() {

    }

    @Override
    public void exp4() {

    }

    @Override
    public void exp4_() {

    }

    @Override
    public void opUnario() {

    }

    @Override
    public Lexer getLexer() {
        return lexer;
    }
}
