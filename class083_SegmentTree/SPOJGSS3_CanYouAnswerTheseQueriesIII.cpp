#include <iostream>
#include <vector>
#include <algorithm>
#include <climits>
using namespace std;

/**
 * SPOJ GSS3 - Can you answer these queries III
 * 题目：支持单点修改和查询区间最大子段和
 * 来源：SPOJ
 * 网址：https://www.spoj.com/problems/GSS3/
 * 
 * 线段树维护四个信息：
 * 1. 区间和 sum
 * 2. 区间最大前缀和 lmax
 * 3. 区间最大后缀和 rmax
 * 4. 区间最大子段和 max
 * 
 * 时间复杂度：
 *   - 建树：O(n)
 *   - 单点修改：O(log n)
 *   - 区间查询：O(log n)
 * 空间复杂度：O(n)
 */

struct Node {
    int sum;     // 区间和
    int lmax;    // 最大前缀和
    int rmax;    // 最大后缀和
    int max;     // 最大子段和
    
    Node(int s = 0, int l = INT_MIN, int r = INT_MIN, int m = INT_MIN) 
        : sum(s), lmax(l), rmax(r), max(m) {}
};

class SegmentTree {
private:
    vector<Node> tree;
    int n;
    
    Node merge(const Node& left, const Node& right) {
        if (left.max == INT_MIN) return right;
        if (right.max == INT_MIN) return left;
        
        Node res;
        res.sum = left.sum + right.sum;
        res.lmax = max(left.lmax, left.sum + right.lmax);
        res.rmax = max(right.rmax, right.sum + left.rmax);
        res.max = max({left.max, right.max, left.rmax + right.lmax});
        
        return res;
    }
    
    void build(int idx, int l, int r, const vector<int>& nums) {
        if (l == r) {
            int val = nums[l];
            tree[idx] = Node(val, val, val, val);
            return;
        }
        
        int mid = (l + r) / 2;
        build(2 * idx + 1, l, mid, nums);
        build(2 * idx + 2, mid + 1, r, nums);
        tree[idx] = merge(tree[2 * idx + 1], tree[2 * idx + 2]);
    }
    
    void update(int idx, int l, int r, int pos, int val) {
        if (l == r) {
            tree[idx] = Node(val, val, val, val);
            return;
        }
        
        int mid = (l + r) / 2;
        if (pos <= mid) {
            update(2 * idx + 1, l, mid, pos, val);
        } else {
            update(2 * idx + 2, mid + 1, r, pos, val);
        }
        tree[idx] = merge(tree[2 * idx + 1], tree[2 * idx + 2]);
    }
    
    Node query(int idx, int l, int r, int ql, int qr) {
        if (ql <= l && r <= qr) {
            return tree[idx];
        }
        
        int mid = (l + r) / 2;
        Node left, right;
        
        if (ql <= mid) {
            left = query(2 * idx + 1, l, mid, ql, qr);
        }
        if (qr > mid) {
            right = query(2 * idx + 2, mid + 1, r, ql, qr);
        }
        
        return merge(left, right);
    }
    
public:
    SegmentTree(const vector<int>& nums) {
        n = nums.size();
        tree.resize(4 * n);
        build(0, 0, n - 1, nums);
    }
    
    void update(int pos, int val) {
        if (pos < 0 || pos >= n) {
            throw invalid_argument("Invalid position");
        }
        update(0, 0, n - 1, pos, val);
    }
    
    int query(int ql, int qr) {
        if (ql < 0 || qr >= n || ql > qr) {
            throw invalid_argument("Invalid range");
        }
        Node res = query(0, 0, n - 1, ql, qr);
        return res.max;
    }
};

int main() {
    // 测试样例
    vector<int> nums = {-1, 2, 3, -4, 5, -6};
    SegmentTree st(nums);
    
    // 查询区间最大子段和
    cout << "区间[0,5]最大子段和: " << st.query(0, 5) << endl; // 2+3-4+5=6
    
    // 单点修改
    st.update(0, 10);
    cout << "修改后区间[0,5]最大子段和: " << st.query(0, 5) << endl; // 10+2+3-4+5=16
    
    // 查询子区间
    cout << "区间[1,4]最大子段和: " << st.query(1, 4) << endl; // 2+3-4+5=6
    
    return 0;
}