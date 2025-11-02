import java.util.*;

/**
 * LeetCode 2343. 裁剪数字后查询第K小的数字
 * 
 * 题目链接: https://leetcode.cn/problems/query-kth-smallest-trimmed-number/
 * 
 * 题目描述:
 * 给你一个下标从0开始的字符串数组nums，其中每个字符串长度相等且只包含数字。
 * 再给你一个下标从0开始的二维整数数组queries，其中queries[i] = [ki, trimi]。
 * 对于每个查询，你需要将nums中的每个数字裁剪到剩下最右边trimi个数位。
 * 在裁剪过后的数字中，找到nums中第ki小数字对应的下标。如果两个裁剪后数字一样大，那么下标较小的数字更小。
 * 返回一个数组answer，其中answer[i]是第i个查询的答案。
 * 
 * 解题思路:
 * 1. 对于每个查询，我们需要：
 *    a. 裁剪所有数字到指定长度
 *    b. 找到第k小的数字及其索引
 * 2. 可以使用基数排序来优化，因为所有数字都是相同长度的字符串
 * 3. 对于每个trim值，我们可以预先计算排序结果，避免重复计算
 * 
 * 时间复杂度分析:
 * 设nums长度为n，每个字符串长度为m，查询次数为q
 * 1. 暴力解法：每次查询都需要O(n*log(n))时间排序，总时间复杂度为O(q*n*log(n))
 * 2. 基数排序优化：对每个trim值进行一次基数排序，时间复杂度为O(m*(n+10))，总时间复杂度为O(m*(n+10)*max_trim + q*n)
 * 
 * 空间复杂度分析:
 * O(n*m) 用于存储裁剪后的数字和索引
 * 
 * 工程化考虑:
 * 1. 输入验证：检查输入参数的有效性
 * 2. 边界情况：空数组、单元素数组等
 * 3. 性能优化：使用基数排序处理大量查询
 * 4. 内存优化：复用数组避免重复分配
 * 
 * 相关题目:
 * 1. LeetCode 164. 最大间距 - 可以使用基数排序在O(n)时间内完成排序
 * 2. LeetCode 912. 排序数组 - 基数排序是此题的高效解法之一
 * 3. 洛谷 P1177 【模板】排序 - 基数排序是此题的高效解法之一
 * 
 * 扩展题目（全平台覆盖）：
 * 
 * 4. 计蒜客 - 整数排序
 *    链接：https://nanti.jisuanke.com/t/40256
 *    描述：给定一个包含N个整数的数组，将它们按升序排列后输出。
 *    解法：基数排序可以在O(d*(n+k))时间内完成排序，对于大规模数据效率高
 * 
 * 5. HackerRank - Counting Sort 3
 *    链接：https://www.hackerrank.com/challenges/countingsort3/problem
 *    描述：使用计数排序的变种解决统计排序问题
 *    解法：基数排序的基础是计数排序，可以灵活应用于此类问题
 * 
 * 6. Codeforces - Sort the Array
 *    链接：https://codeforces.com/problemset/problem/451/B
 *    描述：判断是否可以通过反转一个子数组使得整个数组有序
 *    解法：使用基数排序进行排序，然后比较确定是否满足条件
 * 
 * 7. 牛客 - 数组排序
 *    链接：https://www.nowcoder.com/practice/2baf799ea0594abd974d37139de27896
 *    描述：对数组进行排序并输出
 *    解法：基数排序是高效解法之一，特别适合整数数组
 * 
 * 8. HDU 1051. Wooden Sticks
 *    链接：http://acm.hdu.edu.cn/showproblem.php?pid=1051
 *    描述：贪心问题，需要先对木棍进行排序
 *    解法：使用基数排序可以高效排序，然后应用贪心策略
 * 
 * 9. POJ 3664. Election Time
 *    链接：http://poj.org/problem?id=3664
 *    描述：选举问题，涉及对投票结果的排序
 *    解法：基数排序可以高效处理大量整数排序，适用于统计类问题
 * 
 * 10. UVa 11462. Age Sort
 *     链接：https://onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&page=show_problem&problem=2457
 *     描述：对年龄进行排序，数据量很大
 *     解法：基数排序非常适合处理大规模整数数据，时间复杂度接近线性
 * 
 * 11. USACO 2018 December Platinum - Sort It Out
 *     题目类型：最长递增子序列问题结合基数排序优化
 *     解法：使用O(N*logN)的LIS算法，结合基数排序进行优化
 * 
 * 12. USACO 2018 Open Gold - OutOf Sorts
 *     题目类型：模拟优化问题，涉及排序算法分析
 *     解法：分析冒泡排序的优化版本，使用基数排序验证结果
 * 
 * 13. SPOJ - MSORT
 *     链接：https://www.spoj.com/problems/MSORT/
 *     描述：高效排序大数据
 *     解法：基数排序是处理大规模数据的理想选择
 * 
 * 14. CodeChef - MAX_DIFF
 *     链接：https://www.codechef.com/problems/MAX_DIFF
 *     描述：排序后计算最大差值
 *     解法：使用基数排序高效排序，然后计算差值
 */

