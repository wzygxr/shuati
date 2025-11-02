package class176;

import java.util.*;

/**
 * LeetCode 1814 数对统计问题的普通莫队算法实现
 * 
 * 题目描述：
 * 统计数组中满足 nums[i] + reverse(nums[j]) == nums[j] + reverse(nums[i]) 的数对 (i, j) 的个数，其中 0 <= i < j < n
 * 
 * 解题思路：
 * 1. 首先观察等式 nums[i] + reverse(nums[j]) == nums[j] + reverse(nums[i])
 * 2. 可以变形为 nums[i] - reverse(nums[i]) == nums[j] - reverse(nums[j])
 * 3. 令 a[i] = nums[i] - reverse(nums[i])，则问题转化为统计有多少对 (i, j) 满足 a[i] == a[j] 且 i < j
 * 4. 这样我们可以将问题转化为区间查询问题，使用莫队算法来优化计算
 * 
 * 时间复杂度分析：
 * - 莫队算法的时间复杂度为 O((n + q) * sqrt(n))，其中 n 是数组长度，q 是查询次数
 * - 在本题中，我们可以看作是一个离线查询，所以时间复杂度为 O(n * sqrt(n))
 * 
 * 空间复杂度分析：
 * - 存储数组、a数组、查询结构等需要 O(n) 的空间
 * - 统计频率的哈希表需要 O(n) 的空间
 * - 总体空间复杂度为 O(n)
 * 
 * 工程化考量：
 * 1. 异常处理：处理数组为空的情况
 * 2. 性能优化：使用快速输入输出，预处理所有reverse值
 * 3. 代码可读性：清晰的变量命名和详细的注释
 */
public class LeetCode1814_PairsCount_Java {
    
    // 用于存储查询的结构
    static class Query {
        int l;  // 查询的左边界
        int r;  // 查询的右边界
        int idx;  // 查询的索引，用于输出答案时保持顺序
        
        public Query(int l, int r, int idx) {
            this.l = l;
            this.r = r;
            this.idx = idx;
        }
    }
    
    // 数组的原始值
    private static int[] nums;
    // 存储nums[i] - reverse(nums[i])的值
    private static long[] a;
    // 块的大小
    private static int blockSize;
    // 用于存储每个a[i]出现的频率
    private static Map<Long, Integer> frequencyMap;
    // 当前满足条件的数对数量
    private static long currentResult;
    
    /**
     * 计算一个数的反转
     * @param num 输入的整数
     * @return 反转后的整数
     */
    private static int reverse(int num) {
        int reversed = 0;
        while (num > 0) {
            reversed = reversed * 10 + num % 10;
            num /= 10;
        }
        return reversed;
    }
    
    /**
     * 比较两个查询的顺序，用于莫队算法的排序
     * @param q1 第一个查询
     * @param q2 第二个查询
     * @return 比较结果
     */
    private static int compareQueries(Query q1, Query q2) {
        // 首先按照左边界所在的块排序
        if (q1.l / blockSize != q2.l / blockSize) {
            return Integer.compare(q1.l / blockSize, q2.l / blockSize);
        }
        // 对于同一块内的查询，按照右边界排序，偶数块升序，奇数块降序（奇偶排序优化）
        if ((q1.l / blockSize) % 2 == 0) {
            return Integer.compare(q1.r, q2.r);
        } else {
            return Integer.compare(q2.r, q1.r);
        }
    }
    
    /**
     * 添加一个元素到当前区间
     * @param pos 元素的位置
     */
    private static void add(int pos) {
        long val = a[pos];
        // 如果这个值之前已经出现过，那么新增的这个元素会与之前的所有相同值形成新的数对
        currentResult += frequencyMap.getOrDefault(val, 0);
        // 更新频率
        frequencyMap.put(val, frequencyMap.getOrDefault(val, 0) + 1);
    }
    
    /**
     * 从当前区间移除一个元素
     * @param pos 元素的位置
     */
    private static void remove(int pos) {
        long val = a[pos];
        // 先减少频率，再更新结果
        frequencyMap.put(val, frequencyMap.get(val) - 1);
        // 移除的元素会减少的数对数量等于移除前该值的频率-1（因为它不再与其他相同值形成数对）
        currentResult -= frequencyMap.get(val);
    }
    
    /**
     * 主解题函数
     * @param nums 输入数组
     * @return 满足条件的数对数量
     */
    public static int countNicePairs(int[] nums) {
        // 异常处理：空数组或单元素数组没有数对
        if (nums == null || nums.length <= 1) {
            return 0;
        }
        
        int n = nums.length;
        LeetCode1814_PairsCount_Java.nums = nums;
        
        // 预处理a数组
        a = new long[n];
        for (int i = 0; i < n; i++) {
            a[i] = nums[i] - (long)reverse(nums[i]);
        }
        
        // 创建一个查询，查询整个数组
        Query[] queries = new Query[1];
        queries[0] = new Query(0, n - 1, 0);
        
        // 计算块的大小，一般取sqrt(n)左右
        blockSize = (int)Math.sqrt(n) + 1;
        
        // 对查询进行排序
        Arrays.sort(queries, LeetCode1814_PairsCount_Java::compareQueries);
        
        // 初始化
        frequencyMap = new HashMap<>();
        currentResult = 0;
        long[] results = new long[1];
        
        // 初始化当前区间的左右指针
        int curL = 0;
        int curR = -1;
        
        // 处理每个查询
        for (Query q : queries) {
            // 调整左右指针到目标位置
            while (curL > q.l) add(--curL);
            while (curR < q.r) add(++curR);
            while (curL < q.l) remove(curL++);
            while (curR > q.r) remove(curR--);
            
            // 保存当前查询的结果
            results[q.idx] = currentResult % 1000000007;  // 题目要求取模
        }
        
        return (int)results[0];
    }
    
    /**
     * 主函数，用于测试
     */
    public static void main(String[] args) {
        // 测试用例1
        int[] nums1 = {42, 11, 1, 97};
        System.out.println("Test Case 1: " + countNicePairs(nums1));  // 预期输出: 2
        
        // 测试用例2
        int[] nums2 = {13, 10, 35, 24, 76};
        System.out.println("Test Case 2: " + countNicePairs(nums2));  // 预期输出: 4
        
        // 边界测试用例
        int[] nums3 = {1};
        System.out.println("Test Case 3 (Single element): " + countNicePairs(nums3));  // 预期输出: 0
        
        int[] nums4 = {};
        System.out.println("Test Case 4 (Empty array): " + countNicePairs(nums4));  // 预期输出: 0
    }
}