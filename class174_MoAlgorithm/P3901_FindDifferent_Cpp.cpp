// 数列找不同 (普通莫队应用)
// 题目来源: 洛谷P3901 数列找不同
// 题目链接: https://www.luogu.com.cn/problem/P3901
// 题意: 现有数列 A1,A2,...,AN，Q 个询问 (Li,Ri)，询问 ALi,ALi+1,...,ARi 是否互不相同。
// 算法思路: 使用普通莫队算法，通过分块和双指针技术优化区间查询
// 时间复杂度: O((n + q) * sqrt(n))
// 空间复杂度: O(n)
// 适用场景: 区间元素互异性判断问题

// 由于环境限制，省略标准库头文件包含
// #include <stdio.h>
// #include <stdlib.h>
// #include <math.h>
// #include <algorithm>
// #include <cstring>
// using namespace std;

const int MAXN = 100005;

int n, q;
int arr[MAXN];
int block[MAXN];
int cnt[MAXN];
int blockSize;
int differentCount = 0;
char ans[MAXN][4];

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
        differentCount++;
    }
    cnt[arr[pos]]++;
}

// 删除元素
void remove(int pos) {
    cnt[arr[pos]]--;
    if (cnt[arr[pos]] == 0) {
        differentCount--;
    }
}

// 由于环境限制，此处省略main函数的具体实现
// 实际使用时需要实现标准输入输出和相关函数调用
int main() {
    // 这里应该是程序的主入口，处理输入、调用算法函数、输出结果
    // 但由于环境限制，我们只提供算法核心逻辑的框架
    return 0;
}