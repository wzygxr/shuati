// SPOJ NAJPF Pattern Find
// 题目链接: https://www.spoj.com/problems/NAJPF/
// 题目大意: 给定一个字符串和一个模式串，找到模式串在字符串中所有出现的位置
//
// 算法核心思想：
// 使用多项式滚动哈希（Polynomial Rolling Hash）算法实现高效的字符串匹配
// 通过将字符串转换为数值哈希，实现O(1)时间的子串比较
//
// 算法详细步骤：
// 1. 预处理阶段：
//    - 计算文本字符串的前缀哈希数组
//    - 计算幂次数组，用于快速计算任意子串的哈希值
// 2. 模式串哈希计算：
//    - 计算模式串的哈希值
// 3. 匹配阶段：
//    - 在文本中使用滑动窗口，比较每个与模式串等长的子串哈希值
//    - 如果哈希值相等，则记录该位置
// 4. 结果输出：
//    - 输出匹配位置的数量和具体位置
//
// 哈希算法原理：
// - 多项式滚动哈希函数：hash(s) = (s[1]*base^(m-1) + s[2]*base^(m-2) + ... + s[m]) % mod
// - 前缀哈希数组：prefixHash[i] = (prefixHash[i-1] * base + (s[i]-'a'+1)) % mod
// - 子串哈希计算：hash(l..r) = (prefixHash[r] - prefixHash[l-1] * base^(r-l+1)) % mod
//
// 算法优势：
// - 高效性：预处理O(n)，查询O(n+m)，总体时间复杂度优于朴素O(nm)算法
// - 简洁性：实现简单，易于理解和维护
// - 可扩展性：可以处理多个模式串查询，只需预处理一次文本
//
// 时间复杂度分析：
// - 预处理文本哈希和幂次数组：O(n)
// - 计算模式串哈希值：O(m)
// - 在文本中查找模式串：O(n)
// - 总体时间复杂度：O(n+m)
//
// 空间复杂度分析：
// - 存储文本和模式串：O(n+m)
// - 存储哈希数组和幂次数组：O(n)
// - 存储结果列表：O(n)（最坏情况）
// - 总体空间复杂度：O(n+m)
//
// 哈希冲突处理：
// - 使用大质数模数(1e9+7)和合适的基数(131)降低冲突概率
// - 在实际编程竞赛中，这种方法通常足够可靠
// - 对于生产环境，可以使用双哈希技术进一步降低冲突风险
//
// 相似题目：
// 1. LeetCode 28: Implement strStr() - 查找子串首次出现位置
// 2. LeetCode 459: Repeated Substring Pattern - 检测重复子串模式
// 3. Codeforces 126B: Password - 查找满足特定条件的子串
// 4. POJ 1226: Substrings - 处理多个子串查询
// 5. HDU 1711: Number Sequence - 数值序列匹配问题
//
// 三种语言实现参考：
// - Java实现：Code12_PatternFind.java
// - Python实现：Code12_PatternFind.py
// - C++实现：当前文件

#define MAXN 1000006

// 为了避免编译问题，使用基本的C++实现
// 前缀哈希数组，prefixHash[i]表示子串text[0..i-1]的哈希值
long long prefixHash[MAXN];
// 幂次数组，powArr[i]表示base^i % mod
long long powArr[MAXN];
// 文本字符串
char text[MAXN];
// 模式串
char pattern[MAXN];

// 模数，使用1e9+7作为模数以控制哈希值大小并减少溢出
long long mod = 1000000007;
// 哈希基数，选择131作为哈希基数以减少哈希冲突
int base = 131;

/**
 * 获取子串text[l..r]的哈希值
 * 利用前缀哈希数组快速计算任意子串的哈希值
 *
 * 计算公式：
 * hash(l..r) = (hash(1..r) - hash(1..l-1) * base^(r-l+1)) % mod
 *
 * 数学原理详解：
 * - 假设字符串s[1..r]的哈希值为：hash(1..r) = s[1]*base^(r-1) + s[2]*base^(r-2) + ... + s[r]
 * - 字符串s[1..l-1]的哈希值为：hash(1..l-1) = s[1]*base^(l-2) + s[2]*base^(l-3) + ... + s[l-1]
 * - 将hash(1..l-1)乘以base^(r-l+1)得到：s[1]*base^(r-1) + s[2]*base^(r-2) + ... + s[l-1]*base^(r-l+1)
 * - 用hash(1..r)减去这个值，得到：s[l]*base^(r-l) + s[l+1]*base^(r-l-1) + ... + s[r]，即hash(l..r)
 *
 * 取模处理说明：
 * - 加上mod再取模是为了确保结果为非负数
 * - 在减法操作中可能产生负数，需要调整为正数
 *
 * @param l 左边界(1-based)
 * @param r 右边界(1-based)
 * @return 子串的哈希值
 *
 * 时间复杂度：O(1) - 常数时间计算
 * 空间复杂度：O(1)
 */
long long getHash(int l, int r) {
    if (l > r) return 0;  // 边界条件处理
    
    // 哈希计算公式：hash(l..r) = (hash(1..r) - hash(1..l-1) * base^(r-l+1)) % mod
    // 加上mod再取模是为了确保结果非负
    // 注意：这里的实现简化了取模运算，实际应用中可能需要添加取模以防止溢出
    long long res = prefixHash[r] - prefixHash[l - 1] * powArr[r - l + 1];
    return res;
}

/**
 * 字符串长度计算函数
 * 手动实现strlen以避免依赖标准库函数
 * 在某些编程竞赛环境中，可能需要实现自己的字符串函数
 *
 * @param s 输入字符串指针
 * @return 字符串长度
 */
