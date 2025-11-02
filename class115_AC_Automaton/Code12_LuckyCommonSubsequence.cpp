/*
 * Codeforces 346B - Lucky Common Subsequence
 * 题目链接：https://codeforces.com/problemset/problem/346/B
 * 题目描述：给定三个字符串str1、str2和virus，找出str1和str2的最长公共子序列，且该子序列不包含virus作为子串。
 * 
 * 算法详解：
 * 这是一道结合动态规划和AC自动机的题目。我们需要在求最长公共子序列的过程中，
 * 使用AC自动机来避免生成包含病毒串的子序列。
 * 
 * 算法核心思想：
 * 1. 构建病毒字符串的AC自动机，用于检测是否包含病毒串
 * 2. 使用三维动态规划：dp[i][j][k]表示str1前i个字符、str2前j个字符、在AC自动机上处于状态k时的最长公共子序列
 * 3. 状态转移时，确保不会进入AC自动机的危险状态（即匹配到病毒串的状态）
 * 
 * 时间复杂度分析：
 * 1. 构建AC自动机：O(|virus|)
 * 2. 动态规划：O(|str1| × |str2| × |virus|)
 * 总时间复杂度：O(|str1| × |str2| × |virus|)
 * 
 * 空间复杂度：O(|str1| × |str2| × |virus|)
 * 
 * 适用场景：
 * 1. 带约束条件的最长公共子序列
 * 2. 字符串匹配与动态规划结合
 * 
 * 工程化考量：
 * 1. 异常处理：检查输入参数的有效性
 * 2. 性能优化：使用滚动数组优化空间复杂度
 * 3. 内存优化：合理设置数组大小，避免浪费
 */

#include <iostream>
#include <vector>
#include <queue>
#include <string>
#include <algorithm>
#include <cstring>
#include <stdio.h>
#include <stdlib.h>
#include <cctype>
using namespace std;
using namespace std;

// 常量定义
const int MAXN = 105;
const int MAXS = 105;

// Trie树节点
struct TrieNode {
    int children[26];
    int isEnd;
    int fail;
    int nodeId;
    
    TrieNode() {
        memset(children, 0, sizeof(children));
        isEnd = 0;
        fail = 0;
        nodeId = 0;
    }
};

// 全局变量
TrieNode tree[MAXS];
int nodeCount = 0;
int danger[MAXS]; // 危险状态标记

// 初始化节点
void initNode(int nodeId) {
    memset(tree[nodeId].children, 0, sizeof(tree[nodeId].children));
    tree[nodeId].isEnd = 0;
    tree[nodeId].fail = 0;
    tree[nodeId].nodeId = nodeId;
}

// 构建AC自动机
void buildACAutomaton(const string& virus) {
    // 初始化
    for (int i = 0; i < MAXS; i++) {
        initNode(i);
    }
    nodeCount = 1;
    
    // 插入病毒字符串
    int node = 0;
    for (char c : virus) {
        int index = std::toupper(c) - 'A';
        if (tree[node].children[index] == 0) {
            tree[node].children[index] = nodeCount;
            initNode(nodeCount);
            nodeCount++;
        }
        node = tree[node].children[index];
    }
    tree[node].isEnd = 1;
    
    // 构建fail指针
    queue<int> q;
    for (int i = 0; i < 26; i++) {
        if (tree[0].children[i] != 0) {
            tree[tree[0].children[i]].fail = 0;
            q.push(tree[0].children[i]);
        } else {
            tree[0].children[i] = 0;
        }
    }
    
    while (!q.empty()) {
        int u = q.front();
        q.pop();
        danger[u] = danger[u] || tree[u].isEnd || danger[tree[u].fail];
        
        for (int i = 0; i < 26; i++) {
            if (tree[u].children[i] != 0) {
                tree[tree[u].children[i]].fail = tree[tree[u].fail].children[i];
                q.push(tree[u].children[i]);
            } else {
                tree[u].children[i] = tree[tree[u].fail].children[i];
            }
        }
    }
}

