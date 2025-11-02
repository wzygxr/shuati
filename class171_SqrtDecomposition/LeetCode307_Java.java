package class173.implementations;

/**
 * LeetCode 307. 区域和检索 - 数组可修改 - Java实现
 * 题目链接：https://leetcode.cn/problems/range-sum-query-mutable/
 * 
 * 题目描述：
 * 给你一个数组 nums ，请你实现两类查询：
 * 1. update(index, val)：将下标为 index 的元素更新为 val 。
 * 2. sumRange(left, right)：返回数组 nums 中，下标范围 [left, right] 内的元素之和。
 * 
 * 解题思路：
 * 使用分块算法，将数组分成大小约为sqrt(n)的块。
 * 对于每个块维护块内元素和，这样：
 * - 更新操作：找到对应的块，更新元素值并更新块内和，复杂度 O(1)
 * - 求和操作：遍历左右不完整块，累加元素值；遍历中间完整块，累加块内和，复杂度 O(√n)
 * 
 * 时间复杂度：
 * - update操作：O(1)
 * - sumRange操作：O(√n)
 * 空间复杂度：O(n)
 * 
 * 工程化考量：
 * 1. 异常处理：检查输入参数的有效性
 * 2. 可配置性：块大小可根据需要调整
 * 3. 性能优化：使用块内和减少求和操作的复杂度
 * 4. 鲁棒性：处理边界情况和特殊输入
 * 5. 数据结构：使用数组存储原始数据和块内和
 */

public class LeetCode307_Java {
    private int[] nums;      // 原数组
    private int[] blockSum;  // 每个块的和
    private int blockSize;   // 块大小
    private int blockNum;    // 块数量
    private int n;           // 数组大小
    
    /**
     * 构造函数
     * @param nums 初始化数组
     */
    public LeetCode307_Java(int[] nums) {
        this.nums = nums;
        this.n = nums.length;
        // 计算块大小
        this.blockSize = (int) Math.sqrt(n) + 1;
        // 计算块数量
        this.blockNum = (n + blockSize - 1) / blockSize;
        // 初始化块内和数组
        this.blockSum = new int[blockNum];
        
        // 计算每个块的和
        for (int i = 0; i < n; i++) {
            int blockIndex = i / blockSize;
            blockSum[blockIndex] += nums[i];
        }
    }
    
    /**
     * 更新数组中的元素
     * @param index 要更新的元素索引
     * @param val 新值
     */
    public void update(int index, int val) {
        // 检查索引有效性
        if (index < 0 || index >= n) {
            throw new IndexOutOfBoundsException("Index out of bounds");
        }
        
        // 计算元素所在块
        int blockIndex = index / blockSize;
        // 更新块内和
        blockSum[blockIndex] += val - nums[index];
        // 更新原数组
        nums[index] = val;
    }
    
    /**
     * 计算区间和
     * @param left 区间左边界（包含）
     * @param right 区间右边界（包含）
     * @return 区间和
     */
    public int sumRange(int left, int right) {
        // 检查参数有效性
        if (left < 0 || right >= n || left > right) {
            throw new IllegalArgumentException("Invalid range");
        }
        
        int sum = 0;
        int leftBlock = left / blockSize;
        int rightBlock = right / blockSize;
        
        // 如果在同一个块内，暴力计算
        if (leftBlock == rightBlock) {
            for (int i = left; i <= right; i++) {
                sum += nums[i];
            }
        } else {
            // 计算左边不完整块
            for (int i = left; i < (leftBlock + 1) * blockSize; i++) {
                sum += nums[i];
            }
            
            // 计算中间完整块的和
            for (int i = leftBlock + 1; i < rightBlock; i++) {
                sum += blockSum[i];
            }
            
            // 计算右边不完整块
            for (int i = rightBlock * blockSize; i <= right; i++) {
                sum += nums[i];
            }
        }
        
        return sum;
    }
    
    /**
     * 主函数，用于测试
     */
    public static void main(String[] args) {
        // 测试用例
        int[] nums = {1, 3, 5};
        LeetCode307_Java solution = new LeetCode307_Java(nums);
        
        // 测试sumRange
        System.out.println("sumRange(0, 2) = " + solution.sumRange(0, 2)); // 输出: 9
        
        // 测试update
        solution.update(1, 2);
        System.out.println("sumRange(0, 2) after update = " + solution.sumRange(0, 2)); // 输出: 8
        
        // 更多测试用例
        solution.update(0, 10);
        System.out.println("sumRange(0, 0) = " + solution.sumRange(0, 0)); // 输出: 10
        System.out.println("sumRange(1, 2) = " + solution.sumRange(1, 2)); // 输出: 7
    }
}