/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package scheduler;

/**
 *
 * @author personal
 */
public class ItemDadoLock
{
    private String itemDado;
    private Character lock;

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
     * @return the lock
     */
    public Character getLock() {
        return lock;
    }

    /**
     * @param lock the lock to set
     */
    public void setLock(Character lock) {
        this.lock = lock;
    }
}
