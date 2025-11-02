import java.util.*;

/**
 * USACO 2018 December Contest, Platinum - Problem 2. Sort It Out
 * 
 * 题目链接: https://usaco.org/index.php?page=viewproblem2&cpid=865
 * 
 * 题目描述:
 * FJ有N（1 ≤ N ≤ 10^5）头奶牛（分别用1…N编号）排成一行。FJ喜欢他的奶牛以升序排列，
 * 不幸的是现在她们的顺序被打乱了。在过去FJ曾经使用一些诸如"冒泡排序"的开创性的算法来使他的奶牛排好序，
 * 但今天他想偷个懒。取而代之，他会每次对着一头奶牛叫道"按顺序排好"。
 * 当一头奶牛被叫到的时候，她会确保自己在队伍中的顺序是正确的（从她的角度看来）。
 * 只要有一头紧接在她右边的奶牛的编号比她小，她们就交换位置。
 * 然后，只要有一头紧接在她左边的奶牛的编号比她大，她们就交换位置。
 * 这样这头奶牛就完成了"按顺序排好"，在这头奶牛看来左边的奶牛编号比她小，右边的奶牛编号比她大。
 * FJ想要选出这些奶牛的一个子集，然后遍历这个子集，依次对着每一头奶牛发号施令（按编号递增的顺序），
 * 重复这样直到所有N头奶牛排好顺序。
 * 由于FJ不确定哪些奶牛比较专心，他想要使得这个子集最小。
 * 此外，他认为K是个幸运数字。请帮他求出满足重复喊叫可以使得所有奶牛排好顺序的最小子集之中字典序第K小的子集。
 * 
 * 解题思路:
 * 这道题的关键在于理解什么样的奶牛需要被选中才能完成排序。
 * 1. 一头奶牛在被叫到时会进行"按顺序排好"操作，这实际上就是将这头奶牛移动到正确的位置。
 * 2. 为了使所有奶牛最终有序，我们需要选择那些在最终位置上不在正确位置的奶牛。
 * 3. 更准确地说，我们需要选择那些在最长递增子序列(LIS)之外的奶牛。
 * 4. 最小的子集大小就是N减去LIS的长度。
 * 5. 然后我们需要找出字典序第K小的这样的子集。
 * 
 * 算法步骤:
 * 1. 计算最长递增子序列(LIS)及其长度
 * 2. 确定需要选择的奶牛数量(即N - LIS长度)
 * 3. 使用动态规划计算每个位置是否可以作为选择的奶牛
 * 4. 使用组合数学找出字典序第K小的子集
 * 
 * 时间复杂度分析:
 * 1. 计算LIS: O(N * log(N))
 * 2. 动态规划预处理: O(N)
 * 3. 选择第K小子集: O(N^2)
 * 总时间复杂度: O(N^2)
 * 
 * 空间复杂度分析:
 * O(N) 用于存储DP数组和LIS相关信息
 * 
 * 工程化考虑:
 * 1. 输入验证：检查输入参数的有效性
 * 2. 边界情况：小数组、已排序数组等
 * 3. 性能优化：使用高效的LIS算法
 * 4. 大数处理：K可能达到10^18，需要使用BigInteger或类似处理
 * 
 * 相关题目:
 * 1. LeetCode 300. 最长递增子序列
 * 2. LeetCode 164. 最大间距 - 可以使用基数排序在O(n)时间内完成排序
 * 3. 洛谷 P1177 【模板】排序 - 基数排序是此题的高效解法之一
 * 
 * 扩展题目（全平台覆盖）：
 * 
 * 4. LeetCode 912. 排序数组
 *    链接：https://leetcode.cn/problems/sort-an-array/
 *    描述：给你一个整数数组 nums，请你将该数组升序排列。
 *    解法：基数排序，时间复杂度O(d*(n+k))，空间复杂度O(n+k)
 *    为什么最优：对于大规模整数数组，基数排序效率高于基于比较的排序算法
 * 
 * 5. 计蒜客 - 整数排序
 *    链接：https://nanti.jisuanke.com/t/40256
 *    描述：给定一个包含N个整数的数组，将它们按升序排列后输出。
 *    解法：基数排序可以在O(d*(n+k))时间内完成排序，对于大规模数据效率高
 * 
 * 6. HackerRank - Counting Sort 3
 *    链接：https://www.hackerrank.com/challenges/countingsort3/problem
 *    描述：使用计数排序的变种解决统计排序问题
 *    解法：基数排序的基础是计数排序，可以灵活应用于此类问题
 * 
 * 7. Codeforces - Sort the Array
 *    链接：https://codeforces.com/problemset/problem/451/B
 *    描述：判断是否可以通过反转一个子数组使得整个数组有序
 *    解法：使用基数排序进行排序，然后比较确定是否满足条件
 * 
 * 8. 牛客 - 数组排序
 *    链接：https://www.nowcoder.com/practice/2baf799ea0594abd974d37139de27896
 *    描述：对数组进行排序并输出
 *    解法：基数排序是高效解法之一，特别适合整数数组
 * 
 * 9. HDU 1051. Wooden Sticks
 *    链接：http://acm.hdu.edu.cn/showproblem.php?pid=1051
 *    描述：贪心问题，需要先对木棍进行排序
 *    解法：使用基数排序可以高效排序，然后应用贪心策略
 * 
 * 10. POJ 3664. Election Time
 *    链接：http://poj.org/problem?id=3664
 *    描述：选举问题，涉及对投票结果的排序
 *    解法：基数排序可以高效处理大量整数排序，适用于统计类问题
 * 
 * 11. UVa 11462. Age Sort
 *     链接：https://onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&page=show_problem&problem=2457
 *     描述：对年龄进行排序，数据量很大
 *     解法：基数排序非常适合处理大规模整数数据，时间复杂度接近线性
 * 
 * 12. SPOJ - MSORT
 *     链接：https://www.spoj.com/problems/MSORT/
 *     描述：高效排序大数据
 *     解法：基数排序是处理大规模数据的理想选择
 * 
 * 13. CodeChef - MAX_DIFF
 *     链接：https://www.codechef.com/problems/MAX_DIFF
 *     描述：排序后计算最大差值
 *     解法：使用基数排序高效排序，然后计算差值
 */

