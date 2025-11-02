#include <iostream>
#include <vector>
#include <string>
#include <algorithm>
#include <sstream>
using namespace std;

/**
 * 多重背包问题的单调队列优化实现类
 * 
 * 技术要点：
 * 1. 实现二维DP和空间压缩一维DP两种方法
 * 2. 使用同余分组技术将背包容量分解
 * 3. 应用单调队列维护滑动窗口最大值
 * 4. 通过数学变形将O(n * t * c)优化为O(n * t)
 * 5. 采用高效的数组实现单调队列，避免对象创建开销
 */
class BoundedKnapsackWithMonotonicQueue {
private:
    /** 物品数量的最大可能值 */
    static constexpr int MAXN = 101;
    
    /** 背包容量的最大可能值 */
    static constexpr int MAXW = 40001;
    
    /** 物品价值数组 */
    vector<int> v;
    
    /** 物品重量数组 */
    vector<int> w;
    
    /** 物品数量数组 */
    vector<int> c;
    
    /** 动态规划数组 */
    vector<int> dp;
    
    /** 单调队列：存储容量索引 */
    vector<int> queue;
    
    /** 物品数量 */
    int n;
    
    /** 背包容量 */
    int t;
    
    /**
     * 二维DP中用于计算价值贡献的辅助方法
     * 
     * @param dp 二维DP数组
     * @param i 当前处理的物品编号
     * @param j 当前容量
     * @return 计算后的价值贡献：dp[i-1][j] - j/w[i] * v[i]
     */
    int value1(const vector<vector<int>>& dp, int i, int j) const {
        return dp[i - 1][j] - j / w[i] * v[i];
    }
    
    /**
     * 一维DP中用于计算价值贡献的辅助方法
     * 
     * @param i 当前处理的物品编号
     * @param j 当前容量
     * @return 计算后的价值贡献：dp[j] - j/w[i] * v[i]
     */
    int value2(int i, int j) const {
        return dp[j] - j / w[i] * v[i];
    }
    
    /**
     * 二维DP实现 + 单调队列优化枚举
     * 
     * 算法原理：
     * 1. 使用二维数组dp[i][j]表示前i个物品，容量为j时的最大价值
     * 2. 对每个物品，按模w[i]的余数分组处理
     * 3. 对每组内的元素，使用单调队列维护滑动窗口内的最优值
     * 4. 通过数学变形，将状态转移方程转换为可以应用单调队列的形式
     * 
     * 时间复杂度：O(n * t)
     * 空间复杂度：O(n * t)
     * 
     * @return 背包能够装下的最大价值
     */
    int compute1() {
        // 边界情况快速处理
        if (n == 0 || t == 0) {
            return 0;
        }
        
        // 初始化二维DP数组
        vector<vector<int>> dp(n + 1, vector<int>(t + 1, 0));
        
        // 遍历每个物品
        for (int i = 1; i <= n; i++) {
            int vi = v[i]; // 当前物品价值
            int wi = w[i]; // 当前物品重量
            int ci = c[i]; // 当前物品数量
            
            // 优化1：跳过数量为0的物品
            if (ci == 0) {
                dp[i] = dp[i-1]; // 复制上一行的数据
                continue;
            }
            
            // 优化2：跳过价值为0的物品（选了也不增加总价值）
            if (vi == 0) {
                dp[i] = dp[i-1]; // 复制上一行的数据
                continue;
            }
            
            // 优化3：跳过重量为0的物品（特殊情况处理）
            if (wi == 0) {
                // 重量为0的物品可以全部放入，但需要特殊处理
                dp[i] = dp[i-1]; // 复制上一行的数据
                continue;
            }
            
            // 优化4：跳过重量超过背包容量的物品
            if (wi > t) {
                dp[i] = dp[i-1]; // 复制上一行的数据
                continue;
            }
            
            // 优化5：调整物品数量上限，避免无意义的计算
            ci = min(ci, t / wi);
            
            // 同余分组处理：将容量j按模wi的余数分组
            for (int mod = 0; mod <= min(t, wi - 1); mod++) {
                int l = 0, r = 0; // 队列的头尾指针
                
                // 遍历同余类中的每个容量j = mod, mod+wi, mod+2*wi, ...
                for (int j = mod; j <= t; j += wi) {
                    // 维护单调队列：保证队列中的元素对应的value1值单调递减
                    while (l < r) {
                        int lastIdx = queue[r - 1];
                        if (value1(dp, i, lastIdx) <= value1(dp, i, j)) {
                            r--; // 弹出队尾元素，因为当前元素更优
                        } else {
                            break; // 队列保持单调递减
                        }
                    }
                    
                    // 将当前位置加入队列
                    queue[r++] = j;
                    
                    // 移除超出窗口大小的元素
                    while (l < r && queue[l] < j - ci * wi) {
                        l++; // 弹出队首元素，因为它已超出窗口范围
                    }
                    
                    // 确保队列非空
                    if (l < r) {
                        // 计算dp[i][j]
                        dp[i][j] = value1(dp, i, queue[l]) + j / wi * vi;
                    }
                    
                    // 确保dp[i][j]不小于dp[i-1][j]（不选当前物品的情况）
                    dp[i][j] = max(dp[i][j], dp[i-1][j]);
                }
            }
        }
        
        // 返回最终结果
        return dp[n][t];
    }
    
