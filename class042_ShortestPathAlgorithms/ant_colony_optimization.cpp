/**
 * 蚁群算法 (Ant Colony Optimization, ACO)
 * 
 * 算法原理：
 * 蚁群算法是一种模拟蚂蚁觅食行为的群智能优化算法。
 * 蚂蚁在寻找食物时会在路径上释放信息素，其他蚂蚁能够感知信息素浓度，
 * 并倾向于选择信息素浓度高的路径，形成正反馈机制，最终找到最优路径。
 * 
 * 算法特点：
 * 1. 属于群智能算法，适用于解决组合优化问题
 * 2. 基于分布式计算，具有良好的并行性
 * 3. 正反馈机制使算法能够快速收敛
 * 4. 适用于解决TSP、VRP等路径优化问题
 * 
 * 应用场景：
 * - 旅行商问题(TSP)
 * - 车辆路径问题(VRP)
 * - 网络路由优化
 * - 作业车间调度问题
 * - 图着色问题
 * 
 * 算法流程：
 * 1. 初始化参数和信息素矩阵
 * 2. 循环迭代：
 *    a. 每只蚂蚁根据状态转移规则构建解
 *    b. 计算每只蚂蚁的路径长度
 *    c. 更新全局最优解
 *    d. 根据信息素更新规则更新信息素矩阵
 * 3. 直到满足终止条件
 * 
 * 时间复杂度：O(G×M×N²)，G为迭代次数，M为蚂蚁数量，N为城市数量
 * 空间复杂度：O(N²)，存储距离矩阵和信息素矩阵
 */

#include <iostream>
#include <vector>
#include <set>
#include <random>
#include <cmath>
#include <chrono>
#include <limits>

using namespace std;

class AntColonyOptimization {
private:
    // 城市数量
    int numCities;
    // 蚂蚁数量
    int numAnts;
    // 迭代次数
    int maxIterations;
    // 信息素重要程度参数
    double alpha;
    // 启发因子重要程度参数
    double beta;
    // 信息素挥发系数
    double rho;
    // 信息素总量
    double Q;
    // 距离矩阵
    vector<vector<double>> distanceMatrix;
    // 信息素矩阵
    vector<vector<double>> pheromoneMatrix;
    // 最优路径
    vector<int> bestTour;
    // 最优路径长度
    double bestTourLength;
    // 随机数生成器
    mt19937 rng;
    uniform_real_distribution<double> uniformDist;
    uniform_int_distribution<int> intDist;

public:
    /**
     * 构造函数
     * @param numCities 城市数量
     * @param numAnts 蚂蚁数量
     * @param maxIterations 迭代次数
     * @param alpha 信息素重要程度参数
     * @param beta 启发因子重要程度参数
     * @param rho 信息素挥发系数
     * @param Q 信息素总量
     */
    AntColonyOptimization(int numCities, int numAnts, int maxIterations,
                         double alpha, double beta, double rho, double Q) 
        : numCities(numCities), numAnts(numAnts), maxIterations(maxIterations),
          alpha(alpha), beta(beta), rho(rho), Q(Q),
          rng(chrono::steady_clock::now().time_since_epoch().count()),
          uniformDist(0.0, 1.0), intDist(0, 1000000) {
        distanceMatrix.assign(numCities, vector<double>(numCities, 0));
        pheromoneMatrix.assign(numCities, vector<double>(numCities, 1.0));
        bestTourLength = numeric_limits<double>::max();
    }
    
    /**
     * 设置距离矩阵
     * @param distances 距离矩阵
     */
    void setDistanceMatrix(const vector<vector<double>>& distances) {
        distanceMatrix = distances;
    }
    
    /**
     * 初始化信息素矩阵
     * @param initialValue 初始信息素值
     */
    void initializePheromone(double initialValue) {
        for (int i = 0; i < numCities; i++) {
            for (int j = 0; j < numCities; j++) {
                pheromoneMatrix[i][j] = initialValue;
            }
        }
    }
    
    /**
     * 计算路径长度
     * @param tour 路径
     * @return 路径长度
     */
    double calculateTourLength(const vector<int>& tour) {
        double length = 0;
        for (size_t i = 0; i < tour.size() - 1; i++) {
            length += distanceMatrix[tour[i]][tour[i + 1]];
        }
        // 回到起点
        length += distanceMatrix[tour.back()][tour[0]];
        return length;
    }
    
    /**
     * 选择下一个城市
     * @param currentCity 当前城市
     * @param visited 已访问城市集合
     * @return 下一个城市
     */
    int selectNextCity(int currentCity, const set<int>& visited) {
        // 计算转移概率
        vector<double> probabilities(numCities, 0);
        double sum = 0;
        
        // 计算所有未访问城市的转移概率
        for (int i = 0; i < numCities; i++) {
            if (visited.find(i) == visited.end()) {
                // 避免除零错误
                double distance = distanceMatrix[currentCity][i];
                if (distance == 0) {
                    probabilities[i] = 0;
                } else {
                    // 状态转移规则
                    probabilities[i] = pow(pheromoneMatrix[currentCity][i], alpha) * 
                                      pow(1.0 / distance, beta);
                }
                sum += probabilities[i];
            }
        }
        
        // 如果所有概率都为0，则随机选择一个未访问城市
        if (sum == 0) {
            vector<int> unvisited;
            for (int i = 0; i < numCities; i++) {
                if (visited.find(i) == visited.end()) {
                    unvisited.push_back(i);
                }
            }
            return unvisited[intDist(rng) % unvisited.size()];
        }
        
        // 轮盘赌选择
        double pick = uniformDist(rng) * sum;
        double currentSum = 0;
        for (int i = 0; i < numCities; i++) {
            if (visited.find(i) == visited.end()) {
                currentSum += probabilities[i];
                if (currentSum >= pick) {
                    return i;
                }
            }
        }
        
        // 如果没有选中，则返回第一个未访问城市
        for (int i = 0; i < numCities; i++) {
            if (visited.find(i) == visited.end()) {
                return i;
            }
        }
        
        return currentCity; // 理论上不会执行到这里
    }
    
