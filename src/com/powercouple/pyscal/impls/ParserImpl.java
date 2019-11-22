package com.powercouple.pyscal.impls;

import com.powercouple.pyscal.Parser;
import com.powercouple.pyscal.Tag;
import com.powercouple.pyscal.Token;

import java.io.IOException;
import java.util.Arrays;

public class ParserImpl implements Parser {

    private LexerImpl lexerImpl;
    private Token token;

    public ParserImpl(LexerImpl lexerImpl) throws IOException {
        this.lexerImpl = lexerImpl;
        this.token = lexerImpl.nextToken().orElseThrow(() -> new RuntimeException("Token not found"));
    }

    private void semanticError(String message){
        System.out.print(">>>>>>>>>>>>>>>>>>>[Erro Semantico] Error:(" + token.getLine() + "," + token.getColumn() + ") ");
        System.out.println(message);
    }

    private void sintaticError(String... esperados){
        System.out.print(">>>>>>>>>>>>>>>>>>>[Erro Sintatico] Error:(" + token.getLine() + "," + token.getColumn() + ") ");
        System.out.println("Esperado: " + printWaitedTokens(esperados) + "\"; encontrado " + "\"" + token.getName() + "\"" + "\n");
    }

    private void advance() throws IOException {
        System.out.println("[DEBUG] token: " + token.toString());
        try {
            this.token = lexerImpl.nextToken().orElseThrow(RuntimeException::new);
        } catch(RuntimeException re) {
            System.out.println("[Token not found]");
        }

    }

    private void skip(String... esperados) throws IOException {
        this.sintaticError(esperados);
        this.advance();
    }

    private boolean eat(Tag... token) throws IOException {
        for (Tag t : token) {
            if (this.token.getName().equals(t.toString())) {
                this.advance();
                return true;
            }
        }
        return false;
    }

    //Programa	→ Classe EOF 1
    @Override
    public void programa() throws IOException {
        //1
        if(isNext(Tag.KW_CLASS))

            classe();

        if(!eat(Tag.EOF))
            sintaticError("EOF");
    }

    //Classe → "class" ID ":" ListaFuncao Main "end" "." 2
    @Override
    public void classe() throws IOException {
        //2
        if (eat(Tag.KW_CLASS)) {
            if (!eat(Tag.ID))
                sintaticError("ID");

            if (!eat(Tag.DOIS_PONTOS))
                sintaticError(":");

            listaFuncao();
            main();

            if (!eat(Tag.KW_END))
                sintaticError("end");

            if (!eat(Tag.PONTO))
                sintaticError(".");

        } else
            sintaticError("class");
    }

    @Override
    //DeclaraID	→ TipoPrimitivo ID ";" 3
    public void declaraId() throws IOException {
        if(isNext(Tag.KW_VOID, Tag.KW_STRING, Tag.KW_BOOL, Tag.KW_INTEGER, Tag.KW_DOUBLE)){

            tipoPrimitivo();

            if(!eat(Tag.ID))
                sintaticError("ID");

            if(!eat(Tag.PONTO_VIRGULA))
                sintaticError(";");

        } else
            skip("void", "String", "bool", "int", "double", "ID", ";");
    }

    @Override
    //ListaFuncao → ListaFuncao’ 4
    public void listaFuncao() throws IOException {
        if(isNext(Tag.KW_DEF, Tag.KW_DEFSTATIC))
            listaFuncao_();

        else
            skip("def", "defstatic");
    }

    @Override
    //ListaFuncao’ → Funcao ListaFuncao’ 5 | ε 6
    public void listaFuncao_() throws IOException {
        if(isNext(Tag.KW_DEF)){
            funcao();

            listaFuncao_();

            return;
        }

        if(!isNext(Tag.KW_DEFSTATIC))
            skip("def", "defstatic");
    }

    @Override
    //Funcao → "def" TipoPrimitivo ID "(" ListaArg ")" ":" RegexDeclaraId ListaCmd Retorno "end" ";" 7
    public void funcao() throws IOException {
        if(eat(Tag.KW_DEF)){

            tipoPrimitivo();

            if(!eat(Tag.ID))
                sintaticError("ID");

            if(!eat(Tag.ABRE_PARENTESES))
                sintaticError("(");

            listaArg();

            if(!eat(Tag.FECHA_PARENTESES))
                sintaticError(")");

            if(!eat(Tag.DOIS_PONTOS))
                sintaticError(":");

            regexDeclaraId();

            listaCmd();

            retorno();

            if(!eat(Tag.KW_END))
                sintaticError("end");

            if(!eat(Tag.PONTO_VIRGULA))
                sintaticError(";");
        }

        else {
            sintaticError("def");
        }
    }

