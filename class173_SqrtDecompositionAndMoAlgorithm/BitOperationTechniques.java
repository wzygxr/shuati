package class175.随机化与复杂度分析;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 位运算技巧工具类
 * 提供高效的位操作实现，包括：
 * 1. 子集枚举（使用经典的 for(s=sub; s; s=(s-1)&mask) 模式）
 * 2. Popcount（汉明重量）优化算法
 * 3. 掩码预处理技术
 * 
 * 位运算在算法优化中具有极高的效率，可以将O(n)的操作优化到O(1)或更低
 */
public class BitOperationTechniques {
    
    /**
     * 使用经典的子集枚举算法枚举指定掩码的所有非空子集
     * 时间复杂度：O(2^k)，其中k是掩码中1的个数
     * 空间复杂度：O(1)，不计算结果存储
     * 
     * @param mask 要枚举子集的掩码
     * @return 所有非空子集的列表
     */
    public static List<Integer> enumerateSubsets(int mask) {
        List<Integer> subsets = new ArrayList<>();
        int sub = mask;
        // 经典的子集枚举模式：for(s=sub; s; s=(s-1)&mask)
        do {
            subsets.add(sub);
            sub = (sub - 1) & mask;
        } while (sub != mask); // 当回到mask时，表示所有子集都已枚举完成
        return subsets;
    }
    
    /**
     * 使用位运算枚举所有可能的子集对，满足A是B的子集
     * 时间复杂度：O(3^n)，其中n是位数（对于n位数字，共有3^n种这样的子集对）
     * 
     * @param mask 最大的掩码
     * @return 所有满足A是B的子集对 (A, B) 的列表
     */
    public static List<Pair<Integer, Integer>> enumerateSubsetPairs(int mask) {
        List<Pair<Integer, Integer>> pairs = new ArrayList<>();
        // 枚举所有可能的B
        for (int b = 0; b <= mask; b = (b - mask) & mask) {
            // 枚举B的所有子集A
            int a = b;
            do {
                pairs.add(new Pair<>(a, b));
                a = (a - 1) & b;
            } while (a != b);
        }
        return pairs;
    }
    
    /**
     * 使用查表法计算32位整数的二进制中1的个数（popcount）
     * 这是一种空间换时间的优化方法，预处理所有可能的字节值
     * 时间复杂度：O(1)
     * 空间复杂度：O(1)，需要256字节的查找表
     */
    private static final int[] POPCOUNT_TABLE = new int[256];
    static {
        // 预处理查找表
        for (int i = 0; i < 256; i++) {
            POPCOUNT_TABLE[i] = Integer.bitCount(i);
        }
    }
    
    /**
     * 使用查表法计算popcount（汉明重量）
     * 
     * @param n 要计算的整数
     * @return n的二进制表示中1的个数
     */
    public static int popcountTable(int n) {
        // 将32位整数分解为4个字节，查表累加
        return POPCOUNT_TABLE[n & 0xFF] +
               POPCOUNT_TABLE[(n >>> 8) & 0xFF] +
               POPCOUNT_TABLE[(n >>> 16) & 0xFF] +
               POPCOUNT_TABLE[(n >>> 24) & 0xFF];
    }
    
    /**
     * 使用Brian Kernighan算法计算popcount
     * 时间复杂度：O(k)，其中k是1的个数
     * 空间复杂度：O(1)
     * 
     * @param n 要计算的整数
     * @return n的二进制表示中1的个数
     */
    public static int popcountBrianKernighan(int n) {
        int count = 0;
        // 每次循环清除最低位的1，直到n变为0
        while (n != 0) {
            n &= n - 1; // 清除最低位的1
            count++;
        }
        return count;
    }
    
    /**
     * 使用Java内置函数计算popcount
     * 在现代JVM中，这通常会被优化为CPU指令，效率最高
     * 
     * @param n 要计算的整数
     * @return n的二进制表示中1的个数
     */
    public static int popcountBuiltin(int n) {
        return Integer.bitCount(n);
    }
    
