package class107;

import java.util.*;

/**
 * 加权蓄水池采样算法 (Weighted Reservoir Sampling)
 * 
 * 算法原理：
 * Efraimidis和Spirakis算法是加权蓄水池采样的经典算法。
 * 对于每个元素，计算 random()^(1/weight)，然后选择值最大的k个元素。
 * 
 * 算法步骤：
 * 1. 对于数据流中的每个元素(item, weight)：
 *    a. 计算 key = random()^(1/weight)
 *    b. 如果蓄水池未满，直接加入蓄水池
 *    c. 如果蓄水池已满，找到当前蓄水池中key最小的元素
 *    d. 如果当前元素的key大于最小key，则替换该元素
 * 
 * 时间复杂度：O(n*log(k))，其中n是数据流长度，k是蓄水池大小
 * 空间复杂度：O(k)
 * 
 * 应用场景：
 * 1. 带权重的数据流采样
 * 2. 推荐系统中的内容推荐
 * 3. 负载均衡中的服务器选择
 * 4. A/B测试中的用户分组
 */
public class Code02_WeightedReservoirSampling {
    
    /**
     * 加权蓄水池采样类
     */
    public static class WeightedReservoirSampler<T> {
        private int reservoirSize;  // 蓄水池大小
        private PriorityQueue<Element<T>> reservoir;  // 使用最小堆维护蓄水池
        private Random random;
        
        // 内部类，用于存储元素及其权重和随机键值
        private static class Element<T> {
            T item;
            double weight;
            double key;
            
            Element(T item, double weight) {
                this.item = item;
                this.weight = weight;
                // 计算随机键值：random()^(1/weight)
                this.key = Math.pow(Math.random(), 1.0 / weight);
            }
        }
        
        public WeightedReservoirSampler(int reservoirSize) {
            this.reservoirSize = reservoirSize;
            // 使用最小堆，按key值排序
            this.reservoir = new PriorityQueue<>((a, b) -> Double.compare(a.key, b.key));
            this.random = new Random();
        }
        
        /**
         * 向蓄水池中添加元素
         * @param item 元素
         * @param weight 权重
         */
        public void add(T item, double weight) {
            if (weight <= 0) {
                throw new IllegalArgumentException("权重必须大于0");
            }
            
            Element<T> element = new Element<>(item, weight);
            
            // 如果蓄水池未满，直接加入
            if (reservoir.size() < reservoirSize) {
                reservoir.offer(element);
            } else {
                // 如果蓄水池已满，比较当前元素与堆顶元素的key值
                Element<T> smallest = reservoir.peek();
                if (element.key > smallest.key) {
                    // 替换key值最小的元素
                    reservoir.poll();
                    reservoir.offer(element);
                }
            }
        }
        
        /**
         * 获取蓄水池中的所有元素
         * @return 元素列表
         */
        public List<T> getSample() {
            List<T> result = new ArrayList<>();
            for (Element<T> element : reservoir) {
                result.add(element.item);
            }
            return result;
        }
        
        /**
         * 获取蓄水池中的所有元素及权重
         * @return 元素及权重的映射
         */
        public List<Map.Entry<T, Double>> getSampleWithWeights() {
            List<Map.Entry<T, Double>> result = new ArrayList<>();
            for (Element<T> element : reservoir) {
                result.add(new AbstractMap.SimpleEntry<>(element.item, element.weight));
            }
            return result;
        }
    }
    
    /**
     * 简化版本的加权采样函数
     * 适用于已知完整数据集的情况
     * @param items 元素列表
     * @param weights 权重列表
     * @param k 采样数量
     * @return 采样结果
     */
    public static <T> List<T> weightedSample(List<T> items, List<Double> weights, int k) {
        if (items.size() != weights.size()) {
            throw new IllegalArgumentException("元素数量与权重数量不匹配");
        }
        
        if (k > items.size()) {
            throw new IllegalArgumentException("采样数量不能大于元素总数");
        }
        
        // 计算每个元素的随机键值
        List<Double> keys = new ArrayList<>();
        Random random = new Random();
        for (int i = 0; i < weights.size(); i++) {
            if (weights.get(i) <= 0) {
                throw new IllegalArgumentException("权重必须大于0");
            }
            // 计算 key = random()^(1/weight)
            double key = Math.pow(random.nextDouble(), 1.0 / weights.get(i));
            keys.add(key);
        }
        
        // 创建索引数组并按key值降序排序
        Integer[] indices = new Integer[items.size()];
        for (int i = 0; i < items.size(); i++) {
            indices[i] = i;
        }
        
        Arrays.sort(indices, (a, b) -> Double.compare(keys.get(b), keys.get(a)));
        
        // 选择前k个元素
        List<T> result = new ArrayList<>();
        for (int i = 0; i < k; i++) {
            result.add(items.get(indices[i]));
        }
        
        return result;
    }
    
    /**
     * 测试函数
     */
    public static void main(String[] args) {
        System.out.println("=== 加权蓄水池采样测试 ===");
        
        // 测试1: 使用WeightedReservoirSampler类
        System.out.println("\n测试1: 流式加权采样");
        WeightedReservoirSampler<String> sampler = new WeightedReservoirSampler<>(3);
        
        // 模拟数据流，包含元素及其权重
        String[] items = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J"};
        double[] weights = {1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0, 10.0};
        
        System.out.println("数据流元素及权重:");
        for (int i = 0; i < items.length; i++) {
            System.out.println(items[i] + ": " + weights[i]);
            sampler.add(items[i], weights[i]);
        }
        
        System.out.println("\n采样结果:");
        List<String> sample = sampler.getSample();
        for (String item : sample) {
            System.out.println(item);
        }
        
        // 测试2: 使用简化版加权采样函数
        System.out.println("\n测试2: 完整数据集加权采样");
        List<String> itemList = Arrays.asList(items);
        List<Double> weightList = new ArrayList<>();
        for (double w : weights) {
            weightList.add(w);
        }
        
        List<String> sample2 = weightedSample(itemList, weightList, 3);
        System.out.println("采样结果:");
        for (String item : sample2) {
            System.out.println(item);
        }
        
        // 测试3: 验证权重正确性
        System.out.println("\n测试3: 权重正确性验证");
        System.out.println("进行10000次采样，统计各元素被选中的频率:");
        
        int[] counts = new int[items.length];
        int testTimes = 10000;
        
        for (int i = 0; i < testTimes; i++) {
            List<String> result = weightedSample(itemList, weightList, 1);
            String selectedItem = result.get(0);
            
            // 找到选中元素的索引
            for (int j = 0; j < items.length; j++) {
                if (items[j].equals(selectedItem)) {
                    counts[j]++;
                    break;
                }
            }
        }
        
        System.out.println("元素\t权重\t理论概率\t实际频率");
        double totalWeight = 0;
        for (double w : weights) {
            totalWeight += w;
        }
        
        for (int i = 0; i < items.length; i++) {
            double theoreticalProb = weights[i] / totalWeight;
            double actualFreq = (double) counts[i] / testTimes;
            System.out.printf("%s\t%.1f\t%.4f\t\t%.4f\n", items[i], weights[i], theoreticalProb, actualFreq);
        }
    }
}