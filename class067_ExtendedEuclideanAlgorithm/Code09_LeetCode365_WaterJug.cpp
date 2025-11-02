// LeetCode 365. 水壶问题
// 有两个容量分别为 x 升和 y 升的水壶以及无限多的水。
// 请判断能否通过使用这两个水壶，从而可以得到恰好 z 升的水？
// 如果可以，最后请用以上水壶中的一或两个来盛放取得的 z 升水。
// 你允许：
// 1. 装满任意一个水壶
// 2. 清空任意一个水壶
// 3. 从一个水壶向另外一个水壶倒水，直到装满或者倒空
// 测试链接：https://leetcode.cn/problems/water-and-jug-problem/

/**
 * LeetCode 365. 水壶问题
 * 
 * 问题描述：
 * 有两个容量分别为 x 升和 y 升的水壶以及无限多的水。
 * 请判断能否通过使用这两个水壶，从而可以得到恰好 z 升的水？
 * 
 * 解题思路：
 * 1. 根据裴蜀定理，如果 z 是 x 和 y 的最大公约数的倍数，且 z <= x + y，则有解
 * 2. 特殊情况：如果 z == 0，直接返回 true
 * 3. 如果 x + y < z，返回 false
 * 4. 如果 x == 0 或 y == 0，需要特殊处理
 * 
 * 数学原理：
 * 1. 裴蜀定理：方程 ax + by = z 有整数解当且仅当 gcd(a,b) 能整除 z
 * 2. 水壶问题可以转化为线性丢番图方程：x * a + y * b = z
 *    a 和 b 可以是正数（装满）或负数（倒空）
 * 
 * 时间复杂度：O(log(min(x,y)))，主要消耗在求最大公约数上
 * 空间复杂度：O(1)
 * 
 * 相关题目：
 * 1. LeetCode 365. 水壶问题
 *    链接：https://leetcode.cn/problems/water-and-jug-problem/
 * 2. POJ 2142 The Balance
 *    链接：http://poj.org/problem?id=2142
 * 3. UVA 10090 Marbles
 *    链接：https://onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&page=show_problem&problem=1031
 */

#include <iostream>
#include <algorithm>
using namespace std;

/**
 * 计算两个数的最大公约数（欧几里得算法）
 * 
 * @param a 第一个数
 * @param b 第二个数
 * @return a 和 b 的最大公约数
 */
int gcd(int a, int b) {
    // 使用欧几里得算法
    while (b != 0) {
        int temp = b;
        b = a % b;
        a = temp;
    }
    return a;
}

/**
 * 判断是否可以通过两个水壶得到恰好 z 升水
 * 
 * @param x 第一个水壶的容量
 * @param y 第二个水壶的容量
 * @param z 目标水量
 * @return 是否可以得到 z 升水
 */
bool canMeasureWater(int x, int y, int z) {
    // 边界条件处理
    if (z < 0) {
        return false;
    }
    if (z == 0) {
        return true;
    }
    if (x + y < z) {
        return false;
    }
    if (x == 0 && y == 0) {
        return z == 0;
    }
    if (x == 0) {
        return z % y == 0;
    }
    if (y == 0) {
        return z % x == 0;
    }
    
    // 使用裴蜀定理判断
    return z % gcd(x, y) == 0;
}

/**
 * 扩展欧几里得算法，求解 ax + by = gcd(a,b) 的一组特解
 * 
 * @param a 第一个系数
 * @param b 第二个系数
 * @param x 引用参数，存储解x
 * @param y 引用参数，存储解y
 * @return a 和 b 的最大公约数
 */
int extendedGcd(int a, int b, int &x, int &y) {
    if (b == 0) {
        x = 1;
        y = 0;
        return a;
    }
    int gcd = extendedGcd(b, a % b, y, x);
    y -= (a / b) * x;
    return gcd;
}

int main() {
    // 测试用例1：经典水壶问题
    cout << "测试用例1: x=3, y=5, z=4 -> " << (canMeasureWater(3, 5, 4) ? "true" : "false") << endl; // true
    
    // 测试用例2：无法得到的情况
    cout << "测试用例2: x=2, y=6, z=5 -> " << (canMeasureWater(2, 6, 5) ? "true" : "false") << endl; // false
    
    // 测试用例3：边界情况
    cout << "测试用例3: x=0, y=0, z=0 -> " << (canMeasureWater(0, 0, 0) ? "true" : "false") << endl; // true
    cout << "测试用例4: x=0, y=5, z=0 -> " << (canMeasureWater(0, 5, 0) ? "true" : "false") << endl; // true
    cout << "测试用例5: x=0, y=5, z=10 -> " << (canMeasureWater(0, 5, 10) ? "true" : "false") << endl; // false
    
    // 测试用例6：裴蜀定理验证
    cout << "测试用例6: x=4, y=6, z=2 -> " << (canMeasureWater(4, 6, 2) ? "true" : "false") << endl; // true
    cout << "测试用例7: x=4, y=6, z=7 -> " << (canMeasureWater(4, 6, 7) ? "true" : "false") << endl; // false
    
    return 0;
}