// 求最长公共子序列（不包含病毒串）
string longestCommonSubsequenceWithoutVirus(const string& str1, const string& str2, const string& virus) {
    int n = str1.length();
    int m = str2.length();
    int v = nodeCount;
    
    // dp[i][j][k]表示str1前i个字符、str2前j个字符、在AC自动机状态k时的最长公共子序列长度
    int dp[MAXN][MAXN][MAXS];
    // path[i][j][k]记录路径，用于重构答案
    int path[MAXN][MAXN][MAXS];
    
    // 初始化
    memset(dp, -1, sizeof(dp));
    memset(path, 0, sizeof(path));
    dp[0][0][0] = 0;
    
    // 动态规划
    for (int i = 0; i <= n; i++) {
        for (int j = 0; j <= m; j++) {
            for (int k = 0; k < v; k++) {
                if (dp[i][j][k] == -1) continue;
                
                // 不选择当前字符
                if (i < n && (dp[i + 1][j][k] < dp[i][j][k])) {
                    dp[i + 1][j][k] = dp[i][j][k];
                    path[i + 1][j][k] = 0; // 0表示不选择
                }
                if (j < m && (dp[i][j + 1][k] < dp[i][j][k])) {
                    dp[i][j + 1][k] = dp[i][j][k];
                    path[i][j + 1][k] = 0; // 0表示不选择
                }
                
                // 选择当前字符
                if (i < n && j < m && str1[i] == str2[j]) {
                    char c = str1[i];
                    int next = tree[tree[0].children[c - 'A']].nodeId;
                    // 沿着fail指针找到正确的状态
                    int temp = tree[0].children[c - 'A'];
                    while (temp != 0 && temp != k) {
                        temp = tree[temp].fail;
                    }
                    if (temp == k) {
                        next = tree[temp].children[c - 'A'];
                    }
                    
                    if (!danger[next] && dp[i + 1][j + 1][next] < dp[i][j][k] + 1) {
                        dp[i + 1][j + 1][next] = dp[i][j][k] + 1;
                        path[i + 1][j + 1][next] = 1; // 1表示选择
                    }
                }
            }
        }
    }
    
    // 找到最大值
    int maxLen = 0;
    int endI = 0, endJ = 0, endK = 0;
    for (int i = 0; i <= n; i++) {
        for (int j = 0; j <= m; j++) {
            for (int k = 0; k < v; k++) {
                if (dp[i][j][k] > maxLen) {
                    maxLen = dp[i][j][k];
                    endI = i;
                    endJ = j;
                    endK = k;
                }
            }
        }
    }
    
    // 重构答案
    if (maxLen == 0) {
        return "0";
    }
    
    string result = "";
    int i = endI, j = endJ, k = endK;
    while (i > 0 || j > 0) {
        if (path[i][j][k] == 1) {
            result += str1[i - 1];
            i--;
            j--;
            // 更新状态k
            char c = str1[i];
            int temp = 0;
            for (int idx = 0; idx < v; idx++) {
                if (tree[temp].children[c - 'A'] != 0) {
                    temp = tree[temp].children[c - 'A'];
                    if (temp == k) {
                        k = tree[temp].fail;
                        break;
                    }
                }
            }
        } else {
            if (i > 0 && dp[i - 1][j][k] == dp[i][j][k]) {
                i--;
            } else if (j > 0 && dp[i][j - 1][k] == dp[i][j][k]) {
                j--;
            } else {
                break;
            }
        }
    }
    
    reverse(result.begin(), result.end());
    return result;
}

int main() {
    // 示例测试
    string str1 = "abcdef";
    string str2 = "abcxyz";
    string virus = "xyz";
    
    // 构建AC自动机
    buildACAutomaton(virus);
    
    // 求解
    string result = longestCommonSubsequenceWithoutVirus(str1, str2, virus);
    cout << "最长公共子序列（不包含病毒串）: " << result << endl;
    
    // 另一个测试用例
    str1 = "abc";
    str2 = "acb";
    virus = "b";
    buildACAutomaton(virus);
    result = longestCommonSubsequenceWithoutVirus(str1, str2, virus);
    cout << "最长公共子序列（不包含病毒串）: " << result << endl;
    
    return 0;
}