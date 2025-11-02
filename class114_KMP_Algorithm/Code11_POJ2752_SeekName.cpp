/*
 * POJ 2752 Seek the Name, Seek the Fame
 * 
 * 题目来源：POJ (北京大学在线评测系统)
 * 题目链接：http://poj.org/problem?id=2752
 * 
 * 题目描述：
 * 给定一个字符串，找到所有既是前缀又是后缀的子串。
 * 输出这些子串的长度，按升序排列。
 * 
 * 示例：
 * 输入："alala"
 * 输出："a", "ala", "alala"，对应的长度为1, 3, 5
 * 
 * 算法思路：
 * 使用KMP算法的next数组来解决这个问题。
 * 1. 构建字符串的next数组
 * 2. 通过next[n-1]找到最长的既是前缀又是后缀的子串
 * 3. 通过递归应用next函数，找到所有符合条件的子串
 * 
 * 时间复杂度：O(n)
 * 空间复杂度：O(n)
 */

// 定义最大字符串长度
#define MAXN 1000001

// 全局变量存储next数组和结果数组
int next_array[MAXN];
int result[MAXN];
int result_count;

/*
 * 构建KMP算法的next数组（部分匹配表）
 * 
 * next[i]表示str[0...i]子串的最长相等前后缀的长度
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
 * 找到所有既是前缀又是后缀的子串的长度
 * 
 * @param s 输入字符串
 * @param n 字符串长度
 */
void find_all_prefix_suffix_lengths(char* s, int n) {
    result_count = 0;
    
    // 边界条件处理
    if (n == 0) {
        return;
    }
    
    // 构建next数组
    build_next_array(s, n);
    
    // 通过next数组找到所有符合条件的长度
    int pos = n - 1;
    while (pos >= 0) {
        if (next_array[pos] > 0) {
            result[result_count++] = next_array[pos];
            pos = next_array[pos] - 1;
        } else {
            pos--;
        }
    }
    
    // 添加整个字符串的长度
    result[result_count++] = n;
    
    // 简单的冒泡排序实现升序排列
    for (int i = 0; i < result_count - 1; i++) {
        for (int j = 0; j < result_count - 1 - i; j++) {
            if (result[j] > result[j + 1]) {
                int temp = result[j];
                result[j] = result[j + 1];
                result[j + 1] = temp;
            }
        }
    }
}