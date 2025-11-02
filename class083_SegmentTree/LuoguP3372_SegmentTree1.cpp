#include <iostream>
#include <vector>
#include <algorithm>
using namespace std;

/**
 * Luogu P3372 - 【模板】线段树 1
 * 题目：区间修改（加法），区间查询（求和）
 * 来源：洛谷
 * 网址：https://www.luogu.com.cn/problem/P3372
 * 
 * 线段树模板题，支持区间加法和区间求和查询
 * 时间复杂度：
 *   - 建树：O(n)
 *   - 区间修改：O(log n)
 *   - 区间查询：O(log n)
 * 空间复杂度：O(n)
 */

class SegmentTree {
private:
    vector<long long> tree;  // 线段树数组
    vector<long long> lazy;   // 懒标记数组
    int n;                    // 数组长度

    void build(int idx, int l, int r, const vector<int>& nums) {
        if (l == r) {
            tree[idx] = nums[l];
            return;
        }
        int mid = (l + r) / 2;
        build(2 * idx + 1, l, mid, nums);
        build(2 * idx + 2, mid + 1, r, nums);
        tree[idx] = tree[2 * idx + 1] + tree[2 * idx + 2];
    }

    void pushDown(int idx, int l, int r) {
        if (lazy[idx] != 0) {
            int mid = (l + r) / 2;
            // 更新左子树
            tree[2 * idx + 1] += lazy[idx] * (mid - l + 1);
            lazy[2 * idx + 1] += lazy[idx];
            // 更新右子树
            tree[2 * idx + 2] += lazy[idx] * (r - mid);
            lazy[2 * idx + 2] += lazy[idx];
            // 清除当前节点的懒标记
            lazy[idx] = 0;
        }
    }

    void updateRange(int idx, int l, int r, int ql, int qr, long long val) {
        if (ql <= l && r <= qr) {
            tree[idx] += val * (r - l + 1);
            lazy[idx] += val;
            return;
        }
        pushDown(idx, l, r);
        int mid = (l + r) / 2;
        if (ql <= mid) {
            updateRange(2 * idx + 1, l, mid, ql, qr, val);
        }
        if (qr > mid) {
            updateRange(2 * idx + 2, mid + 1, r, ql, qr, val);
        }
        tree[idx] = tree[2 * idx + 1] + tree[2 * idx + 2];
    }

    long long queryRange(int idx, int l, int r, int ql, int qr) {
        if (ql <= l && r <= qr) {
            return tree[idx];
        }
        pushDown(idx, l, r);
        int mid = (l + r) / 2;
        long long sum = 0;
        if (ql <= mid) {
            sum += queryRange(2 * idx + 1, l, mid, ql, qr);
        }
        if (qr > mid) {
            sum += queryRange(2 * idx + 2, mid + 1, r, ql, qr);
        }
        return sum;
    }

public:
    SegmentTree(const vector<int>& nums) {
        n = nums.size();
        tree.resize(4 * n, 0);
        lazy.resize(4 * n, 0);
        build(0, 0, n - 1, nums);
    }

    void update(int l, int r, long long val) {
        updateRange(0, 0, n - 1, l, r, val);
    }

    long long query(int l, int r) {
        return queryRange(0, 0, n - 1, l, r);
    }
};

int main() {
    // 测试样例
    vector<int> nums = {1, 2, 3, 4, 5};
    SegmentTree st(nums);
    
    // 测试查询
    cout << "初始区间和[0,2]: " << st.query(0, 2) << endl; // 1+2+3=6
    
    // 测试区间更新
    st.update(1, 3, 2); // 给索引1-3的元素加2
    cout << "更新后区间和[0,2]: " << st.query(0, 2) << endl; // 1+4+5=10
    cout << "区间和[1,4]: " << st.query(1, 4) << endl; // 4+5+6+5=20
    
    return 0;
}