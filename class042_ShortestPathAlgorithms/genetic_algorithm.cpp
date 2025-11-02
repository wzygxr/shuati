/**
 * 遗传算法 (Genetic Algorithm)
 * 
 * 算法原理：
 * 遗传算法是一种模拟自然选择和遗传机制的随机搜索算法。
 * 它将问题的解编码为"染色体"，通过选择、交叉、变异等操作，
 * 使种群不断进化，最终找到问题的最优解或近似最优解。
 * 
 * 算法特点：
 * 1. 属于元启发式算法，适用于解决NP难问题
 * 2. 基于种群的全局搜索算法
 * 3. 不需要导数信息，适用于离散和连续优化问题
 * 4. 具有良好的并行性
 * 
 * 应用场景：
 * - 函数优化
 * - 组合优化（TSP、背包问题等）
 * - 机器学习（特征选择、神经网络训练等）
 * - 调度问题
 * - 工程设计优化
 * 
 * 算法流程：
 * 1. 初始化种群（随机生成初始解）
 * 2. 计算适应度（目标函数值）
 * 3. 选择操作（根据适应度选择个体）
 * 4. 交叉操作（模拟生物交配产生后代）
 * 5. 变异操作（模拟生物基因突变）
 * 6. 生成新种群，重复步骤2-6直到满足终止条件
 * 
 * 时间复杂度：O(G×N×M)，G为迭代代数，N为种群大小，M为个体编码长度
 * 空间复杂度：O(N×M)，存储种群信息
 */

#include <iostream>
#include <vector>
#include <random>
#include <algorithm>
#include <chrono>
#include <limits>

using namespace std;

class GeneticAlgorithm {
private:
    // 种群大小
    int populationSize;
    // 染色体长度（问题维度）
    int chromosomeLength;
    // 最大迭代代数
    int maxGenerations;
    // 交叉概率
    double crossoverRate;
    // 变异概率
    double mutationRate;
    // 当前种群
    vector<vector<int>> population;
    // 适应度数组
    vector<double> fitness;
    // 最优个体
    vector<int> bestIndividual;
    // 最优适应度值
    double bestFitness;
    // 随机数生成器
    mt19937 rng;
    uniform_real_distribution<double> uniformDist;
    uniform_int_distribution<int> intDist;

public:
    /**
     * 构造函数
     * @param populationSize 种群大小
     * @param chromosomeLength 染色体长度
     * @param maxGenerations 最大迭代代数
     * @param crossoverRate 交叉概率
     * @param mutationRate 变异概率
     */
    GeneticAlgorithm(int populationSize, int chromosomeLength, int maxGenerations,
                    double crossoverRate, double mutationRate) 
        : populationSize(populationSize), chromosomeLength(chromosomeLength),
          maxGenerations(maxGenerations), crossoverRate(crossoverRate),
          mutationRate(mutationRate), 
          rng(chrono::steady_clock::now().time_since_epoch().count()),
          uniformDist(0.0, 1.0), intDist(0, 1) {
        bestFitness = numeric_limits<double>::lowest();
    }
    
    /**
     * 初始化种群
     */
    void initializePopulation() {
        population.clear();
        fitness.clear();
        
        // 随机生成初始种群
        for (int i = 0; i < populationSize; i++) {
            vector<int> individual;
            for (int j = 0; j < chromosomeLength; j++) {
                // 对于二进制编码，基因值为0或1
                individual.push_back(intDist(rng));
            }
            population.push_back(individual);
        }
    }
    
    /**
     * 计算适应度 - 需要根据具体问题定义
     * 这里以最大化函数 f(x) = sum(x_i) 为例（二进制编码）
     * @param individual 个体（染色体）
     * @return 适应度值
     */
    double calculateFitness(const vector<int>& individual) {
        int sum = 0;
        for (int gene : individual) {
            sum += gene;
        }
        return sum;
    }
    
    /**
     * 评估整个种群的适应度
     */
    void evaluatePopulation() {
        fitness.clear();
        double maxFitness = numeric_limits<double>::lowest();
        int bestIndex = 0;
        
        for (int i = 0; i < populationSize; i++) {
            double fit = calculateFitness(population[i]);
            fitness.push_back(fit);
            
            // 更新最优个体
            if (fit > maxFitness) {
                maxFitness = fit;
                bestIndex = i;
            }
        }
        
        // 更新全局最优
        if (maxFitness > bestFitness) {
            bestFitness = maxFitness;
            bestIndividual = population[bestIndex];
        }
    }
    
    /**
     * 选择操作 - 轮盘赌选择
     * @return 选中的个体索引
     */
    int select() {
        // 计算总适应度
        double totalFitness = 0;
        for (double fit : fitness) {
            totalFitness += max(0.0, fit);  // 确保适应度非负
        }
        
        // 如果总适应度为0，则随机选择
        if (totalFitness <= 0) {
            return rng() % populationSize;
        }
        
        // 轮盘赌选择
        double pick = uniformDist(rng) * totalFitness;
        double currentSum = 0;
        for (int i = 0; i < populationSize; i++) {
            currentSum += max(0.0, fitness[i]);
            if (currentSum >= pick) {
                return i;
            }
        }
        
        return populationSize - 1;
    }
    
