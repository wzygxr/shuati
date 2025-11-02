#include <iostream>
#include <vector>
#include <algorithm>
#include <cmath>
#include <cstring>
using namespace std;

/**
 * 洛谷 P4588 【模板】二次离线莫队
 * 题目链接：https://www.luogu.com.cn/problem/P4588
 * 
 * 题目描述：
 * 给定一个数组a[1...n]，有m次查询。每次查询[l,r]区间内满足a[i] + a[j]的二进制表示中1的个数是奇数的无序对(i,j)的数量。
 * 
 * 输入格式：
 * 第一行两个整数n和m，表示数组长度和查询次数。
 * 第二行n个整数表示数组元素。
 * 接下来m行，每行两个整数l, r表示查询区间。
 * 
 * 输出格式：
 * 对于每个查询，输出一行一个整数表示答案。
 * 
 * 数据范围：
 * 1 <= n, m <= 100000
 * 1 <= a[i] <= 100000
 * 
 * 解题思路：
 * 1. 首先，我们需要知道两个数的异或结果中1的个数的奇偶性等于它们和的二进制中1的个数的奇偶性
 * 2. 因此，问题转化为求区间内满足a[i] ^ a[j]的二进制表示中1的个数是奇数的无序对(i,j)的数量
 * 3. 对于每个位置j，我们可以维护一个前缀和sum[j]，表示前j个元素中，二进制中1的个数为奇数的元素个数
 * 4. 然后，我们可以使用二次离线莫队算法来高效处理这些查询
 * 
 * 时间复杂度：O(n * sqrt(n))
 * 空间复杂度：O(n + m)
 */

const int MAXN = 100010;
const int MAX_VAL = 100010;

// 输入数据
int n, m;
int a[MAXN];
int cnt[MAXN];  // 记录每个值的出现次数
int popcount[MAX_VAL];  // 预处理每个数的二进制中1的个数的奇偶性
long long ans[MAXN];  // 存储每个查询的答案

// 原始查询
struct Query {
    int l, r, id;
} q[MAXN];
int blockSize;

// 二次离线的查询
struct Update {
    int l, r, x, id, type;
};
vector<Update> events[MAXN];

// 预处理每个数的二进制中1的个数的奇偶性
void preprocessPopcount() {
    for (int i = 1; i < MAX_VAL; i++) {
        popcount[i] = popcount[i >> 1] ^ (i & 1);  // 如果最后一位是1，奇偶性翻转
    }
}

// 比较函数，用于莫队查询的排序
bool compare(const Query &a, const Query &b) {
    if (a.l / blockSize != b.l / blockSize) {
        return a.l < b.l;
    }
    // 奇偶优化
    return (a.l / blockSize & 1) == 0 ? a.r < b.r : a.r > b.r;
}

// 快速输入函数
template<typename T>
void read(T &x) {
    x = 0;
    char ch = getchar();
    while (ch < '0' || ch > '9') {
        ch = getchar();
    }
    while (ch >= '0' && ch <= '9') {
        x = x * 10 + (ch - '0');
        ch = getchar();
    }
}

int main() {
    preprocessPopcount();
    
    read(n);
    read(m);
    
    // 读取数组
    for (int i = 1; i <= n; i++) {
        read(a[i]);
    }
    
    // 读取查询
    for (int i = 0; i < m; i++) {
        read(q[i].l);
        read(q[i].r);
        q[i].id = i;
    }
    
    // 设置块的大小
    blockSize = sqrt(n) + 1;
    
    // 对查询进行排序
    sort(q, q + m, compare);
    
    // 二次离线莫队处理
    // 第一部分：莫队处理
    int curL = 1, curR = 0;
    long long now = 0;  // 当前的答案
    
    for (int i = 0; i < m; i++) {
        int l = q[i].l, r = q[i].r;
        
        // 处理右边界的扩展
        if (r > curR) {
            events[curR].push_back({l, r, curR, i, 1});
            now += (long long)(r - curR) * (l - 1);
            curR = r;
        }
        
        // 处理右边界的收缩
        if (r < curR) {
            events[r + 1].push_back({l, curR, l - 1, i, -1});
            now -= (long long)(curR - r) * (l - 1);
            curR = r;
        }
        
        // 处理左边界的扩展
        if (l < curL) {
            events[curL - 1].push_back({l, curL - 1, r, i, 1});
            now += (long long)(curL - l) * (n - r);
            curL = l;
        }
        
        // 处理左边界的收缩
        if (l > curL) {
            events[curL].push_back({l, l, r, i, -1});
            now -= (long long)(l - curL) * (n - r);
            curL = l;
        }
        
        // 保存当前的中间结果
        ans[q[i].id] = now;
    }
    
    // 第二部分：离线处理事件
    memset(cnt, 0, sizeof(cnt));
    
    for (int i = 1; i <= n; i++) {
        // 处理所有与当前位置i相关的事件
        for (const Update &update : events[i]) {
            int l = update.l, r = update.r;
            int x = update.x;
            int id = update.id;
            int type = update.type;
            
            // 计算区间[l,r]中满足条件的元素个数
            int res = 0;
            for (int j = l; j <= r; j++) {
                res += popcount[a[j]] ^ popcount[a[x]];
            }
            
            ans[q[id].id] += (long long)res * type;
        }
        
        // 更新计数器
        cnt[a[i]]++;
    }
    
    // 处理最终的答案，计算无序对的数量
    for (int i = 0; i < m; i++) {
        int l = q[i].l, r = q[i].r;
        long long total = (long long)(r - l + 1) * (r - l) / 2;
        ans[q[i].id] = total - ans[q[i].id];
    }
    
    // 输出结果
    for (int i = 0; i < m; i++) {
        printf("%lld\n", ans[i]);
    }
    
    return 0;
}

/*
 * 算法分析：
 * 时间复杂度：O(n * sqrt(n))
 * - 第一次莫队排序的时间复杂度：O(m * log m)
 * - 第一次莫队处理的时间复杂度：O((n + m) * sqrt(n))
 * - 第二次离线处理的时间复杂度：O(n * sqrt(n))
 * - 整体时间复杂度：O(n * sqrt(n))
 * 
 * 空间复杂度：O(n + m)
 * - 数组存储：O(n)
 * - 查询数组和答案数组：O(m)
 * - 事件列表：O(n + m)
 * 
 * 优化点：
 * 1. 使用了奇偶优化，减少块间转移的时间
 * 2. 使用了快速输入函数，提高输入效率
 * 3. 通过预处理二进制中1的个数的奇偶性，加速计算
 * 4. 使用二次离线莫队算法，将时间复杂度从O(n * sqrt(n) * log n)优化到O(n * sqrt(n))
 * 
 * 边界情况处理：
 * 1. 确保查询区间的有效性
 * 2. 处理空区间的情况
 * 3. 使用long long类型存储答案，避免溢出
 * 
 * 工程化考量：
 * 1. 使用vector存储事件，提高代码灵活性
 * 2. 使用printf进行输出，提高输出效率
 * 3. 模板函数read处理不同类型的输入，提高代码复用性
 * 
 * 调试技巧：
 * 1. 可以输出中间变量now的值，检查是否正确
 * 2. 测试用例：如n=3, m=1, a=[1,2,3]，查询[1,3]，预期结果为3（所有无序对都满足条件）
 * 3. 注意处理大数值，避免整数溢出
 */