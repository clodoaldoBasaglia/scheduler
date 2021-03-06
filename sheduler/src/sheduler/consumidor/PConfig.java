/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sheduler.consumidor;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author personal
 */
public final class PConfig {
    //Inicio da area de Configuração

    //Host do Postgre
    private String pHost = "localhost";
    //Porta do Postgre
    private Integer pPort = 5432;
    //Banco de dados alvo
    private String pDb = "teste";
    //Usuário do Postgre
    private String pUser = "postgres";
    //Senha do Postgre
    private String pPw = "supersix123";

    //Tabela da schedule
    private String tableSchedule = "schedule";

    //Sql para obter Tupla
    private String sqlGetRow = "SELECT * FROM " + tableSchedule + " WHERE idoperacao = ?";

    //Sql para deletar Tupla
    private String sqlDeleteRow = "DELETE FROM " + tableSchedule + " WHERE idoperacao = ?";

    private ArrayList<Tupla> filaIniciada = new ArrayList<Tupla>();
    private ArrayList<Tupla> deadLock = new ArrayList<Tupla>();
    private ArrayList<Tupla> filaTransacoes = new ArrayList<Tupla>();
    private ArrayList<Tupla> filaLeitura = new ArrayList<Tupla>();
    private ArrayList<Tupla> filaEscrita = new ArrayList<Tupla>();
    private ArrayList<Tupla> filaProcessar = new ArrayList<Tupla>();
    HashMap<Tupla, ArrayList<Tupla>> mapaTupla = new HashMap<Tupla, ArrayList<Tupla>>();

    //Fim da area de Configuração
    String url = "jdbc:postgresql://" + pHost + ":" + pPort + "/" + pDb;
    Connection conn;

