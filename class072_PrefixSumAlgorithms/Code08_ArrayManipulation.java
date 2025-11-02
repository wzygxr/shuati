package class046;

/**
 * 数组操作 (Array Manipulation)
 * 
 * 题目描述:
 * 给定一个长度为n的数组，初始时所有元素都为0。然后进行m次操作，每次操作给定三个整数a, b, k，
 * 表示将数组中从索引a到索引b（包含a和b）的所有元素都增加k。求执行完所有操作后数组中的最大值。
 * 
 * 示例:
 * 输入: n = 5, queries = [[1,2,100],[2,5,100],[3,4,100]]
 * 输出: 200
 * 解释: 
 * 初始数组: [0, 0, 0, 0, 0]
 * 操作1: [100, 100, 0, 0, 0]
 * 操作2: [100, 200, 100, 100, 100]
 * 操作3: [100, 200, 200, 200, 100]
 * 最大值: 200
 * 
 * 提示:
 * 3 <= n <= 10^7
 * 1 <= m <= 2*10^5
 * 1 <= a <= b <= n
 * 0 <= k <= 10^9
 * 
 * 题目链接: https://www.hackerrank.com/challenges/crush/problem
 * 
 * 解题思路:
 * 使用差分数组技巧结合前缀和来优化区间更新操作。
 * 1. 创建一个差分数组diff，大小为n+1
 * 2. 对于每个操作[a,b,k]，执行diff[a-1] += k和diff[b] -= k
 * 3. 对差分数组计算前缀和，得到最终数组
 * 4. 在计算前缀和的过程中记录最大值
 * 
 * 时间复杂度: O(n + m) - 需要遍历所有操作和数组一次
 * 空间复杂度: O(n) - 需要额外的差分数组空间
 * 
 * 工程化考量:
 * 1. 边界条件处理：n=0、空操作数组等特殊情况
 * 2. 索引转换：题目使用1-based索引，需要转换为0-based
 * 3. 整数溢出：使用long避免大数溢出
 * 4. 性能优化：差分数组方法将O(n*m)优化到O(n+m)
 * 
 * 最优解分析:
 * 这是最优解，因为需要处理所有操作，而且数组大小可能很大，不能使用暴力方法。
 * 差分数组方法将时间复杂度从O(n*m)优化到O(n+m)。
 * 
 * 算法核心:
 * 差分数组的思想：对于区间[a,b]增加k，只需要在a位置+k，在b+1位置-k。
 * 然后通过前缀和还原整个数组，同时记录最大值。
 * 
 * 算法调试技巧:
 * 1. 打印中间过程：可以在处理每个操作后打印差分数组状态
 * 2. 边界测试：测试n=1、操作重叠、边界索引等情况
 * 3. 性能测试：测试大规模数据下的性能表现
 * 
 * 语言特性差异:
 * Java中数组索引是0-based，需要处理1-based到0-based的转换。
 * 与C++相比，Java有自动内存管理，无需手动释放数组内存。
 * 与Python相比，Java是静态类型语言，需要显式声明数组类型。
 */
public class Code08_ArrayManipulation {

    /**
     * 计算数组操作后的最大值
     * 
     * @param n 数组长度
     * @param queries 操作数组，每个操作包含[起始索引, 结束索引, 增加值]
     * @return 操作后数组的最大值
     * 
     * 异常场景处理:
     * - n <= 0：返回0
     * - queries为空：返回0
     * - 索引越界：题目保证1 <= a <= b <= n
     * - 大数溢出：使用long避免
     * 
     * 边界条件:
     * - n=1的情况
     * - 操作重叠的情况
     * - 边界索引（a=1, b=n）
     * - k=0的情况
     */
    public static long arrayManipulation(int n, int[][] queries) {
        // 边界情况处理
        if (n <= 0 || queries == null || queries.length == 0) {
            return 0;
        }
        
        // 创建差分数组，大小为n+1以便处理边界情况
        long[] diff = new long[n + 1];
        
        // 处理每个操作
        for (int[] query : queries) {
            int a = query[0];      // 起始索引（1-based）
            int b = query[1];      // 结束索引（1-based）
            int k = query[2];      // 增加值
            
            // 在差分数组中标记区间更新
            // 在a-1位置增加k（0-based索引）
            diff[a - 1] += k;
            // 在b位置减少k（如果b < n，避免数组越界）
            if (b < n) {
                diff[b] -= k;
            }
            
            // 调试打印：显示每个操作后的差分数组状态
            // System.out.println("操作: [" + a + "," + b + "," + k + "]");
            // System.out.println("差分数组: " + java.util.Arrays.toString(diff));
        }
        
        // 通过计算差分数组的前缀和得到最终数组，并记录最大值
        long maxVal = Long.MIN_VALUE;
        long currentSum = 0;
        
        for (int i = 0; i < n; i++) {
            currentSum += diff[i];
            if (currentSum > maxVal) {
                maxVal = currentSum;
            }
            
            // 调试打印：显示前缀和计算过程
            // System.out.println("位置 " + i + ": 当前和 = " + currentSum + ", 最大值 = " + maxVal);
        }
        
        return maxVal;
    }

