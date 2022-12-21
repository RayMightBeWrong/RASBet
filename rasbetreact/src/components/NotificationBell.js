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
                < div className="notificationBell-body" >
                    <h1>Notificações</h1>
                    {notificationList.lista.map(notification => (
                        <div key={notification.id}>
                            {notification.type}
                            {notification.msg}
                            {notification.timestamp}
                        </div>
                    ))}
                    <Button onClick={function () { closePopup(); markAsSeen() }} > Close</Button>
                </div>
            </div >
        </>
    );
};