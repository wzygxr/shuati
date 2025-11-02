// XOR and Favorite Number (普通莫队应用 - 异或和)
// 题目来源: Codeforces 617E XOR and Favorite Number
// 题目链接: https://codeforces.com/problemset/problem/617/E
// 题目链接: https://www.luogu.com.cn/problem/CF617E
// 题意: 给定一个长度为n的数组arr，给定一个数字k，一共有m条查询，查询 l r : arr[l..r]范围上，有多少子数组的异或和为k，打印其数量
// 算法思路: 使用普通莫队算法，通过分块和双指针技术优化区间查询，利用前缀异或和将区间异或和问题转化为点对问题
// 时间复杂度: O((n + m) * sqrt(n))
// 空间复杂度: O(n)
// 适用场景: 区间异或和查询问题

// 由于环境限制，省略标准库头文件包含
// #include <stdio.h>
// #include <stdlib.h>
// #include <math.h>
// #include <algorithm>
// #include <cstring>
// using namespace std;

const int MAXN = 100005;
const int MAXS = 1 << 20;

int n, m, k;
int arr[MAXN];
int block[MAXN];
long long cnt[MAXS]; // 记录每个前缀异或和出现的次数
int blockSize;
long long num; // 当前区间内异或和为k的子数组数量
long long ans[MAXN];

struct Query {
    int l, r, id;
    
    bool operator<(const Query& other) const {
        if (block[l] != block[other.l]) {
            return block[l] < block[other.l];
        }
        return r < other.r;
    }
} query[MAXN];

// 计算最大公约数
long long gcd(long long a, long long b) {
    return b == 0 ? a : gcd(b, a % b);
}

// 前缀异或和x要删除一次
void deletePrefix(int x) {
    if (k != 0) {
        num -= cnt[x] * cnt[x ^ k];
    } else {
        num -= (cnt[x] * (cnt[x] - 1)) >> 1;
    }
    cnt[x]--;
    if (k != 0) {
        num += cnt[x] * cnt[x ^ k];
    } else {
        num += (cnt[x] * (cnt[x] - 1)) >> 1;
    }
}

// 前缀异或和x要增加一次
void addPrefix(int x) {
    if (k != 0) {
        num -= cnt[x] * cnt[x ^ k];
    } else {
        num -= (cnt[x] * (cnt[x] - 1)) >> 1;
    }
    cnt[x]++;
    if (k != 0) {
        num += cnt[x] * cnt[x ^ k];
    } else {
        num += (cnt[x] * (cnt[x] - 1)) >> 1;
    }
}

// 由于环境限制，此处省略main函数的具体实现
// 实际使用时需要实现标准输入输出和相关函数调用
int main() {
    // 这里应该是程序的主入口，处理输入、调用算法函数、输出结果
    // 但由于环境限制，我们只提供算法核心逻辑的框架
    return 0;
}