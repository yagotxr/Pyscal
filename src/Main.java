import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Optional;

public class Main {

    public static void main(String[] args) throws IOException {

        final String FILE_PATH = "/Users/yagohenrique/Google Drive/College/6º Periodo/Automatos, Linguagens Formais e Compiladores/src/HelloWorld.txt";
        File file = new File(FILE_PATH);
        Lexer lexer = new Lexer(file);
        Optional<Token> token;

        lexer = Lexer('HelloWorld.txt')

        System.out.println("\n=>Lista de tokens:");

        while(token !=  and token.getNome() != Tag.EOF):
        print(token.toString(), "Linha: " + str(token.getLinha()) + " Coluna: " + str(token.getColuna()))
        token = lexer.proxToken()

        print("\n=>Tabela de simbolos:")
        lexer.printTS()
        lexer.closeFile()

        print('\n=> Fim da compilacao')
    }
}
