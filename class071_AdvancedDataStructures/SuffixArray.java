package class186;

import java.util.*;
import java.io.*;

/**
 * 后缀数组（Suffix Array）实现
 * 包含：
 * 1. 倍增法构造后缀数组 O(n log n)
 * 2. 计算height数组 O(n)
 * 3. ST表预处理用于LCP查询 O(n log n) 预处理，O(1) 查询
 * 时间复杂度：构造O(n log n)，查询O(1)
 * 空间复杂度：O(n log n)
 */
public class SuffixArray {
    private String text;          // 原始文本
    private int[] suffixArray;    // 后缀数组，存储排序后的后缀起始位置
    private int[] rank;           // rank[i]表示起始位置为i的后缀的排名
    private int[] height;         // height[i]表示后缀数组中第i个和第i-1个后缀的LCP
    private int[][] stTable;      // ST表，用于LCP区间查询
    private int logN;             // log2(n)的上界
    private int n;                // 文本长度

    /**
     * 构造函数，构建后缀数组
     * @param text 输入文本
     */
    public SuffixArray(String text) {
        this.text = text;
        this.n = text.length();
        
        // 构造后缀数组
        buildSuffixArray();
        
        // 计算height数组
        computeHeight();
        
        // 构建ST表
        buildST();
    }
    
    /**
     * 使用倍增法构建后缀数组
     */
    private void buildSuffixArray() {
        // 初始化
        suffixArray = new int[n];
        rank = new int[n];
        int[] tempRank = new int[n];
        
        // 初始阶段：每个字符单独排名
        for (int i = 0; i < n; i++) {
            suffixArray[i] = i;
            rank[i] = text.charAt(i); // 初始排名为字符的ASCII值
        }
        
        // 创建Integer数组用于自定义排序
        Integer[] indices = new Integer[n];
        for (int i = 0; i < n; i++) {
            indices[i] = i;
        }
        
        // 倍增排序
        for (int k = 1; k < n; k *= 2) {
            // 按照当前排名和k位置后的排名进行稳定排序
            // 使用自定义的比较器进行排序
            final int kFinal = k; // 创建final变量供lambda表达式使用
            Arrays.sort(indices, (i, j) -> {
                // 首先比较主排名
                if (rank[i] != rank[j]) {
                    return Integer.compare(rank[i], rank[j]);
                }
                // 主排名相同则比较k位置后的排名
                int rankI = (i + kFinal < n) ? rank[i + kFinal] : -1;
                int rankJ = (j + kFinal < n) ? rank[j + kFinal] : -1;
                return Integer.compare(rankI, rankJ);
            });
            
            // 更新suffixArray
            for (int i = 0; i < n; i++) {
                suffixArray[i] = indices[i];
            }
            
            // 更新排名
            tempRank[suffixArray[0]] = 0;
            for (int i = 1; i < n; i++) {
                // 如果当前后缀与前一个后缀的排名相同，则给予相同的排名
                if (rank[suffixArray[i]] == rank[suffixArray[i-1]] && 
                    getRank(suffixArray[i], k) == getRank(suffixArray[i-1], k)) {
                    tempRank[suffixArray[i]] = tempRank[suffixArray[i-1]];
                } else {
                    tempRank[suffixArray[i]] = tempRank[suffixArray[i-1]] + 1;
                }
            }
            
            // 将临时排名复制回rank数组
            System.arraycopy(tempRank, 0, rank, 0, n);
        }
    }
    
    /**
     * 获取位置i开始的后缀，向后移动k位后的排名
     */
    private int getRank(int i, int k) {
        return (i + k < n) ? rank[i + k] : -1;
    }
    
    /**
     * 计算height数组
     * 利用性质：height[rank[i]] >= height[rank[i-1]] - 1
     */
    private void computeHeight() {
        height = new int[n];
        int[] rankToSuffix = new int[n]; // rankToSuffix[r]表示排名为r的后缀的起始位置
        
        // 构建rank到suffix的映射
        for (int i = 0; i < n; i++) {
            rankToSuffix[rank[i]] = i;
        }
        
        int k = 0; // 当前LCP长度
        for (int i = 0; i < n; i++) {
            int r = rank[i];
            if (r == 0) {
                height[r] = 0; // 排名为0的后缀没有前一个后缀
                continue;
            }
            
            // 获取前一个排名的后缀起始位置
            int j = rankToSuffix[r - 1];
            
            // 从上一轮的k-1开始比较（利用性质优化）
            if (k > 0) k--;
            
            // 扩展LCP
            while (i + k < n && j + k < n && text.charAt(i + k) == text.charAt(j + k)) {
                k++;
            }
            
            height[r] = k;
        }
    }
    
    /**
     * 构建ST表用于RMQ（区间最小值查询）
     */
    private void buildST() {
        // 计算log2(n)的上界
        logN = 0;
        int temp = 1;
        while (temp * 2 <= n) {
            temp *= 2;
            logN++;
        }
        
        // 初始化ST表
        stTable = new int[logN + 1][n];
        
        // 填充第0层（原始height数组）
        for (int i = 0; i < n; i++) {
            stTable[0][i] = height[i];
        }
        
        // 构建其余层
        for (int k = 1; k <= logN; k++) {
            for (int i = 0; i + (1 << k) <= n; i++) {
                // stTable[k][i] = min(stTable[k-1][i], stTable[k-1][i + (1 << (k-1))])
                stTable[k][i] = Math.min(
                    stTable[k-1][i], 
                    stTable[k-1][i + (1 << (k-1))]
                );
            }
        }
    }
    
