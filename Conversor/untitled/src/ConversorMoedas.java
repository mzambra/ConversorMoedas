import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class ConversorMoedas {

    private static final String API_KEY = "a5f8dbfb662ea618b33fd939";
    private static final String API_URL = "https://v6.exchangerate-api.com/v6/" + API_KEY + "/latest/USD";

    public static void main(String[] args) {
        try {
            menuConversao();
        } catch (IOException e) {
            System.out.println("Erro ao processar entrada do usuário: " + e.getMessage());
        }
    }

    private static void menuConversao() throws IOException {
        System.out.println("Selecione uma opção de conversão:");
        System.out.println("1. Dólar para Euro");
        System.out.println("2. Dólar para Libra Esterlina");
        System.out.println("3. Dólar para Dólar Canadense");
        System.out.println("4. Dólar para Dólar Australiano");
        System.out.println("5. Dólar para Iene Japonês");
        System.out.println("6. Dólar para Real");

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        int opcao = Integer.parseInt(reader.readLine());

        System.out.println("Digite o valor em Dólar que deseja converter:");
        double valorUSD = Double.parseDouble(reader.readLine());

        double valorConvertido = converterMoeda(opcao, valorUSD);
        System.out.printf("Valor convertido: %.2f %s%n", valorConvertido, nomeMoeda(opcao));
    }

    private static double converterMoeda(int opcao, double valorUSD) throws IOException {
        URL url = new URL(API_URL);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        StringBuilder response = new StringBuilder();
        String line;

        while ((line = reader.readLine()) != null) {
            response.append(line);
        }

        reader.close();
        connection.disconnect();

        String json = response.toString();
        double taxaConversao = extrairTaxaConversao(opcao, json);

        return valorUSD * taxaConversao;
    }

    private static double extrairTaxaConversao(int opcao, String json) {
        switch (opcao) {
            case 1: // USD para EUR
                return extrairValor(json, "\"EUR\":");
            case 2: // USD para GBP
                return extrairValor(json, "\"GBP\":");
            case 3: // USD para CAD
                return extrairValor(json, "\"CAD\":");
            case 4: // USD para AUD
                return extrairValor(json, "\"AUD\":");
            case 5: // USD para JPY
                return extrairValor(json, "\"JPY\":");
            case 6: // USD para BRL
                return extrairValor(json, "\"BRL\":");
            default:
                throw new IllegalArgumentException("Opção inválida.");
        }
    }

    private static double extrairValor(String json, String moeda) {
        int start = json.indexOf(moeda) + moeda.length() + 1;
        int end = json.indexOf(",", start);
        String valor = json.substring(start, end);

        return Double.parseDouble(valor);
    }

    private static String nomeMoeda(int opcao) {
        switch (opcao) {
            case 1:
                return "Euro";
            case 2:
                return "Libra Esterlina";
            case 3:
                return "Dólar Canadense";
            case 4:
                return "Dólar Australiano";
            case 5:
                return "Iene Japonês";
            case 6:
                return "Real";
            default:
                return "";
        }
    }
}
