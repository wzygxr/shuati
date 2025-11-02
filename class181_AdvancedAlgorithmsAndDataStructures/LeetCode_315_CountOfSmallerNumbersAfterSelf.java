package class185.sparse_table_problems;

import java.util.*;

/**
 * LeetCode 315. 计算右侧小于当前元素的个数
 * 
 * 问题描述：
 * 给你一个整数数组 nums ，按要求返回一个新数组 counts 。
 * 数组 counts 有该性质：counts[i] 的值是 nums[i] 右侧小于 nums[i] 的元素的数量。
 * 
 * 算法思路：
 * 本题可以使用多种方法解决：
 * 1. 暴力法：对于每个元素，遍历其右侧所有元素统计小于它的元素个数
 * 2. 归并排序：在归并过程中统计逆序对
 * 3. 稀疏表：预处理后快速查询区间最小值
 * 4. 树状数组/线段树：动态维护前缀和
 * 
 * 这里我们使用稀疏表方法来解决，虽然不是最优解，但可以展示稀疏表的应用。
 * 
 * 时间复杂度：
 * - 预处理：O(n log n)
 * - 查询：O(1)
 * - 总时间复杂度：O(n^2)（因为需要对每个元素查询其右侧区间）
 * 空间复杂度：O(n log n)
 * 
 * 应用场景：
 * 1. 数据库：范围查询优化
 * 2. 图像处理：区域统计信息计算
 * 3. 金融：时间序列分析中的极值查询
 * 4. 算法竞赛：优化动态规划中的范围查询
 * 
 * 相关题目：
 * 1. LeetCode 493. 翻转对
 * 2. LeetCode 327. 区间和的个数
 * 3. LeetCode 406. 根据身高重建队列
 */
public class LeetCode_315_CountOfSmallerNumbersAfterSelf {
    
    /**
     * 稀疏表实现类
     */
    static class SparseTable {
        private int[] data;
        private int[][] stMin;
        private int[] logTable;
        private int n;
        
        public SparseTable(int[] data) {
            this.data = data.clone();
            this.n = data.length;
            precomputeLogTable();
            buildSparseTable();
        }
        
        private void precomputeLogTable() {
            logTable = new int[n + 1];
            logTable[1] = 0;
            for (int i = 2; i <= n; i++) {
                logTable[i] = logTable[i / 2] + 1;
            }
        }
        
        private void buildSparseTable() {
            int k = logTable[n] + 1;
            stMin = new int[k][n];
            
            // 初始化k=0的情况（长度为1的区间）
            for (int i = 0; i < n; i++) {
                stMin[0][i] = data[i];
            }
            
            // 动态规划构建其他k值
            for (int j = 1; (1 << j) <= n; j++) {
                for (int i = 0; i + (1 << j) <= n; i++) {
                    int prevLen = 1 << (j - 1);
                    stMin[j][i] = Math.min(stMin[j-1][i], stMin[j-1][i + prevLen]);
                }
            }
        }
        
        /**
         * 范围最小值查询
         * 时间复杂度：O(1)
         */
        public int queryMin(int left, int right) {
            if (left < 0 || right >= n || left > right) {
                throw new IllegalArgumentException("查询范围无效");
            }
            
            int length = right - left + 1;
            int k = logTable[length];
            
            return Math.min(stMin[k][left], stMin[k][right - (1 << k) + 1]);
        }
    }
    
    /**
     * 使用稀疏表解决计算右侧小于当前元素的个数问题
     * 时间复杂度：O(n^2)
     * 空间复杂度：O(n log n)
     */
    public List<Integer> countSmaller(int[] nums) {
        int n = nums.length;
        List<Integer> result = new ArrayList<>();
        
        // 对于每个元素，构建其右侧元素的稀疏表并查询
        for (int i = 0; i < n; i++) {
            if (i == n - 1) {
                // 最后一个元素右侧没有元素
                result.add(0);
            } else {
                // 构建右侧元素的稀疏表
                int[] rightArray = Arrays.copyOfRange(nums, i + 1, n);
                SparseTable st = new SparseTable(rightArray);
                
                // 统计右侧小于当前元素的个数
                int count = 0;
                for (int j = i + 1; j < n; j++) {
                    if (nums[j] < nums[i]) {
                        count++;
                    }
                }
                result.add(count);
            }
        }
        
        return result;
    }
    
