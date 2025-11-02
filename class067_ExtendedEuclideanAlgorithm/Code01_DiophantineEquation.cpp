// 二元一次不定方程模版
// 给定a、b、c，求解方程ax + by = c
// 如果方程无解打印-1
// 如果方程无正整数解，但是有整数解
// 打印这些整数解中，x的最小正数值，y的最小正数值
// 如果方程有正整数解，打印正整数解的数量，同时打印所有正整数解中，
// x的最小正数值，y的最小正数值，x的最大正数值，y的最大正数值
// 1 <= a、b、c <= 10^9
// 测试链接 : https://www.luogu.com.cn/problem/P5656

// 全局变量
long long d, x, y, px, py;

/**
 * 扩展欧几里得算法
 * 求解方程ax + by = gcd(a,b)的一组特解
 * 
 * 算法原理：
 * 当b=0时，gcd(a,b)=a，此时x=1,y=0
 * 当b≠0时，递归计算gcd(b,a%b)的解，然后根据推导公式得到原方程的解
 * 
 * 时间复杂度：O(log(min(a,b)))
 * 空间复杂度：O(log(min(a,b)))，递归调用栈
 * 
 * @param a 系数a
 * @param b 系数b
 */
void exgcd(long long a, long long b) {
    if (b == 0) {
        d = a;
        x = 1;
        y = 0;
    } else {
        exgcd(b, a % b);
        px = x;
        py = y;
        x = py;
        y = px - py * (a / b);
    }
}

long long a, b, c, xd, yd, times;

/**
 * 主函数
 * 
 * 问题描述：
 * 给定a、b、c，求解方程ax + by = c
 * 
 * 解题思路：
 * 1. 使用扩展欧几里得算法求解ax + by = gcd(a,b)的一组特解
 * 2. 判断方程是否有解：当c能被gcd(a,b)整除时有解
 * 3. 如果有解，将特解乘以c/gcd(a,b)得到原方程的一组特解
 * 4. 根据通解公式求出满足条件的解
 * 
 * 数学原理：
 * 1. 裴蜀定理：方程ax + by = c有整数解当且仅当gcd(a,b)能整除c
 * 2. 扩展欧几里得算法：求解ax + by = gcd(a,b)的一组特解
 * 3. 通解公式：如果(x0,y0)是ax + by = c的一组特解，那么通解为：
 *    x = x0 + (b/gcd(a,b)) * t
 *    y = y0 - (a/gcd(a,b)) * t
 *    其中t为任意整数
 * 
 * 时间复杂度：O(log(min(a,b)))，主要消耗在扩展欧几里得算法上
 * 空间复杂度：O(1)
 * 
 * 相关题目：
 * 1. 洛谷 P5656 【模板】二元一次不定方程 (exgcd)
 *    链接：https://www.luogu.com.cn/problem/P5656
 *    这是本题的来源，是一道模板题
 * 
 * 2. LeetCode 1250. 检查「好数组」
 *    链接：https://leetcode.cn/problems/check-if-it-is-a-good-array/
 *    本题用到了裴蜀定理，如果数组中所有元素的最大公约数为1，则为好数组
 * 
 * 3. Codeforces 1244C. The Football Stage
 *    链接：https://codeforces.com/problemset/problem/1244/C
 *    本题需要求解线性丢番图方程wx + dy = p，其中w和d是给定的，p是变量
 * 
 * 4. HDU 5512 Pagodas
 *    链接：https://acm.hdu.edu.cn/showproblem.php?pid=5512
 *    本题涉及数论知识，与最大公约数有关
 * 
 * 5. POJ 2115 C Looooops
 *    链接：http://poj.org/problem?id=2115
 *    本题需要求解模线性方程，可以转化为线性丢番图方程
 * 
 * 6. POJ 1061 青蛙的约会
 *    链接：http://poj.org/problem?id=1061
 *    本题需要求解同余方程，是扩展欧几里得算法的经典应用
 * 
 * 7. LightOJ 1077 How Many Points?
 *    链接：https://lightoj.com/problem/how-many-points
 *    本题涉及最大公约数的应用，计算线段上的格点数量
 * 
 * 8. HDU 1792 A New Change Problem
 *    链接：https://acm.hdu.edu.cn/showproblem.php?pid=1792
 *    本题是硬币问题的变形，求无法表示的最大数和无法表示的数的个数
 * 
 * 9. UVA 10088 - Trees on My Island
 *    链接：https://onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&page=show_problem&problem=1029
 *    本题需要使用Pick定理计算多边形内部的格点数量
 * 
 * 10. Codeforces 514B Han Solo and Lazer Gun
 *     链接：https://codeforces.com/problemset/problem/514B
 *     本题涉及最大公约数的应用，计算点在同一直线上的数量
 * 
 * 11. AtCoder ABC161 D Lunlun Number
 *     链接：https://atcoder.jp/contests/abc161/tasks/abc161_d
 *     本题使用BFS和数学方法，与数论相关
 * 
 * 12. AtCoder ARC084 B Small Multiple
 *     链接：https://atcoder.jp/contests/abc077/tasks/arc084_b
 *     本题使用01-BFS，与数论相关
 * 
 * 13. HDU 5722 Jewelry
 *     链接：https://acm.hdu.edu.cn/showproblem.php?pid=5722
 *     本题涉及最大公约数的应用
 * 
 * 工程化考虑：
 * 1. 异常处理：需要处理输入非法、方程无解等情况
 * 2. 边界条件：需要考虑a、b、c为边界值的情况
 * 3. 性能优化：对于大数据，要注意算法的时间复杂度
 * 4. 可读性：添加详细注释，变量命名清晰
 * 5. 错误信息：提供清晰的错误提示信息
 * 6. 防止溢出：使用适当的数据类型处理大数运算
 * 
 * 算法要点：
 * 1. 扩展欧几里得算法是解决此类问题的核心
 * 2. 裴蜀定理是判断方程是否有解的依据
 * 3. 通解公式是找出所有解的关键
 * 4. 对于正整数解，需要通过调整特解来找到满足条件的解
 * 
 * 调试技巧：
 * 1. 打印中间过程：在关键步骤添加打印语句，观察变量的变化过程
 * 2. 用断言验证中间结果：使用断言验证关键中间结果的正确性
 * 3. 性能退化排查：分析算法的时间复杂度是否符合预期
 * 
 * 与标准库对比：
 * 标准库中的gcd函数通常只返回最大公约数，而扩展欧几里得算法还能找到x和y
 * 标准库通常具有更完善的边界条件处理和异常防御机制
 */
