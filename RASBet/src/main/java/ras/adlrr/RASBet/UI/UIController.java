package ras.adlrr.RASBet.UI;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.beans.factory.annotation.Autowired;
import ras.adlrr.RASBet.api.*;
import ras.adlrr.RASBet.model.Admin;
import ras.adlrr.RASBet.model.Expert;
import ras.adlrr.RASBet.model.Gambler;

import javax.persistence.Column;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class UIController implements Runnable
{
    //@Autowired
    private BetController betController;
    private GameController gameController;
    private SportController sportController;
    private TransactionController transactionController;
    private UserController userController;
    private WalletController walletController;

    public UIController(BetController betController, GameController gameController,
                        SportController sportController, TransactionController transactionController,
                        UserController userController, WalletController walletController){
        this.betController = betController;
        this.gameController = gameController;
        this.sportController = sportController;
        this.transactionController = transactionController;
        this.userController = userController;
        this.walletController = walletController;
    }

    public static class Flag{
        private Integer flag;
        public static final int SERVER_CLOSED     = -404;
        public static final int CLOSE_CLIENT      = -400;
        public static final int NOT_AUTHENTICATED =   -1;
        public static final int CLIENT_LOGGED_IN  =    0;
        public static final int ADMIN_LOGGED_IN   =    1;
        public static final int EXPERT_LOGGED_IN   =   2;

        public Flag(){
            flag = NOT_AUTHENTICATED;
        }
        public Integer getValue() {
            return flag;
        }
        public void setValue(Integer flag){
            this.flag = flag;
        }
    }
    Flag flag = new Flag();
    Integer clienteID = null;

    @Override
    public void run() {

        //Menu de autenticacao
        Menu menuAutenticao = new Menu("Menu de autenticacao", new String[]{"Registar gambler", "Registar adminstrador","Registar expert", "Autenticar"});
        menuAutenticao.setHandler(1, () -> registarGamblerHandler());
        menuAutenticao.setHandler(2, () -> registarAdminHandler());
        menuAutenticao.setHandler(3, () -> registarExpertHandler());
        menuAutenticao.setHandler(4, () -> autenticarHandler());

        /*
        //Menu de cliente
        Menu menuCliente = new Menu("Cliente", new String[]{"Reservar Viagem", "Cancelar Reserva de Viagem", "Listar Voos", "Listar Viagens", "Listar Viagens a partir de uma Origem ate um Destino","Listar reservas", "Receber resposta de pedidos"});
        menuCliente.setHandlerSaida(() -> flag.setValue(Flag.NOT_AUTHENTICATED));
        menuCliente.setHandler(1, () -> reservarViagemHandler(nrPedido, cliente));
        menuCliente.setHandler(2, () -> cancelarReservaHandler(nrPedido, cliente));
        menuCliente.setHandler(3, () -> listarVoosHandler(nrPedido, cliente));
        menuCliente.setHandler(4, () -> listarViagensHandler(nrPedido, cliente));
        menuCliente.setHandler(5, () -> listarViagensRestritasHandler(nrPedido, cliente));
        menuCliente.setHandler(6, () -> listarReservasHandler(nrPedido,cliente));
        menuCliente.setHandler(7, () -> {});
        menuCliente.setLock(printsLock);

        //Menu de administrador
        Menu menuAdmin = new Menu("Administrador", new String[]{"Executar Operacoes de Cliente", "Inserir Novo Voo", "Encerrar um Dia","Fechar servidor"});
        menuAdmin.setHandlerSaida(() -> flag.setValue(Flag.NOT_AUTHENTICATED));
        menuAdmin.setHandler(1, menuCliente::run);
        menuAdmin.setHandler(2, () -> inserirNovoVooHandler(nrPedido, cliente));
        menuAdmin.setHandler(3, () -> encerrarDiaHandler(nrPedido,cliente));
        menuAdmin.setHandler(4, () -> fecharServidorHandler(nrPedido, cliente));
        menuAdmin.setLock(printsLock);
        */
        while (!flag.getValue().equals(Flag.CLOSE_CLIENT)) {

            //Executa menu de autenticacao
            while (flag.getValue().equals(Flag.NOT_AUTHENTICATED))
                menuAutenticao.runOneTime();

            /*
            if (flag.getValue().equals(Flag.CLIENT_LOGGED_IN))
                menuCliente.run();

            else if (flag.getValue().equals(Flag.ADMIN_LOGGED_IN))
                menuAdmin.run();
            */
            if(flag.getValue().equals(Flag.SERVER_CLOSED)) {
                flag.setValue(Flag.NOT_AUTHENTICATED);
                System.out.println("O Servidor encontra-se fechado. Tente novamente mais tarde!");
            }
        }
    }

    // ****** Handlers Autenticacao ****** //

    private void registarGamblerHandler() {
        //Atualizacao do número de pedido
        MenuInput m1 = new MenuInput("Insira um username", "Username:");
        MenuInput m2 = new MenuInput("Insira um email", "Email:");
        MenuInput m3 = new MenuInput("Insira uma password", "Password:");
        MenuInput m4 = new MenuInput("Insira o seu cartão de cidadão", "Cartão de Cidadão:");
        MenuInput m5 = new MenuInput("Insira a sua nacionalidade", "Nacionalidade:");
        MenuInput m6 = new MenuInput("Insira o seu NIF", "NIF:"); //todo limitar a inteiro
        MenuInput m7 = new MenuInput("Insira a sua ocupação","Ocupação:");
        MenuInput m8 = new MenuInput("Insira a sua data de nascimento", "dd/MM/yyyy:"); //todo limitar a idade
        MenuInput m9 = new MenuInput("Insira o seu código postal", "Código postal:");
        MenuInput m10 = new MenuInput("Insira a sua cidade","Cidade:");
        MenuInput m11 = new MenuInput("Insira o seu endereço","Endereço:");
        MenuInput m12 = new MenuInput("Insira o seu número de télemovel:","Télemovel:"); //todo limitar a inteiro
        m1.executa();
        m2.executa();
        m3.executa();
        m4.executa();
        m5.executa();
        m6.executa();
        m7.executa();
        m8.executa();
        m9.executa();
        m10.executa();
        m11.executa();
        m12.executa();

        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate date = LocalDate.parse(m8.getOpcao(), dateFormat);

        int flagInterna = userController.registerGambler(new Gambler(0,m1.getOpcao(),m3.getOpcao(),m4.getOpcao(), m5.getOpcao(), Integer.parseInt(m6.getOpcao()),m7.getOpcao(),Integer.parseInt(m12.getOpcao()),date,m2.getOpcao(),m9.getOpcao(),m11.getOpcao(),m10.getOpcao()));
        switch (flagInterna) {
            case 0 -> System.out.println("Gambler criado com sucesso");
            case 1 -> System.out.println("Email repetido, a conta gambler não foi criada");
        }
    }

    private void registarAdminHandler() {
        userController.getUserByEmail("arrozcomedor");
        MenuInput m1 = new MenuInput("Insira um username", "Username:");
        MenuInput m2 = new MenuInput("Insira um email", "Email:");
        MenuInput m3 = new MenuInput("Insira uma password", "Password:");
        m1.executa();
        m2.executa();
        m3.executa();

        int flagInterna = userController.registerAdmin(new Admin(0,m1.getOpcao(),m3.getOpcao(),m2.getOpcao()));
        switch (flagInterna) {
            case 0 -> System.out.println("Admininistrador criado com sucesso");
            case 1 -> System.out.println("Email repetido, a conta administrador não foi criada");
        }
    }

    private void registarExpertHandler() {
        MenuInput m1 = new MenuInput("Insira um username", "Username:");
        MenuInput m2 = new MenuInput("Insira um email", "Email:");
        MenuInput m3 = new MenuInput("Insira uma password", "Password:");
        m1.executa();
        m2.executa();
        m3.executa();

        int flagInterna = userController.registerExpert(new Expert(0,m1.getOpcao(),m3.getOpcao(),m2.getOpcao()));
        switch (flagInterna) {
            case 0 -> System.out.println("Expert criado com sucesso");
            case 1 -> System.out.println("Email repetido, a conta expert não foi criada");
        }
    }

    private void autenticarHandler() {
        //Atualizacao do número de pedido
        MenuInput m1 = new MenuInput("Insira o seu email", "Email:");
        MenuInput m2 = new MenuInput("Insira o seu password", "Password:");
        m1.executa();
        m2.executa();

        String headerPedido = "Autenticacao";

        //int flagInterna = cliente.login(nr, m1.getOpcao(), m2.getOpcao());
        int flagInterna = 0;
        if (flagInterna == -1)
            System.out.println("Falha no login");
        else if (flagInterna == 0) {
            System.out.println("Cliente logado com sucesso");
            flag.setValue(Flag.CLIENT_LOGGED_IN);
        } else if (flagInterna == 1) {
            System.out.println("Administrador logado com sucesso");
            flag.setValue(Flag.ADMIN_LOGGED_IN);
        } else if (flagInterna == -2){
            System.out.println("Erro de conexao. Tente novamente. Se o problema persistir o servidor pode estar offline.");
        }
    }
}
