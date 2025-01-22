package model;

import java.util.logging.Logger;

public class Coordonnees {
    private static final Logger logger = Logger.getLogger(Coordonnees.class.getName());
    private int x;
    private int y;

    public Coordonnees(int x, int y) {
        this.x = x;
        this.y = y;
    }


    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Coordonnees)) return false;
        Coordonnees other = (Coordonnees) obj;
        return this.x == other.x && this.y == other.y;
    }

    @Override
    public int hashCode() {
        return 31 * x + y;
    }

    @Override
    public String toString() {
        return "(" + x + ", " + y + ")";
    }

    public String toChessNotation() {
        char file = (char) ('a' + x);
        int rank = 8 - y;
        return "" + file + rank;
    }

}