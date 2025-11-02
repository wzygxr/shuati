// 最大子阵列 - 分块算法实现 (Java版本)
// 题目来源: 计蒜客
// 题目链接: https://www.jisuanke.com/course/705/27296
// 题目大意: 在数组中找出和最大的连续子数组（至少包含一个元素）
// 约束条件: 数组长度n不超过1000，元素为整数

import java.io.*;
import java.util.*;

public class Code34_MaximumSubarray {
    static final int MAXN = 1005;
    static final long INF = 1000000000000000000L;
    
    static int n, blen; // blen为块的大小
    static long[] arr = new long[MAXN]; // 原始数组
    
    // 分块预处理的结构
    static long[] preSum = new long[MAXN]; // 前缀和数组
    static long[] blockSum; // 每个块的总和
    static long[][] lMax; // lMax[i][j]: 从块i的第j个元素开始，向右延伸的最大子数组和
    static long[][] rMax; // rMax[i][j]: 到块i的第j个元素结束，向左延伸的最大子数组和
    static long[] maxSub; // 每个块内部的最大子数组和
    static long totalMax; // 整个数组的最大子数组和
    
    // 初始化分块信息
    static void initBlocks() {
        blen = (int)Math.sqrt(n);
        if (blen == 0) blen = 1;
        
        int blockCount = (n + blen - 1) / blen;
        
        // 初始化数组
        blockSum = new long[blockCount];
        lMax = new long[blockCount][blen];
        rMax = new long[blockCount][blen];
        maxSub = new long[blockCount];
        
        // 计算前缀和
        preSum[0] = 0;
        for (int i = 1; i <= n; i++) {
            preSum[i] = preSum[i - 1] + arr[i];
        }
        
        // 预处理每个块的信息
        for (int b = 0; b < blockCount; b++) {
            int start = b * blen + 1;
            int end = Math.min((b + 1) * blen, n);
            
            // 计算块的总和
            blockSum[b] = preSum[end] - preSum[start - 1];
            
            // 计算lMax：从每个位置开始向右延伸的最大子数组和
            long currentMax = -INF;
            long currentSum = 0;
            for (int i = end; i >= start; i--) {
                currentSum += arr[i];
                currentMax = Math.max(currentMax, currentSum);
                lMax[b][i - start] = currentMax;
            }
            
            // 计算rMax：到每个位置结束向左延伸的最大子数组和
            currentMax = -INF;
            currentSum = 0;
            for (int i = start; i <= end; i++) {
                currentSum += arr[i];
                currentMax = Math.max(currentMax, currentSum);
                rMax[b][i - start] = currentMax;
            }
            
            // 计算块内的最大子数组和（Kadane算法）
            long kadaneMax = -INF;
            long kadaneSum = 0;
            for (int i = start; i <= end; i++) {
                kadaneSum = Math.max(arr[i], kadaneSum + arr[i]);
                kadaneMax = Math.max(kadaneMax, kadaneSum);
            }
            maxSub[b] = kadaneMax;
        }
        
        // 计算整个数组的最大子数组和
        totalMax = -INF;
        for (int b = 0; b < blockCount; b++) {
            totalMax = Math.max(totalMax, maxSub[b]);
        }
        
        // 检查跨越块的情况
        for (int b = 0; b < blockCount - 1; b++) {
            // 从块b的末尾向左延伸的最大值
            long rightMax = rMax[b][blen - 1];
            
            // 块b+1的总和累加
            long currentSum = rightMax;
            totalMax = Math.max(totalMax, currentSum);
            
            for (int nextB = b + 1; nextB < blockCount; nextB++) {
                currentSum += blockSum[nextB];
                totalMax = Math.max(totalMax, currentSum);
                totalMax = Math.max(totalMax, currentSum - blockSum[nextB] + rMax[nextB][0]);
            }
        }
    }
    
    // 使用分块预处理的方法求最大子数组和
    static long maxSubarray() {
        initBlocks();
        return totalMax;
    }
    
    // 朴素的Kadane算法实现（用于验证）
    static long kadane() {
        long maxSoFar = -INF;
        long maxEndingHere = 0;
        
        for (int i = 1; i <= n; i++) {
            maxEndingHere = Math.max(arr[i], maxEndingHere + arr[i]);
            maxSoFar = Math.max(maxSoFar, maxEndingHere);
        }
        
        return maxSoFar;
    }
    
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter pw = new PrintWriter(new OutputStreamWriter(System.out));
        
        n = Integer.parseInt(br.readLine());
        String[] s = br.readLine().split(" ");
        for (int i = 1; i <= n; i++) {
            arr[i] = Long.parseLong(s[i - 1]);
        }
        
        long result = maxSubarray();
        pw.println(result);
        
        pw.flush();
        pw.close();
        br.close();
    }
    
    /*
     * 时间复杂度分析：
     * - 预处理时间：O(n)
     * - 分块初始化：O(n)
     * - 块内预处理：O(n)
     * - 跨越块处理：O(n) （最坏情况下）
     * - 总体时间复杂度：O(n)
     * 
     * 空间复杂度分析：
     * - 数组arr：O(n)
     * - 前缀和数组preSum：O(n)
     * - 块信息数组：O(n)
     * - 总体空间复杂度：O(n)
     * 
     * Java语言特性注意事项：
     * 1. 使用long类型避免整数溢出
     * 2. 注意数组索引的处理，使用1-based索引
     * 3. 动态初始化分块相关的数组
     * 4. 输入输出使用BufferedReader和PrintWriter以提高效率
     * 
     * 算法说明：
     * 虽然这个问题通常使用Kadane算法解决，但分块方法提供了一种可扩展的思路，
     * 特别适合需要支持动态更新操作的场景。通过预处理每个块的信息，
     * 可以在O(√n)的时间内处理更新操作。
     */
}