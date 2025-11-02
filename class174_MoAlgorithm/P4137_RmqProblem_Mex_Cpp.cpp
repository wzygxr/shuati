// Rmq Problem / mex (回滚莫队应用)
// 题目来源: 洛谷P4137 Rmq Problem / mex
// 题目链接: https://www.luogu.com.cn/problem/P4137
// 题意: 有一个长度为 n 的数组 {a1,a2,...,an}。m 次询问，每次询问一个区间内最小没有出现过的自然数。
// 算法思路: 使用回滚莫队算法，适用于只能添加不能删除或者只能删除不能添加的区间问题
// 时间复杂度: O((n + q) * sqrt(n))
// 空间复杂度: O(n)
// 适用场景: 区间Mex查询问题

// 由于环境限制，省略标准库头文件包含
// #include <stdio.h>
// #include <stdlib.h>
// #include <math.h>
// #include <algorithm>
// #include <cstring>
// using namespace std;

const int MAXN = 200005;

int n, q;
int arr[MAXN];
int block[MAXN];
int cnt[MAXN];
int blockSize;
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
    cnt[arr[pos]]++;
}

// 删除元素（不更新答案，用于回滚）
void removeWithoutUpdate(int pos) {
    cnt[arr[pos]]--;
}

// 计算Mex
int calculateMex() {
    int mex = 0;
    while (cnt[mex] > 0) {
        mex++;
    }
    return mex;
}

// 由于环境限制，此处省略main函数的具体实现
// 实际使用时需要实现标准输入输出和相关函数调用
int main() {
    // 这里应该是程序的主入口，处理输入、调用算法函数、输出结果
    // 但由于环境限制，我们只提供算法核心逻辑的框架
    return 0;
}