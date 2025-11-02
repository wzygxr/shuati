// POJ 2352 Stars
// 题目描述：给定N个星星的坐标(x,y)，满足y坐标升序排列，若y相同则x升序排列。
// 每个星星的等级是它左下角区域内星星的数量（即x坐标≤其x，y坐标≤其y的星星数目，不包括自身）。
// 输出等级为0到N-1的星星数目。
// 题目链接：http://poj.org/problem?id=2352
// 解题思路：树状数组 + 离散化

import java.util.*;

/**
 * 使用树状数组解决Stars问题
 * 
 * 时间复杂度：O(N log N)
 * 空间复杂度：O(max_x)
 * 
 * 本题特点：
 * 1. 由于输入是按y升序排列的，所以对于每个星星来说，之前处理过的星星的y坐标都不超过它的y坐标
 * 2. 因此我们只需要统计之前处理过的星星中x坐标小于等于当前星星x坐标的数量
 * 3. 这可以通过树状数组高效实现，每次查询前缀和，然后更新树状数组
 */
public class Code16_Stars {
    
    private int maxX; // 最大x坐标值
    private int[] bit; // 树状数组
    private int[] result; // 存储每个等级的星星数目
    
    public Code16_Stars(int maxXValue) {
        // 初始化树状数组和结果数组
        // 注意：这里maxX+1是因为树状数组下标从1开始
        this.maxX = maxXValue;
        this.bit = new int[maxX + 2]; // +2 防止溢出
        this.result = new int[maxX + 1]; // 等级最多为maxX
    }
    
    /**
     * lowbit操作，获取x二进制表示中最低位的1所对应的值
     * @param x 输入整数
     * @return 最低位的1对应的值
     */
    private int lowbit(int x) {
        return x & (-x);
    }
    
    /**
     * 更新树状数组
     * @param x 要更新的位置（1-based）
     * @param val 要增加的值
     */
    private void update(int x, int val) {
        while (x <= maxX) {
            bit[x] += val;
            x += lowbit(x);
        }
    }
    
    /**
     * 查询前缀和，即1到x的累加和
     * @param x 查询上限（1-based）
     * @return 前缀和
     */
    private int query(int x) {
        int sum = 0;
        while (x > 0) {
            sum += bit[x];
            x -= lowbit(x);
        }
        return sum;
    }
    
    /**
     * 处理星星数据，计算每个星星的等级
     * @param stars 星星坐标数组
     * @return 等级统计结果，result[i]表示等级为i的星星数目
     */
    public int[] processStars(int[][] stars) {
        // 统计每个星星的等级
        for (int[] star : stars) {
            int x = star[0];
            // 由于树状数组索引从1开始，我们将x坐标+1
            // 计算当前星星的等级：查询小于等于x的星星数量
            int level = query(x + 1); // 转换为1-based索引
            
            // 更新等级统计
            result[level]++;
            
            // 将当前星星加入树状数组
            update(x + 1, 1); // 转换为1-based索引
        }
        
        return result;
    }
    
    /**
     * 处理星星数据（带离散化）
     * 当x坐标范围很大时使用离散化可以节省空间
     * @param stars 星星坐标数组
     * @return 等级统计结果
     */
    public int[] processStarsWithDiscretization(int[][] stars) {
        // 提取所有x坐标用于离散化
        int[] xs = new int[stars.length];
        for (int i = 0; i < stars.length; i++) {
            xs[i] = stars[i][0];
        }
        
        // 离散化处理
        Map<Integer, Integer> coordinateMapping = discretize(xs);
        
        // 重置树状数组为离散化后的大小
        this.maxX = coordinateMapping.size();
        this.bit = new int[this.maxX + 2]; // +2 防止溢出
        this.result = new int[stars.length]; // 重置结果数组
        
        // 处理星星数据
        for (int[] star : stars) {
            int x = star[0];
            // 获取离散化后的值（从1开始）
            int discretizedX = coordinateMapping.get(x) + 1;
            
            // 计算当前星星的等级
            int level = query(discretizedX);
            
            // 更新等级统计
            result[level]++;
            
            // 将当前星星加入树状数组
            update(discretizedX, 1);
        }
        
        return result;
    }
    
