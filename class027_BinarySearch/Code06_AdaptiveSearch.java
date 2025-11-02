package class189;

import java.util.*;

/**
 * 自适应查询算法实现
 * 
 * 核心思想：
 * 1. 根据历史查询结果动态调整查询策略
 * 2. 使用反馈信息优化后续查询位置
 * 3. 最小化总查询次数
 * 
 * 应用场景：
 * 1. 智能搜索系统
 * 2. 推荐系统
 * 3. 自适应测试系统
 * 
 * 工程化考量：
 * 1. 查询策略动态调整
 * 2. 反馈信息处理
 * 3. 性能优化
 * 4. 异常处理
 */
public class Code06_AdaptiveSearch {
    
    /**
     * 查询结果反馈枚举
     */
    public enum Feedback {
        TOO_SMALL,   // 查询值太小
        TOO_LARGE,   // 查询值太大
        CORRECT      // 查询值正确
    }
    
    /**
     * 自适应查询器接口
     */
    public interface AdaptiveQuery {
        /**
         * 执行查询
         * @param value 查询值
         * @return 查询反馈
         */
        Feedback query(int value);
        
        /**
         * 获取查询次数
         */
        int getQueryCount();
        
        /**
         * 重置查询器
         */
        void reset();
    }
    
    /**
     * 模拟目标函数接口
     */
    public interface TargetFunction {
        /**
         * 目标函数
         * @param x 输入值
         * @return 目标值
         */
        int target(int x);
        
        /**
         * 获取目标值
         */
        int getTargetValue();
    }
    
    /**
     * 基础二分查询实现
     */
    public static class BinarySearchStrategy {
        private int left;
        private int right;
        private int queryCount;
        
        public BinarySearchStrategy(int minRange, int maxRange) {
            this.left = minRange;
            this.right = maxRange;
            this.queryCount = 0;
        }
        
        /**
         * 获取下一个查询值
         */
        public int getNextQuery() {
            return left + (right - left) / 2;
        }
        
        /**
         * 根据反馈更新搜索范围
         */
        public void update(Feedback feedback, int queryValue) {
            queryCount++;
            switch (feedback) {
                case TOO_SMALL:
                    left = queryValue + 1;
                    break;
                case TOO_LARGE:
                    right = queryValue - 1;
                    break;
                case CORRECT:
                    // 找到目标，不需要更新范围
                    break;
            }
        }
        
        /**
         * 获取查询次数
         */
        public int getQueryCount() {
            return queryCount;
        }
        
        /**
         * 检查是否已完成搜索
         */
        public boolean isFinished() {
            return left > right;
        }
        
        /**
         * 获取当前搜索范围
         */
        public int[] getRange() {
            return new int[]{left, right};
        }
    }
    
    /**
     * 黄金分割搜索策略
     */
    public static class GoldenSectionSearchStrategy {
        private int left;
        private int right;
        private int x1, x2;
        private int fx1, fx2;
        private int queryCount;
        private TargetFunction targetFunction;
        private static final double GOLDEN_RATIO = (Math.sqrt(5) - 1) / 2;
        
        public GoldenSectionSearchStrategy(int minRange, int maxRange, TargetFunction targetFunction) {
            this.left = minRange;
            this.right = maxRange;
            this.targetFunction = targetFunction;
            this.queryCount = 0;
            
            // 初始化两个内点
            initPoints();
        }
        
        /**
         * 初始化内点
         */
        private void initPoints() {
            int range = right - left;
            x1 = left + (int) (range * (1 - GOLDEN_RATIO));
            x2 = left + (int) (range * GOLDEN_RATIO);
            
            fx1 = Math.abs(targetFunction.target(x1) - targetFunction.getTargetValue());
            fx2 = Math.abs(targetFunction.target(x2) - targetFunction.getTargetValue());
            queryCount += 2;
        }
        
        /**
         * 获取下一个查询值
         */
        public int getNextQuery() {
            // 返回函数值较大的点，因为我们将在该点进行新的查询
            return fx1 > fx2 ? x1 : x2;
        }
        
