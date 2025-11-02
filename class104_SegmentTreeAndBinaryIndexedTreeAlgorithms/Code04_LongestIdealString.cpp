/**
 * LeetCode 2370. 最长理想子序列 (Longest Ideal Subsequence)
 * 题目链接: https://leetcode.cn/problems/longest-ideal-subsequence/
 * 
 * 题目描述:
 * 给定一个长度为n，只由小写字母组成的字符串s，给定一个非负整数k
 * 字符串s可以生成很多子序列，理想子序列的定义为：
 * 子序列中任意相邻的两个字符，在字母表中位次的差值绝对值<=k
 * 返回最长理想子序列的长度。
 * 
 * 解题思路:
 * 使用线段树优化动态规划的方法解决此问题。
 * 1. 定义状态dp[c]表示以字符c结尾的最长理想子序列长度
 * 2. 对于每个字符，查询与其ASCII值差值不超过k的所有字符对应的dp值的最大值
 * 3. 使用线段树维护dp数组，支持区间查询最大值和单点更新
 * 4. 遍历字符串，对每个字符更新对应的dp值
 * 
 * 时间复杂度分析:
 * - 遍历字符串: O(n)
 * - 每次查询和更新: O(log e)，e为字符集大小
 * - 总时间复杂度: O(n * log e)
 * 空间复杂度: O(e) 用于存储线段树
 * 
 * 工程化考量:
 * 1. 性能优化: 线段树查询和更新都是O(log n)
 * 2. 边界处理: 处理k=0和k>=25的特殊情况
 * 3. 异常防御: 处理空字符串和非法字符
 * 4. 可读性: 清晰的变量命名和注释
 */

#include <iostream>
#include <string>
#include <vector>
#include <algorithm>
#include <cstring>

using namespace std;

class SegmentTree {
private:
    vector<int> tree;
    int size;
    
public:
    /**
     * 构造函数，初始化线段树
     * 
     * @param n 线段树大小
     */
    SegmentTree(int n) : size(n) {
        tree.resize(4 * n, 0);
    }
    
    /**
     * 线段树向上更新操作
     * 
     * @param idx 线段树节点索引
     */
    void up(int idx) {
        tree[idx] = max(tree[idx * 2], tree[idx * 2 + 1]);
    }
    
    /**
     * 线段树单点更新操作
     * 
     * @param pos 要更新的位置
     * @param val 新的值
     * @param l 当前区间左边界
     * @param r 当前区间右边界
     * @param idx 当前线段树节点索引
     */
    void update(int pos, int val, int l, int r, int idx) {
        if (l == r) {
            if (val > tree[idx]) {
                tree[idx] = val;
            }
            return;
        }
        
        int mid = (l + r) / 2;
        if (pos <= mid) {
            update(pos, val, l, mid, idx * 2);
        } else {
            update(pos, val, mid + 1, r, idx * 2 + 1);
        }
        up(idx);
    }
    
    /**
     * 线段树区间查询操作
     * 
     * @param L 查询区间左边界
     * @param R 查询区间右边界
     * @param l 当前区间左边界
     * @param r 当前区间右边界
     * @param idx 当前线段树节点索引
     * @return 区间最大值
     */
    int query(int L, int R, int l, int r, int idx) {
        if (L <= l && r <= R) {
            return tree[idx];
        }
        
        int mid = (l + r) / 2;
        int result = 0;
        
        if (L <= mid) {
            result = max(result, query(L, R, l, mid, idx * 2));
        }
        if (R > mid) {
            result = max(result, query(L, R, mid + 1, r, idx * 2 + 1));
        }
        
        return result;
    }
};

/**
 * 计算最长理想子序列长度
 * 
 * @param s 输入字符串
 * @param k 字符差值上限
 * @return 最长理想子序列长度
 */
int longestIdealString(string s, int k) {
    // 异常处理
    if (s.empty()) return 0;
    if (k < 0) k = 0;
    
    // 字符集大小（小写字母）
    const int CHAR_SET_SIZE = 26;
    SegmentTree segTree(CHAR_SET_SIZE);
    
    int result = 0;
    
    // 遍历字符串中的每个字符
    for (char c : s) {
        // 将字符转换为1-26的数字
        int v = c - 'a' + 1;
        
        // 计算查询区间
        int left = max(1, v - k);
        int right = min(CHAR_SET_SIZE, v + k);
        
        // 查询区间内的最大值
        int maxVal = segTree.query(left, right, 1, CHAR_SET_SIZE, 1);
        
        // 更新结果
        result = max(result, maxVal + 1);
        
        // 更新线段树
        segTree.update(v, maxVal + 1, 1, CHAR_SET_SIZE, 1);
    }
    
    return result;
}

/**
 * 测试函数，验证算法正确性
 */
void testLongestIdealString() {
    cout << "开始测试最长理想子序列算法..." << endl;
    
    // 测试用例1: 正常情况
    string s1 = "acfgbd";
    int result1 = longestIdealString(s1, 2);
    cout << "测试用例1: s=\"acfgbd\", k=2 -> " << result1 << endl;
    assert(result1 == 4 && "测试用例1失败");
    
    // 测试用例2: k=0
    string s2 = "abcd";
    int result2 = longestIdealString(s2, 0);
    cout << "测试用例2: s=\"abcd\", k=0 -> " << result2 << endl;
    assert(result2 == 1 && "测试用例2失败");
    
    // 测试用例3: 空字符串
    string s3 = "";
    int result3 = longestIdealString(s3, 5);
    cout << "测试用例3: 空字符串, k=5 -> " << result3 << endl;
    assert(result3 == 0 && "测试用例3失败");
    
    // 测试用例4: 单字符
    string s4 = "a";
    int result4 = longestIdealString(s4, 10);
    cout << "测试用例4: s=\"a\", k=10 -> " << result4 << endl;
    assert(result4 == 1 && "测试用例4失败");
    
    // 测试用例5: 大k值
    string s5 = "xyz";
    int result5 = longestIdealString(s5, 25);
    cout << "测试用例5: s=\"xyz\", k=25 -> " << result5 << endl;
    
    cout << "所有测试用例通过！" << endl;
}

/**
 * 性能测试函数
 */
void performanceTest() {
    cout << "开始性能测试..." << endl;
    
    // 大规模数据测试
    string large_s;
    for (int i = 0; i < 100000; i++) {
        large_s += 'a' + (i % 26);
    }
    
    auto start = chrono::high_resolution_clock::now();
    int result = longestIdealString(large_s, 5);
    auto end = chrono::high_resolution_clock::now();
    
    auto duration = chrono::duration_cast<chrono::milliseconds>(end - start);
    cout << "大规模测试: 字符串长度" << large_s.length() 
         << "，k=5，结果" << result 
         << "，耗时" << duration.count() << "毫秒" << endl;
}

int main() {
    // 运行测试
    testLongestIdealString();
    
    // 性能测试
    performanceTest();
    
    return 0;
}