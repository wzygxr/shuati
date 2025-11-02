#include <iostream>
#include <vector>
#include <algorithm>
#include <cmath>
#include <cstring>
#include <string>
#include <unordered_map>
using namespace std;

/**
 * 洛谷 P3604 美好的每一天
 * 题目链接：https://www.luogu.com.cn/problem/P3604
 * 
 * 题目描述：
 * 给定一个字符串，查询区间内能重排成回文串的子串个数。
 * 
 * 输入格式：
 * 第一行一个字符串s
 * 第二行一个整数m
 * 接下来m行，每行两个整数l, r表示查询区间
 * 
 * 输出格式：
 * 对于每个查询，输出一行一个整数表示答案
 * 
 * 数据范围：
 * 1 <= |s| <= 60000
 * 1 <= m <= 60000
 * 
 * 解题思路：
 * 1. 一个字符串能重排成回文串的条件是：最多有一个字符的出现次数是奇数
 * 2. 使用异或前缀和来记录每个字符的奇偶性（出现偶数次为0，奇数次为1）
 * 3. 对于子串[i+1,j]，如果其对应的异或值xor[j] ^ xor[i]有0或1个1，则可以重排成回文串
 * 4. 使用莫队算法维护当前区间内各个异或值的出现次数
 * 5. 对于每个异或值，统计有多少个其他异或值与其相差不超过1个1位
 * 
 * 时间复杂度：O((n + m) * sqrt(n) * 26)
 * 空间复杂度：O(n + 不同异或值的数量)
 */

const int MAXN = 60010;

struct Query {
    int l, r, id;
    long long ans;
};

int n, m;
string s;
int xor_sum[MAXN];  // 前缀异或数组
Query q[MAXN];
int block_size;
// 使用unordered_map代替静态数组，节省内存
unordered_map<int, long long> cnt;

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

// 计算与mask相差不超过1位的所有可能的异或值的出现次数之和
long long count(int mask) {
    long long res = cnt.count(mask) ? cnt[mask] : 0;  // 相同掩码的情况
    // 枚举每一位，尝试翻转该位
    for (int i = 0; i < 26; i++) {
        int tmp = mask ^ (1 << i);
        if (cnt.count(tmp)) {
            res += cnt[tmp];
        }
    }
    return res;
}

// 更新当前区间的统计信息和答案
void update(int pos, long long &res, bool add) {
    int mask = xor_sum[pos];
    if (add) {
        // 添加一个元素时，先统计可以配对的数量，再增加计数
        res += count(mask);
        cnt[mask]++;
    } else {
        // 删除一个元素时，先减少计数，再减少对应的配对数量
        cnt[mask]--;
        if (cnt[mask] == 0) {
            cnt.erase(mask);
        }
        res -= count(mask);
    }
}

int main() {
    // 读取输入
    cin >> s;
    n = s.size();
    read(m);
    
    // 计算前缀异或数组
    xor_sum[0] = 0;
    for (int i = 1; i <= n; i++) {
        int c = s[i - 1] - 'a';  // 将字符转换为0-25的数字
        xor_sum[i] = xor_sum[i - 1] ^ (1 << c);  // 异或操作记录奇偶性
    }
    
    // 读取查询
    for (int i = 0; i < m; i++) {
        int l, r;
        read(l);
        read(r);
        q[i].l = l - 1;  // 转换为前缀异或数组的索引
        q[i].r = r;
        q[i].id = i;
    }
    
    // 设置块的大小
    block_size = sqrt(n) + 1;
    
    // 对查询进行排序
    sort(q, q + m, compare);
    
    // 初始化指针和计数器
    int curL = 0, curR = -1;
    long long res = 0;
    cnt[0] = 1;  // 初始时xor_sum[0]出现一次
    
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
 * 时间复杂度：O((n + m) * sqrt(n) * 26)
 * - 排序查询的时间复杂度：O(m * log m)
 * - 莫队算法处理的时间复杂度：每个元素最多被访问O(sqrt(n))次，每次访问需要O(26)的时间进行位操作
 * - 整体时间复杂度：O(m * log m + n * sqrt(n) * 26)，通常m和n同阶，所以为O((n + m) * sqrt(n) * 26)
 * 
 * 空间复杂度：O(n + 不同异或值的数量)
 * - 前缀异或数组：O(n)
 * - 查询数组：O(m)
 * - 计数字典：最坏情况下O(n)，但实际空间使用会小于2^26
 * 
 * 优化点：
 * 1. 使用了奇偶优化，减少块间转移的时间
 * 2. 使用了快速输入函数，提高输入效率
 * 3. 通过位运算高效表示字符奇偶性状态
 * 4. 使用unordered_map代替固定大小的数组，节省内存空间
 * 
 * 边界情况处理：
 * 1. 初始时cnt[0] = 1，因为xor_sum[0]本身也是一个前缀异或值
 * 2. 查询区间的转换：原问题中的[l,r]对应前缀异或数组的[l-1,r]
 * 3. 当计数减为0时，删除对应的键，节省空间
 * 
 * 工程化考量：
 * 1. 使用unordered_map进行高效的键值查找和更新
 * 2. 使用printf进行输出，提高输出效率
 * 3. 模板函数read处理不同类型的输入，提高代码复用性
 * 
 * 调试技巧：
 * 1. 可以在update函数中输出中间状态，检查计数和答案是否正确
 * 2. 测试用例：如s="abba"，查询[1,4]，预期结果为6（所有子串都可以重排成回文串）
 * 3. 注意内存使用，使用unordered_map避免了分配过大的静态数组
 */