package com.example.vitor.driverforme_motorista.entidades;

import com.google.firebase.database.Exclude;

/**
 * Created by vitor on 25/05/17.
 */

public class Servico {

    private String data, origem, destino, cliente, motorista, tipo, horario, id;
    private double preco;


    public String getHorario() {
        return horario;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setHorario(String horario) {
        this.horario = horario;
    }

    public double getPreco() {
        return preco;
    }

    public void setPreco(double preco) {
        this.preco = preco;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getOrigem() {
        return origem;
    }

    public void setOrigem(String origem) {
        this.origem = origem;
    }

    public String getDestino() {
        return destino;
    }

    public void setDestino(String destino) {
        this.destino = destino;
    }

    public String getCliente() {
        return cliente;
    }

    public void setCliente(String cliente) {
        this.cliente = cliente;
    }

    public String getMotorista() {
        return motorista;
    }

    public void setMotorista(String motorista) {
        this.motorista = motorista;
    }


    public String toString(){
        return "Id:"+getId()+"\nOrigem: "+getOrigem() +"\nDestino: "+getDestino()
                +"\nCliente: "+getCliente()+"\nMotorista: "+getMotorista()+"\nPreço: "+getPreco()
                +"\nData: "+getData()+"\nHorário: "+getHorario();
    }

}