    @Override
    //RegexDeclaraId → DeclaraID RegexDeclaraId 8 | ε 9
    public void regexDeclaraId() throws IOException {
        if (isNext(Tag.KW_VOID, Tag.KW_STRING, Tag.KW_BOOL, Tag.KW_INTEGER, Tag.KW_DOUBLE)) {

            declaraId();

            regexDeclaraId();

            return;
        }

        if (!isNext(Tag.ID, Tag.KW_END, Tag.KW_RETURN, Tag.KW_IF, Tag.KW_WHILE, Tag.KW_WRITE)) {
            skip("void", "String", "bool", "integer", "double", "ID", "end", "return", "if", "while", "write");
        }
    }

    @Override
    //ListaArg → Arg ListaArg’ 10
    public void listaArg() throws IOException {
        if(isNext(Tag.KW_VOID, Tag.KW_STRING, Tag.KW_BOOL, Tag.KW_INTEGER, Tag.KW_DOUBLE)){

            arg();

            listaArg_();
        }

        else {
            skip("void", "String", "bool", "integer", "double");
        }

    }

    @Override
    //ListaArg’	→ "," ListaArg 11 | ε 12
    public void listaArg_() throws IOException {
        if(eat(Tag.VIRGULA)){
            listaArg();
            return;
        }

        if(!isNext(Tag.FECHA_PARENTESES))
            skip(",",  ")");
    }

    @Override
    //Arg → TipoPrimitivo ID 13
    public void arg() throws IOException {
        if(isNext(Tag.KW_VOID, Tag.KW_STRING, Tag.KW_BOOL, Tag.KW_INTEGER, Tag.KW_DOUBLE)){

            tipoPrimitivo();

            if(!eat(Tag.ID))
                sintaticError("ID");
        }

        else {
            skip("void", "String", "bool", "integer", "double");
        }
    }

    @Override
    //Retorno	→ "return" Expressao ";" 14 | ε 15
    public void retorno() throws IOException {
        if(eat(Tag.KW_RETURN)){

            expressao();

            if(!eat(Tag.PONTO_VIRGULA))
                sintaticError(";");
            return;
        }

        if(!isNext(Tag.KW_END)){
            skip("void", "String", "bool", "integer", "double");
        }
    }

    @Override
    //Main	→ "defstatic" "void" "main" "(" "String" "[" "]" ID ")" ":" RegexDeclaraId ListaCmd "end" ";" 16
    public void main() throws IOException {
        if(eat(Tag.KW_DEFSTATIC)){

            if(!eat(Tag.KW_VOID))
                sintaticError("void");

            if(!eat(Tag.KW_MAIN))
                sintaticError("main");

            if(!eat(Tag.ABRE_PARENTESES))
                sintaticError("(");

            if(!eat(Tag.KW_STRING))
                sintaticError("String");

            if(!eat(Tag.ABRE_COLCHETE))
                sintaticError("[");

            if(!eat(Tag.FECHA_COLCHETE))
                sintaticError("]");

            if(!eat(Tag.ID))
                sintaticError("ID");

            if(!eat(Tag.FECHA_PARENTESES))
                sintaticError(")");

            if(!eat(Tag.DOIS_PONTOS))
                sintaticError(":");

            regexDeclaraId();

            listaCmd();

            if(!eat(Tag.KW_END))
                sintaticError("end");

            if(!eat(Tag.PONTO_VIRGULA))
                sintaticError(";");

        }

        else {
            skip("defstatic");
        }
    }

    @Override
    //TipoPrimitivo → "bool" 17 | "integer" 18 | "String" 19 | "double" 20 | "void" 21
    public void tipoPrimitivo() throws IOException {
        if(!eat(Tag.KW_BOOL, Tag.KW_INTEGER, Tag.KW_STRING, Tag.KW_DOUBLE, Tag.KW_VOID))
            sintaticError("bool", "integer", "String", "double", "void");
    }

    @Override
    //ListaCmd	→ ListaCmd’  22
    public void listaCmd() throws IOException {
        if(isNext(Tag.ID, Tag.KW_IF, Tag.KW_WHILE, Tag.KW_WRITE, Tag.KW_RETURN))

            listaCmd_();

        else
            skip("ID", "if", "while", "write");
    }

    @Override
    //ListaCmd’	→ Cmd ListaCmd’ 23 | ε 24
    public void listaCmd_() throws IOException {
        if(isNext(Tag.ID,Tag.KW_IF,Tag.KW_WHILE,Tag.KW_WRITE)){

            cmd();

            listaCmd_();

            return;
        }

        if(!isNext(Tag.KW_END,Tag.KW_RETURN,Tag.KW_ELSE)){
            skip("ID", "if", "while", "write", "end", "return", "else");
        }
    }

