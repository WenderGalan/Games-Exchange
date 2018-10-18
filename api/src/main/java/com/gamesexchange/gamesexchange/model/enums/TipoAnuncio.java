package com.gamesexchange.gamesexchange.model.entities.enums;

/**
 * Games Exchange API
 * Wender Galan
 * Todos os direitos reservados ©
 * *********************************************
 * Nome do arquivo: TipoAnuncio.java
 * Criado por : Wender Galan
 * Data da criação : 17/10/2018
 * Observação :
 * *********************************************
 */
public enum TipoAnuncio {
    VENDA(0), TROCA(1), TROCAVENDA(2);

    private int code;

    TipoAnuncio(int code) {
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
