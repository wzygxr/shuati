/**
 * 蒙特卡洛方法 (Monte Carlo Method)
 * 
 * 算法原理：
 * 蒙特卡洛方法是一种基于随机采样的数值计算方法，通过大量随机实验来求解问题。
 * 它将所求解的问题同一定的概率模型相联系，利用电子计算机实现统计模拟或抽样，
 * 以获得问题的近似解。
 * 
 * 算法特点：
 * 1. 基于概率统计理论的数值计算方法
 * 2. 收敛速度与问题维度无关
 * 3. 适用于高维问题和复杂积分计算
 * 4. 结果具有随机性，需要多次实验取平均
 * 
 * 应用场景：
 * - 数值积分计算
 * - 概率统计问题
 * - 物理模拟
 * - 金融工程
 * - 图形学渲染
 * 
 * 算法流程：
 * 1. 构造概率模型
 * 2. 进行大量随机抽样实验
 * 3. 统计实验结果
 * 4. 根据大数定律得到近似解
 * 
 * 时间复杂度：O(N)，N为抽样次数
 * 空间复杂度：O(1)
 */

#include <iostream>
#include <vector>
#include <random>
#include <cmath>
#include <chrono>
#include <limits>

using namespace std;

class MonteCarlo {
private:
    // 随机数生成器
    mt19937 rng;
    uniform_real_distribution<double> uniformDist;

public:
    /**
     * 构造函数
     */
    MonteCarlo() : rng(chrono::steady_clock::now().time_since_epoch().count()),
                   uniformDist(0.0, 1.0) {}
    
    /**
     * 使用蒙特卡洛方法计算π值
     * 原理：在单位正方形内随机撒点，统计落在单位圆内的点的比例
     * 单位圆面积 = π，单位正方形面积 = 4
     * π/4 = 圆内点数/总点数
     * 
     * @param numSamples 抽样次数
     * @return π的近似值
     */
    double calculatePi(int numSamples) {
        int insideCircle = 0;
        
        for (int i = 0; i < numSamples; i++) {
            // 在[-1, 1]范围内随机生成点坐标
            double x = uniformDist(rng) * 2 - 1;
            double y = uniformDist(rng) * 2 - 1;
            
            // 判断点是否在单位圆内
            if (x * x + y * y <= 1) {
                insideCircle++;
            }
        }
        
        // 根据比例计算π值
        return 4.0 * insideCircle / numSamples;
    }
    
    /**
     * 使用蒙特卡洛方法计算定积分
     * 示例：计算函数 f(x) = x^2 在区间 [0, 1] 上的定积分
     * 理论值为 1/3
     * 
     * @param numSamples 抽样次数
     * @param a 积分下限
     * @param b 积分上限
     * @return 定积分的近似值
     */
    double calculateIntegral(int numSamples, double a, double b) {
        double sum = 0;
        
        for (int i = 0; i < numSamples; i++) {
            // 在区间[a, b]内随机生成点
            double x = a + uniformDist(rng) * (b - a);
            
            // 计算函数值 f(x) = x^2
            double fx = x * x;
            
            sum += fx;
        }
        
        // 根据蒙特卡洛积分公式计算结果
        return (b - a) * sum / numSamples;
    }
    
    /**
     * 使用蒙特卡洛方法估算圆的面积
     * 通过在矩形区域内随机撒点，统计落在圆内的点的比例来估算圆的面积
     * 
     * @param numSamples 抽样次数
     * @param radius 圆的半径
     * @return 圆面积的近似值
     */
    double estimateCircleArea(int numSamples, double radius) {
        // 外接正方形的边长
        double side = 2 * radius;
        // 外接正方形的面积
        double squareArea = side * side;
        
        int insideCircle = 0;
        
        for (int i = 0; i < numSamples; i++) {
            // 在正方形内随机生成点坐标
            double x = uniformDist(rng) * side - radius;
            double y = uniformDist(rng) * side - radius;
            
            // 判断点是否在圆内
            if (x * x + y * y <= radius * radius) {
                insideCircle++;
            }
        }
        
        // 根据比例计算圆面积
        return squareArea * insideCircle / numSamples;
    }
    
