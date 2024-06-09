import java.util.List;

public class Main {

    public static void main(String[] args) {
        String codigoFonte = "int x = 10;\n" +
                             "float y = 3.14;\n" +
                             "x = x + 5;\n" +
                             "y = y * 2;";

        AnalisadorLexico analisadorLexico = new AnalisadorLexico();
        List<Token> tokens = analisadorLexico.analisar(codigoFonte);

        System.out.println("Tokens:");
        for (Token token : tokens) {
            System.out.println(token.getTipo() + ": " + token.getValor());
        }

        AnalisadorSintatico analisadorSintatico = new AnalisadorSintatico(tokens);
        analisadorSintatico.analisar();
    }
}
