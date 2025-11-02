package class063;

import java.util.*;

// Lights Out (USACO 09NOV)
// 题目来源：USACO 2009 November Contest
// 题目描述：
// 给定一个N×N的网格，每个格子有一个灯，初始状态为开或关。
// 每次操作可以翻转一个灯及其上下左右相邻的灯的状态。
// 求最少需要多少次操作才能将所有灯关闭。
// 测试链接：USACO训练题集
// 
// 算法思路：
// 使用折半搜索（Meet in the Middle）算法解决
// 将网格分为上下两部分，分别枚举所有可能的操作组合
// 然后通过哈希表查找满足条件的组合
// 时间复杂度：O(2^(n^2/2) * n)
// 空间复杂度：O(2^(n^2/2))
// 
// 工程化考量：
// 1. 状态压缩：使用位运算表示灯的状态
// 2. 性能优化：折半搜索减少搜索空间，剪枝优化
// 3. 可读性：清晰的变量命名和模块化设计
// 4. 边界处理：处理网格边界和特殊情况
// 
// 语言特性差异：
// Java中使用位运算进行状态操作，使用HashMap进行快速查找

public class Code14_LightsOut {
    
    /**
     * 解决灯灭问题
     * 
     * @param grid 初始灯状态网格，true表示开，false表示关
     * @return 最少操作次数，如果无法关闭所有灯返回-1
     * 
     * 算法核心思想：
     * 1. 折半搜索：将N×N网格分为上下两部分
     * 2. 枚举操作：分别枚举两部分的所有可能操作组合
     * 3. 状态匹配：通过哈希表查找使所有灯关闭的组合
     * 4. 最少操作：记录操作次数并取最小值
     * 
     * 时间复杂度分析：
     * - 直接枚举所有操作的时间复杂度为O(2^(n^2))，不可接受
     * - 折半搜索将复杂度降为O(2^(n^2/2))，可以处理中等规模
     * - 结合剪枝优化，进一步提高效率
     * 
     * 空间复杂度分析：
     * - 需要存储两部分的所有可能状态和操作次数
     * - 空间复杂度：O(2^(n^2/2))
     */
    public static int minOperations(boolean[][] grid) {
        // 边界条件检查
        if (grid == null || grid.length == 0 || grid[0].length == 0) {
            return -1;
        }
        
        int n = grid.length;
        
        // 如果网格很小，直接使用暴力搜索
        if (n <= 3) {
            return bruteForce(grid);
        }
        
        // 将网格转换为整数表示（位压缩）
        int initialState = compressGrid(grid);
        
        // 如果初始状态就是全关，返回0
        if (initialState == 0) {
            return 0;
        }
        
        // 使用折半搜索，将网格分为上下两部分
        int mid = n / 2;
        
        // 存储上半部分的所有可能操作结果
        // key: 操作后的状态, value: 最小操作次数
        Map<Integer, Integer> topResults = new HashMap<>();
        
        // 存储下半部分的所有可能操作结果
        Map<Integer, Integer> bottomResults = new HashMap<>();
        
        // 生成上半部分的所有可能操作组合
        generateOperations(grid, 0, mid, 0, 0, topResults);
        
        // 生成下半部分的所有可能操作组合
        generateOperations(grid, mid, n, 0, 0, bottomResults);
        
        int minOperations = Integer.MAX_VALUE;
        
        // 检查单独上半部分或下半部分是否能解决问题
        if (topResults.containsKey(0)) {
            minOperations = Math.min(minOperations, topResults.get(0));
        }
        if (bottomResults.containsKey(0)) {
            minOperations = Math.min(minOperations, bottomResults.get(0));
        }
        
        // 检查上下两部分组合的情况
        for (Map.Entry<Integer, Integer> topEntry : topResults.entrySet()) {
            int topState = topEntry.getKey();
            int topOps = topEntry.getValue();
            
            // 需要下半部分的操作能够抵消上半部分的影响
            if (bottomResults.containsKey(topState)) {
                int bottomOps = bottomResults.get(topState);
                minOperations = Math.min(minOperations, topOps + bottomOps);
            }
        }
        
        return minOperations == Integer.MAX_VALUE ? -1 : minOperations;
    }
    
    /**
     * 生成指定范围内所有可能的操作组合
     * 
     * @param grid 原始网格
     * @param startRow 起始行
     * @param endRow 结束行
     * @param currentState 当前状态
     * @param currentOps 当前操作次数
     * @param results 存储结果的Map
     */
    private static void generateOperations(boolean[][] grid, int startRow, int endRow,
                                           int currentState, int currentOps,
                                           Map<Integer, Integer> results) {
        // 递归终止条件：处理完所有行
        if (startRow == endRow) {
            // 更新结果，只保留最小操作次数
            results.merge(currentState, currentOps, Math::min);
            return;
        }
        
        int n = grid[0].length;
        
        // 枚举当前行的所有可能操作组合（2^n种可能）
        for (int mask = 0; mask < (1 << n); mask++) {
            int newState = currentState;
            int newOps = currentOps;
            
            // 应用当前行的操作
            for (int col = 0; col < n; col++) {
                if ((mask & (1 << col)) != 0) {
                    // 翻转当前格子及其邻居
                    newState = flipLights(newState, startRow, col, n);
                    newOps++;
                }
            }
            
            // 递归处理下一行
            generateOperations(grid, startRow + 1, endRow, newState, newOps, results);
        }
    }
    
