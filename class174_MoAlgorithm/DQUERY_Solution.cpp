// DQUERY - D-query (普通莫队模板题)
// 题目来源: SPOJ SP3267
// 题目链接: https://www.spoj.com/problems/DQUERY/
// 洛谷链接: https://www.luogu.com.cn/problem/SP3267
// 题意: 给定一个长度为n的数组，每次查询一个区间[l,r]，求该区间内不同数字的个数
// 算法思路: 使用普通莫队算法，通过分块和双指针技术优化区间查询
// 时间复杂度: O((n + q) * sqrt(n))
// 空间复杂度: O(n)
// 适用场景: 区间不同元素个数统计问题

#include <stdio.h>
#include <stdlib.h>
#include <math.h>
#include <algorithm>
#include <cstring>
using namespace std;

const int MAXN = 30005;
const int MAXV = 1000005;

int n, q;
int arr[MAXN];
int block[MAXN];
int cnt[MAXV];
int blockSize;
int answer = 0;
int ans[MAXN];

struct Query {
    int l, r, id;
    
    bool operator<(const Query& other) const {
        if (block[l] != block[other.l]) {
            return block[l] < block[other.l];
        }
        return r < other.r;
    }
} query[MAXN];

// 添加元素
void add(int pos) {
    if (cnt[arr[pos]] == 0) {
        answer++;
    }
    cnt[arr[pos]]++;
}

// 删除元素
void remove(int pos) {
    cnt[arr[pos]]--;
    if (cnt[arr[pos]] == 0) {
        answer--;
    }
}

int main() {
    scanf("%d", &n);
    
    // 计算块大小
    blockSize = (int)sqrt((double)n);
    
    // 为每个位置分配块
    for (int i = 1; i <= n; i++) {
        scanf("%d", &arr[i]);
        block[i] = (i - 1) / blockSize + 1;
    }
    
    scanf("%d", &q);
    
    // 读取查询
    for (int i = 1; i <= q; i++) {
        scanf("%d%d", &query[i].l, &query[i].r);
        query[i].id = i;
    }
    
    // 按照莫队算法排序
    sort(query + 1, query + q + 1);
    
    int curL = 1, curR = 0;
    
    // 处理每个查询
    for (int i = 1; i <= q; i++) {
        int L = query[i].l;
        int R = query[i].r;
        int idx = query[i].id;
        
        // 扩展右边界
        while (curR < R) {
            curR++;
            add(curR);
        }
        
        // 收缩右边界
        while (curR > R) {
            remove(curR);
            curR--;
        }
        
        // 收缩左边界
        while (curL < L) {
            remove(curL);
            curL++;
        }
        
        // 扩展左边界
        while (curL > L) {
            curL--;
            add(curL);
        }
        
        ans[idx] = answer;
    }
    
    // 输出结果
    for (int i = 1; i <= q; i++) {
        printf("%d\n", ans[i]);
    }
    
    return 0;
}