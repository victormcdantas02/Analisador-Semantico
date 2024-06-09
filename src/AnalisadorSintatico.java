import java.util.List;

public class AnalisadorSintatico {
    private List<Token> tokens;
    private int posicaoAtual;

    public AnalisadorSintatico(List<Token> tokens) {
        this.tokens = tokens;
        this.posicaoAtual = 0;
    }

    private void proximoToken() {
        posicaoAtual++;
    }

    private boolean temMaisTokens() {
        return posicaoAtual < tokens.size();
    }

    private Token tokenAtual() {
        if (temMaisTokens()) {
            return tokens.get(posicaoAtual);
        } else {
            throw new RuntimeException("Erro: Nenhum token disponível.");
        }
    }

    private boolean verificarTipo(TipoToken tipo) {
        return tokenAtual().getTipo() == tipo;
    }

    private void erroSintatico(String mensagem) {
        throw new RuntimeException("Erro sintático: " + mensagem + " na posição " + posicaoAtual);
    }

    public void analisar() {
        programa();
    }

    private void programa() {
        while (temMaisTokens()) {
            declaracao();
        }
    }

    private void declaracao() {
        if (verificarTipo(TipoToken.PALAVRA_RESERVADA)) {
            String palavraReservada = tokenAtual().getValor();
            if (isTipo(palavraReservada)) {
                declaracaoVariavel(palavraReservada);
            } else if (palavraReservada.equals("void") || isTipo(palavraReservada)) {
                declaracaoFuncao(palavraReservada);
            } else {
                erroSintatico("Declaração não reconhecida.");
            }
        } else if (verificarTipo(TipoToken.COMENTARIO)) {
            proximoToken();
        } else {
            erroSintatico("Declaração esperada.");
        }
    }

    private void declaracaoVariavel(String tipo) {
        proximoToken();
        if (verificarTipo(TipoToken.IDENTIFICADOR)) {
            String nomeVariavel = tokenAtual().getValor();
            proximoToken();
            if (verificarTipo(TipoToken.SIMBOLO_ESPECIAL) && tokenAtual().getValor().equals(";")) {
                System.out.println("Declaração de variável: " + nomeVariavel + " do tipo " + tipo);
                proximoToken();
            } else if (verificarTipo(TipoToken.OPERADOR) && tokenAtual().getValor().equals("=")) {
                proximoToken();
                expressao();
                if (verificarTipo(TipoToken.SIMBOLO_ESPECIAL) && tokenAtual().getValor().equals(";")) {
                    System.out.println("Declaração de variável com inicialização: " + nomeVariavel + " do tipo " + tipo);
                    proximoToken();
                } else {
                    erroSintatico("';' esperado após a expressão.");
                }
            } else {
                erroSintatico("';' ou '=' esperado após o identificador.");
            }
        } else {
            erroSintatico("Identificador esperado após o tipo.");
        }
    }

    private void declaracaoFuncao(String tipoRetorno) {
        proximoToken();
        if (verificarTipo(TipoToken.IDENTIFICADOR)) {
            String nomeFuncao = tokenAtual().getValor();
            proximoToken();
            if (verificarTipo(TipoToken.SIMBOLO_ESPECIAL) && tokenAtual().getValor().equals("(")) {
                proximoToken();
                parametros();
                if (verificarTipo(TipoToken.SIMBOLO_ESPECIAL) && tokenAtual().getValor().equals(")")) {
                    proximoToken();
                    bloco();
                    System.out.println("Declaração de função: " + nomeFuncao + " do tipo " + tipoRetorno);
                } else {
                    erroSintatico("')' esperado após os parâmetros.");
                }
            } else {
                erroSintatico("'(' esperado após o identificador.");
            }
        } else {
            erroSintatico("Identificador esperado após o tipo.");
        }
    }

    private void parametros() {
        if (verificarTipo(TipoToken.PALAVRA_RESERVADA)) {
            tipo();
            if (verificarTipo(TipoToken.IDENTIFICADOR)) {
                proximoToken();
                while (verificarTipo(TipoToken.SIMBOLO_ESPECIAL) && tokenAtual().getValor().equals(",")) {
                    proximoToken();
                    tipo();
                    if (verificarTipo(TipoToken.IDENTIFICADOR)) {
                        proximoToken();
                    } else {
                        erroSintatico("Identificador esperado após ','.");
                    }
                }
            } else {
                erroSintatico("Identificador esperado após o tipo.");
            }
        }
    }

    private void bloco() {
        if (verificarTipo(TipoToken.SIMBOLO_ESPECIAL) && tokenAtual().getValor().equals("{")) {
            proximoToken();
            while (!verificarTipo(TipoToken.SIMBOLO_ESPECIAL) || !tokenAtual().getValor().equals("}")) {
                declaracao();
            }
            if (verificarTipo(TipoToken.SIMBOLO_ESPECIAL) && tokenAtual().getValor().equals("}")) {
                proximoToken();
            } else {
                erroSintatico("'}' esperado para fechar o bloco.");
            }
        } else {
            erroSintatico("'{' esperado para iniciar o bloco.");
        }
    }

    private void expressao() {
        if (verificarTipo(TipoToken.IDENTIFICADOR) || verificarTipo(TipoToken.NUMERO_INTEIRO) || verificarTipo(TipoToken.NUMERO_DECIMAL)) {
            proximoToken();
        } else if (verificarTipo(TipoToken.OPERADOR)) {
            proximoToken();
            if (verificarTipo(TipoToken.IDENTIFICADOR) || verificarTipo(TipoToken.NUMERO_INTEIRO) || verificarTipo(TipoToken.NUMERO_DECIMAL)) {
                proximoToken();
            } else {
                erroSintatico("Operando esperado após o operador.");
            }
        } else {
            erroSintatico("Expressão inválida.");
        }
    }

    private boolean isTipo(String palavra) {
        return palavra.equals("int") || palavra.equals("float") || palavra.equals("double") || palavra.equals("char") || palavra.equals("boolean");
    }

    private void tipo() {
        if (verificarTipo(TipoToken.PALAVRA_RESERVADA) && isTipo(tokenAtual().getValor())) {
            proximoToken();
        } else {
            erroSintatico("Tipo inválido ou não reconhecido.");
        }
    }
}
