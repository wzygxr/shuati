/**
 * 粒子群优化算法 (Particle Swarm Optimization, PSO)
 * 
 * 算法原理：
 * 粒子群优化算法是一种基于群体智能的优化算法，模拟鸟群觅食行为。
 * 每个粒子代表一个候选解，在解空间中飞行，通过跟踪个体极值和全局极值来更新自己的速度和位置。
 * 
 * 算法特点：
 * 1. 属于群智能算法，适用于连续优化问题
 * 2. 收敛速度快，算法简单易实现
 * 3. 具有良好的全局搜索能力
 * 4. 适用于函数优化、神经网络训练等领域
 * 
 * 应用场景：
 * - 函数优化
 * - 神经网络训练
 * - 工程设计优化
 * - 调度问题
 * - 图像处理
 * 
 * 算法流程：
 * 1. 初始化粒子群（随机生成位置和速度）
 * 2. 计算每个粒子的适应度值
 * 3. 更新个体极值和全局极值
 * 4. 更新粒子的速度和位置
 * 5. 重复步骤2-4直到满足终止条件
 * 
 * 时间复杂度：O(G×N×D)，G为迭代次数，N为粒子数量，D为问题维度
 * 空间复杂度：O(N×D)，存储粒子信息
 */

#include <iostream>
#include <vector>
#include <random>
#include <cmath>
#include <chrono>
#include <limits>

using namespace std;

// 粒子类
class Particle {
public:
    vector<double> position;      // 位置
    vector<double> velocity;      // 速度
    vector<double> bestPosition;  // 个体最优位置
    double bestValue;             // 个体最优值
    
    Particle(int dimension) {
        position.assign(dimension, 0);
        velocity.assign(dimension, 0);
        bestPosition.assign(dimension, 0);
        bestValue = numeric_limits<double>::max();
    }
};

class ParticleSwarmOptimization {
private:
    // 粒子数量
    int numParticles;
    // 问题维度
    int dimension;
    // 最大迭代次数
    int maxIterations;
    // 惯性权重
    double w;
    // 个体学习因子
    double c1;
    // 社会学习因子
    double c2;
    // 位置上下界
    vector<double> lowerBounds;
    vector<double> upperBounds;
    // 速度上下界
    vector<double> velocityBounds;
    // 粒子群
    vector<Particle> particles;
    // 全局最优位置
    vector<double> globalBestPosition;
    // 全局最优值
    double globalBestValue;
    // 随机数生成器
    mt19937 rng;
    uniform_real_distribution<double> uniformDist;

public:
    /**
     * 构造函数
     * @param numParticles 粒子数量
     * @param dimension 问题维度
     * @param maxIterations 最大迭代次数
     * @param w 惯性权重
     * @param c1 个体学习因子
     * @param c2 社会学习因子
     */
    ParticleSwarmOptimization(int numParticles, int dimension, int maxIterations,
                             double w, double c1, double c2) 
        : numParticles(numParticles), dimension(dimension), maxIterations(maxIterations),
          w(w), c1(c1), c2(c2),
          rng(chrono::steady_clock::now().time_since_epoch().count()),
          uniformDist(0.0, 1.0) {
        globalBestPosition.assign(dimension, 0);
        globalBestValue = numeric_limits<double>::max();
    }
    
    /**
     * 设置边界
     * @param lowerBounds 位置下界
     * @param upperBounds 位置上界
     */
    void setBounds(const vector<double>& lowerBounds, const vector<double>& upperBounds) {
        this->lowerBounds = lowerBounds;
        this->upperBounds = upperBounds;
        
        // 设置速度边界为位置边界范围的10%
        velocityBounds.resize(dimension);
        for (int i = 0; i < dimension; i++) {
            velocityBounds[i] = 0.1 * (upperBounds[i] - lowerBounds[i]);
        }
    }
    
    /**
     * 初始化粒子群
     */
    void initializeParticles() {
        particles.clear();
        particles.reserve(numParticles);
        
        for (int i = 0; i < numParticles; i++) {
            particles.emplace_back(dimension);
            
            // 随机初始化位置和速度
            for (int j = 0; j < dimension; j++) {
                // 初始化位置
                particles[i].position[j] = lowerBounds[j] + 
                    uniformDist(rng) * (upperBounds[j] - lowerBounds[j]);
                
                // 初始化速度
                particles[i].velocity[j] = -velocityBounds[j] + 
                    uniformDist(rng) * (2 * velocityBounds[j]);
            }
            
            // 初始化个体最优位置
            particles[i].bestPosition = particles[i].position;
        }
    }
    
