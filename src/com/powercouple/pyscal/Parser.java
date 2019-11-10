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
    void exp2() throws IOException;
    void exp2_() throws IOException;
    void exp3() throws IOException;
    void exp3_() throws IOException;
    void exp4() throws IOException;
    void exp4_() throws IOException;
    void opUnario() throws IOException;
    Lexer getLexer();

}
