package com.gamesexchange.gamesexchange.model.entities.enums;

/**
 * Games Exchange API
 * Wender Galan
 * Todos os direitos reservados ©
 * *********************************************
 * Nome do arquivo: TipoUsuario.java
 * Criado por : Wender Galan
 * Data da criação : 17/10/2018
 * Observação :
 * *********************************************
 */
public enum TipoUsuario {
    ADMIN(0), USER(1);

    private int code;

    TipoUsuario(int code) {
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