    @Override
    //Cmd → CmdIF 25 | CmdWhile 26 | ID CmdAtribFunc 27 | CmdWrite 28
    public void cmd() throws IOException {
        if(eat(Tag.ID)){
            cmdAttibFunc();
            return;
        }

        if(isNext(Tag.KW_IF)){
            cmdIf();
            return;
        }


        if(isNext(Tag.KW_WHILE)){
            cmdWhile();
            return;
        }


        if(isNext(Tag.KW_WRITE)){
            cmdWrite();
        }

        else
            sintaticError("ID", "if", "while", "write");

    }

    @Override
    //CmdAtribFunc → CmdAtribui 29 | CmdFuncao 30
    public void cmdAttibFunc() throws IOException {
        if(isNext(Tag.ABRE_PARENTESES)){
            cmdFuncao();
            return;
        }

        if(isNext(Tag.OP_IGUAL)){
            cmdAtribui();
        }

        else {
            skip("(", "=");
        }
    }

    @Override
    //CmdIF	→ "if" "(" Expressao ")" ":" ListaCmd CmdIF’ 31
    public void cmdIf() throws IOException {
        if(!eat(Tag.KW_IF))
            sintaticError("if");

        if(!eat(Tag.ABRE_PARENTESES))
            sintaticError("(");

        expressao();

        if(!eat(Tag.FECHA_PARENTESES))
            sintaticError(")");

        if(!eat(Tag.DOIS_PONTOS))
            sintaticError(":");

        listaCmd();

        cmdIf_();
    }

    @Override
    //CmdIF’ → "end" ";" 32 | "else" ":" ListaCmd "end" ";" 33
    public void cmdIf_() throws IOException {
        if(eat(Tag.KW_END)){

            if(!eat(Tag.PONTO_VIRGULA))
                sintaticError(";");

            return;
        }

        if(eat(Tag.KW_ELSE)){

            if(!eat(Tag.DOIS_PONTOS))
                sintaticError(":");

            listaCmd();

            if(!eat(Tag.KW_END))
                sintaticError("end");

            if(!eat(Tag.PONTO_VIRGULA))
                sintaticError(";");

        }

        else
            sintaticError("end, else");
    }

    @Override
    //CmdWhile	→ "while" "(" Expressao ")" ":" ListaCmd "end" ";" 34
    public void cmdWhile() throws IOException {
        if(!eat(Tag.KW_WHILE))
            sintaticError("while");

        if(!eat(Tag.ABRE_PARENTESES))
            sintaticError("(");

        expressao();

        if(!eat(Tag.FECHA_PARENTESES))
            sintaticError(")");

        if(!eat(Tag.DOIS_PONTOS))
            sintaticError(":");

        listaCmd();

        if(!eat(Tag.KW_END))
            sintaticError("end");

        if(!eat(Tag.PONTO_VIRGULA))
            sintaticError(";");
    }

    @Override
    //CmdWrite	→ "write" "(" Expressao ")" ";" 35
    public void cmdWrite() throws IOException {
        if(!eat(Tag.KW_WRITE))
            sintaticError("write");

        if(!eat(Tag.ABRE_PARENTESES))
            sintaticError("(");

        expressao();

        if(!eat(Tag.FECHA_PARENTESES))
            sintaticError(")");

        if(!eat(Tag.PONTO_VIRGULA))
            sintaticError(";");

    }

    @Override
    //CmdAtribui → "=" Expressao ";" 36
    public void cmdAtribui() throws IOException {
        if(!eat(Tag.OP_IGUAL))
            sintaticError("=");

        expressao();

        if(!eat(Tag.PONTO_VIRGULA))
            sintaticError(";");
    }

    @Override
    //CmdFuncao	→ "(" RegexExp ")" ";" 37
    public void cmdFuncao() throws IOException {
        if(!eat(Tag.ABRE_PARENTESES))
            sintaticError("(");

        regexExp();

        if(!eat(Tag.FECHA_PARENTESES))
            sintaticError(")");

        if(!eat(Tag.PONTO_VIRGULA))
            sintaticError(";");
    }

    @Override
    //RegexExp	→ Expressao RegexExp’ 38 | ε 39
    public void regexExp() throws IOException {
        if(isNext(Tag.ID, Tag.ABRE_PARENTESES, Tag.CONST_INT, Tag.CONST_DOUBLE,
                Tag.CONST_STRING, Tag.KW_TRUE, Tag.KW_FALSE, Tag.OPUNARIO_NEGACAO,
                Tag.OPUNARIO_NEGATIVO)){

            expressao();

            regexExp_();

            return;
        }

        if(!isNext(Tag.FECHA_PARENTESES))
            skip(")", "ID", "(", "integer", "double", "String", "true", "false", "!", "-n");
    }

