#include <iostream>
#include <algorithm>
#include <cmath>
#include <cstdio>
using namespace std;

/**
 * 01分数规划问题 - 牛群的才艺展示（Talent Show）
 * 
 * 题目信息：
 *  - 题目来源：Luogu P4377，USACO 2018 Open Contest
 *  - 题目描述：有n头奶牛，每头奶牛有重量和才艺两个属性值。要求选若干头牛，使得总重量不少于w，
 *    并且选出的牛的才艺的和与重量的和的比值尽量大。返回该比值乘以1000的整数结果，小数部分舍弃。
 *  - 数据范围：
 *    - 1 <= n <= 250
 *    - 1 <= w <= 1000
 *    - 1 <= 牛的重量 <= 10^6
 *    - 1 <= 牛的才艺 <= 10^3
 *  - 测试链接：https://www.luogu.com.cn/problem/P4377
 *  
 * 补充题目：
 * 1. USACO 2018 Open Contest - Talent Show
 * 2. POJ 3621 - Best Cow Line
 * 3. HDU 4787 - GRE Words Revenge
 * 4. Codeforces 489E - Hiking
 * 5. 洛谷 P1642 - 规划
 * 
 * 算法思路：使用二分法求解01分数规划问题，结合01背包动态规划进行可行性判断
 * 
 * 01分数规划的数学原理：
 * 我们需要最大化 R = (sum(talent_i * x_i)) / (sum(weight_i * x_i))，其中x_i∈{0,1}且sum(weight_i * x_i) >= W
 * 对于给定的L，判断是否存在x的选择使得 R > L
 * 等价于：sum(talent_i * x_i) > L * sum(weight_i * x_i)
 * 等价于：sum((talent_i - L * weight_i) * x_i) > 0，且sum(weight_i * x_i) >= W
 * 
 * 时间复杂度：O(n * W * log(1/ε))，其中ε是精度要求（本题中取1e-6）
 * 空间复杂度：O(W)，使用滚动数组优化动态规划空间
 */

// 常量定义
const int MAXN = 251; // 最大奶牛数量
const int MAXW = 1001; // 最大目标重量W

// 足够小代表无效解（无法达到的状态）
const double NA = -1e9;

// 精度控制，用于二分结束条件
const double PRECISION = 1e-6;

// 重量数组
int weight[MAXN];

// 才艺数组
int talent[MAXN];

// (才艺 - x * 重量)的结余，用于01分数规划判断
double value[MAXN];

/**
 * dp[j]表示在前i头牛中选择若干头，总重量为j时的最大结余和
 * 特别地，dp[w]表示总重量>=w时的最大结余和
 * 采用空间压缩的方式实现，仅用一维数组
 */
double dp[MAXW];

// 全局变量，存储当前数据规模和目标重量
int n, w; // n表示奶牛数量，w表示目标最小总重量

/**
 * 检查函数：判断给定的比率值x是否可行
 * 
 * 核心思想：将01分数规划问题转化为01背包问题。对于当前比率x，计算每个奶牛的结余值（talent_i - x * weight_i），
 * 然后求解总重量至少为w的情况下，能否使总结余值大于等于0。
 * 
 * 动态规划设计：
 * - 状态定义：dp[j]表示总重量为j时的最大结余和
 * - 状态转移：对于每头牛，有选或不选两种选择
 * - 特殊处理：当总重量超过w时，统一记录在dp[w]中
 * 
 * @param x 当前尝试的比率值
 * @return 如果存在一种选择使得比值大于x，则返回true；否则返回false
 */
bool check(double x) {
    // 计算每头牛的value值（才艺值减去x倍的重量值）
    for (int i = 1; i <= n; i++) {
        value[i] = static_cast<double>(talent[i]) - x * weight[i];
    }
    
    // 初始化dp数组
    dp[0] = 0.0; // 初始状态：重量为0时，结余和为0
    for (int i = 1; i <= w; i++) {
        dp[i] = NA; // 其余状态初始化为无效值
    }
    
    // 01背包动态规划过程
    for (int i = 1; i <= n; i++) { // 遍历每头牛
        // 倒序遍历重量，避免重复选择同一头牛
        for (int p = w; p >= 0; p--) {
            if (dp[p] != NA) { // 如果当前状态有效（可达）
                int j = p + weight[i]; // 计算选择当前牛后的总重量
                
                // 两种情况处理：
                // 1. 如果总重量超过或等于目标重量w，统一更新dp[w]
                // 2. 否则更新对应重量的dp值
                if (j >= w) {
                    dp[w] = max(dp[w], dp[p] + value[i]);
                } else {
                    dp[j] = max(dp[j], dp[p] + value[i]);
                }
            }
        }
    }
    
    // 判断条件：如果总重量>=w时的最大结余和>=0，说明存在更优的解
    return dp[w] >= 0;
}

/**
 * 主函数：处理输入输出，执行二分查找算法
 * 
 * 算法流程：
 * 1. 读取输入数据
 * 2. 初始化二分查找的左右边界
 * 3. 进行二分查找，每次调用check函数判断当前比率是否可行
 * 4. 输出结果，将最优比值乘以1000后取整数部分
 */
int main() {
    // 读取输入数据
    scanf("%d %d", &n, &w);
    
    for (int i = 1; i <= n; i++) {
        scanf("%d %d", &weight[i], &talent[i]);
    }
    
    // 初始化二分查找的左右边界
    // 左边界为0，右边界为所有才艺值的和（最大可能比值的上界）
    double left = 0.0, right = 0.0;
    for (int i = 1; i <= n; i++) {
        right += talent[i];
    }
    
    double result = 0.0;
    // 二分查找过程
    // 当左右边界的差大于精度要求时继续循环
    while (left < right && right - left >= PRECISION) {
        double mid = (left + right) / 2.0; // 取中点作为当前尝试的比率值
        if (check(mid)) { // 如果mid可行，说明可以尝试更大的值
            result = mid; // 记录当前最优解
            left = mid + PRECISION; // 左边界右移
        } else { // 如果mid不可行，需要尝试更小的值
            right = mid - PRECISION; // 右边界左移
        }
    }
    
    // 输出结果，将比值乘以1000后取整数部分（向下取整）
    printf("%d\n", static_cast<int>(result * 1000));
    
    return 0;
}