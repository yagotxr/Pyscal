package com.powercouple.pyscal;

public class No {

    private Tag tipo;

    public No(){
        this.tipo = Tag.EMPTY;
    }

    public Tag getTipo() {
        return tipo;
    }

    public void setTipo(Tag numerico) {
        this.tipo = numerico;
    }
}
