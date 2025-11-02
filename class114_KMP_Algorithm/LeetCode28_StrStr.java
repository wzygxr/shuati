package class101.extended;

/**
 * LeetCode 28. 实现 strStr()
 * 
 * 题目描述：
 * 给你两个字符串 haystack 和 needle，请你在 haystack 字符串中找出 needle 字符串的第一个匹配项的下标（下标从 0 开始）。
 * 如果 needle 不是 haystack 的一部分，则返回 -1。
 * 
 * 示例：
 * 输入：haystack = "sadbutsad", needle = "sad"
 * 输出：0
 * 
 * 输入：haystack = "leetcode", needle = "leeto"
 * 输出：-1
 * 
 * 算法思路：
 * 使用KMP算法进行字符串匹配，避免在匹配失败时文本串指针的回溯。
 * 
 * 时间复杂度：O(n + m)，其中n是文本串长度，m是模式串长度
 * 空间复杂度：O(m)，用于存储next数组
 */
public class LeetCode28_StrStr {

    /**
     * 在文本串haystack中查找模式串needle第一次出现的位置
     * 
     * @param haystack 文本串
     * @param needle 模式串
     * @return 第一次匹配的起始位置，如果未找到则返回-1
     */
    public static int strStr(String haystack, String needle) {
        // 边界条件处理
        if (needle == null || needle.length() == 0) {
            return 0;
        }
        
        if (haystack == null || haystack.length() < needle.length()) {
            return -1;
        }
        
        char[] text = haystack.toCharArray();
        char[] pattern = needle.toCharArray();
        
        return kmpSearch(text, pattern);
    }
    
    /**
     * KMP算法核心实现
     * 
     * 算法步骤：
     * 1. 预处理模式串，生成next数组
     * 2. 使用双指针同时遍历文本串和模式串
     * 3. 当字符匹配时，两个指针都向前移动
     * 4. 当字符不匹配且模式串指针不为0时，根据next数组调整模式串指针
     * 5. 当字符不匹配且模式串指针为0时，文本串指针向前移动
     * 6. 当模式串指针等于模式串长度时，说明匹配成功，返回起始位置
     * 
     * @param text 文本串字符数组
     * @param pattern 模式串字符数组
     * @return 第一次匹配的起始位置，如果未找到则返回-1
     */
    private static int kmpSearch(char[] text, char[] pattern) {
        int textLength = text.length;
        int patternLength = pattern.length;
        
        // 构建next数组
        int[] next = buildNextArray(pattern);
        
        int textIndex = 0;      // 文本串指针
        int patternIndex = 0;   // 模式串指针
        
        // 匹配过程
        while (textIndex < textLength && patternIndex < patternLength) {
            // 字符匹配，两个指针都向前移动
            if (text[textIndex] == pattern[patternIndex]) {
                textIndex++;
                patternIndex++;
            } 
            // 字符不匹配且模式串指针不为0，根据next数组调整模式串指针
            else if (patternIndex > 0) {
                patternIndex = next[patternIndex - 1];
            } 
            // 字符不匹配且模式串指针为0，文本串指针向前移动
            else {
                textIndex++;
            }
        }
        
        // 如果模式串指针等于模式串长度，说明匹配成功
        if (patternIndex == patternLength) {
            return textIndex - patternIndex;
        }
        
        return -1;
    }
    
    /**
     * 构建KMP算法的next数组（部分匹配表）
     * 
     * next[i]表示pattern[0...i]子串的最长相等前后缀的长度
     * 
     * 算法思路：
     * 1. 初始化next[0] = 0
     * 2. 使用双指针i和j，i指向当前处理的位置，j指向前缀的末尾
     * 3. 如果pattern[i] == pattern[j]，说明前缀和后缀可以延长，next[i] = j + 1
     * 4. 如果pattern[i] != pattern[j]，需要回退j指针到next[j-1]，直到匹配或j=0
     * 
     * @param pattern 模式串字符数组
     * @return next数组
     */
    private static int[] buildNextArray(char[] pattern) {
        int length = pattern.length;
        int[] next = new int[length];
        
        // 初始化
        next[0] = 0;
        int prefixLen = 0;  // 当前最长相等前后缀的长度
        int i = 1;          // 当前处理的位置
        
        // 从位置1开始处理
        while (i < length) {
            // 如果当前字符匹配，可以延长相等前后缀
            if (pattern[i] == pattern[prefixLen]) {
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
        String haystack1 = "sadbutsad";
        String needle1 = "sad";
        int result1 = strStr(haystack1, needle1);
        System.out.println("测试用例1: haystack=\"" + haystack1 + "\", needle=\"" + needle1 + "\"");
        System.out.println("预期输出: 0, 实际输出: " + result1);
        System.out.println();
        
        // 测试用例2
        String haystack2 = "leetcode";
        String needle2 = "leeto";
        int result2 = strStr(haystack2, needle2);
        System.out.println("测试用例2: haystack=\"" + haystack2 + "\", needle=\"" + needle2 + "\"");
        System.out.println("预期输出: -1, 实际输出: " + result2);
        System.out.println();
        
        // 测试用例3
        String haystack3 = "hello";
        String needle3 = "ll";
        int result3 = strStr(haystack3, needle3);
        System.out.println("测试用例3: haystack=\"" + haystack3 + "\", needle=\"" + needle3 + "\"");
        System.out.println("预期输出: 2, 实际输出: " + result3);
        System.out.println();
        
        // 测试用例4 - 空模式串
        String haystack4 = "abc";
        String needle4 = "";
        int result4 = strStr(haystack4, needle4);
        System.out.println("测试用例4: haystack=\"" + haystack4 + "\", needle=\"" + needle4 + "\"");
        System.out.println("预期输出: 0, 实际输出: " + result4);
        System.out.println();
        
        // 测试用例5 - 模式串比文本串长
        String haystack5 = "a";
        String needle5 = "aa";
        int result5 = strStr(haystack5, needle5);
        System.out.println("测试用例5: haystack=\"" + haystack5 + "\", needle=\"" + needle5 + "\"");
        System.out.println("预期输出: -1, 实际输出: " + result5);
    }
}