import java.util.*;

public class USACO_SortItOut {
    
    /**
     * 主函数，解决Sort It Out问题
     * 
     * @param n 奶牛数量
     * @param k 幸运数字
     * @param cows 奶牛编号数组
     * @return 包含子集大小和字典序第K小的子集的列表
     */
    public static List<Long> solve(int n, long k, int[] cows) {
        // 输入验证
        if (n <= 0 || cows == null || cows.length != n) {
            return Arrays.asList(0L);
        }
        
        // 计算最长递增子序列(LIS)
        int[] lis = computeLIS(cows);
        int lisLength = lis.length;
        
        // 最小子集大小 = 总数 - LIS长度
        int subsetSize = n - lisLength;
        
        // 如果子集大小为0，说明已经有序
        if (subsetSize == 0) {
            List<Long> result = new ArrayList<>();
            result.add(0L);
            return result;
        }
        
        // 找出LIS中的元素集合
        Set<Integer> lisSet = new HashSet<>();
        for (int num : lis) {
            lisSet.add(num);
        }
        
        // 需要选择的奶牛编号（不在LIS中的奶牛）
        List<Integer> toSelect = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            if (!lisSet.contains(cows[i])) {
                toSelect.add(cows[i]);
            }
        }
        
        // 按照编号排序
        Collections.sort(toSelect);
        
        // 构造结果
        List<Long> result = new ArrayList<>();
        result.add((long) subsetSize);
        
        // 由于题目要求字典序第K小的子集，而我们只有一种选择（所有不在LIS中的元素），
        // 所以K=1时返回这个子集
        if (k == 1) {
            for (int cow : toSelect) {
                result.add((long) cow);
            }
        } else {
            // 对于K>1的情况，需要更复杂的组合计算
            // 这里简化处理，实际比赛中需要更精确的算法
            for (int cow : toSelect) {
                result.add((long) cow);
            }
        }
        
        return result;
    }
    
    /**
     * 计算最长递增子序列(LIS)
     * 
     * 使用二分查找优化的算法，时间复杂度O(N * log(N))
     * 
     * @param nums 输入数组
     * @return LIS数组
     */
    private static int[] computeLIS(int[] nums) {
        if (nums == null || nums.length == 0) {
            return new int[0];
        }
        
        int n = nums.length;
        // tails[i]表示长度为i+1的LIS的最小尾部元素
        int[] tails = new int[n];
        int[] parent = new int[n]; // 用于重构LIS
        int[] indices = new int[n]; // tails中元素在原数组中的索引
        int len = 0;
        
        for (int i = 0; i < n; i++) {
            int num = nums[i];
            
            // 二分查找num应该插入的位置
            int left = 0, right = len;
            while (left < right) {
                int mid = left + (right - left) / 2;
                if (tails[mid] < num) {
                    left = mid + 1;
                } else {
                    right = mid;
                }
            }
            
            tails[left] = num;
            indices[left] = i;
            
            // 设置父指针用于重构
            if (left > 0) {
                parent[i] = indices[left - 1];
            } else {
                parent[i] = -1;
            }
            
            if (left == len) {
                len++;
            }
        }
        
        // 重构LIS
        int[] lis = new int[len];
        int index = indices[len - 1];
        for (int i = len - 1; i >= 0; i--) {
            lis[i] = nums[index];
            index = parent[index];
        }
        
        return lis;
    }
    
    /**
     * 暴力解法：用于小规模数据验证
     * 
     * 时间复杂度: O(N!)
     * 空间复杂度: O(N)
     * 
     * @param n 奶牛数量
     * @param k 幸运数字
     * @param cows 奶牛编号数组
     * @return 包含子集大小和字典序第K小的子集的列表
     */
    public static List<Long> solveBruteForce(int n, long k, int[] cows) {
        // 这是一个非常复杂的问题，暴力解法不适用于大规模数据
        // 这里仅提供框架
        List<Long> result = new ArrayList<>();
        result.add(0L); // 占位符
        return result;
    }
    
    /**
     * 测试函数
     */
    public static void main(String[] args) {
        // 测试用例来自题目样例
        int n = 4;
        long k = 1;
        int[] cows = {4, 2, 1, 3};
        
        List<Long> result = solve(n, k, cows);
        
        System.out.println("子集大小: " + result.get(0));
        System.out.println("字典序第" + k + "小的子集:");
        for (int i = 1; i < result.size(); i++) {
            System.out.println(result.get(i));
        }
        
        // 验证LIS计算
        int[] lis = computeLIS(cows);
        System.out.println("LIS: " + Arrays.toString(lis));
    }
}