    @Override
    //RegexExp’	→ , Expressao RegexExp’ 40 | ε 41
    public void regexExp_() throws IOException {
        if(eat(Tag.VIRGULA)){
            expressao();

            regexExp_();

            return;
        }

        if(!isNext(Tag.FECHA_PARENTESES))
            sintaticError(")", ",");
    }

    @Override
    //Expressao	→ Exp1 Exp’ 42
    public void expressao() throws IOException {
        if(isNext(Tag.ID, Tag.CONST_INT, Tag.CONST_DOUBLE, Tag.CONST_STRING, Tag.KW_TRUE, Tag.KW_FALSE, Tag.ABRE_PARENTESES, Tag.OPUNARIO_NEGACAO, Tag.OPUNARIO_NEGATIVO)){
            exp1();

            exp_();
        }

        else {
            skip("ID", "integer", "double", "String", "true", "false", "(", "!", "-");
        }
    }

    @Override
    //Exp’ → "or" Exp1 Exp’ 43 | "and" Exp1 Exp’ 44 | ε 45
    public void exp_() throws IOException {
        //43 // 44
        if(eat(Tag.OP_OR, Tag.OP_AND)){

            exp1();

            exp_();

            return;
        }
        //45
        if(!isNext(Tag.PONTO_VIRGULA, Tag.FECHA_PARENTESES, Tag.VIRGULA)){
            skip("or", "and", ";", ")", ",");
        }
    }

    @Override
    //Exp1	→ Exp2 Exp1’ 46
    public void exp1() throws IOException {
        //46
        if(isNext(Tag.ID, Tag.ABRE_PARENTESES, Tag.CONST_INT, Tag.CONST_DOUBLE,
                Tag.CONST_STRING, Tag.KW_TRUE, Tag.KW_FALSE, Tag.OPUNARIO_NEGATIVO,
                Tag.OPUNARIO_NEGACAO)){

            exp2();

            exp1_();
        }

        else {
            skip("ID", "(", "consInt", "constDouble", "constStr", "true", "false", "-", "!");
        }
    }

    @Override
    //Exp1’	→ "<" Exp2 Exp1’ 47 | "<=" Exp2 Exp1’ 48 | ">" Exp2 Exp1’ 49 |
    // ">=" Exp2 Exp1’ 50 | "==" Exp2 Exp1’ 51 | "!=" Exp2 Exp1’ 52 | ε 53
    public void exp1_() throws IOException {
        //47
        if(eat(Tag.OP_MAIOR, Tag.OP_MAIOR_IGUAL, Tag.OP_MENOR, Tag.OP_MENOR_IGUAL, Tag.OP_COMPARACAO, Tag.OP_DIFERENTE)){

            exp2();

            exp1_();

            return;
        }

         if(!isNext(Tag.PONTO_VIRGULA, Tag.FECHA_PARENTESES, Tag.VIRGULA, Tag.OP_OR, Tag.OP_AND))
            sintaticError("<", "<=", ">", ">=", ",", "or", "and", "==", "!=");
    }

    @Override
    //Exp2	→ Exp3 Exp2’ 54
    public void exp2() throws IOException {
        //54
        if(isNext(Tag.ID, Tag.ABRE_PARENTESES, Tag.CONST_INT, Tag.CONST_DOUBLE, Tag.CONST_STRING, Tag.KW_TRUE,
                Tag.KW_FALSE, Tag.OPUNARIO_NEGACAO, Tag.OPUNARIO_NEGATIVO)){

            exp3();

            exp2_();

        }

        else {
            skip("ID", "(", "consInt", "constDouble", "constStr", "true", "false", "-", "!");
        }
    }

    @Override
    //Exp2’	→ "+" Exp3 Exp2’ 55 | "-" Exp3 Exp2’ 56 | ε 57
    public void exp2_() throws IOException {
        //55 // 56
        if(eat(Tag.OP_SOMA, Tag.OP_SUBTRACAO)) {

            exp3();

            exp2_();

            return;
        }

        if(!isNext(Tag.PONTO_VIRGULA, Tag.FECHA_PARENTESES, Tag.VIRGULA, Tag.OP_OR, Tag.OP_AND, Tag.OP_MAIOR, Tag.OP_MAIOR_IGUAL,
                Tag.OP_MENOR, Tag.OP_MENOR_IGUAL, Tag.OP_COMPARACAO, Tag.OP_DIFERENTE)){
            skip("+", "-", ";", ")", ",", "or", "and", "<", "<=", ">", ">=", "==", "!=");
        }
    }