import java.util.*;

public class LeetCode2343_Java {
    
    /**
     * 主函数，处理查询数组
     * 
     * 算法步骤:
     * 1. 遍历所有查询
     * 2. 对每个查询执行裁剪和查找第k小元素的操作
     * 3. 将结果收集到答案数组中
     * 
     * @param nums 字符串数组，包含数字字符串
     * @param queries 查询数组，每个查询包含[k, trim]
     * @return 答案数组，每个元素对应一个查询的结果
     */
    public static int[] smallestTrimmedNumbers(String[] nums, int[][] queries) {
        // 输入验证
        if (nums == null || nums.length == 0 || queries == null || queries.length == 0) {
            return new int[0];
        }
        
        int n = nums.length;
        int[] answer = new int[queries.length];
        
        // 预处理：按trim值分组查询，避免重复计算
        Map<Integer, List<Integer>> trimToQueries = new HashMap<>();
        for (int i = 0; i < queries.length; i++) {
            int trim = queries[i][1];
            trimToQueries.computeIfAbsent(trim, k -> new ArrayList<>()).add(i);
        }
        
        // 对每个不同的trim值进行处理
        for (Map.Entry<Integer, List<Integer>> entry : trimToQueries.entrySet()) {
            int trim = entry.getKey();
            List<Integer> queryIndices = entry.getValue();
            
            // 使用基数排序对裁剪后的数字进行排序
            int[] sortedIndices = radixSortByTrim(nums, trim);
            
            // 处理所有使用相同trim值的查询
            for (int queryIndex : queryIndices) {
                int k = queries[queryIndex][0];
                // 第k小的元素在排序后的数组中的索引是k-1
                answer[queryIndex] = sortedIndices[k - 1];
            }
        }
        
        return answer;
    }
    
