/*
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
 * 计算字符串所有周期的总和
 * 
 * @param s 输入字符串
 * @param n 字符串长度
 * @return 所有周期的总和
 */
long long calculatePeriodsSum(char* s, int n) {
    // 边界条件处理
    if (n <= 1) {
        return 0;
    }
    
    // 构建next数组
    buildNextArray(s, n);
    
    long long sum = 0;
    
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

// 为了符合项目要求，不包含任何输出语句
// 实际使用时可以根据需要添加适当的输出代码
int main() {
    // 测试用例1
    char s1[] = "aaaa";
    long long result1 = calculatePeriodsSum(s1, 4);
    
    // 测试用例2
    char s2[] = "ababab";
    long long result2 = calculatePeriodsSum(s2, 6);
    
    // 测试用例3
    char s3[] = "abcabcabc";
    long long result3 = calculatePeriodsSum(s3, 9);
    
    // 测试用例4
    char s4[] = "aabaaab";
    long long result4 = calculatePeriodsSum(s4, 7);
    
    return 0;
}