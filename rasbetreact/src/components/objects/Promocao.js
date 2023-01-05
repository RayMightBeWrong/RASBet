import React, { useState, useEffect } from 'react';
import './Promocao.css';
import { Button } from '../Button';


const getDate = (date) => {
    let dateS = date.split('T')
    return(
        dateS[0]+" "+dateS[1].split('.')[0]
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
        <div className='promocao'>
            <div className='promo-title-uses'>
                <div className='promo-title'><h3> Referral Boost Odd </h3></div>
                <div className='promo-uses'><div className='promo-uses-text'>x{nr_uses}</div></div>
            </div>
            <h4>Código promocional: {coupon}</h4>
            <h4>Descrição: {description}</h4>
            <h4>Boost: {boost_percentage}%</h4>
            <div className='promo-line'>
                <div className='promo-validade'>
                    <div className='promo-validade-text'>
                        <h5>begin:</h5>
                        <h5>{getDate(begin_date)}</h5>
                    </div>
                    <div className='promo-validade-text'>
                        <h5>expire:</h5>
                        <h5>{getDate(expiration_date)}</h5>
                    </div>
                </div>
            </div>
        </div>
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
        <div className='promocao'>
            <div className='promo-title-uses'>
                <div className='promo-title'><h3> Referral Boost Odd </h3></div>
                <div className='promo-uses'><div className='promo-uses-text'>x{nr_uses}</div></div>
            </div>
            <h4>Código promocional: {coupon}</h4>
            <h4>Descrição: {description}</h4>
            <h4>Boost: {boost_percentage}%</h4>
            <div className='promo-refrall-validade'>
                <div className='promo-refrall'>
                    <h4>Referrals:</h4>
                    <h4>{number_of_referrals_needed}</h4>
                </div>
                <div className='promo-validade'>
                    <div className='promo-validade-text'>
                        <h5>begin:</h5>
                        <h5>{getDate(begin_date)}</h5>
                    </div>
                    <div className='promo-validade-text'>
                        <h5>expire:</h5>
                        <h5>{getDate(expiration_date)}</h5>
                    </div>
                </div>
            </div>
        </div>
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
        <div className='promocao'>
            <div className='promo-title-uses'>
                <div className='promo-title'><h3> Referral Boost Odd </h3></div>
                <div className='promo-uses'><div className='promo-uses-text'>x{nr_uses}</div></div>
            </div>
            <h4>Código promocional: {coupon}</h4>
            <h4>Descrição: {description}</h4>
            <h4>Valor: {value_to_give} {coin_id}</h4>
            <div className='promo-refrall-validade'>
                <div className='promo-refrall'>
                    <h4>Referrals:</h4>
                    <h4>{number_of_referrals_needed}</h4>
                </div>
                <div className='promo-validade'>
                    <div className='promo-validade-text'>
                        <h5>begin:</h5>
                        <h5>{getDate(begin_date)}</h5>
                    </div>
                    <div className='promo-validade-text'>
                        <h5>expire:</h5>
                        <h5>{getDate(expiration_date)}</h5>
                    </div>
                </div>
            </div>
        </div>
    )
}