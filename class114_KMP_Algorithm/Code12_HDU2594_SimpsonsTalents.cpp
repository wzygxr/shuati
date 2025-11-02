/*
 * HDU 2594 Simpsons' Hidden Talents
 * 
 * 题目来源：HDU (杭州电子科技大学在线评测系统)
 * 题目链接：http://acm.hdu.edu.cn/showproblem.php?pid=2594
 * 
 * 题目描述：
 * 给定两个字符串s1和s2，找到最长的字符串s，使得s既是s1的后缀，又是s2的前缀。
 * 输出这个字符串s及其长度。如果不存在这样的字符串，输出0。
 * 
 * 示例：
 * 输入：s1 = "abcabc", s2 = "bcabca"
 * 输出："bca" 3
 * 
 * 算法思路：
 * 使用KMP算法的思想来解决这个问题。
 * 1. 将s1和s2连接成一个新字符串，中间用特殊字符分隔
 * 2. 构建新字符串的next数组
 * 3. 通过分析next数组找到最长的公共前后缀
 * 
 * 时间复杂度：O(n + m)，其中n是s1的长度，m是s2的长度
 * 空间复杂度：O(n + m)
 */

// 定义最大字符串长度
#define MAXN 2000002

// 全局变量存储next数组
int next_array[MAXN];

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
 * 找到s1的后缀和s2的前缀的最长公共部分
 * 
 * @param s1 第一个字符串
 * @param n1 第一个字符串长度
 * @param s2 第二个字符串
 * @param n2 第二个字符串长度
 * @param common_part 存储公共部分的字符数组
 * @return 公共部分的长度
 */
int find_longest_common_suffix_prefix(char* s1, int n1, char* s2, int n2, char* common_part) {
    // 边界条件处理
    if (n1 == 0 || n2 == 0) {
        return 0;
    }
    
    // 构造新字符串：s1 + "#" + s2
    // 使用特殊字符"#"来分隔两个字符串，确保不会产生虚假匹配
    char* combined = new char[n1 + n2 + 2];
    int combined_len = 0;
    
    // 复制s1
    for (int i = 0; i < n1; i++) {
        combined[combined_len++] = s1[i];
    }
    
    // 添加分隔符
    combined[combined_len++] = '#';
    
    // 复制s2
    for (int i = 0; i < n2; i++) {
        combined[combined_len++] = s2[i];
    }
    
    combined[combined_len] = '\0';
    
    // 构建next数组
    build_next_array(combined, combined_len);
    
    // 最长公共部分的长度就是next[combined_len - 1]
    int common_length = next_array[combined_len - 1];
    
    // 确保公共部分不会超过任何一个字符串的长度
    common_length = (common_length < n1) ? common_length : n1;
    common_length = (common_length < n2) ? common_length : n2;
    
    // 复制公共部分到结果数组
    for (int i = 0; i < common_length; i++) {
        common_part[i] = s1[n1 - common_length + i];
    }
    common_part[common_length] = '\0';
    
    delete[] combined;
    
    return common_length;
}