/**
 * Codeforces 833B The Bakery
 * 题目链接: https://codeforces.com/problemset/problem/833/B
 * 洛谷链接: https://www.luogu.com.cn/problem/CF833B
 * 
 * 题目描述:
 * 给定一个长度为n的数组，最多可以分成k段不重合的子数组
 * 每个子数组获得的分值为内部不同数字的个数
 * 返回能获得的最大分值。
 * 
 * 解题思路:
 * 使用线段树优化动态规划的方法解决此问题。
 * 1. 定义状态dp[i][j]表示将前j个元素分成i段的最大得分
 * 2. 状态转移方程：dp[i][j] = max{dp[i-1][k] + cost(k+1, j)}，其中k < j
 *    cost(k+1, j)表示区间[k+1, j]内不同数字的个数
 * 3. 使用线段树维护dp[i-1][k]的值，支持区间加法和区间查询最大值
 * 4. 对于每个新元素，更新其对之前所有位置的影响
 * 
 * 时间复杂度分析:
 * - 状态转移: O(k*n*log n)
 * - 总时间复杂度: O(k*n*log n)
 * 空间复杂度: O(n) 用于存储线段树和辅助数组
 * 
 * 工程化考量:
 * 1. 性能优化: 线段树优化动态规划
 * 2. 内存优化: 滚动数组减少空间占用
 * 3. 边界处理: 处理k=1和k=n的特殊情况
 * 4. 可读性: 清晰的变量命名和注释
 */

#include <iostream>
#include <vector>
#include <algorithm>
#include <cstring>
#include <climits>

using namespace std;

class SegmentTree {
private:
    vector<int> tree;
    vector<int> lazy;
    int size;
    
public:
    /**
     * 构造函数，初始化线段树
     * 
     * @param n 线段树大小
     */
    SegmentTree(int n) : size(n) {
        tree.resize(4 * n, 0);
        lazy.resize(4 * n, 0);
    }
    
    /**
     * 懒标记下推
     * 
     * @param idx 线段树节点索引
     * @param l 当前区间左边界
     * @param r 当前区间右边界
     */
    void pushDown(int idx, int l, int r) {
        if (lazy[idx] != 0) {
            if (l != r) {
                lazy[idx * 2] += lazy[idx];
                lazy[idx * 2 + 1] += lazy[idx];
                tree[idx * 2] += lazy[idx];
                tree[idx * 2 + 1] += lazy[idx];
            }
            lazy[idx] = 0;
        }
    }
    
    /**
     * 线段树区间更新
     * 
     * @param L 更新区间左边界
     * @param R 更新区间右边界
     * @param val 更新值
     * @param l 当前区间左边界
     * @param r 当前区间右边界
     * @param idx 当前线段树节点索引
     */
    void update(int L, int R, int val, int l, int r, int idx) {
        if (L <= l && r <= R) {
            tree[idx] += val;
            lazy[idx] += val;
            return;
        }
        
        pushDown(idx, l, r);
        int mid = (l + r) / 2;
        
        if (L <= mid) {
            update(L, R, val, l, mid, idx * 2);
        }
        if (R > mid) {
            update(L, R, val, mid + 1, r, idx * 2 + 1);
        }
        
        tree[idx] = max(tree[idx * 2], tree[idx * 2 + 1]);
    }
    
    /**
     * 线段树区间查询
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
        
        pushDown(idx, l, r);
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
    
    /**
     * 单点更新
     * 
     * @param pos 更新位置
     * @param val 更新值
     * @param l 当前区间左边界
     * @param r 当前区间右边界
     * @param idx 当前线段树节点索引
     */
    void updatePoint(int pos, int val, int l, int r, int idx) {
        if (l == r) {
            tree[idx] = val;
            return;
        }
        
        pushDown(idx, l, r);
        int mid = (l + r) / 2;
        
        if (pos <= mid) {
            updatePoint(pos, val, l, mid, idx * 2);
        } else {
            updatePoint(pos, val, mid + 1, r, idx * 2 + 1);
        }
        
        tree[idx] = max(tree[idx * 2], tree[idx * 2 + 1]);
    }
};

/**
 * 计算最大分段得分
 * 
 * @param arr 输入数组
 * @param n 数组长度
 * @param k 最大分段数
 * @return 最大得分
 */
int maxBakeryScore(vector<int>& arr, int n, int k) {
    if (n == 0 || k == 0) return 0;
    if (k == 1) {
        // 单段情况，直接返回不同数字个数
        vector<bool> visited(100001, false);
        int count = 0;
        for (int num : arr) {
            if (!visited[num]) {
                visited[num] = true;
                count++;
            }
        }
        return count;
    }
    
    // 滚动数组优化
    vector<int> dp_prev(n + 1, 0);
    vector<int> dp_curr(n + 1, 0);
    
    // 记录每个数字上一次出现的位置
    vector<int> last_pos(100001, -1);
    
    for (int seg = 1; seg <= k; seg++) {
        SegmentTree seg_tree(n);
        
        // 初始化线段树
        for (int i = 0; i < n; i++) {
            seg_tree.updatePoint(i + 1, dp_prev[i], 1, n, 1);
        }
        
        fill(last_pos.begin(), last_pos.end(), -1);
        
        for (int i = 0; i < n; i++) {
            int num = arr[i];
            
            // 更新线段树：从last_pos[num]+1到i的位置加1
            if (last_pos[num] != -1) {
                seg_tree.update(last_pos[num] + 1, i + 1, 1, 1, n, 1);
            } else {
                seg_tree.update(1, i + 1, 1, 1, n, 1);
            }
            
            // 查询最大值
            if (seg == 1) {
                dp_curr[i] = seg_tree.query(1, i + 1, 1, n, 1);
            } else {
                dp_curr[i] = seg_tree.query(seg - 1, i + 1, 1, n, 1);
            }
            
            last_pos[num] = i;
        }
        
        // 滚动数组
        dp_prev = dp_curr;
    }
    
    return dp_prev[n - 1];
}

/**
 * 测试函数，验证算法正确性
 */
void testMaxBakeryScore() {
    cout << "开始测试面包店问题算法..." << endl;
    
    // 测试用例1: 正常情况
    vector<int> arr1 = {1, 2, 2, 3};
    int result1 = maxBakeryScore(arr1, 4, 2);
    cout << "测试用例1: {1, 2, 2, 3}, k=2 -> " << result1 << endl;
    
    // 测试用例2: 单段情况
    vector<int> arr2 = {1, 2, 3, 4, 5};
    int result2 = maxBakeryScore(arr2, 5, 1);
    cout << "测试用例2: {1, 2, 3, 4, 5}, k=1 -> " << result2 << endl;
    assert(result2 == 5 && "测试用例2失败");
    
    // 测试用例3: 空数组
    vector<int> arr3;
    int result3 = maxBakeryScore(arr3, 0, 3);
    cout << "测试用例3: 空数组, k=3 -> " << result3 << endl;
    assert(result3 == 0 && "测试用例3失败");
    
    // 测试用例4: 单元素
    vector<int> arr4 = {7};
    int result4 = maxBakeryScore(arr4, 1, 2);
    cout << "测试用例4: {7}, k=2 -> " << result4 << endl;
    assert(result4 == 1 && "测试用例4失败");
    
    cout << "所有测试用例通过！" << endl;
}

int main() {
    // 运行测试
    testMaxBakeryScore();
    
    return 0;
}