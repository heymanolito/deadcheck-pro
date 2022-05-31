package com.example.application.backend.data.entity;

public enum Status {
    Entrada,
    Salida,
    Vacaciones;

    public static Status[] userStatus() {
        return new Status[] {
                Entrada,
                Salida
        };
    }
}


