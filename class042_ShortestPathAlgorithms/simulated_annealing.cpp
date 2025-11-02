/**
 * 模拟退火算法 (Simulated Annealing)
 * 
 * 算法原理：
 * 模拟退火算法是一种通用概率算法，用来在一个大的搜寻空间内找寻问题的最优解。
 * 它模仿固体物质的退火过程：将固体加热至高温后缓慢冷却，在冷却过程中，
 * 固体内部粒子逐渐有序排列，最终达到低能态（最优解）。
 * 
 * 算法特点：
 * 1. 属于元启发式算法，适用于解决NP难问题
 * 2. 能以一定概率接受较差解，从而跳出局部最优
 * 3. 温度参数控制接受差解的概率，随时间推移而降低
 * 
 * 应用场景：
 * - TSP旅行商问题
 * - 函数优化
 * - 图着色问题
 * - 调度问题
 * - IOI2023、国集2023等竞赛考点
 * 
 * 算法流程：
 * 1. 初始化温度T和解状态
 * 2. 在当前温度下进行迭代寻优
 * 3. 产生新解并计算目标函数值
 * 4. 根据Metropolis准则决定是否接受新解
 * 5. 降温，重复步骤2-5直到终止条件
 * 
 * 时间复杂度：取决于问题规模和迭代次数，通常为O(k×n)，k为迭代次数，n为问题规模
 * 空间复杂度：O(1) 或 O(n)，取决于具体问题存储需求
 */

#include <iostream>
#include <vector>
#include <random>
#include <cmath>
#include <chrono>
#include <limits>

using namespace std;
#include <vector>
#include <random>
#include <cmath>
#include <chrono>
#include <limits>

class SimulatedAnnealing {
private:
    // 当前解
    std::vector<double> currentSolution;
    // 最优解
    std::vector<double> bestSolution;
    // 当前目标函数值
    double currentValue;
    // 最优目标函数值
    double bestValue;
    // 初始温度
    double initialTemperature;
    // 当前温度
    double temperature;
    // 冷却系数
    double coolingRate;
    // 终止温度
    double minTemperature;
    // 每个温度下的迭代次数
    int iterationsPerTemp;
    // 随机数生成器
    std::mt19937 rng;
    std::uniform_real_distribution<double> uniformDist;
    std::normal_distribution<double> normalDist;

public:
    /**
     * 构造函数
     * @param initialTemperature 初始温度
     * @param coolingRate 冷却系数 (0 < coolingRate < 1)
     * @param minTemperature 终止温度
     * @param iterationsPerTemp 每个温度下的迭代次数
     */
    SimulatedAnnealing(double initialTemperature, double coolingRate, 
                      double minTemperature, int iterationsPerTemp) 
        : initialTemperature(initialTemperature), coolingRate(coolingRate),
          minTemperature(minTemperature), iterationsPerTemp(iterationsPerTemp),
          rng(std::chrono::steady_clock::now().time_since_epoch().count()),
          uniformDist(0.0, 1.0), normalDist(0.0, 1.0) {
        currentValue = std::numeric_limits<double>::max();
        bestValue = std::numeric_limits<double>::max();
        temperature = initialTemperature;
    }
    
    /**
     * 初始化解空间
     * @param dimensions 解的维度
     * @param lowerBounds 下界数组
     * @param upperBounds 上界数组
     */
    void initializeSolution(int dimensions, const std::vector<double>& lowerBounds, 
                          const std::vector<double>& upperBounds) {
        currentSolution.resize(dimensions);
        bestSolution.resize(dimensions);
        
        // 随机初始化解
        for (int i = 0; i < dimensions; i++) {
            currentSolution[i] = lowerBounds[i] + uniformDist(rng) * (upperBounds[i] - lowerBounds[i]);
            bestSolution[i] = currentSolution[i];
        }
        
        // 计算初始目标函数值
        currentValue = objectiveFunction(currentSolution);
        bestValue = currentValue;
    }
    
    /**
     * 目标函数 - 需要根据具体问题定义
     * 这里以最小化函数 f(x) = x1^2 + x2^2 + ... + xn^2 为例
     * @param solution 解向量
     * @return 目标函数值
     */
    double objectiveFunction(const std::vector<double>& solution) {
        double sum = 0;
        for (size_t i = 0; i < solution.size(); i++) {
            sum += solution[i] * solution[i];
        }
        return sum;
    }
    
