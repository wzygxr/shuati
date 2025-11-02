#include <iostream>
#include <vector>
#include <cmath>
#include <stdexcept>
#include <iomanip>

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

class HashCollisionAnalysis {
private:
    /**
     * 判断一个数是否为素数
     */
    static bool isPrime(int num) {
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
        int sqrtNum = static_cast<int>(std::sqrt(num)) + 1;
        for (int i = 5; i <= sqrtNum; i += 6) {
            if (num % i == 0 || num % (i + 2) == 0) {
                return false;
            }
        }
        
        return true;
    }

public:
    /**
     * 计算哈希冲突概率
     * 使用近似公式：1 - e^(-n²/(2m))，其中n是元素数量，m是哈希表大小
     */
    static double calculateCollisionProbability(int n, int m) {
        if (n <= 0 || m <= 0) {
            throw std::invalid_argument("元素数量和哈希表大小必须为正整数");
        }
        
        // 使用近似公式：1 - e^(-n²/(2m))
        double exponent = -std::pow(n, 2) / (2.0 * m);
        return 1 - std::exp(exponent);
    }

    /**
     * 精确计算哈希冲突概率
     * 使用公式：1 - (m * (m-1) * (m-2) * ... * (m-n+1)) / m^n
     * 适用于n较小的情况，避免大数运算溢出
     */
    static double calculateExactCollisionProbability(int n, int m) {
        if (n <= 0 || m <= 0) {
            throw std::invalid_argument("元素数量和哈希表大小必须为正整数");
        }
        
        if (n > m) {
            // 鸽巢原理：当元素数量超过哈希表大小时，必然存在冲突
            return 1.0;
        }
        
        double noCollisionProb = 1.0;
        for (int i = 0; i < n; i++) {
            noCollisionProb *= (m - i) / static_cast<double>(m);
        }
        
        return 1 - noCollisionProb;
    }

    /**
     * 查找大于等于target的下一个素数
     */
    static int findNextPrime(int target) {
        if (target <= 2) {
            return 2;
        }
        
        int candidate = (target % 2 == 0) ? (target + 1) : target;
        while (true) {
            if (isPrime(candidate)) {
                return candidate;
            }
            candidate += 2; // 只检查奇数
        }
    }

    /**
     * 根据预期元素数量和最大负载因子选择合适的哈希表大小（素数）
     */
    static int selectOptimalPrimeSize(int expectedSize, double maxLoadFactor) {
        if (expectedSize <= 0 || maxLoadFactor <= 0 || maxLoadFactor > 1) {
            throw std::invalid_argument("参数无效");
        }
        
        // 计算所需的最小大小
        int minSize = static_cast<int>(std::ceil(expectedSize / maxLoadFactor));
        // 选择大于等于minSize的素数
        return findNextPrime(minSize);
    }

    /**
     * 获取常用的大素数表（用于哈希表大小）
     */
    static std::vector<int> getCommonHashPrimes() {
        return {
            131,
            257,
            521,
            1031,
            2053,
            4099,
            8209,
            16411,
            32771,
            65537,
            131101,
            262147,
            524309,
            1048583,
            2097169,
            4194319,
            8388617,
            16777259,
            33554467,
            67108879,
            134217757,
            268435459,
            536870923,
            1073741827
        };
    }
};

// 测试函数
int main() {
    try {
        // 测试哈希冲突概率计算
        int n = 23; // 元素数量
        int m = 365; // 哈希表大小（例如一年的天数）
        
        double approxProb = HashCollisionAnalysis::calculateCollisionProbability(n, m);
        double exactProb = HashCollisionAnalysis::calculateExactCollisionProbability(n, m);
        
        std::cout << "生日悖论示例：" << std::endl;
        std::cout << "当有" << n << "个人时，至少有两个人生日相同的概率：" << std::endl;
        std::cout << std::fixed << std::setprecision(6);
        std::cout << "近似概率: " << approxProb << std::endl;
        std::cout << "精确概率: " << exactProb << std::endl;
        
        // 测试素数选择
        int expectedSize = 1000;
        double loadFactor = 0.75;
        int optimalSize = HashCollisionAnalysis::selectOptimalPrimeSize(expectedSize, loadFactor);
        
        std::cout << "\n哈希表大小选择示例：" << std::endl;
        std::cout << "预期元素数量: " << expectedSize << std::endl;
        std::cout << "最大负载因子: " << loadFactor << std::endl;
        std::cout << "推荐的哈希表大小（素数）: " << optimalSize << std::endl;
        
        // 测试常用素数表
        std::cout << "\n常用哈希素数表：" << std::endl;
        std::vector<int> primes = HashCollisionAnalysis::getCommonHashPrimes();
        for (size_t i = 0; i < primes.size(); ++i) {
            std::cout << primes[i];
            if (i < primes.size() - 1) {
                std::cout << ", ";
            }
        }
        std::cout << std::endl;
        
    } catch (const std::exception& e) {
        std::cerr << "错误：" << e.what() << std::endl;
    }
    
    return 0;
}