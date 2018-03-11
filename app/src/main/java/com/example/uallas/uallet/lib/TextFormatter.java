package com.example.uallas.uallet.lib;

import com.example.uallas.uallet.model.TipoDado;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Currency;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
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
     * Format double value to string currency
     *
     * @param value
     *            value to be formatted
     * @param locale
     * @return A String formatada.
     */
    public static String formatCurrencyFromDouble(final double value, Locale locale) throws ParseException {
        // Coloca a formatação nos campos de faturamento
        DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols(
                locale);
        otherSymbols.setDecimalSeparator(',');
        otherSymbols.setGroupingSeparator('.');
        Currency currency = Currency.getInstance(locale);

        DecimalFormat formatter = new DecimalFormat(currency.getSymbol() + "###,###,##0.00",
                otherSymbols);
        return formatter.format(cleanCurrency(String.valueOf(value)));
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

    /**
     * Extract a Double number from currency String.
     *
     * @param amount
     * @return
     */
    public static Double cleanCurrency(final String amount) throws ParseException {
        final NumberFormat format = NumberFormat.getNumberInstance(Locale.US);
        if (format instanceof DecimalFormat) {
            ((DecimalFormat) format).setParseBigDecimal(true);
        }

        String cleanAmount = "";
        String numberAmount = amount.replaceAll("[^\\d.,0-9]","");
        String[] amountSplit = numberAmount.split("[.|,]");

        if(amountSplit.length > 1) {
            for (int i = 0; i < amountSplit.length; i++) {
                if (i == 0) {
                    cleanAmount = amountSplit[i];
                } else if (i == amountSplit.length - 1) {
                    cleanAmount += "." + amountSplit[i];
                } else {
                    cleanAmount += "," + amountSplit[i];
                }
            }
        } else {
            cleanAmount = numberAmount;
        }

        return format.parse(cleanAmount).doubleValue();
    }


    public static String formatCurrencyFromString(String value, Locale locale) {
        Map<String, String> myCurrencies = Utils.getAllCurrencies();

        // Coloca a formatação nos campos de faturamento
        DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols(
                Locale.getDefault());
        otherSymbols.setDecimalSeparator(',');
        otherSymbols.setGroupingSeparator('.');
        Currency currency = Currency.getInstance(locale);
        String curSymbol = myCurrencies.get(currency.getCurrencyCode());

        DecimalFormat formatter = new DecimalFormat(curSymbol + "###,###,##0.00",
                otherSymbols);

        String sValue = value.toString();

        sValue = sValue.replaceAll("[,|.]", "");
        if (sValue.length() > 2)
            sValue = sValue.substring(0, sValue.length() - 2) + "," + sValue.substring(sValue.length() - 2);

        String formated = "";
        try {
            formated = formatter.format(cleanCurrency(sValue.toString()));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return formated;

    }
}
