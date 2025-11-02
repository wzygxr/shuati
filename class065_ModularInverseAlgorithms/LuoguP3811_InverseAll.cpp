#include <iostream>
using namespace std;

/**
 * 洛谷 P3811 【模板】模意义下的乘法逆元
 * 题目链接: https://www.luogu.com.cn/problem/P3811
 * 
 * 题目描述:
 * 给定 n, p 求 1∼n 中所有整数在模 p 意义下的乘法逆元。
 * 这里 a 模 p 的乘法逆元定义为 ax ≡ 1 (mod p) 的解。
 * 
 * 输入格式:
 * 一行两个正整数 n, p
 * 
 * 输出格式:
 * 输出 n 行，第 i 行表示 i 在模 p 下的乘法逆元
 * 
 * 数据范围:
 * 1 <= n <= 3 * 10^6
 * n < p < 20000528
 * 输入保证 p 为质数
 * 
 * 解题思路:
 * 使用线性递推方法计算所有逆元，时间复杂度O(n)
 * 递推公式推导：
 * 设 p = k*i + r，其中 k = p / i（整除），r = p % i
 * 则有 k*i + r ≡ 0 (mod p)
 * 两边同时乘以 i^(-1) * r^(-1) 得：
 * k*r^(-1) + i^(-1) ≡ 0 (mod p)
 * 即 i^(-1) ≡ -k*r^(-1) (mod p)
 * 由于 r < i，所以 r 的逆元在计算 i 的逆元时已经计算过了
 * 
 * 时间复杂度: O(n)
 * 空间复杂度: O(n)
 */

const int MAXN = 3000001;
int inv[MAXN];

int main() {
    int n, p;
    ios::sync_with_stdio(false);
    cin >> n >> p;
    
    // 1的逆元是1
    inv[1] = 1;
    cout << inv[1] << endl;
    
    // 线性递推计算2~n的逆元
    for (int i = 2; i <= n; i++) {
        inv[i] = (long long)(p - p / i) * inv[p % i] % p;
        cout << inv[i] << endl;
    }
    
    return 0;
}