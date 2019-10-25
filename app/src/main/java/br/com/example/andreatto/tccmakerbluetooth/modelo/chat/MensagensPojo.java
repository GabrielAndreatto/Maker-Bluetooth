package br.com.example.andreatto.tccmakerbluetooth.modelo.chat;

public class MensagensPojo {

    public String texto;
    public int code = 0;

    public MensagensPojo() {
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
