package class076;

import java.util.*;

/**
 * LeetCode 1130. 叶值的最小代价生成树
 * 题目链接：https://leetcode.cn/problems/minimum-cost-tree-from-leaf-values/
 * 难度：中等
 * 
 * 题目描述：
 * 给你一个正整数数组 arr，考虑所有满足以下条件的二叉树：
 * 1. 每个节点都有 0 个或 2 个子节点
 * 2. 数组 arr 中的值与树的中序遍历中每个叶节点的值一一对应
 * 3. 每个非叶节点的值等于其左子树和右子树中叶节点的最大值的乘积
 * 
 * 解题思路：
 * 这是一个区间动态规划问题，需要构建最优二叉树。
 * 状态定义：dp[i][j]表示区间[i,j]构建二叉树的最小代价
 * 辅助数组：max[i][j]表示区间[i,j]中的最大值
 * 
 * 时间复杂度：O(n^3)
 * 空间复杂度：O(n^2)
 * 
 * 工程化考量：
 * 1. 使用单调栈优化到O(n)时间复杂度
 * 2. 处理边界条件：单个叶节点的情况
 * 3. 优化：预处理区间最大值
 * 
 * 相关题目扩展：
 * 1. LeetCode 1130. 叶值的最小代价生成树 - https://leetcode.cn/problems/minimum-cost-tree-from-leaf-values/
 * 2. LeetCode 1039. 多边形三角剖分的最低得分 - https://leetcode.cn/problems/minimum-score-triangulation-of-polygon/
 * 3. LeetCode 312. 戳气球 - https://leetcode.cn/problems/burst-balloons/
 * 4. LeetCode 1547. 切棍子的最小成本 - https://leetcode.cn/problems/minimum-cost-to-cut-a-stick/
 * 5. LeetCode 1000. 合并石头的最低成本 - https://leetcode.cn/problems/minimum-cost-to-merge-stones/
 * 6. LintCode 1063. 凸多边形的三角剖分 - https://www.lintcode.com/problem/1063/
 * 7. HackerRank - Sherlock and Cost - https://www.hackerrank.com/challenges/sherlock-and-cost/problem
 * 8. Codeforces 1327D - Infinite Path - https://codeforces.com/problemset/problem/1327/D
 * 9. AtCoder ABC144D - Water Bottle - https://atcoder.jp/contests/abc144/tasks/abc144_d
 * 10. 洛谷 P1880 [NOI1995] 石子合并 - https://www.luogu.com.cn/problem/P1880
 */
public class Code12_MinimumCostTreeFromLeafValues {

    /**
     * 区间动态规划解法
     * @param arr 叶节点值数组
     * @return 最小代价
     */
    public static int mctFromLeafValues(int[] arr) {
        int n = arr.length;
        if (n == 1) return 0;
        
        // dp[i][j]表示区间[i,j]构建二叉树的最小代价
        int[][] dp = new int[n][n];
        // max[i][j]表示区间[i,j]中的最大值
        int[][] max = new int[n][n];
        
        // 初始化max数组
        for (int i = 0; i < n; i++) {
            max[i][i] = arr[i];
            for (int j = i + 1; j < n; j++) {
                max[i][j] = Math.max(max[i][j - 1], arr[j]);
            }
        }
        
        // 初始化dp数组
        for (int i = 0; i < n; i++) {
            Arrays.fill(dp[i], Integer.MAX_VALUE);
            dp[i][i] = 0; // 单个叶节点代价为0
        }
        
        // 两个叶节点的情况
        for (int i = 0; i < n - 1; i++) {
            dp[i][i + 1] = arr[i] * arr[i + 1];
        }
        
        // 区间动态规划
        for (int len = 2; len <= n; len++) {
            for (int i = 0; i <= n - len; i++) {
                int j = i + len - 1;
                
                // 枚举分割点k，将区间分成[i,k]和[k+1,j]
                for (int k = i; k < j; k++) {
                    int cost = dp[i][k] + dp[k + 1][j] + max[i][k] * max[k + 1][j];
                    dp[i][j] = Math.min(dp[i][j], cost);
                }
            }
        }
        
        return dp[0][n - 1];
    }
    
