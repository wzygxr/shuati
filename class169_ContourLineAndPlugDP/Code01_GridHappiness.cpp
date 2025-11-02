// 插头DP和轮廓线DP专题 - 题目1: 最大化网格幸福感
// 给定四个整数m、n、in、ex，表示m*n的网格，以及in个内向的人，ex个外向的人
// 你来决定网格中应当居住多少人，并为每个人分配一个网格单元，不必让所有人都生活在网格中
// 每个人的幸福感计算如下：
// 内向的人开始时有120幸福感，但每存在一个邻居，他都会失去30幸福感
// 外向的人开始时有40幸福感，但每存在一个邻居，他都会得到20幸福感
// 邻居只包含上、下、左、右四个方向
// 网格幸福感是每个人幸福感的总和，返回最大可能的网格幸福感
// 1 <= m、n <= 5
// 1 <= in、ex <= 6
// 测试链接 : https://leetcode.cn/problems/maximize-grid-happiness/
// 这是一个典型的轮廓线DP问题，使用三进制表示每个位置的状态
//
// 题目大意：
// 给定一个m×n的网格，以及一定数量的内向和外向的人，要求在网格中安排这些人，
// 使得总幸福感最大。每个人的幸福感会受到邻居的影响。
//
// 解题思路：
// 使用轮廓线DP，逐格处理，记录每个位置的状态（空、内向、外向）
// 状态表示：用三进制表示轮廓线状态，0表示空，1表示内向，2表示外向
// 状态转移：考虑当前格子是否放置人，以及放置什么类型的人
//
// Java实现：https://github.com/yourusername/algorithm-journey/blob/main/src/class126/Code01_GridHappiness.java
// C++实现：https://github.com/yourusername/algorithm-journey/blob/main/src/class126/Code01_GridHappiness.cpp
// Python实现：https://github.com/yourusername/algorithm-journey/blob/main/src/class126/Code01_GridHappiness.py

#include <iostream>
#include <vector>
#include <algorithm>
#include <cmath>
#include <cstring>

using namespace std;

const int MAXN = 5;
const int MAXM = 5;
const int MAXP = 7;
const int MAXS = 243; // 3^5 = 243

// dp[i][j][s][a][b] 表示处理到第i行第j列，轮廓线状态为s，剩余a个内向的人，b个外向的人时的最大幸福感
int dp[MAXN][MAXM][MAXS][MAXP][MAXP];

int n, m, maxs;

/**
 * 计算最大可能的网格幸福感
 * 
 * 算法思路：
 * 使用轮廓线DP，逐格处理，记录每个位置的状态（空、内向、外向）
 * 状态表示：用三进制表示轮廓线状态，0表示空，1表示内向，2表示外向
 * 状态转移：考虑当前格子是否放置人，以及放置什么类型的人
 * 
 * 时间复杂度：O(n * m * 3^m * in * ex)，其中n和m是网格大小，in和ex是人的数量
 * 空间复杂度：O(n * m * 3^m * in * ex)
 * 
 * @param rows 网格行数
 * @param cols 网格列数
 * @param in 内向的人数
 * @param ex 外向的人数
 * @return 最大网格幸福感
 */
int getMaxGridHappiness(int rows, int cols, int in, int ex) {
    // 为了优化，将较大的维度作为行
    n = max(rows, cols);
    // 将较小的维度作为列，减少状态数
    m = min(rows, cols);
    // 状态总数
    maxs = (int)pow(3, m);
    
    // 初始化DP数组为-1（未访问）
    for (int i = 0; i < n; i++) {
        for (int j = 0; j < m; j++) {
            for (int s = 0; s < maxs; s++) {
                for (int a = 0; a <= in; a++) {
                    for (int b = 0; b <= ex; b++) {
                        dp[i][j][s][a][b] = -1;
                    }
                }
            }
        }
    }
    
    return f(0, 0, 0, in, ex, 1);
}

/**
 * 递归函数，计算最大幸福感
 * 
 * @param i 当前行
 * @param j 当前列
 * @param s 轮廓线状态
 * @param a 剩余内向的人数
 * @param b 剩余外向的人数
 * @param bit 3^j的值，用于快速获取和设置状态位
 * @return 最大幸福感
 */
int f(int i, int j, int s, int a, int b, int bit) {
    // 基本情况：处理完所有行
    if (i == n) {
        return 0;
    }
    
    // 处理完当前行，转移到下一行
    if (j == m) {
        return f(i + 1, 0, s, a, b, 1);
    }
    
    // 记忆化搜索
    if (dp[i][j][s][a][b] != -1) {
        return dp[i][j][s][a][b];
    }
    
    int ans = 0;
    
    // 获取当前位置的状态（0:空, 1:内向, 2:外向）
    int state = (s / bit) % 3;
    
    // 获取上方邻居的状态（如果存在）
    int upState = (j > 0) ? ((s / (bit / 3)) % 3) : 0;
    
    // 获取左侧邻居的状态（如果存在）
    int leftState = (i > 0) ? ((s / (bit * 3)) % 3) : 0;
    
    // 选项1：当前位置不放置人
    int option1 = f(i, j + 1, s, a, b, bit * 3);
    
    // 选项2：放置内向的人（如果有剩余）
    int option2 = 0;
    if (a > 0) {
        int newState = s - state * bit + 1 * bit;
        int happiness = 120;
        
        // 计算与上方邻居的幸福感影响
        if (upState == 1) {
            happiness -= 30; // 内向-内向：双方都失去30
            happiness -= 30;
        } else if (upState == 2) {
            happiness -= 30; // 内向-外向：内向失去30，外向得到20
            happiness += 20;
        }
        
        // 计算与左侧邻居的幸福感影响
        if (leftState == 1) {
            happiness -= 30;
            happiness -= 30;
        } else if (leftState == 2) {
            happiness -= 30;
            happiness += 20;
        }
        
        option2 = happiness + f(i, j + 1, newState, a - 1, b, bit * 3);
    }
    
    // 选项3：放置外向的人（如果有剩余）
    int option3 = 0;
    if (b > 0) {
        int newState = s - state * bit + 2 * bit;
        int happiness = 40;
        
        // 计算与上方邻居的幸福感影响
        if (upState == 1) {
            happiness += 20; // 外向-内向：外向得到20，内向失去30
            happiness -= 30;
        } else if (upState == 2) {
            happiness += 20; // 外向-外向：双方都得到20
            happiness += 20;
        }
        
        // 计算与左侧邻居的幸福感影响
        if (leftState == 1) {
            happiness += 20;
            happiness -= 30;
        } else if (leftState == 2) {
            happiness += 20;
            happiness += 20;
        }
        
        option3 = happiness + f(i, j + 1, newState, a, b - 1, bit * 3);
    }
    
    // 取三种选项的最大值
    ans = max(option1, max(option2, option3));
    
    dp[i][j][s][a][b] = ans;
    return ans;
}

// 测试用例
int main() {
    // 测试用例1：2x2网格，1个内向，1个外向
    cout << getMaxGridHappiness(2, 2, 1, 1) << endl;
    
    // 测试用例2：3x3网格，2个内向，1个外向
    cout << getMaxGridHappiness(3, 3, 2, 1) << endl;
    
    return 0;
}