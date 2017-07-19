package com.example.vitor.driverforme_motorista.estaticos;

import com.example.vitor.driverforme_motorista.entidades.Motorista;

/**
 * Created by vitor on 25/05/17.
 */

public class MotoristaEstatico {
    private static Motorista motorista = new Motorista();

    public MotoristaEstatico(Motorista m){
        motorista = m;
    }

    public MotoristaEstatico(){

    }

    public static Motorista getMotorista(){
        return motorista;
    }

    public static  void setMotorista(Motorista m){
        motorista = m;
    }
}
