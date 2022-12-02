import React, { useState } from "react";
import '../Comons.css';
import { Button } from '../../Button';
import { Link } from 'react-router-dom';

function CriarPromocao({
    userState
}) {

    const tiposDePromocoes = ["BoostOdd", "ReferralBoostOdd", "ReferralBalance"]
    const [promType, setPromType] = useState("")
    const [promocaoBase, setPromocaoBase] = useState({
        title: "",
        description: "",
        begin_date: "",
        expiration_date: "",
        nr_uses: "",
        coupon: ""
    })
    const [boost_percentage, setBoost_percentage] = useState("0")
    const [number_of_referrals_needed, setNumber_of_referrals_needed] = useState("")
    const [value_to_give, setValue_to_give] = useState("")
    const [coin_id, setCoin_id] = useState("")

    function escolherPromocao() {
        return (
            <select onClick={e => setPromType(e.target.value)}>
                {tiposDePromocoes.map((opt) => (
                    <option key={opt} value={opt}>
                        {opt}
                    </option>
                ))}
            </select>
        )
    }

    const handleSubmit = event => {
        event.preventDefault()

        let promSpecific
        switch (promType) {
            case "BoostOdd":
                promSpecific = {
                    boost_percentage: 50
                }
                break;
            case "ReferralBoostOdd":
                promSpecific = {
                    boost_percentage: 50,
                    number_of_referrals_needed: 1
                }
                break;
            case "ReferralBalance":
                promSpecific = {
                    number_of_referrals_needed: 1,
                    value_to_give: 10,
                    coin_id: "EUR"
                }
                break;
            default:
                return
        }

        const finalPromocao = {
            ...JSON.parse(promocaoBase),
            ...JSON.parse(promSpecific)
        }

        console.log(finalPromocao)
        const requestOptions = {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(finalPromocao)
        }

        fetch("http://localhost:8080/api/promotions/" + promType, requestOptions)
            .then(res => {
                if (res.status !== 200) {
                    let errorMsg;
                    if ((errorMsg = res.headers.get("x-error")) == null)
                        errorMsg = "Error occured"
                    alert(errorMsg)
                }
                else {
                    alert("Promotion created successfully")
                }
            })
            .catch(_ => alert("Error occured"))
    }


    if (userState === "admin") {
        return (
            <>
                <div className='greenBackGround'>
                    <div className='white-box'>
                        <div className='container'>
                            <form className="container-form" onSubmit={handleSubmit}>
                                <h1>Promotion Creation</h1>
                                {escolherPromocao()}

                                Title <input type="txtL" placeholder="christmas promotion" value={promocaoBase.title}
                                    onChange={(e) => setPromocaoBase(oldVal => ({
                                        ...oldVal,
                                        title: e.target.value,
                                    }))} required />

                                Description <input type="txtL"
                                    placeholder="santas gifts"
                                    value={promocaoBase.description}
                                    onChange={(e) => setPromocaoBase(oldVal => ({
                                        ...oldVal,
                                        description: e.target.value,
                                    }))} required />

                                Begin Date <input type="txtL"
                                    placeholder="2022-12-24T14:11:43.337"
                                    value={promocaoBase.begin_date}
                                    onChange={(e) => setPromocaoBase(oldVal => ({
                                        ...oldVal,
                                        begin_date: e.target.value,
                                    }))} required />

                                Expiration Date <input type="txtL"
                                    placeholder="2022-12-24T14:11:43.337"
                                    value={promocaoBase.expiration_date}
                                    onChange={(e) => setPromocaoBase(oldVal => ({
                                        ...oldVal,
                                        expiration_date: e.target.value,
                                    }))} required />

                                Number of uses <input type="txtL"
                                    placeholder="1"
                                    value={promocaoBase.nr_uses}
                                    onChange={(e) => setPromocaoBase(oldVal => ({
                                        ...oldVal,
                                        nr_uses: e.target.value,
                                    }))} required />

                                Coupon <input type="txtL"
                                    placeholder="CHRISTMAS"
                                    value={promocaoBase.coupon}
                                    onChange={(e) => setPromocaoBase(oldVal => ({
                                        ...oldVal,
                                        coupon: e.target.value,
                                    }))} required />

                                Boost percentage <input type="txtL"
                                    placeholder="CHRISTMAS"
                                    value={promocaoBase.coupon}
                                    onChange={(e) => setBoost_percentage(e.target.value)} required />
                                const [boost_percentage, setBoost_percentage] = useState("0")
                                const [number_of_referrals_needed, setNumber_of_referrals_needed] = useState("")
                                const [value_to_give, setValue_to_give] = useState("")
                                const [coin_id, setCoin_id] = useState("")
                                <Button buttonSize='btn--flex' type="submit">Concluir</Button>
                            </form>
                        </div>
                    </div>
                </div>
            </>
        );
    } else {
        return ("");
    }
}

export default CriarPromocao;
