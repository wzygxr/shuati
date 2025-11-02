package class095;

// SG函数 (Sprague-Grundy定理) 实现
// 公平组合游戏(Impartial Game)的通用解法
// 任何公平组合游戏都可以转化为尼姆堆，通过计算每个子游戏的SG值
// 然后将这些SG值异或起来，若结果非零则先手必胜，否则必败
// 
// 算法思路：
// 1. SG函数是对游戏状态的一种抽象表示
// 2. 对于每个状态x，SG(x) = mex{ SG(y) | y是x的后继状态 }
// 3. mex(最小非负整数)函数返回不属于集合中的最小非负整数
// 4. Sprague-Grundy定理：多个独立的子游戏的组合的SG值等于各子游戏SG值的异或和
// 5. 当且仅当组合游戏的SG值不为0时，当前玩家处于必胜态
// 
// 时间复杂度：O(n * m) - n是状态数，m是每个状态的后继状态数
// 空间复杂度：O(n) - 存储SG值的数组
//
// 适用场景和解题技巧：
// 1. 适用场景：
//    - 公平组合游戏（双方可执行相同操作，游戏状态无差别）
//    - 有确定的终止状态
//    - 每个状态可以转移到有限个其他状态
// 2. 解题技巧：
//    - 确定游戏的状态表示方法
//    - 找出每个状态的所有可能转移
//    - 自底向上计算SG函数值
//    - 利用异或和判断胜负
// 3. 经典应用：
//    - 取石子游戏的变种
//    - 棋盘游戏
//    - 图游戏
//
// 相关题目链接：
// 1. 洛谷 P2197: https://www.luogu.com.cn/problem/P2197
// 2. HDU 1850: http://acm.hdu.edu.cn/showproblem.php?pid=1850
// 3. POJ 2234: http://poj.org/problem?id=2234

public class Code07_SGFunction {
    
    // 最大状态数
    public static int MAXN = 1001;
    
    // 存储SG函数值
    public static int[] sg = new int[MAXN];
    
    // 标记数组，用于计算mex
    public static boolean[] visited = new boolean[MAXN];
    
    // 预处理SG函数值
    // 参数说明：
    // - n: 最大状态数
    // - moves: 可以进行的移动（比如每次可以取1,2,3个石子）
    public static void precomputeSG(int n, int[] moves) {
        // 初始化SG数组
        for (int i = 0; i <= n; i++) {
            // 标记所有后继状态的SG值
            for (int j = 0; j <= n; j++) {
                visited[j] = false;
            }
            
            // 遍历所有可能的移动
            for (int move : moves) {
                if (i >= move) {
                    visited[sg[i - move]] = true;
                }
            }
            
            // 计算mex值
            int mex = 0;
            while (visited[mex]) {
                mex++;
            }
            sg[i] = mex;
        }
    }
    
    // 判断当前玩家是否必胜
    // 参数说明：
    // - piles: 各堆石子的数量（或各个子游戏的状态）
    // - moves: 可以进行的移动
    public static boolean isWinningPosition(int[] piles, int[] moves) {
        // 预处理SG函数值到最大堆的大小
        int maxPile = 0;
        for (int pile : piles) {
            maxPile = Math.max(maxPile, pile);
        }
        precomputeSG(maxPile, moves);
        
        // 计算所有堆的SG值异或和
        int xorSum = 0;
        for (int pile : piles) {
            xorSum ^= sg[pile];
        }
        
        // 异或和不为0则先手必胜
        return xorSum != 0;
    }
    
    // 示例：取石子游戏变种 - 每次可以取1、2、4个石子
    // 测试方法
    public static void main(String[] args) {
        // 测试用例1: 巴什博弈变种 - 每次可以取1、2、4个石子
        int[] moves1 = {1, 2, 4};
        int[] piles1 = {5, 7, 9};
        System.out.println("测试用例1 - 取石子游戏变种（每次取1、2、4个）:");
        System.out.println("各堆石子数: [5, 7, 9]");
        System.out.println("先手是否必胜: " + (isWinningPosition(piles1, moves1) ? "是" : "否"));
        
        // 测试用例2: 标准巴什博弈 - 每次可以取1-3个石子
        int[] moves2 = {1, 2, 3};
        int[] piles2 = {4, 4, 4};
        System.out.println("\n测试用例2 - 标准巴什博弈（每次取1-3个）:");
        System.out.println("各堆石子数: [4, 4, 4]");
        System.out.println("先手是否必胜: " + (isWinningPosition(piles2, moves2) ? "是" : "否"));
        
        // 测试用例3: 斐波那契游戏的SG函数分析
        int[] moves3 = {1, 2}; // 简化版本，实际斐波那契游戏规则更复杂
        int[] piles3 = {5};  // 5是斐波那契数，应该是必败态
        System.out.println("\n测试用例3 - 斐波那契游戏简化版:");
        System.out.println("石子数: [5]");
        System.out.println("先手是否必胜: " + (isWinningPosition(piles3, moves3) ? "是" : "否"));
    }
}