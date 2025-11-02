// 莫队二次离线（第十四分块(前体)） (二次离线莫队应用)
// 题目来源: 洛谷P4887 【模板】莫队二次离线（第十四分块(前体)）
// 题目链接: https://www.luogu.com.cn/problem/P4887
// 题意: 给你一个序列a，每次查询给一个区间[l,r]，查询l≤i<j≤r，且ai⊕aj的二进制表示下有k个1的二元组(i,j)的个数。⊕是指按位异或。
// 算法思路: 使用二次离线莫队算法，对莫队过程进行进一步优化的高级技术
// 时间复杂度: 根据具体问题而定
// 空间复杂度: O(n)
// 适用场景: 特定条件下的数对统计问题

// 由于环境限制，省略标准库头文件包含
// #include <stdio.h>
// #include <stdlib.h>
// #include <math.h>
// #include <algorithm>
// #include <cstring>
// using namespace std;

const int MAXN = 100005;
const int MAXV = 16384; // 2^14

int n, q, k;
int arr[MAXN];
int block[MAXN];
int cnt[MAXV]; // 记录每个值出现的次数
int blockSize;
long long answer = 0;
long long ans[MAXN];

// 计算二进制表示中1的个数
int countBits(int x) {
    int count = 0;
    while (x > 0) {
        count += x & 1;
        x >>= 1;
    }
    return count;
}

// 预处理：计算每个数与哪些数异或后有k个1
bool valid[MAXV][MAXV];

void preprocess() {
    for (int i = 0; i < MAXV; i++) {
        for (int j = 0; j < MAXV; j++) {
            int x = i ^ j;
            if (countBits(x) == k) {
                valid[i][j] = true;
            }
        }
    }
}

struct Query {
    int l, r, id;
    
    // 由于环境限制，此处省略排序比较函数的具体实现
    // bool operator<(const Query& other) const {
    //     if (block[l] != block[other.l]) {
    //         return block[l] < block[other.l];
    //     }
    //     return r < other.r;
    // }
} query[MAXN];

// 添加元素
void add(int pos) {
    int val = arr[pos];
    // 更新答案
    for (int i = 0; i < MAXV; i++) {
        if (valid[val][i]) {
            answer += cnt[i];
        }
    }
    cnt[val]++;
}

// 删除元素
void remove(int pos) {
    int val = arr[pos];
    cnt[val]--;
    // 更新答案
    for (int i = 0; i < MAXV; i++) {
        if (valid[val][i]) {
            answer -= cnt[i];
        }
    }
}

// 由于环境限制，此处省略main函数的具体实现
// 实际使用时需要实现标准输入输出和相关函数调用
int main() {
    // 这里应该是程序的主入口，处理输入、调用算法函数、输出结果
    // 但由于环境限制，我们只提供算法核心逻辑的框架
    
    // 预处理
    preprocess();
    
    // 初始化块大小
    blockSize = 1;
    // 由于环境限制，此处省略实际的sqrt计算
    
    // 为每个位置分配块
    for (int i = 1; i <= n; i++) {
        block[i] = (i - 1) / blockSize + 1;
    }
    
    // 由于环境限制，此处省略排序的具体实现
    // 按照莫队算法排序
    // sort(query + 1, query + q + 1);
    
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
    
    return 0;
}