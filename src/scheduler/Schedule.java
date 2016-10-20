/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package scheduler;

import java.sql.Timestamp;

/**
 *
 * @author personal
 */
public class Schedule
{
    private Integer idOperacao;
    private Integer indiceTransacao;
    private String operacao;
    private String itemDado;
    private Timestamp timeStampJ;
    
    /**
     * @return the idOperacao
     */
    public Integer getIdOperacao() {
        return idOperacao;
    }

    /**
     * @param idOperacao the idOperacao to set
     */
    public void setIdOperacao(Integer idOperacao) {
        this.idOperacao = idOperacao;
    }

    /**
     * @return the indiceTransacao
     */
    public Integer getIndiceTransacao() {
        return indiceTransacao;
    }

    /**
     * @param indiceTransacao the indiceTransacao to set
     */
    public void setIndiceTransacao(Integer indiceTransacao) {
        this.indiceTransacao = indiceTransacao;
    }

    /**
     * @return the operacao
     */
    public String getOperacao() {
        return operacao;
    }

    /**
     * @param operacao the operacao to set
     */
    public void setOperacao(String operacao) {
        this.operacao = operacao;
    }

    /**
     * @return the itemDado
     */
    public String getItemDado() {
        return itemDado;
    }

    /**
     * @param itemDado the itemDado to set
     */
    public void setItemDado(String itemDado) {
        this.itemDado = itemDado;
    }

    /**
     * @return the timeStampJ
     */
    public Timestamp getTimeStampJ() {
        return timeStampJ;
    }

    /**
     * @param timeStampJ the timeStampJ to set
     */
    public void setTimeStampJ(Timestamp timeStampJ) {
        this.timeStampJ = timeStampJ;
    }

    
    
}
