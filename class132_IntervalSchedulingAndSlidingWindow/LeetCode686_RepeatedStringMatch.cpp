/**
 * LeetCode 686. Repeated String Match
 * 
 * 题目描述：
 * 给定两个字符串 a 和 b，寻找重复 a 的最小次数，使得 b 成为重复 a 后的字符串的子串。
 * 如果不存在这样的次数，则返回 -1。
 * 
 * 解题思路：
 * 这是一个字符串匹配问题，需要找到最小的重复次数使得 b 成为重复 a 后的字符串的子串。
 * 
 * 算法步骤：
 * 1. 计算理论最小重复次数：ceil(len(b) / len(a))
 * 2. 从理论最小次数开始尝试，最多尝试 3 次额外的重复
 * 3. 对于每次重复，检查 b 是否为重复字符串的子串
 * 4. 如果找到则返回重复次数，否则返回 -1
 * 
 * 为什么最多尝试 3 次额外重复：
 * - 理论最小次数确保重复字符串长度 >= b 的长度
 * - 额外的 1-2 次重复处理边界情况：
 *   - b 可能从一个 a 的末尾开始
 *   - b 可能到下一个 a 的开头结束
 * - 如果 3 次尝试后仍未找到，则说明不可能匹配
 * 
 * 时间复杂度：O(n * m)，其中 n 是 a 的长度，m 是重复次数
 * 空间复杂度：O(n * m)
 * 
 * 相关题目：
 * - LeetCode 466. 统计重复个数（字符串匹配与循环节）
 * - LeetCode 459. 重复的子字符串
 * - LeetCode 28. 实现 strStr()
 */

// 简化版C++实现，避免使用STL容器
// 由于编译环境限制，使用基本数组和手动实现算法

const int MAX_LEN = 10005;

// 字符串结构
struct String {
    char data[MAX_LEN];
    int length;
};

// 计算字符串长度
int strlen_custom(char* str) {
    int len = 0;
    while (str[len] != '\0') {
        len++;
    }
    return len;
}

// 字符串复制
void strcpy_custom(char* dest, char* src) {
    int i = 0;
    while (src[i] != '\0') {
        dest[i] = src[i];
        i++;
    }
    dest[i] = '\0';
}

// 字符串连接
void strcat_custom(char* dest, char* src) {
    int dest_len = strlen_custom(dest);
    int i = 0;
    while (src[i] != '\0') {
        dest[dest_len + i] = src[i];
        i++;
    }
    dest[dest_len + i] = '\0';
}

// 检查子串
bool contains(char* str, char* substr) {
    int str_len = strlen_custom(str);
    int substr_len = strlen_custom(substr);
    
    if (substr_len > str_len) {
        return false;
    }
    
    for (int i = 0; i <= str_len - substr_len; i++) {
        bool match = true;
        for (int j = 0; j < substr_len; j++) {
            if (str[i + j] != substr[j]) {
                match = false;
                break;
            }
        }
        if (match) {
            return true;
        }
    }
    return false;
}

/**
 * 寻找重复 a 的最小次数，使得 b 成为重复 a 后的字符串的子串
 * 
 * @param a 字符串 a
 * @param b 字符串 b
 * @return 最小重复次数，如果不存在则返回 -1
 */
int repeatedStringMatch(char* a, char* b) {
    int lenA = strlen_custom(a);
    int lenB = strlen_custom(b);
    
    // 边界情况处理
    if (lenA == 0) {
        return -1;
    }
    
    if (lenB == 0) {
        return 0;
    }
    
    // 计算理论最小重复次数
    // 确保重复后的字符串长度至少等于 b 的长度
    int minRepetitions = (lenB + lenA - 1) / lenA;
    
    // 构建初始重复字符串
    char repeatedStr[MAX_LEN * 10] = "";
    for (int i = 0; i < minRepetitions; i++) {
        strcat_custom(repeatedStr, a);
    }
    
    // 尝试最多 3 次额外重复
    // 处理边界情况：b 可能跨越多个 a 的边界
    for (int i = 0; i < 3; i++) {
        // 检查 b 是否为当前重复字符串的子串
        if (contains(repeatedStr, b)) {
            return minRepetitions;
        }
        
        // 添加一次额外重复
        minRepetitions++;
        strcat_custom(repeatedStr, a);
    }
    
    // 如果尝试了足够的重复次数仍未找到，则不可能匹配
    return -1;
}

// 简单的测试函数
void runTests() {
    // 测试用例需要在实际环境中运行
    // 由于没有标准输出库，我们无法直接打印结果
}