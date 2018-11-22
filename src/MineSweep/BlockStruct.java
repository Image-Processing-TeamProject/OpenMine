package MineSweep;

import BlockData.Block;
import BlockData.NoneBlock;

import java.util.ArrayList;

import static MineSweep.Main.*;

public class BlockStruct {
    private volatile static BlockStruct instance = null;
    private Block[][] blocks;
    private int[][] buffers;
    private boolean[][] check_buffers;

    private BlockStruct(){
        for (int i = 0; i < 16; i++){
            for (int j = 0; j < 30; j++){
                blocks[i][j] = new NoneBlock(i, j, nonePosition);
            }
        }

        for (int i = 0; i < 16; i++){
            for (int j = 0; j < 30; j++){
                buffers[i][j] = nonePosition;
            }
        }

        for (int i = 0; i < 16; i++){
            for (int j = 0; j < 30; j++){
                check_buffers[i][j] = false;
            }
        }
    }

    public static BlockStruct getInstance(){
        if (instance == null){
            synchronized (BlockStruct.class){
                if (instance == null){
                    instance = new BlockStruct();
                }
            }
        }

        return instance;
    }
    public void init(){
        for (int i = 0; i < 16; i++){
            for (int j = 0; j < 30; j++){
                blocks[i][j] = new NoneBlock(i, j, nonePosition);
            }
        }

        for (int i = 0; i < 16; i++){
            for (int j = 0; j < 30; j++){
                buffers[i][j] = nonePosition;
            }
        }

        for (int i = 0; i < 16; i++){
            for (int j = 0; j < 30; j++){
                check_buffers[i][j] = false;
            }
        }
    }

    public Block getBlock(int i, int j) {
        if (i < 0 || i > 16){
            System.out.println("BlockStruct getBlock out of Index: i");
            return null;
        }
        if (j < 0 || j > 30){
            System.out.println("BlockStruct getBlock out of Index: j");
            return null;
        }

        return blocks[i][j];
    }
    public int getBuffer(int i, int j) {
        if (i < 0 || i > 16){
            System.out.println("BlockStruct getBlock out of Index: i");
            return Main.Error;
        }
        if (j < 0 || j > 30){
            System.out.println("BlockStruct getBlock out of Index: j");
            return Main.Error;
        }
        return buffers[i][j];
    }
    public void setBlock(int i, int j, Block block){
        if (i < 0 || i > 16){
            System.out.println("BlockStruct getBlock out of Index: i");
            return;
        }
        if (j < 0 || j > 30){
            System.out.println("BlockStruct getBlock out of Index: j");
            return;
        }

        this.blocks[i][j] = block;
    }
    public void setBuffer(int i, int j, int buffer){
        if (i < 0 || i > 16){
            System.out.println("BlockStruct getBlock out of Index: i");
            return;
        }
        if (j < 0 || j > 30){
            System.out.println("BlockStruct getBlock out of Index: j");
            return;
        }

        this.buffers[i][j] = buffer;
    }

    // set buffers after finding mine
    // i, j -> mine of position
    public void calcBuffer(int i, int j){
        for (int x = i - 1; x < i + 2; x++){
            for (int y = j - 1; y < j + 2; y++){
                if (x < 0 || x > 15) continue;
                if (y < 0 || y > 29) continue;

                if (buffers[x][y] == minePosition) continue;
                if (buffers[x][y] == nonePosition) continue;
                if (buffers[x][y] == completeMineSweep) continue;

                buffers[x][y]--;
            }
        }
    }

    public void findMine(int i, int j) {
        if (buffers[i][j] == minePosition) return;
        if (buffers[i][j] == nonePosition) return;
        if (buffers[i][j] == completeMineSweep) return;

        ArrayList<Integer> click_x = new ArrayList<>();
        ArrayList<Integer> click_y = new ArrayList<>();
        int currentBuffer = buffers[i][j];
        int remainPosition = 0;

        for (int x = i - 1; x < i + 2; x++){
            for (int y = j - 1; y < j + 2; y++) {
                if (x < 0 || x > 15) continue;
                if (y < 0 || y > 29) continue;
                if (x == i && y == j) continue;

                if (buffers[x][y] == nonePosition) {
                    remainPosition++;
                    click_x.add(x);
                    click_y.add(y);
                }
                if (buffers[x][y] == minePosition) {
                    remainPosition++;
                }
            }
        }

        if (click_x.size() != 0 && currentBuffer == remainPosition){
            // click right button of mouse

            for (int x = 0; x < click_x.size(); x++){
                buffers[click_x.get(x)][click_y.get(x)] = minePosition;
            }
        }

        if (currentBuffer == remainPosition){
            // click left and right button of mouse
        }
    }



