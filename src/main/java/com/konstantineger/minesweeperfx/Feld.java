package com.konstantineger.minesweeperfx;

public class Feld {
    private boolean mine;
    private boolean geoeffnet;
    private boolean markiert;
    private int     nBenachbarteMinen;

    public Feld(boolean mine, boolean geoeffnet, int nBenachbarteMinen) {
        this.mine              = mine;
        this.geoeffnet         = geoeffnet;
        this.markiert          = false;
        this.nBenachbarteMinen = nBenachbarteMinen;
    }

    public Feld(Feld other) {
        this.mine              = other.mine;
        this.geoeffnet         = other.geoeffnet;
        this.markiert          = other.markiert;
        this.nBenachbarteMinen = other.nBenachbarteMinen;
    }

    public boolean isMine() {
        return mine;
    }

    public void setMine(boolean mine) {
        this.mine = mine;
    }

    public boolean isGeoeffnet() {
        return geoeffnet;
    }

    public void setGeoeffnet(boolean geoeffnet) {
        this.geoeffnet = geoeffnet;
    }

    public boolean isMarkiert() {
        return markiert;
    }

    public void setMarkiert(boolean markiert) {
        this.markiert = markiert;
    }

    public int getnBenachbarteMinen() {
        return nBenachbarteMinen;
    }

    public void setnBenachbarteMinen(int nBenachbarteMinen) {
        this.nBenachbarteMinen = nBenachbarteMinen;
    }

    @Override
    public String toString() {
        if (isGeoeffnet()) {
            if (isMine()) {
                return "\uD83D\uDCA5";
            } else if (getnBenachbarteMinen() == 0) {
                return " ";
            } else {
                return getnBenachbarteMinen() + "";
            }
        } else {
            if (isMarkiert()) return "⚑";
            else return " ";
        }
    }
}
