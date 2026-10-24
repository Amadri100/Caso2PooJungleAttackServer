package src.logica;

public class Jugador {

    private final int id;
    private final String nickname;

    public Jugador(int id, String nickname) {
        this.id = id;
        this.nickname = nickname;
    }

    public int getId() {
        return id;
    }

    public String getNickname() {
        return nickname;
    }
}