    // check noneBlock and this pattern
    // return mine of position
    public void check121(int i, int j){
        if (buffers[i][j] != 1) return;

        ArrayList<Integer> i_list = new ArrayList<>();
        ArrayList<Integer> j_list = new ArrayList<>();
        int direction = 0;

        if (i <= 13 && buffers[i + 1][j] == 2){
            direction = downDirection;
            if (buffers[i + 2][j] == 1){
                i_list.add(i);
                i_list.add(i + 1);
                i_list.add(i + 2);
                j_list.add(j);
                j_list.add(j);
                j_list.add(j);

                if (checkCompleteBlock(i_list, j_list, direction) == 1){
                    if ((buffers[i][j + 1] == minePosition || buffers[i][j + 1] == nonePosition)
                            && (buffers[i + 2][j + 1] == minePosition || buffers[i + 2][j + 1] == nonePosition)){

                        // click mouse
                    }
                } else if (checkCompleteBlock(i_list, j_list, direction) == 2){
                    if ((buffers[i][j - 1] == minePosition || buffers[i][j - 1] == nonePosition)
                            && (buffers[i + 2][j - 1] == minePosition || buffers[i + 2][j - 1] == nonePosition)){

                        // click mouse
                    }
                }
            }
        } else if (j <= 27 && buffers[i][j + 1] == 2){
            direction = rightDirection;
            if (buffers[i][j + 2] == 1){
                i_list.add(i);
                i_list.add(i);
                i_list.add(i);
                j_list.add(j);
                j_list.add(j + 1);
                j_list.add(j + 2);

                if (checkCompleteBlock(i_list, j_list, direction) == 1){
                    if ((buffers[i + 1][j] == minePosition || buffers[i + 1][j] == nonePosition)
                            && (buffers[i + 1][j + 2] == minePosition || buffers[i + 1][j + 2] == nonePosition)){

                        // click mouse
                    }
                } else if (checkCompleteBlock(i_list, j_list, direction) == 2){
                    if ((buffers[i - 1][j] == minePosition || buffers[i - 1][j] == nonePosition)
                            && (buffers[i - 1][j + 2] == minePosition || buffers[i - 1][j + 2] == nonePosition)){

                        // click mouse
                    }
                }
            }
        }

        return;
    }
    public void check1221(int i, int j){
        if (buffers[i][j] != 1) return;

        ArrayList<Integer> i_list = new ArrayList<>();
        ArrayList<Integer> j_list = new ArrayList<>();
        int direction = 0;

        if (i <= 12 && buffers[i + 1][j] == 2 && buffers[i + 2][j] == 2){
            direction = downDirection;
            if (buffers[i + 3][j] == 1){
                i_list.add(i);
                i_list.add(i + 1);
                i_list.add(i + 2);
                i_list.add(i + 3);
                j_list.add(j);
                j_list.add(j);
                j_list.add(j);
                j_list.add(j);

                if (checkCompleteBlock(i_list, j_list, direction) == 1){
                    if ((buffers[i + 1][j + 1] == minePosition || buffers[i + 1][j + 1] == nonePosition)
                            && (buffers[i + 2][j + 1] == minePosition || buffers[i + 2][j + 1] == nonePosition)){

                        // click mouse
                    }
                } else if (checkCompleteBlock(i_list, j_list, direction) == 2){
                    if ((buffers[i + 1][j - 1] == minePosition || buffers[i + 1][j - 1] == nonePosition)
                            && (buffers[i + 2][j - 1] == minePosition || buffers[i + 2][j - 1] == nonePosition)){

                        // click mouse
                    }
                }
            }
        } else if (j <= 26 && buffers[i][j + 1] == 2 && buffers[i][j + 2] == 2){
            direction = rightDirection;
            if (buffers[i][j + 3] == 1){
                i_list.add(i);
                i_list.add(i);
                i_list.add(i);
                i_list.add(i);
                j_list.add(j);
                j_list.add(j + 1);
                j_list.add(j + 2);
                j_list.add(j + 3);

                if (checkCompleteBlock(i_list, j_list, direction) == 1){
                    if ((buffers[i + 1][j + 1] == minePosition || buffers[i + 1][j + 1] == nonePosition)
                            && (buffers[i + 1][j + 2] == minePosition || buffers[i + 1][j + 2] == nonePosition)){

                        // click mouse
                    }
                } else if (checkCompleteBlock(i_list, j_list, direction) == 2){
                    if ((buffers[i - 1][j + 1] == minePosition || buffers[i - 1][j + 1] == nonePosition)
                            && (buffers[i - 1][j + 2] == minePosition || buffers[i - 1][j + 2] == nonePosition)){

                        // click mouse
                    }
                }
            }
        }

        return;
    }
    public void check111(int i, int j){
        if (buffers[i][j] != 1) return;

        ArrayList<Integer> i_list = new ArrayList<>();
        ArrayList<Integer> j_list = new ArrayList<>();
        int direction = 0;

        if (i <= 13 && buffers[i + 1][j] == 1){
            direction = downDirection;
            if (buffers[i + 2][j] == 1){
                i_list.add(i);
                i_list.add(i + 1);
                i_list.add(i + 2);
                j_list.add(j);
                j_list.add(j);
                j_list.add(j);

                if (checkCompleteBlock(i_list, j_list, direction) == 1){

                } else if (checkCompleteBlock(i_list, j_list, direction) == 2){

                }
            }
        } else if (j <= 27 && buffers[i][j + 1] == 1){
            direction = rightDirection;
            if (buffers[i][j + 2] == 1){
                i_list.add(i);
                i_list.add(i);
                i_list.add(i);
                j_list.add(j);
                j_list.add(j + 1);
                j_list.add(j + 2);

                if (checkCompleteBlock(i_list, j_list, direction) == 1){

                } else if (checkCompleteBlock(i_list, j_list, direction) == 2){

                }
            }
        }

        return;
    }
    public void check1111(int i, int j){
        if (buffers[i][j] != 1) return;

        ArrayList<Integer> i_list = new ArrayList<>();
        ArrayList<Integer> j_list = new ArrayList<>();
        int direction = 0;

        if (i <= 12 && buffers[i + 1][j] == 2 && buffers[i + 2][j] == 2){
            direction = downDirection;
            if (buffers[i + 3][j] == 1){
                i_list.add(i);
                i_list.add(i + 1);
                i_list.add(i + 2);
                i_list.add(i + 3);
                j_list.add(j);
                j_list.add(j);
                j_list.add(j);
                j_list.add(j);

                if (checkCompleteBlock(i_list, j_list, direction) == 1){

                } else if (checkCompleteBlock(i_list, j_list, direction) == 2){

                }
            }
        } else if (j <= 26 && buffers[i][j + 1] == 2 && buffers[i][j + 2] == 2){
            direction = rightDirection;
            if (buffers[i][j + 3] == 1){
                i_list.add(i);
                i_list.add(i);
                i_list.add(i);
                i_list.add(i);
                j_list.add(j);
                j_list.add(j + 1);
                j_list.add(j + 2);
                j_list.add(j + 3);

                if (checkCompleteBlock(i_list, j_list, direction) == 1){
                    if ((buffers[i + 1][j + 1] == minePosition || buffers[i + 1][j + 1] == nonePosition)
                            && (buffers[i + 1][j + 2] == minePosition || buffers[i + 1][j + 2] == nonePosition)){

                        // click mouse
                    }
                } else if (checkCompleteBlock(i_list, j_list, direction) == 2){
                    if ((buffers[i - 1][j + 1] == minePosition || buffers[i - 1][j + 1] == nonePosition)
                            && (buffers[i - 1][j + 2] == minePosition || buffers[i - 1][j + 2] == nonePosition)){

                        // click mouse
                    }
                }
            }
        }

        return;
    }


