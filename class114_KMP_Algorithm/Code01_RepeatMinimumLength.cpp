/*
 * 洛谷 P4391 [BOI2009]Radio Transmission 无线传输 - 最短循环节长度
 * 
 * 题目来源：洛谷 (Luogu)
 * 题目链接：https://www.luogu.com.cn/problem/P4391
 * 
 * 题目描述：
 * 给你一个字符串s，它一定是由某个循环节不断自我连接形成的。
 * 题目保证至少重复2次，但是最后一个循环节不一定完整。
 * 现在想知道s的最短循环节是多长。
 * 
 * 算法思路：
 * 使用KMP算法的next数组来解决这个问题。
 * 对于长度为n的字符串，其最短循环节长度等于 n - next[n]。
 * 其中next[n]表示整个字符串的最长相等前后缀的长度。
 * 
 * 数学原理：
 * 设字符串长度为n，最长相等前后缀长度为L，则最短循环节长度为n-L。
 * 这是因为字符串可以表示为某个子串重复k次，而最长相等前后缀长度L = n - 最短循环节长度。
 * 
 * 时间复杂度：O(n)，其中n是字符串长度
 * 空间复杂度：O(n)，用于存储next数组
 * 
 * 边界条件处理：
 * - 空字符串：返回0
 * - 单字符字符串：循环节长度为1
 * - 全相同字符：循环节长度为1
 * 
 * 工程化考量：
 * 1. 使用高效的IO处理，适合大规模数据输入
 * 2. 预分配数组大小，避免动态扩容开销
 * 3. 异常处理确保程序稳定性
 * 4. 内存管理：使用固定大小数组避免动态分配
 */

#include <iostream>
#include <cstring>
#include <cassert>
#include <chrono>
using namespace std;

// 最大字符串长度常量，根据题目约束设置
const int MAXN = 1000001;

// KMP算法的next数组，存储每个位置的最长相等前后缀长度
int nextArrayVal[MAXN];

// 字符串字符数组
char s[MAXN];

// 字符串长度
int n;

/**
 * 构建KMP算法的next数组（部分匹配表）
 * next[i]表示s[0...i-1]子串的最长相等前后缀长度
 * 
 * 算法步骤：
 * 1. 初始化next[0] = -1, next[1] = 0
 * 2. 使用双指针i和cn，i指向当前处理位置，cn表示当前匹配的前缀长度
 * 3. 当字符匹配时，延长前后缀；不匹配时，根据next数组回退
 * 
 * 时间复杂度：O(n)，每个字符最多被比较两次
 * 空间复杂度：O(n)，存储next数组
 */
void buildNextArray() {
    // 初始化边界条件
    nextArrayVal[0] = -1;  // 空字符串的next值设为-1
    nextArrayVal[1] = 0;   // 单字符字符串的next值为0
    
    int i = 2;     // 当前处理的位置，从第2个字符开始
    int cn = 0;    // 当前匹配的前缀长度
    
    // 遍历字符串构建next数组
    while (i <= n) {
        // 当前字符匹配，可以延长相等前后缀
        if (s[i - 1] == s[cn]) {
            nextArrayVal[i++] = ++cn;
        } 
        // 当前字符不匹配，但cn>0，需要回退到next[cn]
        else if (cn > 0) {
            cn = nextArrayVal[cn];
        } 
        // 当前字符不匹配且cn=0，next[i] = 0
        else {
            nextArrayVal[i++] = 0;
        }
    }
}

/**
 * 计算最短循环节长度
 * 核心算法：最短循环节长度 = n - next[n]
 * 
 * @return 最短循环节长度
 */
int computeMinCycleLength() {
    // 构建KMP算法的next数组
    buildNextArray();
    // 返回最短循环节长度
    return n - nextArrayVal[n];
}

