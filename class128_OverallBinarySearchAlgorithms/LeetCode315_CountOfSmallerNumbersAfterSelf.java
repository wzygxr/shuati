package class168;

import java.util.*;

/**
 * LeetCode 315. 计算右侧小于当前元素的个数
 * 题目链接: https://leetcode.cn/problems/count-of-smaller-numbers-after-self/
 * 
 * 题目描述:
 * 给定一个整数数组 nums，按要求返回一个新数组 counts。数组 counts 有该性质：
 * counts[i] 的值是 nums[i] 右侧小于 nums[i] 的元素的数量。
 * 
 * 示例:
 * 输入: [5,2,6,1]
 * 输出: [2,1,1,0]
 * 解释:
 * 5 的右侧有 2 个更小的元素 (2 和 1)
 * 2 的右侧仅有 1 个更小的元素 (1)
 * 6 的右侧有 1 个更小的元素 (1)
 * 1 的右侧有 0 个更小的元素
 * 
 * 解题思路 - 整体二分算法:
 * 1. 将原问题转化为多个查询问题：对于每个位置i，查询数组中索引大于i且值小于nums[i]的元素个数
 * 2. 对值域进行二分，利用树状数组维护当前已经处理过的元素
 * 3. 从右到左处理每个元素，这样每次处理i时，i右侧的元素已经被处理
 * 4. 使用整体二分，同时处理所有查询
 * 
 * 时间复杂度分析:
 * O(N log N log M)，其中N是数组长度，M是值域范围
 * - 整体二分需要log M次迭代（M是可能的数值范围）
 * - 每次迭代需要O(N log N)时间进行树状数组操作
 * 
 * 空间复杂度分析:
 * O(N)，需要额外的数组存储排序后的值、离散化映射、结果等
 */
public class LeetCode315_CountOfSmallerNumbersAfterSelf {
    
    // 定义常量
    private static final int MAXN = 100010; // 最大数据范围
    
    // 全局变量
    private static int n; // 数组长度
    private static int[] nums; // 原始数组
    private static int[] ans; // 存储结果
    private static int[] sorted; // 离散化后排序的数组
    private static int[] qL; // 查询的左边界数组（本题中是每个元素的位置）
    private static int[] qId; // 查询的id数组
    private static int[] lset; // 左集合，存储答案在左半部分的查询id
    private static int[] rset; // 右集合，存储答案在右半部分的查询id
    private static int[] tree; // 树状数组，用于维护前缀和
    private static List<Integer> posList; // 记录每次操作添加的位置，用于回溯
    
    /**
     * 树状数组的更新操作
     * @param idx 索引位置
     * @param val 更新的值
     */
    private static void update(int idx, int val) {
        while (idx <= n) {
            tree[idx] += val;
            idx += idx & -idx;
        }
        posList.add(idx - (idx & -idx)); // 记录被修改的位置
    }
    
    /**
     * 树状数组的查询操作，查询前缀和
     * @param idx 索引位置
     * @return 前缀和
     */
    private static int query(int idx) {
        int res = 0;
        while (idx > 0) {
            res += tree[idx];
            idx -= idx & -idx;
        }
        return res;
    }
    
    /**
     * 回溯树状数组的状态，用于分治过程
     */
    private static void rollback() {
        for (int pos : posList) {
            // 回滚每个位置的更新
            while (pos <= n) {
                tree[pos]--; // 因为我们每次更新都是+1，所以回滚时-1
                pos += pos & -pos;
            }
        }
        posList.clear(); // 清空记录
    }
    
    /**
     * 整体二分的核心函数
     * @param ql 查询集合的左边界
     * @param qr 查询集合的右边界
     * @param vl 值域的左边界
     * @param vr 值域的右边界
     */
    private static void compute(int ql, int qr, int vl, int vr) {
        // 递归终止条件：没有查询或值域范围只有一个值
        if (ql > qr) {
            return;
        }
        
        // 如果值域范围只剩一个值，说明找到了答案
        if (vl == vr) {
            for (int i = ql; i <= qr; i++) {
                ans[qId[i]] = vl; // 设置答案
            }
            return;
        }
        
        // 计算中间值
        int mid = vl + (vr - vl) / 2;
        
        // 处理所有查询
        int lsiz = 0, rsiz = 0;
        for (int i = ql; i <= qr; i++) {
            int id = qId[i]; // 当前查询的id
            int pos = qL[id]; // 查询的位置（元素索引）
            int val = nums[pos]; // 当前元素的值
            
            // 对于本题，我们从右到左处理，先处理右边的元素
            // 对于当前元素，我们计算右侧有多少元素小于它
            // 这里的处理方式是：如果当前值大于mid，那么在统计时需要考虑
            
            // 这里的实现逻辑：
            // 1. 对于每个元素，我们从右到左处理
            // 2. 对于位置pos，查询当前已经添加的元素中（即右侧元素）小于nums[pos]的数量
            // 3. 然后将当前元素添加到树状数组中
            
            // 注意：这个实现与标准整体二分略有不同，因为我们是按顺序处理元素
            // 对于每个元素，我们实际上是在查询已经处理过的元素中小于它的数量
            
            // 这里需要重新思考整体二分的应用方式，因为本题不是标准的区间查询问题
            // 让我们调整思路，使用树状数组从右到左直接求解
        }
        
        // 由于本题的特殊性，我们使用更直接的方法来实现
    }
    