    public Connection startConnection() {
        try {
            ArrayList<Tupla> arrayTupla = new ArrayList<>();
            Class.forName("org.postgresql.Driver");
            conn = DriverManager.getConnection(url, pUser, pPw);
            if (!conn.isClosed()) {
                System.out.println("conexão realizada");
                Runnable codigoThread;
                codigoThread = new Runnable() {
                    @Override
                    public void run() {
                        Statement stm;
                        try {
                            stm = conn.createStatement();
                            ResultSet pRs = stm.executeQuery("SELECT * FROM schedule;");
//                            while(pRs.next()){
//                                System.out.println(pRs.getArray(1));
//                            }
                            diferenciador(pRs);
                        } catch (SQLException ex) {
                            Logger.getLogger(PConfig.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }

                    private void diferenciador(ResultSet pRs) throws SQLException {
                        while (pRs.next()) {
                            Tupla t = new Tupla();
//                            System.out.println(pRs.getInt(1) +" "+pRs.getInt(2)+ " "+ pRs.getString(3)+
//                                    " "+pRs.getString(4)+ " "+pRs.getString(5));
                            t.setIdoperacao(pRs.getInt(1));
                            t.setIndicetransacao(pRs.getInt(2));
                            t.setOperacao(pRs.getString(3));
                            t.setItemdado(pRs.getString(4));
                            t.setTimeStamp(pRs.getString(5));
                            arrayTupla.add(t);
                        }
                        try {
                            classificadorDeOperacoes(arrayTupla);
                        } catch (ParseException ex) {
                            Logger.getLogger(PConfig.class.getName()).log(Level.SEVERE, null, ex);
                        }

                    }

                    private void classificadorDeOperacoes(ArrayList<Tupla> arrayTupla) throws ParseException {

                        for (Tupla tupla : arrayTupla) {
                            System.out.println(tupla.getOperacao());
                            if (tupla.getOperacao().equalsIgnoreCase("S")) {
                                filaIniciada.add(tupla);
                            } else if (tupla.getOperacao().equalsIgnoreCase("R")) {
                                filaLeitura.add(tupla);
                            } else if (tupla.getOperacao().equalsIgnoreCase("W")) {
                                filaEscrita.add(tupla);
                            }
                        }
                        ArrayList<Tupla> aux = new ArrayList<>();
                        for (Tupla tu : filaIniciada) {
                            for (Tupla t : arrayTupla) {
                                if (t.getIndicetransacao() == tu.getIndicetransacao()) {
                                    aux.add(t);
                                }
                            }
                            mapaTupla.put(tu, aux);
                        }
                        SimpleDateFormat dateF = new SimpleDateFormat("yyyyMMdd_hhmmss");
                        for (Map.Entry<Tupla, ArrayList<Tupla>> entry : mapaTupla.entrySet()) {
                            Tupla key = entry.getKey();
                            ArrayList<Tupla> value = entry.getValue();
                            for (Tupla tu : value) {
                                Date par = dateF.parse(tu.getTimeStamp());
                                Timestamp aux1 = new java.sql.Timestamp(par.getTime());
                                for (Tupla t : filaEscrita) {
                                    par = dateF.parse(t.getTimeStamp());
                                    Timestamp aux2 = new java.sql.Timestamp(par.getTime());
                                    if (t.getIndicetransacao() != tu.getIndicetransacao()
                                            || t.getOperacao() == tu.getOperacao()
                                            || t.getItemdado() == tu.getItemdado()
                                            || aux1.before(aux2)) {
                                        deadLock.add(t);
                                    } else if(tu.getOperacao().equalsIgnoreCase("E")) {
                                        filaProcessar.add(tu);
                                    }
                                }
                            }
                        }
                        salvaBD(deadLock,filaProcessar);
                        
                    }

                    private void salvaBD(ArrayList<Tupla> deadLock, ArrayList<Tupla> filaProcessar) {
                        //FALTA SÓ SALVAR ESSES, FILAPROCESSAR PRIMEIRO
                        //DEPOIS DEADLOCK
                    }

                };
                Thread tLeitura = new Thread(codigoThread);
                tLeitura.setName("fucking cunt");
                tLeitura.start();

                /*Statement stm = conn.createStatement();
                
                ResultSet pRs = stm.executeQuery("SELECT nome, idade, pai\n" +
"  FROM public.cadastro;");
                
                while(pRs.next())
                {
                    System.out.println(pRs.getArray(1) + " " + pRs.getInt(2) + " " + pRs.getArray(3));
                }
                pRs.close();
                
                conn.close();*/
                return conn;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public Integer stopConnection(Connection conn) {
        try {
            conn.close();
            return 1;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    //Inicia Prepared Statement
    private PreparedStatement initPreparedStatement(Connection conn, String sql) {
        try {
            PreparedStatement stmt = conn.prepareStatement(sql);
            return stmt;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    //Se tem Próxima Tupla
    private Boolean hasNextRow(Connection conn, Integer i) {
        try {
            PreparedStatement stmt = initPreparedStatement(conn, sqlGetRow);
            stmt.setInt(1, i);

            ResultSet pRs = stmt.executeQuery();
            if (pRs.next()) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    //Executa a query e retorna um Schedule, com dados da tupla i
    private Schedule getRow(Connection conn, Integer i) {
        try {
            Schedule sched = new Schedule();

            PreparedStatement stmt = initPreparedStatement(conn, sqlGetRow);
            stmt.setInt(1, i);

            ResultSet pRs = stmt.executeQuery();

            if (pRs.next()) {
                sched.setIdOperacao(pRs.getInt("idoperacao"));
                sched.setIndiceTransacao(pRs.getInt("indicetransacao"));
                sched.setOperacao(pRs.getString("operacao"));
                sched.setItemDado(pRs.getString("itemdado"));
                sched.setTimeStampJ(pRs.getTimestamp("timestampj"));
                pRs.close();
                stmt.close();
                return sched;
            } else {
                pRs.close();
                stmt.close();
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    //Deleta a tupla i
    private Integer deleteRow(Connection conn, Integer i) {
        try {
            PreparedStatement stmt = initPreparedStatement(conn, sqlDeleteRow);
            stmt.setInt(1, i);

            if (stmt.executeUpdate() == 1) {
                stmt.close();
                return 1;
            } else {
                return 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    //Verifica se o dado já está na lista de lock
    private Boolean isInLock(Schedule sched, List<ItemDadoLock> list) {
        int i = 0;
        while (i < list.size()) {
            if (list.get(i).getItemDado().equals(sched.getItemDado())) {
                return true;
            }
            i++;
        }
        return false;
    }

    private Integer indexOfLock(Schedule sched, List<ItemDadoLock> list) {
        int i = 0;
        while (i < list.size()) {
            if (list.get(i).getItemDado().equals(sched.getItemDado())) {
                return i;
            }
            i++;
        }
        return 0;
    }

    //Execução do Bloqueio compartilhado
    //Semelhante ao slide 02 pag 10
    private void execLockS(Schedule sched, List<ItemDadoLock> lock, List startSched, List commitSched, List sharedLock, List exclusiveLock, Queue aborts) {
        //Pega indice do ItemDado na lista lock
        int i = indexOfLock(sched, lock);
        if (lock.get(i).equals('U')) {
            sharedLock.add(lock.get(i));
            lock.get(i).setLock('S');
        } else if (lock.get(i).equals('S')) {
            sharedLock.add(lock.get(i));
        } else if (lock.get(i).equals('X')) {
            exclusiveLock.add(lock.get(i));
        }
    }

    //Método para executar as schedules
    private void execSchedule(Schedule sched, List<ItemDadoLock> lock, List startSched, List commitSched, List sharedLock, List exclusiveLock, Queue aborts) {
        //Se a operação é S, adiciona na lista de iniciados
        if (sched.getOperacao().equals("S")) {
            startSched.add(sched);
        } //Se não verifica e termina a Schedule
        else if (sched.getOperacao().equals("E")) {
            //verificarETerminarSchedule();
        } else {
            //Se não tiver o dado na lista de Lock, adiciona como Unlocked
            if (!(isInLock(sched, lock))) {
                ItemDadoLock temp = new ItemDadoLock();
                temp.setItemDado(sched.getItemDado());
                temp.setLock('U');
            } else {
                if (sched.getOperacao().equals("R")) {
                    execLockS(sched, lock, startSched, commitSched, sharedLock, exclusiveLock, aborts);
                }
            }

        }
    }

    public PConfig() {
        //Cria conexão e inicia
        Connection conn = startConnection();

        //Lista de bloqueios
        List lock = new ArrayList<ItemDadoLock>();

        //Cria as filas de bloqueio compartilhado, exclusivo e abortados
        List startSched = new ArrayList<Schedule>();
        List commitSched = new ArrayList<Schedule>();
        List sharedLock = new ArrayList<Schedule>();
        List exclusiveLock = new ArrayList<Schedule>();
        Queue aborts = new LinkedList();

        int i = 0;
        while (true) {
            if (hasNextRow(conn, i)) {
                Schedule sched = getRow(conn, i);
                //execSchedule(sched,lock,startSched,commitSched,sharedLock,exclusiveLock,aborts);
                //deleteRow(conn, i);
                System.out.print("foi");
                System.out.println(sched);
            } //Fazer else if pra quando apertar enter cancelar o consumidor
            //E fazer espera
            else {
                break;
            }
            //i++;
        }

        //Termina conexão
        if (stopConnection(conn) == 1) {
            System.out.println("Conexão encerrada");
        } else {
            System.out.println("Conexão não encerrada");
        }
    }
}
