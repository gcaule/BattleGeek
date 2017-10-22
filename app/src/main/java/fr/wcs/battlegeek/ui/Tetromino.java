package fr.wcs.battlegeek.ui;

import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import fr.wcs.battlegeek.R;
import fr.wcs.battlegeek.model.Settings;

import static fr.wcs.battlegeek.ui.Tetromino.Shape.I;
import static fr.wcs.battlegeek.ui.Tetromino.Shape.J;
import static fr.wcs.battlegeek.ui.Tetromino.Shape.L;
import static fr.wcs.battlegeek.ui.Tetromino.Shape.O;
import static fr.wcs.battlegeek.ui.Tetromino.Shape.S;
import static fr.wcs.battlegeek.ui.Tetromino.Shape.T;
import static fr.wcs.battlegeek.ui.Tetromino.Shape.Z;

/**
 * Created by adphi on 27/09/17.
 */
public class Tetromino extends Item {

    /**
     * Tetromino Colors enumeration
     */
    public enum Colors {
        YELLOW, ORANGE, RED, PURPLE, GREEN, BLUE, LTBLUE
    }

    /**
     * Tetromino's Shape's Symbols Enumeration
     */
    public enum Shape {
        I ("I"),
        O ("O"),
        T ("T"),
        J ("J"),
        L ("L"),
        S ("S"),
        Z ("Z"),
        NONE ("X");

        private String name = "";

        Shape(String name){
            this.name = name;
        }
        public String toString(){
            return name;
        }

    }

    private String TAG = Settings.TAG;
    private Shape mShape;
    private Colors mColor;
    private static HashMap<Shape, Colors> mColorsMap = new HashMap<>();

    private static HashMap<Shape, Colors> mRandomColorsMap = new HashMap<>();

    private static HashMap<Shape, ArrayList<int[][]>> mTetrominoMatrixMap = new HashMap<>();

    private int rotationCount = 0;

    public Tetromino(MapView view, Grid grid, Shape shape, @Nullable Colors color) {
        super(view, grid);
        this.mShape = shape;
        this.mColor = color;
        init();
    }

    public Tetromino(MapView view, Grid grid, float x, float y, Shape shape, @Nullable Colors color) {
        super(view, grid, x, y);
        this.mShape = shape;
        this.mColor = color;
        init();
    }

    private void init() {

        if(this.mColor == null) {
            this.mColor = getColorMap().get(mShape);
        }
        getTetrominoMatrixMap();
    }

    @Override
    public void rotate() {
        // BONUS
        if(getBlocks().size() == 1) return;

        // No place
        if(mX + mHeight >= Settings.GRID_SIZE || mY + mWidth >= Settings.GRID_SIZE ) {
            showToast(R.string.RotationImpossible);
            return;
        }

        // Rotation Using Matrix (easier but boring)
        int[][] matrix = this.toMatrix();
        ArrayList<int[][]> list = mTetrominoMatrixMap.get(this.mShape);
        for (int i = 0; i < list.size(); i++) {
            if(Arrays.deepEquals(list.get(i), matrix)){
                rotationCount = i;
                break;
            }
        }
        this.fromMatrix(list.get((rotationCount + 1) % list.size()));
        getWidth();
        getHeight();
        mView.invalidate();
    }


    /**
     * Static Method to get the color of a Tetromino according to its shape
     * @return
     */
    public static HashMap<Shape, Colors> getColorMap() {
        if(mColorsMap.isEmpty()) {
            mColorsMap.put(I, Tetromino.Colors.LTBLUE);
            mColorsMap.put(T, Tetromino.Colors.PURPLE);
            mColorsMap.put(Z, Tetromino.Colors.RED);
            mColorsMap.put(O, Tetromino.Colors.YELLOW);
            mColorsMap.put(J, Tetromino.Colors.BLUE);
            mColorsMap.put(L, Tetromino.Colors.ORANGE);
            mColorsMap.put(S, Tetromino.Colors.GREEN);
        }
        return mColorsMap;
    }

    public static HashMap<Shape, Colors> getRandomColorMap() {
        if(mRandomColorsMap.isEmpty()) {
            Shape[] shapes = Shape.values();
            ArrayList<Colors> colors = new ArrayList<>(Arrays.asList(Colors.values()));
            // Iterate through all shapes except the None one
            for (int i = 0; i < shapes.length - 1; i++) {
                int index = (int)(Math.random() * (colors.size() -1));
                mRandomColorsMap.put(shapes[i], colors.get(index));
                colors.remove(index);
            }
        }
        return mRandomColorsMap;
    }

    public Shape getShape() {
        return mShape;
    }

    public void setShape(Shape shape) {
        mShape = shape;
    }

    public Colors getColor() {
        return mColor;
    }

    public void setColor(Colors color) {
        mColor = color;
    }

