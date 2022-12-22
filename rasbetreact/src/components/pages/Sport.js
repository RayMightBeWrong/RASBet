import React, { useState, useEffect } from 'react';
import { GamesTab } from '../GamesTab'

function Sport({
    sportType,
    userState,
    userId
}) {
    const [gamesWithSubscription, setGamesWithSubscription] = useState([])
    const [jogos, setJogos] = useState([])
    const [subscriptions, setSubscriptions] = useState([])
    const [rerender, setRerender] = useState(false)

    useEffect(() => {
        const requestOptions = {
            method: 'GET',
            headers: { "Content-Type": "application/json" }
        }
        let link = "http://localhost:8080/api/games/sport/" + sportType
        if (sportType === "any" || sportType === "Subscriptions") {
            link = "http://localhost:8080/api/games/"
        }
        fetch(link, requestOptions)
            .then(res => res.json())
            .then((result) => {
                setJogos(result)
            })
    }, [sportType, rerender])

    useEffect(() => {
        const requestOptions = {
            method: 'GET',
            headers: { "Content-Type": "application/json" }
        }
        fetch("http://localhost:8080/api/games/subscribed?gambler_id=" + userId, requestOptions)
            .then(res => res.json())
            .then((result) => {
                setSubscriptions(result)
            })
    }, [userId, rerender])

    useEffect(() => {
        let arr = []
        jogos.forEach(function (jogo) {
            if (subscriptions.includes(jogo.id)) {
                jogo["subscribed"] = true
                arr.push(jogo)
            } else if (sportType !== "Subscriptions") {
                jogo["subscribed"] = false
                arr.push(jogo)
            }
        })
        setGamesWithSubscription(arr)
    }, [jogos, subscriptions, sportType])

    return (
        <>
            <GamesTab
                games={gamesWithSubscription}
                sportType={sportType}
                userState={userState}
                userId={userId}
                rerender={() => setRerender(!rerender)}
            />
        </>
    );
}

export default Sport;
