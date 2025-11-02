package class095;

// 减法游戏 (Subtraction Game)
// 减法游戏是取石子游戏的一个通用变种，也称为Take-away Game
// 游戏规则：
// 1. 有一堆石子，数量为n
// 2. 玩家轮流从堆中取石子，每次可以取的石子数必须属于一个给定的集合S
// 3. 无法取石子的玩家输
// 
// 算法思路：
// 1. 使用动态规划计算每个石子数量对应的必胜态（winning position）和必败态（losing position）
// 2. dp[i] = true 表示当石子数为i时，当前玩家处于必胜态
// 3. 状态转移方程：dp[i] = 存在某个s∈S，使得 i >= s 且 dp[i-s] = false
// 4. 边界条件：dp[0] = false（没有石子时，当前玩家无法操作，必败）
// 
// 时间复杂度：O(n*k)，其中n是石子数量上限，k是集合S的大小
// 空间复杂度：O(n)，用于存储dp数组
//
// 适用场景和解题技巧：
// 1. 适用场景：
//    - 具有特定移动规则的取石子游戏
//    - 需要预处理所有可能状态的博弈问题
//    - 可以作为其他复杂博弈问题的子问题
// 2. 解题技巧：
//    - 识别问题是否符合减法游戏模型
//    - 确定允许的移动集合S
//    - 通过动态规划预处理所有可能的状态
// 3. 变种和扩展：
//    - 标准巴什博弈是减法游戏的特例，其中S = {1, 2, ..., m}
//    - 可以扩展到多堆石子的情况，结合SG函数进行分析

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class Code11_SubtractionGame {
    
    /**
     * 计算减法游戏中每个石子数量对应的胜负状态
     * @param maxN 最大石子数量
     * @param moves 允许的移动集合，表示每次可以取的石子数
     * @return 一个布尔数组，dp[i]表示石子数为i时是否为必胜态
     */
    public static boolean[] calculateWinningPositions(int maxN, int[] moves) {
        // 参数校验
        if (maxN < 0) {
            throw new IllegalArgumentException("最大石子数量不能为负数");
        }
        if (moves == null || moves.length == 0) {
            throw new IllegalArgumentException("移动集合不能为空");
        }
        
        // 确保移动集合中的元素都是正整数且不重复
        Set<Integer> moveSet = new HashSet<>();
        for (int move : moves) {
            if (move <= 0) {
                throw new IllegalArgumentException("移动集合中的元素必须为正整数");
            }
            moveSet.add(move);
        }
        
        // 转换为数组以便排序
        int[] sortedMoves = new int[moveSet.size()];
        int index = 0;
        for (int move : moveSet) {
            sortedMoves[index++] = move;
        }
        Arrays.sort(sortedMoves); // 排序以优化性能
        
        // 初始化dp数组
        boolean[] dp = new boolean[maxN + 1];
        dp[0] = false; // 边界条件：0个石子时必败
        
        // 动态规划计算每个状态
        for (int i = 1; i <= maxN; i++) {
            boolean canWin = false;
            // 尝试所有可能的移动
            for (int move : sortedMoves) {
                if (move > i) {
                    // 当前移动需要的石子数超过了现有石子数，无法进行
                    break; // 由于已排序，可以提前退出
                }
                // 如果存在某个移动，使得对手处于必败态，则当前状态为必胜态
                if (!dp[i - move]) {
                    canWin = true;
                    break; // 找到一个必胜策略即可退出
                }
            }
            dp[i] = canWin;
        }
        
        return dp;
    }
    
    /**
     * 判断在给定石子数和移动集合的情况下，当前玩家是否有必胜策略
     * @param n 当前石子数量
     * @param moves 允许的移动集合
     * @return 如果当前玩家有必胜策略，返回true；否则返回false
     */
    public static boolean canWin(int n, int[] moves) {
        // 参数校验
        if (n < 0) {
            throw new IllegalArgumentException("石子数量不能为负数");
        }
        
        // 计算胜负状态
        boolean[] dp = calculateWinningPositions(n, moves);
        return dp[n];
    }
    
    /**
     * 寻找当前状态下的必胜策略
     * @param n 当前石子数量
     * @param moves 允许的移动集合
     * @return 如果存在必胜策略，返回一个可以取的石子数；否则返回-1
     */
    public static int findWinningMove(int n, int[] moves) {
        // 参数校验
        if (n < 0) {
            throw new IllegalArgumentException("石子数量不能为负数");
        }
        if (moves == null || moves.length == 0) {
            throw new IllegalArgumentException("移动集合不能为空");
        }
        
        // 确保移动集合中的元素都是正整数且不重复
        Set<Integer> moveSet = new HashSet<>();
        for (int move : moves) {
            if (move > 0) {
                moveSet.add(move);
            }
        }
        
        // 尝试所有可能的移动
        for (int move : moveSet) {
            if (move <= n) {
                // 检查取走move个石子后，对手是否处于必败态
                boolean[] dp = calculateWinningPositions(n - move, moves);
                if (!dp[n - move]) {
                    return move; // 找到一个必胜策略
                }
            }
        }
        
        return -1; // 不存在必胜策略
    }
    
    /**
     * 计算SG函数值
     * @param maxN 最大石子数量
     * @param moves 允许的移动集合
     * @return 一个整数数组，sg[i]表示石子数为i时的SG函数值
     */
    public static int[] calculateSG(int maxN, int[] moves) {
        // 参数校验
        if (maxN < 0) {
            throw new IllegalArgumentException("最大石子数量不能为负数");
        }
        if (moves == null || moves.length == 0) {
            throw new IllegalArgumentException("移动集合不能为空");
        }
        
        // 确保移动集合中的元素都是正整数且不重复
        Set<Integer> moveSet = new HashSet<>();
        for (int move : moves) {
            if (move > 0) {
                moveSet.add(move);
            }
        }
        
        // 转换为数组以便排序
        int[] sortedMoves = new int[moveSet.size()];
        int index = 0;
        for (int move : moveSet) {
            sortedMoves[index++] = move;
        }
        Arrays.sort(sortedMoves);
        
        // 初始化SG数组
        int[] sg = new int[maxN + 1];
        sg[0] = 0; // 边界条件：0个石子时SG值为0
        
        // 计算每个状态的SG值
        for (int i = 1; i <= maxN; i++) {
            Set<Integer> reachableSG = new HashSet<>();
            // 收集所有可达状态的SG值
            for (int move : sortedMoves) {
                if (move <= i) {
                    reachableSG.add(sg[i - move]);
                }
            }
            // 找到最小的未出现的非负整数
            int mex = 0; // mex表示最小非负整数
            while (reachableSG.contains(mex)) {
                mex++;
            }
            sg[i] = mex;
        }
        
        return sg;
    }
    
    /**
     * 打印胜负状态表
     * @param dp 胜负状态数组
     */
    public static void printWinningTable(boolean[] dp) {
        System.out.println("石子数\t状态");
        System.out.println("----\t----");
        for (int i = 0; i < dp.length; i++) {
            System.out.println(i + "\t" + (dp[i] ? "必胜态" : "必败态"));
        }
    }
    
    /**
     * 打印SG函数值表
     * @param sg SG函数值数组
     */
    public static void printSGTable(int[] sg) {
        System.out.println("石子数\tSG值");
        System.out.println("----\t----");
        for (int i = 0; i < sg.length; i++) {
            System.out.println(i + "\t" + sg[i]);
        }
    }
    
    /**
     * 测试减法游戏
     */
    public static void main(String[] args) {
        // 测试用例1：标准巴什博弈，每次可以取1-3个石子
        System.out.println("测试用例1：标准巴什博弈（每次取1-3个石子）");
        int[] moves1 = {1, 2, 3};
        int maxN1 = 10;
        boolean[] dp1 = calculateWinningPositions(maxN1, moves1);
        printWinningTable(dp1);
        
        // 测试特定石子数的胜负状态
        int n1 = 4;
        System.out.println("\n石子数为" + n1 + "时，" + 
                         (canWin(n1, moves1) ? "先手必胜" : "先手必败"));
        int winningMove1 = findWinningMove(n1, moves1);
        if (winningMove1 != -1) {
            System.out.println("必胜策略：取" + winningMove1 + "个石子");
        } else {
            System.out.println("无必胜策略");
        }
        
        // 计算SG函数值
        int[] sg1 = calculateSG(maxN1, moves1);
        System.out.println("\nSG函数值表：");
        printSGTable(sg1);
        
        // 测试用例2：只能取奇数个石子
        System.out.println("\n\n测试用例2：只能取1、3、5个石子");
        int[] moves2 = {1, 3, 5};
        int maxN2 = 10;
        boolean[] dp2 = calculateWinningPositions(maxN2, moves2);
        printWinningTable(dp2);
        
        // 测试用例3：只能取2的幂次方个石子
        System.out.println("\n\n测试用例3：只能取1、2、4、8个石子（2的幂次方）");
        int[] moves3 = {1, 2, 4, 8};
        int maxN3 = 10;
        boolean[] dp3 = calculateWinningPositions(maxN3, moves3);
        printWinningTable(dp3);
    }
}