    /**
     * 使用基数排序对裁剪后的数字进行排序
     * 
     * 算法原理:
     * 1. 从最低位开始，对每一位进行计数排序
     * 2. 使用计数排序保证稳定性
     * 3. 重复此过程直到最高位
     * 
     * @param nums 原始数字字符串数组
     * @param trim 裁剪位数
     * @return 排序后的索引数组
     */
    private static int[] radixSortByTrim(String[] nums, int trim) {
        int n = nums.length;
        int len = nums[0].length();
        int start = len - trim; // 裁剪起始位置
        
        // 初始化索引数组
        int[] indices = new int[n];
        for (int i = 0; i < n; i++) {
            indices[i] = i;
        }
        
        // 辅助数组
        int[] tempIndices = new int[n];
        int[] count = new int[10]; // 数字0-9的计数数组
        
        // 从最低位到最高位依次进行计数排序
        for (int pos = len - 1; pos >= start; pos--) {
            // 清空计数数组
            Arrays.fill(count, 0);
            
            // 统计当前位上各数字的出现次数
            for (int i = 0; i < n; i++) {
                int digit = nums[indices[i]].charAt(pos) - '0';
                count[digit]++;
            }
            
            // 计算前缀和，得到各数字在排序后数组中的位置
            for (int i = 1; i < 10; i++) {
                count[i] += count[i - 1];
            }
            
            // 从后向前遍历，保证排序的稳定性
            for (int i = n - 1; i >= 0; i--) {
                int digit = nums[indices[i]].charAt(pos) - '0';
                tempIndices[--count[digit]] = indices[i];
            }
            
            // 将排序结果复制回原数组
            System.arraycopy(tempIndices, 0, indices, 0, n);
        }
        
        return indices;
    }
    
    /**
     * 暴力解法：对每个查询单独排序
     * 
     * 时间复杂度: O(q * n * log(n))
     * 空间复杂度: O(n)
     * 
     * 适用于查询次数较少的情况
     * 
     * @param nums 字符串数组，包含数字字符串
     * @param queries 查询数组，每个查询包含[k, trim]
     * @return 答案数组，每个元素对应一个查询的结果
     */
    public static int[] smallestTrimmedNumbersBruteForce(String[] nums, int[][] queries) {
        int[] answer = new int[queries.length];
        
        for (int i = 0; i < queries.length; i++) {
            int k = queries[i][0];
            int trim = queries[i][1];
            
            int n = nums.length;
            int len = nums[0].length();
            int start = len - trim;
            
            // 创建裁剪后的数字和索引对
            Pair[] pairs = new Pair[n];
            for (int j = 0; j < n; j++) {
                String trimmed = nums[j].substring(start);
                pairs[j] = new Pair(trimmed, j);
            }
            
            // 排序
            Arrays.sort(pairs, (a, b) -> {
                int cmp = a.num.compareTo(b.num);
                if (cmp != 0) {
                    return cmp;
                }
                return Integer.compare(a.index, b.index);
            });
            
            // 获取第k小元素的索引
            answer[i] = pairs[k - 1].index;
        }
        
        return answer;
    }
    
    /**
     * 用于存储裁剪后数字和原始索引的辅助类
     */
    static class Pair {
        String num;
        int index;
        
        Pair(String num, int index) {
            this.num = num;
            this.index = index;
        }
    }
    
    /**
     * 测试函数
     */
    public static void main(String[] args) {
        // 测试用例1
        String[] nums1 = {"102", "473", "251", "814"};
        int[][] queries1 = {{1, 1}, {2, 3}, {4, 2}, {1, 2}};
        int[] result1 = smallestTrimmedNumbers(nums1, queries1);
        System.out.println("测试用例1结果: " + Arrays.toString(result1)); // 预期: [2, 2, 1, 0]
        
        // 测试用例2
        String[] nums2 = {"24", "37", "96", "04"};
        int[][] queries2 = {{2, 1}, {2, 2}};
        int[] result2 = smallestTrimmedNumbers(nums2, queries2);
        System.out.println("测试用例2结果: " + Arrays.toString(result2)); // 预期: [3, 0]
        
        // 验证暴力解法结果一致性
        int[] result1Brute = smallestTrimmedNumbersBruteForce(nums1, queries1);
        int[] result2Brute = smallestTrimmedNumbersBruteForce(nums2, queries2);
        
        System.out.println("暴力解法测试用例1结果: " + Arrays.toString(result1Brute));
        System.out.println("暴力解法测试用例2结果: " + Arrays.toString(result2Brute));
        
        System.out.println("结果一致性验证1: " + Arrays.equals(result1, result1Brute));
        System.out.println("结果一致性验证2: " + Arrays.equals(result2, result2Brute));
    }
}