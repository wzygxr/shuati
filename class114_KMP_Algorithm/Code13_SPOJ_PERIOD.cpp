/*
 * SPOJ PERIOD - Period
 * 
 * 题目来源：SPOJ (Sphere Online Judge)
 * 题目链接：https://www.spoj.com/problems/PERIOD/
 * 
 * 题目描述：
 * 对于给定字符串S的每个前缀，我们需要知道它是否是周期字符串。
 * 也就是说，对于每个i (2 <= i <= N) 我们要找到满足条件的最小的K (K > 1)，
 * 使得长度为i的前缀可以写成某个字符串重复K次的形式。
 * 如果不存在这样的K，则输出0。
 * 
 * 例如：对于字符串 "aabaab"，长度为6的前缀 "aabaab" 可以写成 "aab" 重复2次，
 * 所以K=2。
 * 
 * 算法思路：
 * 使用KMP算法的next数组来解决这个问题。
 * 对于长度为i的前缀，如果i % (i - next[i]) == 0且next[i] > 0，
 * 则该前缀是周期字符串，周期长度为i - next[i]，周期数为i / (i - next[i])。
 * 
 * 时间复杂度：O(N)，其中N是字符串长度
 * 空间复杂度：O(N)，用于存储next数组
 */

// 定义最大字符串长度
#define MAXN 1000001

// 全局变量存储next数组和周期数组
int next_array[MAXN];
int periods[MAXN];

/*
 * 构建KMP算法的next数组（部分匹配表）
 * 
 * next[i]表示str[0...i-1]子串的最长相等前后缀的长度
 * 
 * @param str 字符数组
 * @param length 字符数组长度
 */
void build_next_array(char* str, int length) {
    // 初始化
    next_array[0] = 0;
    int prefix_len = 0;  // 当前最长相等前后缀的长度
    int i = 1;          // 当前处理的位置
    
    // 从位置1开始处理
    while (i < length) {
        // 如果当前字符匹配，可以延长相等前后缀
        if (str[i] == str[prefix_len]) {
            prefix_len++;
            next_array[i] = prefix_len;
            i++;
        } 
        // 如果不匹配且前缀长度大于0，需要回退
        else if (prefix_len > 0) {
            prefix_len = next_array[prefix_len - 1];
        } 
        // 如果不匹配且前缀长度为0，next[i] = 0
        else {
            next_array[i] = 0;
            i++;
        }
    }
}

/*
 * 计算字符串各前缀的周期数
 * 
 * @param s 输入字符串
 * @param n 字符串长度
 */
void calculate_periods(char* s, int n) {
    // 构建next数组
    build_next_array(s, n);
    
    for (int i = 2; i <= n; i++) {
        int len = i - next_array[i - 1]; // 周期长度
        if (len < i && i % len == 0 && next_array[i - 1] > 0) {
            periods[i] = i / len; // 周期数 = 总长度 / 周期长度
        } else {
            periods[i] = 0; // 不是周期字符串
        }
    }
}