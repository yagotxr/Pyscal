package com.powercouple.pyscal.impls;

import com.powercouple.pyscal.Lexer;
import com.powercouple.pyscal.Parser;
import com.powercouple.pyscal.Tag;
import com.powercouple.pyscal.Token;

import java.io.IOException;

public class ParserImpl implements Parser {

    private Lexer lexer;
    private Token token;

    public ParserImpl(Lexer lexer) throws IOException {
        this.lexer = lexer;
        this.token = lexer.nextToken().orElseThrow(() -> new RuntimeException("Token not found"));
    }

    public void sintaticError(String msg){
        System.out.println(">>>>>>>>>>>>>>>>>> [Erro Sintatico] na linha " + token.getLine() + " e coluna " + token.getColumn() + ": ");
        System.out.println(msg + "\n");
    }

    public void advance() throws IOException {
        System.out.println("[DEBUG] token: " + token.toString());
        this.token = lexer.nextToken().orElseThrow(() -> new RuntimeException("Token not found"));
    }

    public void skip(String msg) throws IOException {
        this.sintaticError(msg);
        this.advance();
    }

    public boolean eat(Tag... token) throws IOException {
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
        if(isTag(Tag.KW_CLASS)){
            classe();
            if(!eat(Tag.EOF))
                sintaticError("Esperado \"EOF\"; encontrado " + "\"" + token.getName() + "\"");
        }
    }

    //Classe → "class" ID ":" ListaFuncao Main "end" "." 2
    @Override
    public void classe() throws IOException {
        //2
        if (eat(Tag.KW_CLASS)) {
            if (!eat(Tag.ID))
                sintaticError("Esperado \"ID\"; encontrado " + "\"" + this.token.getName() + "\"");
            if (!eat(Tag.DOIS_PONTOS))
                sintaticError("Esperado \":\"; encontrado " + "\"" + this.token.getName() + "\"");
            listaFuncao();
            main();
            if (!eat(Tag.KW_END))
                sintaticError("Esperado \"end\"; encontrado " + "\"" + this.token.getName() + "\"");
            if (!eat(Tag.PONTO))
                sintaticError("Esperado \".\"; encontrado " + "\"" + this.token.getName() + "\"");
        } else
            sintaticError("Esperado \"class\"; encontrado " + "\"" + this.token.getName() + "\"");
    }

    @Override
    //DeclaraID	→ TipoPrimitivo ID ";" 3
    public void declaraId() throws IOException {
        if(isTag(Tag.KW_VOID, Tag.KW_STRING, Tag.KW_BOOL, Tag.KW_INTEGER, Tag.KW_DOUBLE)){
            tipoPrimitivo();
            if(!eat(Tag.ID))
                sintaticError("Esperado \"ID\"; encontrado " + "\"" + this.token.getName() + "\"");
            if(!eat(Tag.PONTO_VIRGULA))
                sintaticError("Esperado \";\"; encontrado " + "\"" + this.token.getName() + "\"");
        } else
            skip("Esperado \"void, String, bool, integer, double\"; encontrado " + "\"" + this.token.getName() + "\"");
    }

    @Override
    //ListaFuncao → ListaFuncao’ 4
    public void listaFuncao() throws IOException {
        if(isTag(Tag.KW_DEF, Tag.KW_DEFSTATIC))
            listaFuncao_();
        else
            skip("Esperado \"def, defstatic\"; encontrado " + "\"" + this.token.getName() + "\"");
    }

    @Override
    //ListaFuncao’ → Funcao ListaFuncao’ 5 | ε 6
    public void listaFuncao_() throws IOException {
        if(isTag(Tag.KW_DEF)){
            funcao();
            listaFuncao_();
        } else
            skip("Esperado \"def\"; encontrado " + "\"" + this.token.getName() + "\"");
    }

