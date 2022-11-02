package ras.adlrr.RASBet.UI;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;
import ras.adlrr.RASBet.UI.ModelViews.*;
import ras.adlrr.RASBet.api.*;
import ras.adlrr.RASBet.model.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
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
    Bet betAtual = null;
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
        Menu menuGambler = new Menu("Gambler", new String[]{"Consultar  Jogos", "Alterar perfil", "Historico de Transações", "Historico de Apostas", "Consultar carteiras"});
        menuGambler.setHandlerSaida(() -> flag.setValue(Flag.NOT_AUTHENTICATED));
        menuGambler.setHandler(1, this::consultarJogosMenuHandler);
        menuGambler.setHandler(2, this::alterarPerfilHandler);
        menuGambler.setHandler(3, this::historicoTransacoesHandler);
        menuGambler.setHandler(4, this::historicoApostasHandler);
        menuGambler.setHandler(5,this::escolheWalletHandler);



        //Menu de administrador
        Menu menuAdmin = new Menu("Administrador", new String[]{"Update jogos API's"});
        menuAdmin.setHandlerSaida(() -> flag.setValue(Flag.NOT_AUTHENTICATED));
        menuAdmin.setHandler(1, () -> gameController.updateGames());

        Menu menuExpert = new Menu("Especialista", new String[]{"Consultar Jogos"});
        menuExpert.setHandlerSaida(() -> flag.setValue(Flag.NOT_AUTHENTICATED));
        menuExpert.setHandler(1, this::consultarJogosMenuHandler);

        while (!flag.getValue().equals(Flag.CLOSE_CLIENT)) {
            //Executa menu de autenticacao
            while (flag.getValue().equals(Flag.NOT_AUTHENTICATED))
                menuAutenticao.runOneTime();


            if (flag.getValue().equals(Flag.GAMBLER_LOGGED_IN))
                menuGambler.run();

            else if (flag.getValue().equals(Flag.ADMIN_LOGGED_IN))
                menuAdmin.run();

            else if (flag.getValue().equals(Flag.EXPERT_LOGGED_IN))
                menuExpert.run();

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
        AtomicInteger flagInterna = new AtomicInteger();
        flagInterna.set(0);

        while (flagInterna.get()==0) {
            List<Sport> desportos = sportController.getListOfSports().getBody();

            List<String> sportsList = new ArrayList<>(desportos.stream().map(Sport::getName).toList());
            String[] sportNames = sportsList.toArray(new String[0]);
            sportsList.add(0, "Todos os desportos");
            String[] menuTitle = sportsList.toArray(new String[0]);

            List<Game> games = new ArrayList<>();
            Menu menuSports = new Menu("Menu de desportos", menuTitle);
            menuSports.setHandlerSaida(() -> flagInterna.set(1));
            menuSports.setHandler(1, () -> consultarJogosHandler(gameController.getGames().getBody()));
            int i = 2;
            for (String sport : sportNames) {
                menuSports.setHandler(i, () -> consultarJogosHandler(sportController.getGamesFromSport(sport).getBody()));
                i++;
            }
            menuSports.runOneTime();
        }
    }

    private void consultarJogosHandler(List<Game> games){

        //usar flag para cada um dos utilizadores
        if(games.size()!=0) {
            AtomicInteger flagInterna = new AtomicInteger();
            flagInterna.set(0);

            while (flagInterna.get()==0) {
                List<GameView> gameviews = games.stream().map(GameView::new).toList();

                Menu menuChooseGame = new Menu("Menu de jogos", gameviews.stream().map(GameView::toString).toList().toArray(new String[0]));
                menuChooseGame.setHandlerSaida(() -> flagInterna.set(1));
                int i = 1;
                for (Game game : games) {
                    menuChooseGame.setHandler(i, () -> gameMenuHandler(game));
                    i++;
                }
                menuChooseGame.runOneTime();
            }
        } else System.out.println("Não há jogos disponíveis");
    }

    private void gameMenuHandler(Game game){

        AtomicInteger flagInterna = new AtomicInteger();
        flagInterna.set(0);

        GameView gameView = new GameView(game);
        while (flagInterna.get()==0) {
            Set<Participant> participants = gameController.getGameParticipants(game.getId()).getBody();
            System.out.println(gameView.toStringExtended(participants));

            if(flag.getValue()==Flag.GAMBLER_LOGGED_IN){
                Menu gameMenu = new Menu("Menu do jogo", new String[]{"Apostar"});
                gameMenu.setHandler(1, () -> simpleBetHandler(game,participants));
                gameMenu.setHandlerSaida(() -> flagInterna.set(1));
                gameMenu.runOneTime();
            } else if(flag.getValue()==Flag.EXPERT_LOGGED_IN){
                Menu gameMenu = new Menu("Menu do jogo", new String[]{"Alterar odd"});
                gameMenu.setHandler(1, () -> changeOddHandler(game,participants));
                gameMenu.setHandlerSaida(() -> flagInterna.set(1));
                gameMenu.runOneTime();
            }
        }
    }

    private void simpleBetHandler(Game game,Set<Participant> participants){

        Menu gameMenu = new Menu("Menu do jogo", participants.stream().map(ParticipantView::new).map(ParticipantView::toString).toList().toArray(new String[0]));
        int i = 1;
        for (Participant participant:participants){
            gameMenu.setHandler(i, () -> betAux(participant,game.getId()));
            i++;
        }
        gameMenu.runOneTime();

    }

    private void betAux(Participant participant,int gameId){
        if(betAtual==null) {
            Wallet wallet = escolheWalletAux();
            if (wallet != null) {
                boolean valido = false;
                float aposta = 0;
                while (!valido) {
                    MenuInput m = new MenuInput("Insira o valor da sua aposta", "Valor:");
                    m.executa();
                    aposta = Float.parseFloat(m.getOpcao());
                    if (wallet.getBalance() >= aposta && aposta >= 0)
                        valido = true;
                }

                GameChoice gc = new GameChoice(gameId, participant.getId(), participant.getOdd());
                List<GameChoice> gameChoices = new ArrayList<>();
                gameChoices.add(gc);
                betAtual = new Bet(clienteID, wallet.getId(), aposta, gameChoices);
                betMultiplaMenu();
            }
        } else {
            GameChoice gc = new GameChoice(gameId, participant.getId(), participant.getOdd());
            betAtual.addGameChoice(gc);
            betMultiplaMenu();
        }
    }

    private void betMultiplaMenu(){
        Menu apostaMultiplaMenu = new Menu("Opções da aposta",new String[]{"Continuar com aposta multipla","Acabar aposta","Cancelar aposta"});
        apostaMultiplaMenu.setHandler(1, () -> {});
        apostaMultiplaMenu.setHandler(2, () -> {
            betController.addBet(betAtual);
            betAtual = null;
        });
        apostaMultiplaMenu.setHandler(3, () -> {betAtual = null;});
        apostaMultiplaMenu.runOneTime();
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
            System.out.println("Número de telemovel :" + g.getPhoneNumber());

            List<String> dados = new ArrayList<>();
            dados.add("Nome :"+g.getName());
            dados.add("Palavra-chave :"+g.getPassword());
            dados.add("Cidade :" + g.getCity());
            dados.add("Nacionalidade :" + g.getNationality());
            dados.add("Endereço :" + g.getAddress());
            dados.add("Código postal :"+g.getPostal_code());
            dados.add("Nacionality :" + g.getNationality());
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
            case 7 -> {
                MenuInput m7 = new MenuInput("Insira o nova Nacionalidade", "Nacionalidade:");
                m7.executa();
                g.setNationality(m7.getOpcao());
            }
        }
        userController.updateGambler(g.getId(), g.getName(), g.getEmail(), g.getPassword(), g.getPhoneNumber(), g.getNationality(), g.getCity(), g.getAddress(), g.getPostal_code(), g.getOccupation());
    }

    private void historicoTransacoesHandler(){
        List<Transaction> transactions = transactionController.getUserTransactions(clienteID).getBody();
        for (TransactionView transaction:transactions.stream().map(TransactionView::new).toList()){
            System.out.println(transaction.toString());
        }
    }

    private void historicoApostasHandler(){
        List<Bet> apostas = betController.getGamblerBets(clienteID).getBody();
        for (BetView betView:apostas.stream().map(BetView::new).toList())
            System.out.println(betView.toString());
    }


    private void escolheWalletHandler(){
        AtomicInteger flagInterna = new AtomicInteger();
        flagInterna.set(0);

        while (flagInterna.get()==0) {
            List<Wallet> wallets = walletController.getGamblerWallets(clienteID).getBody();
            List<String> walletViews = new ArrayList<>(wallets.stream().map(WalletView::new).map(WalletView::toString).toList());
            walletViews.add(0,"Criar carteira");

            Menu chooseWalletMenu = new Menu("Escolha a sua carteira", walletViews.toArray(new String[0]));
            chooseWalletMenu.setHandlerSaida(() -> flagInterna.set(1));
            chooseWalletMenu.setHandler(1, this::criaWallet);
            int i=2;
            for (Wallet wallet:wallets) {
                chooseWalletMenu.setHandler(i, () -> walletMenuHandler(wallet.getId()));
                i++;
            }
            chooseWalletMenu.runOneTime();
        }
    }

    private Wallet escolheWalletAux(){
        System.out.println("YOO");
        AtomicInteger flagInterna = new AtomicInteger();
        flagInterna.set(0);
        final Wallet[] walletRet = {null};
        List<Wallet> wallets = walletController.getGamblerWallets(clienteID).getBody();
        Menu chooseWalletMenu = new Menu("Escolha a sua carteira", wallets.stream().map(WalletView::new).map(WalletView::toString).toList().toArray(new String[0]));
        for (int i=1;i<wallets.size()+1;i++) {
            int finalI = i;
            chooseWalletMenu.setHandler(i, () -> {
                walletRet[0] = wallets.get(finalI -1);});
            i++;
        }
        chooseWalletMenu.runOneTime();
        return walletRet[0];
    }

    private void walletMenuHandler(int walletId){
        AtomicInteger flagInterna = new AtomicInteger();
        flagInterna.set(0);

        while (flagInterna.get()==0) {
            Wallet wallet = walletController.getWallet(walletId).getBody();
            Menu cangeWalletMenu = new Menu("Indique o que pretende fazer com a carteira", new String[]{"Levantar dinheiro","Depositar dinheiro"});
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
    private void criaWalletAux(int coinId){
        Wallet wallet = new Wallet(coinId,clienteID);
        walletController.createWallet(wallet);
    }

    private void levantarDinheiroHandler(Wallet wallet){
        WalletView walletView = new WalletView(wallet);
        System.out.println(walletView);
        boolean valido = false;
        float levantamento = 0;
        while (!valido) {
            MenuInput m = new MenuInput("Insira o valor do levantamento", "Valor:");
            m.executa();
            levantamento = Float.parseFloat(m.getOpcao());
            if (wallet.getBalance() >= levantamento && levantamento >= 0)
                valido=true;
        }
        transactionController.withdraw(wallet.getId(),levantamento);

    }

    private void depositaDinheiroHandler(Wallet wallet){
        boolean valido = false;
        float inserscao = 0;
        while (!valido) {
            MenuInput m = new MenuInput("Insira o valor que pretende inserir", "Valor:");
            m.executa();
            inserscao = Float.parseFloat(m.getOpcao());
            if (inserscao >= 0)
                valido=true;
        }
        transactionController.deposit(wallet.getId(),inserscao);

    }

    // ****** Expert Handlers ****** //
    private void changeOddHandler(Game game,Set<Participant> participants){

        Menu gameMenu = new Menu("Selecione o participante na qual pretende alterar a odd", participants.stream().map(ParticipantView::new)
                .map(ParticipantView::toString).toList().toArray(new String[0]));
        int i = 1;
        for (Participant participant:participants){
            gameMenu.setHandler(i, () -> changeOddAux(participant,game.getId()));
            i++;
        }
        gameMenu.runOneTime();

    }

    private void changeOddAux(Participant participant,int gameId){
        boolean valido = false;
        float novaOdd = 0;
        while (!valido) {
            MenuInput m = new MenuInput("Insira o valor da nova odd", "Valor:");
            m.executa();
            novaOdd = Float.parseFloat(m.getOpcao());
            if (novaOdd > 0)
                valido = true;
        }
        gameController.editOddInParticipant(participant.getId(), novaOdd);
    }

}
