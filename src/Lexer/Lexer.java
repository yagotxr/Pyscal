package Lexer;

import java.io.*;
import java.util.Optional;

public class Lexer {

    private ST st;
    private RandomAccessFile fileReader;
    private StringBuilder builder;
    private int lookahead;
    private long n_line;
    private long n_column;
    private int state;
    private String lexeme;
    private char c;

    public Lexer(File file) {
        try {
            fileReader = new RandomAccessFile(file, "r");
            lookahead = 0;
            n_line = 1;
            n_column = 1;
            st = new ST();
            state = 1;
            lexeme = "";
            c = '\u0000';
        } catch (IOException ioException) {
            System.out.println("Erro de abertura do arquivo." +
                    "\nVerifique novamente o caminho do arquivo.");
            System.exit(0);
        }
    }


    public void closeFile() {
        try {
            fileReader.close();
        } catch (
                IOException ioException) {
            System.out.println("Erro ao fechar arquivo. Encerrando.");
            System.exit(0);
        }
    }

    private void lexicError() {
        String message = "Caractere invalido [" + c + "] na linha " + n_line + " e coluna " + n_column;
        System.out.println("[Erro Lexico]: " + message + "\n");
    }


    private void returnPointer() throws IOException {
        if ((char) lookahead != '\uFFFF') {
            fileReader.seek(fileReader.getFilePointer() - 1);
        }
    }

    public void printTS() {
        st.printST();
    }


