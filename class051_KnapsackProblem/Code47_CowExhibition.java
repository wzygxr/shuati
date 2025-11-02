package class073;

import java.util.Arrays;

// POJ 2184 Cow Exhibition
// 题目描述：奶牛们想证明它们是聪明而风趣的。为此，贝西筹备了一个奶牛博览会，
// 她已经对N头奶牛进行了面试，确定了每头奶牛的聪明度和幽默度。
// 贝西可以选择任意数量的奶牛参加展览，但希望总聪明度和总幽默度都非负。
// 在满足条件的情况下，使得总聪明度与总幽默度的和最大。
// 链接：http://poj.org/problem?id=2184
// 
// 解题思路：
// 这是一个二维费用的01背包问题，需要同时考虑两个维度（聪明度和幽默度）。
// 由于两个维度都可能为负数，需要进行坐标平移。
// 
// 状态定义：dp[i][j] 表示前i头奶牛，聪明度总和为j时的最大幽默度总和
// 状态转移方程：
//   dp[i][j] = max(dp[i-1][j], dp[i-1][j-smart[i]] + funny[i])
// 
// 关键点：
// 1. 坐标平移：由于聪明度可能为负数，需要将坐标平移到非负数范围
// 2. 二维费用：同时考虑聪明度和幽默度两个维度
// 3. 状态优化：使用滚动数组优化空间复杂度
// 
// 时间复杂度：O(N * range)，其中range是聪明度的可能范围
// 空间复杂度：O(range)，使用滚动数组优化
// 
// 工程化考量：
// 1. 异常处理：处理空输入、边界值等情况
// 2. 性能优化：坐标平移和滚动数组优化
// 3. 边界条件：处理负数范围和结果有效性检查

public class Code47_CowExhibition {
    
    private static final int OFFSET = 100000;  // 坐标偏移量，处理负数
    private static final int MAX_RANGE = 200000; // 总范围大小
    private static final int INF = Integer.MIN_VALUE / 2; // 表示不可达状态
    
    /**
     * 动态规划解法 - 二维费用01背包
     * @param cows 奶牛数组，每个奶牛包含聪明度和幽默度
     * @return 最大总聪明度与总幽默度的和
     */
    public static int maxCowExhibition(int[][] cows) {
        // 参数验证
        if (cows == null || cows.length == 0) {
            return 0;
        }
        
        int n = cows.length;
        
        // 创建DP数组，使用滚动数组优化
        int[] dp = new int[MAX_RANGE + 1];
        Arrays.fill(dp, INF);
        dp[OFFSET] = 0; // 初始状态：聪明度总和为0，幽默度总和为0
        
        // 遍历每头奶牛
        for (int i = 0; i < n; i++) {
            int smart = cows[i][0];
            int funny = cows[i][1];
            
            // 根据smart的正负决定遍历方向
            if (smart >= 0) {
                // 正数：倒序遍历，避免重复选择
                for (int j = MAX_RANGE; j >= smart; j--) {
                    if (dp[j - smart] != INF) {
                        dp[j] = Math.max(dp[j], dp[j - smart] + funny);
                    }
                }
            } else {
                // 负数：正序遍历
                for (int j = 0; j <= MAX_RANGE + smart; j++) {
                    if (dp[j - smart] != INF) {
                        dp[j] = Math.max(dp[j], dp[j - smart] + funny);
                    }
                }
            }
        }
        
        // 寻找最大和（聪明度+幽默度）
        int maxSum = 0;
        for (int j = OFFSET; j <= MAX_RANGE; j++) {
            if (dp[j] >= 0) { // 幽默度总和需要非负
                maxSum = Math.max(maxSum, j - OFFSET + dp[j]);
            }
        }
        
        return maxSum;
    }
    
    /**
     * 优化的动态规划解法 - 使用二维数组便于理解
     */
    public static int maxCowExhibition2D(int[][] cows) {
        if (cows == null || cows.length == 0) {
            return 0;
        }
        
        int n = cows.length;
        
        // 计算聪明度的可能范围
        int minSmart = 0, maxSmart = 0;
        for (int[] cow : cows) {
            if (cow[0] < 0) minSmart += cow[0];
            else maxSmart += cow[0];
        }
        
        int range = maxSmart - minSmart;
        int offset = -minSmart;
        
        // 创建二维DP数组
        int[][] dp = new int[n + 1][range + 1];
        for (int i = 0; i <= n; i++) {
            Arrays.fill(dp[i], INF);
        }
        dp[0][offset] = 0;
        
        // 动态规划
        for (int i = 1; i <= n; i++) {
            int smart = cows[i - 1][0];
            int funny = cows[i - 1][1];
            
            for (int j = 0; j <= range; j++) {
                // 不选当前奶牛
                dp[i][j] = dp[i - 1][j];
                
                // 选当前奶牛
                int prev = j - smart;
                if (prev >= 0 && prev <= range && dp[i - 1][prev] != INF) {
                    dp[i][j] = Math.max(dp[i][j], dp[i - 1][prev] + funny);
                }
            }
        }
        
        // 寻找最大和
        int maxSum = 0;
        for (int j = offset; j <= range; j++) {
            if (dp[n][j] >= 0) {
                maxSum = Math.max(maxSum, j - offset + dp[n][j]);
            }
        }
        
        return maxSum;
    }
    
