package class065;

import java.util.*;

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

public class AntColonyOptimization {
    
    // 城市数量
    private int numCities;
    // 蚂蚁数量
    private int numAnts;
    // 迭代次数
    private int maxIterations;
    // 信息素重要程度参数
    private double alpha;
    // 启发因子重要程度参数
    private double beta;
    // 信息素挥发系数
    private double rho;
    // 信息素总量
    private double Q;
    // 距离矩阵
    private double[][] distanceMatrix;
    // 信息素矩阵
    private double[][] pheromoneMatrix;
    // 最优路径
    private List<Integer> bestTour;
    // 最优路径长度
    private double bestTourLength;
    // 随机数生成器
    private Random random;
    
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
    public AntColonyOptimization(int numCities, int numAnts, int maxIterations,
                                double alpha, double beta, double rho, double Q) {
        this.numCities = numCities;
        this.numAnts = numAnts;
        this.maxIterations = maxIterations;
        this.alpha = alpha;
        this.beta = beta;
        this.rho = rho;
        this.Q = Q;
        this.distanceMatrix = new double[numCities][numCities];
        this.pheromoneMatrix = new double[numCities][numCities];
        this.bestTour = new ArrayList<>();
        this.random = new Random();
    }
    
    /**
     * 设置距离矩阵
     * @param distances 距离矩阵
     */
    public void setDistanceMatrix(double[][] distances) {
        this.distanceMatrix = distances;
    }
    
