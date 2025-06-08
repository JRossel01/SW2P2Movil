package com.grupo12.sw2p2movil.negocio;

import com.grupo12.sw2p2movil.dato.Dlogout;

public class Nlogout {
    private final Dlogout dlogout = new Dlogout();

    public boolean cerrarSesion(String jwt) {
        return dlogout.logout(jwt);
    }
}