    private int[][] toMatrix(){
        int[][] matrix = new int[getHeight() + 1][getWidth() + 1];
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                matrix[i][j] = 0;
            }
        }
        for (Block block : getBlocks()) {
            int x = (int)block.getX();
            int y = (int)block.getY();
            matrix[y][x] = 1;
        }
        return matrix;
    }

    private void fromMatrix(int[][] matrix) {
        int count = 0;
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                if (matrix[i][j] == 1) {
                    getBlocks().get(count).setX(j);
                    getBlocks().get(count).setY(i);
                    count++ ;
                }
            }
        }
    }

    public static  HashMap<Shape, ArrayList<int[][]>> getTetrominoMatrixMap() {
        if (mTetrominoMatrixMap.isEmpty()) {
            // Tetromino O
            ArrayList<int[][]> listMatrixO = new ArrayList<>();
            int[][] matrixO = new int[][]{
                    {1, 1},
                    {1, 1}
            };
            listMatrixO.add(matrixO);
            mTetrominoMatrixMap.put(O, listMatrixO);

            // Tetromino I
            ArrayList<int[][]> listMatrixI = new ArrayList<>();
            int[][] matrixIHorizontal = new int[][]{
                    {1, 1, 1, 1}
            };
            int[][] matrixIVertical = new int[][]{
                    {1},
                    {1},
                    {1},
                    {1}
            };
            listMatrixI.add(matrixIHorizontal);
            listMatrixI.add(matrixIVertical);
            mTetrominoMatrixMap.put(I, listMatrixI);

            // Tetromino L
            ArrayList<int[][]> listMatrixL = new ArrayList<>();
            int[][] matrixL1 = new int[][]{
                    {1, 1, 1},
                    {1, 0, 0}
            };
            int[][] matrixL2 = new int[][]{
                    {1, 1},
                    {0, 1},
                    {0, 1}
            };
            int[][] matrixL3 = new int[][]{
                    {0, 0, 1},
                    {1, 1, 1}
            };
            int[][] matrixL4 = new int[][]{
                    {1, 0},
                    {1, 0},
                    {1, 1}
            };
            listMatrixL.add(matrixL1);
            listMatrixL.add(matrixL2);
            listMatrixL.add(matrixL3);
            listMatrixL.add(matrixL4);
            mTetrominoMatrixMap.put(L, listMatrixL);

            // Tetromino J
            ArrayList<int[][]> listMatrixJ = new ArrayList<>();
            int[][] matrixJ1 = new int[][]{
                    {1, 1, 1},
                    {0, 0, 1}
            };
            int[][] matrixJ2 = new int[][]{
                    {0, 1},
                    {0, 1},
                    {1, 1}
            };
            int[][] matrixJ3 = new int[][]{
                    {1, 0, 0},
                    {1, 1, 1}
            };
            int[][] matrixJ4 = new int[][]{
                    {1, 1},
                    {1, 0},
                    {1, 0}
            };
            listMatrixJ.add(matrixJ1);
            listMatrixJ.add(matrixJ2);
            listMatrixJ.add(matrixJ3);
            listMatrixJ.add(matrixJ4);
            mTetrominoMatrixMap.put(J, listMatrixJ);

            // Tetromino T
            ArrayList<int[][]> listMatrixT = new ArrayList<>();
            int[][] matrixT1 = new int[][]{
                    {1, 1, 1},
                    {0, 1, 0}
            };
            int[][] matrixT2 = new int[][]{
                    {0, 1},
                    {1, 1},
                    {0, 1}
            };
            int[][] matrixT3 = new int[][]{
                    {0, 1, 0},
                    {1, 1, 1}
            };
            int[][] matrixT4 = new int[][]{
                    {1, 0},
                    {1, 1},
                    {1, 0}
            };

            listMatrixT.add(matrixT1);
            listMatrixT.add(matrixT2);
            listMatrixT.add(matrixT3);
            listMatrixT.add(matrixT4);
            mTetrominoMatrixMap.put(T, listMatrixT);

            // Tetromino S
            ArrayList<int[][]> listMatrixS = new ArrayList<>();
            int[][] matrixS1 = new int[][]{
                    {0, 1, 1},
                    {1, 1, 0}
            };
            int[][] matrixS2 = new int[][]{
                    {1, 0},
                    {1, 1},
                    {0, 1}
            };
            listMatrixS.add(matrixS1);
            listMatrixS.add(matrixS2);
            mTetrominoMatrixMap.put(S, listMatrixS);

            // Tetromino Z
            ArrayList<int[][]> listMatrixZ = new ArrayList<>();
            int[][] matrixZ1 = new int[][]{
                    {1, 1, 0},
                    {0, 1, 1}
            };
            int[][] matrixZ2 = new int[][]{
                    {0, 1},
                    {1, 1},
                    {1, 0}
            };
            listMatrixZ.add(matrixZ1);
            listMatrixZ.add(matrixZ2);
            mTetrominoMatrixMap.put(Z, listMatrixZ);
        }
        return mTetrominoMatrixMap;
    }
}
