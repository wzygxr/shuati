#include <cstdio>
#include <algorithm>
#include <cmath>
#include <cstring>
using namespace std;

// XOR and Favorite Number - 普通莫队算法实现 (C++版本)
// 题目来源: Codeforces 617E - XOR and Favorite Number
// 题目链接: https://codeforces.com/problemset/problem/617/E
// 题目大意: 给定一个长度为n的数组和一个目标值k，每次查询区间[l,r]内有多少对(i,j)满足i<=j，
// 使得a[i] XOR a[i+1] XOR ... XOR a[j] = k
// 解题思路: 使用前缀异或和 + 莫队算法
// 时间复杂度: O(n*sqrt(n))
// 空间复杂度: O(n)

const int MAXN = 100010;
int n, m, k;
int arr[MAXN]; // 原数组
int pre[MAXN]; // 前缀异或和数组
struct Query {
    int l, r, id;
} query[MAXN];

int bi[MAXN];
int cnt[1 << 20]; // 记录每种前缀异或值的出现次数 (2^20 > 10^6)
long long curAns = 0; // 当前区间的答案
long long ans[MAXN]; // 存储答案

// 分块大小
int block_size;

// 查询排序比较函数
bool cmp(const Query& a, const Query& b) {
    if (bi[a.l] != bi[b.l]) {
        return bi[a.l] < bi[b.l];
    }
    if (bi[a.l] & 1) {
        return a.r < b.r;
    } else {
        return a.r > b.r;
    }
}

// 添加元素到区间
void add(int pos) {
    // pos位置对应的前缀异或值
    int prefix = pre[pos];
    // 根据异或性质，如果要找区间[i,j]异或值为k，
    // 即pre[j] XOR pre[i-1] = k，也就是pre[i-1] = pre[j] XOR k
    // 所以我们要统计有多少个pre[i-1]满足这个条件
    curAns += cnt[prefix ^ k];
    cnt[prefix]++;
}

// 从区间中删除元素
void del(int pos) {
    int prefix = pre[pos];
    cnt[prefix]--;
    curAns -= cnt[prefix ^ k];
}

// 计算查询结果
void compute() {
    // 初始区间为[1,0]，即空区间
    int winl = 1, winr = 0;
    for (int i = 1; i <= m; i++) {
        // 注意：查询区间是[ql, qr]，对应前缀异或区间是[ql-1, qr]
        int jobl = query[i].l - 1; // 目标区间左端点（前缀异或）
        int jobr = query[i].r;     // 目标区间右端点（前缀异或）
        int id = query[i].id;       // 查询编号
        
        // 扩展右边界
        while (winr < jobr) {
            add(++winr);
        }
        
        // 收缩左边界
        while (winl < jobl) {
            del(winl++);
        }
        
        // 扩展左边界
        while (winl > jobl) {
            add(--winl);
        }
        
        // 收缩右边界
        while (winr > jobr) {
            del(winr--);
        }
        
        ans[id] = curAns;
    }
}

// 预处理
void prepare() {
    // 计算前缀异或和
    for (int i = 1; i <= n; i++) {
        pre[i] = pre[i - 1] ^ arr[i];
    }
    
    block_size = (int)sqrt((double)n);
    for (int i = 0; i <= n; i++) { // 注意：前缀异或数组下标从0到n
        bi[i] = i / block_size + 1;
    }
    sort(query + 1, query + m + 1, cmp);
}

int main() {
    scanf("%d%d%d", &n, &m, &k);
    
    for (int i = 1; i <= n; i++) {
        scanf("%d", &arr[i]);
    }
    
    for (int i = 1; i <= m; i++) {
        scanf("%d%d", &query[i].l, &query[i].r);
        query[i].id = i;
    }
    
    prepare();
    compute();
    
    for (int i = 1; i <= m; i++) {
        printf("%I64d\n", ans[i]);
    }
    
    return 0;
}