    @Override
    //Funcao → "def" TipoPrimitivo ID "(" ListaArg ")" ":" RegexDeclaraId ListaCmd Retorno "end" ";" 7
    public void funcao() throws IOException {
        if(eat(Tag.KW_DEF)){
            tipoPrimitivo();
            if(!eat(Tag.ID))
                sintaticError("Esperado \"ID\"; encontrado " + "\"" + this.token.getName() + "\"");
            if(!eat(Tag.ABRE_PARENTESES))
                sintaticError("Esperado \"(\"; encontrado " + "\"" + this.token.getName() + "\"");
            listaArg();
            if(!eat(Tag.FECHA_PARENTESES))
                sintaticError("Esperado \")\"; encontrado " + "\"" + this.token.getName() + "\"");
            if(!eat(Tag.DOIS_PONTOS))
                sintaticError("Esperado \":\"; encontrado " + "\"" + this.token.getName() + "\"");
            regexDeclaraId();
            listaCmd();
            retorno();
            if(!eat(Tag.KW_END))
                sintaticError("Esperado \"end\"; encontrado " + "\"" + this.token.getName() + "\"");
            if(!eat(Tag.PONTO_VIRGULA))
                sintaticError("Esperado \";\"; encontrado " + "\"" + this.token.getName() + "\"");
        } else {
            sintaticError("Esperado \"def\"; encontrado " + "\"" + this.token.getName() + "\"");
        }
    }

    @Override
    //RegexDeclaraId → DeclaraID RegexDeclaraId 8 | ε 9
    public void regexDeclaraId() throws IOException {
        if (isTag(Tag.KW_VOID, Tag.KW_STRING, Tag.KW_BOOL, Tag.KW_INTEGER, Tag.KW_DOUBLE)) {
            declaraId();
            regexDeclaraId();
        }
        if (!isTag(Tag.ID, Tag.KW_END, Tag.KW_RETURN, Tag.KW_IF, Tag.KW_WHILE, Tag.KW_WRITE)) {
            skip("Esperado \"void, String, bool, integer, double, ID, end, return, if, while, write\"; encontrado " + "\"" + this.token.getName() + "\"");
        }
    }

    @Override
    //ListaArg -> Arg ListArg_ 10
    public void listaArg() throws IOException {
        if(isTag(Tag.KW_VOID, Tag.KW_STRING, Tag.KW_BOOL, Tag.KW_INTEGER, Tag.KW_DOUBLE)){
            arg();
            listaArg_();
        } else {
            skip("Esperado \"void, String, bool, integer, double\"; encontrado " + "\"" + this.token.getName() + "\"");
        }

    }

    @Override
    //ListaArg’	→ "," ListaArg 11 | ε 12
    public void listaArg_() throws IOException {
        if(eat(Tag.VIRGULA))
            listaArg();
        if(!isTag(Tag.FECHA_PARENTESES))
            skip("Esperado \",, )\"; encontrado " + "\"" + this.token.getName() + "\"");
    }

    @Override
    //Arg → TipoPrimitivo ID 13
    public void arg() throws IOException {
        if(isTag(Tag.KW_VOID, Tag.KW_STRING, Tag.KW_BOOL, Tag.KW_INTEGER, Tag.KW_DOUBLE)){
            tipoPrimitivo();
            if(!eat(Tag.ID))
                sintaticError("Esperado \"ID\"; encontrado " + "\"" + this.token.getName() + "\"");
        } else {
            skip("Esperado \"void, String, bool, integer, double\"; encontrado " + "\"" + this.token.getName() + "\"");
        }

    }

    @Override
    //Retorno	→ "return" Expressao ";" 14 | ε 15
    public void retorno() throws IOException {
        if(eat(Tag.KW_RETURN)){
            expressao();
            if(!eat(Tag.PONTO_VIRGULA))
                sintaticError("Esperado \";\"; encontrado " + "\"" + this.token.getName() + "\"");
        }
        if(!isTag(Tag.KW_END)){
            skip("Esperado \"void, String, bool, integer, double\"; encontrado " + "\"" + this.token.getName() + "\"");
        }

    }

