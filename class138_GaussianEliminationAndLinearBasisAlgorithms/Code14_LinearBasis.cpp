// 洛谷 P3812 【模板】线性基
// 给定n个整数，求在这些数中选取任意个，使得他们的异或和最大
// 测试链接 : https://www.luogu.com.cn/problem/P3812

/*
 * 题目解析:
 * 这是一个线性基模板题，需要使用高斯消元的思想来解决。
 * 
 * 解题思路:
 * 1. 将每个数看作一个60位的二进制向量
 * 2. 使用高斯消元的思想构造线性基
 * 3. 从高位到低位贪心地选择，使得异或和最大
 * 
 * 时间复杂度: O(n * 60) = O(n)
 * 空间复杂度: O(60) = O(1)
 * 
 * 工程化考虑:
 * 1. 正确处理64位整数
 * 2. 从高位到低位贪心选择
 * 3. 线性基的构造和维护
 */

#include <stdio.h>
#include <string.h>

using namespace std;

const int MAXN = 100005;
const int BITS = 60;

// 线性基数组
long long basis[BITS];

int n;
long long numbers[MAXN];

/*
 * 插入一个数到线性基中
 * 时间复杂度: O(BITS)
 * 空间复杂度: O(1)
 */
bool insert(long long x) {
    for (int i = BITS - 1; i >= 0; i--) {
        if (((x >> i) & 1) == 1) {
            if (basis[i] == 0) {
                basis[i] = x;
                return true;
            }
            x ^= basis[i];
        }
    }
    return false;
}

/*
 * 求最大异或和
 * 从高位到低位贪心地选择
 * 时间复杂度: O(BITS)
 * 空间复杂度: O(1)
 */
long long getMaxXor() {
    long long result = 0;
    for (int i = BITS - 1; i >= 0; i--) {
        if (basis[i] != 0 && ((result >> i) & 1) == 0) {
            result ^= basis[i];
        }
    }
    return result;
}

int main() {
    scanf("%d", &n);

    // 读取数字
    for (int i = 1; i <= n; i++) {
        scanf("%lld", &numbers[i]);
    }

    // 初始化线性基
    for (int i = 0; i < BITS; i++) {
        basis[i] = 0;
    }

    // 构造线性基
    for (int i = 1; i <= n; i++) {
        insert(numbers[i]);
    }

    // 求最大异或和
    long long result = getMaxXor();

    // 输出结果
    printf("%lld\n", result);

    return 0;
}