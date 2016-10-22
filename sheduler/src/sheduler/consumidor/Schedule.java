package sheduler.consumidor;

import java.sql.Timestamp;
import java.util.LinkedList;
import java.util.Random;

public class Schedule {
	private LinkedList<Operacao> scheduleInList;


	public Schedule(LinkedList<Transacao> transacoes) {
		scheduleInList = new LinkedList<>();
		ligaOperacoes(transacoes);
	}
        public Schedule(){
            
        }

	private void ligaOperacoes(LinkedList<Transacao> transacoes) {
		Random r = new Random();
		while( !transacoes.isEmpty() ){
	       	int n = r.nextInt(transacoes.size());
	       	if(!transacoes.get(n).transIsEmpty()) {
	       		scheduleInList.add(transacoes.get(n).getFirstOp());
	       		transacoes.get(n).removeOp();
	       	} else {
	       		transacoes.remove(n);
	       	}
	   }
	}
	
	public LinkedList<Operacao> getScheduleInList() {
		return scheduleInList;
	}

	public void setScheduleInList(LinkedList<Operacao> scheduleInList) {
		this.scheduleInList = scheduleInList;
	}
        
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
