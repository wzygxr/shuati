package class189;

import java.util.*;

/**
 * 信息论下界优化算法实现
 * 
 * 核心思想：
 * 1. 使用信息论原理计算查询的理论下界
 * 2. 最大化每次查询的信息增益
 * 3. 最小化总查询次数
 * 
 * 应用场景：
 * 1. 最优查询策略设计
 * 2. 信息检索系统
 * 3. 决策树构建
 * 
 * 工程化考量：
 * 1. 信息增益计算
 * 2. 熵值计算
 * 3. 查询策略优化
 * 4. 性能优化
 */
public class Code07_InformationTheoreticOptimization {
    
    /**
     * 信息论查询优化器
     */
    public static class InformationTheoreticOptimizer {
        private List<Integer> candidates;
        private int queryCount;
        private Map<Integer, Double> probabilityDistribution;
        
        public InformationTheoreticOptimizer(List<Integer> initialCandidates) {
            this.candidates = new ArrayList<>(initialCandidates);
            this.queryCount = 0;
            this.probabilityDistribution = new HashMap<>();
            
            // 初始化均匀分布
            double probability = 1.0 / candidates.size();
            for (int candidate : candidates) {
                probabilityDistribution.put(candidate, probability);
            }
        }
        
        /**
         * 计算当前状态的熵值
         */
        public double calculateEntropy() {
            double entropy = 0.0;
            for (double probability : probabilityDistribution.values()) {
                if (probability > 0) {
                    entropy -= probability * Math.log(probability) / Math.log(2);
                }
            }
            return entropy;
        }
        
        /**
         * 计算查询的信息增益
         */
        public double calculateInformationGain(int queryValue) {
            // 当前熵值
            double currentEntropy = calculateEntropy();
            
            // 模拟查询结果
            // 假设查询结果有三种可能：小于、等于、大于
            double probLess = 0.0, probEqual = 0.0, probGreater = 0.0;
            
            for (Map.Entry<Integer, Double> entry : probabilityDistribution.entrySet()) {
                int candidate = entry.getKey();
                double probability = entry.getValue();
                
                if (candidate < queryValue) {
                    probLess += probability;
                } else if (candidate == queryValue) {
                    probEqual += probability;
                } else {
                    probGreater += probability;
                }
            }
            
            // 计算条件熵
            double conditionalEntropy = 0.0;
            
            // 小于queryValue的情况
            if (probLess > 0) {
                double subEntropy = calculateSubEntropy(queryValue, true, false);
                conditionalEntropy += probLess * subEntropy;
            }
            
            // 等于queryValue的情况
            if (probEqual > 0) {
                // 如果等于，熵为0
                conditionalEntropy += probEqual * 0;
            }
            
            // 大于queryValue的情况
            if (probGreater > 0) {
                double subEntropy = calculateSubEntropy(queryValue, false, true);
                conditionalEntropy += probGreater * subEntropy;
            }
            
            // 信息增益 = 当前熵 - 条件熵
            return currentEntropy - conditionalEntropy;
        }
        
        /**
         * 计算子集的熵值
         */
        private double calculateSubEntropy(int queryValue, boolean lessThan, boolean greaterThan) {
            double subEntropy = 0.0;
            double totalProbability = 0.0;
            
            // 计算子集的总概率
            for (Map.Entry<Integer, Double> entry : probabilityDistribution.entrySet()) {
                int candidate = entry.getKey();
                double probability = entry.getValue();
                
                boolean include = false;
                if (lessThan && candidate < queryValue) {
                    include = true;
                } else if (greaterThan && candidate > queryValue) {
                    include = true;
                }
                
                if (include) {
                    totalProbability += probability;
                }
            }
            
            // 计算子集的熵
            if (totalProbability > 0) {
                for (Map.Entry<Integer, Double> entry : probabilityDistribution.entrySet()) {
                    int candidate = entry.getKey();
                    double probability = entry.getValue();
                    
                    boolean include = false;
                    if (lessThan && candidate < queryValue) {
                        include = true;
                    } else if (greaterThan && candidate > queryValue) {
                        include = true;
                    }
                    
                    if (include) {
                        double conditionalProbability = probability / totalProbability;
                        if (conditionalProbability > 0) {
                            subEntropy -= conditionalProbability * Math.log(conditionalProbability) / Math.log(2);
                        }
                    }
                }
            }
            
            return subEntropy;
        }
        
