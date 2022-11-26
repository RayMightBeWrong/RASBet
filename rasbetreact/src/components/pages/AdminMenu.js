import React from 'react';
import './Login.css';
import { Button } from '../Button';
import { Link } from 'react-router-dom';

function AdminMenu({
    userState
}) {
    if (userState === "admin") {
        return (
            <>
                <div className='registo'>
                    <div className='white-box'>
                        <Link to='/admin_Options/registo_Expert'>
                            <Button buttonStyle="btn--inverted" buttonSize='btn--flex'>
                                Registar Expert
                            </Button>
                        </Link>
                        <Link to='/admin_Options/promocoes'>
                            <Button buttonStyle="btn--inverted" buttonSize={'btn--flex'}>
                                Menu promoções
                            </Button>
                        </Link>
                        <Link to='/admin_Options/coins'>
                            <Button buttonStyle="btn--inverted" buttonSize={'btn--flex'}>
                                Criar nova coin
                            </Button>
                        </Link>
                        <Link to='/admin_Options/consultaPerfil'>
                            <Button buttonStyle="btn--inverted" buttonSize={'btn--flex'}>
                                Consulta Perfil
                            </Button>
                        </Link>
                    </div>
                </div>
            </>
        );
    } else {
        return ("");
    }
}

export default AdminMenu;
