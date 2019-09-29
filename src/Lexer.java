import java.io.*;

public class Lexer {

    private ST st;
//    private FileReader fileReader;
    RandomAccessFile fileReader;
    private int lookahead;
    private int n_line;
    private int n_column;
    private int estado;
    private String lexema;
    private char c;

    public Lexer(File file) {
        try {
            fileReader = new RandomAccessFile(file, "r");
            lookahead = 0;
            n_line = 1;
            n_column = 1;
            st = new ST();
            estado = 1;
            lexema = "";
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

    public void sinalizaErroLexico(String message){
        System.out.println("[Erro Lexico]: " +  message + "\n");
    }


    public void retornaPonteiro() throws IOException {
        if((char)lookahead != '\0') {
            fileReader.seek(fileReader.getFilePointer() - 1);
        }
    }


    public void printTS(){
        st.printST();
    }


    public void proxToken(){
        estado = 1;
        lexema = "";
        c = '\u0000';
    }



}


