package com.powercouple.pyscal;

import java.io.IOException;
import java.util.Optional;

public interface Lexer {
    void closeFile();
    void printTS();
    Optional<Token> nextToken() throws IOException;
    void updateLineAndColumn(char c);
}
