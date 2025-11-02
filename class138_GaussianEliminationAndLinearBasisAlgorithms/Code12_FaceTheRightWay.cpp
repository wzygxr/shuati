// POJ 3276 Face The Right Way
// 有一排牛，有的面朝前，有的面朝后，每次可以选K头连续的牛翻转方向
// 求最少的操作次数以及对应的K值
// 测试链接 : http://poj.org/problem?id=3276

/*
 * 题目解析:
 * 这是一个开关问题，可以用贪心+枚举的方法解决。
 * 
 * 解题思路:
 * 1. 枚举所有可能的K值（1到N）
 * 2. 对于每个K值，使用贪心策略从左到右处理
 * 3. 如果当前牛面朝后，则必须翻转以它为起点的K头牛
 * 4. 记录最少操作次数及对应的K值
 * 
 * 时间复杂度: O(n^2)
 * 空间复杂度: O(n)
 * 
 * 工程化考虑:
 * 1. 正确实现贪心策略
 * 2. 优化枚举过程
 * 3. 输入输出处理
 */

#include <stdio.h>
#include <string.h>

using namespace std;

const int MAXN = 5005;

char cows[MAXN];
int flip[MAXN]; // 记录每个位置是否翻转

int n;

/*
 * 计算使用K头牛翻转时的最少操作次数
 * 时间复杂度: O(n)
 * 空间复杂度: O(n)
 */
int calculate(int k) {
    // 初始化
    for (int i = 0; i <= n; i++) {
        flip[i] = 0;
    }

    int res = 0; // 操作次数
    int sum = 0; // 当前位置的翻转次数

    for (int i = 0; i <= n - k; i++) {
        // 更新当前位置的翻转次数
        sum += flip[i];
        
        // 如果当前牛面朝后，则需要翻转
        if ((sum % 2 == 0 && cows[i] == 'B') || (sum % 2 == 1 && cows[i] == 'F')) {
            res++;
            flip[i] = 1; // 标记在位置i进行翻转
            sum++; // 更新翻转次数
        }
        
        // 移除超出窗口的翻转影响
        if (i - k + 1 >= 0) {
            sum -= flip[i - k + 1];
        }
    }

    // 检查最后K-1头牛是否都面朝前
    for (int i = n - k + 1; i < n; i++) {
        sum += flip[i];
        if ((sum % 2 == 0 && cows[i] == 'B') || (sum % 2 == 1 && cows[i] == 'F')) {
            return -1; // 无解
        }
        if (i - k + 1 >= 0) {
            sum -= flip[i - k + 1];
        }
    }

    return res;
}

int main() {
    scanf("%d", &n);

    // 读取牛的方向
    scanf("%s", cows);

    int minFlips = n + 1;
    int bestK = 1;

    // 枚举所有可能的K值
    for (int k = 1; k <= n; k++) {
        int flips = calculate(k);
        if (flips != -1 && flips < minFlips) {
            minFlips = flips;
            bestK = k;
        }
    }

    // 输出结果
    printf("%d %d\n", bestK, minFlips);

    return 0;
}