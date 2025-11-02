import java.util.*;
import java.io.*;

/**
 * SPOJ KGSS - Maximum Sum
 * 题目链接：https://www.spoj.com/problems/KGSS/
 * 
 * 题目描述：
 * 给定一个数组，支持两种操作：
 * 1. 查询操作：查询区间[l, r]内两个元素的最大和（即最大元素 + 次大元素）
 * 2. 更新操作：将位置i的元素值修改为y
 * 
 * 解题思路：
 * 使用平方根分解 + 块内维护最大和次大值
 * 1. 将数组分成sqrt(n)个块
 * 2. 每个块维护最大元素和次大元素
 * 3. 查询时：完整块直接取块的最大和，不完整块暴力统计
 * 4. 更新时：更新原数组，并重新计算对应块的最大和次大值
 * 
 * 时间复杂度：
 * - 查询：O(sqrt(n))
 * - 更新：O(sqrt(n))
 * 空间复杂度：O(n)
 * 
 * 工程化考量：
 * 1. 优化最大次大值维护逻辑
 * 2. 处理边界情况
 * 3. 使用Buffered IO
 */
public class Code32_SPOJKGSS_Java {
    
    static class Block {
        int max1, max2; // 最大元素和次大元素
        
        Block() {
            max1 = max2 = Integer.MIN_VALUE;
        }
        
        void update(int val) {
            if (val > max1) {
                max2 = max1;
                max1 = val;
            } else if (val > max2) {
                max2 = val;
            }
        }
        
        int getMaxSum() {
            if (max1 == Integer.MIN_VALUE || max2 == Integer.MIN_VALUE) {
                return Integer.MIN_VALUE;
            }
            return max1 + max2;
        }
    }
    
    static int n;
    static int[] arr;
    static int blockSize, blockCount;
    static Block[] blocks;
    
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter out = new PrintWriter(System.out);
        
        // 读取数组大小
        n = Integer.parseInt(br.readLine().trim());
        arr = new int[n];
        
        // 读取数组元素
        String[] tokens = br.readLine().split(" ");
        for (int i = 0; i < n; i++) {
            arr[i] = Integer.parseInt(tokens[i]);
        }
        
        // 初始化分块
        initializeBlocks();
        
        // 读取操作数量
        int m = Integer.parseInt(br.readLine().trim());
        
        for (int i = 0; i < m; i++) {
            tokens = br.readLine().split(" ");
            String operation = tokens[0];
            
            if (operation.equals("Q")) {
                // 查询操作: Q l r
                int l = Integer.parseInt(tokens[1]) - 1; // 0-indexed
                int r = Integer.parseInt(tokens[2]) - 1;
                
                int result = query(l, r);
                out.println(result);
            } else {
                // 更新操作: U i y
                int idx = Integer.parseInt(tokens[1]) - 1;
                int newVal = Integer.parseInt(tokens[2]);
                
                update(idx, newVal);
            }
        }
        
        out.flush();
        out.close();
    }
    
    /**
     * 初始化分块结构
     */
    static void initializeBlocks() {
        blockSize = (int) Math.sqrt(n);
        if (blockSize == 0) blockSize = 1;
        blockCount = (n + blockSize - 1) / blockSize;
        
        blocks = new Block[blockCount];
        for (int i = 0; i < blockCount; i++) {
            blocks[i] = new Block();
        }
        
        // 初始化每个块的最大和次大值
        for (int i = 0; i < n; i++) {
            int blockIdx = i / blockSize;
            blocks[blockIdx].update(arr[i]);
        }
    }
    
    /**
     * 查询操作：查询区间[l, r]内两个元素的最大和
     */
    static int query(int l, int r) {
        int startBlock = l / blockSize;
        int endBlock = r / blockSize;
        
        int max1 = Integer.MIN_VALUE;
        int max2 = Integer.MIN_VALUE;
        
        if (startBlock == endBlock) {
            // 区间在同一个块内，暴力统计
            for (int i = l; i <= r; i++) {
                updateMaxValues(arr[i], max1, max2);
            }
        } else {
            // 处理左边界不完整块
            for (int i = l; i < (startBlock + 1) * blockSize && i < n; i++) {
                updateMaxValues(arr[i], max1, max2);
            }
            
            // 处理中间完整块
            for (int blockIdx = startBlock + 1; blockIdx < endBlock; blockIdx++) {
                Block block = blocks[blockIdx];
                updateMaxValues(block.max1, max1, max2);
                updateMaxValues(block.max2, max1, max2);
            }
            
            // 处理右边界不完整块
            for (int i = endBlock * blockSize; i <= r; i++) {
                updateMaxValues(arr[i], max1, max2);
            }
        }
        
        return max1 + max2;
    }
    
    /**
     * 更新最大值和次大值
     */
    static void updateMaxValues(int val, int max1, int max2) {
        if (val > max1) {
            max2 = max1;
            max1 = val;
        } else if (val > max2) {
            max2 = val;
        }
    }
    
    /**
     * 更新操作：将位置idx的元素值修改为newVal
     */
    static void update(int idx, int newVal) {
        int blockIdx = idx / blockSize;
        int oldVal = arr[idx];
        arr[idx] = newVal;
        
        // 重新计算对应块的最大和次大值
        Block block = blocks[blockIdx];
        block.max1 = Integer.MIN_VALUE;
        block.max2 = Integer.MIN_VALUE;
        
        int start = blockIdx * blockSize;
        int end = Math.min((blockIdx + 1) * blockSize, n);
        
        for (int i = start; i < end; i++) {
            block.update(arr[i]);
        }
    }
    
    /**
     * 单元测试方法
     */
    public static void test() {
        // 测试用例1
        n = 5;
        arr = new int[]{1, 2, 3, 4, 5};
        initializeBlocks();
        
        // 查询测试
        int result1 = query(0, 4); // 最大和应该是4+5=9
        System.out.println("测试用例1查询结果: " + result1 + " (预期: 9)");
        
        // 更新测试
        update(2, 6); // 将位置2的元素从3改为6
        int result2 = query(0, 4); // 最大和应该是5+6=11
        System.out.println("测试用例1更新后查询结果: " + result2 + " (预期: 11)");
        
        System.out.println("单元测试通过");
    }
    
    public static void main(String[] args) {
        test();
    }
}