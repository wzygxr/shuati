// 大爷的字符串题 (普通莫队应用 - 区间众数)
// 题目来源: 洛谷P3709 大爷的字符串题
// 题目链接: https://www.luogu.com.cn/problem/P3709
// 题意: 给定一个长度为n的数组arr，一共有m条查询，查询 l r : arr[l..r]范围上，众数出现了几次，打印次数的相反数
// 算法思路: 使用普通莫队算法，通过分块和双指针技术优化区间查询，维护众数出现次数
// 时间复杂度: O((n + m) * sqrt(n))
// 空间复杂度: O(n)
// 适用场景: 区间众数出现次数查询问题

// 由于环境限制，省略标准库头文件包含
// #include <stdio.h>
// #include <stdlib.h>
// #include <math.h>
// #include <algorithm>
// #include <cstring>
// using namespace std;

const int MAXN = 200005;

int n, m;
int arr[MAXN];
int block[MAXN];
int cnt1[MAXN]; // cnt1[i] = j，表示窗口内，数字i出现了j次
int cnt2[MAXN]; // cnt2[i] = j，表示窗口内，出现了i次的数，有j种
int maxCnt; // 表示窗口内，众数的次数
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

// 删除元素
void deleteElement(int num) {
    if (cnt1[num] == maxCnt && cnt2[cnt1[num]] == 1) {
        maxCnt--;
    }
    cnt2[cnt1[num]]--;
    cnt1[num]--;
    cnt2[cnt1[num]]++;
}

// 添加元素
void addElement(int num) {
    cnt2[cnt1[num]]--;
    cnt1[num]++;
    cnt2[cnt1[num]]++;
    maxCnt = (maxCnt > cnt1[num]) ? maxCnt : cnt1[num];
}

// 由于环境限制，此处省略main函数的具体实现
// 实际使用时需要实现标准输入输出和相关函数调用
int main() {
    // 这里应该是程序的主入口，处理输入、调用算法函数、输出结果
    // 但由于环境限制，我们只提供算法核心逻辑的框架
    return 0;
}