    @Override
    //Exp3	→ Exp4 Exp3’ 58
    public void exp3() throws IOException {
        //58
        if(isNext(Tag.ID, Tag.ABRE_PARENTESES, Tag.CONST_INT, Tag.CONST_DOUBLE, Tag.CONST_STRING, Tag.KW_TRUE, Tag.KW_FALSE,
                Tag.OPUNARIO_NEGACAO, Tag.OPUNARIO_NEGATIVO)){

            exp4();

            exp3_();
        }

        else {
            skip("ID", "(", "constInt", "constDouble", "constString", "true", "false", "!", "-n");
        }
    }

    @Override
    //Exp3’	→ "*" Exp4 Exp3’ 59 | "/" Exp4 Exp3’ 60 | ε 61
    public void exp3_() throws IOException {
        //59 // 60
        if(eat(Tag.OP_MULTIPLICACAO, Tag.OP_DIVISAO)){
            exp4();

            exp3_();

            return;

        }
        //61
        if(!isNext(Tag.PONTO_VIRGULA, Tag.FECHA_PARENTESES, Tag.VIRGULA, Tag.OP_OR, Tag.OP_AND, Tag.OP_MAIOR, Tag.OP_MAIOR_IGUAL,
                Tag.OP_MENOR, Tag.OP_MENOR_IGUAL, Tag.OP_COMPARACAO, Tag.OP_DIFERENTE, Tag.OP_SOMA, Tag.OP_SUBTRACAO)){
            skip(";", ")", ",", "or", "and", "<", "<=", ">", ">=" , "==", "!=" , "+", "-");
        }
    }

    @Override
    //Exp4	→ ID Exp4’ 62 | ConstInteger 63 | ConstDouble 64 | ConstString 65 |
    // "true" 66 | "false" 67 | OpUnario Exp4 68 | "(" Expressao")" 69
    public void exp4() throws IOException {
        //62
        if(eat(Tag.ID)){
            exp4_();

            return;
        }

        //68
        if(eat(Tag.OPUNARIO_NEGATIVO, Tag.OPUNARIO_NEGATIVO)){

            exp4();

            return;
        }

        //69
        if(eat(Tag.ABRE_PARENTESES)){

            expressao();

            if(!eat(Tag.FECHA_PARENTESES))
                sintaticError(")");

            return;
        }
        //63 //64 //65 //66 //67
        if(!eat(Tag.CONST_INT, Tag.CONST_DOUBLE, Tag.CONST_STRING, Tag.KW_TRUE, Tag.KW_FALSE)){
            sintaticError("ID", "constInt", "constDouble", "constStr", "true", "false", "!", "-n", "(");
        }
    }

    @Override
    //Exp4’	→ "(" RegexExp ")" 70 | ε 71
    public void exp4_() throws IOException {
        //70
        if(eat(Tag.ABRE_PARENTESES)){

            regexExp();

            if(!eat(Tag.FECHA_PARENTESES))
                sintaticError(")");

            return;
        }

        //71
        if(!isNext(Tag.PONTO_VIRGULA, Tag.FECHA_PARENTESES, Tag.VIRGULA, Tag.OP_OR, Tag.OP_AND, Tag.OP_MAIOR,
                Tag.OP_MAIOR_IGUAL,Tag.OP_MENOR, Tag.OP_MENOR_IGUAL, Tag.OP_COMPARACAO, Tag.OP_DIFERENTE,
                Tag.OP_SOMA, Tag.OP_SUBTRACAO, Tag.OP_MULTIPLICACAO, Tag.OP_DIVISAO)){
            skip(";", ")", ",", "or", "and", "<", "<=", ">", ">=" , "==", "!=" , "+", "-", "*", "/");
        }
    }

    @Override
    //OpUnario	→ "-" 72 | "!" 73
    public void opUnario() throws IOException {
        //72 //73
        if(!eat(Tag.OPUNARIO_NEGATIVO, Tag.OPUNARIO_NEGACAO))
            skip("-n", "!");
    }

    @Override
    public LexerImpl getLexerImpl() {
        return lexerImpl;
    }

    private boolean isNext(Tag... tags){
        for (Tag t: tags) {
            if(this.token.getName().equals(t.toString())){
                return true;
            }
        }
        return false;
    }

    private String printWaitedTokens(String[] tokens){
        String formattedString = "";
        for(String t : tokens){
            formattedString += "[" + t + "]";
        }
        return formattedString;
    }
}