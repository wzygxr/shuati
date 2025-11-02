package class_advanced_algorithms.randomized_algorithms;

import java.util.*;

/**
 * 蓄水池抽样算法 (Reservoir Sampling)
 * 
 * 算法原理：
 * 蓄水池抽样是一种随机抽样算法，用于从包含n个项目的未知大小的数据流中
 * 随机选择k个样本，其中n非常大或未知。该算法能够在只遍历一次数据流的情况下，
 * 保证每个元素被选中的概率相等。
 * 
 * 算法特点：
 * 1. 适用于数据流处理
 * 2. 空间复杂度为O(k)，与数据流大小无关
 * 3. 时间复杂度为O(n)
 * 4. 保证每个元素被选中的概率相等
 * 
 * 应用场景：
 * - 大数据流的随机抽样
 * - 无法预知数据总量的抽样
 * - 在线算法
 * - 数据库查询优化
 * 
 * 算法流程：
 * 1. 将前k个元素放入蓄水池
 * 2. 对于第i个元素(i > k)：
 *    a. 以 k/i 的概率选择该元素
 *    b. 如果被选中，则随机替换蓄水池中的一个元素
 * 3. 重复步骤2直到数据流结束
 * 
 * 时间复杂度：O(n)，n为数据流大小
 * 空间复杂度：O(k)，k为样本大小
 */

public class ReservoirSampling {
    
    // 随机数生成器
    private Random random;
    
    /**
     * 构造函数
     */
    public ReservoirSampling() {
        this.random = new Random();
    }
    
    /**
     * 从数据流中随机选择k个元素（k=1的简单情况）
     * 
     * @param stream 数据流迭代器
     * @param <T> 数据类型
     * @return 随机选择的元素
     */
    public <T> T selectRandomElement(Iterator<T> stream) {
        if (!stream.hasNext()) {
            return null;
        }
        
        T result = stream.next(); // 第一个元素
        int count = 1;
        
        // 遍历剩余元素
        while (stream.hasNext()) {
            T current = stream.next();
            count++;
            
            // 以 1/count 的概率选择当前元素
            if (random.nextInt(count) == 0) {
                result = current;
            }
        }
        
        return result;
    }
    
    /**
     * 从数据流中随机选择k个元素（通用情况）
     * 
     * @param stream 数据流迭代器
     * @param k 样本大小
     * @param <T> 数据类型
     * @return 随机选择的k个元素列表
     */
    public <T> List<T> selectRandomElements(Iterator<T> stream, int k) {
        List<T> reservoir = new ArrayList<>();
        
        // 将前k个元素放入蓄水池
        int i = 0;
        while (stream.hasNext() && i < k) {
            reservoir.add(stream.next());
            i++;
        }
        
        // 如果数据流元素少于k个，直接返回
        if (i < k) {
            return reservoir;
        }
        
        // 处理剩余元素
        int count = k;
        while (stream.hasNext()) {
            T current = stream.next();
            count++;
            
            // 以 k/count 的概率选择当前元素
            int j = random.nextInt(count);
            if (j < k) {
                reservoir.set(j, current);
            }
        }
        
        return reservoir;
    }
    
    /**
     * 从数组中随机选择k个元素
     * 
     * @param array 输入数组
     * @param k 样本大小
     * @param <T> 数据类型
     * @return 随机选择的k个元素数组
     */
    public <T> T[] selectRandomElements(T[] array, int k) {
        if (array.length <= k) {
            return array.clone();
        }
        
        // 创建结果数组
        T[] result = Arrays.copyOf(array, k);
        
        // 处理前k个元素
        for (int i = k; i < array.length; i++) {
            // 以 k/(i+1) 的概率选择当前元素
            int j = random.nextInt(i + 1);
            if (j < k) {
                result[j] = array[i];
            }
        }
        
        return result;
    }
    
    /**
     * 从列表中随机选择k个元素
     * 
     * @param list 输入列表
     * @param k 样本大小
     * @param <T> 数据类型
     * @return 随机选择的k个元素列表
     */
    public <T> List<T> selectRandomElements(List<T> list, int k) {
        if (list.size() <= k) {
            return new ArrayList<>(list);
        }
        
        // 创建结果列表
        List<T> result = new ArrayList<>(list.subList(0, k));
        
        // 处理剩余元素
        for (int i = k; i < list.size(); i++) {
            // 以 k/(i+1) 的概率选择当前元素
            int j = random.nextInt(i + 1);
            if (j < k) {
                result.set(j, list.get(i));
            }
        }
        
        return result;
    }
    
    /**
     * 测试示例
     */
    public static void main(String[] args) {
        ReservoirSampling rs = new ReservoirSampling();
        
        System.out.println("=== 蓄水池抽样算法测试 ===");
        
        // 测试从数组中随机选择元素
        System.out.println("\n1. 从数组中随机选择元素:");
        Integer[] array = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        System.out.println("原数组: " + Arrays.toString(array));
        
        for (int k = 1; k <= 5; k++) {
            Integer[] selected = rs.selectRandomElements(array, k);
            System.out.printf("选择%d个元素: %s%n", k, Arrays.toString(selected));
        }
        
        // 测试从列表中随机选择元素
        System.out.println("\n2. 从列表中随机选择元素:");
        List<String> list = Arrays.asList("A", "B", "C", "D", "E", "F", "G", "H", "I", "J");
        System.out.println("原列表: " + list);
        
        for (int k = 1; k <= 5; k++) {
            List<String> selected = rs.selectRandomElements(list, k);
            System.out.printf("选择%d个元素: %s%n", k, selected);
        }
        
        // 测试从数据流中随机选择元素
        System.out.println("\n3. 从数据流中随机选择元素:");
        List<Integer> streamData = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15);
        
        // 多次运行以验证随机性
        System.out.println("多次运行结果（选择3个元素）:");
        for (int i = 0; i < 5; i++) {
            Iterator<Integer> iterator = streamData.iterator();
            List<Integer> selected = rs.selectRandomElements(iterator, 3);
            System.out.printf("第%d次: %s%n", i + 1, selected);
        }
        
        // 验证概率均匀性
        System.out.println("\n4. 验证概率均匀性（选择1个元素，运行10000次）:");
        Map<Integer, Integer> frequency = new HashMap<>();
        for (int i = 0; i < 10000; i++) {
            Iterator<Integer> iterator = streamData.iterator();
            Integer selected = rs.selectRandomElement(iterator);
            frequency.put(selected, frequency.getOrDefault(selected, 0) + 1);
        }
        
        System.out.println("各元素被选中的频次:");
        for (Map.Entry<Integer, Integer> entry : frequency.entrySet()) {
            System.out.printf("元素%d: %d次 (%.2f%%)%n", 
                            entry.getKey(), entry.getValue(), 
                            entry.getValue() / 100.0);
        }
    }
}