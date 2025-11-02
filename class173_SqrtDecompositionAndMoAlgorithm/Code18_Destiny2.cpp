// Destiny问题 - 分块算法实现 (C++版本)
// 题目来源: Codeforces
// 题目链接: https://codeforces.com/problemset/problem/840/D
// 题目大意: 给定一个数组，多次查询区间[l,r]内出现次数超过(r-l+1)/k的数字
// 约束条件: 1 <= n, q <= 3*10^5, 2 <= k <= 5
// 解法: 分块维护频率信息
// 时间复杂度: O((n + q) * sqrt(n))
// 空间复杂度: O(n)
// 是否最优解: 是，分块算法是解决此类区间查询问题的有效方法

// 为避免编译器问题，这里提供算法核心逻辑的伪代码实现
// 实际提交时需要包含正确的头文件和输入输出

/*
#include <iostream>
#include <algorithm>
#include <cmath>
#include <unordered_map>
#include <vector>
using namespace std;

const int MAXN = 300001;
int n, q, k, blen;
int arr[MAXN];
int ans[MAXN];

// 分块相关数组
int belong[MAXN]; // 每个位置属于哪个块
int blockL[MAXN]; // 每个块的左边界
int blockR[MAXN]; // 每个块的右边界
int bcnt = 0; // 块的数量

// 块内频率信息，blockFreq[i]存储第i个块中每个数字的出现次数
unordered_map<int, int> blockFreq[MAXN];

// 构建分块结构
// 时间复杂度: O(n)
// 设计思路: 将数组分成大小约为sqrt(n)的块，预处理每个块内元素的频率信息
void build() {
    blen = (int)sqrt(n);
    bcnt = (n - 1) / blen + 1;
    
    // 初始化块信息
    for (int i = 1; i <= bcnt; i++) {
        blockL[i] = (i - 1) * blen + 1;
        blockR[i] = min(i * blen, n);
    }
    
    // 计算每个位置属于哪个块
    for (int i = 1; i <= n; i++) {
        belong[i] = (i - 1) / blen + 1;
    }
    
    // 计算每个块内元素的频率
    for (int i = 1; i <= bcnt; i++) {
        for (int j = blockL[i]; j <= blockR[i]; j++) {
            blockFreq[i][arr[j]]++;
        }
    }
}

// 查询区间[l,r]内出现次数超过(r-l+1)/k的数字
// 时间复杂度: O(sqrt(n) + 候选数字个数)
// 设计思路: 利用预处理的块频率信息快速计算候选数字，然后验证候选数字是否满足条件
int query(int l, int r) {
    int len = r - l + 1;
    int threshold = len / k;
    
    // 候选数字集合
    unordered_map<int, int> candidates;
    
    int lb = belong[l];
    int rb = belong[r];
    
    // 如果在同一个块内，暴力计算
    if (lb == rb) {
        for (int i = l; i <= r; i++) {
            candidates[arr[i]]++;
        }
    } else {
        // 添加左边不完整块的元素
        for (int i = l; i <= blockR[lb]; i++) {
            candidates[arr[i]]++;
        }
        
        // 添加中间完整块的频率信息
        for (int i = lb + 1; i < rb; i++) {
            for (auto& entry : blockFreq[i]) {
                int num = entry.first;
                int freq = entry.second;
                candidates[num] += freq;
            }
        }
        
        // 添加右边不完整块的元素
        for (int i = blockL[rb]; i <= r; i++) {
            candidates[arr[i]]++;
        }
    }
    
    // 检查候选数字
    for (auto& entry : candidates) {
        int num = entry.first;
        int freq = entry.second;
        if (freq > threshold) {
            return num;
        }
    }
    
    return -1; // 没有满足条件的数字
}

int main() {
    ios::sync_with_stdio(false);
    cin.tie(0);
    
    cin >> n >> q >> k;
    
    for (int i = 1; i <= n; i++) {
        cin >> arr[i];
    }
    
    // 构建分块结构
    build();
    
    // 处理查询
    for (int i = 1; i <= q; i++) {
        int l, r;
        cin >> l >> r;
        ans[i] = query(l, r);
    }
    
    // 输出结果
    for (int i = 1; i <= q; i++) {
        cout << ans[i] << "\n";
    }
    
    return 0;
}
*/