    @Override
    //Main	→ "defstatic" "void" "main" "(" "String" "[" "]" ID ")" ":" RegexDeclaraId ListaCmd "end" ";" 16
    public void main() throws IOException {
        if(eat(Tag.KW_DEFSTATIC)){
            if(!eat(Tag.KW_VOID))
                sintaticError("Esperado \"void\"; encontrado " + "\"" + this.token.getName() + "\"");
            if(!eat(Tag.KW_MAIN))
                sintaticError("Esperado \"main\"; encontrado " + "\"" + this.token.getName() + "\"");
            if(!eat(Tag.ABRE_PARENTESES))
                sintaticError("Esperado \"(\"; encontrado " + "\"" + this.token.getName() + "\"");
            if(!eat(Tag.KW_STRING))
                sintaticError("Esperado \"String\"; encontrado " + "\"" + this.token.getName() + "\"");
            if(!eat(Tag.ABRE_COLCHETE))
                sintaticError("Esperado \"[\"; encontrado " + "\"" + this.token.getName() + "\"");
            if(!eat(Tag.FECHA_COLCHETE))
                sintaticError("Esperado \"]\"; encontrado " + "\"" + this.token.getName() + "\"");
            if(!eat(Tag.ID))
                sintaticError("Esperado \"ID\"; encontrado " + "\"" + this.token.getName() + "\"");
            if(!eat(Tag.FECHA_PARENTESES))
                sintaticError("Esperado \")\"; encontrado " + "\"" + this.token.getName() + "\"");
            if(!eat(Tag.DOIS_PONTOS))
                sintaticError("Esperado \":\"; encontrado " + "\"" + this.token.getName() + "\"");
            regexDeclaraId();
            listaCmd();
            if(!eat(Tag.KW_END))
                sintaticError("Esperado \"end\"; encontrado " + "\"" + this.token.getName() + "\"");
            if(!eat(Tag.PONTO_VIRGULA))
                sintaticError("Esperado \";\"; encontrado " + "\"" + this.token.getName() + "\"");
        } else {
            skip("Esperado \"defstatic\"; encontrado " + "\"" + this.token.getName() + "\"");
        }
    }

    @Override
    //TipoPrimitivo → "bool" 17 | "integer" 18 | "String" 19 | "double" 20 | "void" 21
    public void tipoPrimitivo() throws IOException {
        if(!eat(Tag.KW_BOOL, Tag.KW_INTEGER, Tag.KW_STRING, Tag.KW_DOUBLE, Tag.KW_VOID))
            sintaticError("Esperado \"bool, integer, String, double, void\"; encontrado " + "\"" + this.token.getName() + "\"");
    }

    @Override
    //ListaCmd	→ ListaCmd’  22
    public void listaCmd() throws IOException {
        if(isTag(Tag.ID, Tag.KW_END, Tag.KW_RETURN, Tag.KW_IF, Tag.KW_ELSE, Tag.KW_WHILE, Tag.KW_WRITE))
            listaCmd_();
        else
            skip("Esperado \"ID, end, return, if, else, while, write\"; encontrado " + "\"" + this.token.getName() + "\"");

    }

    @Override
    //ListaCmd’	→ Cmd ListaCmd’ 23 | ε 24
    public void listaCmd_() throws IOException {
        if(isTag(Tag.ID,
                Tag.KW_IF,
                Tag.KW_WHILE,
                Tag.KW_WRITE)){
            cmd();
            listaCmd_();
        }
        else if(!isTag(Tag.KW_END,
                Tag.KW_RETURN,
                Tag.KW_ELSE)){
            sintaticError("Esperado \"ID, if, while, write, end, return, else\"; encontrado " + "\"" + this.token.getName() + "\"");
        }
    }

    @Override
    //Cmd → CmdIF 25 | CmdWhile 26 | ID CmdAtribFunc 27 | CmdWrite 28
    public void cmd() throws IOException {
        if(isTag(Tag.ID))
            cmdAttibFunc();
        else if(isTag(Tag.KW_IF)){
            cmdIf();
        }
        else if(isTag(Tag.KW_WHILE)){
            cmdWhile();
        }
        else if(isTag(Tag.KW_WRITE)){
            cmdWrite();
        } else {
            sintaticError("Esperado \"ID, if, while, write\"; encontrado " + "\"" + this.token.getName() + "\"");
        }
    }

