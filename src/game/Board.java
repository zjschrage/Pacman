package game;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import tile.MapTile;

public class Board {

    private final int X_TILES = 28;
    private final int Y_TILES = 31;
    private final int TILE_SIZE = 24;
    private MapTile[][] gameBoard;

    public Board() {
        gameBoard = new MapTile[Y_TILES][X_TILES];
        init();
    }

    public void init() {
        Scanner in;
        Scanner in2;
        try {
            in = new Scanner(new File("res/files/PacmanMap.txt"));
            in2 = new Scanner(new File("res/files/PacmanTileDirections.txt"));
            for (int i = 0; i < Y_TILES; i++) {
                for (int j = 0; j < X_TILES; j++) {
                    int tempId = Integer.parseInt(in.next());
                    boolean[] tempDirec = new boolean[4];
                    if (tempId == 1 || tempId == 2 || tempId == 7) {
                        String direc = in2.nextLine();
                        for (int s = 0; s < direc.length(); s++) {
                            switch (direc.charAt(s)) {
                                case 'w':
                                   tempDirec[0] = true;
                                   break;
                                case 'a':
                                   tempDirec[1] = true;
                                   break; 
                                case 's':
                                   tempDirec[2] = true;
                                   break;
                                case 'd':
                                   tempDirec[3] = true;
                                   break; 
                            }
                        }
                    }
                    gameBoard[i][j] = new MapTile(tempId, tempDirec);
    
                }
            }
            in.close();
            in2.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public int getTileSize() {
        return TILE_SIZE;
    }

    public MapTile[][] getBoard() {
        return gameBoard;
    }

    public MapTile getTile(int x, int y) {
        
        return gameBoard[y/TILE_SIZE][x/TILE_SIZE];
    }

    public void setTileId(int x, int y, int id) {
        gameBoard[y/TILE_SIZE][x/TILE_SIZE].setId(id);
    }

    public boolean isOnTrack(int x) {
        if ((x-(TILE_SIZE/2))%TILE_SIZE == 0) return true;
        else return false;
    }

}
