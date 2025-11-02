package class072;

/**
 * 使数组K递增的最少操作次数 - 优化版本
 * 
 * 题目来源：LeetCode 2100. 使数组K递增的最少操作次数
 * 题目链接：https://leetcode.cn/problems/minimum-operations-to-make-the-array-k-increasing/
 * 题目描述：给定一个下标从 0 开始包含 n 个正整数的数组 arr，和一个正整数 k。
 * 如果对于每个满足 k <= i <= n-1 的下标 i，都有 arr[i-k] <= arr[i]，那么称 arr 是 K 递增的。
 * 每一次操作中，你可以选择一个下标 i 并将 arr[i] 改成任意正整数。
 * 请你返回对于给定的 k，使数组变成 K 递增的最少操作次数。
 * 
 * 算法思路：
 * 1. 将数组按照间隔 k 分成 k 组，每组内的元素需要满足递增关系
 * 2. 对每组分别计算最少操作次数，累加得到结果
 * 3. 每组的最少操作次数 = 组长度 - 组内最长不下降子序列长度
 *    - 最长不下降子序列可以保留不动
 *    - 其余元素需要修改
 * 4. 使用贪心+二分查找优化计算最长不下降子序列
 * 
 * 时间复杂度：O(n * log(n/k)) - 分成 k 组，每组平均长度 n/k，每组求 LIS 需要 O((n/k) * log(n/k))
 * 空间复杂度：O(n) - 需要辅助数组存储状态
 * 是否最优解：是，这是目前最优解法
 * 
 * 示例：
 * 输入：arr = [5,4,3,2,1], k = 1
 * 输出：4
 * 解释：对于 k = 1，数组最终要变为非递减的，最少操作次数为 4。
 * 
 * 输入：arr = [4,1,5,2,6,2], k = 2
 * 输出：0
 * 解释：数组已经 K 递增。
 * 
 * 输入：arr = [4,1,5,2,6,2], k = 3
 * 输出：2
 */
public class Code08_MinimumOperationsToMakeArrayKIncreasingOptimized {

    public static int MAXN = 100001;

    public static int[] nums = new int[MAXN];

    public static int[] ends = new int[MAXN];

    /**
     * 计算使数组变成K递增的最少操作次数 - 优化版本
     * 
     * @param arr 输入数组
     * @param k 间隔参数
     * @return 最少操作次数
     */
    public static int kIncreasing(int[] arr, int k) {
        int n = arr.length;
        int ans = 0;
        // 将数组按照间隔k分成k组
        for (int i = 0, size; i < k; i++) {
            size = 0;
            // 把每一组的数字放入容器
            for (int j = i; j < n; j += k) {
                nums[size++] = arr[j];
            }
            // 当前组长度 - 当前组最长不下降子序列长度 = 当前组至少需要修改的数字个数
            ans += size - lengthOfNoDecreasing(size);
        }
        return ans;
    }

    // nums[0...size-1]中的最长不下降子序列长度
    /**
     * 计算数组中最长不下降子序列的长度 - 优化版本
     * 
     * 算法思路：
     * 1. 维护一个数组ends，ends[i]表示长度为i+1的所有不下降子序列中，结尾元素的最小值
     * 2. 贪心思想：为了让不下降子序列尽可能长，我们希望结尾元素尽可能小
     * 3. 对于每个元素num，在ends数组中二分查找<num的最左位置
     *    - 如果找不到，说明num比所有元素都大，可以延长不下降子序列
     *    - 如果找到了位置find，将ends[find]更新为num
     * 
     * 时间复杂度：O(n*logn) - 遍历n个元素，每次二分查找O(logn)
     * 空间复杂度：O(n) - 需要ends数组存储状态
     * 是否最优解：是，这是求LIS长度的最优解法
     * 
     * @param size 数组长度
     * @return 最长不下降子序列的长度
     */
    public static int lengthOfNoDecreasing(int size) {
        int len = 0;
        // 遍历数组中的每个元素
        for (int i = 0, find; i < size; i++) {
            // 在ends数组中查找<num的最左位置
            find = bs(len, nums[i]);
            // 如果找不到，说明nums[i]比所有元素都大，可以延长不下降子序列
            if (find == -1) {
                ends[len++] = nums[i];
            } else {
                // 如果找到了位置，更新该位置的值为nums[i]
                ends[find] = nums[i];
            }
        }
        return len;
    }

    /**
     * 在不降序数组ends中查找<num的最左位置
     * 
     * 算法思路：
     * 1. 使用二分查找在有序数组中查找目标值
     * 2. 维护左边界l和右边界r
     * 3. 计算中间位置m，比较ends[m]与num的大小关系
     * 4. 如果num < ends[m]，说明目标位置在左半部分（包括m），更新ans和r
     * 5. 否则目标位置在右半部分，更新l
     * 
     * 时间复杂度：O(logn) - 标准二分查找
     * 空间复杂度：O(1) - 只使用常数额外空间
     * 是否最优解：是，这是标准的二分查找实现
     * 
     * @param len 有效长度
     * @param num 目标值
     * @return <num的最左位置，如果不存在返回-1
     */
    public static int bs(int len, int num) {
        int l = 0, r = len - 1, m, ans = -1;
        while (l <= r) {
            m = (l + r) / 2;
            // 如果num < ends[m]，记录当前位置并继续在左半部分查找
            if (num < ends[m]) {
                ans = m;
                r = m - 1;
            } else {
                // 否则在右半部分查找
                l = m + 1;
            }
        }
        return ans;
    }

    // 测试方法
    public static void main(String[] args) {
        // 测试用例1
        int[] arr1 = {5, 4, 3, 2, 1};
        int k1 = 1;
        System.out.println("输入: arr = [5,4,3,2,1], k = 1");
        System.out.println("输出: " + kIncreasing(arr1, k1));
        System.out.println("期望: 4");
        System.out.println();
        
        // 测试用例2
        int[] arr2 = {4, 1, 5, 2, 6, 2};
        int k2 = 2;
        System.out.println("输入: arr = [4,1,5,2,6,2], k = 2");
        System.out.println("输出: " + kIncreasing(arr2, k2));
        System.out.println("期望: 0");
        System.out.println();
        
        // 测试用例3
        int[] arr3 = {4, 1, 5, 2, 6, 2};
        int k3 = 3;
        System.out.println("输入: arr = [4,1,5,2,6,2], k = 3");
        System.out.println("输出: " + kIncreasing(arr3, k3));
        System.out.println("期望: 2");
        System.out.println();
    }
}