import React, {useState} from 'react';
import './Historico.css';

const listHist = ["Transações","Apostas"];
const id = 1;
const contentList2 = [
    {"balance_after_mov": 25.0,"description": "Deposit","value": 25.0,"date": "2022-11-27T18:48:21.584163","id": 1,"gambler": {"id": 1},"wallet": {"id": 1},"coin": {"id": "Bitcoin","ratio_EUR": 20500.0}},
    {"balance_after_mov": 0.0,"description": "Withdraw","value": 25.0,"date": "2022-11-27T18:48:30.430386","id": 2,"gambler": {"id": 1},"wallet": {"id": 1},"coin": {"id": "Bitcoin","ratio_EUR": 20500.0}},
    {"balance_after_mov": 25.0,"description": "Deposit","value": 25.0,"date": "2022-11-27T18:48:43.106686","id": 3,"gambler": {"id": 1},"wallet": {"id": 1},"coin": {"id": "Bitcoin","ratio_EUR": 20500.0}},
    {"balance_after_mov": 50.0,"description": "Deposit","value": 25.0,"date": "2022-11-27T18:48:43.781689","id": 4,"gambler": {"id": 1},"wallet": {"id": 1},"coin": {"id": "Bitcoin","ratio_EUR": 20500.0}}
];

function getDate(date){
    const str = date.split('.')[0].split('T');
    return str;
};

function getTrans(sorce){
    let datas = [], horas = [], desc = [], val = [], fin = [], dh;
    sorce.map(e=>{
        dh=e.date.split('.')[0].split('T')
        datas.push(dh[0])
        horas.push(dh[1])
        desc.push(e.description);
        val.push(e.value + ' (' + e.coin.id + ')')
        fin.push(e.balance_after_mov + ' (' + e.coin.id + ')');
    });
    sorce.map(e=>e.date.split('.')[0].split('T')[1]);

    let trans = [
        {"head":"Data", "body": datas},
        {"head":"Hora", "body": horas},
        {"head":"Descrição", "body": desc},
        {"head":"Valor", "body": val},
        {"head":"Saldo Final", "body": fin}
    ];
    return trans;
};

function getBets(sorce){
    let datas = [], horas = [], desc = [], val = [], fin = [], dh;
    sorce.map(e=>{
        dh=e.date.split('.')[0].split('T')
        datas.push(dh[0])
        horas.push(dh[1])
        desc.push(e.description);
        val.push(e.value + ' (' + e.coin.id + ')')
        fin.push(e.balance_after_mov + ' (' + e.coin.id + ')');
    });
    sorce.map(e=>e.date.split('.')[0].split('T')[1]);

    let trans = [
        {"head":"Data", "body": horas},
        {"head":"Desporto", "body": horas},
        {"head":"Descrição", "body": desc},
        {"head":"Resultado", "body": fin},
        {"head":"Odd", "body": val},
        {"head":"Valor inicial", "body": datas},
        {"head":"Valor final", "body": fin}
    ];
    return trans;
};

function getTable(table){
    return(
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
    idGambler
}) {
    const [contentList,setContentList] = useState([]);
    const [content,setContent] = useState(listHist[1]);

    const changeHist = (opt) => {
        setContent(opt)
        if (opt === "Transações"){
            let s = "http://localhost:8080/api/transactions/gambler/" + id
            fetch(s)
            .then(res => res.json())
            .then((result) => {
                setContentList(result);
                console.log(result);
            })
        } else {
            let s = "http://localhost:8080/api/bets/gambler/" + id
            fetch(s)
            .then(res => res.json())
            .then((result) => {
                //setContentList(result);
                console.log(result);
            })
        }
    };


    return (
        <div className='historico'>
            <div className='historico-box'>
                <h1>Histórico de {content}</h1>
                <div className='contentMod'>
                    <h3>Modificar historico:</h3>
                    <select value={content} onChange={(opt) => changeHist(opt.target.value)} required>
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
}

export default Historico;