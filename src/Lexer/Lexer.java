package Lexer;

import java.io.*;
import java.util.Optional;

public class Lexer {

    private ST st;
    private RandomAccessFile fileReader;
    private StringBuilder builder;
    private int lookahead;
    private long line;
    private long column;
    long atColumn;
    private int state;
    private String lexeme;
    private char c;
    private int nErros;

    public Lexer(File file) {
        try {
            fileReader = new RandomAccessFile(file, "r");
            lookahead = 0;
            line = 1;
            column = 0;
            st = new ST();
            state = 1;
            lexeme = "";
            c = '\u0000';
            nErros = 0;
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

    private void lexicError(String message) {
        System.out.println("////////////////[Erro Lexico]: " + message + "\n");
    }


    private void returnPointer() throws IOException {
        if ((char) lookahead != '\uFFFF') {
            column--;
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
            if(nErros == 5){
                return Optional.empty();
            }
            lookahead = fileReader.read();
            c = (char) lookahead;
            updateLineAndColumn(c);
            if (1 == state) {
                if (c == '\uFFFF') {
                    return Optional.of(new Token(Tag.EOF.toString(), "EOF", line, column));
                } else if (c == ' ' || c == '\t' || c == '\n' || c == '\r') {
                    state = 1;
                } else if (Character.isLetter(c)) {
                    state = 2;
                    atColumn = column;
                    lexeme += c;
                } else if (Character.isDigit(c)) {
                    state = 4;
                    lexeme += c;
                    atColumn = column;
                } else if (c == '"') {
                    state = 9;
                    atColumn = column;
                } else if (c == '#') {
                    state = 11;
                } else if (c == '<') {
                    state = 12;
                    lexeme += c;
                    atColumn = column;
                } else if (c == '>') {
                    state = 15;
                    lexeme += c;
                    atColumn = column;
                } else if (c == '=') {
                    state = 18;
                    lexeme += c;
                    atColumn = column;
                } else if (c == '!') {
                    state = 20;
                    lexeme += c;
                    atColumn = column;
                } else if (c == '/') {
                    lexeme += c;
                    atColumn = column;
                    //[STATE 23]
                    return returnToken(lexeme, Tag.OP_DIVISAO, line, atColumn);
                } else if (c == '*') {
                    lexeme += c;
                    atColumn = column;
                    //[STATE 24]
                    return returnToken(lexeme, Tag.OP_MULTIPLICACAO, line, atColumn);
                } else if (c == '-') {
                    lexeme += c;
                    atColumn = column;
                    if(previousIsNumber()){// [STATE 25]
                        return returnToken(lexeme, Tag.OP_SUBTRACAO, line, atColumn);
                    } else {
                        state = 1;
                    }
                } else if (c == '+') {
                    lexeme += c;
                    atColumn = column;
                    //[STATE 26]
                    return returnToken(lexeme, Tag.OP_SOMA, line, atColumn);
                } else if (c == ',') {
                    lexeme += c;
                    atColumn = column;
                    //[STATE 27]
                    return returnToken(lexeme, Tag.VIRGULA, line, atColumn);
                } else if (c == '[') {
                    lexeme += c;
                    atColumn = column;
                    //[STATE 28]
                    return returnToken(lexeme, Tag.A_COLCHETE, line, atColumn);
                } else if (c == ']') {
                    lexeme += c;
                    atColumn = column;
                    //[STATE 29]
                    return returnToken(lexeme, Tag.F_COLCHETE, line, atColumn);
                } else if (c == '(') {
                    lexeme += c;
                    atColumn = column;
                    //[STATE 30]
                    return returnToken(lexeme, Tag.A_PARENTESES, line, atColumn);
                } else if (c == ')') {
                    lexeme += c;
                    atColumn = column;
                    //[STATE 31]
                    return returnToken(lexeme, Tag.F_PARENTESES, line, atColumn);
                } else if (c == '.') {
                    lexeme += c;
                    atColumn = column;
                    //[STATE 32]
                    return returnToken(lexeme, Tag.PONTO, line, atColumn);
                } else if (c == ';') {
                    lexeme += c;
                    atColumn = column;
                    //[STATE 33]
                    return returnToken(lexeme, Tag.PONTO_VIRGULA, line, atColumn);
                } else if (c == ':') {
                    lexeme += c;
                    atColumn = column;
                    //[STATE 34]
                    return returnToken(lexeme, Tag.DOIS_PONTOS, line, atColumn);
                } else { //Panico
                    lexicError("Error:(" + line + "," + column + ") Invalid token [" + c + "]");
                    nErros++;
                }
            }

///[STATE 2]///////////////////////////////////////////////////////////////////////////////////////////////////////////

            else if (state == 2) {
                if (Character.isLetterOrDigit(c) || c == '_') {
                    lexeme += c;
                } else {  //[STATE 3]
                    returnPointer();
                    return createToken(lexeme, Tag.ID, line, atColumn);
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
                    return createToken(lexeme, Tag.CONSTINT, line, atColumn);
                }
            }

///[STATE 6]///////////////////////////////////////////////////////////////////////////////////////////////////////////

            else if (state == 6) {
                if (Character.isDigit(c)) {
                    state = 7;
                    lexeme += c;
                }  else {
                    lexicError("Error:(" + line + "," + column + ") Caracter inválido [" + c + "]");
                    nErros++;
                    return panic();
                }
            }

///[STATE 7]///////////////////////////////////////////////////////////////////////////////////////////////////////////

            else if (state == 7) {
                if (Character.isDigit(c)) {
                    lexeme += c;
                }  else { //[STATE 5]
                    returnPointer();
                    return createToken(lexeme, Tag.CONSTDOUBLE, line, atColumn);
                }
            }

///[STATE 9]///////////////////////////////////////////////////////////////////////////////////////////////////////////

            else if (state == 9) {
                if(c == '\n'){
                    lexicError("Unclosed String literal");
                    nErros++;
                    return Optional.empty();
                }
                else if (c != '"') {
                    lexeme += c;
                }
                else { //[STATE 10]
                    return createToken(lexeme, Tag.CONSTSTRING, line, atColumn);
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
                    return returnToken(lexeme, Tag.OP_MENOR_IGUAL, line, atColumn);
                } else { //[STATE 14]
                    return returnToken(lexeme, Tag.OP_MENOR, line, atColumn);
                }
            }

///[STATE 15]///////////////////////////////////////////////////////////////////////////////////////////////////////////

            else if (state == 15) {
                if (c == '=') {
                    state = 16;//[STATE 16]
                    lexeme += c;
                    return returnToken(lexeme, Tag.OP_MAIOR_IGUAL, line, atColumn);
                } else { //[STATE 17]
                    return returnToken(lexeme, Tag.OP_MAIOR, line, atColumn);
                }
            }

///[STATE 18]///////////////////////////////////////////////////////////////////////////////////////////////////////////

            else if(state == 18){
                if(c == '='){
                    state = 19;
                    lexeme += c;
                    return returnToken(lexeme, Tag.OP_COMPARACAO, line, atColumn);
                } else { //[STATE 38]
                    returnPointer();
                    return returnToken(lexeme, Tag.OP_IGUAL, line, atColumn);
                }
            }
///[STATE 20]///////////////////////////////////////////////////////////////////////////////////////////////////////////

            else if(state == 20){
                if(c == '='){
                    state=22;//[STATE 19]
                    lexeme += c;
                    return returnToken(lexeme, Tag.OP_DIFERENTE, line, atColumn);
                }else{ //[STATE 21]
                    return returnToken(lexeme, Tag.OP_IGUAL, line, atColumn);
                }
            }
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        }//end while
    } // end nextToken()

    private Optional<Token> panic() throws IOException {
        if(state == 6){
            while(!Character.isDigit(c)){
                c = (char) fileReader.read();
                if(c == '\uFFFF'){
                    return Optional.empty();
                }
            }
            lexeme += c;
            return createToken(lexeme, Tag.OP_IGUAL, line, atColumn);
        }
        return Optional.empty();
    }

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private Optional<Token> createToken(String lexeme, Tag tag, long n_line, long n_column) throws IOException {
        Optional<Token> token = st.getToken(lexeme);
        if(token.isPresent()){
            token.get().setColumn(n_column);
            token.get().setLine(n_line);
            return token;
        } else {
            Token newToken = new Token(tag.toString(), lexeme, n_line, n_column);
            st.addToken(lexeme, newToken);
            return Optional.of(newToken);
        }

    }

    private Optional<Token> returnToken(String lexeme, Tag tag, long n_line, long n_column){
        return Optional.of(new Token(tag.toString(), lexeme, n_line, n_column));
    }

    public void updateLineAndColumn(char c){
        if(c == '\n'){
            line++;
            column = -1;
        } if(c == '\t'){
            column += 5;
        } else {
            column++;
        }
    }
    private boolean previousIsNumber() throws IOException {
        long pointerLocation = fileReader.getFilePointer();
        char seenChar;
        try {
            fileReader.seek(fileReader.getFilePointer() - 2);
            seenChar = (char) fileReader.read();
            while ((seenChar == ' ' || seenChar == '\n' || seenChar == '\t' || seenChar == '\r')) {
                fileReader.seek(fileReader.getFilePointer() - 2);
                seenChar = (char) fileReader.read();
            }
        } catch (IOException ioEx) {
            return false;
        }

        fileReader.seek(pointerLocation);
        return Character.isDigit(seenChar);
    }


}


