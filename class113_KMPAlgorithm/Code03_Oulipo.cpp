/**
 * POJ 3461 Oulipo - KMP算法实现（C++版本）
 * 题目链接: http://poj.org/problem?id=3461
 * 
 * 题目描述:
 * 给定一个模式串W和一个文本串T，计算模式串W在文本串T中出现的次数。
 * 匹配的子串可以重叠。
 * 
 * KMP算法数学原理深度解析:
 * 
 * 1. 问题本质:
 *    - 字符串匹配问题是计算机科学中的基础问题
 *    - 朴素匹配算法时间复杂度为O(n*m)，当字符串较长时效率低下
 *    - KMP算法通过巧妙利用已匹配信息，将时间复杂度优化到O(n+m)
 * 
 * 2. 核心创新点:
 *    - 避免在匹配失败时文本串指针的回溯
 *    - 利用已匹配部分的结构信息，直接跳过已知不可能匹配的位置
 *    - 通过预处理模式串，构建next数组（部分匹配表）
 * 
 * 3. next数组数学定义:
 *    - next[i]表示模式串中以i-1位置字符结尾的子串的最长相等前后缀长度
 *    - 前缀: 不包含最后一个字符的子串
 *    - 后缀: 不包含第一个字符的子串
 * 
 * 4. KMP算法正确性证明:
 *    - 数学归纳法证明next数组构建的正确性
 *    - 反证法证明匹配过程不会漏掉任何可能的匹配
 *    - 循环不变式分析确保指针移动的合理性
 * 
 * 5. 重叠匹配处理机制:
 *    - 当找到一个完整匹配后，模式串指针不重置为0
 *    - 而是根据next数组回退到适当位置，继续查找下一个可能的匹配
 *    - 这确保了能够找到所有可能的重叠匹配
 * 
 * 6. 复杂度分析:
 *    - 时间复杂度: O(n + m)，其中n是文本串长度，m是模式串长度
 *      - 预处理next数组: O(m)
 *      - 匹配过程: O(n)
 *    - 空间复杂度: O(m)，仅需存储next数组
 * 
 * 示例:
 * 输入:
 * 3
 * BAPC
 * BAPC
 * AZA
 * AZAZAZA
 * VERDI
 * AVERDXIVYERDIAN
 * 
 * 输出:
 * 1
 * 3
 * 0
 * 
 * 关于输入输出的说明:
 * - 第一行输入是测试用例的数量
 * - 每个测试用例包含两行：模式串和文本串
 * - 输出每个测试用例中模式串在文本串中出现的次数
 */
#include <iostream>
#include <cstring>
using namespace std;

/**
 * 手动实现字符串长度计算函数
 * 
 * 在C++中，我们可以直接使用strlen函数，但这里为了教学目的手动实现，
 * 帮助理解字符串操作的基本原理。
 * 
 * 算法思路: 从字符串开头开始遍历，直到遇到字符串结束符'\0'为止，
 * 计数器累加，最终返回计数器的值即为字符串长度。
 * 
 * 时间复杂度: O(n)，其中n是字符串的长度
 * 空间复杂度: O(1)，只需要常量级别的额外空间
 * 
 * @param str 输入的C风格字符串
 * @return 字符串的长度（不包含结束符'\0'）
 */
int strLen(const char* str) {
    int len = 0;
    // 遍历字符串直到遇到结束符'\0'
    while (str[len] != '\0') {
        len++;
    }
    return len;
}

/**
 * 手动实现字符串比较函数
 * 
 * 在C++中，我们可以直接使用strcmp函数，但这里为了教学目的手动实现，
 * 帮助理解字符串比较的基本原理。
 * 
 * 算法思路: 从两个字符串的第一个字符开始逐个比较，直到：
 * 1. 找到两个不同的字符
 * 2. 其中一个字符串结束
 * 
 * 返回值规则:
 * - 如果str1 < str2，返回负数
 * - 如果str1 == str2，返回0
 * - 如果str1 > str2，返回正数
 * 
 * 时间复杂度: O(min(n,m))，其中n和m是两个字符串的长度
 * 空间复杂度: O(1)，只需要常量级别的额外空间
 * 
 * @param str1 第一个C风格字符串
 * @param str2 第二个C风格字符串
 * @return 比较结果的差值
 */
