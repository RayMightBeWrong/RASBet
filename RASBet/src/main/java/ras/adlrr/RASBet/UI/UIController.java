package ras.adlrr.RASBet.UI;

import org.springframework.http.ResponseEntity;
import ras.adlrr.RASBet.api.*;
import ras.adlrr.RASBet.model.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

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
        public static final int GAMBLER_LOGGED_IN  =    0;
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
    float currentBet = 0.0f;
    @Override
    public void run() {

        //Menu de autenticacao
        Menu menuAutenticao = new Menu("Menu de autenticacao", new String[]{"Registar gambler", "Registar adminstrador","Registar expert", "Autenticar"});
        menuAutenticao.setHandlerSaida(() -> flag.setValue(Flag.CLOSE_CLIENT));
        menuAutenticao.setHandler(1, this::registarGamblerHandler);
        menuAutenticao.setHandler(2, this::registarAdminHandler);
        menuAutenticao.setHandler(3, this::registarExpertHandler);
        menuAutenticao.setHandler(4, this::autenticarHandler);

        //Menu de gambler
        Menu menuGambler = new Menu("Cliente", new String[]{"Consultar  Jogos", "Alterar perfil", "Historico de Transações", "Historico de Apostas", "Consultar carteiras","Listar reservas", "Receber resposta de pedidos"});
        menuGambler.setHandlerSaida(() -> flag.setValue(Flag.NOT_AUTHENTICATED));
        menuGambler.setHandler(1, this::consultarJogosMenuHandler);
        menuGambler.setHandler(2, this::alterarPerfilHandler);
        menuGambler.setHandler(3, this::historicoTransacoesHandler);
        menuGambler.setHandler(4, this::historicoApostasHandler);
        menuGambler.setHandler(5,this::escolheWalletHandler);

        /*
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


            if (flag.getValue().equals(Flag.GAMBLER_LOGGED_IN))
                menuGambler.run();
            /*
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
        MenuInput mnome = new MenuInput("Insira um username", "Username:");
        MenuInput memail = new MenuInput("Insira um email", "Email:");
        MenuInput mpassword = new MenuInput("Insira uma password", "Password:");
        MenuInput mcc = new MenuInput("Insira o seu cartão de cidadão", "Cartão de Cidadão:");
        MenuInput mnationality = new MenuInput("Insira a sua nacionalidade", "Nacionalidade:");
        MenuInput mnif = new MenuInput("Insira o seu NIF", "NIF:"); //todo limitar a inteiro
        MenuInput mocupation = new MenuInput("Insira a sua ocupação","Ocupação:");
        MenuInput mdatanascimento = new MenuInput("Insira a sua data de nascimento", "dd/MM/yyyy:"); //todo limitar a idade
        MenuInput mcodigopostal = new MenuInput("Insira o seu código postal", "Código postal:");
        MenuInput mcity = new MenuInput("Insira a sua cidade","Cidade:");
        MenuInput mendereco = new MenuInput("Insira o seu endereço","Endereço:");
        MenuInput mntelemovel = new MenuInput("Insira o seu número de télemovel:","Télemovel:"); //todo limitar a inteiro
        mnome.executa();
        memail.executa();
        mpassword.executa();
        mcc.executa();
        mnationality.executa();
        mnif.executa();
        mocupation.executa();
        mdatanascimento.executa();
        mcodigopostal.executa();
        mcity.executa();
        mendereco.executa();
        mntelemovel.executa();

        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate date = LocalDate.parse(mdatanascimento.getOpcao(), dateFormat);

        ResponseEntity resposta = userController.addGambler(new Gambler(0,mnome.getOpcao(),memail.getOpcao(),
                mpassword.getOpcao(), mcc.getOpcao(), Integer.parseInt(mnif.getOpcao()),date,Integer.parseInt(mntelemovel.getOpcao()),
                mnationality.getOpcao(), mcity.getOpcao(),mendereco.getOpcao(),mcodigopostal.getOpcao(),mocupation.getOpcao()));
        switch (resposta.getStatusCode()) {
            case OK -> System.out.println("Gambler criado com sucesso");
            case BAD_REQUEST -> System.out.println("Email repetido, a conta gambler não foi criada");
        }
    }

    private void registarAdminHandler() {
        MenuInput m1 = new MenuInput("Insira um username", "Username:");
        MenuInput m2 = new MenuInput("Insira um email", "Email:");
        MenuInput m3 = new MenuInput("Insira uma password", "Password:");
        m1.executa();
        m2.executa();
        m3.executa();

        ResponseEntity resposta = userController.addAdmin(new Admin(0,m1.getOpcao(),m3.getOpcao(),m2.getOpcao()));
        switch (resposta.getStatusCode()) {
            case OK -> System.out.println("Admininistrador criado com sucesso");
            case BAD_REQUEST -> System.out.println("Email repetido, a conta administrador não foi criada");
        }
    }

    private void registarExpertHandler() {
        MenuInput m1 = new MenuInput("Insira um username", "Username:");
        MenuInput m2 = new MenuInput("Insira um email", "Email:");
        MenuInput m3 = new MenuInput("Insira uma password", "Password:");
        m1.executa();
        m2.executa();
        m3.executa();

        ResponseEntity resposta = userController.addExpert(new Expert(0,m1.getOpcao(),m3.getOpcao(),m2.getOpcao()));
        switch (resposta.getStatusCode()) {
            case OK -> System.out.println("Expert criado com sucesso");
            case BAD_REQUEST -> System.out.println("Email repetido, a conta expert não foi criada");
        }
    }

    private void autenticarHandler() {
        //Atualizacao do número de pedido
        MenuInput m1 = new MenuInput("Insira o seu email", "Email:");
        MenuInput m2 = new MenuInput("Insira o seu password", "Password:");
        m1.executa();
        m2.executa();

        int flagInterna = userController.logIn(m1.getOpcao(),m2.getOpcao());
        if (flagInterna == -1)
            System.out.println("Falha no login");
        else if (flagInterna == 0) {
            System.out.println("Gambler logado com sucesso");
            flag.setValue(Flag.GAMBLER_LOGGED_IN);
        } else if (flagInterna == 1) {
            System.out.println("Administrador logado com sucesso");
            flag.setValue(Flag.ADMIN_LOGGED_IN);
        } else if (flagInterna == 2) {
            System.out.println("Expert logado com sucesso");
            flag.setValue(Flag.EXPERT_LOGGED_IN);
        }
        if(flagInterna!=-1){
            clienteID = userController.getUserByEmail(m1.getOpcao()).getId();
        }
    }

    // ****** Handlers Gambler ****** //


    private void consultarJogosMenuHandler(){
        List<Sport> desportos = sportController.getListOfSports().getBody();

        String[] sportNames = (String[]) desportos.stream().map(Sport::getName).toList().toArray();

        List<Game> games = new ArrayList<>();
        Menu menuSports = new Menu("Menu de desportos", sportNames);

        menuSports.setHandler(1, () -> consultarJogosHandler(gameController.getGames().getBody()));
        int i=2;
        for (String sport:sportNames){
            menuSports.setHandler(i, () -> consultarJogosHandler(games)); //todo meter o metodo para ir buscar jogos por desporto
            i++;
        }
    }

    private void consultarJogosHandler(List<Game> games){
        //usar flag para cada um dos utilizadores
        /*
        games = gameController.getGames();
        for (int i = 0; i < games.size(); i++) {
            Game g = games.get(i);
            //todo dar print aos jogos
        }
        Menu menuSports = new Menu("Menu de desportos", sportNames);

        menuSports.setHandler(1, () -> consultarJogosHandler(gameController.getGames()));
        int i=2;
        for (String sport:sportNames){
            menuSports.setHandler(i, () -> consultarJogosHandler(games)); //todo meter o metodo para ir buscar jogos por desporto
            i++;
        }
         */
    }

    private void alterarPerfilHandler(){
        AtomicInteger flagInterna = new AtomicInteger();
        flagInterna.set(0);

        while (flagInterna.get()==0){
            Gambler g = (Gambler) userController.getGambler(clienteID,null).getBody(); //todo faltam anguns getters
            System.out.println("Dados do utilizador");
            System.out.println("Email :"+ g.getEmail());
            System.out.println("CC :"+ g.getCc());
            System.out.println("NIF :"+ g.getNif());
            System.out.println("Data de nascimento :"+g.getDate_of_birth());

            List<String> dados = new ArrayList<>();
            dados.add("Nome :"+g.getName());
            dados.add("Palavra-chave :"+g.getPassword());
            dados.add("Cidade :" + g.getCity());
            dados.add("Nacionalidade :" + g.getNationality());
            dados.add("Endereço :" + g.getAddress());
            dados.add("Código postal :"+g.getPostal_code());

            Menu menuPerfil= new Menu("Dados alteráveis",dados.toArray(new String[0]));
            menuPerfil.setHandlerSaida(() -> flagInterna.set(1));
            for (int i=1;i< dados.size()+1;i++) {
                int finalI = i;
                menuPerfil.setHandler(i, () -> alterarPerfilHandlerAux(finalI,g));
            }
            menuPerfil.runOneTime();
        }
    }

    private void alterarPerfilHandlerAux(int indice,Gambler g){
        switch (indice) {
            case 1 -> {
                MenuInput m1 = new MenuInput("Insira o novo Nome", "Nome:");
                m1.executa();
                g.setName(m1.getOpcao());
            }
            case 2 -> {
                MenuInput m2 = new MenuInput("Insira a nova Palavra-chave", "Password:");
                m2.executa();
                g.setPassword(m2.getOpcao());
            }
            case 3 -> {
                MenuInput m3 = new MenuInput("Insira a nova Cidade", "Cidade:");
                m3.executa();
                g.setCity(m3.getOpcao());
            }
            case 4 -> {
                MenuInput m4 = new MenuInput("Insira a nova Nacionalidade", "Nacionalidade:");
                m4.executa();
                g.setNationality(m4.getOpcao());
            }
            case 5 -> {
                MenuInput m5 = new MenuInput("Insira o novo Endereço", "Endereço:");
                m5.executa();
                g.setAddress(m5.getOpcao());
            }
            case 6 -> {
                MenuInput m6 = new MenuInput("Insira o novo Código Postal", "Código Postal:");
                m6.executa();
                g.setPostal_code(m6.getOpcao());
            }
        }
        //userController.updateGambler(g); //todo metodo update
    }

    private void historicoTransacoesHandler(){
        List<Transaction> transactions = (List<Transaction>) transactionController.getUserTransactions(clienteID).getBody();
        //todo iterar transactions
    }

    private void historicoApostasHandler(){
        List<Bet> apostas = (List<Bet>) betController.getGamblerBets(clienteID).getBody();
        //todo iterar apostas
    }


    private void escolheWalletHandler(){
        AtomicInteger flagInterna = new AtomicInteger();
        flagInterna.set(0);

        while (flagInterna.get()==0) {
            List<Wallet> wallets = null;
            //wallets = walletcontroller.getUserWallets
            Menu chooseWalletMenu = new Menu("Escolha a sua carteira", wallets.stream().map(Wallet::getId).toList().toArray(new String[0])); //todo usar wallet to string
            chooseWalletMenu.setHandlerSaida(() -> flagInterna.set(1));
            chooseWalletMenu.setHandler(1, () -> criaWallet());
            int i=2;
            for (Wallet wallet:wallets) {
                chooseWalletMenu.setHandler(i, () -> walletMenuHandler(wallet.getId()));
                i++;
            }
            chooseWalletMenu.runOneTime();
        }
    }

    private void walletMenuHandler(int walletId){
        AtomicInteger flagInterna = new AtomicInteger();
        flagInterna.set(0);

        while (flagInterna.get()==0) {
            Wallet wallet = walletController.getWallet(walletId).getBody();
            Menu cangeWalletMenu = new Menu("Escolha a sua carteira", new String[]{"Levantar dinheiro","Depositar dinheiro"});
            cangeWalletMenu.setHandlerSaida(() -> flagInterna.set(1));
            cangeWalletMenu.setHandler(1, () -> levantarDinheiroHandler(wallet));
            cangeWalletMenu.setHandler(2, () -> depositaDinheiroHandler(wallet));

            cangeWalletMenu.runOneTime();
        }
    }

    private void criaWallet(){
        Wallet wallet = null;


        List<Coin> coins = walletController.getListOfCoins().getBody();

        Menu chooseWalletMenu= new Menu("Escolha o tipo de moeda da carteira", coins.stream().map(Coin::getName).toList().toArray(new String[0])); //todo usar um coin to string
        int i=1;
        for (Coin coin:coins) {
            chooseWalletMenu.setHandler(i, () -> criaWalletAux(coin.getId()));
            i++;
        }
        chooseWalletMenu.runOneTime();
    }
    private void criaWalletAux(int coinId){ //todo rever funcionamento das wallets
        Wallet wallet = null;
        //add wallet to database
    }

    private void levantarDinheiroHandler(Wallet wallet){

        //wallet print
        MenuInput m = new MenuInput("Insira a quantidade de dinheiro que prentende depositar", "Dinheiro:");
        m.executa();
        //while (m.getOpcao())
        //todo o alex diz que o dinheiro tem que ser positivo but i think im not doin it
        //todo withdraw

    }

    private void depositaDinheiroHandler(Wallet wallet){
        //todo poe dinheiro

    }

    // ****** Expert Handlers ****** //

}
