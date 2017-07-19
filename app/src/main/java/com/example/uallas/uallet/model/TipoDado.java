package com.example.uallas.uallet.model;
import android.util.SparseArray;
/**
 * Created by Uallas on 17/06/2017.
 */

public enum TipoDado {

    NUMERO(1), MOEDA(2), PORCENTAGEM(3), TEXTO(4), DATA(5), DATAHORA(6), DATA_DB(7);

    public static final String DATA_FORMAT = "dd/MM/yyyy";
    public static final String DATAHORA_FORMAT = "dd/MM/yyyy HH:mm:ss";
    public static final String DATA_FORMAT_DB = "yyyy-MM-dd";

    private static final SparseArray<TipoDado> intToEnum = new SparseArray<TipoDado>();

    static {
        for (TipoDado tipoDado : values()) {
            intToEnum.put(tipoDado.value, tipoDado);
        }
    }

    private final int value;

    TipoDado(int value) {
        this.value = value;
    }

    public int value() {
        return value;
    }

    public String toString() {
        return String.valueOf(value);
    }

    public static TipoDado fromInt(int key) {
        return intToEnum.get(key);
    }

}
