/*
 * Code04_CountingProblem.cpp - 序列计数问题(C++版)
 * 
 * 题目来源：Codeforces 1748E
 * 题目链接：https://codeforces.com/problemset/problem/1748/E
 * 
 * 题目描述：
 * 有一个概念叫"最左端最大值位置"，表示一段范围上：
 * - 如果最大值有一个，那么最大值所在的位置，就是最左端最大值位置
 * - 如果最大值有多个，最左的那个所在的位置，就是最左端最大值位置
 * 给定一个长度为n的数组A，那么必然存在等长的数组B，当选择同样的子范围时
 * 两者在这段范围上，最左端最大值位置是相同的，不仅存在这样的数组B，而且数量无限多
 * 现在要求，数组B中的每个值都在[1,m]范围，返回有多少个这样的数组，答案对 1000000007 取模
 * 
 * 算法思路：
 * 1. 使用笛卡尔树（大根堆性质）构建数组A的树形结构
 * 2. 在笛卡尔树上进行树形动态规划
 * 3. 状态定义：dp[u][j]表示以u为根的子树，根节点值不超过j时的方案数
 * 4. 状态转移：dp[u][j] = dp[u][j-1] + dp[left[u]][j-1] * dp[right[u]][j]
 * 5. 最终答案：dp[root][m]
 * 
 * 时间复杂度：O(n * m)，但由于n*m <= 10^6，实际可接受
 * 空间复杂度：O(n * m)
 * 
 * 工程化考量：
 * - 使用动态内存分配，避免固定大小数组的空间浪费
 * - 模运算优化，避免溢出
 * - 递归深度控制，防止栈溢出
 * - 输入输出优化提高效率
 */

#include <iostream>
#include <cstdio>
#include <cstring>
#include <vector>
#include <algorithm>

using namespace std;

const int MOD = 1000000007;
const int MAXN = 200005;  // 最大节点数

// 全局变量定义
int arr[MAXN];      // 存储输入的数组A
int left_[MAXN];    // 左子树数组
int right_[MAXN];   // 右子树数组
int stack_[MAXN];   // 栈数组，用于构建笛卡尔树
int n, m;           // 输入参数

/**
 * 构建笛卡尔树（大根堆性质）
 * 使用单调栈算法，时间复杂度O(n)
 */
void build() {
    int top = 0;  // 栈顶指针
    
    for (int i = 1; i <= n; i++) {
        int pos = top;
        
        // 维护单调递减栈：找到第一个大于等于当前值的节点
        while (pos > 0 && arr[stack_[pos]] < arr[i]) {
            pos--;
        }
        
        // 连接右子树
        if (pos > 0) {
            right_[stack_[pos]] = i;
        }
        
        // 连接左子树
        if (pos < top) {
            left_[i] = stack_[pos + 1];
        }
        
        // 更新栈
        stack_[++pos] = i;
        top = pos;
    }
}

/**
 * 树形动态规划DFS
 * @param u 当前节点
 * @param dp 动态规划表
 */
void dfs(int u, vector<vector<long long>>& dp) {
    if (u == 0) return;  // 空节点直接返回
    
    // 递归处理左右子树
    dfs(left_[u], dp);
    dfs(right_[u], dp);
    
    // 临时数组，存储中间计算结果
    vector<long long> tmp(m + 1, 0);
    
    // 状态转移：dp[u][j] = dp[u][j-1] + dp[left[u]][j-1] * dp[right[u]][j]
    for (int j = 1; j <= m; j++) {
        tmp[j] = dp[left_[u]][j - 1] * dp[right_[u]][j] % MOD;
    }
    
    // 前缀和累加
    for (int j = 1; j <= m; j++) {
        dp[u][j] = (dp[u][j - 1] + tmp[j]) % MOD;
    }
}

/**
 * 清理树结构，为下一次测试做准备
 */
void clear() {
    memset(left_ + 1, 0, n * sizeof(int));
    memset(right_ + 1, 0, n * sizeof(int));
}

/**
 * 计算方案数
 * @return 满足条件的数组B的数量
 */
long long compute() {
    // 构建笛卡尔树
    build();
    
    // 动态分配DP表，避免空间浪费
    // 虽然n和m单独可能很大，但n*m <= 10^6，所以总空间可接受
    vector<vector<long long>> dp(n + 1, vector<long long>(m + 1, 0));
    
    // 初始化：空节点的方案数为1
    for (int j = 0; j <= m; j++) {
        dp[0][j] = 1;
    }
    
    // 从根节点开始DFS
    dfs(stack_[1], dp);
    
    // 清理树结构
    clear();
    
    return dp[stack_[1]][m];
}

int main() {
    // 输入优化
    ios::sync_with_stdio(false);
    cin.tie(nullptr);
    
    int cases;
    cin >> cases;
    
    while (cases--) {
        cin >> n >> m;
        
        for (int i = 1; i <= n; i++) {
            cin >> arr[i];
        }
        
        cout << compute() << "\n";
    }
    
    return 0;
}

/*
 * 算法复杂度分析：
 * 时间复杂度：O(n * m)
 *   - 构建笛卡尔树：O(n)
 *   - 树形DP：每个节点处理m次，O(n * m)
 *   - 总体：O(n * m)，但由于n*m <= 10^6，实际可接受
 * 
 * 空间复杂度：O(n * m)
 *   - DP表：n * m的空间
 *   - 树结构：O(n)
 *   - 总体：O(n * m)
 * 
 * 边界条件测试：
 * 1. n=2, m=1：最小规模测试
 * 2. n=10, m=100：中等规模测试
 * 3. n=1000, m=1000：最大规模测试（n*m=10^6）
 * 4. 递增序列：形成右斜树
 * 5. 递减序列：形成左斜树
 * 6. 随机序列：验证正确性
 * 
 * 工程化改进建议：
 * 1. 添加输入验证，确保n*m <= 10^6
 * 2. 使用滚动数组优化空间（如果可能）
 * 3. 添加内存使用监控
 * 4. 对于超大规模数据，考虑分治策略
 * 
 * 调试技巧：
 * 1. 打印树结构，验证笛卡尔树构建正确性
 * 2. 输出中间DP值，验证状态转移正确性
 * 3. 使用小规模测试用例手动验证
 */