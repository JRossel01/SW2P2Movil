package com.grupo12.sw2p2movil.util;

import android.content.Context;
import android.view.*;
import android.widget.*;
import com.grupo12.sw2p2movil.R;
import com.grupo12.sw2p2movil.negocio.Nassistant.MensajeConversacion;

import java.util.List;

public class MensajeAdapter extends ArrayAdapter<MensajeConversacion> {

    private final Context context;

    public MensajeAdapter(Context context, List<MensajeConversacion> mensajes) {
        super(context, 0, mensajes);
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MensajeConversacion mensaje = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_mensaje, parent, false);
        }

        TextView texto = convertView.findViewById(R.id.textoMensaje);
        texto.setText(mensaje.mensaje);
        texto.setTextColor(0xFFFFFFFF);

        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) texto.getLayoutParams();

        if (mensaje.autor.equals("usuario")) {
            texto.setBackgroundResource(R.drawable.burbuja_usuario);
            params.gravity = Gravity.END;
        } else {
            texto.setBackgroundResource(R.drawable.burbuja_ia);
            params.gravity = Gravity.START;
        }

        texto.setLayoutParams(params);

        return convertView;
    }
}