    /**
     * 目标函数 - 需要根据具体问题定义
     * 这里以最小化函数 f(x) = sum(x_i^2) 为例
     * @param position 位置向量
     * @return 目标函数值
     */
    double objectiveFunction(const vector<double>& position) {
        double sum = 0;
        for (int i = 0; i < dimension; i++) {
            sum += position[i] * position[i];
        }
        return sum;
    }
    
    /**
     * 评估粒子适应度
     */
    void evaluateParticles() {
        for (int i = 0; i < numParticles; i++) {
            double value = objectiveFunction(particles[i].position);
            
            // 更新个体最优
            if (value < particles[i].bestValue) {
                particles[i].bestValue = value;
                particles[i].bestPosition = particles[i].position;
            }
            
            // 更新全局最优
            if (value < globalBestValue) {
                globalBestValue = value;
                globalBestPosition = particles[i].position;
            }
        }
    }
    
    /**
     * 更新粒子速度和位置
     */
    void updateParticles() {
        for (int i = 0; i < numParticles; i++) {
            for (int j = 0; j < dimension; j++) {
                // 更新速度
                particles[i].velocity[j] = w * particles[i].velocity[j] +
                    c1 * uniformDist(rng) * (particles[i].bestPosition[j] - particles[i].position[j]) +
                    c2 * uniformDist(rng) * (globalBestPosition[j] - particles[i].position[j]);
                
                // 速度边界处理
                if (particles[i].velocity[j] > velocityBounds[j]) {
                    particles[i].velocity[j] = velocityBounds[j];
                } else if (particles[i].velocity[j] < -velocityBounds[j]) {
                    particles[i].velocity[j] = -velocityBounds[j];
                }
                
                // 更新位置
                particles[i].position[j] += particles[i].velocity[j];
                
                // 位置边界处理
                if (particles[i].position[j] > upperBounds[j]) {
                    particles[i].position[j] = upperBounds[j];
                } else if (particles[i].position[j] < lowerBounds[j]) {
                    particles[i].position[j] = lowerBounds[j];
                }
            }
        }
    }
    
    /**
     * 执行粒子群优化算法
     * @return 全局最优位置
     */
    vector<double> solve() {
        // 初始化
        initializeParticles();
        globalBestValue = numeric_limits<double>::max();
        
        // 迭代优化
        for (int iteration = 0; iteration < maxIterations; iteration++) {
            // 评估适应度
            evaluateParticles();
            
            // 更新粒子
            updateParticles();
            
            // 可选：打印当前进度
            // cout << "Iteration " << (iteration + 1) << ": Global Best Value = " << globalBestValue << endl;
        }
        
        return globalBestPosition;
    }
    
    /**
     * 获取全局最优值
     * @return 全局最优值
     */
    double getGlobalBestValue() const {
        return globalBestValue;
    }
};

/**
 * 测试示例
 */
int main() {
    // 设置算法参数
    int numParticles = 30;      // 粒子数量
    int dimension = 10;         // 问题维度
    int maxIterations = 1000;   // 最大迭代次数
    double w = 0.7;             // 惯性权重
    double c1 = 1.5;            // 个体学习因子
    double c2 = 1.5;            // 社会学习因子
    
    // 位置边界
    vector<double> lowerBounds(dimension, -10.0);
    vector<double> upperBounds(dimension, 10.0);
    
    // 创建粒子群优化算法实例
    ParticleSwarmOptimization pso(numParticles, dimension, maxIterations, w, c1, c2);
    pso.setBounds(lowerBounds, upperBounds);
    
    // 执行算法
    cout << "开始执行粒子群优化算法..." << endl;
    auto startTime = chrono::high_resolution_clock::now();
    vector<double> result = pso.solve();
    auto endTime = chrono::high_resolution_clock::now();
    
    // 输出结果
    cout << "算法执行完成！" << endl;
    cout << "最优位置: [";
    for (size_t i = 0; i < result.size(); i++) {
        printf("%.6f", result[i]);
        if (i < result.size() - 1) cout << ", ";
    }
    cout << "]" << endl;
    printf("最优值: %.10f\n", pso.getGlobalBestValue());
    
    auto duration = chrono::duration_cast<chrono::microseconds>(endTime - startTime);
    cout << "执行时间: " << duration.count() << " μs" << endl;
    
    // 验证结果 (理论上最优解应该接近全0向量)
    cout << "\n结果分析:" << endl;
    cout << "理论最优位置: 全0向量" << endl;
    cout << "理论最优值: 0" << endl;
    printf("误差: %.10f\n", abs(pso.getGlobalBestValue()));
    
    return 0;
}