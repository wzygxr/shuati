// 威佐夫博弈 (Wythoff's Game)
// 有两堆各若干个物品，两个人轮流从某一堆或同时从两堆中取同样多的物品
// 规定每次至少取一个，多者不限，最后取光者得胜
// 
// 题目来源：
// 1. HDU 1527 取石子游戏 - http://acm.hdu.edu.cn/showproblem.php?pid=1527
// 2. 洛谷 P1290 欧几里得的游戏 - https://www.luogu.com.cn/problem/P1290
// 3. CodeForces 1371A Magical Sticks - https://codeforces.com/problemset/problem/1371/A
// 4. LeetCode LCP 30. 魔塔游戏 - https://leetcode-cn.com/problems/p0NxJO/
// 5. 牛客网 NC14520 取石子游戏 - https://ac.nowcoder.com/acm/problem/14520
// 
// 算法核心思想：
// 1. 威佐夫博弈的关键在于找到"奇异局势"（必败态）
// 2. 奇异局势满足：a = floor(k*(sqrt(5)+1)/2), b = a + k，其中k为非负整数
// 3. 当两堆石子数(a,b)满足奇异局势时，先手必败；否则先手必胜
// 
// 时间复杂度分析：
// O(1) - 只需常数时间计算黄金分割比和判断是否为奇异局势
// 
// 空间复杂度分析：
// O(1) - 只需几个变量存储中间结果
// 
// 工程化考量：
// 1. 异常处理：处理负数输入和边界情况
// 2. 精度控制：使用足够精度计算黄金分割比
// 3. 可读性：添加详细注释说明算法原理
// 4. 可扩展性：支持不同的输入格式和查询方式

#include <iostream>
#include <cmath>
#include <string>
#include <algorithm>

using namespace std;

// 黄金分割比 (sqrt(5)+1)/2
const double GOLDEN_RATIO = (sqrt(5) + 1) / 2;

/**
 * 判断两堆石子数(a,b)是否为威佐夫博弈的奇异局势
 * 
 * @param a 第一堆石子数
 * @param b 第二堆石子数
 * @return 如果是奇异局势返回true（先手必败），否则返回false（先手必胜）
 */
bool isLosingPosition(int a, int b) {
    // 异常处理：处理非法输入
    if (a < 0 || b < 0) {
        throw invalid_argument("石子数不能为负数");
    }
    
    // 确保a <= b
    if (a > b) {
        swap(a, b);
    }
    
    // 计算k值
    int k = b - a;
    
    // 计算理论上的a值
    int expectedA = (int) floor(k * GOLDEN_RATIO);
    
    // 判断实际a值是否等于理论值
    return a == expectedA;
}

/**
 * 威佐夫博弈的解题函数
 * 
 * @param a 第一堆石子数
 * @param b 第二堆石子数
 * @return 返回"先手必胜"或"先手必败"
 */
string solve(int a, int b) {
    // 异常处理：处理非法输入
    try {
        return isLosingPosition(a, b) ? "先手必败" : "先手必胜";
    } catch (const invalid_argument& e) {
        return string("输入错误: ") + e.what();
    }
}

/**
 * 找到获胜策略：如果存在必胜策略，返回应该如何取石子
 * 
 * @param a 第一堆石子数
 * @param b 第二堆石子数
 * @return 返回取石子的策略，如果是必败态返回"无法必胜"
 */
string findWinningMove(int a, int b) {
    // 异常处理：处理非法输入
    if (a < 0 || b < 0) {
        return "输入错误：石子数不能为负数";
    }
    
    // 如果已经是必败态，无法必胜
    if (isLosingPosition(a, b)) {
        return "无法必胜";
    }
    
    // 确保a <= b
    bool swapped = false;
    if (a > b) {
        swap(a, b);
        swapped = true;
    }
    
    // 尝试三种可能的取法：
    // 1. 从第一堆取x个
    // 2. 从第二堆取x个
    // 3. 从两堆同时取x个
    
    // 计算k值
    int k = b - a;
    int expectedA = (int) floor(k * GOLDEN_RATIO);
    
    // 计算需要取多少石子才能到达奇异局势
    if (a > expectedA) {
        // 方案1或3：从第一堆取或同时取
        int x = a - expectedA;
        if (swapped) {
            // 恢复原始顺序
            return "从第二堆取" + to_string(x) + "个石子";
        } else {
            return "从第一堆取" + to_string(x) + "个石子";
        }
    } else {
        // 方案2：从第二堆取
        int expectedB = expectedA + k;
        int x = b - expectedB;
        if (swapped) {
            // 恢复原始顺序
            return "从第一堆取" + to_string(x) + "个石子";
        } else {
            return "从第二堆取" + to_string(x) + "个石子";
        }
    }
}

// 主函数用于测试
int main() {
    cout << "威佐夫博弈求解器" << endl;
    cout << "输入两堆石子数a b (输入-1退出):" << endl;
    
    int a, b;
    while (true) {
        cin >> a;
        if (a == -1) break;
        cin >> b;
        
        cout << "结果: " << solve(a, b) << endl;
        cout << "策略: " << findWinningMove(a, b) << endl;
        cout << "\n输入下一组数据 (输入-1退出):" << endl;
    }
    
    return 0;
}