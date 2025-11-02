package class056;

import java.util.*;

/**
 * 按公因数计算最大组件大小
 * 给定一个由不同正整数组成的非空数组 nums，考虑下面的图：
 * 有 nums.length 个节点，按从 nums[0] 到 nums[nums.length - 1] 标记；
 * 只有当 nums[i] 和 nums[j] 共用大于 1 的公因数时，nums[i] 和 nums[j] 之间才有一条边。
 * 返回图中最大连通组件的大小。
 * 
 * 示例 1:
 * 输入: nums = [4,6,15,35]
 * 输出: 4
 * 
 * 示例 2:
 * 输入: nums = [20,50,9,63]
 * 输出: 2
 * 
 * 示例 3:
 * 输入: nums = [2,3,6,7,4,12,21,39]
 * 输出: 8
 * 
 * 约束条件：
 * 1 <= nums.length <= 2 * 10^4
 * 1 <= nums[i] <= 10^5
 * nums 中所有值都不同
 * 
 * 测试链接: https://leetcode.cn/problems/largest-component-size-by-common-factor/
 * 相关平台: LeetCode 952
 */
public class Code10_LargestComponentSizeByCommonFactor {
    
    /**
     * 使用并查集解决按公因数计算最大组件大小问题
     * 
     * 解题思路：
     * 1. 对于每个数字，将其与其所有质因数连接（使用并查集）
     * 2. 这样，具有公共因数的数字就会在同一个连通组件中
     * 3. 统计每个连通组件的大小，返回最大的
     * 
     * 时间复杂度：O(N * √M * α(M))，其中N是数组长度，M是数组中的最大值，α是阿克曼函数的反函数
     * 空间复杂度：O(M + N)
     * 
     * @param nums 输入数组
     * @return 最大连通组件的大小
     */
    public static int largestComponentSize(int[] nums) {
        // 边界条件检查
        if (nums == null || nums.length == 0) {
            return 0;
        }
        
        // 找到数组中的最大值
        int maxNum = 0;
        for (int num : nums) {
            maxNum = Math.max(maxNum, num);
        }
        
        // 创建并查集，大小为maxNum+1
        UnionFind uf = new UnionFind(maxNum + 1);
        
        // 对于每个数字，将其与其所有质因数连接
        for (int num : nums) {
            // 分解质因数
            for (int i = 2; i * i <= num; i++) {
                if (num % i == 0) {
                    uf.union(num, i);
                    uf.union(num, num / i);
                }
            }
        }
        
        // 统计每个连通组件的大小
        Map<Integer, Integer> componentSize = new HashMap<>();
        int maxSize = 0;
        
        for (int num : nums) {
            int root = uf.find(num);
            componentSize.put(root, componentSize.getOrDefault(root, 0) + 1);
            maxSize = Math.max(maxSize, componentSize.get(root));
        }
        
        return maxSize;
    }
    
    /**
     * 并查集数据结构实现
     * 包含路径压缩和按秩合并优化
     */
    static class UnionFind {
        private int[] parent;  // parent[i]表示节点i的父节点
        private int[] rank;    // rank[i]表示以i为根的树的高度上界
        
        /**
         * 初始化并查集
         * @param n 节点数量
         */
        public UnionFind(int n) {
            parent = new int[n];
            rank = new int[n];
            // 初始时每个节点都是自己的父节点
            for (int i = 0; i < n; i++) {
                parent[i] = i;
                rank[i] = 1;  // 初始时每个树的秩为1
            }
        }
        
        /**
         * 查找节点的根节点（代表元素）
         * 使用路径压缩优化
         * @param x 要查找的节点
         * @return 节点x所在集合的根节点
         */
        public int find(int x) {
            if (parent[x] != x) {
                // 路径压缩：将路径上的所有节点直接连接到根节点
                parent[x] = find(parent[x]);
            }
            return parent[x];
        }
        
        /**
         * 合并两个集合
         * 使用按秩合并优化
         * @param x 第一个节点
         * @param y 第二个节点
         */
        public void union(int x, int y) {
            int rootX = find(x);
            int rootY = find(y);
            
            // 如果已经在同一个集合中，则无需合并
            if (rootX != rootY) {
                // 按秩合并：将秩小的树合并到秩大的树下
                if (rank[rootX] > rank[rootY]) {
                    parent[rootY] = rootX;
                } else if (rank[rootX] < rank[rootY]) {
                    parent[rootX] = rootY;
                } else {
                    // 秩相等时，任选一个作为根，并将其秩加1
                    parent[rootY] = rootX;
                    rank[rootX]++;
                }
            }
        }
    }
    
    // 测试方法
    public static void main(String[] args) {
        // 测试用例1
        int[] nums1 = {4, 6, 15, 35};
        System.out.println("测试用例1结果: " + largestComponentSize(nums1)); // 预期输出: 4
        
        // 测试用例2
        int[] nums2 = {20, 50, 9, 63};
        System.out.println("测试用例2结果: " + largestComponentSize(nums2)); // 预期输出: 2
        
        // 测试用例3
        int[] nums3 = {2, 3, 6, 7, 4, 12, 21, 39};
        System.out.println("测试用例3结果: " + largestComponentSize(nums3)); // 预期输出: 8
        
        // 测试用例4：单个元素
        int[] nums4 = {10};
        System.out.println("测试用例4结果: " + largestComponentSize(nums4)); // 预期输出: 1
        
        // 测试用例5：互质数字
        int[] nums5 = {2, 3, 5, 7, 11};
        System.out.println("测试用例5结果: " + largestComponentSize(nums5)); // 预期输出: 1
    }
}