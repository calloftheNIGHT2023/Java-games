import java.util.Random;
public final class PuzzleBoard implements Board {
    private final int rows;
    private final int cols;
    private final Tile[] tiles;   // row-major；0 为空白
    private int blankIndex;       // 空白所在索引
    public PuzzleBoard(int rows, int cols) {
        if (rows < 2 || cols < 2) throw new IllegalArgumentException("min size 2x2");
        this.rows = rows;
        this.cols = cols;
        this.tiles = new Tile[rows * cols];
        for (int i = 0; i < tiles.length - 1; i++) tiles[i] = new Tile(i + 1);
        tiles[tiles.length - 1] = new Tile(0);
        blankIndex = tiles.length - 1;
    }

    //Board 接口
    @Override public int rows() { return rows; }
    @Override public int cols() { return cols; }
    @Override public boolean isSolved() {
        for (int i = 0; i < tiles.length - 1; i++) if (tiles[i].value() != i + 1) return false;
        return tiles[tiles.length - 1].value() == 0;
    }
    @Override public int[] asArray() {
        int[] a = new int[tiles.length];
        for (int i = 0; i < tiles.length; i++) a[i] = tiles[i].value();
        return a;
    }

    /** 打乱：从目标状态对空格执行若干合法随机移动，保证可解 */
    public void shuffle(int steps, long seed) {
        if (steps < 0) steps = rows * cols * 20;
        Random rnd = new Random(seed);
        for (int i = 0; i < steps; i++) {
            int r = blankIndex / cols, c = blankIndex % cols;
            int[] neigh = new int[4];
            int k = 0;
            if (r > 0) neigh[k++] = toIndex(r - 1, c);
            if (r < rows - 1) neigh[k++] = toIndex(r + 1, c);
            if (c > 0) neigh[k++] = toIndex(r, c - 1);
            if (c < cols - 1) neigh[k++] = toIndex(r, c + 1);
            int pick = neigh[rnd.nextInt(k)];
            swap(blankIndex, pick);
            blankIndex = pick;
        }
    }

    /** 判断值为 v 的格子是否能滑入空格（必须与空格相邻） */
    public boolean canSlide(int v) {
        if (v <= 0 || v >= tiles.length) return false; // 非法/含空白
        int idx = indexOf(v);
        return idx >= 0 && isNeighbor(idx, blankIndex);
    }

    /** 执行滑动：非法时抛 IllegalArgumentException("error")，上层捕获并保持状态不变 */
    public void slide(int v) {
        if (!canSlide(v)) throw new IllegalArgumentException("error");
        int idx = indexOf(v);
        swap(idx, blankIndex);
        blankIndex = idx;
    }

    /** 控制台打印棋盘（空白用 "_"） */
    public void printBoard() {
        for (int r = 0; r < rows; r++) {
            StringBuilder sb = new StringBuilder();
            for (int c = 0; c < cols; c++) {
                int val = tiles[toIndex(r, c)].value();
                sb.append(String.format("%2s ", val == 0 ? "_" : Integer.toString(val)));
            }
            System.out.println(sb.toString());
        }
    }

    // ---- 私有工具 ----
    private int indexOf(int v) {
        for (int i = 0; i < tiles.length; i++) if (tiles[i].value() == v) return i;
        return -1;
    }
    private boolean isNeighbor(int a, int b) {
        int ar = a / cols, ac = a % cols;
        int br = b / cols, bc = b % cols;
        return (ar == br && Math.abs(ac - bc) == 1) || (ac == bc && Math.abs(ar - br) == 1);
    }
    private int toIndex(int r, int c) { return r * cols + c; }
    private void swap(int i, int j) {
        Tile t = tiles[i]; tiles[i] = tiles[j]; tiles[j] = t;
    }
}

