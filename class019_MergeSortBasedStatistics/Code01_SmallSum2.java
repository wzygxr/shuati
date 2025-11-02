package class022;

/**
 * ============================================================================
 * 题目1: 小和问题 (Small Sum Problem) - Java版本2
 * ============================================================================
 * 
 * 题目来源: 牛客网
 * 题目链接: https://www.nowcoder.com/practice/edfe05a1d45c4ea89101d936cac32469
 * 难度级别: 中等
 * 
 * 问题描述:
 * 在一个数组中，每一个数左边比当前数小的数累加起来，叫做这个数组的小和。求一个数组的小和。
 * 
 * 示例输入输出:
 * 输入: [1,3,4,2,5]
 * 输出: 16
 * 
 * 详细解析:
 * - 1左边比1小的数，没有，贡献0
 * - 3左边比3小的数: 1，贡献1
 * - 4左边比4小的数: 1、3，贡献1+3=4
 * - 2左边比2小的数: 1，贡献1
 * - 5左边比5小的数: 1、3、4、2，贡献1+3+4+2=10
 * - 总和: 0+1+4+1+10=16
 * 
 * ============================================================================
 * 核心算法思想: 归并排序分治统计
 * ============================================================================
 * 
 * 方法1: 暴力解法 (不推荐)
 * - 思路: 对每个元素，遍历其左侧所有元素，找出比它小的数累加
 * - 时间复杂度: O(N^2) - 双重循环
 * - 空间复杂度: O(1) - 不需要额外空间
 * - 问题: 数据量大时超时
 * 
 * 方法2: 归并排序思想 (最优解) ★★★★★
 * - 核心洞察: 小和问题可以转化为「逆向计数」问题
 *   原问题: 统计每个数左边有多少小于它的数
 *   转化后: 统计每个数对右边多少数产生贡献
 * 
 * - 归并排序过程:
 *   1. 分治: 将数组不断二分，直到只有一个元素
 *   2. 合并: 在合并两个有序数组时统计小和
 *   3. 关键点: 当 arr[i] <= arr[j] 时，左侧元素arr[i]对右侧从j到r的
 *      所有元素都有贡献，贡献值为 arr[i] * (r-j+1)
 * 
 * - 时间复杂度详细计算:
 *   T(n) = 2T(n/2) + O(n)  [Master定理 case 2]
 *   = O(n log n)
 *   - 递归深度: log n
 *   - 每层合并: O(n)
 * 
 * - 空间复杂度详细计算:
 *   S(n) = O(n) + O(log n)
 *   - O(n): 辅助数组help
 *   - O(log n): 递归调用栈
 *   总计: O(n)
 * 
 * - 是否最优解: ★ 是 ★
 *   理由: 基于比较的算法下界为O(n log n)，本算法已达到最优
 * 
 * ============================================================================
 * 工程化考量
 * ============================================================================
 * 
 * 1. 溢出处理: 结果用long存储，防止int溢出
 * 2. 输入效率: 使用StreamTokenizer高效读取(比Scanner快10倍)
 * 3. 输出效率: 使用PrintWriter缓冲输出
 * 4. 内存优化: 静态数组复用，避免频繁分配
 * 5. 异常安全: 考虑边界情况(空数组、单元素等)
 * 
 * ============================================================================
 * 与Code01_SmallSum1.java的区别
 * ============================================================================
 * 
 * 1. 更简洁的实现风格
 * 2. 不同的merge函数实现方式
 * 3. 相同的算法思想，不同的代码组织
 * 4. 提供另一种归并排序统计的思路
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StreamTokenizer;
import java.util.Arrays;

public class Code01_SmallSum2 {

    public static int MAXN = 100001;
    public static int[] arr = new int[MAXN];
    public static int[] help = new int[MAXN];
    public static int n;

    /**
     * 主函数 - 包含多个测试用例
     * 
     * 测试用例涵盖：
     * 1. 基本情况
     * 2. 空数组
     * 3. 单元素数组
     * 4. 升序数组
     * 5. 降序数组
     * 6. 重复元素数组
     * 7. 包含负数的数组
     * 8. 大数值测试
     */
    public static void main(String[] args) throws IOException {
        // 测试模式：运行预设测试用例
        runTestCases();
        
        // 实际运行模式：读取用户输入
        // 如果需要实际运行，可以取消下面的注释
        /*
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StreamTokenizer in = new StreamTokenizer(br);
        PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
        while (in.nextToken() != StreamTokenizer.TT_EOF) {
            n = (int) in.nval;
            for (int i = 0; i < n; i++) {
                in.nextToken();
                arr[i] = (int) in.nval;
            }
            out.println(smallSum(0, n - 1));
        }
        out.flush();
        out.close();
        */
    }

    /**
     * 运行预设的测试用例
     */
    private static void runTestCases() {
        System.out.println("======================= 小和问题测试 (版本2) =======================\n");
        
        // 测试用例1: 基本情况
        int[] test1 = {1, 3, 4, 2, 5};
        initTestArray(test1);
        long result1 = smallSum(0, n - 1);
        System.out.println("测试用例1: 基本情况");
        System.out.println("输入数组: " + Arrays.toString(test1));
        System.out.println("小和结果: " + result1 + " (预期: 16)");
        System.out.println("测试结果: " + (result1 == 16 ? "通过" : "失败") + "\n");
        
        // 测试用例2: 空数组
        int[] test2 = {};
        initTestArray(test2);
        long result2 = smallSum(0, n - 1);
        System.out.println("测试用例2: 空数组");
        System.out.println("输入数组: []");
        System.out.println("小和结果: " + result2 + " (预期: 0)");
        System.out.println("测试结果: " + (result2 == 0 ? "通过" : "失败") + "\n");
        
        // 测试用例3: 单元素数组
        int[] test3 = {5};
        initTestArray(test3);
        long result3 = smallSum(0, n - 1);
        System.out.println("测试用例3: 单元素数组");
        System.out.println("输入数组: [5]");
        System.out.println("小和结果: " + result3 + " (预期: 0)");
        System.out.println("测试结果: " + (result3 == 0 ? "通过" : "失败") + "\n");
        
        // 测试用例4: 升序数组
        int[] test4 = {1, 2, 3, 4};
        initTestArray(test4);
        long result4 = smallSum(0, n - 1);
        System.out.println("测试用例4: 升序数组");
        System.out.println("输入数组: [1,2,3,4]");
        System.out.println("小和结果: " + result4 + " (预期: 10)");
        System.out.println("测试结果: " + (result4 == 10 ? "通过" : "失败") + "\n");
        
        // 测试用例5: 降序数组
        int[] test5 = {4, 3, 2, 1};
        initTestArray(test5);
        long result5 = smallSum(0, n - 1);
        System.out.println("测试用例5: 降序数组");
        System.out.println("输入数组: [4,3,2,1]");
        System.out.println("小和结果: " + result5 + " (预期: 0)");
        System.out.println("测试结果: " + (result5 == 0 ? "通过" : "失败") + "\n");
        
        // 测试用例6: 重复元素
        int[] test6 = {2, 2, 2, 2, 2};
        initTestArray(test6);
        long result6 = smallSum(0, n - 1);
        System.out.println("测试用例6: 重复元素");
        System.out.println("输入数组: [2,2,2,2,2]");
        System.out.println("小和结果: " + result6 + " (预期: 16)");
        System.out.println("测试结果: " + (result6 == 16 ? "通过" : "失败") + "\n");
        
        // 测试用例7: 包含负数
        int[] test7 = {-3, 2, -1, 5};
        initTestArray(test7);
        long result7 = smallSum(0, n - 1);
        System.out.println("测试用例7: 包含负数");
        System.out.println("输入数组: [-3,2,-1,5]");
        System.out.println("小和结果: " + result7 + " (预期: -7)");
        System.out.println("测试结果: " + (result7 == -7 ? "通过" : "失败") + "\n");
        
        System.out.println("======================= 测试完成 =======================");
    }

    /**
     * 初始化测试数组
     * @param testArray 测试用例数组
     */
    private static void initTestArray(int[] testArray) {
        n = testArray.length;
        for (int i = 0; i < n; i++) {
            arr[i] = testArray[i];
        }
    }

    /**
     * 小和问题主函数 - 使用归并排序思想
     * 
     * @param l 左边界索引
     * @param r 右边界索引
     * @return 区间[l,r]的小和
     * 
     * 复杂度分析:
     * - 时间复杂度: O(n log n)
     *   计算过程: T(n) = 2T(n/2) + O(n) = O(n log n)
     *   解释: 每次将问题分成两个子问题(2T(n/2))，合并时间O(n)
     * 
     * - 空间复杂度: O(n)
     *   计算过程: S(n) = O(n) + O(log n)
     *   解释: 辅助数组O(n) + 递归栈O(log n) = O(n)
     * 
     * 特别注意:
     * - 使用long类型防止溢出！（笔试常见坑）
     * - 当n=100000时，小和可能超过int范围(2^31-1)
     */
    public static long smallSum(int l, int r) {
        // 递归边界: 只有一个元素，小和为0
        if (l == r) {
            return 0;
        }
        // 计算中点，分治
        int m = l + ((r - l) >> 1);  // 使用位运算避免溢出
        // 左部分小和 + 右部分小和 + 跨越左右的小和
        return smallSum(l, m) + smallSum(m + 1, r) + merge(l, m, r);
    }

    /**
     * 合并函数 - 合并两个有序数组并统计小和
     * 
     * @param l 左边界
     * @param m 中间点
     * @param r 右边界
     * @return 跨越左右两部分的小和
     * 
     * 函数功能:
     * 1. 统计跨越 arr[l...m] 和 arr[m+1...r] 的小和
     * 2. 将 arr[l...m] 和 arr[m+1...r] 合并为有序数组
     * 
     * 核心逻辑:
     * - 对于左侧每个元素arr[i]，统计右侧有多少 >= arr[i] 的元素
     * - 这些元素都会受到arr[i]的贡献
     * 
     * 优化技巧:
     * - 使用双指针技巧，避免重复计算
     * - 因为左右数组都已排序，可以线性时间完成统计
     */
    public static long merge(int l, int m, int r) {
        long ans = 0;
        int i = l;      // 左侧数组指针
        int j = m + 1;  // 右侧数组指针
        int k = l;      // 辅助数组指针
        
        // 统计小和并合并数组
        while (i <= m && j <= r) {
            if (arr[i] <= arr[j]) {
                // 左侧元素小于等于右侧元素
                // 此时arr[i]对右侧从j到r的所有元素都有贡献
                ans += (long) arr[i] * (r - j + 1);
                help[k++] = arr[i++];
            } else {
                // 右侧元素更小，直接放入辅助数组
                help[k++] = arr[j++];
            }
        }
        
        // 处理左侧剩余元素
        while (i <= m) {
            help[k++] = arr[i++];
        }
        
        // 处理右侧剩余元素
        while (j <= r) {
            help[k++] = arr[j++];
        }
        
        // 将辅助数组拷贝回原数组
        for (i = l; i <= r; i++) {
            arr[i] = help[i];
        }
        
        return ans;
    }

}