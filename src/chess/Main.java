package chess;

import chess.board.Board;
import processing.core.PApplet;
import util.Color;
import vector.Vector2;

public class Main extends PApplet {
    Board board;

    @Override
    public void settings() {
        size(900, 900);
    }

    @Override
    public void setup() {
        board = new Board(this, width - 100, 8, new Vector2(50, 50));
        board.loadFen("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");

        board.setLightSquareColor(new Color(237, 188, 149));
        board.setDarkSquareColor(new Color(94, 42, 2));
    }

    @Override
    public void draw() {
        background(50);
        board.draw();
    }

    @Override
    public void mousePressed() {
        board.mousePressed(mouseX, mouseY);
    }

    @Override
    public void mouseReleased() {
        board.mouseReleased(mouseX, mouseY);
    }

    @Override
    public void keyPressed() {
        System.out.println(board.getFen());
    }

    public static void main(String[] args) {
        PApplet.main("chess.Main");
    }
}
