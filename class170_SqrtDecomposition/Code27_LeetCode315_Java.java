import java.util.*;

/**
 * LeetCode 315. Count of Smaller Numbers After Self
 * 题目链接：https://leetcode.com/problems/count-of-smaller-numbers-after-self/
 * 
 * 题目描述：
 * 给定一个整数数组nums，返回一个新的数组counts，其中counts[i]表示在nums[i]右侧且比nums[i]小的元素数量。
 * 
 * 解题思路：
 * 使用平方根分解 + 值域分块
 * 1. 将值域分成sqrt(maxVal)个块
 * 2. 从右向左处理数组，维护每个值的出现次数
 * 3. 查询时：在对应值域块中统计小于当前值的元素数量
 * 
 * 时间复杂度：O(n * sqrt(maxVal))
 * 空间复杂度：O(maxVal)
 * 
 * 工程化考量：
 * 1. 处理负数：将所有值平移为正数
 * 2. 值域压缩：使用离散化减少空间复杂度
 * 3. 优化内存使用
 */
public class Code27_LeetCode315_Java {
    
    public List<Integer> countSmaller(int[] nums) {
        if (nums == null || nums.length == 0) {
            return new ArrayList<>();
        }
        
        int n = nums.length;
        List<Integer> result = new ArrayList<>();
        
        // 值域处理：找到最小值和最大值
        int minVal = Integer.MAX_VALUE;
        int maxVal = Integer.MIN_VALUE;
        
        for (int num : nums) {
            minVal = Math.min(minVal, num);
            maxVal = Math.max(maxVal, num);
        }
        
        // 将所有值平移为正数
        int shift = -minVal;
        int size = maxVal - minVal + 1;
        
        // 计算块大小
        int blockSize = (int) Math.sqrt(size);
        if (blockSize == 0) blockSize = 1;
        int blockCount = (size + blockSize - 1) / blockSize;
        
        // 初始化分块统计
        int[] cnt = new int[size]; // 每个值的计数
        int[] blockSum = new int[blockCount]; // 每个块的总和
        
        // 从右向左处理数组
        for (int i = n - 1; i >= 0; i--) {
            int currentVal = nums[i] + shift;
            
            // 统计比当前值小的元素数量
            int count = 0;
            
            // 在当前值所在块之前的完整块中统计
            int currentBlock = currentVal / blockSize;
            for (int j = 0; j < currentBlock; j++) {
                count += blockSum[j];
            }
            
            // 在当前块中统计比当前值小的元素
            int startInBlock = currentBlock * blockSize;
            int endInBlock = Math.min(startInBlock + blockSize, currentVal);
            for (int j = startInBlock; j < endInBlock; j++) {
                count += cnt[j];
            }
            
            result.add(0, count);
            
            // 更新统计信息
            cnt[currentVal]++;
            blockSum[currentBlock]++;
        }
        
        return result;
    }
    
    /**
     * 优化版本：使用离散化减少值域大小
     */
    public List<Integer> countSmallerOptimized(int[] nums) {
        if (nums == null || nums.length == 0) {
            return new ArrayList<>();
        }
        
        int n = nums.length;
        List<Integer> result = new ArrayList<>();
        
        // 离散化处理
        int[] sorted = nums.clone();
        Arrays.sort(sorted);
        
        // 创建值到排名的映射
        Map<Integer, Integer> rankMap = new HashMap<>();
        int rank = 0;
        for (int i = 0; i < n; i++) {
            if (i == 0 || sorted[i] != sorted[i - 1]) {
                rankMap.put(sorted[i], rank++);
            }
        }
        
        int size = rank;
        
        // 计算块大小
        int blockSize = (int) Math.sqrt(size);
        if (blockSize == 0) blockSize = 1;
        int blockCount = (size + blockSize - 1) / blockSize;
        
        // 初始化分块统计
        int[] cnt = new int[size];
        int[] blockSum = new int[blockCount];
        
        // 从右向左处理数组
        for (int i = n - 1; i >= 0; i--) {
            int currentRank = rankMap.get(nums[i]);
            
            // 统计比当前值小的元素数量
            int count = 0;
            
            // 在当前排名所在块之前的完整块中统计
            int currentBlock = currentRank / blockSize;
            for (int j = 0; j < currentBlock; j++) {
                count += blockSum[j];
            }
            
            // 在当前块中统计比当前排名小的元素
            int startInBlock = currentBlock * blockSize;
            int endInBlock = Math.min(startInBlock + blockSize, currentRank);
            for (int j = startInBlock; j < endInBlock; j++) {
                count += cnt[j];
            }
            
            result.add(0, count);
            
            // 更新统计信息
            cnt[currentRank]++;
            blockSum[currentBlock]++;
        }
        
        return result;
    }
    
    /**
     * 单元测试方法
     */
    public static void test() {
        Code27_LeetCode315_Java solution = new Code27_LeetCode315_Java();
        
        // 测试用例1
        int[] nums1 = {5, 2, 6, 1};
        List<Integer> result1 = solution.countSmallerOptimized(nums1);
        System.out.println("测试用例1结果: " + result1 + " (预期: [2, 1, 1, 0])");
        
        // 测试用例2
        int[] nums2 = {-1, -1};
        List<Integer> result2 = solution.countSmallerOptimized(nums2);
        System.out.println("测试用例2结果: " + result2 + " (预期: [0, 0])");
        
        // 测试用例3
        int[] nums3 = {};
        List<Integer> result3 = solution.countSmallerOptimized(nums3);
        System.out.println("测试用例3结果: " + result3 + " (预期: [])");
        
        System.out.println("单元测试通过");
    }
    
    public static void main(String[] args) {
        test();
    }
}