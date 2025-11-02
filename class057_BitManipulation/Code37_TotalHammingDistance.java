package class031;

/**
 * 汉明距离总和
 * 测试链接：https://leetcode.cn/problems/total-hamming-distance/
 * 
 * 题目描述：
 * 两个整数的 汉明距离 指的是这两个数字的二进制数对应位不同的数量。
 * 给你一个整数数组 nums，请你计算并返回 nums 中任意两个数之间汉明距离的总和。
 * 
 * 示例：
 * 输入：nums = [4,14,2]
 * 输出：6
 * 解释：在二进制表示中，4 表示为 0100，14 表示为 1110，2 表示为 0010。
 * 汉明距离计算：
 * 4 和 14 的汉明距离为 2
 * 4 和 2 的汉明距离为 2
 * 14 和 2 的汉明距离为 2
 * 总和为 2 + 2 + 2 = 6
 * 
 * 提示：
 * 1 <= nums.length <= 10^4
 * 0 <= nums[i] <= 10^9
 * 给定数组中的元素范围在 0 到 10^9 之间
 * 数组的长度不超过 10^4
 * 
 * 解题思路：
 * 1. 暴力法：双重循环计算所有组合（会超时）
 * 2. 位运算法：逐位计算贡献值
 * 3. 数学优化：利用组合数学优化计算
 * 
 * 时间复杂度分析：
 * - 暴力法：O(n²)，会超时
 * - 位运算法：O(n * 32)，32位整数
 * - 数学优化：O(n * 32)
 * 
 * 空间复杂度分析：
 * - 所有方法：O(1)，只使用常数空间
 */
public class Code37_TotalHammingDistance {
    
