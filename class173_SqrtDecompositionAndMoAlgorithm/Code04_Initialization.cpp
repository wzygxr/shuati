// 初始化问题 - 分块算法实现 (C++版本)
// 题目来源: https://www.luogu.com.cn/problem/P5309
// 题目大意: 给定一个长度为n的数组arr，支持两种操作：
// 操作 1 x y z : 从arr[y]开始，下标每次+x，所有相应位置的数都+z，题目保证 y <= x
// 操作 2 x y   : 打印arr[x..y]的累加和，答案对1000000007取余
// 约束条件: 1 <= n、m <= 2 * 10^5

#include <cstdio>
#include <cmath>
#include <algorithm>
using namespace std;

const int MAXN = 200001;
const int MAXB = 501;
const int MOD  = 1000000007;
int n, m;

long long pre[MAXB][MAXB];
long long suf[MAXB][MAXB];
long long arr[MAXN];
long long sum[MAXB];

int blen, bnum;
int bi[MAXN];
int bl[MAXB];
int br[MAXB];

/**
 * 操作 1 x y z
 * 从arr[y]开始，下标每次+x，所有相应位置的数都+z
 * @param x 步长
 * @param y 起始位置
 * @param z 增量
 */
void add(int x, int y, long long z) {
    // 如果步长x小于等于块大小，则更新pre和suf数组
    if (x <= blen) {
        // 更新前缀和增量
        for (int i = y; i <= x; i++) {
            pre[x][i] += z;
        }
        // 更新后缀和增量
        for (int i = y; i >= 1; i--) {
            suf[x][i] += z;
        }
    } else {
        // 否则直接更新原数组和块和
        for (int i = y; i <= n; i += x) {
            arr[i] += z;
            sum[bi[i]] += z;
        }
    }
}

/**
 * 查询区间和
 * @param l 左边界
 * @param r 右边界
 * @return 区间和
 */
long long querySum(int l, int r) {
    // 获取左右边界所在的块
    int lb = bi[l], rb = bi[r];
    long long ans = 0;
    
    // 如果左右边界在同一个块内
    if (lb == rb) {
        // 直接遍历计算
        for (int i = l; i <= r; i++) {
            ans += arr[i];
        }
    } else {
        // 否则分三部分计算
        // 1. 左边不完整块
        for (int i = l; i <= br[lb]; i++) {
            ans += arr[i];
        }
        // 2. 右边不完整块
        for (int i = bl[rb]; i <= r; i++) {
            ans += arr[i];
        }
        // 3. 中间完整块
        for (int b = lb + 1; b <= rb - 1; b++) {
            ans += sum[b];
        }
    }
    return ans;
}

/**
 * 操作 2 x y
 * 查询arr[x..y]的累加和
 * @param l 左边界
 * @param r 右边界
 * @return 区间和对MOD取余
 */
long long query(int l, int r) {
    long long ans = querySum(l, r);
    
    // 对于所有步长x <= sqrt(n)，累加其对区间和的贡献
    for (int x = 1, lth, rth, num; x <= blen; x++) {
        // 计算起始位置和结束位置在步长为x时对应的编号
        lth = (l - 1) / x + 1;
        rth = (r - 1) / x + 1;
        
        // 计算中间完整段的数量
        num = rth - lth - 1;
        
        // 如果起始和结束位置在同一段
        if (lth == rth) {
            // 只需要计算起始段的贡献
            ans = ans + pre[x][(r - 1) % x + 1] - pre[x][(l - 1) % x];
        } else {
            // 否则需要计算三部分的贡献
            // 1. 起始段的后缀贡献
            // 2. 中间完整段的贡献
            // 3. 结束段的前缀贡献
            ans = ans + suf[x][(l - 1) % x + 1] + pre[x][x] * num + pre[x][(r - 1) % x + 1];
        }
    }
    return ans % MOD;
}

/**
 * 预处理函数
 * 初始化分块信息
 */
void prepare() {
    // 计算块大小，通常选择sqrt(n)
    blen = (int)sqrt((double)n);
    
    // 计算块数量
    bnum = (n + blen - 1) / blen;
    
    // 计算每个位置属于哪个块
    for (int i = 1; i <= n; i++) {
        bi[i] = (i - 1) / blen + 1;
    }
    
    // 计算每个块的边界
    for (int b = 1; b <= bnum; b++) {
        // 块的左边界
        bl[b] = (b - 1) * blen + 1;
        // 块的右边界
        br[b] = min(b * blen, n);
        
        // 计算块的初始和
        for (int i = bl[b]; i <= br[b]; i++) {
            sum[b] += arr[i];
        }
    }
}

int main() {
    // 读取数组长度n和操作次数m
    scanf("%d%d", &n, &m);
    
    // 读取初始数组
    for (int i = 1; i <= n; i++) {
        scanf("%lld", &arr[i]);
    }
    
    // 进行预处理
    prepare();
    
    // 处理m次操作
    for (int i = 1, op, x, y, z; i <= m; i++) {
        scanf("%d%d%d", &op, &x, &y);
        if (op == 1) {
            scanf("%lld", &z);
            add(x, y, z);
        } else {
            printf("%lld\n", query(x, y));
        }
    }
    return 0;
}