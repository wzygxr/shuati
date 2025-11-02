// Codeforces 271D - Good Substrings 问题C++实现
//
// 题目链接：https://codeforces.com/contest/271/problem/D
//
// 题目描述：
// 给定一个字符串s，由小写英文字母组成。有些英文字母是好的，其余的是坏的。
// 字符串s[l...r]是好的，当且仅当其中最多有k个坏字母。
// 任务是找出字符串s中不同好子串的数量（内容不同的子串视为不同）。
//
// 算法核心思想：
// 1. 滑动窗口枚举：从每个起始位置开始，向右扩展并统计坏字母数量
// 2. 哈希去重：使用多项式滚动哈希和集合（set）高效存储不同子串
// 3. 早期剪枝：当坏字母数量超过k时立即停止扩展
// 4. 预计算优化：预先计算哈希值和幂次数组，支持O(1)时间子串哈希值查询
//
// 多项式滚动哈希数学原理：
// - 哈希函数定义：H(s) = (s[0]*base^(n-1) + s[1]*base^(n-2) + ... + s[n-1]*base^0) % mod_value
// - 在本题中，为了简化实现，我们没有使用模数（mod_value），这在字符串较短时是安全的
// - 前缀哈希：hash_arr[i] = hash_arr[i-1] * base + s[i]的映射值
// - 子串哈希：substring_hash(l,r) = hash_arr[r] - hash_arr[l-1] * pow_arr[r-l+1]（当l>0时）
//
// 算法详细步骤：
// 1. 预处理阶段：
//    - 构建坏字母标记数组：根据输入的好字母标记字符串，标记哪些字母是坏字母
//    - 预计算幂次数组：pow_arr[i] = base^i，用于O(1)时间计算子串哈希值
//    - 计算前缀哈希数组：hash_arr[i]表示s[0...i]的哈希值
// 2. 枚举阶段（核心）：
//    - 遍历每个可能的起始位置i (0 ≤ i < n)
//    - 对于每个i，从j=i开始向右扩展子串，同时维护坏字母计数bad_count
//    - 对于每个j，检查s[j]是否为坏字母，若是则bad_count++
//    - 剪枝优化：当bad_count > k时，立即终止该起始位置的扩展
//    - 对每个有效子串s[i...j]，计算其哈希值并加入集合
// 3. 结果输出：集合的大小即为不同好子串的数量
//
// 算法正确性证明：
// - 对于任意起始位置i，随着j的增加，坏字母计数bad_count是非递减的
// - 因此，一旦bad_count超过k，对于所有j' > j，s[i...j']也必定包含超过k个坏字母
// - 这证明了我们的剪枝策略的正确性
// - 哈希去重机制确保我们只统计不同的子串，集合自动处理重复情况
//
// 时间复杂度分析：
// - 预处理阶段：O(n + 26)，其中n是字符串长度，26是字母表大小
// - 枚举阶段：
//   - 最坏情况下为O(n²)（当k较大时）
//   - 平均情况下由于剪枝优化，实际时间远小于O(n²)
//   - 每个子串哈希值计算为O(1)
//   - 集合的插入操作为平均O(1)时间
// - 总体时间复杂度：O(n²)
//
// 空间复杂度分析：
// - 前缀哈希数组：O(n)
// - 幂次数组：O(n)
// - 坏字母标记数组：O(1)，固定大小26
// - 集合存储：O(m)，其中m是不同好子串的数量，最坏情况为O(n²)
// - 总体空间复杂度：O(n + m) = O(n²)
//
// 作者：Algorithm Journey
// 测试链接: https://codeforces.com/contest/271/problem/D
//
// 三种语言实现参考：
// - Java实现：Code07_GoodSubstrings.java
// - Python实现：Code07_GoodSubstrings.py
// - C++实现：当前文件

#include <iostream>
#include <string>
#include <vector>
#include <unordered_set>
using namespace std;

// 哈希基数，选择499作为大质数以减少哈希冲突
// 使用质数作为基数可以使哈希分布更均匀
const int base = 499;

// 简单的字符串长度计算函数
// 手动实现strlen以避免依赖标准库函数
// 在某些编程竞赛环境中，可能需要实现自己的字符串函数
int strLen(const char* s) {
    int len = 0;
    // 线性遍历直到遇到字符串结束符
    while (s[len] != '\0') len++;
    return len;
}

