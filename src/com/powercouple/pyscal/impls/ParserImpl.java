package com.powercouple.pyscal.impls;

import com.powercouple.pyscal.No;
import com.powercouple.pyscal.Parser;
import com.powercouple.pyscal.Tag;
import com.powercouple.pyscal.Token;

import java.io.IOException;

public class ParserImpl implements Parser {

    private LexerImpl lexerImpl;
    private Token token;

    private Token tempToken;

    public ParserImpl(LexerImpl lexerImpl) throws IOException {
        this.lexerImpl = lexerImpl;
        this.token = lexerImpl.nextToken().orElseThrow(() -> new RuntimeException("Token not found"));
    }

    private void syntacticError(String... esperados){
        System.out.print(">>>>>>>>>>>>>>>>>>>[Erro Sintatico] Error:(" + token.getLine() + "," + token.getColumn() + ") ");
        System.out.println("Esperado: " + printWaitedTokens(esperados) + "\"; encontrado " + "\"" + token.getName() + "\"" + "\n");
    }

    private void semanticError(String msg){
        System.out.print(">>>>>>>>>>>>>>>>>>>[Erro Semantico] Error:(" + token.getLine() + "," + token.getColumn() + ") ");
        System.out.println(msg);
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
        this.syntacticError(esperados);
        if(!token.getName().equals(Tag.EOF.toString())){
            this.advance();
        }
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
            syntacticError("EOF");
    }

    private void copyToken(){
        tempToken = new Token(token.getName(), token.getLexeme(), token.getLine(), token.getColumn());
    }

    //Classe → "class" ID ":" ListaFuncao Main "end" "." 2
    @Override
    public void classe() throws IOException {
        //2
        if (eat(Tag.KW_CLASS)) {
            copyToken();
            if (!eat(Tag.ID))
                syntacticError("ID");

            lexerImpl.getSt().setType(tempToken.getLexeme(), Tag.EMPTY);

            if (!eat(Tag.DOIS_PONTOS))
                syntacticError(":");

            listaFuncao();
            main();

            if (!eat(Tag.KW_END))
                syntacticError("end");

            if (!eat(Tag.PONTO))
                syntacticError(".");

        } else
            syntacticError("class");
    }

    @Override
    //DeclaraID	→ TipoPrimitivo ID ";" 3
    public void declaraId() throws IOException {

        if(isNext(Tag.KW_VOID, Tag.KW_STRING, Tag.KW_BOOL, Tag.KW_INTEGER, Tag.KW_DOUBLE)){

            No noTipoPrimitivo = tipoPrimitivo();

            copyToken();
            if(!eat(Tag.ID)){
                syntacticError("ID");
            }

            lexerImpl.getSt().setType(tempToken.getLexeme(), noTipoPrimitivo.getTipo());

            if(!eat(Tag.PONTO_VIRGULA))
                syntacticError(";");

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

            No noTipoPrimitivo = tipoPrimitivo();

            copyToken();
            if(!eat(Tag.ID))
                syntacticError("ID");

            lexerImpl.getSt().setType(tempToken.getLexeme(), noTipoPrimitivo.getTipo());

            if(!eat(Tag.ABRE_PARENTESES))
                syntacticError("(");

            listaArg();

            if(!eat(Tag.FECHA_PARENTESES))
                syntacticError(")");

            if(!eat(Tag.DOIS_PONTOS))
                syntacticError(":");

            regexDeclaraId();

            listaCmd();

            No noRetorno = retorno();

            if(noRetorno.getTipo() != noTipoPrimitivo.getTipo()){
                semanticError("Retorno incompativel");
            }

            if(!eat(Tag.KW_END))
                syntacticError("end");

            if(!eat(Tag.PONTO_VIRGULA))
                syntacticError(";");
        }

        else {
            syntacticError("def");
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

            No noTipoPrimitivo = tipoPrimitivo();

            copyToken();
            if(!eat(Tag.ID))
                syntacticError("ID");

            lexerImpl.getSt().setType(tempToken.getLexeme(), noTipoPrimitivo.getTipo());

        }


        else {
            skip("void", "String", "bool", "integer", "double");
        }
    }