    /**
     * 预处理所有可能的子集掩码，按1的个数分组
     * 这在需要按子集大小处理问题时非常有用
     * 
     * @param n 位数（最大为31，因为int是32位）
     * @return 按1的个数分组的子集列表，其中第k个列表包含所有恰好有k个1的掩码
     */
    public static List<List<Integer>> precomputeSubsetsBySize(int n) {
        List<List<Integer>> subsetsBySize = new ArrayList<>(n + 1);
        for (int i = 0; i <= n; i++) {
            subsetsBySize.add(new ArrayList<>());
        }
        
        // 枚举所有可能的子集（0到2^n-1）
        int maxMask = (1 << n) - 1;
        for (int mask = 0; mask <= maxMask; mask++) {
            int count = popcountBuiltin(mask);
            subsetsBySize.get(count).add(mask);
        }
        
        return subsetsBySize;
    }
    
    /**
     * 预处理所有可能的掩码及其对应的补码
     * 
     * @param n 位数
     * @return 掩码到其补码的映射
     */
    public static Map<Integer, Integer> precomputeComplements(int n) {
        Map<Integer, Integer> complements = new HashMap<>();
        int maxMask = (1 << n) - 1;
        for (int mask = 0; mask <= maxMask; mask++) {
            complements.put(mask, mask ^ maxMask); // 异或操作计算补码
        }
        return complements;
    }
    
    /**
     * 预处理所有可能的掩码及其超集
     * 
     * @param n 位数
     * @return 每个掩码对应的所有超集
     */
    public static Map<Integer, List<Integer>> precomputeSupersets(int n) {
        Map<Integer, List<Integer>> supersets = new HashMap<>();
        int maxMask = (1 << n) - 1;
        
        // 初始化每个掩码的超集列表
        for (int mask = 0; mask <= maxMask; mask++) {
            supersets.put(mask, new ArrayList<>());
        }
        
        // 对于每个可能的超集
        for (int superset = 0; superset <= maxMask; superset++) {
            // 找出它的所有子集并更新对应子集的超集列表
            int subset = superset;
            do {
                supersets.get(subset).add(superset);
                subset = (subset - 1) & superset;
            } while (subset != superset);
        }
        
        return supersets;
    }
    
    /**
     * 检查两个掩码是否不相交（没有共同的1位）
     * 时间复杂度：O(1)
     * 
     * @param a 第一个掩码
     * @param b 第二个掩码
     * @return 如果两个掩码不相交返回true，否则返回false
     */
    public static boolean areDisjoint(int a, int b) {
        return (a & b) == 0;
    }
    
    /**
     * 计算两个掩码的对称差（异或）
     * 时间复杂度：O(1)
     * 
     * @param a 第一个掩码
     * @param b 第二个掩码
     * @return 对称差的结果
     */
    public static int symmetricDifference(int a, int b) {
        return a ^ b;
    }
    
    /**
     * 获取掩码中最低位的1
     * 时间复杂度：O(1)
     * 
     * @param mask 输入掩码
     * @return 只保留最低位1的掩码
     */
    public static int getLowestSetBit(int mask) {
        return mask & -mask; // 使用补码性质
    }
    
    /**
     * 获取掩码中最高位的1
     * 时间复杂度：O(1)
     * 
     * @param mask 输入掩码
     * @return 只保留最高位1的掩码
     */
    public static int getHighestSetBit(int mask) {
        if (mask == 0) return 0;
        // 对于32位整数，找到最高位1的位置
        int highestBitPosition = 31 - Integer.numberOfLeadingZeros(mask);
        return 1 << highestBitPosition;
    }
    
    /**
     * 计算掩码中1的最低位置（从0开始计数）
     * 时间复杂度：O(1)
     * 
     * @param mask 输入掩码
     * @return 最低位1的位置，如果没有1则返回-1
     */
    public static int getLowestSetBitPosition(int mask) {
        if (mask == 0) return -1;
        return Integer.numberOfTrailingZeros(mask);
    }
    