        /**
         * 选择最优查询值（最大化信息增益）
         */
        public int selectOptimalQuery() {
            if (candidates.size() == 1) {
                return candidates.get(0);
            }
            
            // 在候选值中选择信息增益最大的
            int optimalQuery = candidates.get(0);
            double maxInformationGain = -1;
            
            for (int candidate : candidates) {
                double informationGain = calculateInformationGain(candidate);
                if (informationGain > maxInformationGain) {
                    maxInformationGain = informationGain;
                    optimalQuery = candidate;
                }
            }
            
            return optimalQuery;
        }
        
        /**
         * 根据查询反馈更新候选集和概率分布
         */
        public void update(Feedback feedback, int queryValue) {
            queryCount++;
            
            List<Integer> newCandidates = new ArrayList<>();
            Map<Integer, Double> newDistribution = new HashMap<>();
            double totalProbability = 0.0;
            
            // 根据反馈更新候选集
            for (int candidate : candidates) {
                boolean keep = false;
                switch (feedback) {
                    case TOO_SMALL:
                        keep = candidate > queryValue;
                        break;
                    case TOO_LARGE:
                        keep = candidate < queryValue;
                        break;
                    case CORRECT:
                        keep = candidate == queryValue;
                        break;
                }
                
                if (keep) {
                    newCandidates.add(candidate);
                    double probability = probabilityDistribution.get(candidate);
                    newDistribution.put(candidate, probability);
                    totalProbability += probability;
                }
            }
            
            // 归一化概率分布
            if (totalProbability > 0) {
                for (Map.Entry<Integer, Double> entry : newDistribution.entrySet()) {
                    entry.setValue(entry.getValue() / totalProbability);
                }
            }
            
            candidates = newCandidates;
            probabilityDistribution = newDistribution;
        }
        
        /**
         * 获取查询次数
         */
        public int getQueryCount() {
            return queryCount;
        }
        
        /**
         * 获取候选集大小
         */
        public int getCandidateCount() {
            return candidates.size();
        }
        
        /**
         * 获取最优候选
         */
        public int getOptimalCandidate() {
            if (candidates.size() == 1) {
                return candidates.get(0);
            }
            
            // 返回概率最大的候选
            int optimal = candidates.get(0);
            double maxProbability = -1;
            
            for (Map.Entry<Integer, Double> entry : probabilityDistribution.entrySet()) {
                if (entry.getValue() > maxProbability) {
                    maxProbability = entry.getValue();
                    optimal = entry.getKey();
                }
            }
            
            return optimal;
        }
        
        /**
         * 检查是否已完成搜索
         */
        public boolean isFinished() {
            return candidates.size() <= 1;
        }
    }
    
    /**
     * 反馈枚举
     */
    public enum Feedback {
        TOO_SMALL,   // 查询值太小
        TOO_LARGE,   // 查询值太大
        CORRECT      // 查询值正确
    }
    
    /**
     * 模拟目标函数接口
     */
    public interface TargetFunction {
        Feedback evaluate(int queryValue);
        int getTargetValue();
    }
    
    /**
     * 模拟查询器
     */
    public static class SimulatedQuery implements TargetFunction {
        private int targetValue;
        private int queryCount;
        
        public SimulatedQuery(int targetValue) {
            this.targetValue = targetValue;
            this.queryCount = 0;
        }
        
        @Override
        public Feedback evaluate(int queryValue) {
            queryCount++;
            if (queryValue == targetValue) {
                return Feedback.CORRECT;
            } else if (queryValue < targetValue) {
                return Feedback.TOO_SMALL;
            } else {
                return Feedback.TOO_LARGE;
            }
        }
        
        @Override
        public int getTargetValue() {
            return targetValue;
        }
        
        public int getQueryCount() {
            return queryCount;
        }
        
