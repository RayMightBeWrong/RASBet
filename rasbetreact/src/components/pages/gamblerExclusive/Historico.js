import React, { useEffect, useState } from 'react';
import './Historico.css';

const listHist = ["Transações", "Apostas"];

function getTrans(sorce) {
    let datas = [], horas = [], desc = [], val = [], fin = [], dh;
    sorce.map(e => {
        dh = e.date.split('.')[0].split('T')
        datas.push(dh[0])
        horas.push(dh[1])
        desc.push(e.description);
        val.push(e.value + ' (' + e.coin.id + ')')
        fin.push(e.balance_after_mov + ' (' + e.coin.id + ')');
    });

    let trans = [
        { "head": "Data", "body": datas },
        { "head": "Hora", "body": horas },
        { "head": "Descrição", "body": desc },
        { "head": "Valor", "body": val },
        { "head": "Saldo Final", "body": fin }
    ];
    return trans;
};

function getDesc(game_choices) {
    return(<h1>123</h1>)
}

function getDespostos(game_choices) {
    let list = []
    return(list)
}

function getBets(sorce) {
    let datas = [], horas = [], desporto = [], desc = [], result = [], odd = [], ini = [], fin = [], oddAux, dh, stateAux;
    sorce.map(e => {
        stateAux = e.state
        if (stateAux === 3 || stateAux === 2) {
            dh = e.transaction.date.split('.')[0].split('T')
            datas.push(dh[0])
            horas.push(dh[1])
            // desporto.push(getDespostos(e.game_choices))
            oddAux = 1
            e.game_choices.map(gc => oddAux *= gc.odd)
            odd.push(oddAux)
            ini.push(Math.abs(e.transaction.value))
            fin.push(stateAux === 3 ? e.transaction.value : e.transaction.value * oddAux)
            // desc.push(getDesc(e.game_choices))
        }
    });

    let trans = [
        { "head": "Data", "body": datas },
        { "head": "Hora", "body": horas },
        // { "head": "Desportos", "body": desporto },
        { "head": "Valor apostado", "body": ini },
        { "head": "Odd", "body": odd },
        { "head": "Saldo final", "body": fin },
        // { "head": "Descrição", "body": desc }
    ];
    return trans;
};

function getTable(table) {
    return (
        <div className='hist-body-columns'>
            {table.map((column) => (
                <div className='hist-body-item'>
                    <h3>{column.head}</h3>
                    {column.body.map((element) => (
                        <h4>{element}</h4>
                    ))}
                </div>
            ))}
        </div>
    );
};



function Historico({
    userId,
    userState
}) {
    const [contentList, setContentList] = useState([]);
    const [cGames, setCGames] = useState([]);
    const [content, setContent] = useState(listHist[1]);

    useEffect(() => {
        if (content === "Transações") {
            let s = "http://localhost:8080/api/transactions/gambler/" + userId + "/DESC"
            fetch(s)
            .then(res => res.json())
            .then((result) => {
                setContentList(result);
                console.log(result);
            })
        } else {
            let s = "http://localhost:8080/api/bets/gambler/" + userId + "/DESC"
            fetch(s)
            .then(res => res.json())
            .then((bets) => {
            //     bets.map(bet => {
            //         bet["listGames"]=[]
            //         bet.game_choices.map(gchoice => { console.log(gchoice.game.id);
            //             s = "http://localhost:8080/api/games/" + gchoice.game.id;
            //             fetch(s)
            //             .then(res => res.json())
            //             .then((result) => {
            //                 bet["listgGames"].push(result)
            //             })
            //         })
            //     })
                console.log(bets);
                setContentList(bets);
            })
            
        }
    }, [content])

    if (userState === "gambler") {
        return (
            <div className='historico'>
                <div className='historico-box'>
                    <h1>Histórico de {content}</h1>
                    <div className='contentMod'>
                        <h3>Modificar historico:</h3>
                        <select value={content} onChange={(opt) => setContent(opt.target.value)} required>
                            {listHist.map((opt) => (
                                <option key={opt} value={opt}>
                                    {opt}
                                </option>
                            ))}
                        </select>
                    </div>
                    <div className='transaction'>
                        <div className='transaction-box'>
                            {content === "Transações" ?
                                getTable(getTrans(contentList))
                                :
                                getTable(getBets(contentList))
                            }
                        </div>
                    </div>
                </div>
            </div>
        );
    } else return null;
}

export default Historico;