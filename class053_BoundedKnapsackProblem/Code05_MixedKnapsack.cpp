#include <iostream>
#include <vector>
#include <algorithm>
#include <cstring>
using namespace std;

/**
 * 混合背包问题 - C++实现
 * 
 * 问题描述：
 * 混合背包问题是01背包、完全背包和多重背包的混合体
 * 每种物品可能是01背包物品（只能选0或1次）、完全背包物品（可选任意次）或多重背包物品（有数量限制）
 * 要求在不超过背包容量的前提下，选择物品使得总价值最大
 * 
 * 算法分类：动态规划 - 混合背包问题
 * 
 * 算法原理：
 * 1. 根据物品类型选择不同的处理策略
 * 2. 01背包：逆序遍历容量
 * 3. 完全背包：正序遍历容量  
 * 4. 多重背包：使用二进制优化或单调队列优化
 * 5. 统一使用一维DP数组进行状态转移
 * 
 * 时间复杂度：O(n * V * log(max_count)) 或 O(n * V)
 * 空间复杂度：O(V)
 * 
 * 测试链接：http://poj.org/problem?id=1742（混合背包的可行性问题）
 */

const int MAXN = 101;
const int MAXV = 100001;

int n, V;
int values[MAXN], weights[MAXN], types[MAXN], counts[MAXN];
bool dp[MAXV];

/**
 * 混合背包问题的可行性判断实现
 * 
 * 算法思路：
 * 1. 初始化dp数组，dp[0] = true表示容量0可达
 * 2. 根据物品类型选择不同的处理方式：
 *    - 01背包：逆序遍历容量
 *    - 完全背包：正序遍历容量
 *    - 多重背包：使用二进制优化或滑动窗口优化
 * 3. 统计可达的容量数量
 * 
 * 时间复杂度分析：
 * 最坏情况下O(n * V)，使用优化后可以降低
 * 
 * 空间复杂度分析：
 * O(V)，使用一维数组
 * 
 * @return 可达的容量数量
 */
int mixedKnapsack() {
    // 初始化dp数组
    memset(dp, false, sizeof(dp));
    dp[0] = true;
    
    // 遍历每个物品
    for (int i = 0; i < n; i++) {
        int w = weights[i];
        int type = types[i];
        int cnt = counts[i];
        
        // 根据物品类型选择处理方式
        if (type == 0) { // 01背包
            for (int j = V; j >= w; j--) {
                if (dp[j - w]) {
                    dp[j] = true;
                }
            }
        } else if (type == 1) { // 完全背包
            for (int j = w; j <= V; j++) {
                if (dp[j - w]) {
                    dp[j] = true;
                }
            }
        } else { // 多重背包，使用二进制优化
            // 二进制分组
            for (int k = 1; k <= cnt; k <<= 1) {
                int group_w = k * w;
                
                for (int j = V; j >= group_w; j--) {
                    if (dp[j - group_w]) {
                        dp[j] = true;
                    }
                }
                cnt -= k;
            }
            
            // 处理剩余部分
            if (cnt > 0) {
                int group_w = cnt * w;
                
                for (int j = V; j >= group_w; j--) {
                    if (dp[j - group_w]) {
                        dp[j] = true;
                    }
                }
            }
        }
    }
    
    // 统计可达容量数量
    int result = 0;
    for (int i = 1; i <= V; i++) {
        if (dp[i]) {
            result++;
        }
    }
    
    return result;
}

int main() {
    ios::sync_with_stdio(false);
    cin.tie(nullptr);
    
    // 读取输入
    while (cin >> n >> V) {
        if (n == 0 && V == 0) break;
        
        for (int i = 0; i < n; i++) {
            cin >> values[i] >> weights[i] >> types[i];
            if (types[i] == 2) { // 多重背包需要额外读取数量
                cin >> counts[i];
            } else {
                counts[i] = 1; // 01背包和完全背包数量为1
            }
        }
        
        // 计算并输出结果
        cout << mixedKnapsack() << endl;
    }
    
    return 0;
}

/*
 * 算法详解与原理解析
 * 
 * 1. 混合背包问题特点：
 *    - 包含多种类型的物品：01背包、完全背包、多重背包
 *    - 需要根据物品类型选择不同的状态转移策略
 *    - 统一使用一维DP数组，但遍历顺序不同
 * 
 * 2. 处理策略选择：
 *    - 01背包：逆序遍历容量，确保每个物品只被选择一次
 *    - 完全背包：正序遍历容量，允许物品被多次选择
 *    - 多重背包：使用二进制优化转化为01背包，然后逆序遍历
 * 
 * 3. 二进制优化原理：
 *    - 将数量为c的物品拆分为1,2,4,...,2^k,c-2^k个组合物品
 *    - 这样可以用log(c)个物品表示原物品的所有选择可能
 *    - 将多重背包问题转化为01背包问题
 */

/*
 * 工程化考量与代码优化
 * 
 * 1. 内存优化：
 *    - 使用一维数组替代二维数组
 *    - 对于大规模数据，可以考虑使用bitset进一步压缩空间
 * 
 * 2. 性能优化：
 *    - 根据物品类型选择最优的处理策略
 *    - 对于多重背包，使用二进制优化减少状态转移次数
 *    - 提前剪枝，跳过无法产生影响的物品
 * 
 * 3. 代码健壮性：
 *    - 处理边界情况（n=0, V=0等）
 *    - 验证输入数据的合法性
 *    - 使用合适的数据类型防止溢出
 */

/*
 * 相关题目扩展
 * 
 * 1. POJ 1742. Coins - http://poj.org/problem?id=1742
 *    多重背包可行性问题，计算能组成多少种金额
 * 
 * 2. POJ 1276. Cash Machine - http://poj.org/problem?id=1276
 *    多重背包优化问题，使用二进制优化或单调队列优化
 * 
 * 3. 洛谷 P1833. 樱花 - https://www.luogu.com.cn/problem/P1833
 *    混合背包问题，包含01背包、完全背包和多重背包
 * 
 * 4. HDU 3449. Consumer - http://acm.hdu.edu.cn/showproblem.php?pid=3449
 *    有依赖的背包问题，需要先购买主件
 */

/*
 * 调试与测试建议
 * 
 * 1. 分类测试：
 *    - 分别测试只包含01背包、完全背包、多重背包的情况
 *    - 测试混合不同类型物品的情况
 * 
 * 2. 边界测试：
 *    - 测试n=0或V=0的情况
 *    - 测试所有物品重量都大于V的情况
 *    - 测试存在数量为0的物品的情况
 * 
 * 3. 性能测试：
 *    - 对于大规模数据，测试不同优化方法的效果
 *    - 比较二进制优化和单调队列优化的性能差异
 */
