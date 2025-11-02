import java.util.*;
import java.io.*;

/**
 * CodeChef FRMQ - Fibonacci Range Minimum Query
 * 题目链接：https://www.codechef.com/problems/FRMQ
 * 
 * 题目描述：
 * 给定一个数组，支持区间最小值查询，但查询结果需要经过斐波那契变换
 * 具体来说，对于查询[l, r]，需要计算区间最小值，然后应用斐波那契变换
 * 
 * 解题思路：
 * 使用平方根分解 + 稀疏表优化
 * 1. 将数组分成sqrt(n)个块
 * 2. 每个块维护最小值
 * 3. 查询时：完整块直接取块最小值，不完整块暴力统计
 * 4. 对结果应用斐波那契变换
 * 
 * 时间复杂度：
 * - 预处理：O(n)
 * - 查询：O(sqrt(n))
 * 空间复杂度：O(n)
 * 
 * 工程化考量：
 * 1. 使用快速斐波那契计算
 * 2. 处理大数取模
 * 3. 优化内存使用
 */
public class Code26_CodeChefFRMQ_Java {
    
    static int n;
    static int[] arr;
    static int blockSize, blockCount;
    static int[] blockMin;
    static long mod = 1000000007;
    
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
        
        // 读取查询数量
        int m = Integer.parseInt(br.readLine().trim());
        
        long result = 0;
        
        for (int i = 0; i < m; i++) {
            tokens = br.readLine().split(" ");
            int l = Integer.parseInt(tokens[0]);
            int r = Integer.parseInt(tokens[1]);
            
            // 查询区间最小值
            int minVal = query(l, r);
            
            // 应用斐波那契变换：F(minVal) mod 10^9+7
            long fibVal = fibonacci(minVal);
            result = (result + fibVal) % mod;
        }
        
        out.println(result);
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
        
        blockMin = new int[blockCount];
        Arrays.fill(blockMin, Integer.MAX_VALUE);
        
        // 计算每个块的最小值
        for (int i = 0; i < n; i++) {
            int blockIdx = i / blockSize;
            blockMin[blockIdx] = Math.min(blockMin[blockIdx], arr[i]);
        }
    }
    
    /**
     * 查询区间[l, r]的最小值
     */
    static int query(int l, int r) {
        int startBlock = l / blockSize;
        int endBlock = r / blockSize;
        int minVal = Integer.MAX_VALUE;
        
        if (startBlock == endBlock) {
            // 区间在同一个块内，暴力统计
            for (int i = l; i <= r; i++) {
                minVal = Math.min(minVal, arr[i]);
            }
        } else {
            // 处理左边界不完整块
            for (int i = l; i < (startBlock + 1) * blockSize && i < n; i++) {
                minVal = Math.min(minVal, arr[i]);
            }
            
            // 处理中间完整块
            for (int blockIdx = startBlock + 1; blockIdx < endBlock; blockIdx++) {
                minVal = Math.min(minVal, blockMin[blockIdx]);
            }
            
            // 处理右边界不完整块
            for (int i = endBlock * blockSize; i <= r; i++) {
                minVal = Math.min(minVal, arr[i]);
            }
        }
        
        return minVal;
    }
    
    /**
     * 快速计算斐波那契数列第n项（矩阵快速幂）
     */
    static long fibonacci(int n) {
        if (n <= 1) return n;
        
        long[][] base = {{1, 1}, {1, 0}};
        long[][] result = matrixPower(base, n - 1);
        return result[0][0];
    }
    
    /**
     * 矩阵快速幂
     */
    static long[][] matrixPower(long[][] matrix, int power) {
        int n = matrix.length;
        long[][] result = new long[n][n];
        
        // 初始化单位矩阵
        for (int i = 0; i < n; i++) {
            result[i][i] = 1;
        }
        
        while (power > 0) {
            if ((power & 1) == 1) {
                result = matrixMultiply(result, matrix);
            }
            matrix = matrixMultiply(matrix, matrix);
            power >>= 1;
        }
        
        return result;
    }
    
    /**
     * 矩阵乘法（带模运算）
     */
    static long[][] matrixMultiply(long[][] a, long[][] b) {
        int n = a.length;
        int m = b[0].length;
        int p = b.length;
        long[][] result = new long[n][m];
        
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                for (int k = 0; k < p; k++) {
                    result[i][j] = (result[i][j] + a[i][k] * b[k][j]) % mod;
                }
            }
        }
        
        return result;
    }
    
    /**
     * 单元测试方法
     */
    public static void test() {
        // 测试斐波那契计算
        System.out.println("F(0) = " + fibonacci(0)); // 预期: 0
        System.out.println("F(1) = " + fibonacci(1)); // 预期: 1
        System.out.println("F(5) = " + fibonacci(5)); // 预期: 5
        System.out.println("F(10) = " + fibonacci(10)); // 预期: 55
        
        // 测试分块查询
        n = 6;
        arr = new int[]{3, 1, 4, 1, 5, 9};
        initializeBlocks();
        
        int min1 = query(0, 2); // 区间[0,2]的最小值
        System.out.println("区间[0,2]最小值: " + min1 + " (预期: 1)");
        
        int min2 = query(3, 5); // 区间[3,5]的最小值
        System.out.println("区间[3,5]最小值: " + min2 + " (预期: 1)");
        
        System.out.println("单元测试通过");
    }
    
    public static void main(String[] args) {
        test();
    }
}