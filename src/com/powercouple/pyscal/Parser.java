package com.powercouple.pyscal;

import java.io.IOException;

public class Parser{

    private Lexer lexer;
    private Token token;

    public Parser(Lexer lexer) throws IOException {
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

    public void runParser(){
        this.cmd();
        if(!this.token.getName().equals(Tag.EOF.toString())){
            this.sintaticError("Esperado \"EOF\"; encontrado " + "\"" + this.token.getLexeme() + "\"");
        }
    }

    public void cmd(){
        //Parei aqui
    }

    public Lexer getLexer() {
        return lexer;
    }
}
