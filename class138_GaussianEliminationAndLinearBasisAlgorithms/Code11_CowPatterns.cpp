// POJ 3167 Cow Patterns
// 给定一个母牛序列和一个模式序列，找出所有匹配的位置
// 测试链接 : http://poj.org/problem?id=3167

/*
 * 题目解析:
 * 这是一个模式匹配问题，需要使用KMP算法来解决。
 * 
 * 解题思路:
 * 1. 使用KMP算法进行模式匹配
 * 2. 预处理模式串的next数组
 * 3. 在母牛序列中查找所有匹配位置
 * 
 * 时间复杂度: O(n+m)
 * 空间复杂度: O(m)
 * 
 * 工程化考虑:
 * 1. 正确实现KMP算法
 * 2. 输入输出处理
 */

#include <stdio.h>
#include <string.h>
#include <vector>

using namespace std;

const int MAXN = 100005;
const int MAXM = 25;

int cows[MAXN];
int pattern[MAXM];
int next_arr[MAXM];
vector<int> result;

int n, m, s;

/*
 * 预处理模式串的next数组
 * 时间复杂度: O(m)
 * 空间复杂度: O(m)
 */
void getNext() {
    int i = 0, j = -1;
    next_arr[0] = -1;
    while (i < m) {
        if (j == -1 || pattern[i] == pattern[j]) {
            i++;
            j++;
            next_arr[i] = j;
        } else {
            j = next_arr[j];
        }
    }
}

/*
 * KMP算法匹配
 * 时间复杂度: O(n+m)
 * 空间复杂度: O(m)
 */
void kmp() {
    int i = 0, j = 0;
    while (i < n) {
        if (j == -1 || cows[i] == pattern[j]) {
            i++;
            j++;
        } else {
            j = next_arr[j];
        }

        if (j == m) {
            result.push_back(i - m);
            j = next_arr[j];
        }
    }
}

int main() {
    scanf("%d%d%d", &n, &m, &s);

    // 读取母牛序列
    for (int i = 0; i < n; i++) {
        scanf("%d", &cows[i]);
    }

    // 读取模式序列
    for (int i = 0; i < m; i++) {
        scanf("%d", &pattern[i]);
    }

    // 预处理next数组
    getNext();

    // KMP匹配
    kmp();

    // 输出结果
    printf("%d\n", (int)result.size());
    for (int i = 0; i < (int)result.size(); i++) {
        printf("%d\n", result[i] + 1);
    }

    return 0;
}