    /**
     * 计算掩码中1的最高位置（从0开始计数）
     * 时间复杂度：O(1)
     * 
     * @param mask 输入掩码
     * @return 最高位1的位置，如果没有1则返回-1
     */
    public static int getHighestSetBitPosition(int mask) {
        if (mask == 0) return -1;
        return 31 - Integer.numberOfLeadingZeros(mask);
    }
    
    /**
     * 计算所有可能的子集异或和
     * 时间复杂度：O(2^n)
     * 
     * @param nums 输入数组
     * @return 所有子集异或和的列表
     */
    public static List<Integer> calculateSubsetXORs(int[] nums) {
        Set<Integer> xors = new HashSet<>();
        xors.add(0); // 空集的异或和为0
        
        for (int num : nums) {
            Set<Integer> newXors = new HashSet<>(xors);
            for (int xor : xors) {
                newXors.add(xor ^ num);
            }
            xors = newXors;
        }
        
        return new ArrayList<>(xors);
    }
    
    /**
     * 使用位掩码优化的动态规划解决背包问题
     * 时间复杂度：O(2^n)
     * 空间复杂度：O(1)，使用整型掩码表示状态
     * 
     * @param weights 物品重量数组
     * @param values 物品价值数组
     * @param capacity 背包容量
     * @return 最大价值
     */
    public static int knapsackWithBitmask(int[] weights, int[] values, int capacity) {
        int n = weights.length;
        int maxValue = 0;
        
        // 枚举所有可能的子集（2^n种可能）
        for (int mask = 0; mask < (1 << n); mask++) {
            int totalWeight = 0;
            int totalValue = 0;
            
            // 计算子集的总重量和总价值
            for (int i = 0; i < n; i++) {
                if ((mask & (1 << i)) != 0) {
                    totalWeight += weights[i];
                    totalValue += values[i];
                }
            }
            
            // 如果总重量不超过容量，更新最大价值
            if (totalWeight <= capacity && totalValue > maxValue) {
                maxValue = totalValue;
            }
        }
        
        return maxValue;
    }
    
    /**
     * 测试位运算技巧的各种功能
     */
    public static void testBitOperations() {
        System.out.println("=== 位运算技巧测试 ===");
        
        // 测试子集枚举
        int mask = 0b1011; // 二进制 1011，十进制 11
        System.out.println("枚举掩码 0b1011 的所有子集：");
        List<Integer> subsets = enumerateSubsets(mask);
        for (int subset : subsets) {
            System.out.printf("0b%s (%d)\n", Integer.toBinaryString(subset), subset);
        }
        
        // 测试Popcount方法比较
        System.out.println("\nPopcount方法比较：");
        int testNumber = 0xAAAAAAAA; // 二进制中1的个数为16
        System.out.printf("数字 0xAAAAAAAA 的二进制中1的个数：\n");
        System.out.printf("查表法: %d\n", popcountTable(testNumber));
        System.out.printf("Brian Kernighan算法: %d\n", popcountBrianKernighan(testNumber));
        System.out.printf("Java内置方法: %d\n", popcountBuiltin(testNumber));
        
        // 测试预处理子集
        System.out.println("\n按1的个数分组的子集（n=4）：");
        List<List<Integer>> subsetsBySize = precomputeSubsetsBySize(4);
        for (int i = 0; i < subsetsBySize.size(); i++) {
            System.out.printf("包含 %d 个1的子集：\n", i);
            for (int subset : subsetsBySize.get(i)) {
                System.out.printf("0b%s (%d)  ", 
                    String.format("%4s", Integer.toBinaryString(subset)).replace(' ', '0'), 
                    subset);
            }
            System.out.println();
        }
        
        // 测试位操作辅助函数
        System.out.println("\n位操作辅助函数：");
        int testMask = 0b101010; // 二进制 101010，十进制 42
        System.out.printf("掩码 0b%s (%d) 的最低位1: 0b%s\n", 
            Integer.toBinaryString(testMask), testMask,
            Integer.toBinaryString(getLowestSetBit(testMask)));
        System.out.printf("掩码 0b%s (%d) 的最高位1: 0b%s\n", 
            Integer.toBinaryString(testMask), testMask,
            Integer.toBinaryString(getHighestSetBit(testMask)));
        System.out.printf("掩码 0b%s (%d) 的最低位1位置: %d\n", 
            Integer.toBinaryString(testMask), testMask,
            getLowestSetBitPosition(testMask));
        System.out.printf("掩码 0b%s (%d) 的最高位1位置: %d\n", 
            Integer.toBinaryString(testMask), testMask,
            getHighestSetBitPosition(testMask));
        
        // 测试子集异或和
        System.out.println("\n子集异或和计算：");
        int[] nums = {1, 2, 3};
        List<Integer> xors = calculateSubsetXORs(nums);
        System.out.printf("数组 %s 的所有子集异或和: %s\n", 
            Arrays.toString(nums), xors.toString());
        
        // 测试掩码优化的背包问题
        System.out.println("\n掩码优化的背包问题：");
        int[] weights = {2, 3, 4, 5};
        int[] values = {3, 4, 5, 6};
        int capacity = 8;
        int maxValue = knapsackWithBitmask(weights, values, capacity);
        System.out.printf("物品重量: %s\n物品价值: %s\n背包容量: %d\n最大价值: %d\n",
            Arrays.toString(weights), Arrays.toString(values), capacity, maxValue);
    }
    
