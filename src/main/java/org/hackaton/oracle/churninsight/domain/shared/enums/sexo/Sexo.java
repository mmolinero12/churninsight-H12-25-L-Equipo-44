package org.hackaton.oracle.churninsight.domain.shared.enums.sexo;

public enum Sexo {

    M("M"),
    F("F"),
    O("O");

    private final String code;

    Sexo(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    // Método para convertir de CHAR a enum
    public static Sexo fromCode(String code) {
        for (Sexo s : Sexo.values()) {
            if (s.code.equalsIgnoreCase(code)) {
                return s;
            }
        }
        throw new IllegalArgumentException("Código de sexo inválido: " + code);
    }



}
