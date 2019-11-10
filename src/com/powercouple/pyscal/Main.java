package com.powercouple.pyscal;

import com.powercouple.pyscal.impls.ParserImpl;

import java.io.File;
import java.io.IOException;

import static com.powercouple.pyscal.Lexer.PATHNAME;


public class Main {

    public static void main(String[] args) throws IOException {

        File file = new File(PATHNAME);
        Lexer lexer = new Lexer(file);
        Parser parser = new ParserImpl(lexer);

        parser.programa();

        parser.getLexer().closeFile();

        System.out.println("\n=>Tabela de simbolos:");
        lexer.printTS();
        lexer.closeFile();

        System.out.println("\n=> Fim da compilacao");
    }
}
