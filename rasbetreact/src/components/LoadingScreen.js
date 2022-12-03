import React, { useState, useEffect } from 'react';
import "./LoadingScreen.css";
import { NewWallet } from "./NewWallet";

export const LoadingScreen = ({
    msg
}) => {

    return (
        <>
            < div className="loadingScreen-container" >
                < div className="loadingScreen-body" >
                    <h2>{msg}</h2>
                </div>
            </div>
        </>
    );
};
