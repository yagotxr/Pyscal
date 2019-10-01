import Lexer.Lexer;
import Lexer.Token;
import Lexer.Tag;
import java.io.File;
import java.io.IOException;
import java.util.Optional;



public class Main {

    public static void main(String[] args) throws IOException {

        final String FILE_PATH = "/Users/yagohenrique/Google Drive/College/6º Periodo/Automatos, Linguagens Formais e Compiladores/Pyscal/src/";
//        final String FILE_PATH = "/home/carolinne/Pyscal/src/";

        double a=0.12;
        String fileName = "HelloWorld.txt";
        File file = new File(FILE_PATH + fileName);
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