    @Override
    //CmdAtribFunc → CmdAtribui 29 | CmdFuncao 30
    public void cmdAttibFunc() throws IOException {
        if(isTag(Tag.ABRE_PARENTESES)){
            cmdFuncao();
        }
        else if(isTag(Tag.OP_IGUAL)){
            cmdAtribui();
        } else {
            sintaticError("Esperado \"(, =\"; encontrado " + "\"" + this.token.getName() + "\"");
        }
    }

    @Override
    //CmdIF	→ "if" "(" Expressao ")" ":" ListaCmd CmdIF’ 31
    public void cmdIf() throws IOException {
        if(!eat(Tag.KW_IF))
            sintaticError("Esperado \"if\"; encontrado " + "\"" + this.token.getName() + "\"");
        else if(!eat(Tag.ABRE_PARENTESES))
            sintaticError("Esperado \"(\"; encontrado " + "\"" + this.token.getName() + "\"");
        expressao();
        if(!eat(Tag.FECHA_PARENTESES))
            sintaticError("Esperado \")\"; encontrado " + "\"" + this.token.getName() + "\"");
        else if(!eat(Tag.DOIS_PONTOS))
            sintaticError("Esperado \":\"; encontrado " + "\"" + this.token.getName() + "\"");
        listaCmd_();
        cmdIf_();
    }

    @Override
    //CmdIF’ → "end" ";" 32 | "else" ":" ListaCmd "end" ";" 33
    public void cmdIf_() throws IOException {
        if(eat(Tag.KW_END)){
            if(!eat(Tag.DOIS_PONTOS))
                sintaticError("Esperado \":\"; encontrado " + "\"" + this.token.getName() + "\"");
        } else if(eat(Tag.KW_ELSE)){
            if(!eat(Tag.DOIS_PONTOS))
                sintaticError("Esperado \":\"; encontrado " + "\"" + this.token.getName() + "\"");
            listaCmd();
            if(!eat(Tag.KW_END))
                sintaticError("Esperado \"end\"; encontrado " + "\"" + this.token.getName() + "\"");
            if(!eat(Tag.PONTO_VIRGULA))
                sintaticError("Esperado \";\"; encontrado " + "\"" + this.token.getName() + "\"");
        } else {
            sintaticError("Esperado \"end, else\"; encontrado " + "\"" + this.token.getName() + "\"");
        }
    }

    @Override
    //CmdWhile	→ "while" "(" Expressao ")" ":" ListaCmd "end" ";" 34
    public void cmdWhile() throws IOException {
        if(!eat(Tag.KW_WHILE))
            sintaticError("Esperado \"(\"; encontrado " + "\"" + this.token.getName() + "\"");
        if(!eat(Tag.ABRE_PARENTESES))
            sintaticError("Esperado \"(\"; encontrado " + "\"" + this.token.getName() + "\"");
        expressao();
        if(!eat(Tag.FECHA_PARENTESES))
            sintaticError("Esperado \")\"; encontrado " + "\"" + this.token.getName() + "\"");
        if(!eat(Tag.DOIS_PONTOS))
            sintaticError("Esperado \":\"; encontrado " + "\"" + this.token.getName() + "\"");
        listaCmd();
        if(!eat(Tag.KW_END))
            sintaticError("Esperado \"end\"; encontrado " + "\"" + this.token.getName() + "\"");
        if(!eat(Tag.PONTO_VIRGULA))
            sintaticError("Esperado \";\"; encontrado " + "\"" + this.token.getName() + "\"");
    }

