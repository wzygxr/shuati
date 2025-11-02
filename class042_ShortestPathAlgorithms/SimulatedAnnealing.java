package class065;

import java.util.*;

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

public class SimulatedAnnealing {
    
    // 当前解
    private double[] currentSolution;
    // 最优解
    private double[] bestSolution;
    // 当前目标函数值
    private double currentValue;
    // 最优目标函数值
    private double bestValue;
    // 初始温度
    private double initialTemperature;
    // 当前温度
    private double temperature;
    // 冷却系数
    private double coolingRate;
    // 终止温度
    private double minTemperature;
    // 每个温度下的迭代次数
    private int iterationsPerTemp;
    // 随机数生成器
    private Random random;
    
    /**
     * 构造函数
     * @param initialTemperature 初始温度
     * @param coolingRate 冷却系数 (0 < coolingRate < 1)
     * @param minTemperature 终止温度
     * @param iterationsPerTemp 每个温度下的迭代次数
     */
    public SimulatedAnnealing(double initialTemperature, double coolingRate, 
                             double minTemperature, int iterationsPerTemp) {
        this.initialTemperature = initialTemperature;
        this.coolingRate = coolingRate;
        this.minTemperature = minTemperature;
        this.iterationsPerTemp = iterationsPerTemp;
        this.random = new Random();
    }
    
    /**
     * 初始化解空间
     * @param dimensions 解的维度
     * @param lowerBounds 下界数组
     * @param upperBounds 上界数组
     */
    public void initializeSolution(int dimensions, double[] lowerBounds, double[] upperBounds) {
        currentSolution = new double[dimensions];
        bestSolution = new double[dimensions];
        
        // 随机初始化解
        for (int i = 0; i < dimensions; i++) {
            currentSolution[i] = lowerBounds[i] + random.nextDouble() * (upperBounds[i] - lowerBounds[i]);
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
    public double objectiveFunction(double[] solution) {
        double sum = 0;
        for (int i = 0; i < solution.length; i++) {
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
    public double[] generateNeighbor(double[] solution, double[] lowerBounds, double[] upperBounds) {
        double[] neighbor = solution.clone();
        int index = random.nextInt(solution.length);
        
        // 在当前解的基础上添加一个小的随机扰动
        double delta = (upperBounds[index] - lowerBounds[index]) * 0.1;
        neighbor[index] += (random.nextGaussian() * delta);
        
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
    public boolean metropolisCriterion(double newValue, double oldValue, double temperature) {
        // 如果新解更优，则直接接受
        if (newValue < oldValue) {
            return true;
        }
        
        // 否则以一定概率接受较差解
        double probability = Math.exp(-(newValue - oldValue) / temperature);
        return random.nextDouble() < probability;
    }
    
    /**
     * 降温函数 - 指数降温
     * @param temperature 当前温度
     * @return 新温度
     */
    public double coolDown(double temperature) {
        return temperature * coolingRate;
    }
    
    /**
     * 执行模拟退火算法
     * @param dimensions 解的维度
     * @param lowerBounds 下界数组
     * @param upperBounds 上界数组
     * @return 最优解
     */
    public double[] solve(int dimensions, double[] lowerBounds, double[] upperBounds) {
        // 初始化
        initializeSolution(dimensions, lowerBounds, upperBounds);
        temperature = initialTemperature;
        
        // 主循环 - 直到温度降到最低温度
        while (temperature > minTemperature) {
            // 在当前温度下进行多次迭代
            for (int i = 0; i < iterationsPerTemp; i++) {
                // 产生邻域解
                double[] newSolution = generateNeighbor(currentSolution, lowerBounds, upperBounds);
                double newValue = objectiveFunction(newSolution);
                
                // 根据Metropolis准则决定是否接受新解
                if (metropolisCriterion(newValue, currentValue, temperature)) {
                    // 接受新解
                    System.arraycopy(newSolution, 0, currentSolution, 0, currentSolution.length);
                    currentValue = newValue;
                    
                    // 更新最优解
                    if (currentValue < bestValue) {
                        System.arraycopy(currentSolution, 0, bestSolution, 0, currentSolution.length);
                        bestValue = currentValue;
                    }
                }
            }
            
            // 降温
            temperature = coolDown(temperature);
            
            // 可选：打印当前进度
            // System.out.printf("Temperature: %.2f, Best Value: %.6f%n", temperature, bestValue);
        }
        
        return bestSolution;
    }
    
    /**
     * 获取最优值
     * @return 最优目标函数值
     */
    public double getBestValue() {
        return bestValue;
    }
    
    /**
     * 测试示例
     */
    public static void main(String[] args) {
        // 设置算法参数
        double initialTemp = 1000.0;     // 初始温度
        double coolingRate = 0.95;       // 冷却系数
        double minTemp = 1e-8;           // 终止温度
        int iterations = 100;            // 每个温度下的迭代次数
        
        // 创建模拟退火算法实例
        SimulatedAnnealing sa = new SimulatedAnnealing(initialTemp, coolingRate, minTemp, iterations);
        
        // 定义问题参数 (以2维函数优化为例)
        int dimensions = 2;
        double[] lowerBounds = {-10, -10};  // 各维度下界
        double[] upperBounds = {10, 10};    // 各维度上界
        
        // 执行算法
        System.out.println("开始执行模拟退火算法...");
        long startTime = System.currentTimeMillis();
        double[] result = sa.solve(dimensions, lowerBounds, upperBounds);
        long endTime = System.currentTimeMillis();
        
        // 输出结果
        System.out.println("算法执行完成！");
        System.out.printf("最优解: [%.6f, %.6f]%n", result[0], result[1]);
        System.out.printf("最优值: %.10f%n", sa.getBestValue());
        System.out.printf("执行时间: %d ms%n", endTime - startTime);
        
        // 验证结果 (理论上最优解应该接近 [0, 0])
        System.out.println("\n结果分析:");
        System.out.println("理论最优解: [0, 0]");
        System.out.println("理论最优值: 0");
        System.out.printf("误差: %.10f%n", Math.abs(sa.getBestValue()));
    }
}