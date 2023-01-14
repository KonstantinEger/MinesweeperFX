package com.konstantineger.minesweeperfx;

import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Stream;

public class Spielfeld {
    private Feld[][] felder;
    private int nMinen;
    private int markierungen;

    public Spielfeld(int breite, int hoehe, int nMinen) {
        if (nMinen > breite * hoehe) {
            throw new Error("too many mines");
        }

        this.felder = new Feld[breite][hoehe];
        this.nMinen = nMinen;
        this.markierungen = 0;

        for (int i = 0; i < felder.length; i++) {
            for (int j = 0; j < felder[0].length; j++) {
                felder[i][j] = new Feld(false, false, 0);
            }
        }

        for (int i = 0; i < nMinen; i++) {
            boolean mineIsSet = false;
            while (!mineIsSet) {
                int randRow = (int) Math.floor(Math.random() * breite);
                int randCol = (int) Math.floor(Math.random() * hoehe);
                Feld f = getFeld(randRow, randCol).get();
                if (!f.isMine()) {
                    f.setMine(true);
                    mineIsSet = true;
                }
            }
        }

        calculateNBenachbarteMinen();
    }

    public void calculateNBenachbarteMinen() {
        for (int i = 0; i < felder.length; i++) {
            for (int j = 0; j < felder[0].length; j++) {
                int nachbarMinen = 0;
                Optional<Feld>[] nachbarFelder = new Optional[8];
                nachbarFelder[0] = getFeld(i - 1, j - 1);
                nachbarFelder[1] = getFeld(i, j - 1);
                nachbarFelder[2] = getFeld(i + 1, j - 1);
                nachbarFelder[3] = getFeld(i - 1, j);
                nachbarFelder[4] = getFeld(i + 1, j);
                nachbarFelder[5] = getFeld(i - 1, j + 1);
                nachbarFelder[6] = getFeld(i, j + 1);
                nachbarFelder[7] = getFeld(i + 1, j + 1);

                for (Optional<Feld> f : nachbarFelder) {
                    if (f.isPresent()) {
                        if (f.get().isMine()) {
                            nachbarMinen++;
                        }
                    }
                }

                getFeld(i, j).get().setnBenachbarteMinen(nachbarMinen);
            }
        }
    }

    public Optional<Feld> getFeld(int row, int col) {
        if (row >= 0 && col >= 0 && row < felder.length && col < felder[0].length)
            return Optional.of(felder[row][col]);
        else
            return Optional.empty();
    }

    public boolean linksKlick(int row, int col) {
        return getFeld(row, col)
                .map(f -> {
                    if (f.isGeoeffnet()) return false;
                    f.setGeoeffnet(true);
                    if (f.isMine()) return true;
                    else if (f.getnBenachbarteMinen() == 0) {
                        return linksKlick(row - 1, col)
                                || linksKlick(row, col - 1)
                                || linksKlick(row - 1, col - 1)
                                || linksKlick(row, col + 1)
                                || linksKlick(row + 1, col)
                                || linksKlick(row - 1, col + 1)
                                || linksKlick(row + 1, col - 1)
                                || linksKlick(row + 1, col + 1);
                    } else return false;
                }).orElse(false);
    }

    public void rechtsKlick(int row, int col) {
        getFeld(row, col).ifPresent(feld -> feld.setMarkiert(!feld.isMarkiert()));
    }

    public Stream<Feld> felderStream() {
        return Arrays.stream(this.felder).flatMap(Arrays::stream);
    }

    public void print(boolean allOpen) {
        for (Feld[] row : felder) {
            System.out.print("[");
            for (Feld f : row) {
                boolean temp = f.isGeoeffnet();
                if (allOpen)
                    f.setGeoeffnet(true);
                System.out.print(" " + f + " ");
                f.setGeoeffnet(temp);
            }
            System.out.println("]");
        }
        System.out.println("Minen:    " + nMinen);
        System.out.println("Markiert: " + markierungen);
    }
}