    @Override
    //Retorno	→ "return" Expressao ";" 14 | ε 15
    public No retorno() throws IOException {
        No noRetorno = new No();
        if(eat(Tag.KW_RETURN)){

            No noExpressao = expressao();

            if(!eat(Tag.PONTO_VIRGULA))
                syntacticError(";");

            noRetorno.setTipo(noExpressao.getTipo());

            return noRetorno;
        }

        if(!isNext(Tag.KW_END)){
            skip("void", "String", "bool", "integer", "double");
        }

        return noRetorno;
    }

    @Override
    //Main	→ "defstatic" "void" "main" "(" "String" "[" "]" ID ")" ":" RegexDeclaraId ListaCmd "end" ";" 16
    public void main() throws IOException {
        if(eat(Tag.KW_DEFSTATIC)){

            if(!eat(Tag.KW_VOID))
                syntacticError("void");

            if(!eat(Tag.KW_MAIN))
                syntacticError("main");

            if(!eat(Tag.ABRE_PARENTESES))
                syntacticError("(");

            if(!eat(Tag.KW_STRING))
                syntacticError("String");

            if(!eat(Tag.ABRE_COLCHETE))
                syntacticError("[");

            if(!eat(Tag.FECHA_COLCHETE))
                syntacticError("]");

            copyToken();
            if(!eat(Tag.ID))
                syntacticError("ID");

            lexerImpl.getSt().setType(tempToken.getLexeme(), Tag.STRING);

            if(!eat(Tag.FECHA_PARENTESES))
                syntacticError(")");

            if(!eat(Tag.DOIS_PONTOS))
                syntacticError(":");

            regexDeclaraId();

            listaCmd();

            if(!eat(Tag.KW_END))
                syntacticError("end");

            if(!eat(Tag.PONTO_VIRGULA))
                syntacticError(";");

        }

        else {
            skip("defstatic");
        }
    }

