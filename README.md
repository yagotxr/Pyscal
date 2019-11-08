# Trabalho Compiladores - Análise Léxica
O analisador léxico deverá ser implementado com o auxílio de um Autômato Finito Determinístico. Ele deverá reconhecer um lexema e retornar, a cada chamada,um token de acordo com o lexema encontrado.

Para facilitar a implementação, uma Tabela de Símbolos (TS) é usada. Essa tabela contém, inicialmente, todas as palavras reservadas da linguagem. À medida que novos tokens (Identificadores) forem sendo reconhecidos, esses deverão ser consultados na TS antes de serem cadastrados. Somente palavras reservadas e identificadores serão cadastrados na TS. Não é permitido o cadastro de um mesmo token mais de uma vez na TS.

O Analisador Léxico emprime a lista de todos os tokens reconhecidos, assim como imprime o que está cadastrado na Tabela de Símbolos. Na impressão dos tokens, deverá aparecer a tupla <nome_do_token, lexema> assim como linha e coluna do token.

Além de reconhecer os tokens da linguagem, o analisador léxico deverá detectar possíveis erros e reportá-los ao usuário. O programa informa o erro e o local onde ocorreu (linha e coluna). Os erros verificados serão: i) caracteres desconhecidos (não esperados em um padrão ou inválidos), ii) string não fechada antes de quebra de linha e iii) string não-fechada antes do fim de arquivo. Se o analisador léxico encontrar mais do que um erro léxico, o processo de compilação deve ser interrompido.

