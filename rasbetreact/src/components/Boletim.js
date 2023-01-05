import React, { useState, useEffect } from 'react';
import './Boletim.css';
import { Button } from './Button';
import { BoletimBet } from './BoletimBet';
import { CarteirasPopUp } from './CarteirasPopUp';

const carteira1 = { ratioEuro: "0.35", balance: "30" }
const carteira2 = { ratioEuro: "1", balance: "700" }
const carteiras = [carteira1, carteira2]

export const Boletim = ({
    bets,
    userId
}) => {
    const [value, setValue] = useState('0.00');
    const [tipoAposta, setTipoAposta] = useState("Simples");
    const [finalWin, setFinalWin] = useState(0);
    const [open, setOpen] = useState(false);
    const [cupao, setCupao] = useState('');

    const changeCupao = event => {
        setCupao(event.target.value)
    };

    const handleChange = event => {
        const result = ("000" + event.target.value.replace(/\D/g, '')).match(/(000|00[123456789]|0[123456789]\d|[123456789]\d*)$/g).toString();
        setValue(result.slice(0, -2) + '.' + result.slice(-2));
    };

    useEffect(() => {
        let soma = 0
        if (value !== '' && bets.length !== 0)
            soma = parseFloat(value)
        {
            bets.map(game => (
                soma = soma * parseFloat(game.cota)
            ))
        }
        setFinalWin(soma)
        if (bets.length <= 1) {
            setTipoAposta("Simples")
        } else setTipoAposta("Múltipla")
    }, [value, bets]);

    const handleClick = () => {
        console.log("setopentrue")
        setOpen(true)
    }

    return (
        <>
            <div className='boletim'>
                <div className='boletim-header'>
                    <h3>{tipoAposta}</h3>
                </div>
                <div className='boletim-body'>
                    {bets.map(game => (
                        <BoletimBet key={game.id} title={game.title} winner={game.winner} cota={game.cota} />
                    ))}
                    <br/>
                    <div className='textLines'>
                        <h3>Possivel ganho:</h3>
                        <h3>{finalWin.toFixed(3)}</h3>
                    </div>
                    <div className='boletim-ganhos'>
                        <h3>Aposta:</h3>
                        <input type="txtBoletim"
                            placeholder="Valor da aposta"
                            value={value}
                            onChange={handleChange} />
                    </div>
                    <div className='boletim-ganhos'>
                        <h3>Cupão:</h3>
                        <input type="txtBoletim"
                            placeholder="Código do cupão"
                            value={cupao}
                            onChange={changeCupao} />
                    </div>
                    <br/>
                    <Button buttonStyle={"btn--inverted"}
                        buttonSize={'btn--flex'}
                        onClick={() => handleClick()}>
                        Submeter
                    </Button>
                    {open ?
                        < CarteirasPopUp userId={userId} cupao={cupao} bets={bets} carteiras={carteiras} closePopup={() => setOpen(false)} valormin={value} />
                        : null
                    }
                </div>
            </div>
        </>
    );
};