    /**
     * 离散化处理
     * @param nums 原始数据数组
     * @return 原始值到离散化值的映射
     */
    private Map<Integer, Integer> discretize(int[] nums) {
        // 复制并去重
        Set<Integer> uniqueNums = new HashSet<>();
        for (int num : nums) {
            uniqueNums.add(num);
        }
        
        // 排序
        List<Integer> sortedNums = new ArrayList<>(uniqueNums);
        Collections.sort(sortedNums);
        
        // 构建映射
        Map<Integer, Integer> mapping = new HashMap<>();
        for (int i = 0; i < sortedNums.size(); i++) {
            mapping.put(sortedNums.get(i), i); // 从0开始的离散化值
        }
        
        return mapping;
    }
    
    /**
     * 打印结果
     * @param result 等级统计结果
     */
    public static void printResult(int[] result, int n) {
        for (int i = 0; i < n; i++) {
            System.out.println(result[i]);
        }
    }
    
    /**
     * 测试函数
     */
    public static void test() {
        // 测试用例1：简单示例
        int[][] stars1 = {
            {1, 1},
            {2, 2},
            {3, 3},
            {1, 3},
            {2, 1}
        };
        
        System.out.println("=== 测试用例1（无需离散化）===");
        // 找出最大的x坐标
        int maxX1 = 0;
        for (int[] star : stars1) {
            maxX1 = Math.max(maxX1, star[0]);
        }
        
        Code16_Stars solver1 = new Code16_Stars(maxX1);
        int[] result1 = solver1.processStars(stars1);
        printResult(result1, stars1.length);
        
        // 测试用例2：使用离散化
        System.out.println("\n=== 测试用例2（使用离散化）===");
        int[][] stars2 = {
            {10000, 1},
            {20000, 2},
            {5000, 3},
            {10000, 3},
            {20000, 1}
        };
        
        Code16_Stars solver2 = new Code16_Stars(20000); // 初始值不重要，会在离散化时重置
        int[] result2 = solver2.processStarsWithDiscretization(stars2);
        printResult(result2, stars2.length);
        
        // 测试用例3：边界情况 - 所有星星在同一点
        System.out.println("\n=== 测试用例3（所有星星在同一点）===");
        int[][] stars3 = {
            {1, 1},
            {1, 1},
            {1, 1}
        };
        
        int maxX3 = 1;
        Code16_Stars solver3 = new Code16_Stars(maxX3);
        int[] result3 = solver3.processStars(stars3);
        printResult(result3, stars3.length);
    }
    
    public static void main(String[] args) {
        test();
        
        // 实际输入处理
        Scanner scanner = new Scanner(System.in);
        System.out.println("\n=== 输入测试（输入N和N个坐标）===");
        System.out.print("请输入星星数量N: ");
        int n = scanner.nextInt();
        
        int[][] stars = new int[n][2];
        int maxX = 0;
        for (int i = 0; i < n; i++) {
            stars[i][0] = scanner.nextInt();
            stars[i][1] = scanner.nextInt();
            maxX = Math.max(maxX, stars[i][0]);
        }
        
        // 处理输入数据
        Code16_Stars solver = new Code16_Stars(maxX);
        int[] result = solver.processStars(stars);
        
        // 输出结果
        System.out.println("\n输出结果：");
        printResult(result, n);
        
        scanner.close();
    }
}

/**
 * 算法总结：
 * 
 * 1. 本题的关键洞察：
 *    - 由于输入的星星是按y坐标升序排列的，所以处理每个星星时，所有已处理的星星的y坐标都不大于当前星星的y坐标
 *    - 因此，当前星星的等级就是已处理星星中x坐标小于等于当前星星x坐标的数量
 *    - 这可以通过树状数组高效地进行前缀和查询和单点更新
 * 
 * 2. 离散化的必要性：
 *    - 当x坐标范围很大时（比如到1e9），直接使用树状数组会导致空间浪费
 *    - 离散化可以将所有不同的x坐标映射到较小的连续整数范围，节省空间
 *    - 在本题中，如果x坐标范围不大，可以不使用离散化
 * 
 * 3. 树状数组操作：
 *    - update(x, val): 在位置x增加val
 *    - query(x): 查询前缀和[1,x]
 *    - lowbit(x): 获取x二进制表示中最低位的1
 * 
 * 4. 时间复杂度分析：
 *    - 树状数组的update和query操作都是O(log M)，其中M是最大x坐标值（或离散化后的坐标范围）
 *    - 处理n个星星的总时间复杂度为O(n log M)
 *    - 离散化的时间复杂度为O(n log n)
 *    - 因此总时间复杂度为O(n log n)
 * 
 * 5. 空间复杂度：
 *    - 不使用离散化：O(M)，其中M是最大x坐标值
 *    - 使用离散化：O(n)，只需存储不同的x坐标
 */