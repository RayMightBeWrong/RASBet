import React, { useState, useEffect } from 'react';
import '../Comons.css';
import { Button } from '../../Button';
import { Link } from 'react-router-dom';
import { LoadingScreen } from '../../LoadingScreen';

function AdminMenu({
    userState
}) {
    const [loading, setLoading] = useState(false)


    function atualizaJogos() {
        const requestOptions = {
            method: "POST",
            headers: { "Content-Type": "application/json" }
        }

        setLoading(true)
        fetch("http://localhost:8080/api/games/update", requestOptions)
            .then(res => {
                setLoading(false)
                if (res.status !== 200) {
                    let errorMsg;
                    if ((errorMsg = res.headers.get("x-error")) == null)
                        errorMsg = "Error occured"
                    alert(errorMsg)
                }
                else {
                    alert("Jogos atualizados")
                }
            })
            .catch(_ => alert("Error occured"))
    }
    if (userState === "admin") {
        return (
            <>
                <div className='greenBackGround'>
                    <div className='white-box'>
                        {loading ?
                            < LoadingScreen msg={"Trying update"} />
                            : null
                        }
                        <div>
                            <Link to='/admin_Options/registo_Expert'>
                                <Button buttonStyle="btn--inverted" buttonSize='btn--flex'>
                                    Registar Expert
                                </Button>
                            </Link>
                        </div>
                        <div>
                            <Link to='/admin_Options/promocoes'>
                                <Button buttonStyle="btn--inverted" buttonSize={'btn--flex'}>
                                    Menu promoções
                                </Button>
                            </Link>
                        </div>
                        <div>
                            <Link to='/admin_Options/coins'>
                                <Button buttonStyle="btn--inverted" buttonSize={'btn--flex'}>
                                    Criar nova coin
                                </Button>
                            </Link>
                        </div>
                        <div>
                            <Link to='/admin_Options/consultaPerfil'>
                                <Button buttonStyle="btn--inverted" buttonSize={'btn--flex'}>
                                    Consulta Perfil
                                </Button>
                            </Link>
                        </div>
                        <div>
                            <Button buttonStyle="btn--inverted" buttonSize={'btn--flex'} onClick={() => atualizaJogos()}>
                                Atualiza Jogos
                            </Button>
                        </div>
                    </div>
                </div>
            </>
        );
    } else {
        return ("");
    }
}

export default AdminMenu;
