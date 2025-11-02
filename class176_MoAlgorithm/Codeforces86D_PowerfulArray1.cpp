#include <iostream>
#include <vector>
#include <algorithm>
#include <cmath>
#include <cstring>
using namespace std;

/**
 * Codeforces 86D Powerful array
 * 题目链接：https://codeforces.com/problemset/problem/86/D
 * 
 * 题目描述：
 * 给定一个数组a[1...n]，有m次查询。每次查询[l,r]区间内，
 * 每种数字出现次数的平方和乘以该数字的值的总和。
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
 * 1 <= n, m <= 200000
 * 1 <= a[i] <= 10^6
 * 
 * 解题思路：
 * 1. 使用莫队算法处理区间查询
 * 2. 维护当前区间内每个数字的出现次数cnt[x]
 * 3. 维护当前答案ans，当添加或删除一个元素x时，更新ans：
 *    - 添加x：ans += x * (2 * cnt[x] + 1)，然后cnt[x]++
 *    - 删除x：cnt[x]--，然后ans -= x * (2 * cnt[x] + 1)
 * 4. 使用分块排序优化莫队算法的效率
 * 
 * 时间复杂度：O((n + m) * sqrt(n))
 * 空间复杂度：O(n + max_value)
 */

const int MAXN = 200010;
const int MAXV = 1000010;

struct Query {
    int l, r, id;
    long long ans;
};

int n, m;
int arr[MAXN];
Query q[MAXN];
long long cnt[MAXV];  // 统计每个数字的出现次数
int block_size;

// 快速输入函数，提高输入效率
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

// 比较函数，用于莫队查询的排序
bool compare(const Query &a, const Query &b) {
    if (a.l / block_size != b.l / block_size) {
        return a.l < b.l;
    }
    // 奇偶优化：奇数块右端点升序，偶数块右端点降序
    return (a.l / block_size & 1) == 0 ? a.r < b.r : a.r > b.r;
}

// 更新当前区间的统计信息和答案
void update(int pos, long long &res, bool add) {
    int x = arr[pos];
    if (add) {
        // 添加一个元素x，先更新答案，再增加计数
        res += (long long) x * (2 * cnt[x] + 1);
        cnt[x]++;
    } else {
        // 删除一个元素x，先减少计数，再更新答案
        cnt[x]--;
        res -= (long long) x * (2 * cnt[x] + 1);
    }
}

int main() {
    // 读取输入
    read(n);
    read(m);
    
    // 读取数组
    for (int i = 1; i <= n; i++) {
        read(arr[i]);
    }
    
    // 读取查询
    for (int i = 0; i < m; i++) {
        int l, r;
        read(l);
        read(r);
        q[i].l = l;
        q[i].r = r;
        q[i].id = i;
    }
    
    // 设置块的大小
    block_size = sqrt(n) + 1;
    
    // 对查询进行排序
    sort(q, q + m, compare);
    
    // 初始化指针和计数器
    int curL = 1, curR = 0;
    long long res = 0;
    
    // 莫队算法处理
    for (int i = 0; i < m; i++) {
        Query &query = q[i];
        
        // 扩展或收缩区间
        while (curL > query.l) update(--curL, res, true);
        while (curR < query.r) update(++curR, res, true);
        while (curL < query.l) update(curL++, res, false);
        while (curR > query.r) update(curR--, res, false);
        
        // 保存当前查询的答案
        query.ans = res;
    }
    
    // 将答案按原顺序输出
    long long output[MAXN];
    for (int i = 0; i < m; i++) {
        output[q[i].id] = q[i].ans;
    }
    
    for (int i = 0; i < m; i++) {
        printf("%lld\n", output[i]);
    }
    
    return 0;
}

/*
 * 算法分析：
 * 时间复杂度：O((n + m) * sqrt(n))
 * - 排序查询的时间复杂度：O(m * log m)
 * - 莫队算法处理的时间复杂度：每个元素最多被访问O(sqrt(n))次，总时间为O(n * sqrt(n))
 * - 整体时间复杂度：O(m * log m + n * sqrt(n))，通常m和n同阶，所以为O((n + m) * sqrt(n))
 * 
 * 空间复杂度：O(n + MAXV)，其中MAXV是最大可能的数组元素值
 * - 数组存储：O(n)
 * - 查询数组：O(m)
 * - 计数数组：O(MAXV)
 * 
 * 优化点：
 * 1. 使用了奇偶优化，减少块间转移的时间
 * 2. 使用了快速输入函数，提高输入效率
 * 3. 使用long long类型存储结果，避免溢出
 * 
 * 边界情况处理：
 * 1. 数组元素的值可能很大，但通过使用适当大小的计数数组处理
 * 2. 结果可能超出int范围，使用long long类型存储
 * 
 * 工程化考量：
 * 1. 使用静态数组代替动态分配，提高内存访问效率
 * 2. 使用printf进行输出，提高输出效率
 * 3. 模板函数read处理不同类型的输入，提高代码复用性
 * 
 * 调试技巧：
 * 1. 可以在update函数中输出中间状态，检查计数和答案是否正确
 * 2. 测试用例：如n=3, m=1, arr=[1, 2, 1]，查询[1,3]，预期结果为1*2^2 + 2*1^2 = 4 + 2 = 6
 */