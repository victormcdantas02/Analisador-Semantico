import java.util.HashMap;
import java.util.Map;

public class TabelaDeSimbolos {
    private Map<String, Tipo> variaveis;
    private Map<String, Funcao> funcoes;

    public TabelaDeSimbolos() {
        variaveis = new HashMap<>();
        funcoes = new HashMap<>();
    }

    public void adicionarVariavel(String nome, Tipo tipo) {
        variaveis.put(nome, tipo);
    }

    public void adicionarFuncao(String nome, Funcao funcao) {
        funcoes.put(nome, funcao);
    }

    public Tipo obterTipoVariavel(String nome) {
        return variaveis.get(nome);
    }

    public Funcao obterFuncao(String nome) {
        return funcoes.get(nome);
    }
}