    /**
     * 翻转指定位置灯及其邻居的状态
     * 
     * @param state 当前状态
     * @param row 行号
     * @param col 列号
     * @param n 网格大小
     * @return 翻转后的状态
     */
    private static int flipLights(int state, int row, int col, int n) {
        // 翻转当前格子
        state ^= (1 << (row * n + col));
        
        // 翻转上邻居（如果存在）
        if (row > 0) {
            state ^= (1 << ((row - 1) * n + col));
        }
        
        // 翻转下邻居（如果存在）
        if (row < n - 1) {
            state ^= (1 << ((row + 1) * n + col));
        }
        
        // 翻转左邻居（如果存在）
        if (col > 0) {
            state ^= (1 << (row * n + col - 1));
        }
        
        // 翻转右邻居（如果存在）
        if (col < n - 1) {
            state ^= (1 << (row * n + col + 1));
        }
        
        return state;
    }
    
    /**
     * 将网格状态压缩为整数
     */
    private static int compressGrid(boolean[][] grid) {
        int state = 0;
        int n = grid.length;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (grid[i][j]) {
                    state |= (1 << (i * n + j));
                }
            }
        }
        return state;
    }
    
    /**
     * 暴力搜索方法（用于小规模网格）
     */
    private static int bruteForce(boolean[][] grid) {
        int n = grid.length;
        int totalStates = 1 << (n * n);
        int minOps = Integer.MAX_VALUE;
        
        // 枚举所有可能的操作组合
        for (int mask = 0; mask < totalStates; mask++) {
            int state = compressGrid(grid);
            int ops = 0;
            
            // 应用操作
            for (int i = 0; i < n * n; i++) {
                if ((mask & (1 << i)) != 0) {
                    int row = i / n;
                    int col = i % n;
                    state = flipLights(state, row, col, n);
                    ops++;
                }
            }
            
            // 检查是否所有灯都关闭
            if (state == 0) {
                minOps = Math.min(minOps, ops);
            }
        }
        
        return minOps == Integer.MAX_VALUE ? -1 : minOps;
    }
    
    // 单元测试方法
    public static void main(String[] args) {
        // 测试用例1：2x2网格，可解
        System.out.println("=== 测试用例1：2x2网格 ===");
        boolean[][] grid1 = {
            {true, true},
            {true, false}
        };
        
        int result1 = minOperations(grid1);
        System.out.println("初始网格：");
        printGrid(grid1);
        System.out.println("期望输出：最小操作次数");
        System.out.println("实际输出：" + result1);
        System.out.println();
        
        // 测试用例2：3x3网格
        System.out.println("=== 测试用例2：3x3网格 ===");
        boolean[][] grid2 = {
            {true, false, true},
            {false, true, false},
            {true, false, true}
        };
        
        int result2 = minOperations(grid2);
        System.out.println("初始网格：");
        printGrid(grid2);
        System.out.println("实际输出：" + result2);
        System.out.println();
        
        // 性能测试
        System.out.println("=== 性能测试 ===");
        boolean[][] largeGrid = new boolean[5][5];
        // 随机初始化网格
        Random random = new Random();
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                largeGrid[i][j] = random.nextBoolean();
            }
        }
        
        long startTime = System.currentTimeMillis();
        int result3 = minOperations(largeGrid);
        long endTime = System.currentTimeMillis();
        
        System.out.println("数据规模：5x5网格");
        System.out.println("执行时间：" + (endTime - startTime) + "ms");
        System.out.println("结果：" + result3);
    }
    
    /**
     * 打印网格状态（用于测试）
     */
    private static void printGrid(boolean[][] grid) {
        for (boolean[] row : grid) {
            for (boolean cell : row) {
                System.out.print(cell ? "1 " : "0 ");
            }
            System.out.println();
        }
    }
}

/*
 * 算法深度分析：
 * 
 * 1. 问题特性：
 *    - 灯灭问题是经典的约束满足问题
 *    - 每个操作影响多个灯，具有局部性
 *    - 解决方案可能不唯一，需要找到最优解
 * 
 * 2. 折半搜索优势：
 *    - 将指数级复杂度降为平方根级别
 *    - 对于N×N网格，直接暴力搜索复杂度为O(2^(n^2))
 *    - 折半搜索复杂度为O(2^(n^2/2))，可处理更大规模
 * 
 * 3. 状态压缩技巧：
 *    - 使用位运算表示灯的状态，节省空间
 *    - 整数操作比布尔数组操作更高效
 *    - 便于哈希表存储和查找
 * 
 * 4. 工程化改进：
 *    - 提供暴力搜索版本用于小规模验证
 *    - 模块化设计，便于理解和维护
 *    - 性能监控和优化建议
 * 
 * 5. 扩展应用：
 *    - 类似思路可用于其他约束满足问题
 *    - 可以扩展到三维或更高维度的网格
 *    - 可以处理不同的邻居定义规则
 */