    /**
     * 交叉操作 - 单点交叉
     * @param parent1 父代1
     * @param parent2 父代2
     * @return 两个子代
     */
    vector<vector<int>> crossover(const vector<int>& parent1, const vector<int>& parent2) {
        vector<vector<int>> offspring;
        
        // 以一定概率进行交叉
        if (uniformDist(rng) > crossoverRate) {
            offspring.push_back(parent1);
            offspring.push_back(parent2);
            return offspring;
        }
        
        // 随机选择交叉点
        int crossoverPoint = rng() % chromosomeLength;
        
        // 创建子代
        vector<int> child1, child2;
        
        // 执行单点交叉
        for (int i = 0; i < chromosomeLength; i++) {
            if (i < crossoverPoint) {
                child1.push_back(parent1[i]);
                child2.push_back(parent2[i]);
            } else {
                child1.push_back(parent2[i]);
                child2.push_back(parent1[i]);
            }
        }
        
        offspring.push_back(child1);
        offspring.push_back(child2);
        return offspring;
    }
    
    /**
     * 变异操作 - 位翻转变异
     * @param individual 个体
     */
    void mutate(vector<int>& individual) {
        for (int i = 0; i < chromosomeLength; i++) {
            // 以一定概率进行变异
            if (uniformDist(rng) < mutationRate) {
                // 位翻转
                individual[i] = 1 - individual[i];
            }
        }
    }
    
    /**
     * 生成新种群
     */
    void generateNewPopulation() {
        vector<vector<int>> newPopulation;
        
        // 保留最优个体（精英策略）
        int bestIndex = 0;
        double maxFitness = numeric_limits<double>::lowest();
        for (int i = 0; i < populationSize; i++) {
            if (fitness[i] > maxFitness) {
                maxFitness = fitness[i];
                bestIndex = i;
            }
        }
        newPopulation.push_back(population[bestIndex]);
        
        // 生成其余个体
        while (newPopulation.size() < populationSize) {
            // 选择两个父代
            int parent1Index = select();
            int parent2Index = select();
            
            // 交叉
            vector<vector<int>> offspring = crossover(
                population[parent1Index], 
                population[parent2Index]
            );
            
            // 变异
            for (auto& child : offspring) {
                mutate(child);
                newPopulation.push_back(child);
                
                // 如果新种群已满，跳出循环
                if (newPopulation.size() >= populationSize) {
                    break;
                }
            }
        }
        
        // 确保种群大小不变
        while (newPopulation.size() > populationSize) {
            newPopulation.pop_back();
        }
        
        population = newPopulation;
    }
    
    /**
     * 执行遗传算法
     * @return 最优个体
     */
    vector<int> solve() {
        // 初始化
        initializePopulation();
        bestFitness = numeric_limits<double>::lowest();
        
        // 迭代进化
        for (int generation = 0; generation < maxGenerations; generation++) {
            // 评估适应度
            evaluatePopulation();
            
            // 生成新种群
            generateNewPopulation();
            
            // 可选：打印当前进度
            // cout << "Generation " << (generation + 1) << ": Best Fitness = " << bestFitness << endl;
        }
        
        return bestIndividual;
    }
    
    /**
     * 获取最优适应度值
     * @return 最优适应度值
     */
    double getBestFitness() const {
        return bestFitness;
    }
};

/**
 * 测试示例
 */
int main() {
    // 设置算法参数
    int populationSize = 100;      // 种群大小
    int chromosomeLength = 20;     // 染色体长度
    int maxGenerations = 100;      // 最大迭代代数
    double crossoverRate = 0.8;    // 交叉概率
    double mutationRate = 0.01;    // 变异概率
    
    // 创建遗传算法实例
    GeneticAlgorithm ga(populationSize, chromosomeLength, maxGenerations, 
                        crossoverRate, mutationRate);
    
    // 执行算法
    cout << "开始执行遗传算法..." << endl;
    auto startTime = chrono::high_resolution_clock::now();
    vector<int> result = ga.solve();
    auto endTime = chrono::high_resolution_clock::now();
    
    // 输出结果
    cout << "算法执行完成！" << endl;
    cout << "最优个体: [";
    for (size_t i = 0; i < result.size(); i++) {
        cout << result[i];
        if (i < result.size() - 1) cout << ", ";
    }
    cout << "]" << endl;
    cout << "最优适应度: " << ga.getBestFitness() << endl;
    
    auto duration = chrono::duration_cast<chrono::microseconds>(endTime - startTime);
    cout << "执行时间: " << duration.count() << " μs" << endl;
    
    // 验证结果 (理论上最优解应该全为1)
    cout << "\n结果分析:" << endl;
    cout << "理论最优个体: 全1向量" << endl;
    cout << "理论最优适应度: " << chromosomeLength << endl;
    cout << "误差: " << (chromosomeLength - ga.getBestFitness()) << endl;
    
    return 0;
}