    /**
     * 比较不同Popcount实现的性能
     */
    public static void benchmarkPopcount() {
        System.out.println("\n=== Popcount性能基准测试 ===");
        
        int testIterations = 1000000;
        int[] testNumbers = new int[testIterations];
        
        // 生成随机测试数据
        Random random = new Random(42); // 固定种子以确保公平比较
        for (int i = 0; i < testIterations; i++) {
            testNumbers[i] = random.nextInt();
        }
        
        // 测试查表法性能
        long startTime = System.nanoTime();
        int tableSum = 0;
        for (int num : testNumbers) {
            tableSum += popcountTable(num);
        }
        long tableTime = System.nanoTime() - startTime;
        
        // 测试Brian Kernighan算法性能
        startTime = System.nanoTime();
        int bkSum = 0;
        for (int num : testNumbers) {
            bkSum += popcountBrianKernighan(num);
        }
        long bkTime = System.nanoTime() - startTime;
        
        // 测试Java内置方法性能
        startTime = System.nanoTime();
        int builtinSum = 0;
        for (int num : testNumbers) {
            builtinSum += popcountBuiltin(num);
        }
        long builtinTime = System.nanoTime() - startTime;
        
        // 验证结果一致性
        boolean resultsMatch = (tableSum == bkSum) && (bkSum == builtinSum);
        
        System.out.printf("测试 %d 次Popcount操作的结果一致性: %b\n", testIterations, resultsMatch);
        System.out.printf("查表法耗时: %.3f ms\n", tableTime / 1_000_000.0);
        System.out.printf("Brian Kernighan算法耗时: %.3f ms\n", bkTime / 1_000_000.0);
        System.out.printf("Java内置方法耗时: %.3f ms\n", builtinTime / 1_000_000.0);
        System.out.printf("加速比 (Kernighan/查表法): %.2fx\n", (double)bkTime / tableTime);
        System.out.printf("加速比 (Kernighan/内置方法): %.2fx\n", (double)bkTime / builtinTime);
    }
    
    /**
     * 交互式测试函数
     */
    public static void interactiveMode() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("=== 位运算技巧工具 ===");
        System.out.println("输入操作编号:");
        System.out.println("1. 枚举子集");
        System.out.println("2. 计算Popcount");
        System.out.println("3. 位操作辅助函数");
        System.out.println("4. 子集异或和计算");
        System.out.println("5. 掩码优化背包问题");
        System.out.println("6. Popcount性能基准测试");
        System.out.println("0. 退出");
        
