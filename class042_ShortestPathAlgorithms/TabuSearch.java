package class065;

import java.util.*;

/**
 * 禁忌搜索算法 (Tabu Search)
 * 
 * 算法原理：
 * 禁忌搜索是一种局部搜索的改进算法，通过引入禁忌表来避免循环搜索和陷入局部最优。
 * 算法允许接受劣解，以尽可能地搜索解空间的不同区域。
 * 
 * 算法特点：
 * 1. 属于元启发式算法，适用于解决组合优化问题
 * 2. 通过禁忌表避免循环搜索
 * 3. 具有较强的爬山能力
 * 4. 可以跳出局部最优解
 * 
 * 应用场景：
 * - 旅行商问题(TSP)
 * - 调度问题
 * - 图着色问题
 * - 背包问题
 * - 车间调度问题
 * 
 * 算法流程：
 * 1. 初始化当前解和禁忌表
 * 2. 循环迭代：
 *    a. 在邻域中寻找非禁忌的最佳移动
 *    b. 执行移动，更新当前解
 *    c. 更新禁忌表
 *    d. 更新全局最优解
 * 3. 直到满足终止条件
 * 
 * 时间复杂度：O(G×N)，G为迭代次数，N为邻域大小
 * 空间复杂度：O(T)，T为禁忌表大小
 */

public class TabuSearch {
    
    // 最大迭代次数
    private int maxIterations;
    // 禁忌表长度
    private int tabuTenure;
    // 邻域大小
    private int neighborhoodSize;
    // 禁忌表
    private List<List<Integer>> tabuList;
    // 最优解
    private List<Integer> bestSolution;
    // 最优目标函数值
    private double bestValue;
    // 随机数生成器
    private Random random;
    
    /**
     * 构造函数
     * @param maxIterations 最大迭代次数
     * @param tabuTenure 禁忌表长度
     * @param neighborhoodSize 邻域大小
     */
    public TabuSearch(int maxIterations, int tabuTenure, int neighborhoodSize) {
        this.maxIterations = maxIterations;
        this.tabuTenure = tabuTenure;
        this.neighborhoodSize = neighborhoodSize;
        this.tabuList = new ArrayList<>();
        this.random = new Random();
    }
    
    /**
     * 初始化解 - 需要根据具体问题定义
     * @param dimension 解的维度
     * @return 初始解
     */
    public List<Integer> initializeSolution(int dimension) {
        List<Integer> solution = new ArrayList<>();
        for (int i = 0; i < dimension; i++) {
            solution.add(random.nextInt(2)); // 二进制编码
        }
        return solution;
    }
    
    /**
     * 目标函数 - 需要根据具体问题定义
     * 这里以最大化函数 f(x) = sum(x_i) 为例（二进制编码）
     * @param solution 解
     * @return 目标函数值
     */
    public double objectiveFunction(List<Integer> solution) {
        int sum = 0;
        for (int gene : solution) {
            sum += gene;
        }
        return sum;
    }
    
    /**
     * 生成邻域解
     * @param solution 当前解
     * @return 邻域解集合
     */
    public List<List<Integer>> generateNeighborhood(List<Integer> solution) {
        List<List<Integer>> neighborhood = new ArrayList<>();
        
        // 通过翻转一位生成邻域解
        for (int i = 0; i < Math.min(neighborhoodSize, solution.size()); i++) {
            List<Integer> neighbor = new ArrayList<>(solution);
            // 翻转第i位
            neighbor.set(i, 1 - neighbor.get(i));
            neighborhood.add(neighbor);
        }
        
        return neighborhood;
    }
    
    /**
     * 检查移动是否在禁忌表中
     * @param move 移动操作
     * @return 是否在禁忌表中
     */
    public boolean isTabu(List<Integer> move) {
        for (List<Integer> tabuMove : tabuList) {
            if (tabuMove.equals(move)) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * 更新禁忌表
     * @param move 新的移动操作
     */
    public void updateTabuList(List<Integer> move) {
        // 添加新移动到禁忌表
        tabuList.add(new ArrayList<>(move));
        
        // 如果禁忌表超过长度限制，移除最老的移动
        if (tabuList.size() > tabuTenure) {
            tabuList.remove(0);
        }
    }
    
    /**
     * 执行禁忌搜索算法
     * @param dimension 解的维度
     * @return 最优解
     */
    public List<Integer> solve(int dimension) {
        // 初始化
        List<Integer> currentSolution = initializeSolution(dimension);
        double currentValue = objectiveFunction(currentSolution);
        bestSolution = new ArrayList<>(currentSolution);
        bestValue = currentValue;
        
        // 迭代优化
        for (int iteration = 0; iteration < maxIterations; iteration++) {
            // 生成邻域
            List<List<Integer>> neighborhood = generateNeighborhood(currentSolution);
            
            // 寻找最佳移动
            List<Integer> bestMove = null;
            double bestMoveValue = Double.NEGATIVE_INFINITY;
            
            for (List<Integer> neighbor : neighborhood) {
                double neighborValue = objectiveFunction(neighbor);
                
                // 如果不是禁忌移动或者优于全局最优，则考虑接受
                if (!isTabu(neighbor) || neighborValue > bestValue) {
                    if (neighborValue > bestMoveValue) {
                        bestMove = neighbor;
                        bestMoveValue = neighborValue;
                    }
                }
            }
            
            // 如果找到了有效的移动
            if (bestMove != null) {
                // 执行移动
                currentSolution = bestMove;
                currentValue = bestMoveValue;
                
                // 更新全局最优
                if (currentValue > bestValue) {
                    bestSolution = new ArrayList<>(currentSolution);
                    bestValue = currentValue;
                }
                
                // 更新禁忌表
                updateTabuList(bestMove);
            }
            
            // 可选：打印当前进度
            // System.out.printf("Iteration %d: Best Value = %.2f%n", iteration + 1, bestValue);
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
        int dimension = 20;         // 解的维度
        int maxIterations = 100;    // 最大迭代次数
        int tabuTenure = 10;        // 禁忌表长度
        int neighborhoodSize = 5;   // 邻域大小
        
        // 创建禁忌搜索算法实例
        TabuSearch ts = new TabuSearch(maxIterations, tabuTenure, neighborhoodSize);
        
        // 执行算法
        System.out.println("开始执行禁忌搜索算法...");
        long startTime = System.currentTimeMillis();
        List<Integer> result = ts.solve(dimension);
        long endTime = System.currentTimeMillis();
        
        // 输出结果
        System.out.println("算法执行完成！");
        System.out.print("最优解: [");
        for (int i = 0; i < result.size(); i++) {
            System.out.print(result.get(i));
            if (i < result.size() - 1) System.out.print(", ");
        }
        System.out.println("]");
        System.out.printf("最优值: %.2f%n", ts.getBestValue());
        System.out.printf("执行时间: %d ms%n", endTime - startTime);
        
        // 验证结果 (理论上最优解应该全为1)
        System.out.println("\n结果分析:");
        System.out.println("理论最优解: 全1向量");
        System.out.printf("理论最优值: %d%n", dimension);
        System.out.printf("误差: %.2f%n", dimension - ts.getBestValue());
    }
}