        /**
         * 根据反馈更新搜索范围
         */
        public void update(Feedback feedback, int queryValue) {
            if (fx1 > fx2) {
                // 在x1点进行查询
                if (fx1 < fx2) {
                    // x1是更好的点，移动右边界
                    right = x2;
                    x2 = x1;
                    fx2 = fx1;
                    x1 = left + (int) ((right - left) * (1 - GOLDEN_RATIO));
                    fx1 = Math.abs(targetFunction.target(x1) - targetFunction.getTargetValue());
                } else {
                    // x2是更好的点，移动左边界
                    left = x1;
                    x1 = x2;
                    fx1 = fx2;
                    x2 = left + (int) ((right - left) * GOLDEN_RATIO);
                    fx2 = Math.abs(targetFunction.target(x2) - targetFunction.getTargetValue());
                }
            } else {
                // 在x2点进行查询
                if (fx2 < fx1) {
                    // x2是更好的点，移动左边界
                    left = x1;
                    x1 = x2;
                    fx1 = fx2;
                    x2 = left + (int) ((right - left) * GOLDEN_RATIO);
                    fx2 = Math.abs(targetFunction.target(x2) - targetFunction.getTargetValue());
                } else {
                    // x1是更好的点，移动右边界
                    right = x2;
                    x2 = x1;
                    fx2 = fx1;
                    x1 = left + (int) ((right - left) * (1 - GOLDEN_RATIO));
                    fx1 = Math.abs(targetFunction.target(x1) - targetFunction.getTargetValue());
                }
            }
            queryCount++;
        }
        
        /**
         * 获取查询次数
         */
        public int getQueryCount() {
            return queryCount;
        }
        
        /**
         * 检查是否已完成搜索
         */
        public boolean isFinished() {
            return right - left <= 1;
        }
        
        /**
         * 获取最优解
         */
        public int getOptimalSolution() {
            // 比较两个内点的函数值，返回较优的点
            return fx1 < fx2 ? x1 : x2;
        }
    }
    
    /**
     * 基于历史反馈的自适应策略
     */
    public static class AdaptiveFeedbackStrategy {
        private int left;
        private int right;
        private int queryCount;
        private List<Integer> queryHistory;
        private List<Feedback> feedbackHistory;
        private TargetFunction targetFunction;
        
        public AdaptiveFeedbackStrategy(int minRange, int maxRange, TargetFunction targetFunction) {
            this.left = minRange;
            this.right = maxRange;
            this.targetFunction = targetFunction;
            this.queryCount = 0;
            this.queryHistory = new ArrayList<>();
            this.feedbackHistory = new ArrayList<>();
        }
        
        /**
         * 获取下一个查询值
         */
        public int getNextQuery() {
            if (queryHistory.isEmpty()) {
                // 第一次查询，使用中间值
                return left + (right - left) / 2;
            }
            
            // 根据历史反馈调整查询策略
            if (queryHistory.size() == 1) {
                // 第二次查询，根据第一次的反馈决定方向
                Feedback firstFeedback = feedbackHistory.get(0);
                int firstQuery = queryHistory.get(0);
                
                if (firstFeedback == Feedback.TOO_SMALL) {
                    // 目标在右侧，查询右三分之一
                    return firstQuery + (right - firstQuery) / 3;
                } else if (firstFeedback == Feedback.TOO_LARGE) {
                    // 目标在左侧，查询左三分之一
                    return left + (firstQuery - left) / 3;
                } else {
                    // 第一次就猜对了
                    return firstQuery;
                }
            }
            
            // 更复杂的自适应策略
            // 分析最近几次的反馈模式
            return adaptiveQuerySelection();
        }
        
        /**
         * 自适应查询选择
         */
        private int adaptiveQuerySelection() {
            int size = queryHistory.size();
            int lastQuery = queryHistory.get(size - 1);
            Feedback lastFeedback = feedbackHistory.get(size - 1);
            
            // 简单的自适应策略：
            // 1. 如果连续几次反馈相同，加大步长
            // 2. 如果反馈交替变化，减小步长
            int consecutiveSame = countConsecutiveSameFeedback();
            
            if (consecutiveSame >= 2) {
                // 连续相同反馈，加大步长
                if (lastFeedback == Feedback.TOO_SMALL) {
                    int step = Math.min((right - lastQuery) / 2, (right - left) / 4);
                    return Math.min(right, lastQuery + step);
                } else {
                    int step = Math.min((lastQuery - left) / 2, (right - left) / 4);
                    return Math.max(left, lastQuery - step);
                }
            } else {
                // 使用标准二分法
                return left + (right - left) / 2;
            }
        }
        
        /**
         * 计算连续相同反馈的次数
         */
        private int countConsecutiveSameFeedback() {
            if (feedbackHistory.size() < 2) return 0;
            
            int count = 1;
            Feedback last = feedbackHistory.get(feedbackHistory.size() - 1);
            
            for (int i = feedbackHistory.size() - 2; i >= 0; i--) {
                if (feedbackHistory.get(i) == last) {
                    count++;
                } else {
                    break;
                }
            }
            
            return count;
        }
        
        /**
         * 根据反馈更新搜索范围
         */
        public void update(Feedback feedback, int queryValue) {
            queryCount++;
            queryHistory.add(queryValue);
            feedbackHistory.add(feedback);
            
            switch (feedback) {
                case TOO_SMALL:
                    left = queryValue + 1;
                    break;
                case TOO_LARGE:
                    right = queryValue - 1;
                    break;
                case CORRECT:
                    // 找到目标，不需要更新范围
                    break;
            }
        }
        
