package br.com.luiscutilak.converter;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Scanner;

public class Principal {

    public static void main(String[] args) throws IOException {

        Boolean running = true;

        do {

            //Mapeando objeto.
            HashMap<Integer, String> currencyCodes = new HashMap<>();

            //Adicionando o código das moedas a serem mapeadas:
            currencyCodes.put(1, "USD");
            currencyCodes.put(2, "BRL");
            currencyCodes.put(3, "EUR");
            currencyCodes.put(4, "JPY");
            currencyCodes.put(5, "CNY");
            currencyCodes.put(6, "INR");

            
            String fromCode;
            String toCode;
            BigDecimal amount;
            int to;
            int from;
            
            Scanner sc = new Scanner(System.in);
            System.out.println("*****************************************************");
            System.out.println("Bem vindo ao conversor de moedas em tempo real!");
            System.out.println("*****************************************************");

            //Moeda atual que o usuario deseja ser convertida.
            System.out.println("Qual a sua moeda que deseja converter?");
            System.out.println("1:USD(US Dollar) \t 2:BRL(Brasil Reais) \t 3:EUR(Euro) \t 4:JPY(Iene Japones) \t 5:CNY(Yuan Chines) \t 6:INR(Rupia Indiana)");
            from = sc.nextInt();

            //Loop while que instrui o usuario a digitar a opção correta, enquanto ele nao digitar opção válida esse loop continua instruindo.
            while (from < 1 || from > 6) {
                System.out.println("Por favor selecione um opção válida(1 a 6)");
                System.out.println("1:USD(US Dollar) \t 2:BRL(Brasil Reais) \t 3:EUR(Euro) \t 4:JPY(Iene Japones) \t 5:CNY(Yuan Chines) \t 6:INR(Rupia Indiana)");
                from = sc.nextInt();
            }
            fromCode = currencyCodes.get(from);

            //Moeda destino para conversão que o usuario deseja
            System.out.println("Deseja converter para qual moeda?");
            System.out.println("1:USD(US Dollar) \t 2:BRL(Brasil Reais) \t 3:EUR(Euro) \t 4:JPY(Iene Japones) \t 5:CNY(Yuan Chines) \t 6:INR(Rupia Indiana)");
            to = sc.nextInt();

            //Loop 2 while, que instrui o usuario a digitar a opção correta, da moeda a ser convertida, loop continua instruindo o usuario a opção correta da moeda..
            while (to < 1 || to > 6) {
                System.out.println("Por favor selecione um opção válida(1 a 6)");
                System.out.println("1:USD(US Dollar) \t 2:BRL(Brasil Reais) \t 3:EUR(Euro) \t 4:JPY(Iene Japones) \t 5:CNY(Yuan Chines) \t 6:INR(Rupia Indiana)");
                to = sc.nextInt();
            }
            toCode = currencyCodes.get(to);

            System.out.println("Agora digite o montante(valor) para qual deseja a conversão");
            amount = sc.nextBigDecimal();

            sendHttpGETRequest(fromCode, toCode, amount);

            System.out.println("Você deseja fazer outra conversão?");
            System.out.println("1:Sim \t 2:Não");
            if(sc.nextInt() != 1) {
                running = false;
            }
        } while (running);

        System.out.println("Obrigado por utilizar nosso conversor de moedas!");

    }

    //Conectando com a api e requisitando as cotações em tempo real.
    private static void sendHttpGETRequest(String fromCode, String toCode, BigDecimal amount) throws IOException {

        //Deixando o formato da saída de forma decimal, mais legivel.
        DecimalFormat f = new DecimalFormat("00.00");

        String GET_URL = "https://v6.exchangerate-api.com/v6/d7e5afb42dc51a25563726b2/pair/" + toCode + "/" + fromCode;
        URL url = new URL(GET_URL);
        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
        httpURLConnection.setRequestMethod("GET");
        int responseCode = httpURLConnection.getResponseCode();

        //Abaixo validamos se nossa resposta com a requisição APi foi bem sucedida, se sim, podemos ler o conteudo.
        if(responseCode == HttpURLConnection.HTTP_OK) {
            BufferedReader in = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();
            //O Loop while irá continuar lendo nossa resposta enquanto ainda houver dados nela. E vamos adicionando ao buffer de string.
            while((inputLine = in.readLine()) != null){
                response.append(inputLine);
            }in.close();

            //convertendo resposta, criando um objeto Json.
            JSONObject json = new JSONObject(response.toString());
            BigDecimal conversionRate = json.getBigDecimal("conversion_rate");
            System.out.println("Taxa de conversão: " + conversionRate);
            System.out.println();
            BigDecimal result = amount.divide(conversionRate, RoundingMode.HALF_UP);
            System.out.println(f.format(amount) + fromCode + " = " + f.format(result) + toCode);

        }

        else {
            System.out.println("Erro ao buscar taxa de cambio!");
        }

    }


}

