#include <iostream>
#include <vector>
#include <algorithm>
using namespace std;

/**
 * HDU 1166 - 敌兵布阵
 * 题目：单点更新和区间求和查询
 * 来源：杭电OJ
 * 网址：http://acm.hdu.edu.cn/showproblem.php?pid=1166
 * 
 * 线段树模板题，支持单点更新和区间求和查询
 * 时间复杂度：
 *   - 建树：O(n)
 *   - 单点更新：O(log n)
 *   - 区间查询：O(log n)
 * 空间复杂度：O(n)
 */

class HDU1166_EnemyTroops {
private:
    vector<int> tree;  // 线段树数组
    int n;            // 数组长度
    
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
    
    void update(int idx, int l, int r, int pos, int val) {
        if (l == r) {
            tree[idx] += val;  // 累加更新
            return;
        }
        int mid = (l + r) / 2;
        if (pos <= mid) {
            update(2 * idx + 1, l, mid, pos, val);
        } else {
            update(2 * idx + 2, mid + 1, r, pos, val);
        }
        tree[idx] = tree[2 * idx + 1] + tree[2 * idx + 2];
    }
    
    int query(int idx, int l, int r, int ql, int qr) {
        if (ql <= l && r <= qr) {
            return tree[idx];
        }
        int mid = (l + r) / 2;
        int sum = 0;
        if (ql <= mid) {
            sum += query(2 * idx + 1, l, mid, ql, qr);
        }
        if (qr > mid) {
            sum += query(2 * idx + 2, mid + 1, r, ql, qr);
        }
        return sum;
    }
    
public:
    HDU1166_EnemyTroops(const vector<int>& nums) {
        n = nums.size();
        tree.resize(4 * n, 0);
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
        return query(0, 0, n - 1, ql, qr);
    }
};

int main() {
    // 测试样例
    vector<int> nums = {1, 2, 3, 4, 5};
    HDU1166_EnemyTroops st(nums);
    
    // 查询区间和
    cout << "区间[0,2]和: " << st.query(0, 2) << endl; // 1+2+3=6
    
    // 单点更新：位置1加3
    st.update(1, 3);
    cout << "更新后区间[0,2]和: " << st.query(0, 2) << endl; // 1+5+3=9
    
    // 单点更新：位置3减2
    st.update(3, -2);
    cout << "区间[2,4]和: " << st.query(2, 4) << endl; // 3+2+5=10
    
    // 边界测试
    cout << "单点[0]和: " << st.query(0, 0) << endl; // 1
    cout << "单点[4]和: " << st.query(4, 4) << endl; // 5
    
    return 0;
}