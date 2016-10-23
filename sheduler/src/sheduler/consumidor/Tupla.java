/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sheduler.consumidor;

import java.sql.Timestamp;

/**
 *
 * @author Clodoaldo
 */
public class Tupla {

    int idoperacao;
    int indicetransacao;
    String operacao;
    String itemdado;
    Timestamp timeStamp;

    public Tupla() {

    }

    public int getIdoperacao() {
        return idoperacao;
    }

    public void setIdoperacao(int idoperacao) {
        this.idoperacao = idoperacao;
    }

    public int getIndicetransacao() {
        return indicetransacao;
    }

    public void setIndicetransacao(int indicetransacao) {
        this.indicetransacao = indicetransacao;
    }

    public String getOperacao() {
        return operacao;
    }

    public void setOperacao(String operacao) {
        this.operacao = operacao;
    }

    public String getItemdado() {
        return itemdado;
    }

    public void setItemdado(String itemdado) {
        this.itemdado = itemdado;
    }

    public Timestamp getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Timestamp timeStamp) {
        this.timeStamp = timeStamp;
    }

    @Override
    public String toString() {
        return "Tupla{" + "idoperacao=" + idoperacao + ", indicetransacao=" + indicetransacao + ", operacao=" + operacao + ", itemdado=" + itemdado + ", timeStamp=" + timeStamp + '}';
    }

    
    
}
