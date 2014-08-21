package com.dvr.mailpush;

public interface EstadoGmail {
	public void Alerta(String Alert);

	public void OnEvento(String evento);
	
	public void Login(boolean login);
}
