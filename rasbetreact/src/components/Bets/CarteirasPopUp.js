import React from "react";
import "./CarteirasPopUp.css";
import { Carteira } from './Carteira';
import { Button } from '../Button';

export const CarteirasPopUp = ({
    carteiras,
    valormin,
    closePopup
}) => {

    const verificaCarteira = (valor) => {
        console.log("yoo")
        if (parseFloat(valormin) <= parseFloat(valor) || valormin === '') {
            alert('Aposta inserida'); {/*todo verificar se aposta é possível no backend*/ }
            closePopup()
        }
        else { alert('Dinheiro insuficiente'); }
    }
    return (
        <div className="carteirasPopUp-container" >
            < div className="carteirasPopUp-body" >
                <h1> Seleção de carteiras </h1>
                {carteiras.map(wallet => (
                    <div><Carteira ratioEuro={wallet.ratioEuro} balance={wallet.balance} verificaCarteira={verificaCarteira} /></div>
                ))}
                <Button buttonStyle={"btn--bet"}> {/* todo on click diminui */}
                    Criar carteira
                </Button>
                <button onClick={closePopup}>Fechar menu das carteiras</button >
            </div>
        </div >
    );
};