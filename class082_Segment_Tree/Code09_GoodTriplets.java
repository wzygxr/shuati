package class109;

import java.util.Arrays;

/**
 * 统计数组中好三元组数目
 * 给你两个下标从0 开始且长度为 n 的整数数组 nums1 和 nums2 ，两者都是 [0, 1, ..., n - 1] 的 排列 。
 * 好三元组 指的是 3 个 互不相同 的值，且它们在数组 nums1 和 nums2 中的位置顺序一致。
 * 请你返回好三元组的 总数目 。
 * 
 * 示例 1：
 * 输入：nums1 = [2,0,1,3], nums2 = [0,1,2,3]
 * 输出：1
 * 解释：总共有 4 个三元组 (x,y,z) 满足 pos1x < pos1y < pos1z ，分别是 (2,0,1) ，(2,0,3) ，(2,1,3) 和 (0,1,3) 。
 * 这些三元组中，只有 (0,1,3) 满足 pos2x < pos2y < pos2z 。所以只有 1 个好三元组。
 * 
 * 示例 2：
 * 输入：nums1 = [4,0,1,3,2], nums2 = [4,1,0,2,3]
 * 输出：4
 * 解释：总共有 4 个好三元组 (4,0,3) ，(4,0,2) ，(4,1,3) 和 (4,1,2) 。
 * 
 * 提示：
 * n == nums1.length == nums2.length
 * 3 <= n <= 10^5
 * 0 <= nums1[i], nums2[i] <= n - 1
 * nums1 和 nums2 是 [0, 1, ..., n - 1] 的排列。
 * 
 * 解题思路：
 * 1. 将问题转换为求公共递增子序列个数
 * 2. 对于每个元素，我们需要统计：
 *    - 在它左边有多少个元素小于它（形成升序一元组）
 *    - 在它左边有多少个元素能与它组成升序二元组
 * 3. 使用两个树状数组：
 *    - tree1[i]维护以数值i结尾的升序一元组数量（即小于i的元素个数）
 *    - tree2[i]维护以数值i结尾的升序二元组数量
 * 4. 遍历数组，对每个元素：
 *    - 查询tree2中比当前元素小的元素个数，即为以当前元素为结尾的升序三元组数量
 *    - 更新tree1中当前元素的计数
 *    - 查询tree1中比当前元素小的元素个数，更新tree2中当前元素的计数
 * 
 * 时间复杂度分析：
 * - 离散化排序：O(n log n)
 * - 遍历数组，每次操作树状数组：O(n log n)
 * - 总时间复杂度：O(n log n)
 * 
 * 空间复杂度分析：
 * - 需要额外数组存储原始数据、排序数据和两个树状数组：O(n)
 * - 所以总空间复杂度为O(n)
 * 
 * 测试链接: https://leetcode.cn/problems/count-good-triplets-in-an-array/
 */
public class Code09_GoodTriplets {
    
    // 最大数组长度
    public static int MAXN = 100001;
    
    // 原数组
    public static int[] arr = new int[MAXN];
    
    // 排序数组，用于离散化
    public static int[] sort = new int[MAXN];
    
    // 维护信息 : 课上讲的up1数组
    // tree1不是up1数组，是up1数组的树状数组
    // tree1[i]表示值小于等于i的元素个数（升序一元组数量）
    public static long[] tree1 = new long[MAXN];
    
    // 维护信息 : 课上讲的up2数组
    // tree2不是up2数组，是up2数组的树状数组
    // tree2[i]表示以值i结尾的升序二元组数量
    public static long[] tree2 = new long[MAXN];
    
    // 数组长度和离散化后数组长度
    public static int n, m;
    
    /**
     * lowbit函数：获取数字的二进制表示中最右边的1所代表的数值
     * 例如：x=6(110) 返回2(010)，x=12(1100) 返回4(0100)
     * 
     * @param i 输入数字
     * @return 最低位的1所代表的数值
     */
    public static int lowbit(int i) {
        return i & -i;
    }
    
