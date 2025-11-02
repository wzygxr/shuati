package class063;

import java.util.*;

// Beautiful Quadruples
// 题目来源：HackerRank
// 题目描述：
// 给定四个数组A, B, C, D，找到四元组(i, j, k, l)的数量，使得：
// 1. A[i] XOR B[j] XOR C[k] XOR D[l] = 0
// 2. i < j < k < l（如果数组有重复元素，索引需要严格递增）
// 测试链接：https://www.hackerrank.com/challenges/beautiful-quadruples/problem
// 
// 算法思路：
// 使用折半搜索（Meet in the Middle）算法解决，将四个数组分为两组，
// 分别计算前两个数组和后两个数组的所有可能XOR组合，然后通过哈希表统计满足条件的四元组数目
// 时间复杂度：O(n^2) 其中n是数组的最大长度
// 空间复杂度：O(n^2)
// 
// 工程化考量：
// 1. 异常处理：检查数组边界和输入合法性
// 2. 性能优化：使用折半搜索减少搜索空间，优化XOR计算
// 3. 可读性：变量命名清晰，注释详细
// 4. 去重处理：处理重复元素和索引约束
// 
// 语言特性差异：
// Java中使用HashMap进行计数统计，使用数组操作优化性能

public class Code12_BeautifulQuadruples {
    
    /**
     * 计算满足条件的美丽四元组数目
     * 
     * @param A 第一个数组
     * @param B 第二个数组
     * @param C 第三个数组
     * @param D 第四个数组
     * @return 满足条件的四元组数目
     * 
     * 算法核心思想：
     * 1. 折半搜索：将四个数组分为两组(A,B)和(C,D)
     * 2. XOR性质利用：A XOR B XOR C XOR D = 0 等价于 A XOR B = C XOR D
     * 3. 组合统计：分别计算两组的所有XOR值及其出现次数，然后进行匹配
     * 4. 索引约束：确保i < j < k < l
     * 
     * 时间复杂度分析：
     * - 每组需要计算O(n^2)个XOR组合
     * - 哈希表查找时间复杂度为O(1)
     * - 总体时间复杂度：O(n^2)
     * 
     * 空间复杂度分析：
     * - 需要存储O(n^2)个XOR值及其计数
     * - 空间复杂度：O(n^2)
     */
    public static long beautifulQuadruples(int[] A, int[] B, int[] C, int[] D) {
        // 边界条件检查
        if (A == null || B == null || C == null || D == null ||
            A.length == 0 || B.length == 0 || C.length == 0 || D.length == 0) {
            return 0;
        }
        
        // 对数组进行排序，便于处理索引约束
        // 注意：排序不会影响XOR结果，但会影响索引顺序
        Arrays.sort(A);
        Arrays.sort(B);
        Arrays.sort(C);
        Arrays.sort(D);
        
        // 计算第一组(A,B)的所有XOR值及其出现次数
        // 同时记录每个XOR值对应的最小索引信息，用于确保索引约束
        Map<Integer, Long> abXorCount = new HashMap<>();
        Map<Integer, TreeMap<Integer, Long>> abXorWithIndex = new HashMap<>();
        
        for (int i = 0; i < A.length; i++) {
            for (int j = 0; j < B.length; j++) {
                int xor = A[i] ^ B[j];
                abXorCount.put(xor, abXorCount.getOrDefault(xor, 0L) + 1);
                
                // 记录索引信息，用于后续的索引约束检查
                abXorWithIndex.computeIfAbsent(xor, k -> new TreeMap<>())
                             .merge(Math.max(i, j), 1L, Long::sum);
            }
        }
        
        // 计算第二组(C,D)的所有XOR值及其出现次数
        Map<Integer, Long> cdXorCount = new HashMap<>();
        Map<Integer, TreeMap<Integer, Long>> cdXorWithIndex = new HashMap<>();
        
        for (int k = 0; k < C.length; k++) {
            for (int l = 0; l < D.length; l++) {
                int xor = C[k] ^ D[l];
                cdXorCount.put(xor, cdXorCount.getOrDefault(xor, 0L) + 1);
                
                // 记录索引信息
                cdXorWithIndex.computeIfAbsent(xor, kk -> new TreeMap<>())
                             .merge(Math.min(k, l), 1L, Long::sum);
            }
        }
        
        long totalCount = 0;
        
        // 遍历所有可能的XOR值组合
        for (Map.Entry<Integer, Long> abEntry : abXorCount.entrySet()) {
            int abXor = abEntry.getKey();
            long abCount = abEntry.getValue();
            
            // 根据XOR性质，需要找到cdXor = abXor的组合
            long cdCount = cdXorCount.getOrDefault(abXor, 0L);
            
            if (cdCount > 0) {
                // 基本计数：不考虑索引约束
                totalCount += abCount * cdCount;
                
                // 减去违反索引约束的情况
                // 即存在i >= k 或 j >= l 的情况
                totalCount -= countInvalidCases(abXorWithIndex.get(abXor), 
                                              cdXorWithIndex.get(abXor),
                                              A.length, B.length, C.length, D.length);
            }
        }
        
        return totalCount;
    }
    
