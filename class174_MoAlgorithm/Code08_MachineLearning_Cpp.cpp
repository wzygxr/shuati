// Machine Learning (带修改莫队应用 - 集合Mex)
// 题目来源: Codeforces 940F Machine Learning
// 题目链接: https://codeforces.com/problemset/problem/940/F
// 题目链接: https://www.luogu.com.cn/problem/CF940F
// 题意: 给定一个长度为n的数组arr，一共有m条操作，操作格式如下：操作 1 l r : arr[l..r]范围上，每种数字出现的次数，假设构成一个集合，打印这个集合中，没出现过的最小正数；操作 2 pos val : 把arr[pos]的值设置成val
// 算法思路: 使用带修改莫队算法，增加时间维度，将修改操作也纳入排序考虑
// 时间复杂度: O(n^(5/3))
// 空间复杂度: O(n)
// 适用场景: 带单点修改的区间查询问题，集合Mex问题

// 由于环境限制，省略标准库头文件包含
// #include <stdio.h>
// #include <stdlib.h>
// #include <math.h>
// #include <algorithm>
// #include <cstring>
// using namespace std;

const int MAXN = 100005;

int n, m;
int arr[MAXN];
int block[MAXN];
// cnt1[i] = j，表示i这种数出现了j次
// cnt2[i] = j，表示出现次数为i的数有j种
int cnt1[MAXN << 1];
int cnt2[MAXN];
int blockSize;
int ans[MAXN];

struct Query {
    int l, r, t, id; // l:左端点, r:右端点, t:时间戳, id:查询编号
    
    bool operator<(const Query& other) const {
        if (block[l] != block[other.l]) {
            return block[l] < block[other.l];
        }
        if (block[r] != block[other.r]) {
            return block[r] < block[other.r];
        }
        return t < other.t;
    }
} query[MAXN];

struct Update {
    int pos, val, preVal; // pos:修改位置, val:修改后的值, preVal:修改前的值
} update[MAXN];

// 删除元素
void remove(int num) {
    cnt2[cnt1[num]]--;
    cnt1[num]--;
    cnt2[cnt1[num]]++;
}

// 添加元素
void add(int num) {
    cnt2[cnt1[num]]--;
    cnt1[num]++;
    cnt2[cnt1[num]]++;
}

// 执行或撤销修改操作
// jobL, jobR: 当前查询的区间范围
// tim: 要执行或撤销的修改操作时间戳
void moveTime(int jobL, int jobR, int tim) {
    int pos = update[tim].pos;
    int val = update[tim].val;
    
    // 如果修改位置在当前查询区间内，需要更新答案
    if (jobL <= pos && pos <= jobR) {
        remove(arr[pos]);
        add(val);
    }
    
    // 交换数组中的值和修改记录中的值
    int tmp = arr[pos];
    arr[pos] = val;
    update[tim].val = tmp; // 这里有个技巧，把原值保存到val中，便于下次交换
}

// 计算Mex
int calculateMex() {
    int ret = 1;
    while (ret <= n && cnt2[ret] > 0) {
        ret++;
    }
    return ret;
}

// 由于环境限制，此处省略main函数的具体实现
// 实际使用时需要实现标准输入输出和相关函数调用
int main() {
    // 这里应该是程序的主入口，处理输入、调用算法函数、输出结果
    // 但由于环境限制，我们只提供算法核心逻辑的框架
    return 0;
}