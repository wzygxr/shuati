import java.util.*;

// N皇后问题
// 测试链接 : https://leetcode.cn/problems/n-queens-ii/
public class NQueens {

    // 用数组表示路径实现的N皇后问题，不推荐
    // 时间复杂度: O(N!)，因为对于每个皇后，我们尝试N列，然后(N-1)列，以此类推
    // 空间复杂度: O(N)，递归栈深度为N，path数组大小为N
    public static int totalNQueens1(int n) {
        // 输入校验：n必须为正整数
        if (n < 1) {
            return 0;
        }
        // 创建一个数组path，用来记录每一行皇后所在的列位置
        // path[i]表示第i行的皇后放在了第path[i]列
        return f1(0, new int[n], n);
    }

    // 递归函数：在第i行放置皇后
    // i : 当前来到的行
    // path : 0...i-1行的皇后，都摆在了哪些列
    // n : 一共有多少行
    public static int f1(int i, int[] path, int n) {
        // 递归终止条件：如果已经处理完所有行，说明找到了一个有效解
        if (i == n) {
            return 1;
        }
        int res = 0;
        // 尝试当前行i的所有列
        for (int j = 0; j < n; j++) {
            // 检查在第i行第j列放置皇后是否有效（不与之前放置的皇后冲突）
            if (isValid(path, i, j)) {
                // 记录第i行皇后放在第j列
                path[i] = j;
                // 递归处理下一行，并累加返回的结果
                res += f1(i + 1, path, n);
            }
        }
        return res;
    }

    // 检查在i行j列放置皇后是否有效
    // path: 前i行皇后的列位置
    // i: 当前行
    // j: 当前列
    public static boolean isValid(int[] path, int i, int j) {
        // 检查前面已经放置的皇后是否与当前位置冲突
        for (int k = 0; k < i; k++) {
            // 冲突条件：
            // 1. 同列：path[k] == j
            // 2. 同对角线：行差的绝对值 == 列差的绝对值，即Math.abs(path[k] - j) == Math.abs(k - i)
            if (path[k] == j || Math.abs(path[k] - j) == Math.abs(k - i)) {
                return false;
            }
        }
        return true;
    }

    // 使用位运算优化的N皇后问题
    // 时间复杂度: O(N!)，但常数项更小
    // 空间复杂度: O(N)，递归栈深度为N
    public static int totalNQueens2(int n) {
        // 输入校验：n必须为正整数且不超过32（int的位数）
        if (n < 1 || n > 32) {
            return 0;
        }
        // 限制在n位内，例如n=4时，limit=1111(二进制)
        int limit = n == 32 ? -1 : (1 << n) - 1;
        // 调用递归函数求解
        return f2(limit, 0, 0, 0);
    }

    // 位运算递归函数
    // limit : 限制位，表示棋盘大小
    // colLim : 列限制，表示哪些列已被占用
    // leftDiaLim : 左对角线限制
    // rightDiaLim : 右对角线限制
    public static int f2(int limit, int colLim, int leftDiaLim, int rightDiaLim) {
        // 递归终止条件：所有列都放置了皇后
        if (colLim == limit) {
            return 1;
        }
        // 当前可以放置的位置
        // colLim | leftDiaLim | rightDiaLim 表示所有不能放置皇后的位置
        // ~(colLim | leftDiaLim | rightDiaLim) 表示所有可以放置皇后的位置（可能超出棋盘范围）
        // limit & (~(colLim | leftDiaLim | rightDiaLim)) 表示在棋盘范围内可以放置皇后的位置
        int pos = limit & (~(colLim | leftDiaLim | rightDiaLim));
        int res = 0;
        // 遍历所有可以放置皇后的位置
        while (pos != 0) {
            // 取最右边的1，表示选择在该位置放置皇后
            // pos & (~pos + 1) 等价于 pos & -pos
            int mostRightOne = pos & (~pos + 1);
            // 从pos中移除mostRightOne位置的1
            pos -= mostRightOne;
            // 递归处理下一行
            // colLim | mostRightOne：更新列的占用情况
            // (leftDiaLim | mostRightOne) << 1：更新右上->左下对角线的占用情况
            // (rightDiaLim | mostRightOne) >>> 1：更新左上->右下对角线的占用情况
            res += f2(limit, 
                     colLim | mostRightOne,
                     (leftDiaLim | mostRightOne) << 1,
                     (rightDiaLim | mostRightOne) >>> 1);
        }
        return res;
    }

    public static void main(String[] args) {
        System.out.println("测试开始");
        
        // 测试14皇后问题
        int n = 14;
        System.out.println("解决" + n + "皇后问题");
        
        long start1 = System.currentTimeMillis();
        int ans1 = totalNQueens1(n);
        long end1 = System.currentTimeMillis();
        System.out.println("方法1答案 : " + ans1);
        System.out.println("方法1运行时间 : " + (end1 - start1) + " 毫秒");
        
        long start2 = System.currentTimeMillis();
        int ans2 = totalNQueens2(n);
        long end2 = System.currentTimeMillis();
        System.out.println("方法2答案 : " + ans2);
        System.out.println("方法2运行时间 : " + (end2 - start2) + " 毫秒");
        
        System.out.println("测试结束");
        System.out.println("只有位运算的版本，才能10秒内跑完16皇后问题的求解过程");
    }
}