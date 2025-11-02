package class189;

import java.util.*;

/**
 * 查找质数的算法实现
 * 
 * 核心思想：
 * 1. 通过交互式查询来确定一个数是否为质数
 * 2. 使用二分查找和数论方法优化查询策略
 * 3. 结合质数分布规律减少查询次数
 * 
 * 应用场景：
 * 1. 密码学中的质数生成
 * 2. 数论问题求解
 * 3. 随机质数生成
 * 
 * 工程化考量：
 * 1. 查询次数优化
 * 2. 异常处理
 * 3. 边界条件处理
 * 4. 时间复杂度优化
 */
public class Code05_FindPrime {
    
    /**
     * 模拟交互式质数查询接口
     */
    static class InteractivePrimeChecker {
        private Set<Integer> primes;
        private int queryCount;
        
        InteractivePrimeChecker() {
            this.primes = new HashSet<>();
            this.queryCount = 0;
            
            // 预先计算一些质数用于测试
            generatePrimes(1000);
        }
        
        /**
         * 生成质数表（埃拉托斯特尼筛法）
         */
        private void generatePrimes(int limit) {
            boolean[] isPrime = new boolean[limit + 1];
            Arrays.fill(isPrime, true);
            isPrime[0] = isPrime[1] = false;
            
            for (int i = 2; i * i <= limit; i++) {
                if (isPrime[i]) {
                    for (int j = i * i; j <= limit; j += i) {
                        isPrime[j] = false;
                    }
                }
            }
            
            for (int i = 2; i <= limit; i++) {
                if (isPrime[i]) {
                    primes.add(i);
                }
            }
        }
        
        /**
         * 查询一个数是否为质数
         */
        public boolean isPrime(int n) {
            queryCount++;
            return primes.contains(n);
        }
        
        /**
         * 获取查询次数
         */
        public int getQueryCount() {
            return queryCount;
        }
        
        /**
         * 重置查询次数
         */
        public void resetQueryCount() {
            queryCount = 0;
        }
    }
    
    /**
     * 基础质数查找算法
     * 在给定范围内查找所有质数
     */
    public static List<Integer> findPrimesBasic(int start, int end, InteractivePrimeChecker checker) {
        List<Integer> primes = new ArrayList<>();
        
        for (int i = start; i <= end; i++) {
            if (checker.isPrime(i)) {
                primes.add(i);
            }
        }
        
        return primes;
    }
    
    /**
     * 二分查找质数
     * 在有序质数列表中查找特定质数
     */
    public static boolean binarySearchPrime(List<Integer> primes, int target) {
        int left = 0;
        int right = primes.size() - 1;
        
        while (left <= right) {
            int mid = left + (right - left) / 2;
            int midValue = primes.get(mid);
            
            if (midValue == target) {
                return true;
            } else if (midValue < target) {
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }
        
        return false;
    }
    
    /**
     * 自适应质数查找
     * 根据质数分布规律优化查找策略
     */
    public static List<Integer> findPrimesAdaptive(int start, int end, InteractivePrimeChecker checker) {
        List<Integer> primes = new ArrayList<>();
        
        // 使用6k±1优化：除了2和3，所有质数都可以表示为6k±1的形式
        if (start <= 2 && end >= 2) {
            if (checker.isPrime(2)) {
                primes.add(2);
            }
        }
        
        if (start <= 3 && end >= 3) {
            if (checker.isPrime(3)) {
                primes.add(3);
            }
        }
        
        // 从5开始，检查6k±1形式的数
        int k = 1;
        int candidate1, candidate2;
        
        while (true) {
            candidate1 = 6 * k - 1;
            candidate2 = 6 * k + 1;
            
            if (candidate1 > end) break;
            
            if (candidate1 >= start && checker.isPrime(candidate1)) {
                primes.add(candidate1);
            }
            
            if (candidate2 <= end && checker.isPrime(candidate2)) {
                primes.add(candidate2);
            }
            
            k++;
        }
        
        // 排序结果
        Collections.sort(primes);
        return primes;
    }
    
    /**
     * 信息论优化的质数查找
     * 根据质数定理优化查询策略
     */
    public static List<Integer> findPrimesInformationTheoretic(int start, int end, InteractivePrimeChecker checker) {
        List<Integer> primes = new ArrayList<>();
        
        // 质数定理：小于n的质数大约有n/ln(n)个
        // 我们可以根据这个信息调整查询策略
        
        // 对于较小的范围，直接检查
        if (end - start < 100) {
            return findPrimesAdaptive(start, end, checker);
        }
        
        // 对于较大的范围，使用分段策略
        int segmentSize = Math.max(50, (end - start) / 10);
        int currentStart = start;
        
        while (currentStart <= end) {
            int currentEnd = Math.min(currentStart + segmentSize - 1, end);
            List<Integer> segmentPrimes = findPrimesAdaptive(currentStart, currentEnd, checker);
            primes.addAll(segmentPrimes);
            currentStart = currentEnd + 1;
        }
        
        return primes;
    }
    
    /**
     * 查找第n个质数
     */
    public static int findNthPrime(int n, InteractivePrimeChecker checker) {
        if (n <= 0) return -1;
        
        int count = 0;
        int candidate = 1;
        
        while (count < n) {
            candidate++;
            if (checker.isPrime(candidate)) {
                count++;
            }
        }
        
        return candidate;
    }
    
    /**
     * 查找范围内的最大质数
     */
    public static int findLargestPrime(int start, int end, InteractivePrimeChecker checker) {
        for (int i = end; i >= start; i--) {
            if (checker.isPrime(i)) {
                return i;
            }
        }
        return -1;
    }
    
    // 测试方法
    public static void main(String[] args) {
        InteractivePrimeChecker checker = new InteractivePrimeChecker();
        
        int start = 1;
        int end = 100;
        
        System.out.println("查找范围 [" + start + ", " + end + "] 内的质数");
        System.out.println();
        
        // 测试基础方法
        checker.resetQueryCount();
        List<Integer> primes1 = findPrimesBasic(start, end, checker);
        System.out.println("基础方法找到的质数个数：" + primes1.size());
        System.out.println("查询次数：" + checker.getQueryCount());
        System.out.println("前10个质数：" + primes1.subList(0, Math.min(10, primes1.size())));
        System.out.println();
        
        // 测试自适应方法
        checker.resetQueryCount();
        List<Integer> primes2 = findPrimesAdaptive(start, end, checker);
        System.out.println("自适应方法找到的质数个数：" + primes2.size());
        System.out.println("查询次数：" + checker.getQueryCount());
        System.out.println("前10个质数：" + primes2.subList(0, Math.min(10, primes2.size())));
        System.out.println();
        
        // 测试信息论优化方法
        checker.resetQueryCount();
        List<Integer> primes3 = findPrimesInformationTheoretic(start, end, checker);
        System.out.println("信息论优化方法找到的质数个数：" + primes3.size());
        System.out.println("查询次数：" + checker.getQueryCount());
        System.out.println("前10个质数：" + primes3.subList(0, Math.min(10, primes3.size())));
        System.out.println();
        
        // 测试查找第n个质数
        checker.resetQueryCount();
        int nthPrime = findNthPrime(25, checker);
        System.out.println("第25个质数：" + nthPrime);
        System.out.println("查询次数：" + checker.getQueryCount());
        System.out.println();
        
        // 测试查找范围内最大质数
        checker.resetQueryCount();
        int largestPrime = findLargestPrime(50, 100, checker);
        System.out.println("范围[50, 100]内最大质数：" + largestPrime);
        System.out.println("查询次数：" + checker.getQueryCount());
    }
}