import React, { useState, useEffect } from "react";
import "./NotificationBell.css";
import { Carteira } from './objects/Carteira';
import { Button } from './Button';

const getDate = (date) => {
    let dateS = date.split('T')
    return (
        <div className='notification-date-hour'>
            <h5>{dateS[0]}</h5>
            <h5>{dateS[1].split('.')[0]}</h5>
        </div>
    )
}

export const NotificationBell = ({
    closePopup,
    notificationList,
    markAsSeen
}) => {

    useEffect(() => {
        console.log(notificationList)
    }, [notificationList])

    let vReverse = [...notificationList.lista].reverse()
    console.log("notList->", notificationList.lista);
    console.log("reverse->", vReverse);
    return (
        <>
            < div className="notificationBell-container" >
                <div className="notificationBell-body" >
                    <div className="payMethods-close-button">
                        <div className="pmAline"> </div>
                        <h1>Notificações</h1>
                        <Button buttonStyle={'btn--flex'} onClick={function () { closePopup(); markAsSeen() }}> X </Button >
                    </div>
                    <div className={'notification-box'}>
                        {vReverse.map(notification => (
                            <div className={'notification-line'} key={notification.id}>
                                <div className={'notification-name-time'}>
                                    <div className={'notification-name'}>
                                        <h3>{notification.type}</h3>
                                    </div>
                                    {getDate(notification.timestamp)}
                                </div>
                                <div className='notification-text'>
                                    <h4>{notification.msg}</h4>
                                </div>
                            </div>
                        ))}
                    </div>
                </div>
            </div >
        </>
    );
};