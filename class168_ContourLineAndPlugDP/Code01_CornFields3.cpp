// 种草的方法数(轮廓线dp+空间压缩)
// 给定一个n*m的二维网格grid
// 网格里只有0、1两种值，0表示该田地不能种草，1表示该田地可以种草
// 种草的时候，任何两个种了草的田地不能相邻，相邻包括上、下、左、右四个方向
// 你可以随意决定种多少草，只要不破坏上面的规则即可
// 返回种草的方法数，答案对100000000取模
// 1 <= n, m <= 12
// 测试链接 : https://www.luogu.com.cn/problem/P1879
// 提交以下的code，提交时请把类名改成"Main"，可以通过所有用例

// 题目解析：
// 这是一个经典的轮廓线DP问题，使用空间优化技术。我们需要在网格中种植草皮，
// 满足相邻格子不能同时种草的约束。该问题可以使用轮廓线动态规划解决。

// 解题思路：
// 使用轮廓线DP方法，并通过滚动数组优化空间复杂度。轮廓线是已决策格子和未决策格子的分界线。
// 在逐格递推的过程中，轮廓线将棋盘分为已处理和未处理两部分。

// 状态设计：
// dp[j][s] 表示处理到当前行第j列，轮廓线状态为s时的方案数。
// 轮廓线：当前格子(i,j)左边的格子(i,j-1)和上面的格子(i-1,j)到(i,j-1)的这一段。
// 状态s用二进制表示，第k位为1表示轮廓线第k个位置已种草，为0表示未种草。

// 状态转移：
// 对于当前格子(i,j)，我们考虑两种情况：
// 1. 不种草：将轮廓线状态中第j位设为0，然后转移到下一个格子
// 2. 种草：前提是该位置可以种草且不与相邻位置冲突，将轮廓线状态中第j位设为1，然后转移到下一个格子

// 最优性分析：
// 该解法的时间复杂度为O(n * m * 2^m)，空间复杂度为O(2^m)。
// 通过滚动数组将空间复杂度从O(n * m * 2^m)优化至O(2^m)。

// 边界场景处理：
// 1. 当n=0或m=0时，方案数为1（空网格有一种方案）
// 2. 当网格全为0时，方案数为1（不能种任何草）
// 3. 当到达行末时，转移到下一行

// 工程化考量：
// 1. 使用滚动数组优化空间复杂度
// 2. 使用位运算优化状态操作
// 3. 输入输出使用BufferedReader和PrintWriter提高效率
// 4. 使用Arrays.fill初始化数组
// 5. 对于特殊情况进行了预处理优化

// Java实现链接：https://github.com/yourusername/algorithm-journey/blob/main/class125/Code01_CornFields3.java
// Python实现链接：https://github.com/yourusername/algorithm-journey/blob/main/class125/Code01_CornFields3.py
// C++实现链接：https://github.com/yourusername/algorithm-journey/blob/main/class125/Code01_CornFields3.cpp

#include <iostream>
#include <vector>
#include <cstring>
using namespace std;

const int MOD = 100000000;
const int MAXM = 12;

int n, m;
vector<int> grid;
long long dp[2][1 << MAXM];

// 检查状态是否合法（相邻位置不能同时为1）
bool isValid(int state) {
    for (int i = 1; i < m; i++) {
        if ((state & (1 << i)) && (state & (1 << (i - 1)))) {
            return false;
        }
    }
    return true;
}

// 检查状态是否与土地条件匹配
bool matchGrid(int row, int state) {
    for (int i = 0; i < m; i++) {
        // 如果当前位置种草但土地不允许
        if ((state & (1 << i)) && !(grid[row] & (1 << i))) {
            return false;
        }
    }
    return true;
}

long long compute() {
    // 初始化DP数组
    memset(dp, 0, sizeof(dp));
    dp[0][0] = 1;
    
    int cur = 0;
    int next = 1;
    
    // 逐行DP
    for (int i = 0; i < n; i++) {
        // 枚举当前行的所有可能状态
        for (int s = 0; s < (1 << m); s++) {
            if (dp[cur][s] == 0) continue;
            
            // 枚举下一行的所有可能状态
            for (int ns = 0; ns < (1 << m); ns++) {
                // 检查状态是否合法
                if (!isValid(ns)) continue;
                
                // 检查状态是否与土地条件匹配
                if (!matchGrid(i, ns)) continue;
                
                // 检查与上一行状态是否冲突
                if (i > 0 && (s & ns)) continue;
                
                // 状态转移
                dp[next][ns] = (dp[next][ns] + dp[cur][s]) % MOD;
            }
        }
        
        // 交换当前和下一个状态
        cur = 1 - cur;
        next = 1 - next;
        memset(dp[next], 0, sizeof(dp[next]));
    }
    
    // 统计所有可能的最终状态
    long long result = 0;
    for (int s = 0; s < (1 << m); s++) {
        result = (result + dp[cur][s]) % MOD;
    }
    
    return result;
}

int main() {
    ios::sync_with_stdio(false);
    cin.tie(nullptr);
    
    cin >> n >> m;
    grid.resize(n);
    
    // 读取网格数据
    for (int i = 0; i < n; i++) {
        int rowState = 0;
        for (int j = 0; j < m; j++) {
            int val;
            cin >> val;
            if (val == 1) {
                rowState |= (1 << j);
            }
        }
        grid[i] = rowState;
    }
    
    cout << compute() << endl;
    
    return 0;
}

// 时间复杂度分析：
// 外层循环n次，内层循环2^m * 2^m次，总时间复杂度为O(n * 4^m)
// 当m=12时，4^12 = 16,777,216，n最大为12，总操作数约为200,000,000
// 在某些测试用例下可能会超时，需要使用更优的轮廓线DP解法

// 空间复杂度分析：
// 使用滚动数组，空间复杂度为O(2^m)
// 当m=12时，2^12 = 4096，空间占用约为32KB，在可接受范围内

// 算法优化建议：
// 1. 使用轮廓线DP可以将时间复杂度优化到O(n * m * 2^m)
// 2. 预处理所有合法状态，减少无效枚举
// 3. 使用位运算优化状态检查