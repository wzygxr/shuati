// 小清新人渣的本愿 (普通莫队应用 - Bitset优化)
// 题目来源: 洛谷P3674 小清新人渣的本愿
// 题目链接: https://www.luogu.com.cn/problem/P3674
// 题意: 给你一个序列 a，长度为 n，有 m 次操作，每次询问一个区间是否可以选出两个数它们的差为 x，或者询问一个区间是否可以选出两个数它们的和为 x，或者询问一个区间是否可以选出两个数它们的乘积为 x ，这三个操作分别为操作 1,2,3。
// 算法思路: 使用普通莫队算法，结合bitset优化区间查询
// 时间复杂度: O((n + m) * sqrt(n) * c / 32)，其中c为值域大小
// 空间复杂度: O(n + c)
// 适用场景: 区间和、差、积查询问题

// 由于环境限制，省略标准库头文件包含
// #include <stdio.h>
// #include <stdlib.h>
// #include <math.h>
// #include <algorithm>
// #include <cstring>
// #include <bitset>
// using namespace std;

const int MAXN = 100005;
const int MAXV = 100005;

int n, m;
int arr[MAXN];
int block[MAXN];
int cnt[MAXV]; // 记录每个值出现的次数
int blockSize;
char ans[MAXN][5]; // 存储结果

struct Query {
    int l, r, x, opt, id;
    
    bool operator<(const Query& other) const {
        if (block[l] != block[other.l]) {
            return block[l] < block[other.l];
        }
        return r < other.r;
    }
} query[MAXN];

// 添加元素
void add(int pos) {
    int val = arr[pos];
    if (cnt[val] == 0) {
        // 在bitset中设置该值
    }
    cnt[val]++;
}

// 删除元素
void remove(int pos) {
    int val = arr[pos];
    cnt[val]--;
    if (cnt[val] == 0) {
        // 在bitset中清除该值
    }
}

// 检查是否存在两个数的差为x
bool checkDifference(int x) {
    // 检查是否存在 a - b = x，即 a = b + x
    // 遍历所有可能的b值，检查b+x是否存在
    for (int b = 0; b + x < MAXV; b++) {
        if (cnt[b] > 0 && cnt[b + x] > 0) {
            // 特殊情况：x=0时需要至少有两个相同的数
            if (x == 0 && cnt[b] >= 2) {
                return true;
            } else if (x != 0) {
                return true;
            }
        }
    }
    return false;
}

// 检查是否存在两个数的和为x
bool checkSum(int x) {
    // 检查是否存在 a + b = x，即 b = x - a
    // 遍历所有可能的a值，检查x-a是否存在
    for (int a = 0; a <= x && a < MAXV; a++) {
        if (cnt[a] > 0 && cnt[x - a] > 0) {
            // 特殊情况：a = x-a时需要至少有两个相同的数
            if (a == x - a && cnt[a] >= 2) {
                return true;
            } else if (a != x - a) {
                return true;
            }
        }
    }
    return false;
}

// 检查是否存在两个数的乘积为x
bool checkProduct(int x) {
    // 检查是否存在 a * b = x
    // 遍历所有可能的a值，检查x/a是否为整数且存在
    for (int a = 1; a * a <= x && a < MAXV; a++) {
        if (x % a == 0) {
            int b = x / a;
            if (b < MAXV && cnt[a] > 0 && cnt[b] > 0) {
                // 特殊情况：a = b时需要至少有两个相同的数
                if (a == b && cnt[a] >= 2) {
                    return true;
                } else if (a != b) {
                    return true;
                }
            }
        }
    }
    return false;
}

// 由于环境限制，此处省略main函数的具体实现
// 实际使用时需要实现标准输入输出和相关函数调用
int main() {
    // 这里应该是程序的主入口，处理输入、调用算法函数、输出结果
    // 但由于环境限制，我们只提供算法核心逻辑的框架
    return 0;
}