package com.example.uallas.uallet.lib;
import com.example.uallas.uallet.model.TipoDado;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
/**
 * Created by Uallas on 17/06/2017.
 */

public class ParserHelper {
    public static Date parseDate(final String date) throws ParseException {
        return parseDate(date, TipoDado.DATAHORA);
    }

    /**
     * Faz a transformação de um String para uma Data.
     *
     * @param date
     * @param dateType
     * @return
     * @throws ParseException
     */
    public static Date parseDate(final String date, final TipoDado dateType) {

        SimpleDateFormat dateFormatter = new SimpleDateFormat();

        switch (dateType) {
            case DATA:
                dateFormatter.applyPattern(TipoDado.DATA_FORMAT);
                break;

            case DATA_DB:
                dateFormatter.applyPattern(TipoDado.DATA_FORMAT_DB);
                break;

            default:
                dateFormatter.applyPattern(TipoDado.DATAHORA_FORMAT);
                break;
        }

        try {
            return dateFormatter.parse(date);
        } catch (ParseException e) {
            return null;
        }
    }

    /**
     * Limpa a vírgula de uma string, e retorna um Double dela. Se não
     * conseguir, retorna 0.
     *
     */
    public static double parseDouble(String value) {
        try {
            return Double.parseDouble(value.replace(",", ""));
        } catch (NumberFormatException e) {
            return 0d;
        }
    }

    public static int parseInt(String value) {

        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    public static Date parseTime(String value) throws ParseException {

        final SimpleDateFormat timeParser = new SimpleDateFormat("HH:mm");

        return timeParser.parse(value);
    }
}