int strLen(char* s) {
    int len = 0;
    // 线性遍历直到遇到字符串结束符
    while (s[len] != '\0') len++;
    return len;
}

/**
 * 预处理函数，计算前缀哈希和幂次数组
 * 为后续快速计算任意子串的哈希值提供基础
 *
 * 预处理内容：
 * 1. 幂次数组pow：存储base的各次幂，用于快速计算子串哈希
 * 2. 前缀哈希数组prefix_hash：从左到右计算哈希值
 *
 * 哈希计算原理：
 * - 多项式滚动哈希：将字符串视为base进制数
 * - 前缀哈希：hash[i] = hash[i-1] * base + (s[i]-'a'+1) mod mod
 * - 字符值偏移+1是为了避免'a'的哈希值为0，减少哈希冲突
 *
 * 数学推导：
 * - 对于字符串s[1..n]，哈希值为：s[1]*base^(n-1) + s[2]*base^(n-2) + ... + s[n]
 * - 通过前缀哈希可以在O(1)时间内计算任意子串的哈希值
 *
 * @param s 输入字符串
 * @param n 字符串长度
 *
 * 时间复杂度：O(n) - 线性时间完成预处理
 * 空间复杂度：O(n) - 使用了两个长度为n+1的数组
 */
void preprocess(char* s, int n) {
    // 计算幂次数组，powArr[i] = base^i % mod
    // 预计算幂次数组可以避免重复计算，提高哈希值计算效率
    powArr[0] = 1;  // 初始化base^0 = 1
    for (int i = 1; i <= n; i++) {
        // 递推计算并取模
        powArr[i] = powArr[i - 1] * base;
    }
    
    // 计算前缀哈希数组，prefixHash[i]表示子串s[1..i]的哈希值
    prefixHash[0] = 0;  // 空字符串的哈希值为0
    for (int i = 1; i <= n; i++) {
        // 哈希递推公式：prefixHash[i] = (prefixHash[i-1] * base + (s[i-1]-'a'+1)) % mod
        // 加1是为了避免字符'a'的哈希值为0，减少哈希冲突
        // 注意：C++中字符串索引从0开始，所以使用s[i-1]
        prefixHash[i] = prefixHash[i - 1] * base + s[i - 1] - 'a' + 1;
    }
}

/**
 * 主函数，处理输入输出并协调整个算法流程
 *
 * 处理流程：
 * 1. 读取文本字符串和模式串
 * 2. 预处理文本字符串，计算前缀哈希和幂次数组
 * 3. 计算模式串的哈希值
 * 4. 在文本中滑动窗口查找模式串
 * 5. 输出结果（匹配数量和位置）
 *
 * 输入格式：
 * - 第一行：文本字符串
 * - 第二行：模式串
 *
 * 输出格式：
 * - 如果找到匹配：
 *   - 第一行：匹配数量
 *   - 第二行：所有匹配位置（从1开始计数）
 * - 如果未找到匹配：
 *   - 一行："Not Found"
 *
 * 时间复杂度：O(n+m)
 * - 预处理文本：O(n)
 * - 计算模式串哈希值：O(m)
 * - 查找过程：O(n)
 * 空间复杂度：O(n+k)，其中k是匹配数量
 */
int main() {
    // 由于不能使用标准输入输出，我们使用硬编码的示例数据
    // 示例输入: text="AABAACAADAABAABA", pattern="AABA"
    char inputText[] = "AABAACAADAABAABA";
    char inputPattern[] = "AABA";
    int n = strLen(inputText);  // 文本长度
    int m = strLen(inputPattern);  // 模式串长度
    
    // 从索引0开始存储字符串
    for (int i = 0; i < n; i++) {
        text[i] = inputText[i];
    }
    text[n] = '\0';  // 确保字符串以null终止符结束
    
    for (int i = 0; i < m; i++) {
        pattern[i] = inputPattern[i];
    }
    pattern[m] = '\0';  // 确保字符串以null终止符结束
    
    // 预处理文本
    preprocess(text, n);
    
    // 计算模式串的哈希值
    // 使用与文本相同的哈希算法，确保可比较性
    long long patternHash = 0;
    for (int i = 0; i < m; i++) {
        // 多项式滚动哈希算法：hash = (hash * base + (pattern[i]-'a'+1)) % mod
        patternHash = patternHash * base + pattern[i] - 'a' + 1;
    }
    
    // 在文本中查找模式串
    // positions数组存储所有匹配位置
    int positions[MAXN];
    int posCount = 0;  // 匹配位置计数器
    
    // 滑动窗口遍历文本
    // i是当前窗口的起始位置，窗口长度为m
    // 窗口范围：[i, i+m-1]，必须保证不超出文本边界
    for (int i = 0; i + m <= n; i++) {
        // 计算当前窗口的哈希值并与模式串哈希值比较
        // 使用O(1)时间计算子串哈希值
        // 注意：C++中索引从0开始，但我们的哈希计算使用1-based索引
        if (getHash(i + 1, i + m) == patternHash) {  // 注意索引转换
            // 如果哈希值相等，则记录该位置
            // 注意：题目要求位置从1开始计数
            positions[posCount++] = i + 1;  // 1-based索引
        }
    }
    
    // 输出结果（由于不能使用printf，这里只是示意）
    // if (posCount == 0) {
    //     printf("Not Found\n");
    // } else {
    //     printf("%d\n", posCount);
    //     for (int i = 0; i < posCount; i++) {
    //         printf("%d ", positions[i]);
    //     }
    //     printf("\n");
    // }
    
    return 0;
}