int strCmp(const char* str1, const char* str2) {
    int i = 0;
    // 逐个比较字符，直到遇到不同字符或字符串结束
    while (str1[i] != '\0' && str2[i] != '\0') {
        if (str1[i] != str2[i]) {
            // 返回第一个不同字符的ASCII码差值
            return str1[i] - str2[i];
        }
        i++;
    }
    // 一个字符串是另一个的前缀时，较短的字符串更小
    return str1[i] - str2[i];
}

/**
 * 构建next数组（部分匹配表）
 * 
 * next数组是KMP算法的核心数据结构，它存储了模式串的前缀信息，
 * 用于在匹配失败时快速确定模式串指针的新位置，避免文本串指针的回溯。
 * 
 * next数组的精确数学定义:
 * - next[i]表示模式串中以i-1位置字符结尾的子串，其真前缀和真后缀的最大匹配长度
 * - 真前缀: 不包含最后一个字符的前缀
 * - 真后缀: 不包含第一个字符的后缀
 * 
 * next数组构建过程的数学原理深度解析:
 * 
 * 1. 初始化:
 *    - next[0] = -1（特殊边界条件标记）
 *    - next[1] = 0（长度为1的子串没有真前缀和真后缀，匹配长度为0）
 * 
 * 2. 递推计算（i从2开始）:
 *    - 已知信息: next[1...i-1]已经正确计算
 *    - 目标: 计算next[i]
 *    - 使用cn指针表示当前尝试匹配的前缀末尾位置
 * 
 * 3. 核心递推关系:
 *    a) 情况1: pattern[i-1] == pattern[cn]，即当前字符匹配
 *       - 此时最长公共前后缀长度增加1: next[i] = cn + 1
 *       - 移动指针继续计算: i++, cn++
 *    b) 情况2: pattern[i-1] != pattern[cn]且cn > 0，即当前字符不匹配但cn可回退
 *       - 根据next数组递归回退cn: cn = next[cn]
 *       - 回退到一个更短的可能匹配前缀
 *    c) 情况3: pattern[i-1] != pattern[cn]且cn == 0，即当前字符不匹配且cn不可回退
 *       - 无法找到匹配的前缀，next[i] = 0
 *       - 移动指针继续计算: i++
 * 
 * 4. 数学归纳法证明:
 *    - 归纳基础: i=2时，计算正确（显然成立）
 *    - 归纳假设: 假设对于所有j < i，next[j]的值都正确
 *    - 归纳步骤: 证明next[i]的计算也正确
 *      - 当pattern[i-1] == pattern[cn]时，前后缀匹配长度显然增加1
 *      - 当pattern[i-1] != pattern[cn]时，通过cn = next[cn]回退
        *    这是因为如果存在更短的公共前后缀，那么它必定是当前前缀的前缀
        *    因此正确性得以保证
 * 
 * 5. 复杂度分析:
 *    - 时间复杂度: O(m)
 *      - 虽然存在嵌套循环，但i和cn都是单调递增的
 *      - i最多增加m次，cn最多减少m次
 *      - 因此总操作次数不超过2m
 *    - 空间复杂度: O(m)，需要存储长度为m+1的next数组
 * 
 * 6. 示例计算:
 *    对于模式串"AZA":
 *    - next[0] = -1 (初始值)
 *    - next[1] = 0 (初始值)
 *    - 计算i=2时: pattern[1] = 'Z', cn=0, pattern[0] = 'A'，不匹配且cn=0，next[2] = 0
 *    - 计算i=3时: pattern[2] = 'A', cn=0, pattern[0] = 'A'，匹配，next[3] = 1
 *    因此，next数组为 [-1, 0, 0, 1]
 *    
 *    这表示对于"AZA"模式串：
 *    - 位置0: -1（特殊值）
 *    - 位置1: 0（没有真前后缀）
 *    - 位置2: 0（"AZ"没有相同的真前后缀）
 *    - 位置3: 1（"AZA"的最长相同前后缀是"A"，长度为1）
 * 
 * @param pattern 模式串
 * @param m 模式串长度
 * @param next next数组（输出参数），长度为m+1
 */