    /**
     * 初始化信息素矩阵
     * @param initialValue 初始信息素值
     */
    public void initializePheromone(double initialValue) {
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
    public double calculateTourLength(List<Integer> tour) {
        double length = 0;
        for (int i = 0; i < tour.size() - 1; i++) {
            length += distanceMatrix[tour.get(i)][tour.get(i + 1)];
        }
        // 回到起点
        length += distanceMatrix[tour.get(tour.size() - 1)][tour.get(0)];
        return length;
    }
    
    /**
     * 选择下一个城市
     * @param currentCity 当前城市
     * @param visited 已访问城市集合
     * @return 下一个城市
     */
    public int selectNextCity(int currentCity, Set<Integer> visited) {
        // 计算转移概率
        double[] probabilities = new double[numCities];
        double sum = 0;
        
        // 计算所有未访问城市的转移概率
        for (int i = 0; i < numCities; i++) {
            if (!visited.contains(i)) {
                // 避免除零错误
                double distance = distanceMatrix[currentCity][i];
                if (distance == 0) {
                    probabilities[i] = 0;
                } else {
                    // 状态转移规则
                    probabilities[i] = Math.pow(pheromoneMatrix[currentCity][i], alpha) * 
                                      Math.pow(1.0 / distance, beta);
                }
                sum += probabilities[i];
            }
        }
        
        // 如果所有概率都为0，则随机选择一个未访问城市
        if (sum == 0) {
            List<Integer> unvisited = new ArrayList<>();
            for (int i = 0; i < numCities; i++) {
                if (!visited.contains(i)) {
                    unvisited.add(i);
                }
            }
            return unvisited.get(random.nextInt(unvisited.size()));
        }
        
        // 轮盘赌选择
        double pick = random.nextDouble() * sum;
        double currentSum = 0;
        for (int i = 0; i < numCities; i++) {
            if (!visited.contains(i)) {
                currentSum += probabilities[i];
                if (currentSum >= pick) {
                    return i;
                }
            }
        }
        
        // 如果没有选中，则返回第一个未访问城市
        for (int i = 0; i < numCities; i++) {
            if (!visited.contains(i)) {
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
    public List<Integer> constructSolution(int antId) {
        List<Integer> tour = new ArrayList<>();
        Set<Integer> visited = new HashSet<>();
        
        // 随机选择起始城市
        int currentCity = random.nextInt(numCities);
        tour.add(currentCity);
        visited.add(currentCity);
        
        // 构建完整路径
        while (visited.size() < numCities) {
            int nextCity = selectNextCity(currentCity, visited);
            tour.add(nextCity);
            visited.add(nextCity);
            currentCity = nextCity;
        }
        
        return tour;
    }
    
    /**
     * 更新信息素
     * @param antTours 所有蚂蚁的路径
     * @param antTourLengths 所有蚂蚁的路径长度
     */
    public void updatePheromone(List<List<Integer>> antTours, List<Double> antTourLengths) {
        // 信息素挥发
        for (int i = 0; i < numCities; i++) {
            for (int j = 0; j < numCities; j++) {
                pheromoneMatrix[i][j] *= (1 - rho);
            }
        }
        
        // 信息素增强
        for (int ant = 0; ant < numAnts; ant++) {
            double contribution = Q / antTourLengths.get(ant);
            List<Integer> tour = antTours.get(ant);
            
            for (int i = 0; i < tour.size() - 1; i++) {
                int from = tour.get(i);
                int to = tour.get(i + 1);
                pheromoneMatrix[from][to] += contribution;
                pheromoneMatrix[to][from] += contribution; // 对称矩阵
            }
            
            // 连接最后一个城市和第一个城市
            int last = tour.get(tour.size() - 1);
            int first = tour.get(0);
            pheromoneMatrix[last][first] += contribution;
            pheromoneMatrix[first][last] += contribution;
        }
    }
    
    /**
     * 执行蚁群算法
     * @return 最优路径
     */
    public List<Integer> solve() {
        // 初始化信息素
        initializePheromone(1.0);
        bestTourLength = Double.POSITIVE_INFINITY;
        
        // 迭代优化
        for (int iteration = 0; iteration < maxIterations; iteration++) {
            List<List<Integer>> antTours = new ArrayList<>();
            List<Double> antTourLengths = new ArrayList<>();
            
            // 每只蚂蚁构建解
            for (int ant = 0; ant < numAnts; ant++) {
                List<Integer> tour = constructSolution(ant);
                double tourLength = calculateTourLength(tour);
                
                antTours.add(tour);
                antTourLengths.add(tourLength);
                
                // 更新全局最优解
                if (tourLength < bestTourLength) {
                    bestTourLength = tourLength;
                    bestTour = new ArrayList<>(tour);
                }
            }
            
            // 更新信息素
            updatePheromone(antTours, antTourLengths);
            
            // 可选：打印当前进度
            // System.out.printf("Iteration %d: Best Tour Length = %.2f%n", iteration + 1, bestTourLength);
        }
        
        return bestTour;
    }
    
    /**
     * 获取最优路径长度
     * @return 最优路径长度
     */
    public double getBestTourLength() {
        return bestTourLength;
    }
    
    /**
     * 测试示例 - 对称TSP问题
     */
    public static void main(String[] args) {
        // 创建一个简单的TSP实例（5个城市）
        int numCities = 5;
        int numAnts = 10;
        int maxIterations = 100;
        double alpha = 1.0;   // 信息素重要程度
        double beta = 2.0;    // 启发因子重要程度
        double rho = 0.5;     // 信息素挥发系数
        double Q = 100.0;     // 信息素总量
        
        // 距离矩阵（对称TSP）
        double[][] distances = {
            {0, 10, 15, 20, 25},
            {10, 0, 35, 25, 30},
            {15, 35, 0, 30, 20},
            {20, 25, 30, 0, 15},
            {25, 30, 20, 15, 0}
        };
        
        // 创建蚁群算法实例
        AntColonyOptimization aco = new AntColonyOptimization(
            numCities, numAnts, maxIterations, alpha, beta, rho, Q
        );
        aco.setDistanceMatrix(distances);
        
        // 执行算法
        System.out.println("开始执行蚁群算法...");
        long startTime = System.currentTimeMillis();
        List<Integer> result = aco.solve();
        long endTime = System.currentTimeMillis();
        
        // 输出结果
        System.out.println("算法执行完成！");
        System.out.print("最优路径: ");
        for (int i = 0; i < result.size(); i++) {
            System.out.print(result.get(i));
            if (i < result.size() - 1) System.out.print(" -> ");
        }
        System.out.println(" -> " + result.get(0)); // 回到起点
        System.out.printf("最优路径长度: %.2f%n", aco.getBestTourLength());
        System.out.printf("执行时间: %d ms%n", endTime - startTime);
    }
}