    /**
     * 单调栈优化解法 - O(n)时间复杂度
     * 思路：每次合并最小的两个相邻元素
     */
    public static int mctFromLeafValuesStack(int[] arr) {
        int res = 0;
        Stack<Integer> stack = new Stack<>();
        stack.push(Integer.MAX_VALUE); // 哨兵
        
        for (int num : arr) {
            // 维护单调递减栈
            while (stack.peek() <= num) {
                int mid = stack.pop();
                // 合并代价：mid * min(stack.peek(), num)
                res += mid * Math.min(stack.peek(), num);
            }
            stack.push(num);
        }
        
        // 处理栈中剩余元素
        while (stack.size() > 2) {
            res += stack.pop() * stack.peek();
        }
        
        return res;
    }
    
    /**
     * 贪心解法 - 每次合并乘积最小的相邻元素
     */
    public static int mctFromLeafValuesGreedy(int[] arr) {
        List<Integer> list = new ArrayList<>();
        for (int num : arr) {
            list.add(num);
        }
        
        int res = 0;
        while (list.size() > 1) {
            // 找到乘积最小的相邻元素对
            int minProduct = Integer.MAX_VALUE;
            int minIndex = -1;
            
            for (int i = 0; i < list.size() - 1; i++) {
                int product = list.get(i) * list.get(i + 1);
                if (product < minProduct) {
                    minProduct = product;
                    minIndex = i;
                }
            }
            
            // 合并这两个元素
            res += minProduct;
            int maxVal = Math.max(list.get(minIndex), list.get(minIndex + 1));
            list.remove(minIndex + 1);
            list.set(minIndex, maxVal);
        }
        
        return res;
    }
    
    /**
     * 测试方法
     */
    public static void main(String[] args) {
        // 测试用例1
        int[] arr1 = {6, 2, 4};
        System.out.println("测试用例1: arr = [6,2,4]");
        System.out.println("预期结果: 32");
        System.out.println("DP解法: " + mctFromLeafValues(arr1));
        System.out.println("单调栈: " + mctFromLeafValuesStack(arr1));
        System.out.println("贪心解法: " + mctFromLeafValuesGreedy(arr1));
        System.out.println();
        
        // 测试用例2
        int[] arr2 = {4, 11};
        System.out.println("测试用例2: arr = [4,11]");
        System.out.println("预期结果: 44");
        System.out.println("DP解法: " + mctFromLeafValues(arr2));
        System.out.println("单调栈: " + mctFromLeafValuesStack(arr2));
        System.out.println("贪心解法: " + mctFromLeafValuesGreedy(arr2));
        System.out.println();
        
        // 测试用例3
        int[] arr3 = {1, 2, 3, 4, 5};
        System.out.println("测试用例3: arr = [1,2,3,4,5]");
        System.out.println("DP解法: " + mctFromLeafValues(arr3));
        System.out.println("单调栈: " + mctFromLeafValuesStack(arr3));
        System.out.println("贪心解法: " + mctFromLeafValuesGreedy(arr3));
        
        // 性能测试
        int[] largeArr = new int[100];
        Random random = new Random();
        for (int i = 0; i < largeArr.length; i++) {
            largeArr[i] = random.nextInt(100) + 1;
        }
        
        System.out.println("\n性能测试（数组长度100）：");
        long start = System.currentTimeMillis();
        int result1 = mctFromLeafValuesStack(largeArr);
        long time1 = System.currentTimeMillis() - start;
        
        start = System.currentTimeMillis();
        int result2 = mctFromLeafValuesGreedy(largeArr);
        long time2 = System.currentTimeMillis() - start;
        
        System.out.println("单调栈 - 结果: " + result1 + ", 时间: " + time1 + "ms");
        System.out.println("贪心解法 - 结果: " + result2 + ", 时间: " + time2 + "ms");
    }
    
    /**
     * 复杂度分析：
     * 区间DP解法：
     * - 时间复杂度：O(n^3)
     * - 空间复杂度：O(n^2)
     * 
     * 单调栈解法：
     * - 时间复杂度：O(n)
     * - 空间复杂度：O(n)
     * 
     * 贪心解法：
     * - 时间复杂度：O(n^2)
     * - 空间复杂度：O(n)
     * 
     * 工程化建议：
     * 1. 对于大规模数据，优先使用单调栈解法
     * 2. 对于小规模数据，可以使用DP解法保证正确性
     * 3. 贪心解法虽然简单，但可能不是最优解
     */
}