    /**
     * 空间压缩的动态规划 + 单调队列优化枚举
     * 
     * 算法特点：
     * 1. 使用一维数组dp[j]表示容量为j时的最大价值
     * 2. 从右向左遍历容量，确保使用的是上一轮的状态值
     * 3. 仍然使用同余分组和单调队列优化
     * 4. 比二维DP版本节省O(n*t)的空间
     * 
     * 时间复杂度：O(n * t)
     * 空间复杂度：O(t)
     * 
     * @return 背包能够装下的最大价值
     */
    int compute2() {
        // 边界情况快速处理
        if (n == 0 || t == 0) {
            return 0;
        }
        
        // 遍历每个物品
        for (int i = 1; i <= n; i++) {
            int vi = v[i]; // 当前物品价值
            int wi = w[i]; // 当前物品重量
            int ci = c[i]; // 当前物品数量
            
            // 优化1：跳过数量为0的物品
            if (ci == 0) {
                continue;
            }
            
            // 优化2：跳过价值为0的物品
            if (vi == 0) {
                continue;
            }
            
            // 优化3：跳过重量为0的物品
            if (wi == 0) {
                continue;
            }
            
            // 优化4：跳过重量超过背包容量的物品
            if (wi > t) {
                continue;
            }
            
            // 优化5：调整物品数量上限
            ci = min(ci, t / wi);
            
            // 同余分组处理
            for (int mod = 0; mod <= min(t, wi - 1); mod++) {
                int l = 0, r = 0; // 队列的头尾指针
                
                // 先把ci个的指标进入单调队列
                // 从最大的容量开始向左处理
                for (int j = t - mod, cnt = 1; j >= 0 && cnt <= ci; j -= wi, cnt++) {
                    // 维护单调队列，保证队列中的元素对应的value2值单调递减
                    while (l < r) {
                        int lastIdx = queue[r - 1];
                        if (value2(i, lastIdx) <= value2(i, j)) {
                            r--;
                        } else {
                            break;
                        }
                    }
                    queue[r++] = j;
                }
                
                // 滑动窗口计算每个位置的dp值
                for (int j = t - mod, enter = j - wi * ci; j >= 0; j -= wi, enter -= wi) {
                    // 窗口进入enter位置的指标（如果enter有效）
                    if (enter >= 0) {
                        while (l < r) {
                            int lastIdx = queue[r - 1];
                            if (value2(i, lastIdx) <= value2(i, enter)) {
                                r--;
                            } else {
                                break;
                            }
                        }
                        queue[r++] = enter;
                    }
                    
                    // 移除队列头部超出窗口范围的元素
                    while (l < r && queue[l] < j - ci * wi) {
                        l++;
                    }
                    
                    // 计算当前位置的dp值
                    if (l < r) {
                        int candidate = value2(i, queue[l]) + j / wi * vi;
                        if (candidate > dp[j]) {
                            dp[j] = candidate;
                        }
                    }
                }
            }
        }
        
        // 返回最终结果
        return dp[t];
    }
    
