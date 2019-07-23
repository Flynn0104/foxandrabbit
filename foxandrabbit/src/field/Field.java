package field;

import java.util.ArrayList;

import cell.Cell;

/**Field是Cell数据存储容器，用于存放Cell；
 * Field同时执行管理它所存放的Cell数据的增删改查；
 * Field清楚每一个Cell的位置，而Cell自己不知道
 */
public class Field {
    // 创建九宫格位置对象，用于记录每一个Cell周围其它Cell的位置
    private static final Location[] adjacent = {
            new Location(-1, -1), new Location(-1, 0), new Location(-1, 1),
            new Location(0, -1), new Location(0, 0), new Location(0, 1),
            new Location(1, -1), new Location(1, 0), new Location(1, 1)
    };
    // Field内部采用Cell二维数组存储Cell
    private int width;
    private int height;
    private Cell[][] field;
    // 构造函数进行初始化操作
    public Field(int width, int height) {
        this.width = width;
        this.height = height;
        field = new Cell[height][width];
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
    // 往固定位置放一个Cell，返回被替代的Cell
    public Cell place(int row, int col, Cell o) {
        Cell ret = field[row][col];
        field[row][col] = o;
        return ret;
    }

    public Cell get(int row, int col) {
        return field[row][col];
    }
    // 得到指定位置Cell的8个邻居
    public Cell[] getNeighbour(int row, int col) {
        ArrayList<Cell> list = new ArrayList<Cell>();
        for (int i = -1; i < 2; i++) {
            for (int j = -1; j < 2; j++) {
                int r = row + i;
                int c = col + j;
                if (r > -1 && r < height && c > -1 && c < width && !(r == row && c == col)) {
                    list.add(field[r][c]);
                }
            }
        }
        return list.toArray(new Cell[list.size()]);
    }
    // 获取邻居中的空闲位置
    public Location[] getFreeNeighbour(int row, int col) {
        ArrayList<Location> list = new ArrayList<Location>();
        for (Location loc : adjacent) {
            int r = row + loc.getRow();
            int c = col + loc.getCol();
            if (r > -1 && r < height && c > -1 && c < width && field[r][c] == null) {
                list.add(new Location(r, c));
            }
        }
        return list.toArray(new Location[list.size()]);
    }
    // 随机往空闲位置放置一个Cell，成功返回true
    public boolean placeRandomAdj(int row, int col, Cell cell) {
        boolean ret = false;
        Location[] freeAdj = getFreeNeighbour(row, col);
        if (freeAdj.length > 0) {
            int idx = (int) (Math.random() * freeAdj.length);
            field[freeAdj[idx].getRow()][freeAdj[idx].getCol()] = cell;
            ret = true;
        }
        return ret;
    }

    public Cell remove(int row, int col) {
        Cell ret = field[row][col];
        field[row][col] = null;
        return ret;
    }
    // 删除某一个Cell，比较的是地址值
    public void remove(Cell cell) {
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                if (field[row][col] == cell) {
                    field[row][col] = null;
                    break;
                }
            }
        }
    }
    // 清空
    public void clear() {
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                field[i][j] = null;
            }
        }
    }
    // 移动
    public void move(int row, int col, Location loc) {
        field[loc.getRow()][loc.getCol()] = field[row][col];
        remove(row, col);
    }
}
