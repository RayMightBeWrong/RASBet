import React, { useState, useEffect } from 'react';
import './Promocao.css';
import { Button } from '../Button';


const getDate = (date) => {
    let dateS = date.split('T')
    return(
        <div className='promo-date-hour'>
            <h4>{dateS[0]}</h4>
            <h4>{dateS[1].split('.')[0]}</h4>
        </div>
    )
}


export const Promocao = ({
    tipo,
    title,
    description,
    begin_date,
    expiration_date,
    nr_uses,
    coupon,
    boost_percentage,
    number_of_referrals_needed,
    value_to_give,
    coin_id
}) => {

    function promType() {
        switch (tipo) {
            case "BoostOdd":
                return <PromocaoBoostOdd title={title}
                    description={description}
                    begin_date={begin_date}
                    expiration_date={expiration_date}
                    nr_uses={nr_uses}
                    coupon={coupon}
                    boost_percentage={boost_percentage}
                />

            case "ReferralBoostOdd":
                return <PromocaoReferralBoostOdd title={title}
                    description={description}
                    begin_date={begin_date}
                    expiration_date={expiration_date}
                    nr_uses={nr_uses}
                    coupon={coupon}
                    boost_percentage={boost_percentage}
                    number_of_referrals_needed={number_of_referrals_needed}
                />

            case "ReferralBalance":
                return <PromocaoReferralBalance title={title}
                    description={description}
                    begin_date={begin_date}
                    expiration_date={expiration_date}
                    nr_uses={nr_uses}
                    coupon={coupon}
                    number_of_referrals_needed={number_of_referrals_needed}
                    value_to_give={value_to_give}
                    coin_id={coin_id}
                />

            default:
                return "Erro no tipo da função"
        }
    }
    return (
        <>
            {promType()}
        </>
    )
}


const PromocaoBoostOdd = ({
    title,
    description,
    begin_date,
    expiration_date,
    nr_uses,
    coupon,
    boost_percentage
}) => {
    console.log(begin_date)
    return (
        <>
            <div className='promocao'>
                <div className='info'>
                    <h3> Boost Odd </h3>
                    <h4> Título: {title} </h4>
                    <h4>Inicio: {getDate(begin_date)}</h4>
                    <h4>Fim: {getDate(expiration_date)}</h4>
                    <h4>Cupão: {coupon}</h4>
                    <h4>Número de utilizações: {nr_uses}</h4>
                    <h4>Percentagem de boost: {boost_percentage}</h4>
                    <h4>Descrição: {description}</h4>
                </div>

            </div>
        </>
    )
}


const PromocaoReferralBoostOdd = ({
    title,
    description,
    begin_date,
    expiration_date,
    nr_uses,
    coupon,
    boost_percentage,
    number_of_referrals_needed
}) => {
    return (
        <>
            <div className='promocao'>

                <div className='info'>
                    <h3> Referral Boost Odd </h3>
                    <h4> Título: {title} </h4>
                    <h4>Inicio: {getDate(begin_date)}</h4>
                    <h4>Fim: {getDate(expiration_date)}</h4>
                    <h4>Cupão: {coupon}</h4>
                    <h4>Número de utilizações: {nr_uses}</h4>
                    <h4>Percentagem de boost: {boost_percentage}</h4>
                    <h4>Numero de referrals necessários: {number_of_referrals_needed}</h4>
                    <h4>Descrição: {description}</h4>
                </div>


            </div>
        </>
    )
}


const PromocaoReferralBalance = ({
    title,
    description,
    begin_date,
    expiration_date,
    nr_uses,
    coupon,
    number_of_referrals_needed,
    value_to_give,
    coin_id
}) => {
    return (
        <>
            <div className='promocao'>

                <div className='info'>
                    <h3> Referral Balance </h3>
                    <h4> Título: {title} </h4>
                    <h4>Inicio: {getDate(begin_date)}</h4>
                    <h4>Fim: {getDate(expiration_date)}</h4>
                    <h4>Cupão: {coupon}</h4>
                    <h4>Número de utilizações: {nr_uses}</h4>
                    <h4>Numero de referrals necessários: {number_of_referrals_needed}</h4>
                    <h4>Valor: {value_to_give}</h4>
                    <h4>Moeda: {coin_id}</h4>
                    <h4>Descrição: {description}</h4>

                </div>

            </div>
        </>
    )
}