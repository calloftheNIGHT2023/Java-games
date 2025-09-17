import java.io.BufferedReader;
import java.io.InputStreamReader;
public final class Main {
    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        System.out.println("Welcome to Slide Puzzle！");
        int rows = promptInt(br, "LINE(>=2): ", 2, 50);
        int cols = promptInt(br, "ROW(>=2): ", 2, 50);
        int steps = promptInt(br, "disrupt steps ): ", 1, 10000);

        PuzzleBoard board = new PuzzleBoard(rows, cols);
        board.shuffle(steps, System.nanoTime());

        System.out.println("棋盘大小: " + rows + "x" + cols);
        System.out.println("输入空格相邻的数字来交换；H=帮助；Q=退出。");
        board.printBoard();

        while (true) {
            System.out.print(" > ");
            String line = br.readLine();
            if (line == null) break;
            line = line.trim();
            if (line.isEmpty()) continue;

            char ch = Character.toUpperCase(line.charAt(0));
            if (ch == 'Q') break;
            if (ch == 'H') { printHelp(); continue; }

            int val;
            try {
                val = Integer.parseInt(line);
            } catch (NumberFormatException e) {
                System.out.println("error"); // 非数字
                continue;
            }

            try {
                board.slide(val);
                board.printBoard();
                if (board.isSolved()) {
                    System.out.println("Solved!");
                    break;
                }
            } catch (IllegalArgumentException e) {
                System.out.println("error");
            }
        }
    }

    private static int promptInt(BufferedReader br, String msg, int min, int max) throws Exception {
        while (true) {
            System.out.print(msg);
            String s = br.readLine();
            if (s == null) throw new RuntimeException("输入已关闭");
            try {
                int v = Integer.parseInt(s.trim());
                if (v < min || v > max) {
                    System.out.println("请输入 " + min + " 到 " + max + " 之间的整数");
                    continue;
                }
                return v;
            } catch (NumberFormatException e) {
                System.out.println("Invalid Integer");
            }
        }
    }

    private static void printHelp() {
        System.out.println("直接输入空格相邻的数字（例如 7）即可把该数字滑入空格。");
        System.out.println("If it's invalid or non-relate，print error");
        System.out.println("Q to exit, H to help");
    }
}
