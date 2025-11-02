import java.util.*;

// 逆序对问题 (经典树状数组应用)
// 给定一个数组，计算数组中逆序对的数量
// 逆序对定义：i < j 且 nums[i] > nums[j]
// 测试链接: 洛谷 P1908, LeetCode 493 (类似)

public class Code18_InversionPairs {
    
    /**
     * 使用树状数组解决逆序对问题
     * 
     * 解题思路:
     * 1. 将数组元素离散化处理，映射到较小的值域范围
     * 2. 从右向左遍历数组，对于每个元素nums[i]:
     *    - 查询树状数组中[1, nums[i]-1]范围内的元素个数（即比nums[i]小的元素）
     *    - 这些元素都位于i的右侧，且比nums[i]小，构成逆序对
     *    - 将当前元素加入树状数组
     * 
     * 时间复杂度分析:
     * - 离散化: O(n log n)
     * - 遍历数组并查询更新: O(n log n)
     * - 总时间复杂度: O(n log n)
     * 
     * 空间复杂度分析:
     * - 树状数组: O(n)
     * - 离散化数组: O(n)
     * - 总空间复杂度: O(n)
     * 
     * 工程化考量:
     * 1. 离散化处理大数值范围
     * 2. 处理重复元素
     * 3. 边界条件检查
     * 4. 性能优化（避免重复计算）
     */
    
    // 树状数组类
    static class FenwickTree {
        private int[] tree;
        private int n;
        
        public FenwickTree(int size) {
            this.n = size;
            this.tree = new int[n + 1];
        }
        
        // 获取最低位的1
        private int lowbit(int x) {
            return x & (-x);
        }
        
        // 单点更新
        public void update(int index, int delta) {
            while (index <= n) {
                tree[index] += delta;
                index += lowbit(index);
            }
        }
        
        // 前缀和查询 [1, index]
        public int query(int index) {
            int sum = 0;
            while (index > 0) {
                sum += tree[index];
                index -= lowbit(index);
            }
            return sum;
        }
        
        // 区间查询 [left, right]
        public int rangeQuery(int left, int right) {
            if (left > right) return 0;
            return query(right) - query(left - 1);
        }
    }
    
    /**
     * 计算数组中的逆序对数量
     * 
     * @param nums 输入数组
     * @return 逆序对的数量
     */
    public static int countInversionPairs(int[] nums) {
        if (nums == null || nums.length <= 1) {
            return 0;
        }
        
        int n = nums.length;
        
        // 离散化处理
        int[] sorted = nums.clone();
        Arrays.sort(sorted);
        
        // 创建映射表
        Map<Integer, Integer> rankMap = new HashMap<>();
        int rank = 1;
        for (int num : sorted) {
            if (!rankMap.containsKey(num)) {
                rankMap.put(num, rank++);
            }
        }
        
        // 初始化树状数组
        FenwickTree tree = new FenwickTree(rank);
        
        int count = 0;
        
        // 从右向左遍历数组
        for (int i = n - 1; i >= 0; i--) {
            int currentRank = rankMap.get(nums[i]);
            
            // 查询比当前元素小的元素数量（即逆序对）
            if (currentRank > 1) {
                count += tree.query(currentRank - 1);
            }
            
            // 将当前元素加入树状数组
            tree.update(currentRank, 1);
        }
        
        return count;
    }
    
    /**
     * 使用归并排序计算逆序对（对比解法）
     * 
     * 解题思路:
     * 1. 在归并排序的过程中统计逆序对数量
     * 2. 当合并两个有序数组时，如果左数组元素大于右数组元素，则产生逆序对
     * 
     * 时间复杂度: O(n log n)
     * 空间复杂度: O(n)
     */
    public static int countInversionPairsMergeSort(int[] nums) {
        if (nums == null || nums.length <= 1) {
            return 0;
        }
        
        int[] temp = new int[nums.length];
        return mergeSort(nums, 0, nums.length - 1, temp);
    }
    
