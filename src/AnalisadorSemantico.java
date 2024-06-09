import java.util.List;

public class AnalisadorSemantico {
    private TabelaDeSimbolos tabelaDeSimbolos;

    public AnalisadorSemantico() {
        this.tabelaDeSimbolos = new TabelaDeSimbolos();
    }

    public void adicionarVariavel(String nome, Tipo tipo) {
        tabelaDeSimbolos.adicionarVariavel(nome, tipo);
    }

    public void adicionarFuncao(String nome, List<Tipo> tiposParametros, Tipo tipoRetorno) {
        tabelaDeSimbolos.adicionarFuncao(nome, new Funcao(nome, tiposParametros, tipoRetorno));
    }

    public void analisar(List<Token> tokens) {
        try {
            for (Token token : tokens) {
                if (token.getTipo() == TipoToken.IDENTIFICADOR) {
                    Tipo tipoVariavel = tabelaDeSimbolos.obterTipoVariavel(token.getValor());
                    if (tipoVariavel == null) {
                        throw new Exception("Variável '" + token.getValor() + "' não foi declarada.");
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Erro semântico: " + e.getMessage());
        }
    }

    public void analisarAtribuicao(String nomeVariavel, Tipo tipoExpressao) throws Exception {
        Tipo tipoVariavel = tabelaDeSimbolos.obterTipoVariavel(nomeVariavel);

        if (tipoVariavel == null) {
            throw new Exception("Variável não declarada: " + nomeVariavel);
        }

        if (!tipoVariavel.getClass().equals(tipoExpressao.getClass())) {
            throw new Exception("Tipo incompatível para atribuição à variável: " + nomeVariavel);
        }
    }

    public void analisarDeclaracaoFuncao(String nome, List<Tipo> tiposParametros, Tipo tipoRetorno) throws Exception {
        if (tabelaDeSimbolos.obterFuncao(nome) != null) {
            throw new Exception("Função já declarada: " + nome);
        }

        adicionarFuncao(nome, tiposParametros, tipoRetorno);
    }

    public void analisarChamadaFuncao(String nome, List<Tipo> tiposArgumentos) throws Exception {
        Funcao funcao = tabelaDeSimbolos.obterFuncao(nome);

        if (funcao == null) {
            throw new Exception("Função não declarada: " + nome);
        }

        List<Tipo> tiposParametros = funcao.getTiposParametros();
        if (tiposParametros.size() != tiposArgumentos.size()) {
            throw new Exception("Número de argumentos incompatível na chamada da função: " + nome);
        }

        for (int i = 0; i < tiposParametros.size(); i++) {
            if (!tiposParametros.get(i).getClass().equals(tiposArgumentos.get(i).getClass())) {
                throw new Exception("Tipo de argumento incompatível na posição " + (i + 1) + " na chamada da função: " + nome);
            }
        }
    }

    public TabelaDeSimbolos getTabelaDeSimbolos() {
        return tabelaDeSimbolos;
    }
}