    /**
     * 解析一行输入为整数数组
     * 
     * @param line 输入的一行字符串
     * @return 解析后的整数数组
     */
    vector<int> parseLine(const string& line) {
        vector<int> result;
        istringstream iss(line);
        int num;
        while (iss >> num) {
            result.push_back(num);
        }
        return result;
    }
    
public:
    BoundedKnapsackWithMonotonicQueue() : 
        v(MAXN, 0), 
        w(MAXN, 0), 
        c(MAXN, 0), 
        dp(MAXW, 0), 
        queue(MAXW, 0), 
        n(0), 
        t(0) {}
    
    /**
     * 运行程序的主方法
     */
    void run() {
        ios::sync_with_stdio(false);
        cin.tie(nullptr);
        
        string line;
        while (getline(cin, line)) {
            // 跳过空行
            if (line.empty()) continue;
            
            vector<int> firstLine = parseLine(line);
            if (firstLine.size() < 2) continue;
            
            n = firstLine[0];
            t = firstLine[1];
            
            // 初始化dp数组为0
            fill(dp.begin(), dp.begin() + t + 1, 0);
            
            // 读取每个物品的价值、重量和数量
            for (int i = 1; i <= n; i++) {
                while (getline(cin, line) && line.empty()); // 跳过空行
                vector<int> itemData = parseLine(line);
                if (itemData.size() >= 3) {
                    v[i] = itemData[0];
                    w[i] = itemData[1];
                    c[i] = itemData[2];
                }
            }
            
            // 边界情况快速处理
            if (n == 0 || t == 0) {
                cout << 0 << endl;
                continue;
            }
            
            // 调用空间优化的单调队列实现，输出结果
            cout << compute2() << endl;
        }
    }
    
    /**
     * 单调队列优化多重背包问题的数学原理详解
     * 
     * 1. 朴素多重背包状态转移方程：
     *    dp[i][j] = max{ dp[i-1][j-k*w[i]] + k*v[i] }, 0 ≤ k ≤ min(c[i], j/w[i])
     * 
     * 2. 同余分组思想：
     *    对于容量j，我们可以将其表示为j = m*w[i] + r，其中0 ≤ r < w[i]
     *    这样，所有容量可以按照余数r分成w[i]个组
     *    每组内的容量形式为r, r+w[i], r+2*w[i], ...
     * 
     * 3. 状态转移方程的数学变形：
     *    对于j = m*w[i] + r，考虑k个物品i的选择：
     *    dp[i][m*w[i]+r] = max{ dp[i-1][(m-k)*w[i]+r] + k*v[i] }, 0 ≤ k ≤ min(c[i], m)
     *    
     *    令l = m - k，则k = m - l，此时：
     *    dp[i][m*w[i]+r] = max{ dp[i-1][l*w[i]+r] + (m-l)*v[i] }, max(0, m-c[i]) ≤ l ≤ m
     *    
     *    进一步变形：
     *    dp[i][m*w[i]+r] = max{ dp[i-1][l*w[i]+r] - l*v[i] } + m*v[i]
     *    
     *    令f(l) = dp[i-1][l*w[i]+r] - l*v[i]
     *    则dp[i][m*w[i]+r] = max{ f(l) } + m*v[i]
     */
    
    /**
     * 与Java版本的差异：
     * 1. 使用vector替代数组，提供更好的内存管理
     * 2. 输入处理使用cin和getline，而非Java的BufferedReader
     * 3. 使用fill函数替代Arrays.fill
     * 4. 在compute1中使用vector的赋值操作替代System.arraycopy
     * 5. 使用引用传递dp数组以避免复制开销
     * 6. 使用ios::sync_with_stdio(false)和cin.tie(nullptr)加速输入输出
     */
};

/**
 * 主函数
 */
int main() {
    BoundedKnapsackWithMonotonicQueue solution;
    solution.run();
    return 0;
}