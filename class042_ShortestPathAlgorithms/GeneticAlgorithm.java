package class065;

import java.util.*;

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

public class GeneticAlgorithm {
    
    // 种群大小
    private int populationSize;
    // 染色体长度（问题维度）
    private int chromosomeLength;
    // 最大迭代代数
    private int maxGenerations;
    // 交叉概率
    private double crossoverRate;
    // 变异概率
    private double mutationRate;
    // 当前种群
    private List<List<Integer>> population;
    // 适应度数组
    private List<Double> fitness;
    // 最优个体
    private List<Integer> bestIndividual;
    // 最优适应度值
    private double bestFitness;
    // 随机数生成器
    private Random random;
    
    /**
     * 构造函数
     * @param populationSize 种群大小
     * @param chromosomeLength 染色体长度
     * @param maxGenerations 最大迭代代数
     * @param crossoverRate 交叉概率
     * @param mutationRate 变异概率
     */
    public GeneticAlgorithm(int populationSize, int chromosomeLength, int maxGenerations,
                           double crossoverRate, double mutationRate) {
        this.populationSize = populationSize;
        this.chromosomeLength = chromosomeLength;
        this.maxGenerations = maxGenerations;
        this.crossoverRate = crossoverRate;
        this.mutationRate = mutationRate;
        this.population = new ArrayList<>();
        this.fitness = new ArrayList<>();
        this.bestIndividual = new ArrayList<>();
        this.random = new Random();
    }
    
    /**
     * 初始化种群
     */
    public void initializePopulation() {
        population.clear();
        fitness.clear();
        
        // 随机生成初始种群
        for (int i = 0; i < populationSize; i++) {
            List<Integer> individual = new ArrayList<>();
            for (int j = 0; j < chromosomeLength; j++) {
                // 对于二进制编码，基因值为0或1
                individual.add(random.nextInt(2));
            }
            population.add(individual);
        }
    }
    
    /**
     * 计算适应度 - 需要根据具体问题定义
     * 这里以最大化函数 f(x) = sum(x_i) 为例（二进制编码）
     * @param individual 个体（染色体）
     * @return 适应度值
     */
    public double calculateFitness(List<Integer> individual) {
        int sum = 0;
        for (int gene : individual) {
            sum += gene;
        }
        return sum;
    }
    
    /**
     * 评估整个种群的适应度
     */
    public void evaluatePopulation() {
        fitness.clear();
        double maxFitness = Double.NEGATIVE_INFINITY;
        int bestIndex = 0;
        
        for (int i = 0; i < populationSize; i++) {
            double fit = calculateFitness(population.get(i));
            fitness.add(fit);
            
            // 更新最优个体
            if (fit > maxFitness) {
                maxFitness = fit;
                bestIndex = i;
            }
        }
        
        // 更新全局最优
        if (maxFitness > bestFitness) {
            bestFitness = maxFitness;
            bestIndividual = new ArrayList<>(population.get(bestIndex));
        }
    }
    
