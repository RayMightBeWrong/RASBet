import React, { useState, useEffect } from 'react';
import { Button } from './Button';
import { Link } from 'react-router-dom';
import './Navbar.css';

function Navbar() {
  const [click, setClick] = useState(false);
  const [button, setButton] = useState(true);

  const handleClick = () => setClick(!click);
  const closeMobileMenu = () => setClick(false);

  const showButton = () => {
    if (window.innerWidth <= 1250) {
      setButton(false);
    } else {
      setButton(true);
    }
  };

  useEffect(() => {
    showButton();
  }, []);

  window.addEventListener('resize', showButton);

  return (
    <>
      <nav className='navbar'>
        <div className='navbar-container'>
          <Link to='/' className='navbar-logo' onClick={closeMobileMenu}>
            <img className='icon'
              src={'images/icon.png'}
              alt={''}
            />
          </Link>
          <div className='menu-icon' onClick={handleClick}>
            <i className={click ? 'fas fa-times' : 'fas fa-bars'} />
          </div>
          <ul className={click ? 'nav-menu active' : 'nav-menu'}>
            {/* <li className='nav-item'>
              <Link to='/' className='nav-links' onClick={closeMobileMenu}>
                TODOS
              </Link>
            </li> */}
            <li className='nav-item'>
              <Link
                to='/futebol'
                className='nav-links'
                onClick={closeMobileMenu}
              >
                FUTEBOL
              </Link>
            </li>
            <li className='nav-item'>
              <Link
                to='/basquetebol'
                className='nav-links'
                onClick={closeMobileMenu}
              >
                BASQUETEBOL
              </Link>
            </li>
            <li className='nav-item'>
              <Link
                to='/tenis'
                className='nav-links'
                onClick={closeMobileMenu}
              >
                TÉNIS
              </Link>
            </li>
            <li className='nav-item'>
              <Link
                to='/motogp'
                className='nav-links'
                onClick={closeMobileMenu}
              >
                MOTOGP
              </Link>
            </li>
            <li>
              <Link
                to='/perfil'
                className='nav-links'
                onClick={closeMobileMenu}
              >
                PERFIL
              </Link>
            </li>
            <li>
              <Link
                to='/login'
                className='nav-links-mobile'
                onClick={closeMobileMenu}
              >
                Log In
              </Link>
            </li>
          </ul>
          {button && <Link to='/login' ><Button buttonStyle='btn--outline'>Log In</Button> </Link>}
        </div>
      </nav>
    </>
  );
}

export default Navbar;
