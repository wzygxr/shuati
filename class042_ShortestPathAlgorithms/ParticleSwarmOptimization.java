package class065;

import java.util.*;

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

public class ParticleSwarmOptimization {
    
    // 粒子类
    static class Particle {
        double[] position;      // 位置
        double[] velocity;      // 速度
        double[] bestPosition;  // 个体最优位置
        double bestValue;       // 个体最优值
        
        Particle(int dimension) {
            position = new double[dimension];
            velocity = new double[dimension];
            bestPosition = new double[dimension];
            bestValue = Double.POSITIVE_INFINITY;
        }
    }
    
    // 粒子数量
    private int numParticles;
    // 问题维度
    private int dimension;
    // 最大迭代次数
    private int maxIterations;
    // 惯性权重
    private double w;
    // 个体学习因子
    private double c1;
    // 社会学习因子
    private double c2;
    // 位置上下界
    private double[] lowerBounds;
    private double[] upperBounds;
    // 速度上下界
    private double[] velocityBounds;
    // 粒子群
    private Particle[] particles;
    // 全局最优位置
    private double[] globalBestPosition;
    // 全局最优值
    private double globalBestValue;
    // 随机数生成器
    private Random random;
    
    /**
     * 构造函数
     * @param numParticles 粒子数量
     * @param dimension 问题维度
     * @param maxIterations 最大迭代次数
     * @param w 惯性权重
     * @param c1 个体学习因子
     * @param c2 社会学习因子
     */
    public ParticleSwarmOptimization(int numParticles, int dimension, int maxIterations,
                                   double w, double c1, double c2) {
        this.numParticles = numParticles;
        this.dimension = dimension;
        this.maxIterations = maxIterations;
        this.w = w;
        this.c1 = c1;
        this.c2 = c2;
        this.particles = new Particle[numParticles];
        this.globalBestPosition = new double[dimension];
        this.globalBestValue = Double.POSITIVE_INFINITY;
        this.random = new Random();
    }
    
    /**
     * 设置边界
     * @param lowerBounds 位置下界
     * @param upperBounds 位置上界
     */
    public void setBounds(double[] lowerBounds, double[] upperBounds) {
        this.lowerBounds = lowerBounds.clone();
        this.upperBounds = upperBounds.clone();
        
        // 设置速度边界为位置边界范围的10%
        velocityBounds = new double[dimension];
        for (int i = 0; i < dimension; i++) {
            velocityBounds[i] = 0.1 * (upperBounds[i] - lowerBounds[i]);
        }
    }
    
    /**
     * 初始化粒子群
     */
    public void initializeParticles() {
        for (int i = 0; i < numParticles; i++) {
            particles[i] = new Particle(dimension);
            
            // 随机初始化位置和速度
            for (int j = 0; j < dimension; j++) {
                // 初始化位置
                particles[i].position[j] = lowerBounds[j] + 
                    random.nextDouble() * (upperBounds[j] - lowerBounds[j]);
                
                // 初始化速度
                particles[i].velocity[j] = -velocityBounds[j] + 
                    random.nextDouble() * (2 * velocityBounds[j]);
            }
            
            // 初始化个体最优位置
            System.arraycopy(particles[i].position, 0, particles[i].bestPosition, 0, dimension);
        }
    }
    
    /**
     * 目标函数 - 需要根据具体问题定义
     * 这里以最小化函数 f(x) = sum(x_i^2) 为例
     * @param position 位置向量
     * @return 目标函数值
     */
    public double objectiveFunction(double[] position) {
        double sum = 0;
        for (int i = 0; i < dimension; i++) {
            sum += position[i] * position[i];
        }
        return sum;
    }
    
    /**
     * 评估粒子适应度
     */
    public void evaluateParticles() {
        for (int i = 0; i < numParticles; i++) {
            double value = objectiveFunction(particles[i].position);
            
            // 更新个体最优
            if (value < particles[i].bestValue) {
                particles[i].bestValue = value;
                System.arraycopy(particles[i].position, 0, particles[i].bestPosition, 0, dimension);
            }
            
            // 更新全局最优
            if (value < globalBestValue) {
                globalBestValue = value;
                System.arraycopy(particles[i].position, 0, globalBestPosition, 0, dimension);
            }
        }
    }
    
    /**
     * 更新粒子速度和位置
     */
    public void updateParticles() {
        for (int i = 0; i < numParticles; i++) {
            for (int j = 0; j < dimension; j++) {
                // 更新速度
                particles[i].velocity[j] = w * particles[i].velocity[j] +
                    c1 * random.nextDouble() * (particles[i].bestPosition[j] - particles[i].position[j]) +
                    c2 * random.nextDouble() * (globalBestPosition[j] - particles[i].position[j]);
                
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
    public double[] solve() {
        // 初始化
        initializeParticles();
        globalBestValue = Double.POSITIVE_INFINITY;
        
        // 迭代优化
        for (int iteration = 0; iteration < maxIterations; iteration++) {
            // 评估适应度
            evaluateParticles();
            
            // 更新粒子
            updateParticles();
            
            // 可选：打印当前进度
            // System.out.printf("Iteration %d: Global Best Value = %.10f%n", iteration + 1, globalBestValue);
        }
        
        return globalBestPosition.clone();
    }
    
    /**
     * 获取全局最优值
     * @return 全局最优值
     */
    public double getGlobalBestValue() {
        return globalBestValue;
    }
    
    /**
     * 测试示例
     */
    public static void main(String[] args) {
        // 设置算法参数
        int numParticles = 30;      // 粒子数量
        int dimension = 10;         // 问题维度
        int maxIterations = 1000;   // 最大迭代次数
        double w = 0.7;             // 惯性权重
        double c1 = 1.5;            // 个体学习因子
        double c2 = 1.5;            // 社会学习因子
        
        // 位置边界
        double[] lowerBounds = new double[dimension];
        double[] upperBounds = new double[dimension];
        for (int i = 0; i < dimension; i++) {
            lowerBounds[i] = -10.0;
            upperBounds[i] = 10.0;
        }
        
        // 创建粒子群优化算法实例
        ParticleSwarmOptimization pso = new ParticleSwarmOptimization(
            numParticles, dimension, maxIterations, w, c1, c2
        );
        pso.setBounds(lowerBounds, upperBounds);
        
        // 执行算法
        System.out.println("开始执行粒子群优化算法...");
        long startTime = System.currentTimeMillis();
        double[] result = pso.solve();
        long endTime = System.currentTimeMillis();
        
        // 输出结果
        System.out.println("算法执行完成！");
        System.out.print("最优位置: [");
        for (int i = 0; i < result.length; i++) {
            System.out.printf("%.6f", result[i]);
            if (i < result.length - 1) System.out.print(", ");
        }
        System.out.println("]");
        System.out.printf("最优值: %.10f%n", pso.getGlobalBestValue());
        System.out.printf("执行时间: %d ms%n", endTime - startTime);
        
        // 验证结果 (理论上最优解应该接近全0向量)
        System.out.println("\n结果分析:");
        System.out.println("理论最优位置: 全0向量");
        System.out.println("理论最优值: 0");
        System.out.printf("误差: %.10f%n", Math.abs(pso.getGlobalBestValue()));
    }
}