        while (true) {
            System.out.print("\n请输入操作编号: ");
            int choice = scanner.nextInt();
            
            try {
                switch (choice) {
                    case 0:
                        System.out.println("程序已退出");
                        scanner.close();
                        return;
                    case 1:
                        System.out.print("请输入掩码（十进制）: ");
                        int mask = scanner.nextInt();
                        List<Integer> subsets = enumerateSubsets(mask);
                        System.out.printf("掩码 %d (0b%s) 的所有子集：\n", 
                            mask, Integer.toBinaryString(mask));
                        for (int subset : subsets) {
                            System.out.printf("0b%s (%d)\n", 
                                Integer.toBinaryString(subset), subset);
                        }
                        break;
                    case 2:
                        System.out.print("请输入要计算Popcount的数字: ");
                        int num = scanner.nextInt();
                        System.out.printf("数字 %d (0b%s) 的二进制中1的个数：\n", 
                            num, Integer.toBinaryString(num));
                        System.out.printf("查表法: %d\n", popcountTable(num));
                        System.out.printf("Brian Kernighan算法: %d\n", popcountBrianKernighan(num));
                        System.out.printf("Java内置方法: %d\n", popcountBuiltin(num));
                        break;
                    case 3:
                        System.out.print("请输入掩码（十进制）: ");
                        int testMask = scanner.nextInt();
                        System.out.printf("掩码 %d (0b%s) 的位操作结果：\n", 
                            testMask, Integer.toBinaryString(testMask));
                        System.out.printf("最低位1: 0b%s (%d)\n", 
                            Integer.toBinaryString(getLowestSetBit(testMask)), 
                            getLowestSetBit(testMask));
                        System.out.printf("最高位1: 0b%s (%d)\n", 
                            Integer.toBinaryString(getHighestSetBit(testMask)), 
                            getHighestSetBit(testMask));
                        System.out.printf("最低位1位置: %d\n", getLowestSetBitPosition(testMask));
                        System.out.printf("最高位1位置: %d\n", getHighestSetBitPosition(testMask));
                        break;
                    case 4:
                        System.out.print("请输入数组元素个数: ");
                        int n = scanner.nextInt();
                        int[] array = new int[n];
                        System.out.println("请输入数组元素：");
                        for (int i = 0; i < n; i++) {
                            array[i] = scanner.nextInt();
                        }
                        List<Integer> xors = calculateSubsetXORs(array);
                        System.out.printf("数组 %s 的所有子集异或和: %s\n", 
                            Arrays.toString(array), xors.toString());
                        break;
                    case 5:
                        System.out.print("请输入物品个数: ");
                        int itemCount = scanner.nextInt();
                        int[] weights = new int[itemCount];
                        int[] values = new int[itemCount];
                        System.out.println("请输入物品的重量和价值：");
                        for (int i = 0; i < itemCount; i++) {
                            System.out.printf("物品 %d: ", i + 1);
                            weights[i] = scanner.nextInt();
                            values[i] = scanner.nextInt();
                        }
                        System.out.print("请输入背包容量: ");
                        int capacity = scanner.nextInt();
                        int maxValue = knapsackWithBitmask(weights, values, capacity);
                        System.out.printf("最大价值: %d\n", maxValue);
                        break;
                    case 6:
                        benchmarkPopcount();
                        break;
                    default:
                        System.out.println("无效的操作编号，请重新输入");
                }
            } catch (Exception e) {
                System.out.println("操作出错: " + e.getMessage());
                scanner.nextLine(); // 消耗错误输入后的换行符
            }
        }
    }
    
    /**
     * 简单的Pair类，用于存储键值对
     */
    public static class Pair<K, V> {
        private final K first;
        private final V second;
        
        public Pair(K first, V second) {
            this.first = first;
            this.second = second;
        }
        
        public K getFirst() {
            return first;
        }
        
        public V getSecond() {
            return second;
        }
        
        @Override
        public String toString() {
            return "(" + first + ", " + second + ")";
        }
    }
    
    public static void main(String[] args) {
        // 运行测试
        testBitOperations();
        
        // 启动交互模式
        interactiveMode();
    }
}