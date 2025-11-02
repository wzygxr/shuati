#include <iostream>
#include <vector>
#include <climits>
#include <algorithm>
#include <stack>

using namespace std;

/**
 * LeetCode 1130. 叶值的最小代价生成树
 * 题目链接：https://leetcode.cn/problems/minimum-cost-tree-from-leaf-values/
 * 难度：中等
 * 
 * 题目描述：
 * 给你一个正整数数组 arr，考虑所有满足以下条件的二叉树：
 * 1. 每个节点都有 0 个或 2 个子节点
 * 2. 数组 arr 中的值与树的中序遍历中每个叶节点的值一一对应
 * 3. 每个非叶节点的值等于其左子树和右子树中叶节点的最大值的乘积
 * 
 * 解题思路：
 * 这是一个区间动态规划问题，需要构建最优二叉树。
 * 状态定义：dp[i][j]表示区间[i,j]构建二叉树的最小代价
 * 辅助数组：max[i][j]表示区间[i,j]中的最大值
 * 
 * 时间复杂度：O(n^3)
 * 空间复杂度：O(n^2)
 * 
 * 工程化考量：
 * 1. 使用单调栈优化到O(n)时间复杂度
 * 2. 处理边界条件：单个叶节点的情况
 * 3. 优化：预处理区间最大值
 * 
 * 相关题目扩展：
 * 1. LeetCode 1130. 叶值的最小代价生成树 - https://leetcode.cn/problems/minimum-cost-tree-from-leaf-values/
 * 2. LeetCode 1039. 多边形三角剖分的最低得分 - https://leetcode.cn/problems/minimum-score-triangulation-of-polygon/
 * 3. LeetCode 312. 戳气球 - https://leetcode.cn/problems/burst-balloons/
 * 4. LeetCode 1547. 切棍子的最小成本 - https://leetcode.cn/problems/minimum-cost-to-cut-a-stick/
 * 5. LeetCode 1000. 合并石头的最低成本 - https://leetcode.cn/problems/minimum-cost-to-merge-stones/
 * 6. LintCode 1063. 凸多边形的三角剖分 - https://www.lintcode.com/problem/1063/
 * 7. HackerRank - Sherlock and Cost - https://www.hackerrank.com/challenges/sherlock-and-cost/problem
 * 8. Codeforces 1327D - Infinite Path - https://codeforces.com/problemset/problem/1327/D
 * 9. AtCoder ABC144D - Water Bottle - https://atcoder.jp/contests/abc144/tasks/abc144_d
 * 10. 洛谷 P1880 [NOI1995] 石子合并 - https://www.luogu.com.cn/problem/P1880
 */

class Solution {
public:
    int mctFromLeafValues(vector<int>& arr) {
        int n = arr.size();
        if (n == 1) return 0;
        
        // DP数组和最大值数组
        vector<vector<int>> dp(n, vector<int>(n, INT_MAX));
        vector<vector<int>> maxVal(n, vector<int>(n, 0));
        
        // 初始化max数组
        for (int i = 0; i < n; i++) {
            maxVal[i][i] = arr[i];
            for (int j = i + 1; j < n; j++) {
                maxVal[i][j] = max(maxVal[i][j - 1], arr[j]);
            }
        }
        
        // 初始化dp数组
        for (int i = 0; i < n; i++) {
            dp[i][i] = 0;
        }
        
        // 两个叶节点的情况
        for (int i = 0; i < n - 1; i++) {
            dp[i][i + 1] = arr[i] * arr[i + 1];
        }
        
        // 区间动态规划
        for (int len = 2; len <= n; len++) {
            for (int i = 0; i <= n - len; i++) {
                int j = i + len - 1;
                
                for (int k = i; k < j; k++) {
                    int cost = dp[i][k] + dp[k + 1][j] + maxVal[i][k] * maxVal[k + 1][j];
                    dp[i][j] = min(dp[i][j], cost);
                }
            }
        }
        
        return dp[0][n - 1];
    }
    
    int mctFromLeafValuesStack(vector<int>& arr) {
        int res = 0;
        stack<int> st;
        st.push(INT_MAX); // 哨兵
        
        for (int num : arr) {
            while (st.top() <= num) {
                int mid = st.top();
                st.pop();
                res += mid * min(st.top(), num);
            }
            st.push(num);
        }
        
        while (st.size() > 2) {
            int top = st.top();
            st.pop();
            res += top * st.top();
        }
        
        return res;
    }
    
    int mctFromLeafValuesGreedy(vector<int>& arr) {
        vector<int> list = arr;
        int res = 0;
        
        while (list.size() > 1) {
            int minProduct = INT_MAX;
            int minIndex = -1;
            
            for (int i = 0; i < list.size() - 1; i++) {
                int product = list[i] * list[i + 1];
                if (product < minProduct) {
                    minProduct = product;
                    minIndex = i;
                }
            }
            
            res += minProduct;
            int maxVal = max(list[minIndex], list[minIndex + 1]);
            list.erase(list.begin() + minIndex + 1);
            list[minIndex] = maxVal;
        }
        
        return res;
    }
};

void testMCT() {
    Solution solution;
    
    // 测试用例1
    vector<int> arr1 = {6, 2, 4};
    cout << "测试用例1: arr = [6,2,4]" << endl;
    cout << "预期结果: 32" << endl;
    cout << "DP解法: " << solution.mctFromLeafValues(arr1) << endl;
    cout << "单调栈: " << solution.mctFromLeafValuesStack(arr1) << endl;
    cout << "贪心解法: " << solution.mctFromLeafValuesGreedy(arr1) << endl;
    cout << endl;
    
    // 测试用例2
    vector<int> arr2 = {4, 11};
    cout << "测试用例2: arr = [4,11]" << endl;
    cout << "预期结果: 44" << endl;
    cout << "DP解法: " << solution.mctFromLeafValues(arr2) << endl;
    cout << "单调栈: " << solution.mctFromLeafValuesStack(arr2) << endl;
    cout << "贪心解法: " << solution.mctFromLeafValuesGreedy(arr2) << endl;
}

int main() {
    testMCT();
    return 0;
}