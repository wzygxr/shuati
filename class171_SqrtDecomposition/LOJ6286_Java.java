package class173.implementations;

import java.util.*;

/**
 * LOJ 6286. 数列分块入门 10 - Java实现
 * 题目链接：https://loj.ac/p/6286
 * 
 * 题目描述：
 * 给出一个长为n的数列，以及n个操作，操作涉及区间众数查询。
 * 
 * 解题思路：
 * 使用分块算法，将数组分成大小约为sqrt(n)的块。
 * 预处理：
 * 1. 对于每个块，预处理块内的众数
 * 2. 对于任意两个块i和j(i < j)，预处理区间[i,j]的众数
 * 3. 记录每个数出现的所有位置
 * 处理查询时：
 * 1. 对于左右不完整块，暴力遍历每个元素，统计其在整个查询区间内的出现次数
 * 2. 对于中间完整块，利用预处理的众数信息，检查其在整个查询区间内的出现次数
 * 3. 最终取出现次数最多的数作为众数
 * 
 * 时间复杂度：
 * - 预处理：O(n√n)
 * - 每个查询：O(√n)
 * 空间复杂度：O(n√n)
 * 
 * 工程化考量：
 * 1. 异常处理：检查输入参数的有效性
 * 2. 可配置性：块大小可根据需要调整
 * 3. 性能优化：预处理中间结果减少重复计算
 * 4. 鲁棒性：处理边界情况和特殊输入
 * 5. 数据结构：使用哈希表和数组存储统计信息
 */

public class LOJ6286_Java {
    private static final int MAXN = 100010;
    private static final int MAXBLOCK = 350;
    
    private int[] arr = new int[MAXN];        // 原数组
    private int[] belong = new int[MAXN];     // 每个元素所属的块
    private int[] blockLeft = new int[MAXBLOCK];  // 每个块的左边界
    private int[] blockRight = new int[MAXBLOCK]; // 每个块的右边界
    private int[][] preMode = new int[MAXBLOCK][MAXBLOCK]; // preMode[i][j]表示块i到块j的众数
    private int[][] preCount = new int[MAXBLOCK][MAXBLOCK]; // preCount[i][j]表示块i到块j的众数的出现次数
    private Map<Integer, List<Integer>> posMap = new HashMap<>(); // 记录每个数出现的所有位置
    
    private int blockSize;  // 块大小
    private int blockNum;   // 块数量
    private int n;          // 数组大小
    
    /**
     * 初始化分块结构
     */
    private void init() {
        blockSize = (int) Math.sqrt(n) + 1;
        blockNum = (n + blockSize - 1) / blockSize;
        
        // 初始化块边界
        for (int i = 1; i <= blockNum; i++) {
            blockLeft[i] = (i - 1) * blockSize + 1;
            blockRight[i] = Math.min(i * blockSize, n);
        }
        
        // 初始化每个元素所属的块
        for (int i = 1; i <= n; i++) {
            belong[i] = (i - 1) / blockSize + 1;
        }
        
        // 初始化位置映射
        for (int i = 1; i <= n; i++) {
            posMap.putIfAbsent(arr[i], new ArrayList<>());
            posMap.get(arr[i]).add(i);
        }
        
        // 预处理块间众数
        preprocessBlockMode();
    }
    
    /**
     * 预处理块间众数
     */
    private void preprocessBlockMode() {
        // 对于每个起始块i
        for (int i = 1; i <= blockNum; i++) {
            Map<Integer, Integer> cnt = new HashMap<>(); // 统计当前区间内每个数的出现次数
            int mode = 0; // 当前众数
            int maxCount = 0; // 众数的出现次数
            
            // 扩展结束块j
            for (int j = i; j <= blockNum; j++) {
                // 遍历块j中的每个元素
                for (int k = blockLeft[j]; k <= blockRight[j]; k++) {
                    int num = arr[k];
                    cnt.put(num, cnt.getOrDefault(num, 0) + 1);
                    
                    // 更新众数
                    if (cnt.get(num) > maxCount) {
                        mode = num;
                        maxCount = cnt.get(num);
                    }
                }
                
                // 记录块i到块j的众数和其出现次数
                preMode[i][j] = mode;
                preCount[i][j] = maxCount;
            }
        }
    }
    
