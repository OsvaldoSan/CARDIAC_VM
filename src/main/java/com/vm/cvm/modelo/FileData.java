package com.vm.cvm.modelo;

public class FileData {
    private final String direction;
    private final String datos;

    public FileData(String direction, String datos) {
        this.direction = direction;
        this.datos = datos;
    }

    public String getDirection() {
        return direction;
    }

    public String getDatos() {
        return datos;
    }
}