    /**
     * 构建路径
     * @param antId 蚂蚁编号
     * @return 路径
     */
    vector<int> constructSolution(int antId) {
        vector<int> tour;
        set<int> visited;
        
        // 随机选择起始城市
        int currentCity = intDist(rng) % numCities;
        tour.push_back(currentCity);
        visited.insert(currentCity);
        
        // 构建完整路径
        while (visited.size() < numCities) {
            int nextCity = selectNextCity(currentCity, visited);
            tour.push_back(nextCity);
            visited.insert(nextCity);
            currentCity = nextCity;
        }
        
        return tour;
    }
    
    /**
     * 更新信息素
     * @param antTours 所有蚂蚁的路径
     * @param antTourLengths 所有蚂蚁的路径长度
     */
    void updatePheromone(const vector<vector<int>>& antTours, 
                        const vector<double>& antTourLengths) {
        // 信息素挥发
        for (int i = 0; i < numCities; i++) {
            for (int j = 0; j < numCities; j++) {
                pheromoneMatrix[i][j] *= (1 - rho);
            }
        }
        
        // 信息素增强
        for (int ant = 0; ant < numAnts; ant++) {
            double contribution = Q / antTourLengths[ant];
            const vector<int>& tour = antTours[ant];
            
            for (size_t i = 0; i < tour.size() - 1; i++) {
                int from = tour[i];
                int to = tour[i + 1];
                pheromoneMatrix[from][to] += contribution;
                pheromoneMatrix[to][from] += contribution; // 对称矩阵
            }
            
            // 连接最后一个城市和第一个城市
            int last = tour.back();
            int first = tour[0];
            pheromoneMatrix[last][first] += contribution;
            pheromoneMatrix[first][last] += contribution;
        }
    }
    
    /**
     * 执行蚁群算法
     * @return 最优路径
     */
    vector<int> solve() {
        // 初始化信息素
        initializePheromone(1.0);
        bestTourLength = numeric_limits<double>::max();
        
        // 迭代优化
        for (int iteration = 0; iteration < maxIterations; iteration++) {
            vector<vector<int>> antTours;
            vector<double> antTourLengths;
            
            // 每只蚂蚁构建解
            for (int ant = 0; ant < numAnts; ant++) {
                vector<int> tour = constructSolution(ant);
                double tourLength = calculateTourLength(tour);
                
                antTours.push_back(tour);
                antTourLengths.push_back(tourLength);
                
                // 更新全局最优解
                if (tourLength < bestTourLength) {
                    bestTourLength = tourLength;
                    bestTour = tour;
                }
            }
            
            // 更新信息素
            updatePheromone(antTours, antTourLengths);
            
            // 可选：打印当前进度
            // cout << "Iteration " << (iteration + 1) << ": Best Tour Length = " << bestTourLength << endl;
        }
        
        return bestTour;
    }
    
    /**
     * 获取最优路径长度
     * @return 最优路径长度
     */
    double getBestTourLength() const {
        return bestTourLength;
    }
};

/**
 * 测试示例 - 对称TSP问题
 */
int main() {
    // 创建一个简单的TSP实例（5个城市）
    int numCities = 5;
    int numAnts = 10;
    int maxIterations = 100;
    double alpha = 1.0;   // 信息素重要程度
    double beta = 2.0;    // 启发因子重要程度
    double rho = 0.5;     // 信息素挥发系数
    double Q = 100.0;     // 信息素总量
    
    // 距离矩阵（对称TSP）
    vector<vector<double>> distances = {
        {0, 10, 15, 20, 25},
        {10, 0, 35, 25, 30},
        {15, 35, 0, 30, 20},
        {20, 25, 30, 0, 15},
        {25, 30, 20, 15, 0}
    };
    
    // 创建蚁群算法实例
    AntColonyOptimization aco(numCities, numAnts, maxIterations, alpha, beta, rho, Q);
    aco.setDistanceMatrix(distances);
    
    // 执行算法
    cout << "开始执行蚁群算法..." << endl;
    auto startTime = chrono::high_resolution_clock::now();
    vector<int> result = aco.solve();
    auto endTime = chrono::high_resolution_clock::now();
    
    // 输出结果
    cout << "算法执行完成！" << endl;
    cout << "最优路径: ";
    for (size_t i = 0; i < result.size(); i++) {
        cout << result[i];
        if (i < result.size() - 1) cout << " -> ";
    }
    cout << " -> " << result[0] << endl; // 回到起点
    cout << "最优路径长度: " << aco.getBestTourLength() << endl;
    
    auto duration = chrono::duration_cast<chrono::microseconds>(endTime - startTime);
    cout << "执行时间: " << duration.count() << " μs" << endl;
    
    return 0;
}