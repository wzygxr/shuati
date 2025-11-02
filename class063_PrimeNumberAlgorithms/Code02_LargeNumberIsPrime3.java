// 判断较大的数字是否是质数(Miller-Rabin测试)
// C++同学可以提交如下代码
// 可以通过所有测试用例，核心在于第11行，整型成了128位
// 测试链接 : https://www.luogu.com.cn/problem/U148828

// 相关题目：
// 1. POJ 1811 Prime Test
//    链接：http://poj.org/problem?id=1811
//    题目描述：给定一个大整数(2 <= N < 2^54)，判断它是否为素数，如果不是输出最小质因子
// 2. Luogu U148828 大数质数判断
//    链接：https://www.luogu.com.cn/problem/U148828
//    题目描述：判断给定的大整数是否为质数
// 3. Codeforces 679A Bear and Prime 100 (交互题)
//    链接：https://codeforces.com/problemset/problem/679/A
//    题目描述：系统想了一个2到100之间的数，你需要通过最多20次询问判断这个数是否为质数

/*

#include <bits/stdc++.h>
using namespace std;

// 使用__int128类型解决大数乘法溢出问题
// __int128是128位整数类型，比long long(64位)更大，可以处理大数运算
// 在Miller-Rabin测试中，需要计算(a*b)%mod，当a和b都接近long long最大值时，
// a*b会超出long long范围，使用__int128可以避免这个问题

typedef __int128 ll;
typedef pair<int, int> pii;

template<typename T> inline T read() {
    T x = 0, f = 1; char ch = 0;
    for(; !isdigit(ch); ch = getchar()) if(ch == '-') f = -1;
    for(; isdigit(ch); ch = getchar()) x = (x << 3) + (x << 1) + (ch - '0');
    return x * f;
}

template<typename T> inline void write(T x) {
    if(x < 0) putchar('-'), x = -x;
    if(x > 9) write(x / 10);
    putchar(x % 10 + '0');
}

template<typename T> inline void print(T x, char ed = '\n') {
    write(x), putchar(ed);
}

ll t, n;

/**
 * 快速幂运算：计算 (a^b) % mod
 * 时间复杂度：O(log b)
 * 空间复杂度：O(1)
 * 使用__int128类型避免大数乘法溢出
 */
ll qpow(ll a, ll b, ll mod) {
    ll ret = 1;
    while(b) {
        if(b & 1) ret = (ret * a) % mod;
        a = (a * a) % mod;
        b >>= 1;
    }
    return ret % mod;
}

// 使用前12个质数作为测试底数，可以有效降低误判率
vector<ll> p = {2, 3, 5, 7, 11, 13, 17, 19, 23, 29, 31, 37};

/**
 * Miller-Rabin素性测试主函数
 * 时间复杂度：O(s * (logn)^3)，其中s是测试轮数
 * 空间复杂度：O(1)
 * 
 * 算法原理：
 * 1. 特殊处理小于3的数和偶数
 * 2. 将n-1分解为u*2^t的形式，其中u是奇数
 * 3. 对每个测试底数a进行测试：
 *    a. 如果a等于n，则n是质数
 *    b. 如果a整除n，则n是合数
 *    c. 计算a^u mod n
 *    d. 如果结果为1或n-1，则通过测试
 *    e. 否则重复平方t-1次，如果在过程中得到n-1，则通过测试
 *    f. 否则n是合数
 */
bool miller_rabin(ll n) {
    // 特殊处理小于3的数和偶数
    if(n < 3 || n % 2 == 0) return n == 2;
    
    // 将n-1分解为u*2^t的形式
    ll u = n - 1, t = 0;
    while(u % 2 == 0) u /= 2, ++ t;
    
    // 对每个测试底数进行测试
    for(auto a : p) {
        // 如果底数等于n，则n是质数
        if(n == a) return 1;
        
        // 如果底数整除n，则n是合数
        if(n % a == 0) return 0;
        
        // 计算a^u mod n
        ll v = qpow(a, u, n);
        
        // 如果结果为1，则通过测试
        if(v == 1) continue;
        
        // 重复平方t-1次
        ll s = 1;
        for(; s <= t; ++ s) {
            // 如果在过程中得到n-1，则通过测试
            if(v == n - 1) break;
            v = v * v % n;
        }
        
        // 如果没有在过程中得到n-1，则n是合数
        if(s > t) return 0; 
    }
    
    // 通过所有测试，n很可能是质数
    return 1;
}

int main() {
    t = read<ll>();
    while(t --) {
        n = read<ll>();
        if(miller_rabin(n)) puts("Yes");
        else puts("No");
    }
    return 0;
}

*/


