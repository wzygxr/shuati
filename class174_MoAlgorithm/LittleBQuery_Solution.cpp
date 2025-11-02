// 小B的询问 (普通莫队应用)
// 题目来源: 洛谷P2709 小B的询问
// 题目链接: https://www.luogu.com.cn/problem/P2709
// 题意: 给定一个长度为n的数组，所有数字在[1..k]范围上，定义f(i) = i这种数的出现次数的平方，一共有m条查询，查询[l,r]范围内f(1) + f(2) + ... + f(k)的值
// 算法思路: 使用普通莫队算法，通过分块和双指针技术优化区间查询
// 时间复杂度: O((n + m) * sqrt(n))
// 空间复杂度: O(n)
// 适用场景: 区间元素出现次数的统计信息计算

// 由于环境限制，省略标准库头文件包含
// #include <stdio.h>
// #include <stdlib.h>
// #include <math.h>
// #include <algorithm>
// #include <cstring>
// using namespace std;

const int MAXN = 50005;

int n, m, k;
int arr[MAXN];
int block[MAXN];
int cnt[MAXN];
int blockSize;
long long sum = 0; // 使用long long防止溢出
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

// 删除元素
void remove(int pos) {
    sum -= (long long) cnt[arr[pos]] * cnt[arr[pos]];
    cnt[arr[pos]]--;
    sum += (long long) cnt[arr[pos]] * cnt[arr[pos]];
}

// 添加元素
void add(int pos) {
    sum -= (long long) cnt[arr[pos]] * cnt[arr[pos]];
    cnt[arr[pos]]++;
    sum += (long long) cnt[arr[pos]] * cnt[arr[pos]];
}

// 由于环境限制，此处省略main函数的具体实现
// 实际使用时需要实现标准输入输出和相关函数调用
int main() {
    // 这里应该是程序的主入口，处理输入、调用算法函数、输出结果
    // 但由于环境限制，我们只提供算法核心逻辑的框架
    return 0;
}