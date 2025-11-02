package class175.随机化与复杂度分析;

import java.util.ArrayList;
import java.util.List;

/**
 * 哈希冲突概率估算与素数模选择
 * 算法思想：
 * 1. 哈希冲突概率估算：使用生日悖论公式计算给定元素数量和哈希表大小时的冲突概率
 * 2. 素数模选择：选择合适的素数作为哈希表大小，减少冲突
 * 
 * 相关题目：
 * 1. LeetCode 705. 设计哈希集合 - https://leetcode-cn.com/problems/design-hashset/
 * 2. LeetCode 706. 设计哈希映射 - https://leetcode-cn.com/problems/design-hashmap/
 * 3. LintCode 128. 哈希函数 - https://www.lintcode.com/problem/128/
 * 4. CodeChef - HASHTABLE - https://www.codechef.com/problems/HASHTABLE
 */
public class HashCollisionAnalysis {

    /**
     * 计算哈希冲突概率
     * 使用近似公式：1 - e^(-n²/(2m))，其中n是元素数量，m是哈希表大小
     * 
     * @param n 元素数量
     * @param m 哈希表大小
     * @return 至少发生一次冲突的概率
     */
    public static double calculateCollisionProbability(int n, int m) {
        if (n <= 0 || m <= 0) {
            throw new IllegalArgumentException("元素数量和哈希表大小必须为正整数");
        }
        
        // 使用近似公式：1 - e^(-n²/(2m))
        double exponent = -Math.pow(n, 2) / (2.0 * m);
        return 1 - Math.exp(exponent);
    }

    /**
     * 精确计算哈希冲突概率
     * 使用公式：1 - (m * (m-1) * (m-2) * ... * (m-n+1)) / m^n
     * 适用于n较小的情况，避免大数运算溢出
     * 
     * @param n 元素数量
     * @param m 哈希表大小
     * @return 至少发生一次冲突的概率
     */
    public static double calculateExactCollisionProbability(int n, int m) {
        if (n <= 0 || m <= 0) {
            throw new IllegalArgumentException("元素数量和哈希表大小必须为正整数");
        }
        
        if (n > m) {
            // 鸽巢原理：当元素数量超过哈希表大小时，必然存在冲突
            return 1.0;
        }
        
        double noCollisionProb = 1.0;
        for (int i = 0; i < n; i++) {
            noCollisionProb *= (m - i) / (double) m;
        }
        
        return 1 - noCollisionProb;
    }

    /**
     * 查找大于等于target的下一个素数
     * 
     * @param target 目标值
     * @return 大于等于target的最小素数
     */
    public static int findNextPrime(int target) {
        if (target <= 2) {
            return 2;
        }
        
        int candidate = target % 2 == 0 ? target + 1 : target;
        while (true) {
            if (isPrime(candidate)) {
                return candidate;
            }
            candidate += 2; // 只检查奇数
        }
    }

    /**
     * 判断一个数是否为素数
     * 
     * @param num 要判断的数
     * @return 是否为素数
     */
    private static boolean isPrime(int num) {
        if (num <= 1) {
            return false;
        }
        if (num <= 3) {
            return true;
        }
        if (num % 2 == 0 || num % 3 == 0) {
            return false;
        }
        
        // 检查直到sqrt(num)，跳过偶数和3的倍数
        int sqrt = (int) Math.sqrt(num) + 1;
        for (int i = 5; i <= sqrt; i += 6) {
            if (num % i == 0 || num % (i + 2) == 0) {
                return false;
            }
        }
        
        return true;
    }

    /**
     * 根据预期元素数量和最大负载因子选择合适的哈希表大小（素数）
     * 
     * @param expectedSize 预期元素数量
     * @param maxLoadFactor 最大负载因子
     * @return 推荐的哈希表大小（素数）
     */
    public static int selectOptimalPrimeSize(int expectedSize, double maxLoadFactor) {
        if (expectedSize <= 0 || maxLoadFactor <= 0 || maxLoadFactor > 1) {
            throw new IllegalArgumentException("参数无效");
        }
        
        // 计算所需的最小大小
        int minSize = (int) Math.ceil(expectedSize / maxLoadFactor);
        // 选择大于等于minSize的素数
        return findNextPrime(minSize);
    }

    /**
     * 获取常用的大素数表（用于哈希表大小）
     * 这些素数都是2^k附近的素数，适合作为哈希表的容量
     * 
     * @return 素数列表
     */
    public static List<Integer> getCommonHashPrimes() {
        List<Integer> primes = new ArrayList<>();
        primes.add(131);
        primes.add(257);
        primes.add(521);
        primes.add(1031);
        primes.add(2053);
        primes.add(4099);
        primes.add(8209);
        primes.add(16411);
        primes.add(32771);
        primes.add(65537);
        primes.add(131101);
        primes.add(262147);
        primes.add(524309);
        primes.add(1048583);
        primes.add(2097169);
        primes.add(4194319);
        primes.add(8388617);
        primes.add(16777259);
        primes.add(33554467);
        primes.add(67108879);
        primes.add(134217757);
        primes.add(268435459);
        primes.add(536870923);
        primes.add(1073741827);
        return primes;
    }

    /**
     * 测试方法
     */
    public static void main(String[] args) {
        // 测试哈希冲突概率计算
        int n = 23; // 元素数量
        int m = 365; // 哈希表大小（例如一年的天数）
        
        double approxProb = calculateCollisionProbability(n, m);
        double exactProb = calculateExactCollisionProbability(n, m);
        
        System.out.println("生日悖论示例：");
        System.out.println("当有" + n + "个人时，至少有两个人生日相同的概率：");
        System.out.println("近似概率: " + String.format("%.6f", approxProb));
        System.out.println("精确概率: " + String.format("%.6f", exactProb));
        
        // 测试素数选择
        int expectedSize = 1000;
        double loadFactor = 0.75;
        int optimalSize = selectOptimalPrimeSize(expectedSize, loadFactor);
        
        System.out.println("\n哈希表大小选择示例：");
        System.out.println("预期元素数量: " + expectedSize);
        System.out.println("最大负载因子: " + loadFactor);
        System.out.println("推荐的哈希表大小（素数）: " + optimalSize);
        
        // 测试常用素数表
        System.out.println("\n常用哈希素数表：");
        List<Integer> primes = getCommonHashPrimes();
        for (int i = 0; i < primes.size(); i++) {
            System.out.print(primes.get(i));
            if (i < primes.size() - 1) {
                System.out.print(", ");
            }
        }
        System.out.println();
    }
}