    @Override
    //CmdWrite	→ "write" "(" Expressao ")" ";" 35
    public void cmdWrite() throws IOException {
        if(!eat(Tag.KW_WRITE))
            sintaticError("Esperado \"write\"; encontrado " + "\"" + this.token.getName() + "\"");
        if(!eat(Tag.ABRE_PARENTESES))
            sintaticError("Esperado \"(\"; encontrado " + "\"" + this.token.getName() + "\"");
        expressao();
        if(!eat(Tag.FECHA_PARENTESES))
            sintaticError("Esperado \")\"; encontrado " + "\"" + this.token.getName() + "\"");
        if(!eat(Tag.PONTO_VIRGULA))
            sintaticError("Esperado \";\"; encontrado " + "\"" + this.token.getName() + "\"");
    }

    @Override
    //CmdAtribui → "=" Expressao ";" 36
    public void cmdAtribui() throws IOException {
        if(!eat(Tag.OP_IGUAL))
            sintaticError("Esperado \"=\"; encontrado " + "\"" + this.token.getName() + "\"");
        expressao();
        if(!eat(Tag.PONTO_VIRGULA))
            sintaticError("Esperado \";\"; encontrado " + "\"" + this.token.getName() + "\"");
    }

    @Override
    //CmdFuncao	→ "(" RegexExp ")" ";" 37
    public void cmdFuncao() throws IOException {
        if(!eat(Tag.ABRE_PARENTESES))
            sintaticError("Esperado \"(\"; encontrado " + "\"" + this.token.getName() + "\"");
        regexExp();
        if(!eat(Tag.FECHA_PARENTESES))
            sintaticError("Esperado \")\"; encontrado " + "\"" + this.token.getName() + "\"");
        if(!eat(Tag.PONTO_VIRGULA))
            sintaticError("Esperado \";\"; encontrado " + "\"" + this.token.getName() + "\"");
    }

    @Override
    //RegexExp	→ Expressao RegexExp’ 38 | ε 39
    public void regexExp() throws IOException {
        if(isTag(Tag.ID, Tag.ABRE_PARENTESES, Tag.CONST_INT, Tag.CONST_DOUBLE,
                Tag.CONST_STRING, Tag.KW_TRUE, Tag.KW_FALSE, Tag.OPUNARIO_NEGACAO,
                Tag.OPUNARIO_NEGATIVO))
            expressao();
        if(!isTag(Tag.FECHA_PARENTESES))
            skip("Esperado \")\"; encontrado " + "\"" + this.token.getName() + "\"");
    }

    @Override
    //RegexExp’	→ , Expressao RegexExp’ 40 | ε 41
    public void regexExp_() throws IOException {
        if(eat(Tag.VIRGULA)){
            expressao();
            regexExp_();
        } else if(!isTag(Tag.FECHA_PARENTESES))
            sintaticError("Esperado \"), ,\"; encontrado " + "\"" + this.token.getName() + "\"");
    }

    @Override
    //Expressao	→ Exp1 Exp’ 42
    public void expressao() throws IOException {
        if(isTag(Tag.KW_DEF, Tag.FECHA_PARENTESES, Tag.VIRGULA, Tag.OP_OR, Tag.OP_AND)){
            exp1();
            exp_();
        } else {
            skip("Esperado \")\"; encontrado " + "\"" + this.token.getName() + "\"");
        }
    }

    @Override
    //Exp’ → "or" Exp1 Exp’ 43 | "and" Exp1 Exp’ 44 | ε 45
    public void exp_() throws IOException {
        if(eat(Tag.OP_OR)){
            exp1();
            exp_();
        }
        else if(eat(Tag.OP_AND)){
            exp1();
            exp_();
        }
        else if(!isTag(Tag.PONTO_VIRGULA, Tag.ABRE_PARENTESES, Tag.FECHA_PARENTESES)){
            skip("Esperado \"or, and, ;, (, )\"; encontrado " + "\"" + this.token.getName() + "\"");
        }
    }

