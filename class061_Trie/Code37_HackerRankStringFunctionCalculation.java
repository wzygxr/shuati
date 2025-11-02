package class045_Trie;

import java.util.*;
import java.io.*;

/**
 * HackerRank String Function Calculation
 * 
 * 题目描述：
 * 给定一个字符串t，定义函数f(S) = |S| * (S在t中出现的次数)，其中S是t的任意子串。
 * 求所有子串S中f(S)的最大值。
 * 
 * 解题思路：
 * 这是一个经典的后缀数组应用问题。我们可以使用以下方法：
 * 1. 构建字符串的后缀数组和高度数组（LCP数组）
 * 2. 对于每个可能的子串长度，计算该长度的所有子串的出现次数
 * 3. 使用单调栈来高效计算每个长度对应的最大出现次数
 * 
 * 具体步骤：
 * 1. 构建后缀数组和LCP数组
 * 2. 对于LCP数组中的每个值，使用单调栈计算以该值为最小值的区间能贡献的最大f值
 * 3. 同时考虑所有单个字符的情况
 * 
 * 时间复杂度：O(N)
 * 空间复杂度：O(N)
 * 
 * 注意：这个问题也可以用后缀自动机解决，但后缀数组更容易理解和实现。
 */
public class Code37_HackerRankStringFunctionCalculation {
    
    /**
     * 构建字符串的后缀数组
     * @param s 输入字符串
     * @return 后缀数组
     */
    public static int[] suffixArray(String s) {
        int n = s.length();
        Integer[] suffixes = new Integer[n];
        
        // 初始化：按第一个字符排序
        for (int i = 0; i < n; i++) {
            suffixes[i] = i;
        }
        
        final String str = s;
        Arrays.sort(suffixes, (a, b) -> Character.compare(str.charAt(a), str.charAt(b)));
        
        // 倍增算法构建后缀数组
        int[] rank = new int[n];
        int[] tempRank = new int[n];
        int k = 1;
        
        while (k < n) {
            // 更新rank数组
            for (int i = 0; i < n; i++) {
                rank[suffixes[i]] = i;
            }
            
            // 根据前k个字符的排名对后缀进行排序
            final int kFinal = k;
            final int[] rankFinal = rank;
            Arrays.sort(suffixes, (a, b) -> {
                if (rankFinal[a] != rankFinal[b]) {
                    return Integer.compare(rankFinal[a], rankFinal[b]);
                }
                int nextRankA = (a + kFinal < n) ? rankFinal[a + kFinal] : -1;
                int nextRankB = (b + kFinal < n) ? rankFinal[b + kFinal] : -1;
                return Integer.compare(nextRankA, nextRankB);
            });
            
            k *= 2;
        }
        
        return Arrays.stream(suffixes).mapToInt(Integer::intValue).toArray();
    }
    
    /**
     * 根据后缀数组构建LCP数组
     * @param s 输入字符串
     * @param suffixArray 后缀数组
     * @return LCP数组
     */
    public static int[] lcpArray(String s, int[] suffixArray) {
        int n = s.length();
        int[] rank = new int[n];
        for (int i = 0; i < n; i++) {
            rank[suffixArray[i]] = i;
        }
        
        int[] lcp = new int[n];
        int h = 0;
        
        for (int i = 0; i < n; i++) {
            if (rank[i] > 0) {
                int j = suffixArray[rank[i] - 1];
                while (i + h < n && j + h < n && s.charAt(i + h) == s.charAt(j + h)) {
                    h++;
                }
                lcp[rank[i]] = h;
                if (h > 0) {
                    h--;
                }
            }
        }
        
        return lcp;
    }
    
    /**
     * 解决String Function Calculation问题
     * @param s 输入字符串
     * @return f(S)的最大值
     */
    public static long solveStringFunctionCalculation(String s) {
        if (s == null || s.isEmpty()) {
            return 0;
        }
        
        int n = s.length();
        
        // 特殊情况：所有字符相同
        boolean allSame = true;
        for (int i = 1; i < n; i++) {
            if (s.charAt(i) != s.charAt(0)) {
                allSame = false;
                break;
            }
        }
        
        if (allSame) {
            // 对于n个相同字符，长度为k的子串出现次数为n-k+1
            // f(k) = k * (n-k+1)
            // 求最大值
            long maxVal = 0;
            for (int k = 1; k <= n; k++) {
                long val = (long) k * (n - k + 1);
                maxVal = Math.max(maxVal, val);
            }
            return maxVal;
        }
        
        // 构建后缀数组和LCP数组
        int[] sa = suffixArray(s);
        int[] lcp = lcpArray(s, sa);
        
        // 使用单调栈计算最大f值
        // 在LCP数组上使用单调栈，计算每个LCP值能贡献的最大f值
        Stack<Integer> stack = new Stack<>();
        long maxResult = n; // 至少有n个单字符子串，每个出现1次，f=1*n=n
        
        // 在LCP数组前后添加0，便于处理边界情况
        int[] extendedLcp = new int[lcp.length + 2];
        System.arraycopy(lcp, 0, extendedLcp, 1, lcp.length);
        
        for (int i = 0; i < extendedLcp.length; i++) {
            // 维护单调递增栈
            while (!stack.isEmpty() && 
                   (i == extendedLcp.length - 1 || extendedLcp[stack.peek()] > extendedLcp[i])) {
                // 弹出栈顶元素，计算以该元素为最小值的区间的贡献
                int idx = stack.pop();
                int height = extendedLcp[idx];
                
                // 计算区间的左右边界
                int left = stack.isEmpty() ? 0 : stack.peek() + 1;
                int right = i - 1;
                
                // 区间长度
                int width = right - left + 1;
                
                // 以height为长度的子串出现次数为width
                // f = height * width
                if (height > 0) {
                    long result = (long) height * width;
                    maxResult = Math.max(maxResult, result);
                }
            }
            
            stack.push(i);
        }
        
        return maxResult;
    }
    
    /**
     * 主函数
     * @param args 命令行参数
     */
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        String line = scanner.nextLine().trim();
        if (line.isEmpty()) {
            scanner.close();
            return;
        }
        
        // 求解并输出结果
        long result = solveStringFunctionCalculation(line);
        System.out.println(result);
        
        scanner.close();
    }
}