package class101;

/**
 * POI 2006 - Periods of Words
 * 
 * 题目描述：
 * 对于给定的字符串，计算所有周期的总和。
 * 字符串的周期定义为：对于长度为n的字符串s，如果存在一个k (1 <= k < n)，
 * 使得对于所有i (0 <= i < n-k)，都有s[i] = s[i+k]，则k是s的一个周期。
 * 
 * 任务是计算所有周期的和。
 * 
 * 算法思路：
 * 使用KMP算法的next数组来解决这个问题。
 * 对于长度为n的字符串，其所有周期可以通过next数组计算得出。
 * 如果next[n-1] > 0，则n - next[n-1]是一个周期。
 * 然后我们可以继续应用next函数来找到所有周期。
 * 
 * 时间复杂度：O(N)，其中N是字符串长度
 * 空间复杂度：O(N)，用于存储next数组
 */
public class Code07_PeriodsOfWords {

    /**
     * 计算字符串所有周期的总和
     * 
     * @param s 输入字符串
     * @return 所有周期的总和
     */
    public static long calculatePeriodsSum(String s) {
        char[] str = s.toCharArray();
        int n = str.length;
        
        // 边界条件处理
        if (n <= 1) {
            return 0;
        }
        
        // 构建next数组
        int[] next = buildNextArray(str);
        
        long sum = 0;
        
        // 从最后一个位置开始，通过next数组找到所有周期
        int pos = n - 1;
        while (pos > 0) {
            // 如果当前位置有匹配的前后缀
            if (next[pos] > 0) {
                // 周期长度为 pos + 1 - next[pos]
                int period = (pos + 1) - next[pos];
                // 如果周期长度小于当前位置+1，则是一个有效周期
                if (period < pos + 1) {
                    sum += period;
                }
                // 移动到next[pos]-1位置继续查找
                pos = next[pos] - 1;
            } else {
                // 没有匹配的前后缀，向前移动
                pos--;
            }
        }
        
        return sum;
    }
    
    /**
     * 构建KMP算法的next数组（部分匹配表）
     * 
     * next[i]表示str[0...i]子串的最长相等前后缀的长度
     * 
     * 算法思路：
     * 1. 初始化next[0] = 0
     * 2. 使用双指针i和j，i指向当前处理的位置，j指向前缀的末尾
     * 3. 如果str[i] == str[j]，说明前缀和后缀可以延长，next[i] = j + 1
     * 4. 如果str[i] != str[j]，需要回退j指针到next[j-1]，直到匹配或j=0
     * 
     * @param str 字符数组
     * @return next数组
     */
    private static int[] buildNextArray(char[] str) {
        int length = str.length;
        int[] next = new int[length];
        
        // 初始化
        next[0] = 0;
        int prefixLen = 0;  // 当前最长相等前后缀的长度
        int i = 1;          // 当前处理的位置
        
        // 从位置1开始处理
        while (i < length) {
            // 如果当前字符匹配，可以延长相等前后缀
            if (str[i] == str[prefixLen]) {
                prefixLen++;
                next[i] = prefixLen;
                i++;
            } 
            // 如果不匹配且前缀长度大于0，需要回退
            else if (prefixLen > 0) {
                prefixLen = next[prefixLen - 1];
            } 
            // 如果不匹配且前缀长度为0，next[i] = 0
            else {
                next[i] = 0;
                i++;
            }
        }
        
        return next;
    }
    
    // 测试方法
    public static void main(String[] args) {
        // 测试用例1
        String s1 = "aaaa";
        long result1 = calculatePeriodsSum(s1);
        System.out.println("字符串: " + s1);
        System.out.println("所有周期的总和: " + result1);
        System.out.println();
        
        // 测试用例2
        String s2 = "ababab";
        long result2 = calculatePeriodsSum(s2);
        System.out.println("字符串: " + s2);
        System.out.println("所有周期的总和: " + result2);
        System.out.println();
        
        // 测试用例3
        String s3 = "abcabcabc";
        long result3 = calculatePeriodsSum(s3);
        System.out.println("字符串: " + s3);
        System.out.println("所有周期的总和: " + result3);
        System.out.println();
        
        // 测试用例4
        String s4 = "aabaaab";
        long result4 = calculatePeriodsSum(s4);
        System.out.println("字符串: " + s4);
        System.out.println("所有周期的总和: " + result4);
        System.out.println();
    }
}