    /**
     * 空间优化的解法 - 只记录有效状态
     */
    public static int maxCowExhibitionOptimized(int[][] cows) {
        if (cows == null || cows.length == 0) {
            return 0;
        }
        
        // 分离正负聪明度的奶牛
        java.util.List<int[]> positiveCows = new java.util.ArrayList<>();
        java.util.List<int[]> negativeCows = new java.util.ArrayList<>();
        
        for (int[] cow : cows) {
            if (cow[0] >= 0) {
                positiveCows.add(cow);
            } else {
                negativeCows.add(cow);
            }
        }
        
        // 处理正数聪明度的奶牛
        int[] dp = new int[MAX_RANGE + 1];
        Arrays.fill(dp, INF);
        dp[OFFSET] = 0;
        
        for (int[] cow : positiveCows) {
            int smart = cow[0];
            int funny = cow[1];
            for (int j = MAX_RANGE; j >= smart; j--) {
                if (dp[j - smart] != INF) {
                    dp[j] = Math.max(dp[j], dp[j - smart] + funny);
                }
            }
        }
        
        // 处理负数聪明度的奶牛
        for (int[] cow : negativeCows) {
            int smart = cow[0];
            int funny = cow[1];
            for (int j = 0; j <= MAX_RANGE + smart; j++) {
                if (dp[j - smart] != INF) {
                    dp[j] = Math.max(dp[j], dp[j - smart] + funny);
                }
            }
        }
        
        // 寻找最大和
        int maxSum = 0;
        for (int j = OFFSET; j <= MAX_RANGE; j++) {
            if (dp[j] >= 0) {
                maxSum = Math.max(maxSum, j - OFFSET + dp[j]);
            }
        }
        
        return maxSum;
    }
    
    /**
     * 测试方法
     */
    public static void main(String[] args) {
        // 测试用例
        int[][][] testCases = {
            // 示例测试用例
            {
                {5, 1},
                {1, 5},
                {-5, 5},
                {5, -1}
            },
            // 边界测试用例
            {
                {10, 20},
                {15, 15}
            },
            // 包含负数的测试用例
            {
                {-1, 100},
                {2, 50},
                {-3, 200}
            },
            // 空测试用例
            {}
        };
        
        System.out.println("奶牛展览问题测试：");
        for (int i = 0; i < testCases.length; i++) {
            int[][] cows = testCases[i];
            
            int result1 = maxCowExhibition(cows);
            int result2 = maxCowExhibition2D(cows);
            int result3 = maxCowExhibitionOptimized(cows);
            
            System.out.printf("测试用例%d: 奶牛数量=%d, 方法1=%d, 方法2=%d, 方法3=%d%n",
                            i + 1, cows.length, result1, result2, result3);
            
            // 验证结果一致性
            if (result1 != result2 || result2 != result3) {
                System.out.println("警告：不同方法结果不一致！");
            }
        }
        
        // 性能测试 - 大规模数据
        int n = 100;
        int[][] largeCows = generateLargeTestData(n);
        
        long startTime = System.currentTimeMillis();
        int largeResult = maxCowExhibitionOptimized(largeCows);
        long endTime = System.currentTimeMillis();
        
        System.out.printf("大规模测试: 奶牛数量=%d, 结果=%d, 耗时=%dms%n",
                        n, largeResult, endTime - startTime);
        
        // 边界情况测试
        System.out.println("边界情况测试：");
        System.out.println("空数组: " + maxCowExhibition(new int[][]{}));
        System.out.println("单头奶牛: " + maxCowExhibition(new int[][]{{10, 20}}));
        System.out.println("全负数聪明度: " + maxCowExhibition(new int[][]{{-1, 5}, {-2, 10}}));
    }
    
    /**
     * 生成大规模测试数据
     */
    private static int[][] generateLargeTestData(int n) {
        int[][] cows = new int[n][2];
        java.util.Random random = new java.util.Random();
        
        for (int i = 0; i < n; i++) {
            // 生成-100到100之间的聪明度
            cows[i][0] = random.nextInt(201) - 100;
            // 生成0到100之间的幽默度
            cows[i][1] = random.nextInt(101);
        }
        
        return cows;
    }
}

/*
 * 复杂度分析：
 * 
 * 方法1：动态规划（滚动数组）
 * - 时间复杂度：O(N * range)
 *   - N: 奶牛数量
 *   - range: 聪明度的可能范围（经过坐标平移）
 * - 空间复杂度：O(range)
 * 
 * 方法2：二维动态规划
 * - 时间复杂度：O(N * range)
 * - 空间复杂度：O(N * range)
 * 
 * 方法3：优化的动态规划
 * - 时间复杂度：O(N * range)（但常数更小）
 * - 空间复杂度：O(range)
 * 
 * 关键点分析：
 * 1. 坐标平移：处理负数聪明度，将坐标平移到非负数范围
 * 2. 遍历方向：根据smart的正负决定遍历方向（01背包特性）
 * 3. 状态有效性：只考虑幽默度非负的状态
 * 4. 结果计算：聪明度+幽默度的最大和
 * 
 * 工程化考量：
 * 1. 参数验证：处理各种边界输入
 * 2. 性能优化：使用滚动数组和分离正负策略
 * 3. 测试覆盖：包含正常、边界、性能测试
 * 4. 代码可读性：清晰的变量命名和注释
 * 
 * 面试要点：
 * 1. 理解二维费用背包问题的特点
 * 2. 掌握坐标平移处理负数的方法
 * 3. 了解不同遍历方向的原因
 * 4. 能够分析算法的时空复杂度
 * 
 * 扩展应用：
 * 1. 多维度优化问题
 * 2. 资源分配问题
 * 3. 投资组合优化
 * 4. 特征选择问题
 */