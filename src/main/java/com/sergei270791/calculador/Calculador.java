package com.sergei270791.calculador;

import org.springframework.stereotype.Service;

@Service
public class Calculador {

    public int sum(int a, int b) {
        return a + b;
    }
}