    public Optional<Token> nextToken() throws IOException {
        state = 1;
        lexeme = "";
        c = '\u0000';

            while (true) {
            lookahead = fileReader.read();
            c = (char) lookahead;
            if (1 == state) {
                if (c == '\uFFFF') {
                    return Optional.of(new Token(Tag.EOF.toString(), "EOF", n_line, n_column));
                } else if (c == ' ' || c == '\t' || c == '\n' || c == '\r') {
                    state = 1;
                } else if (Character.isLetter(c)) {
                    state = 2;

                    lexeme += c;
                } else if (Character.isDigit(c)) {
                    state = 4;
                    lexeme += c;
                } else if (c == '"') {
                    state = 9;
                    lexeme += c;
                } else if (c == '#') {
                    state = 11;
                } else if (c == '<') {
                    state = 12;
                    lexeme += c;
                } else if (c == '>') {
                    state = 15;
                    lexeme += c;
                } else if (c == '=') {
                    state = 18;
                    lexeme += c;
                } else if (c == '!') {
                    state = 20;
                    lexeme += c;
                } else if (c == '/') {
                    lexeme += c;
                    //[STATE 23]
                    return createToken(lexeme, Tag.OP_DIVISAO, n_line, n_column);
                } else if (c == '*') {
                    lexeme += c;
                    //[STATE 24]
                    return createToken(lexeme, Tag.OP_MULTIPLICACAO, n_line, n_column);
                } else if (c == '-') {
                    lexeme += c;
                    //[STATE 25]
                    return createToken(lexeme, Tag.OP_SUBTRACAO, n_line, n_column);
                } else if (c == '+') {
                    lexeme += c;
                    //[STATE 26]
                    return createToken(lexeme, Tag.OP_SOMA, n_line, n_column);
                } else if (c == ',') {
                    lexeme += c;
                    //[STATE 27]
                    return createToken(lexeme, Tag.VIRGULA, n_line, n_column);
                } else if (c == '[') {
                    lexeme += c;
                    //[STATE 28]
                    return createToken(lexeme, Tag.A_COLCHETE, n_line, n_column);
                } else if (c == ']') {
                    lexeme += c;
                    //[STATE 29]
                    return createToken(lexeme, Tag.F_COLCHETE, n_line, n_column);
                } else if (c == '(') {
                    lexeme += c;
                    //[STATE 30]
                    return createToken(lexeme, Tag.A_PARENTESES, n_line, n_column);
                } else if (c == ')') {
                    lexeme += c;
                    //[STATE 31]
                    return createToken(lexeme, Tag.F_PARENTESES, n_line, n_column);
                } else if (c == '.') {
                    lexeme += c;
                    //[STATE 32]
                    return createToken(lexeme, Tag.PONTO, n_line, n_column);
                } else if (c == ';') {
                    lexeme += c;
                    //[STATE 33]
                    return createToken(lexeme, Tag.PONTO_VIRGULA, n_line, n_column);
                } else if (c == ':') {
                    lexeme += c;
                    //[STATE 34]
                    return createToken(lexeme, Tag.DOIS_PONTOS, n_line, n_column);
                } else {
                    lexicError();
                    return Optional.empty();
                }
            }

///[STATE 2]///////////////////////////////////////////////////////////////////////////////////////////////////////////

            else if (state == 2) {
                if (Character.isLetterOrDigit(c) || c == '_') {
                    lexeme += c;
                } else {  //[STATE 3]
                    returnPointer();
                    return createToken(lexeme, Tag.ID, n_line, n_column);
                }
            }

///[STATE 4]///////////////////////////////////////////////////////////////////////////////////////////////////////////

            else if (state == 4) {
                if (Character.isDigit(c)) {
                    lexeme += c;
                } else if (c == '.') {
                    state = 6;
                    lexeme += c;
                } else { //[STATE 5]
                    returnPointer();
                    return createToken(lexeme, Tag.CONSTINT, n_line, n_column);
                }
            }

///[STATE 6]///////////////////////////////////////////////////////////////////////////////////////////////////////////

            else if (state == 6) {
                if (Character.isDigit(c)) {
                    state = 7;
                    lexeme += c;
                } else {
                    lexicError();
                    return Optional.empty();
                }
            }

///[STATE 7]///////////////////////////////////////////////////////////////////////////////////////////////////////////

            else if (state == 7) {
                if (Character.isDigit(c)) {
                    lexeme += c;
                } else { //[STATE 8]
                    returnPointer();
                    return createToken(lexeme, Tag.CONSTDOUBLE, n_line, n_column);
                }
            }

///[STATE 9]///////////////////////////////////////////////////////////////////////////////////////////////////////////

            else if (state == 9) {
                if (c != '"') {
                    lexeme += c;
                } else { //[STATE 10]
                    lexeme += c;
                    return createToken(lexeme, Tag.CONSTSTRING, n_line, n_column);
                }
            }

///[STATE 11]///////////////////////////////////////////////////////////////////////////////////////////////////////////

            else if (state == 11) {
                if (c == '\n') {
                    state = 1;
                }
            }

///[STATE 12]///////////////////////////////////////////////////////////////////////////////////////////////////////////

            else if (state == 12) {
                if (c == '=') {
                    state = 13; //[STATE 13]
                    lexeme += c;
                    return createToken(lexeme, Tag.OP_MENOR_IGUAL, n_line, n_column);
                } else { //[STATE 14]
                    return createToken(lexeme, Tag.OP_MENOR, n_line, n_column);
                }
            }

///[STATE 15]///////////////////////////////////////////////////////////////////////////////////////////////////////////

            else if (state == 15) {
                if (c == '=') {
                    state = 16;//[STATE 16]
                    lexeme += c;
                    return createToken(lexeme, Tag.OP_MAIOR_IGUAL, n_line, n_column);
                } else { //[STATE 17]
                    return createToken(lexeme, Tag.OP_MAIOR, n_line, n_column);
                }
            }

///[STATE 18]///////////////////////////////////////////////////////////////////////////////////////////////////////////

            else if(state == 18){
                if(c == '='){
                    state = 19;
                    lexeme += c;
                    return createToken(lexeme, Tag.OP_COMPARACAO, n_line, n_column);
                } else { //[STATE 38]
                    returnPointer();
                    return createToken(lexeme, Tag.OP_IGUAL, n_line, n_column);
                }
            }
///[STATE 20]///////////////////////////////////////////////////////////////////////////////////////////////////////////

            else if(state == 20){
                if(c == '='){
                    state=22;//[STATE 19]
                    lexeme += c;
                    return createToken(lexeme, Tag.OP_DIFERENTE, n_line, n_column);
                }else{ //[STATE 21]
                    return createToken(lexeme, Tag.OP_IGUAL, n_line, n_column);
                }
            }
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        }//end while
    } // end nextToken()
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private Optional<Token> createToken(String lexeme, Tag tag, long n_line, long n_column) throws IOException {
        Optional<Token> token = st.getToken(lexeme);
        if (token.isEmpty()) {
            Token newToken = new Token(tag.toString(), lexeme, n_line, n_column);
            st.addToken(lexeme, newToken);
            return Optional.of(newToken);
        }
        return token;
    }

    public void setN_line(){
        this.n_line++;
    }

}


