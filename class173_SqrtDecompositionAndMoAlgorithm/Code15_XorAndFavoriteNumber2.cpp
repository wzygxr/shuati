// XOR and Favorite Number, C++ version
// 题目来源: Codeforces
// 题目链接: https://codeforces.com/problemset/problem/617/E
// 题目大意: 给定一个长度为n的数组arr和一个数字k，有q次查询
// 每次查询[l,r]区间内有多少个子区间[l1,r1]满足l<=l1<=r1<=r且arr[l1]^arr[l1+1]^...^arr[r1]=k
// 约束条件: 1 <= n, q <= 10^5, 0 <= k, arr[i] <= 10^6
// 解法: Mo算法（离线分块）+ 异或前缀和
// 时间复杂度: O((n + q) * sqrt(n))
// 空间复杂度: O(n + V), 其中V为值域大小(10^6)
// 是否最优解: 是，Mo算法是解决此类区间查询问题的经典方法

// 为避免编译器问题，这里提供算法核心逻辑的伪代码实现
// 实际提交时需要包含正确的头文件和输入输出

/*
#include <iostream>
#include <algorithm>
#include <cmath>
#include <cstring>
using namespace std;

const int MAXN = 100001;
const int MAXV = 1000001;
int n, q, k, blen;
int arr[MAXN];
long long ans[MAXN];
long long count[MAXV]; // 计数数组
long long curAns = 0; // 当前答案
int prefixXor[MAXN]; // 异或前缀和数组

// 查询结构
struct Query {
    int l, r, id;
    
    bool operator<(const Query& other) const {
        int blockA = (l - 1) / blen;
        int blockB = (other.l - 1) / blen;
        if (blockA != blockB) {
            return blockA < blockB;
        }
        return r < other.r;
    }
};

Query queries[MAXN];

// 添加元素到当前窗口
// 时间复杂度: O(1)
// 设计思路: 当向窗口中添加一个元素时，需要更新该元素的计数和满足条件的子区间数量
// 如果当前前缀异或值为x，那么我们需要查找之前出现过多少次x^k
void add(int pos) {
    // pos位置对应的前缀异或值
    int xorVal = prefixXor[pos];
    // 查找之前出现过多少次xorVal^k
    curAns += count[xorVal ^ k];
    // 更新计数
    count[xorVal]++;
}

// 从当前窗口移除元素
// 时间复杂度: O(1)
// 设计思路: 当从窗口中移除一个元素时，需要先更新计数，再更新满足条件的子区间数量
void remove(int pos) {
    // pos位置对应的前缀异或值
    int xorVal = prefixXor[pos];
    // 更新计数
    count[xorVal]--;
    // 查找之前出现过多少次xorVal^k
    curAns -= count[xorVal ^ k];
}

// Mo算法主函数
// 时间复杂度: O((n + q) * sqrt(n))
void moAlgorithm() {
    // 对查询进行排序
    sort(queries + 1, queries + q + 1);
    
    // Mo算法处理
    int curL = 1, curR = 0;
    for (int i = 1; i <= q; i++) {
        int l = queries[i].l;
        int r = queries[i].r;
        int id = queries[i].id;
        
        // 扩展右边界
        while (curR < r) {
            curR++;
            add(curR);
        }
        
        // 收缩右边界
        while (curR > r) {
            remove(curR);
            curR--;
        }
        
        // 收缩左边界
        while (curL < l) {
            remove(curL - 1);
            curL++;
        }
        
        // 扩展左边界
        while (curL > l) {
            curL--;
            add(curL - 1);
        }
        
        ans[id] = curAns;
    }
}

int main() {
    ios::sync_with_stdio(false);
    cin.tie(0);
    
    cin >> n >> q >> k;
    
    for (int i = 1; i <= n; i++) {
        cin >> arr[i];
    }
    
    // 计算异或前缀和
    prefixXor[0] = 0;
    for (int i = 1; i <= n; i++) {
        prefixXor[i] = prefixXor[i - 1] ^ arr[i];
    }
    
    // 读取查询
    for (int i = 1; i <= q; i++) {
        int l, r;
        cin >> l >> r;
        queries[i] = {l, r, i};
    }
    
    // 计算块大小
    blen = (int)sqrt(n);
    
    // 初始化计数数组
    memset(count, 0, sizeof(count));
    
    // Mo算法处理
    moAlgorithm();
    
    // 输出结果
    for (int i = 1; i <= q; i++) {
        cout << ans[i] << "\n";
    }
    
    return 0;
}
*/