void nextArray(const char* pattern, int m, int* next) {
    // 边界情况：如果模式串长度为1，直接返回包含特殊标记的数组
    if (m == 1) {
        next[0] = -1;
        return;
    }
    
    // 初始化：next[0]的特殊值和next[1]的默认值
    next[0] = -1;
    next[1] = 0;
    
    // i: 当前计算next值的位置（范围：2到m）
    // cn: 当前尝试匹配的前缀末尾位置，也是已匹配前缀的长度
    int i = 2, cn = 0;
    
    // 主循环：计算next数组
    while (i <= m) {
        // 情况1：当前字符匹配成功
        if (pattern[i - 1] == pattern[cn]) {
            // 匹配长度增加1，同时移动到下一个位置
            next[i++] = ++cn;
        }
        // 情况2：字符不匹配，但cn仍有回退空间
        else if (cn > 0) {
            // 关键优化：递归回退cn，寻找更短的可能匹配前缀
            // 这是KMP算法的核心思想，避免了暴力回溯
            cn = next[cn];
        }
        // 情况3：字符不匹配且cn已无法回退
        else {
            // 无法找到匹配的前缀，设置next[i]为0并移动到下一个位置
            next[i++] = 0;
        }
    }
}

/**
 * KMP算法核心实现 - 计算模式串在文本串中出现的次数（允许重叠匹配）
 * 
 * KMP算法匹配过程深度解析:
 * 
 * 1. 算法核心思想:
 *    - 使用双指针技术同时遍历文本串和模式串
 *    - 在匹配失败时，利用next数组调整模式串指针，避免文本串指针回溯
 *    - 这确保了文本串指针始终向前移动，从而保证O(n)的时间复杂度
 * 
 * 2. 匹配过程详解:
 *    - i: 文本串指针，始终向前移动
 *    - j: 模式串指针，根据匹配情况前进或回退
 *    - 主循环条件: i < n（文本串未遍历完）
 * 
 * 3. 核心匹配逻辑:
 *    a) 情况1: text[i] == pattern[j]，字符匹配成功
 *       - 两个指针同时前进: i++, j++
 *       - 继续比较下一对字符
 *    b) 情况2: text[i] != pattern[j]且j == 0，字符不匹配且模式串指针在起始位置
 *       - 模式串无法回退，文本串指针前进: i++
 *       - 从文本串下一个位置重新开始匹配
 *    c) 情况3: text[i] != pattern[j]且j > 0，字符不匹配且模式串指针不在起始位置
 *       - 根据next数组回退模式串指针: j = next[j]
 *       - 继续尝试匹配当前文本串字符
 * 
 * 4. 重叠匹配处理机制:
 *    - 当j == m（找到一个完整匹配）时，计数器加1
 *    - 关键处理: 不是重置j为0，而是j = next[j]
 *    - 这确保了能够找到所有可能的重叠匹配
 *    - 数学证明: 对于模式串p，当匹配到完整p时，最长可能的下一个匹配起始位置
 *      由p的最长相等前后缀决定，即由next[m]给出
 * 
 * 5. 算法正确性证明:
 *    - 循环不变式: 在每次循环开始时，j表示下一个要匹配的模式串位置
 *    - 终止条件: 文本串遍历完毕(i == n)
 *    - 数学归纳法证明: 对于任意时刻，KMP算法不会漏掉任何可能的匹配
 * 
 * 6. 复杂度分析:
 *    - 时间复杂度: O(n + m)
 *      - 构建next数组: O(m)
 *      - 匹配过程: O(n)，因为i最多增加n次，j的净增加量最多为n，总操作次数为O(n)
 *    - 空间复杂度: O(m)，用于存储next数组
 * 
 * 7. 重叠匹配示例:
 *    对于模式串"AZA"和文本串"AZAZAZA":
 *    - 第一次匹配: 位置0-2 ("AZA")
 *    - 找到匹配后，j = next[3] = 1
 *    - 第二次匹配: 位置2-4 ("AZA")，注意起始位置是2，说明重叠了
 *    - 找到匹配后，j = next[3] = 1
 *    - 第三次匹配: 位置4-6 ("AZA")
 *    - 总匹配次数为3，验证了重叠匹配的处理正确性
 * 
 * @param text 文本串
 * @param pattern 模式串
 * @return 模式串在文本串中出现的次数（包括重叠匹配）
 */
