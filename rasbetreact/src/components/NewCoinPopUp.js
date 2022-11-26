import React from "react";
import "./CarteirasPopUp.css";
import { Carteira } from './Carteira';
import { Button } from './Button';

export const NewCoinPopUp = ({
    closePopup
}) => {
    return (
        <>
            <form action="/action_page.php" class="container-form">
                <h1>Criar Coin</h1>

                <input type="txtL" placeholder="Nome" name="nome" required />
                <input type="txtL" placeholder="Ratio-Eur" name="ratio" required />

                <Button type="submit" buttonSize='btn--flex' onClick={closePopup}>Concluir</Button>
                <button onClick={closePopup}>Cancelar</button >
            </form>


        </>
    );
};