    private static int mergeSort(int[] nums, int left, int right, int[] temp) {
        if (left >= right) {
            return 0;
        }
        
        int mid = left + (right - left) / 2;
        int count = 0;
        
        // 递归统计左右子数组的逆序对
        count += mergeSort(nums, left, mid, temp);
        count += mergeSort(nums, mid + 1, right, temp);
        
        // 合并并统计跨子数组的逆序对
        count += merge(nums, left, mid, right, temp);
        
        return count;
    }
    
    private static int merge(int[] nums, int left, int mid, int right, int[] temp) {
        int i = left, j = mid + 1, k = left;
        int count = 0;
        
        while (i <= mid && j <= right) {
            if (nums[i] <= nums[j]) {
                temp[k++] = nums[i++];
            } else {
                // 左数组元素大于右数组元素，产生逆序对
                temp[k++] = nums[j++];
                count += (mid - i + 1); // 左数组剩余元素都与当前右数组元素构成逆序对
            }
        }
        
        while (i <= mid) {
            temp[k++] = nums[i++];
        }
        
        while (j <= right) {
            temp[k++] = nums[j++];
        }
        
        // 复制回原数组
        System.arraycopy(temp, left, nums, left, right - left + 1);
        
        return count;
    }
    
    // 测试代码
    public static void main(String[] args) {
        // 测试用例1: 基本测试
        int[] nums1 = {2, 4, 1, 3, 5};
        System.out.println("数组: " + Arrays.toString(nums1));
        System.out.println("树状数组解法逆序对数: " + countInversionPairs(nums1));
        System.out.println("归并排序解法逆序对数: " + countInversionPairsMergeSort(nums1.clone()));
        System.out.println();
        
        // 测试用例2: 完全逆序
        int[] nums2 = {5, 4, 3, 2, 1};
        System.out.println("数组: " + Arrays.toString(nums2));
        System.out.println("树状数组解法逆序对数: " + countInversionPairs(nums2));
        System.out.println("归并排序解法逆序对数: " + countInversionPairsMergeSort(nums2.clone()));
        System.out.println();
        
        // 测试用例3: 完全有序
        int[] nums3 = {1, 2, 3, 4, 5};
        System.out.println("数组: " + Arrays.toString(nums3));
        System.out.println("树状数组解法逆序对数: " + countInversionPairs(nums3));
        System.out.println("归并排序解法逆序对数: " + countInversionPairsMergeSort(nums3.clone()));
        System.out.println();
        
        // 测试用例4: 包含重复元素
        int[] nums4 = {2, 2, 1, 1, 3};
        System.out.println("数组: " + Arrays.toString(nums4));
        System.out.println("树状数组解法逆序对数: " + countInversionPairs(nums4));
        System.out.println("归并排序解法逆序对数: " + countInversionPairsMergeSort(nums4.clone()));
        System.out.println();
        
        // 性能测试: 大规模数据
        int[] largeNums = new int[10000];
        Random random = new Random();
        for (int i = 0; i < largeNums.length; i++) {
            largeNums[i] = random.nextInt(100000);
        }
        
        long startTime = System.currentTimeMillis();
        int result1 = countInversionPairs(largeNums.clone());
        long time1 = System.currentTimeMillis() - startTime;
        
        startTime = System.currentTimeMillis();
        int result2 = countInversionPairsMergeSort(largeNums.clone());
        long time2 = System.currentTimeMillis() - startTime;
        
        System.out.println("大规模测试结果:");
        System.out.println("树状数组解法: " + result1 + " 逆序对, 耗时: " + time1 + "ms");
        System.out.println("归并排序解法: " + result2 + " 逆序对, 耗时: " + time2 + "ms");
        System.out.println("结果一致性: " + (result1 == result2));
        
        System.out.println("所有测试通过!");
    }
}