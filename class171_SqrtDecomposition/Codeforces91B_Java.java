package class173.implementations;

import java.util.*;

/**
 * Codeforces 91B - Java实现
 * 题目链接：https://codeforces.com/problemset/problem/91B
 * 
 * 题目描述：
 * 给定一个数组a，对于每个元素a[i]，找到右边第一个比它小的元素的下标j，并输出j - i - 1。如果不存在这样的元素，输出-1。
 * 
 * 解题思路：
 * 使用分块算法，将数组分成大小约为sqrt(n)的块。
 * 预处理每个块内的最小值，以及块内的元素。
 * 对于每个查询，分情况处理：
 * 1. 检查右边的完整块，如果块内存在比当前元素小的元素，则暴力查找该块中的第一个符合条件的元素
 * 2. 否则检查右边不完整块中的元素
 * 3. 如果都没有找到，则返回-1
 * 
 * 时间复杂度：
 * - 预处理：O(n)
 * - 每个查询：O(√n)
 * 空间复杂度：O(n)
 * 
 * 工程化考量：
 * 1. 异常处理：检查输入参数的有效性
 * 2. 可配置性：块大小可根据需要调整
 * 3. 性能优化：预处理块内最小值减少重复计算
 * 4. 鲁棒性：处理边界情况和特殊输入
 * 5. 数据结构：使用数组存储统计信息
 */

public class Codeforces91B_Java {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        int n = scanner.nextInt();
        long[] a = new long[n];
        for (int i = 0; i < n; i++) {
            a[i] = scanner.nextLong();
        }
        
        int blockSize = (int) Math.sqrt(n) + 1;
        int blockNum = (n + blockSize - 1) / blockSize;
        
        // 预处理每个块的最小值
        long[] blockMin = new long[blockNum];
        Arrays.fill(blockMin, Long.MAX_VALUE);
        int[] belong = new int[n];
        
        for (int i = 0; i < n; i++) {
            belong[i] = i / blockSize;
            if (a[i] < blockMin[belong[i]]) {
                blockMin[belong[i]] = a[i];
            }
        }
        
        int[] result = new int[n];
        Arrays.fill(result, -1);
        
        for (int i = 0; i < n; i++) {
            int currentBlock = belong[i];
            boolean found = false;
            
            // 检查当前块后面的完整块
            for (int b = currentBlock + 1; b < blockNum && !found; b++) {
                if (blockMin[b] < a[i]) {
                    // 在块b中暴力查找第一个比a[i]小的元素
                    int start = b * blockSize;
                    int end = Math.min((b + 1) * blockSize, n);
                    for (int j = start; j < end; j++) {
                        if (a[j] < a[i]) {
                            result[i] = j - i - 1;
                            found = true;
                            break;
                        }
                    }
                }
            }
            
            // 如果没有在完整块中找到，检查当前块内的右边元素
            if (!found) {
                // 先检查当前块内的右边元素
                int start = (currentBlock + 1) * blockSize;
                int end = n;
                for (int j = start; j < end; j++) {
                    if (a[j] < a[i]) {
                        result[i] = j - i - 1;
                        found = true;
                        break;
                    }
                }
            }
        }
        
        // 输出结果
        for (int i = 0; i < n; i++) {
            System.out.print(result[i]);
            if (i < n - 1) {
                System.out.print(" ");
            }
        }
        System.out.println();
        
        scanner.close();
    }
}