int kmp(const char* text, const char* pattern) {
    // 初始化匹配计数器
    int count = 0;
    
    // 获取文本串和模式串长度
    int n = strLen(text), m = strLen(pattern);
    
    // 边界情况处理：如果模式串比文本串长，不可能匹配
    if (m > n) {
        return 0;
    }
    
    // 预处理阶段：构建next数组，O(m)时间复杂度
    // 注意：在实际应用中，应该根据需求动态分配内存，而不是使用固定大小的数组
    int next[10001]; // 假设模式串最大长度为10000
    nextArray(pattern, m, next);
    
    // 初始化双指针
    int i = 0; // 文本串指针
    int j = 0; // 模式串指针
    
    // 匹配阶段：O(n)时间复杂度
    while (i < n) {
        // 情况1：字符匹配成功
        if (text[i] == pattern[j]) {
            // 两个指针同时前进
            i++;
            j++;
        }
        // 情况2：字符不匹配且模式串指针已在起始位置
        else if (j == 0) {
            // 模式串无法回退，文本串指针前进
            i++;
        }
        // 情况3：字符不匹配且模式串指针不在起始位置
        else {
            // 根据next数组回退模式串指针
            // 这是KMP算法的核心优化，避免了文本串指针的回溯
            j = next[j];
        }
        
        // 检查是否找到完整匹配
        if (j == m) {
            // 计数器加1
            count++;
            
            // 关键处理：重叠匹配的核心
            // 不是重置j为0，而是根据next数组回退
            // 这确保了能够找到所有可能的重叠匹配
            j = next[j];
        }
    }
    
    return count;
}

/**
 * 使用KMP算法计算模式串在文本串中出现的次数（包括重叠匹配）
 * 
 * 函数的主要作用是提供一个更简洁的接口，并处理边界情况。
 * 
 * 边界情况处理:
 * - 如果模式串或文本串为空，无法进行匹配，返回0
 * - 对于其他情况，调用kmp函数进行匹配计算
 * 
 * 示例:
 * >>> countOccurrences("BAPC", "BAPC")
 * 1
 * >>> countOccurrences("AZA", "AZAZAZA")
 * 3
 * >>> countOccurrences("VERDI", "AVERDXIVYERDIAN")
 * 0
 * 
 * @param pattern 模式串
 * @param text 文本串
 * @return 模式串在文本串中出现的次数
 */
int countOccurrences(const char* pattern, const char* text) {
    // 边界情况处理：如果模式串或文本串为空，无法进行匹配
    if (strLen(pattern) == 0 || strLen(text) == 0) {
        return 0;
    }
    
    // 调用KMP核心函数进行匹配计算
    return kmp(text, pattern);
}

/**
 * 简单的字符串输出函数
 * 
 * 注意：这是一个简化版本的输出函数，在实际使用时，
 * 应该使用C++标准库中的输出函数，如cout或printf。
 * 
 * @param str 要输出的C风格字符串
 */
