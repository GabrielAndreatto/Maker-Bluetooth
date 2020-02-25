package br.com.example.andreatto.tccmakerbluetooth.modelo;

public class Sensor {

    private long id;
    private String name;
    private String command;
    private String value;
    private String iconId;

    private String used_at;
    private String created_at;
    private String updated_at;

    public Sensor() {}

    public Sensor(long id, String name, String command, String value, String iconId, String used_at, String created_at, String updated_at) {
        this.id = id;
        this.name = name;
        this.command = command;
        this.value = value;
        this.iconId = iconId;
        this.used_at = used_at;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getIcon() {
        return iconId;
    }

    public void setIcon(String iconId) {
        this.iconId = iconId;
    }

    public String getUsed_at() {
        return used_at;
    }

    public void setUsed_at(String used_at) {
        this.used_at = used_at;
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
