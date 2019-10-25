package br.com.example.andreatto.tccmakerbluetooth.modelo;

public class Board {

    private long id;
    private String nome;
    private String descricao;
    private String bluetoothName;
    private String macAddress;
    private String rede;
    private String ip;

    private int conectedBluetooth;
    private int conectedWifi;

    private String created_at;
    private String updated_at;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getBluetoothName() {
        return bluetoothName;
    }

    public void setBluetoothName(String bluetoothName) {
        this.bluetoothName = bluetoothName;
    }

    public String getMacAddress() {
        return macAddress;
    }

    public void setMacAddress(String macAddress) {
        this.macAddress = macAddress;
    }

    public String getRede() {
        return rede;
    }

    public void setRede(String rede) {
        this.rede = rede;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getConectedBluetooth() {
        return conectedBluetooth;
    }

    public void setConectedBluetooth(int conectedBluetooth) {
        this.conectedBluetooth = conectedBluetooth;
    }

    public int getConectedWifi() {
        return conectedWifi;
    }

    public void setConectedWifi(int conectedWifi) {
        this.conectedWifi = conectedWifi;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }
}
