import java.io.*;
import java.util.Optional;

public class Lexer {

    private ST st;
    //    private FileReader fileReader;
    RandomAccessFile fileReader;
    private int lookahead;
    private int n_line;
    private int n_column;
    private int state;
    private String lexeme;
    private char c;
    private Optional<Token> token;

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
            System.out.println("Erro de abertura do arquivo. Encerrando.");
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

    public void lexicError(){
        String message = "Caractere invalido [" + c + "] na linha " + n_line + " e coluna " + n_column;
        System.out.println("[Erro Lexico]: " +  message + "\n");
    }


    public void returnPointer() throws IOException {
        if((char)lookahead != '\uFFFF') {
            fileReader.seek(fileReader.getFilePointer() - 1);
        }
    }

    public void printTS(){
        st.printST();
    }


    public Optional<Token> nextToken() throws IOException {
        state = 1;
        lexeme = "";
        c = '\u0000';

        while (true) {
            lookahead = fileReader.read();
            c = (char) lookahead;
            if (state == 1) {
                if (c == '\uFFFF') {
                    return Optional.of(new Token(Tag.EOF.toString(), "EOF", n_line, n_column));
                } else if (c == ' ' || c == '\t' || c == '\n' || c == '\r') {
                    state = 1;
                } else if (Character.isAlphabetic(c)) {
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
                } else if (c == '>') {
                    state = 15;
                } else if (c == '=') {
                    state = 18;
                } else if (c == '!') {
                    state = 20;
                } else if (c == '/') {
                    state = 23;
                } else if (c == '*') {
                    state = 24;
                } else if (c == '-') {
                    state = 25;
                } else if (c == '+') {
                    state = 26;
                } else if (c == ',') {
                    state = 27;
                } else if (c == '[') {
                    state = 28;
                } else if (c == ']') {
                    state = 29;
                } else if (c == '(') {
                    state = 30;
                } else if (c == ')') {
                    state = 31;
                } else if (c == '.') {
                    state = 32;
                } else if (c == ';') {
                    state = 33;
                } else if (c == ':') {
                    state = 34;
                } else {
                    lexicError();
                    return Optional.empty();
                }
            }

///[STATE 2]///////////////////////////////////////////////////////////////////////////////////////////////////////////

            else if (state == 2) {
                if (Character.isAlphabetic(c) || Character.isDigit(c) || c == '_') {
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

       else if(state == 9) {
                if (c != '"') {
                    lexeme += c;
                } else { //[STATE 10]
                    lexeme += c;
                    return createToken(lexeme, Tag.CONSTSTRING, n_line, n_column);
                }
            }

///[STATE 11]///////////////////////////////////////////////////////////////////////////////////////////////////////////

        else if(state == 11){
            if(c == '\n'){
                state = 1;
            }
            }

///[STATE 12]///////////////////////////////////////////////////////////////////////////////////////////////////////////



        }
    }
        public Optional<Token> createToken (String lexeme, Tag tag,int n_line, int n_column) throws IOException {
            token = st.getToken(lexeme);
            if (token.isEmpty()) {
                Token newToken = new Token(tag.toString(), lexeme, n_line, n_column);
                st.addToken(lexeme, newToken);
                return Optional.of(newToken);
            }
            return Optional.empty();
        }







}