    /**
     * 计算区间[l, r]的最小值
     * @param l 左边界（包含）
     * @param r 右边界（包含）
     * @return 区间最小值
     */
    private int queryMin(int l, int r) {
        if (l > r) {
            int temp = l;
            l = r;
            r = temp;
        }
        
        // 计算区间长度的对数
        int k = 0;
        int len = r - l + 1;
        while ((1 << (k + 1)) <= len) {
            k++;
        }
        
        // 查询最小值
        return Math.min(
            stTable[k][l], 
            stTable[k][r - (1 << k) + 1]
        );
    }
    
    /**
     * 计算两个后缀的最长公共前缀（LCP）
     * @param i 第一个后缀的起始位置
     * @param j 第二个后缀的起始位置
     * @return LCP长度
     */
    public int getLCP(int i, int j) {
        if (i < 0 || i >= n || j < 0 || j >= n) {
            throw new IndexOutOfBoundsException("后缀起始位置超出范围");
        }
        
        if (i == j) return n - i; // 同一个后缀，LCP就是后缀长度
        
        // 获取两个后缀的排名
        int r1 = rank[i];
        int r2 = rank[j];
        
        // 确保r1 < r2
        if (r1 > r2) {
            int temp = r1;
            r1 = r2;
            r2 = temp;
        }
        
        // 后缀排序中，LCP(r1, r2) = min{height[r1+1], height[r1+2], ..., height[r2]}
        return queryMin(r1 + 1, r2);
    }
    
    /**
     * 获取后缀数组
     * @return 后缀数组
     */
    public int[] getSuffixArray() {
        return Arrays.copyOf(suffixArray, suffixArray.length);
    }
    
    /**
     * 获取height数组
     * @return height数组
     */
    public int[] getHeightArray() {
        return Arrays.copyOf(height, height.length);
    }
    
    /**
     * 获取rank数组
     * @return rank数组
     */
    public int[] getRankArray() {
        return Arrays.copyOf(rank, rank.length);
    }
    
    /**
     * 查找模式串在文本串中所有出现的位置
     * @param pattern 模式串
     * @return 所有匹配位置的起始索引数组
     */
    public int[] findPattern(String pattern) {
        if (pattern == null) {
            throw new IllegalArgumentException("模式串不能为null");
        }
        
        int m = pattern.length();
        if (m == 0) {
            // 空模式串匹配所有位置
            int[] result = new int[n + 1];
            for (int i = 0; i <= n; i++) {
                result[i] = i;
            }
            return result;
        }
        
        if (m > n) {
            // 模式串比文本串长，无匹配
            return new int[0];
        }
        
        // 二分查找第一个可能的匹配位置
        int left = 0, right = n - 1;
        int firstMatch = -1;
        
        while (left <= right) {
            int mid = (left + right) / 2;
            int suffixStart = suffixArray[mid];
            int compareResult = compareSuffixWithPattern(suffixStart, pattern);
            
            if (compareResult == 0) {
                firstMatch = mid;
                right = mid - 1; // 继续向左查找第一个匹配
            } else if (compareResult < 0) {
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }
        
        if (firstMatch == -1) {
            // 未找到匹配
            return new int[0];
        }
        
        // 查找最后一个匹配位置
        left = firstMatch;
        right = n - 1;
        int lastMatch = firstMatch;
        
        while (left <= right) {
            int mid = (left + right) / 2;
            int suffixStart = suffixArray[mid];
            int compareResult = compareSuffixWithPattern(suffixStart, pattern);
            
            if (compareResult == 0) {
                lastMatch = mid;
                left = mid + 1; // 继续向右查找最后一个匹配
            } else if (compareResult < 0) {
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }
        
        // 收集所有匹配位置
        int[] matches = new int[lastMatch - firstMatch + 1];
        for (int i = firstMatch; i <= lastMatch; i++) {
            matches[i - firstMatch] = suffixArray[i];
        }
        
        // 按升序排序
        Arrays.sort(matches);
        return matches;
    }
    
    /**
     * 比较以pos开始的后缀和模式串
     */
    private int compareSuffixWithPattern(int pos, String pattern) {
        int m = pattern.length();
        for (int i = 0; i < m; i++) {
            if (pos + i >= n) {
                // 后缀已结束，模式串未结束，后缀小
                return -1;
            }
            char c1 = text.charAt(pos + i);
            char c2 = pattern.charAt(i);
            if (c1 != c2) {
                return c1 < c2 ? -1 : 1;
            }
        }
        // 前缀相同，说明匹配
        return 0;
    }
    
    /**
     * 主函数用于测试
     */
    public static void main(String[] args) {
        String text = "banana";
        SuffixArray sa = new SuffixArray(text);
        
        System.out.println("文本: " + text);
        System.out.println("后缀数组: " + Arrays.toString(sa.getSuffixArray()));
        System.out.println("rank数组: " + Arrays.toString(sa.getRankArray()));
        System.out.println("height数组: " + Arrays.toString(sa.getHeightArray()));
        
        // 测试LCP查询
        System.out.println("LCP(0, 2): " + sa.getLCP(0, 2)); // "banana" 和 "nana"
        System.out.println("LCP(1, 3): " + sa.getLCP(1, 3)); // "anana" 和 "ana"
        
        // 测试模式匹配
        String pattern = "ana";
        int[] matches = sa.findPattern(pattern);
        System.out.println("模式 '" + pattern + "' 的匹配位置: " + Arrays.toString(matches));
    }
}