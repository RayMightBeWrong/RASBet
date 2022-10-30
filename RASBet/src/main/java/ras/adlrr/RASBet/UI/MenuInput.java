package ras.adlrr.RASBet.UI;

import java.util.Scanner;

public class MenuInput {
    private final String title;
    private final String text;
    private String op;


    public MenuInput(String title,String text){
        this.title = title;
        this.text = text;
        this.op = "";
    }

    public void executa() {
        showMenu();
        this.op = lerOpcao();
    }


    private void showMenu() {
        if (title != null) System.out.println(title);
        if (text != null)  System.out.print(text);
    }

    private String lerOpcao() {
        String op;
        Scanner is = new Scanner(System.in);

        op = is.nextLine();

        return op;
    }

    public String getOpcao() {
        return this.op;
    }
}