#include <iostream>
#include <vector>
#include <string>

using namespace std;

/**
 * POJ 3468. A Simple Problem with Integers
 * 
 * 题目描述:
 * 给定一个长度为 N 的数列 A，以及 M 条指令，每条指令可能是以下两种之一：
 * 1. "C a b c"：表示给 [a, b] 区间中的每一个数加上 c。
 * 2. "Q a b"：表示询问 [a, b] 区间中所有数的和。
 * 
 * 示例:
 * 输入:
 * 10 5
 * 1 2 3 4 5 6 7 8 9 10
 * Q 4 4
 * Q 1 10
 * Q 2 4
 * C 3 6 3
 * Q 2 4
 * 
 * 输出:
 * 4
 * 55
 * 9
 * 15
 * 
 * 题目链接: http://poj.org/problem?id=3468
 * 
 * 解题思路:
 * 使用线段树或树状数组来支持区间更新和区间查询。
 * 这里使用差分数组结合树状数组的方法：
 * 1. 维护两个树状数组：一个用于记录区间加法的累积影响，另一个用于记录区间加法的次数
 * 2. 区间更新时，使用差分思想在树状数组上进行标记
 * 3. 区间查询时，通过两个树状数组的组合计算得到区间和
 * 
 * 时间复杂度: O((N+M)logN) - 每次操作的时间复杂度为O(logN)
 * 空间复杂度: O(N) - 需要存储树状数组
 * 
 * 这是最优解之一，线段树和树状数组都是解决此类问题的标准方法。
 */
class FenwickTree {
private:
    vector<long long> tree;
    int n;
    
public:
    FenwickTree(int size) : n(size) {
        tree.resize(n + 1, 0);
    }
    
    // 单点更新
    void update(int index, long long delta) {
        while (index <= n) {
            tree[index] += delta;
            index += index & -index;
        }
    }
    
    // 前缀和查询
    long long query(int index) {
        long long sum = 0;
        while (index > 0) {
            sum += tree[index];
            index -= index & -index;
        }
        return sum;
    }
    
    // 区间和查询
    long long rangeQuery(int l, int r) {
        return query(r) - query(l - 1);
    }
};

int main() {
    int n, m;
    cin >> n >> m;
    
    vector<long long> arr(n + 1);
    FenwickTree bit1(n); // 用于记录区间加法的累积影响
    FenwickTree bit2(n); // 用于记录区间加法的次数
    
    // 读取初始数组
    for (int i = 1; i <= n; i++) {
        cin >> arr[i];
    }
    
    // 构建初始前缀和
    vector<long long> prefix(n + 1, 0);
    for (int i = 1; i <= n; i++) {
        prefix[i] = prefix[i - 1] + arr[i];
    }
    
    // 处理指令
    for (int i = 0; i < m; i++) {
        string op;
        cin >> op;
        if (op == "C") {
            int a, b;
            long long c;
            cin >> a >> b >> c;
            
            // 区间更新: 使用差分思想
            bit1.update(a, c);
            if (b + 1 <= n) {
                bit1.update(b + 1, -c);
            }
            bit2.update(a, c * (a - 1));
            if (b + 1 <= n) {
                bit2.update(b + 1, -c * b);
            }
        } else if (op == "Q") {
            int a, b;
            cin >> a >> b;
            
            // 区间查询: 使用两个树状数组组合计算
            long long sum1 = prefix[b] + bit1.query(b) * b - bit2.query(b);
            long long sum2 = prefix[a - 1] + bit1.query(a - 1) * (a - 1) - bit2.query(a - 1);
            cout << sum1 - sum2 << endl;
        }
    }
    
    return 0;
}