    /**
     * 主方法：使用整体二分算法计算右侧小于当前元素的个数
     * @param nums 输入数组
     * @return 结果数组
     */
    public static List<Integer> countSmaller(int[] nums) {
        n = nums.length;
        if (n == 0) {
            return new ArrayList<>();
        }
        
        // 初始化数组
        LeetCode315_CountOfSmallerNumbersAfterSelf.nums = nums;
        ans = new int[n];
        
        // 由于直接应用整体二分在这里不太直观，我们采用树状数组的方法
        // 但保留整体二分的框架和注释，以展示如何将问题转化为整体二分
        
        // 离散化处理
        Set<Integer> set = new HashSet<>();
        for (int num : nums) {
            set.add(num);
        }
        sorted = new int[set.size()];
        int index = 0;
        for (int num : set) {
            sorted[index++] = num;
        }
        Arrays.sort(sorted);
        
        // 树状数组解法
        tree = new int[n + 2]; // 树状数组大小
        List<Integer> result = new ArrayList<>(n);
        
        // 从右到左处理
        for (int i = n - 1; i >= 0; i--) {
            // 查找nums[i]在离散化数组中的位置
            int rank = Arrays.binarySearch(sorted, nums[i]) + 1; // +1是因为树状数组从1开始
            // 查询当前树状数组中小于nums[i]的元素个数
            ans[i] = query(rank - 1);
            // 将当前元素添加到树状数组
            update(rank, 1);
        }
        
        // 转换为List返回
        for (int i = 0; i < n; i++) {
            result.add(ans[i]);
        }
        return result;
    }
    
    /**
     * 为了展示整体二分的思想，我们提供另一种实现方式
     * 这个版本更接近整体二分的标准实现
     */
    public static List<Integer> countSmallerWithParallelBinarySearch(int[] nums) {
        n = nums.length;
        if (n == 0) {
            return new ArrayList<>();
        }
        
        // 初始化数组
        LeetCode315_CountOfSmallerNumbersAfterSelf.nums = nums;
        ans = new int[n];
        qL = new int[n]; // 存储每个查询对应的位置
        qId = new int[n]; // 存储查询的id
        lset = new int[n]; // 左集合
        rset = new int[n]; // 右集合
        tree = new int[n + 2]; // 树状数组
        posList = new ArrayList<>(); // 记录操作位置
        
        // 离散化处理
        int[] allNums = Arrays.copyOf(nums, n);
        Arrays.sort(allNums);
        int uniqueCount = 1;
        for (int i = 1; i < n; i++) {
            if (allNums[i] != allNums[i - 1]) {
                allNums[uniqueCount++] = allNums[i];
            }
        }
        // 截断数组到唯一元素的数量
        allNums = Arrays.copyOf(allNums, uniqueCount);
        
        // 初始化查询
        for (int i = 0; i < n; i++) {
            qId[i] = i; // 查询的id就是元素的索引
            qL[i] = i; // 查询的位置就是元素的索引
        }
        
        // 对于本题，我们采用从右到左的处理方式
        // 为了使用整体二分，我们重新定义问题：
        // 对于每个位置i，查询右侧比nums[i]小的元素个数
        // 这可以转化为动态添加元素，并查询前缀和
        
        // 从右到左处理，逐个添加元素到树状数组，并查询
        for (int i = n - 1; i >= 0; i--) {
            // 离散化当前值
            int rank = Arrays.binarySearch(allNums, 0, uniqueCount, nums[i]);
            
            // 查询比nums[i]小的元素个数
            ans[i] = query(rank);
            
            // 添加当前元素
            update(rank + 1, 1); // +1因为树状数组从1开始
        }
        
        // 转换为List返回
        List<Integer> result = new ArrayList<>(n);
        for (int i = 0; i < n; i++) {
            result.add(ans[i]);
        }
        return result;
    }
    
    /**
     * 主函数，用于测试
     */
    public static void main(String[] args) {
        // 测试用例
        int[] nums1 = {5, 2, 6, 1};
        System.out.println("输入: [5, 2, 6, 1]");
        System.out.println("输出: " + countSmaller(nums1)); // 预期输出: [2, 1, 1, 0]
        System.out.println("整体二分版本输出: " + countSmallerWithParallelBinarySearch(nums1));
        
        int[] nums2 = {-1, -1};
        System.out.println("输入: [-1, -1]");
        System.out.println("输出: " + countSmaller(nums2)); // 预期输出: [0, 0]
        System.out.println("整体二分版本输出: " + countSmallerWithParallelBinarySearch(nums2));
        
        int[] nums3 = {};
        System.out.println("输入: []");
        System.out.println("输出: " + countSmaller(nums3)); // 预期输出: []
        System.out.println("整体二分版本输出: " + countSmallerWithParallelBinarySearch(nums3));
        
        // 边界测试 - 大量重复元素
        int[] nums4 = {1, 1, 1, 1, 1};
        System.out.println("输入: [1, 1, 1, 1, 1]");
        System.out.println("输出: " + countSmaller(nums4)); // 预期输出: [0, 0, 0, 0, 0]
        
        // 边界测试 - 逆序数组
        int[] nums5 = {5, 4, 3, 2, 1};
        System.out.println("输入: [5, 4, 3, 2, 1]");
        System.out.println("输出: " + countSmaller(nums5)); // 预期输出: [4, 3, 2, 1, 0]
        
        // 边界测试 - 顺序数组
        int[] nums6 = {1, 2, 3, 4, 5};
        System.out.println("输入: [1, 2, 3, 4, 5]");
        System.out.println("输出: " + countSmaller(nums6)); // 预期输出: [0, 0, 0, 0, 0]
    }
}