    @Override
    //Exp1	→ Exp2 Exp1’ 46
    public void exp1() throws IOException {
        if(isTag(Tag.ID, Tag.ABRE_PARENTESES, Tag.CONST_INT, Tag.CONST_DOUBLE,
                Tag.CONST_STRING, Tag.KW_TRUE, Tag.KW_FALSE, Tag.OPUNARIO_NEGATIVO,
                Tag.OPUNARIO_NEGACAO)){
            exp2();
            exp1_();
        } else {
            skip("Esperado \"ID, (, consInt, constDouble, constStr, true, false, -, !\"; encontrado " + "\"" + this.token.getName() + "\"");
        }
    }

    @Override
    //Exp1’	→ "<" Exp2 Exp1’ 47 | "<=" Exp2 Exp1’ 48 | ">" Exp2 Exp1’ 49 |
    // ">=" Exp2 Exp1’ 50 | "==" Exp2 Exp1’ 51 | "!=" Exp2 Exp1’ 52 | ε 53
    public void exp1_() throws IOException {
        if(eat(Tag.OP_MAIOR)){
            exp2();
            exp1_();
        }
        else if(eat(Tag.OP_MAIOR_IGUAL)){
            exp2();
            exp1_();
        }
        else if(eat(Tag.OP_MENOR)){
            exp2();
            exp1_();
        }
        else if(eat(Tag.OP_MENOR_IGUAL)){
            exp2();
            exp1_();
        }
        else if(eat(Tag.OP_COMPARACAO)){
            exp2();
            exp1_();
        }
        else if(eat(Tag.OP_DIFERENTE)){
            exp2();
            exp1();
        }
        else if(!isTag(Tag.PONTO_VIRGULA, Tag.FECHA_PARENTESES, Tag.VIRGULA,
                Tag.OP_OR, Tag.OP_AND)){
            sintaticError("Esperado \"), ,\"; encontrado " + "\"" + this.token.getName() + "\"");
        }
        else {
            skip("Esperado \", >, >=, <, <=, ==, !=, ;, ), ,\"; encontrado " + "\"" + this.token.getName() + "\"");
        }
    }

    @Override
    //Exp2	→ Exp3 Exp2’ 54
    public void exp2() throws IOException {
        if(isTag(Tag.ID, Tag.ABRE_PARENTESES, Tag.CONST_INT, Tag.CONST_DOUBLE, Tag.CONST_STRING, Tag.KW_TRUE,
                Tag.KW_FALSE, Tag.OPUNARIO_NEGACAO, Tag.OPUNARIO_NEGATIVO)){
            exp3();
            exp2_();
        } else {
            skip("Esperado \"ID, (, consInt, constDouble, constStr, true, false, -, !\"; encontrado " + "\"" + this.token.getName() + "\"");
        }
    }

    @Override
    //Exp2’	→ "+" Exp3 Exp2’ 55 | "-" Exp3 Exp2’ 56 | ε 57
    public void exp2_() throws IOException {
        if(eat(Tag.OP_SOMA) || eat(Tag.OP_SUBTRACAO)) {
            exp3();
            exp2_();
        } else if(!isTag(Tag.PONTO_VIRGULA, Tag.FECHA_PARENTESES, Tag.VIRGULA, Tag.OP_OR, Tag.OP_AND, Tag.OP_MAIOR, Tag.OP_MAIOR_IGUAL,
                Tag.OP_MENOR, Tag.OP_MENOR_IGUAL, Tag.OP_COMPARACAO, Tag.OP_DIFERENTE)){
            skip("Esperado \"+, -, ;, ), ,, or, and, <, <=, >, >=, ==, !=\"; encontrado " + "\"" + this.token.getName() + "\"");
        }
    }

    @Override
    //Exp3	→ Exp4 Exp3’ 58
    public void exp3() throws IOException {
        if(isTag(Tag.ID, Tag.ABRE_PARENTESES, Tag.CONST_INT, Tag.CONST_DOUBLE, Tag.CONST_STRING, Tag.KW_TRUE, Tag.KW_FALSE,
                Tag.OPUNARIO_NEGACAO, Tag.OPUNARIO_NEGATIVO)){
            exp4();
            exp3_();
        } else {
            skip("Esperado \"ID, (, constInt, constDouble, constString, true, false, !, -n\"; encontrado " + "\"" + this.token.getName() + "\"");
        }
    }