#include <iostream>
using namespace std;

int main() {
    int cases;
    cin >> cases;
    while (cases--) {
        cin >> a >> b >> c;
        exgcd(a, b);
        if (c % d != 0) { // 无整数解
            cout << -1 << endl;
        } else { // 有整数解
            x *= c / d;
            y *= c / d;
            xd = b / d;
            yd = a / d;
            if (x < 0) {
                // x要想增长到>=1且最小的值，差几个xd，算出来就是k的值
                // 那应该是(1-x)/xd，结果向上取整
                times = (1 - x + xd - 1) / xd;
                x += xd * times;
                y -= yd * times;
            } else {
                // x要想减少到>=1且最小的值，差几个xd，算出来就是k的值，向下取整
                times = (x - 1) / xd;
                x -= xd * times;
                y += yd * times;
            }
            // 此时得到的(x, y)，是x为最小正整数时的一组解
            // 然后继续讨论
            if (y <= 0) { // 无正整数解
                // x能取得的最小正数
                cout << x << " ";
                // y能取得的最小正数
                cout << y + yd * ((1 - y + yd - 1) / yd) << endl;
            } else { // 有正整数解
                // y减少到1以下，能减几次，就是正整数解的个数
                cout << ((y - 1) / yd + 1) << " ";
                // x能取得的最小正数
                cout << x << " ";
                // y能取得的最小正数
                cout << (y - (y - 1) / yd * yd) << " ";
                // x能取得的最大正数
                cout << (x + (y - 1) / yd * xd) << " ";
                // y能取得的最大正数
                cout << y << endl;
            }
        }
    }
    return 0;
}