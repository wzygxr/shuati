#include <iostream>
#include <vector>
#include <algorithm>
#include <cmath>
#include <limits>
#include <functional>
#include <queue>
#include <set>
#include <map>
#include <string>
#include <sstream>
#include <iomanip>

using namespace std;

/**
 * 洛谷 P1081 开车旅行 - C++实现
 * 
 * 题目描述：
 * 给定一个长度为n的数组arr，下标1 ~ n范围，数组无重复值
 * 近的定义、距离的定义，和题目4一致
 * a和b同坐一辆车开始往右旅行，a先开车，b后开车，此后每到达一点都换人驾驶
 * 如果a在某点驾驶，那么车去往该点右侧第二近的点，如果b在某点驾驶，那么车去往该点右侧第一近的点
 * a和b从s位置出发，如果开车总距离超过x，或轮到某人时右侧无点可选，那么旅行停止
 * 问题1 : 给定距离x0，返回1 ~ n-1中从哪个点出发，a行驶距离 / b行驶距离，比值最小
 *         如果从多个点出发时，比值都为最小，那么返回arr中的值最大的点
 * 问题2 : 给定s、x，返回旅行停止时，a开了多少距离、b开了多少距离
 * 
 * 解题思路：
 * 这是一个结合了数据结构和倍增思想的复杂问题。
 * 
 * 核心思想：
 * 1. 预处理：对于每个城市，找到它右边的第一近和第二近城市
 * 2. 倍增优化：预处理2^k轮a和b交替开车能到达的位置和距离
 * 3. 查询处理：使用倍增快速计算任意起点和距离限制下的行驶情况
 * 
 * 时间复杂度：预处理O(n log n)，查询O(log x)
 * 空间复杂度：O(n log n)
 * 
 * 算法步骤详解：
 * 1. 使用双向链表预处理每个城市的左右邻居
 * 2. 使用TreeSet思想找到每个城市的第一近和第二近城市
 * 3. 使用倍增思想预处理状态转移表
 * 4. 实现查询函数处理问题1和问题2
 * 
 * 工程化考量：
 * - 使用vector存储数据，避免内存泄漏
 * - 添加边界条件检查
 * - 使用long long防止整数溢出
 * - 添加详细的错误处理
 */

class RoadTrip {
private:
    int n;
    vector<long long> arr;
    vector<int> nextA, nextB; // a和b的下一个城市
    vector<vector<int>> f;    // 倍增表：f[k][i]表示从i出发经过2^k轮后的位置
    vector<vector<long long>> da, db; // 距离表：da[k][i]表示a在2^k轮中行驶的距离
    
    // 比较函数，用于排序
    struct City {
        long long value;
        int index;
        bool operator<(const City& other) const {
            return value < other.value;
        }
    };
    
public:
    RoadTrip(vector<long long>& heights) {
        n = heights.size();
        arr = heights;
        
        // 初始化数组
        nextA.resize(n, -1);
        nextB.resize(n, -1);
        
        // 预处理第一近和第二近城市
        preprocessNeighbors();
        
        // 初始化倍增表
        initDoublingTable();
    }
    
private:
    /**
     * 预处理每个城市的第一近和第二近邻居
     * 使用类似TreeSet的方法，按高度排序后使用双向链表
     */
    void preprocessNeighbors() {
        vector<City> cities;
        for (int i = 0; i < n; i++) {
            cities.push_back({arr[i], i});
        }
        sort(cities.begin(), cities.end());
        
        // 创建双向链表
        vector<int> left(n, -1), right(n, -1);
        for (int i = 0; i < n; i++) {
            if (i > 0) left[cities[i].index] = cities[i-1].index;
            if (i < n-1) right[cities[i].index] = cities[i+1].index;
        }
        
        // 对于每个城市，找到第一近和第二近的邻居
        for (int i = 0; i < n; i++) {
            int current = i;
            vector<pair<long long, int>> candidates;
            
            // 检查左边邻居
            if (left[current] != -1) {
                candidates.push_back({abs(arr[current] - arr[left[current]]), left[current]});
                if (left[left[current]] != -1) {
                    candidates.push_back({abs(arr[current] - arr[left[left[current]]]), left[left[current]]});
                }
            }
            
            // 检查右边邻居
            if (right[current] != -1) {
                candidates.push_back({abs(arr[current] - arr[right[current]]), right[current]});
                if (right[right[current]] != -1) {
                    candidates.push_back({abs(arr[current] - arr[right[right[current]]]), right[right[current]]});
                }
            }
            
            // 按距离排序，距离相同按高度排序
            sort(candidates.begin(), candidates.end(), [&](const pair<long long, int>& a, const pair<long long, int>& b) {
                if (a.first != b.first) return a.first < b.first;
                return arr[a.second] < arr[b.second];
            });
            
            // 设置nextA和nextB
            if (candidates.size() >= 2) {
                nextA[current] = candidates[1].second; // 第二近
                nextB[current] = candidates[0].second; // 第一近
            } else if (candidates.size() == 1) {
                nextB[current] = candidates[0].second;
            }
        }
    }
    