    /**
     * 选择操作 - 轮盘赌选择
     * @return 选中的个体索引
     */
    public int select() {
        // 计算总适应度
        double totalFitness = 0;
        for (double fit : fitness) {
            totalFitness += fit;
        }
        
        // 如果总适应度为0，则随机选择
        if (totalFitness <= 0) {
            return random.nextInt(populationSize);
        }
        
        // 轮盘赌选择
        double pick = random.nextDouble() * totalFitness;
        double currentSum = 0;
        for (int i = 0; i < populationSize; i++) {
            currentSum += fitness.get(i);
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
    public List<List<Integer>> crossover(List<Integer> parent1, List<Integer> parent2) {
        List<List<Integer>> offspring = new ArrayList<>();
        
        // 以一定概率进行交叉
        if (random.nextDouble() > crossoverRate) {
            offspring.add(new ArrayList<>(parent1));
            offspring.add(new ArrayList<>(parent2));
            return offspring;
        }
        
        // 随机选择交叉点
        int crossoverPoint = random.nextInt(chromosomeLength);
        
        // 创建子代
        List<Integer> child1 = new ArrayList<>();
        List<Integer> child2 = new ArrayList<>();
        
        // 执行单点交叉
        for (int i = 0; i < chromosomeLength; i++) {
            if (i < crossoverPoint) {
                child1.add(parent1.get(i));
                child2.add(parent2.get(i));
            } else {
                child1.add(parent2.get(i));
                child2.add(parent1.get(i));
            }
        }
        
        offspring.add(child1);
        offspring.add(child2);
        return offspring;
    }
    
    /**
     * 变异操作 - 位翻转变异
     * @param individual 个体
     */
    public void mutate(List<Integer> individual) {
        for (int i = 0; i < chromosomeLength; i++) {
            // 以一定概率进行变异
            if (random.nextDouble() < mutationRate) {
                // 位翻转
                individual.set(i, 1 - individual.get(i));
            }
        }
    }
    
    /**
     * 生成新种群
     */
    public void generateNewPopulation() {
        List<List<Integer>> newPopulation = new ArrayList<>();
        
        // 保留最优个体（精英策略）
        int bestIndex = 0;
        double maxFitness = Double.NEGATIVE_INFINITY;
        for (int i = 0; i < populationSize; i++) {
            if (fitness.get(i) > maxFitness) {
                maxFitness = fitness.get(i);
                bestIndex = i;
            }
        }
        newPopulation.add(new ArrayList<>(population.get(bestIndex)));
        
        // 生成其余个体
        while (newPopulation.size() < populationSize) {
            // 选择两个父代
            int parent1Index = select();
            int parent2Index = select();
            
            // 交叉
            List<List<Integer>> offspring = crossover(
                population.get(parent1Index), 
                population.get(parent2Index)
            );
            
            // 变异
            for (List<Integer> child : offspring) {
                mutate(child);
                newPopulation.add(child);
                
                // 如果新种群已满，跳出循环
                if (newPopulation.size() >= populationSize) {
                    break;
                }
            }
        }
        
        // 确保种群大小不变
        while (newPopulation.size() > populationSize) {
            newPopulation.remove(newPopulation.size() - 1);
        }
        
        population = newPopulation;
    }
    
    /**
     * 执行遗传算法
     * @return 最优个体
     */
    public List<Integer> solve() {
        // 初始化
        initializePopulation();
        bestFitness = Double.NEGATIVE_INFINITY;
        
        // 迭代进化
        for (int generation = 0; generation < maxGenerations; generation++) {
            // 评估适应度
            evaluatePopulation();
            
            // 生成新种群
            generateNewPopulation();
            
            // 可选：打印当前进度
            // System.out.printf("Generation %d: Best Fitness = %.2f%n", generation + 1, bestFitness);
        }
        
        return bestIndividual;
    }
    
    /**
     * 获取最优适应度值
     * @return 最优适应度值
     */
    public double getBestFitness() {
        return bestFitness;
    }
    
    /**
     * 测试示例
     */
    public static void main(String[] args) {
        // 设置算法参数
        int populationSize = 100;      // 种群大小
        int chromosomeLength = 20;     // 染色体长度
        int maxGenerations = 100;     // 最大迭代代数
        double crossoverRate = 0.8;   // 交叉概率
        double mutationRate = 0.01;   // 变异概率
        
        // 创建遗传算法实例
        GeneticAlgorithm ga = new GeneticAlgorithm(
            populationSize, chromosomeLength, maxGenerations, 
            crossoverRate, mutationRate
        );
        
        // 执行算法
        System.out.println("开始执行遗传算法...");
        long startTime = System.currentTimeMillis();
        List<Integer> result = ga.solve();
        long endTime = System.currentTimeMillis();
        
        // 输出结果
        System.out.println("算法执行完成！");
        System.out.print("最优个体: [");
        for (int i = 0; i < result.size(); i++) {
            System.out.print(result.get(i));
            if (i < result.size() - 1) System.out.print(", ");
        }
        System.out.println("]");
        System.out.printf("最优适应度: %.2f%n", ga.getBestFitness());
        System.out.printf("执行时间: %d ms%n", endTime - startTime);
        
        // 验证结果 (理论上最优解应该全为1)
        System.out.println("\n结果分析:");
        System.out.println("理论最优个体: 全1向量");
        System.out.printf("理论最优适应度: %d%n", chromosomeLength);
        System.out.printf("误差: %.2f%n", chromosomeLength - ga.getBestFitness());
    }
}