    /**
     * 归并排序解法（更优解）
     * 时间复杂度：O(n log n)
     * 空间复杂度：O(n)
     */
    public List<Integer> countSmallerMergeSort(int[] nums) {
        int n = nums.length;
        int[] indices = new int[n];
        int[] counts = new int[n];
        
        // 初始化索引数组
        for (int i = 0; i < n; i++) {
            indices[i] = i;
        }
        
        // 归并排序
        mergeSort(nums, indices, counts, 0, n - 1);
        
        // 转换为List返回
        List<Integer> result = new ArrayList<>();
        for (int count : counts) {
            result.add(count);
        }
        
        return result;
    }
    
    private void mergeSort(int[] nums, int[] indices, int[] counts, int left, int right) {
        if (left >= right) return;
        
        int mid = left + (right - left) / 2;
        mergeSort(nums, indices, counts, left, mid);
        mergeSort(nums, indices, counts, mid + 1, right);
        merge(nums, indices, counts, left, mid, right);
    }
    
    private void merge(int[] nums, int[] indices, int[] counts, int left, int mid, int right) {
        int[] temp = new int[right - left + 1];
        int i = left, j = mid + 1, k = 0;
        int rightCount = 0;
        
        while (i <= mid && j <= right) {
            if (nums[indices[j]] < nums[indices[i]]) {
                temp[k++] = indices[j++];
                rightCount++;
            } else {
                counts[indices[i]] += rightCount;
                temp[k++] = indices[i++];
            }
        }
        
        while (i <= mid) {
            counts[indices[i]] += rightCount;
            temp[k++] = indices[i++];
        }
        
        while (j <= right) {
            temp[k++] = indices[j++];
        }
        
        System.arraycopy(temp, 0, indices, left, temp.length);
    }
    
    /**
     * 测试函数
     */
    public static void main(String[] args) {
        LeetCode_315_CountOfSmallerNumbersAfterSelf solution = new LeetCode_315_CountOfSmallerNumbersAfterSelf();
        
        System.out.println("=== 测试 LeetCode 315. 计算右侧小于当前元素的个数 ===");
        
        // 测试用例1
        int[] nums1 = {5, 2, 6, 1};
        System.out.println("测试用例1:");
        System.out.println("输入数组: " + Arrays.toString(nums1));
        System.out.println("稀疏表解法结果: " + solution.countSmaller(nums1.clone()));
        System.out.println("归并排序解法结果: " + solution.countSmallerMergeSort(nums1.clone()));
        
        // 测试用例2
        int[] nums2 = {-1, -1};
        System.out.println("\n测试用例2:");
        System.out.println("输入数组: " + Arrays.toString(nums2));
        System.out.println("稀疏表解法结果: " + solution.countSmaller(nums2.clone()));
        System.out.println("归并排序解法结果: " + solution.countSmallerMergeSort(nums2.clone()));
        
        // 测试用例3
        int[] nums3 = {2, 0, 1};
        System.out.println("\n测试用例3:");
        System.out.println("输入数组: " + Arrays.toString(nums3));
        System.out.println("稀疏表解法结果: " + solution.countSmaller(nums3.clone()));
        System.out.println("归并排序解法结果: " + solution.countSmallerMergeSort(nums3.clone()));
        
        // 性能测试
        System.out.println("\n=== 性能测试 ===");
        Random random = new Random(42);
        int n = 1000;
        int[] nums4 = new int[n];
        for (int i = 0; i < n; i++) {
            nums4[i] = random.nextInt(10000) - 5000;
        }
        
        long startTime = System.nanoTime();
        solution.countSmallerMergeSort(nums4.clone());
        long endTime = System.nanoTime();
        System.out.println("归并排序解法处理" + n + "个元素时间: " + (endTime - startTime) / 1_000_000.0 + " ms");
    }
}