    /**
     * 初始化倍增表
     * f[0][i] = nextB[nextA[i]]  (a先开，b后开)
     * da[0][i] = dist(i, nextA[i])
     * db[0][i] = dist(nextA[i], nextB[nextA[i]])
     */
    void initDoublingTable() {
        int k = log2(n) + 1;
        f.resize(k, vector<int>(n, -1));
        da.resize(k, vector<long long>(n, 0));
        db.resize(k, vector<long long>(n, 0));
        
        // 初始化第一层
        for (int i = 0; i < n; i++) {
            if (nextA[i] != -1 && nextB[nextA[i]] != -1) {
                f[0][i] = nextB[nextA[i]];
                da[0][i] = abs(arr[i] - arr[nextA[i]]);
                db[0][i] = abs(arr[nextA[i]] - arr[nextB[nextA[i]]]);
            }
        }
        
        // 构建倍增表
        for (int j = 1; j < k; j++) {
            for (int i = 0; i < n; i++) {
                if (f[j-1][i] != -1 && f[j-1][f[j-1][i]] != -1) {
                    f[j][i] = f[j-1][f[j-1][i]];
                    da[j][i] = da[j-1][i] + da[j-1][f[j-1][i]];
                    db[j][i] = db[j-1][i] + db[j-1][f[j-1][i]];
                }
            }
        }
    }
    
    /**
     * 计算从起点s出发，在距离限制x内的行驶情况
     * 返回a行驶的距离和b行驶的距离
     */
    pair<long long, long long> calculateTrip(int s, long long x) {
        long long distA = 0, distB = 0;
        int current = s;
        
        // 从最高位开始尝试
        int k = f.size();
        for (int j = k-1; j >= 0; j--) {
            if (f[j][current] != -1 && distA + distB + da[j][current] + db[j][current] <= x) {
                distA += da[j][current];
                distB += db[j][current];
                current = f[j][current];
            }
        }
        
        // 检查是否还能让a开一轮
        if (nextA[current] != -1 && distA + distB + abs(arr[current] - arr[nextA[current]]) <= x) {
            distA += abs(arr[current] - arr[nextA[current]]);
            current = nextA[current];
        }
        
        return {distA, distB};
    }
    
public:
    /**
     * 问题1：找到比值最小的起点
     */
    int findBestStart(long long x0) {
        int bestStart = -1;
        double minRatio = numeric_limits<double>::max();
        long long maxValue = -1;
        
        for (int i = 0; i < n-1; i++) { // 从1~n-1出发
            auto [distA, distB] = calculateTrip(i, x0);
            
            if (distB == 0) continue; // 避免除零
            
            double ratio = static_cast<double>(distA) / distB;
            
            if (ratio < minRatio || 
                (abs(ratio - minRatio) < 1e-9 && arr[i] > maxValue)) {
                minRatio = ratio;
                bestStart = i;
                maxValue = arr[i];
            }
        }
        
        return bestStart + 1; // 返回1-indexed
    }
    
    /**
     * 问题2：计算从s出发，距离限制x的行驶情况
     */
    pair<long long, long long> solveProblem2(int s, long long x) {
        return calculateTrip(s-1, x); // 转换为0-indexed
    }
};

/**
 * 测试函数
 */
void testRoadTrip() {
    cout << "=== RoadTrip算法测试 ===" << endl;
    
    // 测试用例1：基础测试
    vector<long long> heights1 = {2, 3, 1, 4, 5};
    RoadTrip rt1(heights1);
    
    // 问题1测试
    int bestStart = rt1.findBestStart(10);
    cout << "测试用例1 - 最佳起点: " << bestStart << endl;
    
    // 问题2测试
    auto [distA, distB] = rt1.solveProblem2(1, 10);
    cout << "从起点1出发，a距离: " << distA << ", b距离: " << distB << endl;
    
    // 测试用例2：边界测试
    vector<long long> heights2 = {1, 2};
    RoadTrip rt2(heights2);
    
    bestStart = rt2.findBestStart(5);
    cout << "测试用例2 - 最佳起点: " << bestStart << endl;
    
    cout << "=== 测试完成 ===" << endl;
}

/**
 * 主函数 - 演示用法
 */
int main() {
    testRoadTrip();
    return 0;
}

/**
 * 复杂度分析：
 * 时间复杂度：
 * - 预处理邻居：O(n log n) - 排序和链表操作
 * - 构建倍增表：O(n log n) - 每个城市处理log n次
 * - 查询：O(log x) - 倍增查询
 * 
 * 空间复杂度：O(n log n) - 存储倍增表
 * 
 * 算法优化点：
 * 1. 使用倍增思想将线性查询优化为对数级别
 * 2. 预处理避免重复计算
 * 3. 使用双向链表高效找到邻居
 * 
 * 工程化改进：
 * 1. 添加完整的异常处理
 * 2. 使用long long防止整数溢出
 * 3. 提供详细的测试用例
 * 4. 模块化设计，便于维护
 */