    // return 0 : not exist completeblock list
    // return 1 : completeblock list exist at -1
    // return 2 : completeblock list exist at 1
    protected int checkCompleteBlock(ArrayList<Integer> i_list, ArrayList<Integer> j_list, int direction){
        int boundary = 0;

        if (direction == downDirection){
            for (int x = 0; x < i_list.size(); x++){
                if (x == 0){
                    if (buffers[i_list.get(x)][j_list.get(x) + 1] >= 0 && buffers[i_list.get(x)][j_list.get(x) + 1] < 4){
                        boundary = 1;
                    } else if (buffers[i_list.get(x)][j_list.get(x) - 1] >= 0 && buffers[i_list.get(x)][j_list.get(x) - 1] < 4){
                        boundary = -1;
                    }
                } else {
                    if (boundary == 0) break;
                    else if (boundary == 1){
                        if (buffers[i_list.get(x)][j_list.get(x) + 1] >= 0 && buffers[i_list.get(x)][j_list.get(x) + 1] < 4){

                        } else {
                            return 0;
                        }
                    } else {
                        if (buffers[i_list.get(x)][j_list.get(x) - 1] >= 0 && buffers[i_list.get(x)][j_list.get(x) - 1] < 4){

                        } else {
                            return 0;
                        }
                    }
                }
            }

            if (boundary == -1){
                return 1;
            } else if (boundary == 1) {
                return 2;
            }
        } else if (direction == rightDirection) {
            for (int y = 0; y < j_list.size(); y++){
                if (y == 0){
                    if (buffers[i_list.get(y) + 1][j_list.get(y)] >= 0 && buffers[i_list.get(y) + 1][j_list.get(y)] < 4){
                        boundary = 1;
                    } else if (buffers[i_list.get(y) - 1][j_list.get(y)] >= 0 && buffers[i_list.get(y) - 1][j_list.get(y)] < 4){
                        boundary = -1;
                    }
                } else {
                    if (boundary == 0) break;
                    else if (boundary == 1){
                        if (buffers[i_list.get(y) + 1][j_list.get(y)] >= 0 && buffers[i_list.get(y) + 1][j_list.get(y)] < 4){

                        } else {
                            return 0;
                        }
                    } else {
                        if (buffers[i_list.get(y) - 1][j_list.get(y)] >= 0 && buffers[i_list.get(y) - 1][j_list.get(y)] < 4){

                        } else {
                            return 0;
                        }
                    }
                }
            }

            if (boundary == -1){
                return 1;
            } else if (boundary == 1) {
                return 2;
            }
        }


        return 0;
    }
}