    /**
     * 方法1：暴力法（不推荐，会超时）
     * 时间复杂度：O(n²)
     * 空间复杂度：O(1)
     */
    public int totalHammingDistance1(int[] nums) {
        int total = 0;
        int n = nums.length;
        
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                total += Integer.bitCount(nums[i] ^ nums[j]);
            }
        }
        
        return total;
    }
    
    /**
     * 方法2：位运算法（推荐）
     * 核心思想：逐位计算每个位的贡献
     * 对于每个位，统计有多少个数的该位是1（设为count）
     * 那么该位的贡献就是 count * (n - count)
     * 时间复杂度：O(n * 32)
     * 空间复杂度：O(1)
     */
    public int totalHammingDistance2(int[] nums) {
        int total = 0;
        int n = nums.length;
        
        // 遍历32位（整数最多32位）
        for (int i = 0; i < 32; i++) {
            int countOnes = 0;
            
            // 统计当前位为1的数的个数
            for (int num : nums) {
                countOnes += (num >> i) & 1;
            }
            
            // 当前位的贡献：countOnes * (n - countOnes)
            total += countOnes * (n - countOnes);
        }
        
        return total;
    }
    
    /**
     * 方法3：数学优化版
     * 使用更高效的位运算技巧
     * 时间复杂度：O(n * 32)
     * 空间复杂度：O(1)
     */
    public int totalHammingDistance3(int[] nums) {
        int total = 0;
        int n = nums.length;
        
        for (int i = 0; i < 32; i++) {
            int mask = 1 << i;
            int count = 0;
            
            for (int num : nums) {
                if ((num & mask) != 0) {
                    count++;
                }
            }
            
            total += count * (n - count);
        }
        
        return total;
    }
    
    /**
     * 方法4：使用Brian Kernighan算法优化
     * 时间复杂度：O(n * 32)
     * 空间复杂度：O(1)
     */
    public int totalHammingDistance4(int[] nums) {
        int total = 0;
        int n = nums.length;
        
        // 对于每个位位置
        for (int bitPos = 0; bitPos < 32; bitPos++) {
            int ones = 0;
            int zeros = 0;
            
            for (int num : nums) {
                // 检查特定位是否为1
                if (((num >> bitPos) & 1) == 1) {
                    ones++;
                } else {
                    zeros++;
                }
            }
            
            // 当前位的汉明距离贡献
            total += ones * zeros;
        }
        
        return total;
    }
    
    /**
     * 方法5：分组统计法
     * 将数字按位分组统计
     * 时间复杂度：O(n * 32)
     * 空间复杂度：O(1)
     */
    public int totalHammingDistance5(int[] nums) {
        if (nums == null || nums.length == 0) {
            return 0;
        }
        
        int total = 0;
        int n = nums.length;
        
        // 创建32个桶，每个桶统计对应位的1的个数
        int[] bitCounts = new int[32];
        
        for (int num : nums) {
            for (int i = 0; i < 32; i++) {
                if ((num & (1 << i)) != 0) {
                    bitCounts[i]++;
                }
            }
        }
        
        // 计算总汉明距离
        for (int count : bitCounts) {
            total += count * (n - count);
        }
        
        return total;
    }
    
    /**
     * 测试方法
     */
    public static void main(String[] args) {
        Code37_TotalHammingDistance solution = new Code37_TotalHammingDistance();
        
        // 测试用例1：基础情况
        int[] nums1 = {4, 14, 2};
        int result1 = solution.totalHammingDistance2(nums1);
        System.out.println("测试用例1 - 输入: [4, 14, 2]");
        System.out.println("结果: " + result1 + " (预期: 6)");
        
        // 测试用例2：两个相同数字
        int[] nums2 = {4, 4};
        int result2 = solution.totalHammingDistance2(nums2);
        System.out.println("测试用例2 - 输入: [4, 4]");
        System.out.println("结果: " + result2 + " (预期: 0)");
        
        // 测试用例3：三个不同数字
        int[] nums3 = {1, 2, 3};
        int result3 = solution.totalHammingDistance2(nums3);
        System.out.println("测试用例3 - 输入: [1, 2, 3]");
        System.out.println("结果: " + result3 + " (预期: 4)");
        
        // 测试用例4：边界情况（单个元素）
        int[] nums4 = {5};
        int result4 = solution.totalHammingDistance2(nums4);
        System.out.println("测试用例4 - 输入: [5]");
        System.out.println("结果: " + result4 + " (预期: 0)");
        
        // 性能测试
        int[] largeNums = new int[1000];
        for (int i = 0; i < largeNums.length; i++) {
            largeNums[i] = i; // 0到999的序列
        }
        
        long startTime = System.currentTimeMillis();
        int result5 = solution.totalHammingDistance2(largeNums);
        long endTime = System.currentTimeMillis();
        System.out.println("性能测试 - 输入长度: " + largeNums.length);
        System.out.println("结果: " + result5);
        System.out.println("耗时: " + (endTime - startTime) + "ms");
        
        // 复杂度分析
        System.out.println("\n=== 复杂度分析 ===");
        System.out.println("方法1 - 暴力法:");
        System.out.println("  时间复杂度: O(n²) - 会超时");
        System.out.println("  空间复杂度: O(1)");
        
        System.out.println("方法2 - 位运算法:");
        System.out.println("  时间复杂度: O(n * 32)");
        System.out.println("  空间复杂度: O(1)");
        
        System.out.println("方法3 - 数学优化版:");
        System.out.println("  时间复杂度: O(n * 32)");
        System.out.println("  空间复杂度: O(1)");
        
        System.out.println("方法4 - Brian Kernighan算法:");
        System.out.println("  时间复杂度: O(n * 32)");
        System.out.println("  空间复杂度: O(1)");
        
        System.out.println("方法5 - 分组统计法:");
        System.out.println("  时间复杂度: O(n * 32)");
        System.out.println("  空间复杂度: O(32)");
        
        // 工程化考量
        System.out.println("\n=== 工程化考量 ===");
        System.out.println("1. 算法选择：方法2是最优选择");
        System.out.println("2. 性能优化：避免O(n²)的暴力解法");
        System.out.println("3. 边界处理：处理空数组和单元素数组");
        System.out.println("4. 可读性：清晰的数学公式解释");
        
        // 算法技巧总结
        System.out.println("\n=== 算法技巧总结 ===");
        System.out.println("1. 组合数学：C(k,2) = k*(k-1)/2 的变形应用");
        System.out.println("2. 位运算：逐位统计1的个数");
        System.out.println("3. 贡献值计算：每个位的独立贡献可以分开计算");
        System.out.println("4. 数学优化：利用对称性减少计算量");
        
        // 数学原理说明
        System.out.println("\n=== 数学原理说明 ===");
        System.out.println("对于每个位位置：");
        System.out.println("  设该位为1的数字有k个，为0的数字有m个");
        System.out.println("  那么该位产生的汉明距离贡献为：k * m");
        System.out.println("  因为每个1和每个0的组合都会产生1的贡献");
        System.out.println("  总贡献 = Σ(每个位的k * m)");
    }
}