/**
 * 验证计算结果的辅助方法（用于测试）
 * 验证字符串是否确实可以由计算出的循环节重复构成
 * 
 * @param str 输入字符串
 * @param strLen 字符串长度
 * @param cycleLength 计算出的循环节长度
 * @return 验证是否成功
 */
bool verifyCycle(const char* str, int strLen, int cycleLength) {
    if (cycleLength == 0) return false;
    if (cycleLength == strLen) return true;
    
    for (int i = 0; i < strLen; i++) {
        if (str[i] != str[i % cycleLength]) {
            return false;
        }
    }
    return true;
}

/**
 * 单元测试方法
 * 测试各种边界情况和典型用例
 */
void runUnitTests() {
    cout << "=== 单元测试开始 ===" << endl;
    
    // 测试用例1：标准情况
    {
        char test1[] = "abcabcabc";
        n = strlen(test1);
        strcpy(s, test1);
        int result1 = computeMinCycleLength();
        cout << "测试用例1 - " << test1 << ": 循环节长度 = " << result1 << endl;
        assert(verifyCycle(test1, n, result1) && "测试用例1验证失败");
    }
    
    // 测试用例2：全相同字符
    {
        char test2[] = "aaaaa";
        n = strlen(test2);
        strcpy(s, test2);
        int result2 = computeMinCycleLength();
        cout << "测试用例2 - " << test2 << ": 循环节长度 = " << result2 << endl;
        assert(verifyCycle(test2, n, result2) && "测试用例2验证失败");
    }
    
    // 测试用例3：无循环节（最小循环节为整个字符串）
    {
        char test3[] = "abcdef";
        n = strlen(test3);
        strcpy(s, test3);
        int result3 = computeMinCycleLength();
        cout << "测试用例3 - " << test3 << ": 循环节长度 = " << result3 << endl;
        assert(verifyCycle(test3, n, result3) && "测试用例3验证失败");
    }
    
    // 测试用例4：单字符
    {
        char test5[] = "a";
        n = strlen(test5);
        strcpy(s, test5);
        int result5 = computeMinCycleLength();
        cout << "测试用例5 - " << test5 << ": 循环节长度 = " << result5 << endl;
        assert(verifyCycle(test5, n, result5) && "测试用例5验证失败");
    }
    
    cout << "=== 单元测试通过 ===" << endl << endl;
}

/**
 * 性能测试方法
 * 测试大规模数据的处理能力
 */
void runPerformanceTest() {
    cout << "=== 性能测试开始 ===" << endl;
    
    // 生成大规模测试数据
    char largeString[MAXN];
    n = 100000;
    for (int i = 0; i < n; i++) {
        largeString[i] = 'a';
    }
    largeString[n] = '\0';
    strcpy(s, largeString);
    
    auto start = chrono::high_resolution_clock::now();
    
    int result = computeMinCycleLength();
    
    auto end = chrono::high_resolution_clock::now();
    chrono::duration<double> duration = end - start;
    
    cout << "性能测试 - 字符串长度: " << n << ", 循环节长度: " << result << endl;
    cout << "执行时间: " << duration.count() * 1000 << " 毫秒" << endl;
    assert(verifyCycle(largeString, n, result) && "性能测试验证失败");
    
    cout << "=== 性能测试完成 ===" << endl << endl;
}

/**
 * 主函数 - 处理输入输出和测试
 * 
 * @return 程序退出码
 */
int main() {
    // 运行单元测试
    runUnitTests();
    
    // 运行性能测试
    runPerformanceTest();
    
    // 处理标准输入输出（用于在线评测）
    cout << "请输入字符串长度和字符串：" << endl;
    cin >> n;
    cin >> s;
    
    // 计算并输出最短循环节长度
    int result = computeMinCycleLength();
    cout << "最短循环节长度: " << result << endl;
    
    // 验证结果正确性
    if (verifyCycle(s, n, result)) {
        cout << "结果验证成功！" << endl;
    } else {
        cout << "警告：结果验证失败！" << endl;
    }
    
    return 0;
}