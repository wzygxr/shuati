package class185.difference_array_problems;

import java.util.*;

/**
 * 差分数组实现 (Java版本)
 * 
 * 算法思路：
 * 差分数组是一种用于高效处理区间更新操作的数据结构。
 * 通过维护原数组的差分数组，可以将区间更新操作的时间复杂度从O(n)降低到O(1)。
 * 
 * 应用场景：
 * 1. 数组操作优化：批量更新处理
 * 2. 前缀和计算：快速计算区间和
 * 3. 算法竞赛：区间操作问题的优化
 * 
 * 时间复杂度：
 * - 区间更新：O(1)
 * - 获取结果数组：O(n)
 * - 单点查询：O(n)（需要重建数组）
 * 空间复杂度：O(n)
 * 
 * 相关题目：
 * 1. LeetCode 370. 区间加法
 * 2. LeetCode 1094. 拼车
 * 3. LeetCode 1109. 航班预订统计
 */
public class DifferenceArray {
    private int[] diff;  // 差分数组
    private int size;    // 数组大小
    private int[] original; // 原始数组（用于重置操作）
    
    /**
     * 构造函数 - 从大小创建
     * @param size 数组大小
     */
    public DifferenceArray(int size) {
        if (size <= 0) {
            throw new IllegalArgumentException("数组大小必须为正整数");
        }
        
        this.size = size;
        this.diff = new int[size + 1];  // 差分数组大小为n+1，便于处理边界
        this.original = new int[size];
    }
    
    /**
     * 构造函数 - 从原始数组创建
     * @param originalArray 原始数组
     */
    public DifferenceArray(int[] originalArray) {
        if (originalArray == null || originalArray.length == 0) {
            throw new IllegalArgumentException("输入数组不能为空");
        }
        
        this.size = originalArray.length;
        this.original = originalArray.clone();
        this.diff = new int[size + 1];
        
        // 初始化差分数组
        diff[0] = originalArray[0];
        for (int i = 1; i < size; i++) {
            diff[i] = originalArray[i] - originalArray[i - 1];
        }
    }
    
    /**
     * 区间更新：将区间[start, end]的每个元素加上val
     * 时间复杂度：O(1)
     * @param start 起始索引（包含）
     * @param end 结束索引（包含）
     * @param val 要增加的值
     */
    public void rangeUpdate(int start, int end, int val) {
        if (start < 0 || end >= size || start > end) {
            throw new IllegalArgumentException("更新范围无效");
        }
        
        diff[start] += val;
        diff[end + 1] -= val;
    }
    
    /**
     * 获取更新后的数组
     * 时间复杂度：O(n)
     * @return 更新后的数组
     */
    public int[] getResult() {
        int[] result = new int[size];
        result[0] = diff[0];
        
        for (int i = 1; i < size; i++) {
            result[i] = result[i - 1] + diff[i];
        }
        
        return result;
    }
    
    /**
     * 直接获取数组中特定位置的值
     * 注意：这需要先重建数组，时间复杂度O(n)
     * @param index 索引位置
     * @return 该位置的值
     */
    public int getValue(int index) {
        if (index < 0 || index >= size) {
            throw new IllegalArgumentException("索引无效");
        }
        
        int[] result = getResult();
        return result[index];
    }
    
    /**
     * 重置差分数组
     */
    public void reset() {
        Arrays.fill(diff, 0);
        if (original != null) {
            diff[0] = original[0];
            for (int i = 1; i < size; i++) {
                diff[i] = original[i] - original[i - 1];
            }
        }
    }
    
    /**
     * 获取数组大小
     * @return 数组大小
     */
    public int getSize() {
        return size;
    }
    
    /**
     * 测试差分数组
     */
    public static void main(String[] args) {
        System.out.println("=== 测试差分数组 ===");
        
        // 测试从大小创建
        System.out.println("测试从大小创建:");
        DifferenceArray da1 = new DifferenceArray(5);
        System.out.println("初始数组: " + Arrays.toString(da1.getResult()));
        
        da1.rangeUpdate(0, 2, 1);
        System.out.println("区间[0,2]加1: " + Arrays.toString(da1.getResult()));
        
        da1.rangeUpdate(1, 4, 2);
        System.out.println("区间[1,4]加2: " + Arrays.toString(da1.getResult()));
        
        da1.rangeUpdate(2, 3, -1);
        System.out.println("区间[2,3]减1: " + Arrays.toString(da1.getResult()));
        
        // 测试从原始数组创建
        System.out.println("\n测试从原始数组创建:");
        int[] original = {1, 2, 3, 4, 5};
        DifferenceArray da2 = new DifferenceArray(original);
        System.out.println("原始数组: " + Arrays.toString(da2.getResult()));
        
        da2.rangeUpdate(1, 3, 10);
        System.out.println("区间[1,3]加10: " + Arrays.toString(da2.getResult()));
        
        da2.rangeUpdate(0, 4, -5);
        System.out.println("区间[0,4]减5: " + Arrays.toString(da2.getResult()));
        
        // 测试重置功能
        da2.reset();
        System.out.println("重置后: " + Arrays.toString(da2.getResult()));
        
        // 测试边界情况
        System.out.println("\n测试边界情况:");
        DifferenceArray da3 = new DifferenceArray(1);
        da3.rangeUpdate(0, 0, 100);
        System.out.println("单元素数组更新: " + Arrays.toString(da3.getResult()));
        
        // 性能测试
        System.out.println("\n=== 性能测试 ===");
        
        // 测试大量区间更新操作
        int n = 100000;
        DifferenceArray da4 = new DifferenceArray(n);
        
        long startTime = System.nanoTime();
        for (int i = 0; i < 10000; i++) {
            int start = i % (n - 100);
            int end = Math.min(start + 100, n - 1);
            da4.rangeUpdate(start, end, 1);
        }
        long updateTime = System.nanoTime() - startTime;
        
        // 获取结果数组
        startTime = System.nanoTime();
        int[] result = da4.getResult();
        long getResultTime = System.nanoTime() - startTime;
        
        System.out.println("执行10000次区间更新时间: " + updateTime / 1_000_000.0 + " ms");
        System.out.println("获取100000元素结果数组时间: " + getResultTime / 1_000_000.0 + " ms");
        System.out.println("结果数组前10个元素: " + Arrays.toString(Arrays.copyOf(result, 10)));
    }
}