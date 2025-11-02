// Maximum Frequency - 回滚莫队算法实现 (C++版本)
// 题目来源: 模板题 - 区间众数查询（强制在线）
// 题目链接: https://www.luogu.com.cn/problem/P5906
// 题目大意: 给定一个数组，每次查询区间[l,r]中的众数（出现次数最多的数）的出现次数
// 时间复杂度: O(n*sqrt(n))，空间复杂度: O(n)

#include <iostream>
#include <vector>
#include <algorithm>
#include <cmath>
#include <cstdio>
#include <cstring>

using namespace std;

const int MAXN = 100010;
const int MAXV = 100010;

struct Query {
    int l, r, id;
};

int n, m;
int arr[MAXN];
int bi[MAXN];
int cnt[MAXV]; // 记录每种数值的出现次数
int ans[MAXN]; // 存储答案
int maxFreq = 0; // 当前最大出现次数
vector<Query> blockQueries[MAXN]; // 按块存储查询

// 查询排序比较器 - 按右端点排序
bool QueryCmp(Query a, Query b) {
    return a.r < b.r;
}

// 添加元素到区间
void add(int value) {
    cnt[value]++;
    if (cnt[value] > maxFreq) {
        maxFreq = cnt[value];
    }
}

// 重置计数器
void reset() {
    memset(cnt, 0, sizeof(cnt));
    maxFreq = 0;
}

int main() {
    // 读取数组长度和查询次数
    scanf("%d%d", &n, &m);
    
    // 读取数组
    for (int i = 1; i <= n; i++) {
        scanf("%d", &arr[i]);
    }

    // 分块
    int blockSize = sqrt(n) + 1;
    for (int i = 1; i <= n; i++) {
        bi[i] = (i - 1) / blockSize;
    }
    
    int maxBlock = bi[n] + 1;

    // 读取查询
    for (int i = 0; i < m; i++) {
        int l, r;
        scanf("%d%d", &l, &r);
        Query q = {l, r, i};
        // 将查询按左端点所在的块存储
        blockQueries[bi[l]].push_back(q);
    }

    // 回滚莫队处理
    for (int b = 0; b < maxBlock; b++) {
        // 当前块的右端点
        int blockR = min((b + 1) * blockSize, n);
        
        // 按右端点排序同一块内的查询
        sort(blockQueries[b].begin(), blockQueries[b].end(), QueryCmp);
        
        // 暴力处理跨越多个块的查询
        
        // 初始化计数器
        reset();
        
        // 记录临时数组，用于回滚
        int tempCnt[MAXV];
        int tempMaxFreq = 0;
        
        // 右指针从块的右端点开始
        int r = blockR;
        
        for (Query q : blockQueries[b]) {
            int ql = q.l;
            int qr = q.r;
            int qid = q.id;
            
            // 如果查询的r也在当前块内，直接暴力查询
            if (bi[qr] == b) {
                // 暴力查询
                int currentMax = 0;
                memset(tempCnt, 0, sizeof(tempCnt));
                for (int i = ql; i <= qr; i++) {
                    tempCnt[arr[i]]++;
                    if (tempCnt[arr[i]] > currentMax) {
                        currentMax = tempCnt[arr[i]];
                    }
                }
                ans[qid] = currentMax;
                continue;
            }
            
            // 否则使用回滚莫队
            // 1. 将右指针移动到qr
            while (r < qr) {
                r++;
                add(arr[r]);
            }
            
            // 2. 记录当前状态
            tempMaxFreq = maxFreq;
            memcpy(tempCnt, cnt, sizeof(cnt));
            
            // 3. 移动左指针到ql，统计答案
            for (int i = blockR; i >= ql; i--) {
                cnt[arr[i]]++;
                if (cnt[arr[i]] > maxFreq) {
                    maxFreq = cnt[arr[i]];
                }
            }
            
            // 4. 记录答案
            ans[qid] = maxFreq;
            
            // 5. 回滚到之前的状态
            memcpy(cnt, tempCnt, sizeof(tempCnt));
            maxFreq = tempMaxFreq;
        }
    }

    // 输出答案
    for (int i = 0; i < m; i++) {
        printf("%d\n", ans[i]);
    }

    return 0;
}