    @Override
    //TipoPrimitivo → "bool" 17 | "integer" 18 | "String" 19 | "double" 20 | "void" 21
    public No tipoPrimitivo() throws IOException {
        No noTipoPrimitivo = new No();

        if(eat(Tag.KW_BOOL)){
            noTipoPrimitivo.setTipo(Tag.LOGIC);
            return noTipoPrimitivo;
        }

        if(eat(Tag.KW_INTEGER)){
            noTipoPrimitivo.setTipo(Tag.NUMERICO);
            return noTipoPrimitivo;
        }

        if(eat(Tag.KW_STRING)){
            noTipoPrimitivo.setTipo(Tag.STRING);
            return noTipoPrimitivo;
        }

        if(eat(Tag.KW_DOUBLE)){
            noTipoPrimitivo.setTipo(Tag.NUMERICO);
            return noTipoPrimitivo;
        }

        if(eat(Tag.KW_VOID)){
            noTipoPrimitivo.setTipo(Tag.EMPTY);
            return noTipoPrimitivo;
        }

        else {
            syntacticError("bool", "integer", "String", "double", "void");
            return null;
        }
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
        copyToken();
        if(eat(Tag.ID)){
            if(lexerImpl.getSt().getType(tempToken.getLexeme()) == null){
                semanticError("ID não declarado");
            }

            No noCmdAtribFunc = cmdAtribFunc();

            if(noCmdAtribFunc.getTipo() != Tag.EMPTY &&
                    lexerImpl.getSt().getType(tempToken.getLexeme()) != noCmdAtribFunc.getTipo()){
                semanticError("Atribuiçao Incompativel");
            }
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
            syntacticError("ID", "if", "while", "write");

    }

    @Override
    //CmdAtribFunc → CmdAtribui 29 | CmdFuncao 30
    public No cmdAtribFunc() throws IOException {

        No noCmdAtribFunc = new No();

        if(isNext(Tag.ABRE_PARENTESES)){
            cmdFuncao();
            noCmdAtribFunc.setTipo(Tag.EMPTY);
            return noCmdAtribFunc;
        }

        if(isNext(Tag.OP_IGUAL)){
            No noCmdAtribui = cmdAtribui();
            noCmdAtribFunc.setTipo(noCmdAtribui.getTipo());
            return noCmdAtribFunc;
        }

        else {
            skip("(", "=");
        }

        return null;
    }

    @Override
    //CmdIF	→ "if" "(" Expressao ")" ":" ListaCmd CmdIF’ 31
    public void cmdIf() throws IOException {
        if(!eat(Tag.KW_IF))
            syntacticError("if");

        if(!eat(Tag.ABRE_PARENTESES))
            syntacticError("(");

        No noExpressao = expressao();

        if(!eat(Tag.FECHA_PARENTESES))
            syntacticError(")");

        if(noExpressao.getTipo() != Tag.LOGIC){
            semanticError("Tipo esperado: LOGIC");
        }

        if(!eat(Tag.DOIS_PONTOS))
            syntacticError(":");

        listaCmd();

        cmdIf_();
    }

    @Override
    //CmdIF’ → "end" ";" 32 | "else" ":" ListaCmd "end" ";" 33
    public void cmdIf_() throws IOException {
        if(eat(Tag.KW_END)){

            if(!eat(Tag.PONTO_VIRGULA))
                syntacticError(";");

            return;
        }

        if(eat(Tag.KW_ELSE)){

            if(!eat(Tag.DOIS_PONTOS))
                syntacticError(":");

            listaCmd();

            if(!eat(Tag.KW_END))
                syntacticError("end");

            if(!eat(Tag.PONTO_VIRGULA))
                syntacticError(";");

        }

        else
            syntacticError("end, else");
    }

    @Override
    //CmdWhile	→ "while" "(" Expressao ")" ":" ListaCmd "end" ";" 34
    public void cmdWhile() throws IOException {
        if(!eat(Tag.KW_WHILE))
            syntacticError("while");

        if(!eat(Tag.ABRE_PARENTESES))
            syntacticError("(");

        No noExpressao = expressao();

        if(!eat(Tag.FECHA_PARENTESES))
            syntacticError(")");

        if(noExpressao.getTipo() != Tag.LOGIC){
            semanticError("Tipo esperado: LOGIC");
        }

        if(!eat(Tag.DOIS_PONTOS))
            syntacticError(":");

        listaCmd();

        if(!eat(Tag.KW_END))
            syntacticError("end");

        if(!eat(Tag.PONTO_VIRGULA))
            syntacticError(";");
    }

    @Override
    //CmdWrite	→ "write" "(" Expressao ")" ";" 35
    public void cmdWrite() throws IOException {
        if(!eat(Tag.KW_WRITE))
            syntacticError("write");

        if(!eat(Tag.ABRE_PARENTESES))
            syntacticError("(");

        No noExpressao = expressao();

        if(!eat(Tag.FECHA_PARENTESES))
            syntacticError(")");

        if(noExpressao.getTipo() != Tag.STRING){
            semanticError("Tipo esperado: STRING");
        }

        if(!eat(Tag.PONTO_VIRGULA))
            syntacticError(";");

    }

    @Override
    //CmdAtribui → "=" Expressao ";" 36
    public No cmdAtribui() throws IOException {

        No noCmdAtribui = new No();

        if(!eat(Tag.OP_IGUAL))
            syntacticError("=");

        No noExpressao = expressao();

        if(!eat(Tag.PONTO_VIRGULA))
            syntacticError(";");

        noCmdAtribui.setTipo(noExpressao.getTipo());

        return noCmdAtribui;
    }

    @Override
    //CmdFuncao	→ "(" RegexExp ")" ";" 37
    public void cmdFuncao() throws IOException {
        if(!eat(Tag.ABRE_PARENTESES))
            syntacticError("(");

        regexExp();

        if(!eat(Tag.FECHA_PARENTESES))
            syntacticError(")");

        if(!eat(Tag.PONTO_VIRGULA))
            syntacticError(";");
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
            syntacticError(")", ",");
    }

    @Override
    //Expressao	→ Exp1 Exp’ 42
    public No expressao() throws IOException {

        No noExp = new No();

        if(isNext(Tag.ID, Tag.CONST_INT, Tag.CONST_DOUBLE, Tag.CONST_STRING, Tag.KW_TRUE, Tag.KW_FALSE, Tag.ABRE_PARENTESES, Tag.OPUNARIO_NEGACAO, Tag.OPUNARIO_NEGATIVO)){
            No noExp1 = exp1();

            No noExp_ = exp_();

            if(noExp_.getTipo() == Tag.EMPTY){
                noExp.setTipo(noExp1.getTipo());
            }
            else if(noExp_.getTipo() == noExp1.getTipo() &&
                    noExp_.getTipo() == Tag.LOGIC){
                noExp.setTipo(Tag.LOGIC);
            } else {
                noExp_.setTipo(Tag.ERROR);
            }

            return noExp;
        }



        else {
            skip("ID", "integer", "double", "String", "true", "false", "(", "!", "-");
        }

        return noExp;
    }

    @Override
    //Exp’ → "or" Exp1 Exp’ 43 | "and" Exp1 Exp’ 44 | ε 45
    public No exp_() throws IOException {

        No noExp_ = new No();

        //43 // 44
        if(eat(Tag.OP_OR, Tag.OP_AND)){

            No noExp1 = exp1();

            No noExp_Filho = exp_();

            if(noExp_Filho.getTipo() == Tag.EMPTY && noExp1.getTipo() == Tag.LOGIC){
                noExp_.setTipo(Tag.LOGIC);
            } else if(noExp_Filho.getTipo() == noExp1.getTipo() && noExp1.getTipo() == Tag.LOGIC ){
                noExp_.setTipo(Tag.LOGIC);
            } else {
                noExp_.setTipo(Tag.ERROR);
            }
            return noExp_;
        }
        //45
        if(!isNext(Tag.PONTO_VIRGULA, Tag.FECHA_PARENTESES, Tag.VIRGULA)){
            skip("or", "and", ";", ")", ",");
        }
        return noExp_;
    }

    @Override
    //Exp1	→ Exp2 Exp1’ 46
    public No exp1() throws IOException {

        No noExp1 = new No();

        //46
        if(isNext(Tag.ID, Tag.ABRE_PARENTESES, Tag.CONST_INT, Tag.CONST_DOUBLE,
                Tag.CONST_STRING, Tag.KW_TRUE, Tag.KW_FALSE, Tag.OPUNARIO_NEGATIVO,
                Tag.OPUNARIO_NEGACAO)){

            No noExp2 = exp2();

            No noExp1_ = exp1_();

            if(noExp1_.getTipo() == Tag.EMPTY){
                noExp1.setTipo(noExp2.getTipo());
            } else if(noExp1_.getTipo() == noExp2.getTipo() && noExp1_.getTipo() == Tag.NUMERICO){
                noExp1.setTipo(Tag.LOGIC);
            } else {
                noExp1.setTipo(Tag.ERROR);
            }
            return noExp1;
        }

        else {
            skip("ID", "(", "consInt", "constDouble", "constStr", "true", "false", "-", "!");
        }

        return noExp1;
    }

    @Override
    //Exp1’	→ "<" Exp2 Exp1’ 47 | "<=" Exp2 Exp1’ 48 | ">" Exp2 Exp1’ 49 |
    // ">=" Exp2 Exp1’ 50 | "==" Exp2 Exp1’ 51 | "!=" Exp2 Exp1’ 52 | ε 53
    public No exp1_() throws IOException {

        No noExp1_ = new No();
        //47
        if(eat(Tag.OP_MAIOR, Tag.OP_MAIOR_IGUAL, Tag.OP_MENOR, Tag.OP_MENOR_IGUAL, Tag.OP_COMPARACAO, Tag.OP_DIFERENTE)){

            No noExp2 = exp2();

            No noExp1_Filho = exp1_();

            if(noExp1_Filho.getTipo() == Tag.EMPTY && noExp2.getTipo() == Tag.NUMERICO){
                noExp1_.setTipo(Tag.NUMERICO);
            } else if(noExp1_Filho.getTipo() == noExp2.getTipo() && noExp2.getTipo() == Tag.NUMERICO){
                noExp1_.setTipo(Tag.NUMERICO);
            } else {
                noExp1_.setTipo(Tag.ERROR);
            }

            return noExp1_;
        }

        if(!isNext(Tag.PONTO_VIRGULA, Tag.FECHA_PARENTESES, Tag.VIRGULA, Tag.OP_OR, Tag.OP_AND))
            syntacticError("<", "<=", ">", ">=", ",", "or", "and", "==", "!=");

        return noExp1_;
    }

    @Override
    //Exp2	→ Exp3 Exp2’ 54
    public No exp2() throws IOException {

        No noExp2 = new No();
        //54
        if(isNext(Tag.ID, Tag.ABRE_PARENTESES, Tag.CONST_INT, Tag.CONST_DOUBLE, Tag.CONST_STRING, Tag.KW_TRUE,
                Tag.KW_FALSE, Tag.OPUNARIO_NEGACAO, Tag.OPUNARIO_NEGATIVO)) {

            No noExp3 = exp3();

            No noExp2_ = exp2_();

            if (noExp2_.getTipo() == Tag.EMPTY) {
                noExp2.setTipo(noExp3.getTipo());
            } else if (noExp2_.getTipo() == noExp3.getTipo() && noExp2_.getTipo() == Tag.NUMERICO) {
                noExp2.setTipo(Tag.NUMERICO);
            } else {
                noExp2.setTipo(Tag.ERROR);
            }
            return noExp2;
        }

        else {
            skip("ID", "(", "consInt", "constDouble", "constStr", "true", "false", "-", "!");
        }

        return noExp2;
    }

    @Override
    //Exp2’	→ "+" Exp3 Exp2’ 55 | "-" Exp3 Exp2’ 56 | ε 57
    public No exp2_() throws IOException {

        No noExp2_ = new No();

        //55 // 56
        if(eat(Tag.OP_SOMA, Tag.OP_SUBTRACAO)) {

            No noExp3 = exp3();

            No noExp2_Filho = exp2_();

            if(noExp2_Filho.getTipo() == Tag.EMPTY && noExp3.getTipo() == Tag.NUMERICO){
                noExp2_.setTipo(Tag.NUMERICO);
            } else if(noExp2_Filho.getTipo() == noExp3.getTipo() && noExp3.getTipo() == Tag.NUMERICO){
                noExp2_.setTipo(Tag.NUMERICO);
            } else {
                noExp3.setTipo(Tag.ERROR);
            }

            return noExp2_;
        }

        if(!isNext(Tag.PONTO_VIRGULA, Tag.FECHA_PARENTESES, Tag.VIRGULA, Tag.OP_OR, Tag.OP_AND, Tag.OP_MAIOR, Tag.OP_MAIOR_IGUAL,
                Tag.OP_MENOR, Tag.OP_MENOR_IGUAL, Tag.OP_COMPARACAO, Tag.OP_DIFERENTE)){
            skip("+", "-", ";", ")", ",", "or", "and", "<", "<=", ">", ">=", "==", "!=");
        }

        return noExp2_;
    }

    @Override
    //Exp3	→ Exp4 Exp3’ 58
    public No exp3() throws IOException {

        No noExp3 = new No();

        //58
        if(isNext(Tag.ID, Tag.ABRE_PARENTESES, Tag.CONST_INT, Tag.CONST_DOUBLE, Tag.CONST_STRING, Tag.KW_TRUE, Tag.KW_FALSE,
                Tag.OPUNARIO_NEGACAO, Tag.OPUNARIO_NEGATIVO)){

            No noExp4 = exp4();

            No noExp3_ = exp3_();

            if(noExp3_.getTipo() == Tag.EMPTY){
                noExp3.setTipo(noExp4.getTipo());
            } else if (noExp3_.getTipo() == noExp4.getTipo() && noExp3_.getTipo() == Tag.NUMERICO){
                noExp3.setTipo(Tag.NUMERICO);
            } else {
                noExp3.setTipo(Tag.ERROR);
            }

            return noExp3;
        }

        else {
            skip("ID", "(", "constInt", "constDouble", "constString", "true", "false", "!", "-n");
        }

        return noExp3;
    }

    @Override
    //Exp3’	→ "*" Exp4 Exp3’ 59 | "/" Exp4 Exp3’ 60 | ε 61
    public No exp3_() throws IOException {

        No noExp3_ = new No();

        //59 // 60
        if(eat(Tag.OP_MULTIPLICACAO, Tag.OP_DIVISAO)){
            No noExp4 = exp4();

            No noExp3_Filho = exp3_();

            if(noExp3_Filho.getTipo() == Tag.EMPTY && noExp4.getTipo() == Tag.NUMERICO){
                noExp3_.setTipo(Tag.NUMERICO);
            } else if (noExp3_Filho.getTipo() == noExp4.getTipo() && noExp4.getTipo() == Tag.NUMERICO){
                noExp3_.setTipo(Tag.NUMERICO);
            } else {
                noExp3_.setTipo(Tag.ERROR);
            }

            return noExp3_;

        }
        //61
        if(!isNext(Tag.PONTO_VIRGULA, Tag.FECHA_PARENTESES, Tag.VIRGULA, Tag.OP_OR, Tag.OP_AND, Tag.OP_MAIOR, Tag.OP_MAIOR_IGUAL,
                Tag.OP_MENOR, Tag.OP_MENOR_IGUAL, Tag.OP_COMPARACAO, Tag.OP_DIFERENTE, Tag.OP_SOMA, Tag.OP_SUBTRACAO)){
            skip(";", ")", ",", "or", "and", "<", "<=", ">", ">=" , "==", "!=" , "+", "-");
        }

        return noExp3_;
    }

    @Override
    //Exp4	→ ID Exp4’ 62 | ConstInteger 63 | ConstDouble 64 | ConstString 65 |
    // "true" 66 | "false" 67 | OpUnario Exp4 68 | "(" Expressao")" 69
    public No exp4() throws IOException {
        No noExp4 = new No();
        //62
        copyToken();
        if(eat(Tag.ID)){
            exp4_();

            noExp4.setTipo(lexerImpl.getSt().getType(tempToken.getLexeme()));
            if(noExp4.getTipo() == null){
                noExp4.setTipo(Tag.ERROR);
                semanticError("ID não declarado.");
            }

            return noExp4;
        }
        //63 //64
        if(eat(Tag.CONST_INT, Tag.CONST_DOUBLE)){
            noExp4.setTipo(Tag.NUMERICO);
            return noExp4;
        }

        //65
        if(eat(Tag.CONST_STRING)){
            noExp4.setTipo(Tag.STRING);
            return noExp4;
        }

        //66 //67
        if(eat(Tag.KW_TRUE, Tag.KW_FALSE)){
            noExp4.setTipo(Tag.LOGIC);
            return noExp4;
        }

        //68
        if(isNext(Tag.OPUNARIO_NEGATIVO, Tag.OPUNARIO_NEGATIVO)){

            No noOpUnario = opUnario();

            No noExp4Filho = exp4();

            if(noExp4Filho.getTipo()== noOpUnario.getTipo() && noOpUnario.getTipo() == Tag.NUMERICO){
                noExp4.setTipo(Tag.NUMERICO);
            } else if(noExp4Filho.getTipo() == noOpUnario.getTipo() && noOpUnario.getTipo() == Tag.LOGIC){
                noExp4.setTipo(Tag.LOGIC);
            } else {
                noExp4.setTipo(Tag.ERROR);
            }

            return noExp4;
        }

        //69
        if(eat(Tag.ABRE_PARENTESES)){

            No noExp = expressao();

            if(!eat(Tag.FECHA_PARENTESES))
                syntacticError(")");

            noExp4.setTipo(noExp.getTipo());
            return noExp4;
        }

        syntacticError("ID", "constInt", "constDouble", "constStr", "true", "false", "!", "-n", "(");
        return noExp4;
    }

    @Override
    //Exp4’	→ "(" RegexExp ")" 70 | ε 71
    public void exp4_() throws IOException {

        //70
        if(eat(Tag.ABRE_PARENTESES)){

            regexExp();

            if(!eat(Tag.FECHA_PARENTESES))
                syntacticError(")");
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
    public No opUnario() throws IOException {
        No noOpUnario = new No();
        //72 //73
        if(token.getName().equals(Tag.OPUNARIO_NEGATIVO.toString())){
            noOpUnario.setTipo(Tag.NUMERICO);
            eat(Tag.OPUNARIO_NEGATIVO);
        }

        else if(token.getName().equals(Tag.OPUNARIO_NEGACAO.toString())) {
            noOpUnario.setTipo(Tag.LOGIC);
            eat(Tag.OPUNARIO_NEGACAO);
        }

        else {
            syntacticError("!", "-n");
            if (!isNext(Tag.ID, Tag.CONST_STRING, Tag.CONST_DOUBLE, Tag.CONST_INT, Tag.KW_TRUE, Tag.KW_FALSE, Tag.ABRE_PARENTESES)) {
                skip("-n", "!");

            }
        }
        return noOpUnario;
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
        StringBuilder formattedString = new StringBuilder();
        for(String t : tokens){
            formattedString.append("[").append(t).append("]");
        }
        return formattedString.toString();
    }
}