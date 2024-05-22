package br.com.luiscutilak.converter;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Scanner;

public class Principal {
    public static void main(String[] args) throws IOException {

        HashMap<Integer, String> currencyCodes = new HashMap<>();

        //Adicionando o código das moedas a serem mapeadas:
        currencyCodes.put(1, "USD");
        currencyCodes.put(2, "BRL");
        currencyCodes.put(3, "EUR");
        currencyCodes.put(4, "JPY");
        currencyCodes.put(5, "CNY");
        currencyCodes.put(6, "INR");

        String fromCode, toCode;
        double amount;

        Scanner sc = new Scanner(System.in);
        System.out.println("*****************************************************");
        System.out.println("Bem vindo ao conversor de moedas em tempo real!");
        System.out.println("*****************************************************");

        //Moeda atual que o usuario deseja ser convertida.
        System.out.println("Qual a sua moeda que deseja converter?");
        System.out.println("1:USD(US Dollar) \t 2:BRL(Brasil Reais) \t 3:EUR(Euro) \t 4:JPY(Iene Japones) \t 5:CNY(Yuan Chines) \t 6:INR(Rupia Indiana)");
        fromCode = currencyCodes.get(sc.nextInt());

        //Moeda destino para conversão que o usuario deseja
        System.out.println("Deseja converter para qual moeda?");
        System.out.println("1:USD(US Dollar) \t 2:BRL(Brasil Reais) \t 3:EUR(Euro) \t 4:JPY(Iene Japones) \t 5:CNY(Yuan Chines) \t 6:INR(Rupia Indiana)");
        toCode = currencyCodes.get(sc.nextInt());

        System.out.println("Agora digite o montante(valor) para qual deseja a conversão");
        amount = sc.nextFloat();

        System.out.println("Obrigado por utilizar nosso conversor de moedas!");
    }

    //Conectando com a api e requisitando as cotações em tempo real.
    private static void sendHttpGETRequest(String fromCode, String toCode, double amount) throws IOException {

        //Deixando o formato da saída de forma decimal, mais legivel.
        DecimalFormat f = new DecimalFormat("00.00");

        String GET_URL = "https://v6.exchangerate-api.com/v6/d7e5afb42dc51a25563726b2/pair/" + toCode + fromCode;
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
            JSONObject obj = new JSONObject(response.toString());
            Double exchangeRate = obj.getJSONObject("conversion_rate").getDouble(fromCode);
            System.out.println(exchangeRate);
            System.out.println();
            System.out.println(f.format(amount) + fromCode + " = " + f.format(amount/exchangeRate) + toCode);

        }

        else {
            System.out.println("Busca requisita falhou!");
        }

    }


}