int main() {
    /*
     * 主函数，处理输入输出并执行算法的核心逻辑
     *
     * 处理流程详解：
     * 1. 输入处理阶段：
     *    - 读取字符串s
     *    - 读取好字母标记字符串（26个字符，'1'表示好字母，'0'表示坏字母）
     *    - 读取整数k，表示允许的最大坏字母数量
     *
     * 2. 预处理阶段：
     *    - 构建坏字母标记数组：将好字母标记转换为布尔数组
     *    - 预计算幂次数组：pow_arr[i] = base^i，支持O(1)时间子串哈希计算
     *    - 构建前缀哈希数组：使用多项式哈希算法计算前缀哈希值
     *
     * 3. 子串枚举与去重阶段：
     *    - 使用双重循环枚举所有可能的好子串
     *    - 外层循环遍历起始位置i
     *    - 内层循环从i开始向右扩展，同时维护坏字母计数
     *    - 使用C++的unordered_set数据结构自动去重，确保每个不同子串只被统计一次
     *
     * 4. 结果输出：
     *    - 输出unordered_set的大小，即为不同好子串的数量
     */
    
    // 读取输入数据
    string s, mark;
    int k;
    cin >> s >> mark >> k;
    
    int n = s.length();  // 字符串长度
    
    // 构建坏字母标记数组
    // bad[i]为true表示字母'a'+i是坏字母
    vector<bool> bad(26, false);
    for (int i = 0; i < 26; i++) {
        // '0'表示坏字母，'1'表示好字母
        bad[i] = (mark[i] == '0');
    }
    
    // 预处理阶段1：预计算base的幂次数组
    // pow_arr是多项式哈希算法的关键组成部分，用于O(1)时间计算子串哈希值
    vector<long long> pow_arr(n);
    pow_arr[0] = 1;  // 基础情况：base^0 = 1
    for (int i = 1; i < n; i++) {
        // 递推计算：pow_arr[i] = pow_arr[i-1] * base
        pow_arr[i] = pow_arr[i - 1] * base;
        // 注意：C++中使用long long类型避免整数溢出
    }
    
    // 预处理阶段2：构建前缀哈希数组
    // 实现多项式滚动哈希算法，为每个前缀计算哈希值
    vector<long long> hash_arr(n);
    // 第一个字符的哈希值，加1避免字符'a'被映射为0
    // 这样可以避免不同前缀产生相同的哈希值
    hash_arr[0] = s[0] - 'a' + 1;
    for (int i = 1; i < n; i++) {
        // 哈希值递推公式：hash_arr[i] = hash_arr[i-1] * base + s[i]的映射值
        // 这构建了一个多项式表示：hash_arr[i] = s[0]*base^i + s[1]*base^(i-1) + ... + s[i]
        hash_arr[i] = hash_arr[i - 1] * base + (s[i] - 'a' + 1);
    }
    
    // 计算子串s[l...r]的哈希值
    // 这是一个内部函数，利用预处理好的前缀哈希数组和幂次数组在O(1)时间内计算任意子串的哈希值
    auto substring_hash = [&](int l, int r) -> long long {
        /*
         * 在O(1)时间内计算子串s[l...r]的哈希值
         *
         * 数学原理解析：
         * - 前缀哈希定义：hash_arr[r] = s[0]*base^r + s[1]*base^(r-1) + ... + s[r]*base^0
         * - 子串哈希计算原理：要得到s[l...r]的哈希值，需要从hash_arr[r]中减去s[0...l-1]部分的影响
         * - 当l=0时，子串就是前缀本身，直接返回hash_arr[r]
         * - 当l>0时，需要将hash_arr[l-1]乘以base^(r-l+1)，然后从hash_arr[r]中减去
         *
         * 数学推导：
         * hash_arr[r] = s[0]*base^r + s[1]*base^(r-1) + ... + s[l-1]*base^(r-l+1) + s[l]*base^(r-l) + ... + s[r]
         * hash_arr[l-1]*base^(r-l+1) = (s[0]*base^(l-1) + ... + s[l-1]) * base^(r-l+1)
         *                                = s[0]*base^r + ... + s[l-1]*base^(r-l+1)
         * 因此：hash_arr[r] - hash_arr[l-1]*base^(r-l+1) = s[l]*base^(r-l) + ... + s[r]
         * 这正是子串s[l...r]的哈希值
         *
         * 参数：
         *     l: 子串起始位置（包含，从0开始）
         *     r: 子串结束位置（包含，从0开始）
         *
         * 返回：
         *     子串s[l...r]的哈希值
         */
        if (l == 0) {
            // 起始位置为0，直接返回前缀哈希值
            return hash_arr[r];
        } else {
            // 计算子串哈希值 = 前缀哈希(r) - 前缀哈希(l-1) * base^(r-l+1)
            return hash_arr[r] - hash_arr[l - 1] * pow_arr[r - l + 1];
        }
    };
    
    // 使用C++的unordered_set数据结构存储不同好子串的哈希值，实现自动去重
    // 这是算法去重的核心，unordered_set的特性确保相同的哈希值只会存储一次
    unordered_set<long long> good_substrings;
    
    // 枚举所有可能的子串起始位置i
    // 这是算法的核心部分，实现了子串枚举和剪枝优化
    for (int i = 0; i < n; i++) {
        // 从位置i开始，向右扩展子串，同时统计坏字母数量
        int bad_count = 0;
        for (int j = i; j < n; j++) {
            // 检查当前字符s[j]是否是坏字母
            // bad数组的索引为字符减去'a'的ASCII值
            if (bad[s[j] - 'a']) {
                bad_count++;  // 坏字母计数加1
            }
            
            // 剪枝优化：如果坏字母数量超过k，停止向右扩展
            // 这是一个关键的优化，基于以下观察：
            // - 当j增加时，子串s[i...j]只在右侧增加了一个字符
            // - 因此，坏字母计数bad_count在j增加时只能保持不变或增加
            // - 一旦bad_count超过k，对于所有j' > j，s[i...j']也必然包含超过k个坏字母
            if (bad_count > k) {
                break;  // 提前终止内层循环，避免不必要的计算
            }
            
            // 计算子串s[i...j]的哈希值并加入set
            // substring_hash函数在O(1)时间内计算子串的哈希值
            // 如果是重复的子串，unordered_set会自动去重（基于哈希值）
            good_substrings.insert(substring_hash(i, j));
        }
    }
    
    // 输出不同好子串的数量，即unordered_set的大小
    // 由于unordered_set自动去重，good_substrings.size()正好是不同子串的数量
    cout << good_substrings.size() << endl;
    
    return 0;
}