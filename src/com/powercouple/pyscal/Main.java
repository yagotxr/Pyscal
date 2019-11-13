package com.powercouple.pyscal;

import com.powercouple.pyscal.impls.LexerImpl;
import com.powercouple.pyscal.impls.ParserImpl;

import java.io.File;
import java.io.IOException;

import static com.powercouple.pyscal.impls.LexerImpl.PATHNAME;


public class Main {

    public static void main(String[] args) throws IOException {

        File file = new File(PATHNAME);
        LexerImpl lexerImpl = new LexerImpl(file);
        Parser parser = new ParserImpl(lexerImpl);

        parser.programa();

        parser.getLexerImpl().closeFile();

        System.out.println("\n=>Tabela de simbolos:");
        lexerImpl.printTS();
        lexerImpl.closeFile();

        System.out.println("\n=> Fim da compilacao");
    }
}
