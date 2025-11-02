package class047;

import java.util.*;

/**
 * AcWing 797. 差分
 * 
 * 题目描述:
 * 输入一个长度为 n 的整数序列。
 * 接下来输入 m 个操作，每个操作包含三个整数 l, r, c，
 * 表示将序列中 [l, r] 之间的每个数加上 c。
 * 
 * 输入格式:
 * 第一行包含两个整数 n 和 m。
 * 第二行包含 n 个整数，表示整数序列。
 * 接下来 m 行，每行包含三个整数 l，r，c，表示一个操作。
 * 
 * 输出格式:
 * 共一行，包含 n 个整数，表示最终序列。
 * 
 * 数据范围:
 * 1 ≤ n, m ≤ 100000,
 * 1 ≤ l ≤ r ≤ n,
 * -1000 ≤ c ≤ 1000,
 * -1000 ≤ 数列中元素的绝对值 ≤ 1000
 * 
 * 题目链接: https://www.acwing.com/problem/content/799/
 * 
 * 解题思路:
 * 使用差分数组技巧来优化区间更新操作。
 * 1. 创建一个差分数组diff，大小为n+2（处理边界情况）
 * 2. 对于每个操作[l, r, c]，执行diff[l] += c和diff[r+1] -= c
 * 3. 对差分数组计算前缀和，得到最终序列
 * 
 * 时间复杂度: O(n + m) - 需要遍历所有操作和数组一次
 * 空间复杂度: O(n) - 需要额外的差分数组空间
 * 
 * 这是最优解，因为需要处理所有操作，使用差分数组将区间更新操作从O(n)优化到O(1)。
 */
public class Code26_AcWing797Difference {

    /**
     * 处理差分数组操作
     * 
     * @param n 数组长度
     * @param m 操作数量
     * @param arr 初始数组
     * @param operations 操作数组，每个操作包含[起始索引, 结束索引, 增加值]
     * @return 最终数组
     * 
     * 时间复杂度: O(n + m) - 需要遍历所有操作和数组一次
     * 空间复杂度: O(n) - 需要额外的差分数组空间
     * 
     * 工程化考量:
     * 1. 边界处理: 使用大小为n+2的数组避免索引越界
     * 2. 异常处理: 可以添加输入参数验证
     * 3. 性能优化: 差分数组将区间更新操作从O(n)优化到O(1)
     * 4. 可读性: 变量命名清晰，注释详细
     */
    public static int[] processDifference(int n, int m, int[] arr, int[][] operations) {
        // 创建差分数组，大小为 n+2 是为了处理边界情况
        int[] diff = new int[n + 2];
        
        // 构造初始数组的差分数组
        diff[1] = arr[0];
        for (int i = 2; i <= n; i++) {
            diff[i] = arr[i - 1] - arr[i - 2];
        }
        
        // 处理每个操作
        for (int[] operation : operations) {
            int l = operation[0];    // 起始索引（1-based）
            int r = operation[1];    // 结束索引（1-based）
            int c = operation[2];    // 增加值
            
            // 在差分数组中标记区间更新
            diff[l] += c;            // 在起始位置增加c
            diff[r + 1] -= c;        // 在结束位置之后减少c
        }
        
        // 通过计算差分数组的前缀和得到最终数组
        int[] result = new int[n];
        result[0] = diff[1];
        for (int i = 1; i < n; i++) {
            result[i] = result[i - 1] + diff[i + 1];
        }
        
        return result;
    }

    /**
     * 主函数，处理输入并输出结果
     * 
     * 时间复杂度: O(n + m) - 需要遍历所有操作和数组一次
     * 空间复杂度: O(n) - 需要额外的差分数组空间
     * 
     * 工程化考量:
     * 1. 输入处理: 使用高效的输入处理方式
     * 2. 边界处理: 确保数组索引正确
     * 3. 性能优化: 使用差分数组避免重复计算
     * 4. 输出格式: 按照题目要求输出结果
     */
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        int n = scanner.nextInt();
        int m = scanner.nextInt();
        
        int[] arr = new int[n];
        for (int i = 0; i < n; i++) {
            arr[i] = scanner.nextInt();
        }
        
        int[][] operations = new int[m][3];
        for (int i = 0; i < m; i++) {
            operations[i][0] = scanner.nextInt();
            operations[i][1] = scanner.nextInt();
            operations[i][2] = scanner.nextInt();
        }
        
        int[] result = processDifference(n, m, arr, operations);
        
        // 输出结果
        for (int i = 0; i < n; i++) {
            System.out.print(result[i]);
            if (i < n - 1) {
                System.out.print(" ");
            }
        }
        System.out.println();
        
        scanner.close();
    }
}