    @Override
    //Exp3’	→ "*" Exp4 Exp3’ 59 | "/" Exp4 Exp3’ 60 | ε 61
    public void exp3_() throws IOException {
        if(eat(Tag.OP_MULTIPLICACAO, Tag.OP_DIVISAO)){
            exp4();
            exp3_();
        } else if(!isTag(Tag.PONTO_VIRGULA, Tag.FECHA_PARENTESES, Tag.VIRGULA, Tag.OP_OR, Tag.OP_AND, Tag.OP_MAIOR, Tag.OP_MAIOR_IGUAL,
                Tag.OP_MENOR, Tag.OP_MENOR_IGUAL, Tag.OP_COMPARACAO, Tag.OP_DIFERENTE, Tag.OP_SOMA, Tag.OP_SUBTRACAO)){
            skip("Esperado \";, ), ,, or, and, <, <=, >, >= , ==, != , +, -\"; encontrado " + "\"" + this.token.getName() + "\"");
        }
    }

    @Override
    //Exp4	→ ID Exp4’ 62 | ConstInteger 63 | ConstDouble 64 | ConstString 65 |
    // "true" 66 | "false" 67 | OpUnario Exp4 68 | "(" Expressao")" 69
    public void exp4() throws IOException {
        if(eat(Tag.ID)){
            exp4_();
        } else if(eat(Tag.OPUNARIO_NEGATIVO, Tag.OPUNARIO_NEGATIVO)){
            exp4();
        } else if(eat(Tag.ABRE_PARENTESES)){
            expressao();
            if(!eat(Tag.FECHA_PARENTESES))
                sintaticError("Esperado \")\"; encontrado " + "\"" + this.token.getName() + "\"");
        } else if(!eat(Tag.CONST_INT, Tag.CONST_DOUBLE, Tag.CONST_STRING, Tag.KW_TRUE, Tag.KW_FALSE)){
            sintaticError("Esperado \"ID, constInt, constDouble, constStr, true, false, !, -n, (\"; encontrado " + "\"" + this.token.getName() + "\"");
        }
    }

    @Override
    //Exp4’	→ "(" RegexExp ")" 70 | ε 71
    public void exp4_() throws IOException {
        if(eat(Tag.ABRE_PARENTESES)){
            regexExp();
            if(!eat(Tag.FECHA_PARENTESES))
                sintaticError("Esperado \")\"; encontrado " + "\"" + this.token.getName() + "\"");
        } else if(!isTag(Tag.PONTO_VIRGULA, Tag.FECHA_PARENTESES, Tag.VIRGULA, Tag.OP_OR, Tag.OP_AND, Tag.OP_MAIOR,
                Tag.OP_MAIOR_IGUAL,Tag.OP_MENOR, Tag.OP_MENOR_IGUAL, Tag.OP_COMPARACAO, Tag.OP_DIFERENTE,
                Tag.OP_SOMA, Tag.OP_SUBTRACAO, Tag.OP_MULTIPLICACAO, Tag.OP_DIVISAO)){
            skip("Esperado \";, ), ,, or, and, <, <=, >, >= , ==, != , +, -, *, /\"; encontrado " + "\"" + this.token.getName() + "\"");
        }
    }

    @Override
    //OpUnario	→ "-" 72 | "!" 73
    public void opUnario() throws IOException {
        if(!eat(Tag.OPUNARIO_NEGATIVO, Tag.OPUNARIO_NEGACAO))
            skip("Esperado \"-n, !\"; encontrado " + "\"" + this.token.getName() + "\"");
    }

    @Override
    public Lexer getLexer() {
        return lexer;
    }

    private boolean isTag(Tag... tags){
        for (Tag t: tags) {
            if(this.token.getName().equals(t.toString())){
                return true;
            }
        }
        return false;
    }
}
