import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AnalisadorLexico {

    private static final String[] PALAVRAS_RESERVADAS = {
            "int", "float", "double", "char", "boolean", "struct", "if", "else", "while", "for", "switch", "case", "default", "break", "continue", "return"
    };

    public List<Token> analisar(String codigoFonte) {
        List<Token> tokens = new ArrayList<>();
        String[] linhas = codigoFonte.split("\n");

        for (String linha : linhas) {
            Pattern pattern = Pattern.compile("\\d+\\.\\d+|\\d+|[a-zA-Z_][a-zA-Z0-9_]*|\".*?\"|//.*|==|!=|>=|<=|&&|\\|\\||[+\\-*/%]|[!><=]|\\p{Punct}");
            Matcher matcher = pattern.matcher(linha);

            while (matcher.find()) {
                String tokenStr = matcher.group().trim();
                if (tokenStr.isEmpty()) continue;

                if (tokenStr.matches("\\d+\\.\\d+")) {
                    tokens.add(new Token(TipoToken.NUMERO_DECIMAL, tokenStr));
                } else if (tokenStr.matches("\\d+")) {
                    tokens.add(new Token(TipoToken.NUMERO_INTEIRO, tokenStr));
                } else if (tokenStr.matches("[a-zA-Z_][a-zA-Z0-9_]*")) {
                    if (Arrays.asList(PALAVRAS_RESERVADAS).contains(tokenStr)) {
                        tokens.add(new Token(TipoToken.PALAVRA_RESERVADA, tokenStr));
                    } else {
                        tokens.add(new Token(TipoToken.IDENTIFICADOR, tokenStr));
                    }
                } else if (tokenStr.matches("\".*?\"")) {
                    tokens.add(new Token(TipoToken.TEXTO, tokenStr));
                } else if (tokenStr.startsWith("//")) {
                    tokens.add(new Token(TipoToken.COMENTARIO, tokenStr));
                } else {
                    tokens.add(new Token(TipoToken.SIMBOLO_ESPECIAL, tokenStr));
                }
            }
        }

        return tokens;
    }
}
