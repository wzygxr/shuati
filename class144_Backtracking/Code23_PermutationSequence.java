package class038;

import java.util.ArrayList;
import java.util.List;

/**
 * LeetCode 60. 排列序列
 * 
 * 题目描述：
 * 给出集合 [1,2,3,...,n]，其所有元素共有 n! 种排列。
 * 按大小顺序列出所有排列情况，并一一标记，当 n = 3 时, 所有排列如下：
 * "123", "132", "213", "231", "312", "321"
 * 给定 n 和 k，返回第 k 个排列。
 * 
 * 示例：
 * 输入：n = 3, k = 3
 * 输出："213"
 * 
 * 输入：n = 4, k = 9
 * 输出："2314"
 * 
 * 输入：n = 3, k = 1
 * 输出："123"
 * 
 * 提示：
 * 1 <= n <= 9
 * 1 <= k <= n!
 * 
 * 链接：https://leetcode.cn/problems/permutation-sequence/
 * 
 * 算法思路：
 * 1. 使用数学方法（康托展开）直接计算第k个排列
 * 2. 对于n个数字，第一个位置有n种选择，每种选择对应(n-1)!个排列
 * 3. 通过计算k所在的区间确定第一个数字
 * 4. 递归处理剩余数字
 * 
 * 时间复杂度：O(n^2)，需要遍历n个位置，每个位置需要O(n)时间确定数字
 * 空间复杂度：O(n)，用于存储可用数字和结果
 */
public class Code23_PermutationSequence {

    /**
     * 获取第k个排列
     * 
     * @param n 数字个数
     * @param k 排列序号
     * @return 第k个排列的字符串表示
     */
    public static String getPermutation(int n, int k) {
        // 计算阶乘数组
        int[] factorial = new int[n + 1];
        factorial[0] = 1;
        for (int i = 1; i <= n; i++) {
            factorial[i] = factorial[i - 1] * i;
        }
        
        // 创建可用数字列表
        List<Integer> numbers = new ArrayList<>();
        for (int i = 1; i <= n; i++) {
            numbers.add(i);
        }
        
        // 康托展开计算排列
        StringBuilder result = new StringBuilder();
        k--;  // 转换为0-based索引
        
        for (int i = n; i >= 1; i--) {
            int index = k / factorial[i - 1];
            result.append(numbers.get(index));
            numbers.remove(index);
            k %= factorial[i - 1];
        }
        
        return result.toString();
    }

    /**
     * 解法二：使用回溯算法（适用于小规模数据）
     * 生成所有排列直到第k个
     * 
     * @param n 数字个数
     * @param k 排列序号
     * @return 第k个排列的字符串表示
     */
    public static String getPermutation2(int n, int k) {
        int[] count = {0};
        StringBuilder result = new StringBuilder();
        boolean[] used = new boolean[n + 1];
        backtrack(n, k, used, new StringBuilder(), count, result);
        return result.toString();
    }

    private static boolean backtrack(int n, int k, boolean[] used, StringBuilder path, int[] count, StringBuilder result) {
        // 终止条件：生成完整排列
        if (path.length() == n) {
            count[0]++;
            if (count[0] == k) {
                result.append(path);
                return true;
            }
            return false;
        }
        
        for (int i = 1; i <= n; i++) {
            if (!used[i]) {
                used[i] = true;
                path.append(i);
                
                if (backtrack(n, k, used, path, count, result)) {
                    return true;
                }
                
                path.deleteCharAt(path.length() - 1);
                used[i] = false;
            }
        }
        
        return false;
    }

    /**
     * 解法三：使用迭代法生成排列
     * 使用字典序算法生成第k个排列
     * 
     * @param n 数字个数
     * @param k 排列序号
     * @return 第k个排列的字符串表示
     */
    public static String getPermutation3(int n, int k) {
        // 生成初始排列（最小排列）
        char[] arr = new char[n];
        for (int i = 0; i < n; i++) {
            arr[i] = (char) ('1' + i);
        }
        
        // 使用字典序算法生成第k个排列
        for (int i = 1; i < k; i++) {
            nextPermutation(arr);
        }
        
        return new String(arr);
    }

    private static void nextPermutation(char[] arr) {
        int n = arr.length;
        
        // 1. 从右向左找到第一个降序的位置
        int i = n - 2;
        while (i >= 0 && arr[i] >= arr[i + 1]) {
            i--;
        }
        
        if (i >= 0) {
            // 2. 从右向左找到第一个大于arr[i]的数字
            int j = n - 1;
            while (j >= 0 && arr[j] <= arr[i]) {
                j--;
            }
            
            // 3. 交换arr[i]和arr[j]
            swap(arr, i, j);
        }
        
        // 4. 反转i+1到末尾的部分
        reverse(arr, i + 1, n - 1);
    }

    private static void swap(char[] arr, int i, int j) {
        char temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }

    private static void reverse(char[] arr, int start, int end) {
        while (start < end) {
            swap(arr, start, end);
            start++;
            end--;
        }
    }

    // 测试方法
    public static void main(String[] args) {
        // 测试用例1
        int n1 = 3, k1 = 3;
        String result1 = getPermutation(n1, k1);
        System.out.println("输入: n = " + n1 + ", k = " + k1);
        System.out.println("输出: \"" + result1 + "\"");
        
        // 测试用例2
        int n2 = 4, k2 = 9;
        String result2 = getPermutation(n2, k2);
        System.out.println("\n输入: n = " + n2 + ", k = " + k2);
        System.out.println("输出: \"" + result2 + "\"");
        
        // 测试用例3
        int n3 = 3, k3 = 1;
        String result3 = getPermutation(n3, k3);
        System.out.println("\n输入: n = " + n3 + ", k = " + k3);
        System.out.println("输出: \"" + result3 + "\"");
        
        // 测试解法二
        System.out.println("\n=== 解法二测试 ===");
        String result4 = getPermutation2(n1, k1);
        System.out.println("输入: n = " + n1 + ", k = " + k1);
        System.out.println("输出: \"" + result4 + "\"");
        
        // 测试解法三
        System.out.println("\n=== 解法三测试 ===");
        String result5 = getPermutation3(n1, k1);
        System.out.println("输入: n = " + n1 + ", k = " + k1);
        System.out.println("输出: \"" + result5 + "\"");
    }
}