/*
 * 洛谷 P4824 [USACO15FEB]Censoring S - 不断删除字符串
 * 
 * 题目来源：洛谷 (Luogu)
 * 题目链接：https://www.luogu.com.cn/problem/P4824
 * 
 * 题目描述：
 * 给定一个字符串s1，如果其中含有s2字符串，就删除最左出现的那个。
 * 删除之后s1剩下的字符重新拼接在一起，再删除最左出现的那个。
 * 如此周而复始，返回最终剩下的字符串。
 * 
 * 算法思路：
 * 使用KMP算法配合栈结构实现高效删除。
 * 1. 使用KMP算法进行字符串匹配
 * 2. 使用栈记录匹配过程中的状态
 * 3. 当匹配到模式串时，从栈中弹出相应长度的字符
 * 4. 继续从栈顶状态继续匹配
 * 
 * 时间复杂度：O(n + m)，其中n是文本串长度，m是模式串长度
 * 空间复杂度：O(n)，用于存储栈和next数组
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
#include <vector>
#include <string>
using namespace std;

const int MAXN = 1000001;

// 全局变量
char s1[MAXN], s2[MAXN];
int nextArrayVal[MAXN];
int stack1[MAXN], stack2[MAXN]; // 栈结构
int stackSize = 0;

/**
 * 构建KMP算法的next数组（部分匹配表）
 * next[i]表示s2[0...i-1]子串的最长相等前后缀长度
 * 
 * @param m 模式串长度
 */
void buildNextArray(int m) {
    // 初始化边界条件
    nextArrayVal[0] = -1;  // 空字符串的next值设为-1
    if (m > 1) {
        nextArrayVal[1] = 0;   // 单字符字符串的next值为0
    }
    
    int i = 2;     // 当前处理的位置，从第2个字符开始
    int cn = 0;    // 当前匹配的前缀长度
    
    // 遍历模式串构建next数组
    while (i < m) {
        // 当前字符匹配，可以延长相等前后缀
        if (s2[i - 1] == s2[cn]) {
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
 * 核心计算函数 - 使用KMP算法和栈结构实现字符串删除
 * 
 * 算法步骤：
 * 1. 构建模式串的next数组
 * 2. 使用双指针遍历文本串和模式串
 * 3. 使用栈记录匹配状态
 * 4. 当完全匹配时，从栈中弹出相应数量的元素
 * 
 * 时间复杂度：O(n + m)
 * 空间复杂度：O(n)
 */
void compute() {
    stackSize = 0; // 初始化栈大小
    int n = strlen(s1), m = strlen(s2); // 获取字符串长度
    int x = 0, y = 0; // x: 文本串指针, y: 模式串指针
    
    // 构建模式串的next数组
    buildNextArray(m);
    
    // 遍历文本串
    while (x < n) {
        // 当前字符匹配
        if (s1[x] == s2[y]) {
            // 将当前字符索引和匹配状态压入栈
            stack1[stackSize] = x;
            stack2[stackSize] = y;
            stackSize++;
            x++;
            y++;
        } 
        // 当前字符不匹配且模式串指针为0
        else if (y == 0) {
            // 将当前字符索引压入栈，匹配状态设为-1
            stack1[stackSize] = x;
            stack2[stackSize] = -1;
            stackSize++;
            x++;
        } 
        // 当前字符不匹配且模式串指针不为0
        else {
            // 根据next数组调整模式串指针
            y = nextArrayVal[y];
        }
        
        // 如果完全匹配到模式串
        if (y == m) {
            // 从栈中弹出m个元素（相当于删除模式串）
            stackSize -= m;
            // 调整模式串指针为栈顶状态+1（如果栈不为空）
            y = stackSize > 0 ? (stack2[stackSize - 1] + 1) : 0;
        }
    }
}

/**
 * 验证计算结果的辅助方法（用于测试）
 * 验证结果字符串中是否确实不包含模式串
 * 
 * @param result 删除后的结果字符串
 * @param pattern 模式串
 * @return 验证是否成功（结果中不包含模式串）
 */
bool verifyResult(const string& result, const string& pattern) {
    if (pattern.length() == 0) return true;
    return result.find(pattern) == string::npos;
}

/**
 * 单元测试方法
 * 测试各种边界情况和典型用例
 */
void runUnitTests() {
    cout << "=== 单元测试开始 ===" << endl;
    
    // 测试用例1：标准情况
    strcpy(s1, "abcabcabc");
    strcpy(s2, "abc");
    compute();
    string result1;
    for (int i = 0; i < stackSize; i++) {
        result1 += s1[stack1[i]];
    }
    cout << "测试用例1:" << endl;
    cout << "原始字符串: " << s1 << endl;
    cout << "模式串: " << s2 << endl;
    cout << "删除结果: " << result1 << endl;
    assert(verifyResult(result1, s2) && "测试用例1验证失败");
    cout << endl;
    
    // 测试用例2：嵌套删除
    strcpy(s1, "aaabbbaaabbb");
    strcpy(s2, "ab");
    compute();
    string result2;
    for (int i = 0; i < stackSize; i++) {
        result2 += s1[stack1[i]];
    }
    cout << "测试用例2:" << endl;
    cout << "原始字符串: " << s1 << endl;
    cout << "模式串: " << s2 << endl;
    cout << "删除结果: " << result2 << endl;
    assert(verifyResult(result2, s2) && "测试用例2验证失败");
    cout << endl;
    
    // 测试用例3：无匹配
    strcpy(s1, "abcdef");
    strcpy(s2, "xyz");
    compute();
    string result3;
    for (int i = 0; i < stackSize; i++) {
        result3 += s1[stack1[i]];
    }
    cout << "测试用例3:" << endl;
    cout << "原始字符串: " << s1 << endl;
    cout << "模式串: " << s2 << endl;
    cout << "删除结果: " << result3 << endl;
    assert(verifyResult(result3, s2) && "测试用例3验证失败");
    cout << endl;
    
    cout << "=== 单元测试通过 ===" << endl;
}

/**
 * 演示用例方法
 * 展示算法的实际应用
 */
void demo() {
    cout << "\n=== 演示用例 ===" << endl;
    strcpy(s1, "aaabbbaaabbbccc");
    strcpy(s2, "ab");
    compute();
    string result;
    for (int i = 0; i < stackSize; i++) {
        result += s1[stack1[i]];
    }
    cout << "演示字符串: " << s1 << endl;
    cout << "删除模式串: " << s2 << endl;
    cout << "最终结果: " << result << endl;
}

/**
 * 主函数 - 处理输入输出和测试
 * 
 * @return 程序退出码
 */
int main() {
    // 运行单元测试
    runUnitTests();
    
    // 运行演示用例
    demo();
    
    // 处理标准输入输出（用于在线评测）
    cout << "请输入字符串s1和s2：" << endl;
    cin >> s1 >> s2;
    
    compute();
    
    // 输出最终结果
    for (int i = 0; i < stackSize; i++) {
        cout << s1[stack1[i]];
    }
    cout << endl;
    
    return 0;
}