        public void reset() {
            queryCount = 0;
        }
    }
    
    /**
     * 计算理论下界（信息论）
     */
    public static double calculateTheoreticalLowerBound(int candidateCount) {
        // 理论下界是 log2(candidateCount)
        return Math.log(candidateCount) / Math.log(2);
    }
    
    /**
     * 信息论优化搜索
     */
    public static int informationTheoreticSearch(List<Integer> candidates, TargetFunction targetFunction) {
        InformationTheoreticOptimizer optimizer = new InformationTheoreticOptimizer(candidates);
        
        while (!optimizer.isFinished()) {
            // 选择最优查询值
            int queryValue = optimizer.selectOptimalQuery();
            
            // 执行查询
            Feedback feedback = targetFunction.evaluate(queryValue);
            
            // 更新状态
            optimizer.update(feedback, queryValue);
            
            // 如果找到了目标，直接返回
            if (feedback == Feedback.CORRECT) {
                return queryValue;
            }
        }
        
        // 返回最优候选
        return optimizer.getOptimalCandidate();
    }
    
    /**
     * 比较不同搜索策略的效率
     */
    public static void compareSearchStrategies(int target, int minRange, int maxRange) {
        // 创建候选集
        List<Integer> candidates = new ArrayList<>();
        for (int i = minRange; i <= maxRange; i++) {
            candidates.add(i);
        }
        
        // 计算理论下界
        double theoreticalLowerBound = calculateTheoreticalLowerBound(candidates.size());
        System.out.println("候选集大小：" + candidates.size());
        System.out.println("理论下界：" + String.format("%.2f", theoreticalLowerBound) + " 次查询");
        System.out.println();
        
        // 模拟查询器
        SimulatedQuery query = new SimulatedQuery(target);
        
        // 1. 信息论优化搜索
        query.reset();
        int result1 = informationTheoreticSearch(new ArrayList<>(candidates), query);
        int queries1 = query.getQueryCount();
        double efficiency1 = queries1 / theoreticalLowerBound;
        
        System.out.println("信息论优化搜索：");
        System.out.println("  结果：" + result1);
        System.out.println("  查询次数：" + queries1);
        System.out.println("  效率：" + String.format("%.2f", efficiency1) + " 倍理论下界");
        System.out.println();
        
        // 2. 标准二分搜索（用于对比）
        query.reset();
        int left = minRange;
        int right = maxRange;
        int result2 = -1;
        int queries2 = 0;
        
        while (left <= right) {
            queries2++;
            int mid = left + (right - left) / 2;
            Feedback feedback = query.evaluate(mid);
            
            if (feedback == Feedback.CORRECT) {
                result2 = mid;
                break;
            } else if (feedback == Feedback.TOO_SMALL) {
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }
        
        double efficiency2 = queries2 / theoreticalLowerBound;
        
        System.out.println("标准二分搜索：");
        System.out.println("  结果：" + result2);
        System.out.println("  查询次数：" + queries2);
        System.out.println("  效率：" + String.format("%.2f", efficiency2) + " 倍理论下界");
        System.out.println();
        
        // 3. 线性搜索（用于对比）
        query.reset();
        int result3 = -1;
        int queries3 = 0;
        
        for (int i = minRange; i <= maxRange; i++) {
            queries3++;
            Feedback feedback = query.evaluate(i);
            if (feedback == Feedback.CORRECT) {
                result3 = i;
                break;
            }
        }
        
        double efficiency3 = queries3 / theoreticalLowerBound;
        
        System.out.println("线性搜索：");
        System.out.println("  结果：" + result3);
        System.out.println("  查询次数：" + queries3);
        System.out.println("  效率：" + String.format("%.2f", efficiency3) + " 倍理论下界");
    }
    
    // 测试方法
    public static void main(String[] args) {
        int target = 73;
        int minRange = 1;
        int maxRange = 100;
        
        System.out.println("查找目标值：" + target);
        System.out.println("搜索范围：[" + minRange + ", " + maxRange + "]");
        System.out.println();
        
        compareSearchStrategies(target, minRange, maxRange);
    }
}