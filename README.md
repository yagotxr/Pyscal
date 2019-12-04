# Trabalho Compiladores 

Análise Léxica

O analisador léxico deverá ser implementado com o auxílio de um Autômato Finito Determinístico. Ele deverá reconhecer um lexema e retornar, a cada chamada,um token de acordo com o lexema encontrado.

Para facilitar a implementação, uma Tabela de Símbolos (TS) é usada. Essa tabela contém, inicialmente, todas as palavras reservadas da linguagem. À medida que novos tokens (Identificadores) forem sendo reconhecidos, esses deverão ser consultados na TS antes de serem cadastrados. Somente palavras reservadas e identificadores serão cadastrados na TS. Não é permitido o cadastro de um mesmo token mais de uma vez na TS.

O Analisador Léxico emprime a lista de todos os tokens reconhecidos, assim como imprime o que está cadastrado na Tabela de Símbolos. Na impressão dos tokens, deverá aparecer a tupla <nome_do_token, lexema> assim como linha e coluna do token.

Além de reconhecer os tokens da linguagem, o analisador léxico deverá detectar possíveis erros e reportá-los ao usuário. O programa informa o erro e o local onde ocorreu (linha e coluna). Os erros verificados serão: i) caracteres desconhecidos (não esperados em um padrão ou inválidos), ii) string não fechada antes de quebra de linha e iii) string não-fechada antes do fim de arquivo. Se o analisador léxico encontrar mais do que um erro léxico, o processo de compilação deve ser interrompido.
Seu analisador sintático deverá ser um analisador de uma única passada. Dessa forma, ele deverá
interagir com o analisador léxico para obter os tokens do arquivo-fonte. Você deve implementar seu
analisador sintático utilizando o algoritmo de Parser Preditivo Recursivo (Tabela Preditiva e
Procedimentos para cada Não-terminal) ou o algoritmo de Parser Preditivo Não-Recursivo (Tabela
Preditiva e Pilha).

Analise Sintatica

O analisador sintático deverá reportar o primeiro erro sintático ocorrido no programa-fonte e abortar
a compilação. O analisador deverá informar qual o erro encontrado e sua localização no arquivofonte.
Para implementar o analisador sintático, você deverá modificar a estrutura gramatical da linguagem,
ou seja, você deverá adequá-la às restrições de precedência de operadores, eliminar a recursividade
à esquerda e fatorar à esquerda a gramática, ou seja, tornar a gramática Pyscal em uma linguagem
LL(1). Portanto, você deverá verificar as regras que infringem as restrições das gramáticas LL(1) e
adaptá-las para torná-la uma gramática LL(1). Antes disso, você deve também tratar as regras
gramaticais descritas por expressões regulares, ou seja, um trecho com * é o fecho de Kleene (de 0
até várias produções), um trecho sob + é o fecho positivo de Kleene (de 1 até várias produções) e
trechos sob o símbolo ? ocorrem 1 vez ou não ocorrem.

Analise Semantica

Nesta etapa, você deverá implementar um analisador semântico para a linguagem Pyscal, cuja
descrição inicial encontra-se no enunciado do Trabalho Prático I, porém, você deve seguir a versão
corrigida da gramática com acréscimo das ações semânticas, que segue em anexo.
Deve-se usar uma implementação de tabela de símbolos (para atualizar os tipos de ID’s) e árvore
sintática abstrata com nós correspondentes às conversões implícitas de tipos (veja exemplo
disponibilizado no uLife). Para essa etapa será verificado apenas os tipos das expressões.
Itens a verificar:
1. Declaração somente dos tipos permitidos (“logico”, “numerico”, “string”, “vazio”, “tipo_erro”);
2. Identificadores declarados antes do uso;
3. Verificação do tipos das expressões entre double e integer resultando em “numerico”;
4. Verificação do tipo resultante das expressões dos comandos if, while e das operações lógicas e
relacionais, que devem retornar “logico”;
5. Verificação do tipo resultante das expressões do comando write, que deve ser do tipo “string”;
6. Em uma atribuição o identificador que recebe o valor deve ter tipo compatível com o tipo do
valor atribuído;
7. Verificação se o método retorna expressão compatível com o tipo de retorno declarado no
método;

