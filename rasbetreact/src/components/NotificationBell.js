import React, { useState, useEffect } from "react";
import "./NotificationBell.css";
import { Carteira } from './objects/Carteira';
import { Button } from './Button';

export const NotificationBell = ({
    closePopup,
    notificationList,
    markAsSeen
}) => {

    useEffect(() => {
        console.log(notificationList)
    }, [notificationList])
    return (
        <>
            < div className="notificationBell-container" >
                <div className="notificationBell-body" >
                    <div className="payMethods-close-button">
                        <div className="pmAline"> </div>
                        <h1>Notificações</h1>
                        <Button buttonStyle={'btn--flex'} onClick={function () { closePopup(); markAsSeen() }}> X </Button >
                    </div>
                    {notificationList.lista.map(notification => (
                        <div key={notification.id}>
                            {notification.type}
                            {notification.msg}
                            {notification.timestamp}
                        </div>
                    ))}
                </div>
            </div >
        </>
    );
};