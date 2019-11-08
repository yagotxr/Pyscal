package com.powercouple.pyscal;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

import static com.powercouple.pyscal.Lexer.PATHNAME;


public class Main {

    public static void main(String[] args) throws IOException {

        File file = new File(PATHNAME);
        Lexer lexer = new Lexer(file);
        Optional<Token> token;

        System.out.println("\n=>Lista de tokens:");
        token = lexer.nextToken();
        while(token.isPresent() && !token.get().getName().equals(Tag.EOF.toString())){
            System.out.println(token.get().toString() + " Linha: " + token.get().getLine() + " Coluna: " + token.get().getColumn());
            token = lexer.nextToken();
        }

        System.out.println("\n=>Tabela de simbolos:");
        lexer.printTS();
        lexer.closeFile();

        System.out.println("\n=> Fim da compilacao");
    }
}
