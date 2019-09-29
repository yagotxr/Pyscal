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

    public void lexicError(String message){
        System.out.println("[Erro Lexico]: " +  message + "\n");
    }


    public void returnPointer() throws IOException {
        if((char)lookahead != '\0') {
            fileReader.seek(fileReader.getFilePointer() - 1);
        }
    }

    public void printTS(){
        st.printST();
    }


    public Token nextToken() throws IOException {
        state = 1;
        lexeme = "";
        c = '\u0000';

        while (true){
            lookahead = fileReader.read();
            c = (char) lookahead;
            if (state == 1){
                if (c == '\0'){
                    return new Token(Tag.EOF.toString(), "EOF", n_line, n_column);
                }
                else if (c == ' ' || c == '\t' || c == '\n' || c == '\r'){
                    state = 1;
                    System.out.println(state);
                }
                else if(Character.isAlphabetic(c)){
                    state = 2;                    System.out.println(state);
                    lexeme += c;
                }
                else if(Character.isDigit(c)){
                    state = 4;                    System.out.println(state);
                    lexeme += c;
                }
                else if(c == '\"'){
                    state = 9;                    System.out.println(state);

                }
                else if(c == '#'){
                    state = 11;                    System.out.println(state);

                }
                else if(c == '<'){
                    state = 12;                    System.out.println(state);

                }
                else if(c == '>'){
                    state = 15;                    System.out.println(state);

                }

                else if(c == '='){
                    state = 18;                    System.out.println(state);

                }
                else if(c == '!'){
                    state = 20;                    System.out.println(state);

                }
                else if(c == '/'){
                    state = 23;                    System.out.println(state);

                }

                else if(c == '*'){
                    state = 24;                    System.out.println(state);

                }

                else if(c == '+'){
                    state = 25;                    System.out.println(state);

                }

                else if(c == '-'){
                    state = 26;                    System.out.println(state);

                }
                else{
                    lexicError("Caractere invalido [" + c + "] na linha " + n_line + " e coluna " + n_column);
                }
                return null;
            }
            else if(state == 2){
                if (Character.isAlphabetic(c) || Character.isDigit(c) || c == '_'){
                    lexeme += c;
                } else {
                    returnPointer();
                    token = st.getToken(lexeme);
                    if(token.isEmpty()){
                        Token newToken = new Token(Tag.ID.toString(), lexeme, n_line, n_column);
                        st.addToken(lexeme, newToken);
                        return newToken;
                    }
                }
            }

        }

    }





}