void printStr(const char* str) {
    int i = 0;
    // 遍历字符串直到遇到结束符'\0'
    while (str[i] != '\0') {
        // 简单输出，实际编译器可能需要特定的输出方式
        // 在实际应用中，应该使用cout << str[i] 或 putchar(str[i])
        i++;
    }
}

/**
 * 简单的整数输出函数
 * 
 * 注意：这是一个简化版本的输出函数，在实际使用时，
 * 应该使用C++标准库中的输出函数，如cout或printf。
 * 
 * 算法思路：
 * 1. 处理特殊情况：数字为0
 * 2. 处理负数情况：输出负号并取绝对值
 * 3. 计算数字的位数
 * 4. 按照正确的顺序输出各位数字
 * 
 * 注意：这里只实现了数字位数的计算，实际输出部分需要根据具体编译器环境补充
 * 
 * @param num 要输出的整数
 */
void printInt(int num) {
    // 处理特殊情况：数字为0
    if (num == 0) {
        // 在实际应用中，应该使用cout << 0 或 putchar('0')
        return;
    }
    
    // 处理负数情况
    if (num < 0) {
        // 在实际应用中，应该使用cout << '-' 或 putchar('-')
        num = -num; // 取绝对值
    }
    
    // 计算数字的位数
    int temp = num;
    int digits = 0;
    while (temp > 0) {
        temp /= 10;
        digits++;
    }
    
    // 实际输出需要根据具体编译器实现
    // 在实际应用中，可以使用循环按位输出数字
}

// 主函数 - 程序入口点
int main() {
    // 测试用例1: 基本匹配 - 简单的一对一匹配情况
    const char* pattern1 = "BAPC";
    const char* text1 = "BAPC";
    int result1 = countOccurrences(pattern1, text1);
    printf("测试用例1:\n");
    printf("模式串: %s\n", pattern1);
    printf("文本串: %s\n", text1);
    printf("匹配次数: %d\n\n", result1);

    // 测试用例2: 重叠匹配 - 验证算法对重叠情况的处理能力
    // 对于"AZAZAZA"和"AZA":
    // 位置0-2: "AZA" (匹配)
    // 位置2-4: "AZA" (匹配，注意与前一个匹配重叠)
    // 位置4-6: "AZA" (匹配，注意与前一个匹配重叠)
    // 总共3次匹配
    const char* pattern2 = "AZA";
    const char* text2 = "AZAZAZA";
    int result2 = countOccurrences(pattern2, text2);
    printf("测试用例2:\n");
    printf("模式串: %s\n", pattern2);
    printf("文本串: %s\n", text2);
    printf("匹配次数: %d\n\n", result2);

    // 测试用例3: 无匹配 - 验证算法在没有匹配时的正确性
    const char* pattern3 = "VERDI";
    const char* text3 = "AVERDXIVYERDIAN";
    int result3 = countOccurrences(pattern3, text3);
    printf("测试用例3:\n");
    printf("模式串: %s\n", pattern3);
    printf("文本串: %s\n", text3);
    printf("匹配次数: %d\n\n", result3);

    // 测试用例4: 空字符串 - 验证边界情况处理
    const char* pattern4 = "";
    const char* text4 = "ABC";
    int result4 = countOccurrences(pattern4, text4);
    printf("测试用例4:\n");
    printf("模式串: %s\n", pattern4);
    printf("文本串: %s\n", text4);
    printf("匹配次数: %d\n\n", result4);

    // 测试用例5: 模式串比文本串长 - 验证边界情况处理
    const char* pattern5 = "ABCD";
    const char* text5 = "ABC";
    int result5 = countOccurrences(pattern5, text5);
    printf("测试用例5:\n");
    printf("模式串: %s\n", pattern5);
    printf("文本串: %s\n", text5);
    printf("匹配次数: %d\n\n", result5);
    
    return 0;
}
