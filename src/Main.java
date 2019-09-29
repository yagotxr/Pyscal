import java.io.FileReader;
import java.io.IOException;
import java.io.RandomAccessFile;

public class Main {

    public static void main(String[] args) throws IOException {


//        try (FileReader fileReader = new FileReader("/Users/yagohenrique/Google Drive/College/6º Periodo/Automatos, Linguagens Formais e Compiladores/src/HelloWorld.txt")) {
//            int singleCharInt;
//            char singleChar;
//            while((singleCharInt = fileReader.read()) != -1) {
//                singleChar = (char) singleCharInt;
//
//                //display one character at a time
//                System.out.println(singleChar);
//                System.out.println(singleCharInt);
//            }
//        }

        RandomAccessFile reader = new RandomAccessFile("/Users/yagohenrique/Google Drive/College/6º Periodo/Automatos, Linguagens Formais e Compiladores/src/HelloWorld.txt", "r");
        int singleCharInt;
        char singleChar;
            while((singleCharInt = reader.read()) != -1) {
                singleChar = (char) singleCharInt;

                //display one character at a time

                System.out.println("Poiter: " + reader.getFilePointer());
                if(singleChar == 's'){
                    reader.seek(reader.getFilePointer() - 1);
                }
                System.out.println(singleChar);
                System.out.println(singleCharInt);
            }
    }
}
