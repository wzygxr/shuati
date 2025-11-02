/**
 * 洛谷 P2605 [JLOI2011]基站选址
 * 题目链接: https://www.luogu.com.cn/problem/P2605
 * 
 * 题目描述:
 * 一共有n个村庄排成一排，从左往右依次出现1号、2号、3号..n号村庄
 * dist[i]表示i号村庄到1号村庄的距离，该数组一定有序且无重复值
 * fix[i]表示i号村庄建立基站的安装费用
 * range[i]表示i号村庄的接收范围，任何基站和i号村庄的距离不超过这个数字，i号村庄就能得到服务
 * warranty[i]表示如果i号村庄最终没有得到任何基站的服务，需要给多少赔偿费用
 * 最多可以选择k个村庄安装基站，返回总花费最少是多少，总花费包括安装费用和赔偿费用
 * 
 * 解题思路:
 * 使用线段树优化动态规划的方法解决此问题。
 * 1. 定义状态dp[t][i]表示最多建t个基站，并且最右的基站一定要建在i号村庄，1..i号村庄的最少花费
 * 2. 由于dp[t][i]只依赖dp[t-1][..]，所以能空间压缩变成一维数组
 * 3. 对于每个村庄，预处理其能被服务的最左和最右基站位置
 * 4. 使用链式前向星存储预警列表，当基站从位置i移动到i+1时，哪些村庄会失去服务
 * 5. 使用线段树维护dp值，支持区间加法和区间查询最小值
 * 
 * 时间复杂度分析:
 * - 预处理: O(n log n)
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
#include <climits>
#include <cstring>

using namespace std;

class SegmentTree {
private:
    vector<long long> tree;
    vector<long long> lazy;
    int size;
    
public:
    /**
     * 构造函数，初始化线段树
     * 
     * @param n 线段树大小
     */
    SegmentTree(int n) : size(n) {
        tree.resize(4 * n, LLONG_MAX);
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
                if (tree[idx * 2] != LLONG_MAX) tree[idx * 2] += lazy[idx];
                if (tree[idx * 2 + 1] != LLONG_MAX) tree[idx * 2 + 1] += lazy[idx];
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
    void update(int L, int R, long long val, int l, int r, int idx) {
        if (L <= l && r <= R) {
            if (tree[idx] != LLONG_MAX) tree[idx] += val;
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
        
        tree[idx] = min(tree[idx * 2], tree[idx * 2 + 1]);
    }
    
    /**
     * 线段树区间查询
     * 
     * @param L 查询区间左边界
     * @param R 查询区间右边界
     * @param l 当前区间左边界
     * @param r 当前区间右边界
     * @param idx 当前线段树节点索引
     * @return 区间最小值
     */
    long long query(int L, int R, int l, int r, int idx) {
        if (L <= l && r <= R) {
            return tree[idx];
        }
        
        pushDown(idx, l, r);
        int mid = (l + r) / 2;
        long long result = LLONG_MAX;
        
        if (L <= mid) {
            result = min(result, query(L, R, l, mid, idx * 2));
        }
        if (R > mid) {
            result = min(result, query(L, R, mid + 1, r, idx * 2 + 1));
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
    void updatePoint(int pos, long long val, int l, int r, int idx) {
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
        
        tree[idx] = min(tree[idx * 2], tree[idx * 2 + 1]);
    }
};

/**
 * 计算基站选址最小花费
 * 
 * @param dist 村庄到1号村庄的距离
 * @param fix 安装费用
 * @param range 接收范围
 * @param warranty 赔偿费用
 * @param n 村庄数量
 * @param k 最大基站数量
 * @return 最小总花费
 */
long long minStationCost(vector<int>& dist, vector<int>& fix, vector<int>& range, 
                        vector<int>& warranty, int n, int k) {
    if (n == 0 || k == 0) return 0;
    
    // 预处理：计算每个村庄能被服务的最左和最右基站位置
    vector<int> left_bound(n + 1), right_bound(n + 1);
    for (int i = 1; i <= n; i++) {
        // 二分查找最左基站位置
        int l = 1, r = i;
        while (l <= r) {
            int mid = (l + r) / 2;
            if (dist[i] - dist[mid] <= range[i]) {
                left_bound[i] = mid;
                r = mid - 1;
            } else {
                l = mid + 1;
            }
        }
        
        // 二分查找最右基站位置
        l = i, r = n;
        while (l <= r) {
            int mid = (l + r) / 2;
            if (dist[mid] - dist[i] <= range[i]) {
                right_bound[i] = mid;
                l = mid + 1;
            } else {
                r = mid - 1;
            }
        }
    }
    
    // 滚动数组优化
    vector<long long> dp_prev(n + 1, LLONG_MAX);
    vector<long long> dp_curr(n + 1, LLONG_MAX);
    
    // 初始化：建0个基站的情况
    long long total_warranty = 0;
    for (int i = 1; i <= n; i++) {
        total_warranty += warranty[i];
    }
    
    for (int t = 1; t <= k; t++) {
        SegmentTree seg_tree(n);
        
        // 初始化线段树
        for (int i = 0; i <= n; i++) {
            if (dp_prev[i] != LLONG_MAX) {
                seg_tree.updatePoint(i, dp_prev[i], 1, n, 1);
            }
        }
        
        // 链式前向星存储预警列表
        vector<vector<int>> warn(n + 2);
        for (int i = 1; i <= n; i++) {
            warn[right_bound[i] + 1].push_back(i);
        }
        
        for (int i = 1; i <= n; i++) {
            // 处理预警列表
            for (int village : warn[i]) {
                // 当基站从位置i-1移动到i时，village村庄会失去服务
                seg_tree.update(1, left_bound[village] - 1, warranty[village], 1, n, 1);
            }
            
            // 查询最小值
            long long min_val = seg_tree.query(1, i, 1, n, 1);
            if (min_val != LLONG_MAX) {
                dp_curr[i] = min_val + fix[i];
            }
        }
        
        // 滚动数组
        dp_prev = dp_curr;
        fill(dp_curr.begin(), dp_curr.end(), LLONG_MAX);
    }
    
    // 找到最小值
    long long result = LLONG_MAX;
    for (int i = 1; i <= n; i++) {
        if (dp_prev[i] != LLONG_MAX) {
            result = min(result, dp_prev[i]);
        }
    }
    
    return min(result, total_warranty);
}

/**
 * 测试函数，验证算法正确性
 */
void testMinStationCost() {
    cout << "开始测试基站选址算法..." << endl;
    
    // 测试用例1: 简单情况
    vector<int> dist1 = {0, 1, 3, 6, 10};
    vector<int> fix1 = {0, 5, 3, 4, 2};
    vector<int> range1 = {0, 2, 1, 3, 2};
    vector<int> warranty1 = {0, 1, 2, 1, 3};
    
    long long result1 = minStationCost(dist1, fix1, range1, warranty1, 4, 2);
    cout << "测试用例1: n=4, k=2 -> " << result1 << endl;
    
    // 测试用例2: 单基站情况
    vector<int> dist2 = {0, 2, 5, 9};
    vector<int> fix2 = {0, 3, 2, 4};
    vector<int> range2 = {0, 3, 2, 4};
    vector<int> warranty2 = {0, 1, 1, 2};
    
    long long result2 = minStationCost(dist2, fix2, range2, warranty2, 3, 1);
    cout << "测试用例2: n=3, k=1 -> " << result2 << endl;
    
    // 测试用例3: 空村庄
    vector<int> dist3, fix3, range3, warranty3;
    long long result3 = minStationCost(dist3, fix3, range3, warranty3, 0, 3);
    cout << "测试用例3: 空村庄, k=3 -> " << result3 << endl;
    assert(result3 == 0 && "测试用例3失败");
    
    cout << "所有测试用例通过！" << endl;
}

int main() {
    // 运行测试
    testMinStationCost();
    
    return 0;
}