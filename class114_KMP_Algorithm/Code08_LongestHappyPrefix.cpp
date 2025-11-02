/*
 * LeetCode 1392. 最长快乐前缀
 * 
 * 题目描述：
 * 「快乐前缀」是在原字符串中既是非空前缀也是后缀（不包括原字符串自身）的字符串。
 * 给你一个字符串 s，请你返回它的最长快乐前缀。
 * 如果不存在满足题意的前缀，则返回一个空字符串 ""。
 * 
 * 示例：
 * 输入：s = "level"
 * 输出："l"
 * 
 * 输入：s = "ababab"
 * 输出："abab"
 * 
 * 算法思路：
 * 使用KMP算法的next数组来解决这个问题。
 * 最长快乐前缀就是字符串的最长相等前后缀。
 * next[n-1]表示整个字符串的最长相等前后缀的长度。
 * 因此，答案就是长度为next[n-1]的前缀。
 * 
 * 时间复杂度：O(N)，其中N是字符串长度
 * 空间复杂度：O(N)，用于存储next数组
 */

#define MAXN 1000001

int next[MAXN];

/*
 * 构建KMP算法的next数组（部分匹配表）
 * 
 * next[i]表示str[0...i]子串的最长相等前后缀的长度
 */
void buildNextArray(char* str, int length) {
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
}

/*
 * 找到字符串的最长快乐前缀
 * 
 * @param s 输入字符串
 * @param n 字符串长度
 * @param result 存储结果的字符数组
 */
void longestPrefix(char* s, int n, char* result) {
    // 边界条件处理
    if (n <= 1) {
        result[0] = '\0';  // 空字符串
        return;
    }
    
    // 构建next数组
    buildNextArray(s, n);
    
    // 最长快乐前缀的长度就是next[n-1]
    int prefixLength = next[n - 1];
    
    // 复制长度为prefixLength的前缀到结果数组
    for (int i = 0; i < prefixLength; i++) {
        result[i] = s[i];
    }
    result[prefixLength] = '\0';  // 字符串结束符
}

// 为了符合项目要求，不包含任何输出语句
// 实际使用时可以根据需要添加适当的输出代码
int main() {
    // 测试用例1
    char s1[] = "level";
    char result1[MAXN];
    longestPrefix(s1, 5, result1);
    
    // 测试用例2
    char s2[] = "ababab";
    char result2[MAXN];
    longestPrefix(s2, 6, result2);
    
    // 测试用例3
    char s3[] = "leetcodeleet";
    char result3[MAXN];
    longestPrefix(s3, 12, result3);
    
    // 测试用例4
    char s4[] = "a";
    char result4[MAXN];
    longestPrefix(s4, 1, result4);
    
    // 测试用例5
    char s5[] = "abcabcabc";
    char result5[MAXN];
    longestPrefix(s5, 9, result5);
    
    return 0;
}