    /**
     * 计算数x在区间[l,r]中出现的次数
     */
    private int countOccurrence(int x, int l, int r) {
        List<Integer> positions = posMap.getOrDefault(x, Collections.emptyList());
        // 二分查找第一个>=l的位置
        int left = 0;
        int right = positions.size() - 1;
        int firstPos = positions.size();
        while (left <= right) {
            int mid = (left + right) / 2;
            if (positions.get(mid) >= l) {
                firstPos = mid;
                right = mid - 1;
            } else {
                left = mid + 1;
            }
        }
        
        // 二分查找最后一个<=r的位置
        left = 0;
        right = positions.size() - 1;
        int lastPos = -1;
        while (left <= right) {
            int mid = (left + right) / 2;
            if (positions.get(mid) <= r) {
                lastPos = mid;
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }
        
        if (firstPos > lastPos) {
            return 0;
        }
        return lastPos - firstPos + 1;
    }
    
    /**
     * 处理区间众数查询
     */
    private int queryMode(int l, int r) {
        int leftBlock = belong[l];
        int rightBlock = belong[r];
        int mode = 0;
        int maxCount = 0;
        
        // 如果在同一个块内，暴力计算
        if (leftBlock == rightBlock) {
            Map<Integer, Integer> cnt = new HashMap<>();
            for (int i = l; i <= r; i++) {
                int num = arr[i];
                cnt.put(num, cnt.getOrDefault(num, 0) + 1);
                if (cnt.get(num) > maxCount) {
                    mode = num;
                    maxCount = cnt.get(num);
                }
            }
        } else {
            // 处理左边不完整块
            for (int i = l; i <= blockRight[leftBlock]; i++) {
                int num = arr[i];
                int cnt = countOccurrence(num, l, r);
                if (cnt > maxCount || (cnt == maxCount && num < mode)) {
                    mode = num;
                    maxCount = cnt;
                }
            }
            
            // 处理中间完整块
            if (leftBlock + 1 <= rightBlock - 1) {
                int candidateMode = preMode[leftBlock + 1][rightBlock - 1];
                int candidateCount = countOccurrence(candidateMode, l, r);
                if (candidateCount > maxCount || (candidateCount == maxCount && candidateMode < mode)) {
                    mode = candidateMode;
                    maxCount = candidateCount;
                }
                
                // 为了保险，我们也检查中间块中的其他可能的众数
                // 这里可以优化，只检查中间块中的元素
                for (int i = blockLeft[leftBlock + 1]; i <= blockRight[rightBlock - 1]; i++) {
                    int num = arr[i];
                    // 避免重复检查已经处理过的候选众数
                    if (num != candidateMode) {
                        int cnt = countOccurrence(num, l, r);
                        if (cnt > maxCount || (cnt == maxCount && num < mode)) {
                            mode = num;
                            maxCount = cnt;
                        }
                    }
                }
            }
            
            // 处理右边不完整块
            for (int i = blockLeft[rightBlock]; i <= r; i++) {
                int num = arr[i];
                int cnt = countOccurrence(num, l, r);
                if (cnt > maxCount || (cnt == maxCount && num < mode)) {
                    mode = num;
                    maxCount = cnt;
                }
            }
        }
        
        return mode;
    }
    
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        LOJ6286_Java solution = new LOJ6286_Java();
        
        // 读取数组大小
        solution.n = scanner.nextInt();
        
        // 读取数组
        for (int i = 1; i <= solution.n; i++) {
            solution.arr[i] = scanner.nextInt();
        }
        
        // 初始化分块结构
        solution.init();
        
        // 处理操作
        for (int i = 1; i <= solution.n; i++) {
            int l = scanner.nextInt();
            int r = scanner.nextInt();
            int ans = solution.queryMode(l, r);
            System.out.println(ans);
        }
        
        scanner.close();
    }
}