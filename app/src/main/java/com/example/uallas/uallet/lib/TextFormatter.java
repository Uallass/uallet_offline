package com.example.uallas.uallet.lib;

import com.example.uallas.uallet.model.TipoDado;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Uallas on 17/06/2017.
 */

public class TextFormatter {

    private static final Locale BRAZIL = new Locale("pt", "BR");

    private static final DecimalFormatSymbols BRAZIL_SYMBOLS = new DecimalFormatSymbols(
            BRAZIL);

    private static final NumberFormat NF = NumberFormat.getInstance(BRAZIL);

    /**
     * Formata um valor double para o padrão de moeda brasileira.
     *
     * @param value
     *            O valor a ser formatado.
     * @return A String formatada.
     */
    public static String formatCurrency(final double value) {
        final DecimalFormat currencyFormatter = new DecimalFormat(
                "¤ ###,###,##0.00", BRAZIL_SYMBOLS);
        return currencyFormatter.format(value);
    }

    /**
     * Formata um valor double para o padrão de moeda brasileira, sem simbolo
     * R$.
     *
     * @param value
     *            O valor a ser formatado.
     * @return A String formatada.
     */
    public static String formatCurrencyNoSymbol(final double value) {
        final DecimalFormat currencyFormatter = new DecimalFormat(
                "###,###,##0.00");
        return currencyFormatter.format(value);
    }

    /**
     * Formata um valor String para o padrão de moeda brasileira.
     *
     * @param value
     *            O valor a ser formatado.
     * @return A String formatada.
     */
    public static String formatCurrency(final String value) {
        return formatCurrency(ParserHelper.parseDouble(formatValidDouble(value)));
    }

    /**
     * Formata um valor String para o padrão de moeda brasileira.
     *
     * @param value
     *            O valor a ser formatado.
     * @return A String formatada.
     */
    public static String formatCurrencyNoSymbol(final String value) {
        return formatCurrencyNoSymbol(ParserHelper.parseDouble(value));
    }

    public static String formatPhoneNumber(final String value) {

        // Nova string formatada a ser retornada
        final StringBuilder formated = new StringBuilder();

        // Limpa a string recebida para que contenha apenas os dígitos
        String cleanString = value.replaceAll("\\D", "");

        // Trunca a string caso tenha mais que 11 digitos
        if (cleanString.length() > 11) {
            cleanString = cleanString.substring(0, 11);
        }

        // Cria uma lista onde cada elemento é um pedaço da string inicial.
        // A lista é ordenada por significância, ou seja, de trás para
        // frente. Então os digitos 0011112222 são separados [2222, 1111,
        // 00].
        // Caso exista um número menor de dígitos essa diferença é
        // incorporada pelos elementos da lista, por exemplo 112222 ->
        // [2222, 11]
        final List<String> array = new ArrayList<String>();
        final Pattern p = Pattern
                .compile("((\\d{1,2})(\\d{4,5})(\\d{4}))|((\\d{1,5})(\\d{4}))");
        final Matcher m = p.matcher(cleanString);

        if (m.matches()) {
            for (int i = m.groupCount(); i > 0; i--) {
                if (i == 5 || i == 1)
                    continue;
                if (m.group(i) != null) {
                    array.add(m.group(i));
                }
            }
        }

        // Monta a string formatada de acordo com a quantidade de
        // digitos.
        switch (array.size()) {
            case 3:
                formated.append('(');
                formated.append(array.get(2));
                formated.append(") ");
            case 2:
                formated.append(array.get(1));
                formated.append('-');
            case 1:
                formated.append(array.get(0));
                break;
            default:
                formated.append(cleanString);
        }

        return formated.toString();
    }

    /**
     * Formata uma String para um endereço IP válido.
     *
     * @param value
     *            A String a ser formatada.
     * @return O endereço IP formatado.
     */
    public static String formatIp(final String value) {

        final StringBuilder formatted = new StringBuilder();

        if (android.util.Patterns.IP_ADDRESS.matcher(value).matches()) {

            String[] octets = value.split("\\.");

            for (String octet : octets) {
                formatted.append(Integer.parseInt(octet)).append(".");
            }

            formatted.deleteCharAt(formatted.length() - 1);

            return formatted.toString();

        }

        return null;

    }

