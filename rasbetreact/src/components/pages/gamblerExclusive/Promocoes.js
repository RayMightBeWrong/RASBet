import React, { useEffect, useState } from 'react'
import '../Comons.css';
import './Promocoes.css';
import { Button } from '../../Button'
import { Link } from 'react-router-dom'
import { Promocao } from '../../objects/Promocao'

function Promocoes({
    userState
}) {
    const [promocoes, setPromocoes] = useState([])

    useEffect(() => {
        const requestOptions = {
            method: 'GET',
            headers: { "Content-Type": "application/json" }
        }
        fetch("http://localhost:8080/api/promotions/ordered?which_date=begin&order=DESC", requestOptions)
            .then(res => res.json())
            .then((result) => {
                setPromocoes(result)
            })
    }, [])

    function detectPromotionType(promotion) {
        switch (Object.keys(promotion).length) {
            case 8:
                return <Promocao tipo="BoostOdd"
                    title={promotion.title}
                    description={promotion.description}
                    begin_date={promotion.begin_date}
                    expiration_date={promotion.expiration_date}
                    nr_uses={promotion.nr_uses}
                    coupon={promotion.coupon}
                    boost_percentage={promotion.boost_percentage}
                />
            case 9:
                return <Promocao tipo="ReferralBoostOdd"
                    title={promotion.title}
                    description={promotion.description}
                    begin_date={promotion.begin_date}
                    expiration_date={promotion.expiration_date}
                    nr_uses={promotion.nr_uses}
                    coupon={promotion.coupon}
                    boost_percentage={promotion.boost_percentage}
                    number_of_referrals_needed={promotion.number_of_referrals_needed}
                />
            case 10:
                return <Promocao tipo="ReferralBalance"
                    title={promotion.title}
                    description={promotion.description}
                    begin_date={promotion.begin_date}
                    expiration_date={promotion.expiration_date}
                    nr_uses={promotion.nr_uses}
                    coupon={promotion.coupon}
                    number_of_referrals_needed={promotion.number_of_referrals_needed}
                    value_to_give={promotion.value_to_give}
                    coin_id={promotion.coin.id}
                />
            default:
                return ""
        }
    }


    function printPromo() {
        if (promocoes.length % 2 == 0) {
            return(
                <div className='gridPromocoes'>
                    {promocoes.map((prom) => (
                        <div key={prom.id}>
                            {detectPromotionType(prom)}
                        </div>
                    ))}
                </div>
            )
        }
        else{
            var promo = promocoes.slice(0, -1)
            var last = promocoes[promocoes.length-1]

            return(
                <>
                    <div className='gridPromocoes'>
                        {promo.map((prom) => (
                            <div key={prom.id}>
                                {detectPromotionType(prom)}
                            </div>
                        ))}
                    </div>
                    {detectPromotionType(last)}
                </>
            )
        }
        
    }

    if (userState === "gambler") {
        return (
            <>
                <div className='promocoes'>
                    <div className='promocoes-box'>
                        {printPromo()}
                    </div>
                </div>
            </>
        );
    } else {
        return "";
    }
}

export default Promocoes;