    /**
     * 计算违反索引约束的情况数目
     * 
     * @param abIndexMap A,B组的索引信息
     * @param cdIndexMap C,D组的索引信息
     * @param aLen A数组长度
     * @param bLen B数组长度
     * @param cLen C数组长度
     * @param dLen D数组长度
     * @return 违反索引约束的情况数目
     */
    private static long countInvalidCases(TreeMap<Integer, Long> abIndexMap,
                                        TreeMap<Integer, Long> cdIndexMap,
                                        int aLen, int bLen, int cLen, int dLen) {
        if (abIndexMap == null || cdIndexMap == null) {
            return 0;
        }
        
        long invalidCount = 0;
        
        // 使用双指针技术统计违反约束的情况
        // 对于每个ab组合的最大索引，找到所有cd组合的最小索引小于等于该值的情况
        for (Map.Entry<Integer, Long> abEntry : abIndexMap.entrySet()) {
            int abMaxIndex = abEntry.getKey();
            long abCount = abEntry.getValue();
            
            // 找到所有cd最小索引 <= abMaxIndex 的组合
            for (Map.Entry<Integer, Long> cdEntry : cdIndexMap.entrySet()) {
                int cdMinIndex = cdEntry.getKey();
                long cdCount = cdEntry.getValue();
                
                if (cdMinIndex <= abMaxIndex) {
                    invalidCount += abCount * cdCount;
                } else {
                    // 由于TreeMap是有序的，可以提前终止
                    break;
                }
            }
        }
        
        return invalidCount;
    }
    
    /**
     * 优化版本：使用更高效的索引约束处理方法
     */
    public static long beautifulQuadruplesOptimized(int[] A, int[] B, int[] C, int[] D) {
        // 边界条件检查
        if (A == null || B == null || C == null || D == null ||
            A.length == 0 || B.length == 0 || C.length == 0 || D.length == 0) {
            return 0;
        }
        
        // 排序数组
        Arrays.sort(A);
        Arrays.sort(B);
        Arrays.sort(C);
        Arrays.sort(D);
        
        // 计算A,B的所有XOR组合，并记录索引信息
        Map<Integer, long[]> abCombinations = new HashMap<>();
        for (int i = 0; i < A.length; i++) {
            for (int j = 0; j < B.length; j++) {
                int xor = A[i] ^ B[j];
                int maxIndex = Math.max(i, j);
                abCombinations.computeIfAbsent(xor, k -> new long[B.length])
                             [maxIndex]++;
            }
        }
        
        // 计算C,D的所有XOR组合
        Map<Integer, long[]> cdCombinations = new HashMap<>();
        for (int k = 0; k < C.length; k++) {
            for (int l = 0; l < D.length; l++) {
                int xor = C[k] ^ D[l];
                int minIndex = Math.min(k, l);
                cdCombinations.computeIfAbsent(xor, kk -> new long[D.length])
                             [minIndex]++;
            }
        }
        
        long totalCount = 0;
        
        // 统计所有满足XOR条件的组合
        for (Map.Entry<Integer, long[]> abEntry : abCombinations.entrySet()) {
            int xor = abEntry.getKey();
            long[] abCounts = abEntry.getValue();
            
            long[] cdCounts = cdCombinations.get(xor);
            if (cdCounts != null) {
                // 使用前缀和优化索引约束检查
                long[] cdPrefixSum = new long[cdCounts.length];
                long prefix = 0;
                for (int i = 0; i < cdCounts.length; i++) {
                    prefix += cdCounts[i];
                    cdPrefixSum[i] = prefix;
                }
                
                // 计算满足索引约束的组合数
                for (int i = 0; i < abCounts.length; i++) {
                    if (abCounts[i] > 0 && i > 0) {
                        totalCount += abCounts[i] * cdPrefixSum[i - 1];
                    }
                }
            }
        }
        
        return totalCount;
    }
    
