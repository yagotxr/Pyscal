package com.powercouple.pyscal;

import java.io.IOException;

public interface Parser {

    void programa() throws IOException;
    void classe() throws IOException;
    void declaraId() throws IOException;;
    void listaFuncao() throws IOException;;
    void listaFuncao_() throws IOException;;
    void funcao() throws IOException;;
    void regexDeclaraId() throws IOException;
    void listaArg() throws IOException;
    void listaArg_() throws IOException;
    void arg() throws IOException;
    void retorno() throws IOException;
    void main() throws IOException;
    void tipoPrimitivo() throws IOException;
    void listaCmd() throws IOException;
    void listaCmd_() throws IOException;
    void cmd() throws IOException;
    void cmdAttibFunc() throws IOException;
    void cmdIf() throws IOException;
    void cmdIf_() throws IOException;
    void cmdWhile() throws IOException;
    void cmdWrite() throws IOException;
    void cmdAtribui() throws IOException;
    void cmdFuncao() throws IOException;
    void regexExp() throws IOException;
    void regexExp_() throws IOException;
    void expressao() throws IOException;
    void exp_() throws IOException;
    void exp1() throws IOException;
    void exp1_() throws IOException;
    void exp2();
    void exp2_();
    void exp3();
    void exp3_();
    void exp4();
    void exp4_();
    void opUnario();
    Lexer getLexer();

}
