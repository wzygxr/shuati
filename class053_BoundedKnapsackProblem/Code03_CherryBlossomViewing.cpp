#include <iostream>
#include <vector>
#include <algorithm>
#include <cstring>
using namespace std;

/**
 * 樱花观赏问题 - C++实现
 * 洛谷 P1833 樱花问题的变种
 * 
 * 问题描述：
 * 混合背包问题的实际应用场景
 * 需要在有限时间内观赏不同种类的樱花，每种樱花有：
 * - 观赏时间（重量）
 * - 观赏价值（价值）  
 * - 观赏次数限制（数量）
 * 要求在不超过总时间的前提下，最大化观赏价值
 * 
 * 算法分类：动态规划 - 混合背包问题
 * 
 * 算法原理：
 * 1. 将樱花观赏问题建模为混合背包问题
 * 2. 根据观赏次数限制选择不同的处理策略：
 *    - 只能观赏一次：01背包
 *    - 可以无限观赏：完全背包
 *    - 有次数限制：多重背包
 * 3. 使用二进制优化处理多重背包部分
 * 
 * 时间复杂度：O(n * T * log(max_count))
 * 空间复杂度：O(T)
 * 
 * 测试链接：https://www.luogu.com.cn/problem/P1833（樱花问题）
 */

const int MAXN = 10001;
const int MAXT = 1001;

int n, T;
int times[MAXN], values[MAXN], types[MAXN], counts[MAXN];
int dp[MAXT];

/**
 * 樱花观赏问题的最大值求解实现
 * 
 * 算法思路：
 * 1. 初始化dp数组为0
 * 2. 根据樱花类型选择不同的处理方式：
 *    - 01樱花：逆序遍历时间
 *    - 完全樱花：正序遍历时间
 *    - 多重樱花：使用二进制优化
 * 3. 返回最大观赏价值
 * 
 * 时间复杂度分析：
 * O(n * T * log(max_count))，使用优化后效率较高
 * 
 * 空间复杂度分析：
 * O(T)，使用一维数组
 * 
 * @return 最大观赏价值
 */
int cherryBlossomViewing() {
    // 初始化dp数组
    memset(dp, 0, sizeof(dp));
    
    // 遍历每种樱花
    for (int i = 0; i < n; i++) {
        int t = times[i];
        int v = values[i];
        int type = types[i];
        int cnt = counts[i];
        
        // 优化：跳过观赏时间为0的樱花（特殊情况）
        if (t == 0) {
            continue;
        }
        
        // 优化：跳过观赏时间超过总时间的樱花
        if (t > T) {
            continue;
        }
        
        // 根据樱花类型选择处理方式
        if (type == 0) { // 01樱花（只能观赏一次）
            for (int j = T; j >= t; j--) {
                dp[j] = max(dp[j], dp[j - t] + v);
            }
        } else if (type == 1) { // 完全樱花（可以无限观赏）
            for (int j = t; j <= T; j++) {
                dp[j] = max(dp[j], dp[j - t] + v);
            }
        } else { // 多重樱花（有观赏次数限制）
            // 二进制优化
            int remaining = cnt;
            for (int k = 1; k <= remaining; k <<= 1) {
                int group_t = k * t;
                int group_v = k * v;
                
                for (int j = T; j >= group_t; j--) {
                    dp[j] = max(dp[j], dp[j - group_t] + group_v);
                }
                remaining -= k;
            }
            
            // 处理剩余部分
            if (remaining > 0) {
                int group_t = remaining * t;
                int group_v = remaining * v;
                
                for (int j = T; j >= group_t; j--) {
                    dp[j] = max(dp[j], dp[j - group_t] + group_v);
                }
            }
        }
    }
    
    return dp[T];
}

int main() {
    ios::sync_with_stdio(false);
    cin.tie(nullptr);
    
    // 读取输入
    cin >> T >> n;
    
    for (int i = 0; i < n; i++) {
        cin >> times[i] >> values[i] >> types[i];
        if (types[i] == 2) { // 多重樱花需要额外读取观赏次数
            cin >> counts[i];
        } else {
            counts[i] = 1; // 01樱花和完全樱花次数为1
        }
    }
    
    // 计算并输出最大观赏价值
    cout << cherryBlossomViewing() << endl;
    
    return 0;
}

/*
 * 算法详解与原理解析
 * 
 * 1. 问题建模：
 *    - 将樱花观赏时间视为背包容量（重量）
 *    - 将樱花观赏价值视为物品价值
 *    - 将观赏次数限制视为物品数量限制
 *    - 问题转化为在时间限制下最大化观赏价值
 * 
 * 2. 混合背包处理：
 *    - 01樱花：对应只能观赏一次的樱花
 *    - 完全樱花：对应可以反复观赏的樱花
 *    - 多重樱花：对应有观赏次数限制的樱花
 * 
 * 3. 二进制优化：
 *    - 将多重樱花转化为多个01樱花组合
 *    - 减少状态转移的次数，提高算法效率
 *    - 数学原理：任何正整数都可以表示为2的幂次之和
 */

/*
 * 工程化考量与代码优化
 * 
 * 1. 性能优化：
 *    - 使用二进制优化处理多重背包
 *    - 提前剪枝，跳过无效的樱花类型
 *    - 使用局部变量缓存频繁访问的值
 * 
 * 2. 内存优化：
 *    - 使用一维数组替代二维数组
 *    - 对于大规模数据，可以考虑使用滚动数组
 * 
 * 3. 代码健壮性：
 *    - 处理边界情况（T=0, n=0等）
 *    - 验证输入数据的合法性
 *    - 使用合适的数据类型防止溢出
 */

/*
 * 相关题目扩展
 * 
 * 1. 洛谷 P1833. 樱花 - https://www.luogu.com.cn/problem/P1833
 *    经典的混合背包问题，实际应用场景
 * 
 * 2. POJ 1742. Coins - http://poj.org/problem?id=1742
 *    多重背包可行性问题
 * 
 * 3. HDU 2191. 悼念512汶川大地震遇难同胞 - http://acm.hdu.edu.cn/showproblem.php?pid=2191
 *    多重背包问题的经典应用
 * 
 * 4. Codeforces 106C. Buns - https://codeforces.com/problemset/problem/106/C
 *    分组背包与多重背包的混合应用
 */

/*
 * 调试与测试建议
 * 
 * 1. 功能测试：
 *    - 测试只包含一种樱花类型的情况
 *    - 测试混合多种樱花类型的情况
 *    - 测试边界情况（所有樱花时间都大于T）
 * 
 * 2. 性能测试：
 *    - 测试大规模数据下的运行时间
 *    - 比较不同优化方法的效果
 * 
 * 3. 正确性验证：
 *    - 使用小数据手动计算验证
 *    - 与标准答案进行对比
 */