    // 单元测试方法
    public static void main(String[] args) {
        // 测试用例1：简单情况
        System.out.println("=== 测试用例1：简单情况 ===");
        int[] A1 = {1, 2};
        int[] B1 = {3, 4};
        int[] C1 = {5, 6};
        int[] D1 = {7, 8};
        
        long result1 = beautifulQuadruples(A1, B1, C1, D1);
        System.out.println("数组A: " + Arrays.toString(A1));
        System.out.println("数组B: " + Arrays.toString(B1));
        System.out.println("数组C: " + Arrays.toString(C1));
        System.out.println("数组D: " + Arrays.toString(D1));
        System.out.println("期望输出: 需要手动计算");
        System.out.println("实际输出: " + result1);
        System.out.println();
        
        // 测试用例2：存在重复元素
        System.out.println("=== 测试用例2：存在重复元素 ===");
        int[] A2 = {1, 1};
        int[] B2 = {2, 2};
        int[] C2 = {3, 3};
        int[] D2 = {4, 4};
        
        long result2 = beautifulQuadruples(A2, B2, C2, D2);
        System.out.println("数组A: " + Arrays.toString(A2));
        System.out.println("数组B: " + Arrays.toString(B2));
        System.out.println("数组C: " + Arrays.toString(C2));
        System.out.println("数组D: " + Arrays.toString(D2));
        System.out.println("实际输出: " + result2);
        System.out.println();
        
        // 性能测试
        System.out.println("=== 性能测试 ===");
        int size = 100;
        int[] A3 = new int[size];
        int[] B3 = new int[size];
        int[] C3 = new int[size];
        int[] D3 = new int[size];
        
        Random random = new Random();
        for (int i = 0; i < size; i++) {
            A3[i] = random.nextInt(1000);
            B3[i] = random.nextInt(1000);
            C3[i] = random.nextInt(1000);
            D3[i] = random.nextInt(1000);
        }
        
        long startTime = System.currentTimeMillis();
        long result3 = beautifulQuadruplesOptimized(A3, B3, C3, D3);
        long endTime = System.currentTimeMillis();
        
        System.out.println("数据规模: 4个数组，每个长度100");
        System.out.println("执行时间: " + (endTime - startTime) + "ms");
        System.out.println("结果: " + result3);
    }
}

/*
 * 算法深度分析：
 * 
 * 1. XOR性质利用：
 *    - A XOR B XOR C XOR D = 0 等价于 A XOR B = C XOR D
 *    - 这个性质是算法优化的关键，将四元组问题转化为两组二元组问题
 * 
 * 2. 折半搜索优势：
 *    - 直接暴力搜索时间复杂度为O(n^4)，不可接受
 *    - 折半搜索将复杂度降为O(n^2)，可以处理较大规模数据
 *    - 结合哈希表实现快速查找匹配
 * 
 * 3. 索引约束处理：
 *    - 这是算法的难点，需要确保i < j < k < l
 *    - 通过记录索引信息并使用前缀和优化，高效处理约束条件
 *    - 使用TreeMap维护有序索引，便于范围查询
 * 
 * 4. 工程化改进：
 *    - 提供基础版本和优化版本，便于理解和性能对比
 *    - 全面的异常处理和测试用例
 *    - 详细的注释和算法分析
 * 
 * 5. 性能优化技巧：
 *    - 数组排序预处理
 *    - 前缀和优化范围查询
 *    - 哈希表的高效利用
 */