    /**
     * 单点增加操作：在位置i上增加v
     * 
     * @param tree 树状数组
     * @param i 位置（从1开始）
     * @param c 增加的值
     */
    public static void add(long[] tree, int i, long c) {
        // 从位置i开始，沿着父节点路径向上更新所有相关的节点
        while (i <= m) {
            tree[i] += c;
            // 移动到父节点
            i += lowbit(i);
        }
    }
    
    /**
     * 查询前缀和：计算从位置1到位置i的所有元素之和
     * 
     * @param tree 树状数组
     * @param i 查询的结束位置
     * @return 前缀和
     */
    public static long sum(long[] tree, int i) {
        long ans = 0;
        // 从位置i开始，沿着子节点路径向下累加
        while (i > 0) {
            ans += tree[i];
            // 移动到前一个相关区间
            i -= lowbit(i);
        }
        return ans;
    }
    
    /**
     * 计算好三元组数目
     * 
     * @param nums1 第一个数组
     * @param nums2 第二个数组
     * @return 好三元组数目
     */
    public static long goodTriplets(int[] nums1, int[] nums2) {
        n = nums1.length;
        
        // 构建位置映射数组
        int[] pos = new int[n];
        for (int i = 0; i < n; i++) {
            pos[nums1[i]] = i;
        }
        
        // 构建转换后的数组
        for (int i = 0; i < n; i++) {
            arr[i + 1] = pos[nums2[i]] + 1;
        }
        
        // 离散化处理
        for (int i = 1; i <= n; i++) {
            sort[i] = arr[i];
        }
        Arrays.sort(sort, 1, n + 1);
        m = 1;
        for (int i = 2; i <= n; i++) {
            // 去重
            if (sort[m] != sort[i]) {
                sort[++m] = sort[i];
            }
        }
        
        // 将原数组元素替换为离散化后的排名
        for (int i = 1; i <= n; i++) {
            arr[i] = rank(arr[i]);
        }
        
        // 初始化树状数组
        for (int i = 1; i <= m; i++) {
            tree1[i] = 0;
            tree2[i] = 0;
        }
        
        long ans = 0;
        // 遍历数组，对每个元素计算以它为结尾的升序三元组数量
        for (int i = 1; i <= n; i++) {
            // 查询以当前值做结尾的升序三元组数量
            // 即查询右方有多少数字能与当前数字组成升序二元组
            ans += sum(tree2, arr[i] - 1);
            
            // 更新以当前值做结尾的升序一元组数量（单个元素）
            add(tree1, arr[i], 1);
            
            // 更新以当前值做结尾的升序二元组数量
            // 即当前元素与左方比它小的元素组成的二元组数量
            add(tree2, arr[i], sum(tree1, arr[i] - 1));
        }
        return ans;
    }
    
    /**
     * 给定原始值v，返回其在离散化数组中的排名（即在排序数组中的位置）
     * 
     * @param v 原始值
     * @return 排名值(排序部分1~m中的下标)
     */
    public static int rank(int v) {
        int l = 1, r = m, mid;
        int ans = 0;
        while (l <= r) {
            mid = (l + r) / 2;
            if (sort[mid] >= v) {
                ans = mid;
                r = mid - 1;
            } else {
                l = mid + 1;
            }
        }
        return ans;
    }
    
    // 测试方法
    public static void main(String[] args) {
        // 测试用例1
        int[] nums1_1 = {2, 0, 1, 3};
        int[] nums2_1 = {0, 1, 2, 3};
        System.out.println("输入: nums1 = [2,0,1,3], nums2 = [0,1,2,3]");
        System.out.println("输出: " + goodTriplets(nums1_1, nums2_1));
        System.out.println("期望: 1\n");
        
        // 测试用例2
        int[] nums1_2 = {4, 0, 1, 3, 2};
        int[] nums2_2 = {4, 1, 0, 2, 3};
        System.out.println("输入: nums1 = [4,0,1,3,2], nums2 = [4,1,0,2,3]");
        System.out.println("输出: " + goodTriplets(nums1_2, nums2_2));
        System.out.println("期望: 4");
    }
}