    /**
     * 产生邻域解
     * @param solution 当前解
     * @param lowerBounds 下界
     * @param upperBounds 上界
     * @return 新解
     */
    std::vector<double> generateNeighbor(const std::vector<double>& solution, 
                                       const std::vector<double>& lowerBounds, 
                                       const std::vector<double>& upperBounds) {
        std::vector<double> neighbor = solution;
        int index = rng() % solution.size();
        
        // 在当前解的基础上添加一个小的随机扰动
        double delta = (upperBounds[index] - lowerBounds[index]) * 0.1;
        neighbor[index] += (normalDist(rng) * delta);
        
        // 确保新解在有效范围内
        if (neighbor[index] < lowerBounds[index]) {
            neighbor[index] = lowerBounds[index];
        } else if (neighbor[index] > upperBounds[index]) {
            neighbor[index] = upperBounds[index];
        }
        
        return neighbor;
    }
    
    /**
     * Metropolis准则 - 决定是否接受新解
     * @param newValue 新解的目标函数值
     * @param oldValue 当前解的目标函数值
     * @param temperature 当前温度
     * @return 是否接受新解
     */
    bool metropolisCriterion(double newValue, double oldValue, double temperature) {
        // 如果新解更优，则直接接受
        if (newValue < oldValue) {
            return true;
        }
        
        // 否则以一定概率接受较差解
        double probability = exp(-(newValue - oldValue) / temperature);
        return uniformDist(rng) < probability;
    }
    
    /**
     * 降温函数 - 指数降温
     * @param temperature 当前温度
     * @return 新温度
     */
    double coolDown(double temperature) {
        return temperature * coolingRate;
    }
    
    /**
     * 执行模拟退火算法
     * @param dimensions 解的维度
     * @param lowerBounds 下界数组
     * @param upperBounds 上界数组
     * @return 最优解
     */
    std::vector<double> solve(int dimensions, const std::vector<double>& lowerBounds, 
                             const std::vector<double>& upperBounds) {
        // 初始化
        initializeSolution(dimensions, lowerBounds, upperBounds);
        temperature = initialTemperature;
        
        // 主循环 - 直到温度降到最低温度
        while (temperature > minTemperature) {
            // 在当前温度下进行多次迭代
            for (int i = 0; i < iterationsPerTemp; i++) {
                // 产生邻域解
                std::vector<double> newSolution = generateNeighbor(currentSolution, lowerBounds, upperBounds);
                double newValue = objectiveFunction(newSolution);
                
                // 根据Metropolis准则决定是否接受新解
                if (metropolisCriterion(newValue, currentValue, temperature)) {
                    // 接受新解
                    currentSolution = newSolution;
                    currentValue = newValue;
                    
                    // 更新最优解
                    if (currentValue < bestValue) {
                        bestSolution = currentSolution;
                        bestValue = currentValue;
                    }
                }
            }
            
            // 降温
            temperature = coolDown(temperature);
            
            // 可选：打印当前进度
            // std::cout << "Temperature: " << temperature << ", Best Value: " << bestValue << std::endl;
        }
        
        return bestSolution;
    }
    
    /**
     * 获取最优值
     * @return 最优目标函数值
     */
    double getBestValue() const {
        return bestValue;
    }
};

/**
 * 测试示例
 */
int main() {
    // 设置算法参数
    double initialTemp = 1000.0;     // 初始温度
    double coolingRate = 0.95;       // 冷却系数
    double minTemp = 1e-8;           // 终止温度
    int iterations = 100;            // 每个温度下的迭代次数
    
    // 创建模拟退火算法实例
    SimulatedAnnealing sa(initialTemp, coolingRate, minTemp, iterations);
    
    // 定义问题参数 (以2维函数优化为例)
    int dimensions = 2;
    std::vector<double> lowerBounds = {-10.0, -10.0};  // 各维度下界
    std::vector<double> upperBounds = {10.0, 10.0};    // 各维度上界
    
    // 执行算法
    std::cout << "开始执行模拟退火算法..." << std::endl;
    auto startTime = std::chrono::high_resolution_clock::now();
    std::vector<double> result = sa.solve(dimensions, lowerBounds, upperBounds);
    auto endTime = std::chrono::high_resolution_clock::now();
    
    // 输出结果
    std::cout << "算法执行完成！" << std::endl;
    std::cout << "最优解: [" << result[0] << ", " << result[1] << "]" << std::endl;
    std::cout << "最优值: " << sa.getBestValue() << std::endl;
    
    auto duration = std::chrono::duration_cast<std::chrono::microseconds>(endTime - startTime);
    std::cout << "执行时间: " << duration.count() << " μs" << std::endl;
    
    // 验证结果 (理论上最优解应该接近 [0, 0])
    std::cout << "\n结果分析:" << std::endl;
    std::cout << "理论最优解: [0, 0]" << std::endl;
    std::cout << "理论最优值: 0" << std::endl;
    std::cout << "误差: " << std::abs(sa.getBestValue()) << std::endl;
    
    return 0;
}