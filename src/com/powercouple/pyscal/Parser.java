package com.powercouple.pyscal;

import com.powercouple.pyscal.impls.LexerImpl;

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
    No retorno() throws IOException;
    void main() throws IOException;
    No tipoPrimitivo() throws IOException;
    void listaCmd() throws IOException;
    void listaCmd_() throws IOException;
    void cmd() throws IOException;
    No cmdAtribFunc() throws IOException;
    void cmdIf() throws IOException;
    void cmdIf_() throws IOException;
    void cmdWhile() throws IOException;
    void cmdWrite() throws IOException;
    No cmdAtribui() throws IOException;
    void cmdFuncao() throws IOException;
    void regexExp() throws IOException;
    void regexExp_() throws IOException;
    No expressao() throws IOException;
    No exp_() throws IOException;
    No exp1() throws IOException;
    No exp1_() throws IOException;
    No exp2() throws IOException;
    No exp2_() throws IOException;
    No exp3() throws IOException;
    No exp3_() throws IOException;
    No exp4() throws IOException;
    void exp4_() throws IOException;
    No opUnario() throws IOException;
    LexerImpl getLexerImpl();

}
