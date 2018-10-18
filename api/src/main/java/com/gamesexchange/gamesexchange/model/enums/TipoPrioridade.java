package com.gamesexchange.gamesexchange.model.entities.enums;

/**
 * Games Exchange API
 * Wender Galan
 * Todos os direitos reservados ©
 * *********************************************
 * Nome do arquivo: TipoPrioridade.java
 * Criado por : Wender Galan
 * Data da criação : 17/10/2018
 * Observação :
 * *********************************************
 */
public enum TipoPrioridade {
    MUITOBAIXA(0), BAIXA(1), MEDIA(2), ALTA(3), MUITOALTA(4);

    private int code;

    TipoPrioridade(int code) {
        this.code = code;
    }

    /**
     * Gets code.
     *
     * @return the code
     */
    public int getCode() {
        return code;
    }

}
