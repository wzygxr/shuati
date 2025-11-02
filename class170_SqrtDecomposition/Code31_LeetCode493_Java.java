import java.util.*;

/**
 * LeetCode 493. Reverse Pairs
 * 题目链接：https://leetcode.com/problems/reverse-pairs/
 * 
 * 题目描述：
 * 给定一个数组nums，统计重要逆序对的数量。
 * 重要逆序对定义为：满足i < j且nums[i] > 2 * nums[j]的(i, j)对。
 * 
 * 解题思路：
 * 使用平方根分解 + 值域分块
 * 1. 将值域分成sqrt(maxVal)个块
 * 2. 从右向左处理数组，维护每个值的出现次数
 * 3. 对于每个元素nums[i]，统计右侧满足nums[j] < nums[i]/2.0的元素数量
 * 
 * 时间复杂度：O(n * sqrt(maxVal))
 * 空间复杂度：O(maxVal)
 * 
 * 工程化考量：
 * 1. 处理整数溢出：使用long类型
 * 2. 值域压缩：使用离散化减少空间复杂度
 * 3. 优化二分查找性能
 */
public class Code31_LeetCode493_Java {
    
    public int reversePairs(int[] nums) {
        if (nums == null || nums.length == 0) {
            return 0;
        }
        
        int n = nums.length;
        
        // 离散化处理
        long[] values = new long[n];
        for (int i = 0; i < n; i++) {
            values[i] = nums[i];
        }
        
        // 创建所有需要离散化的值（包括原值和2倍值）
        Set<Long> valueSet = new TreeSet<>();
        for (long num : values) {
            valueSet.add(num);
            valueSet.add(2L * num); // 用于处理2倍关系
        }
        
        // 创建值到排名的映射
        Map<Long, Integer> rankMap = new HashMap<>();
        int rank = 0;
        for (long val : valueSet) {
            rankMap.put(val, rank++);
        }
        
        int size = rank;
        
        // 计算块大小
        int blockSize = (int) Math.sqrt(size);
        if (blockSize == 0) blockSize = 1;
        int blockCount = (size + blockSize - 1) / blockSize;
        
        // 初始化分块统计
        int[] cnt = new int[size];
        int[] blockSum = new int[blockCount];
        
        int result = 0;
        
        // 从右向左处理数组
        for (int i = n - 1; i >= 0; i--) {
            long currentVal = nums[i];
            
            // 需要统计：右侧满足nums[j] < nums[i]/2.0的元素数量
            // 即：统计值域中小于currentVal/2的值
            long threshold = currentVal % 2 == 0 ? currentVal / 2 - 1 : (currentVal - 1) / 2;
            
            // 找到threshold对应的排名
            Integer thresholdRank = rankMap.get(threshold);
            if (thresholdRank == null) {
                // 如果threshold不在离散化值中，找到第一个小于等于threshold的值
                thresholdRank = findRank(rankMap, threshold);
            }
            
            if (thresholdRank != null && thresholdRank >= 0) {
                // 统计小于等于threshold的元素数量
                int count = 0;
                
                // 在thresholdRank所在块之前的完整块中统计
                int thresholdBlock = thresholdRank / blockSize;
                for (int j = 0; j < thresholdBlock; j++) {
                    count += blockSum[j];
                }
                
                // 在当前块中统计
                int startInBlock = thresholdBlock * blockSize;
                int endInBlock = Math.min(startInBlock + blockSize, thresholdRank + 1);
                for (int j = startInBlock; j < endInBlock; j++) {
                    count += cnt[j];
                }
                
                result += count;
            }
            
            // 更新当前值的统计信息
            int currentRank = rankMap.get(currentVal);
            cnt[currentRank]++;
            blockSum[currentRank / blockSize]++;
        }
        
        return result;
    }
    
    /**
     * 在有序映射中找到小于等于target的最大值的排名
     */
    private Integer findRank(Map<Long, Integer> rankMap, long target) {
        Integer result = null;
        for (Map.Entry<Long, Integer> entry : rankMap.entrySet()) {
            if (entry.getKey() <= target) {
                result = entry.getValue();
            } else {
                break;
            }
        }
        return result;
    }
    
    /**
     * 优化版本：使用树状数组替代分块（更高效）
     */
    public int reversePairsBIT(int[] nums) {
        if (nums == null || nums.length == 0) {
            return 0;
        }
        
        int n = nums.length;
        
        // 离散化处理
        Set<Long> valueSet = new TreeSet<>();
        for (int num : nums) {
            valueSet.add((long) num);
            valueSet.add(2L * num);
        }
        
        List<Long> sorted = new ArrayList<>(valueSet);
        Map<Long, Integer> rankMap = new HashMap<>();
        for (int i = 0; i < sorted.size(); i++) {
            rankMap.put(sorted.get(i), i + 1); // 1-indexed
        }
        
        int size = sorted.size();
        BIT bit = new BIT(size);
        
        int result = 0;
        
        for (int i = n - 1; i >= 0; i--) {
            long currentVal = nums[i];
            long threshold = currentVal % 2 == 0 ? currentVal / 2 - 1 : (currentVal - 1) / 2;
            
            // 找到threshold的排名
            int thresholdRank = findRankBIT(sorted, threshold);
            if (thresholdRank > 0) {
                result += bit.query(thresholdRank);
            }
            
            // 更新当前值
            int currentRank = rankMap.get(currentVal);
            bit.update(currentRank, 1);
        }
        
        return result;
    }
    
    /**
     * 在有序列表中找到小于等于target的最大值的排名
     */
    private int findRankBIT(List<Long> sorted, long target) {
        int left = 0, right = sorted.size() - 1;
        int result = -1;
        
        while (left <= right) {
            int mid = left + (right - left) / 2;
            if (sorted.get(mid) <= target) {
                result = mid;
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }
        
        return result + 1; // 转换为1-indexed
    }
    
    /**
     * 树状数组实现
     */
    class BIT {
        int[] tree;
        int n;
        
        public BIT(int size) {
            this.n = size;
            this.tree = new int[n + 1];
        }
        
        public void update(int index, int delta) {
            while (index <= n) {
                tree[index] += delta;
                index += index & -index;
            }
        }
        
        public int query(int index) {
            int sum = 0;
            while (index > 0) {
                sum += tree[index];
                index -= index & -index;
            }
            return sum;
        }
    }
    
    /**
     * 单元测试方法
     */
    public static void test() {
        Code31_LeetCode493_Java solution = new Code31_LeetCode493_Java();
        
        // 测试用例1
        int[] nums1 = {1, 3, 2, 3, 1};
        int result1 = solution.reversePairsBIT(nums1);
        System.out.println("测试用例1结果: " + result1 + " (预期: 2)");
        
        // 测试用例2
        int[] nums2 = {2, 4, 3, 5, 1};
        int result2 = solution.reversePairsBIT(nums2);
        System.out.println("测试用例2结果: " + result2 + " (预期: 3)");
        
        // 测试用例3：空数组
        int[] nums3 = {};
        int result3 = solution.reversePairsBIT(nums3);
        System.out.println("测试用例3结果: " + result3 + " (预期: 0)");
        
        System.out.println("单元测试通过");
    }
    
    public static void main(String[] args) {
        test();
    }
}