    /**
     * 使用蒙特卡洛方法求解Buffon投针问题
     * 计算概率 π ≈ (2 * 针长度 * 投掷次数) / (平行线间距 * 与线相交次数)
     * 
     * @param numSamples 投掷次数
     * @param needleLength 针的长度
     * @param lineSpacing 平行线间距
     * @return π的近似值
     */
    double buffonNeedle(int numSamples, double needleLength, double lineSpacing) {
        int crossCount = 0;
        uniform_real_distribution<double> angleDist(0.0, M_PI);
        
        for (int i = 0; i < numSamples; i++) {
            // 随机生成针的中心位置
            double center = uniformDist(rng) * lineSpacing;
            
            // 随机生成针的角度 (0到π)
            double angle = angleDist(rng);
            
            // 计算针端点到最近平行线的距离
            double distance = min(center, lineSpacing - center);
            
            // 计算针端点在垂直方向的投影
            double projection = (needleLength / 2) * sin(angle);
            
            // 如果投影大于距离，则针与平行线相交
            if (projection >= distance) {
                crossCount++;
            }
        }
        
        // 根据Buffon公式计算π
        if (crossCount == 0) {
            return numeric_limits<double>::infinity(); // 避免除零错误
        }
        return (2.0 * needleLength * numSamples) / (lineSpacing * crossCount);
    }
};

/**
 * 测试示例
 */
int main() {
    MonteCarlo mc;
    
    cout << "=== 蒙特卡洛方法测试 ===" << endl;
    
    // 测试π值计算
    cout << "\n1. 计算π值:" << endl;
    vector<int> sampleSizes = {1000, 10000, 100000, 1000000};
    for (int samples : sampleSizes) {
        auto startTime = chrono::high_resolution_clock::now();
        double pi = mc.calculatePi(samples);
        auto endTime = chrono::high_resolution_clock::now();
        
        auto duration = chrono::duration_cast<chrono::microseconds>(endTime - startTime);
        printf("抽样次数: %d, π ≈ %.6f, 误差: %.6f, 时间: %ld μs\n", 
               samples, pi, abs(M_PI - pi), duration.count());
    }
    
    // 测试定积分计算
    cout << "\n2. 计算定积分 ∫x²dx (0到1):" << endl;
    double theoreticalValue = 1.0 / 3.0; // 理论值
    for (int samples : sampleSizes) {
        auto startTime = chrono::high_resolution_clock::now();
        double integral = mc.calculateIntegral(samples, 0, 1);
        auto endTime = chrono::high_resolution_clock::now();
        
        auto duration = chrono::duration_cast<chrono::microseconds>(endTime - startTime);
        printf("抽样次数: %d, 积分值 ≈ %.6f, 误差: %.6f, 时间: %ld μs\n", 
               samples, integral, abs(theoreticalValue - integral), duration.count());
    }
    
    // 测试圆面积估算
    cout << "\n3. 估算圆面积 (半径=1):" << endl;
    double theoreticalArea = M_PI; // 理论面积
    for (int samples : sampleSizes) {
        auto startTime = chrono::high_resolution_clock::now();
        double area = mc.estimateCircleArea(samples, 1.0);
        auto endTime = chrono::high_resolution_clock::now();
        
        auto duration = chrono::duration_cast<chrono::microseconds>(endTime - startTime);
        printf("抽样次数: %d, 面积 ≈ %.6f, 误差: %.6f, 时间: %ld μs\n", 
               samples, area, abs(theoreticalArea - area), duration.count());
    }
    
    // 测试Buffon投针问题
    cout << "\n4. Buffon投针问题 (针长=0.5, 线间距=1):" << endl;
    for (int samples : sampleSizes) {
        auto startTime = chrono::high_resolution_clock::now();
        double piBuffon = mc.buffonNeedle(samples, 0.5, 1.0);
        auto endTime = chrono::high_resolution_clock::now();
        
        auto duration = chrono::duration_cast<chrono::microseconds>(endTime - startTime);
        if (isfinite(piBuffon)) {
            printf("投掷次数: %d, π ≈ %.6f, 误差: %.6f, 时间: %ld μs\n", 
                   samples, piBuffon, abs(M_PI - piBuffon), duration.count());
        } else {
            printf("投掷次数: %d, π ≈ 无穷大, 时间: %ld μs\n", samples, duration.count());
        }
    }
    
    return 0;
}