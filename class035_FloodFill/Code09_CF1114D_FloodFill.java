package class058;

/**
 * Codeforces 1114D. Flood Fill
 * 题目链接: https://codeforces.com/problemset/problem/1114/D
 * 
 * 题目描述:
 * 有一个由n个方格组成的1×n的网格，每个方格都有一个颜色。
 * 你可以选择任意一个方格作为起始点，然后进行操作：
 * 每次操作可以选择一个与当前已染色区域相邻的方格，将其染成与起始点相同的颜色。
 * 目标是使所有方格都变成同一种颜色，求最少操作次数。
 * 
 * 解题思路:
 * 这是一个动态规划问题。我们可以将问题转化为：
 * 给定一个由不同颜色段组成的序列，每次操作可以将一个颜色段扩展到相邻的颜色段。
 * 求将整个序列变成同一颜色的最少操作次数。
 * 
 * 我们可以使用区间DP来解决：
 * dp[i][j] 表示将区间[i,j]内的颜色段都变成相同颜色的最少操作次数
 * 状态转移方程：
 * 如果colors[i] == colors[j]，则dp[i][j] = dp[i+1][j-1]
 * 否则dp[i][j] = min(dp[i+1][j], dp[i][j-1]) + 1
 * 
 * 时间复杂度: O(n^2) - 区间DP的时间复杂度
 * 空间复杂度: O(n^2) - DP数组的空间
 * 是否最优解: 是
 * 
 * 工程化考量:
 * 1. 异常处理：检查输入是否为空
 * 2. 边界条件：处理单个颜色段的情况
 * 3. 可配置性：可以扩展到二维情况
 * 
 * 语言特性差异:
 * Java: 对象引用和垃圾回收
 * C++: 指针操作和手动内存管理
 * Python: 动态类型和自动内存管理
 * 
 * 极端输入场景:
 * 1. 空数组
 * 2. 单元素数组
 * 3. 所有元素颜色相同
 * 4. 所有元素颜色都不相同
 * 
 * 性能优化:
 * 1. 预处理合并相同颜色的连续段
 * 2. 使用滚动数组优化空间复杂度
 * 3. 提前终止条件判断
 * 
 * 与其他算法的联系:
 * 1. 动态规划: 区间DP
 * 2. 字符串处理: 颜色段合并
 * 3. 贪心算法: 每次选择最优扩展方向
 */
public class Code09_CF1114D_FloodFill {
    
    /**
     * 计算最少操作次数使所有方格变成同一颜色
     * 
     * @param colors 颜色数组
     * @return 最少操作次数
     */
    public static int minOperations(int[] colors) {
        // 边界条件检查
        if (colors == null || colors.length == 0) {
            return 0;
        }
        
        if (colors.length == 1) {
            return 0;
        }
        
        // 预处理：合并相同颜色的连续段
        int[] compressed = compressColors(colors);
        int n = compressed.length;
        
        // dp[i][j] 表示将区间[i,j]内的颜色段都变成相同颜色的最少操作次数
        int[][] dp = new int[n][n];
        
        // 初始化：单个颜色段不需要操作
        for (int i = 0; i < n; i++) {
            dp[i][i] = 0;
        }
        
        // 区间DP填表
        for (int len = 2; len <= n; len++) {  // 区间长度
            for (int i = 0; i <= n - len; i++) {  // 区间起始位置
                int j = i + len - 1;  // 区间结束位置
                
                if (compressed[i] == compressed[j]) {
                    // 如果两端颜色相同，可以直接连接
                    if (len == 2) {
                        dp[i][j] = 0;
                    } else {
                        dp[i][j] = dp[i + 1][j - 1];
                    }
                } else {
                    // 如果两端颜色不同，需要一次操作
                    if (len == 2) {
                        dp[i][j] = 1;
                    } else {
                        dp[i][j] = Math.min(dp[i + 1][j], dp[i][j - 1]) + 1;
                    }
                }
            }
        }
        
        return dp[0][n - 1];
    }
    
    /**
     * 压缩颜色数组，合并相同颜色的连续段
     * 
     * @param colors 原始颜色数组
     * @return 压缩后的颜色数组
     */
    private static int[] compressColors(int[] colors) {
        if (colors.length == 0) {
            return new int[0];
        }
        
        // 计算压缩后的长度
        int compressedLength = 1;
        for (int i = 1; i < colors.length; i++) {
            if (colors[i] != colors[i - 1]) {
                compressedLength++;
            }
        }
        
        // 创建压缩后的数组
        int[] compressed = new int[compressedLength];
        compressed[0] = colors[0];
        int index = 1;
        
        for (int i = 1; i < colors.length; i++) {
            if (colors[i] != colors[i - 1]) {
                compressed[index++] = colors[i];
            }
        }
        
        return compressed;
    }
    
    // 测试方法
    public static void main(String[] args) {
        // 测试用例1
        int[] colors1 = {1, 2, 1, 2, 1};
        System.out.println("测试用例1:");
        System.out.println("颜色数组: " + java.util.Arrays.toString(colors1));
        System.out.println("最少操作次数: " + minOperations(colors1));
        
        // 测试用例2
        int[] colors2 = {1, 1, 1};
        System.out.println("\n测试用例2:");
        System.out.println("颜色数组: " + java.util.Arrays.toString(colors2));
        System.out.println("最少操作次数: " + minOperations(colors2));
        
        // 测试用例3
        int[] colors3 = {1, 2, 3, 4, 5};
        System.out.println("\n测试用例3:");
        System.out.println("颜色数组: " + java.util.Arrays.toString(colors3));
        System.out.println("最少操作次数: " + minOperations(colors3));
    }
}