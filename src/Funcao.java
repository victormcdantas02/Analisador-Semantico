import java.util.List;

public class Funcao {
    private String nome;
    private Tipo tipoRetorno;
    private List<Tipo> tiposParametros;

    public Funcao(String nome, List<Tipo> tiposParametros, Tipo tipoRetorno) {
        this.nome = nome;
        this.tiposParametros = tiposParametros;
        this.tipoRetorno = tipoRetorno;
    }

    public String getNome() {
        return nome;
    }

    public Tipo getTipoRetorno() {
        return tipoRetorno;
    }

    public List<Tipo> getTiposParametros() {
        return tiposParametros;
    }
}