    /**
     * 单元测试方法
     */
    public static void testArrayManipulation() {
        System.out.println("=== 数组操作单元测试 ===");
        
        // 测试用例1：经典情况
        int n1 = 5;
        int[][] queries1 = {{1, 2, 100}, {2, 5, 100}, {3, 4, 100}};
        long result1 = arrayManipulation(n1, queries1);
        System.out.println("测试用例1 n=5, queries=[[1,2,100],[2,5,100],[3,4,100]]: " + result1 + " (预期: 200)");
        
        // 测试用例2：复杂操作
        int n2 = 10;
        int[][] queries2 = {{2, 6, 8}, {3, 5, 7}, {1, 8, 1}, {5, 9, 15}};
        long result2 = arrayManipulation(n2, queries2);
        System.out.println("测试用例2 n=10, queries=[[2,6,8],[3,5,7],[1,8,1],[5,9,15]]: " + result2 + " (预期: 31)");
        
        // 测试用例3：边界情况
        int n3 = 4;
        int[][] queries3 = {{1, 2, 5}, {2, 4, 10}, {1, 3, 3}};
        long result3 = arrayManipulation(n3, queries3);
        System.out.println("测试用例3 n=4, queries=[[1,2,5],[2,4,10],[1,3,3]]: " + result3 + " (预期: 18)");
        
        // 测试用例4：n=1
        int n4 = 1;
        int[][] queries4 = {{1, 1, 5}};
        long result4 = arrayManipulation(n4, queries4);
        System.out.println("测试用例4 n=1, queries=[[1,1,5]]: " + result4 + " (预期: 5)");
        
        // 测试用例5：空操作
        int n5 = 5;
        int[][] queries5 = {};
        long result5 = arrayManipulation(n5, queries5);
        System.out.println("测试用例5 n=5, queries=[]: " + result5 + " (预期: 0)");
        
        // 测试用例6：n=0
        int n6 = 0;
        int[][] queries6 = {{1, 1, 5}};
        long result6 = arrayManipulation(n6, queries6);
        System.out.println("测试用例6 n=0, queries=[[1,1,5]]: " + result6 + " (预期: 0)");
        
        // 测试用例7：大数测试
        int n7 = 3;
        int[][] queries7 = {{1, 3, 1000000000}};
        long result7 = arrayManipulation(n7, queries7);
        System.out.println("测试用例7 n=3, queries=[[1,3,1000000000]]: " + result7 + " (预期: 1000000000)");
    }

    /**
     * 性能测试方法
     */
    public static void performanceTest() {
        System.out.println("
=== 性能测试 ===");
        int n = 10000000; // 1000万元素
        int m = 200000;   // 20万次操作
        
        // 生成随机操作
        int[][] queries = new int[m][3];
        java.util.Random random = new java.util.Random();
        
        for (int i = 0; i < m; i++) {
            int a = random.nextInt(n) + 1; // 1-based索引
            int b = a + random.nextInt(n - a + 1); // b >= a
            int k = random.nextInt(1000000000); // k在0到10^9之间
            
            queries[i][0] = a;
            queries[i][1] = b;
            queries[i][2] = k;
        }
        
        long startTime = System.currentTimeMillis();
        long result = arrayManipulation(n, queries);
        long endTime = System.currentTimeMillis();
        
        System.out.println("处理 n=" + n + ", m=" + m + " 耗时: " + (endTime - startTime) + "ms");
        System.out.println("最大值: " + result);
    }

    /**
     * 主函数 - 测试入口
     */
    public static void main(String[] args) {
        // 运行单元测试
        testArrayManipulation();
        
        // 运行性能测试（可选）
        // performanceTest();
        
        System.out.println("
=== 测试完成 ===");
    }
}