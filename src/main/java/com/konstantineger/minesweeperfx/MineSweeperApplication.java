package com.konstantineger.minesweeperfx;

import javafx.application.Application;
import javafx.geometry.*;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.util.Optional;

public class MineSweeperApplication extends Application {

    int width = 16;
    int height = 16;
    int nMinen = 35;
    Spielfeld sf;
    private GridPane grid;
    private final int btnSize = 31;

    @Override
    public void start(Stage stage) {

        Button newGameBtn = new Button("Neues Spiel");
        TextField rowsField = new TextField();
        rowsField.setPromptText("Reihen (default: 16)");
        rowsField.textProperty().addListener(((observableValue, s, t1) -> {
            try {
                this.height = Integer.parseInt(t1);
            } catch (Exception e) {
                System.out.println("unable to parse \"" + t1 + "\" to a number.");
            }
        }));
        TextField colsField = new TextField();
        colsField.setPromptText("Spalten (default: 16)");
        colsField.textProperty().addListener(((observableValue, s, t1) -> {
            try {
                this.width = Integer.parseInt(t1);
            } catch (Exception e) {
                System.out.println("unable to parse \"" + t1 + "\" to a number.");
            }
        }));
        TextField minesField = new TextField();
        minesField.setPromptText("Minen (default 35)");
        minesField.textProperty().addListener(((observableValue, s, t1) -> {
            try {
                this.nMinen = Integer.parseInt(t1);
            } catch (Exception e) {
                System.out.println("unable to parse \"" + t1 + "\" to a number.");
            }
        }));


        Button exitBtn = new Button("Beenden");
        exitBtn.setOnAction(actionEvent -> stage.close());

        VBox header = new VBox();
        header.setPadding(new Insets(10, 0, 10, 0));
        VBox g1 = new VBox();
        g1.getChildren().add(exitBtn);
        g1.setAlignment(Pos.BOTTOM_LEFT);
        HBox g2 = new HBox();
        g2.getChildren().addAll(newGameBtn, rowsField, colsField, minesField);
        g2.setAlignment(Pos.TOP_CENTER);
        header.getChildren().addAll(g1, g2);

        this.updateGridPaneToReflectBoard();


        BorderPane mainPane = new BorderPane();
        mainPane.setTop(header);
        mainPane.setCenter(grid);
        newGameBtn.setOnAction(actionEvent -> {
            this.sf = new Spielfeld(height, width, nMinen);
            this.updateGridPaneToReflectBoard();
            mainPane.setCenter(grid);
            this.updateBtnTexts(grid);
        });
        Scene scene = new Scene(mainPane, width * btnSize + 80, height * btnSize + 100);


        stage.setTitle("MineSweeper!");
        stage.setScene(scene);
        stage.show();
    }

    private void updateGridPaneToReflectBoard() {
        grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        this.sf = new Spielfeld(height, width, nMinen);
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                Button btn = new Button("");
                int finalI = i;
                int finalJ = j;
                btn.setOnMouseClicked(mouseEvent -> {
                    if (mouseEvent.getButton() == MouseButton.PRIMARY) {
                        boolean hitMine = this.sf.linksKlick(finalJ, finalI);
                        if (hitMine) {
                            this.setGameOverState(grid);
                            btn.setStyle("-fx-background-color: rgba(255, 0, 0, 0.3)");
                        }
                        else this.updateBtnTexts(grid);
                    } else if (mouseEvent.getButton() == MouseButton.SECONDARY) {
                        this.sf.rechtsKlick(finalJ, finalI);
                        this.updateBtnTexts(grid);
                        if (this.checkIfWon()) this.setGameWonState(grid);
                    }
                });
                btn.setMinSize(btnSize, btnSize);
                grid.add(btn, i, j);
                btn.setText(this.sf.getFeld(j, i).get().toString());
            }
        }
    }

    private void setGameWonState(GridPane grid) {
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                Button b = (Button) this.getByRowAndColInGrid(y, x, grid).get();
                Feld f = this.sf.getFeld(y, x).get();
                f.setGeoeffnet(true);
                b.setText(f.toString());
                b.setStyle("-fx-background-color: rgba(0, 255, 0, 0.20)");
            }
        }
    }

    private boolean checkIfWon() {
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                Feld f = this.sf.getFeld(y, x).get();
                if (f.isMarkiert() ^ f.isMine()) return false;
            }
        }
        return true;
    }

    private void updateBtnTexts(GridPane grid) {
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                Button b = (Button) getByRowAndColInGrid(y, x, grid).get();
                Feld feld = this.sf.getFeld(y, x).get();
                b.setText(feld.toString());
                b.setDisable(feld.isGeoeffnet());
                b.setStyle("");
            }
        }
    }

    private void setGameOverState(GridPane grid) {
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                Button b = (Button) getByRowAndColInGrid(y, x, grid).get();
                Feld feld = this.sf.getFeld(y, x).get();
                feld.setGeoeffnet(true);
                b.setText(feld.toString());
                b.setDisable(true);
            }
        }
    }

    private Optional<Node> getByRowAndColInGrid(int row, int col, GridPane grid) {
        Node result = null;
        for (Node node : grid.getChildren()) {
            if (GridPane.getRowIndex(node) == row && GridPane.getColumnIndex(node) == col) {
                result = node;
                break;
            }
        }
        return Optional.ofNullable(result);
    }

    public static void main(String[] args) {
        launch();
    }
}
