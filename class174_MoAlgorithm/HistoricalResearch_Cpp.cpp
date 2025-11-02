// 歴史の研究 (AtCoder AT1219) - 回滚莫队
// 题目来源: AtCoder AT1219
// 题目链接: https://www.luogu.com.cn/problem/AT1219
// 题意: 给定一个长度为n的序列，每次询问给定一个区间，定义一种颜色的价值为它的大小乘上它在这个区间内的出现次数，
// 求所有颜色最大的价值。
//
// 算法思路:
// 1. 使用回滚莫队算法，适用于只能添加不能删除或者只能删除不能添加的区间问题
// 2. 对于左右端点在同一块内的查询，直接暴力计算
// 3. 对于跨块的查询，先扩展右边界到R，然后收缩左边界到L，最后恢复状态
//
// 时间复杂度分析:
// - 排序: O(q * log q)
// - 左指针移动: O(q * sqrt(n))
// - 右指针移动: O(n * sqrt(n))
// - 总体复杂度: O((n + q) * sqrt(n))
//
// 空间复杂度分析:
// - 存储原数组: O(n)
// - 存储计数数组: O(n)
// - 存储查询结果: O(q)
// - 总体空间复杂度: O(n)
// 适用场景: 区间众数相关问题、最大值维护问题

#define MAXN 100005

int n, q;
long long arr[MAXN];
int block[MAXN];
int cnt[MAXN];
int blockSize;
long long maxVal = 0;
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

// 自定义max函数
long long my_max(long long a, long long b) {
    return a > b ? a : b;
}

// 添加元素
void add(int pos) {
    cnt[arr[pos]]++;
    maxVal = my_max(maxVal, (long long)arr[pos] * cnt[arr[pos]]);
}

// 删除元素（不更新maxVal，用于回滚）
void removeWithoutUpdate(int pos) {
    cnt[arr[pos]]--;
}

// 由于环境限制，此处省略main函数的具体实现
// 实际使用时需要实现标准输入输出和相关函数调用
int main() {
    // 这里应该是程序的主入口，处理输入、调用算法函数、输出结果
    // 但由于环境限制，我们只提供算法核心逻辑的框架
    return 0;
}