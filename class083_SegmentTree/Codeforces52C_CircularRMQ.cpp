#include <iostream>
#include <vector>
#include <algorithm>
#include <climits>
using namespace std;

/**
 * Codeforces 52C - Circular RMQ
 * 题目：环形数组的区间最小值查询和区间加法
 * 来源：Codeforces
 * 网址：https://codeforces.com/problemset/problem/52/C
 * 
 * 支持环形数组的区间最小值查询和区间加法
 * 时间复杂度：
 *   - 建树：O(n)
 *   - 区间修改：O(log n)
 *   - 区间查询：O(log n)
 * 空间复杂度：O(n)
 */

class CircularRMQ {
private:
    vector<long long> tree;  // 线段树数组
    vector<long long> lazy;  // 懒标记数组
    int n;                   // 数组长度
    
    void build(int idx, int l, int r, const vector<int>& nums) {
        if (l == r) {
            tree[idx] = nums[l];
            return;
        }
        int mid = (l + r) / 2;
        build(2 * idx + 1, l, mid, nums);
        build(2 * idx + 2, mid + 1, r, nums);
        tree[idx] = min(tree[2 * idx + 1], tree[2 * idx + 2]);
    }
    
    void pushDown(int idx) {
        if (lazy[idx] != 0) {
            tree[2 * idx + 1] += lazy[idx];
            tree[2 * idx + 2] += lazy[idx];
            lazy[2 * idx + 1] += lazy[idx];
            lazy[2 * idx + 2] += lazy[idx];
            lazy[idx] = 0;
        }
    }
    
    void updateRange(int idx, int l, int r, int ql, int qr, long long val) {
        if (ql <= l && r <= qr) {
            tree[idx] += val;
            lazy[idx] += val;
            return;
        }
        pushDown(idx);
        int mid = (l + r) / 2;
        if (ql <= mid) {
            updateRange(2 * idx + 1, l, mid, ql, qr, val);
        }
        if (qr > mid) {
            updateRange(2 * idx + 2, mid + 1, r, ql, qr, val);
        }
        tree[idx] = min(tree[2 * idx + 1], tree[2 * idx + 2]);
    }
    
    long long queryRange(int idx, int l, int r, int ql, int qr) {
        if (ql <= l && r <= qr) {
            return tree[idx];
        }
        pushDown(idx);
        int mid = (l + r) / 2;
        long long minVal = LLONG_MAX;
        if (ql <= mid) {
            minVal = min(minVal, queryRange(2 * idx + 1, l, mid, ql, qr));
        }
        if (qr > mid) {
            minVal = min(minVal, queryRange(2 * idx + 2, mid + 1, r, ql, qr));
        }
        return minVal;
    }
    
public:
    CircularRMQ(const vector<int>& nums) {
        n = nums.size();
        tree.resize(4 * n, 0);
        lazy.resize(4 * n, 0);
        build(0, 0, n - 1, nums);
    }
    
    /**
     * 处理环形区间更新
     * @param l 起始位置
     * @param r 结束位置
     * @param val 要增加的值
     */
    void circularUpdate(int l, int r, long long val) {
        if (l <= r) {
            // 正常区间
            updateRange(0, 0, n - 1, l, r, val);
        } else {
            // 环形区间：从l到末尾，从开头到r
            updateRange(0, 0, n - 1, l, n - 1, val);
            updateRange(0, 0, n - 1, 0, r, val);
        }
    }
    
    /**
     * 处理环形区间查询
     * @param l 起始位置
     * @param r 结束位置
     * @return 区间最小值
     */
    long long circularQuery(int l, int r) {
        if (l <= r) {
            // 正常区间
            return queryRange(0, 0, n - 1, l, r);
        } else {
            // 环形区间：从l到末尾，从开头到r
            long long min1 = queryRange(0, 0, n - 1, l, n - 1);
            long long min2 = queryRange(0, 0, n - 1, 0, r);
            return min(min1, min2);
        }
    }
};

int main() {
    // 测试样例
    vector<int> nums = {1, 2, 3, 4, 5};
    CircularRMQ st(nums);
    
    // 正常区间查询
    cout << "正常区间[0,2]最小值: " << st.circularQuery(0, 2) << endl; // 1
    
    // 环形区间查询：从4到1 (4->末尾->开头->1)
    cout << "环形区间[4,1]最小值: " << st.circularQuery(4, 1) << endl; // 1
    
    // 环形区间更新：从4到1加2
    st.circularUpdate(4, 1, 2);
    cout << "更新后环形区间[4,1]最小值: " << st.circularQuery(4, 1) << endl; // 3
    
    // 验证更新结果
    cout << "位置0的值: " << st.circularQuery(0, 0) << endl; // 3
    cout << "位置4的值: " << st.circularQuery(4, 4) << endl; // 7
    
    return 0;
}