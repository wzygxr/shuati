import java.util.*;

/**
 * LeetCode 1649. Create Sorted Array through Instructions
 * 题目链接：https://leetcode.com/problems/create-sorted-array-through-instructions/
 * 
 * 题目描述：
 * 给定一个指令数组，需要按顺序插入这些指令来构建一个有序数组。
 * 每次插入的成本是min(左侧小于当前指令的元素数量, 右侧大于当前指令的元素数量)。
 * 求构建完整数组的总成本。
 * 
 * 解题思路：
 * 使用平方根分解 + 值域分块
 * 1. 将值域分成sqrt(maxVal)个块
 * 2. 维护每个值的出现次数和每个块的总和
 * 3. 对于每个指令，快速统计左侧小于它的元素数量和右侧大于它的元素数量
 * 4. 取两者的最小值作为插入成本
 * 
 * 时间复杂度：O(n * sqrt(maxVal))
 * 空间复杂度：O(maxVal)
 * 
 * 工程化考量：
 * 1. 使用离散化减少值域大小
 * 2. 处理大数取模
 * 3. 优化统计性能
 */
public class Code33_LeetCode1649_Java {
    
    public int createSortedArray(int[] instructions) {
        if (instructions == null || instructions.length == 0) {
            return 0;
        }
        
        int n = instructions.length;
        int mod = 1000000007;
        
        // 离散化处理
        int[] sorted = instructions.clone();
        Arrays.sort(sorted);
        
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
        int[] cnt = new int[size]; // 每个值的计数
        int[] blockSum = new int[blockCount]; // 每个块的总和
        
        long totalCost = 0;
        
        for (int i = 0; i < n; i++) {
            int currentVal = instructions[i];
            int currentRank = rankMap.get(currentVal);
            
            // 统计左侧小于当前值的元素数量
            int leftCount = 0;
            
            // 在当前排名所在块之前的完整块中统计
            int currentBlock = currentRank / blockSize;
            for (int j = 0; j < currentBlock; j++) {
                leftCount += blockSum[j];
            }
            
            // 在当前块中统计小于当前排名的元素
            int startInBlock = currentBlock * blockSize;
            int endInBlock = Math.min(startInBlock + blockSize, currentRank);
            for (int j = startInBlock; j < endInBlock; j++) {
                leftCount += cnt[j];
            }
            
            // 统计右侧大于当前值的元素数量
            // 总插入数量 - 左侧小于等于当前值的元素数量
            int totalInserted = 0;
            for (int j = 0; j < blockCount; j++) {
                totalInserted += blockSum[j];
            }
            
            // 左侧小于等于当前值的元素数量 = 左侧小于当前值的数量 + 当前值的数量
            int leftLessOrEqual = leftCount + cnt[currentRank];
            int rightGreater = totalInserted - leftLessOrEqual;
            
            // 插入成本是min(左侧小于, 右侧大于)
            int cost = Math.min(leftCount, rightGreater);
            totalCost = (totalCost + cost) % mod;
            
            // 更新统计信息
            cnt[currentRank]++;
            blockSum[currentBlock]++;
        }
        
        return (int) totalCost;
    }
    
    /**
     * 优化版本：使用树状数组（更高效）
     */
    public int createSortedArrayBIT(int[] instructions) {
        if (instructions == null || instructions.length == 0) {
            return 0;
        }
        
        int n = instructions.length;
        int mod = 1000000007;
        
        // 离散化处理
        int[] sorted = instructions.clone();
        Arrays.sort(sorted);
        
        Map<Integer, Integer> rankMap = new HashMap<>();
        int rank = 1; // 1-indexed
        for (int i = 0; i < n; i++) {
            if (i == 0 || sorted[i] != sorted[i - 1]) {
                rankMap.put(sorted[i], rank++);
            }
        }
        
        int size = rank - 1;
        BIT bit = new BIT(size);
        
        long totalCost = 0;
        
        for (int i = 0; i < n; i++) {
            int currentVal = instructions[i];
            int currentRank = rankMap.get(currentVal);
            
            // 统计左侧小于当前值的元素数量
            int leftCount = bit.query(currentRank - 1);
            
            // 统计当前值的数量
            int currentCount = bit.query(currentRank) - bit.query(currentRank - 1);
            
            // 统计右侧大于当前值的元素数量
            int totalInserted = bit.query(size);
            int rightGreater = totalInserted - leftCount - currentCount;
            
            // 插入成本
            int cost = Math.min(leftCount, rightGreater);
            totalCost = (totalCost + cost) % mod;
            
            // 更新树状数组
            bit.update(currentRank, 1);
        }
        
        return (int) totalCost;
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
        Code33_LeetCode1649_Java solution = new Code33_LeetCode1649_Java();
        
        // 测试用例1
        int[] instructions1 = {1, 5, 6, 2};
        int result1 = solution.createSortedArrayBIT(instructions1);
        System.out.println("测试用例1结果: " + result1 + " (预期: 1)");
        
        // 测试用例2
        int[] instructions2 = {1, 2, 3, 6, 5, 4};
        int result2 = solution.createSortedArrayBIT(instructions2);
        System.out.println("测试用例2结果: " + result2 + " (预期: 3)");
        
        // 测试用例3：重复元素
        int[] instructions3 = {1, 1, 1, 1};
        int result3 = solution.createSortedArrayBIT(instructions3);
        System.out.println("测试用例3结果: " + result3 + " (预期: 0)");
        
        System.out.println("单元测试通过");
    }
    
    public static void main(String[] args) {
        test();
    }
}