        /**
         * 获取查询次数
         */
        public int getQueryCount() {
            return queryCount;
        }
        
        /**
         * 检查是否已完成搜索
         */
        public boolean isFinished() {
            return left > right;
        }
    }
    
    /**
     * 执行自适应搜索
     */
    public static int adaptiveSearch(AdaptiveQuery query, int minRange, int maxRange) {
        BinarySearchStrategy strategy = new BinarySearchStrategy(minRange, maxRange);
        
        while (!strategy.isFinished()) {
            int nextQuery = strategy.getNextQuery();
            Feedback feedback = query.query(nextQuery);
            strategy.update(feedback, nextQuery);
            
            if (feedback == Feedback.CORRECT) {
                return nextQuery;
            }
        }
        
        return -1;  // 未找到
    }
    
    /**
     * 模拟目标函数：查找平方根
     */
    public static class SquareRootFunction implements TargetFunction {
        private int targetValue;
        
        public SquareRootFunction(int targetValue) {
            this.targetValue = targetValue;
        }
        
        @Override
        public int target(int x) {
            return x * x;
        }
        
        @Override
        public int getTargetValue() {
            return targetValue;
        }
    }
    
    /**
     * 模拟查询器
     */
    public static class SimulatedQuery implements AdaptiveQuery {
        private TargetFunction targetFunction;
        private int queryCount;
        
        public SimulatedQuery(TargetFunction targetFunction) {
            this.targetFunction = targetFunction;
            this.queryCount = 0;
        }
        
        @Override
        public Feedback query(int value) {
            queryCount++;
            int result = targetFunction.target(value);
            int target = targetFunction.getTargetValue();
            
            if (result == target) {
                return Feedback.CORRECT;
            } else if (result < target) {
                return Feedback.TOO_SMALL;
            } else {
                return Feedback.TOO_LARGE;
            }
        }
        
        @Override
        public int getQueryCount() {
            return queryCount;
        }
        
        @Override
        public void reset() {
            queryCount = 0;
        }
    }
    
    // 测试方法
    public static void main(String[] args) {
        // 测试查找平方根
        int target = 64;  // 目标值
        SquareRootFunction function = new SquareRootFunction(target);
        SimulatedQuery query = new SimulatedQuery(function);
        
        System.out.println("查找 " + target + " 的平方根");
        System.out.println("搜索范围：[1, 100]");
        System.out.println();
        
        // 测试基础二分查找策略
        query.reset();
        BinarySearchStrategy binaryStrategy = new BinarySearchStrategy(1, 100);
        int result1 = -1;
        
        while (!binaryStrategy.isFinished() && binaryStrategy.getQueryCount() < 20) {
            int nextQuery = binaryStrategy.getNextQuery();
            Feedback feedback = query.query(nextQuery);
            binaryStrategy.update(feedback, nextQuery);
            
            if (feedback == Feedback.CORRECT) {
                result1 = nextQuery;
                break;
            }
        }
        
        System.out.println("基础二分查找结果：" + result1);
        System.out.println("查询次数：" + query.getQueryCount());
        System.out.println();
        
        // 测试黄金分割搜索策略
        query.reset();
        GoldenSectionSearchStrategy goldenStrategy = new GoldenSectionSearchStrategy(1, 100, function);
        int result2 = -1;
        
        while (!goldenStrategy.isFinished() && goldenStrategy.getQueryCount() < 20) {
            int nextQuery = goldenStrategy.getNextQuery();
            Feedback feedback = query.query(nextQuery);
            goldenStrategy.update(feedback, nextQuery);
        }
        
        result2 = goldenStrategy.getOptimalSolution();
        System.out.println("黄金分割搜索结果：" + result2);
        System.out.println("查询次数：" + query.getQueryCount());
        System.out.println();
        
        // 测试自适应反馈策略
        query.reset();
        AdaptiveFeedbackStrategy adaptiveStrategy = new AdaptiveFeedbackStrategy(1, 100, function);
        int result3 = -1;
        
        while (!adaptiveStrategy.isFinished() && adaptiveStrategy.getQueryCount() < 20) {
            int nextQuery = adaptiveStrategy.getNextQuery();
            Feedback feedback = query.query(nextQuery);
            adaptiveStrategy.update(feedback, nextQuery);
            
            if (feedback == Feedback.CORRECT) {
                result3 = nextQuery;
                break;
            }
        }
        
        System.out.println("自适应反馈策略结果：" + result3);
        System.out.println("查询次数：" + query.getQueryCount());
    }
}