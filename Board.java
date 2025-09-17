/** 通用棋盘抽象（便于后续扩展到其他回合制棋盘游戏） */
public interface Board {
    int rows();
    int cols();
    boolean isSolved();
    int[] asArray(); // row-major 复制
}
