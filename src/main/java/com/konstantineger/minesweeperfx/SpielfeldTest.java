package com.konstantineger.minesweeperfx;

import java.util.Optional;
import java.util.function.Function;

public class SpielfeldTest {
    public static void main(String[] args){
        testSpielfeldConstructor();
        testSpielfeldConstructorThrows();
        testGetFeld();
        testRechtsKlick();
        testLinksKlick();
    }

    public static void testSpielfeldConstructor() {
        int size = 3;
        int nMinen = 6;
        Spielfeld sf = new Spielfeld(size, size, nMinen);

        int countedMines = 0;
        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                Optional<Feld> fo = sf.getFeld(x, y);
                assertEq(fo.isPresent(), true);
                Feld f = fo.get();
                assertEq(f.isGeoeffnet(), false);
                assertEq(f.isMarkiert(), false);
                if (f.isMine()) countedMines++;
            }
        }
        assertEq(countedMines, nMinen);
    }

    public static void testSpielfeldConstructorThrows() {
        int size = 3;
        int nMinen = 10;
        try {
            new Spielfeld(size, size, nMinen);
        } catch (Error e) {
            assertEq(true, true);
            return;
        }
        assertEq(true, false);
    }

    public static void testGetFeld() {
        int numRows = 4;
        int numCols = 5;
        Spielfeld sf = new Spielfeld(numRows, numCols, 0);

        assertEq(sf.getFeld(0, 0).isPresent(), true);
        assertEq(sf.getFeld(3, 4).isPresent(), true);
        assertEq(sf.getFeld(4, 3).isPresent(), false);
        assertEq(sf.getFeld(-1, -1).isPresent(), false);
        assertEq(sf.getFeld(3, -1).isPresent(), false);
    }

    public static void testRechtsKlick() {
        Spielfeld sf = new Spielfeld(4, 4, 0);
        int countMarkiert = 0;
        for (int x = 0; x < 4; x++) {
            for (int y = 0; y < 4; y++) {
                if (sf.getFeld(x, y).get().isMarkiert()) countMarkiert++;
            }
        }
        assertEq(countMarkiert, 0);

        sf.rechtsKlick(0, 0);
        sf.rechtsKlick(2, 0);
        sf.rechtsKlick(3, 3);
        sf.rechtsKlick(-1, -2);
        sf.rechtsKlick(2, 6);

        countMarkiert = 0;
        for (int x = 0; x < 4; x++) {
            for (int y = 0; y < 4; y++) {
                if (sf.getFeld(x, y).get().isMarkiert()) countMarkiert++;
            }
        }
        assertEq(countMarkiert, 3);
    }

    public static void testLinksKlick() {
        Function<Integer, Spielfeld> spielfeldCreator = (unused) -> {
            // [1 1     ]
            // [M 2     ]
            // [M 2     ]
            // [1 1     ]
            Spielfeld sf = new Spielfeld(4, 4, 0);
            sf.getFeld(1, 0).get().setMine(true);
            sf.getFeld(2, 0).get().setMine(true);
            sf.calculateNBenachbarteMinen();
            return sf;
        };

        Spielfeld sf = spielfeldCreator.apply(0);
        assertEq(sf.linksKlick(0, 0), false);
        assertEq(sf.felderStream().filter(Feld::isGeoeffnet).count(), (long) 1);

        sf = spielfeldCreator.apply(0);
        assertEq(sf.linksKlick(1, 0), true);

        sf = spielfeldCreator.apply(0);
        assertEq(sf.linksKlick(2, 2), false);
        assertEq(sf.felderStream().filter(f -> !f.isGeoeffnet()).count(), (long) 4);
        assertEq(sf.felderStream().filter(f -> f.isGeoeffnet() && f.isMine()).count(), (long) 0);
    }

    private static <T> void assertEq(T a, T b) {
        if (!a.equals(b)) {
            System.out.println("x assertion failed: " + a + " != " + b);
            new Throwable().printStackTrace();
        } else {
            System.out.println("- assertion success");
        }
    }
}
