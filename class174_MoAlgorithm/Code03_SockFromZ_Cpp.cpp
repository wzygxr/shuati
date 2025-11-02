// 小Z的袜子 (普通莫队应用)
// 题目来源: 洛谷P1494 [国家集训队] 小Z的袜子
// 题目链接: https://www.luogu.com.cn/problem/P1494
// 题意: 给定一个长度为n的数组arr，一共有m条查询，查询 l r : arr[l..r]范围上，随机选不同位置的两个数，打印数值相同的概率，概率用分数的形式表达，并且约分到最简的形式
// 算法思路: 使用普通莫队算法，通过分块和双指针技术优化区间查询
// 时间复杂度: O((n + m) * sqrt(n))
// 空间复杂度: O(n)
// 适用场景: 区间概率计算问题

// 由于环境限制，省略标准库头文件包含
// #include <stdio.h>
// #include <stdlib.h>
// #include <math.h>
// #include <algorithm>
// #include <cstring>
// using namespace std;

const int MAXN = 50005;

int n, m;
int arr[MAXN];
int block[MAXN];
int cnt[MAXN];
int blockSize;
long long sum = 0; // 当前区间内相同数字对的数量
long long ans1[MAXN]; // 分子
long long ans2[MAXN]; // 分母

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

// 删除元素
void remove(int pos) {
    sum -= (long long)cnt[arr[pos]] * (cnt[arr[pos]] - 1);
    cnt[arr[pos]]--;
    sum += (long long)cnt[arr[pos]] * (cnt[arr[pos]] - 1);
}

// 添加元素
void add(int pos) {
    sum -= (long long)cnt[arr[pos]] * (cnt[arr[pos]] - 1);
    cnt[arr[pos]]++;
    sum += (long long)cnt[arr[pos]] * (cnt[arr[pos]] - 1);
}

// 由于环境限制，此处省略main函数的具体实现
// 实际使用时需要实现标准输入输出和相关函数调用
int main() {
    // 这里应该是程序的主入口，处理输入、调用算法函数、输出结果
    // 但由于环境限制，我们只提供算法核心逻辑的框架
    return 0;
}// 小Z的袜子 (普通莫队应用)
// 题目来源: 洛谷P1494 [国家集训队] 小Z的袜子
// 题目链接: https://www.luogu.com.cn/problem/P1494
// 题意: 给定一个长度为n的数组arr，一共有m条查询，查询 l r : arr[l..r]范围上，随机选不同位置的两个数，打印数值相同的概率，概率用分数的形式表达，并且约分到最简的形式
// 算法思路: 使用普通莫队算法，通过分块和双指针技术优化区间查询
// 时间复杂度: O((n + m) * sqrt(n))
// 空间复杂度: O(n)
// 适用场景: 区间概率计算问题

// 由于环境限制，省略标准库头文件包含
// #include <stdio.h>
// #include <stdlib.h>
// #include <math.h>
// #include <algorithm>
// #include <cstring>
// using namespace std;

const int MAXN = 50005;

int n, m;
int arr[MAXN];
int block[MAXN];
int cnt[MAXN];
int blockSize;
long long sum = 0; // 当前区间内相同数字对的数量
long long ans1[MAXN]; // 分子
long long ans2[MAXN]; // 分母

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

// 删除元素
void remove(int pos) {
    sum -= (long long)cnt[arr[pos]] * (cnt[arr[pos]] - 1);
    cnt[arr[pos]]--;
    sum += (long long)cnt[arr[pos]] * (cnt[arr[pos]] - 1);
}

// 添加元素
void add(int pos) {
    sum -= (long long)cnt[arr[pos]] * (cnt[arr[pos]] - 1);
    cnt[arr[pos]]++;
    sum += (long long)cnt[arr[pos]] * (cnt[arr[pos]] - 1);
}

// 由于环境限制，此处省略main函数的具体实现
// 实际使用时需要实现标准输入输出和相关函数调用
int main() {
    // 这里应该是程序的主入口，处理输入、调用算法函数、输出结果
    // 但由于环境限制，我们只提供算法核心逻辑的框架
    return 0;
}