    public static String formatNumber(final int value) {
        return NF.format(value);
    }

    public static String formatNumber(final double value) {
        return NF.format(value);
    }

    public static String formatNumber(final String value) {
        try {
            String validValue = value;

            if (validValue.isEmpty() || validValue == ""){
                validValue = "0";
            }
            return NF.format(Double.parseDouble(formatValidDouble(validValue)));
        } catch (NumberFormatException e) {
            return "";
        }

    }

    public static String formatPercentage(final String value) {

        try {
            return NumberFormat.getPercentInstance(BRAZIL).format(
                    NF.parse(value));
        } catch (ParseException e) {
            return "";
        }

    }

    public static String formatDate(final Date date) {
        return formatDate(date, TipoDado.DATAHORA);
    }

    public static String formatDate(final Date date, final TipoDado dateType) {

        SimpleDateFormat dateFormatter = new SimpleDateFormat();

        switch (dateType) {

            case DATA_DB:
                dateFormatter.applyPattern(TipoDado.DATA_FORMAT_DB);
                break;

            case DATA:
                dateFormatter.applyPattern(TipoDado.DATA_FORMAT);
                break;

            default:
                dateFormatter.applyPattern(TipoDado.DATAHORA_FORMAT);
                break;

        }

        return dateFormatter.format(date);
    }

    public static String formatDataColumn(final String value,
                                          final TipoDado dataType) {

        switch (dataType) {

            case NUMERO:
                return formatNumber(value);

            case MOEDA:
                return formatCurrency(value);

            case PORCENTAGEM:
                return formatPercentage(value);

            default:
                return value;

        }

    }

    public static String formatFirstToUpperCase(final String value) {
        try {
            return Character.toUpperCase(value.charAt(0)) + value.substring(1);
        } catch (Exception e) {
            return "";
        }
    }

    public static String formatTime(final Date value) {

        final SimpleDateFormat timeFormatter = new SimpleDateFormat("HH:mm");

        return timeFormatter.format(value);
    }

    /**
     * Converte uma string que possui formato de real (com vírgulas e pontos) para uma string
     * no formato double. Dessa forma, é possível usar esse valor para fazer cálculos.
     *
     * @param value valor a ser convertido.
     * @return uma String no formato double.
     */
    public static String fromRealStringToDoubleString(String value) {
        return value.replace(".", "").replace(",", ".");
    }

    public static double roundTwoDecimals(double d) {
        DecimalFormat twoDForm = new DecimalFormat("#.##");
        return Double.valueOf(twoDForm.format(d).replace(",", "."));
    }

    /**
     * Se o valor tiver com virgula nas casas decimais, altera para ponto
     *
     * @param value - valor a ser formatado
     * @return valor formatado como Currency com ponto para casas decimais
     */

    public static String formatValidDouble(String value){

        if(value.length() > 1 && (value.charAt(value.length()-2) == ',' || value.charAt(value.length()-2) == '.')){
            value = value + '0';
        }

        if (value.length() > 2 && value.charAt(value.length()-3) == ','){

            value.substring(0, value.length()-4).replace('.', ',');

            StringBuilder formatedValue = new StringBuilder(value);

            formatedValue.setCharAt(value.length()-3, '.');

            return formatedValue.toString();
        } else{
            return value;
        }

    }

    public static BigDecimal cleanCurrency(final String amount, final Locale locale) throws ParseException {
        final NumberFormat format = NumberFormat.getNumberInstance(locale);
        if (format instanceof DecimalFormat) {
            ((DecimalFormat) format).setParseBigDecimal(true);
        }
        return (BigDecimal) format.parse(amount.replaceAll("[^\\d.,]",""));
    }
}
