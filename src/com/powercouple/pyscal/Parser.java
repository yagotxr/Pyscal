package com.powercouple.pyscal;

import java.io.IOException;

public interface Parser {

    void programa() throws IOException;
    void classe() throws IOException;
    void declaraId() throws IOException;;
    void listaFuncao() throws IOException;;
    void listaFuncao_() throws IOException;;
    void funcao() throws IOException;;
    void regexDeclaraId();
    void listaArg();
    void listaArg_();
    void arg();
    void retorno();
    void main();
    void tipoPremitivo();
    void listaCmd();
    void listaCmd_();
    void cmd();
    void cmdAttibFunc();
    void cmdIf();
    void cmdIf_();
    void cmdWhile();
    void cmdWrite();
    void cmdAtribui();
    void cmdFuncao();
    void regexExp();
    void regexExp_();
    void expressao();
    void exp_();
    void exp1();
    void exp1_();
    void exp2();
    void exp2_();
    void exp3();
    void exp3_();
    void exp4();
    void exp4_();
    void opUnario();
    Lexer getLexer();

}
